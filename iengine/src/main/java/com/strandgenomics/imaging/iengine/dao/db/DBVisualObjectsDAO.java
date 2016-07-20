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
 * DBVisualObjectsDAO.java
 *
 * AVADIS Image Management System
 * Data Access Components
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

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import org.apache.commons.lang3.text.WordUtils;

import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iengine.dao.VisualObjectsDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.WKTUtil;

public class DBVisualObjectsDAO extends StorageDAO<VisualObject> implements VisualObjectsDAO {

	public DBVisualObjectsDAO(DBImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "VisualObjectsDAO.xml");
	}
	
	@Override
	public List<VisualObject> getVisualObjects(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException
	{
		List<VisualObject> result = new ArrayList<VisualObject>();
		
		List<VisualObject> voList = getAllVisualObject(projectID, guid, overlayName, d);
		if(voList != null) result.addAll( voList );
		
		List<VisualObject> testBoxes = getTextBoxes(projectID, guid, overlayName, d);
		if(testBoxes != null) result.addAll( testBoxes );
		
		return result;
	}

	@Override
	public List<VisualObject> getFreeHandShapes(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException 
	{
		return getVisualObject(projectID, guid, overlayName, d, WKTUtil.WKTTYPE.FreeHand.name());
	}

	@Override
	public List<VisualObject> getRectangularShapes(int projectID, long guid, String overlayName, VODimension d) throws DataAccessException 
	{
		return getVisualObject(projectID, guid, overlayName, d, WKTUtil.WKTTYPE.Rectangle.name());
	}

	@Override
	public List<VisualObject> getLineSegments(int projectID, long guid, String overlayName, VODimension d) throws DataAccessException 
	{
		return getVisualObject(projectID, guid, overlayName, d, WKTUtil.WKTTYPE.Line.name());
	}

	@Override
	public List<VisualObject> getEllipticalShapes(int projectID, long guid, String overlayName, VODimension d) throws DataAccessException 
	{
		return getVisualObject(projectID, guid, overlayName, d, WKTUtil.WKTTYPE.Ellipse.name());
	}
	
	@Override
	public List<VisualObject> getTextBoxes(int projectID, long guid, String overlayName, VODimension d) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "getTextBoxes", "project id "+projectID+"overlay +"+overlayName+ ", d="+d );
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TEXT_OBJECTS");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		
		RowSet<VisualObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}
	
	@Override
	public String[] getTextBoxeMessages(int projectID, long guid) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "getAllTextBoxes", "project id "+projectID +", guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TEXTBOX_MESSAGES");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		return getRowsWithStringValues(sqlQuery);
	}
	
	@Override
	public void addVisualObjects(int projectID, long guid, String overlayName, List<VisualObject> vObjects, VODimension... imageCoordinates)
			throws DataAccessException 
	{
		if(vObjects == null || vObjects.isEmpty()) return;
		if(imageCoordinates == null || imageCoordinates.length == 0) return;

		for(VODimension d : imageCoordinates)
		{
			insertVisualObjects(projectID, guid, overlayName, d, vObjects);
		}
	}

	@Override
	public void insertVisualObjects(int projectID, long guid, String overlayName, VODimension d, List<VisualObject> shapes)
			throws DataAccessException 
	{
		if(shapes == null || shapes.isEmpty()) return;
		
		for(VisualObject vo : shapes)
		{
			if(vo instanceof TextBox)
			{
				insertTextBox(projectID, guid, overlayName, d, (TextBox)vo);
			}
			else
			{
				insertVisualObject(projectID, guid, overlayName, d, vo);
			}
		}
	}

	@Override
	public void updateVisualOverlay(int projectID, long guid, String overlayName, VODimension d, List<VisualObject> shapes)
			throws DataAccessException 
	{
		if(shapes == null || shapes.isEmpty()) return;

		for(VisualObject vo : shapes)
		{
			if(vo instanceof TextBox)
			{
				updateTextBox(projectID, guid, overlayName, d, (TextBox) vo);
			}
			else
			{
				updateVisualObject(projectID, guid, overlayName, d, vo);
			}
		}
	}

	@Override
	public void deleteVisualObjects(int projectID, long guid, List<VisualObject> vObjects, String overlayName, 
			VODimension... imageCoordinates) throws DataAccessException 
	{
		if(imageCoordinates == null || imageCoordinates.length == 0) return;

		for(VODimension d : imageCoordinates)
		{
			deleteVisualObject(projectID, guid, overlayName, d, vObjects);
		}
	}
	
	@Override
	public void deleteVisualObjects(int projectID, long guid, String overlayName, VODimension ... imageCoordinates) throws DataAccessException 
	{
		if(imageCoordinates == null || imageCoordinates.length == 0) return;

		for(VODimension d : imageCoordinates)
		{
			deleteAllVisualObject(projectID, guid, overlayName, d);
		}
	}
	
	@Override
	public void deleteVisualObjects(int projectID, long guid, String overlayName, VODimension d, int ... voIDs) throws DataAccessException
	{
		if(voIDs == null || voIDs.length == 0) return;

		for(int voID : voIDs)
		{
			deleteVisualObject(projectID, guid, overlayName, d, voID);
		}
	}
	
	@Override
	public void deleteTextObjects(int projectID, long guid, String overlayName, VODimension d, int ... voIDs)
			throws DataAccessException
	{
		if(voIDs == null || voIDs.length == 0) return;

		for(int voID : voIDs)
		{
			deleteTextBox(projectID, guid, overlayName, d, voID);
		}
	}
	
	private void deleteAllVisualObject(int projectID, long guid, String overlayName, VODimension d) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_VISUAL_OBJECTS_FOR_DIMENSION");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "deleteAllVisualObject", "project id "+projectID+"guid="+guid +",dimension="+d +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		
		updateDatabase(sqlQuery);
		
		sqlQuery = queryDictionary.createQueryGenerator("DELETE_TEXT_BOXES_FOR_DIMENSION");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "deleteAllVisualObject", "deleting text boxes from project id "+projectID+"guid="+guid +",dimension="+d +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		
		updateDatabase(sqlQuery);
		
	}

	@Override
	public void deleteVisualObjects(int projectID, long guid, int siteNo, String overlayName) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_VISUAL_OBJECTS_FOR_OVERLAY");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "deleteVisualObjects", "project id "+projectID+"guid="+guid +",siteNo="+siteNo +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SiteNo",  siteNo,       Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		
		updateDatabase(sqlQuery);
		
		sqlQuery = queryDictionary.createQueryGenerator("DELETE_TEXT_BOXES_FOR_OVERLAY");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "deleteVisualObjects", "deleting text boxes from project id "+projectID+"guid="+guid +",siteNo="+siteNo +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SiteNo",  siteNo,       Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	private List<VisualObject> getAllVisualObject(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException
	{
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "getVisualObject", "project id "+projectID+"overlay +"+overlayName+ ", d="+d);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_VISUAL_OBJECTS");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		
		RowSet<VisualObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
		
	}
	
	private List<VisualObject> getVisualObject(int projectID, long guid, String overlayName, VODimension d, String type) 
			throws DataAccessException
	{
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "getVisualObject", "project id "+projectID+"overlay +"+overlayName+ ", d="+d +"type "+type);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_VISUAL_OBJECTS_FOR_TYPE");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("Type",    type,         Types.VARCHAR);
		
		RowSet<VisualObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public VisualObject createObject(Object[] columnValues) 
	{
		//object_id, pen_color, pen_width, AsText(visual_object), vo_type 
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "createObject", "creating object for wkt string "+columnValues[4]);
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "createObject", "columnValueslength="+columnValues.length);
		int vo_id = Util.getInteger( columnValues[0] );
		
		Color pen_color =  new Color( Util.getInteger( columnValues[1] ), true);
		float pen_width =  Util.getFloat( columnValues[2] );

		String wktString = null;
		if (columnValues[3] instanceof DataSource)
		    wktString = toStringObject((DataSource) columnValues[3]);
		else
		    wktString = (String) columnValues[3];
		
		String column4th = (String) columnValues[4];
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "createObject", "creating object for wkt string "+wktString+" possible type "+column4th);
		
		VisualObject vo = null;
		
		if(WKTUtil.WKTTYPE.Rectangle.name().equals(column4th))
		{
			vo = WKTUtil.toShape(wktString, vo_id, null, WKTUtil.WKTTYPE.Rectangle);
			vo.setZoomLevel(Util.getInteger( columnValues[5] ));
		}
		else if(WKTUtil.WKTTYPE.Ellipse.name().equals(column4th))
		{
			vo = WKTUtil.toShape(wktString, vo_id, null, WKTUtil.WKTTYPE.Ellipse);
			vo.setZoomLevel(Util.getInteger( columnValues[5] ));
		}
		else if(WKTUtil.WKTTYPE.Circle.name().equals(column4th))
		{
			vo = WKTUtil.toShape(wktString, vo_id, null, WKTUtil.WKTTYPE.Circle);
			vo.setZoomLevel(Util.getInteger( columnValues[5] ));
		}
		else if(WKTUtil.WKTTYPE.Line.name().equals(column4th))
		{
			vo = WKTUtil.toShape(wktString, vo_id,null, WKTUtil.WKTTYPE.Line);
			vo.setZoomLevel(Util.getInteger( columnValues[5] ));
		}
		else if(WKTUtil.WKTTYPE.FreeHand.name().equals(column4th))
		{
			vo = WKTUtil.toShape(wktString, vo_id, null, WKTUtil.WKTTYPE.FreeHand);
			vo.setZoomLevel(Util.getInteger( columnValues[5] ));
		}
		else if(WKTUtil.WKTTYPE.Polygon.name().equals(column4th))
		{
			vo = WKTUtil.toShape(wktString, vo_id, null, WKTUtil.WKTTYPE.Polygon);
			vo.setZoomLevel(Util.getInteger( columnValues[5] ));
		}
		else if(WKTUtil.WKTTYPE.Arrow.name().equals(column4th))
		{
			vo = WKTUtil.toShape(wktString, vo_id, null, WKTUtil.WKTTYPE.Arrow);
			vo.setZoomLevel(Util.getInteger( columnValues[5] ));
		}		
		else
		{
			Font font = toFont( (String) columnValues[5] );
			Color bgColor = new Color( Util.getInteger( columnValues[6] ) );
			
			vo = WKTUtil.toShape(wktString, vo_id, column4th, WKTUtil.WKTTYPE.Text);
			((TextBox) vo).setFont(font);
			((TextBox) vo).setBkgColor(bgColor);
			vo.setZoomLevel(Util.getInteger( columnValues[7] ));

		}
	
		vo.setPenColor(pen_color);
		vo.setPenWidth(pen_width);
		return vo;
	}

	private String toStringObject(DataSource dataSource) 
	{
		if(dataSource == null) return null;
        
        InputStream inStream = null;
        
        try 
        {
        	StringBuffer sb = new StringBuffer();
        	inStream = dataSource.getInputStream();
        	while(true)
        	{
				int value = inStream.read();
				if(value == -1)
					break;
				sb.append((char)value);
			}
        	return sb.toString();
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DBVisualObjectsDAO", "toStringObject", "error re-serializing object", ex);
            throw new RuntimeException(ex);
        }
        finally
        {
        	Util.closeStream(inStream);
        }
	}
	
	private void insertTextBox(int projectID, long guid, String overlayName, VODimension d, TextBox shape) 
			throws DataAccessException 
	{
		if(shape == null) return;
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "insertTextBox", "project id "+projectID+"overlay="+overlayName +",guid="+guid  +" dimension="+d);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_TEXT_BOX");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));

		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
	    
		sqlQuery.setValue("ObjectID",  shape.ID,            Types.INTEGER);
		sqlQuery.setValue("PenColor",  shape.getPenColor().getRGB(), Types.INTEGER);
		sqlQuery.setValue("PenWidth",  shape.getPenWidth(), Types.REAL);
		sqlQuery.setValue("ZoomLevel",  shape.getZoomLevel(), Types.INTEGER);
		
		sqlQuery.setValue("Shape",     WKTUtil.toWKTFromat(shape),    Types.VARCHAR);
		sqlQuery.setValue("Message",   shape.getText(),               Types.VARCHAR);
		sqlQuery.setValue("Name",      overlayName,                   Types.VARCHAR);
		sqlQuery.setValue("Font",      toFontString(shape.getFont()), Types.VARCHAR);
		sqlQuery.setValue("BackgroundColor", shape.getBkgColor().getRGB(), Types.INTEGER);
		
		updateDatabase(sqlQuery);
	}

	private void insertVisualObject(int projectID, long guid, String overlayName, VODimension d, VisualObject shape) 
			throws DataAccessException 
	{
		if(shape == null) return;
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "insertVisualObject", "project id "+projectID+"overlay="+overlayName +",guid="+guid  +" dimension="+d);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_VISUAL_OBJECT");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));

		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);

		sqlQuery.setValue("ObjectID",  shape.ID,            Types.INTEGER);
		sqlQuery.setValue("PenColor",  shape.getPenColor().getRGB(), Types.INTEGER);
		sqlQuery.setValue("PenWidth",  shape.getPenWidth(), Types.REAL);
		sqlQuery.setValue("ZoomLevel",  shape.getZoomLevel(), Types.INTEGER);
		
		sqlQuery.setValue("Shape",     WKTUtil.toWKTFromat(shape),    Types.VARCHAR);
		if(shape.getType().name().compareTo("PATH")==0){
			sqlQuery.setValue("Type",      "FreeHand", Types.VARCHAR);
		}else{
			sqlQuery.setValue("Type",      WordUtils.capitalizeFully(shape.getType().name()), Types.VARCHAR);
		}sqlQuery.setValue("Name",      overlayName,                   Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	private void deleteVisualObject(int projectID, long guid, String overlayName, VODimension d, List<VisualObject> vObjects) 
			throws DataAccessException 
	{
		if(vObjects == null || vObjects.isEmpty()) return;

		for(VisualObject vo : vObjects)
		{
			if(vo instanceof TextBox)
			{
				deleteTextBox(projectID, guid, overlayName, d, vo.ID);
			}
			else
			{
				deleteVisualObject(projectID, guid, overlayName, d, vo.ID);
			}
		}
	}

	private void deleteVisualObject(int projectID, long guid, String overlayName, VODimension d, int voID) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_VISUAL_OBJECT_FOR_ID");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "deleteVisualObject", "project id "+projectID+"guid="+guid +",dimension="+d +",overlayName="+overlayName+" object_id="+voID);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("ObjectID",voID,         Types.INTEGER);
		
		updateDatabase(sqlQuery);
	}
	
	private void deleteTextBox(int projectID, long guid, String overlayName, VODimension d, int voID) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_TEXT_BOX_FOR_ID");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "deleteTextBox", "deleting text boxes from project id "+projectID+"guid="+guid +",dimension="+d +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",  d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("ObjectID",voID,         Types.INTEGER);
		
		updateDatabase(sqlQuery);
	}
	
	private void updateVisualObject(int projectID, long guid, String overlayName, VODimension d, VisualObject shape) 
			throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_VISUAL_OBJECT");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "updateVisualObject", "updating visual object for project id "+projectID+"guid="+guid +",dimension="+d +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		//where fields
		sqlQuery.setValue("GUID",     guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo",  d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo",  d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",   d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",     overlayName,  Types.VARCHAR);
		sqlQuery.setValue("ObjectID", shape.ID,     Types.INTEGER);
		
		//update fields
		sqlQuery.setValue("Shape",     WKTUtil.toWKTFromat(shape), Types.VARCHAR);
		sqlQuery.setValue("PenColor",  shape.getPenColor().getRGB(),        Types.INTEGER);
		sqlQuery.setValue("PenWidth",  shape.getPenWidth(),        Types.REAL);
		
		updateDatabase(sqlQuery);
		
	}

	private void updateTextBox(int projectID, long guid, String overlayName, VODimension d, TextBox shape) 
			throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_TEXT_BOX");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "updateTextBox", "updating text box for project id "+projectID+"guid="+guid +",dimension="+d +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		//where fields
		sqlQuery.setValue("GUID",     guid,         Types.BIGINT);
		sqlQuery.setValue("SliceNo",  d.sliceNo,    Types.SMALLINT);
		sqlQuery.setValue("FrameNo",  d.frameNo,    Types.SMALLINT);
		sqlQuery.setValue("SiteNo",   d.siteNo,     Types.SMALLINT);
		sqlQuery.setValue("Name",     overlayName,  Types.VARCHAR);
		sqlQuery.setValue("ObjectID", shape.ID,     Types.INTEGER);
		
		//update fields
		sqlQuery.setValue("Shape",     WKTUtil.toWKTFromat(shape),    Types.VARCHAR);
		sqlQuery.setValue("PenColor",  shape.getPenColor().getRGB(),  Types.INTEGER);
		sqlQuery.setValue("PenWidth",  shape.getPenWidth(),           Types.REAL);
		sqlQuery.setValue("Message",   shape.getText(),               Types.VARCHAR);
		sqlQuery.setValue("Font",      toFontString(shape.getFont()), Types.VARCHAR);
		sqlQuery.setValue("BackgroundColor", shape.getBkgColor().getRGB(), Types.INTEGER);
		
		updateDatabase(sqlQuery);
	}
	
	private String toFontString(Font f)
	{
		if(f == null) return null;
		StringBuilder builder = new StringBuilder();
		builder.append(f.getName());
		builder.append('|');
		builder.append(f.getStyle());
		builder.append('|');
		builder.append(f.getSize());
		
		return builder.toString();
	}
	
	private Font toFont(String fontString)
	{
		if(fontString == null) return null;
		try
		{
			String[] tokens = fontString.split("\\|");
			String name = tokens[0];
			int style = Integer.parseInt(tokens[1]);
			int size = Integer.parseInt(tokens[2]);
			return new Font(name, style, size);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "DBVisualObjectsDAO", "toFont", fontString, ex);
			return null;
		}
	}

	@Override
	public List<VODimension> findVisualObjectLocation(int projectID, long guid, int siteNo, String overlayName, int id) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_VISUAL_OBJECT_COORDINATES_FOR_ID");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "findVisualObjectLocation", "project id "+projectID+"guid="+guid +",site="+siteNo +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("ObjectID",id,         Types.INTEGER);
		sqlQuery.setValue("SiteNo",siteNo,         Types.INTEGER);
		
		RowSet<Object[]> rows = executeQuery(sqlQuery);
		
		if(rows == null || rows.size()==0) return null;
		
		List<VODimension> locations = new ArrayList<VODimension>();
		for(Object[] row:rows.getRows())
		{
			int frameNumber = (Integer) row[0];
			int sliceNumber = (Integer) row[1];
			VODimension vodim = new VODimension(frameNumber, sliceNumber, siteNo);
			
			locations.add(vodim);
		}
		
		List<VODimension> textLocations = findTextObjectLocation(projectID, guid, siteNo, overlayName, id);
		if(textLocations!=null)
			locations.addAll(textLocations);
		
		return locations;
	}
	
	public List<VODimension> findTextObjectLocation(int projectID, long guid, int siteNo, String overlayName, int id) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TEXT_OBJECT_COORDINATES_FOR_ID");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "findVisualObjectLocation", "project id "+projectID+"guid="+guid +",site="+siteNo +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("ObjectID",id,         Types.INTEGER);
		sqlQuery.setValue("SiteNo",siteNo,         Types.INTEGER);
		
		RowSet<Object[]> rows = executeQuery(sqlQuery);
		
		if(rows == null || rows.size()==0) return null;
		
		List<VODimension> locations = new ArrayList<VODimension>();
		for(Object[] row:rows.getRows())
		{
			int frameNumber = (Integer) row[0];
			int sliceNumber = (Integer) row[1];
			VODimension vodim = new VODimension(frameNumber, sliceNumber, siteNo);
			
			locations.add(vodim);
		}
		
		return locations;
	}

	@Override
	public List<VODimension> findOverlayLocation(int projectID, long guid, int siteNo, String overlayName) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_VISUAL_OBJECT_COORDINATES");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "findVisualObjectLocation", "project id "+projectID+"guid="+guid +",site="+siteNo +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("SiteNo",siteNo,         Types.INTEGER);
		
		RowSet<Object[]> rows = executeQuery(sqlQuery);
		
		if(rows == null || rows.size()==0) return null;
		
		List<VODimension> locations = new ArrayList<VODimension>();
		for(Object[] row:rows.getRows())
		{
			int frameNumber = (Integer) row[0];
			int sliceNumber = (Integer) row[1];
			VODimension vodim = new VODimension(frameNumber, sliceNumber, siteNo);
			
			locations.add(vodim);
		}
		
		List<VODimension> textLocations = findOverlayLocationForText(projectID, guid, siteNo, overlayName);
		if(textLocations!=null)
			locations.addAll(textLocations);
		
		return locations;
	}
	
	public List<VODimension> findOverlayLocationForText(int projectID, long guid, int siteNo, String overlayName) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TEXT_OBJECT_COORDINATES");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "findVisualObjectLocation", "project id "+projectID+"guid="+guid +",site="+siteNo +",overlayName="+overlayName);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("SiteNo",siteNo,         Types.INTEGER);
		
		RowSet<Object[]> rows = executeQuery(sqlQuery);
		
		if(rows == null || rows.size()==0) return null;
		
		List<VODimension> locations = new ArrayList<VODimension>();
		for(Object[] row:rows.getRows())
		{
			int frameNumber = (Integer) row[0];
			int sliceNumber = (Integer) row[1];
			VODimension vodim = new VODimension(frameNumber, sliceNumber, siteNo);
			
			locations.add(vodim);
		}
		
		return locations;
	}

	@Override
	public List<VisualObject> findVisualObjects(int projectID, long guid, int frame, int slice, int site, String overlayName, int x, int y, int height, int width) throws DataAccessException 
	{
		String queryRect = WKTUtil.toWKTFromat(new Rectangle(x, y, width, height));
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_VISUAL_OBJECTS_WITHIN_AREA");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "findVisualObjects", "project id "+projectID+"guid="+guid +",site="+site +",overlayName="+overlayName+", query area "+queryRect);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("FrameNo",frame,         Types.INTEGER);
		sqlQuery.setValue("SliceNo",slice,         Types.INTEGER);
		sqlQuery.setValue("SiteNo",site,         Types.INTEGER);
		sqlQuery.setValue("SearchArea",     queryRect,    Types.VARCHAR);
		
		List<VisualObject> result = new ArrayList<VisualObject>();
		
		List<VisualObject> testBoxes = findTextObjects(projectID, guid, frame, slice, site, overlayName, x, y, height, width);
		if(testBoxes != null) result.addAll( testBoxes );
		
		RowSet<VisualObject> voresult = find(sqlQuery);
		if(voresult!=null) result.addAll(voresult.getRows());
		
		return result;
	}
	
	public List<VisualObject> findTextObjects(int projectID, long guid, int frame, int slice, int site, String overlayName, int x, int y, int height, int width) throws DataAccessException 
	{
		String queryRect = WKTUtil.toWKTFromat(new Rectangle(x, y, width, height));
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TEXT_OBJECTS_WITHIN_AREA");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "findVisualObjects", "project id "+projectID+"guid="+guid +",site="+site +",overlayName="+overlayName+", query area "+queryRect);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("Name",    overlayName,  Types.VARCHAR);
		sqlQuery.setValue("FrameNo",frame,         Types.INTEGER);
		sqlQuery.setValue("SliceNo",slice,         Types.INTEGER);
		sqlQuery.setValue("SiteNo",site,         Types.INTEGER);
		sqlQuery.setValue("SearchArea",     queryRect,    Types.VARCHAR);
		
		RowSet<VisualObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public void deleteVisualObjectsByGUID(int projectID, long guid) throws DataAccessException {
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_VISUAL_OBJECTS_BY_GUID");
		logger.logp(Level.INFO, "DBVisualObjectsDAO", "deleteVisualObjectsByGUID", "project id "+projectID+"guid="+guid);

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		
		updateDatabase(sqlQuery);		
	}
}
