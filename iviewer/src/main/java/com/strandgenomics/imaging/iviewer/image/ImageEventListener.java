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
