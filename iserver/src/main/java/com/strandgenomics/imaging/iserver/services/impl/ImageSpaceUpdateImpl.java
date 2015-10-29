/*
 * ImageSpaceUpdateImpl.java
 *
 * AVADIS Image Management System
 * Web Service Implementation
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

package com.strandgenomics.imaging.iserver.services.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.axis.transport.http.AxisHttpSession;

import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.RecordManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdate;
import com.strandgenomics.imaging.iserver.services.ws.update.VOIndex;

public class ImageSpaceUpdateImpl implements ImageSpaceUpdate, Serializable {
	
	private static final long serialVersionUID = 276586843632433849L;
	private transient Logger logger = null;
	
	public ImageSpaceUpdateImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}
	
    /**
     * Returns a URL to upload the specified attachment
     * @param guid the record (it's GUID) under consideration
     * @param name name of the attachment
     * @return a URL to download the specified attachment
     */
	@Override
    public String getAttachmentUploadURL(String accessToken, long guid, String name) 
    		throws RemoteException
	{
		String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceUpdateImpl", "getAttachmentUploadURL", "creating upload URL for attachment "+name+" for guid "+ guid +" by "+actorLogin);
			return SysManagerFactory.getAttachmentManager().getUploadURL(actorLogin, guid, name, getClientIPAddress());
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceUpdateImpl", "getAttachmentUploadURL", "error creating attachment upload URL "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    /**
     * Deletes the specified named attachment
     * @param guid the record (it's signature) containing the attachment
     * @param name name of the attachment
     */
    @Override
    public void deleteAttachment(String accessToken, long guid, String name) 
    		throws RemoteException
	{
    	Client client = Service.ISPACE.getClient(accessToken);
    	String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceUpdateImpl", "deleteAttachment", "trying to delete attachment "+name +" for record "+ guid +" by "+actorLogin);
			
			Application app = new Application(client.getName(), client.getVersion());
			SysManagerFactory.getAttachmentManager().deleteRecordAttachments(app, actorLogin, accessToken, guid, name);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceUpdateImpl", "deleteAttachment", "error deleting attachment for record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	/**
	 * Adds or replace the notes associated the specified attachment 
	 * @param guid the record under consideration
	 * @param name name of an existing attachment of the record under consideration
	 * @param notes the note to update
	 */
    @Override
    public void updateAttachmentNotes(String accessToken, long guid, String name, String notes) 
    		throws RemoteException
	{
    	String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceUpdateImpl", "updateAttachmentNotes", "trying to update notes on attachment "+name+" of guid "+ guid +" by "+actorLogin);
			SysManagerFactory.getAttachmentManager().updateAttachmentNotes(actorLogin, guid, name,	notes);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceUpdateImpl", "updateAttachmentNotes", "error updating attachment notes for record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}	
	}
    
    /**
     * updates the specified user integer annotation with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
     * @param guid the record under consideration
     * @param name of the meta-data
     * @param value of the annotation
     */
    @Override
    public void updateRecordUserAnnotation(String accessToken, long guid, String name, Object value) 
    		throws RemoteException
	{
    	Client client = Service.UPDATE.getClient(accessToken);
    	String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceUpdateImpl", "updateRecordUserAnnotation", "trying to update user annotation on "+guid+" by "+actorLogin);
			SysManagerFactory.getRecordManager().updateUserAnnotation(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, name, value);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceUpdateImpl", "updateRecordUserAnnotation", "error updating user annotation for record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Deletes all visual overlays for the specified record for all relevant frames and slices for the fixed site
     * @param guid the record signature under consideration 
     * @param siteNo the site
     * @param overlayName name of the visual overlay to delete
     * @return a overlay that is created
     */
    @Override
	public void deleteVisualOverlays(String accessToken, long guid, int siteNo, String overlayName)
			throws RemoteException
	{
    	Client client = Service.UPDATE.getClient(accessToken);
    	String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
    	
		try
		{
			logger.logp(Level.INFO, "ImageSpaceUpdateImpl", "deleteVisualOverlays", "trying to delete visual overlay "+overlayName+" guid "+ guid +" by "+actorLogin);
			RecordManager rm = SysManagerFactory.getRecordManager();
			rm.deleteVisualOverlays(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, siteNo, overlayName);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceUpdateImpl", "deleteVisualOverlays", "error deleting visual overlay "+overlayName +" in record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	/**
	 * Removes the specified visual objects from the visual-overlays of the specified records 
	 * @param guid the record signature under consideration 
	 * @param objectIDs the list of visual objects IDs to delete
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
	@Override
	public void deleteVisualObjects(String accessToken, long guid, 
			int[] objectIDs, String overlayName, VOIndex[] indexes) throws RemoteException
	{
		Client client = Service.UPDATE.getClient(accessToken);
		String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceUpdateImpl", "deleteVisualObjects", "trying to delete visual objects in overlay "+overlayName+" guid "+ guid +" by "+actorLogin);
			RecordManager rm = SysManagerFactory.getRecordManager();
			
			for(VOIndex o : indexes)
			{
				rm.deleteVisualObjects(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, overlayName, new VODimension(o.getFrame(), o.getSlice(), o.getSite()), objectIDs);
			}
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceUpdateImpl", "deleteVisualObjects", "error deleting visual objects for record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * Removes the specified visual objects from the visual-overlays of the specified records 
	 * @param guid the record signature under consideration 
	 * @param objectIDs the list of visual objects IDs to delete
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
	@Override
	public void deleteTextObjects(String accessToken, long guid, 
			int[] objectIDs, String overlayName, VOIndex[] indexes) throws RemoteException
	{
		Client client = Service.UPDATE.getClient(accessToken);
		String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceUpdateImpl", "deleteVisualObjects", "trying to delete visual objects in overlay "+overlayName+" guid "+ guid +" by "+actorLogin);
			RecordManager rm = SysManagerFactory.getRecordManager();
			
			for(VOIndex o : indexes)
			{
				rm.deleteTextObjects(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, overlayName, new VODimension(o.getFrame(), o.getSlice(), o.getSite()), objectIDs);
			}
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceUpdateImpl", "deleteVisualObjects", "error deleting visual objects for record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    private String getClientIPAddress(){

        MessageContext context = MessageContext.getCurrentContext();
        Session session = context.getSession();

        String clientIP = null;
        if(session instanceof AxisHttpSession)
        {
            clientIP = ((AxisHttpSession) session).getClientAddress();
        }

        if(clientIP == null)
            clientIP = "";
        
        return clientIP;
    }

}

