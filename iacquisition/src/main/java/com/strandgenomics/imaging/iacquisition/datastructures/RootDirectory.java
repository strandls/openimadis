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

package com.strandgenomics.imaging.iacquisition.datastructures;

import java.net.URI;

public class RootDirectory implements IRootDirectory {

	private URI rootDirectory;
	private long lastCrawlTime;
	private String filterName;
	private long timeReqdForLastCrawl = 0;
	private long currentIndexingStartedOn = 0;
	private long currentIndexingEndedOn = 0;

	private String indexingStatus = INDEXING_STATUS.SCHEDULED.toString();
	private ROOT_DIRECTORY_STATUS status = ROOT_DIRECTORY_STATUS.TO_BE_INDEXED;
	private long indexStatusUpdateTime = 0;

	int noOfFilesCrawled;
	int noOfFilesIdentified;
	int noOfFilesConsumed;
	int noOfRecordsCreated;
	int noOfRecordsAdded;
	int noOfRecordsDeleted;
	long totalNoOfRecords;

	private int indexingPriority = 1;
	private boolean doIndex = true;
	private boolean recursive = true;

	public RootDirectory() {
		resetCounters();
	}

	public RootDirectory(URI rootDirectory) {
		this();
		setRootDirectory(rootDirectory);
	}

