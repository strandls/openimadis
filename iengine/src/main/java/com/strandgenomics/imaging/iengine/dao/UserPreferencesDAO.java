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

package com.strandgenomics.imaging.iengine.dao;

import java.sql.Timestamp;
import java.util.List;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.LegendField;
import com.strandgenomics.imaging.iengine.models.LegendLocation;
import com.strandgenomics.imaging.iengine.models.SearchColumn;

/**
 * Gives access to user preferences about navigator fields and projects
 */
public interface UserPreferencesDAO {
	
	public Timestamp getLastAccessTime(String userLogin, int projectID) 
			throws DataAccessException;

	public int getNumberOfNavigatorBins(String userLogin, int projectID) 
			throws DataAccessException;
	
	public boolean isBinsAreInAscendingOrder(String userLogin, int projectID) 
			throws DataAccessException;
	
	public List<SearchColumn> getSpreadSheetColumns(String userLogin, int projectID) 
			throws DataAccessException;
	
	public List<SearchColumn> getNavigationColumns(String userLogin, int projectID) 
			throws DataAccessException;

	public List<SearchColumn> getOverlayColumns(String userLogin, int projectID) 
			throws DataAccessException;
	
	public int[] getRecentProjects(String userLogin, int maxProjects) 
			throws DataAccessException;
	
	public void setSpreadSheetColumns(String userLogin, int projectID, List<SearchColumn> columns) 
			throws DataAccessException;
	
	public void setNavigationColumns(String userLogin, int projectID, List<SearchColumn> columns) 
			throws DataAccessException;
	
	public void setOverlayColumns(String userLogin, int projectID, List<SearchColumn> columns) 
			throws DataAccessException;

	public void updateProjectUsage(String userLogin, int projectID)
			throws DataAccessException;
	
	public void createDefaultPreference(String userLogin, int projectID);

	/**
	 * Returns the rgb colors for the channels for the specified record
	 * @param userLogin login id of the user
	 * @param guid record GUID
	 * @return list of colors for the channels
	 * @throws DataAccessException
	 */
	public List<Channel> getChannels(String userLogin, long guid) throws DataAccessException;

	/**
	 * Adds the rgb colors for the channels for the specified record
	 * @param userLogin login id of the user
	 * @param guid record GUID
	 * @return list of colors for the channels
	 * @throws DataAccessException
	 */
	public void addChannels(String userLogin, long guid, List<Channel> channels) throws DataAccessException;
	
	/**
	 * Update the rgb colors for the channels for the specified record
	 * @param userLogin login id of the user
	 * @param guid record GUID
	 * @return list of colors for the channels
	 * @throws DataAccessException
	 */
	public void updateChannels(String userLogin, long guid, List<Channel> channels) throws DataAccessException;

	/**
	 * updates the name of channel to new name given for all users
	 * @param guid of specified record
	 * @param channelNo of specified channel
	 * @param newChannelName new name to be given for the channel
	 */
	public void updateChannels(long guid, List<Channel> channels) throws DataAccessException;
	
	/**
	 * Create a query to delete preference relationships from user_recent_projects table
	 * @param projectID int value used as id for the project just deleted
	 * @param userLogin string value used as the login id of the user who has been removed from the project
	 * @throws DataAccessException5:13:11 PM
	 */
	public void deleteUserPreferences(int projectID , String userLogin ) throws DataAccessException ;

	/**
	 * sets set of legends for specified user
	 * @param actorLogin user login
	 * @param chosenFields specified user
	 * @throws DataAccessException 
	 */
	public void setLegendFields(String actorLogin, List<LegendField> chosenFields) throws DataAccessException;

	/**
	 * gets the chosen legend fields for specified user
	 * @param actorLogin user login
	 * @return list of specified legends for specified user
	 */
	public List<LegendField> getLegends(String actorLogin) throws DataAccessException;

	/**
	 * updates the set of legends for specified user
	 * @param actorLogin user login
	 * @param chosenFields specified legend fields
	 * @throws DataAccessException
	 */
	public void updateLegendFields(String actorLogin, List<LegendField> chosenFields) throws DataAccessException;

	/**
	 * get the legend location for specified user
	 * @param actorLogin specified user
	 * @return legend location
	 * @throws DataAccessException
	 */
	public LegendLocation getLegendLocation(String actorLogin) throws DataAccessException;
	
	/**
	 * set the legend location for specified user
	 * @param actorLogin specified user
	 * @param location legend location
	 * @throws DataAccessException
	 */
	public void setLegendLocation(String actorLogin, LegendLocation location) throws DataAccessException;
}
