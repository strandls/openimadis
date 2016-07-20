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
 * DBNavigationDAO.java
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
package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.Operator;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.NavigationDAO;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.models.ValueCount;

/**
 * Information about the navigation table for a specific project
 * @author arunabha
 *
 */
public class DBNavigationDAO extends StorageDAO<SearchColumn> implements NavigationDAO {
	
	/**
	 * fixed fields are same for all projects
	 */
	private static final TreeMap<String, SearchColumn> FIXED_FILEDS = populateFixedFields();
	
	/**
	 * annotation name Vs search columns
	 */
	protected TreeMap<String, SearchColumn> annotationNameToFieldMap = null;
	/**
	 * column name Vs search columns
	 */
	protected TreeMap<String, SearchColumn> columnNameToFieldMap = null;
	/**
	 * ID of the project
	 */
	protected final int projectID;

	/**
	 * Creates the navigation table model for the specified project
	 * @param factory the data access factpry
	 * @param connectionProvider the connection provider
	 * @param projectName name of the project
	 * @throws DataAccessException 
	 */
	public DBNavigationDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider, int projectID) 
			throws DataAccessException
	{
		super(factory, connectionProvider, "NavigationDAO.xml");
		this.projectID = projectID;
		//load the name to column maps
		load();
	}
	
	@Override
	public Collection<SearchColumn> getFixedFields()
	{
		return FIXED_FILEDS.values();
	}
	
	@Override
    public Collection<SearchColumn> getUserAnnotationFields() throws DataAccessException
    {
		List<SearchColumn> userAnnotations = new ArrayList<SearchColumn>();
		for(SearchColumn column : annotationNameToFieldMap.values())
		{
			if( !FIXED_FILEDS.containsKey(column.fieldName) )
			{
				userAnnotations.add(column);
			}
		}
		return userAnnotations;
    }
	
	@Override
	public Collection<SearchColumn> getNavigableFields() throws DataAccessException
	{
		return annotationNameToFieldMap.values();
	}
	
	@Override
	public void registerRecord(long guid, String uploadedBy,
			int noOfSlices, int noOfFrames, int noOfChannels, int noOfSites, 
			int imageWidth, int imageHeight,
			Long uploadTime, Long sourceTime, Long creationTime, Long acquiredTime,
			PixelDepth imageDepth, double xPixelSize, double yPixelSize, double zPixelSize,
			SourceFormat sourceType, ImageType imageType, 
			String machineIP, String macAddress,
			String sourceFolder, String sourceFilename) throws DataAccessException
	{
		
		logger.logp(Level.INFO, "DBNavigationDAO", "insertRecord", "inserting record for project "+projectID);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_NEW_RECORD");
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",         guid,            Types.INTEGER);
		sqlQuery.setValue("UserID",       uploadedBy,      Types.VARCHAR);

		sqlQuery.setValue("SliceCount",   noOfSlices,      Types.INTEGER);
		sqlQuery.setValue("FrameCount",   noOfFrames,      Types.INTEGER);
		sqlQuery.setValue("ChannelCount", noOfChannels,    Types.INTEGER);
		sqlQuery.setValue("SiteCount",    noOfSites,       Types.INTEGER);
		
		sqlQuery.setValue("ImageWidth",   imageWidth,      Types.INTEGER);
		sqlQuery.setValue("ImageHeight",  imageHeight,     Types.INTEGER);
		
		sqlQuery.setValue("UploadTime",   new Timestamp(uploadTime.longValue()),   Types.TIMESTAMP);
		sqlQuery.setValue("SourceTime",   new Timestamp(sourceTime.longValue()),   Types.TIMESTAMP);
		sqlQuery.setValue("CreationTime", new Timestamp(creationTime.longValue()), Types.TIMESTAMP);
		sqlQuery.setValue("AcquiredTime", acquiredTime == null ? null : new Timestamp(acquiredTime.longValue()), Types.TIMESTAMP);

		sqlQuery.setValue("ImageDepth", imageDepth.getByteSize(), Types.INTEGER);
		sqlQuery.setValue("PixelSizeX", xPixelSize, Types.DOUBLE);
		sqlQuery.setValue("PixelSizeY", yPixelSize, Types.DOUBLE);
		sqlQuery.setValue("PixelSizeZ", zPixelSize, Types.DOUBLE);
		
		sqlQuery.setValue("SourceType", sourceType.name,  Types.VARCHAR);
		sqlQuery.setValue("ImageType",  imageType.name(), Types.VARCHAR);
		
		sqlQuery.setValue("MachineIP",  machineIP,        Types.VARCHAR);
		sqlQuery.setValue("MacAddress", macAddress,       Types.VARCHAR);

		sqlQuery.setValue("SourceFolder",   sourceFolder, Types.VARCHAR);
		sqlQuery.setValue("SourceFilename", sourceFilename, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void insertUserAnnotation(long guid, MetaData data)
			throws DataAccessException
	{
		if(!annotationNameToFieldMap.containsKey(data.getName()))
		{
			registerUserAnnotation(data.getName(), data.getType());
		}
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_ANNOTATION");
		logger.logp(Level.INFO, "DBNavigationDAO", "insertUserAnnotation", "name="+data.getName());
		
		SearchColumn column = findFieldForAnnotationName(data.getName());
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setParameter("ColumnName", column.columnName);
		
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		sqlQuery.setValue("Value", data.getValue(), data.getType().getSQLType());

		updateDatabase(sqlQuery);	
	}
	
	@Override
	public void insertStorageLocation(long guid, String storageLocation) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_STORAGE_LOCATION");
		logger.logp(Level.INFO, "DBNavigationDAO", "insertStorageLocation", "storageLocation="+storageLocation+" guid="+guid);
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		sqlQuery.setValue("StorageLocation", storageLocation, Types.VARCHAR);

		updateDatabase(sqlQuery);	
	}
	
	@Override
	public void insertMicroscopeName(long guid, String microscopeName) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_MICROSCOPE_NAME");
		logger.logp(Level.INFO, "DBNavigationDAO", "insertMicroscopeName", "microscopeName="+microscopeName+" guid="+guid);
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		sqlQuery.setValue("MicroscopeName", microscopeName, Types.VARCHAR);

		updateDatabase(sqlQuery);
	}

	@Override
	public SearchColumn findFieldForAnnotationName(String annotationName) 
	{
		return annotationNameToFieldMap.containsKey(annotationName) ? annotationNameToFieldMap.get(annotationName) : null;
	}
	
	@Override
	public SearchColumn findFieldForColumnName(String columnName) 
	{
		return columnNameToFieldMap.containsKey(columnName) ? columnNameToFieldMap.get(columnName) : null;
	}
	
	@Override
	public List<ValueCount> findFixedTextBins(Set<SearchCondition> preConditions, SearchCondition field,
			boolean ascending, int maxBins) throws DataAccessException
	{
		SearchColumn column = annotationNameToFieldMap.get(field.getField());
		logger.logp(Level.INFO, "DBNavigationDAO", "findTextValueCount", "column="+column);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_TEXT_BINS");
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setParameter("ColumnName", column.getColumn());
		
		setParameterAndValues(sqlQuery, preConditions);
		setRangeValues(sqlQuery, field, "MinValue", "MaxValue");
		
		sqlQuery.setParameter("SortOrder", ascending ? "ASC" : "DESC");
		sqlQuery.setValue("Offset", 0, Types.INTEGER);
		sqlQuery.setValue("Limit", maxBins, Types.INTEGER);
		
		RowSet<Object[]> result = executeQuery(sqlQuery);
		
		if(result == null || result.isEmpty()) return null;
		return toValueCount(result.getRows());
	}
	
	@Override
	public int getRecordCountForNullValue(Set<SearchCondition> preConditions, SearchField field) throws DataAccessException 
	{
		SearchColumn column = annotationNameToFieldMap.get(field.fieldName);
		
		logger.logp(Level.INFO, "DBNavigationDAO", "getRecordCountForNullValue", "column="+column);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_COUNT_FOR_NULL_BIN");
        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
        sqlQuery.setParameter("ColumnName", column.getColumn());
        setParameterAndValues(sqlQuery, preConditions);

        return getInteger(sqlQuery);
	}

	@Override
	public int getRecordCount(Set<SearchCondition> preConditions, SearchCondition field) throws DataAccessException 
	{
		SearchColumn column = annotationNameToFieldMap.get(field.fieldName);
		
		logger.logp(Level.INFO, "DBNavigationDAO", "getRecordCount", "column="+column);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_COUNT_FOR_BIN");
        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
        sqlQuery.setParameter("ColumnName", column.getColumn());
        
        setParameterAndValues(sqlQuery, preConditions);
        setRangeValues(sqlQuery, field, "MinValue", "MaxValue");

        return getInteger(sqlQuery);
	}

	@Override
	public Object findMinimum(Set<SearchCondition> preConditions, SearchCondition field) throws DataAccessException 
	{
		SearchColumn column = annotationNameToFieldMap.get(field.getField());
		
		logger.logp(Level.INFO, "NavigationTable", "findMinimum", "column="+column);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_MIN_FOR_BIN");
        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
        sqlQuery.setParameter("ColumnName", column.getColumn());
        
        setParameterAndValues(sqlQuery, preConditions);
        setRangeValues(sqlQuery, field, "MinValue", "MaxValue");
        
        return getObject(sqlQuery);
	}

	@Override
	public Object findMaximum(Set<SearchCondition> preConditions, SearchCondition field) throws DataAccessException 
	{
		SearchColumn column = annotationNameToFieldMap.get(field.getField());
		
		logger.logp(Level.INFO, "NavigationTable", "findMaximum", "column="+column);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_MAX_FOR_BIN");
        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
        sqlQuery.setParameter("ColumnName", column.getColumn());
        
        setParameterAndValues(sqlQuery, preConditions);
        setRangeValues(sqlQuery, field, "MinValue", "MaxValue");
        
        return getObject(sqlQuery);
	}

	@Override
	public int getRecordCountForClosedInterval(Set<SearchCondition> preConditions, SearchField field, 
			Timestamp lowerLimit, Timestamp upperLimit) throws DataAccessException 
	{
		return getRecordCountForClosedInterval_(preConditions, field, lowerLimit, upperLimit);
	}

	@Override
	public int getRecordCountForClosedInterval(Set<SearchCondition> preConditions, SearchField field, 
			Long lowerLimit, Long upperLimit) throws DataAccessException 
	{
		return getRecordCountForClosedInterval_(preConditions, field, lowerLimit, upperLimit);
	}

	@Override
	public int getRecordCountForOpenInterval(Set<SearchCondition> preConditions, SearchField field, 
			Double lowerLimit, Double upperLimit) throws DataAccessException 
	{
		return getRecordCountForOpenInterval_(preConditions, field, lowerLimit, upperLimit);
	}
	
	@Override
	public int getRecordCountForClosedInterval(Set<SearchCondition> preConditions, SearchField field, 
			Double lowerLimit, Double upperLimit) throws DataAccessException
	{
		return getRecordCountForClosedInterval_(preConditions, field, lowerLimit, upperLimit);
	}
	
	@Override
	public long[] getRecordGuids(Set<SearchCondition> searchConditions, Integer limit)
			throws DataAccessException 
	{
		if(searchConditions == null || searchConditions.isEmpty())
			return null;
		
		logger.logp(Level.INFO, "DBNavigationDAO", "getRecordGuids", "no of filters is "+searchConditions.size());
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_RECORDS");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		setParameterAndValues(sqlQuery, searchConditions);
		sqlQuery.setValue("Limit", limit, Types.INTEGER);
		
		return getRowsWithLongValues(sqlQuery);
	}
	
	@Override
	public Map<Long, List<MetaData>> getRecordMetaData(Set<Long> guids, SearchColumn ... columns) 
			throws DataAccessException
	{
		if(guids == null || guids.isEmpty() || columns == null || columns.length == 0)
			return null;
		
		logger.logp(Level.INFO, "DBNavigationDAO", "getRecords", "no of filters is "+columns.length);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_RECORDS_FOR_GUID");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		for(int i = 0;i < columns.length; i++)
		{
			sqlQuery.setParameter("ColumnName"+(i+1), columns[i].columnName);
		}
		
		sqlQuery.setValue("GUID", guids, Types.BIGINT);

		List<Object[]> qResult = executeQuery(sqlQuery).getRows();
		
		if(qResult == null || qResult.isEmpty()) return null;
		
		Map<Long, List<MetaData>> result = new HashMap<Long, List<MetaData>>();
		for(Object[] values : qResult)
		{
			long guid = Util.getLong( values[values.length-1] ); //the last columns is the guid
			List<MetaData> valueList = new ArrayList<MetaData>();
			result.put(guid, valueList);
			
			for(int i = 0; i < values.length-1; i++)
			{
				valueList.add( MetaData.createInstance(columns[i].fieldName, values[i]));
			}
		}
		
		return result;
	}
	
	@Override
	public SearchColumn createObject(Object[] columnValues) 
	{
		String annotationName = (String) columnValues[0];
		String columnName = (String) columnValues[1];
		AnnotationType fieldType = AnnotationType.valueOf((String)columnValues[2]);

		return new SearchColumn(annotationName, fieldType, columnName);
	}
	
	private int getRecordCountForClosedInterval_(Set<SearchCondition> preConditions, SearchField field, 
			Object minValue, Object maxValue) throws DataAccessException 
	{
		SearchColumn column = annotationNameToFieldMap.get(field.getField());
		
		logger.logp(Level.INFO, "NavigationTable", "getRecordCountForClosedInterval", "column="+column);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_COUNT_FOR_BIN");

        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
        sqlQuery.setParameter("ColumnName", column.getColumn());
        
        setParameterAndValues(sqlQuery, preConditions);
        
        sqlQuery.setValue("MinValue", minValue, field.getType().getSQLType(), Operator.GREATER_THAN_EQUALS_TO);
        sqlQuery.setValue("MaxValue", maxValue, field.getType().getSQLType(), Operator.LESS_THAN_EQUALS_TO);
        
        return getInteger(sqlQuery);
	}
	
	private int getRecordCountForOpenInterval_(Set<SearchCondition> preConditions, SearchField field, 
			Object minValue, Object maxValue) throws DataAccessException 
	{
		SearchColumn column = annotationNameToFieldMap.get(field.getField());
		
		logger.logp(Level.INFO, "NavigationTable", "getRecordCountForOpenInterval", "column="+column);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("FIND_COUNT_FOR_BIN");
        
        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
        sqlQuery.setParameter("ColumnName", column.getColumn());
        
        setParameterAndValues(sqlQuery, preConditions);
        
        sqlQuery.setValue("MinValue", minValue, field.getType().getSQLType(), Operator.GREATER_THAN_EQUALS_TO);
        sqlQuery.setValue("MaxValue", maxValue, field.getType().getSQLType(), Operator.LESS_THAN);
        
        return getInteger(sqlQuery);
	}
	
	private void save(SearchColumn newColumn) throws DataAccessException
	{
		logger.logp(Level.INFO, "NavigationTable", "save", "newColumn="+newColumn);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_META_DATA");
        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
        
		sqlQuery.setValue("AnnotationName",  newColumn.getField(),       Types.VARCHAR);
		sqlQuery.setValue("ColumnName",      newColumn.getColumn(),      Types.VARCHAR);
		sqlQuery.setValue("AnnotationType",  newColumn.getType().name(), Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	private void load() 
	{
		TreeMap<String, SearchColumn> nameMap   = new TreeMap<String, SearchColumn>();
		TreeMap<String, SearchColumn> columnMap = new TreeMap<String, SearchColumn>();
		
        Collection<SearchColumn> fixedFields =  getFixedFields();
    	for(SearchColumn column : fixedFields)
    	{
    		nameMap.put(column.fieldName, column);
    		columnMap.put(column.columnName, column);
    	}
		
		try
		{
	        logger.logp(Level.INFO, "NavigationTable", "load", "project="+projectID);

	        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_META_DATA");
	        sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
	
	        RowSet<SearchColumn> result = find(sqlQuery);
	        List<SearchColumn> columns = result == null ? null : result.getRows();
	
	        if(columns != null)
	        {
	        	for(SearchColumn column : columns)
	        	{
	        		nameMap.put(column.fieldName, column);
	        		columnMap.put(column.columnName, column);
	        	}
	        }
		}
		catch(DataAccessException ex)
		{
			logger.logp(Level.WARNING, "NavigationTable", "load", "project="+projectID, ex);
		}
        
		annotationNameToFieldMap = nameMap;
		columnNameToFieldMap = columnMap;
	}
	
	private List<ValueCount> toValueCount(List<Object[]> rows) 
	{
		if(rows == null || rows.isEmpty())
			return null;
		
		List<ValueCount> values = new ArrayList<ValueCount>();
		for(Object[] result : rows)
		{
			ValueCount vc = new ValueCount((String) result[0], Util.getInteger(result[1]));
			values.add( vc );
		}
		
		return values;

	}
	
	private void setParameterAndValues(SQLQuery sqlQuery, Set<SearchCondition> preConditions) 
	{
		int conditionCounter = 1;
		if(preConditions != null)
		{
			for(SearchCondition sc : preConditions)
			{
				setParameterAndValues(sqlQuery, sc, conditionCounter);
				conditionCounter++;
			}
		}
	}
	
	private void setParameterAndValues(SQLQuery sqlQuery, SearchCondition sc, int conditionCounter) 
	{
		String searchColumnName = "ColumnName"+conditionCounter;
		String minValueName     = "MinValue"+conditionCounter;
		String maxValueName     = "MaxValue"+conditionCounter;
		
		SearchColumn conditionColumn = annotationNameToFieldMap.get(sc.getField());
		sqlQuery.setParameter(searchColumnName, conditionColumn.getColumn());
		
		setConditionValues(sqlQuery, sc, minValueName, maxValueName);
	}
	
	private void setConditionValues(SQLQuery sqlQuery, SearchCondition sc, String minValueName, String maxValueName)
	{
		if(sc.isIdentity())
		{
			sqlQuery.setValue(minValueName, sc.getLowerLimit(), sc.getType().getSQLType(), Operator.EQUALS_TO, true);
			return;
		}
		
		switch(sc.getType())
		{
			case Integer:
			case Time:
				sqlQuery.setValue(minValueName, sc.getLowerLimit(), sc.getType().getSQLType(), Operator.GREATER_THAN_EQUALS_TO, true);
				sqlQuery.setValue(maxValueName, sc.getUpperLimit(), sc.getType().getSQLType(), Operator.LESS_THAN_EQUALS_TO, true);
				break;
			case Real:
				sqlQuery.setValue(minValueName, sc.getLowerLimit(), sc.getType().getSQLType(), Operator.GREATER_THAN_EQUALS_TO, true);
				sqlQuery.setValue(maxValueName, sc.getUpperLimit(), sc.getType().getSQLType(), Operator.LESS_THAN, true);
				break;
			case Text:	
				sqlQuery.setValue(minValueName, sc.getLowerLimit(), sc.getType().getSQLType(), Operator.GREATER_THAN_EQUALS_TO, true);
				sqlQuery.setValue(maxValueName, sc.getUpperLimit(), sc.getType().getSQLType(), Operator.LESS_THAN_EQUALS_TO, true);
				break;
		}
	}
	
	private void setRangeValues(SQLQuery sqlQuery, SearchCondition sc, String minValueName, String maxValueName)
	{
		sqlQuery.setValue(minValueName, sc.getLowerLimit(), sc.getType().getSQLType(), Operator.GREATER_THAN);
		sqlQuery.setValue(maxValueName, sc.getUpperLimit(), sc.getType().getSQLType(), Operator.LESS_THAN);
	}
	
	/**
	 * Adds a new column in the search table
	 * @param fieldName name of the annotation that will be searchable
	 * @param fieldType value type of the annotation
	 * @throws DataAccessException 
	 */
	private SearchColumn registerUserAnnotation(String annotationName, AnnotationType fieldType) throws DataAccessException
	{
		SearchColumn newColumn = SearchColumn.toSearchColumn(annotationName, fieldType);

		if(!annotationNameToFieldMap.containsKey(annotationName))
		{
			registerColumn(newColumn);
			annotationNameToFieldMap.put(newColumn.getField(), newColumn);
			columnNameToFieldMap.put(newColumn.getColumn(), newColumn);
			save(newColumn);
		}
		return newColumn;
	}
	
	private void registerColumn(SearchColumn column) throws DataAccessException 
	{
		SQLQuery sqlQuery = null;
		
		switch(column.getType())
		{
			case Text:
				sqlQuery = queryDictionary.createQueryGenerator("REGISTER_TEXT_ANNOTATION");
				break;
			case Integer:
				sqlQuery = queryDictionary.createQueryGenerator("REGISTER_INT_ANNOTATION");
				break;
			case Real:
				sqlQuery = queryDictionary.createQueryGenerator("REGISTER_REAL_ANNOTATION");
				break;
			case Time:
				sqlQuery = queryDictionary.createQueryGenerator("REGISTER_TIME_ANNOTATION");
				break;
		}
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setParameter("ColumnName", column.getColumn());
		
		updateDatabase(sqlQuery);
	}
	
	private static TreeMap<String, SearchColumn> populateFixedFields()
	{
		TreeMap<String, SearchColumn> fixedMap = new TreeMap<String, SearchColumn>();
        
		List<SearchColumn> fixedColumns = new ArrayList<SearchColumn>();
		fixedColumns.add(new SearchColumn("Uploaded By",   AnnotationType.Text, "uploaded_by"));
        
		fixedColumns.add(new SearchColumn("Slice Count",   AnnotationType.Integer, "number_of_slices"));
		fixedColumns.add(new SearchColumn("Frame Count",   AnnotationType.Integer, "number_of_frames"));
		fixedColumns.add(new SearchColumn("Channel Count", AnnotationType.Integer, "number_of_channels"));
		fixedColumns.add(new SearchColumn("Site Count",    AnnotationType.Integer, "number_of_sites"));
		
		fixedColumns.add(new SearchColumn("Image Width",   AnnotationType.Integer, "image_width"));
		fixedColumns.add(new SearchColumn("Image Height",  AnnotationType.Integer, "image_height"));
		
		fixedColumns.add(new SearchColumn("Upload Time",   AnnotationType.Time, "upload_time"));
		fixedColumns.add(new SearchColumn("Source Time",   AnnotationType.Time, "source_file_time"));
		fixedColumns.add(new SearchColumn("Creation Time", AnnotationType.Time, "creation_time"));
		fixedColumns.add(new SearchColumn("Acquired Time", AnnotationType.Time, "acquired_time"));
		
		fixedColumns.add(new SearchColumn("Image Depth",  AnnotationType.Integer, "pixel_depth"));
		fixedColumns.add(new SearchColumn("Pixel Size X", AnnotationType.Real, "pixel_size_x"));
		fixedColumns.add(new SearchColumn("Pixel Size Y", AnnotationType.Real, "pixel_size_y"));
		fixedColumns.add(new SearchColumn("Pixel Size Z", AnnotationType.Real, "pixel_size_z"));
		
		fixedColumns.add(new SearchColumn("Source Type",  AnnotationType.Text, "source_type"));
		fixedColumns.add(new SearchColumn("Image Type",   AnnotationType.Text, "image_type"));
		
		
		fixedColumns.add(new SearchColumn("Machine IP",   AnnotationType.Text, "machine_ip"));
		fixedColumns.add(new SearchColumn("Machine MAC",  AnnotationType.Text, "mac_address"));
		
		fixedColumns.add(new SearchColumn("Source Folder", AnnotationType.Text, "source_folder"));
		fixedColumns.add(new SearchColumn("Source File",   AnnotationType.Text,"source_filename"));
		fixedColumns.add(new SearchColumn("Storage Location",   AnnotationType.Text,"storage_location"));
		fixedColumns.add(new SearchColumn("Microscope Name",   AnnotationType.Text,"microscope_name"));

		
		for(SearchColumn column : fixedColumns)
		{
			fixedMap.put(column.fieldName, column);
		}
		
		return fixedMap;
	}
	

	public static void main(String ...args) throws DataAccessException
	{
		ConnectionProvider connectionProvider = new ConnectionProvider()
		{

			@Override
			public Connection getConnection() throws DataAccessException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Connection getStorageConnection() throws DataAccessException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getDatabaseName() {
				// TODO Auto-generated method stub
				return "mysql";
			}
			
		};
		
		DBNavigationDAO dao = new DBNavigationDAO(null, connectionProvider, 1);
		Set<Long> guids = new HashSet<Long>();
		guids.add(1L);
		dao.getRecordMetaData(guids, SearchColumn.toSearchColumn("kutta", AnnotationType.Integer));
	}

	@Override
	public void removeAnnotationColumn(int projectId, String columnName)
			throws DataAccessException
	{
		if(annotationNameToFieldMap.containsKey(columnName))
			annotationNameToFieldMap.remove(columnName);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_ANNOTATION_COLUMN");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setParameter("ColumnName", columnName);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void removeAnnotationColumnFromInfo(int projectId, String columnName)
			throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_ANNOTATION_COLUMN_FROM_INFO");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("Name", columnName, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void deleteRecord(long guid) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_RECORD");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("Guid", guid, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void applyAcquisitionProfile(long guid, AcquisitionProfile acqProfile) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("APPLY_PROFILE");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID", guid, Types.VARCHAR);
		
		sqlQuery.setValue("XSize", acqProfile.getxPixelSize(), Types.DOUBLE);
		sqlQuery.setValue("YSize", acqProfile.getyPixelSize(), Types.DOUBLE);
		sqlQuery.setValue("ZSize", acqProfile.getzPixelSize(), Types.DOUBLE);
		
		sqlQuery.setValue("SourceFormat", acqProfile.getSourceType().name, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
}
