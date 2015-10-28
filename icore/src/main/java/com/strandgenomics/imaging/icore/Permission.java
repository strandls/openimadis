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
