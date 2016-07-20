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

/*
 * DBTicketDAO.java
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

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.TicketDAO;
import com.strandgenomics.imaging.iengine.models.Job;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.Ticket;

/**
 * Provides access to the tickets
 * @author arunabha
 *
 */
public class DBTicketDAO extends ImageSpaceDAO<Job> implements TicketDAO {
	
	DBTicketDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "TicketDAO.xml");
	}
	
	@Override
	public Job findTicket(long ticketID) throws DataAccessException 
	{
        logger.logp(Level.FINEST, "DBTicketDAO", "findTicket", "ticketID="+ticketID);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TICKET_FOR_ID");
        sqlQuery.setValue("TicketID", ticketID, Types.BIGINT);

        return fetchInstance(sqlQuery);
	}
	
	@Override
	public List<Job> listTickets() throws DataAccessException
	{
		logger.logp(Level.FINEST, "DBTicketDAO", "listTickets", "listing all the tickets");

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LIST_TICKETS");

        RowSet<Job> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public List<Job> findTicket(Status jobStatus) throws DataAccessException 
	{
		if(jobStatus == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.FINEST, "DBTicketDAO", "findTicket", "jobStatus="+jobStatus);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TICKETS_FOR_STATUS");
        sqlQuery.setValue("Status", jobStatus.name(), Types.VARCHAR);

        RowSet<Job> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public Job updateJobStatus(long ticketID, Status jobStatus) throws DataAccessException 
	{
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_TICKET");
        logger.logp(Level.INFO, "DBTicketDAO", "updateJobStatus", "ticketID="+ticketID +", jobStatus="+jobStatus);
        
        sqlQuery.setValue("TicketID",          ticketID,           Types.BIGINT);
        sqlQuery.setValue("Status",            jobStatus.name(),   Types.VARCHAR);
        sqlQuery.setValue("ModificationTime",  new Timestamp(System.currentTimeMillis()),  Types.TIMESTAMP);

        updateDatabase(sqlQuery);
        return findTicket(ticketID);
	}

	@Override
	public Job insertTicket(long ticketID, long requestTime, Status jobStatus) throws DataAccessException 
	{
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_TICKET");
        logger.logp(Level.INFO, "DBTicketDAO", "insertTicket", "ticketID="+ticketID);
        
        sqlQuery.setValue("TicketID",          ticketID,                    Types.BIGINT);
        sqlQuery.setValue("Status",            jobStatus.name(),            Types.VARCHAR);
        sqlQuery.setValue("RequestTime",       new Timestamp(requestTime),  Types.TIMESTAMP);

        updateDatabase(sqlQuery);
        return findTicket(ticketID);
	}

	@Override
	public Job createObject(Object[] columnValues) 
	{
		long ticketID              = Util.getLong(columnValues[0]);
		Timestamp requestTime      = Util.getTimestamp(columnValues[1]);
		Timestamp modificationTime = Util.getTimestamp(columnValues[2]);
		Status status              = Status.valueOf((String)columnValues[3]);
		
		return new Job(ticketID, requestTime.getTime(), modificationTime.getTime(), status);
	}
	
	private int getActiveTickets(Connection conn) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LIST_TICKETS");

        RowSet<Job> result = find(sqlQuery);
        List<Job> allTickets = result == null ? null : result.getRows();
		
		int activeTickets = 0;
		
		if(allTickets !=null)
		{
			List<Long> removeList = new ArrayList<Long>();
			for(Job job : allTickets)
			{
				if(job.hasCompleted() || job.hasExpired())
					removeList.add(job.getTicketID());
				
				else if(job.isWaiting() && (System.currentTimeMillis() - job.getRequestTime()) > Constants.getRequestTimeout())
					removeList.add(job.getTicketID());
					
				else if(job.isBusy())
					activeTickets++;
			}
			
			for(Long dirty : removeList)
			{
				System.out.println("removing dirty tickets "+dirty);
				clean(dirty);
			}
		}
		
		return activeTickets;
	}
	
	private void clean(long ticketID)
	{
		try
		{
			// remove creation request for ticket id
			RecordCreationRequest request = getRequest(ticketID);
			
			removeCreationRequest(ticketID);
			
			// remove old signature to new signature entry
			BigInteger dummySignature = request.getArchiveHash();
			DBImageSpaceDAOFactory.getDAOFactory().getArchiveDAO().deleteDummySignatureMapping(dummySignature);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "TicketManager", "clean", "error in removing request "+ticketID);
		}
		
		try
		{
			removeTicket(ticketID);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "TicketManager", "clean", "error in removing ticket "+ticketID);
		}
	}

	@Override
	public Ticket createTicketTransaction(int maxQueueSize, RecordCreationRequest request) throws DataAccessException
	{
		Connection conn = null;
        boolean autoCommitStatus = true;
        
        try {
            conn = getConnection();
            autoCommitStatus = conn.getAutoCommit();
            //we will commit all in one shot, transaction
            conn.setAutoCommit(false);
            
            // perform operation
            Ticket ticket = ticketCreationTransaction(maxQueueSize, request, conn);
            
            //make the changes permanent
            conn.commit();
            
            return ticket;
        }
        catch(SQLException ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error executing transaction", ex);

            try 
            {
                if(conn != null) conn.rollback();
            }
            catch(Exception exx)
            {
                logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while doing a rollback", exx);
            }
            
            logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while creating tables", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            try 
            {
                conn.setAutoCommit(autoCommitStatus);
            }
            catch(Exception ex)
            {
                logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while setting autocommit status", ex);
            }
            //close the connection/set it free in the pool
            closeAll(null, null, conn);
        }
	}
	
	private Ticket ticketCreationTransaction(int maxQueueSize, RecordCreationRequest request, Connection conn) throws DataAccessException
	{
		if(getActiveTickets(conn) >= maxQueueSize)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.TICKET_NOT_AVAILABLE));
		}
		
		RecordCreationRequest oldRequest = getRequest(request.getArchiveHash());
		if(oldRequest != null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.TICKET_ALREADY_EXIST, oldRequest.getProject(), oldRequest.getActor()));
		}
		
		Ticket ticket = new Ticket(request);
		insertRequest(ticket.ID, request);
		
		return ticket;
	}
	
	@Override
	public void insertRequest(long ticketId, RecordCreationRequest request) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_REQUEST");
		sqlQuery.setValue("TicketID", ticketId, Types.BIGINT);
		sqlQuery.setValue("RequestHash", Util.toHexString(request.getArchiveHash()), Types.VARCHAR);
		sqlQuery.setValue("Request", toByteArray(request), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public Long getTicket(BigInteger requestHash) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TICKET_ID");
		sqlQuery.setValue("RequestHash", Util.toHexString(requestHash), Types.VARCHAR);
		
		return getLong(sqlQuery);
	}

	@Override
	public RecordCreationRequest getRequest(long ticketId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_REQUEST_FOR_TICKET");
		sqlQuery.setValue("TicketID", ticketId, Types.BIGINT);
		
		DataSource ds = (DataSource) getObject(sqlQuery);
		return (RecordCreationRequest) toJavaObject( ds );
	}
	
	@Override
	public RecordCreationRequest getRequest(BigInteger requestHash) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_REQUEST_FOR_HASH");
		sqlQuery.setValue("RequestHash", Util.toHexString(requestHash), Types.VARCHAR);
		
		DataSource ds = (DataSource) getObject(sqlQuery);
		return (RecordCreationRequest) toJavaObject(ds);
	}

	@Override
	public void removeCreationRequest(long ticketId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_REQUEST");
		sqlQuery.setValue("TicketID", ticketId, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void removeTicket(long ticketId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_TICKET");
		sqlQuery.setValue("TicketID", ticketId, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void updateTicketRequest(long ticketID, RecordCreationRequest request) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_REQUEST");
		sqlQuery.setValue("TicketID", ticketID, Types.BIGINT);
		sqlQuery.setValue("Request", toByteArray(request), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}
}
