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

package com.strandgenomics.imaging.iengine.export;

import java.util.List;

import com.strandgenomics.imaging.icore.Storable;

/**
 * identifies export request
 * 
 * @author Anup Kulkarni
 */
public class ExportRequest implements Storable{
	/**
	 * unique identifier for export request
	 */
	public final long requestId;
	/**
	 * name for the export
	 */
	public final String name;
	/**
	 * specified records
	 */
	private List<Long> guids;
	/**
	 * format in which record is exported
	 */
	public final ExportFormat format;
	/**
	 * validity time of the exported record
	 */
	public final long validTill;
	/**
	 * user who submitted record export request
	 */
	public final String submittedBy;
	/**
	 * the time when request is submitted
	 */
	public final long submittedOn;
	/**
	 * size of the download archive
	 */
	public final long size;
	/**
	 * location where record is exported
	 */
	private String url;
	/**
	 * status of request
	 */
	private ExportStatus status;
	
	public ExportRequest(long requestId, String submittedBy, List<Long> guids, ExportFormat format, long validTill, long submittedOn, long size, String name, ExportStatus status)
	{
		this.name = name;
		this.requestId = requestId;
		this.guids = guids;
		this.format = format;
		this.validTill = validTill;
		this.submittedBy = submittedBy;
		this.submittedOn = submittedOn;
		this.status = status;
		this.size = size;
	}
	
	public String getURL()
	{
		return this.url;
	}
	
	public void setURL(String url)
	{
		this.url = url;
	}
	
	public List<Long> getRecordIds()
	{
		return this.guids;
	}
	
	public void setStatus(ExportStatus status)
	{
		this.status = status;
	}
	
	public ExportStatus getStatus()
	{
		return this.status;
	}
	
	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}
}
