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

import javax.imageio.ImageIO;


/**
 * This class represents individual tile in an image.
 * 
 * @author Anup Kulkarni
 */
public class GridTile {
	/**
	 * file containing image for this tile
	 */
	private String filePath;
	/**
	 * row number of this tile in entire grid, start = 0
	 */
	private int row;
	/**
	 * column number of this tile in entire grid, start = 0 
	 */
	private int column;
	
	public GridTile(String path, int row, int column)
	{
		this.filePath = path;
		this.row = row;
		this.column = column;
	}

	/**
	 * returns image for this path
	 * @return image for this path
	 * @throws IOException
	 */
	public BufferedImage getImage() throws IOException
	{
		return ImageIO.read(new File(filePath));
	}

	/**
	 * returns the row number in the grid
	 * @return the row number in the grid
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * returns the column number in the grid
	 * @return the column number in the grid
	 */
	public int getColumn()
	{
		return column;
	}
	
	@Override
	public String toString()
	{
		return "row="+row+",col="+column;
	}
}
