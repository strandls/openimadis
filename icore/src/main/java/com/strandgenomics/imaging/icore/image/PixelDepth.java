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

package com.strandgenomics.imaging.icore.image;

/**
 * Represents the quality or depth of pixel data.
 * The total number of bits per pixel or bit depth per pixel
 * @author arunabha
 */
public enum PixelDepth {
	
	INT(4) {
		
		public String toString()
		{
			return "32 Bits";
		}
	}, 
	
	SHORT(2) {
		
		public String toString()
		{
			return "16 Bits";
		}
	},
	
	BYTE(1) {
		
		public String toString()
		{
			return "8 Bits";
		}
	}; 
	
	private int size;

	private PixelDepth(int c) 
	{
		size = c;
	}

	/**
	 * returns number of bytes for the pixel depth
	 * @return number of bytes for the pixel depth
	 */
	public int getByteSize() 
	{
		return size;
	}
	
	/**
	 * returns number of bits for the pixel depth
	 * @return number of bits for the pixel depth
	 */
	public int getBitSize() 
	{
		return size * 8;
	}
	
	/**
	 * returns pixel depth object for byte
	 * @param depth in bytes
	 * @return pixel depth object for specified depth in byte
	 */
	public static final PixelDepth toPixelDepth(int depth)
	{
		switch(depth)
		{
			case 1: return BYTE;
			case 2: return SHORT;
			case 4: return INT;
		}
		throw new IllegalArgumentException("unknown depth "+depth);
	}
}
