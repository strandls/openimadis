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

import com.strandgenomics.imaging.icore.Storable;

/**
 * Parameters which identify a tile created out of a record image
 * 
 * @author Navneet
 */
public class TileParameters implements Storable{
	/**
	 * record id
	 */
	private long guid;
	
	/**
	 * x index of tile according to reverse zoom level
	 */
	private int X;
	
	/**
	 * y index of tile according to reverse zoom level
	 */
	private int Y;	
	
	/**
	 * zoom reverse level of map
	 */
	private int Z;	
	
	
	public TileParameters(long guid, int x, int y, int z) {
		this.guid = guid;
		X = x;
		Y = y;
		Z = z;
	}
	
	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getZ() {
		return Z;
	}

	public void setZ(int z) {
		Z = z;
	}

	public long getGuid() {
		return guid;
	}

	public void setGuid(long guid) {
		this.guid = guid;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.guid);
		sb.append("_");
		sb.append(this.X);
		sb.append("_");
		sb.append(this.Y);
		sb.append("_");
		sb.append(this.Z);
		
		return sb.toString();
	}
	
	public int hashCode()
	{
		// TODO:
		// ensure equals and hashCode methods are consistent
		
		// to ensure that hashcodes are equal for objects for whom equals method return true,
		// simple trick is to create a String using same parameters that are used in equals method
		// and call hashCode of that String
		return this.toString().hashCode();
	}

	public boolean equals(Object obj)
	{
		if(obj instanceof TileParameters)
		{
			TileParameters that = (TileParameters) obj;
			if(this == that) return true;
			
			boolean equals = (this.guid == that.guid)&&(this.X==that.X)&&(this.Y==that.Y)&&(this.Z==that.Z);
			// TODO:
			// appropriate equality check for extra parameters
			return equals;
		}
		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
