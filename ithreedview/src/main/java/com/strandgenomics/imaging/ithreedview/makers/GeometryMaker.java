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

package com.strandgenomics.imaging.ithreedview.makers;

import java.util.ArrayList;

import javax.media.j3d.QuadArray;
import javax.vecmath.Point3f;

public class GeometryMaker{

	ArrayList<QuadArray> currentGeometries;

	public GeometryMaker()
	{
		currentGeometries = new ArrayList<QuadArray>();

	}
	///{{{generateGeometryX 
	private void generateGeometryX(int num,float xMax,float yMax,float zMax) {

		float gridSpacing = 1f/num;

		for (float x = 0; x < xMax; x+=gridSpacing) {

			Point3f[] genCoords = new Point3f[4];

			genCoords[0] = new Point3f(x,	0,	0);
			genCoords[1] = new Point3f(x,	yMax,	0);
			genCoords[2] = new Point3f(x,	yMax,	zMax);
			genCoords[3] = new Point3f(x,	0,	zMax);

			QuadArray genSquare = new QuadArray(4, QuadArray.COORDINATES);
			genSquare.setCoordinates(0, genCoords);
			currentGeometries.add(genSquare);
		}
	}
	///}}}

	///{{{ generate Y geometry
	private void generateGeometryY(int num,float xMax, float yMax, float zMax) {

		float gridSpacing = 1f/num;

		for (float y = 0; y < yMax; y+=gridSpacing) {

			Point3f[] genCoords = new Point3f[4];

			genCoords[0] = new Point3f(0,	y,	0);
			genCoords[1] = new Point3f(xMax,	y,	0);
			genCoords[2] = new Point3f(xMax,	y,	zMax);
			genCoords[3] = new Point3f(0,	y,	zMax);

			QuadArray genSquare = new QuadArray(4, QuadArray.COORDINATES);
			genSquare.setCoordinates(0, genCoords);

			currentGeometries.add(genSquare);
		}
	}
	///}}}

	///{{{  Generates geometry along Z
	private void generateGeometryZ(int num,float xMax, float yMax, float zMax) {

		float gridSpacing = (float)1/(float)num;
		for (float z = 0; z < zMax; z+=gridSpacing) {

			Point3f[] genCoords = new Point3f[4];
			genCoords[0] = new Point3f(0,	0,	z);
			genCoords[1] = new Point3f(xMax,	0,	z);
			genCoords[2] = new Point3f(xMax,	yMax,	z);
			genCoords[3] = new Point3f(0,	yMax,	z);
			QuadArray genSquare = new QuadArray(4, QuadArray.COORDINATES);
			genSquare.setCoordinates(0, genCoords);
			currentGeometries.add(genSquare);
		}
	}
	///}}}

	public ArrayList<QuadArray> generateGeometries(int num, float xMax, float yMax, float zMax)
	{
		return generateGeometries(num,num,num,xMax,yMax,zMax,0);
	
	}
	public ArrayList<QuadArray> generateGeometries(int num, float xMax, float yMax, float zMax, float xMin, float yMin, float zMin)
	{
	
		clearGeometries();

		generateReverseGeometryX(num,xMin,xMax,yMin,yMax,zMin,zMax);
		generateReverseGeometryY(num,xMin,xMax,yMin,yMax,zMin,zMax);
		generateReverseGeometryZ(num,xMin,xMax,yMin,yMax,zMin,zMax);
		return currentGeometries;
	}


	public ArrayList<QuadArray> generateGeometries(int xnum, int  ynum, int znum, float xMax, float yMax, float zMax, int type )
	{

		clearGeometries();
		float zother = zMax;
		if(type==1) {zother = 0.0f;}
		generateGeometryX(xnum,xMax,yMax,zother);
		generateGeometryY(ynum,xMax,yMax,zother);
		generateGeometryZ(znum,xMax,yMax,zMax);
		return currentGeometries;

	}

	public void clearGeometries()
	{
		for(int i=0;i< currentGeometries.size();i++){
			QuadArray q = currentGeometries.get(i);
			q = null;
		}
		currentGeometries.clear();
	}
	///{{{generateGeometryX 
	private void generateReverseGeometryX(int num,float xMin,float xMax,float yMin,float yMax,float zMin, float zMax) {

		float gridSpacing = 1f/num;

		for (float x = xMin; x < xMax; x+=gridSpacing) {

			Point3f[] genCoords = new Point3f[4];

			genCoords[0] = new Point3f(x,	yMin,	zMin);
			genCoords[1] = new Point3f(x,	yMax,	zMin);
			genCoords[2] = new Point3f(x,	yMax,	zMax);
			genCoords[3] = new Point3f(x,	yMin,	zMax);

			QuadArray genSquare = new QuadArray(4, QuadArray.COORDINATES);
			genSquare.setCoordinates(0, genCoords);
			currentGeometries.add(genSquare);
		}
	}
	///}}}

	///{{{ generate Y geometry
	private void generateReverseGeometryY(int num,float xMin,float xMax, float yMin, float yMax,float zMin, float zMax) {

		float gridSpacing = 1f/num;

		for (float y = yMin; y < yMax; y+=gridSpacing) {

			Point3f[] genCoords = new Point3f[4];

			genCoords[0] = new Point3f(xMin,	y,	zMin);
			genCoords[1] = new Point3f(xMax,	y,	zMin);
			genCoords[2] = new Point3f(xMax,	y,	zMax);
			genCoords[3] = new Point3f(xMin,	y,	zMax);

			QuadArray genSquare = new QuadArray(4, QuadArray.COORDINATES);
			genSquare.setCoordinates(0, genCoords);

			currentGeometries.add(genSquare);
		}
	}
	///}}}

	///{{{  Generates geometry along Z
	private void generateReverseGeometryZ(int num,float xMin,float xMax, float yMin,float yMax, float zMin, float zMax) {

		float gridSpacing = (float)1/(float)num;
		for (float z = zMin; z < zMax; z+=gridSpacing) {

			Point3f[] genCoords = new Point3f[4];
			genCoords[0] = new Point3f(xMin,	yMin,	z);
			genCoords[1] = new Point3f(xMax,	yMin,	z);
			genCoords[2] = new Point3f(xMax,	yMax,	z);
			genCoords[3] = new Point3f(xMin,	yMax,	z);
			QuadArray genSquare = new QuadArray(4, QuadArray.COORDINATES);
			genSquare.setCoordinates(0, genCoords);
			currentGeometries.add(genSquare);
		}
	}
	///}}}

}

