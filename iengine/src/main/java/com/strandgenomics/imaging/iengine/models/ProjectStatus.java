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
