/**
 * PixelArray.java
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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import cern.colt.map.OpenIntIntHashMap;

import com.strandgenomics.imaging.icore.Disposable;

/**
 * Represents the raw pixel data
 * @author arunabha
 */
public abstract class PixelArray implements Serializable, Disposable {
	
	/** Interpolation methods */
	public static enum InterpolationMethod {NEAREST_NEIGHBOR, NONE, BILINEAR, BICUBIC};

	/**
	 * the logger
	 */
	static Logger logger = Logger.getLogger("com.strandgenomics.imaging.icore.image");
	/**
	 * generated serial version id
	 */
	private static final long serialVersionUID = -8405783227065082155L;
	
	/**
	 * Creates a RGB image from the rgb pixel values
	 * @param imageWidth the width of the image
	 * @param imageHeight the height of the image
	 * @param pixels the pixels value
	 * @return the Buffered Image created using the pixels
	 */
	public static BufferedImage getRGBImage(int imageWidth, int imageHeight, int[] pixels)
	{
		long startTime = System.currentTimeMillis();
		if(pixels.length != imageWidth * imageHeight)
			throw new IllegalArgumentException("pixels dimension mismatch");
		
		ColorModel cm = new DirectColorModel(32, 0x00FF0000, 0x0000FF00, 0x000000FF);
		
		WritableRaster wr = cm.createCompatibleWritableRaster(1, 1); //some dummy
		SampleModel sm = wr.getSampleModel();
		//create the relevant sample model
		sm = sm.createCompatibleSampleModel(imageWidth, imageHeight);
		//create the data buffer to hold the image
		DataBuffer db = new DataBufferInt(pixels, imageWidth*imageHeight, 0);
		//the relevant raster to write images
		WritableRaster r = Raster.createWritableRaster(sm, db, null);
		
		BufferedImage img = new BufferedImage(cm, r, false, null);
		
		long endTime = System.currentTimeMillis();
		logger.logp(Level.INFO, "PixelArray", "getRGBImage", "Created RGB Image in "+(endTime-startTime) +" ms");
		
		return img;
	}
	
	/**
	 * creates a RGBA from given pixel values
	 * @param imageWidth
	 * @param imageHeight
	 * @param pixels
	 * @return
	 */
	public static BufferedImage getRGBAImage(int imageWidth, int imageHeight, int[] pixels)
	{
		long startTime = System.currentTimeMillis();
		if(pixels.length != imageWidth * imageHeight)
			throw new IllegalArgumentException("pixels dimension mismatch");
		
		ColorModel cm = new DirectColorModel(32, 0x00FF0000, 0x0000FF00, 0x000000FF,  0xFF000000);
		
		WritableRaster wr = cm.createCompatibleWritableRaster(1, 1); //some dummy
		SampleModel sm = wr.getSampleModel();
		//create the relevant sample model
		sm = sm.createCompatibleSampleModel(imageWidth, imageHeight);
		//create the data buffer to hold the image	
		DataBuffer db = new DataBufferInt(pixels, imageWidth*imageHeight, 0);
		//the relevant raster to write images
		WritableRaster r = Raster.createWritableRaster(sm, db, null);
		
		BufferedImage img = new BufferedImage(cm, r, false, null);
		
		long endTime = System.currentTimeMillis();
		logger.logp(Level.INFO, "PixelArray", "getRGBAImage", "Created ARGB Image in "+(endTime-startTime) +" ms");
		
		return img;
	}
	
	public static BufferedImage getOverlayImage(PixelArray ... pixelDataList)
	{
		Integer overlay = getRGBPixels(pixelDataList);
		return getRGBImage(overlay.width, overlay.height, overlay.pixelArray);
	}
	
	/**
	 * Overlay and return the RGB pixel values
	 * @param pixelDataList
	 * @return
	 */
	public static Integer getRGBPixels(PixelArray ... pixelDataList)
	{
		long startTime = System.currentTimeMillis();

		ColorModel[] cms = new ColorModel[pixelDataList.length];
		byte[][] scaledPixels = new byte[pixelDataList.length][];
		
		for(int i = 0;i < pixelDataList.length; i++)
		{
			cms[i] = pixelDataList[i].getColorModel();
			scaledPixels[i] = pixelDataList[i].create8BitImage();
		}
		
		int imageWidth = pixelDataList[0].width;
		int imageHeight = pixelDataList[0].height;
		
		int noOfPixels = imageWidth * imageHeight;
		int[] overlayPixels = new int[noOfPixels];
		
		for(int i = 0;i < noOfPixels; i++)
		{
			int alpha = 255;
            int red   = 0;
            int green = 0;
            int blue  = 0;

			for(int j = 0;j < pixelDataList.length; j++)
			{
				int pixel = scaledPixels[j][i];
				
                red   += cms[j].getRed(pixel);
                green += cms[j].getGreen(pixel);
                blue  += cms[j].getBlue(pixel);

                if (red > 255)   red   = 255;
                if (green > 255) green = 255;
                if (blue > 255)  blue  = 255;
			}
			
			overlayPixels[i] = ((alpha & 0xFF) << 24) |
	                ((red & 0xFF) << 16)   |
	                ((green & 0xFF) << 8)  |
	                ((blue & 0xFF) << 0);
		}
		
		long endTime = System.currentTimeMillis();
		logger.logp(Level.INFO, "PixelArray", "getRGBPixels", "Overlaid "+ pixelDataList.length +" in "+(endTime-startTime) +" ms");
		return new Integer(overlayPixels, imageWidth, imageHeight);
	}
	
