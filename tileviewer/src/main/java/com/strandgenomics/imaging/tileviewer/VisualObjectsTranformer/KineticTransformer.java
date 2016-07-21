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

package com.strandgenomics.imaging.tileviewer.VisualObjectsTranformer;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.vo.Arrow;
import com.strandgenomics.imaging.icore.vo.Circle;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Polygon;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.icore.vo.VisualObjectType;

/**
 * Visual object transformer to and from KineticJS objects. Instances are
 * thread-safe.
 * 
 * @author navneet
 * 
 */
public class KineticTransformer  extends VisualObjectTransformer<Map<String, Object>>  {


    /**
     * Logger instance to use
     */
    private Logger logger;

    /**
     * Create new kinetic transformer
     */
    KineticTransformer() {
        logger = Logger.getLogger("com.strandgenomics.imaging.iserver.impl.web.io");
    }    

	@Override
	protected VisualObjectType getType(Map<String, Object> object) {
        String typeString = (String) object.get("type");
        if (typeString.equals("Rect"))
            return VisualObjectType.RECTANGLE;
        else if (typeString.equals("Ellipse"))
            return VisualObjectType.ELLIPSE;
        else if (typeString.equals("Circle"))
            return VisualObjectType.CIRCLE;
        else if (typeString.equals("Text"))
            return VisualObjectType.TEXT;
        else if (typeString.equals("Path"))
            return VisualObjectType.PATH;
        else if (typeString.equals("Line"))
            return VisualObjectType.LINE;  
        else if (typeString.equals("Polygon"))
            return VisualObjectType.POLYGON;   
        else if (typeString.equals("Arrow"))
            return VisualObjectType.ARROW;         
        return null;
	}

	/**
	 * In kineticjs x,y is the center of ellipse
	 * and radiusX and radiusY are radius in X&Y directions
	 */
	@Override
	protected Map<String, Object> encodeEllipse(Ellipse ellipse) {
		
        Double bounds = ellipse.getBounds();
        Map<String, Object> kineticObject = getCommonKineticObject(ellipse);
        kineticObject.put("type", "Ellipse");
        kineticObject.put("x", bounds.x+bounds.width/2);
        kineticObject.put("y",bounds.y+bounds.height/2);
        kineticObject.put("radiusX", bounds.width / 2);
        kineticObject.put("radiusY", bounds.height / 2);
        kineticObject.put("rotation", ellipse.getRotation());
        
        logger.logp(Level.INFO, "KineticTransformer", "encodeEllipse", "radiusX="+bounds.width/2+" radiusY="+bounds.width/2+" x="+(bounds.x+bounds.width)+" y="+(bounds.y+bounds.height));
        return kineticObject;

	}

	/**
	 * In kineticjs x,y is the center of ellipse
	 * and radiusX and radiusY are radius in X&Y directions
	 */
	@Override
	protected VisualObject decodeEllipse(Map<String, Object> ellipseData) {

        double width = getDouble(ellipseData.get("radiusX"))*2;
        double height = getDouble(ellipseData.get("radiusY"))*2;
        double x = getDouble(ellipseData.get("x"))-width/2;
        double y = getDouble(ellipseData.get("y"))-height/2;
        
		logger.logp(Level.INFO, "KineticTransformer", "decodeEllipse", "width="+width+" height="+height+" x="+x+" y="+y);
        Ellipse ellipse;
        if (ellipseData.containsKey("custom")) {
            @SuppressWarnings("unchecked")
			Map<String, Object> customData = (Map<String, Object>) ellipseData.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            ellipse = new Ellipse(ID, x, y, width, height);
        } else {
            ellipse = new Ellipse(x, y, width, height);
        }
        ellipse.setType(VisualObjectType.ELLIPSE);
        addCommonAttributes(ellipse, ellipseData);
        return ellipse;
        
	}

