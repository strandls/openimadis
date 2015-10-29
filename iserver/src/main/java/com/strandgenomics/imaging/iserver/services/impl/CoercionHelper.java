/*
 * CoercionHelper.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.impl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.app.ApplicationSpecification;
import com.strandgenomics.imaging.icore.app.ListConstraints;
import com.strandgenomics.imaging.icore.app.ParameterConstraints;
import com.strandgenomics.imaging.icore.app.ParameterType;
import com.strandgenomics.imaging.icore.app.RangeConstraints;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.icore.vo.VisualObjectType;
import com.strandgenomics.imaging.iengine.bioformats.ClientSourceFile;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfileType;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.LengthUnit;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.models.TimeUnit;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.Ticket;
import com.strandgenomics.imaging.iserver.services.ws.compute.Application;
import com.strandgenomics.imaging.iserver.services.ws.compute.Constraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.DoubleListConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.DoubleRangeConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.LongListConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.LongRangeConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.NVPair;
import com.strandgenomics.imaging.iserver.services.ws.compute.Parameter;
import com.strandgenomics.imaging.iserver.services.ws.compute.Publisher;
import com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Channel;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Comments;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Contrast;
import com.strandgenomics.imaging.iserver.services.ws.ispace.EllipticalShape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.FingerPrint;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Font;
import com.strandgenomics.imaging.iserver.services.ws.ispace.FreehandShape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.HistoryItem;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Image;
import com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex;
import com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicParameters;
import com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicRequest;
import com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Project;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Property;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Record;
import com.strandgenomics.imaging.iserver.services.ws.ispace.RecordAttachment;
import com.strandgenomics.imaging.iserver.services.ws.ispace.RecordSite;
import com.strandgenomics.imaging.iserver.services.ws.ispace.RectangularShape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Shape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics;
import com.strandgenomics.imaging.iserver.services.ws.ispace.StraightLine;
import com.strandgenomics.imaging.iserver.services.ws.ispace.TextArea;
import com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex;
import com.strandgenomics.imaging.iserver.services.ws.loader.Archive;
import com.strandgenomics.imaging.iserver.services.ws.loader.SourceFile;
import com.strandgenomics.imaging.iserver.services.ws.loader.UploadTicket;
import com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition;
import com.strandgenomics.imaging.iserver.services.ws.search.SearchField;
import com.strandgenomics.imaging.iserver.services.ws.search.SearchNode;

/**
 * Converts server (WS) class to engine classes and vice versa
 * @author arunabha
 *
 */
public class CoercionHelper {
	
	public static Project[] toRemoteProject(List<com.strandgenomics.imaging.iengine.models.Project> pList) 
	{
		if(pList == null) return null;

		Project[] poList = new Project[pList.size()];
		for(int i = 0; i < poList.length; i++)
		{
			poList[i] = toRemoteProject(pList.get(i));
		}

		return poList;
	}

	public static Project toRemoteProject(com.strandgenomics.imaging.iengine.models.Project p) 
	{
		if(p == null) return null;
		
		Project po = new Project();
		po.setName(p.getName());
		po.setNotes(p.getNotes());
		po.setSpaceUsage(p.getSpaceUsage());
		po.setStorageQuota(p.getStorageQuota());
		po.setCreationDate(p.getCreationDate().getTime());
		po.setRecordCount(p.getNoOfRecords());

		return po;
	}
	
	public static com.strandgenomics.imaging.iserver.services.ws.manage.User[] toRemoteUser(Collection<User> uList) 
	{
		if(uList == null || uList.size() == 0) return null;
		com.strandgenomics.imaging.iserver.services.ws.manage.User[] uo = new com.strandgenomics.imaging.iserver.services.ws.manage.User[uList.size()];
		
		int i = 0;
		for(User u : uList)
		{
			uo[i++] = toRemoteUser(u); 
		}
		
		return uo;
	}
	
	
	public static com.strandgenomics.imaging.iserver.services.ws.manage.User toRemoteUser(User u) 
	{
		if(u == null) return null;
		com.strandgenomics.imaging.iserver.services.ws.manage.User uo = new com.strandgenomics.imaging.iserver.services.ws.manage.User();
		uo.setLogin(u.getLogin());
		uo.setEmailID(uo.getEmailID());
		uo.setRank(u.getRank().name());
		uo.setFullName(u.getName());
		
		return uo;
	}

	public static UploadTicket toTicketObject(Ticket ticket) 
	{
		if(ticket == null) return null;
		UploadTicket to = new UploadTicket();
		to.setID(ticket.ID);
		to.setArchiveSignature( Util.toHexString( ticket.getArchiveSignature() ) );
		to.setUploadURL( ticket.getUploadURL() );
		to.setDownloadURL( ticket.getDownloadURL() );
		return to;
	}

	public static List<ISourceReference> toSourceReference(SourceFile[] clientFiles) 
	{
		if(clientFiles == null || clientFiles.length == 0) return null;
		List<ISourceReference> sourceFiles = new ArrayList<ISourceReference>();
		
		for(int i = 0;i < clientFiles.length; i++)
		{
			if(clientFiles[i] == null) continue;
			sourceFiles.add( toSourceReference(clientFiles[i]) );
		}
		return sourceFiles;
	}

	public static ISourceReference toSourceReference(SourceFile sourceFile) 
	{
		if(sourceFile == null) return null;
		return new ClientSourceFile(sourceFile.getFilePath(), sourceFile.getLength(), sourceFile.getLastModificationTime());
	}

