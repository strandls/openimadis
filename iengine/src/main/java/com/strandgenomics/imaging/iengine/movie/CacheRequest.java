package com.strandgenomics.imaging.iengine.movie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;

/**
 * Encapsulation of cache request for image prefetching
 * 
 * @author Anup Kulkarni
 */
public class CacheRequest extends MovieTicket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1529850811000563572L;

	public CacheRequest(long ID, String actorLogin, long guid, int site, boolean onFrames, int otherCoorinate, List<Integer> channels)
	{
		super(ID, actorLogin, guid, site, onFrames, otherCoorinate, true, false, channels, new ArrayList<String>(), MovieType.CACHE_IMAGE_DATA);
	}
	
	@Override
	public File createStorageDirectory()
	{
		String cacheStoreDir = Constants.getStringProperty(Property.IMAGE_CACHE_STORAGE_LOCATION, null);

		File recordCache = new File(cacheStoreDir, "RecordID_"+guid);
		recordCache.mkdirs();
		
		return recordCache;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CacheRequest)
		{
			CacheRequest that = (CacheRequest) obj;
			
			if(this == that) return true;
			
			return (this.guid == that.guid && this.site == that.site && this.onFrames == that.onFrames && this.otherCoordinate == that.otherCoordinate && this.type.equals(that.type));
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public String toString()
	{
		return this.guid+" "+this.site+" "+this.onFrames+" "+this.otherCoordinate+" "+this.type;
	}
}