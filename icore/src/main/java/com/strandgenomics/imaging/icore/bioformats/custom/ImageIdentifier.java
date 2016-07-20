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

/*
 * ImageIdentifier.java
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

package com.strandgenomics.imaging.icore.bioformats.custom;

import java.io.Serializable;
import java.util.Date;

import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.image.PixelDepth;

/**
 * ImageFields encapsulating fields specifying image dimension of a record as extracted from filename
 * 
 * @author Anup Kulkarni
 */
public class ImageIdentifier implements Serializable
{
	private static final long serialVersionUID = -3006067551171471319L;
	
	/**
	 * the file that generated this Image
	 */
	public final String filename;
	/**
	 * the frame token
	 */
	public final String frame;
	/**
	 * the channel token
	 */
	public final String channel;
	/**
	 * the site token
	 */
	public final String site;
	/**
	 * the slice token
	 */
	public final String slice;
	/**
	 * position of the image
	 */
	public final double positionX, positionY, positionZ;
	/**
	 * exposure times etc
	 */
	public final double deltaTime, exposureTime;
	/**
	 * the record identifier
	 */
	public final RecordIdentifier recordID; 
	
	/**
	 * The fields the are needed/extracted for specific image files
	 * @param fileName the source image file
	 * @param recordLabel the record label, not null
	 * @param frame the frame indicator
	 * @param slice the slice indicator
	 * @param channel the channel indicator
	 * @param site the site indicator
	 * @param imageWidth image width
	 * @param imageHeight image height
	 * @param depth depth of pixels, 8 bits, 16 bits etc
	 * @param type image type, RGB or GRAYSCALE
	 * @param isLittleEndian 
	 * @param sizeX size of a pixel along x axis
	 * @param sizeY size of a pixel along y axis
	 * @param sizeZ size of a pixel along z axis
	 * @param positionX  position of this image along x axis
	 * @param positionY  position of this image along y axis
	 * @param positionZ  position of this image along z axis
	 * @param deltaTime the delta time (from the start)
	 * @param exposureTime the exposure time
	 * @param acquiredTime the acquisition time
	 * @param multiImageCoordinate the dimension(F,Z,C,S) used for coordinate inside multi image tiff
	 */
	public ImageIdentifier(String fileName, String recordLabel,
			String frame, String slice, String channel, String site, 
			int imageWidth, int imageHeight, PixelDepth depth, ImageType type, boolean isLittleEndian,
			double sizeX, double sizeY, double sizeZ,
			double positionX, double positionY, double positionZ,
			double deltaTime, double exposureTime, Date acquiredTime, FieldType multiImageCoordinate)
	{
		this.filename = fileName;
		this.frame = frame;
		this.slice = slice;
		this.channel = channel;
		this.site = site;
		
		this.positionX = positionX;
		this.positionY = positionY;
		this.positionZ = positionZ;
		
		this.deltaTime = deltaTime;
		this.exposureTime = exposureTime;
		
		this.recordID = new RecordIdentifier(recordLabel,
				imageWidth, imageHeight, depth, type, isLittleEndian,
				sizeX, sizeY, sizeZ, acquiredTime, multiImageCoordinate);
	}
	
	/**
	 * Checks whether the specified image identifier and this image identifier 
	 * form a record
	 * @param that the image identifier to check with
	 * @return true iff this and that ImageIdentifier can be part of the same Record
	 */
	public boolean isFromSameRecord(ImageIdentifier that)
	{
		return this.recordID.equals(that.recordID);
	}
	
	
	@Override
	public String toString()
	{
		return filename;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof ImageIdentifier)
		{
			ImageIdentifier that = (ImageIdentifier) obj;
			if(this == that) return true;
			
			return filename.equals(that.filename) && this.channel.equals(that.channel) && this.frame.equals(that.frame) && this.slice.equals(that.slice) && this.site.equals(that.site);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return filename.hashCode();
	}
}
