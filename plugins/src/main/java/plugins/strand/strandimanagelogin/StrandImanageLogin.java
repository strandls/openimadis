/*
 * StrandImanageLogin.java
 *
 * AVADIS Image Management System
 * ICY Plugin for interacting with Avadis iManage
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package plugins.strand.strandimanagelogin;

import icy.gui.dialog.MessageDialog;
import icy.plugin.abstract_.Plugin;
import icy.plugin.interface_.PluginImageAnalysis;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.strandgenomics.imaging.iclient.AuthenticationException;
import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;

/**
 * ICY plugin to login to Avadis iManage. Once logged in the other plugins for
 * interacting with Avadis iManage can be used for that session.
 * 
 * @author Anup Kulkarni
 */
public class StrandImanageLogin extends Plugin implements PluginImageAnalysis {

	@Override
	public void compute() {
		final DatabaseLoginDialog pd = new DatabaseLoginDialog();
		Object[] options = {"Login", "Request Account"};
		int value = JOptionPane.showOptionDialog(null, pd, "Strand Avadis iManage Login", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		switch (value)
		{
			case 0:
				login(pd);
				break;
			case 1:
				register();
			default:
				break;
		}
	}
	
	public void register()
	{
		int accept = JOptionPane.showConfirmDialog(null, "Click 'OK' to get trial account on Strand's demo server. For other servers kindly contact respective administrator.", "User Registration", JOptionPane.OK_CANCEL_OPTION);
		if(accept != JOptionPane.OK_OPTION)
			return;
		
		RegisterDialog registerDialog = new RegisterDialog();
		int value = JOptionPane.showConfirmDialog(null, registerDialog, "User Registration", JOptionPane.OK_CANCEL_OPTION);
		if(value == JOptionPane.OK_OPTION)
		{
			if(!registerDialog.isConsistent())
				return;
			
			UserInformation userInfo = registerDialog.getUserInformation();
			
			try
			{
				registerUser(userInfo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "User Registration Failed", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void registerUser(UserInformation userInfo) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://demo.avadis-img.com/registration/rest/userinfo/submit");
		method.addParameter("login", userInfo.getLogin());
		method.addParameter("password", userInfo.getPassword());
		method.addParameter("fullName",userInfo.getFullName());
		method.addParameter("workProfile", userInfo.getWorkProfile().name());
		method.addParameter("email",userInfo.getEmail());
		method.addParameter("fascility",userInfo.getFascilityName());
		method.addParameter("size",userInfo.getSizeOfFascility().name());
		method.addParameter("data",String.valueOf(userInfo.getDataSizePerYear()));

		int ret = client.executeMethod(method);
		if(ret/100 == 2)
		{
			JOptionPane.showMessageDialog(null, "Activation link is sent to your registered email address. Kindly click on the link to complete the registration process.");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "User registration failed. Contact imanage@strandls.com for requesting an account", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void login(final DatabaseLoginDialog pd)
	{
		// Login happens in separate thread making gui thread free
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ImageSpace ispace = ImageSpaceObject.getConnectionManager();
				boolean login = false;

				try
				{
					login = ispace.login(pd.isSSL(), pd.getServerName(), pd.getPort(), pd.getAppId(), pd.getAuthCode());

					if (login)
					{
						// MessageDialog.showDialog("Login successful! You can now use other plugins for Strand Avadis iManage.");

						new BrowseFrame();
					}
					else
					{
						MessageDialog.showDialog("Login failed");
					}
				}
				catch (AuthenticationException e)
				{
					MessageDialog.showDialog("Please provide a valid Authentication code");
				}
				catch (Exception e)
				{

					MessageDialog.showDialog("Error occured while connecting to server");
				}
			}
		});
//		ThreadUtil.invoke(t, false);
		SwingUtilities.invokeLater(t);
	}

	
	private class DatabaseLoginDialog extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8558163232727109005L;
		
		/**
		 * application id is the unique identifier for the iManage client
		 */
		private JTextField applicationId;
		/**
		 * authentication code that will identify a user for current session
		 */
		private JTextField authenticationCode;
		/**
		 * hostname where the Avadis iManage is running
		 */
		private JTextField databaseServerName;
		/**
		 * port where the Avadis iManage is running
		 */
		private JTextField databaseServerPort;
		/**
		 * whether ssl is used for connection (http or https)
		 */
		private JComboBox isSSL;
		/**
		 * available servers
		 */
		private JComboBox server;
		private String[] serverValues = {"cid.curie.fr", "demo.avadis-img.com", "localhost"};
		
		private String appIds[] = {"QbzHG37KbGrdQvZ103mjzeA867UoWgPzdTDn31nu", "5zkcZhmObF13WqOQqObADUMsb9fjVWx8RotjpHRc", "PlUBfYR3XKuI0DNf0ejoDjkMwQ2V8W7QniuqPFFu"};

		private JTextField userLoginField;

		private JPasswordField userPasswordField;

		public DatabaseLoginDialog()
		{
			setLayout(new GridLayout(4, 2, 5, 5));

			setupUI();
		}

		private void setupUI()
		{
			final JLabel userLoginLabel = new JLabel("User Login");
			userLoginLabel.setVisible(false);
			userLoginField = new JTextField();
			userLoginField.setVisible(false);
			
			final JLabel userPasswordLabel = new JLabel("Password");
			userPasswordLabel.setVisible(false);
			userPasswordField = new JPasswordField();
			userPasswordField.setVisible(false);
			
			final JLabel authCodeLabel = new JLabel("Authentication Code");
			authenticationCode = new JTextField();
			
			JLabel serverURL = new JLabel("Choose Server");
			server = new JComboBox(serverValues);
			server.setSelectedIndex(0);
			server.addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(server.getSelectedIndex()==1)
					{
						userLoginLabel.setVisible(true);
						userLoginField.setVisible(true);
						userPasswordLabel.setVisible(true);
						userPasswordField.setVisible(true);
						
						authenticationCode.setVisible(false);
						authCodeLabel.setVisible(false);
						repaint();
					}
					else
					{
						userLoginLabel.setVisible(false);
						userLoginField.setVisible(false);
						userPasswordLabel.setVisible(false);
						userPasswordField.setVisible(false);
						
						authenticationCode.setVisible(true);
						authCodeLabel.setVisible(true);
						repaint();
					}
				}
			});
			
			add(serverURL);
			add(server);
			
			add(authCodeLabel);
			add(authenticationCode);
			
			add(userLoginLabel);
			add(userLoginField);
			
			add(userPasswordLabel);
			add(userPasswordField);
		}

