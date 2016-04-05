package org.openslide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;




import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;

public class BigTileTest {
	private final int MAX_ZOOM_LEVEL_FOR_PREFETCHING = 10;
	
	private final int MIN_ZOOM_LEVEL_FOR_PREFETCHING = 4 ;

	public static void main(String[] args) throws FormatException, IOException {
		// TODO Auto-generated method stub
//		BufferedImageReader br = new BufferedImageReader();
//		br.setId("/home/ravikiran/curie_Data/tumour/Normal_004.tif");
//		BufferedImage bi = br.openImage(0, 60000, 150000, 2000, 2000);
//		System.out.println(""+br.getSizeX() + "  "+ br.getSizeY());
//		ImageIO.write(bi, "jpg", new File("/home/ravikiran/out.jpeg"));
		//doTiling(br);
		//br.close();
		BufferedImage bi = ImageIO.read(new File("/home/ravikiran/Pictures/4k_wallpaper_231.jpg"));
		long startTime = System.currentTimeMillis();
		BufferedImage res = scaleImage0(bi, 25, 25);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime);
		
		ImageIO.write(res, "jpg", new File("/home/ravikiran/out0.jpeg"));
	}
	public static void doTiling(BufferedImageReader br) throws IOException
	{	
		
		
		
		prepareImages(br.getSizeX(),br.getSizeY());
		
		long allImagesSize = 0;
		long nTiles = 0; 
		int count = 0;
		long totalTime = 0;
	
		for(int x=0; x < br.getSizeX() ;x+=5000)
		{
			for(int y=0;y<br.getSizeY();y+=5000)
			{
				nTiles++;
			}
		}
		
		for(int x=0; x < br.getSizeX() ;x+=5000)
		{
			for(int y=0;y<br.getSizeY();y+=5000)
			{
				System.out.println("Doing for startx="+x+" starty"+y);
				// find out the bounds
				int tileWidth = (x + 5000) < br.getSizeX() ? 5000 : (br.getSizeX() - x);
				int tileHeight = (y + 5000) < br.getSizeY() ? 5000 : (br.getSizeY() - y);
				System.out.println("tile dimension "+tileWidth+" "+tileHeight);
				
				// get the image in original scale
				BufferedImage image = null;
				try {
					image = br.openImage(0,x, y, tileWidth, tileHeight);
				} catch (FormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("fetched tile for x="+x+" y="+y);
				
				count++;
				allImagesSize = 0;
				int zoomCount = 0;
				totalTime = 0;
				for (int zoom = 5; zoom >= 5; zoom--)
				{
					zoomCount++;
					
					System.out.println("for zoom="+zoom);
					int scalingFactor = (int)Math.pow(2, zoom);
					
					int scaledTileWidth = (int)Math.ceil((double)tileWidth / scalingFactor);
					int scaledTileHeight = (int) Math.ceil((double)tileHeight / scalingFactor);
					
					int scaledTopX = x / scalingFactor;
					int scaledTopY = y / scalingFactor;
					
					//BufferedImage scaledImage = Util.resizeImage(image, scaledTileWidth, scaledTileHeight);
					//writeImage(scaledImage, scaledTopX, scaledTopY, zoom);
					
					BufferedImage imageForZoom = getScaledImageForZoom(zoom);
					Graphics gfx = imageForZoom.getGraphics();
					gfx.drawImage(image, scaledTopX, scaledTopY, scaledTopX+scaledTileWidth, scaledTopY+scaledTileHeight, 0, 0, tileWidth, tileHeight, null);
					gfx.dispose();

					writeImage(imageForZoom, zoom);
					break;
					
	
					
				}
			}
		}
		
		
	}
	private static BufferedImage getScaledImageForZoom(int zoom) throws IOException
	{
		File outputFile = new File(new File("/home/ravikiran/TestTile"), String.valueOf(zoom)+".png");
		return ImageIO.read(outputFile);
	}
	
	private static void writeImage(BufferedImage image, int zoom) throws IOException
	{
		File outputFile = new File(new File("/home/ravikiran/TestTile"), String.valueOf(zoom)+".png");
		ImageIO.write(image, "png", outputFile);
	}
	
	private static void prepareImages(int width,int height) throws IOException
	{
		for (int zoom = 5; zoom >= 5; zoom--)
		{
			int scalingFactor = (int)Math.pow(2, zoom);
			
			int scaledRecordWidth = width / scalingFactor;
			int scaledRecordHeight = height / scalingFactor;
			
			BufferedImage scaledImage = new BufferedImage(scaledRecordWidth, scaledRecordHeight, BufferedImage.TYPE_INT_RGB);
			File outputFile = new File(new File("/home/ravikiran/TestTile"), String.valueOf(zoom)+".png");
			ImageIO.write(scaledImage, "png", outputFile);
		}
	}
	private static BufferedImage scaleImage0(BufferedImage img, int scaledWidth, int scaledHeight){
		
		BufferedImage tmp = Scalr.resize(img, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT,
				scaledWidth, scaledHeight, Scalr.OP_ANTIALIAS);
		return tmp;
	}
	private static BufferedImage scaleImage1(BufferedImage img, int scaledWidth, int scaledHeight){
		
		BufferedImage tmp = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = tmp.getGraphics();
		g.drawImage(img, 0,0,scaledWidth, scaledHeight, null);
		return tmp;
	}
	private static BufferedImage scaleImage(BufferedImage img, int scaledWidth, int scaledHeight){
		
		BufferedImage tmp = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = tmp.getGraphics();
		g.drawImage(img, 0,0,scaledWidth, scaledHeight, null);
		return tmp;
	}
}