	/**
     * Convert the buffer image to PixelArray 
     * @param image a buffered image
     * @return the pixel data of the buffered image
     */
    public static PixelArray toPixelArray(BufferedImage image)
    {
    	if(image == null) return null;
		
		Raster raster = image.getData();
		DataBuffer data = raster.getDataBuffer();
		
		PixelArray pixels = null;
		
		if(data instanceof DataBufferByte)
		{
			pixels = new PixelArray.Byte(((DataBufferByte)data).getData(), image.getWidth(), image.getHeight());
		}
		else if(data instanceof DataBufferShort)
		{
			pixels = new PixelArray.Short(((DataBufferShort)data).getData(), image.getWidth(), image.getHeight());
		}
		else if(data instanceof DataBufferUShort)
		{
			pixels = new PixelArray.Short(((DataBufferUShort)data).getData(), image.getWidth(), image.getHeight());
		}
		else if(data instanceof DataBufferInt)
		{
			pixels = new PixelArray.Integer(((DataBufferInt)data).getData(), image.getWidth(), image.getHeight());
		}
		else if(data instanceof DataBufferFloat)
		{
			pixels = new PixelArray.Float(((DataBufferFloat)data).getData(), image.getWidth(), image.getHeight());
		}
		else
		{
			throw new IllegalStateException("unsupported buffered image "+data.getDataType());
		}
		
		return pixels;
    }
    
	/**
	 * Write this pixel array in the specified DataOutputStream
	 * @param out the output stream
	 * @throws IOException 
	 */
	public static PixelArray read(InputStream in) throws IOException
	{
		DataInputStream inStream = new DataInputStream(in);
		
		PixelDepth depth = PixelDepth.toPixelDepth( inStream.readInt() );
		int width  = inStream.readInt();
		int height = inStream.readInt();
		
		PixelArray rawData = null;
		
		switch(depth)
		{
			case INT:
			{
				int[] pixels = new int[width*height];
				for(int i = 0;i < pixels.length; i++)
					pixels[i] = inStream.readInt();
				
				rawData = new Integer(pixels, width, height);
				break;
			}
			case SHORT:
			{
				short[] pixels = new short[width*height];
				for(int i = 0;i < pixels.length; i++)
					pixels[i] = inStream.readShort();
				
				rawData = new Short(pixels, width, height);
				break;
			}
			case BYTE:
			{
				byte[] pixels = new byte[width*height];
				inStream.readFully(pixels);
				
				rawData = new Byte(pixels, width, height);
				break;
			}		
		}
		
		return rawData;
	}
	
    /**
     * Height of the image
     */
	protected int height;
	/**
	 * Width of the image
	 */
    protected int width;
    /**
     * actual minimum intensity value - raw pixel value
     */
    protected int minIntensity = java.lang.Integer.MAX_VALUE;
    /**
     * actual maximum intensity value - the raw pixel value
     */
    protected int maxIntensity = 0;
    /**
     * custom, user specified minimum intensity value
     */
    protected int customMin = java.lang.Integer.MAX_VALUE;
    /**
     * custom, user specified maximum intensity value
     */
    protected int customMax = 0;
    /**
     * the gamma correction value, default is 1.0 which essentially ignores gamma correction 
     */
    protected double gamma = 1.0;
    /**
     * the color model or LUT for image generation
     */
    protected transient ColorModel lut = null;
    /**
     * the default GRAYSCALE color model for creating images from intensity value
     */
    protected static IndexColorModel defaultColorModel = null;
    /**
     * the raster for writing images
     */
	protected transient WritableRaster raster = null;
	protected transient SampleModel sampleModel = null;
	/**
	 * interpolation for resizing
	 */
	protected InterpolationMethod interpolationMethod = InterpolationMethod.NONE;
	
	@Override
	public void dispose() 
	{
		lut = null;
		raster = null;
		sampleModel = null;
	}
	
	/**
	 * Write this pixel array in the specified DataOutputStream
	 * @param out the output stream
	 * @throws IOException 
	 */
	public void write(OutputStream out) throws IOException
	{
		DataOutputStream outStream = new DataOutputStream(out);
		
		outStream.writeInt(getType().getByteSize());
		outStream.writeInt(getWidth());
		outStream.writeInt(getHeight());
		writePixelArray(outStream);
		outStream.flush();
	}
	
	protected abstract void writePixelArray(DataOutputStream outStream) throws IOException; 
	
	/**
	 * Sets the contrast to auto contrast (for the actual intensity distribution of the intensity values)
	 */
	public void setAutoContrast()
	{
		if(minIntensity == java.lang.Integer.MAX_VALUE) //not computed
		{
			computeIntensityDistribution();
		}
		
		customMin = minIntensity;
		customMax = maxIntensity;
	}

	/**
	 * Ensures that proper contrast values are set
	 */
	protected void ensureContrast()
	{
		if(customMin == java.lang.Integer.MAX_VALUE) //not set
		{
			setAutoContrast();
		}
	}
	
	/**
	 * Sets the contrast by defining the min and max intensity values
	 * @param min the minimum intensity value
	 * @param max the maximum intensity value
	 */
	public abstract void setContrast(int min, int max);
	
