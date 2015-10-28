/*
 * SolrSearchManager.java
 *
 * AVADIS Image Management System
 * Core Engine
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
package com.strandgenomics.imaging.iengine.system;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.params.ModifiableSolrParams;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.search.SearchResult;
/**
 * This class will expose API to control the data in Apache Solr.
 * Data for records could be added, modified, deleted and searched 
 * in the database.
 * @author varun
 *
 */
public class SolrSearchManager extends SystemManager {
	
	private static final int MAX_RESULT = 1000;
	private static final String RANGE_CONNECTOR = "TO";
	
	private SolrServer server = null;
	private SimpleDateFormat solrDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	SolrSearchManager()
	{}
	
	/**
	 * Suggests results while search
	 * @throws IOException 
	 */
	public List<String> suggest(String searchText) throws IOException{
		
		if(searchText == null) return null;
		
		logger.logp(Level.INFO, "SolrSearchManager", "suggest", "suggesting for"+searchText);
		
	    SolrServer server;
	    
		try {
			server = getServer();
		} catch (MalformedURLException e) {
        	logger.logp(Level.INFO, "SolrSearchManager", "suggest", "error while suggesting for"+searchText, e);
            throw new IOException(e);
		}
	    
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("qt", "/suggest");
        params.set("q", searchText);
        
        QueryResponse response;
        
		try {
			response = server.query(params);
		} catch (SolrServerException e) {
        	logger.logp(Level.INFO, "SolrSearchManager", "suggest", "error while suggesting for"+searchText, e);
            throw new IOException(e);
		}
		
        SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse() ;
        
        if(spellCheckResponse==null)
        	return null;
        	
        List<Suggestion> suggestionList = spellCheckResponse.getSuggestions();        
        List<String> suggestions=new ArrayList<String>();
        
        if(suggestionList!=null && suggestionList.size()>0)
        {
            System.out.println("Suggestion List: ");
            for(Suggestion suggestion : suggestionList)
            {
                List<String> alternatives = suggestion.getAlternatives() ;
                if(alternatives!=null && alternatives.size()>0)
                {
                	suggestions.addAll(alternatives);	
                }
            }
        }
        
        return suggestions;		
	}
	
	/**
	 * Searches solr for the specified free text and possibly filtered with the specified list of projects 
	 * and meta data and return the result by project
	 * @param actorLogin the user doing the search
	 * @param searchText the free text search string
	 * @param projects list of projects to restrict this search, null will means all valid projects 
	 * @param filter list of given meta data the result result should satisfy
	 * @return list of record IDs this user can see and satisfying the search condition
	 * @throws IOException 
	 */
	public List<SearchResult> solrSearch(String actorLogin, String searchText, List<String> projects, List<MetaData> filter, boolean isAdvancedQuery) 
			throws IOException
	{
		logger.logp(Level.INFO, "SolrSearchManager", "solrSearch", "searching for "+actorLogin +" with "+searchText);
		ProjectManager pm = SysManagerFactory.getProjectManager();
		
		List<Integer> projectIDList = projects == null ? null : new ArrayList<Integer>();
		if(projects != null)
		{
			for(String projectName : projects)
			{
				Project p = pm.getProject(projectName);
				if(p != null)
				{
					projectIDList.add( p.getID() );
				}
			}
		}
		
        try 
        {
        	return searchByProjects(actorLogin, searchText, projectIDList, filter, isAdvancedQuery);
        } 
        catch (MalformedURLException e)
        {
        	logger.logp(Level.INFO, "SolrSearchManager", "solrSearch", "error while searching for "+actorLogin +" with "+searchText, e);
            throw new IOException(e);
        } 
        catch (SolrServerException e) 
        {
        	logger.logp(Level.INFO, "SolrSearchManager", "solrSearch", "error while searching for "+actorLogin +" with "+searchText, e);
            throw new IOException(e);
        }
	}
	
	
	/**
	 * Searches solr for the specified free text and possibly filtered with the specified list of projects 
	 * and meta data and return the result by project
	 * @param actorLogin the user doing the search
	 * @param text the free text search string
	 * @param projectIDs list of projects to restrict this search, null will means all valid projects 
	 * @param filter list of given meta data the result result should satisfy
	 * @param isAdvanced is advanced query (advanced queries will be submitted as is to solr without preprocessing)
	 * @return list of record IDs this user can see and satisfying the search condition
	 * @throws SolrServerException 
	 * @throws MalformedURLException 
	 * @throws DataAccessException 
	 */
	public List<SearchResult> searchByProjects(String actorLogin, String text, List<Integer> projectIDs, List<MetaData> filter, boolean isAdvanced) 
			throws MalformedURLException, SolrServerException, DataAccessException
	{
		List<SearchResult> result = search(text, projectIDs, filter, isAdvanced);
		if(result == null || result.isEmpty()) return null;
		
		List<Project> activeProjects = SysManagerFactory.getProjectManager().getProjects(actorLogin, ProjectStatus.Active);
		if(activeProjects == null || activeProjects.isEmpty()) return null;
		
		Set<Integer> validProjectIDs = new HashSet<Integer>(); //set of valid project ids
		for(Project p : activeProjects)
		{
			validProjectIDs.add( p.getID() );
		}
		
		List<SearchResult> filteredResult = new ArrayList<SearchResult>();
		for(SearchResult r : result)
		{
			if(validProjectIDs.contains(r.projectid))
			{
				filteredResult.add(r);
			}
		}
		
		return filteredResult;
	}
	
