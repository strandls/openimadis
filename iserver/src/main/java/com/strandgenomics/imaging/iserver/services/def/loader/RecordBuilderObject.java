package com.strandgenomics.imaging.iserver.services.def.loader;

/**
 *  
 * @author Anup Kulkarni
 */
public class RecordBuilderObject {
	/**
	 * id of parent record, (null if does not have parent record)
	 */
	private Long parentGuid;
	/**
	 * record identifier prefix given by the record creator
	 */
	private String recordLabel;
	/**
	 * name of the parent project
	 */
	private String projectName; 
	/**
	 * user login who created the record
	 */
	private String uploadedBy;
	/**
	 * number of frames in the final record
	 */
	private int noOfFrames;
	/**
	 * number of slices in the final record
	 */
	private int noOfSlices;
	/**
	 * width of every image in the record
	 */
	private int imageWidth;
	/**
	 * height of every image in the record
	 */
	private int imageHeight;
	/**
	 * channels in the record
	 */
	private Channel[] channels;
	/**
	 * sites in the record
	 */
	private RecordSite[] sites;
	/**
	 * pixel depth 
	 */
	private int imageDepth;
	/**
	 * size along the x axis
	 */
	private double xPixelSize;
	/**
	 * size along the y axis
	 */
	private double yPixelSize;
	/**
	 * size along the z axis
	 */
	private double zPixelSize;
	/**
	 * source type
	 */
	private String sourceType;
	/**
	 * image type: RGB:GRAYSCALE
	 */
	private int imageType;
	/**
	 * ip of client machine
	 */
	private String machineIP;
	/**
	 * mac of client machine
	 */
	private String macAddress;
	/**
	 * source folder on client machine
	 */
	private String sourceFolder;
	/**
	 * source filename on client machine
	 */
	private String sourceFilename;
	/**
	 * source time
	 */
	private Long sourceTime;
	/**
	 * creation time
	 */
	private Long creationTime;
	/**
	 * acquired time
	 */
	private Long acquiredTime;
	
	public Long getParentRecord()
	{
		return this.parentGuid;
	}
	
	public void setParentRecord(long parentGuid)
	{
		this.parentGuid = parentGuid;
	}
	
	public String getRecordLabel()
	{
		return recordLabel;
	}

	public void setRecordLabel(String recordLabel)
	{
		this.recordLabel = recordLabel;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getUploadedBy()
	{
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy)
	{
		this.uploadedBy = uploadedBy;
	}

	public int getNoOfFrames()
	{
		return noOfFrames;
	}

	public void setNoOfFrames(int noOfFrames)
	{
		this.noOfFrames = noOfFrames;
	}

	public int getNoOfSlices()
	{
		return noOfSlices;
	}

	public void setNoOfSlices(int noOfSlices)
	{
		this.noOfSlices = noOfSlices;
	}

	public int getImageWidth()
	{
		return imageWidth;
	}

	public void setImageWidth(int imageWidth)
	{
		this.imageWidth = imageWidth;
	}

	public int getImageHeight()
	{
		return imageHeight;
	}

	public void setImageHeight(int imageHeight)
	{
		this.imageHeight = imageHeight;
	}

	public Channel[] getChannels()
	{
		return channels;
	}

	public void setChannels(Channel[] channels)
	{
		this.channels = channels;
	}

	public RecordSite[] getSites()
	{
		return sites;
	}

	public void setSites(RecordSite[] sites)
	{
		this.sites = sites;
	}

	public int getImageDepth()
	{
		return imageDepth;
	}

	public void setImageDepth(int imageDepth)
	{
		this.imageDepth = imageDepth;
	}

	public double getxPixelSize()
	{
		return xPixelSize;
	}

	public void setxPixelSize(double xPixelSize)
	{
		this.xPixelSize = xPixelSize;
	}

	public double getyPixelSize()
	{
		return yPixelSize;
	}

	public void setyPixelSize(double yPixelSize)
	{
		this.yPixelSize = yPixelSize;
	}

	public double getzPixelSize()
	{
		return zPixelSize;
	}

	public void setzPixelSize(double zPixelSize)
	{
		this.zPixelSize = zPixelSize;
	}

	public String getSourceType()
	{
		return sourceType;
	}

	public void setSourceType(String sourceType)
	{
		this.sourceType = sourceType;
	}

	public int getImageType()
	{
		return imageType;
	}

	public void setImageType(int imageType)
	{
		this.imageType = imageType;
	}

	public String getMachineIP()
	{
		return machineIP;
	}

	public void setMachineIP(String machineIP)
	{
		this.machineIP = machineIP;
	}

	public String getMacAddress()
	{
		return macAddress;
	}

	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}

	public String getSourceFolder()
	{
		return sourceFolder;
	}

	public void setSourceFolder(String sourceFolder)
	{
		this.sourceFolder = sourceFolder;
	}

	public String getSourceFilename()
	{
		return sourceFilename;
	}

	public void setSourceFilename(String sourceFilename)
	{
		this.sourceFilename = sourceFilename;
	}

	public Long getSourceTime()
	{
		return sourceTime;
	}

	public void setSourceTime(Long sourceTime)
	{
		this.sourceTime = sourceTime;
	}

	public Long getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(Long creationTime)
	{
		this.creationTime = creationTime;
	}

	public Long getAcquiredTime()
	{
		return acquiredTime;
	}

	public void setAcquiredTime(Long acquiredTime)
	{
		this.acquiredTime = acquiredTime;
	}

	public RecordBuilderObject() {}
}
