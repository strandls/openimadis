package com.strandgenomics.imaging.iengine.migration;

import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.icore.vo.VisualObjectType;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

public class VisualObjectsMigration {
	
	private int MAX_GUID = 1026;
	
	public VisualObjectsMigration()
	{}
	
	private void migrateVisualObjects()
	{
		for(long i=5;i<MAX_GUID;i++)
		{
			try
			{
				System.out.println(i);
				Record record = SysManagerFactory.getRecordManager().findRecord("administrator", i);
				for(int site=0;site<record.numberOfSites;site++)
				{
					for(int frame = 0;frame<record.numberOfFrames;frame++)
					{
						for(int slice = 0;slice<record.numberOfSlices;slice++)
						{
							VODimension d = new VODimension(frame, slice, site);
							List<VisualOverlay> voNames = SysManagerFactory.getRecordManager().getVisualOverlays("administrator", i, d);
							if(voNames!=null && voNames.size()>0)
							{
								for(int vos=0;vos<voNames.size();vos++)
								{
									List<VisualObject> objects = SysManagerFactory.getRecordManager().getVisualObjects("administrator", i, d, voNames.get(vos).getName());
									if(objects!=null && objects.size()>0)
									{
										List<VisualObject> newObjects = transformObjects("administrator", i, objects);
//										SysManagerFactory.getRecordManager().setVisualObjects("administrator", i, newObjects, voNames.get(vos).getName(), d);
									}
								}
							}
						}
					}
				}
				
			}
			catch (DataAccessException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private List<VisualObject> transformObjects(String actor, long guid, List<VisualObject> objects) throws DataAccessException
	{
		List<VisualObject> newObjects = new ArrayList<VisualObject>();
		for(VisualObject obj:objects)
		{
			if(obj.getType() == VisualObjectType.ELLIPSE)
			{
				Point2D origin = getTranslatedCoordinates(actor, guid, new Point2D.Double(obj.getBounds().x, obj.getBounds().y));
				Point2D bounds = getTranslatedCoordinates(actor, guid, new Point2D.Double(obj.getBounds().width, obj.getBounds().height));
				
				Ellipse e = new Ellipse(obj.ID, origin.getX(), origin.getY(), bounds.getX(), bounds.getY());
				e.setPenColor(obj.getPenColor());
				e.setPenWidth(obj.getPenWidth());
				newObjects.add(e);
			}
			else if(obj.getType() == VisualObjectType.LINE)
			{
				int x1 = (int) ((LineSegment)obj).startX;
				int y1 = (int) ((LineSegment)obj).startY;

				int x2 = (int) ((LineSegment)obj).endX;
				int y2 = (int) ((LineSegment)obj).endY;
				
				Point2D start = getTranslatedCoordinates(actor, guid, new Point2D.Double(x1, y1));
				Point2D end = getTranslatedCoordinates(actor, guid, new Point2D.Double(x2, y2));
				
				LineSegment e = new LineSegment(obj.ID, start.getX(), start.getY(), end.getX(), end.getY());
				e.setPenColor(obj.getPenColor());
				e.setPenWidth(obj.getPenWidth());
				newObjects.add(e);
			}
			else if(obj.getType() == VisualObjectType.TEXT)
			{
				String text = ((TextBox)obj).getText();
				Font f = ((TextBox)obj).getFont();
				
				Point2D origin = getTranslatedCoordinates(actor, guid, new Point2D.Double(obj.getBounds().x, obj.getBounds().y));
				Point2D bounds = getTranslatedCoordinates(actor, guid, new Point2D.Double(obj.getBounds().width, obj.getBounds().height));
				
				TextBox e = new TextBox(obj.ID, origin.getX(), origin.getY(), bounds.getX(), bounds.getY(), text);
				e.setBkgColor(((TextBox)obj).getBkgColor());
				e.setFont(f);
				e.setPenColor(obj.getPenColor());
				e.setPenWidth(obj.getPenWidth());
				newObjects.add(e);
			}
			else if(obj.getType() == VisualObjectType.RECTANGLE)
			{
				Point2D origin = getTranslatedCoordinates(actor, guid, new Point2D.Double(obj.getBounds().x, obj.getBounds().y));
				Point2D bounds = getTranslatedCoordinates(actor, guid, new Point2D.Double(obj.getBounds().width, obj.getBounds().height));
				
				Rectangle e = new Rectangle(obj.ID, origin.getX(), origin.getY(), bounds.getX(), bounds.getY());
				e.setPenColor(obj.getPenColor());
				e.setPenWidth(obj.getPenWidth());
				newObjects.add(e);
			}
			else if(obj.getType() == VisualObjectType.PATH)
			{
				List<Point2D> points = ((GeometricPath)obj).getPathPoints();
				List<Point2D> newPoints = new ArrayList<Point2D>();
				for(Point2D point:points)
				{
					newPoints.add(getTranslatedCoordinates(actor, guid, point));
				}
				
				GeometricPath e = new GeometricPath(obj.ID, newPoints.size());
				for(Point2D point:newPoints)
				{
					e.lineTo(point.getX(), point.getY());
				}
				e.setPenColor(obj.getPenColor());
				e.setPenWidth(obj.getPenWidth());
				newObjects.add(e);
			}
		}
		
		return newObjects;
	}
	
	private Point2D getTranslatedCoordinates(String actor, long guid, Point2D point) throws DataAccessException
	{
		Record record = SysManagerFactory.getRecordManager().findRecord(actor,
				guid);
		int originalHeight = record.imageHeight;
		int originalWidth = record.imageWidth;

		int scaledHeight = 512;
		int scaledWidth = scaledHeight * originalWidth / originalHeight;

		double scaleX = originalWidth / scaledWidth;
		double scaleY = originalHeight / scaledHeight;

		return new Point2D.Double(point.getX() * scaleX, point.getY() * scaleY);
	}

	public static void main(String args[]) throws IOException, SQLException
	{
		if(args != null && args.length>0)
    	{
    		File f = new File(args[0]);//iworker.properties.
    		System.out.println(f.getName());
    		if(f.isFile())
    		{
    			System.out.println("loading system properties from "+f);
    			BufferedReader inStream = new BufferedReader(new FileReader(f));
	    		Properties props = new Properties();
	    		props.load(inStream);
	    		
	    		props.putAll(System.getProperties()); //copy existing properties, it is overwritten :-(
	    		props.list(System.out);
	    		
	    		System.setProperties(props);
	    		inStream.close();
    		}
    	}
		VisualObjectsMigration r = new VisualObjectsMigration();
		r.migrateVisualObjects();
	}
}
