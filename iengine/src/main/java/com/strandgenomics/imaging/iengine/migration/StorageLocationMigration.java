package com.strandgenomics.imaging.iengine.migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Properties;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.models.Archive;
import com.strandgenomics.imaging.iengine.models.Record;

/**
 * class to migrate navigation table to contain physical storage location of the record
 * @author anup
 *
 */
public class StorageLocationMigration {
	
	private static long MAX_GUID = 66;
	
	public StorageLocationMigration() {}
	
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
		
		MAX_GUID = Long.parseLong(args[1]);
		
		StorageLocationMigration c = new StorageLocationMigration();
		c.injectStorageLocations();
	}

	private void injectStorageLocations() throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		RecordDAO recordDao = factory.getRecordDAO();
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		
		
		for(long i=1;i<=MAX_GUID;i++)
		{
			Record r = recordDao.findRecord(i);
			if(r == null) continue;
			BigInteger archiveSign = r.archiveSignature;
			
			Archive arch = archiveDao.findArchive(archiveSign);
			
			String storageLocation = arch.getStorageLocation();
			factory.getNavigationDAO(r.projectID).insertStorageLocation(i, storageLocation);
		}
	}
}
