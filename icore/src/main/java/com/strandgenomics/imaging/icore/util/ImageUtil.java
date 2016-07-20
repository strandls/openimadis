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

package com.strandgenomics.imaging.icore.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.ImageSource;

/**
 * utility class to work on images
 * 
 * @author Anup Kulkarni
 */
public class ImageUtil {

	private static Logger logger =  Logger.getLogger("com.strandgenomics.imaging.icore.util");

	/**
	 * creates mosaic of images given list of image sources
	 * 
	 * @param images  list of image sources from which images has to be mosaiced   
	 * @param imageWidth  width of each image in mosaic
	 * @param imageHeight height of each image in mosaic       
	 * @param gap  the gap between the images   
	 * @param useChannelColor use channel color
	 *            
	 * @return mosaiced bufferedimage
	 */
	public static BufferedImage createMosaic(List<ImageSource> images, int imageWidth, int imageHeight, 
			int gap, boolean useChannelColor)
	{
		List<BufferedImage> imageList = new ArrayList<BufferedImage>();
		try
		{
			for (int i = 0; i < images.size(); i++)
			{
				imageList.add( images.get(i).getImage(useChannelColor) );
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		} 

		return createMosaicImage(imageList, imageWidth, imageHeight, gap);
	}
	
	/**
	 * creates mosaic of images given list of image sources
	 * 
	 * @param images  list of image sources from which images has to be mosaiced   
	 * @param imageWidth  width of each image in mosaic
	 * @param imageHeight height of each image in mosaic       
	 * @param gap  the gap between the images   
	 * @param useChannelColor use channel color
	 *            
	 * @return mosaiced bufferedimage
	 */
	public static BufferedImage createMosaicImage(List<BufferedImage> images, int imageWidth, int imageHeight, int gap)
	{
		int totalSize = images.size();
		int sqrt = (int) Math.sqrt(totalSize);
		
		int lowerSqr =  (sqrt * sqrt);
		int heigherSqr = (sqrt+1)*(sqrt+1);
		
		int gridX = 0;
		int gridY = 0;
		
		if ((totalSize - lowerSqr) > (heigherSqr - totalSize)) 
		{
			gridX = sqrt + 1;
			gridY = sqrt + 1;
		} 
		else if ((totalSize - lowerSqr) < (heigherSqr - totalSize))
		{
			gridY = sqrt;
			if(totalSize!=lowerSqr)
				gridX = sqrt + 1;
			else
				gridX = sqrt;
		} 
		
		int resultWidth = (imageWidth + gap) * gridX;
		int resultHeight = (imageHeight + gap) * gridY;
		
		BufferedImage resultImage = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resultImage.createGraphics();
		
		try
		{
			for (int i = 0; i < images.size(); i++)
			{
				BufferedImage img = images.get(i);

				int x = (i % gridX) * (imageWidth + gap);
				int y = (i / gridX) * (imageHeight + gap);

				graphics2D.drawImage(img, x, y, imageWidth, imageHeight, null);
			}

		}
		finally
		{
			graphics2D.dispose();
		}
		return resultImage;
	}
	
	
	public static BufferedImage createMosaicImage2(List<BufferedImage> images, int imageWidth, int imageHeight, int gap)
	{
		logger.logp(Level.INFO, "ImageUtil", "createMosaicImage2", "imageWidth"+imageWidth+"imageHeight"+imageHeight);
		
		int totalSize = images.size();
		int sqrt = (int) Math.sqrt(totalSize);
		
		int lowerSqr =  (sqrt * sqrt);
		int heigherSqr = (sqrt+1)*(sqrt+1);
		
		int gridX = 0;
		int gridY = 0;
		
		if ((totalSize - lowerSqr) > (heigherSqr - totalSize)) 
		{
			gridX = sqrt + 1;
			gridY = sqrt + 1;
		} 
		else if ((totalSize - lowerSqr) < (heigherSqr - totalSize))
		{
			gridX = sqrt;
			if(totalSize!=lowerSqr)
				gridY = sqrt + 1;
			else
				gridY = sqrt;
		} 
		
		int resultWidth = (imageWidth + gap) * gridX;
		int resultHeight = (imageHeight + gap) * gridY;
		
		double scale = (double)imageHeight/(double)resultHeight;
		
		BufferedImage resultImage = new BufferedImage((int)(resultWidth*scale), (int)(resultHeight*scale), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = resultImage.createGraphics();
		
		graphics2D.scale(scale, scale);
		
		try
		{
			for (int i = 0; i < images.size(); i++)
			{
				BufferedImage img = images.get(i);

				int x = (i % gridX) * (imageWidth + gap);
				int y = (i / gridX) * (imageHeight + gap);

				graphics2D.drawImage(img, x, y, imageWidth, imageHeight, null);
			}

		}
		finally
		{
			graphics2D.dispose();
		}
		return resultImage;
	}

	/***
	 * This method should be used instead of java ImageIO.write to gracefully
	 * handle the exceptions
	 * 
	 * @param image
	 * @param formatName
	 * @param output
	 * @throws IOException
	 */
	public static void writeImage(BufferedImage image, OutputStream output)
			throws IOException {
		PngEncoderB png = new PngEncoderB(image, PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 1);
		byte[] pngbytes;
		try {
			pngbytes = png.pngEncode();
			if (pngbytes == null) {
				throw new IOException("Error in writing png");
			} else {
				output.write(pngbytes);
			}
			output.flush();
			//output.close();
		} catch (IOException e) {
//			output.flush();
			throw e;
		}
	}

}