	public static Record[] toRemoteRecords(String actorLogin, List<com.strandgenomics.imaging.iengine.models.Record> records) throws DataAccessException {
		if(records == null) return null;

		Record[] recordList = new Record[records.size()];
		for(int i = 0; i < records.size(); i++)
		{
			recordList[i] = toRemoteRecord(actorLogin, records.get(i));
		}

		return recordList;
	}

	public static Record toRemoteRecord(String actorLogin, com.strandgenomics.imaging.iengine.models.Record r) throws DataAccessException 
	{
		if(r == null) return null;
		
		Record recordObject = new Record();
		
		recordObject.setUploadTime(r.uploadTime);
		recordObject.setSourceFileTime(r.sourceTime);
		recordObject.setAcquiredDate(r.acquiredTime);
		recordObject.setCreationTime(r.creationTime);
		
		recordObject.setImageType(r.imageType.ordinal());
		recordObject.setIpAddress(r.machineIP);
		recordObject.setMacAddress(r.macAddress);
		
		recordObject.setPixelDepth(r.imageDepth.getByteSize());
		recordObject.setPixelSizeAlongXAxis(r.getXPixelSize());
		recordObject.setPixelSizeAlongYAxis(r.getYPixelSize());
		recordObject.setPixelSizeAlongZAxis(r.getZPixelSize());
		recordObject.setSourceFormat(r.getSourceFormat().name);
		recordObject.setSignature( toRemoteSignature(r.getSignature()));
		
		User u = SysManagerFactory.getUserManager().getUser(r.uploadedBy);
		recordObject.setUploadedBy(u.getLogin());
		
		boolean writable = SysManagerFactory.getUserPermissionManager().canRead(actorLogin, r.guid);
		recordObject.setWritable(writable);
		
		recordObject.setSourceFolder(r.sourceFolder);
		recordObject.setSourceFilename(r.sourceFilename);
		
		return recordObject;
	}

	public static FingerPrint toRemoteSignature(Signature r) 
	{
		if(r == null) return null;
		
		FingerPrint s = new FingerPrint();
		
		s.setArchiveHash( Util.toHexString(r.archiveHash) );
		s.setSiteHash( Util.toHexString(r.siteHash) );
		
		s.setImageHeight(r.imageHeight);
		s.setImageWidth(r.imageWidth);
		
		s.setNoOfFrames( r.noOfFrames );
		s.setNoOfSlices( r.noOfSlices );
		s.setNoOfChannels( r.noOfChannels );
		s.setNoOfSites( r.noOfSites );
		
		return s;
	}

	public static RecordSite[] toRemoteSite(List<Site> sites) 
	{
		if(sites == null || sites.isEmpty()) return null;
		
		List<RecordSite> rSites = new ArrayList<RecordSite>();
		for(Site s : sites)
		{
			RecordSite r = toRemoteSite(s);
			if(r != null) rSites.add(r);
		}
		
		return rSites.isEmpty() ? null : rSites.toArray(new RecordSite[0]);
	}

	public static RecordSite toRemoteSite(Site s) 
	{
		if(s == null) return null;
		RecordSite r = new RecordSite();
		r.setName(s.getName());
		r.setSeriesNo(s.getSeriesNo());
		return r;
	}
	
	public static final List<com.strandgenomics.imaging.icore.Channel> toChannel(Channel[] cList)
	{
		if(cList == null || cList.length == 0) return null;
		List<com.strandgenomics.imaging.icore.Channel> channels = new ArrayList<com.strandgenomics.imaging.icore.Channel>();
		
		for(Channel rc : cList)
		{
			com.strandgenomics.imaging.icore.Channel c = toChannel(rc);
			if(c != null) channels.add(c);
		}
		
		return channels;
	}
	
	public static final com.strandgenomics.imaging.icore.Channel toChannel(Channel rc)
	{
		if(rc == null) return null;
		com.strandgenomics.imaging.icore.Channel c = new com.strandgenomics.imaging.icore.Channel(rc.getName(), rc.getLutName());
		c.setContrast(false, toVisualContrast(rc.getContrast()) );
		c.setContrast(true, toVisualContrast(rc.getZStackedContrast()) );
		c.setWavelength(rc.getWavelength());
		c.setRevision(rc.getRevision());
		return c;
	}
	
	public static final VisualContrast toVisualContrast(Contrast rc)
	{
		if(rc == null) return null;
		return new VisualContrast(rc.getMinIntensity(), rc.getMaxIntensity(), rc.getGamma());
	}

	public static Channel[] toRemoteChannel(List<com.strandgenomics.imaging.icore.Channel> channels) 
	{
		if(channels == null || channels.isEmpty()) return null;
		
		List<Channel> rChannels = new ArrayList<Channel>();
		for(com.strandgenomics.imaging.icore.Channel c : channels)
		{
			Channel r = toRemoteChannel(c);
			if(r != null) rChannels.add(r);
		}
		
		return rChannels.isEmpty() ? null : rChannels.toArray(new Channel[0]);
	}

	public static Channel toRemoteChannel(com.strandgenomics.imaging.icore.Channel c)
	{
		if(c == null) return null;
		Channel r = new Channel();
		r.setName(c.getName());
		r.setLutName(c.getLut());
		r.setContrast( toRemoteContrast(c.getContrast(false)));
		r.setZStackedContrast( toRemoteContrast(c.getContrast(true)));
		r.setWavelength( c.getWavelength() );
		r.setRevision(c.getRevision());
		return r;
	}

	private static Contrast toRemoteContrast(VisualContrast contrast)
	{
		if(contrast == null) return null;
		Contrast c = new Contrast();
		c.setGamma( contrast.getGamma() );
		c.setMaxIntensity( contrast.getMaxIntensity() );
		c.setMinIntensity( contrast.getMinIntensity() );
		return c;
	}

