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

package com.strandgenomics.imaging.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class GetPropertyValues {
	
	public String PropFilePath;
	public File file = null;
	public FileInputStream fileInput = null;
	public Properties prop = null;
	
	public GetPropertyValues(String PropFilePath) throws IOException{
		this.PropFilePath = PropFilePath;
		try{
			file = new File(PropFilePath);
			fileInput = new FileInputStream(file);
		}catch (Exception e) {
			e.printStackTrace();
		}
		prop = new Properties();
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			fileInput.close();
		}
	}
		
		

	public String getPropValues(String key) throws IOException {
		String value = prop.getProperty(key);
		return value;
	}
}
