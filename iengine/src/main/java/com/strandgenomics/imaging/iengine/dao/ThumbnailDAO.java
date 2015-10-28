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
