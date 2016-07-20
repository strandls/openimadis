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

package com.strandgenomics.imaging.iengine.system;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.RecordExportDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.export.ExportFormat;
import com.strandgenomics.imaging.iengine.export.ExportRequest;
import com.strandgenomics.imaging.iengine.export.ExportService;
import com.strandgenomics.imaging.iengine.export.ExportStatus;
import com.strandgenomics.imaging.iengine.models.Project;

/**
 * Manages record export related requests
 * 
 * @author Anup Kulkarni
 */
public class ExportManager extends SystemManager {
	
    /**
     * How frequently should the cache be cleaned
     */
    private static final int CACHE_CLEAN_FREQUENCY = 24 * 60 * 60;
	/**
	 * map to store export id to export-request mapping
	 */
	private long usedMemory;
    /**
     * service to clean up the cache and db periodically.
     */
    private ScheduledThreadPoolExecutor cleanupService = null;
	
	ExportManager() 
	{ 
		init();
	}
	
	private void init()
	{
		File exportStorage = new File(Constants.getStringProperty(Property.EXPORT_STORAGE_LOCATION, null));
		usedMemory = Util.calculateSize(exportStorage);

		cleanupService = new ScheduledThreadPoolExecutor(1);
		RequestCleaner requestCleaner = new RequestCleaner();
		requestCleaner.setDaemon(true);
		cleanupService.scheduleWithFixedDelay(requestCleaner, CACHE_CLEAN_FREQUENCY, CACHE_CLEAN_FREQUENCY, TimeUnit.SECONDS);
	}
	
