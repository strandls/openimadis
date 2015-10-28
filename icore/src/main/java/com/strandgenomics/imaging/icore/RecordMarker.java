package com.strandgenomics.imaging.icore;

/**
 * the status of the record: Active/Deleted/Archived etc
 * 
 * @author Anup Kulkarni
 */
public enum RecordMarker {

	/**
	 * record is active in the system
	 */
	Active {
		public String toString()
		{
			return "Active";
		}
	},
	/**
	 * record is deleted from the system
	 */
	Deleted {
		public String toString()
		{
			return "Deleted";
		}
	},
	/**
	 * record is archived
	 */
	Archived {
		public String toString()
		{
			return "Archived";
		}
	},
	/**
	 * record is under construction
	 */
	UNDER_CONSTRUCTION {
		public String toString()
		{
			return "Under Construction";
		}
	}
}
