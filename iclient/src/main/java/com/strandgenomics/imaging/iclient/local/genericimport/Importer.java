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

package com.strandgenomics.imaging.iclient.local.genericimport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.bioformats.custom.FieldType;
import com.strandgenomics.imaging.icore.bioformats.custom.ImageIdentifier;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordIdentifier;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;
import com.strandgenomics.imaging.icore.util.ProgressListener;

/**
 * filename based record importer, where each file represent a image of a specific 
 * record dimension.
 * 
 * @author arunabha
 *
 */
public class Importer implements ProgressListener {
	
	private Logger logger = Logger.getRootLogger();
    /**
     * List of listeners listening for record addition events
     */
	protected EventListenerList listenerList = new EventListenerList();
	
	/**
	 * the separator character to be used to tokenize the fields from the file name
	 */
	protected ImportFilter filter = null;
	
	/**
	 * Create a record importer instance with a specific position to field type table
	 * @param positionToFieldMap  the field token position versus field type table
	 */
	public Importer(ImportFilter filter)
	{
		this.filter = filter;
	}
	
	public void addProgressListener(ProgressListener listener) 
	{
	    listenerList.add(ProgressListener.class, listener);
	}
	
	public void removeProgressListener(ProgressListener listener) 
	{
	    listenerList.remove(ProgressListener.class, listener);
	}
	
    public void fireProgressEvent(String message, int min, int max, int value)
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == ProgressListener.class) 
	    	{
	    		((ProgressListener) listeners[i+1]).reportProgress( message,  min,  max,  value);
	    	}
	    }
    }
    	
	/**
	 * Returns a bunch of record extracted from files within the specified root directory 
	 * @param rootDirectory the root directory under consideration
	 * @return a bunch of records that can be potentially created
	 */
	public Collection<RecordMetaData> generateRecords(File rootDirectory, String seedFileName, boolean doValidation, FieldType multiImageDimension)
	{
		File sourceFiles[] = rootDirectory.listFiles();
		if(sourceFiles == null || sourceFiles.length == 0)
			return null;
		
		System.out.println("Source files before filtering "+sourceFiles.length);
		// restrict source files to only those which have same extension as Seed File
		List<File> extFilteredSourceFiles = new ArrayList<File>();
		String seedFileExtension = seedFileName.substring(seedFileName.lastIndexOf("."));
		for(File sourceFile : sourceFiles)
		{
			String currentExtension = sourceFile.getName().substring(sourceFile.getName().lastIndexOf("."));
			if(seedFileExtension.equals(currentExtension))
				extFilteredSourceFiles.add(sourceFile);
		}
		sourceFiles = extFilteredSourceFiles.toArray(new File[0]);
		System.out.println("Source files after filtering "+sourceFiles.length);
		
		logger.info("[GenericImporter]:\tstarting importer with "+sourceFiles.length +" files");
		Set<ImageIdentifier> images = extractImages(sourceFiles, doValidation, multiImageDimension);
		//group the images as records
		Map<RecordIdentifier, Set<ImageIdentifier>> recordImages = extractRecordImages(images);
		//the records
		List<RecordMetaData> records = new ArrayList<RecordMetaData>();
		
		for(Map.Entry<RecordIdentifier, Set<ImageIdentifier>> entry : recordImages.entrySet())
		{
			RecordIdentifier recordID = entry.getKey();
			Set<ImageIdentifier> imageList = entry.getValue();
			
			logger.info("[GenericImporter]:\tcreating "+recordID);
			records.add( new RecordMetaData(rootDirectory, recordID, imageList) );
		}
		
		//done, report completion
		reportProgress("Found "+records.size() +" records", 0, sourceFiles.length, sourceFiles.length);
		
		return records;
	}

	private Map<RecordIdentifier, Set<ImageIdentifier>> extractRecordImages(Set<ImageIdentifier> images)
	{
		Map<RecordIdentifier, Set<ImageIdentifier>> recordList = new HashMap<RecordIdentifier, Set<ImageIdentifier>>();
		for(ImageIdentifier image : images)
		{
			if(recordList.containsKey(image.recordID))
			{
				recordList.get(image.recordID).add(image);
			}
			else
			{
				Set<ImageIdentifier> record = new HashSet<ImageIdentifier>();
				record.add(image);
				recordList.put(image.recordID, record);
			}
		}
		
		return recordList;
	}

	private Set<ImageIdentifier> extractImages(File[] sourceFiles, boolean doValidation, FieldType multiImageDimension)
	{
		Set<ImageIdentifier> images = new HashSet<ImageIdentifier>();
		//extract all images
		for(int progress = 0; progress < sourceFiles.length; progress++)
		{
			File sourceFile = sourceFiles[progress];
			reportProgress("checking "+sourceFile.getName(), 0, sourceFiles.length, progress);
			
			Set<ImageIdentifier> imageMetaData = filter.getFields(sourceFile, doValidation, multiImageDimension);
			if(imageMetaData == null || imageMetaData.isEmpty()) 
			{
				reportProgress("rejecting "+sourceFile.getName(), 0, sourceFiles.length, progress);
				continue;
			}

			reportProgress("found image file "+sourceFile.getName(), 0, sourceFiles.length, progress);
			images.addAll(imageMetaData);
		}
		
		return images;
	}

	@Override
	public void reportProgress(String message, int min, int max, int value) 
	{
		fireProgressEvent(message, min, max, value);
	}
}
