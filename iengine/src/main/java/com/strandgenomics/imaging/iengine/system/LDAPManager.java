package com.strandgenomics.imaging.iengine.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.iengine.models.AuthenticationType;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserStatus;

/**
 * Manages LDAP related functionalities
 * 
 * @author Anup Kulkarni
 */
public class LDAPManager  extends SystemManager {
	private static final String MEMBER_OF = "member";
	private static final String CN = "cn";
	private static final String MAIL = "mail";
	/**
	 * initial context
	 */
	private String initial_context = "com.sun.jndi.ldap.LdapCtxFactory";
	
	LDAPManager() 
	{ }
	
	/**
	 * returns all the users of LDAP
	 * @return
	 */
	public List<User> listLDAPUsers()
	{
		return getMembers(Constants.getLDAPGroupDN());
	}
	
	/**
	 * list all the users for given group
	 * @param groupDN
	 * @return all the users for given group
	 */
	public List<User> getMembers(String groupDN)
    {
    	List<User> members = new ArrayList<User>();
    	DirContext lookedContext;
		try
		{
			DirContext ldapContext = getContext();
			
			lookedContext = (DirContext) ldapContext.lookup(groupDN);
			
			Attribute attrs = lookedContext.getAttributes("").get(MEMBER_OF);
			for (int i = 0; i < attrs.size(); i++)
			{
				// get user DN
				String foundMember = (String) attrs.get(i);
				
				DirContext memberContext = (DirContext) ldapContext.lookup(foundMember);
				String userCN = (String) memberContext.getAttributes("").get(CN).get();
				String userMail = (String) memberContext.getAttributes("").get(MAIL).get();
				
				User u = new User(userCN, userCN, userMail, "", AuthenticationType.External, Rank.TeamMember, UserStatus.Active, new Timestamp(System.currentTimeMillis()), 0);
				members.add(u);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return members;
    }
	
	/**
	 * get the details of the user
	 * @param dn of the user
	 */
    public void searchUser(String dn)
    {
		try
		{
			NamingEnumeration answer = getContext().search(dn , null);
			while (answer.hasMore()) 
			{
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
			}
		}
		catch (NamingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/**
	 * @throws NamingException 
     */
    public DirContext getContext()
    {
        return getContext(initial_context, Constants.getLDAP_URL(), Constants.getLDAP_CN(), Constants.getLDAP_CN_Password());
    }
    
    /**
     * returns the initial context
     * @return
     */
    public String getInitialContext()
    {
    	return initial_context;
    }
    
    /**
     * @param initial_context_factory   the initial context factory of the LDAP server
     * @param ldapURL                   address where the LDAP server is running
     * @param DN                        distinguished name of the user to serve as the security principal
     * @param password                  password of the user specified by the above DN
     * @return                          the directory context of the user
     */
    public DirContext getContext(String initial_context_factory, String ldapURL, String DN, String password){

        Hashtable<String, String> env = new Hashtable<String, String>();
        DirContext dcontext = null;

        env.put(Context.INITIAL_CONTEXT_FACTORY,initial_context_factory);
        env.put(Context.PROVIDER_URL,ldapURL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL,DN);
        env.put(Context.SECURITY_CREDENTIALS, password );

        try{
            dcontext =  new InitialDirContext(env);
            return dcontext;
        }
        catch(NamingException ne){
            ne.printStackTrace();
        }
        return null;
    }

    /**
     * returns the ldap url
     * @return
     */
	public String getLDAP_URL()
	{
		return Constants.getLDAP_URL();
	}

	/**
	 * list of searchable paths
	 * @return
	 */
	public List<String> getValidPaths()
	{
		List<String> paths = new ArrayList<String>();
		paths.add(Constants.getLDAPGroupDN());
		
		return paths;
	}

	/**
	 * returns the base DN for ldap
	 * @return
	 */
	public String getBaseDN()
	{
		return Constants.getLDAPBaseDN();
	}
}
