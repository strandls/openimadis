package com.strandgenomics.imaging.iengine.export;
/**
 * status of export request
 * 
 * @author Anup Kulkarni
 */
public enum ExportStatus {
	/**
	 * submitted to worker
	 */
	SUBMITTED,
	/**
	 * completed successfully
	 */
	SUCCESSFUL,
	/**
	 * failed to export successfully
	 */
	FAILED,
	/**
	 * terminated by user
	 */
	TERMINATED
}
