package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.Storable;

/**
 * AcquisitionProfile is profile created before starting an experiment. This
 * encapsulates the set of parameters/values used to acquire set of records of
 * an experiment on a microscope. These values will override the record metadata
 * obtained from source files.
 * 
 * @author Anup Kulkarni
 */
public class AcquisitionProfile implements Storable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8987993257196896245L;
	/** 
	 * microscope 
	 */
	private String microscopeName;
	/** 
	 * pixel size in microns in x dimension 
	 */
	private Double xPixelSize = null;
	/**
	 * type of xPixelSzie
	 */
	private AcquisitionProfileType xPixelType = AcquisitionProfileType.VALUE;
	/** 
	 * pixel size in microns in y dimension  
	 */
	private Double yPixelSize = null;
	/**
	 * type of yPixelSzie
	 */
	private AcquisitionProfileType yPixelType = AcquisitionProfileType.VALUE;
	/** 
	 * pixel size in microns in z dimension  
	 */
	private Double zPixelSize = null;
	/**
	 * type of zPixelSzie
	 */
	private AcquisitionProfileType zPixelType = AcquisitionProfileType.VALUE;
	/** 
	 * source type (formats) 
	 */
	private SourceFormat sourceType = null;
	/**
	 * unit used for elapsed time
	 */
	private TimeUnit timeUnit = TimeUnit.MILISECONDS;
	/**
	 * unit used for exposure time
	 */
	private TimeUnit exposureTimeUnit = TimeUnit.MILISECONDS;
	/**
	 * unit used for xPixelLength etc
	 */
	private LengthUnit lengthUnit = LengthUnit.MICROMETER;
	/**
	 * name of acquisition profile
	 */
	private String profileName = "Default-Profile";
	
	public AcquisitionProfile(String microscopeName, Double xPixelSize, AcquisitionProfileType xType, Double yPixelSize, AcquisitionProfileType yType, Double zPixelSize, AcquisitionProfileType zType, SourceFormat sourceType, TimeUnit elapsedTimeUnit, TimeUnit exposureTimeUnit, LengthUnit lengthUnit)
	{
		if(microscopeName==null||microscopeName.isEmpty())
			microscopeName = "default";
		
		this.microscopeName = microscopeName;
		this.xPixelSize = xPixelSize;
		this.yPixelSize = yPixelSize;
		this.zPixelSize = zPixelSize;
		this.sourceType = sourceType;
		
		if(lengthUnit!=null)
			this.lengthUnit = lengthUnit;
		
		if(elapsedTimeUnit!=null)
			this.timeUnit = elapsedTimeUnit;
		
		if(exposureTimeUnit!=null)
			this.exposureTimeUnit = exposureTimeUnit;
		
		this.xPixelType = xType;
		this.yPixelType = yType;
		this.zPixelType = zType;
	}
	
	public AcquisitionProfile(String profileName, String microscopeName, Double xPixelSize, AcquisitionProfileType xType, Double yPixelSize, AcquisitionProfileType yType, Double zPixelSize, AcquisitionProfileType zType, SourceFormat sourceType, TimeUnit elapsedTimeUnit,TimeUnit exposureTimeUnit, LengthUnit lengthUnit)
	{
		this(microscopeName, xPixelSize, xType, yPixelSize, yType, zPixelSize, zType, sourceType, elapsedTimeUnit, exposureTimeUnit, lengthUnit);
		
		this.profileName = profileName;
	}
	
	public AcquisitionProfile(String microscopeName, double xPixelSize, double yPixelSize, double zPixelSize, SourceFormat sourceType, TimeUnit elapsedTimeUnit, TimeUnit exposureTimeUnit, LengthUnit micrometer)
	{
		this(microscopeName, xPixelSize, AcquisitionProfileType.VALUE, yPixelSize, AcquisitionProfileType.VALUE, zPixelSize, AcquisitionProfileType.VALUE, sourceType, elapsedTimeUnit, exposureTimeUnit, micrometer);
	}
	
	public String getProfileName()
	{
		return this.profileName;
	}
	
	public void setxPixelSize(Double xPixelSize)
	{
		this.xPixelSize = xPixelSize;
	}

	public void setyPixelSize(Double yPixelSize)
	{
		this.yPixelSize = yPixelSize;
	}

	public void setzPixelSize(Double zPixelSize)
	{
		this.zPixelSize = zPixelSize;
	}

	public void setSourceType(SourceFormat sourceType)
	{
		this.sourceType = sourceType;
	}

	public void setTimeUnit(TimeUnit timeUnit)
	{
		this.timeUnit = timeUnit;
	}

	public void setLengthUnit(LengthUnit lengthUnit)
	{
		this.lengthUnit = lengthUnit;
	}
	
	public void setMicroscope(String microscope)
	{
		this.microscopeName = microscope;
	}

	public String getMicroscope()
	{
		return microscopeName;
	}
	
	public TimeUnit getElapsedTimeUnit()
	{
		return timeUnit;
	}
	
	public TimeUnit getExposureTimeUnit()
	{
		if(exposureTimeUnit==null)
			exposureTimeUnit = TimeUnit.MICROSECONDS;
		return exposureTimeUnit;
	}

	public LengthUnit getLengthUnit()
	{
		return lengthUnit;
	}

	public Double getxPixelSize()
	{
		return xPixelSize;
	}

	public Double getyPixelSize()
	{
		return yPixelSize;
	}

	public Double getzPixelSize()
	{
		return zPixelSize;
	}

	public SourceFormat getSourceType()
	{
		return sourceType;
	}
	
	public AcquisitionProfileType getXType()
	{
		return this.xPixelType;
	}
	
	public AcquisitionProfileType getYType()
	{
		return this.yPixelType;
	}
	
	public AcquisitionProfileType getZType()
	{
		return this.zPixelType;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("ProfileName=");
		sb.append(this.profileName);
		sb.append(",");
		
		sb.append("MicroscopeName=");
		sb.append(this.microscopeName);
		sb.append(",");
		
		sb.append("X Size=");
		sb.append(this.xPixelSize);
		sb.append(",");
		
		sb.append("X Type=");
		sb.append(this.xPixelType);
		sb.append(",");
		
		sb.append("Y Size=");
		sb.append(this.yPixelSize);
		sb.append(",");
		
		sb.append("Y Type=");
		sb.append(this.yPixelType);
		sb.append(",");
		
		sb.append("Z Size=");
		sb.append(this.zPixelSize);
		sb.append(",");
		
		sb.append("Z Type=");
		sb.append(this.zPixelType);
		sb.append(",");
		
		sb.append("Elapsed Time Unit=");
		sb.append(this.timeUnit);
		sb.append(",");
		
		sb.append("Exposure Time Unit=");
		sb.append(this.getExposureTimeUnit());
		sb.append(",");
		
		sb.append("Length Unit=");
		sb.append(this.lengthUnit);
		
		return sb.toString();
	}

	@Override
	public void dispose()
	{}
}
