/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
