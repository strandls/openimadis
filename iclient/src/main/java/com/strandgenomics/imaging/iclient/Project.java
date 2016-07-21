/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.strandgenomics.imaging.iclient;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.strandgenomics.imaging.iclient.local.DirectUploadExperiment;
import com.strandgenomics.imaging.iclient.local.Indexer;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.util.DirectUploader;
import com.strandgenomics.imaging.iclient.util.Uploader;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.IProject;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.HttpUtil;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * A place holder of many records - may be an administrative unit or biological
 * @author arunabha
 */
public class Project extends ImageSpaceObject implements IProject {
	
	private static final long serialVersionUID = 2655751645007893345L;
	/**
	 * name of the project
	 */
	protected String name;
	/**
	 * notes/comments/description associated with this project
	 */
	protected String notes;
	/**
	 * creation date
	 */
	protected Date creationDate;
	/** 
	 * disk quota in gb 
	 */
	protected double storageQuota;
	/** 
	 * disk usage in gb 
	 */
	protected double spaceUsage;
	/**
	 * Number of records within the project
	 */
	protected int recordCount;
	
	/**
	 * creats project 
	 * @param name name of the project
	 * @param notes description
	 * @param creationDate date of creation of this project
	 * @param storageQuota storage space in gb allocated for this project
	 */
	public Project(String name, String notes, Date creationDate, double storageQuota)
	{
		this(name, notes, creationDate, storageQuota, 0, 0);
	}
	
	///package projected method, called internally to create a project instance
	Project(String name, String notes, Date creationDate, double storageQuota, double spaceUsage, int count)
	{
		this.name = name;
		this.notes = notes;
		this.creationDate = creationDate;
		this.storageQuota = storageQuota;
		this.spaceUsage = spaceUsage;
		this.recordCount = count;
	}
	
	/**
	 * Lists experiments within this projects
	 * @return
	 */
	public List<Experiment> listExperiment()
	{
		//makes a system call
    	List<BigInteger> archiveSignatures = getImageSpace().listArchives(this);
    	if(archiveSignatures == null || archiveSignatures.isEmpty())
    		return null;
    	
    	List<Experiment> exptList = new ArrayList<Experiment>();
    	for(BigInteger i : archiveSignatures)
    	{
    		exptList.add( new Experiment(i) );
    	}
    	
    	return exptList;
	}
	
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * Creates and returns an uploader to upload the specified raw experiment
	 * @param rawExpt the raw expt to upload
	 * @return the uploader
	 * @throws IOException
	 */
	public Uploader getUploader(RawExperiment rawExpt)
	{
		if(rawExpt instanceof DirectUploadExperiment)
			return new DirectUploader(this, rawExpt);
		
		return new Uploader(this, rawExpt);
	}
	
	/**
	 * Disk quota in GBs
	 * @return Disk quota in GBs
	 */
	public double getDiskQuota()
	{
		return storageQuota;
	}
	
	/**
	 * Disk space consumption in GBs
	 * @return Disk space consumption in GBs
	 */
	public double getSpaceUsage()
	{
		return spaceUsage;
	}
	
	/**
	 * Returns the name of the project
	 * Project names are unique across the Enterprise IMG Server
	 * @return the name of the project
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Notes or comments associated with the project 
	 * @return Notes or comments associated with the project 
	 */
	public String getNotes()
	{
		return notes;
	}
	
	/**
	 * Returns the creation time
	 * @return the creation time
	 */
	public Date getCreationDate()
	{
		return creationDate;
	}
	
	/**
	 * Returns ticket for uploading experiment
	 * @return ticket for uploading experiment
	 */
	public Ticket requestTicket(RawExperiment experiment)
	{
		//makes a system call
    	return getImageSpace().requestTicket(this, experiment);
	}
	
	/**
	 * returns the access key associated with user session
	 * @return
	 */
	public String getAccessKey()
	{
		return getImageSpace().getAccessKey();
	}
	
	/**
	 * List all legal members of this projects, may return null if the concerned user do not have permission
	 * to get this list
	 * @return all legal members of this projects
	 */
    public List<User> getProjectMembers()
    {
    	//makes a system call
    	return getImageSpace().getProjectMembers(name);
    }
    