	public void resetCounters() {
		noOfFilesCrawled = 0;
		noOfFilesIdentified = 0;
		noOfFilesConsumed = 0;
		noOfRecordsCreated = 0;
		noOfRecordsAdded = 0;
		noOfRecordsDeleted = 0;
		totalNoOfRecords = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.strandgenomics.imaging.framework.indexer.IRootDirectory#getRootDirectory
	 * ()
	 */
	public URI getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(URI rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.strandgenomics.imaging.framework.indexer.IRootDirectory#getLastCrawlTime
	 * ()
	 */
	public long getLastCrawlTime() {
		return lastCrawlTime;
	}

	public void setLastCrawlTime(long lastCrawlTime) {
		this.lastCrawlTime = lastCrawlTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.strandgenomics.imaging.framework.indexer.IRootDirectory#getFilterName
	 * ()
	 */
	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getRootDirectoryStr() {
		return getRootDirectory().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.strandgenomics.imaging.framework.indexer.IRootDirectory#getIndexingStatus
	 * ()
	 */
	public String getIndexingStatus() {
		return indexingStatus;
	}

	public void setIndexingStatus(INDEXING_STATUS indexingStatus) {
		setIndexingStatus(indexingStatus.toString());
	}

	public void setIndexingStatus(String indexingStatus) {
		this.indexingStatus = indexingStatus;
		setIndexStatusUpdateTime(System.currentTimeMillis());
		// logger.info("Indexing Status : "+indexingStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.strandgenomics.imaging.framework.indexer.IRootDirectory#getStatus()
	 */
	public ROOT_DIRECTORY_STATUS getStatus() {
		return status;
	}

	public void setStatus(ROOT_DIRECTORY_STATUS status) {
		this.status = status;
		this.setIndexingStatus(status.toString());
	}

	public long getTotalNoOfRecords() {
		return totalNoOfRecords;
	}

	public void setTotalNoOfRecords(long totalNoOfRecords) {
		this.totalNoOfRecords = totalNoOfRecords;
	}

	public void getAndAddTotalNoOfRecords(long totalNoOfRecords) {
		this.totalNoOfRecords += totalNoOfRecords;
	}

	public int getNoOfRecordsCreated() {
		return noOfRecordsCreated;
	}

	public void setNoOfRecordsCreated(int noOfRecordsCreated) {
		this.noOfRecordsCreated = noOfRecordsCreated;
	}

	public void getAndAddNoOfRecordsCreated(int noOfRecordsCreated) {
		this.noOfRecordsCreated += noOfRecordsCreated;
	}

	public int getNoOfRecordsAdded() {
		return noOfRecordsAdded;
	}

	public void setNoOfRecordsAdded(int noOfRecordsAdded) {
		this.noOfRecordsAdded = noOfRecordsAdded;
	}

	public void getAndAddNoOfRecordsAdded(int noOfRecordsAdded) {
		this.noOfRecordsAdded += noOfRecordsAdded;
	}

	public int getNoOfRecordsDeleted() {
		return noOfRecordsDeleted;
	}

	public void setNoOfRecordsDeleted(int noOfRecordsDeleted) {
		this.noOfRecordsDeleted = noOfRecordsDeleted;
	}

	public void getAndAddNoOfRecordsDeleted(int noOfRecordsDeleted) {
		this.noOfRecordsDeleted += noOfRecordsDeleted;
	}

	public int getNoOfFilesCrawled() {
		return noOfFilesCrawled;
	}

	public void getAndAddNoOfFilesCrawled(int noOfRecordsCrawled) {
		this.noOfFilesCrawled += noOfRecordsCrawled;
	}

	public void setNoOfFilesCrawled(int noOfRecordsCrawled) {
		this.noOfFilesCrawled = noOfRecordsCrawled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#
	 * getNoOfFilesIdentified()
	 */
	public int getNoOfFilesIdentified() {
		return noOfFilesIdentified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#
	 * getAndAddNoOfFilesIdentified(int)
	 */
	public void getAndAddNoOfFilesIdentified(int noOfRecordsIdentified) {
		this.noOfFilesIdentified += noOfRecordsIdentified;
	}

	public void setNoOfFilesIdentified(int noOfRecordsIdentified) {
		this.noOfFilesIdentified = noOfRecordsIdentified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#
	 * getNoOfFilesConsumed()
	 */
	public int getNoOfFilesConsumed() {
		return noOfFilesConsumed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#
	 * getAndAddNoOfFilesConsumed(int)
	 */
	public void getAndAddNoOfFilesConsumed(int noOfRecordsConsumed) {
		this.noOfFilesConsumed += noOfRecordsConsumed;
	}

	public void setNoOfFilesConsumed(int noOfRecordsConsumed) {
		this.noOfFilesConsumed = noOfRecordsConsumed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#
	 * getIndexStatusUpdateTime()
	 */
	public long getIndexStatusUpdateTime() {
		return indexStatusUpdateTime;
	}

	public void setIndexStatusUpdateTime(long indexStatusUpdateTime) {
		this.indexStatusUpdateTime = indexStatusUpdateTime;
	}

	public long getTimeReqdForLastCrawl() {
		return timeReqdForLastCrawl;
	}

	public void setTimeReqdForLastCrawl(long timeReqdForLastCrawl) {
		this.timeReqdForLastCrawl = timeReqdForLastCrawl;
	}

	public long getTimeReqdForThisCrawl() {
		if (currentIndexingEndedOn == 0 && currentIndexingStartedOn == 0) {
			return 0;
		}
		
		if (currentIndexingEndedOn == 0) {
			return System.currentTimeMillis() - currentIndexingStartedOn;
		} else
			return currentIndexingEndedOn - currentIndexingStartedOn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#
	 * getCurrentIndexingStartedOn()
	 */
	public long getCurrentIndexingStartedOn() {
		return currentIndexingStartedOn;
	}

	public void setCurrentIndexingStartedOn(long currentIndexingStartedOn) {
		this.currentIndexingStartedOn = currentIndexingStartedOn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#
	 * getCurrentIndexingEndedOn()
	 */
	public long getCurrentIndexingEndedOn() {
		return currentIndexingEndedOn;
	}

	public void setCurrentIndexingEndedOn(long currentIndexingEndedOn) {
		this.currentIndexingEndedOn = currentIndexingEndedOn;
	}

	public void setIndexingPriority(int indexingPriority) {
		this.indexingPriority = indexingPriority;
	}

	public int getIndexingPriority() {
		return indexingPriority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.framework.indexer.IRootDirectory#clone()
	 */
	@Override
	public RootDirectory clone() throws CloneNotSupportedException {
		return (RootDirectory) super.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.strandgenomics.imaging.framework.indexer.IRootDirectory#compareTo
	 * (java.lang.Object)
	 */
	@Override
	public int compareTo(Object arg0) {
		IRootDirectory r = (IRootDirectory) arg0;
		if (lastCrawlTime < r.getLastCrawlTime()) {
			return -1;
		} else if (lastCrawlTime > r.getLastCrawlTime()) {
			return 1;
		} else {
			return 0;
		}
	}

	public boolean isDoIndex() {
		return doIndex;
	}

	public void setDoIndex(boolean doIndex) {
		this.doIndex = doIndex;
	}

    @Override
    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

	@Override
	public String toString() {
		return "RootDirectory [rootDirectory=" + rootDirectory
				+ ", lastCrawlTime=" + lastCrawlTime + ", filterName="
				+ filterName + ", indexingStatus=" + indexingStatus
				+ ", status=" + status + ", doIndex=" + doIndex + "]";
	}

}
