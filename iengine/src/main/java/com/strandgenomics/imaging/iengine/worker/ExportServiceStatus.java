package com.strandgenomics.imaging.iengine.worker;

/**
 * status of record export service
 * @author navneet
 *
 */
@ServiceAnnotation(type = ServiceType.EXPORT_SERVICE)
public class ExportServiceStatus implements ServiceStatus {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9139273338078567809L;
	
	@ServiceParameter(name = "Export Queue Size")
	private int exportQueueSize;

	public int getExportQueueSize() {
		return exportQueueSize;
	}

	public void setExportQueueSize(int exportQueueSize) {
		this.exportQueueSize = exportQueueSize;
	}
}
