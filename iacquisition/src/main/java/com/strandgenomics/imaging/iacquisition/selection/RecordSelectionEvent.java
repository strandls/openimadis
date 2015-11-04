/**
 * 
 */
package com.strandgenomics.imaging.iacquisition.selection;

import java.util.List;

import com.strandgenomics.imaging.icore.IRecord;

/**
 * @author mayuresh
 *
 */
public class RecordSelectionEvent {

    List<IRecord> records;
    Object source;

    /**
     * Create an event setting passed records as selected
     * @param records
     */
    public RecordSelectionEvent(List<IRecord> records) {
    	this(records, null);
    }
    
    /**
     * Create an event setting passed records as selected
     * @param records
     */
    public RecordSelectionEvent(List<IRecord> records, Object source) {
        this.records = records;
        this.source = source;
    }

    /**
     * @return the records
     */
    public List<IRecord> getRecords() {
        return records;
    }
    
    public Object getSource(){
    	return source;
    }
}
