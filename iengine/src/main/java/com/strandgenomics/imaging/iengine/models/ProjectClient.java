/*
 * UserClient.java
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
 * {@link UserClient} is a trusted software in the system. Clients can be
 * dynamically added to the system. Each client is associated with a clientID
 * which is required for during generation of access token. Administrator can at
 * any point disable/enable clients in the system. There are two types of
 * clients, those which the server knows how to run and those it cannot run.
 * 
 * @author navneet
 * 
 */
public class ProjectClient implements Storable {

    /**
     * 
     */
    private static final long serialVersionUID = 2601821316673363166L;

    /**
     * Unique identifier for the project client
     */
    private final int id;
    
    /**
     * name for the client or folder
     */
    private final String name;
    
    /**
     * Unique identifier for parent
     */
    private final int parentID;
    
    /**
     * If is folder
     */
    private final int isDirectory;
    
    

    /**
     * Create new userclient instance
     * 
     * @param clientID
     *            id of the client
     * @param user
     *            user who added this client
     */
    public ProjectClient(int id,String name,int parentID,int isDirectory) {
    	this.id=id;
        this.name = name;
        this.parentID= parentID;
        this.isDirectory=isDirectory;
    }
    
    /**
     * @return get unique identifier of the project client
     */
    public int getId() {
        return id;
    } 

    /**
     * @return get name of the client or folder
     */
    public String getName() {
        return name;
    }  
    
    /**
     * @return get parent for the client
     */
    public int getParentID() {
        return parentID;
    }
    
    
    /**
     * @return get isDirectory for client
     */
    public int getIsDirecotry() {
        return isDirectory;
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.icore.Disposable#dispose()
     */
    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Client) {
            if (this.name.equals(((Client) obj).getClientID()))
                return true;
        }
        return super.equals(obj);
    }

}
