package com.strandgenomics.imaging.iserver.services.def.ispace;

/**
 * Object capturing history item for a record 
 * 
 * @author Anup Kulkarni
 */
public class HistoryItem {
	/**
	 * guid of the record
	 */
	private long guid;
	/**
	 * the name of client used for modification of the record
	 */
	private String appName;
	/**
	 * the version of client used for modification of the record
	 */
	private String appVersion;
	/**
	 * authorized user who modified the record
	 */
	private String modifiedBy;
	/**
	 * the time of modification
	 */
	private long modificationTime;
	/**
	 * type of the modification
	 */
	private String type;
	/**
	 * description of the modification
	 */
	private String description;
	
	public HistoryItem() 
	{ }
	
	public void setGuid(long guid)
	{
		this.guid = guid;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public void setAppVersion(String appVersion)
	{
		this.appVersion = appVersion;
	}

	public void setModifiedBy(String modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	}

	public void setModificationTime(long modificationTime)
	{
		this.modificationTime = modificationTime;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setDescription(String description)
	{
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
}
