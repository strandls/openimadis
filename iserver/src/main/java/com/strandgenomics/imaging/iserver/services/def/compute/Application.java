/*
 * Application.java
 *
 * AVADIS Image Management System
 * Web-service definition
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
package com.strandgenomics.imaging.iserver.services.def.compute;

/**
 * An application hosted by the server
 * @author arunabha
 *
 */
public class Application {
	
	/**
	 * name of the application
	 */
	protected String name;
	/**
	 * general description of the application
	 */
	protected String description;
	/**
	 * broad area (or domain) where this application is relevant - for example deconvolution, denoising 
	 */
	protected String categoryName;
	/**
	 * version of this application 
	 */
	protected String version;
	/**
	 * notes associated with this application 
	 */
	protected String notes;
	
	
	public Application()
	{}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version of the application
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param version of the application
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
