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
 * Based on ImageJ LutLoader
 */
package com.strandgenomics.imaging.icore.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.util.Util;

/** 
 * Opens NIH Image look-up tables (LUTs), 
 * 768 byte binary LUTs (256 reds, 256 greens and 256 blues),
 * LUTs in text format 
 * see ImageJ ij.plugin.LutLoader
 */
public class LutLoader {
	
	/**
	 * Some standard LUTs that are available by default
	 */
	public static final String[] STD_LUTS = {"grays", "red", "green", "blue", "cyan", "magenta", "yellow", "redgreen", "fire","ice", "spectrum", "3-3-2 RGB"};

	/**
	 * used for synchronizing the creation data access objects
	 */
	private static Object padLock = new Object();
	/**
	 * singleton instance of the cache
	 */
	private static LutLoader instance = null;
	
	/**
	 * Returns the singleton instance of Cache 
	 * @return the singleton instance of Cache 
	 */
	public static LutLoader getInstance()
	{
		if(instance == null)
		{
			synchronized(padLock)
			{
				if(instance == null)
				{
					LutLoader c = new LutLoader();
					//do initialization is needed
		            c.initialize();
					instance = c;
				}
			}
		}
		return instance;
	}
	
	/**
	 * all loaded LUTs
	 */
	protected Map<String, LUT> lutTable = null;
	/**
	 * lut images for display
	 */
	protected Map<String, BufferedImage> lutImages = null;
	/**
	 * initialize the LUTs
	 */
	private void initialize()
	{
		lutTable = new LinkedHashMap<String, LUT>();
		//images for LUTs
		lutImages = new HashMap<String, BufferedImage>();
		
		//load std LUTs
		for(String stdName : STD_LUTS)
		{
			LUT lut = loadStandardLUT(stdName);
			if(lut != null)
			{
				lutTable.put(stdName, lut);
				
			}
		}
		
		//Load from classpath 
		List<String> fixedLuts = null;
		try 
		{
			fixedLuts = Util.getResourceFromPackage("luts");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if(fixedLuts != null)
		{
			for(String lutfile : fixedLuts)
			{
				try
				{
					LUT lut = openLut( new ClassResource("luts/"+lutfile) );
					if(lut != null)
					lutTable.put(getLUTName(lutfile), lut);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					System.out.println("unable to load LUT "+lutfile);
				}
			}
		}
		
		File lutFolder = Constants.getLUTDirectory();
		File[] lutFiles = lutFolder == null ? null : lutFolder.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name) 
			{
				return name.toLowerCase().endsWith(".lut");
			}
		});
			
		if(lutFiles != null)
		{
			for(File f : lutFiles)
			{
				try
				{
					LUT lut = openLut( new FileResource(f) );
					if(lut != null)
					lutTable.put(getLUTName(f.getName()), lut);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns the LUT icon image
	 * @param name
	 * @return
	 */
	public BufferedImage getLUTImage(String name)
	{
		
		BufferedImage lutImage = lutImages.get(name);
		if(lutImage == null)
		{
			LUT lut = getLUT(name);
			lutImage = createImage(lut);
			
			lutImages.put(name, lutImage);
		}
		
		return lutImage;
	}
	
	/** 
	 * Creates a 256x32 image from this IndexColorModel
	 */
	private BufferedImage createImage(LUT lut)
	{
		int width = 256;
		int height = 32;
		byte[] pixels = new byte[width*height];
		int value = 0;
		
		for (int y=0; y<height; y++)
		{
			for (int x=0; x<width; x++)
			{
				value = x;
				if (value>255) value = 255;
				if (value<0) value = 0;
			
				pixels[y*width + x] = (byte)value;
			}
		}
		
		WritableRaster wr = lut.createCompatibleWritableRaster(1, 1);
		SampleModel sampleModel = wr.getSampleModel();
		sampleModel = sampleModel.createCompatibleSampleModel(width, height);
			
		DataBuffer db = new DataBufferByte(pixels, width*height, 0);
		WritableRaster raster = Raster.createWritableRaster(sampleModel, db, null);
		return new BufferedImage(lut, raster, false, null);
	}

	/**
	 * Set of available LUTs
	 * @return
	 */
	public List<String> getAvailableLUTs()
	{			
		return new ArrayList(lutTable.keySet());
	}
	
	public String[] getPrimaryColorLUTs()
	{
		return new String[] {"red", "green", "blue", "cyan", "magenta", "yellow"};
	}
	
	/****
	 * returns the index in primaryColorsLUT of the color passed 
	 * @param colorName : string containing the colorName whose index needs to be identified
	 * @return index (integer)
	 * 3:31:25 PM
	 */
	public int getPrimaryLutIndex(String colorName){
		if(colorName.equals("red")){
			return 0;
		}else if(colorName.equals("green")){
			return 1;
		}else if(colorName.equals("blue")){
			return 2;
		}else if(colorName.equals("cyan")){
			return 3;
		}else if(colorName.equals("magenta")){
			return 4;
		}else if(colorName.equals("yellow")){
			return 5;
		}else return 6 ;
	}
	public LUT getLUT(String name)
	{
		LUT lut = lutTable.get(name);
		if(lut == null)
		{
			try
			{
				File lutFolder = Constants.getLUTDirectory();
				if(lutFolder != null)
				{
					File lutFile = new File(lutFolder, name+".lut");
					
					if(lutFile.isFile())
					{
						lut = openLut( new FileResource(lutFile) );
						if(lut != null)
						{
							lutTable.put(name, lut);
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return lut;
	}
		
	private LUT loadStandardLUT(String arg)
	{
		byte[] reds   = new byte[256]; 
		byte[] greens = new byte[256]; 
		byte[] blues  = new byte[256];
		
		int nColors = 0;
		
		if (arg.equals("fire"))
			nColors = fire(reds, greens, blues);
		else if (arg.equals("grays"))
			nColors = grays(reds, greens, blues);
		else if (arg.equals("ice"))
			nColors = ice(reds, greens, blues);
		else if (arg.equals("spectrum"))
			nColors = spectrum(reds, greens, blues);
		else if (arg.equals("3-3-2 RGB"))
			nColors = rgb332(reds, greens, blues);
		else if (arg.equals("red"))
			nColors = primaryColor(4, reds, greens, blues);
		else if (arg.equals("green"))
			nColors = primaryColor(2, reds, greens, blues);
		else if (arg.equals("blue"))
			nColors = primaryColor(1, reds, greens, blues);
		else if (arg.equals("cyan"))
			nColors = primaryColor(3, reds, greens, blues);
		else if (arg.equals("magenta"))
			nColors = primaryColor(5, reds, greens, blues);
		else if (arg.equals("yellow"))
			nColors = primaryColor(6, reds, greens, blues);
		else if (arg.equals("redgreen"))
			nColors = redGreen(reds, greens, blues);
		
		if (nColors > 0)
		{
			if (nColors<256)
				interpolate(reds, greens, blues, nColors);
			
			return new LUT(8, 256, reds, greens, blues);
		}
		
		return null;
	}
	
	/** 
	 * Opens an NIH Image LUT, 768 byte binary LUT or text LUT from a file
	 * @throws IOException 
	 */
	private LUT openLut(Resource resource) throws IOException 
	{
		long length = resource.length();
		if(length > 10000) return null;
		
		LUT lut = null;

		try
		{
			if (length > 768)
				lut = openBinaryLut(resource, false); // attempt to read NIH Image LUT
			
			if (lut == null && (length == 0 || length == 768 || length == 970))
				lut = openBinaryLut(resource, true); // otherwise read raw LUT
			
			if (lut == null && length > 768)
				lut = openTextLut(resource);
			
			if (lut == null)
				throw new IllegalArgumentException("unknown LUT format");
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return lut;
	}
	
	/** 
	 * Opens an NIH Image LUT or a 768 byte binary LUT. 
	 */
	public LUT openBinaryLut(Resource resource, boolean raw) throws IOException 
	{
		return openBinaryLut(new DataInputStream(new BufferedInputStream(resource.getInputStream())), raw);
	}
	
	
	/** 
	 * Opens an NIH Image LUT or a 768 byte binary LUT. (closes the stream as well)
	 */
	public LUT openBinaryLut(DataInputStream f, boolean raw) throws IOException 
	{
		LUT lut = null;
		try
		{
			int nColors   = 256;
		    byte[] reds   = new byte[256];
			byte[] greens = new byte[256];
			byte[] blues  = new byte[256];
			
			if (!raw)
			{
				// attempt to read 32 byte NIH Image LUT header
				int id = f.readInt();
				if (id!=1229147980) 
				{ // 'ICOL'
					f.close();
					return null;
				}
				
				//some fields not used
				int version = f.readShort();
				nColors = f.readShort();
				
				int start = f.readShort();
				int end = f.readShort();
				
				long fill1 = f.readLong();
				long fill2 = f.readLong();
				int filler = f.readInt();
			}
			
			f.read(reds, 0, nColors);
			f.read(greens, 0, nColors);
			f.read(blues, 0, nColors);
			
			if (nColors < 256)
				interpolate(reds, greens, blues, nColors);
			
			lut = new LUT(8, 256, reds, greens, blues);
		}
		finally
		{
			Util.closeStream(f);
		}
		
		return lut;
	}
	
	/** 
	 * This opens a tab or comma delimeted text file 
	 * Modified to accept commas as delimiters on 4/22/08 by 
	 * Jay Unruh, Stowers Institute for Medical Research. 
	 */
	public LUT openTextLut(Resource resource) throws IOException
	{
        int[] linesNWidth = countLines(new BufferedReader(new InputStreamReader(resource.getInputStream())));
        
        int noOfLines    = linesNWidth[0];
        int wordsPerLine = linesNWidth[1];

		if (wordsPerLine < 3 || wordsPerLine > 4 || noOfLines < 256 || noOfLines > 258) 
			return null; 
		
		float[][] pixels = read(new BufferedReader(new InputStreamReader(resource.getInputStream())), wordsPerLine, noOfLines);
		
		return readLUT(noOfLines, wordsPerLine, pixels);
	}
	
	private LUT readLUT(int noOfLines, int wordsPerLine, float[][] pixels)
	{
		byte[] reds   = new byte[256]; 
		byte[] greens = new byte[256]; 
		byte[] blues  = new byte[256];

		int firstRowNaNCount = 0;
        for (int i = 0; i < wordsPerLine; i++) 
        {
        	if (Float.isNaN(pixels[0][i]))
        		firstRowNaNCount++;
        }
        
        int x = wordsPerLine == 4 ? 1 : 0; //first one is the index, ignore
        // assume first row is header
        int y = firstRowNaNCount==wordsPerLine ? 1 : 0;

		for (int i = y, counter = 0; i < noOfLines && counter <= 255; i++, counter++)
		{
			reds[counter]   = (byte) pixels[i][x+0];
			greens[counter] = (byte) pixels[i][x+1];
			blues[counter]  = (byte) pixels[i][x+2];
		}

		return new LUT(8, 256, reds, greens, blues);
	}
	
	/**
	 * Counts the relevant LUT lines (also closes the stream
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private int[] countLines(BufferedReader reader) throws IOException
	{
        int lines = 0, width = 0;
        
        try
        {
        	StreamTokenizer tok = new StreamTokenizer(reader);
	
	        tok.resetSyntax();
	        tok.wordChars(43, 43);
	        tok.wordChars(45, 127);
	        tok.whitespaceChars(0, 42);
	        tok.whitespaceChars(44, 44);
	        //tok.wordChars(33, 127);
	        //tok.whitespaceChars(0, ' ');
			tok.whitespaceChars(128, 255);
	        tok.eolIsSignificant(true);
	        
	        int wordsPerLine = 0, wordsInPreviousLine = 0;
	
	        while (tok.nextToken() != StreamTokenizer.TT_EOF)
	        {
	            switch (tok.ttype) 
	            {
	                case StreamTokenizer.TT_EOL:
	                    lines++;
	                    if (wordsPerLine==0)
	                        lines--;  // ignore empty lines
	                    
	                    if (lines==1)
	                        width = wordsPerLine;
	                    
	                    else if (wordsPerLine!=0 && wordsPerLine!=wordsInPreviousLine)
	                        throw new IOException("Line "+lines+ " is not the same length as the first line.");
	                    
	                    if (wordsPerLine!=0)
	                        wordsInPreviousLine = wordsPerLine;
	                    
	                    wordsPerLine = 0;
	                    break;
	                    
	                case StreamTokenizer.TT_WORD:
	                    wordsPerLine++;
	                    break;
	            }
	        }
	        
	        if (wordsPerLine==width) 
	            lines++; // last line does not end with EOL
        }
        finally
        {
        	if(reader != null)
        	{
        		try
        		{
        			reader.close();
        		}
        		catch(Exception e)
        		{}
        	}
        }

        return new int[]{lines, width};
    }
	
	/**
	 * Reads the LUT (and closes the stream as well)
	 * @param reader
	 * @param wordsPerLine
	 * @param noOfLines
	 * @return
	 * @throws IOException
	 */
    private float[][] read(BufferedReader reader, int wordsPerLine, int noOfLines) throws IOException 
    {
    	float[][] data = null;
    	try
    	{
	        StreamTokenizer tok = new StreamTokenizer(reader);
	        tok.resetSyntax();
	        tok.wordChars(43, 43);
	        tok.wordChars(45, 127);
	        tok.whitespaceChars(0, 42);
	        tok.whitespaceChars(44, 44);
	        //tok.wordChars(33, 127);
	        //tok.whitespaceChars(0, ' ');
	        tok.whitespaceChars(128, 255);
	        //tok.parseNumbers();
	
	        data = new float[noOfLines][wordsPerLine];
	        for(int i = 0;i < noOfLines; i++)
	        {
	        	for(int j = 0;j < wordsPerLine; j++)
	        	{
	        		if(tok.nextToken() != StreamTokenizer.TT_EOF)
	        		{
	        			data[i][j] = (float) Util.parseDouble(tok.sval, Double.NaN);
	        		}
	        	}
	        }
    	}
        finally
        {
        	if(reader != null)
        	{
        		try
        		{
        			reader.close();
        		}
        		catch(Exception e)
        		{}
        	}
        }

        return data;
    }
    
    private int fire(byte[] reds, byte[] greens, byte[] blues) 
    {
		int[] r = {0,0,1,25,49,73,98,122,146,162,173,184,195,207,217,229,240,252,255,255,255,255,255,255,255,255,255,255,255,255,255,255};
		int[] g = {0,0,0,0,0,0,0,0,0,0,0,0,0,14,35,57,79,101,117,133,147,161,175,190,205,219,234,248,255,255,255,255};
		int[] b = {0,61,96,130,165,192,220,227,210,181,151,122,93,64,35,5,0,0,0,0,0,0,0,0,0,0,0,35,98,160,223,255};
		for (int i=0; i<r.length; i++) {
			reds[i] = (byte)r[i];
			greens[i] = (byte)g[i];
			blues[i] = (byte)b[i];
		}
		return r.length;
	}

	private int grays(byte[] reds, byte[] greens, byte[] blues)
	{
		for (int i=0; i<256; i++) 
		{
			reds[i] = (byte)i;
			greens[i] = (byte)i;
			blues[i] = (byte)i;
		}
		return 256;
	}
	
	private int primaryColor(int color, byte[] reds, byte[] greens, byte[] blues)
	{
		for (int i=0; i<256; i++)
		{
			if ((color&4)!=0)
				reds[i] = (byte)i;
			if ((color&2)!=0)
				greens[i] = (byte)i;
			if ((color&1)!=0)
				blues[i] = (byte)i;
		}
		return 256;
	}
	
	private int ice(byte[] reds, byte[] greens, byte[] blues)
	{
		int[] r = {0,0,0,0,0,0,19,29,50,48,79,112,134,158,186,201,217,229,242,250,250,250,250,251,250,250,250,250,251,251,243,230};
		int[] g = {156,165,176,184,190,196,193,184,171,162,146,125,107,93,81,87,92,97,95,93,93,90,85,69,64,54,47,35,19,0,4,0};
		int[] b = {140,147,158,166,170,176,209,220,234,225,236,246,250,251,250,250,245,230,230,222,202,180,163,142,123,114,106,94,84,64,26,27};
		
		for (int i=0; i<r.length; i++) 
		{
			reds[i] = (byte)r[i];
			greens[i] = (byte)g[i];
			blues[i] = (byte)b[i];
		}
		return r.length;
	}

	private int spectrum(byte[] reds, byte[] greens, byte[] blues)
	{
		Color c;
		for (int i=0; i<256; i++)
		{
			c = Color.getHSBColor(i/255f, 1f, 1f);
			reds[i] = (byte)c.getRed();
			greens[i] = (byte)c.getGreen();
			blues[i] = (byte)c.getBlue();
		}
		return 256;
	}
	
	private int rgb332(byte[] reds, byte[] greens, byte[] blues)
	{
		Color c;
		for (int i=0; i<256; i++) {
			reds[i] = (byte)(i&0xe0);
			greens[i] = (byte)((i<<3)&0xe0);
			blues[i] = (byte)((i<<6)&0xc0);
		}
		return 256;
	}

	private int redGreen(byte[] reds, byte[] greens, byte[] blues) 
	{
		for (int i=0; i<128; i++) {
			reds[i] = (byte)(i*2);
			greens[i] = (byte)0;
			blues[i] = (byte)0;
		}
		for (int i=128; i<256; i++) {
			reds[i] = (byte)0;
			greens[i] = (byte)(i*2);
			blues[i] = (byte)0;
		}
		return 256;
	}

	private void interpolate(byte[] reds, byte[] greens, byte[] blues, int nColors) 
	{
		byte[] r = new byte[nColors]; 
		byte[] g = new byte[nColors]; 
		byte[] b = new byte[nColors];
		
		System.arraycopy(reds, 0, r, 0, nColors);
		System.arraycopy(greens, 0, g, 0, nColors);
		System.arraycopy(blues, 0, b, 0, nColors);
		
		double scale = nColors/256.0;
		int i1, i2;
		double fraction;
		
		for (int i=0; i<256; i++)
		{
			i1 = (int)(i*scale);
			i2 = i1+1;
			
			if (i2==nColors) i2 = nColors-1;
			fraction = i*scale - i1;
			
			reds[i] = (byte)((1.0-fraction)*(r[i1]&255) + fraction*(r[i2]&255));
			greens[i] = (byte)((1.0-fraction)*(g[i1]&255) + fraction*(g[i2]&255));
			blues[i] = (byte)((1.0-fraction)*(b[i1]&255) + fraction*(b[i2]&255));
		}
	}
	
	private String getLUTName(String name)
	{
		int indexOfDot = name.lastIndexOf('.');
		
		if(indexOfDot != -1)
		{
			name = name.substring(0, indexOfDot);
		}
		return name;
	}
	
	/** 
	 * Opens the specified ImageJ LUT and returns
	 * it as an IndexColorModel. Since 1.43t. 
	 */
	public LUT open(String path) throws IOException 
	{
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(path));
		try
		{
			return open(inStream);
		}
		finally
		{
			Util.closeStream(inStream);
		}
	}
	
	/** 
	 * Opens an ImageJ LUT using an InputStream
	 * and returns it as an IndexColorModel. Since 1.43t. 
	 */
	public LUT open(InputStream stream) throws IOException 
	{
		DataInputStream f = new DataInputStream(stream);
		
		byte[] reds = new byte[256]; 
		byte[] greens = new byte[256]; 
		byte[] blues = new byte[256];
		
		f.read(reds, 0, 256);
		f.read(greens, 0, 256);
		f.read(blues, 0, 256);
		
		return new LUT(8, 256, reds, greens, blues);
	}
	
	static interface Resource 
	{
		public long length() throws IOException;
		
		public InputStream getInputStream() throws IOException;
	}
	
	static class FileResource implements Resource
	{
		private File sourceFile;
		
		public FileResource(File f)
		{
			sourceFile = f.getAbsoluteFile();
		}

		@Override
		public long length() throws IOException 
		{
			return sourceFile.length();
		}

		@Override
		public InputStream getInputStream() throws IOException 
		{
			return new FileInputStream(sourceFile);
		}
	}
	
	static class ClassResource implements Resource
	{
		private String resourceName;
		
		public ClassResource(String name)
		{
			resourceName = name;
		}

		@Override
		public long length() throws IOException 
		{
			BufferedInputStream inStream = new BufferedInputStream(getInputStream());
	        int counter = 0, fileSize = 0;
	        byte[] buffer = new byte[512];
	        
	        while((counter=inStream.read(buffer)) != -1)
	        {
	        	fileSize += counter;
	        }
	        inStream.close();
	        return fileSize;
		}

		@Override
		public InputStream getInputStream() throws IOException 
		{
	        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
	        return contextLoader.getResourceAsStream(resourceName);
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		LutLoader loader = LutLoader.getInstance();
		List<String> luts = loader.getAvailableLUTs();
		
		for(String name : luts)
		{
			LUT lut = loader.getLUT(name);
			System.out.println("successfully found lut "+name);
			BufferedImage lutImage = loader.getLUTImage(name);
			ImageIO.write(lutImage, "PNG", new File("D:\\temp", name+".png"));
		}
	}
}
