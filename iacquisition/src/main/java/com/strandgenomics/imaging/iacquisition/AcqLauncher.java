package com.strandgenomics.imaging.iacquisition;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.net.ssl.SSLException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

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
import com.strandgenomics.imaging.iclient.util.LoadCert;
import com.strandgenomics.imaging.iclient.util.ConnectionPreferences;
import com.strandgenomics.imaging.icore.Constants;

/**
 * For testing AcquisitionUI
 */
public class AcqLauncher {
	public static void main(String... args) throws HttpException, IOException
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
			String url = preferences.getHostAddress();
			String server = null;
			String protocol = new String();
			//String server = preferences.getHostAddress();
			URI uri = null;
			try {
				uri = new URI(url);
				String domain = uri.getHost();
				if(domain!=null){
					server = domain.startsWith("www.") ? domain.substring(4) : domain;
					if(url.startsWith("https")){
						protocol = "https";
					} 
					else if(url.startsWith("http")){
						protocol = "http";
					}
				}
				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			Integer port = preferences.getHostPort();
			
			String proxyHost = null;
			String proxyusername = null;
			String proxypassword = null;
			int proxyPort = 0;
			if(preferences.toUseProxy()){
				proxyHost = preferences.getProxyAddress();
				proxyPort = preferences.getProxyPort();
				proxyusername = preferences.getProxyUser();
				proxypassword = preferences.getProxyPassword();
			}
			
			if(preferences.toUseProxy()){
				HostConfiguration config = client.getHostConfiguration();
		        config.setProxy(preferences.getProxyAddress(), preferences.getProxyPort());
		        if(proxyusername!= null && proxypassword!=null){
		        	Credentials credentials = new UsernamePasswordCredentials(proxyusername, proxypassword);
			        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT);
			        client.getState().setProxyCredentials(authScope, credentials);
			        client.getParams().setAuthenticationPreemptive(true);
		        }
			}
			if(protocol.equals("https")){
				String path = null;
				try {
					path = LoadCert.loadCert(server, port,proxyHost,proxyPort,proxyusername,proxypassword);
					System.out.println(path);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(path!=null){
					System.setProperty("javax.net.ssl.trustStore",path );
					System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
				}
				
			}
			
			
			PostMethod method = new PostMethod(protocol + "://" + server +":" + port + "/iManage/auth/login");
			method.addParameter("loginUsername", username);
			method.addParameter("loginPassword", password);
			
			
			
			
			PostMethod postMethod = new PostMethod(protocol+"://" + server +":" + port + "/iManage/compute/generateAuthCode");
			postMethod.addParameter("clientID", "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI");
			postMethod.addParameter("services", "[\"AUTHENTICATION\", \"ISPACE\", \"SEARCH\", \"LOADER\", \"UPDATE\", \"MANAGEMENT\", \"COMPUTE\"]");
			postMethod.addParameter("expiryTime", ""+((new Date()).getTime()+(24*3600*1000)));
			System.out.println("Connecting to server...");
			
			
			
			JFrame frame = new JFrame();    // progress monitor
	        JPanel panel = new JPanel();
	        JLabel label = new JLabel("Connecting to server...");
	        JProgressBar jpb = new JProgressBar();
	        jpb.setIndeterminate(true);
	        panel.add(label);
	        panel.add(jpb);
	        frame.add(panel);
	        frame.pack();
	        frame.setSize(200,90);
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
			try{
				client.executeMethod(method);
				String loginResponse = method.getResponseBodyAsString();
				if(loginResponse.equals("success")){
					client.executeMethod(postMethod);
				}
				
				else{
					JOptionPane.showMessageDialog(null, loginResponse, "Error", JOptionPane.ERROR_MESSAGE);
					continue;
					//System.exit(0);
				}
				String auth = postMethod.getResponseBodyAsString();	
				String success = auth.split(":")[2].split("}")[0];
				auth = parseString(auth);
				String a[] = {auth, server, Integer.toString(port), getVersion(), protocol};
				if(success.equals("true")){
					System.out.println("Auth code generated");
					login = true;
					frame.setVisible(false);
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
				if(port==443 && protocol.equalsIgnoreCase("http")){
					JOptionPane.showMessageDialog(null, "\nIncorrect Protocol.Please change it to HTTPS.\n", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				JOptionPane.showMessageDialog(null, e.getMessage()+"."+"\nPlease refer to Readme for connection settings and try again.\n", "Error", JOptionPane.ERROR_MESSAGE);
				logger.error("Error",e);
				continue;
				//System.exit(0);
			}
			finally{
				 frame.setVisible(false);
			}
			
		}while(!login);
		
	
	
	}

	private static String getVersion(){
		return getVersion("config.properties");
	}

	private static String getVersion(String propertiesFileName){
		Properties prop = new Properties();
		InputStream inp = AcqLauncher.class.getClassLoader().getResourceAsStream(propertiesFileName);

		if (inp != null){
		    try{
		        prop.load(inp);
		    }catch(IOException e){
		        e.printStackTrace();
		    }	
		}

		String version = prop.getProperty("acq.launcher.version");
		
		if (version == null){
		    return "__VERSION__";
		}

		return version;
	}
		
	private static String parseString(String auth)
	{
		return auth.split(":")[1].split(",")[0].split("\"")[1];
	}
}

