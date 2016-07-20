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

package com.strandgenomics.imaging.iengine.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.mail.EmailNotifier;
import com.strandgenomics.imaging.iengine.mail.MailClient;
import com.strandgenomics.imaging.iengine.mail.Notification;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.User;

public class NotificationMessageManager extends SystemManager {
	NotificationMessageManager() {}

	/**
	 * send the notification message
	 * @param sender
	 * @param receivers receiver names
	 * @param receiverEmails receiver emails
	 * @param attachments attachments to be sent, null if not
	 * @param type type of the notification message
	 * @param args arguments required to generate message body. Check ImagingNotificationMessages_en.properties
	 * @throws DataAccessException
	 */
    public void sendNotificationMessage(String sender, Set<User> receivers, List<File> attachments, NotificationMessageType type, String... args)
	{
        logger.logp(Level.INFO, "AuthorizationManager", "sendNotificationMessage", "entered params :" + sender + "   " + receivers + "   " + type  + "	" + args[0]);
    	
        List<String> emailIds = new ArrayList<String>();
        List<String> receiversList = new ArrayList<String>();
        
		Iterator<User> iter = receivers.iterator();
		while (iter.hasNext())
		{
			User receiver = iter.next();
			emailIds.add(receiver.getEmail());
			receiversList.add(receiver.getName());
		}
		
		try
		{
			createNotificationMessage(sender, emailIds, attachments, type, type.getDescription(sender, receiversList, args), args);
		}
		catch (FileNotFoundException e)
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "failed in sending the notification ", e);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "failed in sending the notification ", e);
		}
		catch (Exception e) // ensuring all the checked exceptions are caught here 
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "failed in sending the notification ", e);
		}
		catch (Throwable e) // this is to ensure that even the Errors are caught and does not crash the underlaying system just because email notifications are failed
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "FATAL ERROR", e);
		}
	}
    
    /**
     * send notification to the specified receivers
     * @param sender
     * @param receivers
     * @param attachments
     * @param type
     * @param args
     */
    public void sendNotificationMessage(String sender, List<String> receivers, List<File> attachments, NotificationMessageType type, String... args)
    {
        logger.logp(Level.INFO, "AuthorizationManager", "sendNotificationMessage", "entered params :" + sender + "   " + receivers + "   " + type  + "	" + args[0]);
    	
		try
		{
			createNotificationMessage(sender, receivers, attachments, type, type.getDescription(sender, receivers, args), args);
		}
		catch (FileNotFoundException e)
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "failed in sending the notification ", e);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "failed in sending the notification ", e);
		}
		catch (Exception e) // ensuring all the checked exceptions are caught here 
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "failed in sending the notification ", e);
		}
		catch (Throwable e) // this is to ensure that even the Errors are caught and does not crash the underlaying system just because email notifications are failed
		{
			logger.logp(Level.WARNING, "NotificationMessageManager", "sendNotificationMessage", "FATAL ERROR", e);
		}
    }
    
	private void createNotificationMessage(String sender, List<String> receivers, List<File> attachments, NotificationMessageType type, String description, String... args) throws Exception
	{
		List<String> NotificationMessageArgs = new ArrayList<String>();
		if (args != null)
		{
			for (String arg : args)
				NotificationMessageArgs.add(arg);
		}

		logger.logp(Level.INFO, "Notification manager", "create notification message", "successfully created mailclient");

		Notification message = new Notification();
		message.setMessageSubject(type.toString());
		message.setMessageBody(description);
		if(attachments!=null && !attachments.isEmpty())
			message.setAttchments(attachments);

		for(String email : receivers)
			message.addReceiver(email);

		File mailConfigFile = new File(Constants.getStringProperty(Constants.Property.MAIL_STORE, null));
		if (mailConfigFile != null && mailConfigFile.isFile())
		{
			MailClient mc = new MailClient(mailConfigFile);
			logger.logp(Level.INFO, "Notification manager", "create notification message", "created mail client");
			
			EmailNotifier emailer = EmailNotifier.getInstance();
			logger.logp(Level.INFO, "Notification manager", "create notification message", "created email notifier");
			
			emailer.registerSender(mc);
			logger.logp(Level.INFO, "Notification manager", "create notification message", "registered sender");

			emailer.sendNotification(message);
			logger.logp(Level.INFO, "Notification manager", "create notification message", "sent notification message");
			
		}
	}
	
}
