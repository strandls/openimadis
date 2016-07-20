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

package com.strandgenomics.imaging.iengine.dao;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.strandgenomics.imaging.icore.Thumbnail;
import com.strandgenomics.imaging.icore.db.DataAccessException;

public interface ThumbnailDAO {
	/**
	 * sets the particular image as thumbnail for record
	 * @param guid of specified record
	 * @param img image to be set as thumbnail
	 * @throws DataAccessException 
	 * @throws IOException 
	 */
	public void setThumbnail(long guid, BufferedImage img) throws DataAccessException, IOException;
	
	/**
	 * get the thumbnail from particular record
	 * @param guid of specified record
	 * @return InputStream of thumbnail
	 * @throws DataAccessException
	 * @throws IOException 
	 */
	public Thumbnail getThumbnail(long guid) throws DataAccessException, IOException;
}
