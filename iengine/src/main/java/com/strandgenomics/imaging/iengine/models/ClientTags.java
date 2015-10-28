/*
 * ClientTags.java
 *
 * Product:  AvadisIMG Server
 *
 * Copyright 2007-2012, Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal,
 * Bangalore 560024
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
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
