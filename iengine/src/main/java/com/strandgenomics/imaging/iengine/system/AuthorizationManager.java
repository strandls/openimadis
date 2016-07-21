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

package com.strandgenomics.imaging.iengine.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.system.ErrorCode.Authorization;
import com.strandgenomics.imaging.icore.system.ErrorCode.ImageSpace;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.auth.AuthorizationException;
import com.strandgenomics.imaging.iengine.auth.IAccessToken;
import com.strandgenomics.imaging.iengine.auth.IPFilter;
import com.strandgenomics.imaging.iengine.dao.AuthCodeDAO;
import com.strandgenomics.imaging.iengine.dao.ClientDAO;
import com.strandgenomics.imaging.iengine.dao.ClientTagsDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.AuthCode;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.User;

/**
 * Authorization related api
 * 
 * @author santhosh
 */
public class AuthorizationManager extends SystemManager {

    /**
     * Client ID for acquisition client
     */
    public static final String ACQ_CLIENT_ID = "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI";

    /**
     * How frequently should the cache be cleaned
     */
    private static final int CACHE_CLEAN_FREQUENCY = 24 * 60 * 60;

    /**
     * Length of keys to generate
     */
    private static final int KEY_LENGTH = 40;

    /**
     * service to clean up the cache and db periodically.
     */
    private ScheduledThreadPoolExecutor cleanupService = null;

    /**
     * Lock for token cache
     */
    private ReadWriteLock tokenLock = new ReentrantReadWriteLock();

    /**
     * Lock for client cache.
     */
    private ReadWriteLock clientLock = new ReentrantReadWriteLock();

    AuthorizationManager() {
        
        initClient();
        cleanupService = new ScheduledThreadPoolExecutor(1);
        cleanupService.scheduleWithFixedDelay(new CacheCleaner(), CACHE_CLEAN_FREQUENCY, CACHE_CLEAN_FREQUENCY,
                TimeUnit.SECONDS);
    }
    
    private void initClient()
    {
		String clientVersion = Constants.getWebApplicationVersion();
		try
		{
			ImageSpaceDAOFactory.getDAOFactory().getClientDAO().updateClientVersion(ACQ_CLIENT_ID, clientVersion);
		}
		catch (DataAccessException e)
		{}
    }

    /**
     * Acquistion client is a special client which the server knows about
     * already. Registering it ahead.
     * 
     * @throws DataAccessException
     * 
     */
    void registerAcqClient() {
        try {
            Client client = getClient(ACQ_CLIENT_ID);
            if (client != null)
                return;
            addClient(ACQ_CLIENT_ID, "Acquisition Client", "1.12", "Acquisition Client for iManage", Constants.ADMIN_USER,
                    true, null);
        } catch (DataAccessException e) {
            logger.logp(Level.WARNING, "AuthorizationManager", "registerAcqClient", "errror adding acq client id: ", e);
        }
    }

    /**
     * Does the token provided have access to the service specified
     * 
     * @param accessToken
     *            token whose access needs to be checked
     * @param service
     *            service which the token needs to access
     * @return <code>true</code> if access is allowed. <code>false</code>
     *         otherwise.
     * @throws AuthorizationException
     *             if any other error happens during the check. For example, if
     *             the token is valid but expired.
     */
    public boolean canAccess(String accessToken, Service service) {
        IAccessToken tokenInstance = getValidToken(accessToken);
        boolean retValue = tokenInstance.canAccess(service);
        
        try
		{
			ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO().updateAccessTime(accessToken, tokenInstance.getLastAccessTime());
		}
		catch (DataAccessException e)
		{}
        
        return retValue;
    }

    /**
     * Get the login of the user associated with the token
     * 
     * @param accessToken
     *            token whose user is required
     * @return login of the user if token is valid. <code>null</code> otherwise.
     */
    public String getUser(String accessToken) {
        IAccessToken tokenInstance = getValidToken(accessToken);
        return tokenInstance.getUser();
    }
    
    /**
     * Get the client id of the client associated with the token
     * 
     * @param accessToken token whose client id is required
     * @return client id if token is valid. <code>null</code> otherwise.
     */
    public String getClientID(String accessToken) 
    {
        IAccessToken tokenInstance = getValidToken(accessToken);
        return tokenInstance.getClientID();
    }

