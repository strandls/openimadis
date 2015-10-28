package com.strandgenomics.imaging.iengine.worker;

/**
 * class for status of movie creation service
 * @author navneet
 *
 */
@ServiceAnnotation(type = ServiceType.MOVIE_SERVICE)
public class MovieServiceStatus implements ServiceStatus{

	/**
	 * 
	 */
	private static final long serialVersionUID = 803527278892534035L;
	
//	@Override
//	public boolean isAlive() throws DataAccessException {
//		
//		long currentTime = System.currentTimeMillis();
//		long lastModifiedTimes = ImageSpaceDAOFactory.getDAOFactory().getWorkerDAO().getLastModificationTime(serviceType);
//		
//		if(Math.abs(currentTime-lastModifiedTimes)>=ServiceMonitor.period)
//			return false;
//		
//		return true;
//		
//	}
	
	@ServiceParameter(name = "Movie Creation Queue")
	private int movieCreatorQueueSize;
	
	@ServiceParameter(name = "Image Prefetching Queue")
	private int imagePrefetchersQueueSize;

	public int getMovieCreatorQueueSize() {
		return movieCreatorQueueSize;
	}

	public void setMovieCreatorQueueSize(int movieCreatorQueueSize) {
		this.movieCreatorQueueSize = movieCreatorQueueSize;
	}

	public int getImagePrefetchersQueueSize() {
		return imagePrefetchersQueueSize;
	}

	public void setImagePrefetchersQueueSize(int imagePrefetchersQueueSize) {
		this.imagePrefetchersQueueSize = imagePrefetchersQueueSize;
	}
	
}