	public static Signature toSignature(FingerPrint fingerPrint) 
	{
		if(fingerPrint==null) return null;

		return new Signature(fingerPrint.getNoOfFrames(),
				fingerPrint.getNoOfSlices(), 
				fingerPrint.getNoOfChannels(),
				fingerPrint.getNoOfSites(),
				fingerPrint.getImageWidth(), 
				fingerPrint.getImageHeight(), 
				Util.toBigInteger(fingerPrint.getSiteHash()),
				Util.toBigInteger(fingerPrint.getArchiveHash()));
	}

	public static Property toRemoteMetadata(MetaData metaData) 
	{
		if(metaData == null) return null;
		
		Property p = new Property();
		p.setName( metaData.getName() );
		p.setValue( metaData.getValue() );

		return p;
	}

	public static Property[] toRemoteMetadataList(List<MetaData> metaDataList)
	{
		if(metaDataList == null) return null;
		
		List<Property> remoteMetadata = new ArrayList<Property>();
		
		for(int i=0;i<metaDataList.size();i++)
		{
			Property remoteData = toRemoteMetadata(metaDataList.get(i));
			if(remoteData!=null)
				remoteMetadata.add(remoteData);
		}
		Property[] result = remoteMetadata.toArray(new Property[0]);
		return result;
	}

	public static RecordAttachment[] toRemoteAttachments(List<Attachment> localAttachment) 
	{
		if(localAttachment == null) return null;
		List<RecordAttachment> remoteAttachments = new ArrayList<RecordAttachment>();
		
		for(int i=0;i<localAttachment.size();i++)
		{
			RecordAttachment remoteAttachment = toRemoteAttachment(localAttachment.get(i));
			if(remoteAttachment != null)
				remoteAttachments.add(remoteAttachment);
		}
		
		return remoteAttachments.toArray(new RecordAttachment[0]);
	}

	private static RecordAttachment toRemoteAttachment(Attachment attachmentModel)
	{
		if(attachmentModel == null) return null;
		return new RecordAttachment(attachmentModel.getName(), attachmentModel.getNotes());
	}


	public static List<List<Site>> toSiteList(RecordSite[][] sites) 
	{
		if(sites == null || sites.length == 0) 
			return null;
		
		List<List<Site>> listOfListOfSites = new ArrayList<List<Site>>();
		for(int i = 0;i < sites.length; i++)
		{
			List<Site> siteList = toSiteList(sites[i]);
			if(siteList != null) listOfListOfSites.add( siteList );
		}
		
		return listOfListOfSites;
	}

	private static List<Site> toSiteList(RecordSite[] recordSites) 
	{
		if(recordSites == null || recordSites.length == 0) 
			return null;
		
		List<Site> siteList = new ArrayList<Site>();
		for(int i = 0;i < recordSites.length; i++)
		{
			Site site = toSite(recordSites[i]);
			if(site != null) siteList.add( site );
		}
		
		return siteList;
	}

	private static Site toSite(RecordSite recordSite) 
	{
		return new Site(recordSite.getSeriesNo(), recordSite.getName());
	}

	public static List<MetaData> toMetaDataList(Property[] metadata) 
	{
		if(metadata == null)
			return null;
		
		List<MetaData> metadataList = new ArrayList<MetaData>();
		
		for(int i=0;i<metadata.length;i++){
			Property a = metadata[i];
			MetaData m = MetaData.createInstance(a.getName(), a.getValue());
			metadataList.add(m);
		}
		
		return metadataList;
	}

	public static List<VisualObject> toVisualObjects(Shape[] shapes) 
	{
		if(shapes == null) return null;
		
		List<VisualObject> vobjects = new ArrayList<VisualObject>();
		for(int i=0;i<shapes.length;i++)
		{
			VisualObject vobject = toVisualObject(shapes[i]);
			vobjects.add(vobject);
		}
		return vobjects;
	}

	private static VisualObject toVisualObject(Shape shape) 
	{
		if(shape == null) return null;
		
		VisualObject vo = null;
		if(shape instanceof EllipticalShape)
		{
			EllipticalShape s = (EllipticalShape)shape;
			vo = new Ellipse(shape.getID(), s.getX(), s.getY(), s.getWidth(), s.getHeight());
			vo.setRotation(s.getRotation());
		}
		else if (shape instanceof FreehandShape)
		{
			FreehandShape s = (FreehandShape)shape;
			Float[] coordinates = s.getCoordinates();
			vo = new GeometricPath(shape.getID(), 1 + coordinates.length/2);
			
			for (int i = 0; i < coordinates.length; i += 2)
			{
				((GeometricPath)vo).lineTo(coordinates[i], coordinates[i+1]);
			}
		}
		else if (shape instanceof TextArea)
		{
			TextArea s = (TextArea)shape;
			vo = new TextBox(s.getID(), s.getX(), s.getY(), s.getWidth(), s.getHeight(), s.getText());
			Font f = s.getFont();
			if(f!=null)
			{
				((TextBox)vo).setFont(new java.awt.Font(f.getName(), f.getStyle(), f.getSize()));
			}
		}
		else if (shape instanceof StraightLine)
		{
			StraightLine s = (StraightLine)shape;
			vo = new LineSegment(s.getID(), s.getStartX(), s.getStartY(), s.getEndX(), s.getEndY());
		}
		else if (shape instanceof RectangularShape)
		{
			RectangularShape s = (RectangularShape)shape;
			vo = new Rectangle(s.getID(), s.getX(), s.getY(), s.getWidth(), s.getHeight());
			vo.setRotation(s.getRotation());
		}
		
		if (vo != null)
		{
			vo.setPenColor( new Color(shape.getPenColor()) );
			vo.setPenWidth(shape.getPenWidth());
			vo.setZoomLevel(shape.getZoomLevel());
			vo.setType(VisualObjectType.valueOf(shape.getType()));
		}
		
		return vo;
	}

