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
