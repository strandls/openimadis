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

/*
 * MailClient.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iengine.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Simple mail client class for sending a message to a single recipient The
 * configuration for connecting to the mail server and sending mail is loaded
 * from a file
 * 
 * @author ysrikanth@strandls.com
 */
public class MailClient implements NotificationSender {

	private Logger m_logger = null;
	/** mail configuration */
	private Properties m_mailConfig = null;
	/** session variable */
	private Session m_session = null;
	/** sender of the email (system email address) */
	private InternetAddress m_sender = null;

	/**
	 * Constructor for MailClient throws FileNotFoundException if the mail
	 * configuration file is not found
	 */
	public MailClient(File configFile)
	{
		m_mailConfig = new Properties();
		m_logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		// loading configuration from a properties file
		try
		{
			m_mailConfig.load(new FileInputStream(configFile));
		}
		catch (Exception e)
		{
			m_logger.logp(Level.WARNING, "MailClient", "constructor", "error creating mail configuration", e);
			e.printStackTrace();
		}
		m_logger.logp(Level.INFO, "MailClient", "constructor", "successfully got the files");
	}

	public MailClient() throws FileNotFoundException, IOException
	{
		m_mailConfig = new Properties();
		m_logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		// loading configuration from a properties file
		try
		{
			ClassLoader myCL = MailClient.class.getClassLoader();
			m_mailConfig.load(myCL.getResourceAsStream("mailClientTest.properties"));
		}
		catch (IOException e)
		{
			m_logger.logp(Level.WARNING, "MailClient", "MailClient", "Properties file not found", e);
		}

		m_logger.logp(Level.INFO, "MailClient", "constructor", "successfully got the files");
	}

	public void consume(Notification message)
	{
		try
		{
			sendMail(message);
		}
		catch (Exception ex)
		{
			m_logger.logp(Level.INFO, "MailClient", "sendMail", "failure", ex);
		}
	}

	/**
	 * Sends mails
	 */
	public void sendMail(Notification notify)
	{

		m_logger.logp(Level.INFO, "MailClient", "sendMail", "about to send mail");

		String[] receivers = notify.getReceivers();
		if (receivers != null)
		{
			for (String receiver : receivers)
			{
				try
				{
					MimeMessage message = new MimeMessage(getSession());
					message.setFrom(getSender());
					message.setSubject(notify.getSubject());
					// message.setText(notify.getBody());
					message.setReplyTo(null);
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

					// create multipart message
					Multipart multipart = new MimeMultipart();

					// message body
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart.setText(notify.getBody());

					multipart.addBodyPart(messageBodyPart);

					// message attachments if any
					if (notify.attachments != null)
					{
						m_logger.logp(Level.INFO, "MailClient", "sendMail", "message has attachments " + notify.attachments.size());
						for (File attachment : notify.attachments)
						{
							m_logger.logp(Level.INFO, "MailClient", "sendMail", "attaching " + attachment.getName());

							BodyPart messageAttachmentPart = new MimeBodyPart();

							DataSource source = new FileDataSource(attachment.getAbsolutePath());
							messageAttachmentPart.setDataHandler(new DataHandler(source));
							String filename = attachment.getName();
							messageAttachmentPart.setFileName(filename);

							multipart.addBodyPart(messageAttachmentPart);
						}
					}

					// set multipart as message contents
					message.setContent(multipart);

					m_logger.logp(Level.INFO, "MailClient", "sendMail", "successfully created message wid receiver : " + receiver);
					Transport t = m_session.getTransport(m_mailConfig.getProperty("mail.transport.protocol"));
					try
					{
						t.connect(m_mailConfig.getProperty("mail.smtp.host"), m_mailConfig.getProperty("mail.user"), m_mailConfig.getProperty("mail.password"));
						t.sendMessage(message, message.getAllRecipients());
					}
					finally
					{
						t.close();
					}

				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

		m_logger.logp(Level.INFO, "MailClient", "sendMail", "successfully send mail");
	}

	protected Session getSession()
	{

		if (m_session == null)
		{

			m_session = Session.getInstance(m_mailConfig, getAuthenticator());

			if (m_mailConfig.getProperty("mail.debug").equals("true"))
			{
				m_session.setDebug(true);
			}
		}

		return m_session;

	}

	public InternetAddress getSender() throws AddressException
	{
		if (m_sender == null)
		{
			String a = m_mailConfig.getProperty("mail.sender");
			m_sender = new InternetAddress(a);
		}
		return m_sender;
	}

	protected Authenticator getAuthenticator()
	{

		Authenticator auth = null;

		if (m_mailConfig.getProperty("mail.smtps.auth").equals("true"))
		{

			final String username = m_mailConfig.getProperty("mail.user");
			final String password = m_mailConfig.getProperty("mail.password");

			auth = new Authenticator()
			{
				public PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(username, password);
				}
			};
		}

		return auth;
	}

	public static void main(String... args)
	{
		try
		{
			// File configFile = new File(args[0]);

			MailClient mailer = new MailClient();

			Notification message = new Notification();
			message.setMessageSubject("Test Mail");
			message.setMessageBody("Please Ignore");
			message.addReceiver("abishekahluwalia@gmail.com");

			mailer.sendMail(message);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}