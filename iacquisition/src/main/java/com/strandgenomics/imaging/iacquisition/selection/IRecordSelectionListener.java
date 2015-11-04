/**
 * 
 */
package com.strandgenomics.imaging.iacquisition.selection;

/**
 * Used by thumbnail and spreadsheet to listen to each other's selections
 * @author mayuresh
 */
public interface IRecordSelectionListener {

    /**
     * Communicates record selection change
     * @param selectionEvent
     */
    public void selectionChanged(RecordSelectionEvent selectionEvent);

}
