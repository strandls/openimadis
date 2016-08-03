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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openslide.OpenSlide;


import loci.formats.ChannelSeparator;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.ImageReader;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.MetadataStore;

public class BIFFormatReader extends FormatReader{

	private ImageReader actualReader = null;
	private BIFRecordMetaData parser;
	private File imgSourceFile;
	private BIFMetaDataStore bifMetaData = null;
	private OpenSlide os = null;
	private int imageHeight = 0;
	private int imageWidth = 0;
	
	public BIFFormatReader() {
		super("BIF", new String[] { "bif" });
		//System.out.println("BIF reader initialised.");
		suffixNecessary = true;
		suffixSufficient = true;
		domains = new String[] { FormatTools.UNKNOWN_DOMAIN };
	}
	public BIFFormatReader(BIFRecordMetaData parser) throws IOException, FormatException
	{
		this();
		this.parser = parser;
		this.imgSourceFile = parser.getSeed();
		initialize(imgSourceFile.getAbsolutePath());
	}
	private void initialize(String id) throws FormatException, IOException
	{
		actualReader = new ImageReader();
		//setCurrentFile(getSampleFilePath());
		super.initFile(id);
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
		
		imgSourceFile = new File(id).getAbsoluteFile();
		parser = new BIFRecordMetaData(imgSourceFile);
		os = new OpenSlide(new File(id));
		int level = 0;
		imageHeight = (int) os.getLevelHeight(level);
		imageWidth = (int) os.getLevelWidth(level);
		initialize(id);
	}
	
	@Override
	public int getIndex(int z, int c, int t)
	{
		  String order = getDimensionOrder();
		  int zSize = getSizeZ();
		  int cSize = getSizeC();
		  int tSize = getSizeT();
		  int num = zSize * cSize * tSize;
		  return FormatTools.getIndex(order, zSize, cSize, tSize, num, z, c, t);
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
	@Override
	public int getSizeZ() 
	{
		return 1;
	}
	@Override
	public int getSizeC() 
	{
		return 3;
	}

	@Override
	public int getImageCount()
	{
		return 3;
	}
	
	/**
	 * @return number of frames in img record
	 */
	@Override
	public int getSizeT() 
	{
		
		return 1;
	}
	@Override
	public String getDimensionOrder() 
	{
		return BIFRecordMetaData.dimensionOrder;
	}
	
	@Override
	public boolean isNormalized()
	{
		return false;
	}
	@Override
	public boolean isInterleaved() 
	{
		return false;
	}
	@Override
	public boolean isIndexed() 
	{
		return false;
	}
	@Override
	public boolean isLittleEndian() 
	{
		return false;
	}
	@Override
	public int getPixelType() 
	{
		return 1;
	}
	@Override
	public byte[] openBytes(int index) throws FormatException, IOException 
	{
		return actualReader.openBytes(index);
	}
	
	@Override
	public byte[] openBytes(int index, int x, int y, int w, int h) throws FormatException, IOException 
	{
		return actualReader.openBytes(index, x, y, w, h);
	}
	
	@Override
	public byte[] openBytes(int index, byte[] buf) throws FormatException, IOException 
	{
		return actualReader.openBytes(index, buf);
	}
	
	@Override
	public byte[] openBytes(int index, byte[] buf, int x, int y, int w, int h)
			throws FormatException, IOException {
		os = new OpenSlide(new File(currentId));
		BufferedImage img = new BufferedImage(w,h,
                BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
        int data[] = ((DataBufferInt) img.getRaster().getDataBuffer())
                .getData();

        try {
			os.paintRegionARGB(data, 0, 0, 0, w, h);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        os.close();
		System.out.println("here BIF op");
		
		File tmp = File.createTempFile("temp", "png");
		
		ImageIO.write(img, "png", tmp);
		
		BufferedImageReader imageReader = createImageReader(tmp);
    	int seriesCount = imageReader.getSeriesCount();
    	int sliceCount = imageReader.getSizeZ();
    	int channelCount = imageReader.getSizeC();
    	int frameCount = imageReader.getSizeT();
    	System.out.println(""+seriesCount+sliceCount+channelCount+frameCount+imageReader.getImageCount());
    	
    	byte[] temp = imageReader.openBytes(index);
    	try{
    	imageReader.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return temp;
	}
	@Override
	public Object openPlane(int no, int x, int y, int w, int h)
			throws FormatException, IOException {
		return null;
	}
	@Override
	public MetadataStore getMetadataStore() 
	{
		if(bifMetaData == null)
		{
			bifMetaData = new BIFMetaDataStore();
		}
		
		return bifMetaData;
	}
	
	
	
	private static BufferedImageReader createImageReader(File file){
		ChannelSeparator separator = new ChannelSeparator();
		BufferedImageReader imageReader = new BufferedImageReader(separator); 
		//imageReader.setOriginalMetadataPopulated(false);

		try
		{
			imageReader.setId(file.getAbsolutePath());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Logger.getRootLogger().error("Cannot create Format Handler ..",e);
		}

		return imageReader;
	}
}
