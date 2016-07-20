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

package com.strandgenomics.imaging.icore;

/**
 * User has a permission in project
 * 
 * @author Anup Kulkarni
 */
public enum Permission {

	/**
	 * has all rights plus rights to change facility manager
	 */
	Administrator {
		public String toString()
		{
			return "Administrator";
		}
	},
	/**
	 * has all the rights of project manager plus have rights to change project
	 * manager
	 */
	FacilityManager {
		public String toString()
		{
			return "Facility Manager";
		}
	}, 
	/**
	 * have all rights on project:read, export, write, create and delete
	 *	also rights of updating other members of the project
	 */
	Manager { 
		
		public String toString()
		{
			return "Manager";
		}
	}, 
	
	/**
	 * can upload/create new records
	 */
	Upload { 
		
		public String toString()
		{
			return "Upload";
		}
	}, 
	
	/**
	 * can modify existing records
	 */
	Write {
		
		public String toString()
		{
			return "Write";
		}
	}, 
	
	/**
	 * can export existing records
	 */
	Export {
		
		public String toString()
		{
			return "Export";
		}
	},
	
	/**
	 * can read existing records
	 */
	Read {
		
		public String toString()
		{
			return "Read";
		}
	},
	
	/**
	 * have no rights
	 */
	None {
		public String toString()
		{
			return "None";
		}
	}
}
