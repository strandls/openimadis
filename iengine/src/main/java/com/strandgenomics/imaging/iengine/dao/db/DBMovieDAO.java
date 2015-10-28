package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.MovieDAO;
import com.strandgenomics.imaging.iengine.movie.MovieTicket;

public class DBMovieDAO extends ImageSpaceDAO<MovieTicket> implements MovieDAO{
	DBMovieDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "MovieDAO.xml");
	}

	@Override
	public MovieTicket createObject(Object[] columnValues)
	{
		long movieID = (Long)columnValues[0];
		MovieTicket movie = (MovieTicket) toJavaObject( (DataSource)columnValues[1]);
		
		return movie;
	}
	
	/**
	 * inserts the image of specified movieID and specified coordinate
	 * @param movieID unique identifier of movie ticket
	 * @param coordinate the value of slice/frame
	 * @param path absolute path where the image is stored
	 * @throws DataAccessException 
	 */
	@Override
	public void insertImage(long movieID, int coordinate, String path) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMovieDAO", "insertImage", "movieID="+movieID+" coordinate="+coordinate);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_IMAGE");
        sqlQuery.setValue("MovieID", movieID, Types.BIGINT);
        sqlQuery.setValue("Counter", coordinate, Types.INTEGER);
        sqlQuery.setValue("Path", path, Types.VARCHAR);

        updateDatabase(sqlQuery);
	}
	
	@Override
	public void deleteImagePath(long movieID, int coordinate)
			throws DataAccessException {
		logger.logp(Level.INFO, "DBMovieDAO", "deleteImagePath", "movieID="+movieID+" coordinate="+coordinate);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_IMAGEPATH_OF_MOVIE");
        sqlQuery.setValue("MovieID", movieID, Types.BIGINT);
        sqlQuery.setValue("Counter", coordinate, Types.INTEGER);

        updateDatabase(sqlQuery);
	}
	
	/**
	 * returns the path of the specified image of specified movie 
	 * @param movieID unique identifier of movie ticket
	 * @param coordinate the specified slice/frame
	 * @return returns the path of the specified image
	 * @throws DataAccessException 
	 */
	@Override
	public String getImagePath(long movieID, int coordinate) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMovieDAO", "getImagePath", "movieID="+movieID+" coordinate="+coordinate);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_IMAGE");
        sqlQuery.setValue("MovieID", movieID, Types.BIGINT);
        sqlQuery.setValue("Counter", coordinate, Types.INTEGER);

        return getString(sqlQuery);
	}

	@Override
	public void registerMovie(MovieTicket newMovie) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMovieDAO", "registerMovie", "movieID="+newMovie.getMovieid());
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_MOVIE");
        sqlQuery.setValue("MovieID", newMovie.getMovieid(), Types.BIGINT);
        sqlQuery.setValue("Movie", toByteArray(newMovie), Types.BLOB);

        updateDatabase(sqlQuery);
	}

	@Override
	public List<MovieTicket> loadMovies() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMovieDAO", "registerMovie", "loading movies");
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LOAD_ALL_MOVIES");

        RowSet<MovieTicket> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public void deleteMovie(long movieid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMovieDAO", "deleteMovie", "deleting movies");
        
		deleteFromMovieImageTable(movieid);
        
		deleteFromMovieRegistry(movieid);
	}

	private void deleteFromMovieRegistry(long movieid) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_MOVIE");
		sqlQuery.setValue("MovieID", movieid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	private void deleteFromMovieImageTable(long movieid) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_IMAGES_OF_MOVIE");
		sqlQuery.setValue("MovieID", movieid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public MovieTicket getMovie(long movieId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_MOVIE");
		sqlQuery.setValue("MovieID", movieId, Types.BIGINT);
		
		return fetchInstance(sqlQuery);
	}
}