	public static Overlay toRemoteVisualAnnotation(VisualOverlay overlay) 
	{
		if(overlay == null) return null;
		
		Overlay va = new Overlay();
		va.setHeight(overlay.getHeight() );
		va.setWidth( overlay.getWidth() );
		va.setName( overlay.getName() );
		
		return va;
	}

	public static Overlay[] toRemoteVisualAnnotations(List<VisualOverlay> overlays) 
	{
		if(overlays == null) return null;
		
		List<Overlay> vas = new ArrayList<Overlay>();
		for(VisualOverlay vo : overlays)
		{
			vas.add(toRemoteVisualAnnotation(vo));
		}
		
		return vas.toArray(new Overlay[0]);
	}

	public static Shape[] toRemoteShapes(List<VisualObject> vobjects) 
	{
		if(vobjects == null) return null;
		
		List<Shape> shapes = new ArrayList<Shape>();
		for(VisualObject object : vobjects)
		{
			shapes.add(toRemoteShape(object));
		}
		
		return shapes.toArray(new Shape[0]);
	}

	public static Shape toRemoteShape(VisualObject object) 
	{
		if(object == null) return null;
		
		Shape s = null;
		if(object instanceof TextBox)
		{
			TextBox t = (TextBox) object;
			Rectangle2D.Double bounds = object.getBounds();
			java.awt.Font f = t.getFont();
			Font fo = new Font(f.getName(),f.getSize(), f.getStyle());
			s = new TextArea(t.ID, t.getPenColor().getRGB(), t.getPenWidth(), t.getType().name(), t.getZoomLevel(), bounds.getHeight(), object.getRotation(), bounds.getWidth(), bounds.getX(), bounds.getY(), t.getBkgColor().getRGB(), fo, t.getText());
		}
		else if(object instanceof Rectangle)
		{
			Rectangle2D.Double bounds = object.getBounds();
			s = new RectangularShape(object.ID, object.getPenColor().getRGB(), object.getPenWidth(), object.getType().name(),object.getZoomLevel(), bounds.getHeight(), object.getRotation(), bounds.getWidth(), bounds.getX(), bounds.getY());
		}
		else if(object instanceof Ellipse)
		{
			Rectangle2D.Double bounds = object.getBounds();
			s = new EllipticalShape(object.ID, object.getPenColor().getRGB(), object.getPenWidth(), object.getType().name(), object.getZoomLevel(), bounds.getHeight(), object.getRotation(), bounds.getWidth(), bounds.getX(), bounds.getY());
		}
		else if(object instanceof LineSegment)
		{
			s = new StraightLine(object.ID, object.getPenColor().getRGB(), object.getPenWidth(), object.getType().name(), object.getZoomLevel(), ((LineSegment) object).getEndX(), ((LineSegment) object).getEndY(), ((LineSegment) object).getStartX(), ((LineSegment) object).getStartY());
		}
		else if(object instanceof GeometricPath)
		{
			float[] coordinates = ((GeometricPath) object).getCoordinates();
			s = new FreehandShape(object.ID, object.getPenColor().getRGB(), object.getPenWidth(), object.getType().name(), object.getZoomLevel(), toFloatArray(coordinates));
		}
			
		return s;
	}

	private static Float[] toFloatArray(float[] coordinates) 
	{
		if(coordinates == null) return null;
		Float[] array = new Float[coordinates.length];
		for(int i = 0;i < coordinates.length; i++)
			array[i] = coordinates[i];
		
		return array;
	}

	public static EllipticalShape[] toRemoteEllipticalShapes(List<VisualObject> vobjects) 
	{
		if(vobjects == null) return null;
		
		List<EllipticalShape> shapes = new ArrayList<EllipticalShape>();
		for(VisualObject object : vobjects)
		{
			if(object instanceof Ellipse)
				shapes.add((EllipticalShape) toRemoteShape(object));
		}
		
		return shapes.toArray(new EllipticalShape[0]);
	}

	public static StraightLine[] toRemoteStraightLines(List<VisualObject> vobjects) 
	{
		if(vobjects == null) return null;
		
		List<StraightLine> shapes = new ArrayList<StraightLine>();
		for(VisualObject object : vobjects)
		{
			if(object instanceof LineSegment)
				shapes.add((StraightLine) toRemoteShape(object));
		}
		
		return shapes.toArray(new StraightLine[0]);
	}

	public static RectangularShape[] toRemoteRectangularShapes(List<VisualObject> vobjects) 
	{
		if(vobjects == null) return null;
		
		List<RectangularShape> shapes = new ArrayList<RectangularShape>();
		for(VisualObject object : vobjects)
		{
			if(object instanceof Rectangle)
				shapes.add((RectangularShape) toRemoteShape(object));
		}
		
		return shapes.toArray(new RectangularShape[0]);
	}

	public static FreehandShape[] toRemoteFreeHandShapes(
			List<VisualObject> vobjects) 
	{
		if(vobjects == null) return null;
		
		List<FreehandShape> shapes = new ArrayList<FreehandShape>();
		for(VisualObject object : vobjects)
		{
			if(object instanceof GeometricPath)
				shapes.add((FreehandShape) toRemoteShape(object));
		}
		
		return shapes.toArray(new FreehandShape[0]);
	}
	
