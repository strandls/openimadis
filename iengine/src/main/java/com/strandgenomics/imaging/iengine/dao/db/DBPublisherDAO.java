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

package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.compute.Publisher;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.PublisherDAO;

public class DBPublisherDAO extends ImageSpaceDAO<Publisher> implements PublisherDAO {

	DBPublisherDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "PublisherDAO.xml");
	}

	@Override
	public Publisher getPublisher(String publisherName) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBPublisherDAO", "getPublisher", "publisher=" + publisherName);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PUBLISHER");
		sqlQuery.setValue("PubName", publisherName, Types.VARCHAR);

		return fetchInstance(sqlQuery);
	}

	@Override
	public List<Publisher> listPublishers() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBPublisherDAO", "listPublishers", "listing all publishers");

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LIST_PUBLISHERS");
		
		RowSet<Publisher> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public void insertPublisher(String name, String description, String publisherCode, String ipfilter) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBTaskDAO", "insertPublisher", "inserting publisher = "+name+" for publisherCode = "+publisherCode);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_PUBLISHER");

		sqlQuery.setValue("PubName",  name,        Types.VARCHAR);
		sqlQuery.setValue("PubDesc", description,       Types.VARCHAR);
		sqlQuery.setValue("PubCode",   publisherCode,      Types.VARCHAR);
		sqlQuery.setValue("PubIPFilter",   ipfilter,      Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public Publisher createObject(Object[] columnValues)
	{
		String pubName = Util.getString(columnValues[0]);
		String pubDescription = Util.getString(columnValues[1]);
		String pubCode = Util.getString(columnValues[2]);
		String ipfilter = Util.getString(columnValues[3]);
		
		Publisher pub = new Publisher(pubName, pubDescription, pubCode, ipfilter);
		return pub;
	}

	@Override
	public void removePublisher(String name) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBPublisherDAO", "getPublisher", "publisher=" + name);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_PUBLISHER");
		sqlQuery.setValue("PubName", name, Types.VARCHAR);

		updateDatabase(sqlQuery);
	}

}
