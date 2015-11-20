package com.strandgenomics.imaging.tileviewer.system;

import com.strandgenomics.imaging.tileviewer.Helper;

/**
 * Parameters required to fetch tile of a record. These parameters uniquely defines the tile 
 * across the records.
 * 
 * @author Anup Kulkarni
 */
public class TileParameters {
	/**
	 * record id
	 */
	private long guid;
	
	/**
	 * x index of tile according to map
	 */
	private int X;
	
	/**
	 * y index of tile according to map
	 */
	private int Y;	
	
	/**
	 * zoom reverse level of map
	 */
	private int Z;
	
	/**
	 * dimension of map tile
	 */
	private int tileSize;
	
	/**
	 * record specific parameters
	 */
	private RecordParameters recordParameters;
	
	public TileParameters(long guid, int x, int y, int z, int tileSize, RecordParameters recordParameters) {
		this.guid = guid;
		X = x;
		Y = y;
		Z = z;
		this.tileSize = tileSize;
		this.recordParameters = recordParameters;
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

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public RecordParameters getRecordParameters() {
		return recordParameters;
	}

	public void setRecordParameters(RecordParameters recordParameters) {
		this.recordParameters = recordParameters;
	}

	public long getGuid() {
		return guid;
	}

	public void setGuid(long guid) {
		this.guid = guid;
	}

	@Override
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
		sb.append("_");
		sb.append(this.tileSize);
		sb.append("_");
		sb.append(this.recordParameters.toString());
		
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		// TODO:
		// ensure equals and hashCode methods are consistent
		
		// to ensure that hashcodes are equal for objects for whom equals method return true,
		// simple trick is to create a String using same parameters that are used in equals method
		// and call hashCode of that String
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof TileParameters)
		{
			TileParameters that = (TileParameters) obj;
			if(this == that) return true;
			
			boolean equals = (this.guid == that.guid)&&(this.X==that.X)&&(this.Y==that.Y)&&(this.Z==that.Z)&&(this.tileSize==that.tileSize)&&(this.recordParameters.equals(that.recordParameters));
			// TODO:
			// appropriate equality check for extra parameters
			return equals;
		}
		return false;
	}
}
