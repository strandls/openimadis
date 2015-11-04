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
