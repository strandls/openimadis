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

package src.main.java.com.strandgenomics.imaging.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

//import com.gargoylesoftware.htmlunit.javascript.host.Map;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.ImageSpaceSystem;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.util.Uploader;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.db.DataAccessException;

public class Upload {
	String runType;
	String projectName;
	String sourcePath;
	String reportPath;
	String recordInfoPath;
	String benchmarkingReportPath;
	ReportWriter repWriter;
	String value_RecordInfo; // value to be written in record info file
	String value_Benchmarking; // value to be written in benchmarking file
	List<String> uploadTimeStringList = new ArrayList<String>();

	public Upload() throws IOException{
		GetPropertyValues PropVal = new GetPropertyValues("/home/gs/Eclipse_dir/iManage_automation/config_properties/config.properties");
		runType = System.getProperty("runType");//PropVal.getPropValues("runType");
		projectName = System.getProperty("project_name");//PropVal.getPropValues("project_name");
		sourcePath = System.getProperty("source_path");//PropVal.getPropValues("source_path");
		reportPath = System.getProperty("report_path");//PropVal.getPropValues("report_path");
		recordInfoPath = System.getProperty("recordInfo_path");//PropVal.getPropValues("recordInfo_path");
		benchmarkingReportPath = System.getProperty("benchmarkingReportPath");//PropVal.getPropValues("benchmarkingReportPath");
		repWriter = new ReportWriter(recordInfoPath+"_"+runType);
	}

	// this metohod will upload all the records recursively from the given sourcePath
	public void uploadFiles()throws DataAccessException, IOException, InterruptedException {
		uploadFiles1();
		System.out.println("");
		System.out.println("######### Uploading finished..... ##########");
		writeUpoadBenchmarkingResults();
		repWriter.closeFileWriters();
		if ("test".equals(runType)){
			FileComparator.sortFile(recordInfoPath+"_test");
			FileComparator.sortFile(recordInfoPath+"_gold");
			int flag = FileComparator.compareFiles(recordInfoPath+"_test_sorted", recordInfoPath+"_gold_sorted");
			System.out.println("flag: "+ flag);
			ReportWriter repWriter1 = new ReportWriter(reportPath);
			if (flag==1){
				repWriter1.writeToReport("Upload"+"\t"+"PASS");
				repWriter1.closeFileWriters();
			}else{
				repWriter1.writeToReport("Upload"+"\t"+"FAIL");
				repWriter1.closeFileWriters();
			}
		}
		
		
		
	}

