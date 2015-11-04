package com.strandgenomics.imaging.iviewer.dataobjects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentsModel extends AbstractAnnotationModel{
	
	
	public CommentsModel(String key, String value, String user){
		
		this.value = value;
		this.user = user;
		this.key = key;
		this.date = Calendar.getInstance().getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy 'at' HH:mm:ss z");
		this.timeStamp = sdf.format(this.date);
	}
	
}
