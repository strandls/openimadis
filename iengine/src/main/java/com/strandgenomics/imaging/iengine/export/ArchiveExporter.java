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

package com.strandgenomics.imaging.iengine.export;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.models.Archive;
import com.strandgenomics.imaging.iengine.models.Shortcut;
import com.strandgenomics.imaging.iengine.system.StorageManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Exporting record in original format
 * 
 * @author Anup Kulkarni
 */
public class ArchiveExporter {
	public ArchiveExporter() 
	{}
	
	/**
	 * exports given record to specified file 
	 * @param recordId specified record
	 * @param filepath path of the target output file
	 * @throws DataAccessException 
	 * @throws IOException 
	 */
	public String export(String actorLogin, List<Long> guids, long requestId, String exportName, String rootDirectory) throws IOException
	{
		File requestRoot = new File(rootDirectory, String.valueOf(requestId));
		requestRoot.mkdir();
		
		File tarBall = new File(rootDirectory, exportName+"_"+requestId+".tar.gz");
		
		try 
		{
			for(long guid:guids)
			{
				exportRecord(guid, requestRoot.getAbsolutePath());
			}
			
			Archiver.createTarRecursively(tarBall, true, requestRoot);
		} 
		catch (IOException e) 
		{
			// remove the tar ball
			tarBall.delete();
			// remove the request root
			Util.deleteTree(requestRoot);
			
			throw new IOException();
		}
		
		// delete the request root
		Util.deleteTree(requestRoot);
		
		return tarBall.getAbsolutePath();
	}
	
	private void exportRecord(long guid, String rootDirectory) throws DataAccessException, IOException	
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		// get archive signature
		RecordDAO recordDao = factory.getRecordDAO();
		BigInteger archiveSignature = recordDao.getArchiveSignature(guid);

		// find location of archive
		ArchiveDAO archiveDao = factory.getArchiveDAO();		
		Shortcut s = SysManagerFactory.getShortcutManager().getShortcut(archiveSignature);
		String location = "";
		if(s!=null)
		{
			BigInteger originalArchiveSign = s.getOriginalArchiveSign();
			Archive originalArchive = archiveDao.findArchive(originalArchiveSign);
			location = originalArchive.getStorageLocation();
		}
		else 
		{
			Archive archive = archiveDao.findArchive(archiveSignature);
			location = archive.getStorageLocation();
		}
		
		File storageRoot = SysManagerFactory.getStorageManager().getStorageRoot();
		File archiveRoot = new File(storageRoot, location);
		
		if(!archiveRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		// location of source files
		File srcRoot = new File(archiveRoot, StorageManager.FOLDER_NAME_SOURCE_FILES);
		
		if(!srcRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		File recordRoot = new File(rootDirectory, "Record-"+guid);
		recordRoot.mkdir();
		
		Util.copyTree(srcRoot, recordRoot);
	}
}