	public static TextArea[] toRemoteTextArea(List<VisualObject> vobjects) {
		if(vobjects == null) return null;
		
		List<TextArea> shapes = new ArrayList<TextArea>();
		for(VisualObject object : vobjects)
		{
			if(object instanceof Rectangle)
				shapes.add((TextArea) toRemoteShape(object));
		}
		
		return shapes.toArray(new TextArea[0]);
	}

	public static Image toRemotePixelData(ImagePixelData p) 
	{
		if(p == null) return null;
		
		ImageIndex c = toImageCoordinate(p.getDimension());

		Image io = new Image();
		io.setElapsedTime(p.getElapsed_time());
		io.setExposureTime(p.getExposureTime());
		io.setIndex(c);
		io.setTimeStamp(p.getTimestamp().getTime());
		io.setX(p.getX());
		io.setY(p.getY());
		io.setZ(p.getZ());
		
		return io;
	}
	
	public static Dimension toDimension(ImageIndex c)
	{
		if(c == null) return null;
		return new Dimension(c.getFrame(), c.getSlice(), c.getChannel(), c.getSite());
	}
	
	public static VODimension toVODimension(VOIndex c)
	{
		if(c == null) return null;
		
		return new VODimension(c.getFrame(), c.getSlice(), c.getSite());
	}
	
	public static VODimension[] toVODimension(VOIndex[] c)
	{
		if(c == null) return null;
		
		VODimension[] coList = new VODimension[c.length];
		for(int i = 0;i < c.length; i++)
			coList[i] = toVODimension(c[i]);
		return coList;
	}
	
	public static VOIndex[] toVOCoordinates(List<VODimension> locations) 
	{
		if(locations == null || locations.size() == 0) return null;
		
		List<VOIndex> remoteLocations = new ArrayList<VOIndex>();
		for(VODimension location:locations)
		{
			VOIndex remoteLocation = toVOCoordinate(location);
			if(remoteLocation!=null)
				remoteLocations.add(remoteLocation);
		}
		return remoteLocations.toArray(new VOIndex[0]);
	}
	
	public static VOIndex toVOCoordinate(VODimension d)
	{
		if(d == null) return null;
		
		VOIndex c = new VOIndex();
		c.setFrame(d.frameNo);
		c.setSite(d.siteNo);
		c.setSlice(d.sliceNo);
		return c;
	}
	
	public static ImageIndex toImageCoordinate(Dimension d)
	{
		if(d == null) return null;
		
		ImageIndex c = new ImageIndex();
		c.setChannel(d.channelNo);
		c.setFrame(d.frameNo);
		c.setSite(d.siteNo);
		c.setSlice(d.sliceNo);
		return c;
	}

	public static Comments[] toComments(List<UserComment> comments)
	{
		if(comments == null || comments.isEmpty())
			return null;
		
		List<Comments> cList = new ArrayList<Comments>();
		for(UserComment uc : comments)
		{
			Comments c = toComments(uc);
			if(c != null) cList.add(c);
		}
		
		return cList.toArray(new Comments[0]);
	}

	private static Comments toComments(UserComment uc)
	{
		if(uc == null)  return null;
		Comments c = new Comments();
		c.setCreationDate( uc.getCreationDate().getTime());
		c.setNotes(uc.getNotes());
		c.setUserLogin(uc.getUserLogin());
		return c;
	}

	public static SearchField[] toRemoteSearchField(Collection<? extends com.strandgenomics.imaging.icore.SearchField> fields) 
	{
		if(fields == null || fields.size() == 0)
			return null;
		
		 List<SearchField> rFields = new ArrayList<SearchField>();
		 for(com.strandgenomics.imaging.icore.SearchField f : fields)
		 {
			 SearchField sf = toRemoteSearchField(f);
			 if(sf != null)
			 {
				 rFields.add(sf);
			 }
		 }
		 
		 return rFields.isEmpty() ? null : rFields.toArray(new SearchField[0]);
	}

	private static SearchField toRemoteSearchField(com.strandgenomics.imaging.icore.SearchField f)
	{
		if(f == null) return null;
		return new SearchField(f.fieldName, f.fieldType.ordinal());
	}

	public static SearchField[] toSearchFields(Collection<SearchColumn> fields) 
	{
		if(fields == null || fields.size() == 0)
			return null;
		
		 List<SearchField> rFields = new ArrayList<SearchField>();
		 for(SearchColumn f : fields)
		 {
			 SearchField sf = toRemoteSearchField(f);
			 if(sf != null)
			 {
				 rFields.add(sf);
			 }
		 }
		 
		 return rFields.isEmpty() ? null : rFields.toArray(new SearchField[0]);
	}

	public static Set<com.strandgenomics.imaging.icore.SearchCondition> toSearchConditions(SearchCondition[] preConditions) 
	{
		if(preConditions == null || preConditions.length == 0) return null;
		
		HashSet<com.strandgenomics.imaging.icore.SearchCondition> conditions = new HashSet<com.strandgenomics.imaging.icore.SearchCondition>();
		for(SearchCondition preCondition : preConditions)
		{
			com.strandgenomics.imaging.icore.SearchCondition searchCondition = toSearchCondition(preCondition);
			if(searchCondition != null)
			{
				conditions.add(searchCondition);
			}
		}
		
		return conditions;
	}

