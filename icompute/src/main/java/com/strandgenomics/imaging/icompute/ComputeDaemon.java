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

package com.strandgenomics.imaging.icompute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Application;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Constraints;
import com.strandgenomics.imaging.iclient.impl.ws.worker.ImageSpaceWorkers;
import com.strandgenomics.imaging.iclient.impl.ws.worker.ImageSpaceWorkersServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Request;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Response;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Work;
import com.strandgenomics.imaging.iclient.impl.ws.worker.WorkStatus;
import com.strandgenomics.imaging.icore.app.Directive;

/**
 * This class is entry point class for compute worker.
 * This will be invoked from run-compute.sh with 2 arguments 
 * arg[1]: icompute.properties  //Contains configurable parameters of icompute
 * arg[2]: publisher.properties //Contains publisher name, publisher code
 * @author devendra
 *
 */

public class ComputeDaemon extends TimerTask {

	//keys for property files
	private final static String PUBLISHER_NAME_KEY = "publisher.name";
	private final static String PUBLISHER_CODE_KEY = "publisher.code";
	private final static String IWORKER_WEBSERVICE_URL = "iworkers.webservice.url";
	private final static String ICOMPUTE_APPLICATIONS_ROOT_DIR = "icompute.applications.root.dir";
	private static final String ICOMPUTE_PING_INTERVAL = "icompute.ping.interval";
	private final static String ICOMPUTE_APPLAUNCHER_EXTENSION = "icompute.applauncher.extension";
	private static final String ICOMPUTE_TYPE = "iengine.compute.task.type";
	
	protected Logger logger;

	private ImageSpaceWorkers iworker;
	private String accessToken;
	private Gson gson;
	private Timer timer;
	private String publisherName;
	private String publisherCode;
	
	/**
	 * the compute task manager
	 */
	private ITaskManager computeTaskManager;
	
	/**
	 * map for application name to its specification
	 */
	private Map<String,ComputeApplication> applicationMap;
	
	/**
	 * @param publisherName
	 * @param publisherCode
	 */
	public ComputeDaemon(String publisherName, String publisherCode) 
	{
		logger = Logger.getLogger("com.strandgenomics.imaging.icompute");
		
		this.publisherName = publisherName;
		this.publisherCode = publisherCode;

		this.applicationMap = new HashMap<String, ComputeApplication>();
		
		this.gson = new GsonBuilder().registerTypeAdapter(Constraints.class, new InterfaceAdapter<Constraints>()).create();
		
		this.timer = new Timer();
		
		this.computeTaskManager = getTaskManager();

		ImageSpaceWorkersServiceLocator iComputeWorkerLocator = new ImageSpaceWorkersServiceLocator();

		try 
		{
			iworker = iComputeWorkerLocator.getiWorkers(new URL(System.getProperty(IWORKER_WEBSERVICE_URL)));
		} 
		catch (MalformedURLException e) 
		{
			logger.logp(Level.WARNING, "ComputeDaemon", "ComputeDaemon",
					e.getMessage());
		} 
		catch (ServiceException e) 
		{
			logger.logp(Level.WARNING, "ComputeDaemon", "ComputeDaemon", e.getMessage());
		}
	}
	
	private ITaskManager getTaskManager()
	{
		ITaskManager computeTaskManager = null; 
		
		String computeTypeString = System.getProperty(ICOMPUTE_TYPE);
		ComputeWorkerType computeType = ComputeWorkerType.valueOf(computeTypeString);
		
		switch (computeType)
		{
			case EXTERNAL:
				computeTaskManager = TaskManagerFactory.getExternalTaskManager();
				break;
			case INTERNAL:
				computeTaskManager = TaskManagerFactory.getComputeTaskManager();
				break;
			default:
				computeTaskManager = TaskManagerFactory.getComputeTaskManager();
				break;
		}
		
		System.out.println("compute task manager = "+computeTaskManager);
		return computeTaskManager;
	}

	private void initialize() 
	{
		register(publisherName, publisherCode);
	}

	/**
	 * Register this worker with the server
	 * @param publisherName Publisher name
	 * @param publisherCode Publisher code
	 * 
	 */
	private void register(String publisherName, String publisherCode)
	{
		try
		{
			accessToken = iworker.register(publisherName, publisherCode);
		}
		catch (RemoteException e)
		{
			logger.logp(Level.WARNING, "ComputeDaemon", "register", e.getMessage());
		}
	}

	/**
	 * Publish worker applications to the server
	*/
	private void publishApplications()
	{
		List<Application> applications = loadApplications();
		try
		{
			iworker.publishApplications(accessToken, applications.toArray(new Application[0]));
		}
		catch (RemoteException e)
		{
			logger.logp(Level.WARNING, "ComputeDaemon", "publishApplications", e.getMessage());
		}
	}
	
	/**
	 * This function is called from main to start the compute daemon
	 */
	private void start() 
	{
		startPingTimer();
	}

	/**
	 * Start timer for ping request at regular intervals
	 */
	private void startPingTimer() 
	{
		long pingInterval = Long.parseLong(System.getProperty(ICOMPUTE_PING_INTERVAL));
		timer.schedule(this, 0, pingInterval);
	}