	public int getMinContrastSetting()
	{
		return customMin;
	}
	
	public int getMaxContrastSetting()
	{
		return customMax;
	}
	
	public double getGamma()
	{
		return gamma;
	}
	
	/**
	 * Sets the Gamma correction value
	 * @param g the gamma (valid values are between 0.1 and 5.0)
	 */
	public void setGamma(double g)
	{
		if(g < 0.1 || g > 5.0)
			throw new IllegalArgumentException("gamma must be within 0.1 and 5.0");
		this.gamma = g;
	}
    
	/** 
	 * Returns the default grayscale IndexColorModel.
     */
	public synchronized IndexColorModel getDefaultColorModel() 
	{
		if (defaultColorModel == null) 
		{
			byte[] r = new byte[256];
			byte[] g = new byte[256];
			byte[] b = new byte[256];
			
			for(int i=0; i<256; i++)
			{
				r[i]=(byte)i;
				g[i]=(byte)i;
				b[i]=(byte)i;
			}
			IndexColorModel cm = new IndexColorModel(8, 256, r, g, b);
			defaultColorModel = cm;
		}
		return defaultColorModel;
	}
	
	/**
	 * Returns a look up table for gamma correction
	 * @param gamma a valid gamma value between 0.1 and 5.0
	 * @return the LUT
	 */
	protected int[] getGammaLUT(double gamma)
	{
		int[] lut = new int[256];
		int v = 0;
		
		for(int i=0; i<256; i++) 
		{
			v = (int)(Math.exp(Math.log(i/255.0)*gamma)*255.0);
			
			if (v < 0) v = 0;
			if (v > 255) v = 255;
			
			lut[i] = v;
		}
		
		return lut;
	}
	
	/**
	 * Sets the color model (IndexColorModel) for creating images
	 * @param cm the color model
	 */
	public void setColorModel(ColorModel cm)
	{
		if (!(cm instanceof IndexColorModel))
			throw new IllegalArgumentException("Must be IndexColorModel");
		
		this.lut = cm;
	}
	
	/**
	 * Set color for color images, the default is gray scale
	 * @param color
	 */
	public void setColor(Color color)
	{
		this.lut = color == null ? getDefaultColorModel() : LUT.createLutFromColor(color);
	}
	
	/**
	 * Returns the color model
	 * @return
	 */
	public ColorModel getColorModel()
	{
		if(lut == null)
		{
			lut = getDefaultColorModel();
		}
		return lut;
	}
    
    /**
     * @return the height
     */
    public int getHeight() 
    {
        return this.height;
    }
    
    /**
     * Returns the underlying bytes of this pixel array
     * @return the underlying bytes of this pixel array
     */
    public abstract byte[] getBytes();

    /**
     * @return the pixelArray
     */
    public abstract Object getPixelArray();
    
    /**
     * @return the pixelArray
     */
    public abstract PixelArray clone();

    /**
     * @return the type
     */
    public abstract PixelDepth getType();

    /**
     * @return the width
     */
    public int getWidth() 
    {
        return this.width;
    }
    
    /**
     * compute the message digest of this pixel array, default is MD5
     * @return computed message digest of this pixel array 
     */
    public abstract BigInteger computeMessageDigest(String algoName) 
    		throws NoSuchAlgorithmException;

    /**
     * Returns the total number of pixels
     * @return the total number of pixels
     */
    public abstract int computePixelCount();

    /**
     * Returns the pixel value at the specified row and column index
     * @param i column no
     * @param j row no
     * @return the pixel value at the specified row and column index
     */
    public int getPixelValue(int i, int j)
    {
        if(i >= width || j >= height || i < 0 || j < 0)
        {
            return 0;
        }
        return getPixelValue((j * width) + i);
    }

    /**
     * Returns the pixel at the specified index
     * @param index the specified index
     * @return the pixel at the specified index
     */
    public abstract int getPixelValue(int index);

    /**
     * Returns the maximum pixel value
     * @return the defaultMax
     */
    public abstract int defaultMax();
    
    /**
     * Overlay with another pixel array - the overlay is done for each pixel values
     * if pixel value at the ith position of this PixelArray is less than the given PixelArray
     * it is overwritten with the pixel value from the given PixelArray.
     * Note, the type must match and this operation will mutate this PixelArray
     * @param another another pixel array of the same depth
     */
    public abstract void overlay(PixelArray another);
    
    /**
     * Scan the pixels and compute the intensity distribution 
     * @return the intensity distribution of this PixelData
     */
    public abstract Histogram computeIntensityDistribution();
    
    /**
     * Returns the default sample model
     * @return
     */
	protected SampleModel getIndexSampleModel() 
	{
		if (sampleModel==null) 
		{
			IndexColorModel icm = getDefaultColorModel();
			WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
			sampleModel = wr.getSampleModel();
			sampleModel = sampleModel.createCompatibleSampleModel(width, height);
		}
		return sampleModel;
	}
	
    /**
     * Creates an awt image with the pixel data
     * @return an awt image with the pixel data
     */
	public BufferedImage createImage()
	{
		if (raster == null)
		{
			SampleModel sm = getIndexSampleModel();
			byte[] scaledPixels = create8BitImage();
			DataBuffer db = new DataBufferByte(scaledPixels, width*height, 0);
			raster = Raster.createWritableRaster(sm, db, null);
		}
		
		return new BufferedImage(getColorModel(), raster, false, null);
	}
	
