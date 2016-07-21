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

package com.strandgenomics.imaging.icore;

import java.io.IOException;

import com.strandgenomics.imaging.icore.image.PixelArray;


/**
 * A tile or rectangular block within a pixel data
 * @author arunabha
 *
 */
public interface ITile extends ImageSource, Disposable {
	
	/**
	 * Returns the dimension of this pixel-data
	 * @return the frame number for this image
	 */
	public Dimension getDimension();
	
	/**
	 * Returns the X coordinate of the upper-left corner of the specified rectangular region
	 * @return the X coordinate of the upper-left corner of the specified rectangular region
	 */
	public int getX();
	
	/**
	 * Returns the Y coordinate of the upper-left corner of the specified rectangular region
	 * @return the Y coordinate of the upper-left corner of the specified rectangular region
	 */
	public int getY();
	
    /**
     * Returns the raw data associated with this image 
     * @return the raw data associated with this image 
     */
    public PixelArray getRawData() throws IOException;
}
