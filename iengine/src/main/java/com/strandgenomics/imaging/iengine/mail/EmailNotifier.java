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
 * EmailNotifier.java
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

import java.net.InetAddress;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The singleton instance of this class is reponsible for sending email
 * notification for events associated with Enterprise Adminitration
 */
public class EmailNotifier {
    
    public static class Event {
        
        public static final String USER_CREATION         = "USER_CREATION";
        public static final String USER_DELETION         = "USER_DELETION";
        
        public static final String USER_PASSWORD_CHANGED = "PASSWORD_CHANGED";
        
        public static final String USER_ACTIVATION       = "USER_ACTIVATION";
        public static final String USER_SUSPENSION       = "USER_SUSPENSION";
        
        public static final String GROUP_ADMIN_CREATION  = "GROUP_ADMIN_CREATION";
        public static final String GROUP_ADMIN_DELETION  = "GROUP_ADMIN_DELETION";
     
        public static final String USER_ADDED_TO_GROUP     = "USER_ADDED_TO_GROUP";
        public static final String USER_REMOVED_FROM_GROUP = "USER_REMOVED_FROM_GROUP";
    }
    
    public static final String MAIL_MESSAGE_RESOURCE_NAME    = "ImagingNotificationMessages";
    public static final ResourceBundle MAIL_MESSAGE_RESOURCE = ResourceBundle.getBundle(MAIL_MESSAGE_RESOURCE_NAME, Locale.getDefault());

    /** single instance of NotificationManager */
    private static EmailNotifier singletonInstance = null ;

    /**
     * A single instance of ProductManager is shared by all
     */
   public static final EmailNotifier getInstance(){
        if(singletonInstance == null){
            synchronized(EmailNotifier.class){
                if(singletonInstance == null){
                    singletonInstance = new EmailNotifier();             
                }
            }
        }
        return singletonInstance;
    }

    private Logger m_logger = null;
    private NotificationManager m_manager = null;
    
    /**
     * Creates a Config instance
     * Typically the server should be initialized by calling this method
     */
    private EmailNotifier(){
        m_logger  = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
        m_manager = new NotificationManager();
        m_manager.start();
    }

    public void registerSender(NotificationSender sender){
        m_logger.logp(Level.INFO, "EmailNotifier", "registerSender", "registering notification sender "+sender);
        m_manager.addNotificationSender(sender);
    }
    
    public void sendNotification(Notification notification){
    	m_logger.logp(Level.INFO, "EmailNotifier", "registerSender", "sending notification "+notification.getBody());
        m_manager.sendNotification(notification);
    }
    
    protected String getMessageSubject(String eventID)
    {  
        String messageID = eventID+".subject";
        return MAIL_MESSAGE_RESOURCE.getString(messageID);      
    }
    
    protected static String getLocalHost() 
    {
        String localhost = "127.0.0.1";
        try {
            InetAddress in = InetAddress.getLocalHost();
            InetAddress[] all = InetAddress.getAllByName(in.getHostName());
            
            for (int i = 0; i < all.length; i++) {
                localhost = all[i].getHostAddress();
                if (!localhost.equals("127.0.0.1")) {
                    break;
                }
            }
        } catch (Exception ex) {
            localhost = "127.0.0.1";
        }
        return localhost;
    }
}
