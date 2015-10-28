package com.strandgenomics.imaging.iengine.search;

import java.util.List;

/**
 * Class to wrap the results of search in a object
 * @author varun
 * @author Anup Kulkarni
 *
 */
public class SearchResult {
	/**
	 * matched record id
	 */
	public final long guid;
	/**
	 * id of th parent project
	 */
	public final int projectid;
	/**
	 * list of fields where the search query is matched
	 */
	public final List<String> matched_fields;
	
	public SearchResult(long guid, int projectid, List<String>matchedFields) {
		this.guid = guid;
		this.projectid = projectid;
		this.matched_fields = matchedFields;
	}
	
	public long getGuId() {
		return guid;
	}
	
	public long getProjectId() {
		return projectid;
	}
	
	public String toString() {
		return "{guid:" + guid + ", projectID:" + projectid + "}";
	}

}