	public long[] searchWithFilters(String actorLogin, String searchText, List<String> projects, Collection<SearchCondition> filter, int maxResult) 
			throws IOException 
	{
		logger.logp(Level.INFO, "SolrSearchManager", "searchWithFilters", "searching for "+actorLogin +" with "+searchText);
		ProjectManager pm = SysManagerFactory.getProjectManager();
		
		List<Integer> projectIDList = projects == null ? null : new ArrayList<Integer>();
		if(projects != null)
		{
			for(String projectName : projects)
			{
				Project p = pm.getProject(projectName);
				if(p != null)
				{
					projectIDList.add( p.getID() );
				}
			}
		}
		
		List<Project> activeProjects = pm.getProjects(actorLogin, ProjectStatus.Active);
		if(activeProjects == null || activeProjects.isEmpty()) return null;
		
		List<SearchResult> result = null;
		try
		{
			result = rangeSearch(searchText, projectIDList, filter,  maxResult);
		}
		catch(Exception ex)
		{
			throw new IOException(ex);
		}
		
		if(result == null || result.isEmpty()) return null;
		
		Set<Integer> validProjectIDs = new HashSet<Integer>(); //set of valid project ids
		for(Project p : activeProjects)
		{
			validProjectIDs.add( p.getID() );
		}
		
		List<Long> guidList = new ArrayList<Long>();
		for(SearchResult r : result)
		{
			if(validProjectIDs.contains(r.projectid))
			{
				guidList.add(r.guid);
			}
		}
		
		long[] guids = new long[guidList.size()];
		for(int i = 0; i < guids.length; i++)
			guids[i] = guidList.get(i);
		
		return guids;
	}
	
	private List<SearchResult> rangeSearch(String searchText, List<Integer> projects, Collection<SearchCondition> filters, int maxResult) throws MalformedURLException, SolrServerException 
	{
		if(searchText == null) return null;
		
		logger.logp(Level.INFO, "SolrSearchManager", "search", "searching for records with "+searchText);
		
		SolrServer server = getServer();
		SolrQuery query = new SolrQuery();
		query.setRows(maxResult);
		
		String queryText = getQuery(searchText);
		logger.logp(Level.INFO, "SolrSearchManager", "search", "searching for records, queryText="+queryText);
		
		query.setQuery(queryText);
		
		if (projects != null && projects.size() > 0)
		{
			String fq = "projectID:" + projects.get(0);
			for (int i = 1; i < projects.size(); i++)
			{
				fq += " OR " + "projectID:" + projects.get(i);
			}
			query.addFilterQuery(fq);
		}
		
		if (filters != null && filters.size() > 0)
		{
			for (SearchCondition f : filters) 
			{
				if(f.getLowerLimit() != null && f.getUpperLimit() != null)
				{
					String field = getId(f.fieldName, f.fieldType.toString());
					String fq = field + ":["+f.getLowerLimit() +" TO "+f.getUpperLimit()  +"] " ;
					query.addFilterQuery(fq);
				}
			}
		}
		
		QueryResponse response = server.query(query);
		SolrDocumentList docs = response.getResults();
		List<SearchResult> result = new ArrayList<SearchResult>();
		
		for (SolrDocument doc : docs)
		{
			long guid = (Long) doc.getFieldValue("guid");
			int projectID = (Integer) doc.getFieldValue("projectID");
			result.add(new SearchResult(guid, projectID, new ArrayList<String>()));// TODO: check highlighting
		}
		return result;
	}

