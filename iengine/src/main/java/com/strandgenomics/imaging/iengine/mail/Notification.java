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