	@Override
	protected Map<String, Object> encodeCircle(Circle circle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VisualObject decodeCircle(Map<String, Object> serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> encodeRectangle(Rectangle rectangle) {
        Double bounds = rectangle.getBounds();
        Map<String, Object> kineticObject = getCommonKineticObject(rectangle);
        kineticObject.put("type", "Rect");
        kineticObject.put("x", bounds.x);
        kineticObject.put("y", bounds.y);
        kineticObject.put("width", bounds.width);
        kineticObject.put("height", bounds.height);
        kineticObject.put("rotation", rectangle.getRotation());
        
		return kineticObject;
	}

	@Override
	protected VisualObject decodeRectangle(Map<String, Object> rectData) {
        double width = getDouble(rectData.get("width"));
        double height = getDouble(rectData.get("height"));
        double x = getDouble(rectData.get("x"));
        double y = getDouble(rectData.get("y"));
        Rectangle rectangle;
        if (rectData.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) rectData.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            rectangle = new Rectangle(ID, x, y, width, height);
        } else {
            rectangle = new Rectangle(x, y, width, height);
        }
        rectangle.setType(VisualObjectType.RECTANGLE);
        addCommonAttributes(rectangle, rectData);
        return rectangle;
	}

	@Override
	protected Map<String, Object> encodeText(TextBox object) {
        Double bounds = object.getBounds();
        Map<String, Object> kineticObject = new HashMap<String, Object>();
        kineticObject.put("type", "Text");
        kineticObject.put("x", bounds.x);
        kineticObject.put("y", bounds.y-bounds.height);
        kineticObject.put("width", bounds.width);
        kineticObject.put("height", bounds.height);
        kineticObject.put("text", object.getText());
        kineticObject.put("fontFamily", object.getFont().getName());
        kineticObject.put("fontSize", object.getFont().getSize());
        kineticObject.put("strokeWidth", 0);
        kineticObject.put("stroke", TransformUtil.getColorString(object.getPenColor()));
        kineticObject.put("opacity", object.getPenColor().getAlpha() / 255.0);
        kineticObject.put("fill", TransformUtil.getColorString(object.getPenColor()));
        Map<String, Object> customAttributes = new HashMap<String, Object>();
        customAttributes.put("objectid", object.ID);
        System.out.println("on display object id:"+object.ID);
        kineticObject.put("custom", customAttributes);
        return kineticObject;
	}

	@Override
	protected VisualObject decodeText(Map<String, Object> serialized) {
        double x = getDouble(serialized.get("x"));
        double y = getDouble(serialized.get("y"))+getDouble(serialized.get("height"));
        double width=getDouble(serialized.get("width"));
        double height=getDouble(serialized.get("height"));
        String text = (String) serialized.get("text");
        String fontName = (String) serialized.get("fontFamily");
        int fontSize = new java.lang.Double(java.lang.Double.parseDouble((serialized.get("fontSize").toString())))
                .intValue();
        Font font = new Font(fontName, Font.PLAIN, fontSize);
        TextBox textBox;
        if (serialized.containsKey("custom")) {
        	System.out.println("hii: ");
        	Map<String, Object> customData = (Map<String, Object>) serialized.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            System.out.println("id:"+ID);
            textBox = new TextBox(ID, x, y, width, height, text);
        } else {
        	System.out.println("hello: ");
        	textBox = new TextBox(x, y, width, height, text);
        }
        textBox.setFont(font);
        float opacity = getFloat(serialized.get("opacity"));
        int op = (int) (opacity*255);
        textBox.setPenColor(TransformUtil.parseHexString((String) serialized.get("fill"), op));   // fill is colour of text
        float strokewidth = getFloat(serialized.get("strokeWidth"));    //stroke width not required for text
        textBox.setPenWidth(strokewidth);
        textBox.setType(VisualObjectType.TEXT);
        return textBox;
	}

	@Override
	protected Map<String, Object> encodePath(GeometricPath object) {
		logger.logp(Level.INFO, "KineticTransformer", "encodePath","");
        Map<String, Object> path = getCommonKineticObject(object);
        path.put("type", "Path");
        path.put("data", getPathString(object.getPathPoints()));
        return path;
	}

	@Override
	protected VisualObject decodePath(Map<String, Object> serialized) {
        GeometricPath path;
        if (serialized.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) serialized.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            path = new GeometricPath(ID, 0);
        } else {
            path = new GeometricPath();
        }
        List<double[]> points = getPathPoints((String) serialized.get("data"));
        for (double[] next : points) {
            path.lineTo(next[0], next[1]);
        }
        path.setType(VisualObjectType.PATH);
        addCommonAttributes(path, serialized);
        return path;
	}

	@Override
	protected Map<String, Object> encodeLine(LineSegment object) {
        Map<String, Object> line = getCommonKineticObject(object);
        line.put("type", "Line");
        
        double[] points=new double[4];
        points[0]=object.startX;
        points[1]=object.startY;
        points[2]=object.endX;
        points[3]=object.endY;
        
        line.put("points", points);
        return line;
	}

