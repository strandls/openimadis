package com.strandgenomics.imaging.iviewer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.SearchField;

public class RecordPropertyManager {
	public static Map<File, String> addAttachments(){
		AttachmentDialog addAttachment = new AttachmentDialog(null);
		
		File file = null;
		String notes  = null;
		addAttachment.setVisible(true);
		addAttachment.setSize(400,100);
		Map<File, String> property = new HashMap<File, String>();
		if(addAttachment.isSucceeded()){
			file = addAttachment.getFile();
			notes = addAttachment.getNotes();
			property.put(file, notes);
		}
		return property;
	}
	
	public static Map<String, Object> addUserAnnotations(List <SearchField> searchFields){
		PropertyDialog addProperty = new PropertyDialog(null,true,searchFields);
		addProperty.setVisible(true);
		
		Object value = null;
		String name  = null;
		AnnotationType type = null;
		
		if(addProperty.isSucceeded()){
			name = addProperty.getPropertyName();
			value = addProperty.getPropertyValue();
			type = addProperty.getPropertyType();
		}
		Map<String, Object> property = new HashMap<String, Object>();
		property.put("key", name);
		property.put("value", value);
		property.put("type", type);
		return property;
	}
	
	public static String removeUserAnnotations(List <SearchField> searchFields){
		PropertyDialog addProperty = new PropertyDialog(null,false,searchFields);
		addProperty.setVisible(true);
		String name  = null;
		if(addProperty.isSucceeded()){
			name = addProperty.getPropertyName();
		}
		return name;
	}
}
