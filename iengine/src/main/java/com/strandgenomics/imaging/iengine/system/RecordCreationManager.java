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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.ImageWriter;
import loci.formats.meta.IMetadata;
import loci.formats.services.OMEXMLService;
import ome.xml.model.enums.DimensionOrder;
import ome.xml.model.enums.EnumerationException;
import ome.xml.model.enums.PixelType;
import ome.xml.model.primitives.NonNegativeInteger;
import ome.xml.model.primitives.PositiveInteger;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.RecordMarker;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.bioformats.custom.FieldType;
import com.strandgenomics.imaging.icore.bioformats.custom.ImageIdentifier;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordIdentifier;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.bioformats.ClientSourceFile;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ImagePixelDataDAO;
import com.strandgenomics.imaging.iengine.dao.NavigationDAO;
import com.strandgenomics.imaging.iengine.dao.RecordCreationDAO;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.RecordBuilder;

/**
 * manages record record under construction and record creation using image data
 * 
 * @author Anup Kulkarni
 *
 */
public class RecordCreationManager extends SystemManager {
	
	private static final Random rnd = new Random(System.currentTimeMillis());
	
    /**
     * How frequently should the record be cleaned
     * 1 days
     */
    private static final int CLEAN_FREQUENCY = 24 * 60 * 60;
	/**
     * service to clean up the uncommitted record requests
     */
    private ScheduledThreadPoolExecutor cleanupService = null;

	RecordCreationManager() 
	{ 
		cleanupService = new ScheduledThreadPoolExecutor(1);
        cleanupService.scheduleWithFixedDelay(new RecordCleaner(), CLEAN_FREQUENCY, CLEAN_FREQUENCY, TimeUnit.SECONDS);
	}
	
	public Long registerRecord(Application app, String actorLogin,
			String recordLabel, Long parentGuid, int projectID, String uploadedBy,
			int noOfSlices, int noOfFrames, int imageWidth, int imageHeight,
			List<Channel> channels, List<Site> sites, PixelDepth imageDepth,
			double xPixelSize, double yPixelSize, double zPixelSize,
			SourceFormat sourceType, ImageType imageType, String machineIP,
			String macAddress, String sourceFolder, String sourceFilename,
			Long sourceTime, Long creationTime, Long acquiredTime)
			throws IOException
	{
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canUpload(actorLogin, project.getName()))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
		logger.logp(Level.INFO, "RecordCreationManager", "registerRecord", "registering record "+recordLabel);
		// create random archive signature
		BigInteger archiveSignature = new BigInteger(127, rnd);
		
		// insert archive
		// a.  create the folder structure for storage
		File storageRoot = getStorageRoot();
		File archiveRoot = getArchiveRoot(storageRoot, uploadedBy, projectID);

		String recordFolder = recordLabel + System.currentTimeMillis();
		File recordDirectory = new File(archiveRoot, recordFolder);
		recordDirectory.mkdir();
		
		File sourceFilesDir = new File(recordDirectory, StorageManager.FOLDER_NAME_SOURCE_FILES);
		sourceFilesDir.mkdir();
		
		File attachmentRoot = new File(recordDirectory, StorageManager.Attachment);
		attachmentRoot.mkdir();
		
		logger.logp(Level.FINEST, "RecordCreationManager", "registerRecord", "created storage structure for record "+recordLabel);

		// b. create dummy source files reference
		List<ISourceReference> sourceFiles = new ArrayList<ISourceReference>();
		String projectLocation = SysManagerFactory.getProjectManager().getProject(projectID).getLocation();
		
		insertArchive(archiveSignature, projectLocation+"/"+archiveRoot.getName(), recordFolder, sourceFiles );
		
		List<Site>dummySites = new ArrayList<Site>();
		int cnt = 0;
		for(Site site:sites)
		{
			Site s = new Site(cnt, site.getName());
			dummySites.add(s);
		}
		
		sites = dummySites;
		
		// insert record
		BigInteger siteSignature = Signature.createSiteHash(sites);
		
