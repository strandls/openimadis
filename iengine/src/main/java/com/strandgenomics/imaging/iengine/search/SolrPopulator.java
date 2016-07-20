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
 * SolrPopulator.java
 *
 * AVADIS Image Management System
 * Engine Implementation
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
package com.strandgenomics.imaging.iengine.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.AttachmentDAO;
import com.strandgenomics.imaging.iengine.dao.HistoryDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.MetaDataDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.UserCommentDAO;
import com.strandgenomics.imaging.iengine.dao.VisualObjectsDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysDAO;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.SolrSearchManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * re-populate solr indexes for all records
 * @author arunabha
 *
 */
public class SolrPopulator{
	
	/**
	 * the logger
	 */
	protected Logger logger;
	
	public SolrPopulator()
	{
		Config.getInstance(); //initializes the system
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
	}
	
	/**
	 * For all records, do the following
	 * Step 0: Delete All Document from the Index
	 * Step 1: Add the record and commit
	 * Step 2: Add user annotation
	 * Step 3: Add user Comments
	 * Step 4: Add Visual Text box names
	 * Step 5: Add Overlay name
	 * Step 6: Add Attachment names and notes
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	void start() throws SolrServerException, IOException
	{
		logger.logp(Level.INFO, "SolrPopulator", "start", "deleting existing documents from solr");
		SolrSearchManager solr = SysManagerFactory.getSolrSearchEngine();
		
		//Step 1: Delete All Document from the Index
		solr.deleteAll();
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO pao = factory.getProjectDAO();
		
		List<Project> activeProjects = pao.findProjectByStatus(ProjectStatus.Active);
		if(activeProjects == null || activeProjects.isEmpty()) return;
		
		for(Project p : activeProjects)
		{
			createSolrIndex(p);
		}
		solr.commit();
	}
	
	/**
	 * change solr server to temp core
	 * @throws MalformedURLException
	 */
	void changeToReindex() throws MalformedURLException
	{
		SolrSearchManager solr = SysManagerFactory.getSolrSearchEngine();
		
		String url = Constants.getSolrReindexURL();
		CommonsHttpSolrServer newServer = new CommonsHttpSolrServer(url);
		solr.setServer(newServer);
	}
	
	/**
	 *  change solr server back to main core
	 *  
	 * @throws SolrServerException
	 * @throws IOException
	 */
	void swapCores() throws SolrServerException, IOException
	{
		SolrSearchManager solr = SysManagerFactory.getSolrSearchEngine();
		
		//change solr server back to main core
		solr.setServer(new CommonsHttpSolrServer(Constants.getSolrMainURL()));
		
		//swap solr cores
		solr.swapCores();
	}
	
	private void createSolrIndex(Project p) throws DataAccessException
	{
		logger.logp(Level.INFO, "SolrPopulator", "createSolrIndex", "creating solr index for project "+p.getName());
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO rao = factory.getRecordDAO();
		long[] guids = rao.getRecordGUIDs(p.getID());
		
		if(guids == null || guids.length == 0) return;
		for(long guid : guids)
		{
			try
			{
				System.out.println("creating solr indices for "+guid);
				createSolrIndexForRecord(p.getID(), guid);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.logp(Level.WARNING, "SolrPopulator", "createSolrIndex", "error in creating solr index for project "+p.getName()+" for record "+guid, e);
			}
		}
	}

	private void createSolrIndexForRecord(int projectID, long guid) throws SolrServerException, IOException 
	{
		logger.logp(Level.INFO, "SolrPopulator", "createSolrIndexForRecord", "creating solr index for GUID="+guid);
		SolrInputDocument document = new SolrInputDocument();

		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO rao = factory.getRecordDAO();
		Record r = rao.findRecord(guid);
		
		SolrSearchManager solr = SysManagerFactory.getSolrSearchEngine();
		// Step 1: Add the record and commit
		solr.addRecord(document, r);
		
		// Step 2: Add user annotation
		AnnotationType[] allTypes = {AnnotationType.Integer, AnnotationType.Real, AnnotationType.Text, AnnotationType.Time};
		//add annotations for all types
		for(AnnotationType type : allTypes)
		{
			MetaDataDAO mao = factory.getMetaDataDAO(type);
			List<MetaData> metaData = mao.find(projectID, guid);
			solr.addUserAnnotation(document, metaData);
		}
		
		// Step 3: Add user Comments
		UserCommentDAO cao = factory.getUserCommentDAO();
	    List<UserComment> comments = cao.findComments(guid);
	    if(comments != null)
	    {
	    	for(UserComment comment : comments)
	    	{
	    		solr.addComment(document, comment.getNotes());
	    	}
	    }
	    
		// Step 4: Add Visual Text box names
	    VisualObjectsDAO vao = factory.getVisualObjectsDAO();
	    String[] textMessages = vao.getTextBoxeMessages(projectID, guid);
	    if(textMessages != null)
	    {
	    	for(String message : textMessages)
	    	{
	    		solr.addTextBox(document, message);
	    	}
	    }
	    
		// Step 5: Add Overlay name
	    VisualOverlaysDAO dao = factory.getVisualOverlaysDAO();
	    List<String> overlayNames = dao.getAvailableVisualOverlays(projectID, guid, null);
	    if(overlayNames != null)
	    {
	    	for(String overlayName : overlayNames)
	    	{
	    		solr.addOverlay(document, overlayName);
	    	}
	    }
	    
		// Step 6: Add Attachment name and notes
	    AttachmentDAO aao = factory.getAttachmentDAO();
	    List<Attachment> attachments = aao.getRecordAttachments(guid);
	    if(attachments != null)
	    {
	    	for(Attachment a : attachments)
	    	{
	    		solr.addAttachment(document, a.getName(), a.getNotes());
	    	}
	    }
	    
	    // Step 7: Add record history
	    HistoryDAO historyDAO = factory.getHistoryDAO();
	    List<HistoryObject> recordHistory = historyDAO.getRecordHistory(r.projectID, guid);
	    if(recordHistory != null)
	    {
	    	for(HistoryObject historyObject:recordHistory)
	    	{
	    		solr.addHistory(document, historyObject.getDescription());
	    	}
	    }
	    
		solr.add(document);
		logger.logp(Level.INFO, "SolrPopulator", "createSolrIndexForRecord", "successfully added record to solr "+r);
	}
	
	/**
	 * runs the indexer to recreate all relevant solr indexes
	 * IMPORTANT - Stop solr server, Please delete the Solr Data folder and make sure Solr server is running again
	 * @param args
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public static void main(String ... args) throws IOException, SolrServerException
	{
    	if(true)
    	{
    		File f = new File(args[0]);//solr-reindexer.properties
    		if(f.isFile())
    		{
    			System.out.println("loading system properties from "+f);
    			BufferedReader inStream = new BufferedReader(new FileReader(f));
	    		Properties props = new Properties();
	    		props.load(inStream);
	    		
	    		props.putAll(System.getProperties()); //copy existing properties, it is overwritten :-(
	    		props.list(System.out);
	    		
	    		System.setProperties(props);
	    		inStream.close();
    		}
    	}
    	
    	System.out.println("IMPORTANT:  Stop solr server, delete the Solr Data folder and make sure Solr server is running again..");
    	System.out.println("\n\n STARTING SOLR RE-INDEXER \n\n");
    	
    	SolrPopulator populator = new SolrPopulator();
    	
    	populator.changeToReindex();
    	populator.start();
    	populator.swapCores();
	}
}
