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

package com.strandgenomics.imaging.iengine.worker;

/**
 * enum to represent different types of services which can run on worker
 * @author navneet
 *
 */
public enum ServiceType {
	
	/**
	 * movie creation service
	 */
	MOVIE_SERVICE ("Movie Creation Service"),
	
	/**
	 * record export service
	 */
	EXPORT_SERVICE ("Record Export Service"),
	
	/**
	 * record extraction service
	 */
	EXTRACTION_SERVICE ("Record Extraction Service"),
	
	/**
	 * image tiling service
	 */
	TILING_SERVICE ("Image Tiling Service"),
	
	/**
	 * reindexing service
	 */
	REINDEX_SERVICE ("Reindexing Service"),
	
	/**
	 * backup service
	 */
	BACKUP_SERVICE ("Backup Service"),
	
	/**
	 * cache cleaning service
	 */
	CACHE_CLEANING_SERVICE ("Cache Cleaning Service");
	
	private final String name;
	 
    ServiceType(String name){
        this.name = name;
    }

    public String getName(){
    	return name;
    }
}