	/**
	 * create the processed (scaled) pixel values ready for image creation
	 * @param min the min intensity value
	 * @param max the max intensity value
	 * @param gamma the gamma correction
	 * @return
	 */
	public abstract byte[] create8BitImage();
	
	/**
	 * Creates an empty pixel array with the given width and height while sharing the contrast/color model etc
	 * with this PixelArray
	 * @param width
	 * @param height
	 * @return
	 */
	public abstract PixelArray create(int width, int height);
    
    /**
     * Creates a sub-image with the specified bounds
     * @param x the X coordinate of the upper-left corner of the specified rectangular region
     * @param y the Y coordinate of the upper-left corner of the specified rectangular region
     * @param width the width of the specified rectangular region
     * @param height the height of the specified rectangular region
     * @return a sub-image with the specified bounds
     * @exception IllegalArgumentException if the specified area is not contained within this PixelArray.
     */
    public abstract PixelArray getSubArray(int x, int y, int width, int height);
    
    /**
     * The <code>Byte</code> class defines a PixelData specified
     * in <code>byte</code> depth.
     */
    public static class Byte extends PixelArray implements Serializable
    {
		private static final long serialVersionUID = -1347072227693291118L;
		private byte[] pixelArray; 
    	
        public Byte(byte[] data, int width, int height) 
        {
            this.pixelArray = data;
            this.width = width;
            this.height = height;
        }
        
        @Override
        public void setContrast(int min, int max)
        {
			min = min & 0xFF;
			max = max & 0xFF;
			
    		if(max <= min)
    			throw new IllegalArgumentException("min("+min +") must be less then max ("+max+")");
    		
    		this.customMin = min;
    		this.customMax = max;
        }
        
        /**
         * @return the type
         */
        public PixelDepth getType()
        {
        	return PixelDepth.BYTE;
        }
        
        @Override
        public int getPixelValue(int index)
        {
            if(index >= pixelArray.length || index < 0)
            {
                return 0;
            }
            
            return pixelArray[index] & 0xFF;
        }
        
        @Override
        public int defaultMax() 
        {
        	return 0xFF;
        }

		@Override
		public Object getPixelArray() 
		{
			return pixelArray;
		}

		@Override
		public int computePixelCount() 
		{
			return pixelArray.length;
		}

		@Override
		public Histogram computeIntensityDistribution() 
		{
			//list of intensities with non zero frequencies
			OpenIntIntHashMap distribution = new OpenIntIntHashMap(255);
			
			// optimizing only in case of BYTE because array performs much better than OpenIntIntHashMap
			// NOTE: this CANNOT be done in other cases because that will end up creating huge array 
			int[] arr = new int[256];

			int maxFrequency = 0;
			minIntensity = 0xFF;
			maxIntensity = 0;
			
			for(int index = 0;index < pixelArray.length; index++)
			{
				//the raw intensity value at index, last byte only
				int intensityValue = pixelArray[index] & 0xFF;
				if(intensityValue == 0) continue; //ignore zero intensity values
				
//				int frequency = distribution.containsKey(intensityValue) ? distribution.get(intensityValue) : 0;
//				frequency += 1; //increment the pixel count at this intensity value
//				distribution.put(intensityValue, frequency);
				int frequency = arr[intensityValue]+1;
				arr[intensityValue] = frequency;
				
                if (maxFrequency < frequency) 
                	maxFrequency = frequency;
                
                if(minIntensity > intensityValue)
                	minIntensity = intensityValue;
                
                if(maxIntensity < intensityValue)
                	maxIntensity = intensityValue;
			}
			
			for(int i=0;i<arr.length;i++)
			{
				if(arr[i]!=0)
					distribution.put(i, arr[i]);
			}
			
			if(minIntensity == 0xFF) minIntensity = 0; //if nothing is there
			distribution.trimToSize();
				
			return new Histogram(getType(), distribution, minIntensity, maxIntensity, maxFrequency);
		}

		@Override
		public BigInteger computeMessageDigest(String algoName) throws NoSuchAlgorithmException 
		{
			MessageDigest md = MessageDigest.getInstance(algoName == null ? "MD5" : algoName);
			md.update(pixelArray);
			
			byte[] hash = md.digest();//128 bit number for MD5
			return new BigInteger(hash);
		}
		
		
		public PixelArray clone(){
			
			return new Byte(Arrays.copyOf(pixelArray, computePixelCount()), getWidth(), getHeight());
		}
		
		@Override
		public PixelArray create(int w, int h) 
		{
			Byte pArray = new Byte(new byte[w*h], w, h);
			
			ensureContrast(); //ensure contrast is set
			pArray.setContrast(this.getMinContrastSetting(), this.getMaxContrastSetting());
			pArray.setGamma(this.getGamma());
			pArray.setColorModel(this.getColorModel());
			
			return pArray;
		}

		@Override
		public PixelArray getSubArray(int x, int y, int w, int h) 
		{
			if(x < 0 || y < 0 || w <= 0 || h <= 0 ||
					(x+w) > this.width || (y+h) > this.height)
				throw new IllegalArgumentException("specified area is not contained within this PixelArray");
			
			Byte subArray = (Byte) create(w, h);
			byte[] bachha = subArray.pixelArray;
			
			for (int j = y; j < y+h; j++) 
			{
				int offset1 = (j - y) * w;
				int offset2 = j * width + x;
				
				for (int i = 0; i < w; i++)
					bachha[offset1++] = pixelArray[offset2++];
			}

			return subArray;
		}

