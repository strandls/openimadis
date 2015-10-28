package com.strandgenomics.imaging.iengine.migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.iengine.system.Config;

public class DBUserChange {
	
	/**
	 * the logger
	 */
	protected Logger logger;
	
	/**
	 * name mappings
	 */
	private HashMap<String, String> names;

	public DBUserChange()
	{
		Config.getInstance(); //initializes the system
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		names = new HashMap<String, String>();
	}
	
	public void populate(String filename) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		while(br.ready())
		{
			String line = br.readLine().trim();
			String map[] = line.split("\\s+");
			
			System.out.println(map[0]+" "+map[1]);
			names.put(map[0].trim(), map[1].trim());
		}
	}
	
	public void updateUserTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update user_registry set user_login=? where user_login=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateProjectTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update project_registry set created_by=? where created_by=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateMembershipTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update project_membership set user_login=? where user_login=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateArchiveTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		HashSet<String> oldEntries = new HashSet<String>();
		
		PreparedStatement stmt = dbConn.prepareStatement("select * from archive_registry");
		ResultSet result = stmt.executeQuery();
		while (result.next())
		{
			String root_dir = result.getString("root_folder_name");
			
			oldEntries.add(root_dir);
		}
		stmt.close();
		
		for(String entry:oldEntries)
		{
			String root = entry.split("/")[0];
			String oldName = entry.split("/")[1];
			if(names.containsKey(oldName))
			{
				String newName = names.get(oldName);
				String newEntry = root+File.separator+newName;
				
				stmt = dbConn.prepareStatement("update archive_registry set root_folder_name=? where root_folder_name=?");
				stmt.setString(1, newEntry);
				stmt.setString(2, entry);
				
				int i = stmt.executeUpdate();
				stmt.close();
			}
		}
		
		dbConn.close();
		
	}
	
	public void updateRecordTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update record_registry set uploaded_by=? where uploaded_by=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateCommentsTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update user_comments_on_records set added_by=? where added_by=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateBookmarksTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update user_bookmarks set create_by=? where create_by=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateAttachmentsTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update record_attachments set uploaded_by=? where uploaded_by=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateRecentProjectTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update user_recent_projects set user_login=? where user_login=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateRecordChannelTable() throws FileNotFoundException, SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt = dbConn.prepareStatement("update user_record_channel_colors set user_login=? where user_login=?");
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			
			stmt.executeUpdate();
			stmt.close();
		}
		
		dbConn.close();
	}
	
	public void updateStorage(String storage_root) throws FileNotFoundException, SQLException
	{
		File root = new File(storage_root);
		
		if(!root.exists() || !root.isDirectory())
			return;
		
		String projects[] = root.list();
		for(String project:projects)
		{
			System.out.println(project);
			File p = new File(storage_root, project);
			if(!p.exists() || !p.isDirectory())
				continue;
			
			String users[] = p.list();
			for(String user:users)
			{
				System.out.println(user);
				File u = new File(p.getAbsolutePath(), user);
				if(!u.exists() || !u.isDirectory())
					continue;
				
				if(names.containsKey(user))
				{
					String newName = names.get(user);
					u.renameTo(new File(p.getAbsolutePath(), newName));
				}
			}
		}
	}
	
	public void updateVisualOverlaysTable(int projectNo)
	{
		Connection dbConn;
		try
		{
			dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt;
			try
			{
				stmt = dbConn.prepareStatement("update project_"+projectNo+"_visual_overlays set modified_by=? where modified_by=?");
				stmt.setString(1, newName);
				stmt.setString(2, oldName);
				
				stmt.executeUpdate();
				stmt.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateTextAnnotationsTable(int projectNo)
	{
		Connection dbConn;
		try
		{
			dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt;
			try
			{
				stmt = dbConn.prepareStatement("update project_"+projectNo+"_user_annotations_text set modified_by=? where modified_by=?");
				stmt.setString(1, newName);
				stmt.setString(2, oldName);
				
				stmt.executeUpdate();
				stmt.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateRealAnnotationsTable(int projectNo)
	{
		Connection dbConn;
		try
		{
			dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt;
			try
			{
				stmt = dbConn.prepareStatement("update project_"+projectNo+"_user_annotations_real set modified_by=? where modified_by=?");
				stmt.setString(1, newName);
				stmt.setString(2, oldName);
				
				stmt.executeUpdate();
				stmt.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateIntAnnotationsTable(int projectNo)
	{
		Connection dbConn = null;
		try
		{
			dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
			return;
		}
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt;
			try
			{
				stmt = dbConn.prepareStatement("update project_"+projectNo+"_user_annotations_int set modified_by=? where modified_by=?");
				stmt.setString(1, newName);
				stmt.setString(2, oldName);
				
				stmt.executeUpdate();
				
				stmt.close();
				stmt.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateTimeAnnotationsTable(int projectNo)
	{
		Connection dbConn;
		try
		{
			dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt;
			try
			{
				stmt = dbConn.prepareStatement("update project_"+projectNo+"_user_annotations_time set modified_by=? where modified_by=?");
				stmt.setString(1, newName);
				stmt.setString(2, oldName);
				
				stmt.executeUpdate();
				stmt.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateNavigationTable(int projectNo)
	{
		Connection dbConn;
		try
		{
			dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		for(Entry<String, String> name:names.entrySet())
		{
			String oldName = name.getKey();
			String newName = name.getValue();
			
			PreparedStatement stmt;
			try
			{
				stmt = dbConn.prepareStatement("update project_"+projectNo+"_record_navigation set uploaded_by=? where uploaded_by=?");
				stmt.setString(1, newName);
				stmt.setString(2, oldName);
				
				stmt.executeUpdate();
				stmt.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			dbConn.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<Integer> listTables() throws SQLException
	{
		Connection dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		
		DatabaseMetaData md = dbConn.getMetaData();
	    ResultSet rs = md.getTables(null, null, "%", null);
	    
	    Set<Integer> projectNo = new HashSet<Integer>();
	    while (rs.next()) 
	    {
	    	String name = rs.getString(3);
	    	String fields[] = name.split("_");
	    	int num = Integer.parseInt(fields[1]);
	    	projectNo.add(num);
	    }
	    rs.close();
	    
	    dbConn.close();
	    
	    return projectNo;
	}
	
	public Connection getConnection(String url, String user, String pw) throws SQLException
	{

		Connection dbConn = DriverManager.getConnection(url, user, pw);

		return dbConn;
	}
	
	public static void main(String args[]) throws IOException, SQLException{
		if(args != null && args.length>1)
    	{
    		File f = new File(args[0]);//iworker.properties
    		if(f.isFile())
    		{
    			System.out.println("loading system properties from "+f);
    			BufferedReader inStream = new BufferedReader(new FileReader(f));
	    		Properties props = new Properties();
	    		props.load(inStream);
	    		
	    		props.putAll(System.getProperties()); //copy existing properties, it is overwritten :-(
	    		props.list(System.out);
	    		
	    		System.setProperties(props);
	    		inStream.close();
    		}
    	}
		DBUserChange u = new DBUserChange();
		u.populate(args[1]);//nameMapping.txt
		
		u.updateRecordTable();
		
//		u.updateStorage(Constants.getStorageRoot().getAbsolutePath());
//		
//		System.out.println("Updating archive table");
//		u.updateArchiveTable();
//		System.out.println("Updating Attachments table");
//		u.updateAttachmentsTable();
//		System.out.println("Updating Bookmarks table");
//		u.updateBookmarksTable();
//		System.out.println("Updating Comments table");
//		u.updateCommentsTable();
//		System.out.println("Updating RecentProject table");
//		u.updateRecentProjectTable();
//		System.out.println("Updating RecordChannel table");
//		u.updateRecordChannelTable();
//		System.out.println("Updating Membership table");
//		u.updateMembershipTable();
//		System.out.println("Updating Project table");
//		u.updateProjectTable();
//		System.out.println("Updating User table");
//		u.updateUserTable();
//		
//		Set<Integer> projectNo = u.listTables();
//		for(int i:projectNo)
//		{
//			System.out.println("Updating project "+i);
//			u.updateIntAnnotationsTable(i);
//			u.updateNavigationTable(i);
//			u.updateRealAnnotationsTable(i);
//			u.updateTextAnnotationsTable(i);
//			u.updateTimeAnnotationsTable(i);
//			u.updateVisualOverlaysTable(i);
//		}
	}
}
