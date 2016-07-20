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

package com.strandgenomics.imaging.ithreedview;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.IRecord;

/**
 * This class manages the session for a particular user. Session Manager is attached per user. It is initiated when the user logs in
 * and is functional till the user logs out. 
 * 
 * @author Anup Kulkarni
 * 
 */
public class SessionManager {

	private String userName;
	
	public SessionManager(String userName){
		this.userName = userName;
	}

	/**
	 * stores the session given list of records
	 * @param list of records to be saved
	 */
	public void storeSession(List<IRecord> records){
		
		String filename = Constants.getConfigDirectory() + File.separator + "imanage-acqclient-" + userName + ".session";
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(new File(filename)));
			os.writeObject(records);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if (os != null) {
                	os.flush();
                	os.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
	
	/**
	 * loads the session for this user
	 * @return list of records saved in previous session
	 */
	public List<IRecord> loadSession()
	{
		List<IRecord> records = new ArrayList<IRecord>();
		File sessionFile = new File(Constants.getConfigDirectory(), "imanage-acqclient-" + userName + ".session");
		
		if(!sessionFile.exists())
			return records;
		
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream(sessionFile));
			records = (List<IRecord>) is.readObject();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
            try {
                if (is != null) {
                	is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		
		return records;
	}
	
	/**
	 * loads the session for this user
	 * @return list of records saved in previous session
	 * @throws URISyntaxException 
	 */
	public List<IRecord> loadSession(String filePath) throws URISyntaxException
	{
		List<IRecord> records = new ArrayList<IRecord>();
		File sessionFile = new File("imanage-acqclient-anup");
		
		if(!sessionFile.exists())
			return records;
		
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream(sessionFile));
			records = (List<IRecord>) is.readObject();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
            try {
                if (is != null) {
                	is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		
		return records;
	}
}
