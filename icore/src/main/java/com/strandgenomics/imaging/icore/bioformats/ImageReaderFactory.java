/**
 * ImageReaderFactory.java
 *
 * Project imaging
 * Core Bioformat Component
 *
 * Copyright 2009-2010 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.bioformats;

import loci.formats.gui.BufferedImageReader;

/**
 * defines how a BufferedImageReader can be created
 * @author arunabha
 *
 */
public abstract class ImageReaderFactory {
	
	/**
	 * Returns a Bioformats reader
	 * @return the image reader instance
	 */
	public abstract BufferedImageReader createBufferedImageReader();
}
