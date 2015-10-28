package com.strandgenomics.imaging.iengine.models;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.vo.Arrow;
import com.strandgenomics.imaging.icore.vo.Circle;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Polygon;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * utility class used for converting shapes to Well-Known-Text format and vice
 * versa
 * 
 * @author Anup Kulkarni
 */
public class WKTUtil {
	/**
	 * mainly used to disambiguate (Ellipse, Rectangle, TextBox) and (Line and
	 * Freehand) this type is stored in DB along with visual object, hence is
	 * known during conversion
	 * 
	 * @author Anup Kulkarni
	 */
	public enum WKTTYPE 
	{
		FreeHand, Line, Text, Ellipse, Rectangle, Circle, Polygon, Arrow
	}

	/**
	 * converts input shape object to WKT format
	 * 
	 * @param shape
	 * @return WKT representation of input shape
	 * @throws IllegalArgumentException
	 */
	public static String toWKTFromat(VisualObject shape) throws IllegalArgumentException 
	{
		if (shape == null)
			return null;

		if (shape instanceof TextBox) 
		{
			TextBox r = (TextBox) shape;
			Rectangle2D.Double bounds = r.getBounds();
			double x = bounds.getX();
			double y = bounds.getY();
			double height = bounds.getHeight();
			double width = bounds.getWidth();

			StringBuffer wktString = new StringBuffer("POLYGON( ( ");
			wktString.append(x + " " + y + " , ");
			wktString.append((x + width) + " " + y + " , ");
			wktString.append((x + width) + " " + (y + height) + " , ");
			wktString.append(x + " " + (y + height) + " , ");
			wktString.append(x + " " + y);
			wktString.append(" ) )");

			return wktString.toString();
		}

		else if (shape instanceof Rectangle) 
		{
			Rectangle r = (Rectangle) shape;
			Rectangle2D.Double bounds = r.getBounds();
			double x = bounds.getX();
			double y = bounds.getY();
			double height = bounds.getHeight();
			double width = bounds.getWidth();
			double theta= shape.getRotation();
			
			double center_x=x+width/2;
			double center_y=y+height/2;
			
			double radius= Math.sqrt(Math.pow(height/2,2)+Math.pow(width/2,2));
			double alpha=Math.atan2(height/2, width/2);

			double x1=center_x+radius*Math.cos(Math.PI-alpha+theta);
			double y1=center_y+radius*Math.sin(Math.PI-alpha+theta);
			
			double x2=center_x+radius*Math.cos(alpha+theta);
			double y2=center_y+radius*Math.sin(alpha+theta);
			
			double x3=center_x+radius*Math.cos(2*Math.PI-alpha+theta);
			double y3=center_y+radius*Math.sin(2*Math.PI-alpha+theta);
			
			double x4=center_x+radius*Math.cos(Math.PI+alpha+theta);
			double y4=center_y+radius*Math.sin(Math.PI+alpha+theta);
			
			StringBuffer wktString = new StringBuffer("POLYGON( ( ");
			wktString.append(x1 + " " + y1 + " , ");
			wktString.append(x2 + " " + y2 + " , ");
			wktString.append(x3 + " " + y3 + " , ");
			wktString.append(x4 + " " + y4 + " , ");
			wktString.append(x1 + " " + y1);
			wktString.append(" ) )");
			
			/*StringBuffer wktString = new StringBuffer("POLYGON( ( ");
			wktString.append(x + " " + y + " , ");
			wktString.append((x + width) + " " + y + " , ");
			wktString.append((x + width) + " " + (y + height) + " , ");
			wktString.append(x + " " + (y + height) + " , ");
			wktString.append(x + " " + y);
			wktString.append(" ) )");*/		

			return wktString.toString();
		}

		else if (shape instanceof Ellipse) 
		{
			Ellipse r = (Ellipse) shape;
			Rectangle2D.Double bounds = r.getBounds();
			double x = bounds.getX();
			double y = bounds.getY();
			double height = bounds.getHeight();
			double width = bounds.getWidth();
			double theta= shape.getRotation();
			
			double center_x=x+width/2;
			double center_y=y+height/2;
			
			double radius= Math.sqrt(Math.pow(height/2,2)+Math.pow(width/2,2));
			double alpha=Math.atan2(height/2, width/2);

			double x1=center_x+radius*Math.cos(Math.PI-alpha+theta);
			double y1=center_y+radius*Math.sin(Math.PI-alpha+theta);
			
			double x2=center_x+radius*Math.cos(alpha+theta);
			double y2=center_y+radius*Math.sin(alpha+theta);
			
			double x3=center_x+radius*Math.cos(2*Math.PI-alpha+theta);
			double y3=center_y+radius*Math.sin(2*Math.PI-alpha+theta);
			
			double x4=center_x+radius*Math.cos(Math.PI+alpha+theta);
			double y4=center_y+radius*Math.sin(Math.PI+alpha+theta);
			
			StringBuffer wktString = new StringBuffer("POLYGON( ( ");
			wktString.append(x1 + " " + y1 + " , ");
			wktString.append(x2 + " " + y2 + " , ");
			wktString.append(x3 + " " + y3 + " , ");
			wktString.append(x4 + " " + y4 + " , ");
			wktString.append(x1 + " " + y1);
			wktString.append(" ) )");

			return wktString.toString();
		}
		
		else if (shape instanceof Circle) 
		{
			Circle r = (Circle) shape;
			Rectangle2D.Double bounds = r.getBounds();
			double x = bounds.getX();
			double y = bounds.getY();
			double height = bounds.getHeight();
			double width = bounds.getWidth();

			StringBuffer wktString = new StringBuffer("POLYGON( ( ");
			wktString.append(x + " " + y + " , ");
			wktString.append((x + width) + " " + y + " , ");
			wktString.append((x + width) + " " + (y + height) + " , ");
			wktString.append(x + " " + (y + height) + " , ");
			wktString.append(x + " " + y);
			wktString.append(" ) )");

			return wktString.toString();
		}
		else if (shape instanceof Polygon) 
		{
			Polygon r = (Polygon) shape;

			StringBuffer wktString = new StringBuffer("LINESTRING( ");
			float[] coordinates = r.getCoordinates();
			for (int i = 0; i < coordinates.length; i += 2) 
			{
				wktString.append(coordinates[i] + " " + coordinates[i + 1]
						+ " ,");
			}
			wktString.deleteCharAt(wktString.length() - 1);// remove last ','
			wktString.append(")");

			return wktString.toString();
		}		

		else if (shape instanceof Arrow) 
		{
			Arrow r = (Arrow) shape;

			StringBuffer wktString = new StringBuffer("LINESTRING( ");
			float[] coordinates = r.getCoordinates();
			for (int i = 0; i < coordinates.length; i += 2) 
			{
				wktString.append(coordinates[i] + " " + coordinates[i + 1]
						+ " ,");
			}
			wktString.deleteCharAt(wktString.length() - 1);// remove last ','
			wktString.append(")");

			return wktString.toString();
		}	
		
		else if (shape instanceof GeometricPath) 
		{
			GeometricPath r = (GeometricPath) shape;

			StringBuffer wktString = new StringBuffer("LINESTRING( ");
			float[] coordinates = r.getCoordinates();
			for (int i = 0; i < coordinates.length; i += 2) 
			{
				wktString.append(coordinates[i] + " " + coordinates[i + 1]
						+ " ,");
			}
			wktString.deleteCharAt(wktString.length() - 1);// remove last ','
			wktString.append(")");

			return wktString.toString();
		}

		else if (shape instanceof LineSegment) 
		{
			LineSegment r = (LineSegment) shape;

			StringBuffer wktString = new StringBuffer("LINESTRING( ");
			wktString.append(r.getStartX() + " " + r.getStartY() + " , ");
			wktString.append(r.getEndX() + " " + r.getEndY());
			wktString.append(")");

			return wktString.toString();
		}

		throw new IllegalArgumentException("Unknown Shape");
	}

