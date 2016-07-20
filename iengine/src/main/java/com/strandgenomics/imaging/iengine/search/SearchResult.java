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
