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
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.ClientTagsDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.ClientTags;

/**
 * DB implementation of {@link ClientTagsDAO}
 * 
 * @author navneet
 */
public class DBClientTagsDAO extends ImageSpaceDAO<ClientTags> implements ClientTagsDAO {

    DBClientTagsDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) {
        super(factory, connectionProvider, "ClientTagsDAO.xml");
    }

	@Override
	public ClientTags createObject(Object[] columnValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getClientIdsByTag(String tag) throws DataAccessException {
        logger.logp(Level.INFO, "DBClientTagsDAO", "getClientIdsByTag", "Get all clients by tag");
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_CLIENTS_BY_TAG");
        sqlQuery.setValue("Tag", tag, Types.VARCHAR);
        return getRowsWithStringValues(sqlQuery);
	}

	@Override
	public String[] getTagsForClient(String clientID) throws DataAccessException {
        logger.logp(Level.INFO, "DBClientTagsDAO", "getTagsForClient", "Get all tags for a client");
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TAGS_FOR_CLIENT");
        sqlQuery.setValue("ClientID", clientID, Types.VARCHAR);
        return getRowsWithStringValues(sqlQuery);
	}

	@Override
	public void addTagForClient(String clientID, String tag) throws DataAccessException {
        logger.logp(Level.INFO, "DBClientTagsDAO", "addTagForClient", "add tag for a client");
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ADD_TAG_FOR_CLIENT");
        sqlQuery.setValue("ClientID", clientID, Types.VARCHAR);
        sqlQuery.setValue("Tag", tag, Types.VARCHAR);
        
        updateDatabase(sqlQuery);		
	}

}