		@Override
		public void overlay(PixelArray another) 
		{
			if(!(another instanceof Byte))
				throw new IllegalArgumentException("overlay can be done with the same type");
			
			byte[] otherArray = ((Byte)another).pixelArray;

			for (int i = 0; i < pixelArray.length; i++)
			{
				// to handle unsigned case
				int x = pixelArray[i] & 0xFF;
				int y = otherArray[i] & 0xFF;
				
				if (x < y)
				{
					pixelArray[i] = otherArray[i];
				}
			}
		}

		@Override
		public byte[] getBytes() 
		{
			return pixelArray;
		}
		
		@Override
		public byte[] create8BitImage()
		{
			ensureContrast(); //ensure that relevant contrast values are set
			
			int size = width*height;
			byte[] pixels8 = new byte[size];
			
			int value;
			double scale = 256.0/(customMax-customMin+1);
			int[] gLut = getGammaLUT(gamma);

			for (int i=0; i<size; i++) 
			{
				value = (pixelArray[i] & 0xFF)-customMin;
				if (value<0) value = 0;
				value = (int)(value*scale+0.5);
				if (value>255) value = 255;
				//apply gamma correction
				value = gLut[value];

				pixels8[i] = (byte)value;
			}
			return pixels8;
		}

		@Override
		public void dispose()
		{
			super.dispose();
			pixelArray = null;
		}

		@Override
		protected void writePixelArray(DataOutputStream outStream) throws IOException
		{
			outStream.write( pixelArray );
		}
    }
    
    /**
     * The <code>Short</code> class defines a PixelData specified
     * in <code>short</code> depth.
     */
    public static class Short extends PixelArray implements Serializable
    {
		private static final long serialVersionUID = -5029904779077373366L;
		private short[] pixelArray; 
    	
        public Short(short[] data, int width, int height) 
        {
            this.pixelArray = data;
            this.width = width;
            this.height = height;
        }
        
        @Override
        public void setContrast(int min, int max)
        {
			min = min & 0xFFFF;
			max = max & 0xFFFF;
			
    		if(max <= min)
    			throw new IllegalArgumentException("min("+min +") must be less then max ("+max+")");
    		
    		this.customMin = min;
    		this.customMax = max;
        }
        
        
        /**
         * @return the type
         */
        public PixelDepth getType()
        {
        	return PixelDepth.SHORT;
        }
        
        @Override
        public int getPixelValue(int index)
        {
            if(index >= pixelArray.length || index < 0)
            {
                return 0;
            }
            
            return pixelArray[index] & 0xFFFF;
        }
        
        @Override
        public int defaultMax() 
        {
        	return 0xFFFF;
        }
        
		@Override
		public int computePixelCount() 
		{
			return pixelArray.length;
		}
		
		@Override
		public Object getPixelArray() 
		{
			return pixelArray;
		}

		@Override
		public Histogram computeIntensityDistribution() 
		{
			//list of intensities with non zero frequencies
			OpenIntIntHashMap distribution = new OpenIntIntHashMap(1024);

			int maxFrequency = 0;
			minIntensity = 0xFFFF;
			maxIntensity = 0;
			
			for(int index = 0;index < pixelArray.length; index++)
			{
				//the raw intensity value at index, last byte only
				int intensityValue = pixelArray[index] & 0xFFFF;
				if(intensityValue == 0) continue; //ignore zero intensity values 
				
				int frequency = distribution.containsKey(intensityValue) ? distribution.get(intensityValue) : 0;
				frequency += 1; //increment the pixel count at this intensity value
				distribution.put(intensityValue, frequency);
				
                if (maxFrequency < frequency) 
                	maxFrequency = frequency;
                
                if(minIntensity > intensityValue)
                	minIntensity = intensityValue;
                
                if(maxIntensity < intensityValue)
                	maxIntensity = intensityValue;
			}
			
			if(minIntensity == 0xFFFF) minIntensity = 0; //if nothing is there
			distribution.trimToSize();
				
			return new Histogram(getType(), distribution, minIntensity, maxIntensity, maxFrequency);
		}

		@Override
		public BigInteger computeMessageDigest(String algoName) throws NoSuchAlgorithmException 
		{
			MessageDigest md = MessageDigest.getInstance(algoName == null ? "MD5" : algoName);
			for(int i = 0;i < pixelArray.length; i++)
			{
				short v = pixelArray[i];
				byte[] ithPixel = {(byte)v, (byte) (v >>> 8)};
				md.update(ithPixel);
			}
			
			byte[] hash = md.digest();//128 bit number for MD5
			return new BigInteger(hash);
		}

		public PixelArray clone(){
			
			return new Short(Arrays.copyOf(pixelArray, computePixelCount()), getWidth(), getHeight());
		}
		
		@Override
		public PixelArray create(int w, int h) 
		{
			Short pArray = new Short(new short[w*h], w, h);
			
			ensureContrast(); //ensure contrast is set
			pArray.setContrast(this.getMinContrastSetting(), this.getMaxContrastSetting());
			pArray.setGamma(this.getGamma());
			pArray.setColorModel(this.getColorModel());
			
			return pArray;
		}
		
