/*
 * RaphaelSerializer.java
 *
 * Product:  faNGS
 * Next Generation Sequencing
 *
 * Copyright 2007-2012, Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal,
 * Bangalore 560024
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
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
 * Visual object transformer to and from RaphaelJS objects. Instances are
 * thread-safe.
 * 
 * @author santhosh
 * 
 */
@SuppressWarnings("unchecked")
public class RaphaelTransformer extends VisualObjectTransformer<Map<String, Object>> {

    /**
     * Logger instance to use
     */
    private Logger logger;

    /**
     * Create new raphael transformer
     */
    RaphaelTransformer() {
        logger = Logger.getLogger("com.strandgenomics.imaging.iserver.impl.web.io");
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
            int count = getNumberOfPoints((String) object.get("path"));
            if (count == 2) {
                logger.logp(Level.INFO, "RaphaelTransformer", "getType", "Treating path as line");
                return VisualObjectType.LINE;
            } else {
                logger.logp(Level.INFO, "RaphaelTransformer", "getType", "Treating path as path");
                return VisualObjectType.PATH;
            }

        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#encodeEllipse(com.strandgenomics.imaging.icore.vo.Ellipse)
     */
    @Override
    protected Map<String, Object> encodeEllipse(Ellipse ellipse) {
        Double bounds = ellipse.getBounds();
        Map<String, Object> raphaelObject = getBaseRaphaelObject(ellipse);
        raphaelObject.put("type", "ellipse");
        raphaelObject.put("cx", bounds.x + (bounds.width / 2));
        raphaelObject.put("cy", bounds.y + (bounds.height / 2));
        raphaelObject.put("rx", bounds.width / 2);
        raphaelObject.put("ry", bounds.height / 2);
        return raphaelObject;
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#decodeEllipse(java.lang.String)
     */
    @Override
    protected VisualObject decodeEllipse(Map<String, Object> ellipseData) {
        double width = getDouble(ellipseData.get("rx")) * 2;
        double height = getDouble(ellipseData.get("ry")) * 2;
        double x = getDouble(ellipseData.get("cx")) - width / 2;
        double y = getDouble(ellipseData.get("cy")) - height / 2;
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
    
    
    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#encodeCircle(com.strandgenomics.imaging.icore.vo.Ellipse)
     */
    @Override
    protected Map<String, Object> encodeCircle(Circle circle) {
        Double bounds = circle.getBounds();
        Map<String, Object> raphaelObject = getBaseRaphaelObject(circle);
        raphaelObject.put("fill", raphaelObject.get("stroke"));
        raphaelObject.put("type", "circle");
        raphaelObject.put("cx", bounds.x + (bounds.width / 2));
        raphaelObject.put("cy", bounds.y + (bounds.height / 2));
        raphaelObject.put("r", bounds.width / 2);
        return raphaelObject;
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#decodeCircle(java.lang.String)
     */
    @Override
    protected VisualObject decodeCircle(Map<String, Object> circleData) {
        double width = getDouble(circleData.get("r")) * 2;
        double height = getDouble(circleData.get("r")) * 2;
        double x = getDouble(circleData.get("cx")) - width / 2;
        double y = getDouble(circleData.get("cy")) - height / 2;
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
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#encodeRectangle(com.strandgenomics.imaging.icore.vo.Rectangle)
     */
    @Override
    protected Map<String, Object> encodeRectangle(Rectangle rectange) {
        Double bounds = rectange.getBounds();
        Map<String, Object> raphaelObject = getBaseRaphaelObject(rectange);
        raphaelObject.put("type", "rect");
        raphaelObject.put("x", bounds.x);
        raphaelObject.put("y", bounds.y);
        raphaelObject.put("r", 0);
        raphaelObject.put("rx", 0);
        raphaelObject.put("ry", 0);
        raphaelObject.put("width", bounds.width);
        raphaelObject.put("height", bounds.height);

        return raphaelObject;
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iserver.services.impl.web.io.VisualObjectSerializer#decodeRectangle(java.lang.String)
     */
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
        addRaphaelCommon(rectangle, rectData);
        return rectangle;
    }

    @Override
    protected Map<String, Object> encodeText(TextBox object) {
        Double bounds = object.getBounds();
        Map<String, Object> raphaelObject = getBaseRaphaelObject(object);
        raphaelObject.put("type", "text");
        raphaelObject.put("x", bounds.x);
        raphaelObject.put("y", bounds.y);
        raphaelObject.put("text-anchor", "start");
        raphaelObject.put("text", object.getText());
        raphaelObject.put("font", "" + object.getFont().getSize() + "px " + object.getFont().getName());
        raphaelObject.put("font-size", object.getFont().getSize());
        raphaelObject.put("stroke-width", 1);
        raphaelObject.put("fill", TransformUtil.getColorString(object.getPenColor()));
        return raphaelObject;
    }

    @Override
    protected VisualObject decodeText(Map<String, Object> serialized) {
        double x = getDouble(serialized.get("x"));
        double y = getDouble(serialized.get("y"));
        String text = (String) serialized.get("text");
        String fontName = (String) serialized.get("font");
        int fontSize = new java.lang.Double(java.lang.Double.parseDouble((serialized.get("font-size").toString())))
                .intValue();
        Font font = new Font(fontName, Font.PLAIN, fontSize);
        TextBox textBox;
        if (serialized.containsKey("custom")) {
            Map<String, Object> customData = (Map<String, Object>) serialized.get("custom");
            int ID = Integer.parseInt(customData.get("objectid").toString());
            textBox = new TextBox(ID, x, y, 100.0, 10.0, text);
        } else {
            textBox = new TextBox(x, y, 100.0, 10.0, text);
        }
        textBox.setFont(font);
        float opacity = getFloat(serialized.get("stroke-opacity"));
        int op = (int) (opacity*255);
        textBox.setPenColor(TransformUtil.parseHexString((String) serialized.get("stroke"), op));
        float width = getFloat(serialized.get("stroke-width"));
        textBox.setPenWidth(width);
        return textBox;
    }

    @Override
    protected Map<String, Object> encodePath(GeometricPath object) {
        Map<String, Object> path = getBaseRaphaelObject(object);
        path.put("type", "path");
        path.put("path", getPathString(object.getPathPoints()));
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
        List<double[]> points = getPathPoints((String) serialized.get("path"));
        for (double[] next : points) {
            path.lineTo(next[0], next[1]);
        }
        addRaphaelCommon(path, serialized);
        return path;
    }

    @Override
    protected Map<String, Object> encodeLine(LineSegment object) {
        Map<String, Object> path = getBaseRaphaelObject(object);
        path.put("type", "path");
        path.put("path", getPathString(object.startX, object.startY, object.endX, object.endY));
        return path;
    }

    @Override
    protected VisualObject decodeLine(Map<String, Object> serialized) {
        List<double[]> points = getPathPoints((String) serialized.get("path"));
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
        addRaphaelCommon(line, serialized);
        return line;
    }

    /**
     * Add attributes common to all raphael objects
     * 
     * @param vo
     * @param data
     */
    private void addRaphaelCommon(VisualObject vo, Map<String, Object> data) {
        float opacity = getFloat(data.get("stroke-opacity"));
        int op = (int) (opacity*255);
        Color color = TransformUtil.parseHexString((String) data.get("stroke"), op);
        vo.setPenColor(color);
        float width = getFloat(data.get("stroke-width"));
        vo.setPenWidth(width);
    }

    private Map<String, Object> getBaseRaphaelObject(VisualObject obj) {
        Map<String, Object> ro = new HashMap<String, Object>();
        ro.put("stroke-linejoin", "round");
        ro.put("stroke-linecap", "round");
        ro.put("transform", new ArrayList<Object>());
        ro.put("fill", "none");
        ro.put("stroke-opacity", obj.getPenColor().getAlpha() / 255);
        ro.put("stroke", TransformUtil.getColorString(obj.getPenColor()));
        ro.put("stroke-width", obj.getPenWidth());
        Map<String, Object> customAttributes = new HashMap<String, Object>();
        customAttributes.put("objectid", obj.ID);
        ro.put("custom", customAttributes);

        return ro;
    }

    /**
     * Get number of points in a path string. This is the number of 'L'
     * characters plus one.
     * 
     * @param object
     * @return
     */
    private int getNumberOfPoints(String object) {
        int lCount = 0;
        for (int i = 0; i < object.length(); ++i) {
            if (object.charAt(i) == 'L') {
                lCount += 1;
            }
        }
        lCount += 1;
        return lCount;
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
     * Get raphael path string from list of points
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
     * Get path string for the simple line starting at (startX, startY) and
     * ending at (endX, endY).
     * 
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    private String getPathString(double startX, double startY, double endX, double endY) {
        StringBuilder builder = new StringBuilder("M");
        builder.append(startX + "," + startY);
        builder.append('L');
        builder.append(endX + "," + endY);
        return builder.toString();
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
     * @param args
     */
    public static void main(String[] args) {
      //  System.out.println(TransformUtil.parseHexString("#FF0000", 0.5f));
        List<double[]> pathPoints = getPathPoints("M189.3002481389578,129.58808933002481L189.3002481389578,129.58808933002481L189.3002481389578,129.58808933002481L189.3002481389578,129.58808933002481L190.57071960297768,127.04714640198512L190.57071960297768,127.04714640198512L190.57071960297768,127.04714640198512L190.57071960297768,127.04714640198512L190.57071960297768,127.04714640198512L190.57071960297768,127.04714640198512L190.57071960297768,127.04714640198512L190.57071960297768,127.04714640198512L223.6029776674938,95.28535980148884L223.6029776674938,95.28535980148884L223.6029776674938,95.28535980148884L223.6029776674938,95.28535980148884L223.6029776674938,95.28535980148884L223.6029776674938,95.28535980148884L223.6029776674938,95.28535980148884L223.6029776674938,95.28535980148884L229.95533498759306,85.12158808933002L229.95533498759306,85.12158808933002L229.95533498759306,85.12158808933002L229.95533498759306,85.12158808933002L229.95533498759306,85.12158808933002L229.95533498759306,85.12158808933002L229.95533498759306,85.12158808933002L229.95533498759306,85.12158808933002L232.49627791563276,80.03970223325062L232.49627791563276,80.03970223325062L232.49627791563276,80.03970223325062L232.49627791563276,80.03970223325062L232.49627791563276,80.03970223325062L232.49627791563276,80.03970223325062L232.49627791563276,80.03970223325062L232.49627791563276,80.03970223325062L237.57816377171216,73.68734491315136L237.57816377171216,73.68734491315136L237.57816377171216,73.68734491315136L237.57816377171216,73.68734491315136L237.57816377171216,73.68734491315136L237.57816377171216,73.68734491315136L237.57816377171216,73.68734491315136L237.57816377171216,73.68734491315136L241.38957816377172,68.60545905707195L241.38957816377172,68.60545905707195L241.38957816377172,68.60545905707195L241.38957816377172,68.60545905707195L241.38957816377172,68.60545905707195L241.38957816377172,68.60545905707195L241.38957816377172,68.60545905707195L241.38957816377172,68.60545905707195L242.66004962779155,64.79404466501241L242.66004962779155,64.79404466501241L242.66004962779155,64.79404466501241L242.66004962779155,64.79404466501241L242.66004962779155,64.79404466501241L242.66004962779155,64.79404466501241L242.66004962779155,64.79404466501241L242.66004962779155,64.79404466501241L243.9305210918114,62.2531017369727L243.9305210918114,62.2531017369727L243.9305210918114,62.2531017369727L243.9305210918114,62.2531017369727L243.9305210918114,62.2531017369727L243.9305210918114,62.2531017369727L243.9305210918114,62.2531017369727L243.9305210918114,62.2531017369727L243.9305210918114,60.98263027295285L243.9305210918114,60.98263027295285L243.9305210918114,60.98263027295285L243.9305210918114,60.98263027295285L243.9305210918114,60.98263027295285L243.9305210918114,60.98263027295285L243.9305210918114,60.98263027295285L243.9305210918114,60.98263027295285L245.20099255583128,59.712158808933005L245.20099255583128,59.712158808933005L245.20099255583128,59.712158808933005L245.20099255583128,59.712158808933005L245.20099255583128,59.712158808933005L245.20099255583128,59.712158808933005L245.20099255583128,59.712158808933005L245.20099255583128,59.712158808933005L245.20099255583128,57.1712158808933L245.20099255583128,57.1712158808933L245.20099255583128,57.1712158808933L245.20099255583128,57.1712158808933L245.20099255583128,57.1712158808933L245.20099255583128,57.1712158808933L245.20099255583128,57.1712158808933L245.20099255583128,57.1712158808933L245.20099255583128,55.90074441687345L245.20099255583128,55.90074441687345L245.20099255583128,55.90074441687345L245.20099255583128,55.90074441687345L245.20099255583128,55.90074441687345L245.20099255583128,55.90074441687345L245.20099255583128,55.90074441687345L245.20099255583128,55.90074441687345L243.9305210918114,54.630272952853595L243.9305210918114,54.630272952853595L243.9305210918114,54.630272952853595L243.9305210918114,54.630272952853595L243.9305210918114,54.630272952853595L243.9305210918114,54.630272952853595L243.9305210918114,54.630272952853595L243.9305210918114,54.630272952853595L243.9305210918114,53.359801488833746L243.9305210918114,53.359801488833746L243.9305210918114,53.359801488833746L243.9305210918114,53.359801488833746L243.9305210918114,53.359801488833746L243.9305210918114,53.359801488833746L243.9305210918114,53.359801488833746L243.9305210918114,53.359801488833746L240.11910669975185,52.0893300248139L240.11910669975185,52.0893300248139L240.11910669975185,52.0893300248139L240.11910669975185,52.0893300248139L240.11910669975185,52.0893300248139L240.11910669975185,52.0893300248139L240.11910669975185,52.0893300248139L240.11910669975185,52.0893300248139L236.30769230769232,50.81885856079405L236.30769230769232,50.81885856079405L236.30769230769232,50.81885856079405L236.30769230769232,50.81885856079405L236.30769230769232,50.81885856079405L236.30769230769232,50.81885856079405L236.30769230769232,50.81885856079405L236.30769230769232,50.81885856079405L232.49627791563276,49.54838709677419L232.49627791563276,49.54838709677419L232.49627791563276,49.54838709677419L232.49627791563276,49.54838709677419L232.49627791563276,49.54838709677419L232.49627791563276,49.54838709677419L232.49627791563276,49.54838709677419L232.49627791563276,49.54838709677419L224.87344913151364,49.54838709677419L224.87344913151364,49.54838709677419L224.87344913151364,49.54838709677419L224.87344913151364,49.54838709677419L224.87344913151364,49.54838709677419L224.87344913151364,49.54838709677419L224.87344913151364,49.54838709677419L224.87344913151364,49.54838709677419L217.25062034739454,49.54838709677419L217.25062034739454,49.54838709677419L217.25062034739454,49.54838709677419L217.25062034739454,49.54838709677419L217.25062034739454,49.54838709677419L217.25062034739454,49.54838709677419L217.25062034739454,49.54838709677419L217.25062034739454,49.54838709677419L210.8982630272953,49.54838709677419L210.8982630272953,49.54838709677419L210.8982630272953,49.54838709677419L210.8982630272953,49.54838709677419L210.8982630272953,49.54838709677419L210.8982630272953,49.54838709677419L210.8982630272953,49.54838709677419L210.8982630272953,49.54838709677419L204.54590570719603,49.54838709677419L204.54590570719603,49.54838709677419L204.54590570719603,49.54838709677419L204.54590570719603,49.54838709677419L204.54590570719603,49.54838709677419L204.54590570719603,49.54838709677419L204.54590570719603,49.54838709677419L204.54590570719603,49.54838709677419L196.92307692307693,49.54838709677419L196.92307692307693,49.54838709677419L196.92307692307693,49.54838709677419L196.92307692307693,49.54838709677419L196.92307692307693,49.54838709677419L196.92307692307693,49.54838709677419L196.92307692307693,49.54838709677419L196.92307692307693,49.54838709677419L186.75930521091811,49.54838709677419L186.75930521091811,49.54838709677419L186.75930521091811,49.54838709677419L186.75930521091811,49.54838709677419L186.75930521091811,49.54838709677419L186.75930521091811,49.54838709677419L186.75930521091811,49.54838709677419L186.75930521091811,49.54838709677419L181.67741935483872,50.81885856079405L181.67741935483872,50.81885856079405L181.67741935483872,50.81885856079405L181.67741935483872,50.81885856079405L181.67741935483872,50.81885856079405L181.67741935483872,50.81885856079405L181.67741935483872,50.81885856079405L181.67741935483872,50.81885856079405L174.0545905707196,52.0893300248139L174.0545905707196,52.0893300248139L174.0545905707196,52.0893300248139L174.0545905707196,52.0893300248139L174.0545905707196,52.0893300248139L174.0545905707196,52.0893300248139L174.0545905707196,52.0893300248139L174.0545905707196,52.0893300248139L170.24317617866004,53.359801488833746L170.24317617866004,53.359801488833746L170.24317617866004,53.359801488833746L170.24317617866004,53.359801488833746L170.24317617866004,53.359801488833746L170.24317617866004,53.359801488833746L170.24317617866004,53.359801488833746L170.24317617866004,53.359801488833746L165.16129032258064,55.90074441687345L165.16129032258064,55.90074441687345L165.16129032258064,55.90074441687345L165.16129032258064,55.90074441687345L165.16129032258064,55.90074441687345L165.16129032258064,55.90074441687345L165.16129032258064,55.90074441687345L165.16129032258064,55.90074441687345L163.8908188585608,58.44168734491315L163.8908188585608,58.44168734491315L163.8908188585608,58.44168734491315L163.8908188585608,58.44168734491315L163.8908188585608,58.44168734491315L163.8908188585608,58.44168734491315L163.8908188585608,58.44168734491315L163.8908188585608,58.44168734491315L162.62034739454094,62.2531017369727L162.62034739454094,62.2531017369727L162.62034739454094,62.2531017369727L162.62034739454094,62.2531017369727L162.62034739454094,62.2531017369727L162.62034739454094,62.2531017369727L162.62034739454094,62.2531017369727L162.62034739454094,62.2531017369727L160.07940446650124,72.41687344913151L160.07940446650124,72.41687344913151L160.07940446650124,72.41687344913151L160.07940446650124,72.41687344913151L160.07940446650124,72.41687344913151L160.07940446650124,72.41687344913151L160.07940446650124,72.41687344913151L160.07940446650124,72.41687344913151L158.80893300248138,81.31017369727047L158.80893300248138,81.31017369727047L158.80893300248138,81.31017369727047L158.80893300248138,81.31017369727047L158.80893300248138,81.31017369727047L158.80893300248138,81.31017369727047L158.80893300248138,81.31017369727047L158.80893300248138,81.31017369727047L158.80893300248138,87.66253101736973L158.80893300248138,87.66253101736973L158.80893300248138,87.66253101736973L158.80893300248138,87.66253101736973L158.80893300248138,87.66253101736973L158.80893300248138,87.66253101736973L158.80893300248138,87.66253101736973L158.80893300248138,87.66253101736973L158.80893300248138,91.47394540942928L158.80893300248138,91.47394540942928L158.80893300248138,91.47394540942928L158.80893300248138,91.47394540942928L158.80893300248138,91.47394540942928L158.80893300248138,91.47394540942928L158.80893300248138,91.47394540942928L158.80893300248138,91.47394540942928L161.34987593052108,102.90818858560795L161.34987593052108,102.90818858560795L161.34987593052108,102.90818858560795L161.34987593052108,102.90818858560795L161.34987593052108,102.90818858560795L161.34987593052108,102.90818858560795L161.34987593052108,102.90818858560795L161.34987593052108,102.90818858560795L161.34987593052108,109.26054590570719L161.34987593052108,109.26054590570719L161.34987593052108,109.26054590570719L161.34987593052108,109.26054590570719L161.34987593052108,109.26054590570719L161.34987593052108,109.26054590570719L161.34987593052108,109.26054590570719L161.34987593052108,109.26054590570719L162.62034739454094,115.61290322580645L162.62034739454094,115.61290322580645L162.62034739454094,115.61290322580645L162.62034739454094,115.61290322580645L162.62034739454094,115.61290322580645L162.62034739454094,115.61290322580645L162.62034739454094,115.61290322580645L162.62034739454094,115.61290322580645L166.4317617866005,121.9652605459057L166.4317617866005,121.9652605459057L166.4317617866005,121.9652605459057L166.4317617866005,121.9652605459057L166.4317617866005,121.9652605459057L166.4317617866005,121.9652605459057L166.4317617866005,121.9652605459057L166.4317617866005,121.9652605459057L166.4317617866005,127.04714640198512L166.4317617866005,127.04714640198512L166.4317617866005,127.04714640198512L166.4317617866005,127.04714640198512L166.4317617866005,127.04714640198512L166.4317617866005,127.04714640198512L166.4317617866005,127.04714640198512L166.4317617866005,127.04714640198512L167.70223325062034,130.85856079404468L167.70223325062034,130.85856079404468L167.70223325062034,130.85856079404468L167.70223325062034,130.85856079404468L167.70223325062034,130.85856079404468L167.70223325062034,130.85856079404468L167.70223325062034,130.85856079404468L167.70223325062034,130.85856079404468L168.9727047146402,133.39950372208438L168.9727047146402,133.39950372208438L168.9727047146402,133.39950372208438L168.9727047146402,133.39950372208438L168.9727047146402,133.39950372208438L168.9727047146402,133.39950372208438L168.9727047146402,133.39950372208438L168.9727047146402,133.39950372208438L170.24317617866004,133.39950372208438L170.24317617866004,133.39950372208438L170.24317617866004,133.39950372208438L170.24317617866004,133.39950372208438L170.24317617866004,133.39950372208438L170.24317617866004,133.39950372208438L170.24317617866004,133.39950372208438L170.24317617866004,133.39950372208438L171.5136476426799,134.6699751861042L171.5136476426799,134.6699751861042L171.5136476426799,134.6699751861042L171.5136476426799,134.6699751861042L171.5136476426799,134.6699751861042L171.5136476426799,134.6699751861042L171.5136476426799,134.6699751861042L171.5136476426799,134.6699751861042");
        System.out.println(pathPoints.size());
        System.out.println((float) pathPoints.get(0)[0] + " " + (float) pathPoints.get(0)[1]);

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
