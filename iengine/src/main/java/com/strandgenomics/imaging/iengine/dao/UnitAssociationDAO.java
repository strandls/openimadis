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

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.UnitAssociation;
/**
 * 
 * 
 * @author Anup Kulkarni
 */
public interface UnitAssociationDAO {
	/**
	 * associates project with unit
	 * @param unitName specified unit
	 * @param projectName specified project
	 * @param spaceContribution amount of physical storage space contributed by unit in specified project
	 * @throws DataAccessException 
	 */
	public void associateProject(String unitName, int projectId, double spaceContribution) throws DataAccessException;
	/**
	 * removed unit-project association
	 * @param unitName specified unit
	 * @param projectName specified project
	 * @throws DataAccessException 
	 */
	public void removeProject(String unitName, int projectId) throws DataAccessException;
	/**
	 * updates the amount of storage space contributed to project by unit
	 * @param unitName specified unit
	 * @param projectName specified project
	 * @param spaceContribution amount of physical storage space contributed by unit in specified project
	 * @throws DataAccessException 
	 */
	public void updateProjectSpace(String unitName, int projectId, double spaceContribution) throws DataAccessException;
	/**
	 * returns the list of project-unit associations for specified unit
	 * @param unitName specified unit
	 * @return the list of project-unit associations for specified unit
	 * @throws DataAccessException
	 */
	public List<UnitAssociation> getAssociationsForUnit(String unitName) throws DataAccessException;
	/**
	 * returns the list of project-unit associations for specified project
	 * @param projectId specified project
	 * @return the list of project-unit associations for specified project
	 * @throws DataAccessException
	 */
	public List<UnitAssociation> getAssociationsForProject(int projectId) throws DataAccessException;
	/**
	 * returns the association object for unit-project
	 * @param unitName
	 * @param id
	 * @return the association object for unit-project
	 * @throws DataAccessException 
	 */
	public UnitAssociation getAssociation(String unitName, int id) throws DataAccessException;
	/**
	 * returns the list of project-unit associations for specified project/unit
	 * @param unitName
	 * @param projectId
	 * @return
	 * @throws DataAccessException
	 */
	public List<UnitAssociation> getAssociations(String unitName, Integer projectId) throws DataAccessException;
}
