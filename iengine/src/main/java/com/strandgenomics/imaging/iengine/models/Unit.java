package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Storable;

/**
 * a unit is the physical storage space supplier for project
 * 
 * @author Anup Kulkarni
 */
public class Unit implements Storable{
	/**
	 * name of the unit
	 */
	public final String unitName;
	/**
	 * amount of physical storage quota in GB
	 */
	private double globalStorage;
	/**
	 * type of unit
	 */
	private UnitType type;
	/**
	 * email of point of contact
	 */
	private String pointOfContact;
	
	public String getUnitName() {
		return unitName;
	}

	public Unit(String name, double globalStorage, UnitType type, String contact)
	{
		this.unitName = name;
		this.setGlobalStorage(globalStorage);
		this.setType(type);
		this.setPointOfContact(contact);
	}

	public void setPointOfContact(String pointOfContact)
	{
		this.pointOfContact = pointOfContact;
	}

	public String getPointOfContact()
	{
		return pointOfContact;
	}

	public void setGlobalStorage(double globalStorage)
	{
		this.globalStorage = globalStorage;
	}

	public double getGlobalStorage()
	{
		return globalStorage;
	}

	public void setType(UnitType type)
	{
		this.type = type;
	}

	public UnitType getType()
	{
		return type;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}
}
