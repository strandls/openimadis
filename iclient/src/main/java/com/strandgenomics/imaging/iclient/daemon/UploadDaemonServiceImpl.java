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

package com.strandgenomics.imaging.iclient.daemon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.strandgenomics.imaging.iclient.local.UploadStatus;
import com.strandgenomics.imaging.icore.Constants;

/**
 * Record upload service which will run as OS service and used for scheduling
 * record uploads
 * 
 * @author Anup Kulkarni
 */
public class UploadDaemonServiceImpl implements UploadDaemonService
{
	public static long ID_GENERATOR = System.nanoTime();
	
	private static Object lock = new Object();
	/**
	 * sinleton instance of upload daemon
	 */
	private static UploadDaemonServiceImpl singleton = null;
	/**
	 * queue of all upload requests
	 */
	private List<UploadSpecification> uploadQueue;
	/**
	 * map of upload-id to upload-specification
	 */
	private Map<Long, UploadSpecification> idToSpecMap;
	/**
	 * logger
	 */
	private Logger logger;
	/**
	 * executors
	 */
	private ExecutorService[] executors;
	
	private UploadDaemonServiceImpl(int noOfThreads) throws MalformedURLException, ServiceException
    {
    	logger = Logger.getLogger("com.strandgenomics.imaging.iclient.daemon");
    	
    	executors = new ExecutorService[noOfThreads];
    	//Creates a thread pool that reuses a fixed number of threads operating off a 
    	//shared unbounded queue. At any point, at most nThreads threads will be active 
    	//processing tasks. If additional tasks are submitted when all threads are active,
    	//they will wait in the queue until a thread is available. 
    	//If any thread terminates due to a failure during execution prior to shutdown, 
    	//a new one will take its place if needed to execute subsequent tasks. 
    	//The threads in the pool will exist until it is explicitly shutdown. 
    	for(int i = 0; i < noOfThreads; i++)
    	{
    		executors[i] = Executors.newFixedThreadPool(1);
    	}
    	
    	loadSession();
    }
	
	/**
	 * Returns the singleton instance of the UploadService 
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 */
	public static UploadDaemonServiceImpl getInstance() throws MalformedURLException, ServiceException
	{
		if(singleton == null)
		{
			synchronized(lock)
			{
				if(singleton == null)
				{
					singleton = new UploadDaemonServiceImpl(1);
					
					//initialize the log4j logger
					try 
					{
						File log4jFile = new File(Constants.getLogDirectory(), Constants.getLogFilename()); 
						RollingFileAppender rfa = new RollingFileAppender(new PatternLayout(), log4jFile.getAbsolutePath());
						org.apache.log4j.Logger.getRootLogger().addAppender(rfa);
						
						org.apache.log4j.Level logLevel = org.apache.log4j.Level.INFO;
						org.apache.log4j.Logger.getRootLogger().setLevel(logLevel);
					} 
					catch (IOException e) 
					{
						System.out.println("unable to initialize log4j logger used by bio-format library "+e);
					} 
				}
			}
		}
		
		return singleton;
	}
	
	@Override
	public synchronized void submitUploadRequest(UploadSpecification spec) throws Exception
	{
		logger.logp(Level.INFO, "UploadDaemonServiceImpl", "submitUploadRequest", "submitted upload request "+spec);
		
		// generate the id
		spec.setUploadId(generateID());
		logger.logp(Level.INFO, "UploadDaemonServiceImpl", "submitUploadRequest", "got request "+spec.getUploadId());
		
		// submit the task
		submitTask(spec);
		
		// add to upload queue
		uploadQueue.add(spec);
		idToSpecMap.put(spec.getUploadId(), spec);
		
		// write the session
		writeSession(spec);
	}
	
	@Override
	public synchronized void clearCompleted(String user, String projectName) throws RemoteException, Exception
	{
		List<UploadSpecification> toRemove = new ArrayList<UploadSpecification>();
		for(UploadSpecification spec:uploadQueue)
		{
			if(spec.getStatus() != UploadStatus.QueuedBackground && spec.getUserName().equals(user) && spec.getProjectName().equals(projectName))
			{
				// delete from filesystem
				File file = new File(spec.getSessionFilepath());
				file.delete();
				
				// delete from cache
				toRemove.add(spec);
				
				idToSpecMap.remove(spec.getUploadId());
			}
		}
		
		uploadQueue.removeAll(toRemove);
	}

