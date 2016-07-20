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

package com.strandgenomics.imaging.iengine.models;

/**
 * This class contains represents the input to the MosaicManager.
 * It contains all the data relevant for mosaic manager.
 * This class will be modified accordingly to provide required data to mosaic manager.
 * @author navneet
 *
 */
public class MosaicRequest {

	/**
	 * all the records which will be used to form final mosaic image
	 */
	private long[] recordids;

	public long[] getRecordids() {
		return recordids;
	}

	public void setRecordids(long[] recordids) {
		this.recordids = recordids;
	}
	
}
