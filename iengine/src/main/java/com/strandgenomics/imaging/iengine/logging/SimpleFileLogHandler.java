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

package com.strandgenomics.imaging.iengine.logging;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * Default log handler
 * @author arunabha
 *
 */
public class SimpleFileLogHandler extends StreamHandler {

    private SimpleDateFormat m_dateFormat = null;

    /**
     * the folder where log files will be written 
     */
    protected File m_rootFolder = null;
    /**
     * name of log files
     */
    protected String m_filename = null;
    /**
     * extension of the log files
     */
    protected String m_fileExtn = null;
    protected String m_prefix = null;

    protected final long m_interval;
    protected final long m_maxFileSize;

    private long m_endTime;
    private MeteredStream m_meterStream;

    private SimpleLogFormatter m_logFormatter;

    public SimpleFileLogHandler(File rootFolder, String basicName, String extn, int days, long maxSize)
            throws IOException {

        configure();

        if (basicName == null || extn == null) {
            throw new NullPointerException("null filename name or extension");
        }
        if (days <= 0) {
            throw new IllegalArgumentException("only positve intervals are allowed " + days);
        }

        if (maxSize <= 0) {
            throw new IllegalArgumentException("only positive max size is acceptable " + maxSize);
        }

        m_rootFolder = rootFolder.getCanonicalFile();
        m_rootFolder.mkdirs();

        m_filename = basicName;
        m_fileExtn = extn;
        
        m_prefix = getPrefix();

        m_interval = days * 24 * 60 * 60 * 1000L; // numbers of milli seconds in
                                                  // a day;
        m_maxFileSize = maxSize;

        if (!m_fileExtn.startsWith(".")) {
            m_fileExtn = "." + m_fileExtn;
        }

        m_dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        long currentTime = System.currentTimeMillis();
        m_endTime = computeEndTime(currentTime);

       	openNewLogStream(currentTime);
    }

    /**
     * @return
     */
    private String getPrefix() {
        if(m_filename == null)
            return null;
        String[] parts = m_filename.split("_");
        if(parts.length == 2)
            return parts[1].toUpperCase();
        return m_filename.toUpperCase();
    }

    /**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object, which
     * initialized the <tt>LogRecord</tt> and forwarded it here.
     * <p>
     * The <tt>Handler</tt> is responsible for formatting the message, when and
     * if necessary. The formatting should include localization.
     * 
     * @param record
     *            description of the log event
     */
    public void publish(LogRecord record) {

        if (!isLoggable(record)) {
            return;
        }

        // check whether there is any need to change the output log file
        synchronized (this) {

            final long currentTime = System.currentTimeMillis();

            if (m_meterStream.written >= m_maxFileSize || currentTime >= m_endTime) {

                AccessController.doPrivileged(new PrivilegedAction<Void>() 
                {
                    public Void run() 
                    {
                        rotate(currentTime);
                        return null;
                    }
                });

                // reset the end time
                if (currentTime >= m_endTime) 
                {
                    m_endTime = computeEndTime(currentTime);
                }
            }
        }

        super.publish(record); //publish now
        flush();
    }

    // Rotate the set of output files
    private final synchronized void rotate(long currentTime) {

        Level oldLevel = getLevel();
        // switch it off for the time being
        setLevel(Level.OFF);
        // close the existing opened stream
        close();

        try 
        {
            openNewLogStream(currentTime);
        } 
        catch (IOException ex) 
        {
            // We don't want to throw an exception here, but we
            // report the exception to any registered ErrorManager.
            reportError(null, ex, ErrorManager.OPEN_FAILURE);
        }

        // set the log level back
        setLevel(oldLevel);
    }

    private void configure() 
    {
        setLevel(Level.ALL);
        setFilter(null);
        
        m_logFormatter = new SimpleLogFormatter();
        setFormatter(m_logFormatter);
        
        try 
        {
            setEncoding(null);
        } 
        catch (Exception ex) 
        {}
    }

    private final long computeEndTime(long currentTime) 
    {
        // start time is assumed at 00:00:00 this date
        // interval is calculated from this time
        try 
        {
            long startTime = (m_dateFormat.parse(m_dateFormat.format(new Date(currentTime)))).getTime();
            // System.out.println("startTime="+startTime);
            long endTime = startTime + m_interval;
            // System.out.println("endTime="+endTime);
            return endTime;
        } 
        catch (ParseException ex) 
        {
            System.err.println(ex);
            return Long.MAX_VALUE;
        }
    }

    private void openNewLogStream(long currentTime) throws IOException 
    {
        // get the next log file
        File logFile = getNextLogFile(currentTime);
        System.out.println("[SimpleFileLogHandler]:\topening new log file "+logFile);
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(logFile));
        m_meterStream = new MeteredStream(bout);
        setOutputStream(m_meterStream);
    }

    private File getNextLogFile(long currentTime) 
    {
        File logFile = null;
        for (int counter = 0;; ++counter) 
        {
            String filename = generateLogFilename(currentTime, counter);
            logFile = new File(m_rootFolder, filename);
            if (!logFile.isFile()) // if this file exit
            {
                break;
            }
        }
        return logFile;
    }

    private String generateLogFilename(long currentTime, int counter) {

        Date currentDate = new Date(currentTime);
        StringBuffer buffer = new StringBuffer();

        buffer.append(m_filename);
        buffer.append('_');
        buffer.append(m_dateFormat.format(currentDate));

        if (counter > 0) 
        {
            buffer.append('.');
            buffer.append(counter);
        }

        buffer.append(m_fileExtn);
        return buffer.toString();
    }

    // A metered stream is a subclass of OutputStream that
    // (a) forwards all its output to a target stream
    // (b) keeps track of how many bytes have been written
    private class MeteredStream extends OutputStream {

        OutputStream out;
        int written;

        MeteredStream(OutputStream out) 
        {
            this.out = out;
            this.written = 0;
        }

        public void write(int b) throws IOException 
        {
            out.write(b);
            written++;
        }

        public void write(byte buff[]) throws IOException 
        {
            out.write(buff);
            written += buff.length;
        }

        public void write(byte buff[], int off, int len) throws IOException 
        {
            out.write(buff, off, len);
            written += len;
        }

        public void flush() throws IOException 
        {
            out.flush();
        }

        public void close() throws IOException 
        {
            out.close();
        }
    }
}
