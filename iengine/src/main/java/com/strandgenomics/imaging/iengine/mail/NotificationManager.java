/*
 * NotificationManager.java
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mail sender
 * @author ysrikanth@strandls.com
 *
 */
public class NotificationManager extends Thread {
    
    /** list of pending notifications */
    private LinkedList<Notification> m_messageQ = null;
    /** client that will send the mail for us */
    private Set<NotificationSender> m_clients = null;

    private Logger m_logger   = null;
    
    /**
     * Creates a Config instance
     * Typically the server should be initialized by calling this method
     */
    public NotificationManager(){
        m_messageQ = new LinkedList<Notification>();
        m_clients  = new HashSet<NotificationSender>();
        
        m_logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
        m_logger.logp(Level.INFO, "NotificationManager", "init", "creating Notification Manager instance ");
    }
    
    public void addNotificationSender(NotificationSender consumer){
    	   m_logger.logp(Level.INFO, "NotificationManager", "addNotificationSender", " Notificationsenders are : " + m_clients.size());
        if(consumer != null){
            m_clients.add(consumer);
        }
    }
    
	public void sendNotification(Notification message)
	{
		if (message != null)
		{
			m_logger.logp(Level.INFO, "NotificationManager", "run", "enqueuing notification message "+ message.getBody());
			boolean wasEmpty = m_messageQ.isEmpty();
			m_messageQ.add(message);

			if (wasEmpty)
			{
				synchronized (m_messageQ)
				{
					m_messageQ.notify();
				}
			}
		}
	}
    
	public void run()
	{
		while (true)
		{
			try
			{
				if (m_messageQ.isEmpty())
				{
					synchronized (m_messageQ)
					{
						m_messageQ.wait();
					}
				}
				else
				{
					Notification message = m_messageQ.remove();
					m_logger.logp(Level.INFO, "NotificationManager", "run", " Notificationsenders are : " + m_clients.size());
					for (NotificationSender consumer : m_clients)
					{

						m_logger.logp(Level.INFO, "NotificationManager", "run", " Notificationsender : " + consumer.toString());
						m_clients.remove(consumer);
						m_logger.logp(Level.FINE, "NotificationManager", "run", " Notificationsender : " + consumer.toString());

						consumer.consume(message);

					}
				}
			}
			catch (Exception ex)
			{
				m_logger.logp(Level.INFO, "NotificationManager", "run", "waiting for message to mail", ex);
			}
		}
	}
}
