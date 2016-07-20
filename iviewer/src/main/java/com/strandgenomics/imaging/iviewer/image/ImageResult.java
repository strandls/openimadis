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
