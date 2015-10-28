package com.strandgenomics.imaging.iengine.movie;
/**
 * defines the type of the movie
 * 
 * @author Anup Kulkarni
 */
public enum MovieType {
	/**
	 * movie will be simulated from series of prefetched images
	 */
	PREFETCHED_IMAGES,
	/**
	 * actual video will be created 
	 */
	VIDEO,
	/**
	 * for the purpose of caching the image data
	 */
	CACHE_IMAGE_DATA,
}
