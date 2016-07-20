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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.TimeUnit;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Iterator to fetch images sequentially from given record and specified configuration. This will be used for creating movies.
 * 
 * @author Anup Kulkarni
 */
public class RecordImageIterator implements Iterator<VideoFrame> {

	/**
	 * logged in user
	 */
	private final String actor;
	/**
	 * guid of record on which movie is played
	 */
	private final long guid;
	/**
	 * frame/slice of record
	 * depending upon onFrames flag this coordinate is decided to be frame or slice
	 * if onFrames is true: otherCoordinate is slice
	 * if onFrames is false: otherCoordinate is frame
	 */
	private final int otherCoordinate;
	/**
	 * movies played on frames or z slices
	 * default: movies are played on frames 
	 */
	private final boolean onFrames;
	/**
	 * site of record on which movie is played
	 */
	private final int site;
	/**
	 * selected channels
	 */
	private List<Integer> channels = null;
	/**
	 * selected overlays
	 */
	private List<String> overlays = null;
	/**
	 * true if channel color is used
	 */
	private final boolean useChannelColor;
	/**
	 * true if Z Stack is used
	 */
	private final boolean useZStack;
	/**
	 * state varible keeping count of current frame
	 */
	private int current = 0;
	/**
	 * max limit on the dimention on which movie is generated 
	 */
	private final int max;
	/**
	 * time unit used for elapased time
	 */
	private final TimeUnit elapsedTimeUnit;
	
	public RecordImageIterator(String actor, long guid, boolean onFrames, int otherCoordinate, int site, List<Integer> channels, List<String> overlays, boolean useChannelColor, boolean useZStack) throws DataAccessException
	{
		this.actor = actor;
		
		this.guid = guid;
		
		this.onFrames = onFrames;
		this.otherCoordinate = otherCoordinate;
		this.site = site;
		this.channels = channels;
		
		this.overlays = overlays;
		
		this.useChannelColor = useChannelColor;
		this.useZStack = useZStack;
		
		this.current = 0;
		
		Record record = SysManagerFactory.getRecordManager().findRecord(actor, guid);
		this.elapsedTimeUnit = record.getElapsedTimeUnit();
		if(onFrames)
		{
			max = record.numberOfFrames;
		}
		else
		{
			max = record.numberOfSlices;
		}
	}
	
	@Override
	public boolean hasNext()
	{
		return current < max;
	}

	@Override
	public VideoFrame next()
	{
		int sliceNo = 0, frameNo = 0;
		if(onFrames)
		{
			sliceNo = otherCoordinate;
			frameNo = current;
		}
		else
		{
			frameNo = otherCoordinate;
			sliceNo = current;
		}
		
		try
		{
			BufferedImage img = SysManagerFactory.getImageManager().getPixelDataOverlay(actor, guid, sliceNo, frameNo, site, channels, useChannelColor, useZStack, false, overlays, null);
			ImagePixelData metadata = SysManagerFactory.getImageManager().getImageMetaData(actor, guid, new Dimension(frameNo, sliceNo, 0, site));
			
			double elapsedTimeNanoSec = convertToNano(metadata.getElapsed_time());
				
			VideoFrame frame = new VideoFrame(img, elapsedTimeNanoSec);
			
			current++;// increment current pointer
			
			return frame;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private double convertToNano(double elapsedTime)
	{
		double elapsedTimeNS = elapsedTime;
		switch (this.elapsedTimeUnit)
		{
			case SECONDS:
				elapsedTimeNS = elapsedTime * 1000 * 1000 * 1000;
				break;
			case MILISECONDS:
				elapsedTimeNS = elapsedTime * 1000 * 1000;
				break;
			case MICROSECONDS:
				elapsedTimeNS = elapsedTime * 1000;
				break;
			case NANOSECONDS:
			default:
				break;
		}
		
		return elapsedTimeNS;
	}

	@Override
	public void remove()
	{
		// TODO Auto-generated method stub
	}

}
