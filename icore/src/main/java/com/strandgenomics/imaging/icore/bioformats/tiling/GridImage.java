package com.strandgenomics.imaging.icore.bioformats.tiling;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * encapsulation of images as set of tiles
 * 
 * @author Anup Kulkarni
 */
public class GridImage {
	
	/**
	 * set of images which creates this grid
	 */
	private Set<GridTile> tiles;
	
	/**
	 * width of the original image
	 */
	private int originalWidth;
	
	/**
	 * height of the original image
	 */
	private int originalHeight;
	
	public GridImage(Set<GridTile> tiles)
	{
		// save all the tiles in row major order
		TreeSet<GridTile> orderedTiles = new TreeSet<GridTile>(new Comparator<GridTile>()
		{
			@Override
			public int compare(GridTile t1, GridTile t2)
			{
				if (t1.getRow() != t2.getRow())
					return t1.getRow() - t2.getRow();
				else
					return t1.getColumn() - t2.getColumn();
			}
		});
		orderedTiles.addAll(tiles);
		
		this.tiles = orderedTiles;
	}
	
	/**
	 * returns true if all the tiles are present and have identical in width and height 
	 * @return true if all the tiles are present and have identical in width and height, false otherwise
	 */
	public boolean validate()
	{
		//TODO:
		return false;
	}
	
	/**
	 * stiches all the tiles based upon their row-col value in the grid and returns the resultant image. Resultant Image should have requested width and height
	 * @param requiredWidth width of the stiched image
	 * @param requiredHeight height of the stiched image
	 * @return stiched image from all the tiles of specified height and width
	 * @throws IOException 
	 */
	public BufferedImage getStichedImage(int requiredWidth, int requiredHeight) throws IOException
	{
		// create a empty buffered image for required width and height
		BufferedImage image = new BufferedImage(requiredWidth, requiredHeight, BufferedImage.TYPE_INT_ARGB);
		
		// get the instance of graphics 
		Graphics g = image.createGraphics();
		
		int x = 0;
		int y = 0;
		for(GridTile tile : tiles)
		{
			long stime = System.currentTimeMillis();
			
			// calculate the ratio of actual image dimensions with the requested dimensions
			double widthRatio = (getGridWidth() * 1.0) / requiredWidth;
			double heightRatio = (getGridHeight() * 1.0) / requiredHeight;
			
			// get the tile in actual size
			BufferedImage tileImage = tile.getImage();
			// resize the tile in requested size
			BufferedImage scaledTile = resizeImage(tileImage, (int)(tileImage.getWidth() / widthRatio), (int)(tileImage.getHeight() / heightRatio));
			// draw the tile to the graphics at apropriate x,y,w,h
			g.drawImage(scaledTile, x, y, scaledTile.getWidth(), scaledTile.getHeight(), null);

			System.out.println("done tile="+tile+" at "+x+" "+y);
			
			// calculate x,y,w,h for the next location
			x += scaledTile.getWidth();
			if(tile.getColumn()>=20)
			{
				x = 0;
				y+=scaledTile.getHeight();
			}
			
			long etime = System.currentTimeMillis();
		}
		
		// release the resources
		g.dispose();
		
		return image;
	}
	
	/**
	 * resizes the specified image to requested dimensions
	 * @param originalImage specified buffered image
	 * @param width 
	 * @param height
	 * @return
	 */
	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height)
    {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
     
        return resizedImage;
    }
	
	/**
	 * returns the image corresponding to requested rectangle inside the grid.
	 * this rectangle may be across the tiles.
	 * 
	 * @param x top x of the requested rectangle 
	 * @param y top y of the requested rectangle
	 * @param width of the requested rectangle
	 * @param height of the requested rectangle
	 * @return the image corresponding to requested portion inside the grid.
	 */
	public BufferedImage getArbitraryTile(int x, int y, int width, int height)
	{
		//TODO:
		return null;
	}

	/**
	 * returns the height of the grid
	 * @return the height of the grid
	 * @throws IOException 
	 */
	public int getGridHeight() throws IOException
	{
		if(this.originalHeight > 0)
			return this.originalHeight;
		
		this.originalHeight = 0;
		for(GridTile tile : tiles)
		{
			if(tile.getColumn() == 0)
			{
				this.originalHeight += tile.getImage().getHeight();
			}
		}
		
		System.out.println("original height "+this.originalHeight);
		return this.originalHeight;
	}
	
	/**
	 * returns the width of the grid
	 * @return the width of the grid
	 * @throws IOException 
	 */
	public int getGridWidth() throws IOException
	{
		if(this.originalWidth > 0)
			return this.originalWidth;
		
		this.originalWidth = 0;
		for(GridTile tile : tiles)
		{
			if(tile.getRow() == 0)
			{
				this.originalWidth += tile.getImage().getWidth();
			}
		}
		
		return this.originalWidth;
	}
}
