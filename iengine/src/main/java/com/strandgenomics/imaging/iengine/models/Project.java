/*
 * Project.java
 *
 * AVADIS Image Management System
 * Engine Implementation
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iengine.models;

import java.sql.Timestamp;

import com.strandgenomics.imaging.icore.Storable;

/**
 * A project
 * @author arunabha
 *
 */
public class Project implements Storable {

	private static final long serialVersionUID = -3335155698006349068L;
	
	/** 
	 * database generated id 
	 */
	protected final int projectID;
	/** 
	 * name of the project 
	 */
	protected String name;
	/** 
	 * short description of the project 
	 */
	protected String notes;
	/** 
	 * the date and time when the project was created
	 */
	protected Timestamp creationDate;
	/** 
	 * a project can be in Active/Archived/Pending Archival/Pending Restoration/Pending Deletion/Deleted State 
	 */
	protected ProjectStatus status;
	/** 
	 * the guy who created this project 
	 */
	protected String createdBy;
	/** 
	 * number of records in this project 
	 */
	protected int noOfRecords;
	/** 
	 * disk usage in gb 
	 */
	protected double spaceUsage;
	/** 
	 * disk quota in gb 
	 */
	protected double storageQuota;
	/**
	 * project location, basically the name of the project after removing special characters etc 
	 */
	protected String storageLocation;
	
	/**
	 * Creates a Project with the specified information
	 * @param projectID  db generated unique identifier
	 * @param name name of the project 
	 * @param notes short description of the project 
	 * @param creationDate the date and time when the project was created
	 * @param status a project can be in Active/Archived/Pending Archival/Pending Restoration/Pending Deletion/Deleted State 
	 * @param createdBy the guy who created this project 
	 * @param noOfRecords current number of records in this project 
	 * @param spaceUsage  disk usage in gb 
	 * @param storageQuota disk quota in gb 
	 */
	public Project(int projectID, String name, String notes, Timestamp creationDate, ProjectStatus status, 
			String createdBy, int noOfRecords, double spaceUsage, double storageQuota, String location)
	{
		this.projectID = projectID;
		this.name = name;
		this.notes = notes;
		this.creationDate = creationDate;
		this.status = status;
		this.createdBy = createdBy;
		this.noOfRecords = noOfRecords;
		this.spaceUsage = spaceUsage;
		this.storageQuota = storageQuota;
		this.storageLocation = location;
	}
	
	/**
	 * @return the projectID
	 */
	public int getProjectID()
	{
		return projectID;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() 
	{
		return creationDate;
	}

	/**
	 * @return the status
	 */
	public ProjectStatus getStatus() 
	{
		return status;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() 
	{
		return createdBy;
	}

	/**
	 * @return the noOfRecords
	 */
	public int getNoOfRecords()
	{
		return noOfRecords;
	}

	/**
	 * @return the spaceUsage
	 */
	public double getSpaceUsage()
	{
		return spaceUsage;
	}
	
	public void incrementUsage(double gb)
	{
		spaceUsage += gb;
	}

	/**
	 * @return the storageQuota
	 */
	public double getStorageQuota() 
	{
		return storageQuota;
	}
	
	/**
	 * project location, basically the name of the project after removing special characters etc 
	 */
	public String getLocation()
	{
		return storageLocation;
	}
	
	/**
	 * All table names belonging to a specific project is prefixed with the a std name
	 * Returns the project specific prefix
	 * @return the project specific prefix
	 */
	public String getTablePrefix()
	{
		return getTablePrefix(projectID);
	}
	
	public static String getTablePrefix(int projectID)
	{
		return "project_"+projectID;
	}
	
	@Override
    public void dispose()
    {
		this.name = null;
		this.notes = null;
		this.creationDate = null;
		this.status = null;
    }
	
    /**
     * Returns the ID of this project
     * @return  the ID of this project
     */
    public int getID() 
    {
        return projectID;
    }
    
    @Override
    public String toString() 
    {
        return name;
    }
    
    @Override
    public int hashCode()
    {
    	return name.hashCode();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean status = false;
        if(obj instanceof Project)
        {
            Project that = (Project)obj;
            if(this.name != null && that.name != null && this.name.equals(that.name))
            {
                status = true;
            }
        }
        return status;
    }
}
