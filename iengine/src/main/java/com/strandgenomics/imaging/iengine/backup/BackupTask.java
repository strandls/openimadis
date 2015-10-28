package com.strandgenomics.imaging.iengine.backup;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.models.BackupDataObject;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Task performing actual backup of the project
 * 
 * @author Anup Kulkarni
 */
public class BackupTask implements Callable<Void> {
	
	/**
	 * user performing the backup task
	 */
	public final String user;
	
	/**
	 * specified project to be backed up
	 */
	public final int projectId;
	
	/**
	 * location where backup is to be done
	 */
	public final String backupLocation;
	
	/**
	 * cancels current task
	 */
	private boolean cancelled;
	
	/**
	 * task is finished (either successful or failed)
	 */
	private boolean finished;
	
	private Logger logger;
	
	public BackupTask(String actorLogin, int projectId, String location)
	{
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		this.user = actorLogin;
		this.projectId = projectId;
		this.backupLocation = location;
		
		this.cancelled = false;
		this.finished = false;
	}

	@Override
	public Void call() throws IOException
	{
		logger.logp(Level.INFO, "BackupTask", "run", "backing up project data for project"+projectId);
		System.out.println("backing up project data for project"+projectId);
		
		Project project = SysManagerFactory.getProjectManager().getProject(projectId);
		if(project.getStatus() == ProjectStatus.Archived)
		{
			// already archived
			this.finished = true;
			return null;
		}
		
		// update the project status
		SysManagerFactory.getProjectManager().updateProjectDetails(user, project.getName(), project.getNotes(), ProjectStatus.Archiving, project.getStorageQuota());
		project = SysManagerFactory.getProjectManager().getProject(project.getName());// get updated project
		
		// get the locations
		File projectLocation = new File(SysManagerFactory.getStorageManager().getStorageRoot(), project.getLocation());
		System.out.println("backing up project data from "+projectLocation);
		File backup = new File(this.backupLocation);
		backup.mkdirs();
		System.out.println("backing up project data to "+backup.getAbsolutePath());
		
		boolean copyStatus = false;
		BigInteger md5 = null;
		File tarBall = null;
		try
		{
			copyStatus = copyTree(projectLocation, backup);
			if(copyStatus)
			{
				tarBall = new File(backup.getParentFile(), project.getLocation()+".tar.gz");
				
				// create tar ball
				Archiver.createTarRecursively(tarBall, false, backup);
				
				// delete the directory
				boolean deleteResult = Util.deleteTree(backup);
				System.out.println("deleting temp directory "+deleteResult);
				
				// calculate MD5
				md5 = Util.computeMD5Hash(tarBall);
				System.out.println(md5.toString());
			}
		}
		catch (Exception e)
		{
			copyStatus = false;
			e.printStackTrace();
		}
		
		System.out.println("done copying "+copyStatus);
		
		if(!copyStatus)
		{
			// cancelled/failed
			SysManagerFactory.getProjectManager().updateProjectDetails(user, project.getName(), project.getNotes(), ProjectStatus.Active, project.getStorageQuota());
			
			// get updated project status
			project = SysManagerFactory.getProjectManager().getProject(projectId);
		}
		
		if(project.getStatus() == ProjectStatus.Archiving)
		{
			System.out.println("changing the status");
			
			// in case the archive request has been cancelled dont delete/update the project data
			BackupDataObject details = new BackupDataObject(this.projectId, tarBall.getName(), md5.toString());
			SysManagerFactory.getBackupManager().markProjectAsArchived(this.user, details);
			
			// delete the project data from storage location
			boolean deleteResult = Util.deleteTree(projectLocation);
			System.out.println("deleting project directory "+deleteResult);
		}
		
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
