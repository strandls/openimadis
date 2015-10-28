/*
 * Rank.java
 *
 * AVADIS Image Management System
 * Client APIs
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

package com.strandgenomics.imaging.icore;

/**
 * A user has a rank in the institution 
 * @author arunabha
 *
 */
public enum Rank {
	
	/**
	 * system administrator will have all rights
	 */
	Administrator { 
		
		public String toString()
		{
			return "IT Administrator";
		}
	}, 
	
	/**
	 * facility members has all the rights to do anything perhaps
	 */
	FacilityManager { 
		
		public String toString()
		{
			return "Facility Manager";
		}
	}, 
	
	/**
	 * A Team Leader can create Projects perhaps
	 */
	TeamLeader {
		
		public String toString()
		{
			return "Team Leader";
		}
	}, 
	
	/**
	 * general worker in a project
	 */
	TeamMember {
		
		public String toString()
		{
			return "Team Member";
		}
	}
}

