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
