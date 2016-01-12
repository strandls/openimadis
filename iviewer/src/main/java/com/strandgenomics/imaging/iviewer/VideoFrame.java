package com.strandgenomics.imaging.iviewer;

import java.awt.image.BufferedImage;

/**
 * buffered image with elapsed time
 * 
 * @author Anup Kulkarni
 */
public class VideoFrame {
	/**
	 * image
	 */
	private BufferedImage image;
	/**
	 * time elapsed since begining in ns
	 */
	private double elapsedTime;
	
	public VideoFrame(BufferedImage image, double elapsedTime)
	{
		this.image = image;
		
		this.elapsedTime = elapsedTime;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public double getElapsedTime()
	{
		return elapsedTime;
	}
}