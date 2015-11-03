/*
 * Indexer.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.iclient.local;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import loci.formats.ImageReader;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.iclient.ImageSpaceException;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.IRecordObserver;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.IValidator;

/**
 * Indexes a folder
 * @author arunabha
 */
public class Indexer implements IRecordObserver {
	
	/**
	 * Logger
	 */
	protected Logger logger = Logger.getRootLogger();
	/**
	 * the temporary directory to write to
	 */
	public static final File TEMP_DIR = createTempDir();
    /**
     * List of listeners listening for record addition events
     */
	protected EventListenerList listenerList = new EventListenerList();
    /**
     * the worker doing the index
     */
    protected List<IndexWorker> workerList = null;
    /**
     * checks whether to stop indexing 
     */
    protected boolean stop = false;
	/**
	 * compute md5 checksum of the source files
	 */
	protected boolean computeSignature;


    /**
     * Creates an Indexer instance to be used to index any set of directories
     */
    public Indexer()
    {
    	workerList = new ArrayList<IndexWorker>();
    	computeSignature = true;
    }
    
    /**
     * Starts indexing on the specific root directory
     * @param rootFile the root folder to index
     * @param recursive checks whether to index sub folders also 
     */
    public void startIndexing(final File rootFolder, final boolean recursive, IValidator validator)
    {
    	this.stop = false;
    	IndexWorker worker = new IndexWorker(rootFolder, recursive, validator);
        worker.start();
        workerList.add(worker);
    }
    
    public void stopIndexing()
    {
    	logger.info("[Indexer]:\tStopping indexer...");
    	
    	this.stop = true;
    	for(IndexWorker worker : workerList)
    	{
    		worker.interrupt();
    		worker = null;
    	}
    	
    	workerList.clear();
    }
    
	/**
	 * check whether the indexing needs toi be stopped
	 * @return true iff the indexing needs to be stopped, false otherwise
	 */
    @Override
	public boolean isStopped()
    {
    	return stop;
    }
    
    /**
     * Non thread way of doing things
     * @return list of discovered experiments
     */
    public List<RawExperiment> findRecords(File rootFolder, boolean recursive, IValidator validator) 
    {
    	IndexWorker worker = new IndexWorker(rootFolder, recursive, validator);
    	return worker.doIndexing();
    }
    
    /**
     * searches the specified folder for all supported records and returns
     * a list of series it discovers 
     * @param indexDir the directory to search
     * @return list of record-groups (series)
     */
    public List<RawExperiment> findRecords(File indexDir, IValidator validator)
    { 
    	logger.info("[Indexer]:\tIndexing  \t" + indexDir);
    	
        List<RawExperiment> groupList = new ArrayList<RawExperiment>();

        Set<ISourceReference> usedFiles = new HashSet<ISourceReference>();
        File[] sourceFiles = getSelectedFiles(indexDir);

        if(sourceFiles != null) 
        {
            for(File sourceFile : sourceFiles)
            {
            	if(isStopped()) break;
                if(usedFiles.contains(new RawSourceReference(sourceFile))) continue;
                
                // Check if it is a supported file type ..
				ImageReader tester = new ImageReader();
				// skip unsupported files
				if (!tester.isThisType(sourceFile.getAbsolutePath(), true))
				{
					logger.info("[Indexer]:\tSkipping unsupported file  \t" + sourceFile.getAbsolutePath());
					continue;
				}

				try
				{
					tester.close();
				} 
				catch (Exception e) 
				{
					System.out.println(e.getMessage());
					logger.warn("Closing tester failed ..", e);
				}
                
                logger.info("[Indexer]:\tExtracting records from \t" + sourceFile);
                
                RawExperiment group = RawExperimentFactory.createExperiment(sourceFile, computeSignature);
                
                try
                {
                	group.extractRecords(this, validator); //extract the records within this source file
                	
                	logger.info("[Indexer]:\tNumber of used files: " + group.getReference().size());
                	usedFiles.addAll(group.getReference());
                }
                catch(Exception e)
                {
                	logger.error("[Indexer]:\t Error Processing  File: " + sourceFile.getAbsolutePath());
                	SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run()
						{
							fireFailedEvent(false);
						}
					});
                }
               
                if(group.getRecords().size() == 0)
                {
                	logger.info("[Indexer]:\t No record created from  File: " + sourceFile.getAbsolutePath());
                	continue;
                }
                else
                {
                	groupList.add((RawExperiment)group);
 //               fireRecordGroupFound(group);
                	logger.info("[Indexer]:\tFound "+group.size() +" records from "+group.getSourceFileCount() +" source file(s): "+sourceFile);
                }
            }
        }

        return groupList;
    }
    
    /**
     * Finds all files that can be read using bio-formats from a directory.
     * @param indexDir the root folder to index
     * @return list of relevant files containing records
     */
	public File[] getSelectedFiles(File indexDir)
	{
		indexDir = indexDir.getAbsoluteFile();
		
		if (!indexDir.isDirectory())
		{
			File[] files = new File[1];
			files[0] = indexDir;
			return files;
		} 
		else
		{

			File[] files = prioritiesFiles(indexDir);
			if (files == null || files.length == 0)
			{
				logger.info("[Indexer]:\tEmpty folder " + indexDir);
				return null;
			} 
			else
			{
				return files;
			}
		}
	}
	
	private File[] prioritiesFiles(File indexDir)
	{
		File[] files = indexDir.listFiles();
		
		List<File> others = new ArrayList<File>(files.length);
		List<File> tiffs  = new ArrayList<File>(files.length);
		
		for(int i=0;i<files.length;i++)
		{
			String name = files[i].getName().toLowerCase();
			if(name.endsWith(".tif") || name.endsWith(".tiff"))
			{
				tiffs.add(files[i]);
			}
			else
			{
				others.add(files[i]);
			}
		}
		
		logger.info("[Indexer]:\tFound "+tiffs.size() +" tiff files in " + indexDir);
		logger.info("[Indexer]:\tFound "+others.size() +" non tiff files in " + indexDir);

		others.addAll(tiffs);// add the tiffs after the other files
		
		return others.toArray(new File[0]);
	}
    
	public void addIndexerListener(IndexerListener listener) 
	{
	    listenerList.add(IndexerListener.class, listener);
	}
	
	public void removeIndexerListener(IndexerListener listener) 
	{
	    listenerList.remove(IndexerListener.class, listener);
	}
    
    public void fireStartEvent()
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexingStarted();
	    	}
	    }
    }
    
    public void fireCompletionEvent()
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexingComplete();
	    	}
	    }
    }
    
    void fireExperimentFound(List<RawExperiment> experimentList)
	{
    	Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).experimentFound(experimentList);
	    	}
	    }
	}
    
    public void fireFailedEvent(boolean serverError)
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexingFailed(serverError);
	    	}
	    }
    }
    
    public void fireRecordFound(IRecord record) 
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexed(record);
	    	}
	    }
    }
    
    private void fireFoundDuplicate(IExperiment experiment)
	{
    	Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).ignoredDuplicate(experiment);
	    	}
	    }
	}
    
	@Override
	public void recordAdded(IRecord record)
	{
		if(!isStopped()) fireRecordFound(record);
	}
	
	@Override
	public void foundDuplicate(IExperiment experiment)
	{
		fireFoundDuplicate(experiment);
	}

	private static File createTempDir()
	{
		try
		{
			File tempDir = new File(new File(System.getProperty("java.io.tmpdir")), "imanage_cache");
			tempDir.mkdirs(); //create if needed
			return tempDir.getAbsoluteFile();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new File(System.getProperty("java.io.tmpdir")).getAbsoluteFile();
		}
	}
    