	private void uploadFiles1() throws IOException, InterruptedException,
			DataAccessException {
		File file = new File(sourcePath);
		File [] files = file.listFiles();
		int filesCount = files.length;
		System.out.println(filesCount);

		for (File f:files){
			if (f.isFile()){
				System.out.println("");
				System.out.println("**************************************");
				System.out.println(f);

				Project project1 = ImageSpaceObject.getImageSpace().findProject(projectName);
				int recordCount_BeforeUpload = project1.getRecordCount();

				System.out.println("recordCount_BeforeUpload: "+recordCount_BeforeUpload);

				File sourceFile = f;
				value_RecordInfo = f.getName();
				System.out.println("Sourcefile Exists: " + sourceFile.exists());

				// upload start
				long uploadStartTime = System.currentTimeMillis();
				System.out.println("uploadStartTime: "+uploadStartTime);
				RawExperiment experiment = null;
				try{
					experiment = new RawExperiment(sourceFile);				
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
					String uploadTimeString = f.getAbsolutePath()+ "\t" +f.getName()+"\t"+ uploadStartTime +"\t"+ "NULL"+"\t"+ "NULL"+"\t"+"NULL"+"\t"+"NULL"+"\t"+"FAIL"+"\n";
					uploadTimeStringList.add(uploadTimeString);
					continue;
				}

				if (experiment != null){
					try {
						experiment.extractRecords(null, null);
					} catch (Exception e) {
						System.out.println(e.getMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
						String uploadTimeString = f.getAbsolutePath()+ "\t" +f.getName()+"\t"+ f.length()+"\t"+ uploadStartTime +"\t"+ "NULL"+"\t"+ "NULL"+"\t"+"NULL"+"\t"+"NULL"+"\t"+"FAIL"+"\n";
						uploadTimeStringList.add(uploadTimeString);
						continue;
					} 


					// Check if this file already exists on the server ..
					boolean serverStatus = experiment.isExistOnServer();
					System.out.println("Exists on Server: " + serverStatus);
					if(serverStatus){
						System.out.println("File: " + experiment.getSourceFilename() + " exists on Server: ");
						String uploadTimeString = f.getAbsolutePath()+ "\t" +f.getName()+"\t"+ f.length()+"\t"+uploadStartTime +"\t"+ "NULL"+"\t"+ "NULL"+"\t"+"NULL"+"\t"+"NULL"+"\t"+"Exists on Server"+"\n";
						uploadTimeStringList.add(uploadTimeString);
					}
					else{
						// If doesn't exist on server.. upload
						Uploader uploader = project1.getUploader(experiment);
						// Create a tar ..
						File tarFile = uploader.packSourceFiles();
						System.out.println("Tarfile Exists: " + tarFile.exists());
						if(tarFile!=null)
							tarFile.deleteOnExit();
						System.out.println("**********tarFile.deleteOnExit() executed");
						// Create a server ticket to upload
						uploader.fetchTicket();
						System.out.println("********** uploader.fetchTicket() executed");
						try {
							// upload the source files
							uploader.uploadSourceFiles(tarFile, null);
							System.out.println("********** uploader.uploadSourceFiles(tarFile, null) executed");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// upload the record fields 
						uploader.uploadRecordFields(null);
						System.out.println("********** uploader.uploadRecordFields(null) executed");

						// If ticket status is true, upload is completed. Else sleep for 1 second.
						if (uploader.monitorTicketStatus()){
							System.out.println("Done with Upload");
							long uploadEndTime = System.currentTimeMillis();
							System.out.println("uploadEndTime: "+uploadEndTime);
							// upload ends....
							long uploadTime_milli = uploadEndTime - uploadStartTime;
							double uploadTime_sec = uploadTime_milli/1000.0;
							Project project2 = ImageSpaceObject.getImageSpace().findProject(projectName);

							int recordCount_AfterUpload = project2.getRecordCount();
							System.out.println("recordCount_AfterUpload: "+recordCount_AfterUpload);

							int recordCount = recordCount_AfterUpload - recordCount_BeforeUpload;
							value_RecordInfo = value_RecordInfo+"\t"+recordCount;
							System.out.println("recordCount: "+recordCount);
							String uploadTimeString = f.getAbsolutePath()+ "\t" +f.getName()+"\t"+ f.length()+"\t"+ uploadStartTime +"\t"+ uploadEndTime+"\t"+ uploadTime_milli+"\t"+uploadTime_sec+"\t"+recordCount+"\t"+"PASS"+"\n";
							uploadTimeStringList.add(uploadTimeString);

							writeMetadataForUploadedRecords(f.getName(),project2, recordCount, runType);

						}else{
							Thread.sleep(1000);
						}
					}
				}

			}else if (f.isDirectory()){
				sourcePath = f.getAbsolutePath();
				uploadFiles1();
			}
		}
	}
	
	public void writeUpoadBenchmarkingResults() throws IOException{
		ReportWriter reportWriter2 = new ReportWriter(benchmarkingReportPath);
		for (String item:uploadTimeStringList){
			reportWriter2.writeToReport(item);
		}
		reportWriter2.closeFileWriters();
	}

	public void writeMetadataForUploadedRecords(String filename, Project project, int recordCount, String runType) throws IOException{
		// this method will fetch record ids generated by each source file
		List<Long> recordIdsAdded = getRecordIdsAdded(project, recordCount);
		for (long recordId:recordIdsAdded){
			String recordSignature = getRecordSignature(recordId);
			String recordMetadata = getRecordMetadata(recordId);
			String metaDataValue = filename+"\t"+recordSignature+"\t"+recordMetadata;
			repWriter.writeToRecordsInfo(metaDataValue);
		}
	}

	public List<Long> getRecordIdsAdded(Project project, int recordCount){
		List<Long> recordIds = new ArrayList<Long>();
		long allRecordsIds[] = project.getRecords();
		int allRecordsCount = allRecordsIds.length;		
		Arrays.sort(allRecordsIds);

		int j=0;
		for (int i=allRecordsCount-recordCount; i< allRecordsCount;i++){
			recordIds.add(j, allRecordsIds[i]);
			System.out.println(allRecordsIds[i]);
			j++;
		}
		return recordIds;
	}
	
	// not using this recordId to signature map for now....
	public LinkedHashMap<Long,String> createRecordIdToSignatureMap(List<Long> recordIdsAdded){
		LinkedHashMap<Long,String> recordIdToSignatureMap = new LinkedHashMap<Long,String>();
		for (long recordId:recordIdsAdded){
			recordIdToSignatureMap.put(recordId, getRecordSignature(recordId));
		}
		return recordIdToSignatureMap;
	}

	public String getRecordSignature(long recordId){
		long guid = recordId;
		Record record = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
		Signature sign = record.getSignature();
		String signature = sign.toString()+ sign.getSiteHash(); /* adding getSiteHash to signature string, bcz its missing in toString method
		of Signature class */
		return signature;
	}
	
	public String getRecordMetadata(long recordId){
		long guid = recordId;
		Record record = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
		int sliceCount = record.getSliceCount();
		int frameCount = record.getFrameCount();
		int channelCount = record.getChannelCount();
		int siteCount = record.getSiteCount();
		int imageWidth = record.getImageWidth();
		int imageheight = record.getImageHeight();
		String imageType = record.getImageType().toString();
		String sourceFile = record.getSourceFilename();
		String recordMetadata = sliceCount+"\t"+frameCount+"\t"+channelCount+"\t"+siteCount+"\t"+imageWidth+"\t"+imageheight+"\t"+imageType+"\t"+sourceFile;
		return recordMetadata;
	}

}
