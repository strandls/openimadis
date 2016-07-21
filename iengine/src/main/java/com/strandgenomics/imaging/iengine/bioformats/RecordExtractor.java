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

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.system.ErrorCode.ImageSpace;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest.RecordSpec;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Extracts records from source files
 * @author arunabha
 *
 */
public class RecordExtractor {
	
	/**
	 * The specification of the client request
	 */
	protected final RecordCreationRequest context;
	/**
	 * width of a record thumb-nail in pixels
	 */
	protected int thumbnailWidth;
	/**
	 * height of a record thumb-nail in pixels
	 */
	protected int thumbnailHeight;
	
	protected Logger logger = Logger.getLogger("com.strandgenomics.imaging.iworker.services");

	/**
	 * Creates a record extractor to extract records from the specified tar-ball
	 * @param context the specification of the upload request 
	 */
	public RecordExtractor(RecordCreationRequest context) 
	{
		this.context  = context;
		
		this.thumbnailWidth = Constants.getRecordThumbnailWidth();
		this.thumbnailHeight = Constants.getRecordThumbnailHeight();
	}

	public void extractRecords() throws ImagingEngineException,Exception
	{
		File rootFolder = null;
		ClientExperiment expt = null;
		try
		{
			rootFolder = new File(Constants.TEMP_DIR, context.getArchiveHash().toString());
			//first: un-tar the archive
			rootFolder.mkdirs(); //create the temp folder for this record
			
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "extracting source files for "+context +" to "+rootFolder);
			context.extractTarBall(rootFolder);
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "successfully uncompressed source files XXXX from "+context.getTarBall() +" in "+rootFolder);
			
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords","computing signature");		
			BigInteger md5Signature=Util.computeMD5Hash(context.getSourceFiles());
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "computed signature="+Util.toHexString(md5Signature));							
			
			//check if archive hash already present
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			ArchiveDAO archiveDao = factory.getArchiveDAO();
			
			if(archiveDao.findArchive(md5Signature)!=null){
				logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "archive already exists");
				throw new ImagingEngineException(new ErrorCode(ImageSpace.ARCHIVE_ALREADY_EXIST));
			}
			
			BigInteger dummySignature=context.getArchiveHash();
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "dummySignature="+Util.toHexString(dummySignature));
			
			context.setMD5Signature(md5Signature);
			// priorities source files
			context.prioritiesFiles();
			
			expt = new ClientExperiment(context);
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "extracting records from "+context);
			expt.extractRecords(null,null);
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "successfully extracted "+expt.size() +" records from "+context);
			//merge the specified sites as multi-site records
			mergeRecordsAsSites(expt);
			//now update the database etc
			SysManagerFactory.getStorageManager().registerExperiment(context.getApplication(), context.getActor(), context.getAccessToken(), context.getProject(), expt);
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "successfully extracted records from "+context);
			
			try
			{
				// maintain map of old request to new request
				// if they are not the same
				if(dummySignature != md5Signature)
					DBImageSpaceDAOFactory.getDAOFactory().getArchiveDAO().insertSignatureMapping(dummySignature, md5Signature);
			}
			catch (Exception e)
			{
				logger.logp(Level.WARNING, "DirectUploadRecordExtractor", "extractRecords", "failed to insert mapping between old and new signature");
			}
			
			context.setMD5Signature(dummySignature);
		}
		catch(ImagingEngineException e){
			throw e;
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "error while extracting records for "+context, ex);
			throw ex;
		}
		finally
		{
			logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "dipose context");
			if(expt != null) expt.dispose();
			if(rootFolder != null) Util.deleteTree(rootFolder);
			if(context != null) context.dispose();
		}
		
		logger.logp(Level.INFO, "RecordExtractor", "extractRecords", "successfully extracted records from "+context);
	}

	protected void mergeRecordsAsSites(ClientExperiment expt)
	{
		List<RecordSpec> recordSpecs = context.getRecordSpecification();
		if(recordSpecs == null || recordSpecs.isEmpty()) return;
		
		logger.logp(Level.INFO, "RecordExtractor", "mergeRecordsAsSites", "merging records from "+context);
		
		//we extract records as single site (per series) records
		Map<Integer, Signature> seriesSignatureMap = createSeriesSignatureMap(expt);
		
		//now check the sites to merge
		for(RecordSpec spec :  recordSpecs)
		{
			if(spec.getSiteCount() == 1) //not merged, keep as is
			{
				//we remove all valid sites from this map so that the left over sites 
				//and hence records can be removed from the experiment
				int seriesNo = spec.getSites().get(0).getSeriesNo();
				Signature singleSeries = seriesSignatureMap.remove(seriesNo);
				addCustomChannelInfo(expt, singleSeries, spec.getChannels());
			}
			else
			{
				Signature multiSeries = mergeRecords(expt, seriesSignatureMap, spec.getSites());
				addCustomChannelInfo(expt, multiSeries, spec.getChannels());
			}
		}
		
		//delete the sites that are not referenced
		if(!seriesSignatureMap.isEmpty())
		{
			for(Map.Entry<Integer, Signature> entry : seriesSignatureMap.entrySet())
			{
				Signature rejects = entry.getValue();
				logger.logp(Level.INFO, "RecordExtractor", "mergeRecordsAsSites", "removing records "+rejects);
				expt.removeRecord(rejects);
			}
		}
	}
	
	private void addCustomChannelInfo(ClientExperiment expt, Signature signature, List<Channel> channels)
	{
		if(channels == null || channels.isEmpty()) return;
		IRecord ithRecord = expt.getRecord(signature);
		logger.logp(Level.FINE, "RecordExtractor", "addCustomChannelInfo", "found series "+ithRecord +" for signature "+signature);

		if(ithRecord.getChannelCount() != channels.size())
		{
			logger.logp(Level.WARNING, "RecordExtractor", "addCustomChannelInfo", "chhanels count do not match, ignoring");
			return;
		}
		
		if(ithRecord instanceof ClientRecord)
		{
			logger.logp(Level.INFO, "RecordExtractor", "addCustomChannelInfo", "setting custom channels");
			((ClientRecord) ithRecord).setChannels(channels);
		}
		else
		{
			logger.logp(Level.WARNING, "RecordExtractor", "addCustomChannelInfo", "unknown record instance, ignoring");
		}
	}

	private Signature mergeRecords(ClientExperiment expt, Map<Integer, Signature> seriesSignatureMap, List<Site> sites) 
	{
		List<Signature> signaturesToMerge = new ArrayList<Signature>();
		for(Site s : sites)
		{
			//we remove all valid sites from this map so that the left over sites 
			//and hence records can be removed from the experiment
			signaturesToMerge.add( seriesSignatureMap.remove(s.getSeriesNo()) );
		}
		
		logger.logp(Level.INFO, "RecordExtractor", "mergeRecords", "merging "+signaturesToMerge.size() +" records ");
		return expt.mergeRecordsAsSites( signaturesToMerge.toArray(new Signature[0]) );
	}

	private Map<Integer, Signature> createSeriesSignatureMap(ClientExperiment expt)
	{
		Map<Integer, Signature> seriesSignatureMap = new HashMap<Integer, Signature>();
		//populate this map
		for(Signature s : expt.getRecordSignatures())
		{
			IRecord r = expt.getRecord(s);
			seriesSignatureMap.put(r.getSite(0).getSeriesNo(), s); //all are single site records
		}
		
		return seriesSignatureMap;
	}
}
