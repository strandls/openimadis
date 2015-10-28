package com.strandgenomics.imaging.iengine.dao.db;

import java.math.BigInteger;
import java.sql.Types;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.CreationRequestDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;

public class DBCreationRequestDAO extends ImageSpaceDAO<RecordCreationRequest> implements CreationRequestDAO {

	DBCreationRequestDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "CreationRequestDAO.xml");
	}

	public void insertRequest(long ticketId, RecordCreationRequest request) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_REQUEST");
		sqlQuery.setValue("TicketID", ticketId, Types.BIGINT);
		sqlQuery.setValue("RequestHash", Util.toHexString(request.getArchiveHash()), Types.VARCHAR);
		sqlQuery.setValue("Request", toByteArray(request), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}

	public Long getTicket(BigInteger requestHash) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TICKET_ID");
		sqlQuery.setValue("RequestHash", Util.toHexString(requestHash), Types.VARCHAR);
		
		return getLong(sqlQuery);
	}

	public RecordCreationRequest getRequest(long ticketId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_REQUEST_FOR_TICKET");
		sqlQuery.setValue("TicketID", ticketId, Types.BIGINT);
		
		return fetchInstance(sqlQuery);
	}
	
	public RecordCreationRequest getRequest(BigInteger requestHash) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_REQUEST_FOR_HASH");
		sqlQuery.setValue("RequestHash", Util.toHexString(requestHash), Types.VARCHAR);
		
		return fetchInstance(sqlQuery);
	}

	@Override
	public RecordCreationRequest createObject(Object[] columnValues)
	{
		RecordCreationRequest request = (RecordCreationRequest) toJavaObject( (DataSource)columnValues[0]);
		return request;
	}
	
	public void removeCreationRequest(long ticketId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_REQUEST");
		sqlQuery.setValue("TicketID", ticketId, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	public void updateTicketRequest(long ticketID, RecordCreationRequest request) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_REQUEST");
		sqlQuery.setValue("TicketID", ticketID, Types.BIGINT);
		sqlQuery.setValue("Request", toByteArray(request), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}

}
