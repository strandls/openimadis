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

package com.strandgenomics.imaging.iserver.services.impl.web.util;

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
 * Visual object transformer to and from FabricJS objects.
 * @author navneet
 *
 */
@SuppressWarnings("unchecked")
public class FabricTransformer extends VisualObjectTransformer<Map<String, Object>> {

    /**
     * Logger instance to use
     */
    private Logger logger;
    
    private static FabricTransformer instance;
    
    private static Object padLock = new Object();

    /**
     * Create new fabric transformer
     */
    private FabricTransformer() {
        logger = Logger.getLogger("com.strandgenomics.imaging.iserver.impl.web.io");
    }
    
    /**
     * get instance method for singleton object of RaphaelTransformer
     * @return singleton object of RaphaelTransformer
     */
    public static FabricTransformer getInstance()
    {
    	synchronized (padLock) {
			if(instance == null)
			{
				instance = new FabricTransformer();
			}
			return instance;
		}
    }

    @Override
    protected VisualObjectType getType(Map<String, Object> object) {
        String typeString = (String) object.get("type");
        if (typeString.equals("rect"))
            return VisualObjectType.RECTANGLE;
        else if (typeString.equals("ellipse"))
            return VisualObjectType.ELLIPSE;
        else if (typeString.equals("circle"))
            return VisualObjectType.CIRCLE;
        else if (typeString.equals("text"))
            return VisualObjectType.TEXT;
        else if (typeString.equals("path")) {
            // Raphael does not distinguish between a GeometricPath and
            // LineSegment
            // A line is essentially a path with two points. Special handling
            // for the same
            //int count = getNumberOfPoints((String) object.get("path"));
           // if (count == 2) {
            //    logger.logp(Level.INFO, "RaphaelTransformer", "getType", "Treating path as line");
            //    return VisualObjectType.LINE;
            //} else {
            //    logger.logp(Level.INFO, "RaphaelTransformer", "getType", "Treating path as path");
             //   return VisualObjectType.PATH;
           // }

        }
        return null;
    }
   
    
    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#encodeCircle(com.strandgenomics.imaging.icore.vo.Ellipse)
     */
    @Override
    protected Map<String, Object> encodeCircle(Circle circle) {
        Double bounds = circle.getBounds();
        Map<String, Object> raphaelObject = getBaseRaphaelObject(circle);
        logger.logp(Level.INFO, "FabricTransformer", "encodeCircle", "x="+bounds.x+" y="+bounds.y+" width="+bounds.width+" height="+bounds.height);
        raphaelObject.put("type", "circle");
        raphaelObject.put("left", bounds.x + (bounds.width / 2));
        raphaelObject.put("top", bounds.y + (bounds.height / 2));
        raphaelObject.put("width", bounds.width);
        raphaelObject.put("height", bounds.height);
        raphaelObject.put("radius", bounds.height/2);
        return raphaelObject;
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#decodeCircle(java.lang.String)
     */
    @Override
    protected VisualObject decodeCircle(Map<String, Object> circleData) {
        double width = getDouble(circleData.get("width"));
        double height = getDouble(circleData.get("height"));
        double x = getDouble(circleData.get("left")) - getDouble(circleData.get("radius"));
        double y = getDouble(circleData.get("top")) - getDouble(circleData.get("radius"));
        Circle circle;
        if (circleData.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) circleData.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            circle = new Circle(ID, x, y, width, height);
        } else {
        	circle = new Circle(x, y, width, height);
        }
        addRaphaelCommon(circle, circleData);
        return circle;
    }
    
    
    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#encodeEllipse(com.strandgenomics.imaging.icore.vo.Ellipse)
     */
    @Override
    protected Map<String, Object> encodeEllipse(Ellipse ellipse) {
        Double bounds = ellipse.getBounds();
        Map<String, Object> raphaelObject = getBaseRaphaelObject(ellipse);
        raphaelObject.put("type", "ellipse");
        raphaelObject.put("left", bounds.x + (bounds.width / 2));
        raphaelObject.put("top", bounds.y + (bounds.height / 2));
        raphaelObject.put("width", bounds.width);
        raphaelObject.put("height", bounds.height);
        raphaelObject.put("rx", bounds.width/2);
        raphaelObject.put("ry", bounds.height/2);
        logger.logp(Level.INFO, "FabricTransformer", "encodeCircle","ellipse="+raphaelObject);
        return raphaelObject;
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#decodeEllipse(java.lang.String)
     */
    @Override
    protected VisualObject decodeEllipse(Map<String, Object> ellipseData) {
    	logger.logp(Level.INFO, "FabricTransformer", "decodeEllipse", "data="+ellipseData);
        double width = getDouble(ellipseData.get("width"));
        double height = getDouble(ellipseData.get("height"));
        double x = getDouble(ellipseData.get("left")) - (getDouble(ellipseData.get("width")) / 2);
        double y = getDouble(ellipseData.get("top")) - (getDouble(ellipseData.get("height")) / 2);
        Ellipse ellipse;
        if (ellipseData.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) ellipseData.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            ellipse = new Ellipse(ID, x, y, width, height);
        } else {
            ellipse = new Ellipse(x, y, width, height);
        }
        addRaphaelCommon(ellipse, ellipseData);
        return ellipse;
    }

	@Override
	protected Map<String, Object> encodeRectangle(Rectangle rectange) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VisualObject decodeRectangle(Map<String, Object> serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> encodeText(TextBox object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VisualObject decodeText(Map<String, Object> serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> encodePath(GeometricPath object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VisualObject decodePath(Map<String, Object> serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> encodeLine(LineSegment object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VisualObject decodeLine(Map<String, Object> serialized) {
		// TODO Auto-generated method stub
		return null;
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
    
    private void addRaphaelCommon(VisualObject vo, Map<String, Object> data) {
        float opacity = getFloat(data.get("opacity"));
        int op = (int)opacity*255;
        Color color = TransformUtil.parseHexString((String) data.get("stroke"), op);
        vo.setPenColor(color);
        float width = getFloat(data.get("strokeWidth"));
        vo.setPenWidth(width);
    }
    
    private Map<String, Object> getBaseRaphaelObject(VisualObject obj) {
        Map<String, Object> ro = new HashMap<String, Object>();
        //ro.put("stroke-linejoin", "round");
       // ro.put("stroke-linecap", "round");
       // ro.put("transform", new ArrayList<Object>());
        ro.put("fill", "none");
        ro.put("opacity", obj.getPenColor().getAlpha() / 255);
        ro.put("stroke", TransformUtil.getColorString(obj.getPenColor()));
        ro.put("strokeWidth", obj.getPenWidth());
        Map<String, Object> customAttributes = new HashMap<String, Object>();
        customAttributes.put("objectid", obj.ID);
        ro.put("custom", customAttributes);

        return ro;
    }

	@Override
	protected VisualObject decodePolygon(Map<String, Object> serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> encodePolygon(Polygon object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VisualObject decodeArrow(Map<String, Object> serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> encodeArrow(Arrow object) {
		// TODO Auto-generated method stub
		return null;
	}


}
