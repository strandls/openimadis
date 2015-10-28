package com.strandgenomics.imaging.iengine.worker;

/**
 * enum to represent different types of services which can run on worker
 * @author navneet
 *
 */
public enum ServiceType {
	
	/**
	 * movie creation service
	 */
	MOVIE_SERVICE ("Movie Creation Service"),
	
	/**
	 * record export service
	 */
	EXPORT_SERVICE ("Record Export Service"),
	
	/**
	 * record extraction service
	 */
	EXTRACTION_SERVICE ("Record Extraction Service"),
	
	/**
	 * image tiling service
	 */
	TILING_SERVICE ("Image Tiling Service"),
	
	/**
	 * reindexing service
	 */
	REINDEX_SERVICE ("Reindexing Service"),
	
	/**
	 * backup service
	 */
	BACKUP_SERVICE ("Backup Service"),
	
	/**
	 * cache cleaning service
	 */
	CACHE_CLEANING_SERVICE ("Cache Cleaning Service");
	
	private final String name;
	 
    ServiceType(String name){
        this.name = name;
    }

    public String getName(){
    	return name;
    }
}
