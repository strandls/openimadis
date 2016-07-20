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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * class to migrate defintation of channels that used int rgb color parameter to
 * LUTs
 * 
 * @author Anup Kulkarni
 *
 */
public class ChannelMigrationTest {
	
	public ChannelMigrationTest()
	{}
	
	public void migrateChannelsFromRecordRegistry() throws SQLException
	{
		Connection conn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		PreparedStatement stmt = conn.prepareStatement("select guid, channels from record_registry");
		ResultSet rs = stmt.executeQuery();
		while(rs.next())
		{
			Blob blob = rs.getBlob("channels");
			List<Channel> channels = (List<Channel>) toJavaObject(blob.getBinaryStream());
			
			Long guid = rs.getLong("guid");
			try
			{
				System.out.println(guid+" "+channels.get(0).getName()+" "+channels.get(0).getLut());
//				migrateChannels(guid, channels, "record_registry");
			}
			catch(Exception e)
			{
				System.out.println("error reading "+guid);
				e.printStackTrace();
			}
		}
		
		stmt.close();
		conn.close();
	}
	
	public void migrateChannelsFromUserPreferences() throws SQLException
	{
		Connection conn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		PreparedStatement stmt = conn.prepareStatement("select guid, channels from user_record_channel_colors");
		ResultSet rs = stmt.executeQuery();
		while(rs.next())
		{
			Blob blob = rs.getBlob("channels");
			List<Channel> channels = (List<Channel>) toJavaObject(blob.getBinaryStream());
			
			Long guid = rs.getLong("guid");
			migrateChannels(guid, channels, "user_record_channel_colors");
		}
		
		stmt.close();
		conn.close();
	}
	
	private void migrateChannels(long guid, List<Channel>channels, String tableName) throws SQLException
	{
		if(channels == null || channels.size()==0) return;
		
		List<String> lutNames = LutLoader.getInstance().getAvailableLUTs();
		
		int i = 1;
		for(Channel channel:channels)
		{
			if(channels.size()==1)
				channel.setLut("grays");
			else
			{
				channel.setLut(lutNames.get(i));
				i++;
				if(i>6)
					i = 1;
			}
		}
			
		Connection conn = getConnection(Constants.getDatabaseURL(),Constants.getDatabaseUser(), Constants.getDatabasePassword());
		
		PreparedStatement stmt = conn.prepareStatement("update "+tableName+" set channels=? where guid=?");
		byte[] data = toByteArray(channels);
		try
		{
			System.out.println("migrating "+guid+" "+channels);
			stmt.setBinaryStream(1, new ByteArrayInputStream(data), data.length);
			stmt.setLong(2, guid);
			
			stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		stmt.close();
		conn.close();
	}
	
	protected byte[] toByteArray(Object obj)  
	{
		try
		{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(8096);
			ObjectOutputStream oos = new ObjectOutputStream(outStream);
			oos.writeObject(obj);
			oos.close();
			
			return outStream.toByteArray();
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	protected Object toJavaObject(InputStream inStream)
	{
        if(inStream == null) return null;
        
        Object pojo = null;
        
        try 
        {
            ObjectInputStream ois = new ObjectInputStream(inStream);
            pojo = ois.readObject();
            
            ois.close();
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
        finally
        {
        	Util.closeStream(inStream);
        }

        return pojo;
	}
	
	public Connection getConnection(String url, String user, String pw) throws SQLException
	{
		Connection dbConn = DriverManager.getConnection(url, user, pw);

		return dbConn;
	}
	
	public static void main(String args[]) throws IOException, SQLException
	{
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
		
		ChannelMigrationTest c = new ChannelMigrationTest();
		c.migrateChannelsFromRecordRegistry();
//		c.migrateChannelsFromUserPreferences();
	}
}