	public static com.strandgenomics.imaging.icore.SearchCondition toSearchCondition(SearchCondition preCondition) 
	{
		if(preCondition == null) return null;
		
		com.strandgenomics.imaging.icore.SearchCondition s = null;
		
		Object lowerLimit = preCondition.getLowerLimit();
		Object upperLimit = preCondition.getUpperLimit();
		
		if(lowerLimit instanceof Long)
			s = new com.strandgenomics.imaging.icore.SearchCondition(preCondition.getName(), (Long)lowerLimit, (Long)upperLimit);
		else if(lowerLimit instanceof Double)
			s = new com.strandgenomics.imaging.icore.SearchCondition(preCondition.getName(), (Double)lowerLimit, (Double)upperLimit);
		else if(lowerLimit instanceof Timestamp)
			s = new com.strandgenomics.imaging.icore.SearchCondition(preCondition.getName(), (Timestamp)lowerLimit, (Timestamp)upperLimit);
		else if(lowerLimit instanceof String)
			s = new com.strandgenomics.imaging.icore.SearchCondition(preCondition.getName(), (String)lowerLimit, (String)upperLimit);
		
		return s;
	}

	public static SearchNode[] toSearchNodes(List<NavigationBin> bins) 
	{
		if(bins == null || bins.size() == 0) return null;
		
		List<SearchNode> nodes = new ArrayList<SearchNode>();
		
		for(NavigationBin bin:bins)
		{
			SearchNode node = toSearchNode(bin);
			if(node != null)
				nodes.add(node);
		}
		
		return nodes.toArray(new SearchNode[0]);
	}

	private static SearchNode toSearchNode(NavigationBin bin) 
	{
		if(bin == null) return null;
		
		Set<com.strandgenomics.imaging.icore.SearchCondition> preConditions = bin.getPreExitingFilters();
		SearchCondition[] remotePreConditions = toRemoteSearchConditions(preConditions);
		
		SearchNode node = new SearchNode(remotePreConditions, bin.getRecordCount());
		
		return node;
	}

	private static SearchCondition[] toRemoteSearchConditions(Set<com.strandgenomics.imaging.icore.SearchCondition> preConditions) 
	{
		if(preConditions == null || preConditions.size()==0)
			return null;
		
		List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
		for(com.strandgenomics.imaging.icore.SearchCondition preCondition:preConditions)
		{
			SearchCondition searchCondition = toRemoteSearchCondition(preCondition);
			if(searchCondition != null)
				searchConditions.add(searchCondition);
		}
		
		return searchConditions.toArray(new SearchCondition[0]);
	}

	private static SearchCondition toRemoteSearchCondition(com.strandgenomics.imaging.icore.SearchCondition preCondition) 
	{
		if(preCondition == null) return null;
		
		AnnotationType annotationType = preCondition.getType();
		int type = 2;// default text?
		if(annotationType == AnnotationType.Integer)
			type = 0;
		else if(annotationType == AnnotationType.Real)
			type = 1;
		else if(annotationType == AnnotationType.Text)
			type = 2;
		else if(annotationType == AnnotationType.Time)
			type = 3;
		
		return new SearchCondition(preCondition.getField(), type, preCondition.getLowerLimit(), preCondition.getUpperLimit());
	}

	public static Application[] toRemoteApplications(List<com.strandgenomics.imaging.icore.app.Application> applications)
	{
		if(applications == null) return null;
		
		List<Application> remoteApplications = new ArrayList<Application>();
		for(com.strandgenomics.imaging.icore.app.Application app:applications)
		{
			remoteApplications.add(toRemoteApplication(app));
		}
		
		return remoteApplications.toArray(new Application[0]);
	}

	public static Application toRemoteApplication(com.strandgenomics.imaging.icore.app.Application app)
	{
		if(app == null)	return null;
		
		Application remoteApplication = new Application();
		remoteApplication.setName(app.name);
		
		
		if(app instanceof ApplicationSpecification)
		{
			remoteApplication.setCategoryName(((ApplicationSpecification)app).categoryName);
			remoteApplication.setDescription(((ApplicationSpecification)app).getDescription());
			
		}
		
		return remoteApplication;
	}
	
	public static Statistics toImageStatistics(Histogram h) 
	{
		if(h == null) return null;
		Statistics stat = new Statistics();
		stat.setFrequencies(toIntegerArray(h.getFrequencies()));
		stat.setIntensities(toIntegerArray(h.getIntensities()));
		stat.setMaxFreq(h.getMaxFrequency());
		stat.setMaxValue(h.getMax());
		stat.setMinValue(h.getMin());
		stat.setPixelDepth(h.pixelDepth.getByteSize());
		
		return stat;
	}
	
	public static final Integer[] toIntegerArray(int[] a)
	{
		if(a == null || a.length == 0) return null;
		Integer[] A = new Integer[a.length];
		for(int i = 0;i < a.length; i++)
			A[i] = a[i];
		
		return A;
	}
	
	public static Archive toRemoteArchive(com.strandgenomics.imaging.iengine.models.Archive archive) 
	{
		if(archive == null) return null;
		
		Archive a = new Archive();
		a.setSignature( Util.toHexString( archive.getSignature()));
		a.setSourceFiles( toSourceFile(archive.getSourceFiles()) );
		
		return a;
	}

	private static SourceFile[] toSourceFile(List<ISourceReference> sFiles)
	{
		if(sFiles == null || sFiles.isEmpty()) return null;
		List<SourceFile> sourceFiles = new ArrayList<SourceFile>();
		
		for(ISourceReference sf : sFiles)
		{
			if(sf == null) continue;
			sourceFiles.add( toSourceFile(sf) );
		}
		return sourceFiles.toArray( new SourceFile[0]);
	}

	private static SourceFile toSourceFile(ISourceReference sf) 
	{
		if(sf == null) return null;
		SourceFile r = new SourceFile();
		r.setFilePath(sf.getSourceFile());
		r.setLastModificationTime(sf.getLastModified());
		r.setLength(sf.getSize());
		return r;
	}