	/**
	 * This function is invoked at regular intervals by ping timer
	 * refer <code>TimerTask.run()</code>
	 * 
	 * Sends ping request and handles response directive by the server
	 */
	@Override
	public synchronized void run() 
	{
		WorkStatus[] statusReport = computeTaskManager.getActiveJobsWorkStatus();
		String workerState = computeTaskManager.getWorkerState().name();
		
		Request request = new Request(statusReport, workerState);
		try 
		{
			Response response = iworker.ping(accessToken, request);
			Directive directive = Directive.valueOf(response.getDirective());
			Work work = response.getWork();
			processDirectiveAction(directive, work);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.logp(Level.WARNING, "ComputeDaemon", "run", e.getMessage());
		}
	}
	
	/**
	 * Take appropriate action for given directive, work specification
	 * @param directive
	 * @param work work for <code>EXECUTE_TASK</code> directive.
	 * taskId specified in <code>work</code> is used for <code>TERMINATE_TASK</code> directive.
	 * @throws Exception 
	 * 
	 */
	private void processDirectiveAction(Directive directive, Work work) throws Exception 
	{
		logger.logp(Level.INFO, "ComputeDaemon", "processDirectiveAction", "Ping response: "+directive);		
		switch (directive) 
		{
			case EXECUTE_TASK:
				ComputeApplication computeApplication=applicationMap.get(work.getAppName());
				computeTaskManager.execute(computeApplication, work);
				break;
				
			case PUBLISH_APPLICATIONS:
				publishApplications();
				break;
				
			case REGISTRATION_REQUIRED:
				register(publisherName, publisherCode);
				break;
				
			case TERMINATE:
				computeTaskManager.terminate();
				break;
				
			case TERMINATE_TASK:
				computeTaskManager.terminateTask(work);
				break;
				
			case UNKNOWN_REQUEST:
				break;
				
			case WAIT:
				break;
				
			case CONTINUE:
				break;
				
			default:
				break;
		}
	}

	private List<Application> loadApplications() 
	{
		List<Application> applications = new ArrayList<Application>();

		String appRootDir = System.getProperty(ICOMPUTE_APPLICATIONS_ROOT_DIR);
		File appRootDirFile = new File(appRootDir);

		if (appRootDirFile.isDirectory())
		{
			File[] appDirs = appRootDirFile.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname)
				{
					return pathname.isDirectory();
				}
			});

			for (File appDir : appDirs)
			{
				Application application = loadApp(appDir);
				if (application != null)
				{
					applications.add(application);
				}
			}
		}
		else
		{
			logger.logp(Level.WARNING, "ComputeDaemon", "loadApplications",
					appRootDir + " is not a valid directory");
		}

		return applications;
	}

	private Application loadApp(File appDir) 
	{
		String appName = appDir.getName();
		String appLauncher = appName + System.getProperty(ICOMPUTE_APPLAUNCHER_EXTENSION);
		String appConfig = appName + ".gson";

		File appLauncherFile = new File(appDir, appLauncher);
		File appConfigFile = new File(appDir, appConfig);

		if (appConfigFile.exists() && appLauncherFile.exists())
		{
			try
			{
				Reader reader = new FileReader(appConfigFile);
				Application application = gson.fromJson(reader, Application.class);
				System.out.println(application.getName());
				applicationMap.put(application.getName(), new ComputeApplication(application, appLauncherFile, appConfigFile));
				
				return application;
			}
			catch (FileNotFoundException e)
			{
				logger.logp(Level.WARNING, "ComputeDaemon", "loadApp", e.getMessage());
			}
			catch (JsonSyntaxException e)
			{
				logger.logp(Level.WARNING, "ComputeDaemon", "loadApp", e.getMessage());
			}
			catch (JsonIOException e)
			{
				logger.logp(Level.WARNING, "ComputeDaemon", "loadApp", e.getMessage());
			}
		}
		return null;
	}

	public static void main(String[] args)
	{
		try
		{
			if (args != null && args.length > 0)
			{
				File f = new File(args[0]);
				if (f.isFile())
				{
					System.out.println("loading system properties from " + f);
					BufferedReader inStream = new BufferedReader(
							new FileReader(f));
					Properties props = new Properties();

					props.putAll(System.getProperties()); // copy existing
															// properties, it is
															// overwritten :-(
					props.load(inStream);
					props.list(System.out);

					System.setProperties(props);
					inStream.close();
				}
			}

			System.out.println("\n\n............\n\n");

			if (args.length > 1 && args[1] != null)
			{
				File f = new File(args[1]);
				if (f.isFile())
				{
					BufferedReader inStream = new BufferedReader(
							new FileReader(f));
					Properties props = new Properties();

					props.load(inStream);
					String publisherName = props
							.getProperty(PUBLISHER_NAME_KEY);
					String publisherCode = props
							.getProperty(PUBLISHER_CODE_KEY);
					ComputeDaemon computeDaemon = new ComputeDaemon(
							publisherName, publisherCode);
					computeDaemon.initialize();
					computeDaemon.start();
				}
				else
				{
					System.err.println(args[1]
							+ "is not a valid publisher.properties file");
				}
			}
			else
			{
				System.err.println("Missing argument: publisher.properties");
			}
		}
		catch (FileNotFoundException e)
		{
			System.err.println(e.getMessage());
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	
}
