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
