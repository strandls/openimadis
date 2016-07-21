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

package com.strandgenomics.imaging.iserver.services.def.loader;


/**
 * Specification of the record to be created by the server
 * Specifies the sites that needs to be merged, additionally specifies the
 * custom channel colors and contrast
 * @author arunabha
 *
 */
public class RecordSpecification {
	
	/**
	 * List of sites to merge into a single record
	 */
	private RecordSite[] siteToMerge = null;
	/**
	 * custom channel specifications for the record
	 */
	private Channel[] customChannels = null;
	
	public RecordSpecification()
	{}

	/**
	 * @return the siteToMerge
	 */
	public RecordSite[] getSiteToMerge()
	{
		return siteToMerge;
	}

	/**
	 * @param siteToMerge the siteToMerge to set
	 */
	public void setSiteToMerge(RecordSite[] siteToMerge)
	{
		this.siteToMerge = siteToMerge;
	}

	/**
	 * @return the customColors
	 */
	public Channel[] getCustomChannels() 
	{
		return customChannels;
	}

	/**
	 * @param customColors the customColors to set
	 */
	public void setCustomChannels(Channel[] customColors)
	{
		this.customChannels = customColors;
	}
}
