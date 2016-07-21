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

package com.strandgenomics.imaging.iclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.strandgenomics.imaging.iclient.impl.ws.compute.Constraints;
import com.strandgenomics.imaging.iclient.impl.ws.compute.DoubleListConstraints;
import com.strandgenomics.imaging.iclient.impl.ws.compute.DoubleRangeConstraints;
import com.strandgenomics.imaging.iclient.impl.ws.compute.LongListConstraints;
import com.strandgenomics.imaging.iclient.impl.ws.compute.LongRangeConstraints;
import com.strandgenomics.imaging.iclient.impl.ws.compute.NVPair;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.Contrast;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.HistoryItem;
import com.strandgenomics.imaging.iclient.impl.ws.loader.Image;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.RawRecord;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.app.ListConstraints;
import com.strandgenomics.imaging.icore.app.Parameter;
import com.strandgenomics.imaging.icore.app.ParameterConstraints;
import com.strandgenomics.imaging.icore.app.ParameterType;
import com.strandgenomics.imaging.icore.app.RangeConstraints;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.icore.vo.VisualObjectType;

public class CoercionHelper {

	public static final Project toLocalProject(com.strandgenomics.imaging.iclient.impl.ws.ispace.Project remoteProject) 
	{
		if (remoteProject == null)
			return null;

		return new Project(remoteProject.getName(), 
				remoteProject.getName(),
				new Date(remoteProject.getCreationDate()),
				remoteProject.getStorageQuota(), 
				remoteProject.getSpaceUsage(),
				remoteProject.getRecordCount());
	}
	
	public static final com.strandgenomics.imaging.iclient.impl.ws.loader.CreationRequest toCreationRequest(RawExperiment experiment) 
	{		
		com.strandgenomics.imaging.iclient.impl.ws.loader.Archive ra = new com.strandgenomics.imaging.iclient.impl.ws.loader.Archive();
		ra.setSignature(Util.toHexString(experiment.getMD5Signature()));
		ra.setSourceFiles(toClientFile(experiment.getReference()));
		ra.setRootDirectory(experiment.getRootDirectory());
		ra.setName(experiment.getSourceFilename());
		
		com.strandgenomics.imaging.iclient.impl.ws.loader.CreationRequest request = new com.strandgenomics.imaging.iclient.impl.ws.loader.CreationRequest();

		request.setArchive(ra);
		request.setClientMacAddress(experiment.getOriginMachineAddress());
		request.setValidSignatures( toRemoteSpec(experiment) );
		
		return request;
	}
	
	public static final com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSpecification[] toRemoteSpec(RawExperiment expt)
	{
		List<com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSpecification> specList = new ArrayList<com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSpecification>();

		for(Signature s : expt.getRecordSignatures())
		{
			IRecord r = expt.getRecord(s);
			List<Site> sList = new ArrayList<Site>();
			
			for(int i = 0;i < r.getSiteCount(); i++)
			{
				sList.add( r.getSite(i));
			}
			
			List<Channel> channels = ((RawRecord)r).getChannels();
			specList.add( new com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSpecification( toRemoteChannel(channels), toRemoteSite(sList)));
		}
		
		return specList.toArray(new com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSpecification[0]);
	}
	
	public static final Record[] toLocalRecord(com.strandgenomics.imaging.iclient.impl.ws.ispace.Record[] rList)
	{
		if(rList == null || rList.length == 0)
			return null;
		
		Record[] recordList = new Record[rList.length];
		
		for(int i = 0;i < rList.length; i++)
		{
			recordList[i] = toLocalRecord(rList[i]);
		}
		
		return recordList;
	}

	public static final Record toLocalRecord(com.strandgenomics.imaging.iclient.impl.ws.ispace.Record r) 
	{
		if(r == null) return null;
		
		Signature s = toSignature(r.getSignature());
		
		return new Record(
				r.getUploadedBy(), 
				r.getUploadTime() == null ? null : new Date(r.getUploadTime()),
				r.getCreationTime() == null ? null : new Date(r.getCreationTime()), 
				r.getAcquiredDate() == null ? null : new Date(r.getAcquiredDate()), 
				r.getSourceFileTime() == null ? null : new Date(r.getSourceFileTime()),
				r.isWritable(),
				r.getMacAddress(),
				r.getIpAddress(), 
				s,
				toLocalPixelDepth(r.getPixelDepth()),
				r.getPixelSizeAlongXAxis(), 
				r.getPixelSizeAlongYAxis(), 
				r.getPixelSizeAlongZAxis(),
				toLocalImageType(r.getImageType()), 
				new SourceFormat( r.getSourceFormat() ),
				r.getSourceFolder(), 
				r.getSourceFilename());

	}
	
	public static Signature toSignature(com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint fingerPrint) 
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
	
	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint toRemoteSignature(Signature signature) 
	{
		com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint s = new com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint();
		
		s.setArchiveHash( Util.toHexString(signature.archiveHash) );
		s.setSiteHash( Util.toHexString(signature.siteHash) );
		
		s.setImageHeight( signature.imageHeight );
		s.setImageWidth( signature.imageWidth );
		
		s.setNoOfChannels( signature.noOfChannels );
		s.setNoOfFrames( signature.noOfFrames );
		s.setNoOfSlices( signature.noOfSlices );
		s.setNoOfSites( signature.noOfSites );
		
		return s;
	}
	