    /**
     * Returns the number of milliseconds left for the specified access to
     * expire
     * 
     * @param accessToken
     *            the access token under consideration
     * @return milliseconds left for the specified access to expire
     */
    public long getExpiryTime(String accessToken) {
        IAccessToken tokenInstance = getValidToken(accessToken);
        Date expiryTime = tokenInstance.getExpiryTime();
        return (expiryTime.getTime() - System.currentTimeMillis());
    }

    /**
     * Get access token instance with the given id
     * 
     * @param accessTokenID
     *            id whose access token is required
     * @return access token instance if id is valid. throws exception if not.
     */
	private IAccessToken getValidToken(String accessTokenID)
	{
		tokenLock.readLock().lock();
		try
		{
			AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
			IAccessToken token = authCodeDAO.getAuthCode(accessTokenID);
			if (token != null)
			{
				validateToken(token);
				return token;
			}
			throw new AuthorizationException(new ErrorCode(Authorization.INVALID_ACCESS_TOKEN));
		}
		catch (DataAccessException e)
		{
			throw new AuthorizationException(new ErrorCode(Authorization.INVALID_ACCESS_TOKEN));
		}
		finally
		{
			tokenLock.readLock().unlock();
		}

	}

    /**
     * Validate the access token provided
     * 
     * @param token
     */
	private void validateToken(IAccessToken token)
	{
		if (token == null || !token.isValid())
			throw new AuthorizationException(new ErrorCode(Authorization.INVALID_ACCESS_TOKEN));
		if (token.hasExpired())
			throw new AuthorizationException(new ErrorCode(Authorization.ACCESS_TOKEN_EXPIRED));
	}

    /**
     * Validate the auth code provided
     * 
     * @param authCode
     * @param clientID
     */
    private void validateAuthCode(AuthCode authCode, String clientID) {
        Client client = getClient(clientID);
        if (client == null)
            throw new AuthorizationException(new ErrorCode(Authorization.INVALID_CLIENT));
        if (authCode == null || !authCode.isValid() || authCode.isDelivered())
            throw new AuthorizationException(new ErrorCode(Authorization.INVALID_AUTH_CODE));
        if (authCode.hasExpired())
            throw new AuthorizationException(new ErrorCode(Authorization.AUTH_CODE_EXPIRED));
        if (!authCode.getClientID().equals(clientID))
            throw new AuthorizationException(new ErrorCode(Authorization.INVALID_KEYPAIR));
    }

    /**
     * Get the access token for the authcode and clientid provided
     * 
     * @param authCode
     * @param clientID
     * @return access token if the authcode and clientid is a valid combination.
     *         <code>null</code> otherwise.
     * @throws DataAccessException
     */
	public String getAccessToken(String authCode, String clientID) throws DataAccessException
	{
		AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
		AuthCode authCodeInstance = authCodeDAO.getAuthCode(authCode);
		validateAuthCode(authCodeInstance, clientID);
		tokenLock.writeLock().lock();
		try
		{
			String accessToken = Util.generateRandomString(KEY_LENGTH);
			IAccessToken accessTokenInstance = authCodeDAO.exchangeAuthCode(authCode, accessToken);
			return accessTokenInstance.getId();
		}
		finally
		{
			tokenLock.writeLock().unlock();
		}
	}

