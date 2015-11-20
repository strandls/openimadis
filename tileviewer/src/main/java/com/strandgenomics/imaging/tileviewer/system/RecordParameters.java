package com.strandgenomics.imaging.tileviewer.system;

/**
 * Parameters specific to record required to fetch tile of a record.
 * @author navneet
 *
 */
public class RecordParameters{

	/**
	 * record specific parameters
	 */
	private int imageWidth;
	private int imageHeight;
	
	private int frameNumber; 
	private int sliceNumber; 
	private int channelCount; 
	
	private boolean isGrayScale; 
	private boolean isZStacked; 
	
	private int[] channelContrasts = null;
	
	public int getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}

	public int getSliceNumber() {
		return sliceNumber;
	}

	public void setSliceNumber(int sliceNumber) {
		this.sliceNumber = sliceNumber;
	}

	public int getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(int channelCount) {
		this.channelCount = channelCount;
	}

	public boolean isGrayScale() {
		return isGrayScale;
	}

	public void setGrayScale(boolean isGrayScale) {
		this.isGrayScale = isGrayScale;
	}

	public boolean isZStacked() {
		return isZStacked;
	}

	public void setZStacked(boolean isZStacked) {
		this.isZStacked = isZStacked;
	}

	public int[] getChannelContrasts() {
		return channelContrasts;
	}

	public void setChannelContrasts(int[] channelContrasts) {
		this.channelContrasts = channelContrasts;
	}

	public RecordParameters(int frameNumber, int sliceNumber, int channelCount,
			boolean isGrayScale, boolean isZStacked, int[] channelContrasts) {
		this.frameNumber = frameNumber;
		this.sliceNumber = sliceNumber;
		this.channelCount = channelCount;
		this.isGrayScale = isGrayScale;
		this.isZStacked = isZStacked;
		this.channelContrasts = channelContrasts;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof TileParameters)
		{
			RecordParameters that = (RecordParameters) obj;
			if(this == that) return true;
			
			boolean equals = (this.frameNumber == that.frameNumber)&&(this.sliceNumber==that.sliceNumber)&&(this.channelCount==that.channelCount)&&(this.isGrayScale==that.isGrayScale)&&(this.isZStacked==that.isZStacked);
			// TODO:
			// appropriate equality check for extra parameters
			return equals;
		}
		return false;
	}	
	
	/**
	 * get string of parameters
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.frameNumber);
		sb.append("_");
		sb.append(this.sliceNumber);
		sb.append("_");
		sb.append(this.channelCount);
		sb.append("_");
		sb.append(this.isGrayScale);
		sb.append("_");
		sb.append(this.isZStacked);
		
		return sb.toString();
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}