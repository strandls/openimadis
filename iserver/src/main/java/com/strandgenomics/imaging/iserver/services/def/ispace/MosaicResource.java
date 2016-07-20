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

package com.strandgenomics.imaging.iserver.services.def.ispace;

public class MosaicResource {
	
	public MosaicResource() {}

	/**
	 * records which are being used to create stitched image
	 */
	private Long[] recordids;
	
	/**
	 * the coordinate which represents the top most point of stitched image
	 */
	private int anchor_left;
	
	/**
	 * the coordinate which represents the left most point of stitched image
	 */
	private int anchor_top;
	
	/**
	 * width of resultant mosaic image
	 */
	private int mosaicImageWidth;
	
	/**
	 * height of resultant mosaic image
	 */
	private int mosiacImageHeight;
	
	public Long[] getRecordids() {
		return recordids;
	}

	public void setRecordids(Long[] recordids) {
		this.recordids = recordids;
	}

	public int getAnchor_left() {
		return anchor_left;
	}

	public void setAnchor_left(int anchor_left) {
		this.anchor_left = anchor_left;
	}

	public int getAnchor_top() {
		return anchor_top;
	}

	public void setAnchor_top(int anchor_top) {
		this.anchor_top = anchor_top;
	}

	public int getMosaicImageWidth() {
		return mosaicImageWidth;
	}

	public void setMosaicImageWidth(int mosaicImageWidth) {
		this.mosaicImageWidth = mosaicImageWidth;
	}

	public int getMosiacImageHeight() {
		return mosiacImageHeight;
	}

	public void setMosiacImageHeight(int mosiacImageHeight) {
		this.mosiacImageHeight = mosiacImageHeight;
	}
}
