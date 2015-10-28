package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.movie.MovieTicket;

/**
 * manages images in the movie
 * 
 * @author Anup Kulkarni
 */
public interface MovieDAO {

	/**
	 * returns the path of the specified image of specified movie 
	 * @param movieID unique identifier of movie ticket
	 * @param coordinate the specified slice/frame
	 * @return returns the path of the specified image
	 * @throws DataAccessException 
	 */
	public String getImagePath(long movieID, int coordinate) throws DataAccessException;

	/**
	 * inserts the image of specified movieID and specified coordinate
	 * @param movieID unique identifier of movie ticket
	 * @param coordinate the value of slice/frame
	 * @param path absolute path where the image is stored
	 * @throws DataAccessException 
	 */
	public void insertImage(long movieID, int coordinate, String path) throws DataAccessException;

	/**
	 * deletes the image of specified movieID and specified coordinate
	 * @param movieID unique identifier of movie ticket
	 * @param coordinate the value of slice/frame
	 * @param path absolute path where the image is stored
	 * @throws DataAccessException 
	 */
	public void deleteImagePath(long movieID, int coordinate) throws DataAccessException;
	
	/**
	 * registers new movie in DB
	 * @param newMovie 
	 * @throws DataAccessException 
	 */
	public void registerMovie(MovieTicket newMovie) throws DataAccessException;
	
	/**
	 * loads all the existing active movies in the DB
	 * @return list of movies
	 * @throws DataAccessException 
	 */
	public List<MovieTicket> loadMovies() throws DataAccessException;

	/**
	 * delete all the entries related to this movie
	 * @param movieid specified movie
	 * @throws DataAccessException 
	 */
	public void deleteMovie(long movieid) throws DataAccessException;

	/**
	 * returns movie for specified movieId
	 * @param movieId specified movie id
	 * @return movie for specified movieId
	 * @throws DataAccessException 
	 */
	public MovieTicket getMovie(long movieId) throws DataAccessException;
}
