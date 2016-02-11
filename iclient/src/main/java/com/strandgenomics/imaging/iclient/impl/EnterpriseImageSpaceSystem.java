/*
 * EnterpriseImageSpaceSystem.java
 *
 * AVADIS Image Management System
 * Client Side WS Hook
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
package com.strandgenomics.imaging.iclient.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisProperties;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import com.strandgenomics.imaging.iclient.AcquisitionProfile;
import com.strandgenomics.imaging.iclient.Application;
import com.strandgenomics.imaging.iclient.AuthenticationException;
import com.strandgenomics.imaging.iclient.BookmarkFolder;
import com.strandgenomics.imaging.iclient.CoercionHelper;
import com.strandgenomics.imaging.iclient.DuplicateRequestException;
import com.strandgenomics.imaging.iclient.Experiment;
import com.strandgenomics.imaging.iclient.IllegalRequestException;
import com.strandgenomics.imaging.iclient.ImageSpaceException;
import com.strandgenomics.imaging.iclient.ImageSpaceSystem;
import com.strandgenomics.imaging.iclient.InsufficientPermissionException;
import com.strandgenomics.imaging.iclient.InvalidAccessTokenException;
import com.strandgenomics.imaging.iclient.Job;
import com.strandgenomics.imaging.iclient.PixelMetaData;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.Publisher;
import com.strandgenomics.imaging.iclient.QuotaExceededException;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.iclient.ServerIsBusyException;
import com.strandgenomics.imaging.iclient.Task;
import com.strandgenomics.imaging.iclient.Ticket;
import com.strandgenomics.imaging.iclient.Tile;
import com.strandgenomics.imaging.iclient.User;
import com.strandgenomics.imaging.iclient.VisualOverlay;
import com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorization;
import com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorizationServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.compute.ImageSpaceCompute;
import com.strandgenomics.imaging.iclient.impl.ws.compute.ImageSpaceComputeServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.compute.NVPair;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.Area;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.HistoryItem;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageSpaceService;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageSpaceServiceServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.MosaicParameters;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.MosaicRequest;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.MosaicResource;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex;
import com.strandgenomics.imaging.iclient.impl.ws.loader.Image;
import com.strandgenomics.imaging.iclient.impl.ws.loader.ImageSpaceLoader;
import com.strandgenomics.imaging.iclient.impl.ws.loader.ImageSpaceLoaderServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.loader.RecordBuilderObject;
import com.strandgenomics.imaging.iclient.impl.ws.manage.ImageSpaceManagement;
import com.strandgenomics.imaging.iclient.impl.ws.manage.ImageSpaceManagementServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.search.ImageSpaceSearch;
import com.strandgenomics.imaging.iclient.impl.ws.search.ImageSpaceSearchServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.update.ImageSpaceUpdate;
import com.strandgenomics.imaging.iclient.impl.ws.update.ImageSpaceUpdateServiceLocator;
import com.strandgenomics.imaging.iclient.local.DirectUploadExperiment;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.util.InstallCert;
import com.strandgenomics.imaging.iclient.util.LoadCert;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.app.Parameter;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.LUT;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.HttpUtil;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;

import ucar.nc2.util.net.EasySSLProtocolSocketFactory;

/**
 * A concrete implementation of an Enterprise wide available ImageSpace system
 * 
 * @author arunabha
 * 
 */
public class EnterpriseImageSpaceSystem extends ImageSpaceSystem {

	/**
	 * handle to the iAuth web-service stub
	 */
	private ImageSpaceAuthorization iauth = null;
	/**
	 * handle to the iSpace web-service stub
	 */
	private ImageSpaceService ispace = null;
	/**
	 * handle to the iLoader web-service stub
	 */
	private ImageSpaceLoader iloader = null;
	/**
	 * handle to the iSearch web-service stub
	 */
	private ImageSpaceSearch isearch = null;
	/**
	 * handle to the iUpdate web-service stub
	 */
	private ImageSpaceUpdate iupdate = null;
	/**
	 * handle to the iCompute web-service stub
	 */
	private ImageSpaceCompute icompute = null;
	/**
	 * handle to the iManage web-service stub
	 */
	private ImageSpaceManagement imanage = null;
	/**
	 * the clientID of this application
	 */
	private String clientID;
	/**
	 * the access token for the client obtained from the iAuth Service
	 */
	private String accessToken;
	