/*    public void fireRecordGroupFound(RawExperiment recordGroup) 
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexed(recordGroup);
	    	}
	    }
    }*/

    /**
     * Runs indexing in a separate thread
     */
    class IndexWorker extends Thread {

        private File rootFolder;
        private boolean recursive;
		private IValidator validator;

        public IndexWorker(File rootFolder, boolean recursive, IValidator validator) 
        {
            this.rootFolder = rootFolder;
            this.recursive = recursive;
            this.validator = validator;
        }
        
        public void run()
        {
        	fireStartEvent();
        	logger.info("[Indxer]:\tIndexing Starting...");
        	try
        	{
        		doIndexing();
        	}
        	catch (final ImageSpaceException e)
    		{
    			SwingUtilities.invokeLater(new Runnable() {
    				public void run() {
    					JOptionPane.showMessageDialog(null, "Connection Failed","Error", JOptionPane.ERROR_MESSAGE);
    				}
    			});
    			fireFailedEvent(true);
    		}
        	catch(Exception ex)
        	{
        		logger.error("[Indxer]:\tIndexing Complete with error...",ex);
        		fireFailedEvent(false);
        	}
        	catch(OutOfMemoryError error)
        	{
        		logger.error("[Indxer]:\tIndexing Complete with memory error...",error);
        		fireFailedEvent(false);
        	}
        	finally
        	{
        		logger.info("[Indxer]:\tIndexing Complete...");
        		fireCompletionEvent();
        	}
        }

        public List<RawExperiment> doIndexing() 
        {
        	logger.info("[Indexer]:\tStarted indexing directory: "+rootFolder);
        	List<RawExperiment> groupList = new ArrayList<RawExperiment>();
        	
            if(!recursive)
            {
            	List<RawExperiment> result = findRecords(rootFolder, validator);
            	if(result != null) groupList.addAll(result);
            }
            else
            {
                Queue<File> folderStack = new LinkedList<File>();
                folderStack.add(rootFolder);

                while(!stop && folderStack.peek() != null)
                {
                    File indexFolder = folderStack.remove();
                    List<RawExperiment> result = findRecords(indexFolder, validator);
                    if(result != null) groupList.addAll(result);

                    File[] subFolders = indexFolder.listFiles(new FileFilter() 
                    {
                        @Override
                        public boolean accept(File f) 
                        {
                            return f.isDirectory(); //choose only folders
                        }
                    });

                    if(subFolders != null && subFolders.length > 0) 
                    {
                        for(File sf : subFolders)
                            folderStack.add(sf);
                    }
                }
            }
            
            return groupList;
        }
    }
}
