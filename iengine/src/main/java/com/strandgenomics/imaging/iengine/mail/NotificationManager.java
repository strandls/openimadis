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
