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

package com.strandgenomics.imaging.iengine.zoom;

import java.io.File;
import java.io.IOException;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Encapsulates parameters for Image Tiling Task
 * 
 * @author Anup Kulkarni
 */
public class TilingRequest {

	/**
	 * user
	 */
	private final String actor;
	/**
	 * record id
	 */
	private final long guid;
	/**
	 * dimension
	 */
	private final Dimension dim;
	/**
	 * record
	 */
	private final Record record;
	/**
	 * 
	 */
	private final String location;
	
	public TilingRequest(String actor, long guid, Dimension dim) throws IOException
	{
		this.actor = actor;
		this.guid = guid;
		this.dim = dim;
		
		File storageLocation = new File(Constants.getZoomStorageLocation(), "RecordID_"+String.valueOf(guid));
		location = storageLocation.getAbsolutePath();
		
		record = SysManagerFactory.getRecordManager().findRecord(actor, guid);
	}
	
	/**
	 * returns location where the images should be stored
	 * @return location where the images should be stored
	 */
	public File getStorageRoot()
	{
		File root = new File(location);
		root.mkdirs();
		return root;
	}
	
	public int getChannelCount()
	{
		return this.record.numberOfChannels;
	}
	
	public int getRecordWidth()
	{
		return this.record.imageWidth;
	}
	
	public int getRecordHeight()
	{
		return this.record.imageHeight;
	}
	
	public long getRecordId(){
		return this.record.guid;
	}
	
	public int getProjectId(){
		return this.record.projectID;
	}
	
	public String getRecordFileName(){
		return this.record.sourceFilename;
	}
	
	public long getGuid()
	{
		return guid;
	}

	public String getActor()
	{
		return actor;
	}

	public Dimension getDim()
	{
		return dim;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("RecordId=");
		sb.append(this.guid);
		sb.append(";");
		sb.append("Dimension=");
		sb.append(this.dim);
		
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof TilingRequest)
		{
			TilingRequest that = (TilingRequest) obj;
			return (this.guid == that.guid) && (this.dim.equals(that.dim));
		}
		
		return false;
	}
}