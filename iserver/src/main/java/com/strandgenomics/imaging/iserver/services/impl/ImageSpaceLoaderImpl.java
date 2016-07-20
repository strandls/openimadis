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
 * ImageSpaceLoaderImpl.java
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

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.axis.transport.http.AxisHttpSession;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.RecordCreationManager;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest.RecordSpec;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.Ticket;
import com.strandgenomics.imaging.iserver.services.ws.loader.Archive;
import com.strandgenomics.imaging.iserver.services.ws.loader.Channel;
import com.strandgenomics.imaging.iserver.services.ws.loader.Contrast;
import com.strandgenomics.imaging.iserver.services.ws.loader.CreationRequest;
import com.strandgenomics.imaging.iserver.services.ws.loader.Image;
import com.strandgenomics.imaging.iserver.services.ws.loader.ImageSpaceLoader;
import com.strandgenomics.imaging.iserver.services.ws.loader.RecordBuilderObject;
import com.strandgenomics.imaging.iserver.services.ws.loader.RecordSite;
import com.strandgenomics.imaging.iserver.services.ws.loader.RecordSpecification;
import com.strandgenomics.imaging.iserver.services.ws.loader.UploadTicket;

/**
 * services used by acquisition client etc to upload records into the system
 * @author arunabha
 *
 */
public class ImageSpaceLoaderImpl implements ImageSpaceLoader, Serializable {
	
	private static final long serialVersionUID = 535313497064787492L;
	/**
	 * the logger
	 */
	private transient Logger logger = null;
	
