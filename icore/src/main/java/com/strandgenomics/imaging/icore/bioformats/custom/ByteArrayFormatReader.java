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

package com.strandgenomics.imaging.icore.bioformats.custom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;

import com.strandgenomics.imaging.icore.image.PixelArray;

public class ByteArrayFormatReader extends FormatReader {
	
	public static final String METADATA_FILE_NAME = "metadata.rawimg";
	
	public static final String BYTE_ARRAY_FORMAT_EXTENSION = ".rawimg";
	
	/** Custom parser to read dimension to filename mapping */
	private RecordMetaData parser;
	/**
	 * series number
	 */
	private int currentSeriesNo = 0;
	/**
	 * max frames of the record
	 */
	private int maxFrame;
	/**
	 * max slicess of the record
	 */
	private int maxSlice;
	/**
	 * max channles of the record
	 */
	private int maxChannel;
	/**
	 * max sites of the record
	 */
	private int maxSite;
	/**
	 * image width for every image
	 */
	private int imageWidth;
	/**
	 * image height for every image
	 */
	private int imageHeight;
	/**
	 * pixel depth of every image
	 */
	private int pixelDepth;
	/**
	 * name of seed file
	 */
	private String seedFileName;
	
	public ByteArrayFormatReader() 
	{
		// Registers this reader for .rawimg filename extensions
		super("RAWIMG", new String[] { "rawimg" });
		suffixNecessary = true;
		suffixSufficient = true;
		domains = new String[] { FormatTools.UNKNOWN_DOMAIN };
	}
	
	/**
	 * Initializes the given file (parsing header information, etc.).
	 * Most subclasses should override this method to perform
	 * initialization operations such as parsing metadata.
	 */
	@Override
	protected void initFile(String id) throws FormatException, IOException 
	{
		if(parser != null) return;
		
		File imgSourceFile = new File(id).getAbsoluteFile();
		
		seedFileName = imgSourceFile.getAbsolutePath();
		
		readMetaDataFile(imgSourceFile);
		
		initialize(imgSourceFile.getAbsolutePath());
	}
	
	private void readMetaDataFile(File imgSourceFile) throws IOException
	{
		File metadata = new File(imgSourceFile.getParentFile(), METADATA_FILE_NAME);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(metadata)));
		int i=0;
		while(br.ready())
		{
			String line = br.readLine();
			System.out.println(line);
			if(i==0)
				maxFrame = Integer.parseInt(line);
			if(i==1)
				maxSlice = Integer.parseInt(line);
			if(i==2)
				maxChannel = Integer.parseInt(line);
			if(i==3)
				maxSite = Integer.parseInt(line);
			if(i==4)
				imageWidth = Integer.parseInt(line);
			if(i==5)
				imageHeight = Integer.parseInt(line);
			i++;
		}
	}

	private void initialize(String id) throws FormatException, IOException
	{
		super.initFile(id);
	}
	
	@Override
	public String getDimensionOrder() 
	{
		return RecordMetaData.dimensionOrder;
	}
	
	@Override
	public void setSeries(int no) 
	{
		currentSeriesNo = no;
	}
	
	@Override
	public int getSeries()
	{
		return currentSeriesNo;
	}
	
	@Override
	public int getSizeX() 
	{
		return imageWidth;
	}
	
	@Override
	public int getSizeY()
	{
		return imageHeight;
	}
	
	/**
	 * @return number of channels in img record
	 */
	@Override
	public int getEffectiveSizeC() 
	{
		System.out.println("effective c size "+maxChannel);
		return maxChannel;
	}
	
	/**
	 * @return number of channels in img record
	 */
	@Override
	public int getSizeC() 
	{
		System.out.println("get c size "+maxChannel);
		return maxChannel;
	}

	@Override
	public int getImageCount()
	{
		int ct = getSizeZ() * getSizeT() * getSizeC();
		return ct;
	}
	
	/**
	 * @return number of frames in img record
	 */
	@Override
	public int getSizeT() 
	{
		System.out.println("effective T size "+maxFrame);
		return maxFrame;
	}

	/**
	 * @return number of frames in img record
	 */
	@Override
	public int getSizeZ() 
	{
		System.out.println("effective Z size "+maxSlice);
		return maxSlice;
	}

	@Override
	public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
			throws FormatException, IOException
	{
		System.out.println("index = "+no);
		
		int zct[] = FormatTools.getZCTCoords(this, no);
		System.out.println("z = "+zct[0]);
		System.out.println("c = "+zct[1]);
		System.out.println("t = "+zct[2]);
		System.out.println("site = "+currentSeriesNo);
		
		File seedFile = new File(seedFileName);
		File dataFile = new File(seedFile.getParentFile(), zct[2]+"_"+zct[0]+"_"+zct[1]+"_"+currentSeriesNo+BYTE_ARRAY_FORMAT_EXTENSION);
		FileInputStream fin = new FileInputStream(dataFile);
		
		PixelArray originalData = PixelArray.read(fin);
		PixelArray subArr = originalData.getSubArray(x, y, w, h);
		
		return subArr.getBytes();
	}

	@Override
	public byte[] openBytes(int no) throws FormatException, IOException 
	{
		byte[] buff = new byte[50];
		return openBytes(no, buff, 0, 0, getSizeX(), getSizeY());
	}
	
	@Override
	public byte[] openBytes(int no, int x, int y, int w, int h) throws FormatException, IOException 
	{
		byte[] buff = new byte[50];
		return openBytes(no, buff, x, y, w, h);
	}
	
	@Override
	public byte[] openBytes(int no, byte[] buf) throws FormatException, IOException 
	{
		return openBytes(no, buf, 0, 0, getSizeX(), getSizeY());
	}
}
