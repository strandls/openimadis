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

/**
 * 
 */
package com.strandgenomics.imaging.iacquisition.selection;


/**
 * @author nimisha
 *
 */
public class RecordsChangedEvent {

    
    public static final int RECORDS_ADDED=1;
    public static final int RECORDS_REMOVED=2;
    public static final int ANNOTATIONS_CHANGED=3;
    public static final int ANNOTATION_ADDED=4;
    public static final int ANNOTATION_REMOVED=5;
    
    int type;
    String key;

    /**
     * Create an event setting passed records as selected
     * @param records
     */
    public RecordsChangedEvent(int type) {
        this(type,null);
    }
    

    public RecordsChangedEvent(int type, String key) {
    	this.type = type;
        this.key = key;
	}

	public int getType(){
    	return type;
    }
    
    public String getField(){
    	return key;
    }
}