		@Override
		public PixelArray getSubArray(int x, int y, int w, int h) 
		{
			if(x < 0 || y < 0 || w <= 0 || h <= 0 ||
					(x+w) > this.width || (y+h) > this.height)
				throw new IllegalArgumentException("specified area is not contained within this PixelArray");
			
			Short subArray = (Short) create(w, h);
			short[] bachha = subArray.pixelArray;
			
			for (int j = y; j < y+h; j++) 
			{
				int offset1 = (j - y) * w;
				int offset2 = j * width + x;
				
				for (int i = 0; i < w; i++)
					bachha[offset1++] = pixelArray[offset2++];
			}
			
			return subArray;
		}

		@Override
		public void overlay(PixelArray another)
		{
			if(!(another instanceof Short))
				throw new IllegalArgumentException("overlay can be done with the same type");
			
			short[] otherArray = ((Short)another).pixelArray;

			for (int i = 0; i < pixelArray.length; i++)
			{
				// to handle unsigned case
				int x = pixelArray[i] & 0xFFFF;
				int y = otherArray[i] & 0xFFFF;
				
				if (x < y)
				{
					pixelArray[i] = otherArray[i];
				}
			}
		}

		@Override
		public byte[] getBytes()
		{
			 byte[] bytes = new byte[pixelArray.length*2];
			 for(int s = 0, b = 0; s < pixelArray.length & b < bytes.length; s++)
			 {
				 bytes[b++] = (byte)((pixelArray[s] >> 8) & 0xFF);
				 bytes[b++] = (byte)(pixelArray[s] & 0xFF);
			 }
			 
			 return bytes;
		}
		
		@Override
		public byte[] create8BitImage()
		{
			ensureContrast(); //ensure that relevant contrast values are set
			
			int size = width*height;
			byte[] pixels8 = new byte[size];
			
			int value;
			double scale = 256.0/(customMax-customMin+1);
			int[] gLut = getGammaLUT(gamma);
			
			for (int i=0; i<size; i++) 
			{
				value = (pixelArray[i] & 0xFFFF)-customMin;
				if (value<0) value = 0;
				value = (int)(value*scale+0.5);
				if (value>255) value = 255;
				//apply gamma correction
				value = gLut[value];
				
				pixels8[i] = (byte)value;
			}
			return pixels8;
		}

		@Override
		public void dispose() 
		{
			super.dispose();
			pixelArray = null;
		}

		@Override
		protected void writePixelArray(DataOutputStream outStream) throws IOException
		{
			for(int i = 0;i < pixelArray.length; i++)
				outStream.writeShort( pixelArray[i] );
		}
    }
    
    /**
     * The <code>Integer</code> class defines a PixelData specified
     * in <code>int</code> depth.
     */
    public static class Integer extends PixelArray implements Serializable
    {
		private static final long serialVersionUID = -7475009690499362674L;
		private int[] pixelArray; 
    	
        public Integer(int[] data, int width, int height) 
        {
            this.pixelArray = data;
            this.width = width;
            this.height = height;
        }
        
        @Override
        public void setContrast(int min, int max)
        {
			min = min & 0x7FFFFFFF;
			max = max & 0x7FFFFFFF;
			
    		if(max <= min)
    			throw new IllegalArgumentException("min("+min +") must be less then max ("+max+")");
    		
    		this.customMin = min;
    		this.customMax = max;
        }
        
        /**
         * @return the type
         */
        public PixelDepth getType()
        {
        	return PixelDepth.INT;
        }
        
        @Override
        public int getPixelValue(int index)
        {
            if(index >= pixelArray.length || index < 0)
            {
                return 0;
            }
            return pixelArray[index] & 0xFFFFFFFF;
        }
        
        @Override
        public int defaultMax() 
        {
        	//2 power 31 - 1, 32nd bit is the sign bit, sign bit 1 is a negative number
        	return 0x7FFFFFF; 
        }
        
		@Override
		public int computePixelCount() 
		{
			return pixelArray.length;
		}
		
		@Override
		public Object getPixelArray() 
		{
			return pixelArray;
		}

		@Override
		public Histogram computeIntensityDistribution() 
		{
			//list of intensities with non zero frequencies
			OpenIntIntHashMap distribution = new OpenIntIntHashMap(255);

			int maxFrequency = 0;
			minIntensity = 0x7FFFFFFF;
			maxIntensity = 0;
			
			for(int index = 0;index < pixelArray.length; index++)
			{
				//the raw intensity value at index, last byte only
				int intensityValue = pixelArray[index] & 0x7FFFFFFF; //positive integer max
				if(intensityValue == 0) continue; //ignore zero intensity values 
				
				int frequency = distribution.containsKey(intensityValue) ? distribution.get(intensityValue) : 0;
				frequency += 1; //increment the pixel count at this intensity value
				distribution.put(intensityValue, frequency);
				
                if (maxFrequency < frequency) 
                	maxFrequency = frequency;
                
                if(minIntensity > intensityValue)
                	minIntensity = intensityValue;
                
                if(maxIntensity < intensityValue)
                	maxIntensity = intensityValue;
			}
			
			if(minIntensity == 0x7FFFFFFF) minIntensity = 0; //if nothing is there
			distribution.trimToSize();
				
			return new Histogram(getType(), distribution, minIntensity, maxIntensity, maxFrequency);
		}