		/**
		 * returns server hostname
		 * @return server hostname
		 */
		public String getServerName()
		{
			int index = this.server.getSelectedIndex();
			return this.serverValues[index];
		}

		/**
		 * returns the client id
		 * @return the client id
		 */
		public String getAppId()
		{
			int index = this.server.getSelectedIndex();
			return this.appIds[index];
		}

		/**
		 * returns the authentication code
		 * @return the authentication code
		 * @throws IOException 
		 * @throws HttpException 
		 */
		public String getAuthCode() throws HttpException, IOException
		{
			if(this.server.getSelectedIndex()==1)
			{
				HttpClient client = new HttpClient();
				
				PostMethod method = new PostMethod("http://demo.avadis-img.com:8080/iManage/auth/login");
				method.addParameter("loginUsername", this.userLoginField.getText());
				method.addParameter("loginPassword", this.userPasswordField.getText());
				client.executeMethod(method);
				
				PostMethod postMethod = new PostMethod("http://demo.avadis-img.com:8080/iManage/compute/generateAuthCode");
				postMethod.addParameter("clientID", getAppId());
				postMethod.addParameter("services", "[\"AUTHENTICATION\", \"ISPACE\", \"SEARCH\", \"LOADER\", \"UPDATE\", \"MANAGEMENT\", \"COMPUTE\"]");
				postMethod.addParameter("expiryTime", ""+((new Date()).getTime()+(24*3600*1000)));
				
				client.executeMethod(postMethod);
				
				String auth = postMethod.getResponseBodyAsString();
		
				auth = parseString(auth);
				
				return auth;
			}
			return this.authenticationCode.getText();
		}
		
		private String parseString(String auth)
		{
			return auth.split(":")[1].split(",")[0].split("\"")[1];
		}

		/**
		 * returns the port on which iManage server is running
		 * @return the port on which iManage server is running
		 */
		public int getPort()
		{
			int index = this.server.getSelectedIndex();
			if(index==0) return 443;
			else return 8080;
		}

		/**
		 * returns true if ssl is used for connection(https), false otherwise(http)
		 * @return true if ssl is used for connection(https), false otherwise(http)
		 */
		public boolean isSSL()
		{
			int index = this.server.getSelectedIndex();
			if(index==0) return true;
			else return false;
		}
	}
}