    /**
     * Returns the project manager of this project
     * @return the project manager of this project
     */
    public List<User> getProjectManager()
    {
    	return getImageSpace().getProjectManager(name);
    }
    
    /**
     * Makes the specified user as the project Manager
     * Note that to make this system call to succeed, the login user must be the existing project manager
     * or a team leader or a facility manager
     * @param userName the new project manager
     */
    public void changeProjectManager(String userlogin)
    {
    	//makes a system call
    	getImageSpace().changeProjectManager(this, userlogin);
    }

    /**
     * Adds the specified users as the member of the this project.
     * Note that the login user must be the project Manager of this project for this call to be successful
     * @param users list of users to add to the project
     * @return the list of users who are successfully added
     */
    public void addProjectMembers(Permission p, String ... userLogin)
    {
    	if(userLogin == null) return;
    	//makes a system call
    	getImageSpace().addProjectMembers(name, p, userLogin);
    }
    
    /**
     * Removes the specified users as the member of the this project.
     * Note that the login user must be the project Manager of this project for this call to be successful
     * @param users list of users to add to the project
     * @return the list of users who are successfully removed
     */
    public List<User> removeProjectMembers(List<String> users)
    {
    	//makes a system call
    	return getImageSpace().removeProjectMembers(this, users);
    }
    
    /**
     * Deletes this project - place a request to delete this project. Projects are not deleted right away
     * Note that the login user may not have permission to delete, in that case an exception will be thrown
     */
    public Task delete()
    {
    	//makes a system call
    	return getImageSpace().deleteProject(getName());
    }
    
    
    /**
     * Archives this project - place a request to archive this project
     * Note that the login user may not have permission to archive, in that case an exception will be thrown
     */
    public Task archive()
    {
    	//makes a system call
    	return getImageSpace().archiveProject(getName());
    }
    
    
    /**
     * Restores this project from archive - place a request to restore this project
     * Note that the login user may not have permission to restore, in that case an exception will be thrown
     */
    public Task restore()
    {
    	//makes a system call
    	return getImageSpace().restoreProject(getName());
    }
    
    /**
     * creates new record builder having same metadata as parent record
     * @param recordLabel specified record label
     * @param parentRecord specified parent record
     * @return record builder instance
     */
    public RecordBuilder createRecordBuilder(String recordLabel, Record r)
    {
    	List<Channel> channels = new ArrayList<Channel>();
		for(int i=0;i<r.getChannelCount();i++)
		{
			channels.add((Channel) r.getChannel(i));
		}
		
		List<Site> sites = new ArrayList<Site>();
		for(int i=0;i<r.getSiteCount();i++)
		{
			sites.add(r.getSite(i));
		}
		
		Long sourceTime = r.getSourceFileTime() == null ? System.currentTimeMillis() : r.getSourceFileTime().getTime(); 
    	Long creationTime = System.currentTimeMillis();
		Long acqTime = r.getAcquiredDate() == null ? System.currentTimeMillis() : r.getAcquiredDate().getTime(); 
		
		return createRecordBuilder(recordLabel, r, r.getFrameCount(),
				r.getSliceCount(), channels, sites, r.getImageWidth(),
				r.getImageHeight(), r.getPixelDepth(),
				r.getPixelSizeAlongXAxis(), r.getPixelSizeAlongYAxis(),
				r.getPixelSizeAlongZAxis(), r.getImageType(),
				r.getSourceType(), r.getSourceFilename(),
				r.getSourceFilename(), sourceTime, creationTime, acqTime);
    }
    
