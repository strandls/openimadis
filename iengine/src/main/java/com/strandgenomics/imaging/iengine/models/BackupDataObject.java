package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Storable;

/**
 * Details about the project backup information  
 * 
 * @author Anup Kulkarni
 */
public class BackupDataObject implements Storable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -115232587649469560L;
	/**
	 * project id
	 */
	public final int projectId;
	/**
	 * location where the data has been backed up
	 */
	public final String location;
	/**
	 * signature computed while backing up the data
	 */
	public final String sign;
	
	public BackupDataObject(int projectId, String location, String sign)
	{
		this.location = location;
		this.projectId = projectId;
		this.sign = sign;
	}
	
	@Override
	public void dispose()
	{}
	
}
