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

/*
 * SearchEngine.java
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
package com.strandgenomics.imaging.iengine.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.NavigationDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.models.ValueCount;

/**
 * navigation and search support per project
 * @author arunabha
 *
 */
public class SearchEngine extends SystemManager {
	
	/**
	 * Valid latin-1 - ISO 8859-1 character sets
	 */
	public static final char[] VALID_SEACRH_CHARACTERS = { 
	  32 ,33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, //32 is space character ' '
	  //Numbers 33 through 47, 58 through 64, 91 through 96, and 123 through 126 are the non-letter 
	  //and non-numerical characters on the keyboard, things like a $ and a *. 
	  48, 49, 50, 51, 52, 53, 54, 55, 56, 57, //Numbers 48 through 57 are the numerals zero through nine. 
	  58, 59, 60, 61, 62, 63, 64, //symbols 
	  //Numbers 65 through 90 are the capital letters A through Z, are not relevant, we do lower case search
	  91, 92, 93, 94, 95, 96, // symbols again
	  97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, //a is 97,  English small case letters
	  111,112,113,114,115,116,117,118,119,120,121,122, //Numbers 97 through 122 are the lower-case letters a through z. 
	  123,124,125,126, //symbols
	  //Number 127 is the delete key entry. 
	  //Numbers 128 through 159 are not in use. 
	  //Number 160 is the space bar. 
	  161,162,163,164,165,166,167,168,169,170,171,172,173,174,175, //additional symbols 
	  176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191, //additional symbols 
	  //192-214 are additional french/german/west-european upper case characters
	  215, //the multiplication symbol
	  //216 - 222 are additional french/german/west-european upper case characters
	  223, //beta
	  224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246, //lower case west european characters
	  247,// division symbol
	  248,249,250,251,252,253,254, //additional lower case special west european characters
	  255 //�
	};
	
	/**
	 * the milliseconds in a day
	 */
	public static final long MILLISECONDS_IN_A_DAY = 24 * 60 * 60 * 1000;
	
	SearchEngine()
	{}