	/**
	 * Logs in the user to Enterprise IMG Server
	 * 
	 * @param host
	 *            the IP address of the Enterprise IMG Server machine
	 * @param port
	 *            the port to use
	 * @param appID
	 *            Client/Application Identifier - obtained after registering the
	 *            client/Application with iManage (web client)
	 * @param authCode
	 *            the authorisation grant obtained by the user for the above
	 *            mentioned clientID (again using iManage)
	 * @throws AuthenticationException
	 */
	@Override
	public boolean login(boolean useSSL, String host, int port, String clientID, String authCode) throws AuthenticationException {
		synchronized (this) {
			try {
				//decide whether to use SSL
				String protocol = getProtocol(useSSL);
				
				if(protocol.equals("https"))
				{
					// install required certificate
					
			        
			        //String path = InstallCert.certPath(host, port, proxyHost, proxyPort, proxyUser, proxyPassword);
			        System.setProperty("javax.net.ssl.trustStore","jssecacerts" );
			        //LoadCert.installFakeTrustManager();
					//AxisProperties.setProperty("axis.socketSecureFactory",
							 //"org.apache.axis.components.net.SunFakeTrustSocketFactory");
					//Protocol.unregisterProtocol("https");
					//EasySSLProtocolSocketFactory easySSLProtocolSocketFactory = new EasySSLProtocolSocketFactory();
				        //Protocol.registerProtocol("https", new Protocol("https",
				        //           (ProtocolSocketFactory) easySSLProtocolSocketFactory, 443));
				}
				
				String baseURL = getBaseURL();
				
				System.out.println(protocol);
				System.out.println(host);
				System.out.println(port);
				System.out.println(baseURL);
				
				System.out.println("Locating relavent services");
				ImageSpaceAuthorizationServiceLocator iAuthLocator = new ImageSpaceAuthorizationServiceLocator();
				iauth = iAuthLocator.getiAuth(new URL(protocol, host, port, baseURL+"iAuth"));
				
				System.out.println("logging in");
				//get the relevant access token to access the methods of the services
				accessToken = iauth.getAccessToken(clientID, authCode);
				
				System.out.println("requesting user id");
				//login id of the user on whose behalf the access code is generated
				user = iauth.getUser(accessToken);
				
				System.out.println("user "+user+" logged in successfully");
				
				initServices(protocol, host, port, baseURL);
				
				System.out.println("Services initialized");
				
				this.host = host;//keep this for information only
				this.port = port;//keep this for information only
				this.clientID = clientID; //keep this for refreshing the authorisation code
				this.useSSL = useSSL;// keep this for information only
				return true;
			} catch(Exception ex) {
				ex.printStackTrace();
				handleLoginError(ex);
				return false;
			}
		}
	}
    
    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iclient.ImageSpace#getAccessKey()
     */
    @Override
	public String getAccessKey() {
		return accessToken;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#setAccessKey(boolean, java.lang.String, int, java.lang.String)
	 */
	@Override
	public boolean setAccessKey(boolean useSSL, String host, int port, String accessToken) {
		this.accessToken = accessToken;
		
		try {
			initServices(getProtocol(useSSL), host, port, getBaseURL());
			this.host = host;//keep this for information only
			this.port = port;//keep this for information only
			this.useSSL = useSSL;// keep this for information only
			return true;
		} catch(Exception ex) {
			handleLoginError(ex);
			return false;
		}
	}
	
	/**
	 * @param useSSL
	 * @return
	 */
	private String getProtocol(boolean useSSL) {
		return useSSL ? "https" : "http";
	}
	
	/**
	 * @return
	 */
	private String getBaseURL() {
		return "/" + Constants.getWebApplicationContext() + "/services/";
	}

	/**
	 * @param protocol
	 * @param host
	 * @param port
	 * @param baseURL
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	private void initServices(String protocol, String host, int port, String baseURL) throws MalformedURLException, ServiceException {
		ImageSpaceServiceServiceLocator iSpacelocator = new ImageSpaceServiceServiceLocator();
		ispace = iSpacelocator.getiSpace(new URL(protocol, host, port, baseURL + "iSpace"));
		
		ImageSpaceLoaderServiceLocator iLoaderLocator = new ImageSpaceLoaderServiceLocator();
		iloader = iLoaderLocator.getiLoader(new URL(protocol, host, port, baseURL + "iLoader"));
		
		ImageSpaceSearchServiceLocator iSearchLocator = new ImageSpaceSearchServiceLocator();
		isearch = iSearchLocator.getiSearch(new URL(protocol, host, port, baseURL + "iSearch"));
		
		ImageSpaceUpdateServiceLocator iUpdateLocator = new ImageSpaceUpdateServiceLocator();
		iupdate = iUpdateLocator.getiUpdate(new URL(protocol, host, port, baseURL + "iUpdate"));
		
		ImageSpaceComputeServiceLocator iComputeLocator = new ImageSpaceComputeServiceLocator();
		icompute = iComputeLocator.getiCompute(new URL(protocol, host, port, baseURL + "iCompute"));
		
		ImageSpaceManagementServiceLocator iManageLocator = new ImageSpaceManagementServiceLocator();
		imanage = iManageLocator.getiManage(new URL(protocol, host, port, baseURL + "iManage"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#logout()
	 */
	public void logout() {
		try {
			iauth.surrenderAccessToken(clientID, accessToken);
		} catch (Exception ex) {
			handleError(ex);
		}
	}

	/**
	 * Refreshes the access token with a new authorisation code( generally after
	 * the access token expires)
	 * 
	 * @param authCode
	 *            the new authorisation code for the current client identifier
	 * @throws RemoteException
	 */
	public synchronized void refreshAccessToken(String authCode) throws RemoteException {
		//get the relevant access token to access the methods of the services
		accessToken = iauth.getAccessToken(clientID, authCode);
		//login id of the user on whose behalf the access code is generated
		user = iauth.getUser(accessToken); 
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#isArchiveExist(java.math.BigInteger)
	 */
	@Override
	public boolean isArchiveExist(BigInteger archiveSignature) {
		validate();
		try {
			return ispace.findProjectForArchive(accessToken, Util.toHexString(archiveSignature)) != null;
		} catch (RemoteException ex) {
			handleError(ex);
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#findGUID(com.strandgenomics.imaging.icore.Signature)
	 */
	@Override
	public long findGUID(Signature signature) {
		validate();
		try {
			return ispace.findGUID(accessToken, CoercionHelper.toRemoteSignature(signature));
		} catch (RemoteException ex) {
			return -1;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRecordChannels(long)
	 */
	@Override
	public List<Channel> getRecordChannels(long guid) {
		validate();
		try {
			if (guid == -1)
				return null;
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Channel[] remoteChannels = ispace.getRecordChannels(accessToken, guid);
			return CoercionHelper.toLocalChannel(remoteChannels);
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRecordSites(long)
	 */
	@Override
	public List<Site> getRecordSites(long guid) {
		validate();
		try {
			if (guid == -1)
				return null;
			com.strandgenomics.imaging.iclient.impl.ws.ispace.RecordSite[] remoteSites = ispace.getRecordSite(accessToken, guid);
			return CoercionHelper.toSiteList(remoteSites);
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#findProject(long)
	 */
	@Override
	public Project findProject(long guid) {
		validate();
		try {
			if (guid == -1)
				return null;
			String projectName = ispace.findProjectForRecord(accessToken, guid);
			return CoercionHelper.toLocalProject(ispace.findProject(accessToken, projectName));
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#findProjectName(long)
	 */
	@Override
	public String findProjectName(long guid) {
		validate();
		try {
			if (guid == -1)
				return null;
			String projectName = ispace.findProjectForRecord(accessToken, guid);
			return projectName;
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#getActiveProjects()
	 */
	@Override
	public List<Project> getActiveProjects() {
		validate();
		List<Project> projects = null;
		try {
			String[] projectNames = ispace.getActiveProjects(accessToken);
			if (projectNames == null)
				return null;
			projects = new ArrayList<Project>();
			for (String projectName : projectNames) {
				Project p = CoercionHelper.toLocalProject(ispace.findProject(accessToken, projectName));
				if (p != null)
					projects.add(p);
			}
		} catch (RemoteException ex) {
			handleError(ex);
		}
		return projects;
	}
	public List<String> getActiveProjectsNames() {
		validate();
		String[] projectNames = null;
		try {
			projectNames = ispace.getActiveProjects(accessToken);
			if (projectNames == null)
				return null;
		} catch (RemoteException ex) {
			handleError(ex);
		}
		List<String> projectNamesList = null;
		if(projectNames!=null){
			projectNamesList = Arrays.asList(projectNames);  
		}
		return projectNamesList;
	}
	
	public List<Project> getActiveProjectsUpload() {   // to get projects for which user has upload permission. Not used anywhere right now
		validate();
		List<Project> projects = null;
		try {
			String[] projectNames = ispace.getActiveProjects(accessToken);
			if (projectNames == null)
				return null;
			projects = new ArrayList<Project>();
			for (String projectName : projectNames) {
				Project p = CoercionHelper.toLocalProject(ispace.findProject(accessToken, projectName));
				if (p != null)
					projects.add(p);
			}
		} catch (RemoteException ex) {
			handleError(ex);
		}
		return projects;
	}
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#getArchivedProjects()
	 */
	@Override
	public List<Project> getArchivedProjects() {
		validate();
		List<Project> projects = null;
		try {
			String[] projectNames = ispace.getArchivedProjects(accessToken);
			if (projectNames == null)
				return null;
			
			projects = new ArrayList<Project>();
			for (String projectName : projectNames) {
				Project p = CoercionHelper.toLocalProject(ispace.findProject(accessToken, projectName));
				if (p != null)
					projects.add(p);
			}
		} catch (RemoteException ex) {
			handleError(ex);
		}
		return projects;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getProjectMembers(java.lang.String)
	 */
	@Override
	public List<User> getProjectMembers(String projectName) {
		validate();
		List<User> memberList = null;
		try {
			memberList = CoercionHelper.toLocalUser(imanage.getProjectMembers(accessToken, projectName));
		} catch (RemoteException ex) {
			handleError(ex);
		}
		return memberList;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getProjectManager(java.lang.String)
	 */
	@Override
	public List<User> getProjectManager(String projectName) {
		validate();
		List<User> managers = null;
		try {
			managers = CoercionHelper.toLocalUser(imanage.getProjectManager(accessToken, projectName));
		} catch (RemoteException ex) {
			handleError(ex);
		}
		return managers;
	}
	
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceManagement#createNewProject(java.lang.String, java.lang.String, double)
	 */
	@Override
	public Project createNewProject(String projectName, String notes, double storageQuota) {
		validate();
		if (projectName == null || storageQuota < 0)
			return null;
		try {
			boolean success = imanage.createNewProject(accessToken, projectName, notes, storageQuota);
			if (success) {
				return new Project(projectName, notes, new Date(), storageQuota);
			} else {
				return null;// TODO
			}
		} catch (RemoteException e) {
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceManagement#allowExternalUser(java.lang.String, java.lang.String, com.strandgenomics.imaging.icore.Rank)
	 */
	@Override
	public boolean allowExternalUser(String login, String email, Rank rank) {
		validate();
		if (login == null)
			return false;
		try {
			return imanage.allowExternalUser(accessToken, login, email, rank.name());
		} catch (RemoteException ex) {
			handleError(ex);
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceManagement#createInternalUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.strandgenomics.imaging.icore.Rank)
	 */
	@Override
	public boolean createInternalUser(String login, String password, String email, String fullName, Rank rank) {
		validate();
		if (login == null || password == null)
			return false;
		try {
			return imanage.createInternalUser(accessToken, login, password, email, fullName, rank.name());
		} catch (RemoteException ex) {
			handleError(ex);
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#requestTicket(com.strandgenomics.imaging.iclient.Project, com.strandgenomics.imaging.iclient.local.RawExperiment)
	 */
	@Override
	public Ticket requestTicket(Project project, RawExperiment experiment) {
		validate();
		try {
			if(experiment instanceof DirectUploadExperiment)
				return toLocalTicket(iloader.directUploadCreationRequest(accessToken, project.getName(), CoercionHelper.toCreationRequest(experiment)));
			
			return toLocalTicket(iloader.recordCreationRequest(accessToken, project.getName(), CoercionHelper.toCreationRequest(experiment)));
		} catch (MalformedURLException ex) {
			handleError(ex);
			return null;
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getTicketStatus(com.strandgenomics.imaging.iclient.Ticket)
	 */
	@Override
	public Status getTicketStatus(Ticket ticket) {
		validate();
		try {
			return Status.valueOf(iloader.getTicketStatus(accessToken, ticket.getID()));
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addProjectMembers(java.lang.String, com.strandgenomics.imaging.icore.Permission, java.lang.String[])
	 */
	@Override
	public void addProjectMembers(String projectName, Permission p, String... userLogins) {
		validate();
		if (userLogins == null)
			return;
		try {
			imanage.addProjectMembers(accessToken, projectName, userLogins, p.name());
		} catch (RemoteException ex) {
			handleError(ex);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#findProject(java.lang.String)
	 */
	@Override
	public Project findProject(String projectName) {
		validate();
		if (projectName == null)
			return null;
		try {
			return CoercionHelper.toLocalProject(ispace.findProject(accessToken, projectName));
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getGUIDsForArchive(com.strandgenomics.imaging.iclient.Experiment)
	 */
	@Override
	public long[] getGUIDsForArchive(Experiment experiment) {
		validate();
		if (experiment == null)
			return null;
		try {
			return ispace.listGUIDsForArchive(accessToken, Util.toHexString(experiment.getMD5Signature()));
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#listArchives(com.strandgenomics.imaging.iclient.Project)
	 */
	@Override
	public List<BigInteger> listArchives(Project project) {
		validate();
		if (project == null)
			return null;
		try {
			String[] archiveSignature = ispace.listArchives(accessToken, project.getName());
			if (archiveSignature == null || archiveSignature.length == 0)
				return null;
			List<BigInteger> signs = new ArrayList<BigInteger>();
			for (String hexString : archiveSignature) {
				signs.add(Util.toBigInteger(hexString));
			}
			return signs;
		} catch (RemoteException ex) {
			handleError(ex);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#setRecordThumbnail(long, java.io.File)
	 */
	@Override
	public void setRecordThumbnail(long guid, File imageFile) {
		validate();
		try {
			String url = ispace.getThumbnailUploadURL(accessToken, guid);
			URL uploadURL = toServerURL(url);
			HttpUtil httpUtil = new HttpUtil(uploadURL, "image/x-png");
			httpUtil.upload(imageFile);
		} catch (Exception ex) {
			handleError(ex);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRecordThumbnail(long)
	 */
	@Override
	public BufferedImage getRecordThumbnail(long guid) {
		validate();
		try {
			String url = ispace.getThumbnailDownloadURL(accessToken, guid);
			URL downloadURL = toServerURL(url);
			HttpUtil httpUtil = new HttpUtil(downloadURL);
			return readImage(httpUtil.getInputStream(null));
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#exportExperiment(java.math.BigInteger)
	 */
	@Override
	public InputStream exportExperiment(BigInteger experimentID)
	{
		validate();
		
		try
		{
			String url = iloader.getArchiveDownloadURL(accessToken, Util.toHexString(experimentID));
			URL downloadURL = toServerURL(url);
			
			HttpUtil httpUtil = new HttpUtil(downloadURL);
			return httpUtil.getInputStream(null);
		}
		catch(Exception ex)
		{
			handleError(ex);
		}
		
		return null;
	}
	
	/**
	 * @param rex
	 * @throws AuthenticationException
	 */
	protected void handleLoginError(Exception rex) throws AuthenticationException {

		ErrorCode error = ErrorCode.extractErrorCode(rex.getMessage());
        if(error == null)
        {
            throw new AuthenticationException(rex.getMessage());
        }

        switch(error.getCode())
        {
        	case ErrorCode.ImageSpace.INVALID_CREDENTIALS:
        		throw new AuthenticationException(error.getErrorMessage());
        	default:
        		throw new AuthenticationException(error.getErrorMessage());
        }
	}
	
	/**
	 * @param rex
	 * @throws ImageSpaceException
	 */
	protected void handleError(Exception rex) throws ImageSpaceException
	{
		rex.printStackTrace(System.err);
        
		ErrorCode error = ErrorCode.extractErrorCode(rex.getMessage());
        if(error == null){
            throw new ImageSpaceException(rex.getMessage());
        }
        
        switch(error.getCode())
        {
        	case ErrorCode.ImageSpace.INVALID_CREDENTIALS:
        		throw new AuthenticationException(error.getErrorMessage());
        	case ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS:
        		throw new InsufficientPermissionException(error.getErrorMessage());
        	case ErrorCode.ImageSpace.TICKET_ALREADY_EXIST:
        		throw new IllegalRequestException(error.getErrorMessage());
        	case ErrorCode.ImageSpace.ARCHIVE_ALREADY_EXIST:
        		throw new DuplicateRequestException(error.getErrorMessage());
        	case ErrorCode.ImageSpace.QUOTA_EXCEEDED:
        		throw new QuotaExceededException(error.getErrorMessage());
        	case ErrorCode.ImageSpace.TICKET_NOT_AVAILABLE:
        		throw new ServerIsBusyException(error.getErrorMessage());
        	case ErrorCode.Authorization.INVALID_ACCESS_TOKEN:
        		throw new InvalidAccessTokenException(error.getErrorMessage());
        	default:
        		throw new ImageSpaceException(error.getErrorMessage());
        }
	}
	
	/**
	 * @param t
	 * @return
	 * @throws MalformedURLException
	 */
	public Ticket toLocalTicket(com.strandgenomics.imaging.iclient.impl.ws.loader.UploadTicket t) throws MalformedURLException 
	{
		if(t == null) return null;
		BigInteger archiveID = new BigInteger( Util.hexToByteArray( t.getArchiveSignature() ) );
		URL uploadURL = toServerURL(t.getUploadURL());
		URL downloadURL = toServerURL(t.getDownloadURL());
		
		return CoercionHelper.toLocalTicket(t.getID(), archiveID, uploadURL, downloadURL);
	}
	
	/**
	 * converts a string url to URL object
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
    public URL toServerURL(String url) throws MalformedURLException
    {
        URL serverURL = null;
       
        if(url == null) 
        {
            ;
        }
        else if(url.startsWith("http")) 
        {
        	serverURL = new URL(url);
        }
        else 
        {
            //decide whether to use SSL ?? not really !! we do not use SSL for data transfer ??
        	String protocol = this.useSSL? "https" : "http";// decide depending upon the scheme
            String path = "/"+Constants.getWebApplicationContext() +"/"+ url;
            //the url provided by the server is a relative path
            serverURL = new URL(protocol, host, port, path);
        }
        
        return serverURL;
    }

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getUserAnnotations(long)
	 */
	@Override
	public Map<String, Object> getUserAnnotations(long guid) 
	{
		validate();
		
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Property[] metaData = ispace.getRecordUserAnnotations(accessToken, guid);
			
			Map<String, Object> annotations = new HashMap<String, Object>();
			if(metaData == null || metaData.length == 0)
				return annotations;

			for(int i = 0;i < metaData.length; i++)
			{
				if(metaData[i].getName() != null && metaData[i].getValue() != null)
				{
					annotations.put(metaData[i].getName(), metaData[i].getValue());
				}
			}
			
			return annotations;
		}
		catch (RemoteException ex) 
		{
			handleError(ex);
			return null;
		}
	}
	
	/**
	 * @param guid
	 * @param name
	 * @param value
	 */
	private void addRecordUserMetaData(long guid, String name, Object value)
	{
		validate();
		try 
		{	
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Property p = new com.strandgenomics.imaging.iclient.impl.ws.ispace.Property(name, value);
			ispace.addRecordUserAnnotation(accessToken, guid, new com.strandgenomics.imaging.iclient.impl.ws.ispace.Property[]{p});
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addRecordUserAnnotation(long, java.lang.String, long)
	 */
	@Override
	public void addRecordUserAnnotation(long guid, String name, long value) 
	{
		addRecordUserMetaData(guid, name, value);
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addRecordUserAnnotation(long, java.lang.String, double)
	 */
	@Override
	public void addRecordUserAnnotation(long guid, String name, double value) 
	{
		addRecordUserMetaData(guid, name, value);
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addRecordUserAnnotation(long, java.lang.String, java.lang.String)
	 */
	@Override
	public void addRecordUserAnnotation(long guid, String name, String value) 
	{
		addRecordUserMetaData(guid, name, value);
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addRecordUserAnnotation(long, java.lang.String, java.util.Date)
	 */
	@Override
	public void addRecordUserAnnotation(long guid, String name, Date value) 
	{
		if(value == null) return;
		
		Calendar c = Calendar.getInstance();
		c.setTime(value);
		
		addRecordUserMetaData(guid, name, c);
	}
	
	/**
	 * @param guid
	 * @param name
	 * @param value
	 */
	private void updateRecordUserMetaData(long guid, String name, Object value) 
	{
		validate();
		try 
		{
			iupdate.updateRecordUserAnnotation(accessToken, guid, name, value);
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#updateRecordUserAnnotation(long, java.lang.String, long)
	 */
	@Override
	public void updateRecordUserAnnotation(long guid, String name, long value) 
	{
		updateRecordUserMetaData(guid, name, value);
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#updateRecordUserAnnotation(long, java.lang.String, double)
	 */
	@Override
	public void updateRecordUserAnnotation(long guid, String name, double value) 
	{
		updateRecordUserMetaData(guid, name, value);
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#updateRecordUserAnnotation(long, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateRecordUserAnnotation(long guid, String name, String value) 
	{
		updateRecordUserMetaData(guid, name, value);
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#updateRecordUserAnnotation(long, java.lang.String, java.util.Date)
	 */
	@Override
	public void updateRecordUserAnnotation(long guid, String name, Date value) 
	{
		if(value == null) return;
		Calendar c = Calendar.getInstance();
		c.setTime(value);
		
		updateRecordUserMetaData(guid, name, c);
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addRecordUserAnnotation(long, java.util.Map)
	 */
	@Override
	public void addRecordUserAnnotation(long guid, Map<String, Object> annotations) 
	{
		validate();
		
		com.strandgenomics.imaging.iclient.impl.ws.ispace.Property[] propertyList = CoercionHelper.toPropertyList(annotations);
		
		try 
		{
			ispace.addRecordUserAnnotation(accessToken, guid, propertyList);
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRecordAttachments(long)
	 */
	@Override
	public Collection<IAttachment> getRecordAttachments(long guid) 
	{
		validate();
		
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.RecordAttachment[] attachments = ispace.getRecordAttachments(accessToken, guid);

			List<IAttachment> localAttachments = new ArrayList<IAttachment>();
			
			if(attachments!=null)
			{
				for (int i = 0; i < attachments.length; i++) 
				{
					IAttachment localAttachment = CoercionHelper.toLocalAttachment(guid, attachments[i]);
					localAttachments.add(localAttachment);
				}
			}
			
			return localAttachments;
		}
		catch (RemoteException e)
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#updateAttachmentNotes(long, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateAttachmentNotes(long guid, String attachmentName, String notes) 
	{
		validate();
		try 
		{
			iupdate.updateAttachmentNotes(accessToken, guid, attachmentName, notes);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#deleteAttachment(long, java.lang.String)
	 */
	@Override
	public void deleteAttachment(long guid, String attachmentName)
	{
		validate();
		try 
		{
			iupdate.deleteAttachment(accessToken, guid, attachmentName);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addRecordAttachments(long, java.io.File, java.lang.String, java.lang.String)
	 */
	@Override
	public void addRecordAttachments(long guid, File attachmentFile, String name, String notes) 
	{
		validate();
		try 
		{
			name = name == null ? attachmentFile.getName() : name;
			// inserts attachment name and notes returning upload URL
			String url = ispace.createRecordAttachment(accessToken, guid, name, notes);

			// actual upload of attachment 
			URL uploadURL = toServerURL(url);
			HttpUtil httpUtil = new HttpUtil(uploadURL);
			httpUtil.upload(attachmentFile);
		} 
		catch (RemoteException e)
		{
			handleError(e);
		} 
		catch (MalformedURLException e) 
		{
			handleError(e);
		} 
		catch (IOException e) 
		{
			handleError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#uploadTaskLog(long, java.io.File)
	 */
	@Override
	public void uploadTaskLog(long jobID, File logFile)
	{
		try 
		{
			String url = icompute.getTaskLogUploadURL(accessToken, jobID, logFile.getName());
			
			// actual upload of attachment 
			URL uploadURL = toServerURL(url);
			HttpUtil httpUtil = new HttpUtil(uploadURL);
			httpUtil.upload(logFile);
		} 
		catch (RemoteException e)
		{
			handleError(e);
		} 
		catch (MalformedURLException e) 
		{
			handleError(e);
		} 
		catch (IOException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#listAvailableFormats()
	 */
	@Override
	public Collection<SourceFormat> listAvailableFormats() 
	{
		validate();
		try 
		{
			String[] formats = ispace.listAvailableFormats(accessToken);

			List<SourceFormat> localFormats = new ArrayList<SourceFormat>();
			for (int i = 0; i < formats.length; i++)
			{
				localFormats.add(new SourceFormat(formats[i]));
			}

			return localFormats;
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceManagement#deleteProject(java.lang.String)
	 */
	@Override
	public Task deleteProject(String projectName) 
	{
		validate();
		try 
		{
			return CoercionHelper.toLocalTask(imanage.deleteProject(accessToken, projectName));
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceManagement#archiveProject(java.lang.String)
	 */
	@Override
	public Task archiveProject(String projectName)
	{
		validate();
		try 
		{
			return CoercionHelper.toLocalTask(imanage.archiveProject(accessToken, projectName));
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceManagement#restoreProject(java.lang.String)
	 */
	@Override
	public Task restoreProject(String projectName) 
	{
		validate();
		try
		{
			return CoercionHelper.toLocalTask(imanage.restoreProject(accessToken, projectName));
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getNavigableFields(com.strandgenomics.imaging.iclient.Project)
	 */
	@Override
	public Collection<SearchField> getNavigableFields(Project project)
	{
		validate();
		try 
		{
			return CoercionHelper.toSearchField(isearch.getNavigableFields(accessToken, project.getName()));
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getUserAnnotationFields(com.strandgenomics.imaging.iclient.Project)
	 */
	@Override
	public Collection<SearchField> getUserAnnotationFields(Project project)
	{
		validate();
		try 
		{
			return CoercionHelper.toSearchField( isearch.getAvailableUserAnnotations(accessToken, project.getName()) );
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return null;
		}
	}

	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#findRecords(com.strandgenomics.imaging.iclient.Project, java.util.Set)
	 */
	@Override
	public long[] findRecords(Project parentProject, Set<SearchCondition> conditions)
	{
		validate();
		try 
		{
			return isearch.findRecords(accessToken, parentProject.getName(), CoercionHelper.toRemoteSearchConditions(conditions));
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#search(java.lang.String, java.util.Set, java.util.Set, int)
	 */
	@Override
	public long[] search(String freeText, Set<String> projectNames, Set<SearchCondition> filters, int maxResult)
	{
		if(freeText == null) return null;
		validate();
		
		try 
		{
			return isearch.search(accessToken, freeText, 
					projectNames == null ? null : projectNames.toArray(new String[0]),
							CoercionHelper.toRemoteSearchConditions(filters), maxResult);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#findNavigationBins(com.strandgenomics.imaging.iclient.Project, java.util.Set, com.strandgenomics.imaging.icore.SearchCondition)
	 */
	@Override
	public List<NavigationBin> findNavigationBins(Project project, Set<SearchCondition> preConditions, SearchCondition current) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[] remoteConditions = CoercionHelper.toRemoteSearchConditions(preConditions);
			com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition rc = CoercionHelper.toRemoteSearchCondition(current);
			
			com.strandgenomics.imaging.iclient.impl.ws.search.SearchNode[] remoteNodes = isearch.getNavigationBins(accessToken, project.getName(), remoteConditions, rc);
			return CoercionHelper.toLocalNavigationNodes(remoteNodes, current);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}
	

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getAttachmentInputStream(long, java.lang.String)
	 */
	@Override
	public InputStream getAttachmentInputStream(long guid, String attachmentName) 
	{
		validate();
		try 
		{
			String url = ispace.getAttachmentDownloadURL(accessToken, guid, attachmentName);
			
			URL downloadURL = toServerURL(url);
			HttpUtil httpUtil = new HttpUtil(downloadURL);
			
			InputStream is = httpUtil.getInputStream(null);
			File tempFile = File.createTempFile("Attachment", ".xml");
			OutputStream os = new FileOutputStream(tempFile);
			Util.transferData(is, os, true);
			
			return new FileInputStream(tempFile);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		} 
		catch (MalformedURLException e) 
		{
			handleError(e);
			return null;
		} 
		catch (IOException e) 
		{
			handleError(e);
			return null;
		}
	}


	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getPixelDataForRecord(com.strandgenomics.imaging.iclient.Record, com.strandgenomics.imaging.icore.Dimension)
	 */
	@Override
	public IPixelData getPixelDataForRecord(Record parent, Dimension imageCordinate) 
	{
		validate();

		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = CoercionHelper.toImageCoordinate(imageCordinate);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Image pixelData = ispace.getPixelDataForRecord(accessToken, parent.getGUID(), index);
			return CoercionHelper.toPixelData(parent, pixelData);
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getChannelOverlaidImagesForSlices(long, int, int, int, boolean)
	 */
	@Override
	public InputStream getChannelOverlaidImagesForSlices(long guid, int frameNo, int siteNo, 
			int imageWidth, boolean useChannelColor)
	{
		validate();
		try 
		{
			String url = ispace.getChannelOverlaidSliceImagesURL(accessToken, guid, frameNo, siteNo, imageWidth, useChannelColor);
			URL downloadURL = toServerURL(url);
			
			HttpUtil httpUtil = new HttpUtil(downloadURL);
			return httpUtil.getInputStream(null);
		} 
		catch (Exception e)
		{
			handleError(e);
			return null;
		} 
	}
	
	/**
	 * Find the record for the specified global universal identifier for record
	 * 
	 * @param guid
	 *            the universal identifier for records
	 * @return the record
	 */
	@Override
	public Record findRecordForGUID(long guid)
	{
		if(guid == -1) return null;
		
		long[] guids = new long[] {guid};
		Record[] rList = findRecordForGUIDs(guids);
		return rList == null || rList.length == 0 ? null : rList[0];
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#findRecordForGUIDs(long[])
	 */
	@Override
	public Record[] findRecordForGUIDs(long[] guids)
	{
		if(guids == null || guids.length == 0)
			return null;
		
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Record[] r = ispace.findRecordForGUIDs(accessToken, guids);
			Record[] rList = CoercionHelper.toLocalRecord(r);
			
			return rList;
		} 
		catch (Exception e)
		{
			handleError(e);
			return null;
		} 
	}
	
	/**
	 * @param freeText
	 * @param projectNames
	 * @param filters
	 * @param maxResult
	 * @return
	 */
	public long[] search(String freeText, List<String> projectNames, List<SearchCondition> filters, int maxResult)
	{
		validate();
		try 
		{
			return isearch.search(accessToken, freeText, 
					projectNames == null ? null : projectNames.toArray(new String[0]),
							CoercionHelper.toRemoteSearchConditions(filters), maxResult);
		} 
		catch (Exception e)
		{
			handleError(e);
			return null;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////
	////////////////////////// visual annotation methods ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getEllipticalShapes(long, com.strandgenomics.imaging.iclient.VisualOverlay)
	 */
	@Override
	public Collection<Ellipse> getEllipticalShapes(long guid, VisualOverlay visualOverlay)
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(visualOverlay.voID);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.EllipticalShape[] eShapes = ispace.getEllipticalShapes(accessToken, guid, index, visualOverlay.getName());
			
			List<Ellipse> shapes = new ArrayList<Ellipse>();
			for(int i=0;i<eShapes.length;i++){
				shapes.add(CoercionHelper.toLocalEllipseShape(eShapes[i]));
			}
			
			return shapes;
		} 
		catch (RemoteException e)
		{
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getLineSegments(long, com.strandgenomics.imaging.iclient.VisualOverlay)
	 */
	@Override
	public Collection<LineSegment> getLineSegments(long guid, VisualOverlay visualOverlay)
	{
		validate();
		try
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(visualOverlay.voID);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.StraightLine[] eShapes = ispace.getLineSegments(accessToken, guid, index, visualOverlay.getName());
			
			List<LineSegment> shapes = new ArrayList<LineSegment>();
			for(int i=0;i<eShapes.length;i++)
			{
				shapes.add(CoercionHelper.toLocalLineSegment(eShapes[i]));
			}
			
			return shapes;
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRectangularShapes(long, com.strandgenomics.imaging.iclient.VisualOverlay)
	 */
	@Override
	public Collection<Rectangle> getRectangularShapes(long guid, VisualOverlay visualOverlay) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(visualOverlay.voID);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.RectangularShape[] eShapes = ispace.getRectangularShapes(accessToken, guid, index, visualOverlay.getName());
			
			List<Rectangle> shapes = new ArrayList<Rectangle>();
			for(int i=0;i<eShapes.length;i++){
				shapes.add(CoercionHelper.toLocalRectangleShape(eShapes[i]));
			}
			
			return shapes;
		}
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getTextBoxes(long, com.strandgenomics.imaging.iclient.VisualOverlay)
	 */
	@Override
	public Collection<TextBox> getTextBoxes(long guid, VisualOverlay visualOverlay) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(visualOverlay.voID);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.TextArea[] eShapes = ispace.getTextBoxes(accessToken, guid, index, visualOverlay.getName());
			
			List<TextBox> shapes = new ArrayList<TextBox>();
			for(int i=0;i<eShapes.length;i++)
			{
				shapes.add(CoercionHelper.toLocalTextArea(eShapes[i]));
			}
			
			return shapes;
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getFreeHandShapes(long, com.strandgenomics.imaging.iclient.VisualOverlay)
	 */
	@Override
	public Collection<GeometricPath> getFreeHandShapes(long guid, VisualOverlay visualOverlay) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(visualOverlay.voID);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.FreehandShape[] eShapes = ispace.getFreeHandShapes(accessToken, guid, index, visualOverlay.getName());
			
			List<GeometricPath> shapes = new ArrayList<GeometricPath>();
			for(int i=0;i<eShapes.length;i++)
			{
				shapes.add(CoercionHelper.toLocalFreeHandShapes(eShapes[i]));
			}
			
			return shapes;
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getVisualObjects(long, com.strandgenomics.imaging.iclient.VisualOverlay)
	 */
	@Override
	public Collection<VisualObject> getVisualObjects(long guid, VisualOverlay visualOverlay) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(visualOverlay.voID);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape[] eShapes = ispace.getVisualObjects(accessToken, guid, index, visualOverlay.getName());
			
			if(eShapes == null || eShapes.length == 0)
				return null;
			
			return CoercionHelper.toLocalVisualObjects(eShapes);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getVisualOverlays(long, com.strandgenomics.imaging.icore.VODimension)
	 */
	@Override
	public Collection<IVisualOverlay> getVisualOverlays(long guid, VODimension dimension)
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(dimension);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Overlay[] vaList = ispace.getVisualOverlays(accessToken, guid, index);
			
			if(vaList == null || vaList.length == 0)
				return null;
			
			return CoercionHelper.toLocalOverlays(vaList, guid, dimension);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getVisualOverlay(long, com.strandgenomics.imaging.icore.VODimension, java.lang.String)
	 */
	@Override
	public IVisualOverlay getVisualOverlay(long guid, VODimension coordinate, String name) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex index = CoercionHelper.toVOCoordinate(coordinate);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Overlay va = ispace.getVisualOverlay(accessToken, guid, index, name);
			
			return CoercionHelper.toLocalOverlay(va, guid, coordinate);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getAvailableVisualOverlays(long, int)
	 */
	@Override
	public Set<String> getAvailableVisualOverlays(long guid, int siteNo)
	{
		validate();
		try 
		{
			String[] names = ispace.getAvailableVisualOverlays(accessToken, guid, siteNo);
			if(names == null || names.length == 0)
				return null;
			
			Set<String> nameSets = new HashSet<String>();
			nameSets.addAll(Arrays.asList(names));
			return nameSets;
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#createVisualOverlays(long, int, java.lang.String)
	 */
	@Override
	public void createVisualOverlays(long guid, int siteNo, String name)
	{
		validate();
		try 
		{
			ispace.createVisualOverlays(accessToken, guid, siteNo, name);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#deleteVisualOverlays(long, int, java.lang.String)
	 */
	@Override
	public void deleteVisualOverlays(long guid, int siteNo, String name) 
	{
		validate();
		try 
		{
			iupdate.deleteVisualOverlays(accessToken, guid, siteNo, name);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addVisualObjects(long, java.util.Collection, java.lang.String, com.strandgenomics.imaging.icore.VODimension[])
	 */
	@Override
	public void addVisualObjects(long guid, Collection<VisualObject> vObjects, String name, VODimension ... imageCoordinates) 
	{
		if(imageCoordinates == null || imageCoordinates.length == 0)
			return;
		
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex[] vocs = CoercionHelper.toVOCoordinate(imageCoordinates);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape[] shapeList = CoercionHelper.toShapes(vObjects);
			
			ispace.addVisualObjects(accessToken, guid, shapeList, name, vocs);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#deleteVisualObjects(long, java.util.Collection, java.lang.String, com.strandgenomics.imaging.icore.VODimension[])
	 */
	@Override
	public void deleteVisualObjects(long guid, Collection<VisualObject> vObjects, String name, VODimension ... index) 
	{
		if(index == null || index.length == 0)
			return;
		
		validate();
		try 
		{
			List<com.strandgenomics.imaging.iclient.impl.ws.update.VOIndex> vodList = new ArrayList<com.strandgenomics.imaging.iclient.impl.ws.update.VOIndex>();
			for(VODimension vo : index)
			{
				com.strandgenomics.imaging.iclient.impl.ws.update.VOIndex vod = new com.strandgenomics.imaging.iclient.impl.ws.update.VOIndex();
				vod.setFrame(vo.frameNo);
				vod.setSite(vo.siteNo);
				vod.setSlice(vo.sliceNo);
				
				vodList.add( vod );
			}
			
			com.strandgenomics.imaging.iclient.impl.ws.update.VOIndex[] voArray = vodList.toArray(new com.strandgenomics.imaging.iclient.impl.ws.update.VOIndex[0]);
			
			List<Integer> textIDs = new ArrayList<Integer>();
			List<Integer> otherIDs = new ArrayList<Integer>();
			
			for(VisualObject obj : vObjects)
			{
				if(obj instanceof TextBox)
					textIDs.add(obj.ID);
				else
					otherIDs.add(obj.ID);
			}
			
			
			if(!textIDs.isEmpty())
			{
				iupdate.deleteTextObjects(accessToken, guid, Util.toIntArray(textIDs), name, voArray);
			}
			
			if(!otherIDs.isEmpty())
			{
				iupdate.deleteVisualObjects(accessToken, guid, Util.toIntArray(otherIDs), name, voArray);
			}
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addUserComment(long, java.lang.String)
	 */
	@Override
	public void addUserComment(long guid, String comments) 
	{
		if(comments == null || comments.trim().length() == 0)
			return;
		
		validate();
		
		try 
		{
			ispace.addUserComment(accessToken, guid, comments.trim());
		} 
		catch (RemoteException e) 
		{
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#fetchUserComment(long)
	 */
	@Override
	public List<UserComment> fetchUserComment(long guid) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Comments[] cList = ispace.fetchUserComment(accessToken, guid);
			return CoercionHelper.toUserComment(cList);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getDynamicMetaData(long)
	 */
	@Override
	public Map<String, Object> getDynamicMetaData(long guid) 
	{
		validate();
		try 
		{
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Property[] dynamicMetaData = ispace.getDynamicMetaData(accessToken, guid);
			return CoercionHelper.toLocalPropertyMap(dynamicMetaData);
		} 
		catch (RemoteException e) 
		{
			handleError(e);
			return null;
		}
	}

	
	/////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// IMAGE APIs /////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRawPixelData(long, com.strandgenomics.imaging.icore.Dimension)
	 */
	@Override
	public PixelArray getRawPixelData(long guid, Dimension d) {
		validate();
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = CoercionHelper.toImageCoordinate(d);
			String url = ispace.getRawIntensitiesDownloadURL(accessToken, guid, index);
			URL downloadURL = toServerURL(url);
			return readPixelArray(downloadURL);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getPixelDataImage(long, com.strandgenomics.imaging.icore.Dimension, boolean)
	 */
	@Override
	public BufferedImage getPixelDataImage(long guid, Dimension d, boolean useChannelColor) {
		validate();
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = CoercionHelper.toImageCoordinate(d);
			String url = ispace.getImageDownloadURL(accessToken, guid, useChannelColor, index);
			URL downloadURL = toServerURL(url);
			return readImage(downloadURL);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getOverlayedImage(long, int, int, int, int[], boolean, boolean, boolean, int, int, int, int)
	 */
	@Override
	public BufferedImage getOverlayedImage(long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos, boolean zStacked, boolean mosaic,
			boolean useChannelColor, int x, int y, int width, int height) {
		validate();
		try {
			// String accessToken, long guid, int frameNo, int sliceNo, int
			// siteNo, int[] channels, boolean useChannelColor, boolean
			// zStacked, boolean mosaic
			String url = ispace.getOverlayImageDownloadURL(accessToken, guid, frameNo, sliceNo, siteNo, channelNos, useChannelColor, zStacked, mosaic, x, y, width, height);
			URL downloadURL = toServerURL(url);
			return readImage(downloadURL);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	@Override
	public BufferedImage getOverlayedImage(long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos, boolean zStacked, boolean mosaic,
			boolean useChannelColor, int x, int y, int width, int height, int targetWidth, int targetHeight)
	{
		validate();
		try {
			// String accessToken, long guid, int frameNo, int sliceNo, int
			// siteNo, int[] channels, boolean useChannelColor, boolean
			// zStacked, boolean mosaic
			String url = ispace.getOverlayImageDownloadURL(accessToken, guid, frameNo, sliceNo, siteNo, channelNos, useChannelColor, zStacked, mosaic, x, y, width, height, targetWidth, targetHeight);
			URL downloadURL = toServerURL(url);
			return readARGBImage(downloadURL);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getOverlayedImage(long, int, int, int, int[], boolean, boolean, boolean, int, int, int, int, int[])
	 */
	@Override
	public BufferedImage getOverlayedImage(long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos, boolean zStacked, boolean mosaic,
			boolean useChannelColor, int x, int y, int width, int height, int[] contrasts) {
		validate();
		try {
			if (contrasts != null) {
				// String accessToken, long guid, int frameNo, int sliceNo, int
				// siteNo, int[] channels, boolean useChannelColor, boolean
				// zStacked, boolean mosaic
				String url = ispace.getOverlayImageDownloadURL(accessToken, guid, frameNo, sliceNo, siteNo, channelNos, useChannelColor, zStacked, mosaic, x, y, width, height, contrasts);
				URL downloadURL = toServerURL(url);
				return readImageWithContrast(downloadURL);
			} else
				return getOverlayedImage(guid, sliceNo, frameNo, siteNo, channelNos, zStacked, mosaic, useChannelColor, x, y, width, height);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRawDataForTile(long, com.strandgenomics.imaging.iclient.Tile)
	 */
	@Override
	public PixelArray getRawDataForTile(long guid, Tile t) {
		validate();
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = CoercionHelper.toImageCoordinate(t.getDimension());
			String url = ispace.getTileIntensitiesDownloadURL(accessToken, guid, index, t.x, t.y, t.width, t.height);
			URL downloadURL = toServerURL(url);
			return readPixelArray(downloadURL);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getTileImage(long, com.strandgenomics.imaging.iclient.Tile, boolean)
	 */
	@Override
	public BufferedImage getTileImage(long guid, Tile t, boolean useChannelColor) {
		validate();
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = CoercionHelper.toImageCoordinate(t.getDimension());
			String url = ispace.getTileImageDownloadURL(accessToken, guid, useChannelColor, index, t.x, t.y, t.width, t.height);
			URL downloadURL = toServerURL(url);
			return readImage(downloadURL);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getIntensityDistibutionForImage(long, com.strandgenomics.imaging.icore.Dimension)
	 */
	@Override
	public Histogram getIntensityDistibutionForImage(long guid, Dimension coordinate) {
		validate();
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = CoercionHelper.toImageCoordinate(coordinate);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Statistics stat = ispace.getIntensityDistibution(accessToken, guid, index);
			return CoercionHelper.toHistogram(stat);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getIntensityDistibutionForTile(long, com.strandgenomics.imaging.icore.Dimension, java.awt.Rectangle)
	 */
	@Override
	public Histogram getIntensityDistibutionForTile(long guid, Dimension coordinate, java.awt.Rectangle rect) {
		validate();
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = CoercionHelper.toImageCoordinate(coordinate);
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Statistics stat = ispace.getIntensityDistibutionForTile(accessToken, guid, index, rect.x, rect.y, rect.width, rect.height);
			return CoercionHelper.toHistogram(stat);
		} catch (Exception ex) {
			handleError(ex);
		}
		return null;
	}
	
	/**
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	private BufferedImage readImage(InputStream inStream) throws IOException {
		BufferedInputStream in = null;
		BufferedImage renderableImage = null;
		try
		{
			in = new BufferedInputStream(inStream);
			renderableImage = ImageIO.read(in);
		}
		finally
		{
			Util.closeStream(in);
		}
		
		return renderableImage;
	}
	
	/**
	 * @param downloadURL
	 * @return
	 * @throws IOException
	 */
	private BufferedImage readImage(URL downloadURL) throws IOException {
		return readImageURL(downloadURL, false);
	}
	
	private BufferedImage readARGBImage(URL downloadURL) throws IOException{
		return readImageURL(downloadURL, true);
	}
	
	/**
	 * downloadURL and create image of required type
	 * @param downloadURL
	 * @param isARGB
	 * @return
	 * @throws IOException
	 */
	private BufferedImage readImageURL(URL downloadURL, boolean isARGB) throws IOException {
		DataInputStream inStream = null;
		try {
			HttpUtil httpUtil = new HttpUtil(downloadURL);
			try{
				inStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(httpUtil.getInputStream(null))));
			}catch(java.util.zip.ZipException e){
				
			}
			
			PixelDepth depth = PixelDepth.toPixelDepth( inStream.readInt() );
			int width = inStream.readInt();
			int height = inStream.readInt();
			
			BufferedImage image = null;
			
			if (depth.equals(PixelDepth.INT)) {
				int[] rgbPixels = new int[width * height];
				for (int i = 0; i < rgbPixels.length; i++)
					rgbPixels[i] = inStream.readInt();
				
				if(isARGB)
					image = PixelArray.getRGBAImage(width, height, rgbPixels);
				else
					image = PixelArray.getRGBImage(width, height, rgbPixels);
					
			} else if (depth.equals(PixelDepth.BYTE)) {
				byte[] data = new byte[width * height];
				inStream.readFully(data);
				
				//read the LUT
				byte[] red = new byte[256], green = new byte[256], blue = new byte[256];
				inStream.readFully(red);
				inStream.readFully(green);
				inStream.readFully(blue);
				
				PixelArray pixels = new PixelArray.Byte(data, width, height);
				pixels.setColorModel(new LUT(red, green, blue));
				image = pixels.createImage();
			}
			return image;
		}
		finally {
			Util.closeStream(inStream);
		}
	}
	
	/**
	 * @param downloadURL
	 * @return
	 * @throws IOException
	 */
	private BufferedImage readImageWithContrast(URL downloadURL) throws IOException {
		DataInputStream inStream = null;
		try {
			HttpUtil httpUtil = new HttpUtil(downloadURL);
			inStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(httpUtil.getInputStream(null))));
			
			PixelDepth depth = PixelDepth.toPixelDepth( inStream.readInt() );
			int width = inStream.readInt();
			int height = inStream.readInt();
			int min = inStream.readInt();
			int max = inStream.readInt();
			
			BufferedImage image = null;
			
			if(depth.equals(PixelDepth.INT)) {
				int[] rgbPixels = new int[width*height];
				for(int i = 0;i < rgbPixels.length; i++)
					rgbPixels[i] = inStream.readInt();
				
				image = PixelArray.getRGBImage(width, height, rgbPixels);
			} else if(depth.equals(PixelDepth.BYTE)) {
				byte[] data = new byte[width*height];
				inStream.readFully(data);
				
				//read the LUT
				byte[] red = new byte[256], green = new byte[256], blue = new byte[256];
				inStream.readFully(red);
				inStream.readFully(green);
				inStream.readFully(blue);
				
				PixelArray pixels = new PixelArray.Byte(data, width, height);
				pixels.setColorModel(new LUT(red, green, blue));
				pixels.setContrast(min, max);
				
				image = pixels.createImage();
			}
			
			return image;
		} finally {
			Util.closeStream(inStream);
		}
	}
	
	/**
	 * @param downloadURL
	 * @return
	 * @throws IOException
	 */
	private PixelArray readPixelArray(URL downloadURL) throws IOException {
		InputStream inStream = null;
		try {
			HttpUtil httpUtil = new HttpUtil(downloadURL);
			inStream = new BufferedInputStream(new GZIPInputStream(httpUtil.getInputStream(null)));
			PixelArray rawData = PixelArray.read(inStream);
			return rawData;
		} finally {
			Util.closeStream(inStream);
		}
	}
	
	private void validate() {
		if (accessToken == null)
			throw new ImageSpaceException("access token not found");
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#getPublisher(com.strandgenomics.imaging.iclient.Application)
	 */
	@Override
	public Publisher getPublisher(Application application) {
		try {
			icompute.getPublisher(accessToken, application.getName(), application.getVersion());
		} catch (RemoteException e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#listApplications(com.strandgenomics.imaging.iclient.Publisher, java.lang.String)
	 */
	@Override
	public List<Application> listApplications(Publisher publisher, String categoryName) {
		com.strandgenomics.imaging.iclient.impl.ws.compute.Application[] applications;
		try {
			applications = icompute.listApplications(accessToken, publisher.getName(), categoryName);
			return CoercionHelper.toLocalApplications(applications);
		} catch (RemoteException e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#getParameters(com.strandgenomics.imaging.iclient.Application)
	 */
	@Override
	public Set<Parameter> getParameters(Application application) {
		try {
			com.strandgenomics.imaging.iclient.impl.ws.compute.Parameter[] parameters = icompute.getApplicationParameters(accessToken, application.getName(), application.getVersion());
			return CoercionHelper.toLocalParameters(parameters);
		} catch (RemoteException e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#getJobState(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public State getJobState(Job job) {
		try {
			String state = icompute.getJobState(accessToken, job.getJobID());
			return State.valueOf(state);
		} catch (Exception e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#rescheduleTask(com.strandgenomics.imaging.iclient.Job, long)
	 */
	@Override
	public boolean rescheduleTask(Job job, long rescheduleTime) {
		try {
			return icompute.rescheduleTask(accessToken, job.getJobID(),
					rescheduleTime);
		} catch (Exception e) {
			handleError(e);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#pauseTask(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public boolean pauseTask(Job job) {
		try {
			return icompute.pauseTask(accessToken, job.getJobID());
		} catch (Exception e) {
			handleError(e);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#resumeTask(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public boolean resumeTask(Job job) {
		try {
			return icompute.resumeTask(accessToken, job.getJobID());
		} catch (Exception e) {
			handleError(e);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#removeTask(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public boolean removeTask(Job job) {
		try {
			return icompute.removeTask(accessToken, job.getJobID());
		} catch (Exception e) {
			handleError(e);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#terminateTask(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public boolean terminateTask(Job job) {
		try {
			return icompute.terminateTask(accessToken, job.getJobID());
		} catch (Exception e) {
			handleError(e);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#getTaskProgress(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public int getTaskProgress(Job job) {
		try {
			int progress = icompute.getTaskProgress(accessToken, job.getJobID());
			return progress;
		} catch (Exception e) {
			handleError(e);
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#setTaskProgress(long, int)
	 */
	@Override
	public void setTaskProgress(long jobId, int progress) {
		try {
			icompute.setTaskProgress(accessToken, jobId, progress);
		} catch (Exception e) {
			handleError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#getTaskParameters(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public Map<String, Object> getTaskParameters(Job job) {
		try {
			NVPair[] parameters = icompute.getTaskParameters(accessToken, job.getJobID());
			return CoercionHelper.toParametersMap(parameters);
		} catch (Exception e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#getTaskInputs(com.strandgenomics.imaging.iclient.Job)
	 */
	@Override
	public long[] getTaskInputs(Job job) {
		try {
			long[] inputs = icompute.getTaskInputs(accessToken, job.getJobID());
			return inputs;
		} catch (Exception e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#getTaskOutputs(java.lang.String, long)
	 */
	@Override
	public long[] getTaskOutputs(String accessToken, long jobID) {
		try {
			long[] outputs = icompute.getTaskOutputs(accessToken, jobID);
			return outputs;
		} catch (Exception e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#executeApplication(com.strandgenomics.imaging.iclient.Application, java.util.Map, com.strandgenomics.imaging.icore.app.Priority, java.lang.String, long[])
	 */
	@Override
	public Job executeApplication(Application application, Map<String, Object> parameters, Priority priority, String projectName, long... guids) {
		NVPair[] nvPairParameters = CoercionHelper.toRemoteComputeNVPairs(parameters);
		try {
			long jobID = icompute.executeApplication(accessToken,application.getName(), application.getVersion(), projectName, nvPairParameters, guids, priority.ordinal());
			return CoercionHelper.toLocalJob(jobID);
		} catch (RemoteException e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceWorkflow#scheduleApplication(com.strandgenomics.imaging.iclient.Application, java.util.Map, com.strandgenomics.imaging.icore.app.Priority, java.lang.String, long, long[])
	 */
	@Override
	public Job scheduleApplication(Application application, Map<String, Object> parameters, Priority priority, String projectName, long scheduleTime, long... guids) {
		NVPair[] nvPairParameters = CoercionHelper.toRemoteComputeNVPairs(parameters);
		try {
			long jobID = icompute.scheduleApplication(accessToken, application.getName(), application.getVersion(), projectName, nvPairParameters, guids, priority.ordinal(), scheduleTime);
			return CoercionHelper.toLocalJob(jobID);
		} catch (RemoteException e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getRecordHistory(long)
	 */
	@Override
	public List<com.strandgenomics.imaging.iclient.HistoryItem> getRecordHistory(long guid) {
		try {
			HistoryItem[] history = ispace.getRecordHistory(accessToken, guid);
			return CoercionHelper.toLocalHistory(history);
		} catch (Exception e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addCustomHistory(long, java.lang.String)
	 */
	@Override
	public void addCustomHistory(long guid, String historyMessage) {
		try {
			ispace.addRecordHistory(accessToken, guid, historyMessage);
		} catch (Exception e) {
			handleError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#registerRecordBuilder(java.lang.String, java.lang.String, java.lang.Long, int, int, int, int, java.util.List, java.util.List, com.strandgenomics.imaging.icore.image.PixelDepth, double, double, double, java.lang.String, com.strandgenomics.imaging.icore.ImageType, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public long registerRecordBuilder(String recordLabel, String projectName, Long parentGuid, int noOfFrames, int noOfSlices, int imageWidth,
			int imageHeight, List<Channel> channels, List<Site> sites, PixelDepth imageDepth, double xPixelSize, double yPixelSize,
			double zPixelSize, String sourceType, ImageType imageType, String machineIP, String macAddress, String sourceFolder,
			String sourceFilename, Long sourceTime, Long creationTime, Long acquiredTime) {
		try {
			com.strandgenomics.imaging.iclient.impl.ws.loader.Channel[] remoteChannels = CoercionHelper.toRemoteChannel(channels);
			com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[] remoteSites = CoercionHelper.toRemoteSite(sites);
			
			RecordBuilderObject rb = new RecordBuilderObject();
			rb.setAcquiredTime(acquiredTime);
			rb.setChannels(remoteChannels);
			rb.setCreationTime(creationTime);
			rb.setImageDepth(imageDepth.getByteSize());
			rb.setImageHeight(imageHeight);
			rb.setImageWidth(imageWidth);
			rb.setImageType(imageType.ordinal());
			rb.setMacAddress(macAddress);
			rb.setMachineIP(machineIP);
			rb.setNoOfFrames(noOfFrames);
			rb.setNoOfSlices(noOfSlices);
			rb.setProjectName(projectName);
			rb.setRecordLabel(recordLabel);
			rb.setSites(remoteSites);
			rb.setSourceFilename(sourceFilename);
			rb.setSourceFolder(sourceFolder);
			rb.setSourceTime(sourceTime);
			rb.setSourceType(sourceType);
			rb.setXPixelSize(xPixelSize);
			rb.setYPixelSize(yPixelSize);
			rb.setZPixelSize(zPixelSize);
			rb.setParentRecord(parentGuid);
			
			Long guid = iloader.registerRecordBuilder(accessToken, rb);
			return guid;
		} catch (Exception e) {
			handleError(e);
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addImageData(long, com.strandgenomics.imaging.icore.Dimension, com.strandgenomics.imaging.icore.image.PixelArray, com.strandgenomics.imaging.iclient.PixelMetaData)
	 */
	@Override
	public boolean addImageData(long builderId, Dimension dim,
			PixelArray pixelArr, PixelMetaData pixelData) {
		try {
			Image imageData = CoercionHelper.toRemoteImageData(pixelData);
			
			String url = iloader.addImageData(accessToken, builderId, CoercionHelper.toImageIndex(dim), imageData);
			URL uploadURL = toServerURL(url);
			HttpUtil httpUtil = new HttpUtil(uploadURL);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			pixelArr.write(bos);
			return httpUtil.upload(bos.toByteArray());
		} catch (Exception e) {
			handleError(e);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#commitRecordBuilder(long)
	 */
	@Override
	public void commitRecordBuilder(long builderId) {
		try {
			iloader.commitRecordCreation(accessToken, builderId);
		} catch (RemoteException e) {
			handleError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#abortRecordBuilder(long)
	 */
	@Override
	public void abortRecordBuilder(long builderId) {
		try {
			iloader.abortRecordCreation(accessToken, builderId);
		} catch (RemoteException e) {
			handleError(e);
		}
	}
	
	///////////////////////////////////////////////////////
	////////////// Bookmark related //////////////////////
	///////////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getBookmarkRoot(com.strandgenomics.imaging.iclient.Project)
	 */
	@Override
	public BookmarkFolder getBookmarkRoot(Project project) {
		try {
			String bookmarkRoot = ispace.getBookmarkRoot(accessToken, project.getName());
			return CoercionHelper.toClientBookmarkFolder(project.getName(), bookmarkRoot);
		} catch (RemoteException e) {
			handleError(e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getSubfolders(com.strandgenomics.imaging.iclient.BookmarkFolder)
	 */
	@Override
	public BookmarkFolder[] getSubfolders(BookmarkFolder folder) {
		try {
			String[] subfolders = ispace.getBookmarkSubFolders(accessToken, folder.projectName, folder.absolutePath);
			return CoercionHelper.toClientBookmarkFolders(folder.projectName, subfolders);
		} catch (RemoteException e) {
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#getBookmarkedRecords(com.strandgenomics.imaging.iclient.BookmarkFolder)
	 */
	@Override
	public long[] getBookmarkedRecords(BookmarkFolder folder) {
		try {
			return ispace.getBookmarkGuids(accessToken, folder.projectName, folder.absolutePath);
		} catch (RemoteException e) {
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#createBookmarkFolder(com.strandgenomics.imaging.iclient.BookmarkFolder, java.lang.String)
	 */
	@Override
	public void createBookmarkFolder(BookmarkFolder parentFolder, String newFolder) {
		try {
			ispace.createBookmarkFolder(accessToken, parentFolder.projectName, parentFolder.absolutePath, newFolder);
		} catch (RemoteException e) {
			handleError(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#addBookmark(com.strandgenomics.imaging.iclient.BookmarkFolder, long)
	 */
	@Override
	public void addBookmark(BookmarkFolder parentFolder, long guid) {
		try {
			ispace.addBookmark(accessToken, parentFolder.projectName, parentFolder.absolutePath, guid);
		} catch (RemoteException e) {
			handleError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#setAcquisitionProfile(long, com.strandgenomics.imaging.iclient.AcquisitionProfile)
	 */
	@Override
	public void setAcquisitionProfile(long guid, AcquisitionProfile profile) {
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile serverProfile = CoercionHelper.toServerProfile(profile);
			ispace.setAcquisitionProfile(accessToken, guid, serverProfile);
		} catch (RemoteException e) {
			handleError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#listAcquisitionProfiles()
	 */
	@Override
	public List<AcquisitionProfile> listAcquisitionProfiles() {
		try {
			com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile[] serverProfiles = ispace.listAvailableProfiles(accessToken);
			return CoercionHelper.toLocalAcqProfiles(serverProfiles);
		} catch (RemoteException e) {
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpace#getMicroscope()
	 */
	@Override
	public String getMicroscope() {
		InetAddress localhost = null;
		String ipAddress = "NA";
		String macAddress = "NA";
		try {
			localhost = InetAddress.getLocalHost();
			if (!localhost.isLoopbackAddress()) {
				ipAddress = localhost.getHostAddress();
				NetworkInterface nw = NetworkInterface.getByInetAddress(localhost);
				byte[] mac = nw.getHardwareAddress();
				macAddress = Util.toHexString(mac);
			} else {
				String ip = Util.getMachineIP();
				ipAddress = (ip == null) ? "NA" : ip;
				String address = Util.getMachineAddress();
				macAddress = address == null ? "NA" : address;
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			return ispace.getMicroscopeName(accessToken, ipAddress, macAddress);
		} catch (RemoteException e) {
			handleError(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#requestAcquisitionLicense(java.lang.String)
	 */
	@Override
	public boolean requestAcquisitionLicense(String user) {
		System.out.println("Requesting acquisition license for user "+user);
		InetAddress localhost = null;
		String ipAddress = "NA";
		String macAddress = "NA";
		try {
			localhost = InetAddress.getLocalHost();
			if (!localhost.isLoopbackAddress()) {
				ipAddress = localhost.getHostAddress();
				NetworkInterface nw = NetworkInterface.getByInetAddress(localhost);
				byte[] mac = nw.getHardwareAddress();
				macAddress = Util.toHexString(mac);
			} else {
				String ip = Util.getMachineIP();
				ipAddress = (ip == null) ? "NA" : ip;
				String address = Util.getMachineAddress();
				macAddress = address == null ? "NA" : address;
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("Requesting acquisition license for user making ispace call"+user);
			return ispace.requestAcquisitionLicense(accessToken, ipAddress, macAddress);
		} catch (RemoteException e) {
			handleError(e);
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void setChannelContrast(Record record, int channelNo, VisualContrast contrast)
	{
		try
		{
			ispace.setChannelColorAndContrast(accessToken, record.getGUID(), channelNo, CoercionHelper.toRemoteISpaceContrast(contrast), "");
		}
		catch (RemoteException e)
		{
			handleError(e);
		}
	}
	
	@Override
	public void setChannelLUT(Record record, int channelNo, String lut)
	{
		try
		{
			ispace.setChannelColorAndContrast(accessToken, record.getGUID(), channelNo, null, lut);
		}
		catch (RemoteException e)
		{
			handleError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.strandgenomics.imaging.iclient.ImageSpaceSystem#surrenderAcquisitionLicense()
	 */
	@Override
	public void surrenderAcquisitionLicense() {
		try {
			// surrender the license
			ispace.surrenderAcquisitionLicense(accessToken);
		} catch (Exception e) {
			handleError(e);
		}
	}

	@Override
	public Collection<VisualObject> findVisualObjects(long guid,
			VODimension coordinate, String overlayName, Area a) {
			
			try {
				
				VOIndex index = CoercionHelper.toVOCoordinate(coordinate);
				
				Shape shapes[] = ispace.findVisualObjects(accessToken, guid, index, overlayName, a);
				
				if(shapes == null || shapes.length == 0)
					return null;
				
				return CoercionHelper.toLocalVisualObjects(shapes);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	}

	@Override
	public MosaicResource getMosaicResource(MosaicRequest request) {
	
		try {
			return ispace.getMosaicResource(accessToken, request);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BufferedImage getMosaicElementDownloadUrl(MosaicResource resource,
			MosaicParameters params) {
	
			try {
				String url = ispace.getMosaicElementDownloadUrl(accessToken, resource, params);
				URL downloadURL = toServerURL(url);
				return readImage(downloadURL);
			
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}

}
