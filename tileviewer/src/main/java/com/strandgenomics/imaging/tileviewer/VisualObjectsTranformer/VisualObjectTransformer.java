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
 * VisualObjectSerializer.java
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
package com.strandgenomics.imaging.tileviewer.VisualObjectsTranformer;

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
 * Abstract serializer for {@link VisualObject} Performs dispatch on
 * {@link VisualObjectType}. Subclasses should do appropriate serialization for
 * every type.
 * 
 * @author santhosh
 * @param <T>
 * 
 */
public abstract class VisualObjectTransformer<T> {

    /**
     * Encode the given object as
     * 
     * @param object
     * @return serialized encoding of the given object
     */
    public T encode(VisualObject object) {
        VisualObjectType type = object.getType();
        switch (type) {
        case RECTANGLE:
            return encodeRectangle((Rectangle) object);
        case ELLIPSE:
            return encodeEllipse((Ellipse) object);
        case CIRCLE:
            return encodeCircle((Circle) object);
        case PATH:
            return encodePath((GeometricPath) object);
        case TEXT:
            return encodeText((TextBox) object);
        case LINE:
            return encodeLine((LineSegment) object);
        case POLYGON:
        	return encodePolygon((GeometricPath) object);  
        case ARROW:
        	return encodeArrow((GeometricPath) object);          	
        }
        return null;
    }

    /**
     * Decode the given serialized to visual object
     * 
     * @param serialized
     *            serialized to decode
     * @param type
     *            type of the object
     * @return decoded object
     */
    public VisualObject decode(T serialized) {
        VisualObjectType type = getType(serialized);
        switch (type) {
        case RECTANGLE:
            return decodeRectangle(serialized);
        case ELLIPSE:
            return decodeEllipse(serialized);
        case CIRCLE:
            return decodeCircle(serialized);
        case PATH:
            return decodePath(serialized);
        case TEXT:
            return decodeText(serialized);
        case LINE:
            return decodeLine(serialized);
        case POLYGON:
        	return decodePolygon(serialized);
        case ARROW:
        	return decodeArrow(serialized);        	
        }
        return null;
    }

	/**
     * Get type of the visual annotation from the object
     * 
     * @param object
     * @return the type of visual object
     */
    protected abstract VisualObjectType getType(T object);

    /**
     * Encode the ellipse
     * 
     * @param ellipse
     * @return serialized ellipse
     */
    protected abstract T encodeEllipse(Ellipse ellipse);

    /**
     * decode the serialized to get ellipse
     * 
     * @param serialized
     * @return ellipse
     */
    protected abstract VisualObject decodeEllipse(T serialized);
    
    /**
     * Encode the circle
     * 
     * @param circle
     * @return serialized circle
     */
    protected abstract T encodeCircle(Circle circle);

    /**
     * decode the serialized to get circle
     * 
     * @param serialized
     * @return circle
     */
    protected abstract VisualObject decodeCircle(T serialized);

    /**
     * Encode the rectangle
     * 
     * @param rectange
     * @return serialized serialized
     */
    protected abstract T encodeRectangle(Rectangle rectange);

    /**
     * Decode the serialized to get rectangle
     * 
     * @param serialized
     * @return rectangle
     */
    protected abstract VisualObject decodeRectangle(T serialized);

    /**
     * Encode text
     * 
     * @param object
     * @return serialized
     */
    protected abstract T encodeText(TextBox object);

    /**
     * Decode text
     * 
     * @param serialized
     * @return text
     */
    protected abstract VisualObject decodeText(T serialized);

    /**
     * Encode path element
     * 
     * @param object
     * @return serialized
     */
    protected abstract T encodePath(GeometricPath object);

    /**
     * Decode path element
     * 
     * @param serialized
     * @return VisualObject
     */
    protected abstract VisualObject decodePath(T serialized);

    /**
     * Encode line
     * 
     * @param object
     * @return serialized
     */
    protected abstract T encodeLine(LineSegment object);

    /**
     * Decode line
     * 
     * @param serialized
     * @return VisualObject
     */
    protected abstract VisualObject decodeLine(T serialized);
    
    /**
     * Decode Polygon
     * @param serialized
     * @return
     */
    protected abstract VisualObject decodePolygon(T serialized);    

    /**
     * Encode Polygon
     * @param object
     * @return
     */
    protected abstract T encodePolygon(GeometricPath object);
    
    /**
     * Decode Polygon
     * @param serialized
     * @return
     */
    protected abstract VisualObject decodeArrow(T serialized);    

    /**
     * Encode Polygon
     * @param object
     * @return
     */
    protected abstract T encodeArrow(GeometricPath object);
}