    /**
     * creates new record builder from the record metadata
     * @param recordLabel
     * @param noOfFrames
     * @param noOfSlices
     * @param channels
     * @param sites
     * @param imageWidth
     * @param imageHeight
     * @param pixelDepth
     * @param pixelSizeX
     * @param pixelSizeY
     * @param pixelSizeZ
     * @param imageType
     * @param sourceFormat
     * @param sourceFileName
     * @param sourceFolder
     * @param sourceTime
     * @param creationTime
     * @param acquiredTime
     * @return
     */
	public RecordBuilder createRecordBuilder(String recordLabel,
			int noOfFrames, int noOfSlices, List<Channel> channels,
			List<Site> sites, int imageWidth, int imageHeight,
			PixelDepth pixelDepth, double pixelSizeX, double pixelSizeY,
			double pixelSizeZ, ImageType imageType, SourceFormat sourceFormat,
			String sourceFileName, String sourceFolder, long sourceTime,
			long creationTime, long acquiredTime)
    {
		return createRecordBuilder(recordLabel, null, noOfFrames, noOfSlices,
				channels, sites, imageWidth, imageHeight, pixelDepth,
				pixelSizeX, pixelSizeY, pixelSizeZ, imageType, sourceFormat,
				sourceFileName, sourceFolder, sourceTime, creationTime,
				acquiredTime);
    }
	
	/**
	 * creates new record builder having a parent record but with the record metadata
	 * @param recordLabel
	 * @param parentRecord
	 * @param noOfFrames
	 * @param noOfSlices
	 * @param channels
	 * @param sites
	 * @param imageWidth
	 * @param imageHeight
	 * @param pixelDepth
	 * @param pixelSizeX
	 * @param pixelSizeY
	 * @param pixelSizeZ
	 * @param imageType
	 * @param sourceFormat
	 * @param sourceFileName
	 * @param sourceFolder
	 * @param sourceTime
	 * @param creationTime
	 * @param acquiredTime
	 * @return
	 */
	public RecordBuilder createRecordBuilder(String recordLabel,
			Record parentRecord, int noOfFrames, int noOfSlices,
			List<Channel> channels, List<Site> sites, int imageWidth,
			int imageHeight, PixelDepth pixelDepth, double pixelSizeX,
			double pixelSizeY, double pixelSizeZ, ImageType imageType,
			SourceFormat sourceFormat, String sourceFileName,
			String sourceFolder, long sourceTime, long creationTime,
			long acquiredTime)
	{
		Long parentGuid = parentRecord == null ? null : parentRecord.getGUID();
		
		String ip = Util.getMachineIP(); 
		String ipAddress = (ip == null) ? "NA" : ip;
		
		String address = Util.getMachineAddress();
		String macAddress = address == null ? "NA" : address;
		
		long guid = getImageSpace().registerRecordBuilder(recordLabel, name, parentGuid,
				noOfFrames, noOfSlices, imageWidth, imageHeight, channels,
				sites, pixelDepth, pixelSizeX, pixelSizeY, pixelSizeZ, sourceFormat.name,
				imageType, ipAddress, macAddress,
				sourceFolder, sourceFileName, sourceTime, creationTime,
				acquiredTime);
		
		RecordBuilder rb = new RecordBuilder(guid, this, noOfFrames,
				noOfSlices, channels, sites, imageWidth, imageHeight,
				pixelDepth, pixelSizeX, pixelSizeY, pixelSizeZ, imageType,
				sourceFormat);
		return rb;
	}

    /**
     * Creates record (one or more, if not already there) with the specified files at the server side
     * Notes that this method returns immediately after uploading the files to the server, (it blocks till then)
     * the request is typically queued and generally served at a convenient time (for the server)
     * @param indexFolder the root folder/file to index
     * @param recursive whether to recursive descent into
     * @param autoMerge whether to merge the records (per experiment) based on their dimensions
     */
    public List<Experiment> createRecord(File indexFolder, boolean recursive, boolean autoMerge)
    {
    	try
    	{
    		Class.forName("loci.formats.gui.BufferedImageReader");
    	}
    	catch(ClassNotFoundException notFound)
    	{
    		throw new RuntimeException("bio-formats classes not found, please include loci_tools.jar in the classpath");
    	}

		Indexer indexer = new Indexer();
		List<RawExperiment> exptList = indexer.findRecords(indexFolder, recursive, null); 
		if(exptList == null || exptList.isEmpty()) return null;
		
		List<RawExperiment> eList = new ArrayList<RawExperiment>();
		for(RawExperiment e : exptList)
		{
			if(e.isExistOnServer())
			{
				System.out.println("Ignoring, Experiment already exist "+e);
			}
			else
			{
				eList.add(e);
			}
		}
		
		System.out.println("Number of Experiments to upload :\t"+eList.size());
		if(autoMerge) 
		{
			for(RawExperiment expt : eList)
			{
				mergeRecords(expt);
			}
		}
		
		List<Experiment> successList = new ArrayList<Experiment>();

		for(RawExperiment expt : eList)
		{
			try
			{
				uploadExperiment(expt);
				successList.add( new Experiment(expt.getMD5Signature()) );
				expt.dispose();
			} 
			catch (Exception e) 
			{
				System.out.println("unable to upload records in experiment "+expt);
				e.printStackTrace();
			}
		}
		
		return successList;
    }
    
