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
