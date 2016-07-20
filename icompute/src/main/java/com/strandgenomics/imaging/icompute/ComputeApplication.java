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
