package com.strandgenomics.imaging.icore.bioformats.tiling;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class GridTester {
	public static void main(String[] args) throws IOException
	{
		Set<GridTile> tiles = new HashSet<GridTile>();
		
		File dir = new File("C:\\test\\tile_images");
		File[] files = dir.listFiles();
		for(File f:files)
		{
			String filePath = f.getAbsolutePath();

			String name = f.getName();
			String tokens[] = name.split("\\.")[0].split("_");
			int row = Integer.parseInt(tokens[0]);
			int col = Integer.parseInt(tokens[1]);
			
			GridTile tile = new GridTile(filePath, row, col);
			
			tiles.add(tile);
		}
		
		System.out.println(tiles.size());
		
		GridImage gridImage = new GridImage(tiles);
		
		BufferedImage image = gridImage.getStichedImage(20992, 12544);
		
		long startTime = System.currentTimeMillis();
		ImageIO.write(image, "png", new File("C:\\test\\stichedimage.png"));
		long endTime = System.currentTimeMillis();
		
		System.out.println("Dont stiching in "+(endTime-startTime)+"ms");
	}
}
