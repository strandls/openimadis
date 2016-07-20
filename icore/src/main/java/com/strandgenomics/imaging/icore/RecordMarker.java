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

package com.strandgenomics.imaging.icore;

/**
 * the status of the record: Active/Deleted/Archived etc
 * 
 * @author Anup Kulkarni
 */
public enum RecordMarker {

	/**
	 * record is active in the system
	 */
	Active {
		public String toString()
		{
			return "Active";
		}
	},
	/**
	 * record is deleted from the system
	 */
	Deleted {
		public String toString()
		{
			return "Deleted";
		}
	},
	/**
	 * record is archived
	 */
	Archived {
		public String toString()
		{
			return "Archived";
		}
	},
	/**
	 * record is under construction
	 */
	UNDER_CONSTRUCTION {
		public String toString()
		{
			return "Under Construction";
		}
	}
}
