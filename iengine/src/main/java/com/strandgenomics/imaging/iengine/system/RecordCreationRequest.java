/*
 * RecordCreationRequest.java
 *
 * AVADIS Image Management System
 * Core Engine Components
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iengine.system;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.util.Archiver;

/**
 * Defines the context of the client when he upload a bunch of source files (as a tar-ball)
 * to upload a multi-series record (experiment)
 * @author arunabha
 *
 */
public class RecordCreationRequest implements Storable {
	
	private static final long serialVersionUID = 4090342971404790032L;
	/**
	 * Signature - md5 hash of the source files as computed by the client
	 */
	protected BigInteger archiveHash = null;
	/**
	 * List of client side source files that is actually transferred (as the tar ball)
	 */
	protected List<ISourceReference> clientFiles = null;
	/**
	 * IP of the client machine from which the archive is uploaded
	 */
	protected String clientIPAddress = null;
	/**
	 * MAC address of the client machine
	 */
	protected String clientMacAddress = null;
	/**
	 * The tar-ball uploaded by the client and stored locally by the server
	 */
	protected File tarBall = null;
	/**
	 * the access token used for making this request
	 */
	protected Application app = null;
	/**
	 * the application used for uploading the tar-ball to create records
	 */
	protected String accessToken = null;
	/**
	 * the user uploading the tar-ball to create records
	 */
	protected String actorLogin = null;
	/**
	 * the project under which the extracted records are to be added
	 */
	protected String projectName = null;
	/**
	 * the folder where the tar-ball will be extracted, should be empty
	 */
	protected File extractionFolder = null;
	/**
	 * name of the source folder (as is in the client machine)
	 */
	protected String clientFolderName = null;
	/**
	 * name of the source file (as is in the client machine)
	 */
	protected String clientFilename = null;
	/**
	 * the time when the request is submitted
	 */
	protected final long uploadTime;
	/**
	 * list of custom specifications (just the sites, may be null implying default strategy) to extract
	 */
	protected List<RecordSpec> clientSpec = null;
	/**
	 * seed file for record extraction
	 */
	private File seedFile;
	/**
	 * source files for record extraction
	 */
	private File[] sourceFiles;
	/**
	 * 
	 */
	private boolean isDirectUpload = false;
	
	/**
	 * Creates a client context
	 * @param app the application used for uploading the tar-ball to create records
	 * @param actorLogin the user uploading the tar-ball to create records
	 * @param projectName the project under which the extracted records are to be added
	 * @param clientIPAddress IP of the client machine from which the archive is uploaded
	 * @param clientMacAddress MAC address of the client machine
	 * @param archiveSignature md5 hash of the source files as computed by the client
	 * @param clientFiles  List of client side source files that is actually transferred (as the tar ball)
	 * @param selectedSignatures list of record signatures (just the sites, since rest are identical) to extract
	 */
	public RecordCreationRequest(Application app, String actorLogin, String accessToken, String projectName, String clientIPAddress, String clientMacAddress, 
			String clientRootDirectory, String clientFilename, BigInteger archiveSignature, 
			List<ISourceReference> clientFiles, List<RecordSpec> recordSpec, boolean isDirectUpload)
	{
		this.app = app;
		this.actorLogin       = actorLogin;
		this.accessToken      = accessToken;
		this.projectName      = projectName;
		
		this.clientIPAddress  = clientIPAddress;
		this.clientMacAddress = clientMacAddress;
		
		this.clientFolderName = clientRootDirectory;
		this.clientFilename = clientFilename;
		
		this.archiveHash      = archiveSignature;
		this.clientFiles      = clientFiles;
		this.clientSpec       = recordSpec;
		
		this.isDirectUpload = isDirectUpload;
		
		this.uploadTime = System.currentTimeMillis();
	}
	
	public boolean isDirectUploadRequest()
	{
		return this.isDirectUpload;
	}
	
