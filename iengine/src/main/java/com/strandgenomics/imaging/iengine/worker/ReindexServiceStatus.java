package com.strandgenomics.imaging.iengine.worker;

/**
 * status for reindexing service
 * @author navneet
 *
 */
@ServiceAnnotation(type = ServiceType.REINDEX_SERVICE)
public class ReindexServiceStatus implements ServiceStatus{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3017138287840717900L;
	
	@ServiceParameter(name = "Reindexing")
	private boolean isReindexing;

	public boolean isReindexing() {
		return isReindexing;
	}
	
	public String getisReindexing(){
		
		return isReindexing == true ? "Yes" : "No";
	}

	public void setReindexing(boolean isReindexing) {
		this.isReindexing = isReindexing;
	}
}
