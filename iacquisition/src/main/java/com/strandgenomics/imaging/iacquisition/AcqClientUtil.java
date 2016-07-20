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

package com.strandgenomics.imaging.iacquisition;

/**
 * used by Acq Client for frequently used utility methods
 * 
 * @author Anup Kulkarni
 */
public class AcqClientUtil {

	/**
	 * generate status message depending upon user and indexing conditions
	 * @return 
	 */
	public static String generateStatus(String user, int recordCnt, String indexingNote, String misc) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Logged in as : ");
		sb.append(user);
		sb.append(" - ");
		sb.append("No. of Records : ");
		sb.append(recordCnt);
		sb.append(" ");
		sb.append(indexingNote);
		sb.append(" ");
		sb.append(misc);
		
		return sb.toString();
	}
}