		// create dummy profile
		String microscopeName = SysManagerFactory.getMicroscopeManager().getMicroscope(machineIP, macAddress);
	
		RecordDAO recordDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordDAO();
		long uploadTime = System.currentTimeMillis();
		recordDao.insertRecord(projectID, uploadedBy, noOfSlices, noOfFrames,
				imageWidth, imageHeight, siteSignature , archiveSignature,
				uploadTime, sourceTime, creationTime, acquiredTime, imageDepth,
				xPixelSize, yPixelSize, zPixelSize, sourceType, imageType,
				machineIP, macAddress, sourceFolder, sourceFilename, channels,
				sites, RecordMarker.UNDER_CONSTRUCTION, microscopeName);
		
		long guid = recordDao.findGUIDForRecordBuilder(new Signature(noOfFrames, noOfSlices, channels.size(), imageWidth, imageHeight, sites, archiveSignature));
		
		logger.logp(Level.FINEST, "RecordCreationManager", "registerRecord", "found guid="+guid+" for "+recordLabel);
		
		// write metadata file
//		writeMetadata(sourceFilesDir.getAbsolutePath(), noOfFrames, noOfSlices, channels.size(), sites.size(), imageWidth, imageHeight);
		Date acqTime = null;
		if(acquiredTime != null)
			acqTime = new Date(acquiredTime);
		writeImgMetaData(sourceFilesDir, recordLabel, noOfFrames, noOfSlices, channels.size(), sites.size(), imageWidth, imageHeight, imageDepth, imageType, false, xPixelSize, yPixelSize, zPixelSize,acqTime);
		
		// update cache and DB
		RecordBuilder rb = new RecordBuilder(guid, parentGuid, noOfFrames, noOfSlices, channels.size(), sites.size(), imageWidth, imageHeight, imageDepth, sourceFilesDir.getAbsolutePath());
		insertRecordBuilder(rb);
		
