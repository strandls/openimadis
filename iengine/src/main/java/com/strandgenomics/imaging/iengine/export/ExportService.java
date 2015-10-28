package com.strandgenomics.imaging.iengine.export;

import java.io.IOException;
import java.rmi.Remote;
/**
 * service used to extract records
 * 
 * @author Anup Kulkarni
 */
public interface ExportService extends Remote 
{
	/**
	 * submit record export request
	 * @param request specified record export request
	 * @throws IOException
	 */
	public void submitExportRequest(ExportRequest request) throws IOException;
}
