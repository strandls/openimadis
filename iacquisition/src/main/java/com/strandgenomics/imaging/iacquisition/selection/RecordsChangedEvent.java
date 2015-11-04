/**
 * 
 */
package com.strandgenomics.imaging.iacquisition.selection;


/**
 * @author nimisha
 *
 */
public class RecordsChangedEvent {

    
    public static final int RECORDS_ADDED=1;
    public static final int RECORDS_REMOVED=2;
    public static final int ANNOTATIONS_CHANGED=3;
    public static final int ANNOTATION_ADDED=4;
    public static final int ANNOTATION_REMOVED=5;
    
    int type;
    String key;

    /**
     * Create an event setting passed records as selected
     * @param records
     */
    public RecordsChangedEvent(int type) {
        this(type,null);
    }
    

    public RecordsChangedEvent(int type, String key) {
    	this.type = type;
        this.key = key;
	}

	public int getType(){
    	return type;
    }
    
    public String getField(){
    	return key;
    }
}
