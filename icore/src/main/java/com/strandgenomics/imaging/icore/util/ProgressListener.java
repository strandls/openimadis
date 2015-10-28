/*
 * ProgressListener.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.icore.util;

import java.util.EventListener;

public interface ProgressListener extends EventListener {
	
	/**
	 * Reports a progress
	 * @param message optional text message
	 * @param min lower limit of the progress, e.g., 0
	 * @param max the upper limit of the progress e.g., 100
	 * @param value a value between and inclusive of the lower and upper limits
	 */
	public void reportProgress(String message, int min, int max, int value);
}
