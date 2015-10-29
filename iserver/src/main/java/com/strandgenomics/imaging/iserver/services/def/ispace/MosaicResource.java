package com.strandgenomics.imaging.iserver.services.def.ispace;

public class MosaicResource {
	
	public MosaicResource() {}

	/**
	 * records which are being used to create stitched image
	 */
	private Long[] recordids;
	
	/**
	 * the coordinate which represents the top most point of stitched image
	 */
	private int anchor_left;
	
	/**
	 * the coordinate which represents the left most point of stitched image
	 */
	private int anchor_top;
	
	/**
	 * width of resultant mosaic image
	 */
	private int mosaicImageWidth;
	
	/**
	 * height of resultant mosaic image
	 */
	private int mosiacImageHeight;
	
	public Long[] getRecordids() {
		return recordids;
	}

	public void setRecordids(Long[] recordids) {
		this.recordids = recordids;
	}

	public int getAnchor_left() {
		return anchor_left;
	}

	public void setAnchor_left(int anchor_left) {
		this.anchor_left = anchor_left;
	}

	public int getAnchor_top() {
		return anchor_top;
	}

	public void setAnchor_top(int anchor_top) {
		this.anchor_top = anchor_top;
	}

	public int getMosaicImageWidth() {
		return mosaicImageWidth;
	}

	public void setMosaicImageWidth(int mosaicImageWidth) {
		this.mosaicImageWidth = mosaicImageWidth;
	}

	public int getMosiacImageHeight() {
		return mosiacImageHeight;
	}

	public void setMosiacImageHeight(int mosiacImageHeight) {
		this.mosiacImageHeight = mosiacImageHeight;
	}
}
