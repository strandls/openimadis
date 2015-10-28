package com.strandgenomics.imaging.icore;

public interface IRecordObserver {
	
	/**
	 * check whether the indexing needs toi be stopped
	 * @return true iff the indexing needs to be stopped, false otherwise
	 */
	public boolean isStopped();
	
	public void recordAdded(IRecord record);
	
	public void foundDuplicate(IExperiment experiment);
}
