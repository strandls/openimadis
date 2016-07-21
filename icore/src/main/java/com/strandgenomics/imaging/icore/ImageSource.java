/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
