/*
 * LogManager.java
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;

/**
 * manages the logging system
 * @author arunabha
 *
 */
public class LogManager {

    //singleton instance
    private static LogManager singleTon = null;
    private static final Object padLock = new Object();

    public static LogManager getInstance()
    {
        if(singleTon == null)
        {
            synchronized(padLock)
            {
                if(singleTon == null)
                {
                	LogManager temp = new LogManager();
                	singleTon = temp;
                }
            }
        }

        return singleTon;
    }
    
    private boolean isInitialized = false;
    
    /**
     * Create an initialize the application logger
     */
    public void initializeLogger
    (
            File rootLogFolder, 
            String basicName, 
            String extn,
            Level logLevel,
            String logScope, 
            long maxFileSize, 
            int dayIntervals) throws IOException {
        
        if(isInitialized){
            return;
        }
        
        synchronized(this){
            
            if(isInitialized){
                return;
            }

            if(logScope == null){
                logScope = "com.strandgenomics";
            }
            
            if(basicName == null){
                basicName = "avadis_img_log";
            }
            
            if(extn == null){
                extn = ".txt";
            }
            
            if(rootLogFolder == null){
                File userDir = new File(System.getProperty("user.dir")).getAbsoluteFile();
                rootLogFolder = new File(userDir, "logs");
            }
            
            rootLogFolder = rootLogFolder.getAbsoluteFile();
            
            if(rootLogFolder.isDirectory()){
                //create the log folder if not there already
                rootLogFolder.mkdirs();
            }
            
            if(logLevel == null){
                logLevel = Level.INFO;
            }
            
            //create the log handler
            Handler logHandler = new SimpleFileLogHandler(rootLogFolder, basicName, extn, dayIntervals, maxFileSize);
            //set the formatter
            logHandler.setFormatter(new SimpleLogFormatter());
            // Request that every detail gets logged.
            logHandler.setLevel(logLevel);

            //create the logger if not already there
            Logger logger = Logger.getLogger(logScope);
            // Request that every detail gets logged below this level
            logger.setLevel(logLevel);
            // Send logger output to our FileHandler.
            logger.addHandler(logHandler);

            //don't broadcast
            logger.setUseParentHandlers(false);

            isInitialized = true;
        }
    }
    
    public  void initializeLogger()
    {
        try 
        {
            File rootLogFolder = Constants.getLogDirectory();
            
            long maxFileSize   = Constants.getMaximumLogSize();
            int dayIntervals   = Constants.getLogInterval();
            
            Level logLevel     = Constants.getLogLevel();
            String logScope    = Constants.getLogScope();
            
            String filename    = Constants.getLogFilename();
            
            String basicName   = null;
            String extn        = null;
            
            if(filename != null){
                int indexOfDot = filename.lastIndexOf('.');
                if(indexOfDot > 0 && indexOfDot < filename.length() - 1){
                    basicName   = filename.substring(0, indexOfDot);
                    extn        = filename.substring(indexOfDot+1);
                }
                else {
                    basicName = filename;
                    extn      = ".log";
                }
            }
        
           initializeLogger(rootLogFolder, basicName, extn, logLevel, logScope, maxFileSize, dayIntervals);
        }
        catch(Exception ex)
        {
            System.err.println("error setting up logger "+ex);
        }
    }
}

