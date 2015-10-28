package com.strandgenomics.imaging.iengine.movie;

import java.util.concurrent.Callable;

/**
 * Creating movie creation task depending on MovieType
 * 
 * @author Anup Kulkarni
 */
public class MovieTaskFactory {
	
	/**
	 * creates appropriate task for movie generation based upon movie specification
	 * @param actor login of the requesting user
	 * @param movie movie specification
	 * @param startingIndex image index from which movie generation is to be started
	 * @return appropriate movie task
	 */
	public static Callable<Void> createTask(String actor, MovieTicket movie, int startingIndex)
	{
		Callable<Void> task = null;
		switch (movie.type)
		{
			case VIDEO:
				task = new VideoMovieCreationTask(actor, movie);
				break;
			case PREFETCHED_IMAGES:
				task = new FrameMovieCreationTask(actor, movie, startingIndex);
				break;
			case CACHE_IMAGE_DATA:
				if(movie instanceof CacheRequest)
					task = new ImageCachingTask(actor, (CacheRequest) movie, startingIndex);
				break;
		}
		return task;
	}

}
