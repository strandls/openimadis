package com.strandgenomics.imaging.iengine.bioformats;

import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;

/**
 * factory class to create record extractor depending upon request type
 * 
 * @author Anup Kulkarni
 */
public class RecordExtractorFactory {

	public static RecordExtractor getExtractor(RecordCreationRequest context)
	{
		if(context.isDirectUploadRequest())
			return new DirectUploadRecordExtractor(context);
		return new RecordExtractor(context);
	}

}