		return guid;
	}
	
	/**
	 * creates the upload URL for uploading raw data
	 * 
	 * @param projectId  of parent project
	 * @param guid  of record under construction
	 * @param dim of raw data
	 * @param clientIP  of client machine
	 * @return upload URL
	 */
	public String getUploadURL(String actorLogin, long guid, Dimension dim, String clientIP) throws DataAccessException 
	{
		return DataExchange.UPLOAD_RAW_DATA.generateURL(actorLogin, clientIP, System.currentTimeMillis(), guid, dim.frameNo, dim.sliceNo, dim.channelNo, dim.siteNo);
	}

	/**
	 * adds specified pixel data to specified record under construction
	 * @param app used for creating record
	 * @param actorLogin authorized user
	 * @param guid specified record under construction 
	 * @param dim to which the pixel data is to be added
	 * @param imageMetaData meta data for the image
	 * @throws IOException
	 */
	public void addImageData(Application app, String actorLogin, long guid, Dimension dim, ImagePixelData imageMetaData) throws IOException
	{
		logger.logp(Level.INFO, "RecordCreationManager", "addImageData", "adding image metada for guid="+guid);
		
		// check if request is valid
		RecordBuilder recordBuilder = getRecordBuilder(guid);
		
		if(recordBuilder == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.RECORD_CREATION_REQUEST_DOES_NOT_EXIST));
		}
		
		if(recordBuilder.maxChannels<=dim.channelNo || recordBuilder.maxSites<=dim.siteNo || recordBuilder.maxFrames<=dim.frameNo || recordBuilder.maxSlices<=dim.sliceNo)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.RECORD_DIMENSIONS_DO_NOT_MATCH));
		}
		
		// insert pixel data in DB
		ImagePixelDataDAO imageDataDao = DBImageSpaceDAOFactory.getDAOFactory().getImagePixelDataDAO();
		imageDataDao.insertPixelDataForRecord(guid, imageMetaData.getX(),
				imageMetaData.getY(), imageMetaData.getZ(),
				imageMetaData.getElapsed_time(),
				imageMetaData.getExposureTime(), imageMetaData.getTimestamp(),
				dim.frameNo, dim.sliceNo, dim.channelNo, dim.siteNo);
	}
	
	/**
	 * stores raw pixel data
	 * @param actorLogin authorized user
	 * @param guid record under construction 
	 * @param dim (Frame, Slice, Channel, Site) dimension of the provided pixel data
	 * @param rawData actual raw pixel data
	 * @throws IOException
	 * @throws EnumerationException 
	 * @throws FormatException 
	 * @throws DependencyException 
	 * @throws ServiceException 
	 */
	public void storeRawData(String actorLogin, long guid, Dimension dim, PixelArray rawData) throws IOException
	{
		logger.logp(Level.INFO, "RecordCreationManager", "storeRawData", "storing raw data for guid="+guid);
		
		// transfer pixel data to storage location
		RecordBuilder rb = getRecordBuilder(guid);
		
		if(rb == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.RECORD_CREATION_REQUEST_DOES_NOT_EXIST));
		}
		
		if(rb.imageWidth != rawData.getWidth() || rb.imageHeight != rawData.getHeight() || rb.depth != rawData.getType())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.IMAGE_DIMENSIONS_DO_NOT_MATCH));
		}
		
		File parent = new File(rb.sourceFileLocation);
		parent.mkdir();
		
		// write using Bioformats tiff writer
		Exception ex = null;
		try
		{
			writeRawDataToTiff(actorLogin, guid, dim, rawData, rb);
		}
		catch (DependencyException e)
		{
			logger.logp(Level.WARNING, "RecordCreationManager", "storeRawData", "error in storing raw data for guid="+guid, e);
			ex = e;
		}
		catch (ServiceException e)
		{
			logger.logp(Level.WARNING, "RecordCreationManager", "storeRawData", "error in storing raw data for guid="+guid, e);
			ex = e;
		}
		catch (EnumerationException e)
		{
			logger.logp(Level.WARNING, "RecordCreationManager", "storeRawData", "error in storing raw data for guid="+guid, e);
			ex = e;
		}
		catch (FormatException e)
		{
			logger.logp(Level.WARNING, "RecordCreationManager", "storeRawData", "error in storing raw data for guid="+guid, e);
			ex = e;
		}
		
		if(ex!=null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.IMAGE_DIMENSIONS_DO_NOT_MATCH));
		}
		
		// write raw data
//    	String child = getFileName(dim.frameNo, dim.sliceNo, dim.channelNo, dim.siteNo);
//    	File dataFile = new File(parent, child);
//		OutputStream out = new DataOutputStream(new FileOutputStream(dataFile));
//		rawData.write(out);

		// write raw metadata file
