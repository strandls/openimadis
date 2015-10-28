package com.strandgenomics.imaging.icore.bioformats;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import loci.formats.ChannelSeparator;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.image.PixelArray;

/**
 * Testing the bioformats for handling huge images(~ 60k * 60k)
 * 
 * @author Anup Kulkarni
 */
public class BioformatsTiffTest {
	
	/**
	 * Initialize a Bioformats reader by setting a filepath
	 * @param file the file source of records
	 * @return the image reader instance
	 */
	private static BufferedImageReader createImageReader(File file)
	{
		ChannelSeparator separator = new ChannelSeparator();
		BufferedImageReader imageReader = new BufferedImageReader(separator);

		// initialize the file
		try
		{
			imageReader.setId(file.getAbsolutePath());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return imageReader;
	}
	
	private static BufferedImage readImage(BufferedImageReader reader, Dimension d, Rectangle rect)
	{
		reader.setSeries(d.siteNo);
		
		int index = reader.getIndex(d.sliceNo, d.channelNo, d.frameNo);
		
		BufferedImage image = null;
		try
		{
			image = reader.openImage(index, rect.x, rect.y, rect.width, rect.height);
		}
		catch (FormatException fe)
		{
			fe.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		return image;
	}
	
	private static BufferedImage readImage(BufferedImageReader reader, Dimension d)
	{
		System.out.println("Image Width="+reader.getSizeX());
		System.out.println("Image Height="+reader.getSizeY());
		
		return readImage(reader, d, new Rectangle(0, 0, reader.getSizeX(), reader.getSizeY()));
	}
	
	private static BufferedImage readImage(File file, int series, int slice, int frame, int channel)
	{
		BufferedImageReader imageReader = createImageReader(file);
		
		Dimension d = new Dimension(frame, slice, channel, series);
		
		return readImage(imageReader, d);
	}

	public static void main(String[] args) throws IOException
	{
		File file = new File("C:\\test\\FMG_344.tiff");
		BufferedImageReader imageReader = createImageReader(file);
		
		File tileDir = new File("C:\\test\\tile_images_test");
		tileDir.mkdirs();
		
		int tileWidth = 4096;
		int tileHeight = 4096;
		
		long stime = System.currentTimeMillis();
		for(int i=0;i<imageReader.getSizeX();i+=tileWidth)
		{
			for(int j=0;j<imageReader.getSizeY();j+=tileHeight)
			{
				System.out.println("Doing image i="+i+" j="+j);
				
				long s = System.currentTimeMillis();
				
				int tw = tileWidth;
				int th = tileHeight;
				if(i+tileWidth>imageReader.getSizeX())
				{
					tw = imageReader.getSizeX() - i;
				}
				if(j+tileWidth>imageReader.getSizeY())
				{
					th = imageReader.getSizeY() - j;
				}

				int row = i / tileWidth;
				int column = j / tileHeight;
				
				BufferedImage image = readImage(imageReader, new Dimension(0,0,0,0), new Rectangle(i, j, tw, th));
				
				long e = System.currentTimeMillis();
				System.out.println("Tile read in "+(e-s)+" ms");

				File tile = new File(tileDir.getAbsolutePath(), column+"_"+row+".zip");
//				ImageIO.write(image, "jpg", tile);
				PixelArray arr = PixelArray.toPixelArray(image);
				
				GZIPOutputStream zipOs = new GZIPOutputStream(new FileOutputStream(tile));
				ObjectOutputStream oos = new ObjectOutputStream(zipOs);
				oos.writeObject(arr);
				oos.close();
				
				long e2 = System.currentTimeMillis();
				System.out.println("Tile done in "+(e2-s)+" ms");
			}
		}
		long etime = System.currentTimeMillis();
		
		System.out.println("Tile done in "+(etime-stime)+" ms");
	}
}
