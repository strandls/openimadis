package com.strandgenomics.imaging.iengine.worker;

/**
 * status for extraction service
 * @author navneet
 *
 */
@ServiceAnnotation(type = ServiceType.EXTRACTION_SERVICE)
public class ExtractionServiceStatus implements ServiceStatus {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6487509706406375231L;
	
	@ServiceParameter(name = "Record Extraction Queue Size")
	private int recordExtractionQueueSize;

	public int getRecordExtractionQueueSize() {
		return recordExtractionQueueSize;
	}

	public void setRecordExtractionQueueSize(int recordExtractionQueueSize) {
		this.recordExtractionQueueSize = recordExtractionQueueSize;
	}
}
