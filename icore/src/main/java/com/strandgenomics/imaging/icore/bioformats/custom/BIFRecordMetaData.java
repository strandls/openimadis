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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import loci.formats.FormatTools;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.image.PixelDepth;
public class BIFRecordMetaData {


	private static final long serialVersionUID = 8033911466792641496L;
	
	/**
	 * dimension order is xyzct
	 */
	public static final String dimensionOrder = "XYZCT";
	/**
	 * name extracted from the file (as record label)
	 */
	public  String name;
	/**
	 * total number of frames
	 */
	protected int noOfFrames;
	/**
	 * total number of sites
	 */
	protected int noOfSites;
	/**
	 * total number of slices
	 */
	protected int noOfSlices;
	/**
	 * total number of channels
	 */
	protected int noOfChannels;
	/**
	 * potential name of the channels as extracted from the file names
	 */
	protected List<String> channelNames;
	/**
	 * potential name of the sites as extracted from the file names
	 */
	protected List<String> siteNames;
	/**
	 * image width
	 */
	protected int imageWidth;
	/**
	 * image height
	 */
	protected int imageHeight;
	/**
	 * pixel depth of the images
	 */
	protected PixelDepth depth = null;
	/**
	 * type of the image, RGB or GRAYSCALE
	 */
	protected ImageType imageType = null;
	/**
	 * checks if the images are little endians
	 */
	protected boolean isLittleEndian = false;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected double pixelSizeAlongXAxis = 0.0;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected double pixelSizeAlongYAxis = 0.0;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * for z-axis movement, each slice move by this physical value
	 * value is in microns
	 */
	protected double pixelSizeAlongZAxis = 0.0;
	/**
	 * actual acquisition time of this record 
	 */
	protected Date acquiredTime = null;
	
	/**
	 * dimensions against image files
	 */
	protected TreeMap<Dimension, ImageIdentifier> imageDimnsions = null;
	/**
	 * the root directory where the image files are located
	 */
	protected File rootDirectory = null;
	/**
	 * the file containing the dump of this data
	 */
	protected File seedFile = null;
	
	protected List<Site> sites = null;
	protected List<Channel> channels = null;

	
	/**
	 * Creates a CustomRecord with the specified name
	 * @param recordLabel name of the record
	 */
	public BIFRecordMetaData(File seedFile)
	{
		/*this.name = id.recordLabel;
		this.depth = id.depth;
		this.imageType = id.imageType;
		this.imageWidth = id.imageWidth;
		this.imageHeight = id.imageHeight;
		this.isLittleEndian = id.isLittleEndian;
		this.pixelSizeAlongXAxis = id.pixelSizeAlongXAxis;
		this.pixelSizeAlongYAxis = id.pixelSizeAlongYAxis;
		this.pixelSizeAlongZAxis = id.pixelSizeAlongZAxis;
		this.acquiredTime = id.acquiredTime;
		
		this.rootDirectory = rootDirectory.getAbsoluteFile();
		
		this.multiImageType = id.multiImageCoordinate;
		//extract the record dimensions
		this.imageDimnsions = extractDimensions(images);*/
		
		if(seedFile == null || !seedFile.isFile()) return;
		
		File rootDirectory = seedFile.getParentFile().getAbsoluteFile();
		
		int noOfFrames = 1;
		int noOfChannels = 3;
		int noOfSlices = 1;
		int noOfSites = 1;
		
		List<String>channelNames = new ArrayList<String>();
		channelNames.add("0");
		channelNames.add("1");
		channelNames.add("2");
		List<String>siteNames = null;
		double pixelSizeAlongXAxis = 0.0;
		double pixelSizeAlongYAxis = 0.0;
		double pixelSizeAlongZAxis = 0.0;
		
		PixelDepth pixelDepth = PixelDepth.SHORT;
		ImageType imageType = ImageType.GRAYSCALE;
		String recordLabel = null;
		boolean isLittleEndian = false;
		Date acquiredTime = null;
		int imageWidth = 701;
		int imageHeight = 383;
		
		this.name= "SI09";
		
		this.channelNames = channelNames;
		this.siteNames = siteNames;
		
		this.noOfFrames = noOfFrames;
		this.noOfChannels = noOfChannels;
		this.noOfSlices = noOfSlices;
		this.noOfSites = noOfSites;
		
		this.depth = pixelDepth;
		this.imageType = imageType;
		this.isLittleEndian = isLittleEndian;
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		this.pixelSizeAlongXAxis = pixelSizeAlongXAxis;
		this.pixelSizeAlongYAxis = pixelSizeAlongYAxis;
		this.pixelSizeAlongZAxis = pixelSizeAlongZAxis;
	}
	
	
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public File getRootDirectory()
	{
		return rootDirectory;
	}
	