	public List<RecordSpec> getRecordSpecification()
	{
		return clientSpec;
	}
	
	public long getSourceTime()
	{
		return  getClientFiles().get(0).getLastModified();
	}
	
	/**
	 * returns the application used for uploading the tar-ball of the records
	 * @return the application used for uploading the tar-ball of the records
	 */
	public Application getApplication()
	{
		return this.app;
	}
	
	/**
	 * Returns the name of the seed file as in the client machine 
	 * @return  name of the seed file as in the client machine 
	 */
	public String getSourceFilename()
	{
		if(clientFilename == null)
		{
			String sourceFile = getClientFiles().get(0).getSourceFile();
			if(sourceFile.indexOf('/') != -1) //UNIX CLIENT MACHINE
			{
				clientFilename = sourceFile.substring(sourceFile.lastIndexOf('/')+1);
			}
			else //windows client machine
			{
				clientFilename = sourceFile.substring(sourceFile.lastIndexOf('\\')+1);
			}
		}
		return clientFilename;
	}

	/**
	 * Returns the name of the source folder containing the source files as in the client machine 
	 * @return the name of the source folder containing the source files as in the client machine 
	 */
	public String getSourceDirectory() 
	{
		if(clientFolderName == null)
		{
			String sourceFile = getClientFiles().get(0).getSourceFile();
			if(sourceFile.indexOf('/') != -1) //UNIX CLIENT MACHINE
			{
				clientFolderName = sourceFile.substring(0, sourceFile.lastIndexOf('/'));
			}
			else //windows client machine
			{
				clientFolderName = sourceFile.substring(0, sourceFile.lastIndexOf('\\'));
			}
		}
		return clientFolderName;
	}
	
	/**
	 * Returns true if this request of valid
	 * @return true if this request of valid, false otherwise
	 */
	public boolean isValid() 
	{
		return (actorLogin != null && projectName != null && archiveHash != null 
				&& clientFiles != null && clientFiles.size() > 0 && tarBall != null && tarBall.isFile());
	}
	
	/**
	 * Sets the tar-ball uploaded by the client and stored locally by the server
	 */
	public void setTarBall(File tarBall)
	{
		this.tarBall = tarBall;
	}
	
	public void setMD5Signature(BigInteger hash){
		this.archiveHash=hash;
	}
	
	public File getTarBall()
	{
		return tarBall;
	}
	
	/**
	 * Extracts the tar ball at the specified location
	 * Note that the extractionFolder must exist and be empty 
	 */
	public void extractTarBall(File extractionFolder) throws IOException
	{
		this.extractionFolder = extractionFolder;
		Archiver.unTar(extractionFolder, tarBall);
	}
	
	public void setExtractionFolder(File rootFolder)
	{ 
		this.extractionFolder = rootFolder;
	}
	
	public File getExtractionFolder()
	{
		return this.extractionFolder;
	}
	
	/**
	 * The source files - after extracting the tar ball
	 * @return the source files for record extraction
	 */
	public File[] getSourceFiles()
	{
		if(sourceFiles == null)
			this.sourceFiles = extractionFolder.listFiles();
		
		return this.sourceFiles;
	}
	
	public void setSourceFiles(File[] sourceFiles)
	{
		this.sourceFiles = sourceFiles;
	}
	
	public File getSeedFile()
	{
		if(seedFile == null)
		{
			seedFile = extractionFolder.listFiles()[0];
			File[] files = extractionFolder.listFiles();
			for(File file:files)
			{
				String filename = file.getName().toLowerCase();
				if(!(filename.endsWith(".tif") || filename.endsWith(".tiff")))
				{
					seedFile = file;
					break;
				}
			}
		}
		return seedFile;
	}
	
	public void setSeedFile(File seedFile)
	{
		this.seedFile = seedFile;
	}
	
	/**
	 * Returns the project under which the extracted records are to be added
	 * @return the project under which the extracted records are to be added
	 */
	public String getProject()
	{
		return projectName;
	}
	
