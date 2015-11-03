package com.strandgenomics.imaging.iclient.local;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.strandgenomics.imaging.iclient.ImageSpaceException;
import com.strandgenomics.imaging.iclient.local.Indexer.IndexWorker;
import com.strandgenomics.imaging.icore.IValidator;
import com.strandgenomics.imaging.icore.util.Archiver;

/**
 * This is psuedo indexer does not created records but only creates tar ball out
 * of selected files, actual record creation will be done on server side
 * 
 * @author Anup Kulkarni
 */
public class DirectUploadIndexer extends SignaturelessIndexer{
	public DirectUploadIndexer()
	{
		super();
	}
	
	/**
     * Starts indexing on the specific root directory
     * @param rootFile the root folder to index
     * @param recursive checks whether to index sub folders also 
     */
    public void startIndexing(final File rootFolder, final boolean recursive, IValidator validator)
    {
    	this.stop = false;
    	IndexWorker worker = new ArchiveWorker(rootFolder, recursive, validator);
        worker.start();
        workerList.add(worker);
    }
	
	/**
     * Runs indexing in a separate thread
     */
    class ArchiveWorker extends IndexWorker {

        private File rootFolder;
        private boolean recursive;
		private IValidator validator;

        public ArchiveWorker(File rootFolder, boolean recursive, IValidator validator) 
        {
        	super(rootFolder, recursive, validator);
        	
            this.rootFolder = rootFolder;
            this.recursive = recursive;
            this.validator = validator;
        }
        
        public void run()
        {
        	fireStartEvent();
        	logger.info("[ArchiverIndexer]:\tIndexing Starting...");
        	try
        	{
        		List<RawExperiment> experimentList = doIndexing();
        		fireExperimentFound(experimentList);
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
        		logger.error("[ArchiverIndexer]:\tIndexing Complete with error...",ex);
        		fireFailedEvent(false);
        	}
        	catch(OutOfMemoryError error)
        	{
        		logger.error("[ArchiverIndexer]:\tIndexing Complete with memory error...",error);
        		fireFailedEvent(false);
        	}
        	finally
        	{
        		logger.info("[ArchiverIndexer]:\tIndexing Complete...");
        		fireCompletionEvent();
        	}
        }

		public List<RawExperiment> doIndexing() 
        {
        	logger.info("[ArchiverIndexer]:\tStarted indexing directory: "+rootFolder);
        	List<RawExperiment> groupList = new ArrayList<RawExperiment>();
        					
			DirectUploadExperiment experiment = new DirectUploadExperiment(rootFolder, rootFolder);
			groupList.add(experiment);
            
            return groupList;
        }
    }
	
}
