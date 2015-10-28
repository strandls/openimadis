package com.strandgenomics.imaging.iengine.worker;

/**
 * status of tiling service
 * @author navneet
 *
 */
@ServiceAnnotation(type = ServiceType.TILING_SERVICE)
public class TilingServiceStatus implements ServiceStatus{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8216687434688851990L;
	
	@ServiceParameter(name = "Image Tiling Queue Size")
	private int tilingServiceQueueSize;
	
	public int getTilingServiceQueueSize() {
		return tilingServiceQueueSize;
	}

	public void setTilingServiceQueueSize(int tilingServiceQueueSize) {
		this.tilingServiceQueueSize = tilingServiceQueueSize;
	}
}
