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

package com.strandgenomics.imaging.iclient.util;

/**
 * The Exception that is generally thrown when illegal configurations are set in the UI
 * @author arunabha
 *
 */
public class ConfigurationException extends Exception {
	
	private static final long serialVersionUID = 1659802333527270067L;
	
	protected String category;
	protected String categoryMsg;
	protected Object value = null;
	
	
	public ConfigurationException(String category, String msg)
	{
		this(category, msg, null);
	}

	public ConfigurationException(String category, String msg, Object value)
	{
		this.category = category;
		this.categoryMsg = msg;
		this.value = value;
	}
	
	public String getCategory()
	{
		return this.category;
	}
	
	public String getMessage()
	{
		return this.categoryMsg;
	}
	
	public Object getValue()
	{
		return value;
	}
}
