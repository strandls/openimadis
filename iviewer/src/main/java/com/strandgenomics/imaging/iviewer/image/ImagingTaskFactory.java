package com.strandgenomics.imaging.iviewer.image;

import java.util.List;
import java.util.Set;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.va.VAObject;

/**
 * creates imaging task depending upon image dimensions
 * 
 * @author Anup Kulkarni
 *
 */
public class ImagingTaskFactory {

	public static ImagingTask createTask(IRecord record, int frame, int slice, int site, Set<Integer> channels, List<VAObject> vos, boolean useChannelColor, int channelState, int sliceState,
			ImageConsumer consumer)
	{
		if(record.getImageHeight() > Constants.MAX_TILE_HEIGHT || record.getImageWidth() > Constants.MAX_TILE_WIDTH)
			return createTilingTask(record, frame, slice, site, channels, vos, useChannelColor, channelState, sliceState,  consumer);
		
		return createImagingTask(record, frame, slice, site, channels, vos, useChannelColor, channelState, sliceState,  consumer);
	}

	private static ImagingTask createImagingTask(IRecord record, int frame, int slice, int site, Set<Integer> channels, List<VAObject> vos, boolean useChannelColor, int channelState, int sliceState,
			ImageConsumer consumer)
	{
		return new ImagingTask(record, frame, slice, site, channels, vos, useChannelColor, channelState, sliceState, consumer);
	}

	private static ImagingTask createTilingTask(IRecord record, int frame, int slice, int site, Set<Integer> channels, List<VAObject> vos, boolean useChannelColor, int channelState, int sliceState,
			ImageConsumer consumer)
	{
		return new TilingTask(record, frame, slice, site, channels, vos, useChannelColor, channelState, sliceState, consumer);
	}

}
