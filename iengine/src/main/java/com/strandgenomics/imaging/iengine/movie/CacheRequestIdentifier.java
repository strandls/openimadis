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

package com.strandgenomics.imaging.iengine.movie;

/**
 * Identifies raw data prefetching request
 * 
 * @author Anup Kulkarni
 */
public class CacheRequestIdentifier {
	/**
	 * record id
	 */
	private long guid;
	/**
	 * specified site
	 */
	private int site;
	/**
	 * is on frames
	 */
	private boolean isOnFrames;
	/**
	 * fixed dimension
	 */
	private int fixedDimension;
	
	public CacheRequestIdentifier(long guid, boolean onFrames, int fixedDimension, int site)
	{
		this.guid = guid;
		this.isOnFrames = onFrames;
		this.fixedDimension = fixedDimension;
		this.site = site;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CacheRequestIdentifier)
		{
			CacheRequestIdentifier that = (CacheRequestIdentifier) obj;
			if(this == that) return true;
			
			boolean equals = ((this.guid == that.guid) && (this.isOnFrames == that.isOnFrames) && (this.site == that.site)
					&& (this.fixedDimension == that.fixedDimension));
			return equals;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("guid:");
		sb.append(guid);
		sb.append(",");
		
		sb.append("onFrames:");
		sb.append(isOnFrames);
		sb.append(",");
		
		sb.append("fixedDimension:");
		sb.append(fixedDimension);
		sb.append(",");
		
		sb.append("site:");
		sb.append(site);
		
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
}