//		File metadataFile = new File(parent, ByteArrayFormatReader.METADATA_FILE_NAME);
//		FileOutputStream fos = new FileOutputStream(metadataFile, true);
//		PrintWriter pw = new PrintWriter(fos);
//		pw.println(child);
//		pw.close();
		
		// update the list of dimensions for which pixel data is received
		updateReceivedDimensions(rb, dim);
	}
	
	private void writeRawDataToTiff(String actorLogin, long guid, Dimension dim, PixelArray rawData, RecordBuilder rb) throws DependencyException, ServiceException, EnumerationException, FormatException, IOException
	{
		logger.logp(Level.INFO, "RecordCreationManager", "writeRawDataToTiff", "guid="+guid+" pixel depth="+rawData.getType().getByteSize()+" pixel depth="+rb.depth.getByteSize());
		
		String child = getFileName(dim.frameNo, dim.sliceNo, dim.channelNo, dim.siteNo);
		File dataFile = new File(rb.sourceFileLocation, child);
		
		ImageWriter tw = new ImageWriter();
		
		ServiceFactory factory = new ServiceFactory();
		OMEXMLService service = factory.getInstance(OMEXMLService.class);
		IMetadata meta = service.createOMEXMLMetadata();

		meta.createRoot();
		
		meta.setImageID("Image:" + 0, 0);
		meta.setPixelsID("Pixels:" + 0, 0);
		
		meta.setPixelsBinDataBigEndian(Boolean.TRUE, 0, 0);

		// specify that the images are stored in ZCT order
		meta.setPixelsDimensionOrder(DimensionOrder.XYZCT,0);

		// specify that the pixel type of the images
		meta.setPixelsType(PixelType.fromString(FormatTools.getPixelTypeString(rb.depth.getByteSize())), 0);

		// specify the dimensions of the images
		meta.setPixelsSizeX(new PositiveInteger(rb.imageWidth), 0);
		meta.setPixelsSizeY(new PositiveInteger(rb.imageHeight), 0);

		meta.setPixelsSizeZ(new PositiveInteger(1), 0);
		meta.setPixelsSizeC(new PositiveInteger(1), 0);
		meta.setPixelsSizeT(new PositiveInteger(1), 0);
		
		meta.setChannelID("Channel 1", 0, 0);
		meta.setChannelName("Channel 1", 0, 0);
		meta.setChannelSamplesPerPixel(new PositiveInteger(1), 0, 0);
		
		meta.setPlaneTheC(new NonNegativeInteger(1), 0, 0);
		meta.setPlaneTheZ(new NonNegativeInteger(1), 0, 0);
		meta.setPlaneTheT(new NonNegativeInteger(1), 0, 0);
		
		tw.setMetadataRetrieve(meta);
		
		tw.setId(dataFile.getAbsolutePath());
		
		tw.saveBytes(0, rawData.getBytes());
		
		tw.close();
	}
	
	/**
	 * commits record creation process
	 * @param app application used for creating record
	 * @param actorLogin authorized user who created the record
	 * @param guid server side unique identifier for record builder
	 * @throws IOException
	 */
	public void commitRecord(Application app, String actorLogin, String accessToken, long guid) throws IOException
	{
		logger.logp(Level.INFO, "RecordCreationManager", "commitRecord", "commiting record "+guid);
		
		RecordDAO recordDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordDAO();
		ArchiveDAO archiveDao = DBImageSpaceDAOFactory.getDAOFactory().getArchiveDAO();
		
		RecordBuilder rb = getRecordBuilder(guid);
		
		// 0. perform validations
		if(rb == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.RECORD_CREATION_REQUEST_DOES_NOT_EXIST));
		}
		
		if(rb.getUnreceivedDimensions().size()!=0)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.ALL_PIXEL_DATA_NOT_RECEIVED));
		}
		
		// 1. update archive registry
		// a. compute the archive hash
		File parent = new File(rb.sourceFileLocation);
		File[] sourceFiles = parent.listFiles();
		
		BigInteger archiveSignature = Util.computeMD5Hash(sourceFiles);
		logger.logp(Level.FINEST, "RecordCreationManager", "commitRecord", "computed actual archive signature="+archiveSignature);
		
		// b. update archive signature and source references
		List<ISourceReference> sourceRef = new ArrayList<ISourceReference>();
		for(File f:sourceFiles)
		{
			ClientSourceFile sf = new ClientSourceFile(f.getName(), f.length(), f.lastModified());
			sourceRef.add(sf);
		}
		
		BigInteger dummySign = recordDao.getArchiveSignature(guid, RecordMarker.UNDER_CONSTRUCTION);
		archiveDao.updateArchiveSignature(dummySign, archiveSignature, sourceRef);
		logger.logp(Level.FINEST, "RecordCreationManager", "commitRecord", "updated archive registry for guid="+guid);
		
		// 2. update record registry
		// a. update archive signature
		recordDao.updateArchiveSignature(guid, archiveSignature);
		// b. update record marker
		recordDao.updateRecordMarker(guid, RecordMarker.Active);
		logger.logp(Level.FINEST, "RecordCreationManager", "commitRecord", "updated record registry for guid="+guid);
		
		try
		{
			// 3. insert thumbnail
			List<Integer> channelList = new ArrayList<Integer>();
			channelList.add(0);
			SysManagerFactory.getThumbnailManager().setThumbnail(actorLogin, guid, 0, 0, 0, channelList, true, false, false);
			logger.logp(Level.FINEST, "RecordCreationManager", "commitRecord", "updated thumbnail registry for guid="+guid);
			
			// 4. update navigation table
			Record r = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);

			int projectID = recordDao.getProjectID(guid);
			NavigationDAO navDao = DBImageSpaceDAOFactory.getDAOFactory().getNavigationDAO(projectID);
			navDao.registerRecord(guid,r.uploadedBy, r.numberOfSlices,r.numberOfFrames,r.getChannelCount(),r.getSiteCount(),r.imageWidth,r.imageHeight,r.uploadTime,r.sourceTime,r.creationTime,r.acquiredTime,r.imageDepth,r.getXPixelSize(), r.getYPixelSize(), r.getZPixelSize(), r.getSourceFormat(), r.imageType, r.machineIP, r.macAddress, r.sourceFolder, r.sourceFilename);
			logger.logp(Level.FINEST, "RecordCreationManager", "commitRecord", "updated navigation table for guid="+guid+" in project "+projectID);
			
			// 5. update project record count
			File sourceRoot = new File(rb.sourceFileLocation);
			long size = Util.calculateSize(sourceRoot);
			double increaseInGB = (double)size/ ((double)1024*1024*1024);
			SysManagerFactory.getProjectManager().updateProjectRecords(projectID, 1, increaseInGB);
			logger.logp(Level.FINEST, "RecordCreationManager", "commitRecord", "updated project registry "+projectID);
			
			// add history
			addHistory(guid, app, actorLogin, accessToken, HistoryType.RECORD_CREATED);
			if(rb.parentGuid != null)
			{
				// add parent
				addHistory(rb.parentGuid, app, actorLogin, accessToken, HistoryType.ADDED_CHILD_RECORD, String.valueOf(guid));
				// add child
				addHistory(guid, app, actorLogin, accessToken, HistoryType.ADDED_PARENT_RECORD, String.valueOf(rb.parentGuid));
			}
			
			// remove record builder entry
			removeRecordBuilder(guid);
		}
		catch (Exception e)
		{
			recordDao.updateRecordMarker(guid, RecordMarker.UNDER_CONSTRUCTION);
			throw new DataAccessException(e.getMessage());
		}
		
	}
	
	private void addHistory(long guid, Application app, String user, String accessToken, HistoryType type, String... args) throws DataAccessException
	{
		SysManagerFactory.getHistoryManager().insertHistory(guid, app, user, accessToken, type, type.getDescription(guid, user, args), args);
	}
	
	/**
	 * terminates record creation process cleaning up all the data
	 * @param app application used for creating record
	 * @param actorLogin authorized user who created the record
	 * @param guid server side unique identifier for record builder
	 * @throws DataAccessException 
	 */
	public void abort(Application app, String actorLogin, long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "RecordCreationManager", "abort", "aborting record building for "+guid);

		RecordBuilder rb = getRecordBuilder(guid);
		if(rb == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.RECORD_COMMITTED_OR_DO_NOT_EXIST));
		}

		RecordDAO recordDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canUpload(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		// delete image metadata
		DBImageSpaceDAOFactory.getDAOFactory().getImagePixelDataDAO().deletePixelDataForRecord(guid);
		logger.logp(Level.FINEST, "RecordCreationManager", "abort", "deleted image metadata ");
		
		// delete archive
		BigInteger sign = recordDao.getArchiveSignature(guid, RecordMarker.UNDER_CONSTRUCTION);
		
		
		SysManagerFactory.getStorageManager().deleteArchive(sign, projectID);
		logger.logp(Level.FINEST, "RecordCreationManager", "abort", "deleted archive "+guid+" "+projectID);
		
		// delete record
		recordDao.markForDeletion(guid);
		logger.logp(Level.FINEST, "RecordCreationManager", "abort", "deleted record "+guid);
		
		// remove record builder entry
		removeRecordBuilder(guid);
	}
	
	private void removeRecordBuilder(long guid) throws DataAccessException
	{
		// remove from DB
		RecordCreationDAO recordCreationDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordCreationDAO();
		recordCreationDao.deleteRecordBuilder(guid);
		
		// remove from cache
		SysManagerFactory.getCacheManager().remove(new CacheKey(guid, CacheKeyType.RecordBuilderId));
	}
	
	private RecordBuilder getRecordBuilder(long guid)
	{
		CacheKey key = new CacheKey(guid, CacheKeyType.RecordBuilderId);
		if(SysManagerFactory.getCacheManager().isCached(key)) return (RecordBuilder) SysManagerFactory.getCacheManager().get(key);
		
		try
		{
			RecordCreationDAO recordCreationDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordCreationDAO();
			RecordBuilder recordBuilder = recordCreationDao.getRecordBuilder(guid);
			
			if(recordBuilder!=null)
			{
				SysManagerFactory.getCacheManager().set(key, recordBuilder);
			}
			
			return recordBuilder;
		}
		catch(DataAccessException e)
		{
			logger.logp(Level.FINEST, "RecordCreationManager", "abort", "deleted record "+guid);
			return null;
		}
	}
	
	private void writeImgMetaData(File sourceFilesDir, String recordLabel, int numFrames, int numSlices, int numChannels, int numSites, int imageWidth, int imageHeight, PixelDepth depth, ImageType type, boolean isLittleEndian, double sizeX, double sizeY, double sizeZ, Date acquiredTime) throws IOException
	{
		RecordIdentifier id = new RecordIdentifier(recordLabel, imageWidth, imageHeight, depth, type, isLittleEndian, sizeX, sizeY, sizeZ, acquiredTime, FieldType.IGNORE);
		Set<ImageIdentifier> images = new HashSet<ImageIdentifier>();
		
		for(int f=0;f<numFrames;f++)
		{
			for(int z=0;z<numSlices;z++)
			{
				for(int c=0;c<numChannels;c++)
				{
					for(int site=0;site<numSites;site++)
					{
						ImageIdentifier img = new ImageIdentifier(getFileName(f, z, c, site), recordLabel, String.valueOf(f),
								String.valueOf(z), String.valueOf(c),
								String.valueOf(site), imageWidth, imageHeight,
								depth, type, isLittleEndian, sizeX, sizeY,
								sizeZ, 0.0, 0.0, 0.0,
								System.currentTimeMillis(),
								System.currentTimeMillis(), acquiredTime, FieldType.IGNORE);
						
						images.add(img);
					}
				}
			}
		}
		
		RecordMetaData rm = new RecordMetaData(sourceFilesDir, id, images);
//		rm.write(new File(sourceFilesDir, ByteArrayFormatReader.METADATA_FILE_NAME));
		rm.write(new File(sourceFilesDir, "metadata.img"));
	}
	
	private String getFileName(int f, int z, int c, int site)
	{
		String child = f + "_" + z + "_" + c + "_" + site + ".tiff";
		return child;
	}

	private void insertRecordBuilder(RecordBuilder rb) throws DataAccessException
	{
		// update DB
		DBImageSpaceDAOFactory.getDAOFactory().getRecordCreationDAO().insertRecordBuilder(rb.guid, rb);
		
		// update cache
		SysManagerFactory.getCacheManager().set(new CacheKey(rb.guid, CacheKeyType.RecordBuilderId), rb);
	}
	
