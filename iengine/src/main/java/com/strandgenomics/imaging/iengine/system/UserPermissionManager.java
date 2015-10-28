package com.strandgenomics.imaging.iengine.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.dao.UserDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectAccess;
import com.strandgenomics.imaging.iengine.models.ProjectAccessKey;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserAction;
import com.strandgenomics.imaging.iengine.models.UserMembership;

/**
 * This class manager the permission and roles of the user
 * 
 * @author Anup Kulkarni
 */
public class UserPermissionManager extends SystemManager {

	/***
	 * a map of all the times any project is accessed by any user
	 */
	public Map<ProjectAccessKey, ProjectAccess> projectAccessMap;

	UserPermissionManager()
	{
		projectAccessMap = new HashMap<ProjectAccessKey, ProjectAccess>();

		initialize();
	}

	/**
	 * initializes the maps from the db
	 */
	private void initialize()
	{
		populateUsers();
		populateProjects();
		populateUserPermissionMap();
		populateProjectAccessMap();
	}

	private void populateProjects()
	{
		ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
		try
		{
			List<Project> existingProjects = projectDAO.findProjectByStatus(null);
			if (existingProjects != null)
			{
				for (Project project : existingProjects)
				{
					SysManagerFactory.getCacheManager().set(new CacheKey(project.getID(), CacheKeyType.ProjectID), project);
					SysManagerFactory.getCacheManager().set(new CacheKey(project.getID(), CacheKeyType.ProjectName), project);
				}
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
	}

	private void populateProjectAccessMap()
	{
		// todo put how to get back the saved info from the previous session
	}

	private void populateUserPermissionMap()
	{
		ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
		try
		{
			List<UserMembership> memberships = projectDAO.getAllUserMemberships();
			if (memberships != null)
			{
				for (UserMembership membership : memberships)
				{
					String projectName = getProjectFromCache(membership.getProjectId()).getName();
					String userLogin = membership.getUserLogin();

					updateUserPermissionCache(projectName, userLogin, membership.getPermission());
				}
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
	}

	private void populateUsers()
	{
		try
		{
			UserDAO userDAO = ImageSpaceDAOFactory.getDAOFactory().getUserDAO();

			List<User> users = userDAO.findUser();
			if (users != null)
			{
				for (User u : users)
				{
					CacheKey key = new CacheKey(u.login, CacheKeyType.Users);
					SysManagerFactory.getCacheManager().set(key, u);
				}
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
	}

	public boolean isAdministrator(String userLogin)
	{
		User user = getUser(userLogin);
		return (user.getRank() == Rank.Administrator);
	}

	public boolean isFacilityManager(String actorLogin)
	{
		User user = getUser(actorLogin);
		return (user.getRank().ordinal() <= Rank.FacilityManager.ordinal());
	}

	/**
	 * list of all project names managed by this user
	 * 
	 * @param user
	 *            specified user
	 * @return
	 */
	public List<Project> getManagedProjects(String user)
	{
		populateProjects();

		if (getUser(user) == null)
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.USER_DO_NOT_EXIT, user));

		if (getUser(user).getRank() == Rank.Administrator)
		{
			try
			{
				List<Project> activeProjects = DBImageSpaceDAOFactory.getDAOFactory().getProjectDAO().findAllProjects();
				if (activeProjects != null)
					return new ArrayList<Project>(activeProjects);
			}
			catch (DataAccessException e)
			{
				logger.logp(Level.WARNING, "UserPermissionManager", "getManagerProjects", "error getting project list from DB ", e);
			}

			return new ArrayList<Project>();
		}

		if (getUser(user).getRank() == Rank.FacilityManager)
		{
			try
			{
				List<Project> activeProjects = DBImageSpaceDAOFactory.getDAOFactory().getProjectDAO().findAllProjects();
				if (activeProjects != null)
					return new ArrayList<Project>(activeProjects);
			}
			catch (DataAccessException e)
			{
				logger.logp(Level.WARNING, "UserPermissionManager", "getManagerProjects", "error getting project list from DB ", e);
			}

			return new ArrayList<Project>();
		}

		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(user, CacheKeyType.UserPermission)))
		{
			Map<String, Permission> projectPermission = (Map<String, Permission>) SysManagerFactory.getCacheManager().get(new CacheKey(user, CacheKeyType.UserPermission));
			if (projectPermission != null)
			{
				List<Project> projects = new ArrayList<Project>();
				for (String key : projectPermission.keySet())
				{
					if (projectPermission.get(key).ordinal() <= Permission.Manager.ordinal())
					{
						Project prj = getProjectFromCache(key);
						if (!projects.contains(prj))
							projects.add(prj);
					}
				}
				return projects;
			}
		}

		return null;
	}

	/**
	 * returns the UserPermission of specified user on specified project
	 * 
	 * @param user
	 *            specified user
	 * @param projectName
	 *            specified project
	 * @return UserPermission
	 */
	public Permission getUserPermission(String user, String projectName)
	{
		logger.logp(Level.FINE, "UserPermissionManager", "getUserPermission", "getting permission for user " + user + " on project " + projectName);
		if (getUser(user) == null)
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.USER_DO_NOT_EXIT, user));

		if (getUser(user).getRank() == Rank.Administrator)
			return Permission.Administrator;

		if (getUser(user).getRank() == Rank.FacilityManager)
			return Permission.FacilityManager;

		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(user, CacheKeyType.UserPermission)))
		{
			Map<String, Permission> projectPermissionMap = (Map<String, Permission>) SysManagerFactory.getCacheManager().get(new CacheKey(user, CacheKeyType.UserPermission));
			if (projectPermissionMap.containsKey(projectName))
			{
				logger.logp(Level.INFO, "UserPermissionManager", "getUserPermission",
						"getting permission for user " + user + " on project " + projectName + " got permission " + projectPermissionMap.get(projectName));
				return projectPermissionMap.get(projectName);
			}
		}

		return checkInDb(user, projectName);
	}

	/**
	 * list project for specific user and specific permission
	 * 
	 * @param user
	 *            specified user
	 * @param permission
	 *            specified permission
	 * @return
	 */
	public List<Project> listProjects(String user, Permission permission)
	{
		List<Project> prjList = new ArrayList<Project>();

		try
		{
			List<Project> activeProjects = DBImageSpaceDAOFactory.getDAOFactory().getProjectDAO().findAllProjects();
			if (activeProjects != null)
			{
				for (Project p : activeProjects)
				{
					Permission perm = getUserPermission(user, p.getName());
					if (perm.ordinal() <= permission.ordinal())
					{
						prjList.add(p);
					}
				}
			}
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "UserPermissionManager", "getUserPermission", "error getting project list from DB ", e);
			return new ArrayList<Project>();
		}

		logger.logp(Level.INFO, "UserPermissionManager", "getUserPermission", "getting allowed projects for " + user + " " + permission + " " + prjList.size());
		return prjList;
	}

	private Permission checkInDb(String user, String projectName)
	{
		Permission permission = Permission.None;

		try
		{
			UserDAO userDAO = ImageSpaceDAOFactory.getDAOFactory().getUserDAO();
			User u = userDAO.findUser(user);
			if (u != null)
			{
				if (SysManagerFactory.getCacheManager().isCached(new CacheKey(u.login, CacheKeyType.Users)))
					SysManagerFactory.getCacheManager().set(new CacheKey(u.login, CacheKeyType.Users), u);
			}
			
			logger.logp(Level.INFO, "UserPermissionManager", "checkInDb", "not cached "+user+" "+projectName);
			
			ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
			Project p = projectDAO.findProject(projectName);
			if (p != null)
			{
				updateProject(p);
			}

			List<UserMembership> memberships = projectDAO.getMemberships(p.getID(), user);
			if (memberships != null)
			{
				for (UserMembership membership : memberships)
				{
					String name = getProject(membership.getProjectId()).getName();
					String userLogin = membership.getUserLogin();
					if (projectName.equals(name) && user.equals(userLogin))
					{
						permission = membership.getPermission();
						updateUserPermissionCache(projectName, userLogin, membership.getPermission());
						break;
					}
				}
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		return permission;
	}

	/**
	 * removes particular member from project
	 * 
	 * @param actor
	 * @param projectName
	 * @param login
	 * @throws DataAccessException
	 */
	void removeMemberUser(String actor, String projectName, String login) throws DataAccessException
	{
		logger.logp(Level.INFO, "UserPermissionManager", "removeMemberUser", "removing user " + login + " from project " + projectName);
		if (!canManageUsers(actor, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(login, CacheKeyType.UserPermission)))
		{
			Map<String, Permission> projectMembership = (Map<String, Permission>) SysManagerFactory.getCacheManager().get(new CacheKey(login, CacheKeyType.UserPermission));
			if (projectMembership.containsKey(projectName))
			{
				logger.logp(Level.FINEST, "UserPermissionManager", "removeMemberUser", "removed user " + login + " from project " + projectName);

				// update in cache
				projectMembership.remove(projectName);
				SysManagerFactory.getCacheManager().set(new CacheKey(login, CacheKeyType.UserPermission), projectMembership);

				// update in DB
				ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
				int projectID = getProjectFromCache(projectName).getID();

				projectDAO.removeProjectMembership(projectID, login);
			}
		}
	}

	/**
	 * returns list of all users in the system
	 * 
	 * @return
	 */
	public List<User> listUsers()
	{
		populateUsers();

		List<User> allUsers = new ArrayList<User>();

		List<User> users;
		try
		{
			users = DBImageSpaceDAOFactory.getDAOFactory().getUserDAO().findUser();
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "UserPermissionManager", "listUsers", "error retrieving users list from DB ", e);
			return allUsers;
		}

		if (users != null)
		{
			for (User u : users)
			{
				if (u.getRank() != Rank.Administrator)
					allUsers.add(u);
			}
		}

		return allUsers;
	}

	/**
	 * set permission of specified user on specified project
	 * 
	 * @param actorLogin
	 *            current logged in user
	 * @param projectName
	 *            specified project
	 * @param user
	 *            specified user
	 * @param newPermission
	 *            new permission to set
	 * @throws DataAccessException
	 */
	void setUserPermission(String actorLogin, String projectName, String user, Permission newPermission) throws DataAccessException
	{
		logger.logp(Level.INFO, "UserPermissionManager", "setUserPermission", "setting user " + user + " for project " + projectName + " permission " + newPermission.name());
		if (!canManageUsers(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int projectID = getProjectFromCache(projectName).getID();

		if (newPermission == Permission.None)
			removeMemberUser(actorLogin, projectName, user);

		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(user, CacheKeyType.UserPermission)))
		{
			Map<String, Permission> projectPermissionMap = (Map<String, Permission>) SysManagerFactory.getCacheManager().get(new CacheKey(user, CacheKeyType.UserPermission));
			if (!projectPermissionMap.containsKey(projectName))
			{
				insertUserPermission(projectID, user, newPermission);
			}
			else
			{
				updateUserPermission(projectID, user, newPermission);
			}
		}
		else
		{
			insertUserPermission(projectID, user, newPermission);
		}

		updateUserPermissionCache(projectName, user, newPermission);
	}

	private void updateUserPermissionCache(String projectName, String user, Permission permission)
	{
		Map<String, Permission> projectPermission = null;

		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(user, CacheKeyType.UserPermission)))
		{
			projectPermission = (Map<String, Permission>) SysManagerFactory.getCacheManager().get(new CacheKey(user, CacheKeyType.UserPermission));
		}
		else
		{
			projectPermission = new HashMap<String, Permission>();
		}
		projectPermission.put(projectName, permission);

		SysManagerFactory.getCacheManager().set(new CacheKey(user, CacheKeyType.UserPermission), projectPermission);
	}

	private void insertUserPermission(int projectID, String userLogin, Permission newPermission) throws DataAccessException
	{
		ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
		projectDAO.addUserPermission(projectID, userLogin, newPermission);
	}

	private void updateUserPermission(int projectID, String userLogin, Permission newPermission) throws DataAccessException
	{
		ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
		projectDAO.updateUserPermission(projectID, userLogin, newPermission);
	}

	private void insertProjectAccess(String userLogin, String projectName, Date lastAccess, UserAction action)
	{

		logger.logp(Level.INFO, "UserPermissionManager", "insertProjectAccess",
				"modification made by user " + userLogin + " to project " + projectName + " on " + lastAccess.toString() + ":" + action.toString());
		ProjectAccessKey projectAccessKey = new ProjectAccessKey();
		projectAccessKey.setUserLogin(userLogin);
		projectAccessKey.setProjectName(projectName);

		ProjectAccess projectAccess = new ProjectAccess();
		projectAccess.setKey(projectAccessKey);
		projectAccess.setAccessTime(lastAccess);
		projectAccess.setUserAction(action);

		projectAccessMap.put(projectAccessKey, projectAccess);
		logger.logp(Level.INFO, "UserPermissionManager", "insertProjectAccess", "projectAccessMap size increased to " + projectAccessMap.size());
		/*
		 * Add saving info into some datastricture here which wont get destroyed
		 * on restartProjectDAO projectDAO =
		 * ImageSpaceDAOFactory.getDAOFactory().getProjectDAO()
		 * projectDAO.addUserPermission(projectID, userLogin, newPermission);
		 */
	}

	/**
	 * returns the project access map
	 * 
	 * @return the project access map
	 */
	public TreeMap<ProjectAccessKey, ProjectAccess> getProjectAccessMap()
	{
		ProjectAccessComparator comparator = new ProjectAccessComparator(projectAccessMap);

		TreeMap<ProjectAccessKey, ProjectAccess> treeMap = new TreeMap<ProjectAccessKey, ProjectAccess>(comparator);
		treeMap.putAll(projectAccessMap);

		return treeMap;
	}

	public boolean canManageUsers(String user, String projectName)
	{
		Permission permission = getUserPermission(user, projectName);
		if (permission.ordinal() <= Permission.Manager.ordinal())
		{
			insertProjectAccess(user, projectName, new Date(), UserAction.MANAGE_USERS);
			return true;
		}
		return false;
	}

	/**
	 * can specified user perform delete operation on specified project
	 * 
	 * @param actorLogin
	 *            specified user
	 * @param projectName
	 *            specified project
	 * @return true if operation is permitted; false otherwise
	 */
	public boolean canDelete(String user, String projectName)
	{
		Permission permission = getUserPermission(user, projectName);
		if (permission.ordinal() <= Permission.Manager.ordinal())
		{
			insertProjectAccess(user, projectName, new Date(), UserAction.DELETE);
			return true;
		}

		return false;
	}

	/**
	 * can specified user upload/create new record in specified project
	 * 
	 * @param actorLogin
	 *            specified user
	 * @param projectName
	 *            specified project
	 * @return true if operation is permitted; false otherwise
	 */
	public boolean canUpload(String user, String projectName)
	{
		Permission permission = getUserPermission(user, projectName);
		if (permission.ordinal() <= Permission.Upload.ordinal())
		{
			insertProjectAccess(user, projectName, new Date(), UserAction.UPLOAD);
			return true;
		}
		return false;
	}

	/**
	 * can specified modify record in specified project
	 * 
	 * @param actorLogin
	 *            specified user
	 * @param projectName
	 *            specified project
	 * @return true if operation is permitted; false otherwise
	 */
	public boolean canWrite(String user, String projectName)
	{
		Permission permission = getUserPermission(user, projectName);
		if (permission.ordinal() <= Permission.Write.ordinal())
		{
			insertProjectAccess(user, projectName, new Date(), UserAction.WRITE);
			return true;
		}
		return false;
	}

	/**
	 * can specified user perform export operation on specified project
	 * 
	 * @param actorLogin
	 *            specified user
	 * @param projectName
	 *            specified project
	 * @return true if operation is permitted; false otherwise
	 */
	public boolean canExport(String user, String projectName)
	{
		Permission permission = getUserPermission(user, projectName);
		if (permission.ordinal() <= Permission.Export.ordinal())
		{
			insertProjectAccess(user, projectName, new Date(), UserAction.EXPORT);
			return true;
		}
		return false;
	}

	/**
	 * can specified user perform read operation on specified project
	 * 
	 * @param actorLogin
	 *            specified user
	 * @param guid
	 *            specified record
	 * @return true if operation is permitted; false otherwise
	 * @throws DataAccessException
	 */
	public boolean canRead(String user, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "UserPermissionManager", "canRead", "checking access permission for " + guid + " for actor " + user);

		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = getProjectFromCache(projectID).getName();

		return canRead(user, projectName);
	}

	/**
	 * Checks whether the specified user can do a write of the specified record
	 * 
	 * @param actorLogin
	 *            the login id of the user under consideration
	 * @param guid
	 *            the GUID of the relevant record
	 * @return true iff the user has write permission on the specified record,
	 *         false otherwise
	 * @throws DataAccessException
	 */
	public boolean canWrite(String actorLogin, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "UserPermissionManager", "canWrite", "checking access permission for " + guid + " for actor " + actorLogin);

		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = getProjectFromCache(projectID).getName();

		return canWrite(actorLogin, projectName);
	}

	/**
	 * Checks whether the specified user can do a delete of the specified record
	 * 
	 * @param actorLogin
	 *            the login id of the user under consideration
	 * @param guid
	 *            the GUID of the relevant record
	 * @return true iff the user has write permission on the specified record,
	 *         false otherwise
	 * @throws DataAccessException
	 */
	public boolean canDelete(String actorLogin, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "UserPermissionManager", "canDelete", "checking access permission for " + guid + " for actor " + actorLogin);

		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = getProjectFromCache(projectID).getName();

		return canDelete(actorLogin, projectName);
	}

	/**
	 * can specified user perform read operation on specified project
	 * 
	 * @param actorLogin
	 *            specified user
	 * @param projectName
	 *            specified project
	 * @return true if operation is permitted; false otherwise
	 */
	public boolean canRead(String user, String projectName)
	{
		logger.logp(Level.FINE, "UserPermissionManager", "canRead", "checking access permission for " + projectName + " for actor " + user);

		Permission permission = getUserPermission(user, projectName);
		if (permission.ordinal() <= Permission.Read.ordinal())
		{
			insertProjectAccess(user, projectName, new Date(), UserAction.READ);
			return true;
		}
		return false;
	}

	/**
	 * only administrators can edit user details
	 * 
	 * @param login
	 *            specified user
	 * @return true if operation is permitted; false otherwise
	 */
	public boolean canEditUserDetails(String login)
	{
		logger.logp(Level.FINEST, "UserPermissionManager", "canEditUserDetails", "is administrator " + getUser(login));

		if (getUser(login) == null)
			return false;

		logger.logp(Level.FINEST, "UserPermissionManager", "canEditUserDetails", "is administrator " + getUser(login));

		return getUser(login).getRank() == Rank.Administrator;
	}

	void updateProject(Project p)
	{
		logger.logp(Level.FINEST, "UserPermissionManager", "updateProject", "updated project " + p.getName() + " project status " + p.getStatus());

		SysManagerFactory.getCacheManager().set(new CacheKey(p.getID(), CacheKeyType.ProjectID), p);
		SysManagerFactory.getCacheManager().set(new CacheKey(p.getID(), CacheKeyType.ProjectName), p);
	}

	private synchronized Project getProjectFromCache(int projectID)
	{
		// check in cache
		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(projectID, CacheKeyType.ProjectID)))
			return (Project) SysManagerFactory.getCacheManager().get(new CacheKey(projectID, CacheKeyType.ProjectID));

		return getProject(projectID);
	}

	private synchronized Project getProjectFromCache(String projectName)
	{
		// check in cache
		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(projectName, CacheKeyType.ProjectName)))
			return (Project) SysManagerFactory.getCacheManager().get(new CacheKey(projectName, CacheKeyType.ProjectName));

		return getProject(projectName);
	}

	public synchronized Project getProject(int projectID)
	{
		try
		{
			CacheKey key = new CacheKey(projectID, CacheKeyType.ProjectID);
			if (SysManagerFactory.getCacheManager().isCached(key))
				return (Project) SysManagerFactory.getCacheManager().get(new CacheKey(projectID, CacheKeyType.ProjectID));

			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			ProjectDAO projectDao = factory.getProjectDAO();
			Project p = projectDao.findProject(projectID);

			if (p != null)
			{
				updateProject(p);
			}

			return p;
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "UserPermissionManager", "getProject", "error finding project for ID " + projectID, ex);
			return null;
		}
	}

	public synchronized Project getProject(String projectName)
	{
		try
		{
			CacheKey key = new CacheKey(projectName, CacheKeyType.ProjectName);
			if (SysManagerFactory.getCacheManager().isCached(key))
				return (Project) SysManagerFactory.getCacheManager().get(new CacheKey(projectName, CacheKeyType.ProjectName));

			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			ProjectDAO projectDao = factory.getProjectDAO();
			Project p = projectDao.findProject(projectName);

			if (p != null)
			{
				updateProject(p);
			}

			return p;
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "UserPermissionManager", "getProject", "error finding project for name " + projectName, ex);
			return null;
		}
	}

	synchronized void updateUser(String login, User user)
	{
		SysManagerFactory.getCacheManager().set(new CacheKey(login, CacheKeyType.Users), user);
	}

	synchronized User getUser(String login)
	{
		if (SysManagerFactory.getCacheManager().isCached(new CacheKey(login, CacheKeyType.Users)))
			return (User) SysManagerFactory.getCacheManager().get(new CacheKey(login, CacheKeyType.Users));

		try
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			UserDAO userDao = factory.getUserDAO();
			User u = userDao.findUser(login);

			if (u != null)
			{
				updateUser(login, u);
			}

			return u;
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "UserPermissionManager", "getUser", "error finding user for name " + login, ex);
			return null;
		}
	}

	private class ProjectAccessComparator implements Comparator<ProjectAccessKey> {
		private Map<ProjectAccessKey, ProjectAccess> baseMap;

		public ProjectAccessComparator(Map<ProjectAccessKey, ProjectAccess> paMap)
		{
			this.baseMap = paMap;
		}

		@Override
		public int compare(ProjectAccessKey o1, ProjectAccessKey o2)
		{
			ProjectAccess id1 = baseMap.get(o1);
			ProjectAccess id2 = baseMap.get(o2);

			if (id1 == null)
				return -1;

			if (id2 == null)
				return 1;

			if (id1.getAccessTime().compareTo(id2.getAccessTime()) == 0)
				return 0;
			else if (id1.getAccessTime().compareTo(id2.getAccessTime()) < 0)
				return 1;

			return -1;
		}
	}

	public static void main(String args[]) throws IOException
	{
		if (args != null && args.length > 0)
		{
			File f = new File(args[0]);// iworker.properties.
			System.out.println(f.getName());
			if (f.isFile())
			{
				System.out.println("loading system properties from " + f);
				BufferedReader inStream = new BufferedReader(new FileReader(f));
				Properties props = new Properties();
				props.load(inStream);

				props.putAll(System.getProperties()); // copy existing
														// properties, it is
														// overwritten :-(
				props.list(System.out);

				System.setProperties(props);
				inStream.close();
			}
		}
	}
}
