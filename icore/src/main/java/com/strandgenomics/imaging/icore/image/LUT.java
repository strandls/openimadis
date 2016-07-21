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

import java.awt.Color;
import java.awt.image.IndexColorModel;

/** 
 * This is an indexed color model that allows an
 * lower and upper bound to be specified. 
 * this is actually imageJ LUT in package ij.process
 */
public class LUT extends IndexColorModel {
	
    public double min, max;

	public LUT(byte r[], byte g[], byte b[]) 
	{
		this(8, 256, r, g, b);
	}

	public LUT(int bits, int size, byte r[], byte g[], byte b[])
	{
		super(bits, size, r, g, b);
	}

	public LUT(IndexColorModel cm, double min, double max) 
	{
		super(8, cm.getMapSize(), getReds(cm), getGreens(cm), getBlues(cm));
		this.min = min;
		this.max = max;
	}

	static byte[] getReds(IndexColorModel cm)
	{
		byte[] reds=new byte[256]; 
		cm.getReds(reds); 
		return reds;
	}

	static byte[] getGreens(IndexColorModel cm)
	{
		byte[] greens=new byte[256]; 
		cm.getGreens(greens); 
		return greens;
	}

	static byte[] getBlues(IndexColorModel cm)
	{
		byte[] blues=new byte[256]; 
		cm.getBlues(blues); 
		return blues;
	}

	public byte[] getBytes()
	{
		int size = getMapSize();
		if (size!=256) return null;
		byte[] bytes = new byte[256*3];
		for (int i=0; i<256; i++) bytes[i] = (byte)getRed(i);
		for (int i=0; i<256; i++) bytes[256+i] = (byte)getGreen(i);
		for (int i=0; i<256; i++) bytes[512+i] = (byte)getBlue(i);
		return bytes;
	}

	public LUT createInvertedLut()
	{
		int mapSize = getMapSize();
		byte[] reds = new byte[mapSize];
		byte[] greens = new byte[mapSize];
		byte[] blues = new byte[mapSize];	
		
		byte[] reds2 = new byte[mapSize];
		byte[] greens2 = new byte[mapSize];
		byte[] blues2 = new byte[mapSize];	
		
		getReds(reds); 
		getGreens(greens); 
		getBlues(blues);
		
		for (int i=0; i<mapSize; i++) 
		{
			reds2[i] = (byte)(reds[mapSize-i-1]&255);
			greens2[i] = (byte)(greens[mapSize-i-1]&255);
			blues2[i] = (byte)(blues[mapSize-i-1]&255);
		}
		return new LUT(8, mapSize, reds2, greens2, blues2);
	}

	/** 
	 * Creates a color LUT from a Color. 
	 */
	public static LUT createLutFromColor(Color color)
	{
		byte[] rLut = new byte[256];
		byte[] gLut = new byte[256];
		byte[] bLut = new byte[256];
		
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		
		double rIncr = ((double)red)/255d;
		double gIncr = ((double)green)/255d;
		double bIncr = ((double)blue)/255d;
		
		for (int i=0; i<256; ++i) 
		{
			rLut[i] = (byte)(i*rIncr);
			gLut[i] = (byte)(i*gIncr);
			bLut[i] = (byte)(i*bIncr);
		}
		return new LUT(rLut, gLut, bLut);
	}
}
