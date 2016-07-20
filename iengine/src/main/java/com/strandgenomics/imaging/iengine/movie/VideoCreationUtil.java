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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

/**
 * Util class to encode movies using Xuggler library
 * @author Anup Kulkarni
 *
 */
public class VideoCreationUtil {
	
	/**
	 * create movie from list of video frames honoring the elapsed time encoded in video frame. the format of the resulting movie will be MPEG-4
	 * @param inputFrames list of frames from which the movies is to be created
	 * @param imageWidth width of all the images and the resulting movie
	 * @param imageHeight height of all the images and the resulting movie
	 * @param outputDirectory directory at which resulting movie will be stored
	 * @param outputFilename name of the file
	 * @return movie file
	 * @throws IOException
	 */
	public static File createMovie(Iterator<VideoFrame> inputFrames, int imageWidth, int imageHeight, String outputDirectory, String outputFilename) throws IOException
	{
		File outputFile = new File(outputDirectory, outputFilename+".mp4"); 
		IMediaWriter writer = ToolFactory.makeWriter(outputFile.getAbsolutePath());
		
		int ret = writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, imageWidth, imageHeight);
		if(ret < 0)// failed to create
			return null;
		
		long startTime = System.nanoTime();
		
		int index = 0;
		while(inputFrames.hasNext())
		{
			System.out.println("Fetching "+index+"ith image");
			VideoFrame frame = inputFrames.next();
			
			BufferedImage image = frame.getImage();
			image = convertToType(image, BufferedImage.TYPE_3BYTE_BGR);
			
			// from frame rate compute the timestamp of the current frame inside movie
			long timeStamp = (long) frame.getElapsedTime();

			// encode the image to stream #0
			writer.encodeVideo(0, image, timeStamp, TimeUnit.NANOSECONDS);
			
			index++;
		}

		// tell the writer to close and write the trailer if needed
		writer.close();
		
		System.out.println("Movie Creation Done in "+(System.nanoTime()-startTime)+" ns");

		return outputFile;
	}
	
	/**
	 * create movie from list of images and specified frame rate. the format of the resulting movie will be MPEG-4
	 * @param inputImages list of images from which the movies is to be created
	 * @param imageWidth width of all the images and the resulting movie
	 * @param imageHeight height of all the images and the resulting movie
	 * @param frameRate frame rate in double eg. 0.1 for 10fps, 0.05 for 20fps etc
	 * @param outputDirectory directory at which resulting movie will be stored
	 * @param outputFilename name of the file
	 * @return movie file
	 * @throws IOException
	 */
	public static File createMovie(Iterator<VideoFrame> inputImages, int imageWidth, int imageHeight, double frameRate, String outputDirectory, String outputFilename) throws IOException
	{
		File outputFile = new File(outputDirectory, outputFilename+".mp4"); 
		IMediaWriter writer = ToolFactory.makeWriter(outputFile.getAbsolutePath());
		
		int ret = writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, imageWidth, imageHeight);
		if(ret < 0)// failed to create
			return null;
		
		long startTime = System.nanoTime();
		
		int index = 0;
		while(inputImages.hasNext())
		{
			System.out.println("Fetching "+index+"ith image");
			BufferedImage image = inputImages.next().getImage();
			
			image = convertToType(image, BufferedImage.TYPE_3BYTE_BGR);
			
			// from frame rate compute the timestamp of the current frame inside movie
			long timeStamp = (long) (index * frameRate * 1000 * 1000 * 1000);

			// encode the image to stream #0
			writer.encodeVideo(0, image, timeStamp, TimeUnit.NANOSECONDS);
			
			index++;
		}

		// tell the writer to close and write the trailer if needed
		writer.close();
		
		System.out.println("Movie Creation Done in "+(System.nanoTime()-startTime)+" ns");
		
		return outputFile;
	}
	
	private static BufferedImage convertToType(BufferedImage sourceImage, int targetType)
	{
		BufferedImage image;
		// if the source image is already the target type, return the source
		// image
		if (sourceImage.getType() == targetType)
		{
			image = sourceImage;
		}
		// otherwise create a new image of the target type and draw the new
		// image
		else
		{
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
	
	public static File createMovie(List<BufferedImage> inputImages, int imageWidth, int imageHeight, double frameRate, String outputDirectory, String outputFilename) throws IOException
	{
		File outputFile = new File(outputDirectory, outputFilename+".mp4"); 
		IMediaWriter writer = ToolFactory.makeWriter(outputFile.getAbsolutePath());
		
		int ret = writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, imageWidth, imageHeight);
		if(ret < 0)// failed to create
			return null;
		
		long startTime = System.nanoTime();
		
		int index = 0;
		for(int i=0;i<inputImages.size();i++)
		{
			System.out.println("Fetching "+index+"ith image");
			BufferedImage image = inputImages.get(i);
			
			// from frame rate compute the timestamp of the current frame inside movie
			long timeStamp = (long) (index * frameRate * 1000 * 1000 * 1000); 

			// encode the image to stream #0
			writer.encodeVideo(0, image, timeStamp, TimeUnit.NANOSECONDS);
			
			index++;
		}

		// tell the writer to close and write the trailer if needed
		writer.close();
		
		System.out.println("Movie Creation Done in "+(System.nanoTime()-startTime)+" ns");

		return outputFile;
	}
	
	public static void main(String[] args) throws IOException
	{
		
	}
}
