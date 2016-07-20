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

import com.strandgenomics.imaging.icore.Constants;

/**
 * provide relavent TaskManager
 * 
 * @author Anup Kulkarni
 */
public class TaskManagerFactory {

	/**
	 * returns instance of ComputeTaskManager
	 * @return instance of ComputeTaskManager
	 */
	public static ITaskManager getComputeTaskManager() 
	{
        ITaskManager taskManager = createTaskManager(Constants.getComputeTaskManager());
        return taskManager;
    }
	
	/**
	 * returns instance of external task manager
	 * @return instance of external task manager
	 */
	public static ITaskManager getExternalTaskManager() 
	{
		ITaskManager taskManager = createTaskManager(Constants.getExternalTaskManager());
        return taskManager;
    }
	
	/**
	 * creates task manager for given class name
	 * @param className
	 * @return
	 */
	private static ITaskManager createTaskManager(String className)
    {
        
    	ITaskManager taskManager = null;

        // check for null
        if (className == null || className.length() == 0) 
        {
            throw new RuntimeException("system property "+className +" not found");
        }

        try 
        {
            // load class dynamically
            Class<?> coreClass = Class.forName(className);
            //calls the default constructor
            taskManager = (ITaskManager) coreClass.newInstance();
        }
        catch(ClassNotFoundException ex)
        {
            throw new RuntimeException("cannot find task manager class "+className);
        }
        catch(Exception eex)
        {
            throw new RuntimeException("cannot instantiate task manager class "+className);
        }

        return taskManager;
    }
}
