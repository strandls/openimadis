package com.strandgenomics.imaging.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ReportWriter {
	FileWriter fw;
	BufferedWriter bw;
	private static final String newLine = System.getProperty("line.separator");
	
	public ReportWriter(String filePath) throws IOException{
		fw = new FileWriter(filePath, true);
		bw  = new BufferedWriter(fw);
	}
	
	public void writeToReport(String value) throws IOException{
		bw.write(newLine + value);
	}
	
	public void writeToRecordsInfo(String value) throws IOException{
		bw.write(newLine + value);
	}
	public void closeFileWriters() throws IOException{
		bw.close();
		fw.close();
	}
	
}