	public File getSeed() throws IOException
	{
		if(seedFile == null)
		{
			seedFile = File.createTempFile(name+"_metadata", ".img");
			write(seedFile);
		}
		
		return seedFile;
	}
	
	public Dimension getSampleDimension()
	{
		return imageDimnsions.firstKey();
	}
	
	/**
	 * Returns the image file corresponding to the specified dimension
	 * @param d the dimension under consideration
	 * @return the source file for this dimension
	 */
	public File getImageFile(Dimension d)
	{
		if( imageDimnsions.containsKey(d) )
			return new File(rootDirectory, imageDimnsions.get(d).filename).getAbsoluteFile();
		else
			return null;
	}
	
	public boolean isLittleEndian() 
	{
		return isLittleEndian;
	}
	
    /**
     * Returns the number of Z-axis slices (planes) supported by this record
     */
    public int getSliceCount()
    {
    	return noOfSlices;
    }
    
    /**
     * Returns the maximum number of frames (time samples) associated with this record
     */
    public int getFrameCount()
    {
    	return noOfFrames;
    }
    
    /**
     * Returns the number of different channels available with this record
     */
    public int getChannelCount()
    {
    	return noOfChannels;
    }
    
    /**
     * Returns the number of different sites available with this record
     */
    public int getSiteCount()
    {
    	return noOfSites;
    }
	
	/**
	 * Returns the number of files associated with this record
	 * @return the number of files associated with this record
	 */
	public int getImageCount()
	{
		return imageDimnsions.size();
	}
	
	public int getImageWidth()
	{
		return this.imageWidth;
	}
	
	public int getImageHeight()
	{
		return this.imageHeight;
	}
	
	/**
	 * Returns the list of files associated with this record
	 * @return the list of files associated with this record
	 */
	public Set<File> getFiles()
	{
		Set<File> files = new HashSet<File>();
		for(ImageIdentifier f : imageDimnsions.values())
		{
			files.add(new File(rootDirectory, f.filename).getAbsoluteFile());
		}
		
		return files;
	}
	
	public ImageIdentifier getImageMetaData(Dimension d)
	{
		return imageDimnsions.get(d);
	}
	
	/**
	 * Once all the image files are added, call this method to extract
	 * the various dimensions associated with this record
	 */
	private TreeMap<Dimension, ImageIdentifier> extractDimensions(Set<ImageIdentifier> imageFiles)
	{
		Set<String> frameNames   = new HashSet<String>(); //distinct frame names (may be a number)
		Set<String> sliceNames   = new HashSet<String>(); //distinct slice names (may be a number)
		Set<String> channelNames = new HashSet<String>(); //distinct channel names (may be a number)
		Set<String> siteNames    = new HashSet<String>(); //distinct sites names (may be a number)
		
		//for each potential image file discovered get the unique list of frame,slice,channels and sites
		for(ImageIdentifier fields : imageFiles)
		{
			frameNames.add(fields.frame);
			sliceNames.add(fields.slice);
			channelNames.add(fields.channel);
			siteNames.add(fields.site);
		}
		
		//frame-names and their (numeric) sorted position -> frame number
		Map<String, Integer> frameNamesToNumMap   = toSortedMap(frameNames);
		//slice-names and their (numeric) sorted position
		Map<String, Integer> sliceNamesToNumMap   = toSortedMap(sliceNames);
		//channel-names and their (numeric) sorted position
		Map<String, Integer> channelNamesToNumMap = toSortedMap(channelNames);
		//site-names and their (numeric) sorted position
		Map<String, Integer> siteNamesToNumMap    = toSortedMap(siteNames);
		
		this.noOfFrames   = frameNames.size();
		this.noOfSlices   = sliceNames.size();
		this.noOfChannels = channelNames.size();
		this.noOfSites    = siteNames.size();
		
		this.channelNames = getNames(channelNamesToNumMap);
		this.siteNames = getNames(siteNamesToNumMap);
		
		TreeMap<Dimension, ImageIdentifier> localImageDimnsions = new TreeMap<Dimension, ImageIdentifier>();
		//now set the dimensions to the images
		for(ImageIdentifier image : imageFiles)
		{
			int frame   = frameNamesToNumMap.get(image.frame);
			int slice   = sliceNamesToNumMap.get(image.slice);
			int channel = channelNamesToNumMap.get(image.channel);
			int site    = siteNamesToNumMap.get(image.site);
			
			//the actual dimension of the image within this record
			Dimension imageDimension = new Dimension(frame, slice, channel, site);
			localImageDimnsions.put(imageDimension, image);
		}
		
		return localImageDimnsions;
	}

