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

package com.strandgenomics.imaging.iengine.bioformats;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import loci.formats.gui.BufferedImageReader;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.bioformats.BufferedImageReaderPool;
import com.strandgenomics.imaging.icore.bioformats.ImageManager;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.system.ErrorCode.ImageSpace;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Extracts records from source files
 * @author Anup Kulkarni
 *
 */
public class DirectUploadRecordExtractor extends RecordExtractor{
	
	private Map<String, ISourceReference> nameToReferenceMapping = new HashMap<String, ISourceReference>();
	
	protected Logger logger = Logger.getLogger("com.strandgenomics.imaging.iworker.services");

	/**
	 * Creates a record extractor to extract records from the specified tar-ball
	 * @param context the specification of the upload request 
	 */
	public DirectUploadRecordExtractor(RecordCreationRequest context) 
	{
		super(context);
		
		this.thumbnailWidth = Constants.getRecordThumbnailWidth();
		this.thumbnailHeight = Constants.getRecordThumbnailHeight();
	}

	public void extractRecords() throws ImagingEngineException,Exception
	{
		ClientExperiment expt = null;
		File rootFolder = null;
		try
		{
			rootFolder = new File(Constants.TEMP_DIR, context.getArchiveHash().toString());
			//first: un-tar the archive
			rootFolder.mkdirs(); //create the temp folder for this record
			
			logger.logp(Level.INFO, "DirectUploadRecordExtractor", "extractRecords", "extracting source files for "+context +" to "+rootFolder);
			context.extractTarBall(rootFolder);
			logger.logp(Level.INFO, "DirectUploadRecordExtractor", "extractRecords", "successfully uncompressed source files XXXX from "+context.getTarBall() +" in "+rootFolder);
			
			// save previous signature
			BigInteger dummySignature=context.getArchiveHash();
			
			// priorities source files
			context.prioritiesFiles();
			
			// group experiments
			List<ClientExperiment> group = getExperimentGroups(rootFolder);
			
			boolean duplicate = false;
			
			for(ClientExperiment member:group)
			{
				expt = member;
						
				logger.logp(Level.FINE, "DirectUploadRecordExtractor", "extractRecords","computing signature");	
				List<ISourceReference> refs = expt.getReference();
				File[] sourceFiles = new File[refs.size()];
				int i=0;
				for(ISourceReference ref:refs)
				{
					sourceFiles[i] = new File(expt.getContext().getExtractionFolder(), getFileName(context.getSourceDirectory(), ref.getSourceFile()));
					logger.logp(Level.FINE, "DirectUploadRecordExtractor", "extractRecords", "soucefile="+sourceFiles[i].getAbsolutePath());
					i++;
				}
				expt.context.setSourceFiles(sourceFiles);
				BigInteger md5Signature=Util.computeMD5Hash(expt.context.getSourceFiles());
				logger.logp(Level.FINE, "RecordExtractor", "extractRecords", "computed signature="+Util.toHexString(md5Signature));							
				
				//check if archive hash already present
				ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
				ArchiveDAO archiveDao = factory.getArchiveDAO();
				
				if (archiveDao.findArchive(md5Signature) != null)
				{
					logger.logp(Level.INFO, "DirectUploadRecordExtractor", "extractRecords", "archive already exists");
					duplicate = true;
					continue;
				}
				
				logger.logp(Level.FINE, "DirectUploadRecordExtractor", "extractRecords", "dummySignature="+Util.toHexString(dummySignature));
				
				expt.context.setMD5Signature(md5Signature);
				
				logger.logp(Level.FINE, "DirectUploadRecordExtractor", "extractRecords", "extracting records from "+expt.context);
				expt.extractRecords(null,null);
				
				logger.logp(Level.FINE, "DirectUploadRecordExtractor", "extractRecords", "successfully extracted "+expt.size() +" records from "+expt.context);
				//merge the specified sites as multi-site records
				mergeRecordsAsSites(expt);
				//now update the database etc
				SysManagerFactory.getStorageManager().registerExperiment(expt.context.getApplication(), expt.context.getActor(), expt.context.getAccessToken(), expt.context.getProject(), expt);
				logger.logp(Level.INFO, "DirectUploadRecordExtractor", "extractRecords", "successfully extracted records from "+expt.context);
			}
			
			context.setMD5Signature(dummySignature);
			
			if(duplicate)
			{
				throw new ImagingEngineException(new ErrorCode(ImageSpace.ARCHIVE_ALREADY_EXIST));
			}
		}
		catch(ImagingEngineException e){
			throw e;
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "DirectUploadRecordExtractor", "extractRecords", "error while extracting records for "+context, ex);
			throw ex;
		}
		finally
		{
			logger.logp(Level.INFO, "DirectUploadRecordExtractor", "extractRecords", "dipose context");
			if(expt != null) expt.dispose();
			if(rootFolder != null) Util.deleteTree(rootFolder);
			if(context != null) context.dispose();
		}
		
