/**
 * 
 */
package com.strandgenomics.imaging.iclient.local;

import java.util.EventListener;
import java.util.List;

import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;

/**
 * @author mayuresh
 *
 */
public interface IndexerListener extends EventListener {

    /**
     * Indexer fires this event when it has created a record group
     * @param recordGroup the records that are created
     */
    public void indexed(IRecord record);
    
    /**
     * this method is called when indexing is started
     */
    public void indexingStarted();
    
    /**
     * Indexer calls this method when it is through with indexing, either normally or terminated
     */
    public void indexingComplete();

    /**
     * this method is called when indexing is failed
     * @param serverError true if its server error
     */
	public void indexingFailed(boolean serverError);
	
	/**
	 * this event is fired when duplicate experiment is ignored from indexing
	 * @param experiment
	 */
	public void ignoredDuplicate(IExperiment experiment);

	/**
	 * list of experiments found
	 * @param experimentList
	 */
	public void experimentFound(List<RawExperiment> experimentList);

}
