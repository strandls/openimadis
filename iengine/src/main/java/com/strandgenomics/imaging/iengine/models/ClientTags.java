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

package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Storable;

/**
 * {@link ClientTag} is a trusted software in the system. Clients can be
 * dynamically added to the system. Each client is associated with a clientID
 * which is required for during generation of access token. Administrator can at
 * any point disable/enable clients in the system. There are two types of
 * clients, those which the server knows how to run and those it cannot run.
 * 
 * @author navneet
 * 
 */
public class ClientTags implements Storable {

    /**
     * 
     */
    private static final long serialVersionUID = 2601821316673363166L;

    /**
     * Unique identifier for the client
     */
    private final String clientID;

    /**
     * Name of the client
     */
    private final String tag;

    /**
     * Create new client instance
     * 
     * @param clientID
     *            id of the client
     * @param name
     *            name of the client
     * @param version
     *            version of the client
     * @param description
     *            description of the client
     * @param user
     *            user who added this client
     * @param isWorkflow
     *            can the server run this client. The client is treated as a
     *            workflow if this is true.
     */
    public ClientTags(String clientID, String tag){
        this.clientID = clientID;
        this.tag = tag;
    }

    /**
     * @return get unique identifier of the client
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * @return get name of the client
     */
    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof ClientTags) {
            if (this.clientID.equals(((ClientTags) obj).getClientID()) && this.tag.equals(((ClientTags) obj).getTag()))
                return true;
        }
        return super.equals(obj);
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