	/**
	 * Searches solr for the specified free text and possibly filtered with the specified list of projects and meta data
	 * @param actorLogin the user doing the search
	 * @param text the free text search string
	 * @param projects list of projects to restrict this search, null will means all valid projects 
	 * @param filter list of given meta data the result result should satisfy
	 * @param isAdvanced is advanced query (advanced queries will be submitted as is to solr without preprocessing)
	 * @return list of record IDs this user can see and satisfying the search condition
	 * @throws SolrServerException 
	 * @throws MalformedURLException 
	 * @throws DataAccessException 
	 */
	public List<Long> search(String actorLogin, String text, List<Integer> projects, List<MetaData> filter, boolean isAdvancedQuery) 
			throws MalformedURLException, SolrServerException, DataAccessException
	{
		List<SearchResult> result = search(text, projects, filter, isAdvancedQuery);
		if(result == null || result.isEmpty()) return null;
		
		List<Project> activeProjects = SysManagerFactory.getProjectManager().getProjects(actorLogin, ProjectStatus.Active);
		if(activeProjects == null || activeProjects.isEmpty()) return null;
		
		Set<Integer> validProjectIDs = new HashSet<Integer>(); //set of valid project ids
		for(Project p : activeProjects)
		{
			validProjectIDs.add( p.getID() );
		}
		
		List<Long> guidList = new ArrayList<Long>();
		for(SearchResult r : result)
		{
			if(validProjectIDs.contains(r.projectid))
			{
				guidList.add(r.guid);
			}
		}
		
		return guidList;
	}

	/**
	 * The wiki says that there should be only one instance of CommonsHttpSolrServer
	 * throughout the program.
	 * @return
	 * @throws MalformedURLException 
	 */
	public synchronized SolrServer getServer() throws MalformedURLException 
	{
		if (server == null)
			server = new CommonsHttpSolrServer(Constants.getSolrMainURL());
		System.out.println("returning server "+server);
		return server;
	}
	
	/**
	 * The wiki says that there should be only one instance of CommonsHttpSolrServer
	 * throughout the program.
	 * @return
	 * @throws MalformedURLException 
	 */
	public synchronized void setServer(CommonsHttpSolrServer solrServer) throws MalformedURLException 
	{
		if (solrServer != null)
			server = solrServer;
	}
	
	/**
	 * Add the record as a solr document with all the fixed fields in the 
	 * record. Add the user as one of the fields. The guid property of the 
	 * record will be used as the uniqueId for the solr document.
	 * @param record
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void addRecord(Record r) throws SolrServerException, IOException 
	{
		logger.logp(Level.INFO, "SolrSearchManager", "addRecord", "adding record to solr "+r);
		solrDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		SolrServer server = getServer();
		SolrInputDocument document = new SolrInputDocument();
		
		addRecord(document, r);

		server.add(document);
		logger.logp(Level.INFO, "SolrSearchManager", "addRecord", "successfully added record to solr "+r);
		server.commit(); //this is needed, other update on this document cannot be done
	}

	/**
	 * Adds the record to the specified solr document
	 * @param document the target document
	 * @param r the record to add
	 */
	public void addRecord(SolrInputDocument document, Record r) 
	{
		document.addField("guid", r.guid);
		document.addField("projectID", r.projectID);
		document.addField("uploadedBy", r.uploadedBy);
		
		document.addField("numberOfSlices", r.numberOfSlices);
		document.addField("numberOfFrames", r.numberOfFrames);
		document.addField("numberOfChannels", r.numberOfChannels);
		document.addField("numberOfSites", r.numberOfSites);
		
		document.addField("imageWidth", r.imageWidth);
		document.addField("imageHeight", r.imageHeight);
		
		document.addField("uploadTime", solrDateFormat.format(new Date(r.uploadTime)));
		document.addField("sourceTime", solrDateFormat.format(new Date(r.sourceTime)));
		document.addField("creationTime", solrDateFormat.format(new Date(r.creationTime)));
		
		if (r.acquiredTime != null)
			document.addField("acquiredTime", solrDateFormat.format(new Date(r.acquiredTime)));
		
		document.addField("imageDepth", r.imageDepth.getByteSize());
		
		document.addField("xPixelSize", r.getXPixelSize());
		document.addField("yPixelSize", r.getYPixelSize());
		document.addField("zPixelSize", r.getZPixelSize());
		
		document.addField("sourceType", r.getSourceFormat().name);
		document.addField("imageType", r.imageType.name());
		
		document.addField("machineIP", r.machineIP);
		document.addField("macAddress", r.macAddress);
		
		//replace the File.separator with space
		document.addField("sourceFolder", r.sourceFolder.replace('\\', ' ').replace('/', ' '));
		document.addField("sourceFilename", r.sourceFilename.replace('\\', ' ').replace('/', ' '));
		
		//add channels 
		for(Channel c : r.channels)
		{
			document.addField("channelName", c.getName());
		}
		
		//and sites
		for(Site s : r.sites)
		{
			document.addField("siteName", s.getName());
		}
	}