	/**
	 * Sorts the names  in numeric order if possible 
	 * else with alphabetic order and sets the indices in the sorted order as values for those names 
	 */
	private Map<String,Integer> toSortedMap(Set<String> names) 
	{
		String[] array = names.toArray(new String[0]);
		Arrays.sort(array, new StringComparator());
		
		Map<String,Integer> positionMap = new HashMap<String, Integer>();
		for(int i = 0; i < array.length; i++)
		{
			positionMap.put(array[i], i);
		}
		return positionMap;
	}
	
	private List<String> getNames(Map<String, Integer> namesToNumMap)
	{
		String[] names = new String[namesToNumMap.size()];
		for(Map.Entry<String, Integer> entry : namesToNumMap.entrySet())
		{
			names[entry.getValue()] = entry.getKey();
		}
		
		return Arrays.asList(names);
	}
	
	/**
	 * String comparator used to sort StringHashMap to get int values of
	 * dimension fields obtained from filename fields
	 * 
	 * @author Devendra/Yogi
	 */
	static class StringComparator implements Comparator<String>
	{
		Pattern pattern = Pattern.compile("[0-9]+");
		
		@Override
		public int compare(String o1, String o2)
		{
			 Matcher matcher1 = pattern.matcher(o1);
			 boolean isPresent1 = matcher1.find();
			  
			 Matcher matcher2 = pattern.matcher(o2);
			 boolean isPresent2 = matcher2.find();
			  
			 if(isPresent1 && isPresent2)
			 {
				 long no1=Long.parseLong(matcher1.group());
				 long no2=Long.parseLong(matcher2.group());
				  
				 if(no1 > no2)
				 {
					 return 1;
				 }
				 else if(no1<no2)
				 {
					 return -1;
				 }
				 else
				 {
					 return 0;
				 }
			 }
			 return o1.compareTo(o2);
		 }
	}

	public Date getSourceTime() 
	{
		return new Date(getImageFile(getSampleDimension()).lastModified());
	}

	public PixelDepth getPixelDepth() 
	{
		return depth;
	}

	public double getPixelSizeX() 
	{
		return pixelSizeAlongXAxis;
	}

	public double getPixelSizeY() 
	{
		return pixelSizeAlongYAxis;
	}

	public double getPixelSizeZ()
	{
		return pixelSizeAlongZAxis;
	}

	public ImageType getImageType() 
	{
		return imageType;
	}

	public SourceFormat getSourceFormat()
	{
		return new SourceFormat("bif");
	}
	
	/**
	 * Returns the available channels
	 * @return
	 */
	public synchronized List<Channel> getChannels()
	{
		if(channels == null)
		{
			channels = new ArrayList<Channel>(); 
			
			String[] primaryColorLUTs = LutLoader.getInstance().getPrimaryColorLUTs();
			int noOfColors = primaryColorLUTs.length;
			
			for(int i = 0;i < channelNames.size(); i++)
			{
				String channelName = channelNames.get(i).trim();
				Integer channel = toInteger(channelName);
				
				if(channel != null) 
					channelName = "Channel " + i;
			
				String lutName = noOfChannels == 1 ? LutLoader.STD_LUTS[0] : primaryColorLUTs[i%noOfColors];
				channels.add( new Channel(channelName, lutName) );
			}
		}
		
		return channels;
	}

	/**
	 * Returns the available sites
	 * @return
	 */
	public synchronized List<Site> getSites()
	{
		if(sites == null)
		{
			sites = new ArrayList<Site>();
			for(int i = 0;i < siteNames.size(); i++)
			{
				String siteName = siteNames.get(i).trim();
				Integer site = toInteger(siteName);
				
				if(site != null) 
					siteName = "Site " + i;
				
				sites.add( new Site(i, siteName) );
			}
		}
		
		return sites;
	}
	