    /**
     * Remove authorization code / access token with the given id. Note that
     * this is a common api to remove both access token and to remove auth code.
     * 
     * @param loginUser
     *            login user
     * 
     * @param authID
     *            internal id of token to remove
     * @throws DataAccessException
     */
    public void removeToken(String loginUser, long authID) throws DataAccessException {
        logger.logp(Level.WARNING, "AuthorizationManager", "removeToken", "removing token");

        AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
        IAccessToken token = authCodeDAO.getAuthCode(authID);
        if (token == null)
            throw new AuthorizationException(new ErrorCode(Authorization.INVALID_ACCESS_TOKEN));
        UserManager userManager = SysManagerFactory.getUserManager();
        User userInstance = userManager.getUser(loginUser);
        // Only admin or self can remove a token.
        tokenLock.writeLock().lock();
        try {
            if (userInstance.getRank().equals(Rank.Administrator) || token.getUser().equals(loginUser)) {
                authCodeDAO.removeAuthCode(authID);
               
                Set<User> receivers = new HashSet<User>();
                receivers.add(userManager.getUser(token.getUser()));

				SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers, null, NotificationMessageType.AUTHCODE_DELETED,
						String.valueOf(authID));
            } else {
                throw new AuthorizationException(new ErrorCode(ImageSpace.INVALID_CREDENTIALS));
            }
        } finally {
            tokenLock.writeLock().unlock();
        }
    }
    
    /**
     * returns authcode/accesstoken for given internal authcode id
     * @param accessTokenID internal authcode id
     * @return access-token/authcode associated with the given id
     * @throws DataAccessException 
     */
    public String getAccessToken(long accessTokenID) throws DataAccessException
	{
    	AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
    	AuthCode authCode = authCodeDAO.getAuthCode(accessTokenID);
    	return authCode.getId();
	}

    /**
     * Update details of an auth code/access token. If the token is an access
     * token, the cache is also updated so that the changes taken effect
     * immediately. All the options are optional (can be null).
     * 
     * @param loginUser
     *            login user
     * 
     * @param authID
     *            internal id of the token that needs to be updated
     * @param services
     *            new set of services. can be <code>null</code>
     * @param newExpiry
     *            new expiry time. can be <code>null</code>
     * @param newFilters
     *            new set of ip filters. can be <code>null</code>
     * @throws DataAccessException
     */
    public void updateToken(String loginUser, long authID, Service[] services, Date newExpiry, List<IPFilter> newFilters)
            throws DataAccessException {
        AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
        IAccessToken token = authCodeDAO.getAuthCode(authID);
        if (token == null)
            throw new AuthorizationException(new ErrorCode(Authorization.INVALID_ACCESS_TOKEN));

        User user = SysManagerFactory.getUserManager().getUser(loginUser);
        // Only administrator or self can update a token
        if (!user.getRank().equals(Rank.Administrator) && !token.getUser().equals(loginUser))
            throw new AuthorizationException(new ErrorCode(ImageSpace.INVALID_CREDENTIALS));
        // If services null, just use the old set of services

        List<Service> originalServices = new ArrayList<Service>();
        if (services == null) {
            for (Service service : Service.values())
                if (token.canAccess(service))
                    originalServices.add(service);
            services = originalServices.toArray(new Service[0]);
        }
        // If newExpiry is null, copy from the old acccess token
        if (newExpiry == null)
            newExpiry = token.getExpiryTime();
        if (newFilters == null)
            newFilters = token.getFilters();
        
        Set<User> receivers = new HashSet<User>();
        receivers.add(SysManagerFactory.getUserManager().getUser(token.getUser()));
        
        String[] args = new String[3];
        args[0] = token.getId();
        for(int j = 0 ; j < originalServices.size() ; j++){
        	if (j < originalServices.size() -2)
        			args[1] += services[j].toString() + " , " ;
        	else if (j == originalServices.size() -2)
    				args[1] += services[j].toString() + " and " ;
        	else if (j == originalServices.size() - 1)
        			args[1] += services[j].toString() ;
        	logger.logp(Level.INFO, "Authorization manager", "update token",
                    "service added: " + services[j].toString() );
            
        }
        
        args[2] = newExpiry.toString() ;
        SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers , null, NotificationMessageType.AUTHCODE_MODIFIED, args);
        
        tokenLock.writeLock().lock();
		try
		{
			authCodeDAO.updateToken(authID, getServiceFlag(services), newExpiry, newFilters);
		}
		finally
		{
			tokenLock.writeLock().unlock();
		}

    }

    /**
     * Register a new client with the server.
     * 
     * @param name
     *            name of the client
     * @param version
     *            version of the client
     * @param description
     *            description
     * @param added_by
     *            user who added the client
     * @param isWorkflow
     *            can the server run this client. The client is treated as a
     *            workflow if this is true.
     * @return id of the client added.
     * @throws DataAccessException
     */
	public String registerClient(String name, String version, String description, String added_by, boolean isWorkflow, String url) throws DataAccessException
	{
		Client client = getClient(name, version);
		if (client != null)
			throw new AuthorizationException(new ErrorCode(Authorization.CLIENT_EXISTS, name, version));
		String clientID = Util.generateRandomString(KEY_LENGTH);
		Client newClient = addClient(clientID, name, version, description, added_by, isWorkflow, url);
		return newClient.getClientID();
	}

	private Client addClient(String clientID, String name, String version, String description, String added_by, boolean isWorkflow, String url) throws DataAccessException
	{
		clientLock.writeLock().lock();
		try
		{
			ClientDAO clientDAO = ImageSpaceDAOFactory.getDAOFactory().getClientDAO();
			Client newClient = clientDAO.addClient(clientID, name, version, description, added_by, isWorkflow, url);
			return newClient;
		}
		finally
		{
			clientLock.writeLock().unlock();
		}
	}
    
    /**
     * Add tags for a client
     * @param clientID
     * @param tags
     * @throws DataAccessException 
     */
    public void addClientTags(String clientID, String[] tags) throws DataAccessException{
    	
    	ClientTagsDAO clientTagsDAO=ImageSpaceDAOFactory.getDAOFactory().getClientTagsDAO();
    	
    	for(String tag : tags){    		
    		if(!tag.equals(""))
    			clientTagsDAO.addTagForClient(clientID, tag);
    	}
    }

    /**
     * Remove client with its id
     * 
     * @param loginUser
     *            logged in user
     * 
     * @param clientID
     *            id of client to remove
     * @throws DataAccessException
     */
	public void removeClient(String loginUser, String clientID) throws DataAccessException
	{
		User user = SysManagerFactory.getUserManager().getUser(loginUser);
		ClientDAO clientDAO = ImageSpaceDAOFactory.getDAOFactory().getClientDAO();
		Client client = clientDAO.getClient(clientID);
		if (client == null)
			throw new AuthorizationException(new ErrorCode(Authorization.INVALID_CLIENT));
		
		// only admin or self can remove a client.
		if (!user.getRank().equals(Rank.Administrator) && !client.getUser().equals(user))
			throw new AuthorizationException(new ErrorCode(ImageSpace.INVALID_CREDENTIALS));
					
		clientLock.writeLock().lock();
		try
		{
			clientDAO.removeClient(clientID);
		}
		finally
		{
			clientLock.writeLock().unlock();
		}
	}

    /**
     * Get the client with the given name and version
     * 
     * @param name
     *            name of client required
     * @param version
     *            version of client required
     * @return client instance with the given name and version
     * @throws DataAccessException
     */
    public Client getClient(String name, String version) throws DataAccessException {
        ClientDAO clientDAO = ImageSpaceDAOFactory.getDAOFactory().getClientDAO();
        return clientDAO.getClient(name, version);
    }

    /**
     * Get clients registered for user.
     * 
     * @param user
     *            user whose clients are required
     * @return list of clients registered by user. empty list if no client was
     *         registered.
     * @throws DataAccessException
     */
    public List<Client> getClients(String loginUser) throws DataAccessException 
    {
    	logger.logp(Level.INFO, "AuthorizationManager", "getClients", "returning clients for ="+loginUser);
    	
        
        ClientDAO clientDAO = ImageSpaceDAOFactory.getDAOFactory().getClientDAO();
        List<Client> allClients = clientDAO.getAllClients();
        
        User user = SysManagerFactory.getUserManager().getUser(loginUser);
        logger.logp(Level.FINEST, "AuthorizationManager", "getClients", "returning clients for user rank="+user.getRank());
        
        // administrator can view all the clients
        if(user.getRank().ordinal() <= Rank.FacilityManager.ordinal())
        	return allClients;

        List<Client> clients = new ArrayList<Client>();
        for (Client client : allClients) {
            if (client.getUser().equals(user.login))
                clients.add(client);
        }
        return clients;
    }

    /**
     * Get all the clients in the system
     * 
     * @return list of clients in the system.
     * @throws DataAccessException
     */
    public List<Client> getAllClients() throws DataAccessException {
        ClientDAO clientDAO = ImageSpaceDAOFactory.getDAOFactory().getClientDAO();
        return clientDAO.getAllClients();
    }
    
    /**
     * Get the client with the given client id.
     * 
     * @param clientID
     *            id of the client
     * @return client instance with the given id
     * @throws DataAccessException
     */
	public Client getClient(String clientID)
	{
		try
		{
			return ImageSpaceDAOFactory.getDAOFactory().getClientDAO().getClient(clientID);
		}
		catch (DataAccessException e)
		{
			return null;
		}
	}

    /**
     * Generate a new authcode with the specified constraints.
     * 
     * @param user
     *            user requesting the authcode
     * @param clientID
     *            client which needs to access
     * @param services
     *            services for which the authcode is valid
     * @param expiryTime
     *            unix timestamp when the authcode expires
     * @param filters
     *            filters to apply during access
     * @return generated auth code
     * @throws DataAccessException
     */
    public String generateAuthCode(String user, String clientID, Service[] services, long expiryTime,
            List<IPFilter> filters) throws DataAccessException {
        AuthCode authCode = getAuthCode(user, clientID, services, expiryTime, filters);
        return authCode.getId();
    }
    
    /**
     * Generate a new authcode with the specified constraints.
     * 
     * @param user
     *            user requesting the authcode
     * @param clientID
     *            client which needs to access
     * @param services
     *            services for which the authcode is valid
     * @param expiryTime
     *            unix timestamp when the authcode expires
     * @param filters
     *            filters to apply during access
     * @return generated auth code
     * @throws DataAccessException
     */
    public AuthCode getAuthCode(String user, String clientID, Service[] services, long expiryTime, List<IPFilter> filters) throws DataAccessException 
	{
    	Client client = getClient(clientID);
        if (client == null)
            throw new AuthorizationException(new ErrorCode(ErrorCode.Authorization.INVALID_CLIENT));
        String authCode = Util.generateRandomString(KEY_LENGTH);
        AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
        Date expiryDate = new Date(expiryTime);
        int serviceFlag = getServiceFlag(services);
        AuthCode addAuthCode = authCodeDAO.addAuthCode(authCode, user, clientID, serviceFlag, expiryDate, filters);
        return addAuthCode;
	}
    
    /**
     * List all the tokens in the system for a particular user. Tokens include
     * auth code and access tokens. If the user requesting has rank
     * {@link Rank#Administrator}, tokens of all users will be returned. Else
     * only the user's tokens will be returned.
     * 
     * @param loginUser
     *            logged in user
     * @param forUser
     *            get tokens for the user
     * 
     * @return list of tokens generated by the corresponding user.
     * @throws DataAccessException
     */
    public List<IAccessToken> listAllTokens(String loginUser, String forUser) throws DataAccessException {
        UserManager userManager = SysManagerFactory.getUserManager();
        User userInstance = userManager.getUser(loginUser);
        if (!userInstance.getRank().equals(Rank.Administrator) && !loginUser.equals(forUser)) {
            throw new AuthorizationException(new ErrorCode(ImageSpace.INVALID_CREDENTIALS));
        }
        AuthCodeDAO dao = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
        List<AuthCode> userTokens = dao.listUserTokens(forUser);
        List<IAccessToken> tokens = new ArrayList<IAccessToken>();
        tokenLock.readLock().lock();
        try {
            if (userTokens != null) {
                for (AuthCode authCode : userTokens) {
                    // Prefer the cache instance of a token as it carries the
                    // latest
                    // information about the token (access time).
                    tokens.add(authCode);
                }
            }
        } finally {
            tokenLock.readLock().unlock();
        }
        return tokens;
    }
    
    /***
     * 
     * @param authCodeId
     * @param loginUser
     * @throws DataAccessException12:16:59 PM
     */
    public void disableAuthCode(String authCodeId , String loginUser) throws DataAccessException{

        AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
        IAccessToken token = authCodeDAO.getAuthCode(authCodeId);
    	UserManager userManager = SysManagerFactory.getUserManager();
        User userInstance = userManager.getUser(loginUser);
        if (token == null)
            throw new AuthorizationException(new ErrorCode(Authorization.INVALID_ACCESS_TOKEN));
        
        if (!userInstance.getRank().equals(Rank.Administrator)) {
            throw new AuthorizationException(new ErrorCode(ImageSpace.INVALID_CREDENTIALS));
        }
        
		tokenLock.writeLock().lock();
		try
		{
			if (userInstance.getRank().equals(Rank.Administrator) || token.getUser().equals(loginUser))
			{
				if(ACQ_CLIENT_ID.equals(getClientID(authCodeId)))
				{
					// surrender acq license
					SysManagerFactory.getMicroscopeManager().surrenderAcquisitionLicense(authCodeId);
				}
				
				authCodeDAO.disableAuthCode(authCodeId);
			}
			else
			{
				throw new AuthorizationException(new ErrorCode(ImageSpace.INVALID_CREDENTIALS));
			}

		}
		finally
		{
			tokenLock.writeLock().unlock();
		}

    	
    }