	/**
	 * parses WKT string to extract points
	 * 
	 * @param wktFormat
	 *            representation of shape
	 * @return Double array containing a series of x followed by y - coordinates
	 *         of points capturing the shape
	 */
	private static Float[] parseWTKFormat(String wktFormat) 
	{
		List<Float> points = new ArrayList<Float>();

		// tokenizing on ',' '<space>' and '(' ')'
		String[] result = wktFormat.split("\\(|,|\\)| ");
		
		// result[0] will be type of the shape: POLYGON, LINESTRING etc
		for (int i = 1; i < result.length; i++) 
		{

			if (!result[i].isEmpty() && result[i].length() > 0) 
			{
				float value = Float.parseFloat(result[i]);
				points.add(value);
			}
		}

		return points.toArray(new Float[0]);
	}

	/**
	 * returns bounding rectangle for Ellipse, Rectangle and TextBox object
	 * 
	 * @param points
	 *            points representing Ellipse, Rectangle and TextBox object
	 * @return bounding rectangle for (x,y,width, height) format
	 */
	private static Rectangle2D.Float getBoundingRectangle(Float[] points) 
	{
		if (points.length != 10)
			throw new IllegalArgumentException("improper arguments for creating bounding rectangle");

		// finding top, left, height and width
		float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
		
		for (int i = 0; i < points.length; i += 2) 
		{
			float x = points[i];
			float y = points[i + 1];

			if (minX > x)
				minX = x;
			if (maxX < x)
				maxX = x;

			if (minY > y)
				minY = y;
			if (maxY < y)
				maxY = y;
		}

		return new Rectangle2D.Float(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * converts given description of shape into ShapeObject
	 * 
	 * @param wktFormat
	 *            shape description in WKT format
	 * @param ID
	 * @param penColor
	 * @param penWidth
	 * @param message
	 *            contains message if shape type is TextBox otherwise null
	 * @param type
	 *            type of target VisualObject
	 * @return VisualObject from input description
	 * @throws IllegalArgumentException
	 */
	public static VisualObject toShape(String wktFormat, int ID, String message, WKTTYPE type)
			throws IllegalArgumentException 
	{
		if (wktFormat == null)
			return null;

		int endIndex = wktFormat.indexOf("(");
		if (endIndex < 0)
			throw new IllegalArgumentException("problem reading wkt format "+ wktFormat);
		// used for cross validation of type parameter
		String objectType = wktFormat.substring(0, endIndex).trim();

		Float points[] = parseWTKFormat(wktFormat);

		if (points.length % 2 != 0)
			throw new IllegalArgumentException("problem parsing wkt string: count of coordinates is not even");

		if (type == WKTTYPE.Text) 
		{
			// TextBox
			if (!"POLYGON".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found "	+ objectType + " specified " + type);

			Rectangle2D.Float boundingRect = getBoundingRectangle(points);
			return new TextBox(ID, boundingRect.getX(), boundingRect.getY(), boundingRect.getWidth(), boundingRect.getHeight(), message);
		}
		
		else if (type == WKTTYPE.Rectangle) 
		{
			// Rectangle
			if (!"POLYGON".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found " + objectType + " specified " + type);

			//Rectangle2D.Float boundingRect = getBoundingRectangle(points);
			//return new Rectangle(boundingRect.getX(), boundingRect.getY(), boundingRect.getWidth(), boundingRect.getHeight());
			
			double x1=points[0];
			double y1=points[1];
			
			double x2=points[2];
			double y2=points[3];
			
			double x3=points[4];
			double y3=points[5];
			
			double center_x=(x1+x3)/2;
			double center_y=(y1+y3)/2;
			
			double theta=Math.atan2((y2-y1),(x2-x1));
			
			double width=Math.sqrt((Math.pow((x1-x2), 2))+(Math.pow((y1-y2), 2)));
			double height=Math.sqrt((Math.pow((x2-x3), 2))+(Math.pow((y2-y3), 2)));
			
			double alpha=Math.atan2( height/2, width/2);
			
			double radius= Math.sqrt(Math.pow(height/2,2)+Math.pow(width/2,2));
			double x= center_x-radius*Math.cos(alpha);
			double y= center_y-radius*Math.sin(alpha);
			
			Rectangle r = new Rectangle(ID, x, y,width, height);
			r.setRotation(theta);
			return r;
		}

		else if (type == WKTTYPE.Ellipse) 
		{
			// Ellipse
			if (!"POLYGON".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found "
						+ objectType + " specified " + type);

			//Rectangle2D.Float boundingRect = getBoundingRectangle(points);
			//return new Ellipse(boundingRect.getX(), boundingRect.getY(), boundingRect.getWidth(), boundingRect.getHeight());
			double x1=points[0];
			double y1=points[1];
			
			double x2=points[2];
			double y2=points[3];
			
			double x3=points[4];
			double y3=points[5];
			
			double center_x=(x1+x3)/2;
			double center_y=(y1+y3)/2;
			
			double theta=Math.atan2((y2-y1),(x2-x1));
			
			double width=Math.sqrt((Math.pow((x1-x2), 2))+(Math.pow((y1-y2), 2)));
			double height=Math.sqrt((Math.pow((x2-x3), 2))+(Math.pow((y2-y3), 2)));
			
			double alpha=Math.atan2( height/2, width/2);
			
			double radius= Math.sqrt(Math.pow(height/2,2)+Math.pow(width/2,2));
			double x= center_x-radius*Math.cos(alpha);
			double y= center_y-radius*Math.sin(alpha);
			
			Ellipse e = new Ellipse(ID, x, y,width, height);
			e.setRotation(theta);
			return e;
		}
		
		else if (type == WKTTYPE.Circle) 
		{
			// Circle
			if (!"POLYGON".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found "
						+ objectType + " specified " + type);

			Rectangle2D.Float boundingRect = getBoundingRectangle(points);
			return new Circle(ID, boundingRect.getX(), boundingRect.getY(), boundingRect.getWidth(), boundingRect.getHeight());
		}

		else if (type == WKTTYPE.Line) 
		{
			// Line Segment
			if (!"LINESTRING".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found "
						+ objectType + " specified " + type);

			if (points.length != 4)
				throw new IllegalArgumentException(
						"improper arguments for object type line. Found coordinates "
								+ points.length);

			return new LineSegment(ID, points[0], points[1], points[2], points[3]);
		}

		else if (type == WKTTYPE.FreeHand) 
		{
			// Freehand Drawing
			if (!"LINESTRING".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found "
						+ objectType + " specified " + type);

			float[] coordinates = new float[points.length];
			GeometricPath path = new GeometricPath(ID,20);
			
			for (int i = 0; i < points.length; i += 2) 
			{
				coordinates[i] = points[i];
				path.lineTo(points[i], points[i + 1]);
			}

			return path;
		}
		
		else if (type == WKTTYPE.Arrow) 
		{
			// Arrow
			if (!"LINESTRING".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found "
						+ objectType + " specified " + type);

			float[] coordinates = new float[points.length];
			Arrow arrow = new Arrow(ID,20);
			
			for (int i = 0; i < points.length; i += 2) 
			{
				coordinates[i] = points[i];
				arrow.lineTo(points[i], points[i + 1]);
			}

			return arrow;
		}
		
		else if (type == WKTTYPE.Polygon) 
		{
			// Polygon
			if (!"LINESTRING".equals(objectType))
				throw new IllegalArgumentException("inconsistent types: found "
						+ objectType + " specified " + type);

			float[] coordinates = new float[points.length];
			Polygon polygon = new Polygon(ID,20);
			
			for (int i = 0; i < points.length; i += 2) 
			{
				coordinates[i] = points[i];
				polygon.lineTo(points[i], points[i + 1]);
			}

			return polygon;
		}
		throw new IllegalArgumentException("unknown shape");
	}

	public static WKTTYPE getType(VisualObject shape) 
	{
		if (shape == null)
			return null;

		if (shape instanceof TextBox) 
		{
			return WKTTYPE.Text;
		}

		else if (shape instanceof Rectangle) 
		{
			return WKTTYPE.Rectangle;
		}

		else if (shape instanceof Ellipse) 
		{
			return WKTTYPE.Ellipse;
		}
		
		else if (shape instanceof Circle) 
		{
			return WKTTYPE.Circle;
		}
		
		else if (shape instanceof Polygon) 
		{
			return WKTTYPE.Polygon;
		}
		
		else if (shape instanceof Arrow) 
		{
			return WKTTYPE.Arrow;
		}
		
		else if (shape instanceof GeometricPath) 
		{
			return WKTTYPE.FreeHand;
		}

		else if (shape instanceof LineSegment) 
		{
			return WKTTYPE.Line;
		}

		throw new IllegalArgumentException("Unknown Shape");
	}
}
