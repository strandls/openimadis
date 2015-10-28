/*
 * SimpleLogFormatter.java
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
package com.strandgenomics.imaging.iengine.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Print a brief summary of the LogRecord in a human readable format.
 * @author arunabha
 */
public class SimpleLogFormatter extends Formatter {

    // To, Minimize memory allocations
    private Date m_currentTime            = new Date();
    private SimpleDateFormat m_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Line separator string.  This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    private String m_lineSeparator = (String) java.security.AccessController.doPrivileged(
               new sun.security.action.GetPropertyAction("line.separator"));

    /**
     * Format the given log record and return the formatted string.
     *
     * @param record the log record to be formatted.
     * @return the formatted log record
     */
    public String format(LogRecord record){

        StringBuffer sb = new StringBuffer();

        sb.append(record.getLevel().getLocalizedName());
        sb.append(" [").append(getFormatedDateString(record)).append(' ');

        String methodName = record.getSourceMethodName();
        String className  = record.getSourceClassName();

        if(methodName != null){
            sb.append(methodName);
        }

        if(className != null){
            if(methodName != null){
                sb.append('@');
            }
            sb.append(className);
        }

        sb.append("]:\t");
        sb.append(formatMessage(record));
        sb.append(m_lineSeparator);

        Throwable ex = record.getThrown();
        if (ex != null) 
        {
            try 
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            }
            catch (Exception dhur) {
            }
        }

        return sb.toString();
    }

    public String getFormatedDateString(LogRecord record)
    {
        synchronized(m_currentTime)
        {
            m_currentTime.setTime(record.getMillis());
            return m_dateFormat.format(m_currentTime);
        }
    }
}