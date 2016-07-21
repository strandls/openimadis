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

import java.io.Serializable;
import java.util.Date;

import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.image.PixelDepth;

/**
 * fields that uniquely identify a record
 * @author arunabha
 *
 */
public class RecordIdentifier implements Serializable {
	
	private static final long serialVersionUID = 952136024889561097L;
	/**
	 * the record label token
	 */
	public final String recordLabel;
	/**
	 * image width
	 */
	public final int imageWidth;
	/**
	 * image height
	 */
	public final int imageHeight;
	/**
	 * pixel depth of the images
	 */
	public final PixelDepth depth;
	/**
	 * type of the image, RGB or GRAYSCALE
	 */
	public final ImageType imageType;
	/**
	 * checks if the images are little endians
	 */
	public final boolean isLittleEndian;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	public final double pixelSizeAlongXAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	public final double pixelSizeAlongYAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * for z-axis movement, each slice move by this physical value
	 * value is in microns
	 */
	public final double pixelSizeAlongZAxis;
	/**
	 * actual acquisition time of this record 
	 */
	public final Date acquiredTime;
	/**
	 * multi image coordinate
	 */
	public final FieldType multiImageCoordinate;
	
	public RecordIdentifier(String recordLabel,
			int imageWidth, int imageHeight, PixelDepth depth, ImageType type, boolean isLittleEndian,
			double sizeX, double sizeY, double sizeZ, Date acquiredTime, FieldType multiImageCoordinate)
	{
		this.recordLabel = recordLabel;
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		this.depth = depth;
		this.imageType = type;
		
		this.isLittleEndian = isLittleEndian;
		
		this.pixelSizeAlongXAxis = sizeX;
		this.pixelSizeAlongYAxis = sizeY;
		this.pixelSizeAlongZAxis = sizeZ;
		
		this.acquiredTime = acquiredTime;
		
		this.multiImageCoordinate = multiImageCoordinate;
	}
	
	@Override
	public String toString()
	{
		return recordLabel;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof RecordIdentifier)
		{
			RecordIdentifier that = (RecordIdentifier) obj;
			if(this == that) return true;
			
			return (this.recordLabel.equals(that.recordLabel)
					&& this.depth == that.depth
					&& this.imageType == that.imageType 
					&& this.imageWidth == that.imageWidth
					&& this.imageHeight == that.imageHeight
					&& this.isLittleEndian == that.isLittleEndian
//					&& this.pixelSizeAlongXAxis == that.pixelSizeAlongXAxis
//					&& this.pixelSizeAlongYAxis == that.pixelSizeAlongYAxis
//					&& this.pixelSizeAlongZAxis == that.pixelSizeAlongZAxis
					//&& Util.safeEquals(this.acquiredTime, that.acquiredTime)
					);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return recordLabel.hashCode();
	}
}
