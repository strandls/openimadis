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

package com.strandgenomics.imaging.iclient;

/**
 * client side representation of record bookmarks. a bookmark folder represent
 * hierarchical organization of bookmarked records
 * BookmarkFolder contains children BookmarkFolder and list of records under the BookmarkFolder 
 * 
 * @author Anup Kulkarni
 */
public class BookmarkFolder  extends ImageSpaceObject {
	
	/**
	 * server side representation of path of this folder
	 */
	public final String absolutePath;
	/**
	 * parent project id
	 */
	public final String projectName;
	
	BookmarkFolder(String parentProject, String absolutePath)
	{ 
		this.absolutePath = absolutePath;
		this.projectName = parentProject;
	}
	
	/**
	 * returns all the subfolders for this bookmark folder 
	 * @return all the subfolders
	 */
	public BookmarkFolder[] getSubfolders()
	{
		return getImageSpace().getSubfolders(this);
	}
	
	/**
	 * returns the bookmarked records for this folder
	 * @return the bookmarked records
	 */
	public long[] getBookmarkedRecords()
	{
		return getImageSpace().getBookmarkedRecords(this);
	}
	
	/**
	 * create new folder under current folder
	 * @param newFolder name of the bookmark folder
	 */
	public void createBookmarkFolder(String newFolder)
	{
		getImageSpace().createBookmarkFolder(this, newFolder);
	}
	
	/**
	 * adds bookmark record
	 * @param guid to be bookmarked
	 */
	public void addBookmark(long guid)
	{
		getImageSpace().addBookmark(this, guid);
	}
	
	@Override
	public void dispose()
	{ }
}
