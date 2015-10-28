package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Storable;
/**
 * captures the association between unit and project
 * 
 * @author Anup Kulkarni 
 */
public class UnitAssociation implements Storable{
	/**
	 * name of the unit
	 */
	public final String unitName;
	/**
	 * project id
	 */
	public final int projectId;
	/**
	 * amount of storage contributed to project by unit
	 */
	public final double storageSpace;
	
	public UnitAssociation(String unitName, int projectId, double storageSpace)
	{
		this.unitName = unitName;
		this.projectId = projectId;
		this.storageSpace = storageSpace;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

}