	/**
	 * Add user annotation to a particular record. The record with the given 
	 * guid will be extracted as a solr document, and the user annotations 
	 * will be added as fields in the document with the given type in the 
	 * metadata of a particular annotation.
	 * @param guid
	 * @param annots
	 * @throws SolrServerException 
	 * @throws IOException 
	 */
	public void addUserAnnotation(long guid, List<MetaData> annots) throws SolrServerException, IOException
	{
		if(annots == null || annots.isEmpty()) return;
		logger.logp(Level.INFO, "SolrSearchManager", "addUserAnnotation", "adding user annotations to solr "+guid);
		SolrInputDocument document = getSolrInputDocument(guid);
		
		addUserAnnotation(document, annots);
		
		getServer().add(document);
		commit();
	}
	
	/**
	 * Adds the specified user annotation to the specified solr document
	 * @param document
	 * @param annots
	 */
	public void addUserAnnotation(SolrInputDocument document, List<MetaData> annots) 
	{
		if(annots == null || annots.isEmpty()) return;
		
		for (MetaData annot : annots)
		{
			String name = annot.getName();
			String type = annot.getType().toString();
			
			String field = getId(name, type);
			Object value = annot.getValue();
			
			document.addField(field, value);
		}
	}

	/**
	 * Change the user annotation of a particular record. The record with 
	 * the given guid will be extracted as a solr document, and the user 
	 * annotation will be either modified or added in the document.
	 * @param guid
	 * @param annot
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void updateUserAnnotation(long guid, MetaData annot) throws SolrServerException, IOException
	{
		logger.logp(Level.INFO, "SolrSearchManager", "updateUserAnnotation", "updating user annotations to solr "+guid);
		List<MetaData> annots = new ArrayList<MetaData>();
		annots.add(annot);
		addUserAnnotation(guid, annots);
	}
	
	/**
	 * Deleting a particular record with given guid.
	 * @param actor
	 * @param guid
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void deleteRecord(long guid) throws SolrServerException, IOException 
	{
		logger.logp(Level.INFO, "SolrSearchManager", "deleteRecord", "deleting records from solr "+guid);
		
		SolrServer server = getServer();
		server.deleteById("" + guid);
		
		server.commit();
	}
	
	/**
	 * Remove a user annotation from the record. The record with the 
	 * given guid will be extracted as a solr document and the field 
	 * will be removed depending on its type from the document.
	 * @param actor
	 * @param guid
	 * @param field
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void deleteUserAnnotation(long guid, MetaData md) throws SolrServerException, IOException 
	{
		logger.logp(Level.INFO, "SolrSearchManager", "deleteUserAnnotation", "deleting user annotations to solr "+guid);
		
		SolrInputDocument document = getSolrInputDocument(guid);

		String field = getId(md.getName(), md.getType().toString());
		document.removeField(field);

		getServer().add(document);
	}
	
	public void addAttachment(long guid, String name, String notes)  throws SolrServerException, IOException 
	{
		logger.logp(Level.INFO, "SolrSearchManager", "addAttachment", "adding attachment to solr "+guid);
		SolrInputDocument document = getSolrInputDocument(guid);
		addAttachment(document, name, notes);
		getServer().add(document);
		commit();
	}
	
	/**
	 * Adds attachment name and notes to the specified solr document
	 * @param document
	 * @param name
	 * @param notes
	 */
	public void addAttachment(SolrInputDocument document, String name, String notes)
	{
		if(name != null) document.addField("attachmentName", name);
		if(notes != null) document.addField("attachmentNotes", notes);
	}

