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
 * LDAPUserAuthenticator.java
 *
 * AVADIS Image Management System
 * Server Implementation
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
package com.strandgenomics.imaging.iengine.auth;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.system.LDAPManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * LDAP based user authentication
 * @author mantri
 * @author Anup Kulkarni
 */
public class LDAPUserAuthenticator implements UserAuthenticator {

    private static final String MEMBER_OF = "member";
    
    protected Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.auth");
    
	public LDAPUserAuthenticator() 
	{ }
	
    /**
     * authenticate user (w.r.t his password) from the information stored with a external
     * LDAP server
     */
    public boolean authenticateUser(String login, String password) throws DataAccessException 
    {
    	logger.logp(Level.INFO, "LDAPUserAuthenticator", "authenticateUser", "authenticating using LDAP user="+login);
    	
    	LDAPManager ldapManager = SysManagerFactory.getLDAPManager();
    	DirContext ctx = ldapManager.getContext();
    	
    	if(ctx == null)
    	{
    		throw new DataAccessException("ldap context is null");
    	}
    	
        List<String> validGroups = ldapManager.getValidPaths();
        
        if (validGroups == null || validGroups.size() == 0 ) 
        {
            throw new DataAccessException("Config: No valid search paths could be found.");
        }
        
        // get the DN for user
		String DN = retrieveDN(ctx, ldapManager.getBaseDN(), login);
		
        for(String groupDN : validGroups)
        {
			// check if user is in same group
			if(!checkMembership(ctx, DN, groupDN))
			{
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.NOT_IN_VALID_LDAP_GROUP));
			}
			
			// authenticate the user
			String ldapURL = ldapManager.getLDAP_URL();
			DirContext dctx = ldapManager.getContext(ldapManager.getInitialContext(), ldapURL, DN, password);
			
			if (dctx != null)
			{
				try
				{
					dctx.close();
				}
				catch (NamingException e)
				{
					logger.logp(Level.INFO, "LDAPUserAuthenticator", "authenticateUser", "error closing the context ", e);
				}
				return true;
			}
        }
        return false;
    }
    
    /**
     * changes user password. not implemented
     * @param login       the login of the user concerned
     * @param oldPassword the existing password for the user (encrypted)
     * @param newPassword the new password for this user (encrypted)
     */
    public boolean changePassword(String login, String oldPassword, String newPassword) throws DataAccessException 
    {
    	logger.logp(Level.INFO, "LDAPUserAuthenticator", "changePassword", "cannot change password of ldap user");
    	
        return false;
    }
    
//    public boolean authenticateUserOld(String login, String password) throws DataAccessException {
//    	
//    	logger.logp(Level.INFO, "LDAPUserAuthenticator", "authenticateUser", "login="+login+" password="+password);
//    	
//        // STEP 1: Get LDAP config object
//        LDAPRequirements config = getLDAPObject();
//        // STEP 2: Get list of paths to search (example : ou=Enterprise, ou=users, ou=system)
//        List<String> validPaths = new ArrayList<String>();
//        validPaths = config.getList();
//        // STEP 3: Get ldap URL
//        String ldapURL = getURL(config.getHostip(), config.getPort());
//
//        if (validPaths == null || validPaths.size() == 0 ) {
//            throw new DataAccessException("Config: No valid search paths could be found.");
//        }
//
//        for(String searchPath : validPaths)
//        {
//            DirContext dctx = null;
//			String DN = getDN(config.getName(), login, searchPath);
//            // Get Directory Context
//            dctx =  SysManagerFactory.getLDAPManager().getContext(config.getInitial_Context_Factory(),ldapURL,DN,password);
//            if(dctx!=null){
//                try{
//                    dctx.close();
//                }
//                catch(NamingException e){
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        }
//        return false;
//    }
    
    
    /**
     * @param id        LDAP attribute which is used to authenticate for login
     * @param login     login of the user
     * @param path      directory wherein the user is
     * @return          the DN of the user
     */
    private String getDN( String id, String login , String path ) 
    {
        StringBuffer buffer  = new StringBuffer();

        buffer.append(id);
        buffer.append("=");
        buffer.append(login);
        buffer.append(",");
        buffer.append(path);

        return buffer.toString();
    }
    
    /**
     * returns DN for username
     * @param ldapContext
     * @param path
     * @param username
     * @return
     */
    private String retrieveDN(DirContext ldapContext, String path, String username)
	{
    	logger.logp(Level.INFO, "LDAPUserAuthentication", "retrieveDN", "retrieving DN for user="+username);
    	
    	String dn = null;
    	try
		{
    		SearchControls searchControls = new SearchControls();
    	    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    	    searchControls.setTimeLimit(5000);

			NamingEnumeration answer = ldapContext.search(path, "(cn="+username+")", searchControls);
			while (answer.hasMore()) 
			{
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				dn = (String) attrs.get("distinguishedName").get();
			}
		}
		catch (NamingException e)
		{
			e.printStackTrace();
		}
		
    	return dn;
	}

    /**
     * check if the user is part of group
     * @param ldapContext
     * @param userDN DN of user
     * @param groupDN DN of group
     */
    private boolean checkMembership(DirContext ldapContext, String userDN, String groupDN)
	{
    	logger.logp(Level.INFO, "LDAPUserAuthentication", "checkMembership", "checking membership of user="+userDN+" in group="+groupDN);
    	
    	DirContext lookedContext;
		try
		{
			lookedContext = (DirContext) ldapContext.lookup(groupDN);
			Attribute attrs = lookedContext.getAttributes("").get(MEMBER_OF);
			for (int i = 0; i < attrs.size(); i++)
			{
				String foundMember = (String) attrs.get(i);
				if(foundMember.equals(userDN))
					return true;
			}
		}
		catch (NamingException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
    
	public static void main(String args[]) {
	}
}