	private Integer toInteger(String name)
	{
		try
		{
			return Integer.parseInt(name);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * write out (serialize) this RecordMetaData as txt file
	 * @param folder the folder to write to
	 * @return the file where it is written
	 */
	public void write(File metaSourceFile) throws IOException
	{
		PrintWriter writer = null;
		
		try
		{
			writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(metaSourceFile, true)));
			StringBuilder builder = new StringBuilder();
			
			//write the number of frames, slices, channels and sites
			builder.append("NoOfFrames");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(noOfFrames);
			writer.println(builder.toString());
			builder.setLength(0);
			
			builder.append("NoOfSlices");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(noOfSlices);
			writer.println(builder.toString());
			builder.setLength(0);
			
			builder.append("NoOfChannels");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(noOfChannels);
			writer.println(builder.toString());
			builder.setLength(0);
			
			builder.append("NoOfSites");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(noOfSites);
			writer.println(builder.toString());
			builder.setLength(0);
			
			builder.append("MultiImageCoordinate");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(multiImageType.name());
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write the channel names
			builder.append("ChannelNames");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			for(String channelName:channelNames)
			{
				builder.append(channelName);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
			}
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write the site names
			builder.append("SiteNames");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			for(String siteName:siteNames)
			{
				builder.append(siteName);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
			}
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write the isLittleEndian
			builder.append("isLittleEndian");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(isLittleEndian);
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write the pixel depth
			builder.append("PixelDepth");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(depth);
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write the pixel size along x,y, and z axis
			builder.append("PixelSizeAlongXAxis");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(pixelSizeAlongXAxis);
			writer.println(builder.toString());
			builder.setLength(0);
			
			builder.append("PixelSizeAlongYAxis");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(pixelSizeAlongYAxis);
			writer.println(builder.toString());
			builder.setLength(0);
			
			builder.append("PixelSizeAlongZAxis");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(pixelSizeAlongZAxis);
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write the image width and height
			builder.append("ImageWidth");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(imageWidth);
			writer.println(builder.toString());
			builder.setLength(0);
			
			builder.append("ImageHeight");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(imageHeight);
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write image type
			builder.append("ImageType");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(imageType);
			writer.println(builder.toString());
			builder.setLength(0);

			//write record label
			builder.append("RecordLabel");
			builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
			builder.append(name);
			writer.println(builder.toString());
			builder.setLength(0);
			
			//write record acquired time
			if(this.acquiredTime != null)
			{
				builder.append("AcquiredTime");
				builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
				builder.append(acquiredTime.getTime());
				writer.println(builder.toString());
				builder.setLength(0);
			}
			
			//and finally, write the dimension files (this are the last entries)
			for (Map.Entry<Dimension, ImageIdentifier> entry : imageDimnsions.entrySet())
			{
				//write all for this image
				Dimension d = entry.getKey();
				ImageIdentifier image = entry.getValue();
				
				if(image == null)
				{
					System.err.println(d);
				}
				
				builder.append("Dimension");
				builder.append(ImgFormatConstants.FILENAME_SEPARATOR);
				
				//the frame number
				builder.append(d.frameNo);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the slice number
				builder.append(d.sliceNo);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the channel number
				builder.append(d.channelNo);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the site number
				builder.append(d.siteNo);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the source image filename
				builder.append(image.filename);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the position x
				builder.append(image.positionX);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the position y
				builder.append(image.positionY);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the position z
				builder.append(image.positionZ);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the delta time
				builder.append(image.deltaTime);
				builder.append(ImgFormatConstants.DIMENSION_SEPARATOR);
				//the exposure time
				builder.append(image.exposureTime);
				//the multiimage coordinate
				
				writer.println(builder.toString());
				builder.setLength(0);
			}
		}
		finally
		{
			writer.close();
		}
	}
	