	/**
	 * Users can add comment to a record. These comments will be appended 
	 * to the existing comments in the solr document, to allow full searching 
	 * of these. Make the comments as a multiValued field in solr, 
	 * so that two different comments shouldn't combine to allow for a search 
	 * string.
	 * @param guid
	 * @param comment
	 * @throws SolrServerException 
	 * @throws IOException 
	 */
	public void addComment(long guid, String comment) throws SolrServerException, IOException 
	{
		if(comment == null) return;
		logger.logp(Level.INFO, "SolrSearchManager", "addComment", "adding user comments to solr "+guid +", "+comment);
		SolrInputDocument document = getSolrInputDocument(guid);
		addComment(document, comment);
		getServer().add(document);
		commit();
	}
	
	public void addComment(SolrInputDocument document, String comment) 
	{
		if(comment == null) return;
		document.addField("comment", comment);
	}
	
	/**
	 * Add the history item to the record
	 * @param document
	 * @param description
	 */
	public void addHistory(SolrInputDocument document, String description) 
	{
		if(description == null) return;
		document.addField("history", description);
	}

	/**
	 * Users can add textbox over the record to create a annotation.
	 * These textboxes should be appended to the existing textbox contents 
	 * in the solr document, to allow full searching of these. Make the 
	 * textboxes as a multiValued field in solr, so that two different 
	 * textbox contents shouldn't combine to allow for a search string.
	 * @param guid
	 * @param comment
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void addTextBox(long guid, String text) throws SolrServerException, IOException
	{
		if(text == null) return;
		logger.logp(Level.INFO, "SolrSearchManager", "addTextBox", "addingtext boxes to solr "+guid);
		SolrInputDocument document = getSolrInputDocument(guid);
		addTextBox(document, text);
		getServer().add(document);
		commit();
	}
	
	public void addTextBox(SolrInputDocument document, String text)
	{
		if(text == null) return;
		document.addField("textbox", text);
	}

	public void addOverlay(long guid, String overlayName) throws SolrServerException, IOException
	{
		if(overlayName == null) return;
		logger.logp(Level.INFO, "SolrSearchManager", "addOverlay", "adding overlay name to solr "+guid);
		SolrInputDocument document = getSolrInputDocument(guid);
		addOverlay(document, overlayName);
		getServer().add(document);
		commit();
	}
	
	public void addOverlay(SolrInputDocument document, String overlayName) 
	{
		if(overlayName == null) return;
		document.addField("overlay", overlayName);
	}

	/**
	 * Given a text(Lucene query) search for it in the documents. Return 
	 * all the documents which match the search.
	 * @param text
	 * @param isAdvanced is advanced query (advanced queries will be submitted as is to solr without preprocessing)
	 * @return list of the results as objects of SearchResult
	 * @throws MalformedURLException 
	 * @throws SolrServerException 
	 */
	public List<SearchResult> search(String text, boolean isAdvanceQuery) throws MalformedURLException, SolrServerException
	{
		return search(text, null, null, isAdvanceQuery);
	}
	
	/**
	 * Given a text(Lucene query) search for it in the documents only for 
	 * particular project. Return all the documents which match the search.
	 * @param text
	 * @param projects
	 * @param isAdvanced is advanced query (advanced queries will be submitted as is to solr without preprocessing)
	 * @return list of the results as objects of SearchResult
	 * @throws MalformedURLException 
	 * @throws SolrServerException 
	 */
	public List<SearchResult> search(String text, List<Integer> projects, boolean isAdvanceQuery) throws MalformedURLException, SolrServerException 
	{
		return search(text, projects, null, isAdvanceQuery);
	}
	
