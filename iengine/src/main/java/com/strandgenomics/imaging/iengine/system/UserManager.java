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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.EncryptionUtil;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.auth.AuthenticatorFactory;
import com.strandgenomics.imaging.iengine.auth.UserAuthenticator;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.LoginHistoryDAO;
import com.strandgenomics.imaging.iengine.dao.UserDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.AuthenticationType;
import com.strandgenomics.imaging.iengine.models.LoginHistoryObject;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserStatus;

/**
 * Manages user related requests
 * @author arunabha
 *
 */
public class UserManager extends SystemManager {
	
	private static final int SUSPENSION_TIME = 2 * 60 * 1000;
	
	/**
	 * should be typically instantiated only once
	 */
	UserManager()
	{ }
	
	/**
	 * inserts the admin user
	 */
	void initialize() 
	{
		try 
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			UserDAO userDao = factory.getUserDAO();
			
			User admin = userDao.findUser(Constants.ADMIN_USER);
			if(admin != null) return;
			
			//default password of administrator
			String password = Util.toHexString(EncryptionUtil.computeMessageDigest(Constants.getStringProperty(Property.DEFAULT_ADMIN_PASSWORD, null)));
			
			userDao.insertUser(Constants.ADMIN_USER, password, AuthenticationType.Internal, Rank.Administrator, "support@strandls.com", "Facility Administrator");
			logger.logp(Level.INFO, "UserManager", "initialize", "successfully initialize iengine...");
		} 
		catch (DataAccessException ex) 
		{
			logger.logp(Level.INFO, "UserManager", "initialize", "unable to initialize iengine...",ex);
		}
	}
	
	/**
	 * updates the user details for specified user login
	 * fields can be null; null fields are not updated
	 * @param actor logged in user
	 * @param login specified login
	 * @param name new name
	 * @param status new status
	 * @param rank new rank
	 * @param email new email
	 * @throws DataAccessException 
	 */
	public void updateUserDetails(String actor, String login, String name, UserStatus status, Rank rank, String email, AuthenticationType type) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canEditUserDetails(actor))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
			
		logger.logp(Level.INFO, "UserManager", "updateUserDetails", "updating details for "+login);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserDAO userDao = factory.getUserDAO();
		
		User u = getUser(login);
		name = name == null ? u.getName() : name;
		email = email == null ? u.getEmail() : email;
		rank = rank == null ? u.getRank() : rank;
		status = status == null ? u.getStatus() : status;
		type = type == null ? u.getAuthenticationType() : type;
		
		// if new status is Active and old status is not Active
		// then validate License
		if(status == UserStatus.Active && u.getStatus()!=status)
		{
//			try
//			{
//				validateMaxActiveUsers();
//			}
//			catch (IOException e)
//			{
//				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.LICENSE_EXCEPTION));
//			}
		}
		
		User updatedUser = new User(login, name, email, u.getPassword(), type, rank, status, u.getLastLogin(), u.getLoginCount());
		userDao.updateUser(updatedUser);
		
		updateUserCache(login, updatedUser);
	}
	
	/**
	 * updates password of specified user login
	 * @param actor logged in user
	 * @param login specified user
	 * @param password new password
	 * @throws DataAccessException 
	 */
	public void updateUserPassword(String actor, String login, String password) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canEditUserDetails(actor))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		User user = SysManagerFactory.getUserManager().getUser(login);
		if(user.getAuthenticationType() == AuthenticationType.External) // cannot modify password of external user
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.CANT_CHANGE_LDAP_ATTR));
		
		if(!isValidPassword(password))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.POOR_PASSWORD_STRENGTH));
			
		logger.logp(Level.INFO, "UserManager", "updateUserPassword", "updating password for "+login);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserDAO userDao = factory.getUserDAO();
		
		password = Util.toHexString(EncryptionUtil.computeMessageDigest(password));
		
		userDao.updateUserPassword(login, password);
	}
	
	private boolean isValidPassword(String password)
	{
		return (password.length() > 7) &&Util.containsDigit(password) && Util.containsLowerCase(password) && Util.containsUpperCase(password) && Util.containsSpecialCharacter(password) && !Util.containsSpace(password);
	}
	
    /**
     * Get list of all users. Un-authorized call. Should not be public.
     * 
     * @return list of users in the system
     */
    private List<User> getAllUsers() 
    {
        return SysManagerFactory.getUserPermissionManager().listUsers();
    }
	
	public User getUser(String login)
	{
		return SysManagerFactory.getUserPermissionManager().getUser(login);
	}
	
	public List<User> getUser(String ... logins)
	{
		List<User> userList = new ArrayList<User>();
		for(String login : logins)
		{
			User u = getUser(login);
			if(u != null) userList.add(u);
		}
		return userList;
	}
	
	public List<User> getUser(List<String> logins)
	{
		List<User> userList = new ArrayList<User>();
		for(String login : logins)
		{
			User u = getUser(login);
			if(u != null) userList.add(u);
		}
		return userList;
	}
	
	/**
	 * Create a new user. Note that Only Administrator are allowed to create new user
	 * @param actorLogin the user making the call
	 * @param userlogin the login ID of the new user
	 * @param userPassword the password of the new user
	 * @param authType the authentication type
	 * @param rank rank of the user 
	 * @param emailID email id of the user
	 * @param fullName Christian name
	 * @exception ImagingEngineException if the user cannot be created
	 */
	public synchronized void createUser(String actorLogin, String userlogin, String userPassword, 
			AuthenticationType authType, Rank rank, String emailID, String fullName)
	{
		User actor = getUser(actorLogin);
		
		if(!actor.getRank().equals(Rank.Administrator))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		if(getUser(userlogin)!=null)
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.USER_ALREADY_EXIT, userlogin));
		
		if(!isValidPassword(userPassword))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.POOR_PASSWORD_STRENGTH));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserDAO userDao = factory.getUserDAO();
		
		try 
		{
			//validate the number of active users
//			validateMaxActiveUsers();
			
			//in the database, passwords are stored as md5 hash of the original text
			userPassword = Util.toHexString(EncryptionUtil.computeMessageDigest(userPassword));
			User newUser = userDao.insertUser(userlogin, userPassword, authType, rank, emailID, fullName);
			
			updateUserCache(userlogin, newUser);
		} 
		catch (DataAccessException ex)
		{
			logger.logp(Level.INFO, "UserManager", "createUser", "unable to create user with login "+userlogin, ex);
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR, userlogin));
		}
		catch (IOException ex)
		{
			logger.logp(Level.INFO, "UserManager", "createUser", "unable to create user with login "+userlogin, ex);
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.LICENSE_EXCEPTION, userlogin));
		}
	}
	
