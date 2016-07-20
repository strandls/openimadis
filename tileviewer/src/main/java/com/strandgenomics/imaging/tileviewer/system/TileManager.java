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

package com.strandgenomics.imaging.tileviewer.system;

import java.awt.image.BufferedImage;

import com.strandgenomics.imaging.iclient.ImageSpaceSystem;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class TileManager {
	/**
	 * singleton instance of manger class
	 */
	private static TileManager manager = null;
	
	/**
	 * object of tile fetching service
	 */
	private static TileFetchingService service=null;
	
	/**
	 * no. of threads in service
	 */
	private static int nThreads=1;
	
	private TileManager()
	{
		service= TileFetchingService.getInstance(nThreads);
	}
	
	/**
	 * returns singleton instance of the TileManager
	 * @return singleton instance of the TileManager
	 */
	public static TileManager getInstance()
	{
		if(manager == null)
		{
			manager = new TileManager();
		}
		return manager;
	}
	
	/**
	 * get image for specified tile parameters
	 * @param params tile parameters
	 * @return image for specified tile parameters
	 * @throws TileNotReadyException 
	 * @throws TileRequestCancelledException 
	 */
	public BufferedImage getTile(ImageSpaceSystem ispace, TileParameters params) throws TileNotReadyException, TileRequestCancelledException
	{
		BufferedImage tile=null;
		
		try{			
			tile=StorageManager.getInstance().getTile(params);
			System.out.println("already present "+params.getX()+" "+params.getY());
			return tile;		 
		}catch(TileNotReadyException e){			
			submitRequest(ispace, params);
			throw e;
		}
	}
	
	/**
	 * submit appropriate request for tile prefetching 
	 * @param ispace service endpoint for valid user session
	 * @param params specified tile parameters
	 * @throws TileNotReadyException 
	 * @throws TileRequestCancelledException 
	 */
	public void submitRequest(ImageSpaceSystem ispace, TileParameters params) throws TileNotReadyException, TileRequestCancelledException
	{
		// TODO:
		// create appropriate task
		// submit the request
		service.submitRequest(ispace, params);
	}
	
	/**
	 * aborts tile fetching
	 */
	public void cancelTileFetching() {
		service.cancelSubmitedTasks();
	}
}
