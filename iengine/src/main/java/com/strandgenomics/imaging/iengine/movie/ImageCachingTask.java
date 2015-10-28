package com.strandgenomics.imaging.iengine.movie;

import java.util.concurrent.Callable;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * class for creating image cache for the record
 * 
 * @author Anup Kulkarni
 */
public class ImageCachingTask implements Callable<Void> {
	/**
	 * request for prefetching the raw images
	 */
	private CacheRequest request;
	/**
	 *  login of the requesting user
	 */
	private String actor;
	/**
	 * starting index
	 */
	private int startingIndex;
	
	public ImageCachingTask(String actor, CacheRequest request, int startingIndex)
	{
		this.request = request;
		this.actor = actor;
		this.startingIndex = startingIndex;
	}

	@Override
	public Void call() throws Exception
	{
		try
		{
			int limit = (startingIndex+10) >= request.maxCoordinate ? request.maxCoordinate : (startingIndex+10);
			for(int index = startingIndex;index<limit;index++)
			{
				if(request.isFrameGenerated(index))
				{
					continue;
				}
				
				int slice = index;
				int frame = request.otherCoordinate;
				if (request.isOnFrames())
				{
					frame = index;
					slice = request.otherCoordinate;
				}

				for(int channel: request.getChannels())
				{
					Dimension dim = new Dimension(frame, slice, channel, request.site);
					SysManagerFactory.getImageManager().getRawData(actor, request.guid, dim, null);
				}
				
				request.setGenerated(index);
			}
		}
		catch (Exception e)
		{}
		
		return null;
	}

}