	/**
	 * Given a text(Lucene query) search for it in the documents only for 
	 * particular project. Filter on the search results with the given 
	 * filter annotations. Return all the documents which match the search.
	 * @param text
	 * @param projects
	 * @param filter
	 * @return
	 * @throws MalformedURLException 
	 * @throws SolrServerException 
	 */
	private List<SearchResult> search(String searchText, List<Integer> projects, List<MetaData> filter, boolean isAdvanced) throws MalformedURLException, SolrServerException 
	{
		return search(searchText, projects, filter, MAX_RESULT, isAdvanced);
	}
	
	private List<SearchResult> search(String searchText, List<Integer> projects, List<MetaData> filter, int maxResult, boolean advanceSearch) throws MalformedURLException, SolrServerException 
	{
		if(searchText == null) return null;
		
		logger.logp(Level.INFO, "SolrSearchManager", "search", "searching for records with "+searchText);
		
		SolrServer server = getServer();
		SolrQuery query = new SolrQuery();
		query.setRows(maxResult);
		
		String queryText = searchText;
		if(!advanceSearch)
		{
			searchText = searchText.replaceAll(":", "");// remove all field queries
			queryText = getQuery(searchText);
		}
		logger.logp(Level.INFO, "SolrSearchManager", "search", "searching for records, queryText="+queryText);
		
		query.setQuery(queryText);
		
		if (projects != null && projects.size() > 0)
		{
			String fq = "projectID:" + projects.get(0);
			for (int i = 1; i < projects.size(); i++)
			{
				fq += " OR " + "projectID:" + projects.get(i);
			}
			query.addFilterQuery(fq);
		}
		
		if (filter != null && filter.size() > 0)
		{
			String field, name, type, fq;
			Object value;
			
			for (MetaData f : filter) 
			{
				name = f.getName();
				type = f.getType().toString();
				value = f.getValue();
				
				field = getId(name, type);
				fq = field + ":" + value;
				query.addFilterQuery(fq);
			}
		}
		
		// for highlighting
		query.setParam("hl", "on");
		query.setParam("hl.fl", "*");
		
		// query response
		QueryResponse response = server.query(query);
		
		// for highlighting
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		// result docs
		SolrDocumentList docs = response.getResults();
		List<SearchResult> result = new ArrayList<SearchResult>();
		
		for (SolrDocument doc : docs)
		{
			long guid = (Long) doc.getFieldValue("guid");
			int projectID = (Integer) doc.getFieldValue("projectID");
			List<String> matchedFields = new ArrayList<String>();
			
			try
			{
				Map<String, List<String>> hl = highlighting.get(String.valueOf(guid));
				
				for(String key:hl.keySet())
				{
					matchedFields.add(key);
				}
			}
			catch(Exception e)
			{
				logger.logp(Level.WARNING, "SolrSearchManager", "search", "error in getting highlightings", e);
			}
			
			result.add(new SearchResult(guid, projectID, matchedFields));
		}
		
		return result;
	}
	
	private SolrInputDocument getSolrInputDocument(long guid) throws MalformedURLException, SolrServerException
	{
		SolrDocument solrDoc = getSolrDocument(guid);
		SolrInputDocument document = new SolrInputDocument();

		Collection<String> fields = solrDoc.getFieldNames();
		for (String fieldName : fields) 
		{
			Collection<Object> values = solrDoc.getFieldValues(fieldName);
			if(values == null || values.isEmpty()) continue;
			logger.logp(Level.FINEST, "SolrSearchManager", "getSolrInputDocument", fieldName +", value="+values);
			
			for(Object value : values)
			{
				document.addField(fieldName, value);
			}
		}
		
		return document;
	}
	
	private SolrDocument getSolrDocument(long guid) throws SolrServerException, MalformedURLException
	{
		SolrServer server = getServer();
		SolrQuery query = new SolrQuery();
		query.setQuery("guid:" + guid);
		
		QueryResponse response = server.query(query);
		SolrDocumentList docs = response.getResults();
		return docs.get(0);
	}

	private String getId(String name, String type) 
	{
		return type + "_" + name;
	}
	