/**
 * 
 * @param loginUser
 * @param forUser
 * @return
 * @throws DataAccessException2:52:35 PM
 */
    public List<IAccessToken> listTokens(String loginUser) throws DataAccessException {
        UserManager userManager = SysManagerFactory.getUserManager();
        User userInstance = userManager.getUser(loginUser);
        if (!userInstance.getRank().equals(Rank.Administrator)) {
            throw new AuthorizationException(new ErrorCode(ImageSpace.INVALID_CREDENTIALS));
        }
        AuthCodeDAO dao = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
        List<AuthCode> allTokens = dao.listTokens();
        List<IAccessToken> tokens = new ArrayList<IAccessToken>();
        tokenLock.readLock().lock();
        try {
            if (allTokens != null) {
                for (AuthCode authCode : allTokens) {
                    // Prefer the cache instance of a token as it carries the
                    // latest
                    // information about the token (access time).
                    tokens.add(authCode);
                }
            }
        } finally {
            tokenLock.readLock().unlock();
        }
        return tokens;
    }

    /**
     * Surrender the access token. It cannot be used after surrendering.
     * 
     * @param clientID
     *            client which is surrendering the auth id
     * @param accessTokenID
     *            id of access token which has to be surrendered.
     * @throws DataAccessException
     */
	public void surrender(String clientID, String accessTokenID) throws DataAccessException
	{
		IAccessToken token = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO().getAuthCode(accessTokenID);
		if (token == null)
			throw new AuthorizationException(new ErrorCode(ErrorCode.Authorization.INVALID_ACCESS_TOKEN));
		tokenLock.writeLock().lock();
		try
		{
			Client client = getClient(clientID);
			if (client == null || !client.getClientID().equals(token.getClientID()))
				throw new AuthorizationException(new ErrorCode(ErrorCode.Authorization.INVALID_CLIENT));

			// removing auth code only from cache
			// but maintaining it in DB;
			// so that archived task can use it
		}
		finally
		{
			tokenLock.writeLock().unlock();
		}

	}

    /**
     * Generate an integer representing the services provided.
     * 
     * @param services
     *            services to encode
     * @return int representation
     */
    private int getServiceFlag(Service[] services) {
        int serviceFlag = 0;
        if (services != null) {
            for (Service service : services) {
                serviceFlag = serviceFlag | (1 << service.ordinal());
            }
        }
        return serviceFlag;
    }

    private class CacheCleaner extends Thread {
        @Override
        public void run() {
            tokenLock.writeLock().lock();
            try {
                cleanDB();
            } finally {
                tokenLock.writeLock().unlock();
            }
        }

        /**
         * Clean old tokens
         */
        private void cleanDB() {
            try {
                AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
                authCodeDAO.clean();
            } catch (DataAccessException e) {
                logger.logp(Level.INFO, "AuthorizationManager", "cacheCleaner:cleanDB", "failed", e);
            }
        }

    }

    /**
     * main method to run tests
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String... args) throws Exception {
    }
}
