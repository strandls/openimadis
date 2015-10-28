package com.strandgenomics.imaging.iengine.dao.db;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Types;
import java.util.logging.Level;

import javax.activation.DataSource;
import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Thumbnail;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ThumbnailDAO;

/**
 * gives access to thumbnail of the record stored in thumbnail_registry table
 * 
 * @author Anup Kulkarni
 */
public class DBThumbnailDAO extends ImageSpaceDAO<Thumbnail> implements ThumbnailDAO{

	DBThumbnailDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "ThumbnailDAO.xml");
	}

	@Override
	public void setThumbnail(long guid, BufferedImage img) throws IOException
	{
		if(getThumbnail(guid)!=null)
		{
			updateThumbnail(guid, img);
		}
		else
		{
			insertThumbnail(guid, img);
		}
	}
	
	private void insertThumbnail(long guid, BufferedImage img) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBThumbnailDAO", "insertThumbnail", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_THUMBNAIL");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("Thumbnail", getThumbnailByteArray(img), Types.BLOB);

        updateDatabase(sqlQuery);
	}
	
	private void updateThumbnail(long guid, BufferedImage img) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBThumbnailDAO", "setThumbnail", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_THUMBNAIL");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("Thumbnail", getThumbnailByteArray(img), Types.BLOB);

        updateDatabase(sqlQuery);
	}
	
	@Override
	public Thumbnail getThumbnail(long guid) throws IOException
	{
		logger.logp(Level.FINE, "DBThumbnailDAO", "getThumbnail", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_THUMBNAIL");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        return fetchInstance(sqlQuery);
       
	}
	
	private byte[] getThumbnailByteArray(BufferedImage thumbnail)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{
			ImageIO.write(thumbnail, "JPG", os);
			os.flush();
			Util.closeStream(os);
			return os.toByteArray();
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "DBThumbnailDAO", "getThumbnailByteArray", "Error creating thumbnail byte array");
			try {
				os.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public Thumbnail createObject(Object[] columnValues)
	{
		 DataSource ds = (DataSource) columnValues[0];
		 long revision = Util.getLong(columnValues[1]) ;
		if (ds == null)
			return null;
		try {
			return new Thumbnail(ds.getInputStream(), revision);
		} catch (IOException e) {
			return null;
		}
	}

}