	/**
	 * Assumes that record meta data are written before the image meta data per dimension
	 * loads a record meta instance from the seed file
	 * @param seedFile the seed file under consideration
	 * @return the reconstructed RecordMetaData instance
	 * @throws IOException 
	 */
	/*public static final BIFRecordMetaData load(File seedFile) throws IOException
	{
		if(seedFile == null || !seedFile.isFile()) return null;
		
		BufferedReader br = new BufferedReader(new FileReader(seedFile));
		File rootDirectory = seedFile.getParentFile().getAbsoluteFile();
		
		int noOfFrames = 1;
		int noOfChannels = 0;
		int noOfSlices = 1;
		int noOfSites = 1;
		
		List<String>channelNames = null;
		List<String>siteNames = null;
		
		double pixelSizeAlongXAxis = 0.0;
		double pixelSizeAlongYAxis = 0.0;
		double pixelSizeAlongZAxis = 0.0;
		
		PixelDepth pixelDepth = PixelDepth.BYTE;
		ImageType imageType = ImageType.RGB;
		String recordLabel = null;
		boolean isLittleEndian = false;
		Date acquiredTime = null;
		int imageWidth = 100;
		int imageHeight = 100;
		
		FieldType multiImageCoordinate = FieldType.IGNORE;
		
		TreeMap<Dimension, ImageIdentifier> imageDimnsions = new TreeMap<Dimension, ImageIdentifier>();
		
		
		
		BIFRecordMetaData rm = new BIFRecordMetaData(rootDirectory, recordLabel, imageDimnsions);
		
		rm.channelNames = channelNames;
		rm.siteNames = siteNames;
		
		rm.noOfFrames = noOfFrames;
		rm.noOfChannels = noOfChannels;
		rm.noOfSlices = noOfSlices;
		rm.noOfSites = noOfSites;
		
		rm.depth = pixelDepth;
		rm.imageType = imageType;
		rm.isLittleEndian = isLittleEndian;
		
		rm.imageWidth = imageWidth;
		rm.imageHeight = imageHeight;
		
		rm.pixelSizeAlongXAxis = pixelSizeAlongXAxis;
		rm.pixelSizeAlongYAxis = pixelSizeAlongYAxis;
		rm.pixelSizeAlongZAxis = pixelSizeAlongZAxis;
		
		return rm;
	}*/
	
	private transient HashMap<Integer,ZCT> indexToDimensionMap = null;

	/**
	 * indicates which dimension field to use if source file contains more than one image
	 */
	private FieldType multiImageType;
	
	/**
	 * Returns the dimension for the specified plane index and series
	 * @param seriesNo
	 * @param planeIndex
	 * @return
	 */
	public synchronized Dimension getDimension(int seriesNo, int planeIndex)
	{
		if(indexToDimensionMap == null)
			indexToDimensionMap = createIndexToDimensionMap();
		
		ZCT order = indexToDimensionMap.get(planeIndex);
		return order == null ? null : new Dimension(order.t, order.z, order.c, seriesNo);
	}
	
	private HashMap<Integer, ZCT> createIndexToDimensionMap() 
	{
		HashMap<Integer, ZCT> map = new HashMap<Integer, ZCT>(noOfFrames*noOfSlices*noOfChannels);
		
		for(int t = 0; t < noOfFrames; t++)
		{
			for(int z = 0;z < noOfSlices; z++)
			{
				for(int c = 0; c < noOfChannels; c++)
				{
					int index = getIndex(z, c, t);
					ZCT dimension = new ZCT(z,c,t);
					map.put(index, dimension);
				}
			}
		}
		
		return map;
	}
	
	public FieldType getMultiImageField()
	{
		if (this.multiImageType != null)
			return this.multiImageType;
		
		Set<Dimension> dim = new HashSet<Dimension>();
		String sampleFile = null;
		for(Entry<Dimension, ImageIdentifier> entry:imageDimnsions.entrySet())
		{
			if(sampleFile == null)
				sampleFile = entry.getValue().filename;
			
			if(sampleFile.equals(entry.getValue().filename))
				dim.add(entry.getKey());
		}
		
		if(dim.size()<=1)
		{
			this.multiImageType = FieldType.IGNORE;
		}
		else
		{
			Dimension d = null;
			for(Dimension m : dim)
			{
				if(d==null)
					d = m; // first one in the set
				else
				{
					if(d.channelNo != m.channelNo)
						this.multiImageType = FieldType.CHANNEL;
					else if(d.sliceNo != m.sliceNo)
						this.multiImageType = FieldType.SLICE;
					else if(d.frameNo != m.frameNo)
						this.multiImageType = FieldType.FRAME;
					else if(d.siteNo != m.siteNo)
						this.multiImageType = FieldType.SITE;
					else
						this.multiImageType = FieldType.IGNORE;
				}
			}
		}
		
		return this.multiImageType;
	}
	
	public int getIndex(int z, int c, int t)
	{
		int num = noOfSlices * noOfChannels * noOfFrames;
		return FormatTools.getIndex(dimensionOrder, noOfSlices, noOfChannels, noOfFrames, num, z, c, t);
	}
	
	private static class ZCT
	{
		public final int z,c,t;
		
		public ZCT(int z, int c, int t)
		{
			this.z = z;
			this.c = c;
			this.t = t;
		}
	}


}
