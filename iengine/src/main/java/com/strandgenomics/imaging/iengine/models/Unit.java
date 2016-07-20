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
