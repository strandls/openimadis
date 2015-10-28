package com.strandgenomics.imaging.iengine.movie;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Job of this task is to generate mp4 movie from given configuration.
 * This will make use of xuggler library for encoding images to video. 
 * The elapsed time will be read from record metadata and will be honored.
 * 
 * @author Anup Kulkarni
 */
public class VideoMovieCreationTask implements Callable <Void>{
	
	/**
	 * movie for which video will be generated
	 */
	private MovieTicket movie; 
	/**
	 * logged in user
	 */
	private String actor;
	/**
	 * logger
	 */
	private Logger logger;
	private String storage_dir;
	
	private Record record;
	
	public VideoMovieCreationTask(String actor, MovieTicket ticket)
	{
		System.out.println("video creation task");
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		this.actor = actor;
		this.movie = ticket;
		
		storage_dir = movie.createStorageDirectory().getAbsolutePath();
	}
	
	@Override
	public Void call() throws Exception
	{
		logger.logp(Level.INFO, "VideoMovieCreationTask", "run", "prefetching images for movie");

		System.out.println("starting movie creation");
		try
		{
			if(movie.isDone()) return null;
			
			for(int i=0;i<movie.maxCoordinate;i++)
				movie.setGenerated(i);

			// read record width and height
			record = SysManagerFactory.getRecordManager().findRecord(actor, movie.guid);
			int imageWidth = record.imageWidth;
			int imageHeight = record.imageHeight;

			RecordImageIterator it = new RecordImageIterator(actor, movie.guid, movie.onFrames, movie.otherCoordinate, movie.site, movie.getChannels(), movie.getOverlays(), movie.useChannelColor, movie.useZStack);
			
			File videoFile;
			if(movie.getFPS() != null)
			{
				logger.logp(Level.INFO, "VideoMovieCreationTask", "run", "fps="+(1.0/movie.getFPS()));
				videoFile = VideoCreationUtil.createMovie(it, imageWidth, imageHeight, 1.0/movie.getFPS(), storage_dir, movie.ID+"");
			}
			else
			{
				videoFile = VideoCreationUtil.createMovie(it, imageWidth, imageHeight, storage_dir, movie.ID+"");
			}
			
			// put in DB
			SysManagerFactory.getMovieManager().insertImage(movie.getMovieid(), 0, videoFile.getAbsolutePath());
		}
		catch (Throwable e)
		{
			logger.logp(Level.WARNING, "VideoMovieCreationTask", "call", " error while creating images for movie "+movie.getMovieid(),e);
		}
		return null;
	}
}