	/**
	 * Returns the user (login id) uploading the tar-ball to create records
	 * @return the user (login id) uploading the tar-ball to create records
	 */
	public String getActor()
	{
		return actorLogin;
	}
	
	/**
	 * returns the access token granted for this request
	 * @return the access token granted for this request
	 */
	public String getAccessToken()
	{
		return this.accessToken;
	}

	/**
	 * Returns md5 hash of the source files as computed by the client
	 * @return md5 hash of the source files as computed by the client
	 */
	public BigInteger getArchiveHash() 
	{
		return archiveHash;
	}

	/**
	 * Returns the list of client side source files that is actually transferred (as the tar ball)
	 * @return list of client side source files that is actually transferred (as the tar ball)
	 */
	public List<ISourceReference> getClientFiles()
	{
		return clientFiles;
	}
	
	public void prioritiesFiles()
	{
		List<ISourceReference> tempFiles = new ArrayList<ISourceReference>();
		
		for(ISourceReference clientSourceReference:clientFiles)
		{
			System.out.println("checking candidature of file " + clientSourceReference.getSourceFile());
			
			String sourceFile = clientSourceReference.getSourceFile().toLowerCase();
			System.out.println(" adding file as valid source file "+sourceFile);
			if(sourceFile.endsWith(".tiff") || sourceFile.endsWith(".tif"))
			{
				tempFiles.add(clientSourceReference);
			}
			else
			{
				tempFiles.add(0, clientSourceReference);
			}
		}
		clientFiles = tempFiles;
	}
	
	public void validateFiles()
	{
		List<ISourceReference> tempFiles = new ArrayList<ISourceReference>();
		
		for(ISourceReference clientFile:clientFiles)
		{
			String sourceFile = clientFile.getSourceFile().toLowerCase();
			if(sourceFile.endsWith(".tiff") || sourceFile.endsWith(".tif"))
			{
				tempFiles.add(clientFile);
			}
			else
			{
				tempFiles.add(0, clientFile);
			}
		}
		clientFiles = tempFiles;
	}

	/**
	 * Returns the IP of the client machine from which the archive is uploaded
	 * @return IP of the client machine from which the archive is uploaded
	 */
	public String getClientIPAddress() 
	{
		return clientIPAddress;
	}

	/**
	 * Returns the MAC address of the client machine
	 * @return MAC address of the client machine
	 */
	public String getClientMacAddress() 
	{
		return clientMacAddress;
	}
	
	@Override
	public int hashCode()
	{
		return archiveHash.hashCode();
	}
	
	@Override
	public String toString()
	{
		return projectName +":"+actorLogin+":"+archiveHash+":"+tarBall;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof RecordCreationRequest)
		{
			RecordCreationRequest that = (RecordCreationRequest) obj;
			if(this == that) return true;
			return this.archiveHash.equals(that.archiveHash);
		}
		return false;
	}
	
	public void dispose()
	{
		if(tarBall != null) tarBall.delete();
	}
	
	public static class RecordSpec implements Serializable
	{
		private static final long serialVersionUID = -7845523310987215372L;
		
		/**
		 * List of sites to merge into a single record
		 */
		protected List<Site> sitesToMerge = null;
		/**
		 * custom channel specifications for the record, color and contrast
		 */
		protected List<Channel> customChannels = null;
		
		public RecordSpec(List<Site> sites, List<Channel> channels)
		{
			this.sitesToMerge = sites;
			this.customChannels = channels;
		}
		
		/**
		 * Returns the number of sites to merge
		 * @return  the number of sites to merge
		 */
		public int getSiteCount()
		{
			return sitesToMerge.size();
		}
		
		/**
		 * List of sites to merge into a single record
		 */
		public List<Site> getSites()
		{
			return sitesToMerge;
		}
		
		/**
		 * custom channel specifications for the record
		 */
		public List<Channel> getChannels()
		{
			return customChannels;
		}
		
	}
}
