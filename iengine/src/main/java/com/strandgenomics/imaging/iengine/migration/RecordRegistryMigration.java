package com.strandgenomics.imaging.iengine.migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import com.strandgenomics.imaging.iengine.system.StorageManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.ThumbnailManager;

public class RecordRegistryMigration {
	private static final long MAX_GUID = 1026;

	public RecordRegistryMigration()
	{ }
	
	public void migrateRecordRegistry()
	{
		StorageManager storageManager = SysManagerFactory.getStorageManager();
		ThumbnailManager thumbnailManager = SysManagerFactory.getThumbnailManager();
		for(long guid = 1;guid<MAX_GUID;guid++)
		{
			try
			{
				System.out.println(guid);
//				File thumb = storageManager.getThumbnailFile(guid);
//				BufferedImage img = ImageIO.read(thumb);
//				thumbnailManager.setThumbnail("administrator",guid, img);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
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
		RecordRegistryMigration r = new RecordRegistryMigration();
		r.migrateRecordRegistry();
	}
}
