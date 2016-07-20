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

/**
 * Histogram.java
 *
 * Project imaging
 * Module com.strandgenomics.imaging.indexaccess
 *
 * Copyright 2009-2010 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cern.colt.list.IntArrayList;
import cern.colt.map.OpenIntIntHashMap;

/**
 * The distribution of intensity values across the pixels within a PixelArray or Image
 * @author arunabha
 *
 */
public class Histogram {

	/**
	 * Maximum intensity value
	 */
    private final int maxValue;
    /**
     * Minimum intensity value
     */
    private final int minValue;
    /**
     * list of all intensity values having non zero frequencies
     */
    private final int[] intensities;
    /**
     * frequency distribution for the corresponding intensities
     */
    private final int[] frequencies;
    /**
     * maximum frequency
     */
    private final int maxFreq;
    /**
     * pixel depth
     */
    public final PixelDepth pixelDepth;
    
    /**
     * captures the intensity distribution of pixel-array
     * @param type type of the image
     * @param intensities intensity values
     * @param frequencies frequencies for corresponding intensity values  
     * @param minValue maximum intensity value
     * @param maxValue minimum intensity value
     * @param maxFreq  maximum frequency 
     */
    public Histogram(PixelDepth type, int[] intensities, int[] frequencies, int minValue, int maxValue, int maxFreq)
    {
    	if(intensities.length != frequencies.length) 
    		throw new IllegalArgumentException("intensities and frequencies array length do not match");
    	
    	this.pixelDepth  = type;
    	this.intensities = intensities;
    	this.frequencies = frequencies;
    	this.minValue   = minValue;
    	this.maxValue   = maxValue;
    	this.maxFreq    = maxFreq;
    }
    
    /**
     * captures the intensity distribution of pixel-array
     * @param type type of the image
     * @param histogram raw intensity distribution 
     * @param minValue maximum intensity value
     * @param maxValue minimum intensity value
     * @param maxFreq  maximum frequency 
     */
    Histogram(PixelDepth type, OpenIntIntHashMap intensitiesDist, int minValue, int maxValue, int maxFreq)
    {
    	IntArrayList keys = intensitiesDist.keys();
    	keys.trimToSize();
    	
    	intensities = keys.elements();
    	Arrays.sort(intensities);
    	frequencies = new int[intensities.length];
    	for(int i = 0; i < intensities.length; i++)
    	{
    		frequencies[i] = intensitiesDist.get(intensities[i]);
    	}
    	    	
    	this.pixelDepth  = type;
    	this.minValue   = minValue;
    	this.maxValue   = maxValue;
    	this.maxFreq    = maxFreq;
    }
    
    /**
     * Creates a buffered image with the specified dimension drawing the histogram within the given range
     * @param size the size of the required image 
     * @param lower the lower cutoff intensity, can be null
     * @param upper the upper cutoff intensity, can be null
     * @return
     */
    public BufferedImage createImage(Dimension size, Integer lower, Integer upper, Color bgColor, Color penColor, float penWidth)
    {
		int startIntensity = lower == null ? intensities[0] : lower;
		int endIntensity = upper == null ? intensities[intensities.length-1] : upper;
		
		int maxFrequency = getMaxFrequency(startIntensity, endIntensity);
		double yScale = (double)size.height/(double)maxFrequency; //zero based
		double xScale = (double)size.width/(double)(endIntensity-startIntensity+1);
		
		GeneralPath path = new GeneralPath();
		
		boolean withinRange = false;
		for(int i = 0;i < intensities.length; i++)
		{
			if(intensities[i] == startIntensity)
			{
				double x = 0;
				double y = size.getHeight() - frequencies[i] * yScale;
				path.moveTo(x, y);
    			withinRange = true;
			}
			
			if(withinRange)
			{
				double x = (intensities[i] - startIntensity) * xScale;
				double y = size.getHeight() - frequencies[i] * yScale;
				path.lineTo(x, y);
			}

    		if(intensities[i] == endIntensity)
    			break;
		}
		
		BufferedImage resultImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = resultImage.createGraphics();
//		graphics2D.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setColor(bgColor);
		graphics2D.fillRect(0, 0, size.width, size.height);
		graphics2D.setStroke(new BasicStroke(penWidth));
		graphics2D.setColor(penColor);
		graphics2D.draw(path);
		
		graphics2D.dispose();
		
		return resultImage;
	}
    
    private int getMaxFrequency(int startIntensity, int endIntensity)
    {
    	int maxFrequency = 0;
    	boolean withinRange = false;
    	
    	for(int i = 0;i < intensities.length; i++)
    	{
    		if(intensities[i] == startIntensity)
    		{
    			withinRange = true;
    			maxFrequency = frequencies[i];
    		}
    		
    		if(withinRange)
    		{
    			if(frequencies[i] > maxFrequency)
    				maxFrequency = frequencies[i];
    		}
    		
    		if(intensities[i] == endIntensity)
    		{
    			break;
    		}
    	}
    	
    	return maxFrequency;
    	
    }

    public int getMaxFrequency() 
    {
        return maxFreq;
    }

    public int getMax() 
    {
        return maxValue;
    }

    public int getMin() 
    {
        return minValue;
    }
    
    /**
     * Returns the number of intensity values having non zero frequencies
     * @return the number of intensity values having non zero frequencies
     */
    public int getCount()
    {
    	return intensities.length;
    }

    public int[] getIntensities() 
    {
        return intensities;
    }
    
    public int[] getFrequencies() 
    {
        return frequencies;
    }
    
    /**
     * Returns an iterator over the intensities in this histogram in proper sequence.
     * @return an iterator over the intensities in this histogram in proper sequence.
     */
    public Iterator<Distribution> iterator()
    {
    	return new IntensityDistributionIterator();
    }
    
    public static class Distribution
    {
    	/**
    	 * the intensity value
    	 */
    	public int intensity;
    	/**
    	 * the number of such values within the pixelarray
    	 */
    	public int frequency;
    	
    	public Distribution(int intensity, int freq)
    	{
    		this.intensity = intensity;
    		this.frequency = freq;
    	}
    }
    
    private class IntensityDistributionIterator implements Iterator<Distribution> 
    {
        private int cursor = 0;       // index of next element to return
        private int size = intensities.length;

        public boolean hasNext() 
        {
            return cursor != size;
        }

        public Distribution next() 
        {
            if (cursor >= size)
                throw new NoSuchElementException();
            
            Distribution d = new Distribution(intensities[cursor], frequencies[cursor]);
            cursor++;
            
            return d;
        }

        public void remove()
        {
        	throw new UnsupportedOperationException("remove is not supported");
        }
    }
}
