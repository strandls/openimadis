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

package com.strandgenomics.imaging.iengine.migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.iengine.system.Config;

public class DBUploadTimeChange {
	
	/**
	 * the logger
	 */
	protected Logger logger;
	
	/**
	 * upload time mappings
	 */
	private HashMap<String, String> uploadTime;

	public DBUploadTimeChange()
	{
		Config.getInstance(); //initializes the system
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		uploadTime = new HashMap<String, String>();
	}
	
	private void populate() throws SQLException
	{
		Connection dbConn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		PreparedStatement stmt = dbConn.prepareStatement("select * from record_registry");
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			Long guid = result.getLong("guid");
			Timestamp upload_time = result.getTimestamp("upload_time");
			Integer projectId = result.getInt("project_id");
			
			updateTimeStamp(guid, projectId, upload_time);
		}
		
		stmt.close();
		dbConn.close();
	}
	
	private void alterTable(int projectNo) throws SQLException
	{
		Connection dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		PreparedStatement stmt = dbConn.prepareStatement("alter table project_?_record_navigation modify column source_file_time timestamp NOT NULL DEFAULT 0");
		stmt.setInt(1, projectNo);
		stmt.execute();
		stmt.close();
		
		stmt = dbConn.prepareStatement("alter table project_?_record_navigation modify column upload_time timestamp NOT NULL DEFAULT 0");
		stmt.setInt(1, projectNo);
		stmt.execute();
		stmt.close();
		
		stmt = dbConn.prepareStatement("alter table project_?_record_navigation modify column creation_time timestamp NOT NULL DEFAULT 0");
		stmt.setInt(1, projectNo);
		stmt.execute();
		stmt.close();
		
		stmt = dbConn.prepareStatement("alter table project_?_record_navigation modify column acquired_time timestamp NOT NULL DEFAULT 0");
		stmt.setInt(1, projectNo);
		stmt.execute();
		stmt.close();
		
		dbConn.close();
	}
	
	private void updateTimeStamp(Long guid, Integer projectId, Timestamp upload_time) throws SQLException
	{
		Connection dbConn = getConnection(Constants.getStorageDatabaseURL(),Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
		PreparedStatement stmt = dbConn.prepareStatement("update project_?_record_navigation set upload_time=? where guid=?");
		stmt.setInt(1, projectId);
		stmt.setTimestamp(2, upload_time);
		stmt.setLong(3, guid);
		
		stmt.executeUpdate();
		
		stmt.close();
		dbConn.close();
	}

	public Connection getConnection(String url, String user, String pw) throws SQLException
	{

		Connection dbConn = DriverManager.getConnection(url, user, pw);

		return dbConn;
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
	
	public static void main(String args[]) throws IOException, SQLException{
		if(args != null && args.length>0)
    	{
    		File f = new File(args[0]);//iworker.properties.
    		System.out.println(f.getName());
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
		DBUploadTimeChange u = new DBUploadTimeChange();
		Set<Integer> projectNos = u.listTables();
		
		for(int no:projectNos)
		{
			u.alterTable(no);
		}
		
		u.populate();
	}
}
