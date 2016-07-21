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

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.ClientTags;

/**
 * Data access methods for {@link ClientTags}
 * 
 * @author navneet
 */
public interface ClientTagsDAO {

    /**
     * Get all clientids with the given tag
     * 
	 **/
    public String[] getClientIdsByTag(String tag) throws DataAccessException;

    /**
     * Get tags for given clientID
     **/
    public String[] getTagsForClient(String clientID) throws DataAccessException;
    
    /**
     * Add tag for a client
     * @param tag
     * @throws DataAccessException
     */
    public void addTagForClient(String clientID, String tag) throws DataAccessException;

}
