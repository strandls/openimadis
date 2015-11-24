package com.strandgenomics.imaging.tileviewer.system;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Locally stores image tiles(in DB, FS or Both) as cache
 * 
 * @author Anup Kulkarni
 */
public class StorageManager {
	/**
	 * singleton instance of manger class
	 */
	private static StorageManager manager = null;
	
	/**
	 * root of tile storage
	 */
	private final String storage_root;
	
	/**
	 * A mapping between hashcode and location of tile
	 * hashcode uniquely identifies the tile 
	 */
	private Map<Integer,String> fileLocator;
	
	private StorageManager()
	{
		// TODO:
		// Initialise storage location where local cache will be stored

		storage_root = System.getProperty("cache_storage_root");

		// create dir structure for the same
		
		fileLocator=new HashMap<Integer,String>();
	}
	
	/**
	 * returns singleton instance of the TileManager
	 * @return
	 */
	public static StorageManager getInstance()
	{
		if(manager == null)
		{
			manager = new StorageManager();
		}
		return manager;
	}

	/**
	 * search for the tile in local cache and return if available
	 * @param params parameters that uniquely identify a tile
	 * @return buffered image for the specified parameters
	 * @throws TileNotReadyException
	 */
	public BufferedImage getTile(TileParameters params) throws TileNotReadyException
	{
		// locate file for the specified parameters in local cache
		// return the image if found, throw exception otherwise
		int hashcode = params.hashCode();
		
		BufferedImage tile=null;
		
		if(fileLocator.containsKey(hashcode)){
			try {
				tile =ImageIO.read(new File(getStorageRoot(params),getTilePath(params)));
			} catch (IOException e) {
				throw new TileNotReadyException("Tile not found");
			}
		}
		else{
			throw new TileNotReadyException("Tile not ready");
		}
		
		return tile;
	}
	
	/**
	 * return the path where tile would exist in local storage
	 * @param params specified tile
	 * @return the path where tile would exist in local storage
	 */
	private String getTilePath(TileParameters params)
	{
		return params.toString();
	}
	
	/**
	 * stores tile in local cache
	 * @param params
	 * @param image
	 */
	public void storeTile(TileParameters params, BufferedImage image)
	{
		// TODO: 
		// create appropriate directory structure where file will be stored
		// stores given buffered image in file
		
		String tileLocation = getTilePath(params);
		
		File dir = getStorageRoot(params);
		
		if(!dir.exists()){
			dir.mkdirs();
		}		
		
		File file = new File(getStorageRoot(params),tileLocation);
		
		try {
			ImageIO.write(image,"png",file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fileLocator.put(params.hashCode(), tileLocation);
	}
	
	/**
	 * returns directory root where specified tile would be stored
	 * @param params 
	 * @return directory root where specified tile would be stored
	 */
	private File getStorageRoot(TileParameters params)
	{
		// TODO:
		// create and return location where the tile for the specified parameters
		// will be stored
		return new File(storage_root, String.valueOf(params.getGuid()));
	}

	/**
	 * delete tiles for the specified record
	 * @param guid
	 */
	public void deleteTilesForRecord(long guid)
	{
		File file = new File(storage_root+guid);
		file.delete();
	}
	
	/**
	 * this function populates the fileLocator map 
	 * it parses the storage root and gets name of all records
	 * which have been cached and makes an entry into the fileLocator map
	 */
	public void populateFileLocator(){
		
		File storageDirectory = new File(storage_root);
		File recordDirectories[] = storageDirectory.listFiles();
		
		if(recordDirectories!=null){
			for(File recordDirectory : recordDirectories){
				
				String recordFiles[] = recordDirectory.list();
				if(recordFiles!=null){
					for(String recordFile : recordFiles){
						fileLocator.put(recordFile.hashCode(), recordFile);
					}
				}
			}
		}
	}
}
