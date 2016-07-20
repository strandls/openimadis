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
 * GeometricPath.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.icore.vo;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The GeometricPath class represents a geometric path constructed from a series line segments. 
 * A general free-hand drawing can be considered as a series of connected line segments
 */
public class GeometricPath extends VisualObject {
	
	private static final long serialVersionUID = -4055774170586971771L;
	static final int INIT_SIZE = 20;
    static final int EXPAND_MAX = 500;
	
    /**
     * a series of x followed by y - coordinates of points capturing the path
     */
	protected float[] coordinates;
	/**
	 * number of coordinates
	 */
	protected int noOfCoordinates;
	
	/**
	 * creates empty path of default initial capacity=20
	 */
	public GeometricPath()
	{
		this(INIT_SIZE);
	}
	
    /**
     * Constructs a new empty GeometricPath with the specified initial capacity to store path segments.
     * This number is an initial guess as to how many path segments will be added to the path, 
     * but the storage is expanded as needed to store whatever path segments are added.
     * 
     * @param initialCapacity the estimate for the number of path segments in this GeometricPath
     */
	public GeometricPath(int initialCapacity)
	{
	    super(VisualObjectType.PATH);
		coordinates = new float[initialCapacity * 2];
		noOfCoordinates = 0;
	}
	
	/**
	 * 
	 * @param ID object id
	 * @param initialCapacity  the estimate for the number of path segments in this GeometricPath
	 */
	public GeometricPath(int ID, int initialCapacity)
	{
		super(VisualObjectType.PATH, ID);
		coordinates = new float[initialCapacity * 2];
		noOfCoordinates = 0;
	}
	
	
	/**
	 * geometric objects constructed from a series of points
	 * type takes the geometric object type (eg. polygon)
	 * @param type
	 * @param initialCapacity
	 */
	public GeometricPath(VisualObjectType type,int initialCapacity)
	{
	    super(type);
		coordinates = new float[initialCapacity * 2];
		noOfCoordinates = 0;
	}
	
	/**
	 * 
	 * @param ID object id
	 * @param initialCapacity  the estimate for the number of path segments in this GeometricPath
	 */
	public GeometricPath(VisualObjectType type, int ID, int initialCapacity)
	{
		super(type, ID);
		coordinates = new float[initialCapacity * 2];
		noOfCoordinates = 0;
	}
	
    /**
     * Trims the capacity of this <tt>GeometricPath</tt> instance to be the
     * path's number of coordinates.  An application can use this operation to minimize
     * the storage of an <tt>GeometricPath</tt> instance.
     */
    public synchronized void trimToSize() 
    {
    	if(coordinates.length != noOfCoordinates)
    	{
	    	float[] copiedData = new float[noOfCoordinates];
	    	System.arraycopy(coordinates, 0, copiedData, 0, noOfCoordinates);
	    	coordinates = copiedData;
    	}
    }
    
    /**
     * Returns the underlying coordinate values
     * @return the underlying coordinate values
     */
    public synchronized float[] getCoordinates()
    {
    	float[] copiedData = new float[noOfCoordinates];
    	System.arraycopy(coordinates, 0, copiedData, 0, noOfCoordinates);
    	return copiedData;
    }
	
    /**
     * Adds a point to the path by drawing a straight line from the
     * current coordinates to the new specified coordinates
     *
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     */
    public synchronized void lineTo(float x, float y) 
    {
        makeRoom(2);
        coordinates[noOfCoordinates++] = x;
        coordinates[noOfCoordinates++] = y;
    }
    
    /**
     * Adds a point to the path by drawing a straight line from the
     * current coordinates to the new specified coordinates
     *
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     */
    public synchronized void lineTo(double x, double y) 
    {
        makeRoom(2);
        coordinates[noOfCoordinates++] = (float)x;
        coordinates[noOfCoordinates++] = (float)y;
    }
	
    /**
     * returns ordered list of points in this path
     * @return ordered list of points in this path
     */
    public synchronized List<Point2D> getPathPoints()
    {
    	List<Point2D> path = new ArrayList<Point2D>(noOfCoordinates/2);
    	for(int i=0;i<noOfCoordinates;i+=2)
    	{
    		Point2D point = new Point2D.Float(coordinates[i], coordinates[i+1]);
    		path.add(point);
    	}
    	return path;
    }
    
	void makeRoom(int newCoords) 
	{
        if (noOfCoordinates + newCoords > coordinates.length) 
        {
        	coordinates = Arrays.copyOf(coordinates, coordinates.length+EXPAND_MAX);
        }
    }
	
	@Override
    public final synchronized Rectangle2D.Double getBounds() 
	{
        double startX, startY, endX, endY;
        int i = noOfCoordinates;
        
        if (i > 0) 
        {
            startY = endY = coordinates[--i];
            startX = endX = coordinates[--i];
            
            while (i > 0) 
            {
                double y = coordinates[--i];
                double x = coordinates[--i];
                
                if (x < startX) startX = x;
                if (y < startY) startY = y;
                
                if (x > endX) endX = x;
                if (y > endY) endY = y;
            }
        } 
        else 
        {
            startX = startY = endX = endY = 0.0;
        }
        
        return new Rectangle2D.Double(startX, startY, endX - startX, endY - startY);
    }
}
