/*
 * SearchColumn.java
 *
 * AVADIS Image Management System
 * Core Engine
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iengine.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Definition of a search column
 * @author arunabha
 *
 */
public class SearchColumn extends SearchField {

	private static final long serialVersionUID = 72867934968071913L;
	/**
	 * list of reserved or illegal words
	 */
	private static Set<String> reservedWords = null;
	
	/**
	 * name of the column in the navigation table
	 */
	public final String columnName;

	/**
	 * Create a search-column
	 * @param fieldName
	 * @param fieldType
	 */
	public synchronized static SearchColumn toSearchColumn(String annotationName, AnnotationType fieldType) 
	{
		String columnName = getSanitizedName(annotationName);
		return new SearchColumn(annotationName, fieldType, columnName);
	}
	
	/**
	 * Create a search-column
	 * @param fieldName
	 * @param fieldType
	 * @param columnName
	 */
	public SearchColumn(String fieldName, AnnotationType fieldType, String columnName) 
	{
		super(fieldName, fieldType);
		this.columnName = columnName;
	}
	
	/**
	 * Returns the actual columns name
	 * @return the actual columns name
	 */
	public String getColumn()
	{
		return columnName;
	}
	
	private static String getSanitizedName(String annotationName)
	{
		String columnName = Util.asciiText(annotationName, '_');
		Set<String> illegalWords = getReservedWords();
		
		String finalName = columnName.toLowerCase();
		for(int i = 0; i < 100; i++) //finite loop
		{
			if(illegalWords.contains(finalName))
			{
				finalName = columnName +"_"+i;
			}
			else
			{
				break;
			}
		}
		
		return finalName;
	}
	
	/**
	 * Returns illegal words/reserved database keywords
	 * @return
	 */
	public static Set<String> getReservedWords()
	{
		if(reservedWords == null)
		{
			synchronized(SearchColumn.class)
			{
				if(reservedWords == null)
				{
					Set<String> words = loadIllegalWords();
					reservedWords = words;
				}
			}
		}
		
		return reservedWords;
	}
	
	/**
	 * Loads illegal words/reserved database keywords
	 * @return the set of reserved words
	 */
	private static Set<String> loadIllegalWords()
	{
		Set<String> illegalWords = new HashSet<String>();
		String resourceName = "imagingdao/"+ Constants.getDatabaseType() +"/"+ "reserved-words.txt";
		
		BufferedReader reader = null;
        try {

            ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
            reader = new BufferedReader(new InputStreamReader(contextLoader.getResourceAsStream(resourceName)));

            while(true)
            {
            	String line = reader.readLine();
            	if(line == null) break;
            	line = line.trim().toLowerCase();
            	
            	if(line.length() > 0)
            	{
            		illegalWords.add(line);
            	}
            }
            
            System.out.println("successfully read illegal word list");
        }
        catch (Exception ex) 
        {
        	ex.printStackTrace(System.err);
        }
        finally 
        {
        	try {
				reader.close();
			} 
        	catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        return illegalWords;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof SearchColumn)
		{
			SearchColumn that = (SearchColumn) obj;
			if(this == that) return true;
			
			boolean equals = this.columnName.equals(that.columnName) && this.fieldName.equals(that.fieldName) && this.fieldType.equals(that.fieldType);
			return equals;
		}
		return false;
	}
}
