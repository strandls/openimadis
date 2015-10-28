package com.strandgenomics.imaging.iengine.worker;

/**
 * status for CacheCleaningService
 * @author navneet
 *
 */
@ServiceAnnotation(type = ServiceType.CACHE_CLEANING_SERVICE)
public class CacheCleaningServiceStatus implements ServiceStatus {

	public boolean isCleaning() {
		return isCleaning;
	}
	
	public String getisCleaning() {
		return isCleaning == true ? "Yes" : "No";
	}

	public void setCleaning(boolean isCleaning) {
		this.isCleaning = isCleaning;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5657034139567244397L;
	
	@ServiceParameter(name = "Cache cleaning")
	private boolean isCleaning;

}
