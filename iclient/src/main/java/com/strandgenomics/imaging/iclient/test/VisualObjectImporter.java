package com.strandgenomics.imaging.iclient.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.icore.vo.VisualObjectType;

/**
 * Creates Shape objects from the objects given in text file as input
 * The format of the file is assumed to be:
 * 1. Visual Object is specified as series of X Y coordinates, separated by white space
 * 2. Two visual objects are separated by empty line(there should be empty line at the end of file as well)
 * 
 * @author Anup Kulkarni
 */
public class VisualObjectImporter {
	
	public static void main(String... args) throws IOException
	{
		List<VisualObject> shapes = getVisualObjectsFromFile("/home/anup/objects.txt");
		
		print(shapes);
	}
	
	public static void print(List<VisualObject> shapes)
	{
		for(VisualObject shape:shapes)
		{
			if(shape.getType() == VisualObjectType.PATH)
			{
				float[] points = ((GeometricPath)shape).getCoordinates();
				for(float point:points)
					System.out.println(point);
				System.out.println("_______________________________");
			}
		}
	}

	/**
	 * reads input file and returns list of visual objects
	 * @param string filepath
	 * @return list of visual objects
	 * @throws IOException 
	 */
	public static List<VisualObject> getVisualObjectsFromFile(String filepath) throws IOException 
	{
		return createVisualObjects(parseFile(filepath));
	}


	/**
	 * creates visual objects from list of points
	 * @param visualObjects
	 * @return visual objects from list of points
	 */
	public static List<VisualObject> createVisualObjects(List<List<double[]>> visualObjects) 
	{
		if(visualObjects == null || visualObjects.size()==0) return null;
		
		List<VisualObject> shapes = new ArrayList<VisualObject>();
		for(List<double[]> visualObject:visualObjects)
		{
			if(visualObject == null || visualObject.size()==0) continue;
			
			GeometricPath path = new GeometricPath();
			for(double[] point:visualObject)
			{
				path.lineTo(point[0], point[1]);
			}
			shapes.add(path);
		}
		return shapes;
	}

	/**
	 * returns the object coordinates for visual object given in file
	 * @param filename name of input file
	 * @return the object coordinates for visual object given in file
	 * @throws IOException 
	 */
	public static List<List<double[]>> parseFile(String filename) throws IOException 
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		
		List<List<double[]>> shapes = new ArrayList<List<double[]>>();
		ArrayList<double[]> shape = new ArrayList<double[]>();
		
		int line_no = 0;
		while(br.ready())
		{
			line_no++;
			String line = br.readLine().trim();
			if(line.isEmpty())
			{
				// object delimiter reached
				shapes.add(shape);
				shape = new ArrayList<double[]>();
				continue;
			}
				
			
			String[] fields = line.split("\\s+");
			if(fields.length != 2)
				throw new IllegalArgumentException("error while parsing line no. "+line_no);
			
			try
			{
				double x = Double.parseDouble(fields[0]);
				double y = Double.parseDouble(fields[1]);
				double point[] = {x,y};
				
				shape.add(point);
			}
			catch (NumberFormatException e)
			{
				throw new IllegalArgumentException("error while parsing line no. "+line_no);
			}
		}
		
		//for last object delimiter(empty line) may not be present
		if(shape.size()!=0)
			shapes.add(shape);
		
		return shapes;
	}
}
