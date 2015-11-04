/*
 * ImageConsumer.java
 *
 * AVADIS Image Management System
 * GUI
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iviewer.image;

public interface ImageConsumer {
	
	/**
	 * each consumer will have an unique ID
	 * @return the ID of the consumer
	 */
	public long getID();
	
	/**
	 * A consumer may submit a series of request (triggered by user action like panning a component needing image etc)
	 * to generate images. The {@link ImagingService} maintains a FIFO queue of such request across consumers. 
	 * Whereas the ImageConsumer may be only interested in the last request rather than all the intermediate requests 
	 * (they needs to be aborted as a matter of fact). 
	 * So when a imaging request is submitted to the Imaging service, its current request ID is assigned to the imaging Task.
	 * And when the task is executed (at some later point of time), it check the ID saved with the task with the actual
	 * request ID available with the consumer. If the IDs are different, the task is aborted
	 * 
	 * @return current request ID (may or may not change with requests)
	 */
	public long getCurrentRequestID();
	
	/**
	 * The imaging service calls this method to notify the image consumer that
	 * the image is successfully created and ready to be consumed
	 * @param image the image that is created
	 */
	public void consumeImage(ImageEvent image);
}

