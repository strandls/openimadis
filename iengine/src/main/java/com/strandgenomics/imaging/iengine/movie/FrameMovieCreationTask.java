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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Emulates movies as image frames. Idea is all the images will be prefetched
 * and played one by one to emulate movie. The job of this task is to prefetch
 * the images
 * 
 * @author Anup Kulkarni
 */
public class FrameMovieCreationTask implements Callable <Void>{
	/**
	 * movie for which images are to be prefetched
	 */
	private MovieTicket movie; 
	/**
	 * logged in user
	 */
	private String actor;
	/**
	 * the value of the start of movie
	 */
	private int start = 0;
	/**
	 * logger
	 */
	private Logger logger;
	/**
	 * directory where the movie images will be stored
	 */
	private String storage_dir;
	/**
	 * dividing movie creation task in list of subtasks
	 */
	private final int noOfThreads = 4;
	/**
	 * counting the threads
	 */
	private int threadNo = 0;
	/**
	 * for synchronizing among the threads
	 */
	private CountDownLatch doneSignal = new CountDownLatch(noOfThreads);
	
	public FrameMovieCreationTask(String actor, MovieTicket movie, int start)
	{
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		storage_dir = Constants.getStringProperty(Property.MOVIE_STORAGE_LOCATION, null);
		
		this.actor = actor;
		this.movie = movie;
		this.start = start;
	}
	
	@Override
	public Void call() throws Exception
	{
		logger.logp(Level.FINEST, "MoviePrefetchingTask", "run", "prefetching images for movie");

		if(movie.isDone()) return null;

		try
		{
			for(threadNo=0;threadNo<noOfThreads;threadNo++)
			{
				Thread t = new Thread(new ParallalWorker(start, movie.maxCoordinate, threadNo, doneSignal));
				t.start();
			}
			
			// wait for all the threads to finish
			doneSignal.await();
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "MoviePrefetchingTask", "call", " error while creating images for movie "+movie.getMovieid(),e);
		}
		return null;
	}
	
	/**
	 * starts prefetching of images from specified slice/frame
	 * @param movie specified movie
	 * @param initialPosition initial value of slice/frame
	 * @throws IOException 
	 */
	public void generateImageForFrame(int imageCoordinate) throws IOException
	{
		int sliceNo = 0;
		int frameNo = 0;
		if(movie.isOnFrames())
		{
			sliceNo = movie.getFixedCoordinate();
			frameNo  = imageCoordinate; 
		}
		else
		{
			frameNo = movie.getFixedCoordinate();
			sliceNo  = imageCoordinate;
		}
		BufferedImage img = SysManagerFactory.getImageManager().getPixelDataOverlay(actor, movie.getGuid(), sliceNo, frameNo, movie.getSite(), movie.getChannels(), movie.isColored(), movie.useZStack, false, movie.getOverlays(),null);
		
		String path = storeImage(movie.getMovieid(), imageCoordinate, img);
		
		if(path!=null)
			SysManagerFactory.getMovieManager().insertImage(movie.getMovieid(), imageCoordinate, path);
	}

	private String storeImage(long movieid, int index, BufferedImage img) throws IOException
	{
		File movieStorage = new File(storage_dir, String.valueOf(movieid));
		File movieFile = new File(movieStorage, String.valueOf(index)+".png");
		
		if(movieFile.isFile()) // file already exists
			return null;
		
		ImageIO.write(img, "png", movieFile);
		
		return movieFile.getAbsolutePath();
	}
	
	/**
	 * Create multiple sub tasks which will run parallelly
	 * 
	 * @author Anup Kulkarni
	 */
	private class ParallalWorker implements Runnable
	{
		private int threadId; 
		private int start;
		private int end;
		private CountDownLatch doneSignal;
		
		public ParallalWorker(int start, int end, int threadNo, CountDownLatch signal)
		{
			this.threadId = threadNo;
			this.start = start;
			this.end = end;
			this.doneSignal = signal;
		}
		
		@Override
		public void run()
		{
			for(;this.start < this.end;this.start++)
			{
				if (this.start % noOfThreads != this.threadId) // threading
					continue;
				
				if(!movie.isFrameGenerated(start))
				{
					try
					{
						movie.setGenerated(start);
						generateImageForFrame(start);
					}
					catch(Exception ex)
					{
						movie.setNotGenerated(start);
						logger.logp(Level.WARNING, "MoviePrefetchingTask", "call", " error while creating movie frame# "+start+" for movie "+movie.guid,ex);
					}
				}
			}
			
			this.doneSignal.countDown();
		}
	}
}