	/**
	 * to commit instantaneously
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void commit() throws SolrServerException, IOException
	{
		SolrServer server = getServer();
		server.commit();
	}

	/**
	 * to delete all the documents from the server
	 * use with care.
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void deleteAll() throws SolrServerException, IOException 
	{
		SolrServer server = getServer();
		server.deleteByQuery("*:*");
		server.commit();
	}
	
	private String getQuery(String searchText)
	{
		String[] result = searchText.trim().split("\\s"); //split with white space
		StringBuilder query = new StringBuilder();
		
		if (result.length == 3 && RANGE_CONNECTOR.equals(result[1]))
		{
			SearchField type1 = findType(result[0]);
			SearchField type2 = findType(result[2]);
			
			if (type1.equalsType(type2))
			{
				query.append( type1.getStronger(type2).getRangeQuery(result[0], result[2]) );
				return query.toString().trim();
			}
		}
		
		// if not a range query
		for(String component : result)
		{
			query.append(' ');
			
			SearchField type = findType(component);
			if(type == null)
			{
				query.append(component);
			}
			else
			{
				query.append( type.getQuery(component) );
			}
		}
		
		return query.toString().trim();
	}

	private SearchField findType(String text)
	{
		try
		{
			solrDateFormat.parse(text);
			return SearchField.DATE;
		}
		catch(ParseException px)
		{
			if(text.indexOf(':') != -1) return null; //already a specific query
			
			try
			{
				Long.parseLong(text);
				return SearchField.INT;
			}
			catch(NumberFormatException e)
			{
				try
				{
					Double.parseDouble(text);
					return SearchField.REAL;
				}
				catch(NumberFormatException ee)
				{
					if(getCharCount(text, '.') >= 3)
						return SearchField.IP;
				}
			}
		}
		return SearchField.TEXT;
	}
	
	private int getCharCount(String text, char c)
	{
		int count = 0;
		int index = -1;
		while((index = text.indexOf(c, index+1)) != -1)
		{
			count++;
		}
		return count;
	}
	
	private static enum SearchField
	{
		UNKNOWN("", "", -1),
		MAC("macAddress", "string", 0),
		IP("machineIP", "string", 0),
		TEXT("all_text_fields", "string", 1),
		INT("all_int_fields", "number", 0),
		REAL("all_real_fields", "number", 1),
		DATE("all_date_fields", "string", 0);
		
		private String type = null;
		private String klass = null;
		private int rank = -1;
	    
	    private SearchField(String op, String klass, int rank)
	    {
	    	this.type = op;
	    	this.klass = klass;
	    	this.rank = rank;
	    }
	    
	    public String toString()
	    {
	    	return type;
	    }
	    
	    public String getQuery(String text)
	    {
	    	text = text.replaceAll(":", "\\\\:");
	    	return type+":"+text;
	    }
	    
	    public boolean equalsType(SearchField sf)
	    {
	    	if (klass == null)
	    		return false;
	    	if (klass.equals(sf.klass))
	    		return true;
	    	return false;
	    }
	    
	    public SearchField getStronger(SearchField type2)
	    {
	    	if (type2.rank > rank)
	    		return type2;
	    	return this;
	    }
	    
	    public String getRangeQuery(String text1, String text2)
	    {
	    	text1 = text1.replaceAll(":", "\\\\:");
	    	text2 = text2.replaceAll(":", "\\\\:");
	    	return type+":"+"["+text1+" TO "+text2+"]";
	    }
	}
	
	public void add(SolrInputDocument document) throws MalformedURLException, SolrServerException, IOException
	{
		getServer().add(document);
	}


	public void swapCores() throws SolrServerException, IOException
	{
		String solrMainUrl = Constants.getSolrMainURL();
		String solrUrl = solrMainUrl.substring(0, solrMainUrl.lastIndexOf("/main"));
		
		CommonsHttpSolrServer mainServer = new CommonsHttpSolrServer(solrUrl);
		
		CoreAdminRequest adminRequest = new CoreAdminRequest();
		adminRequest.setCoreName("main");
		adminRequest.setOtherCoreName("reindex");
		adminRequest.setAction(CoreAdminAction.SWAP);
		
		adminRequest.process(mainServer);
	}
}
