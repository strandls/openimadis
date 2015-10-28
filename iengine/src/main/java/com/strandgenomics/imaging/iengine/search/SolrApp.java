package com.strandgenomics.imaging.iengine.search;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrApp {
	
	private static String URL = "http://localhost:8080";
	private static String SOLR_BASE = "/solr";

	private static SolrServer server = null;
	
	public static SolrServer getServer() throws SolrServerException, IOException {
		// TODO reuse the server if initially used
		server = new CommonsHttpSolrServer(URL + SOLR_BASE);
		return server;
	}
	
	public static SolrInputDocument getDocument(Map<String, String> doc) {
		SolrInputDocument document = new SolrInputDocument();
		for (String key : doc.keySet()) {
			document.addField(key, doc.get(key));
		}
		return document;
	}
	
	public static void addDocument(Map<String, String> doc) throws SolrServerException, IOException {
		SolrServer server = getServer();
		SolrInputDocument document = getDocument(doc);
		server.add(document);
	}
	
	public static void addDocuments(Collection<SolrInputDocument> docs) throws SolrServerException, IOException {
		SolrServer server = getServer();
		server.add(docs);
	}
	
	public static void update(String idKey, String idValue, Map<String, String> newparams) throws SolrServerException, IOException {
		SolrServer server = getServer();
		SolrQuery query = new SolrQuery();
		query.setQuery(idKey + ":" + idValue);
		
		query.setRows(100);	// maximum results in one page
		query.setStart(0);	// start at the particular row
		query.setParam("wt", "json");	// return type is JSON
		QueryResponse response = server.query(query);
		SolrDocumentList docs = response.getResults();
		
		for (SolrDocument doc : docs) {
			for (String k : newparams.keySet()) {
				doc.setField(k, newparams.get(k));
			}
			SolrInputDocument d = new SolrInputDocument();
			Map<String, Object> data = doc.getFieldValueMap();
			for (String k : data.keySet()) {
				d.addField(k, data.get(k));
			}
			server.add(d);
		}
		
	}
	
	public static void commit() throws SolrServerException, IOException {
		server.commit();
	}
	
	public static String search(String key, String value) throws SolrServerException, IOException {
		SolrServer server = getServer();
		SolrQuery query = new SolrQuery();
		if (key != null && key.length() > 0)
			query.setQuery(key + ":" + value);
		else
			query.setQuery("_allFields" + ":" + value);
		
		query.setParam("wt", "json");	// return type is JSON
		QueryResponse response = server.query(query);
		SolrDocumentList docs = response.getResults();
		
	    return docs.toString();
	}
	
	/**
	 * Sample Code with examples
	 */
	
	private static void deleteAll() throws SolrServerException, IOException {
		SolrServer server = getServer();
		server.deleteByQuery("*:*");
		commit();
	}
	
	private static void adding() throws SolrServerException, IOException {
		Map<String, String> doc = new HashMap<String, String>();
		double rand;
		char id = 'a';
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for (int i=0; i<26; i++) {
			rand = Math.random();
			doc.clear();
			doc.put("id", "" + id);
			doc.put("name", "" + rand);
			doc.put("test_s", "helo");
			doc.put("test_d", "" + rand);
			doc.put("test_i", "" + (int)(rand*100));
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			String testing = df.format(date);
			System.out.println(testing);
			doc.put("test_date", testing);
			docs.add(getDocument(doc));
			System.out.println(id);
			id++;
		}
		addDocuments(docs);
		commit();
	}
	
	private static void searching() throws SolrServerException, IOException {
		String res = search("*", "*");	// return all
		System.out.println(res);

		res = search("test_i", "23");	// return document with field test_i as 23
		System.out.println(res);
		
		res = search("test_i", "[10 TO 60]");	// return integers in range
		System.out.println(res);
		
		res = search("test_d", "[0.001 TO 0.8]");	// return double in range
		System.out.println(res);
		
		res = search("test_s", "hel");	// substring of the actual string, does not give result
		System.out.println(res);
		
		res = search("test_date", "NOW");	
		System.out.println(res);
		
		res = search("test_date", "[NOW-1DAY TO NOW]");	
		System.out.println(res);

	}
	
	private static void updating() throws SolrServerException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("test_s", "new");
		update("id", "a", params);
		commit();
	}
	
        /**
         * four operations implemented
         */
	public static void main(String[] args) throws SolrServerException, IOException {
//		adding();
//		deleteAll();
//		searching();
//		updating();
	}
	

}
