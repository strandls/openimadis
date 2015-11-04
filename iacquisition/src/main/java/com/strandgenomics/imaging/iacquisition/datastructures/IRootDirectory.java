package com.strandgenomics.imaging.iacquisition.datastructures;

import java.net.URI;

public interface IRootDirectory extends Cloneable, Comparable {

	public static final String FORMAT_ID = "imaging.framework.indexer.RootDirectory";

	public abstract URI getRootDirectory();

	public abstract long getLastCrawlTime();

	public abstract String getFilterName();

	public abstract String getIndexingStatus();

	public abstract ROOT_DIRECTORY_STATUS getStatus();

	public abstract int getNoOfFilesCrawled();
	
	public abstract int getNoOfFilesIdentified();

	public abstract void getAndAddNoOfFilesIdentified(int noOfRecordsIdentified);

	public abstract int getNoOfFilesConsumed();

	public abstract void getAndAddNoOfFilesConsumed(int noOfRecordsConsumed);

	public abstract int getNoOfRecordsCreated();

	public abstract int getNoOfRecordsAdded();
	
	public abstract int getNoOfRecordsDeleted();

	public abstract long getTotalNoOfRecords();
	
	public abstract long getIndexStatusUpdateTime();

	public abstract long getCurrentIndexingStartedOn();

	public abstract long getCurrentIndexingEndedOn();

	public abstract long getTimeReqdForThisCrawl();

	public abstract long getTimeReqdForLastCrawl();

	public abstract IRootDirectory clone() throws CloneNotSupportedException;
	
	public abstract boolean isDoIndex();
	
    public abstract boolean isRecursive();

	//////////////////////////////////////////////////////////////////
	
	public static enum ROOT_DIRECTORY_STATUS {
		TO_BE_INDEXED {
			@Override
			public String toString() {
				return "to be indexed ... ";
			}
		}, TO_BE_DELETED{
			@Override
			public String toString() {
				return "to be deleted ... ";
			}
		}, TO_BE_CLEARED{
			@Override
			public String toString() {
				return "to be cleared ... ";
			}
		}, TO_BE_REINDEXED{
			@Override
			public String toString() {
				return "to be re-indexed ... ";
			}
		}
	}

	public static enum INDEXING_STATUS {
		NOT_SCHEDULED {
			public String toString() {
				return "Not scheduled";
			}
		},

		SCHEDULED {
			public String toString() {
				return "Scheduled";
			}
		},

		INDEXING {
			public String toString() {
				return "Indexing";
			}
		},

		CRAWLING_IN_PROGRESS {
			public String toString() {
				//return "Crawling ...";
				return INDEXING.toString();
			}
		},

		CRAWLING_COMPLETE {
			public String toString() {
				//return "Crawling complete";
				return INDEXING.toString();
			}
		},

		CREATING_RECORDS_IN_PROGRESS {
			public String toString() {
				//return "Creating records ...";
				return INDEXING.toString();
			}
		},

		CREATING_RECORDS_COMPLETE {
			public String toString() {
				//return "Creating records complete";
				return INDEXING.toString();
			}
		},

		CREATING_GENERIC_RECORDS {
			public String toString() {
				//return "Creating generic formahandler records ...";
				return INDEXING.toString();
			}
		},

		STORING_IN_PROGRESS {
			public String toString() {
				//return "Storing records ...";
				return INDEXING.toString();
			}
		},

		STORING_COMPLETE {
			public String toString() {
				//return "Storing complete";
				return INDEXING.toString();
			}
		},

		TO_BE_DELETED {
			public String toString() {
				return "To be deleted ...";
			}
		},

		TO_BE_CLEARED {
			public String toString() {
				return "To be cleared";
			}
		},

		DELETION_IN_PROGRESS {
			public String toString() {
				return "Clearing index";
			}
		},

		DELETION_COMPLETE {
			public String toString() {
				return DELETION_IN_PROGRESS.toString();
			}
		},

		INDEXING_COMPLETE {
			public String toString() {
				return INDEXING+ " done";
			}
		},

		FINISHED_INDEXING {
			public String toString() {
				return "Finished indexing";
			}
		},

		ERROR {
			public String toString() {
				return "ERROR";
			}
		}
	}

}
