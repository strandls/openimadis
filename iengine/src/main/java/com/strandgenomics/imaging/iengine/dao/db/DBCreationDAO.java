/*
 * DBCreationDAO.java
 *
 * AVADIS Image Management System
 * Data Access Components
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
package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TreeSet;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Project;

/**
 * Provides relevant method to create tables, indexes and triggers dynamically
 * 
 * @author arunabha
 * 
 */
public abstract class DBCreationDAO<T extends Storable> extends StorageDAO<T> {

	/**
	 * updating/creating a database table/index through jdbc connection too fast
	 * in windows was leading to MySQL Error Code 13 (essentially an OS I/O
	 * error while renaming. the problem can be solved by slowing down the speed
	 * with which we are trying to modifiy existing tables/indexes
	 */
	public static final long SLEEP_TIME_DURING_CREATION = 2000L;
	/**
	 * maximum number of times a specific creation query will be fired before it reports failure
	 */
	public static final long MAXIMUM_ATTEMPTS = 3;

	DBCreationDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider, String configFile) 
	{
		super(factory, connectionProvider, configFile);
	}

	public boolean createStorageTables(Project project, Object... args) throws DataAccessException 
	{
		TreeSet<String> tableKeys = getTableCreationQueries();
		if (tableKeys == null || tableKeys.isEmpty()) 
		{
			return false;
		}

		Connection conn = null;
		boolean autoCommitStatus = true;

		try {
			conn = getConnection();
			autoCommitStatus = conn.getAutoCommit();
			// we will commit all in one shot, transaction
			conn.setAutoCommit(false);

			int counter = 0;
			// fire all queries that is there to create tables
			for (String queryKey : tableKeys) 
			{
				// execute the SQLQuery for this key
				logger.logp(Level.INFO, "DBCreationDAO", "createStorage", "creating interaction table #" + (++counter) + " in " + project);
				executeQueryWithRepeats(conn, queryKey, project, args);
			}

			// make the changes permanent
			conn.commit();
		} 
		catch (Exception ex)
		{
			try 
			{
				if (conn != null)
					conn.rollback();
			} 
			catch (Exception exx) 
			{
				logger.logp(Level.WARNING, "DBCreationDAO", "createStorage", "error while doing a rollback", exx);
			}
			logger.logp(Level.WARNING, "DBCreationDAO", "createStorage", "error while creating tables", ex);
			throw new DataAccessException(ex);
		} 
		finally 
		{
			try 
			{
				conn.setAutoCommit(autoCommitStatus);
			} 
			catch (Exception ex) 
			{
				logger.logp(Level.WARNING, "DBCreationDAO", "createStorage", "error while setting autocommit status", ex);
			}
			// close the connection/set it free in the pool
			closeAll(null, null, conn);
		}

		return true;
	}

	/**
	 * This method should be called after createStorageTables is called
	 * 
	 * @param organism
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	public boolean createPerformanceIndexes(Project organism, Object... args) throws DataAccessException 
	{
		TreeSet<String> tableKeys = getTableCreationQueries();
		TreeSet<String> indexKeys = getIndexCreationQueries();
		TreeSet<String> functionKeys = getFunctionCreationQueries();
		TreeSet<String> triggerKeys = getTriggerCreationQueries();

		if (tableKeys == null || tableKeys.isEmpty()) {
			return false;
		}

		Connection conn = null;
		boolean autoCommitStatus = true;

		try {
			conn = getConnection();
			autoCommitStatus = conn.getAutoCommit();
			// we will commit all in one shot, transaction
			conn.setAutoCommit(false);

			int counter = 0;
			// fire all queries that is there to create indexes on existing
			// tables
			if (indexKeys != null)
			{
				for (String queryKey : indexKeys) 
				{
					logger.logp(Level.INFO, "DBCreationDAO", "createStorage",
							"creating index #" + (++counter) + " in "
									+ organism);
					// execute the SQLQuery for this key
					executeQueryWithRepeats(conn, queryKey, organism, args);
				}
			}

			counter = 0;
			if (functionKeys != null) 
			{
				for (String queryKey : functionKeys) 
				{
					// execute the SQLQuery for this key
					logger.logp(Level.INFO, "DBCreationDAO", "createStorage",
							"creating function #" + (++counter) + " in "
									+ organism);
					executeQueryWithRepeats(conn, queryKey, organism, args);
				}
			}

			counter = 0;
			if (triggerKeys != null) 
			{
				for (String queryKey : triggerKeys) 
				{
					// execute the SQLQuery for this key
					logger.logp(Level.INFO, "DBCreationDAO", "createStorage",
							"creating trigger #" + (++counter) + " in "
									+ organism);
					executeQueryWithRepeats(conn, queryKey, organism, args);
				}
			}

			// make the changes permanent
			conn.commit();
		} 
		catch (SQLException ex)
		{
			logger.logp(Level.WARNING, "DBCreationDAO", "createStorage",
					"error while getting database connection", ex);
			throw new DataAccessException(ex.getMessage());
		} 
		catch (DataAccessException ex) 
		{
			try
			{
				if (conn != null)
					conn.rollback();
			} 
			catch (Exception exx) 
			{
				logger.logp(Level.WARNING, "DBCreationDAO", "createStorage",
						"error while doing a rollback", exx);
			}
			logger.logp(Level.WARNING, "DBCreationDAO", "createStorage",
					"error while creating tables", ex);
			throw ex;
		} 
		finally
		{
			try 
			{
				conn.setAutoCommit(autoCommitStatus);
			} 
			catch (Exception ex)
			{
				logger.logp(Level.WARNING, "DBCreationDAO", "createStorage",
						"error while setting autocommit status", ex);
			}
			// close the connection/set it free in the pool
			closeAll(null, null, conn);
		}

		return true;
	}

	private void sleep() 
	{
		try 
		{
			Thread.sleep(SLEEP_TIME_DURING_CREATION);
		} 
		catch (Exception e) 
		{}
	}

	private void executeQueryWithRepeats(Connection conn, String queryKey,
			Project project, Object... args) throws DataAccessException 
	{
		int noOfAttempt = 3;

		for (int r = 1; r <= noOfAttempt; r++) {
			try {
				logger.logp(Level.INFO, "DBCreationDAO", "executeQuery",
						"attempt no = " + r);
				// execute the SQLQuery for this key
				executeCreationQuery(conn, queryKey, project, args);
				// if the above method do not throws an exception we are through
				return;
			} catch (DataAccessException dax) {
				sleep(); // try once again
				if (r == noOfAttempt)
					throw dax;
			}
		}
	}

	protected abstract void executeCreationQuery(Connection conn, String queryKey, Project project, Object... args)
			throws DataAccessException;
}
