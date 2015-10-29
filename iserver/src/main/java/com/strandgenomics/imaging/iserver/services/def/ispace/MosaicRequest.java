package com.strandgenomics.imaging.iserver.services.def.ispace;

public class MosaicRequest {
	
	public MosaicRequest() {}

	/**
	 * all the records which will be used to form final mosaic image
	 */
	private Long[] recordids;

	public Long[] getRecordids() {
		return recordids;
	}

	public void setRecordids(Long[] recordids) {
		this.recordids = recordids;
	}
}
