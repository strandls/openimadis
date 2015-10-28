/*
 * PixelDepth.java
 *
 * AVADIS Image Management System
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
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
