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