		@Override
		public BigInteger computeMessageDigest(String algoName) throws NoSuchAlgorithmException 
		{
			MessageDigest md = MessageDigest.getInstance(algoName == null ? "MD5" : algoName);
			for(int i = 0;i < pixelArray.length; i++)
			{
				int v = pixelArray[i];
				byte[] ithPixel = {(byte)v, (byte) (v >>> 8), (byte) (v >>> 16), (byte) (v >>> 24)};
				md.update(ithPixel);
			}
			
			byte[] hash = md.digest();//128 bit number for MD5
			return new BigInteger(hash);
		}
		
		public PixelArray clone(){
			
			return new Integer(Arrays.copyOf(pixelArray, computePixelCount()), getWidth(), getHeight());
		}
		
		@Override
		public PixelArray create(int w, int h) 
		{
			Integer pArray = new Integer(new int[w*h], w, h);
			
			ensureContrast(); //ensure contrast is set
			pArray.setContrast(this.getMinContrastSetting(), this.getMaxContrastSetting());
			pArray.setGamma(this.getGamma());
			pArray.setColorModel(this.getColorModel());
			
			return pArray;
		}

		@Override
		public PixelArray getSubArray(int x, int y, int w, int h) 
		{
			if(x < 0 || y < 0 || w <= 0 || h <= 0 ||
					(x+w) > this.width || (y+h) > this.height)
				throw new IllegalArgumentException("specified area is not contained within this PixelArray");
			
			Integer subArray = (Integer) create(w,h);
			
			int[] bachha = subArray.pixelArray;
			for (int j = y; j < y+h; j++) 
			{
				int offset1 = (j - y) * w;
				int offset2 = j * width + x;
				
				for (int i = 0; i < w; i++)
					bachha[offset1++] = pixelArray[offset2++];
			}
			
			return subArray;
		}

		@Override
		public void overlay(PixelArray another) 
		{
			if(!(another instanceof Integer))
				throw new IllegalArgumentException("overlay can be done with the same type");
			
			int[] otherArray = ((Integer)another).pixelArray;

			for (int i = 0; i < pixelArray.length; i++)
			{
				if (pixelArray[i] < otherArray[i])
				{
					pixelArray[i] = otherArray[i];
				}
			}
		}

		@Override
		public byte[] getBytes() 
		{
			 byte[] bytes = new byte[pixelArray.length*4];
			 for(int i = 0, b = 0; i < pixelArray.length & b < bytes.length; i++)
			 {
				 bytes[b++] = (byte)((pixelArray[i] >> 24) & 0xFF);
				 bytes[b++] = (byte)((pixelArray[i] >> 16) & 0xFF);
				 bytes[b++] = (byte)((pixelArray[i] >> 8) & 0xFF);
				 bytes[b++] = (byte)(pixelArray[i] & 0xFF);
			 }
			 
			 return bytes;
		}
		
		@Override
		public byte[] create8BitImage()
		{
			ensureContrast(); //ensure that relevant contrast values are set
			
			int size = width*height;
			byte[] pixels8 = new byte[size];
			
			int value;
			double scale = 256.0/(customMax-customMin+1);
			int[] gLut = getGammaLUT(gamma);

			for (int i=0; i<size; i++) 
			{
				value = (pixelArray[i] & 0x7FFFFFFF)-customMin;
				if (value<0) value = 0;
				value = (int)(value*scale+0.5);
				if (value>255) value = 255;
				//apply gamma correction
				value = gLut[value];

				pixels8[i] = (byte)value;
			}
			return pixels8;
		}

		@Override
		public void dispose() 
		{
			super.dispose();
			pixelArray = null;
		}

		@Override
		protected void writePixelArray(DataOutputStream outStream) throws IOException
		{
			for(int i = 0;i < pixelArray.length; i++)
				outStream.writeInt( pixelArray[i] );
		}
    }
    
    /**
     * The <code>Integer</code> class defines a PixelData specified
     * in <code>int</code> depth.
     */
    public static class Float extends PixelArray implements Serializable
    {
		private static final long serialVersionUID = -7475009690499362674L;
		private float[] pixelArray; 
    	
        public Float(float[] data, int width, int height) 
        {
            this.pixelArray = data;
            this.width = width;
            this.height = height;
        }
        
        @Override
        public void setContrast(int min, int max)
        {
			min = min & 0x7FFFFFFF;
			max = max & 0x7FFFFFFF;
			
    		if(max <= min)
    			throw new IllegalArgumentException("min("+min +") must be less then max ("+max+")");
    		
    		this.customMin = min;
    		this.customMax = max;
        }
        
        /**
         * @return the type
         */
        public PixelDepth getType()
        {
        	return PixelDepth.INT;
        }
        
        private float applyBitMask(float value, int bitmask)
        {
        	int intBits = java.lang.Float.floatToIntBits(value) & bitmask;
        	
        	return java.lang.Float.intBitsToFloat(intBits);
        }
        
        @Override
        public int getPixelValue(int index)
        {
            if(index >= pixelArray.length || index < 0)
            {
                return 0;
            }
            return (int) applyBitMask(pixelArray[index], 0xFFFFFFFF);
        }
        
        @Override
        public int defaultMax() 
        {
        	//2 power 31 - 1, 32nd bit is the sign bit, sign bit 1 is a negative number
        	return 0x7FFFFFF; 
        }
        
