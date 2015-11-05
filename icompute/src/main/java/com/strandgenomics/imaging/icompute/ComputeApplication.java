package com.strandgenomics.imaging.icompute;

import java.io.File;

import com.strandgenomics.imaging.iclient.impl.ws.worker.Application;

/**
 * description of ComputeApplication
 * 
 * @author Devendra/Anup Kulkarni
 */
public class ComputeApplication{
	
	/**
	 * application name and version
	 */
	private Application application;
	/**
	 * the shell script which is supposed to be executed
	 */
	private File appLauncherFile;
	/**
	 * gson file which describes the parameters for the application
	 */
	private File appConfigFile;

	public ComputeApplication(Application application, File appLauncherFile, File appConfigFile) 
	{
		this.application=application;
		this.appLauncherFile=appLauncherFile;
		this.appConfigFile=appConfigFile;
	}
	
	/**
	 * returns the application shell script
	 * @return
	 */
	public File getAppLauncherFile() 
	{
		return appLauncherFile;
	}
	
	/**
	 * returns the application configuration file
	 * @return
	 */
	public File getAppConfigFile()
	{
		return appConfigFile;
	}
}
