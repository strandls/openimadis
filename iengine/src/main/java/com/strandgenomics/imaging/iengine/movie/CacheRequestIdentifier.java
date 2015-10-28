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