	@Override
	public synchronized void cancelSelected(String userName, List<Long> uploadIds) throws RemoteException, Exception
	{
		for(Long id: uploadIds)
		{
			if(idToSpecMap.containsKey(id))
			{
				UploadSpecification spec = idToSpecMap.get(id);
				spec.setStatus(UploadStatus.NotUploaded);
				spec.setMessage("Cancelled");
				spec.setCanceled();
			}
		}
	}
	
	/**
	 * submit the upload task to uploaders
	 * @param spec the upload task
	 */
	private void submitTask(UploadSpecification spec)
	{
		UploadDaemonTask task = new UploadDaemonTask(spec);
		executors[0].submit(task);
	}
	
	@Override
	public List<UploadSpecification> getStatus(String userName, String projectName) throws RemoteException, Exception
	{
		List<UploadSpecification> userUploads = new ArrayList<UploadSpecification>();
		
		for(UploadSpecification spec:uploadQueue)
		{
			userUploads.add(spec);
		}
		
		return userUploads;
	}
	
	/**
	 * generates the unique identifier for upload task
	 * @return
	 */
	private static synchronized final long generateID()
	{
		return ID_GENERATOR++;
	}
	
	/**
	 * store the object as state of upload daemon
	 * @param spec
	 */
	private void writeSession(UploadSpecification spec)
	{
		File file = new File(getUploadSessionDirectory(), spec.getUploadId()+"");
		spec.setSessionFilepath(file.getAbsolutePath());
		
		UploadDaemonUtil.storeObject(spec);
	}
	
	/**
	 * load the previous state of the upload daemon 
	 */
	private void loadSession()
	{
		uploadQueue = new ArrayList<UploadSpecification>();
		idToSpecMap = new HashMap<Long, UploadSpecification>();
		
		// load the upload queue from session
		File sessionDir = new File(getUploadSessionDirectory());
		if(sessionDir.isDirectory())
		{
			File files[] = sessionDir.listFiles();
			for(File file:files)
			{
				UploadSpecification spec = UploadDaemonUtil.loadObject(file.getAbsolutePath());
				if(spec!=null)
				{
					System.out.println(spec.getUploadId()+" "+spec.getStatus());
					uploadQueue.add(spec);
					
					idToSpecMap.put(spec.getUploadId(), spec);
				}
			}
		}
		
		// submit the queued uploads
		for (UploadSpecification element : uploadQueue)
		{
			if(element.getStatus()==UploadStatus.QueuedBackground)
			{
				submitTask(element);
			}
		}
	}
	
	/**
	 * returns the rmi port where the upload daemon is running
	 * @return
	 */
	private static int getServicePort()
	{
		System.out.println(Constants.getUploadServicePort());
		return Constants.getUploadServicePort();
	}
	
	/**
	 * returns the path of directory where the state of upload daemon is stored
	 * @return
	 */
	private String getUploadSessionDirectory()
	{
		String acqClientDir = Constants.getConfigDirectory();
		
		File uploadSession = new File(acqClientDir, "upload-daemon");
		uploadSession.mkdirs();
		
		return uploadSession.getAbsolutePath();
	}
	
	/**
	 * start the upload daemon
	 */
	private static void startUploadService()
	{
		try 
        {
        	//create the registry
            LocateRegistry.createRegistry(getServicePort());  
            UploadDaemonServiceImpl serverObj = UploadDaemonServiceImpl.getInstance();
            
            UploadDaemonService stub = (UploadDaemonService) UnicastRemoteObject.exportObject(serverObj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(getServicePort());
            registry.bind(UploadDaemonService.class.getCanonicalName(), stub);

            System.out.println("UploadDaemonService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}
	
	public static void main(String args[]) throws IOException {

    	if(args != null && args.length > 0)
    	{
    		File f = new File(args[0]);
    		if(f.isFile())
    		{
    			System.out.println("loading system properties from "+f);
    			BufferedReader inStream = new BufferedReader(new FileReader(f));
	    		Properties props = new Properties();
	    		
	    		props.putAll(System.getProperties()); //copy existing properties, it is overwritten :-(
	    		props.load(inStream);
	    		props.list(System.out);
	    		
	    		System.setProperties(props);
	    		inStream.close();
    		}
    	}
    	
    	String uploadPort = System.getProperty("iengine.upload.port");
    	System.out.println(uploadPort);
    	
    	System.out.println("\n\n............\n\n");
    	
    	// start export service
    	startUploadService();
    }
}
