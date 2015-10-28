package com.strandgenomics.imaging.iengine.export;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * identifies export request for worker
 * 
 * @author Anup Kulkarni
 */
public class RecordExportTask  implements Callable <Void> {
	/**
	 * unique identifier for export request
	 */
	public final long requestId;
	/**
	 * specified record
	 */
	private List<Long> guids;
	/**
	 * format in which record is exported
	 */
	public final ExportFormat format;
	/**
	 * validity time of the exported record
	 */
	public final long validTill;
	/**
	 * user who submitted record export request
	 */
	public final String submittedBy;
	/**
	 * name of export
	 */
	public final String exportName;
	
	private Logger logger;
	
	public RecordExportTask(long requestId, String submittedBy, List<Long> guids, ExportFormat format, long validTill, String exportName)
	{
		this.requestId = requestId;
		this.guids = guids;
		this.format = format;
		this.validTill = validTill;
		this.submittedBy = submittedBy;
		this.exportName = exportName;
		
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
	}
	
	@Override
	public Void call()
	{
		// check if request is terminated
		logger.logp(Level.INFO, "RecordExportTask", "run", "task="+requestId+"is submitted");
		try
		{
			if(SysManagerFactory.getExportManager().getExportRequestStatus(requestId) == ExportStatus.TERMINATED)
			{
				logger.logp(Level.INFO, "RecordExportTask", "run", "task="+requestId+"is terminated");
				return null;
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		
		ExportStatus status = ExportStatus.SUCCESSFUL;
		try
		{
			logger.logp(Level.INFO, "RecordExportTask", "run", "submitting task="+requestId+" format="+format);
			if(ExportFormat.OME_TIFF_FORMAT.equals(format))
			{
				// export as OME-XML
				exportAsOMEXML(requestId, submittedBy, guids);
			}
			else if(ExportFormat.ORIGINAL_FORMAT.equals(format))
			{
				// export in original format
				exportOriginalFormat(requestId, submittedBy, guids);
			}
		}
		catch(IOException e)
		{
			logger.logp(Level.WARNING, "RecordExportTask", "run", " task="+requestId+" failed", e);
			status = ExportStatus.FAILED;
		}
		
		try
		{
			SysManagerFactory.getExportManager().setExportRequestStatus(requestId, status);
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private File getUserRoot(String user)
	{
		File exportRoot = new File(Constants.getStringProperty(Property.EXPORT_STORAGE_LOCATION, null));
		exportRoot.mkdir();
		
		File userRoot = new File(exportRoot, user);
		userRoot.mkdir();
		return userRoot;
	}
	
	private void exportOriginalFormat(long requestId, String actorLogin, List<Long> guids) throws DataAccessException, IOException
	{
		ArchiveExporter exporter = new ArchiveExporter();
		String filepath = exporter.export(actorLogin, guids, requestId, exportName, getUserRoot(actorLogin).getAbsolutePath());
		
		// update the db
		SysManagerFactory.getExportManager().setExportLocation(requestId, filepath);
	}

	private void exportAsOMEXML(long requestId, String actorLogin, List<Long> guids) throws IOException
	{
		// export the record as ome xml
		OMEExporter exporter = new OMEExporter();
		String filepath = exporter.export(actorLogin, guids, requestId, exportName, getUserRoot(actorLogin).getAbsolutePath());
		
		// update the db
		SysManagerFactory.getExportManager().setExportLocation(requestId, filepath);
	}
}
