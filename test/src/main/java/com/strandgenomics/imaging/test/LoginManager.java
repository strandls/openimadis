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

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;

public class LoginManager {
	String hostIP;
	int hostPort;
	String clientId;
	String username;
	String password;
	String url;
	String project_name;
	String reportPath;
	String recordInfoPath;
	ReportWriter repWriter;
	
	public LoginManager() throws IOException{
		GetPropertyValues PropVal = new GetPropertyValues("/home/gs/Eclipse_dir/iManage_automation/config_properties/config.properties");
		hostIP = PropVal.getPropValues("hostIP");
		hostPort = Integer.parseInt(PropVal.getPropValues("hostPort"));
		clientId = PropVal.getPropValues("clientId");
		username = PropVal.getPropValues("username");
		password = PropVal.getPropValues("password");
		url = PropVal.getPropValues("url");
		project_name = PropVal.getPropValues("project_name");
		reportPath = PropVal.getPropValues("report_path");
		repWriter = new ReportWriter(reportPath);
				
	}
	
	public void printConfigPropValues(){
		System.out.println(hostIP+hostPort+clientId+username+password+url+project_name);
	}
	
	private String getAuthCode(String username, String password, String clientID){

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url + "iManage/auth/login");
		method.addParameter("loginUsername", username);
		method.addParameter("loginPassword", password);

		try {
			client.executeMethod(method);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PostMethod postMethod = new PostMethod(url + "iManage/compute/generateAuthCode");
		postMethod.addParameter("clientID",clientID );
		postMethod.addParameter("services", "[\"AUTHENTICATION\", \"ISPACE\", \"SEARCH\", \"LOADER\", \"UPDATE\", \"MANAGEMENT\", \"COMPUTE\"]");
		postMethod.addParameter("expiryTime", ""+((new Date()).getTime()+(24*3600*1000)));

		try {
			client.executeMethod(postMethod);
		} catch (HttpException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String auth = null;
		try {
			auth = postMethod.getResponseBodyAsString();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return auth.split(":")[1].split(",")[0].split("\"")[1];
	}

	public void login(){
		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		String auth = getAuthCode(username, password, clientId);

		try{
			// login using auth code
			boolean status = ispace.login(false, hostIP, hostPort, clientId, auth);
			if (status){
				String value = "Login"+"\t"+"PASS";
				repWriter.writeToReport(value);
				repWriter.closeFileWriters();
			}else{
				String value = "Login"+"\t"+"FAIL";
				repWriter.writeToReport(value);
				repWriter.closeFileWriters();
			}
				
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

	}	

}
