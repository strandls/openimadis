package com.strandgenomics.imaging.ithreedview.listeners;

public class ImageCreatedEvent {

	private int currImageIndex;
	private int totalImageCount;

	public ImageCreatedEvent(int currImageIndex, int totalImageCount) {
		this.currImageIndex = currImageIndex;
		this.totalImageCount = totalImageCount;
	}

	public int getCurrImageIndex() {
		return currImageIndex;
	}

	public int getTotalImageCount() {
		return totalImageCount;
	}
}