	public ImageSpaceLoaderImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}
	

    /**
     * checks if the specified archive exist, then returns the relevant project
     * @param the access token required to make this call
     * @param archiveSignature archive signature
     * @return the project containing the archive, otherwise null
     * @throws RemoteException
     */
	@Override
    public Archive findArchive(String accessToken, String archiveSignature) throws RemoteException
	{
		String actorLogin = Service.LOADER.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "findArchive", "searching for archive with signature "+archiveSignature +" by "+actorLogin);
			com.strandgenomics.imaging.iengine.models.Archive archive = SysManagerFactory.getProjectManager().findArchive( Util.toBigInteger(archiveSignature) );
			
			return CoercionHelper.toRemoteArchive(archive);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "findArchive", "error searching for archive "+archiveSignature, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
	/**
	 * Returns the URL to download the specified archive
	 * @param the access token required to make this call
	 * @param archiveSignature archive signature
	 * @throws RemoteException 
	 */
	@Override
	public String getArchiveDownloadURL(String accessToken, String archiveSignature) throws RemoteException
	{
		String actorLogin = Service.LOADER.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "getArchiveDownloadURL", "fetching archive download URL for "+archiveSignature);
	   		BigInteger signature = Util.toBigInteger(archiveSignature);
			return SysManagerFactory.getStorageManager().getArchiveDownloadURL(actorLogin, getClientIPAddress(), signature);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "getArchiveDownloadURL", "error fetching download URL for archive signature "+actorLogin, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * Returns the URL to download the specified record as a tar.gz of tiff images and xml meta data
	 * @param the access token required to make this call
	 * @param guid the record GUID
	 * @throws RemoteException 
	 */
	@Override
	public String getRecordDownloadURL(String accessToken, long guid) throws RemoteException
	{
		String actorLogin = Service.LOADER.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "getRecordDownloadURL", "fetching record download URL for "+guid +" from "+actorLogin);
			return null;//TODO
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "getRecordDownloadURL", "error fetching download URL for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * Request for a ticket
	 * @param project the relevant project
	 * @param request the description of the request
	 * @return the ticket
	 * @throws RemoteException
	 */
	@Override
	public UploadTicket recordCreationRequest(String accessToken, String projectName, CreationRequest request)
			throws RemoteException
	{
		Client client = Service.LOADER.getClient(accessToken);
		String actorLogin = Service.LOADER.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", "submitting a record upload request in "+ projectName +" by "+actorLogin);
			RecordCreationRequest req = toRecordCreationRequest(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, projectName, request, false);
			
			Ticket ticket = SysManagerFactory.getTicketManager().requestUploadTicket(req);
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", "created upload ticket "+ ticket +" by "+actorLogin);
			
			UploadTicket uploadTicket = CoercionHelper.toTicketObject(ticket);
			
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", "returning upload ticket" + uploadTicket);
			System.out.println("returning upload ticket "+uploadTicket);
			return uploadTicket;
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", ex.getMessage(), ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	@Override
	public UploadTicket directUploadCreationRequest(String accessToken, String projectName, CreationRequest request) throws RemoteException
	{
		Client client = Service.LOADER.getClient(accessToken);
		String actorLogin = Service.LOADER.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", "submitting a record upload request in "+ projectName +" by "+actorLogin);
			RecordCreationRequest req = toRecordCreationRequest(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, projectName, request, true);
			
			Ticket ticket = SysManagerFactory.getTicketManager().requestUploadTicket(req);
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", "created upload ticket "+ ticket +" by "+actorLogin);
			
			UploadTicket uploadTicket = CoercionHelper.toTicketObject(ticket);
			
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", "returning upload ticket" + uploadTicket);
			System.out.println("returning upload ticket "+uploadTicket);
			return uploadTicket;
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "recordCreationRequest", ex.getMessage(), ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * Returns the status of the specified ticketID
	 * @param ticketID  ticket id
	 * @return status - WAITING, EXECUTING, SUCCESS, FAILURE
	 */
	@Override
	public String getTicketStatus(String accessToken, long ticketID) throws RemoteException
	{
		String actorLogin = Service.LOADER.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "getTicketStatus", "finding status of ticket "+ ticketID +" by "+actorLogin);
			//TODO check access rights
			Status status = SysManagerFactory.getTicketManager().getTicketStatus(ticketID);		
			return status == null ? null : status.name();
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceLoaderImpl", "getTicketStatus", "error finding status for ticket "+ticketID, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
	private RecordCreationRequest toRecordCreationRequest(Application app, String actorLogin, String accessToken, String projectName, CreationRequest cRequest, boolean directUpload) 
	{
		String clientIPAddress = getClientIPAddress();
		String clientMacAddress = cRequest.getClientMacAddress();
		String clientRootDirectory = cRequest.getArchive().getRootDirectory();
		String clientFilename = cRequest.getArchive().getName();
		
		BigInteger archiveSignature = new BigInteger(Util.hexToByteArray(cRequest.getArchive().getSignature()));
		
		List<ISourceReference> clientFiles = CoercionHelper.toSourceReference(cRequest.getArchive().getSourceFiles());
		List<RecordSpec> recordSpec = toRecordSpec( cRequest.getValidSignatures() );
		
		return new RecordCreationRequest(app, actorLogin, accessToken, projectName, clientIPAddress, clientMacAddress, 
				clientRootDirectory, clientFilename, archiveSignature, clientFiles, recordSpec, directUpload);
	}
	
	public static final List<RecordSpec> toRecordSpec(RecordSpecification[] specs)
	{
		if(specs == null || specs.length == 0) 
			return null;
		
		List<RecordSpec> specList = new ArrayList<RecordSpec>();
		for(int i = 0;i < specs.length; i++)
		{
			RecordSpec s = toRecordSpec(specs[i]);
			if(s != null) specList.add( s );
		}
		
		return specList;
	}
	
	public static final RecordSpec toRecordSpec(RecordSpecification r)
	{
		if(r == null) return null;
		
		List<Site> sitesToMerge = toSiteList( r.getSiteToMerge() );
		List<com.strandgenomics.imaging.icore.Channel> customChannels = toChannel( r.getCustomChannels() );
		
		return new RecordSpec(sitesToMerge, customChannels);
	}
	
	public static List<List<Site>> toSiteList(RecordSite[][] sites) 
	{
		if(sites == null || sites.length == 0) 
			return null;
		
		List<List<Site>> listOfListOfSites = new ArrayList<List<Site>>();
		for(int i = 0;i < sites.length; i++)
		{
			List<Site> siteList = toSiteList(sites[i]);
			if(siteList != null) listOfListOfSites.add( siteList );
		}
		
		return listOfListOfSites;
	}

	private static List<Site> toSiteList(RecordSite[] recordSites) 
	{
		if(recordSites == null || recordSites.length == 0) 
			return null;
		
		List<Site> siteList = new ArrayList<Site>();
		for(int i = 0;i < recordSites.length; i++)
		{
			Site site = toSite(recordSites[i]);
			if(site != null) siteList.add( site );
		}
		
		return siteList;
	}

	private static Site toSite(RecordSite recordSite) 
	{
		return new Site(recordSite.getSeriesNo(), recordSite.getName());
	}
	
	public static final List<com.strandgenomics.imaging.icore.Channel> toChannel(Channel[] cList)
	{
		if(cList == null || cList.length == 0) return null;
		List<com.strandgenomics.imaging.icore.Channel> channels = new ArrayList<com.strandgenomics.imaging.icore.Channel>();
		
		for(Channel rc : cList)
		{
			com.strandgenomics.imaging.icore.Channel c = toChannel(rc);
			if(c != null) channels.add(c);
		}
		
		return channels;
	}
	
	public static final com.strandgenomics.imaging.icore.Channel toChannel(Channel rc)
	{
		if(rc == null) return null;
		com.strandgenomics.imaging.icore.Channel c = new com.strandgenomics.imaging.icore.Channel(rc.getName(), rc.getLutName());
		c.setContrast(false, toVisualContrast(rc.getContrast()) );
		c.setContrast(true, toVisualContrast(rc.getZStackedContrast()) );
		c.setWavelength(rc.getWavelength());
		return c;
	}
	
	public static final VisualContrast toVisualContrast(Contrast rc)
	{
		if(rc == null) return null;
		return new VisualContrast(rc.getMinIntensity(), rc.getMaxIntensity(), rc.getGamma());
	}
	
    private String getClientIPAddress(){

        MessageContext context = MessageContext.getCurrentContext();
        Session session = context.getSession();

        String clientIP = null;
        if(session instanceof AxisHttpSession)
        {
            //clientIP = ((AxisHttpSession) session).getClientAddress();
        }

        if(clientIP == null)
            clientIP = "";
        
        return clientIP;
    }


	@Override
	public void commitRecordCreation(String accessToken, long guid) throws RemoteException
	{
		Client client = Service.LOADER.getClient(accessToken);
    	String actorLogin = Service.LOADER.validateAccessToken(accessToken);
    	
    	Application app = new Application(client.getName(), client.getVersion());
    	
    	logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "commit", "committing record "+guid);
    	try
		{
    		RecordCreationManager recordCreationManager = SysManagerFactory.getRecordCreationManager();
    		
    		recordCreationManager.commitRecord(app, actorLogin, accessToken, guid);
		}
		catch (IOException ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceLoaderImpl", "commit", "error committing record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	@Override
	public Long registerRecordBuilder(String accessToken, RecordBuilderObject rb) throws RemoteException
	{
		logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "registerRecordBuilder", "received request from record "+rb);
		
		if(rb == null) return null;

		logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "registerRecordBuilder", "registering record "+rb.getRecordLabel());
		
		int projectID = SysManagerFactory.getProjectManager().getProject(rb.getProjectName()).getProjectID();
		
		return registerRecordBuilder(accessToken, rb.getRecordLabel(), rb.getParentRecord(), 
				projectID, rb.getUploadedBy(), rb.getNoOfFrames(),
				rb.getNoOfSlices(), rb.getImageWidth(), rb.getImageHeight(),
				rb.getChannels(), rb.getSites(), rb.getImageDepth(),
				rb.getXPixelSize(), rb.getYPixelSize(), rb.getZPixelSize(),
				rb.getSourceType(), rb.getImageType(), rb.getMachineIP(),
				rb.getMacAddress(), rb.getSourceFolder(),
				rb.getSourceFilename(), rb.getSourceTime(),
				rb.getCreationTime(), rb.getAcquiredTime());
	}


	@Override
	public void abortRecordCreation(String accessToken, long guid) throws RemoteException
	{
		Client client = Service.LOADER.getClient(accessToken);
    	String actorLogin = Service.LOADER.validateAccessToken(accessToken);
    	
    	Application app = new Application(client.getName(), client.getVersion());
    	
    	logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "abortRecordCreation", "aborting record creation "+guid);
    	try
		{
    		RecordCreationManager recordCreationManager = SysManagerFactory.getRecordCreationManager();
    		
    		recordCreationManager.abort(app, actorLogin, guid);
		}
		catch (IOException ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceLoaderImpl", "commit", "error aborting record creation"+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	private Long registerRecordBuilder(String accessToken, String recordLabel, Long parentGuid, int projectID, String uploadedBy, int noOfFrames, int noOfSlices,
			int imageWidth, int imageHeight, Channel[] channels, RecordSite[] sites, int imageDepth, double xPixelSize, double yPixelSize, 
			double zPixelSize, String sourceType, int imageType, String machineIP, String macAddress, String sourceFolder, String sourceFilename, Long sourceTime, 
			Long creationTime, Long acquiredTime) throws RemoteException
	{
    	Client client = Service.LOADER.getClient(accessToken);
    	String actorLogin = Service.LOADER.validateAccessToken(accessToken);
    	
    	Application app = new Application(client.getName(), client.getVersion());
    	
		try
		{
			
			List<com.strandgenomics.imaging.icore.Channel> serverChannels = toChannel(channels);
			logger.logp(Level.FINEST, "ImageSpaceLoaderImpl", "registerRecordBuilder", "converted channels "+recordLabel);
			List<Site> serverSites = toSiteList(sites);
			logger.logp(Level.FINEST, "ImageSpaceLoaderImpl", "registerRecordBuilder", "converted sites "+recordLabel);
			PixelDepth depth = PixelDepth.toPixelDepth(imageDepth);
			logger.logp(Level.FINEST, "ImageSpaceLoaderImpl", "registerRecordBuilder", "converted depth "+recordLabel);
			ImageType type = ImageType.values()[imageType];
			logger.logp(Level.FINEST, "ImageSpaceLoaderImpl", "registerRecordBuilder", "converted type "+recordLabel);
			
			RecordCreationManager recordCreationManager = SysManagerFactory.getRecordCreationManager();
			
			Long guid = recordCreationManager.registerRecord(app, actorLogin,
					recordLabel, parentGuid, projectID, actorLogin, noOfSlices, noOfFrames,
					imageWidth, imageHeight, serverChannels, serverSites,
					depth, xPixelSize, yPixelSize, zPixelSize,
					new SourceFormat(sourceType), type, machineIP, macAddress,
					sourceFolder, sourceFilename, sourceTime, creationTime,
					acquiredTime);
			return guid;
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceLoaderImpl", "registerRecordBuilder", "error registering record "+recordLabel, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public String addImageData(String accessToken, long guid, com.strandgenomics.imaging.iserver.services.ws.loader.ImageIndex index, Image imageData) throws RemoteException
	{
		Client client = Service.LOADER.getClient(accessToken);
    	String actorLogin = Service.LOADER.validateAccessToken(accessToken);
    	
    	Application app = new Application(client.getName(), client.getVersion());
    	
    	logger.logp(Level.WARNING, "ImageSpaceLoaderImpl", "addImageData", "adding image data to record "+guid);
    	try
		{
    		RecordCreationManager recordCreationManager = SysManagerFactory.getRecordCreationManager();
    		Dimension dim = toDimension(index);
    		
    		recordCreationManager.addImageData(app, actorLogin, guid, dim,CoercionHelper.toImagePixelData(imageData));
    		
    		return recordCreationManager.getUploadURL(actorLogin, guid, dim, getClientIPAddress());
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceLoaderImpl", "commit", "error committing record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	private Dimension toDimension(com.strandgenomics.imaging.iserver.services.ws.loader.ImageIndex index)
	{
		if(index == null) return null;
		
		Dimension dim = new Dimension(index.getFrame(), index.getSlice(), index.getChannel(), index.getSite());
		return dim;
	}
}
