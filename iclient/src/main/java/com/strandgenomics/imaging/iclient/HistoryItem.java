package com.strandgenomics.imaging.iclient;

public class HistoryItem {
	/**
	 * guid of the record
	 */
	protected long guid;
	/**
	 * the name of client used for modification of the record
	 */
	protected String appName;
	/**
	 * the version of client used for modification of the record
	 */
	protected String appVersion;
	/**
	 * authorized user who modified the record
	 */
	protected String modifiedBy;
	/**
	 * the time of modification
	 */
	protected long modificationTime;
	/**
	 * type of the modification
	 */
	protected String type;
	/**
	 * description of the modification
	 */
	protected String description;
	
	HistoryItem(long guid, String appName, String appVersion, String user, long modificationTime, String historyType, String description) 
	{ 
		this.guid = guid;
		this.appName = appName;
		this.appVersion = appVersion;
		this.modifiedBy = user;
		this.modificationTime = modificationTime;
		this.type = historyType;
		this.description = description;
	}
	
	public long getGuid()
	{
		return guid;
	}

	public String getAppName()
	{
		return appName;
	}

	public String getAppVersion()
	{
		return appVersion;
	}

	public String getModifiedBy()
	{
		return modifiedBy;
	}

	public long getModificationTime()
	{
		return modificationTime;
	}

	public String getType()
	{
		return type;
	}

	public String getDescription()
	{
		return description;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(guid+" ");
		sb.append(appName+" ");
		sb.append(appVersion+" ");
		sb.append(modifiedBy+" ");
		sb.append(modificationTime+" ");
		sb.append(type+" ");
		sb.append(description+" ");
		
		return sb.toString();
	}
}