		@Override
		public int computePixelCount() 
		{
			return pixelArray.length;
		}
		
		@Override
		public Object getPixelArray() 
		{
			return pixelArray;
		}

		@Override
		public Histogram computeIntensityDistribution() 
		{
			//list of intensities with non zero frequencies
			OpenIntIntHashMap distribution = new OpenIntIntHashMap(255);
			
			int maxFrequency = 0;
			minIntensity = 0x7FFFFFFF;
			maxIntensity = 0;
			
			for(int index = 0;index < pixelArray.length; index++)
			{
				//the raw intensity value at index, last byte only
				int intensityValue = (int) applyBitMask(pixelArray[index], 0x7FFFFFFF); //positive integer max
				if(intensityValue == 0) continue; //ignore zero intensity values 
				
				int frequency = distribution.containsKey(intensityValue) ? distribution.get(intensityValue) : 0;
				frequency += 1; //increment the pixel count at this intensity value
				distribution.put(intensityValue, frequency);
				
                if (maxFrequency < frequency) 
                	maxFrequency = frequency;
                
                if(minIntensity > intensityValue)
                	minIntensity = intensityValue;
                
                if(maxIntensity < intensityValue)
                	maxIntensity = intensityValue;
			}
			
			if(minIntensity == 0x7FFFFFFF) minIntensity = 0; //if nothing is there
			distribution.trimToSize();
				
			return new Histogram(getType(), distribution, minIntensity, maxIntensity, maxFrequency);
		}

		@Override
		public BigInteger computeMessageDigest(String algoName) throws NoSuchAlgorithmException 
		{
			MessageDigest md = MessageDigest.getInstance(algoName == null ? "MD5" : algoName);
			for(int i = 0;i < pixelArray.length; i++)
			{
				int v = (int) pixelArray[i];
				byte[] ithPixel = {(byte)v, (byte) (v >>> 8), (byte) (v >>> 16), (byte) (v >>> 24)};
				md.update(ithPixel);
			}
			
			byte[] hash = md.digest();//128 bit number for MD5
			return new BigInteger(hash);
		}
		
		public PixelArray clone(){
			
			return new Float(Arrays.copyOf(pixelArray, computePixelCount()), getWidth(), getHeight());
		}
		
		@Override
		public PixelArray create(int w, int h) 
		{
			Integer pArray = new Integer(new int[w*h], w, h);
			
			ensureContrast(); //ensure contrast is set
			pArray.setContrast(this.getMinContrastSetting(), this.getMaxContrastSetting());
			pArray.setGamma(this.getGamma());
			pArray.setColorModel(this.getColorModel());
			
			return pArray;
		}

		@Override
		public PixelArray getSubArray(int x, int y, int w, int h) 
		{
			if(x < 0 || y < 0 || w <= 0 || h <= 0 ||
					(x+w) > this.width || (y+h) > this.height)
				throw new IllegalArgumentException("specified area is not contained within this PixelArray");
			
			Float subArray = (Float) create(w,h);
			
			float[] bachha = subArray.pixelArray;
			for (int j = y; j < y+h; j++) 
			{
				int offset1 = (j - y) * w;
				int offset2 = j * width + x;
				
				for (int i = 0; i < w; i++)
					bachha[offset1++] = pixelArray[offset2++];
			}
			
			return subArray;
		}

		@Override
		public void overlay(PixelArray another) 
		{
			if(!(another instanceof Integer))
				throw new IllegalArgumentException("overlay can be done with the same type");
			
			int[] otherArray = ((Integer)another).pixelArray;

			for (int i = 0; i < pixelArray.length; i++)
			{
				if (pixelArray[i] < otherArray[i])
				{
					pixelArray[i] = otherArray[i];
				}
			}
		}

		@Override
		public byte[] getBytes() 
		{
			 byte[] bytes = new byte[pixelArray.length*4];
			 for(int i = 0, b = 0; i < pixelArray.length & b < bytes.length; i++)
			 {
				 int intBits = java.lang.Float.floatToIntBits(pixelArray[i]);
				 
				 bytes[b++] = (byte)((intBits >> 24) & 0xFF);
				 bytes[b++] = (byte)((intBits >> 16) & 0xFF);
				 bytes[b++] = (byte)((intBits >> 8) & 0xFF);
				 bytes[b++] = (byte)(intBits & 0xFF);
			 }
			 
			 return bytes;
		}
		
		@Override
		public byte[] create8BitImage()
		{
			ensureContrast(); //ensure that relevant contrast values are set
			
			int size = width*height;
			byte[] pixels8 = new byte[size];
			
			float value;
			double scale = 256.0/(customMax-customMin+1);
			int[] gLut = getGammaLUT(gamma);

			for (int i=0; i<size; i++) 
			{
				value = (applyBitMask(pixelArray[i] , 0x7FFFFFFF)-customMin);
				if (value<0) value = 0;
				value = (int)(value*scale+0.5);
				if (value>255) value = 255;
				//apply gamma correction
				value = gLut[(int) value];

				pixels8[i] = (byte)value;
			}
			return pixels8;
		}

		@Override
		public void dispose() 
		{
			super.dispose();
			pixelArray = null;
		}

		@Override
		protected void writePixelArray(DataOutputStream outStream) throws IOException
		{
			for(int i = 0;i < pixelArray.length; i++)
				outStream.writeFloat( pixelArray[i] );
		}
    }
    
}