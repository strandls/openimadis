package com.strandgenomics.imaging.iacquisition;


import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

import com.strandgenomics.imaging.iclient.dialogs.LoginDialog;
import com.strandgenomics.imaging.iclient.util.ConnectionPreferences;
import com.strandgenomics.imaging.icore.Constants;

/**
 * For testing AcquisitionUI
 */
public class AcqLauncher {
	public static void main(String... args) throws HttpException, IOException
	{
		boolean login = false;
		do{
			HttpClient client = new HttpClient();
			
			JFrame f = new JFrame();
			LoginDialog dialog = new LoginDialog(f, "Server Connection Preferences", false);
	        dialog.setVisible(true);
	        if(dialog.isCancelled())
	        	System.exit(0);
	        ConnectionPreferences preferences = ConnectionPreferences.getInstance();
	        
	        Logger logger = Logger.getRootLogger();
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			File logFile = new File(Constants.getConfigDirectory(), "imanage-acqclient-pre-launcher" + dateFormat.format(date)+".log");
			RollingFileAppender fileAppender = new RollingFileAppender(new PatternLayout(), logFile.getAbsolutePath());
			logger.addAppender(fileAppender);
			
			
			
			
			String username = preferences.getLoginName();
			String password = preferences.getPassword();
			String server = preferences.getHostAddress();
			Integer port = preferences.getHostPort();
			Integer protocol_flag = preferences.getProtocol();
			String protocol = new String();
			String proxyusername = preferences.getProxyUser();
			String proxypassword = preferences.getProxyPassword();
			if(protocol_flag == 0)
				protocol = "http";
			else
				protocol = "https";
			String keystore = "jssecacerts";
			if(protocol.equals("https")){
				
				System.setProperty("javax.net.ssl.trustStore",keystore );
				System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
			}
			if(preferences.toUseProxy()){
				HostConfiguration config = client.getHostConfiguration();
		        config.setProxy(preferences.getProxyAddress(), preferences.getProxyPort());
		        if(proxyusername!= null && proxypassword!=null){
		        	Credentials credentials = new UsernamePasswordCredentials(proxyusername, proxypassword);
			        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT);
			        client.getState().setProxyCredentials(authScope, credentials);
		        }
		        
			}
			
			
		    
			PostMethod method = new PostMethod(protocol + "://" + server +":" + port + "/iManage/auth/login");
			method.addParameter("loginUsername", username);
			method.addParameter("loginPassword", password);
			
			
			
			
			PostMethod postMethod = new PostMethod(protocol+"://" + server +":" + port + "/iManage/compute/generateAuthCode");
			postMethod.addParameter("clientID", "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI");
			postMethod.addParameter("services", "[\"AUTHENTICATION\", \"ISPACE\", \"SEARCH\", \"LOADER\", \"UPDATE\", \"MANAGEMENT\", \"COMPUTE\"]");
			postMethod.addParameter("expiryTime", ""+((new Date()).getTime()+(24*3600*1000)));
			
			try{
				client.executeMethod(method);
				String loginResponse = method.getResponseBodyAsString();
				if(loginResponse.equals("success")){
					client.executeMethod(postMethod);
					System.out.println("Success");
				}
				
				else{
					JOptionPane.showMessageDialog(null, loginResponse, "Error", JOptionPane.ERROR_MESSAGE);
					continue;
					//System.exit(0);
				}
				String auth = postMethod.getResponseBodyAsString();	
				String success = auth.split(":")[2].split("}")[0];
				auth = parseString(auth);
				String a[] = {auth, server, Integer.toString(port), "1.23", protocol};
				if(success.equals("true")){
					login = true;
					AcquisitionUI.main(a);
				}
				else{
					JOptionPane.showMessageDialog(null, auth, "Error", JOptionPane.ERROR_MESSAGE);
					continue;
					//System.exit(0);
				}
			}
			catch(SSLException s){
				logger.error("Error",s);
				JOptionPane.showMessageDialog(null, "Error: "+ s.getMessage() + "\nPlease refer to Readme for connection settings and try again.\n", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
				//System.exit(0);
				
			}
			catch(ConnectException e){
				JOptionPane.showMessageDialog(null, e.getMessage()+".\nPlease refer to Readme for connection settings and try again.\n", "Error", JOptionPane.ERROR_MESSAGE);
				logger.error("Error",e);
				continue;
				//System.exit(0);
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(null, e.getMessage()+"."+"\nPlease refer to Readme for connection settings and try again.\n", "Error", JOptionPane.ERROR_MESSAGE);
				logger.error("Error",e);
				continue;
				//System.exit(0);
			}
			
		}while(!login);
		
	
	
	}
	
	private static String parseString(String auth)
	{
		return auth.split(":")[1].split(",")[0].split("\"")[1];
	}
}