	/**
	 * removes exported record
	 * @param actorLogin logged in user
	 * @param requestId specified export request
	 * @throws DataAccessException
	 */
	public synchronized void removeExportedRecord(String actorLogin, long requestId) throws DataAccessException
	{
		// check for valid request
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		ExportRequest request = exportDao.getExportRequest(requestId);
		if(request == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INVALID_EXPORT_REQUEST));
		}
		
		Project p = SysManagerFactory.getProjectManager().getProjectForRecord(actorLogin, request.getRecordIds().get(0));
		
		// check for user permission
		if(!SysManagerFactory.getUserPermissionManager().canExport(actorLogin, p.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		// remove from DB
		exportDao.removeRecordExportRequest(requestId);
		
		// remove from storage location
		String exportLocation = request.getURL();
		if(exportLocation!=null)
		{
			File f = new File(exportLocation);
			if(f.isFile())
			{
				long size = Util.calculateSize(f);
				
				f.delete();
				
				// update size
				usedMemory -= size;
			}
		}
	}
	
	/**
	 * returns the URL to download the exported record (null if record export is not completed)
	 * @param actorLogin logged in user
	 * @param requestId request id
	 * @return the URL to download the exported record (null if record export is not completed)
	 * @throws DataAccessException
	 */
	public String getDownloadURL(String actorLogin, long requestId) throws DataAccessException
	{
		// check for valid request
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		ExportRequest request = exportDao.getExportRequest(requestId);
		
		if(request == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INVALID_EXPORT_REQUEST));
		}
		
		if(request.validTill <= System.currentTimeMillis())
		{
			removeExportedRecord(actorLogin, requestId);
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.EXPORT_VALIDITY_EXPIRED));
		}
		
		logger.logp(Level.INFO, "ExportManager", "getDownloadURL", "requesting download url for requestId="+requestId+" format="+request.format);
		
		Project p = SysManagerFactory.getProjectManager().getProjectForRecord(actorLogin, request.getRecordIds().get(0));
		
		// check for user permission
		if(!SysManagerFactory.getUserPermissionManager().canExport(actorLogin, p.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		// get the download url
		String exportLocation = request.getURL();
		
		return exportLocation;
	}
	
	/**
	 * registers export request
	 * @param actorLogin current logged in user
	 * @param guid record to be exported
	 * @param format format in which record is to be exported
	 * @param validityTime time till which the exported record is valid
	 * @param exportName name of the export
	 * @return requestid
	 * @throws DataAccessException 
	 * @throws IOException
	 */
	public long registerExportRequest(String actorLogin, List<Long> guids, ExportFormat format, long validityTime, String exportName) throws DataAccessException
	{
		if(guids==null || guids.size()<1)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR));
		}
		
		logger.logp(Level.INFO, "ExportManager", "registerExportRequest", "registering export request for guids="+guids+" format="+format);
		
		Project p = SysManagerFactory.getProjectManager().getProjectForRecord(actorLogin, guids.get(0));
		
		if(!SysManagerFactory.getUserPermissionManager().canExport(actorLogin, p.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		// check for space
		if(!sufficientMemory(actorLogin, guids))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.NO_SPACE_LEFT));
		}
		
		// create new record export request
		long submittedOn = System.currentTimeMillis();
		ExportRequest request = createExportRequest(actorLogin, guids, format, validityTime, submittedOn, exportName);
		
		// submit the request to worker
		try
		{
			submitRequest(request);
		}
		catch (IOException e)
		{
			request.setStatus(ExportStatus.FAILED);
			DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO().setExportStatus(request.requestId, ExportStatus.FAILED);
		}
		
		return request.requestId;
	}
	
	/**
	 * returns the export requests for user
	 * @param loginUser current logged in user
	 * @return all the export requests for user
	 * @throws DataAccessException
	 */
	public List<ExportRequest> getExportRequsts(String loginUser) throws DataAccessException
	{
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		long currTime = System.currentTimeMillis();

		List<ExportRequest>allRequests = exportDao.getExportRequests(loginUser);
		
		List<ExportRequest> valid = new ArrayList<ExportRequest>();
		if (allRequests != null)
		{
			for (ExportRequest req : allRequests)
			{ // dont consider timed out requests
				if (req.validTill > currTime)
					valid.add(req);
			}
		}
		
		return valid;
	}
	
	
	/**
	 * returns the export requests of all users
	 * @return the export requests of all users
	 * @throws DataAccessException
	 */
	public List<ExportRequest> getAllExportRequsts(String actorLogin) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorLogin))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		long currTime = System.currentTimeMillis();

		List<ExportRequest>allRequests = exportDao.loadAllRequests();
		
		List<ExportRequest> valid = new ArrayList<ExportRequest>();
		if (allRequests != null)
		{
			for (ExportRequest req : allRequests)
			{ // dont consider timed out requests
				if (req.validTill > currTime)
					valid.add(req);
			}
		}
		
		return valid;
	}
 
	/**
	 * update the export status of export request
	 * @param requestId specified export request id
	 * @param status new status
	 * @throws DataAccessException
	 */
	public void setExportRequestStatus(long requestId, ExportStatus status) throws DataAccessException
	{
		logger.logp(Level.INFO, "ExportManager", "setExportRequestStatus", "updating export status of request="+requestId+" to "+status);
		
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		ExportRequest request = exportDao.getExportRequest(requestId);
		
		if(request!=null)
		{
			exportDao.setExportStatus(requestId, status);
		}
	}
	
	/**
	 * sets export location
	 * @param requestId specified export request
	 * @param exportLocation location on storage device where the exported archive will be stored
	 * @throws DataAccessException
	 */
	public void setExportLocation(long requestId, String exportLocation) throws DataAccessException
	{
		logger.logp(Level.INFO, "ExportManager", "setExportLocation", "updating export location of request="+requestId+" to "+exportLocation);
		
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		ExportRequest request = exportDao.getExportRequest(requestId);
		
		if(request!=null)
		{
			exportDao.setExportLocation(requestId, exportLocation);
		}
	}
	
	/**
	 * returns the status of the export request
	 * @param requestId
	 * @return
	 * @throws DataAccessException
	 */
	public ExportStatus getExportRequestStatus(long requestId) throws DataAccessException
	{
		logger.logp(Level.INFO, "ExportManager", "getExportRequestStatus", "getting export status for task="+requestId);
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		ExportRequest request = exportDao.getExportRequest(requestId);
		
		if(request!=null)
		{
			return request.getStatus();
		}
		
		return ExportStatus.TERMINATED;
	}
	
	private boolean sufficientMemory(String actorLogin, List<Long> guids) throws DataAccessException
	{
		if(getMemoryRequired(actorLogin, guids) <= getAvailableMemory())
			return true;
		return false;
	}
	
	private long getMemoryRequired(String actorLogin, List<Long> guids) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		long totalSize = 0;
		for(Long guid:guids)
		{
			BigInteger archiveSignature = recordDao.getArchiveSignature(guid);
			
			File storageRoot = SysManagerFactory.getStorageManager().getStorageRoot(archiveSignature);
			long size = Util.calculateSize(storageRoot);
			
			totalSize += size;
		}
		
		return totalSize;
	}
	
	private synchronized long getAvailableMemory()
	{
		long allocatedMemory = Constants.getLongProperty(Property.EXPORT_CACHE_SIZE, 100 * 1024 * 1024 * 1024);
		allocatedMemory = allocatedMemory * 1024 * 1024;
		
		logger.logp(Level.INFO, "ExportManager", "getAvailableMemory", "available memory "+allocatedMemory);
		
		if((allocatedMemory - usedMemory) < 0)
		{
			// over the period of time used memory is updated as per estimation of required memory
			// if allocated memory becomes less than export memory
			// calculate actual used memory
			File exportStorage = new File(Constants.getStringProperty(Property.EXPORT_STORAGE_LOCATION, null));
			usedMemory = Util.calculateSize(exportStorage);
		}
		
		return (allocatedMemory - usedMemory);
	}

	private ExportRequest createExportRequest(String actorLogin, List<Long> guids, ExportFormat format, long validityTime, long submittedOn, String exportName) throws DataAccessException
	{
		long requstId = generateID();
		
		long memRequired = getMemoryRequired(actorLogin, guids);
		
		ExportRequest request = new ExportRequest(requstId, actorLogin, guids, format, validityTime, submittedOn, memRequired, exportName, ExportStatus.SUBMITTED);
		logger.logp(Level.INFO, "ExportManager", "createExportRequest", "new export request "+request);
		
		// updata DB
		updateDB(request);

		// update memory
		usedMemory += memRequired;
		
		return request;
	}

	private void updateDB(ExportRequest request) throws DataAccessException
	{
		logger.logp(Level.INFO, "ExportManager", "updateDB", "updating db "+request);
		
		RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
		exportDao.insertRecordExportRequest(request);
	}

	private void submitRequest(ExportRequest request) throws IOException
	{
		logger.logp(Level.INFO, "ExportManager", "submitRequest", "submitting request for record export "+request);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getExportServicePort());
        ExportService serviceStub = null;
		try 
		{
			serviceStub = (ExportService) registry.lookup(ExportService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "ExportManager", "submitRequest", "error submitting request for record export "+request);
			throw new IOException(e);
		}
                    
        serviceStub.submitExportRequest(request);
	}
	
	/**
	 * returns unique id which will be used for export request
	 * @return unique id which will be used for export request
	 */
	private static synchronized final long generateID()
	{
		return System.nanoTime();
	}
	
	/**
	 * deletes the request which are expired
	 * 
	 * @author Anup Kulkarni
	 */
	private class RequestCleaner extends Thread {
		@Override
		public void run()
		{
			logger.logp(Level.INFO, "ExportManager", "RequestCleaner", "start cleaning requests");
			try
			{
				RecordExportDAO exportDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordExportDAO();
				List<ExportRequest>allRequests = exportDao.loadAllRequests();
				if(allRequests==null)
					return;
				
				long currTime = System.currentTimeMillis();
				
				List<ExportRequest> toDelete = new ArrayList<ExportRequest>();
				for(ExportRequest req: allRequests)
				{
					if(req.validTill <= currTime)
						toDelete.add(req);
				}
				
				for(ExportRequest req: toDelete)
				{
					// remove from storage location
					String exportLocation = exportDao.getExportLocation(req.requestId);
					
					if(exportLocation!=null && !exportLocation.isEmpty())
					{
						File f = new File(exportLocation);
						long size = Util.calculateSize(f);
						
						f.delete();
						
						// update size
						usedMemory -= size;
					}
					
					// remove from DB
					exportDao.removeRecordExportRequest(req.requestId);
				}
			}
			catch (Exception e)
			{
				logger.logp(Level.WARNING, "ExportManager", "RequestCleaner", "start cleaning requests", e);
			}
		}
	}
}