	public static Parameter[] toRemoteParameters(Set<com.strandgenomics.imaging.icore.app.Parameter> parameters)
	{
		if(parameters == null) return null;
		
		List<Parameter> remoteParameters = new ArrayList<Parameter>();
		for(com.strandgenomics.imaging.icore.app.Parameter parameter:parameters)
		{
			remoteParameters.add(toRemoteParameter(parameter));
		}
		
		return remoteParameters.toArray(new Parameter[0]);
	}
	
	public static Parameter toRemoteParameter(com.strandgenomics.imaging.icore.app.Parameter parameter)
	{
		if(parameter == null) return null;
		
		Constraints constraints = toRemoteConstraints(parameter.constraints);
		
		Parameter remoteParameter = new Parameter(constraints, parameter.defaultValue, parameter.description, parameter.name, parameter.type.name());
		return remoteParameter;
	}
	
	public static Constraints toRemoteConstraints(ParameterConstraints constraints)
	{
		if(constraints == null) return null;
		
		Constraints remoteConstraints = null;
		if(constraints.getType() == ParameterType.INTEGER)
		{
			if(constraints instanceof ListConstraints)
			{
				Set<Object>validValues = ((ListConstraints)constraints).getValidValues();
				Long[] longValues = new Long[validValues.size()];
				int i = 0;
				for(Object value:validValues)
				{
					longValues[i] = (Long)value;
					i++;
				}
				remoteConstraints = new LongListConstraints(longValues);
			}
			else if(constraints instanceof RangeConstraints)
			{
				long longLowerLimit = (Long) ((RangeConstraints)constraints).lowerLimit;
				long longUpperLimit = (Long) ((RangeConstraints)constraints).upperLimit;
				
				remoteConstraints = new LongRangeConstraints(longLowerLimit, longUpperLimit);
			}
		}
		
		if(constraints.getType() == ParameterType.DECIMAL)
		{
			if(constraints instanceof ListConstraints)
			{
				Set<Object>validValues = ((ListConstraints)constraints).getValidValues();
				Double[] doubleValues = new Double[validValues.size()];
				int i = 0;
				for(Object value:validValues)
				{
					doubleValues[i] = (Double)value;
					i++;
				}
				remoteConstraints = new DoubleListConstraints(doubleValues);
			}
			else if(constraints instanceof RangeConstraints)
			{
				double doubleLowerLimit = (Double) ((RangeConstraints)constraints).lowerLimit;
				double doubleUpperLimit = (Double) ((RangeConstraints)constraints).upperLimit;
				
				remoteConstraints = new DoubleRangeConstraints(doubleLowerLimit, doubleUpperLimit);
			}
		}
		
		return remoteConstraints;
	}

	public static Map<String, String> toLocalParameters(NVPair[] parameters)
	{
		if(parameters == null) return null;
		
		Map<String, String> nameValueMap = new HashMap<String, String>();
		for(int i=0;i<parameters.length;i++)
		{
			nameValueMap.put(parameters[i].getName(), String.valueOf(parameters[i].getValue()));
		}
		
		return nameValueMap;
	}

	public static Publisher[] toPublishers(Collection<com.strandgenomics.imaging.iengine.compute.Publisher> localPublishers)
	{
		if(localPublishers == null) return null;
		
		List<Publisher> publishers = new ArrayList<Publisher>();
		for(com.strandgenomics.imaging.iengine.compute.Publisher publisher:localPublishers)
		{
			publishers.add(toPublisher(publisher));
		}
		return publishers.toArray(new Publisher[0]);
	}

	private static Publisher toPublisher(com.strandgenomics.imaging.iengine.compute.Publisher localPublisher)
	{
		if(localPublisher == null) return null;
		
		Publisher publisher = new Publisher("", "", 0, localPublisher.name, 0); // TODO: publisher only contains name
		return publisher;
	}

	public static HistoryItem[] toRemoteHistory(List<HistoryObject> history)
	{
		if(history == null) return null;
		
		List<HistoryItem> remoteHistory = new ArrayList<HistoryItem>();
		for(HistoryObject item:history)
		{
			HistoryItem remoteItem = toRemoteHistoryItem(item);
			if(remoteItem != null)
				remoteHistory.add(remoteItem);
		}
		
		return remoteHistory.toArray(new HistoryItem[0]);
	}

	private static HistoryItem toRemoteHistoryItem(HistoryObject item)
	{
		if(item == null) return null;
		
		HistoryItem remoteItem = new HistoryItem();
		remoteItem.setAppName(item.getClient().name);
		remoteItem.setAppVersion(item.getClient().version);
		remoteItem.setGuid(item.getGuid());
		remoteItem.setDescription(item.getDescription());
		remoteItem.setModificationTime(item.getModificationTime());
		remoteItem.setModifiedBy(item.getModifiedBy());
		remoteItem.setType(item.getType().name());
		
		return remoteItem;
	}

	public static ImagePixelData toImagePixelData(com.strandgenomics.imaging.iserver.services.ws.loader.Image imageData)
	{
		if(imageData == null) return null;
		
		ImagePixelData pixelData = new ImagePixelData(imageData.getX(),
				imageData.getY(), imageData.getZ(), imageData.getElapsedTime(),
				imageData.getExposureTime(),
				new Date(imageData.getTimeStamp()), imageData.getIndex()
						.getSlice(), imageData.getIndex().getFrame(), imageData
						.getIndex().getChannel(), imageData.getIndex()
						.getSite());
		return pixelData;
	}