	public static com.strandgenomics.imaging.iclient.impl.ws.loader.Channel[] toRemoteChannel(List<Channel> channels) 
	{
		if(channels == null) return null;
		
		List<com.strandgenomics.imaging.iclient.impl.ws.loader.Channel> rChannels = new ArrayList<com.strandgenomics.imaging.iclient.impl.ws.loader.Channel>();
		for(Channel c : channels)
		{
			com.strandgenomics.imaging.iclient.impl.ws.loader.Channel rChannel = toRemoteChannel( c );
			if(rChannel != null) rChannels.add(rChannel);
		}
		
		return rChannels.isEmpty() ? null : rChannels.toArray(new com.strandgenomics.imaging.iclient.impl.ws.loader.Channel[0]);
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.loader.Channel toRemoteChannel(Channel c)
	{
		if(c == null) return null;
		com.strandgenomics.imaging.iclient.impl.ws.loader.Channel r = new com.strandgenomics.imaging.iclient.impl.ws.loader.Channel();
		r.setName(c.getName());
		r.setLutName(c.getLut());
		r.setContrast( toRemoteContrast(c.getContrast(false)) );
		r.setZStackedContrast(toRemoteContrast(c.getContrast(true)));
		r.setWavelength(c.getWavelength());
		
		return r;
	}
	
	public static final com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast toRemoteContrast(VisualContrast c)
	{
		if(c == null) return null;
		com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast rc = new com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast();
		rc.setGamma( c.getGamma() );
		rc.setMaxIntensity( c.getMaxIntensity() );
		rc.setMinIntensity( c.getMinIntensity() );
		return rc;
	}
	
	
	public static com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[] toRemoteSite(List<Site> list) 
	{
		if(list == null || list.isEmpty())
			return null;
		
		List<com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite> rSites = new ArrayList<com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite>();
		for(Site s : list)
		{
			com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite rs = toRemoteSite(s);
			if(rs != null) rSites.add(rs);
		}
		
		return rSites.isEmpty() ? null : rSites.toArray(new com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[0]);
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[] toRemoteSite(Record r) 
	{
		List<com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite> rSites = new ArrayList<com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite>();
		for(int i = 0;i < r.getSiteCount(); i++)
		{
			com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite remoteSite = toRemoteSite( r.getSite(i) );
			if(remoteSite != null) rSites.add(remoteSite);
		}
		
		return rSites.isEmpty() ? null : rSites.toArray(new com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[0]);
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite toRemoteSite(Site s) 
	{
		if(s == null) return null;
		com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite r = new com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite();
		r.setName(s.getName());
		r.setSeriesNo(s.getSeriesNo());
		return r;
	}
	
	public static List<Site> toSiteList(com.strandgenomics.imaging.iclient.impl.ws.ispace.RecordSite[] recordSites) 
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
	
	public static Site toSite(com.strandgenomics.imaging.iclient.impl.ws.ispace.RecordSite recordSite) 
	{
		return new Site(recordSite.getSeriesNo(), recordSite.getName());
	}

	public static final User toLocalUser(com.strandgenomics.imaging.iclient.impl.ws.manage.User remoteUser) 
	{
		if (remoteUser == null)
			return null;
		
		return new User(remoteUser.getLogin(), remoteUser.getEmailID(),
				Rank.valueOf(remoteUser.getRank()));
	}

	public static final List<User> toLocalUser(com.strandgenomics.imaging.iclient.impl.ws.manage.User[] remoteUsers) 
	{
		if (remoteUsers == null || remoteUsers.length == 0)
			return null;
		
		List<User> userList = new ArrayList<User>();

		for (com.strandgenomics.imaging.iclient.impl.ws.manage.User remoteUser : remoteUsers) 
		{
			userList.add(toLocalUser(remoteUser));
		}

		return userList;
	}


	public static final ImageType toLocalImageType(int type)
	{
		//TODO: check this
		if(type == 0)
			return ImageType.GRAYSCALE;
		else 
			return ImageType.RGB;
	}
	
	public static final PixelDepth toLocalPixelDepth(int depth)
	{
		if(depth == 1)
			return PixelDepth.BYTE;
		if(depth == 2)
			return PixelDepth.SHORT;
		if(depth == 4)
			return PixelDepth.INT;
		return null;
	}

	public static final com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile[] toClientFile(List<ISourceReference> sourceRefs) 
	{
		if (sourceRefs == null || sourceRefs.isEmpty())
			return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile[] sourceList = new com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile[sourceRefs.size()];

		for (int i = 0; i < sourceList.length; i++) {
			sourceList[i] = toClientFile(sourceRefs.get(i));
		}

		return sourceList;
	}

	public static final com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile toClientFile(ISourceReference iSourceReference) 
	{
		if (iSourceReference == null)
			return null;
		
		return new com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile(
				iSourceReference.getSourceFile(),
				iSourceReference.getLastModified(),
				iSourceReference.getSize());
	}

	public static Ticket toLocalTicket(long id, BigInteger archiveID, URL uploadURL, URL downloadURL) 
	{
		return new Ticket(id, archiveID, uploadURL, downloadURL);
	}

	/**
	 * creates user annotation (metadata) object to send to server
	 * @param name name of the user annotation
	 * @param value value of the user annotation
	 * @return 
	 */
	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.Property toProperty(String name, Object value) 
	{
		com.strandgenomics.imaging.iclient.impl.ws.ispace.Property p = new com.strandgenomics.imaging.iclient.impl.ws.ispace.Property();
		p.setName(name);
		p.setValue(value);
		
		return p;
	}

	public static IAttachment toLocalAttachment(long guid, com.strandgenomics.imaging.iclient.impl.ws.ispace.RecordAttachment recordAttachment)
{
		if (recordAttachment == null)
			return null;

		Attachment localAttachment = new Attachment(guid,
				recordAttachment.getName(), recordAttachment.getNotes());
		return localAttachment;
	}
	
	public static List<Channel> toLocalChannel(com.strandgenomics.imaging.iclient.impl.ws.ispace.Channel[] remoteChannel)
	{
		if(remoteChannel == null || remoteChannel.length == 0)
			return null;
		
		List<Channel> channels = new ArrayList<Channel>();
		for(com.strandgenomics.imaging.iclient.impl.ws.ispace.Channel rc : remoteChannel)
		{
			Channel c = toLocalChannel(rc);
			if(c != null) channels.add(c);
		}
		return channels;
	}

	public static Channel toLocalChannel(com.strandgenomics.imaging.iclient.impl.ws.ispace.Channel r) 
	{
		if (r == null)
			return null;

		Channel c = new Channel(r.getName(), r.getLutName());
		c.setContrast(true,  toLocalContrast(r.getZStackedContrast()));
		c.setContrast(false, toLocalContrast(r.getZStackedContrast()));
		c.setWavelength(r.getWavelength());
		
		return c;
	}
	
	public static final VisualContrast toLocalContrast(com.strandgenomics.imaging.iclient.impl.ws.ispace.Contrast c)
	{
		if(c == null) return null;
		return new VisualContrast(c.getMinIntensity(), c.getMaxIntensity(), c.getGamma());
	}

	
	public static Task toLocalTask(com.strandgenomics.imaging.iclient.impl.ws.manage.Task task)
	{
		if(task == null) return null;
		return new Task(task.getId(), task.getName());
	}
	
	public static final List<SearchField> toSearchField(com.strandgenomics.imaging.iclient.impl.ws.search.SearchField[] searchFields)
	{
		if(searchFields == null || searchFields.length == 0)
			return null;
		
		List<SearchField> navigableFields = new ArrayList<SearchField>();
		for(int i=0;i<searchFields.length;i++)
		{
			navigableFields.add(CoercionHelper.toSearchField(searchFields[i]));
		}

		return navigableFields;
	}

	public static SearchField toSearchField(com.strandgenomics.imaging.iclient.impl.ws.search.SearchField searchField)
	{
		if(searchField == null) return null;
		AnnotationType type = AnnotationType.values()[searchField.getType()];
		return new SearchField(searchField.getName(), type);
	}

	public static Ellipse toLocalEllipseShape(com.strandgenomics.imaging.iclient.impl.ws.ispace.EllipticalShape ellipticalShape) 
	{
		if(ellipticalShape == null) return null;
		
		Ellipse e = new Ellipse(ellipticalShape.getID(),ellipticalShape.getX(),
				ellipticalShape.getY(), ellipticalShape.getWidth(),
				ellipticalShape.getHeight());
		e.setPenColor(new Color(ellipticalShape.getPenColor()));
		e.setPenWidth(ellipticalShape.getPenWidth());
		e.setRotation(ellipticalShape.getRotation());
		e.setZoomLevel(ellipticalShape.getZoomLevel());
		e.setType(VisualObjectType.valueOf(ellipticalShape.getType()));
		return e;
	}

	public static LineSegment toLocalLineSegment(com.strandgenomics.imaging.iclient.impl.ws.ispace.StraightLine straightLine) 
	{
		if(straightLine == null) return null;
		
		LineSegment l = new LineSegment(straightLine.getID(),straightLine.getStartX(),
				straightLine.getStartY(), straightLine.getEndX(),
				straightLine.getEndY());
		l.setPenColor(new Color(straightLine.getPenColor()));
		l.setPenWidth(straightLine.getPenWidth());
		l.setZoomLevel(straightLine.getZoomLevel());
		l.setType(VisualObjectType.valueOf(straightLine.getType()));
		return l;
	}

	public static Rectangle toLocalRectangleShape(com.strandgenomics.imaging.iclient.impl.ws.ispace.RectangularShape rectangularShape)
	{
		if(rectangularShape == null) return null;

		Rectangle r = new Rectangle(rectangularShape.getID(),rectangularShape.getX(), rectangularShape.getY(),
				rectangularShape.getWidth(), rectangularShape.getHeight());
		r.setPenColor(new Color(rectangularShape.getPenColor()));
		r.setPenWidth(rectangularShape.getPenWidth());
		r.setRotation(rectangularShape.getRotation());
		r.setZoomLevel(rectangularShape.getZoomLevel());
		r.setType(VisualObjectType.valueOf(rectangularShape.getType()));
		return r;
	}

	public static TextBox toLocalTextArea(com.strandgenomics.imaging.iclient.impl.ws.ispace.TextArea textArea)
	{
		if(textArea == null) return null;
		
		TextBox t = new TextBox(textArea.getID(), textArea.getX(), textArea.getY(),
				textArea.getWidth(), textArea.getHeight(), textArea.getText());
		t.setPenColor(new Color(textArea.getPenColor()));
		t.setPenWidth(textArea.getPenWidth());
		t.setZoomLevel(textArea.getZoomLevel());
		t.setType(VisualObjectType.valueOf(textArea.getType()));
		
		return t;
	}
	
	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape toShape(VisualObject vobject)
	{
		if(vobject == null)
			return null;
		
		Rectangle2D.Double bounds = vobject.getBounds();
		double rotation = vobject.getRotation();
		int id = vobject.ID;
		int penColor = vobject.getPenColor().getRGB();
		float penWidth = vobject.getPenWidth();
		int zoomLevel = vobject.getZoomLevel();
		VisualObjectType type = vobject.getType();
		com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape s = null;
		
		if(vobject instanceof TextBox)
		{
			TextBox t = (TextBox) vobject;
			Font f = t.getFont();
			com.strandgenomics.imaging.iclient.impl.ws.ispace.Font fo = new com.strandgenomics.imaging.iclient.impl.ws.ispace.Font(f.getName(), f.getSize(), f.getStyle());
			s = new com.strandgenomics.imaging.iclient.impl.ws.ispace.TextArea(id,penColor,penWidth, type.name(), zoomLevel, bounds.height, rotation, bounds.width, bounds.x, bounds.y, t.getBkgColor().getRGB(), fo, ((TextBox) vobject).getText()); 
		}
		
		else if(vobject instanceof Rectangle)
		{
			s = new com.strandgenomics.imaging.iclient.impl.ws.ispace.RectangularShape(id, penColor, penWidth, type.name(), zoomLevel, bounds.height, rotation, bounds.width, bounds.x, bounds.y);
		}
		
		else if(vobject instanceof LineSegment)
		{
			s = new com.strandgenomics.imaging.iclient.impl.ws.ispace.StraightLine(id, penColor, penWidth, type.name(), zoomLevel,
					((LineSegment) vobject).getEndX(),
					((LineSegment) vobject).getEndY(),
					((LineSegment) vobject).getStartX(),
					((LineSegment) vobject).getStartY());
		}
		
		else if(vobject instanceof GeometricPath)
		{
			float[] coordinates = ((GeometricPath)vobject).getCoordinates();
			s = new com.strandgenomics.imaging.iclient.impl.ws.ispace.FreehandShape(id, penColor, penWidth, type.name(), zoomLevel, coordinates);
		}
		
		else if(vobject instanceof Ellipse)
		{
			s = new com.strandgenomics.imaging.iclient.impl.ws.ispace.EllipticalShape(id, penColor, penWidth, type.name(), zoomLevel,
					bounds.getHeight(),rotation, bounds.getWidth(), bounds.getX(),
					bounds.getY());
		}
		return s;
	}

	public static GeometricPath toLocalFreeHandShapes(com.strandgenomics.imaging.iclient.impl.ws.ispace.FreehandShape freehandShape) 
	{
		if(freehandShape == null) return null;
		
		GeometricPath p = new GeometricPath(freehandShape.getID(), 0);
		p.setPenColor(new Color(freehandShape.getPenColor()));
		p.setPenWidth(freehandShape.getPenWidth());
		p.setZoomLevel(freehandShape.getZoomLevel());
		p.setType(VisualObjectType.valueOf(freehandShape.getType()));
		float[] coordinates = freehandShape.getCoordinates();
		for (int i = 0; i < coordinates.length; i += 2)
		{
			p.lineTo(coordinates[i], coordinates[i+1]);
		}
		
		return p;
	}

	public static Collection<IVisualOverlay> toLocalOverlays(com.strandgenomics.imaging.iclient.impl.ws.ispace.Overlay[] overlays, long guid, VODimension dimension)
	{
		if(overlays == null)
			return null;
		
		List<IVisualOverlay> localOverlays = new ArrayList<IVisualOverlay>();
		for(int i=0;i<overlays.length;i++)
		{
			localOverlays.add(toLocalOverlay(overlays[i], guid, dimension));
		}
		
		return localOverlays;
	}

	public static IVisualOverlay toLocalOverlay(com.strandgenomics.imaging.iclient.impl.ws.ispace.Overlay va, long guid, VODimension dimension) 
	{
		if(va == null) return null;
		return new VisualOverlay(guid, dimension, va.getName(), va.getWidth(), va.getHeight());
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape[] toShapes(Collection<VisualObject> vObjects) 
	{
		com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape shapes[] = new com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape[vObjects.size()];
		Iterator<VisualObject> it = vObjects.iterator();
		int cnt = 0;
		
		while(it.hasNext())
		{
			VisualObject vobject = it.next();
			shapes[cnt++] = CoercionHelper.toShape(vobject);
		}
		return shapes;
	}

	public static Collection<VisualObject> toLocalVisualObjects(com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape[] shapes) 
	{
		if(shapes == null)
			return null;
		
		List<VisualObject> vobjects = new ArrayList<VisualObject>();
		for(int i=0;i<shapes.length;i++)
		{
			vobjects.add(toLocalVisualObejct(shapes[i]));
		}
		return vobjects;
	}

	public static VisualObject toLocalVisualObejct(com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape shape) 
	{
		if(shape == null)
			return null;
		
		if(shape instanceof com.strandgenomics.imaging.iclient.impl.ws.ispace.TextArea)
			return toLocalTextArea((com.strandgenomics.imaging.iclient.impl.ws.ispace.TextArea)shape);
		else if(shape instanceof com.strandgenomics.imaging.iclient.impl.ws.ispace.RectangularShape)
			return toLocalRectangleShape((com.strandgenomics.imaging.iclient.impl.ws.ispace.RectangularShape)shape);
		else if(shape instanceof com.strandgenomics.imaging.iclient.impl.ws.ispace.EllipticalShape)
			return toLocalEllipseShape((com.strandgenomics.imaging.iclient.impl.ws.ispace.EllipticalShape)shape);
		else if(shape instanceof com.strandgenomics.imaging.iclient.impl.ws.ispace.StraightLine)
			return toLocalLineSegment((com.strandgenomics.imaging.iclient.impl.ws.ispace.StraightLine)shape);
		else if(shape instanceof com.strandgenomics.imaging.iclient.impl.ws.ispace. FreehandShape)
			return toLocalFreeHandShapes((com.strandgenomics.imaging.iclient.impl.ws.ispace.FreehandShape)shape);
		
		return null;
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.Property[] toPropertyList(Map<String, Object> annotations) 
	{
		if(annotations == null || annotations.isEmpty())
			return null;
		
		List<com.strandgenomics.imaging.iclient.impl.ws.ispace.Property> pList = new ArrayList<com.strandgenomics.imaging.iclient.impl.ws.ispace.Property>();
		for(Map.Entry<String, Object> entry : annotations.entrySet())
		{
			pList.add( new com.strandgenomics.imaging.iclient.impl.ws.ispace.Property(entry.getKey(), entry.getValue()) );
		}
		
		return pList.toArray(new com.strandgenomics.imaging.iclient.impl.ws.ispace.Property[0]);
	}

	public static Dimension toDimension(com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex c)
	{
		if(c == null) return null;
		return new Dimension(c.getFrame(), c.getSlice(), c.getChannel(), c.getSite());
	}
	
	public static VODimension toVODimension(com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex c)
	{
		if(c == null) return null;
		return new VODimension(c.getFrame(), c.getSlice(), c.getSite());
	}
	
	public static VODimension[] toVODimension(com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex[] c)
	{
		if(c == null) return null;
		VODimension[] coList = new VODimension[c.length];
		for(int i = 0;i < c.length; i++)
			coList[i] = toVODimension(c[i]);
		return coList;
	}
	
	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex[] toVOCoordinate(VODimension[] d)
	{
		if(d == null) return null;
		com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex[] coList = new com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex[d.length];
		for(int i = 0;i < d.length; i++)
			coList[i] = toVOCoordinate(d[i]);
		return coList;
	}
	
	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex toVOCoordinate(VODimension d)
	{
		if(d == null) return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex c = new com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex();
		c.setFrame(d.frameNo);
		c.setSite(d.siteNo);
		c.setSlice(d.sliceNo);
		return c;
	}
	
	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex toImageCoordinate(Dimension d)
	{
		if(d == null) return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex c = new com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex();
		c.setChannel(d.channelNo);
		c.setFrame(d.frameNo);
		c.setSite(d.siteNo);
		c.setSlice(d.sliceNo);
		return c;
	}

	public static IPixelData toPixelData(Record parent, com.strandgenomics.imaging.iclient.impl.ws.ispace.Image pixelData) 
	{
		com.strandgenomics.imaging.iclient.impl.ws.ispace.ImageIndex index = pixelData.getIndex();
		Dimension d = new Dimension(index.getFrame(), index.getSlice(), index.getChannel(), index.getSite());
		return new PixelData(parent, d, pixelData.getX(), pixelData.getY(), pixelData.getZ(), pixelData.getElapsedTime(), pixelData.getExposureTime(), new Date(pixelData.getTimeStamp()));
	}

	public static List<UserComment> toUserComment(com.strandgenomics.imaging.iclient.impl.ws.ispace.Comments[] comments)
	{
		if(comments == null || comments.length == 0)
			return null;
		
		List<UserComment> ucList = new ArrayList<UserComment>();
		for(com.strandgenomics.imaging.iclient.impl.ws.ispace.Comments c : comments)
		{
			UserComment uc = toUserComment(c);
			if(uc != null) ucList.add(uc);
		}
		
		return ucList;
	}

	private static UserComment toUserComment(com.strandgenomics.imaging.iclient.impl.ws.ispace.Comments c)
	{
		if(c == null)  return null;
		return new UserComment(c.getUserLogin(), c.getNotes(), new Timestamp(c.getCreationDate()));
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[] toRemoteSearchConditions(Collection<SearchCondition> searchConditions) 
	{
		if(searchConditions == null || searchConditions.size() == 0) return null;
		
		List<com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition> remoteConditions = new ArrayList<com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition>();
		for(SearchCondition searchCondition : searchConditions)
		{
			com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition s = toRemoteSearchCondition(searchCondition);
			if(s!=null)
				remoteConditions.add(s);
		}
		
		return remoteConditions.isEmpty() ? null : remoteConditions.toArray(new com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[0]);
	}

	public static final com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition toRemoteSearchCondition(SearchCondition f) 
	{
		if(f == null) return null;

		com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition remoteSC = new com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition();
		remoteSC.setLowerLimit( f.getLowerLimit() );
		remoteSC.setUpperLimit( f.getUpperLimit() );
		remoteSC.setName( f.fieldName );
		remoteSC.setType( f.fieldType.ordinal() );

		return remoteSC;
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.search.SearchField toRemoteSearchField(SearchField f) 
	{
		if(f == null) return null;
		com.strandgenomics.imaging.iclient.impl.ws.search.SearchField sf = new com.strandgenomics.imaging.iclient.impl.ws.search.SearchField();
		sf.setName(f.fieldName);
		sf.setType(f.fieldType.ordinal());
		return sf;
	}

	public static List<NavigationBin> toLocalNavigationNodes(com.strandgenomics.imaging.iclient.impl.ws.search.SearchNode[] remoteNodes, SearchField current) 
	{
		if(remoteNodes == null || remoteNodes.length == 0) return null;
		
		List<NavigationBin> navigationNodes = new ArrayList<NavigationBin>();
		for(com.strandgenomics.imaging.iclient.impl.ws.search.SearchNode remoteNode : remoteNodes)
		{
			NavigationBin navigationNode = toLocalNavigationNode(remoteNode, current);
			if(navigationNode != null)
				navigationNodes.add(navigationNode);
		}
		
		return navigationNodes;
	}

	private static NavigationBin toLocalNavigationNode(
			com.strandgenomics.imaging.iclient.impl.ws.search.SearchNode remoteNode, SearchField current) 
	{
		if(remoteNode == null) return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[] remoteFilters = remoteNode.getFilters();
		List<SearchCondition> searchConditions = toLocalSearchConditions(remoteFilters);
		
		if(searchConditions == null)
			return new NavigationBin(null, current);
		
		Set<SearchCondition> filters = new HashSet<SearchCondition>(searchConditions);
		return new NavigationBin(filters, current);
	}
	
	public static List<SearchCondition> toLocalSearchConditions(com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition[] remoteConditions)
	{
		if(remoteConditions == null || remoteConditions.length == 0) return null;
		
		List<SearchCondition> conditions = new ArrayList<SearchCondition>();
		for(com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition remoteCondition:remoteConditions)
		{
			SearchCondition localCondition = toLocalSearchCondition(remoteCondition);
			if(localCondition != null)
				conditions.add(localCondition);
		}
		return conditions;
	}

	private static SearchCondition toLocalSearchCondition(com.strandgenomics.imaging.iclient.impl.ws.search.SearchCondition remoteCondition)
	{
		if(remoteCondition == null)	return null;
		
		String fieldName = remoteCondition.getName();
		Object lowerLimit = remoteCondition.getLowerLimit();
		Object upperLimit = remoteCondition.getUpperLimit();
		
		if(lowerLimit instanceof Long)
			return new SearchCondition(fieldName, (Long)lowerLimit, (Long)upperLimit);
		if(lowerLimit instanceof Double)
			return new SearchCondition(fieldName, (Double)lowerLimit, (Double)upperLimit);
		if(lowerLimit instanceof String)
			return new SearchCondition(fieldName, (String)lowerLimit, (String)upperLimit);
		if(lowerLimit instanceof Timestamp)
			return new SearchCondition(fieldName, (Timestamp)lowerLimit, (Timestamp)upperLimit);
		
		return null;
	}

	public static Map<String, Object> toLocalPropertyMap(com.strandgenomics.imaging.iclient.impl.ws.ispace.Property[] dynamicMetaData) 
	{
		if(dynamicMetaData == null || dynamicMetaData.length == 0) return null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		for(com.strandgenomics.imaging.iclient.impl.ws.ispace.Property metaData : dynamicMetaData)
		{
			if(metaData == null)
				continue;
			
			String key = metaData.getName();
			Object value = metaData.getValue();
			map.put(key, value);
		}
		return map;
	}

	public static Histogram toHistogram(com.strandgenomics.imaging.iclient.impl.ws.ispace.Statistics s) 
	{
		if(s == null) return null;
		return new Histogram(PixelDepth.toPixelDepth(s.getPixelDepth()), s.getIntensities(), s.getFrequencies(), s.getMinValue(), s.getMaxValue(), s.getMaxFreq());
	}

	public static NVPair[] toRemoteComputeNVPairs(Map<String, Object> parameters)
	{
		if(parameters == null)	return null;
		
		List<NVPair> nvParams = new ArrayList<NVPair>();
		for(Entry<String, Object>entry:parameters.entrySet())
		{
			String name = entry.getKey();
			Object value = entry.getValue();
		
			nvParams.add(new NVPair(name, value));
		}
		
		return nvParams.toArray(new NVPair[0]);
	}

	public static List<Application> toLocalApplications(com.strandgenomics.imaging.iclient.impl.ws.compute.Application[] applications)
	{
		if(applications == null) return null;
		
		List<Application> localApplications = new ArrayList<Application>();
		for(int i=0;i<applications.length;i++)
		{
			localApplications.add(toLocalApplication(applications[i]));
		}
		
		return localApplications;
	}

	private static Application toLocalApplication(com.strandgenomics.imaging.iclient.impl.ws.compute.Application application)
	{
		if(application == null)	return null;
		
		Application app = new Application(application.getName(), application.getVersion(), application.getCategoryName(), application.getNotes());
		return app;
	}

	public static Set<Parameter> toLocalParameters(com.strandgenomics.imaging.iclient.impl.ws.compute.Parameter[] parameters)
	{
		if(parameters == null) return null;
		
		Set<Parameter> localParameters = new HashSet<Parameter>();
		for(int i=0;i<parameters.length;i++)
		{
			Parameter localParameter = toLocalParameter(parameters[i]);
			localParameters.add(localParameter);
		}
		
		return localParameters;
	}

	private static Parameter toLocalParameter(com.strandgenomics.imaging.iclient.impl.ws.compute.Parameter parameter)
	{
		ParameterConstraints paramConstraints = toLocalParameterConstraints(parameter.getConstraints());
		
		ParameterType type = ParameterType.valueOf(parameter.getType());
		Object deserializedDefaultValue = convertDefaultValue(parameter.getDefaultValue(), type);
		
		Parameter localParameter = new Parameter(parameter.getName(), type, paramConstraints, deserializedDefaultValue, parameter.getDescription());
		
		return localParameter;
	}
	
	private static Object convertDefaultValue(Object value, ParameterType type)
	{
		Object convertedValue = null;
		switch (type)
		{
		case BOOLEAN:
			convertedValue = Util.getBoolean(value);
			break;
		case INTEGER:
			convertedValue = Util.getInteger(value);
			break;
		case DECIMAL:
			convertedValue = Util.getDouble(value);
			break;
		case STRING:
			convertedValue = Util.getString(value);
			break;
		}
		
		return convertedValue;
	}
	
	public static ParameterConstraints toLocalParameterConstraints(Constraints con)
	{
		if(con == null) return null;
		
		ParameterConstraints paramConstraints = null;
		if(con instanceof DoubleListConstraints)
		{
			paramConstraints = new ListConstraints(((DoubleListConstraints) con).getValidValues());
		}
		
		if(con instanceof LongListConstraints)
		{
			paramConstraints = new ListConstraints(((LongListConstraints) con).getValidValues());
		}
		
		if(con instanceof DoubleRangeConstraints)
		{
			paramConstraints = new RangeConstraints(((DoubleRangeConstraints) con).getLowerLimit(), ((DoubleRangeConstraints) con).getUpperLimit());
		}
		
		if(con instanceof LongRangeConstraints)
		{
			paramConstraints = new RangeConstraints(((LongRangeConstraints) con).getLowerLimit(), ((LongRangeConstraints) con).getUpperLimit());
		}
		
		return paramConstraints;
	}

	public static Job toLocalJob(long jobId)
	{
		return new Job(jobId);
	}
	
	public static Map<String, Object> toParametersMap(NVPair[] parameters)
	{
		if(parameters == null) return null;
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		for(int i=0;i<parameters.length;i++)
		{
			String name = parameters[i].getName();
			Object value = parameters[i].getValue();
			
			parameterMap.put(name, value);
		}
		
		return parameterMap;
	}

	public static List<com.strandgenomics.imaging.iclient.HistoryItem> toLocalHistory(HistoryItem[] history)
	{
		if(history == null) return null;
		
		List<com.strandgenomics.imaging.iclient.HistoryItem> localHistory = new ArrayList<com.strandgenomics.imaging.iclient.HistoryItem>();
		
		for(HistoryItem item:history)
		{
			com.strandgenomics.imaging.iclient.HistoryItem localItem = toLocalHistoryItem(item);
			if(localItem!=null)
				localHistory.add(localItem);
		}
		
		return localHistory;
	}

	private static com.strandgenomics.imaging.iclient.HistoryItem toLocalHistoryItem(HistoryItem item)
	{
		if(item == null) return null;
		
		com.strandgenomics.imaging.iclient.HistoryItem localItem = new com.strandgenomics.imaging.iclient.HistoryItem(item.getGuid(), item.getAppName(),item.getAppVersion(),item.getModifiedBy(),item.getModificationTime(),item.getType(),item.getDescription());
		return localItem;
	}
	
	public static com.strandgenomics.imaging.iclient.impl.ws.loader.ImageIndex toImageIndex(Dimension d)
	{
		if(d == null) return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.loader.ImageIndex c = new com.strandgenomics.imaging.iclient.impl.ws.loader.ImageIndex();
		c.setChannel(d.channelNo);
		c.setFrame(d.frameNo);
		c.setSite(d.siteNo);
		c.setSlice(d.sliceNo);
		return c;
	}

	public static Image toRemoteImageData(IPixelData pixelData)
	{
		if(pixelData == null) return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.loader.ImageIndex imageIndex = toImageIndex(pixelData.getDimension());
		Image imageData = new Image(pixelData.getElapsedTime(), pixelData.getExposureTime(), imageIndex, pixelData.getTimeStamp().getTime(), pixelData.getX(), pixelData.getY(), pixelData.getZ());
		
		return imageData;
	}
	
	public static Image toRemoteImageData(PixelMetaData pixelData)
	{
		if(pixelData == null) return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.loader.ImageIndex imageIndex = toImageIndex(pixelData.getDimension());
		Image imageData = new Image(pixelData.getElapsedTime(), pixelData.getExposureTime(), imageIndex, pixelData.getTimeStamp().getTime(), pixelData.getX(), pixelData.getY(), pixelData.getZ());
		
		return imageData;
	}

	public static BookmarkFolder[] toClientBookmarkFolders(String projectName, String[] subfolders)
	{
		if(projectName == null || projectName.isEmpty() || subfolders == null) return null;
		
		List<BookmarkFolder> folderList = new ArrayList<BookmarkFolder>();
		for(String folder:subfolders)
		{
			folderList.add(toClientBookmarkFolder(projectName, folder));
		}
		
		return folderList.toArray(new BookmarkFolder[0]);
	}
	
	public static BookmarkFolder toClientBookmarkFolder(String projectName, String subfolderPath)
	{
		return new BookmarkFolder(projectName, subfolderPath);
	}

	public static com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile toServerProfile(AcquisitionProfile profile)
	{
		if(profile == null) return null;
		
		com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile serverProfile = new com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile();

		serverProfile.setProfileName(profile.getProfileName());
		serverProfile.setName(profile.getName());
		
		serverProfile.setLengthUnit(profile.getLengthUnit());
		serverProfile.setTimeUnit(profile.getTimeUnit());
		
		serverProfile.setSourceFormat(profile.getSourceFormat());
		
		serverProfile.setXPixelSize(profile.getxPixelSize());
		serverProfile.setXType(profile.xType);
		
		serverProfile.setYPixelSize(profile.getyPixelSize());
		serverProfile.setYType(profile.yType);
		
		serverProfile.setZPixelSize(profile.getzPixelSize());
		serverProfile.setZType(profile.zType);
		
		return serverProfile;
	}

	public static List<AcquisitionProfile> toLocalAcqProfiles(com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile[] serverProfiles)
	{
		if(serverProfiles == null) return null;
		
		List<AcquisitionProfile> localProfiles = new ArrayList<AcquisitionProfile>();
		
		for(com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile serverProfile:serverProfiles)
		{
			AcquisitionProfile localProfile = toLocalAcqProfile(serverProfile);
			if(localProfile !=null)
				localProfiles.add(localProfile);
		}
		
		return localProfiles;
	}
	
	private static AcquisitionProfile toLocalAcqProfile(com.strandgenomics.imaging.iclient.impl.ws.ispace.AcquisitionProfile serverProfile)
	{
		if(serverProfile == null) return null;
		
		AcquisitionProfile localProfile = new AcquisitionProfile(serverProfile.getProfileName(), serverProfile.getName(), serverProfile.getXPixelSize(), serverProfile.getXType(), serverProfile.getYPixelSize(), serverProfile.getYType(), serverProfile.getZPixelSize(), serverProfile.getZType(), serverProfile.getSourceFormat(), serverProfile.getTimeUnit(), serverProfile.getLengthUnit());
		return localProfile;
	}

	public static Contrast toRemoteISpaceContrast(VisualContrast contrast)
	{
		if(contrast == null) return null;
		
		Contrast c = new Contrast(contrast.getGamma(), contrast.getMaxIntensity(), contrast.getMinIntensity());
		return c;
	}
}
