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

package com.strandgenomics.imaging.iengine.backup;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Task performing actual backup of the project
 * 
 * @author Anup Kulkarni
 */
public class RestorationTask implements Callable<Void> {
	
	/**
	 * user performing the backup task
	 */
	public final String user;
	
	/**
	 * specified project to be backed up
	 */
	public final int projectId;
	
	/**
	 * location from where restore is to be done
	 */
	public final String restoreFrom;
	
	/**
	 * signature computed while backing up the data;
	 * this will be validated, and if not matched restoration will fail
	 */
	public final String sign;
	
	/**
	 * cancels current task
	 */
	private boolean cancelled;
	
	/**
	 * task is finished (either successful or failed)
	 */
	private boolean finished;
	
	private Logger logger;
	
	public RestorationTask(String actorLogin, int projectId, String location, String sign)
	{
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		this.user = actorLogin;
		this.projectId = projectId;
		this.restoreFrom = location;
		this.sign = sign;
		
		this.cancelled = false;
		this.finished = false;
	}

	@Override
	public Void call() throws IOException
	{
		logger.logp(Level.FINEST, "BackupTask", "run", "backing up project data for project"+projectId);
		
		Project project = SysManagerFactory.getProjectManager().getProject(projectId);
		if(project.getStatus() == ProjectStatus.Active)
		{
			// already active
			return null;
		}
		
		// update the project status
		SysManagerFactory.getProjectManager().updateProjectDetails(user, project.getName(), project.getNotes(), ProjectStatus.Restoring, project.getStorageQuota());
		project = SysManagerFactory.getProjectManager().getProject(projectId);
				
		File projectLocation = new File(SysManagerFactory.getStorageManager().getStorageRoot(), project.getLocation());
		projectLocation.mkdirs();
		
		File backup = new File(this.restoreFrom);
		
		boolean copyStatus = false;
		try
		{
			File gzTarball = backup;
			System.out.println("restoring from "+gzTarball.getAbsolutePath());
			
			// check md5 sum
			BigInteger md5 = Util.computeMD5Hash(gzTarball);
			System.out.println(md5.toString());
			if(!this.sign.equals(md5.toString()))
			{
				System.out.println("Signature doesnt match, cant restore the project");
				// cant restore
				SysManagerFactory.getProjectManager().updateProjectDetails(user, project.getName(), project.getNotes(), ProjectStatus.Archived, project.getStorageQuota());
				return null;
			}
			
			// untar
			Archiver.unTar(backup.getParentFile(), gzTarball);
			
			File untarDir = new File(backup.getParentFile(), project.getLocation());
			copyStatus = copyTree(untarDir, projectLocation);
		}
		catch (Exception e)
		{
			copyStatus = false;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("copying successful "+copyStatus);
		
		// delete untarred directory
		File tarFile = new File(backup.getParentFile(), project.getLocation());
		Util.deleteTree(tarFile);
		System.out.println("deleted tarred directory");
		
		if(!copyStatus)
		{
			// cancelled/failed
			SysManagerFactory.getProjectManager().updateProjectDetails(user, project.getName(), project.getNotes(), ProjectStatus.Archived, project.getStorageQuota());
			
			// get updated project status
			project = SysManagerFactory.getProjectManager().getProject(projectId);
		}
		
		else if(project.getStatus() == ProjectStatus.Restoring)
		{
			// in case the archive request has been cancelled dont delete/update the project data
			SysManagerFactory.getBackupManager().markProjectAsRestored(user, projectId);
		}
		
		System.out.println("Done");
		
		finished = true;
		return null;
	}
	
	/**
     * copies the content of the source folders to the destination folder; 
     * checks the cancelled flag if cancelled flag is set then returns false;
     * @param srcFolder
     * @param destinationFolder
     * @return true if successful, false otherwise
     */
	private boolean copyTree(File srcFolder, File destinationFolder) throws IOException 
	{
		srcFolder = srcFolder.getAbsoluteFile();
		destinationFolder = destinationFolder.getAbsoluteFile();
		
		if(!srcFolder.isDirectory() || !destinationFolder.isDirectory())
			throw new IllegalArgumentException("source "+srcFolder +" and destination "+destinationFolder +" must be existing folders");

		File[] contentsList = srcFolder.listFiles();

        if(contentsList != null)
        {
	        for (File member : contentsList) 
	        {
	        	if(this.cancelled)
	        		return false;
	        	
	            if (member.isDirectory()) 
	            {
	            	File target = new File(destinationFolder, member.getName());
	            	target.mkdir();
	            	
	            	copyTree(member, target);
	            } 
	            else 
	            {
	            	File target = new File(destinationFolder, member.getName());
	                Util.copy(member, target); //do a physical copy
	            }
	        }
        }
        return true;
	}

	/**
	 * cancel the current task
	 */
	public void cancelTask()
	{
		this.cancelled = true;
	}

	public boolean isFinished()
	{
		return this.finished;
	}
}