//	private void validateMaxActiveUsers() throws IOException
//	{
//		List<User> activeUsers = DBImageSpaceDAOFactory.getDAOFactory().getUserDAO().listActiveUsers();
//		int nActiveUsers = activeUsers==null? 0 : activeUsers.size();
//		if(nActiveUsers >= SysManagerFactory.getLicenseManager().getLicenseProperties().getMaxActiveUsers())
//			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.MAX_USER_LIMIT_EXCEEDED));
//	}

	private void updateUserCache(String login, User user)
	{
		SysManagerFactory.getUserPermissionManager().updateUser(login, user);
	}
	
	/**
	 * Authenticate the specified user with its password 
	 * @param app the application used for login 
	 * @param login the login of the user 
	 * @param password the password of the user
	 * @return true if successful, false otherwise
	 */
	public boolean authenticate(Application app, String login, String password)
	{
		User validUser = getUser(login);
		if(validUser == null) return false;
		
		if(validUser.getRank() == Rank.Administrator && validUser.getStatus() == UserStatus.Suspended)
		{
			// only administrator will be reactivated automatically
			// for rest of the users administrator will activate them manually
			try
			{
				long lastFailedLogin = (Long) SysManagerFactory.getCacheManager().get(new CacheKey(login, CacheKeyType.LastFailedLogin));
				if(lastFailedLogin + SUSPENSION_TIME <= System.currentTimeMillis())
				{
					updateUserDetails(Constants.ADMIN_USER, login, validUser.getName(), UserStatus.Active, validUser.getRank(), validUser.getEmail(), validUser.getAuthenticationType());
				}
			}
			catch(Exception e)
			{
				logger.logp(Level.INFO, "UserManager", "authenticate", "failed reactivating the "+login, e);
			}
		}
		
		checkUserStatus(validUser);
		
		try
		{
			UserAuthenticator authHandler = getUserAuthenticator(validUser.getAuthenticationType());
			boolean result = authHandler.authenticateUser(login, password);
			
			if(result)
			{
				addLoginHistory(app, login);
				SysManagerFactory.getCacheManager().set(new CacheKey(login, CacheKeyType.FailedLoginCount), 0);
			}
			else
			{
				SysManagerFactory.getCacheManager().set(new CacheKey(login, CacheKeyType.LastFailedLogin), System.currentTimeMillis());
				if(SysManagerFactory.getCacheManager().isCached(new CacheKey(login, CacheKeyType.FailedLoginCount)))
				{
					int count = (Integer) SysManagerFactory.getCacheManager().get(new CacheKey(login, CacheKeyType.FailedLoginCount));
					SysManagerFactory.getCacheManager().set(new CacheKey(login, CacheKeyType.FailedLoginCount), count+1);
					
					if(count == 2)
					{
						// after 3 consecutive failed logins suspend the user
						updateUserDetails(Constants.ADMIN_USER, login, validUser.getName(), UserStatus.Suspended, validUser.getRank(), validUser.getEmail(), validUser.getAuthenticationType());
					}
				}
			}
			
			return result;
		}
		catch(ImagingEngineException ex)
		{
			logger.logp(Level.INFO, "UserManager", "authenticate", "unable to authenticate with login "+login, ex);
			throw ex;
		}
		catch(DataAccessException ex)
		{
			logger.logp(Level.INFO, "UserManager", "authenticate", "unable to authenticate with login "+login, ex);
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR));
		}
	}
	
	/**
	 * inserts login history for user
	 * @param app application used by user for login
	 * @param userLogin logged in user
	 * @throws DataAccessException 
	 */
	public void addLoginHistory(Application app, String login) throws DataAccessException
	{
		logger.logp(Level.INFO, "UserManager", "addLoginHistory", "adding login history"+login);
		
		LoginHistoryDAO loginHistoryDao = DBImageSpaceDAOFactory.getDAOFactory().getLoginHistoryDAO();
		loginHistoryDao.insertLoginHistory(app, login);
		
		logger.logp(Level.INFO, "UserManager", "addLoginHistory", "DONE adding login history"+login);
	}
	
	/**
	 * returns list of LoginHistoryObject for specified filter details
	 * @param app used for login whos login history is required, can be null
	 * @param user whos login history is required, can be null 
	 * @param fromDate start date, can be null
	 * @param toDate end date, can be null
	 * @param limit , can be null
	 * @param offset, can be null
	 * @return requested login history
	 * @throws DataAccessException 
	 */
	public List<LoginHistoryObject> getLoginHistory(Application app, String user, Date fromDate, Date toDate, Long limit, Long offset) throws DataAccessException
	{
		LoginHistoryDAO loginHistoryDao = DBImageSpaceDAOFactory.getDAOFactory().getLoginHistoryDAO();
		return loginHistoryDao.getLoginHistory(app, user, fromDate, toDate, limit, offset);
	}
	
	public long countLoginHistory(Application app, String user, Date fromDate, Date toDate) throws DataAccessException{
		LoginHistoryDAO loginHistoryDao = DBImageSpaceDAOFactory.getDAOFactory().getLoginHistoryDAO();
		return loginHistoryDao.countLoginHistory(app, user, fromDate, toDate);
	}

	/**
	 * Get list of all users present in the system. Does not include users with rank {@link Rank#Administrator}
	 * 
	 * @param loginUser currently logged in user
	 * @return list of all users. If none present <code>null</code>
	 */
	public List<User> listUsers(String loginUser)
    {
	    // Authorize user.
	    User validUser = getUser(loginUser);
	    if (validUser == null) 
	    {
            logger.logp(Level.INFO, "UserManager", "listUsers", "Not a valid user "+loginUser);
            throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.ILLEGAL_LOGIN));
	    }
	    if (!validUser.getRank().equals(Rank.Administrator))
            throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INVALID_CREDENTIALS));

	    List<User> allUsers = getAllUsers();
	    List<User> ret = new ArrayList<User>();
	    for (User user : allUsers) {
            if (!user.getRank().equals(Rank.Administrator)) {
                ret.add(user);
            }
        }
	    return ret;
    }
	
	/**
     * Get list of all logins in the system. This call is accessible to any valid user unlike
     * {@link #listUsers(String)}. However this call just returns login names instead of
     * {@link User} objects 
     * 
     *  @param loginUser
     *            currently logged in user
     * @return list of all users. If none present <code>null</code>
     */
    public List<String> listLogins(String loginUser) 
    {
        User validUser = getUser(loginUser);
        if (validUser == null) {
            logger.logp(Level.INFO, "UserManager", "listMembers", "Not a valid user " + loginUser);
            throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.ILLEGAL_LOGIN));
        }
        List<String> logins = new ArrayList<String>();
        List<User> users = getAllUsers();
        for (User user : users) {
            if (!(user.getRank().equals(Rank.Administrator)))
                logins.add(user.login);
        }
        return logins;
    }
    
	private void checkUserStatus(User user)
	{
		switch(user.getStatus())
		{
			case Active:
				break;
			case Suspended:
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.LOGIN_DISABLED));
			case Deleted:
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.LOGIN_DELETED));
		}
	}
	
	private UserAuthenticator getUserAuthenticator(AuthenticationType authType)
	{
		UserAuthenticator authHandler = null;
		
		switch(authType)
		{
		case External:
			authHandler = AuthenticatorFactory.getExternalAuthenticationHandler();
			break;
		case Internal:
			authHandler = AuthenticatorFactory.getLocalAuthenticationHandler();
			break;
		}
		
		return authHandler;
	}
	
	/**
	 * get all users for a specified rank
	 * @param rank
	 * @return
	 * @throws DataAccessException
	 */
	public List<User> getUsersForRank(Rank rank) throws DataAccessException{
		
		return ImageSpaceDAOFactory.getDAOFactory().getUserDAO().getUsersForRank(rank);
	}
}