    /**
     * returns list of user annotation keys used in this project 
     * @return list of user annotation keys used in this project 
     */
    public Collection<SearchField> getUserAnnotationFields()
    {
    	return getImageSpace().getUserAnnotationFields(this);
    }
    
    /**
     * Returns a list of meta-data names that are navigable
     * @return a list of meta-data names that are navigable
     */
    public Collection<SearchField> getNavigableFields()
    {
    	//makes a system call
    	return getImageSpace().getNavigableFields(this);
    }
    
    /**
     * Returns the list of available records for the login user to read 
     * @return the list of available records for the login user to read 
     */
    public int getRecordCount()
    {
    	return recordCount;
    }
    
    /**
     * Returns a list of all records the login user can read
     * @return a list of all records the login user can read
     */
    public long[] getRecords()
    {
		return getImageSpace().findRecords(this, null);
    }
    
    /**
     * List a list of navigation bins for the specified pre conditions and a current condition
     * @param preConditions a list of pre-existing search conditions
     * @param current the current condition
     * @return list of bins
     */
    public List<NavigationBin> findNavigationBins(Set<SearchCondition> preConditions, SearchCondition current)
    {
    	return  getImageSpace().findNavigationBins(this, preConditions, current);
    }
    
    /**
     * Search this project with the specified set of search conditions
     * @param conditions set of search condition
     * @return list of records satisfying the set of search condition
     */
    public long[] search(Set<SearchCondition> conditions)
    {
		return getImageSpace().findRecords(this, conditions);
    }
    
    /**
     * returns bookmark root for this project
     * @return
     */
    public BookmarkFolder getBookmarkRoot()
    {
    	return getImageSpace().getBookmarkRoot(this);
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	private void uploadExperiment(RawExperiment expt) throws IOException 
	{
		Uploader uploader = getUploader(expt);
		System.out.println("zipping experiment source files "+expt);
		File tarBall = uploader.packSourceFiles();
		tarBall.deleteOnExit();
		System.out.println("created zipped file "+tarBall);
		
		try
		{
			//fetch a upload ticket first
			System.out.println("Fetching tickets for uploading experiment "+expt);
			Ticket ticket = uploader.fetchTicket();
			upload(ticket, tarBall);
		}
		finally
		{
			tarBall.delete();
		}
	}
	
	private void upload(Ticket ticket, File tarBall) throws IOException
	{
		URL uploadUrl = ticket.getUploadURL();
		HttpUtil httpUtil = new HttpUtil(uploadUrl);
		httpUtil.upload(tarBall, null);
		System.out.println("Successfully uploaded experiment archive "+tarBall);
	}

	private void mergeRecords(RawExperiment expt) 
	{
		if(expt.size() == 1) return;// nothing to merge
		for(int i = expt.size(); i > 0 ; i--)
		{
			int counter = 0;
			for(Signature s : expt.getRecordSignatures())
			{
				IRecord ithRecord = expt.getRecord(s);
				counter++;
				
				if(ithRecord.getSiteCount() == 1)
				{
					List<IRecord> mergableRecords = expt.getAllSiblings(ithRecord);
					if(mergableRecords.size() > 1)
					{
						mergeRecords(expt, mergableRecords);
						break;
					}
				}
				
				if(counter == expt.size())
					break;
			}
		}
	}
	
	private void mergeRecords(RawExperiment expt, List<IRecord> mergableRecords)
	{
		Signature[] signatureList = new Signature[mergableRecords.size()];
		for(int i = 0;i < signatureList.length; i++)
		{
			signatureList[i] = mergableRecords.get(i).getSignature();
		}
		
		expt.mergeRecordsAsSites(signatureList);
	}
}
