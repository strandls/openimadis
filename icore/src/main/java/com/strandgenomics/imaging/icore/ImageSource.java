/*
 * ImageSource.java
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
package com.strandgenomics.imaging.icore;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A source or provider of image within a record
 * @author arunabha
 *
 */
public interface ImageSource {
	
    /**
     * Returns the width (in pixels) of the images represented by this source
     * Note that width is same for all images within the record
     */
    public int getImageWidth();
    
    /**
     * Returns the height (in pixels) of the images represented by this source
     * Note that height is same for all images within the record
     */
    public int getImageHeight();
    
	/**
	 * Returns the raw BufferedImage instance associated with this image source
	 * @param useChannelColor if true a color image (based on the channel color) is returned, gray-scale image otherwise
	 * @return BufferedImage instance associated with this image source
	 */
	public BufferedImage getImage(boolean useChannelColor) throws IOException;
}