	/**
	 * Returns a list of navigation bins for the specified search condition
	 * @param actorLogin the user doing the search
	 * @param projectName name of the project
	 * @param preConditions pre-existing conditions
	 * @param annotationName the current search field
	 * @param min the minimum value of this search field
	 * @param max the maximum value of this search field
	 * @return a list of navigation bins
	 * @throws DataAccessException 
	 */
	public List<NavigationBin> getNavigationBins(String actorLogin, String projectName, 
			Set<SearchCondition> preConditions, String annotationName, 
			Object min, Object max) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "SearchEngine", "getNavigationBins", "finding navigation bins with "+annotationName);
		
		Project p = SysManagerFactory.getProjectManager().getProject(projectName);
		
		//find the field
		SearchCondition field = toSearchCondition(p.getID(), annotationName, min, max);

		boolean ascending = SysManagerFactory.getUserPreference().isBinsAreInAscendingOrder(actorLogin, p.getName());
		int binSize = SysManagerFactory.getUserPreference().getNumberOfNavigatorBins(actorLogin, p.getName());
		
		List<NavigationBin> bins = null;
		switch(field.getType())
		{
			case Real:
				bins = createRealBins(p.getID(), preConditions, field, ascending, binSize);
				break;
			case Integer:
				bins = createIntBins(p.getID(), preConditions, field, ascending, binSize);
				break;
			case Time:
				bins = createTimeBins(p.getID(), preConditions, field, ascending, binSize);
				break;
			case Text:
				bins = createTextBins(p.getID(), preConditions, field, ascending, binSize);
				break;
		}
		
		//the bin where this field is not defined
		NavigationBin nullBin = getNullBin(p.getID(), preConditions, annotationName);
		if(nullBin != null)
		{
			if(bins == null) 
			{
				bins = new ArrayList<NavigationBin>();
			}
			bins.add( nullBin );
		}
		
		return bins; 
	}

	/**
	 * For Text Bins, the strategy is little different as compared to numbers and date
	 * There are three category of bins
	 * 1. with record count > 1
	 * 2. with record count == 1
	 * 3. with record count == 0 (null bin) 
	 * @param projectID
	 * @param preConditions
	 * @param field
	 * @param ascending
	 * @param binSize
	 * @return
	 * @throws DataAccessException
	 */
	private List<NavigationBin> createTextBins(int projectID, Set<SearchCondition> preConditions, 
			SearchCondition field, boolean ascending, int binSize) throws DataAccessException 
	{
		if(!field.isText())
			throw new IllegalArgumentException("only text fields are valid here "+field);
		
		logger.logp(Level.INFO, "SearchEngine", "createTextBins", "finding navigation bins with "+field);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(projectID);
		List<ValueCount> result = dao.findFixedTextBins(preConditions, field, ascending, binSize);
		
		if(result == null || result.isEmpty()) return null;
		List<NavigationBin> bins = toNavigationBin(preConditions, field, result);
		
		if(result.size() < binSize)
		{
			return bins;
		}
		else
		{
			NavigationBin others = createOthers(projectID, preConditions, field, result, ascending);
			if(others != null) bins.add(others); //add the others
			return bins;
		}
	}

	/**
	 * Returns a list of records satisfying the search conditions of the NavigationBin 
	 * @param bin the navigation bin under consideration
	 * @return list of records satisfying the search conditions of the NavigationBin 
	 * @throws DataAccessException 
	 */
	public long[] getRecords(String actorLogin, String projectName, Set<SearchCondition> conditions) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		Project p = SysManagerFactory.getProjectManager().getProject(projectName);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(p.getID());
		
		return dao.getRecordGuids(conditions, null);
	}
	
	public long[] getRecords(String actorLogin, String projectName, Set<SearchCondition> conditions, int limit) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		Project p = SysManagerFactory.getProjectManager().getProject(projectName);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(p.getID());
		
		return dao.getRecordGuids(conditions, new Integer(limit));
	}

	private List<NavigationBin> createTimeBins(int projectID, 
			Set<SearchCondition> preConditions, SearchCondition field, 
			boolean ascending, int noOfBins) throws DataAccessException
	{
		logger.logp(Level.INFO, "SearchEngine", "createTimeBins", "finding navigation bins with "+field);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(projectID);
		
		//find the count
		int noOfRecords = dao.getRecordCount(preConditions, field);
		if(noOfRecords == 0) return null;
		//find the minimum
		Timestamp minValue = Util.getTimestamp(dao.findMinimum(preConditions, field));
		//find the maximum
		Timestamp maxValue = Util.getTimestamp(dao.findMaximum(preConditions, field));		
		
		logger.logp(Level.INFO, "SearchEngine", "createTimeBins", "finding navigation bins, noOfRecords="+noOfRecords +", minValue="+minValue +", maxValue="+maxValue);
		
		double dayWidth = (maxValue.getTime() - minValue.getTime())/(double)MILLISECONDS_IN_A_DAY;
		
		if(dayWidth <= 1.0 || noOfRecords == 1)
		{
			logger.logp(Level.INFO, "SearchEngine", "createTimeBins", "creating one bin, noOfRecords="+noOfRecords +", minValue="+minValue +", maxValue="+maxValue);
			
			NavigationBin bin = new NavigationBin(preConditions, field.getField(), minValue, maxValue);
			bin.setRecordCount(noOfRecords);
			
			List<NavigationBin> bins = new ArrayList<NavigationBin>();
			bins.add( bin );
			return bins;
		}
		
		//reset minimum and maximum time to the 00:00 hours of the day
		Calendar minTime = resetToDay(minValue);
		Calendar maxTime = resetToDay(maxValue);

		//for maximum, set to the 00:00:00 hours next day
		maxTime.set(Calendar.DAY_OF_YEAR, maxTime.get(Calendar.DAY_OF_YEAR)+1);
		
		//24 hour day is the minimum width, width w.r.t integral number of days
		int noOfDays = (int) Math.ceil( (maxTime.getTimeInMillis() - minTime.getTimeInMillis())/(double)MILLISECONDS_IN_A_DAY );
		//for open interval - the higher end is open, non inclusive
		double binWidth = (double)noOfDays/ (double)noOfBins;
		binWidth = binWidth < 1 ? 1 : binWidth;  //minimum bin width is one day
		
		logger.logp(Level.INFO, "SearchEngine", "createTimeBins", "creating bins, noOfDays="+noOfDays +", binWidth="+binWidth);
		
		int[] lowerDayLimits = new int[noOfBins+1];
		for(int i = 0;i <= noOfBins; i++) //calculate the lower limits first
		{
			lowerDayLimits[i] = (int)Math.floor( i * binWidth ); //calculate in terms of no.of days from minTime
		}

		List<NavigationBin> bins = new ArrayList<NavigationBin>();

		for(int i = 0;i < noOfBins; i++)
		{
			//the bins are closed intervals, both values are inclusive 
			int upperDayLimit = lowerDayLimits[i+1]; 
			if(upperDayLimit > noOfDays || i == noOfBins-1) upperDayLimit = noOfDays;
			
			//now calculate the timestamp
			Timestamp lowerLimit = new Timestamp(minTime.getTimeInMillis() + lowerDayLimits[i] * MILLISECONDS_IN_A_DAY);
			//so upper limit of the previous interval is ONE milli-sec less then the lower limit of the preceeding interval
			Timestamp upperLimit = new Timestamp(minTime.getTimeInMillis() + upperDayLimit * MILLISECONDS_IN_A_DAY - 1);
			
			int binSize = dao.getRecordCountForClosedInterval(preConditions, field, lowerLimit, upperLimit);
			
			logger.logp(Level.INFO, "SearchEngine", "createTimeBins", "creating one bin, binSize="+binSize +", lowerLimit="+lowerLimit +", upperLimit="+upperLimit);
			
			if(binSize > 0) //add the bin if it contains something
			{
				NavigationBin bin = new NavigationBin(preConditions, field.getField(), lowerLimit, upperLimit);
				bin.setRecordCount( binSize );
				bins.add( bin );
			}
			//the next non-overlapping range
			if(upperDayLimit == noOfDays) break;
		}
		
		return bins;
	}

	private List<NavigationBin> createIntBins(int projectID, 
			Set<SearchCondition> preConditions, SearchCondition field, 
			boolean ascending, int noOfBins) throws DataAccessException 
	{
		logger.logp(Level.INFO, "SearchEngine", "createIntBins", "finding navigation bins with "+field);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(projectID);
		
		//find the count
		int noOfRecords = dao.getRecordCount(preConditions, field);
		if(noOfRecords == 0) return null;
		//find the minimum
		long minValue = Util.getLong(dao.findMinimum(preConditions, field));
		//find the maximum
		long maxValue = Util.getLong(dao.findMaximum(preConditions, field));	
		
		if(minValue == maxValue || noOfRecords == 1)
		{
			NavigationBin bin = new NavigationBin(preConditions, field.getField(), minValue, maxValue);
			bin.setRecordCount(noOfRecords);
			
			List<NavigationBin> bins = new ArrayList<NavigationBin>();
			bins.add( bin );
			return bins;
		}
		
		//for closed interval, all ends are inclusive
		double width = (maxValue - minValue + 1)/(double)noOfBins;
		width = width < 1 ? 1 : width; 
		
		long[] lowerLimits = new long[noOfBins+1];
		for(int i = 0;i <= noOfBins; i++) //calculate the lower limits first
		{
			lowerLimits[i] = minValue + (long)Math.floor( i * width );
		}

		List<NavigationBin> bins = new ArrayList<NavigationBin>();

		for(int i = 0;i < noOfBins; i++)
		{
			//the bins are closed intervals, both values are inclusive 
			//so upper limit of the previous interval is one less then the lower limit of the preceeding interval
			long upperLimit = lowerLimits[i+1] - 1; 
			if(upperLimit > maxValue || i == noOfBins-1) upperLimit = maxValue;
			
			int binSize = dao.getRecordCountForClosedInterval(preConditions, field, lowerLimits[i], upperLimit);
			if(binSize > 0) //add the bin if it contains something
			{
				NavigationBin bin = new NavigationBin(preConditions, field.getField(), lowerLimits[i], upperLimit);
				bin.setRecordCount( binSize );
				bins.add( bin );
			}
			//the next non-overlapping range
			if(upperLimit == maxValue) break;
		}
		
		return bins;
	}

	private List<NavigationBin> createRealBins(int projectID, 
			Set<SearchCondition> preConditions, SearchCondition field, 
			boolean ascending, int noOfBins) throws DataAccessException 
	{
		logger.logp(Level.INFO, "SearchEngine", "createRealBins", "finding navigation bins with "+field);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(projectID);
		
		//find the count
		int noOfRecords = dao.getRecordCount(preConditions, field);
		if(noOfRecords == 0) return null;
		//find the minimum
		double minValue = Util.getDouble(dao.findMinimum(preConditions, field));
		//find the maximum
		double maxValue = Util.getDouble(dao.findMaximum(preConditions, field));
				
		if(minValue == maxValue || noOfRecords == 1)
		{
			maxValue = maxValue + maxValue/100; //to get a number higher than the
			minValue = minValue - minValue/100; //to get a number lesser than min , precession errors in mysql
			
			NavigationBin bin = new NavigationBin(preConditions, field.getField(), minValue, maxValue );
			bin.setRecordCount(noOfRecords);
			
			List<NavigationBin> bins = new ArrayList<NavigationBin>();
			bins.add( bin );
			return bins;
		}
		
		//double percentageChange = maxValue/(maxValue - minValue);
		//for open interval - the higher end is open, non inclusive
		double width = (maxValue - minValue)/(double)noOfBins;

		List<NavigationBin> bins = new ArrayList<NavigationBin>();
		
		double lowerLimit = minValue;
		double upperLimit = lowerLimit;
		int binSize = 0;

		for(int i = 1;i <= noOfBins; i++)
		{
			upperLimit = lowerLimit + width;
			if(upperLimit >= maxValue || i == noOfBins) 
			{
				upperLimit = maxValue + maxValue/100; //XXX: to get a number higher than the
			}
			
			if(i == 1) //the first bin
			{
				lowerLimit = lowerLimit - lowerLimit/100; //XXX hack, precession issue with mysql
			}
			
			binSize = dao.getRecordCountForOpenInterval(preConditions, field, lowerLimit, upperLimit);

			if(binSize > 0) //add the bin if it contains something
			{
				NavigationBin bin = new NavigationBin(preConditions, field.getField(), lowerLimit, upperLimit);
				bin.setRecordCount( binSize );
				bins.add( bin );
			}
			//the next non-overlapping range
			lowerLimit = upperLimit;
			if(upperLimit >= maxValue) break;
		}
		
		return bins;
	}
	
	private Calendar resetToDay(Timestamp t)
	{
		Calendar newTime = Calendar.getInstance();
		newTime.setTimeInMillis(t.getTime()); 
		newTime.set(Calendar.HOUR_OF_DAY, 0);
		newTime.set(Calendar.MINUTE, 0);
		newTime.set(Calendar.SECOND, 0);
		newTime.set(Calendar.MILLISECOND, 0);
		
		return newTime;
	}
	
	private SearchCondition toSearchCondition(int projectID, String annotationName, 
			Object min, Object max) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(projectID);
		//find the field
		SearchColumn field = dao.findFieldForAnnotationName(annotationName);
		if(field == null) throw new NullPointerException("search field not found "+annotationName);
		
		SearchCondition sc = null;
		switch(field.getType())
		{
			case Integer:
				sc =  new SearchCondition(annotationName, Util.getLong(min), Util.getLong(max));   
				break;
			case Real:
				sc =  new SearchCondition(annotationName, Util.getDouble(min), Util.getDouble(max));   
				break;
			case Text:
				sc =  new SearchCondition(annotationName, (String)min, (String)max);   
				break;
			case Time:
				sc =  new SearchCondition(annotationName, Util.getTimestamp(min), Util.getTimestamp(max));   
				break;
		}
		
		return sc;
	}
	
	private NavigationBin createOthers(int projectID, Set<SearchCondition> preConditions,
			SearchCondition field, List<ValueCount> result, boolean ascending) throws DataAccessException 
	{
		logger.logp(Level.INFO, "SearchEngine", "createOthers", "finding others bin with "+field);
		
		if(result == null || result.isEmpty())
			return null;
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(projectID);
		//find the count
		int noOfRecords = toRecordCount(result);
		int totalNoOfRecords = dao.getRecordCount(preConditions, field);
		
		//create the "others" bin
		if(totalNoOfRecords <= noOfRecords)
		{
			return null;// not needed
		}
		
		int noOfRecordsInOthers = totalNoOfRecords - noOfRecords;
		String lastValue = result.get(result.size()-1).value;
		
		SearchCondition others = new SearchCondition(field.getField(), 
				ascending ? lastValue : null, ascending ? null : lastValue);
		
		String min = (String) dao.findMinimum(preConditions, others);
		String max = (String) dao.findMaximum(preConditions, others);
	
		NavigationBin bin = new NavigationBin(preConditions, field.getField(), min, max);
		bin.setRecordCount(noOfRecordsInOthers);
		return bin;
	}

	private List<NavigationBin> toNavigationBin(Set<SearchCondition> preConditions, 
			SearchCondition field, List<ValueCount> result) 
	{
		if(result == null || result.isEmpty()) return null;
		
		List<NavigationBin> bins = new ArrayList<NavigationBin>();
		for(ValueCount vc : result)
		{
			NavigationBin bin = new NavigationBin(preConditions, field.getField(), vc.value, vc.value);
			bin.setRecordCount(vc.count);
			
			bins.add( bin );
		}
		
		return bins;
	}
	
	private int toRecordCount(List<ValueCount> result) 
	{
		if(result == null || result.isEmpty()) return 0;
		int noOfRecords = 0;
		
		for(ValueCount vc : result)
		{
			noOfRecords += vc.count; 
		}
		
		return noOfRecords;
	}
	
	private NavigationBin getNullBin(int projectID, Set<SearchCondition> preConditions, String annotationName) 
			throws DataAccessException
	{
		logger.logp(Level.INFO, "SearchEngine", "getNullBin", "finding null bins with "+annotationName);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(projectID);
		//find the field
		SearchColumn field = dao.findFieldForAnnotationName(annotationName);
		int noOfRecords =  dao.getRecordCountForNullValue(preConditions, field);
		
		if(noOfRecords == 0) return null;

		NavigationBin bin = new NavigationBin(preConditions, field);
		bin.setRecordCount(noOfRecords);
		
		return bin;
	}
	
	public static void main(String ... args) throws Exception
	{}
}
