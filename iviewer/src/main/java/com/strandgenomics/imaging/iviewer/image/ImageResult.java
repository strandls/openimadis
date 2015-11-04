package com.strandgenomics.imaging.iviewer.image;

/**
 * 
 * @author arunabha
 *
 */
public class ImageResult
{
	/**
	 * the consumer of image
	 */
	private ImageConsumer m_consumer = null;
	/**
	 * generated image
	 */
	private ImageEvent m_image = null;
	
	public ImageResult(ImageConsumer consumer, ImageEvent image)
	{
		m_consumer = consumer;
		m_image = image;
	}
	
	public ImageEvent getImage()
	{
		return m_image;
	}
	
	public ImageConsumer getConsumer()
	{
		return m_consumer;
	}
}