//	private void writeMetadata(String sourceRoot, int noOfFrames, int noOfSlices, int noOfChannels, int noOfSites, int imageWidth, int imageHeight) throws FileNotFoundException
//	{
//		File sourceDir = new File(sourceRoot);
//		sourceDir.mkdir();
//		
//		File metadataFile = new File(sourceDir, ByteArrayFormatReader.METADATA_FILE_NAME);
//		PrintWriter pw = new PrintWriter(metadataFile);
//		
//		pw.println(noOfFrames);
//		pw.println(noOfSlices);
//		pw.println(noOfChannels);
//		pw.println(noOfSites);
//		pw.println(imageWidth);
//		pw.println(imageHeight);
//		
//		pw.close();
//	}
	
	private File getStorageRoot()
	{
		return SysManagerFactory.getStorageManager().getStorageRoot();
	}

	private File getArchiveRoot(File storageRoot, String uploadedBy, int projectID)
	{
		String prjStorageLocation = SysManagerFactory.getProjectManager().getProject(projectID).getLocation();
		
		File projectDir = new File(storageRoot, prjStorageLocation);
		projectDir.mkdir();
		
		File archiveRoot = new File(projectDir, Util.asciiText(uploadedBy, '_'));
		archiveRoot.mkdir();
		
		logger.logp(Level.FINE, "RecordCreationManager", "getArchiveRoot", " returning archive root "+archiveRoot.getAbsolutePath());
		
		return archiveRoot;
	}
	
	private void updateReceivedDimensions(RecordBuilder rb, Dimension dim)
	{
		rb.addReceivedDimension(dim);
		//write back the cache
		SysManagerFactory.getCacheManager().set(new CacheKey(rb.guid, CacheKeyType.RecordBuilderId), rb);
		
		RecordCreationDAO recordCreationDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordCreationDAO();
		try
		{
			logger.logp(Level.INFO, "RecordCreationManager", "updateReceivedDimensions", " adding received dimensions "+dim);
			recordCreationDao.updateReceivedDimensions(rb.guid, rb.getReceivedDimensions());
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "RecordCreationManager", "updateReceivedDimensions", " error updating received dimensions ", e);
		}
	}
	
	private void insertArchive(BigInteger archiveSignature, String rootFolder, String archiveFolder, List<ISourceReference> sourceFiles) throws DataAccessException
	{
		ArchiveDAO archiveDao = DBImageSpaceDAOFactory.getDAOFactory().getArchiveDAO();
		archiveDao.insertArchive(archiveSignature, rootFolder, archiveFolder, sourceFiles);
	}
	
	/**
	 * cleaner class to clean up the records which are not committed for long time
	 * 
	 * @author Anup Kulkarni
	 */
	private class RecordCleaner extends Thread 
	{
		/**
		 * max time in millis for which record can be idle
		 * 24 hours  
		 */
        private static final long MAX_IDLE_TIME = 24 * 60 * 60 * 1000;

		@Override
        public void run() 
        {
            logger.logp(Level.INFO, "RecordCleaner", "run", "start cleaning uncommitted records: ");
            
            List<Long> candidates = getIdleRecords();
			for(long guid:candidates)
            {
            	try
				{
					abort(new Application("Cleanup Service", "1.0"), Constants.ADMIN_USER, guid);
				}
				catch (DataAccessException e)
				{
					logger.logp(Level.WARNING, "RecordCleaner", "run", "failed cleaning uncommitted record "+guid, e);
				}
            }
            
            logger.logp(Level.INFO, "RecordCleaner", "run", "done cleaning uncommitted records: ");
        }

		private List<Long> getIdleRecords()
		{
			logger.logp(Level.INFO, "RecordCleaner", "run", "retreiving uncommitted records");
			
			List<Long> idleRecordBuilders = new ArrayList<Long>();
			RecordCreationDAO recordCreationDao = DBImageSpaceDAOFactory.getDAOFactory().getRecordCreationDAO();
			try
			{
				List<RecordBuilder> rbs = recordCreationDao.getRecordBuilders();
				long currTime = System.currentTimeMillis();
				
				for(RecordBuilder rb:rbs)
				{
					if(rb.getLastAccessTime() + MAX_IDLE_TIME < currTime)
					{
						idleRecordBuilders.add(rb.guid);
					}
				}
			}
			catch (DataAccessException e)
			{
				logger.logp(Level.WARNING, "RecordCleaner", "run", "failed retreiving uncommitted records: ", e);
			}
			
			return idleRecordBuilders;
		}
    }
}
