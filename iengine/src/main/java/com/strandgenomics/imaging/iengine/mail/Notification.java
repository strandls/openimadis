/*
 * Notification.java
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An email notification
 * @author ysrikanth@strandls.com
 *
 */
public class Notification {
    
    protected Set<String> m_receivers  = null; 
    protected String m_messageSubject  = null; 
    protected String m_messageBody     = null;
    protected final long m_timeStamp;
    /**
     * attachments if any to be sent
     */
    protected List<File> attachments = null;
 
    public Notification(){
        m_timeStamp = System.currentTimeMillis();
        m_receivers = new HashSet<String>();
    }
    
    public void addReceiver(String receiver){
        if(receiver != null) {
            m_receivers.add(receiver);
        }
    }
    
    public void setMessageBody(String messageBody){     
        m_messageBody = messageBody;
    }
    
    public void setMessageSubject(String messageSubject){      
        m_messageSubject = messageSubject == null ? "No Subject" : messageSubject;   
    }
    
    /**
     * returns list of attachments, if any
     * @return list of attachments
     */
    public List<File> getAttachments()
    {
    	return this.attachments;
    }
    
    /**
     * set the attachments to be sent
     * @param attachments list of file to be attached
     */
    public void setAttchments(List<File> attachments)
    {
    	this.attachments = attachments;
    }

    /**
     * Get the notification timestamp.
     */
    public long getTimeStamp(){
        return m_timeStamp;
    }
    
    public String getBody(){
        return m_messageBody;
    }
    
    public String getSubject(){
        return m_messageSubject;
    } 
    
    public String[] getReceivers(){
        return m_receivers.isEmpty() ? null : m_receivers.toArray(new String[0]);
    }
}