	public static AcquisitionProfile[] toRemoteAcqProfiles(List<com.strandgenomics.imaging.iengine.models.AcquisitionProfile> acqProfiles)
	{
		if(acqProfiles == null)
			return null;
		List<AcquisitionProfile> remoteProfiles = new ArrayList<AcquisitionProfile>();
		for(com.strandgenomics.imaging.iengine.models.AcquisitionProfile profile:acqProfiles)
		{
			AcquisitionProfile remoteProfile = toRemoteAcqProfile(profile);
			if(remoteProfile!=null)
				remoteProfiles.add(remoteProfile);
		}
		return remoteProfiles.toArray(new AcquisitionProfile[0]);
	}
	
	private static AcquisitionProfile toRemoteAcqProfile(com.strandgenomics.imaging.iengine.models.AcquisitionProfile profile)
	{
		if(profile == null)
			return null;
		AcquisitionProfile remoteProfile = new AcquisitionProfile();
		
		remoteProfile.setLengthUnit(profile.getLengthUnit().name());
		remoteProfile.setTimeUnit(profile.getElapsedTimeUnit().name());
		
		remoteProfile.setXPixelSize(profile.getxPixelSize());
		remoteProfile.setXType(profile.getXType().name());
		
		remoteProfile.setYPixelSize(profile.getyPixelSize());
		remoteProfile.setYType(profile.getYType().name());
		
		remoteProfile.setZPixelSize(profile.getzPixelSize());
		remoteProfile.setZType(profile.getZType().name());
		
		remoteProfile.setName(profile.getMicroscope());
		//remoteProfile.setSourceFormat(profile.getSourceType().name);
		String sourceType = profile.getSourceType() == null ? null : profile.getSourceType().name;
		remoteProfile.setSourceFormat(sourceType);
		remoteProfile.setProfileName(profile.getProfileName());
		return remoteProfile;
	}

	public static com.strandgenomics.imaging.iengine.models.AcquisitionProfile toLocalAcqProfile(AcquisitionProfile remoteProfile)
	{
		if(remoteProfile == null) return null;
		
		
		String profileName = remoteProfile.getProfileName();
		String microscopeName = remoteProfile.getName();
		Double xPixelSize = remoteProfile.getXPixelSize();
		Double yPixelSize = remoteProfile.getYPixelSize();
		Double zPixelSize = remoteProfile.getZPixelSize();
		
		AcquisitionProfileType xType = AcquisitionProfileType.valueOf(remoteProfile.getXType());
		AcquisitionProfileType yType = AcquisitionProfileType.valueOf(remoteProfile.getYType());
		AcquisitionProfileType zType = AcquisitionProfileType.valueOf(remoteProfile.getZType());
		
		String sourceType = remoteProfile.getSourceFormat();
		SourceFormat sourceFormat = (sourceType==null || sourceType.isEmpty())? null :new SourceFormat(sourceType);
		
		String timeUnit = remoteProfile.getTimeUnit();
		String lengthUnit = remoteProfile.getLengthUnit();
		
		TimeUnit tUnit = TimeUnit.valueOf(timeUnit);
		LengthUnit lUnit = LengthUnit.valueOf(lengthUnit);
		com.strandgenomics.imaging.iengine.models.AcquisitionProfile localProfile = new com.strandgenomics.imaging.iengine.models.AcquisitionProfile(profileName, microscopeName, xPixelSize, xType, yPixelSize, yType, zPixelSize, zType, sourceFormat, tUnit, tUnit, lUnit);
		
		return localProfile;
	}
	
	public static com.strandgenomics.imaging.iengine.models.MosaicRequest toLocalMosaicRequest(MosaicRequest remoteRequest){

		com.strandgenomics.imaging.iengine.models.MosaicRequest localRequest = new com.strandgenomics.imaging.iengine.models.MosaicRequest();
		
		long[] ids = new long[remoteRequest.getRecordids().length];
		
		int k=0;
		for(Long id : remoteRequest.getRecordids()){
			ids[k]=id;
			k++;
		}
		
		localRequest.setRecordids(ids);
		
		return localRequest;
		
	}
	
	public static MosaicResource toRemoteMosaicResource(com.strandgenomics.imaging.iengine.models.MosaicResource resource) {
		
		Long[] ids = new Long[resource.getRecordids().length];
		
		int k=0;
		for(long id : resource.getRecordids()){
			ids[k]=id;
			k++;
		}
		
		MosaicResource remoteResource = new MosaicResource(resource.getAnchor_left(), resource.getAnchor_top(), resource.getMosaicImageWidth(), resource.getMosiacImageHeight(), ids);
	
		return remoteResource;
	}
	
	public static com.strandgenomics.imaging.iengine.models.MosaicResource toLocalMosaicResource(MosaicResource remoteResource){

		com.strandgenomics.imaging.iengine.models.MosaicResource localResource = new com.strandgenomics.imaging.iengine.models.MosaicResource();
		
		long[] ids = new long[remoteResource.getRecordids().length];
		
		int k=0;
		for(Long id : remoteResource.getRecordids()){
			ids[k]=id;
			k++;
		}
		
		localResource.setRecordids(ids);
		localResource.setAnchor_left(remoteResource.getAnchor_left());
		localResource.setAnchor_top(remoteResource.getAnchor_top());
		
		return localResource;
		
	}
	
	public static com.strandgenomics.imaging.iengine.models.MosaicParameters toLocalMosaicParameters(MosaicParameters remoteParameters){

		com.strandgenomics.imaging.iengine.models.MosaicParameters localParameters = new com.strandgenomics.imaging.iengine.models.MosaicParameters(remoteParameters.getX(),remoteParameters.getY(), remoteParameters.getWidth(), remoteParameters.getHeight(), remoteParameters.getTileWidth(), remoteParameters.getTileHeight());
		
		return localParameters;
		
	}
}
