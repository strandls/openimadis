/*
 * ProjectStatus.java
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

/**
 * A project can be in Active/Archived/Pending Archival/Pending Restoration/Pending Deletion/Deleted State 
 * @author arunabha
 *
 */
public enum ProjectStatus {
	
	Active {
		
		public String toString()
		{
			return "Active";
		}
	},
	Archived {
		
		public String toString()
		{
			return "Archived";
		}
	},
	Deleted {
		
		public String toString()
		{
			return "Deleted";
		}
	}, 
	ArchiveQ {
		
		public String toString()
		{
			return "Queued For Archiving";
		}
	},
	Archiving {
		
		public String toString()
		{
			return "Archiving In Progress";
		}
	},
	RestorationQ {
		
		public String toString()
		{
			return "Queued For Restoration";
		}
	},
	Restoring {
		
		public String toString()
		{
			return "Restoration In Progress";
		}
	},
	DeletionQ {
		
		public String toString()
		{
			return "Queued For Deletion";
		}
	}
}
