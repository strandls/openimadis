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

import com.strandgenomics.imaging.icore.IRecord;

/**
 * holds the reference of currently selected record which can be accessed via script editor
 * 
 * @author Anup Kulkarni
 *
 */
public class RecordHolder {
	private static IRecord record = null;
	
	public static void setSelectedRecord(IRecord selectedRecord)
	{
		record = selectedRecord;
	}
	
	public static IRecord getSelectedRecord()
	{
		return record;
	}
}
