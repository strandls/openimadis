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
