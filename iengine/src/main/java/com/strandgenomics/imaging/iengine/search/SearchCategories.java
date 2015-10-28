package com.strandgenomics.imaging.iengine.search;

/**
 * categorizes search results based on the fields on which they are matched
 * 
 * @author Anup Kulkarni
 */
public enum SearchCategories {
	RECORD_METADATA {
		@Override
		public String toString()
		{
			return "Record Metadata";
		}
	}, USER_ANNOTATIONS{
		@Override
		public String toString()
		{
			return "User Annotations";
		}
	}, HISTORY{
		@Override
		public String toString()
		{
			return "History";
		}
	}, MISC{
		@Override
		public String toString()
		{
			return "Misc";
		}
	};
	
	private final static String recordMetadataFields[] = { "guid", "projectID", "uploadedBy", "numberOfSlices", "numberOfFrames", "numberOfChannels",
			"numberOfSites", "imageWidth", "imageHeight", "uploadTime", "sourceTime", "creationTime", "acquiredTime", "imageDepth", "xPixelSize",
			"yPixelSize", "zPixelSize", "sourceType", "imageType", "machineIP", "macAddress", "sourceFolder", "sourceFilename", "channelName",
			"siteName" };
	
	private final static String userAnnotationFields[] = {"overlay", "comment", "text_*", "real_*", "int_*", "date_*", "textbox", "attachmentName", "attachmentNotes"};
	
	private final static String history = "history";
	
	/**
	 * returns category of the matched search field
	 * @param fieldName search field
	 * @return category of the matched search field
	 */
	public static SearchCategories getSearchCategory(String fieldName)
	{
		if(fieldName==null || fieldName.isEmpty())
			return MISC;

		if(history.equalsIgnoreCase(fieldName))
			return HISTORY;
		
		for(String metadata:recordMetadataFields)
			if(metadata.equalsIgnoreCase(fieldName))
				return RECORD_METADATA;
		
		for(String annotation:userAnnotationFields)
			if(fieldName.matches(annotation))
				return USER_ANNOTATIONS;
		
		return MISC;
	}
}
