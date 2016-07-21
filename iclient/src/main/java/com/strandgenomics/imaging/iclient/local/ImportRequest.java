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

package com.strandgenomics.imaging.iclient.local;

import java.io.File;

/**
 * Handles import - e.g., file/folder based imports, or a generic pattern based imports
 * @author arunabha
 *
 */
public interface ImportRequest {
	
	/**
	 * Returns the root directory or a specific file chosen for import 
	 * @return  the root directory or a specific file chosen for import 
	 */
	public File getRoot();
	
	/**
	 * Starts the import
	 */
	public void start();
	
	/**
	 * Stops the import
	 */
	public void stop();
	
	/**
	 * add the specified index listener to this importer
	 * @param listener the listener to add
	 */
	public void addIndexerListener(IndexerListener listener) ;
	
	/**
	 * removes the specified listener from this importer
	 * @param listener the listener to remove
	 */
	public void removeIndexerListener(IndexerListener listener); 
}
