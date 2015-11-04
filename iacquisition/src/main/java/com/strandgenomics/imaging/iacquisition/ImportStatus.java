package com.strandgenomics.imaging.iacquisition;

/**
 * status of record import
 * 
 * @author Anup Kulkarni
 */
public enum ImportStatus {
	Indexed {
		@Override
		public String toString()
		{
			return "Complete";
		}
	},

	Queued {
		@Override
		public String toString()
		{
			return "Queued";
		}
	},
	
	Stopping {
		@Override
		public String toString()
		{
			return "Stopping";
		}
	},

	Stopped {
		@Override
		public String toString()
		{
			return "Stopped";
		}
	},

	Failed {
		@Override
		public String toString()
		{
			return "Complete";
		}
	},
	
	Partial {
		@Override
		public String toString()
		{
			return "Complete";
		}
	},

	Duplicate {
		@Override
		public String toString()
		{
			return "Successful (Some Duplicate Files Ignored).";
		}
	},

	Started {
		@Override
		public String toString()
		{
			return "Started";
		}
	}
}
