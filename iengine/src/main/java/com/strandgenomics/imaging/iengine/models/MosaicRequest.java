package com.strandgenomics.imaging.iengine.models;

/**
 * This class contains represents the input to the MosaicManager.
 * It contains all the data relevant for mosaic manager.
 * This class will be modified accordingly to provide required data to mosaic manager.
 * @author navneet
 *
 */
public class MosaicRequest {

	/**
	 * all the records which will be used to form final mosaic image
	 */
	private long[] recordids;

	public long[] getRecordids() {
		return recordids;
	}

	public void setRecordids(long[] recordids) {
		this.recordids = recordids;
	}
	
}
