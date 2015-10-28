package com.strandgenomics.imaging.iengine.models;

/**
 * location where legends may be present
 * 
 * @author Anup Kulkarni
 */
public enum LegendLocation {
	TOPLEFT("Left","Top"), BOTTOMRIGHT("Right", "Bottom"), TOPRIGHT("Right","Top"), BOTTOMLEFT("Left", "Botton");

	private String x;
	private String y;
	private LegendLocation(String x, String y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String getX()
	{
		return x;
	}
	
	public String getY()
	{
		return y;
	}
	
	public static LegendLocation getLocation(String xLocation, String yLocation)
	{
		String value = yLocation.toUpperCase() + xLocation.toUpperCase();
		return LegendLocation.valueOf(value);
	}
}