		logger.logp(Level.INFO, "DirectUploadRecordExtractor", "extractRecords", "successfully extracted records from "+context);
	}
	
	private List<ClientExperiment> getExperimentGroups(File rootFolder) throws DataAccessException
	{
		List<ClientExperiment> expList = new ArrayList<ClientExperiment>();
		
		for(ISourceReference ref: context.getClientFiles())
		{
			String name = getFileName(context.getSourceDirectory(), ref.getSourceFile());
			nameToReferenceMapping.put(name, ref);
		}
		
		for(ISourceReference ref: context.getClientFiles())
		{
			String name = getFileName(context.getSourceDirectory(), ref.getSourceFile());
			if(!nameToReferenceMapping.containsKey(name)) continue;
			
			List<ISourceReference> probeFiles = new ArrayList<ISourceReference>();
			probeFiles.add(ref);
			
			RecordCreationRequest probeContext = new RecordCreationRequest(context.getApplication(), context.getActor(), context.getAccessToken(), context.getProject(), context.getClientIPAddress(),
					context.getClientMacAddress(), context.getSourceDirectory(), ref.getSourceFile(), context.getArchiveHash(), probeFiles, context.getRecordSpecification(), context.isDirectUploadRequest());
			logger.logp(Level.FINER, "DirectUploadRecordExtractor", "getExperimentGroups", "setting root folder "+rootFolder);
			probeContext.setExtractionFolder(rootFolder);
			File seedFile = new File(rootFolder, getFileName(context.getSourceDirectory(), ref.getSourceFile()));
			logger.logp(Level.FINER, "DirectUploadRecordExtractor", "getExperimentGroups", "setting seed file"+seedFile.getAbsolutePath());
			probeContext.setSeedFile(seedFile);
			
			ClientExperiment expt = new ClientExperiment(probeContext);
			
			try
			{
				BufferedImageReaderPool pool = ImageManager.getInstance().createPool(expt);
				BufferedImageReader imageReader = pool.getImageReader();
				String[] usedFileNames = imageReader.getUsedFiles();
				
				File expRoot = new File(rootFolder, String.valueOf(System.currentTimeMillis()));
				expRoot.mkdir();
				
				List<ISourceReference> clientFiles = new ArrayList<ISourceReference>();
				File[] sourceFiles = new File[usedFileNames.length];
				int i = 0;
				for(String usedFile:usedFileNames)
				{
					File temp = new File(usedFile);

					sourceFiles[i] = new File(expRoot, temp.getName());
					Util.copy(temp, sourceFiles[i]);

					clientFiles.add(nameToReferenceMapping.remove(sourceFiles[i].getName()));
					
					i++;
				}
				
				
				RecordCreationRequest actualReq = new RecordCreationRequest(context.getApplication(), context.getActor(), context.getAccessToken(), context.getProject(), context.getClientIPAddress(),
						context.getClientMacAddress(), context.getSourceDirectory(), usedFileNames[0], context.getArchiveHash(), clientFiles, context.getRecordSpecification(), context.isDirectUploadRequest());
				
				actualReq.setExtractionFolder(expRoot);
				actualReq.setSeedFile(new File(expRoot, getFileName(context.getSourceDirectory(), ref.getSourceFile())));
				actualReq.setSourceFiles(sourceFiles);
				ClientExperiment actualExpt = new ClientExperiment(actualReq);
				
				expList.add(actualExpt);
			}
			catch (Exception e)
			{
				logger.logp(Level.WARNING, "DirectUploadRecordExtractor", "getExperimentGroups", "ignoring file "+ref.getSourceFile(), e);
			}
		}
		
		return expList;
	}
	
	private String getFileName(String sourceFile, String name)
	{
		if(name.contains(sourceFile))
		{
			name = name.substring(sourceFile.length()+1, name.length());
		}
		return name;
	}
}
