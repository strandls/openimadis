/*
 * ImageEventListener.java
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

import java.util.EventListener;


/**
 * An image consumer will be interested in listening ImageEvents
 * @author arunabha
 *
 */
public interface ImageEventListener extends EventListener {
	
	/**
	 * this method is called when the image producer is ready with a image
	 * @param evt contains relevant information about the generated image 
	 */
	public void imageIsReady(ImageEvent evt);
}