	@Override
	protected VisualObject decodeLine(Map<String, Object> serialized) {
		logger.logp(Level.INFO, "KineticTransformer", "decodeLine", "points="+serialized.get("points").getClass());
		List<double[]> points=getLinePoints(serialized.get("points"));
        if (points.size() != 2) {
            throw new RuntimeException("Line string invalid: " + serialized.get("path"));
        }
        double startX = points.get(0)[0];
        double startY = points.get(0)[1];
        double endX = points.get(1)[0];
        double endY = points.get(1)[1];

        LineSegment line;
        if (serialized.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) serialized.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            line = new LineSegment(ID, startX, startY, endX, endY);
        } else {
            line = new LineSegment(startX, startY, endX, endY);
        }
        line.setType(VisualObjectType.LINE);
        addCommonAttributes(line, serialized);
        return line;
	}
	
	@Override
	protected VisualObject decodePolygon(Map<String, Object> serialized) {
        Polygon polygon;
        if (serialized.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) serialized.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            polygon = new Polygon(ID, 0);
        } else {
        	polygon = new Polygon(0);
        }
        List<double[]> points = getPathPoints((String) serialized.get("points"));
        for (double[] next : points) {
        	polygon.lineTo(next[0], next[1]);
        }
        //to close the polygon
        polygon.lineTo(points.get(0)[0], points.get(0)[1]);
        polygon.setType(VisualObjectType.POLYGON);
        addCommonAttributes(polygon, serialized);
        return polygon;
	}

	@Override
	protected Map<String, Object> encodePolygon(GeometricPath object) {
		logger.logp(Level.INFO, "KineticTransformer", "encodePolygon","");		
        Map<String, Object> polygon = getCommonKineticObject(object);
        polygon.put("type", "Polygon");
        
        //remove last lineto which is used to close polygon
        String points = getPathString(object.getPathPoints());
        int index=points.lastIndexOf('L');
        points=points.substring(0, index-1);
        		
        polygon.put("points", points);
        return polygon;
	}	

	@Override
	protected VisualObject decodeArrow(Map<String, Object> serialized) {
        Arrow arrow;
        if (serialized.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) serialized.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            arrow = new Arrow(ID, 0);
        } else {
        	arrow = new Arrow(0);
        }
        List<double[]> points = getPathPoints((String) serialized.get("points"));
        for (double[] next : points) {
        	arrow.lineTo(next[0], next[1]);
        }
        arrow.setType(VisualObjectType.ARROW);
        addCommonAttributes(arrow, serialized);
        return arrow;		
	}

	@Override
	protected Map<String, Object> encodeArrow(GeometricPath object) {
        Map<String, Object> arrow = getCommonKineticObject(object);
        arrow.put("type", "Arrow");               		
        arrow.put("points",  getPathString(object.getPathPoints()));
        return arrow;
	}
	
    private static java.lang.Double getDouble(Object obj) {
        if (obj == null)
            return null;
        String doubleString = obj.toString();
        return java.lang.Double.parseDouble(doubleString);
    }

    private static Float getFloat(Object obj) {
        if (obj == null)
            return null;
        String doubleString = obj.toString();
        double value = java.lang.Double.parseDouble(doubleString);
        return (new java.lang.Double(value)).floatValue();
    }

    /**
     * Add attributes common to all kinetic objects
     * 
     * @param vo
     * @param data
     */
    private void addCommonAttributes(VisualObject vo, Map<String, Object> data) {
    	float opacity = getFloat(data.get("opacity"));
    	int op = (int) (opacity*255);
        Color color = TransformUtil.parseHexString((String) data.get("stroke"), op);
        vo.setPenColor(color);
        float width = getFloat(data.get("strokeWidth"));
        vo.setPenWidth(width);
        float rotation=getFloat(data.get("rotation"));
        vo.setRotation(rotation);
    }
    
    /**
     * Get a Kinetic object with attributes common to all shapes
     * @param obj
     * @return
     */
    private Map<String, Object> getCommonKineticObject(VisualObject obj) {
        Map<String, Object> ro = new HashMap<String, Object>();
        ro.put("opacity", obj.getPenColor().getAlpha() / 255.0);
        ro.put("stroke", TransformUtil.getColorString(obj.getPenColor()));
        ro.put("strokeWidth", obj.getPenWidth());
        Map<String, Object> customAttributes = new HashMap<String, Object>();
        customAttributes.put("objectid", obj.ID);
        ro.put("custom", customAttributes);
        ro.put("rotation", obj.getRotation());
        return ro;
    }
    
    /**
     * Get list of path points from a path string
     * 
     * @param pathString
     * @return
     */
    private static List<double[]> getPathPoints(String pathString) {
        if (!pathString.startsWith("M"))
            throw new RuntimeException("Invalid path string: " + pathString);
        List<double[]> ret = new ArrayList<double[]>();
        String[] pointStrings = pathString.substring(1).split("L");
        for (String string : pointStrings) {
            String[] split = string.split(",");
            if (split.length != 2)
                throw new RuntimeException("Invalid coordinate: " + string + " in path string: " + pathString);
            double[] next = new double[2];
            next[0] = getDouble(split[0]);
            next[1] = getDouble(split[1]);
            ret.add(next);
        }
        return ret;
    }

    /**
     * Get kinetic path string from list of points
     * 
     * @param pathPoints
     * @return
     */
    private String getPathString(List<Point2D> pathPoints) {
        StringBuilder builder = new StringBuilder("M");
        for (int i = 0; i < pathPoints.size(); ++i) {
            Point2D point2d = pathPoints.get(i);
            builder.append(point2d.getX());
            builder.append(",");
            builder.append(point2d.getY());
            if (i != pathPoints.size() - 1)
                builder.append("L");
        }
        return builder.toString();
    }
    
    /**
     * Get point of a line
     * @param points
     * @return
     */
    private List<double[]> getLinePoints(Object points){
    	 List<double[]> ret = new ArrayList<double[]>();
    	 for( int i=0;i<((List<Object>) points).size();i=i+2){
    		 double[] next = new double[2];
    		 next[0]=java.lang.Double.valueOf((String)((List<Object>)points).get(i));
    		 next[1]=java.lang.Double.valueOf((String)((List<Object>)points).get(i+1));
    		 ret.add(next);
    	 }
    	  	 
    	return ret;
    }

}
