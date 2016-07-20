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

package com.strandgenomics.imaging.ithreedview;

import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;

import com.strandgenomics.imaging.ithreedview.dataobjects.Volume3DData;
import com.strandgenomics.imaging.ithreedview.makers.AppearanceMaker;
import com.strandgenomics.imaging.ithreedview.makers.GeometryMaker;
import com.strandgenomics.imaging.ithreedview.makers.TextureMaker;
import com.strandgenomics.imaging.ithreedview.slicing.SliceConfigurer;


public class ShapedTexture3D extends Shape3D {

	private Volume3DData vol3d = null; // holds the data
	private TextureMaker texture = null; // holds and manupulates the texture
											// map
	private GeometryMaker geometry = null;// hold and manipulates the geometry
	private AppearanceMaker appearance = null; // holds and manipulates the
												// appearance

	private float currentScale;
	private SliceConfigurer.SlicePlane slicePlane = SliceConfigurer.SlicePlane.YZPLANE;
	private float slicePercent = 1;

	// /{{{ default constructor
	public ShapedTexture3D() {
		this.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		this.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		this.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		this.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
	}

	// /}}}

	// /{{{ Set up the properties of texture
	private void prepareAppearance(float xScale, float yScale, float zScale) {

		/*
		 * String systemOS = System.getProperty("os.name");
		 * if(systemOS.contains("Windows")) { zScale = zScale/2.0f; }
		 */
		Appearance a = appearance.getAppearance(xScale, yScale, zScale);
		this.setAppearance(a);

	}

	// /}}}

	// /{{{ set data from record ..
	public void setData(Volume3DData vol3d, int threshold, int brightness,
			float scale) {
		this.vol3d = vol3d;

		float zScale = 1.0f / scale * (float) vol3d.maxDim
				/ (float) vol3d.scaledZDimension;
		float xScale = (float) vol3d.maxDim / (float) vol3d.xDim;
		float yScale = (float) vol3d.maxDim / (float) vol3d.yDim;
		this.currentScale = scale;
		appearance = new AppearanceMaker();
		prepareAppearance(xScale, yScale, zScale);

		setShape(threshold, brightness);
	}

	// /}}}

	// /{{{ Set up the view with 3D ..
	public void setShape(int threshold, int brightness) {
		texture = new TextureMaker();
		geometry = new GeometryMaker();

		modifyAppearence(threshold, brightness);
		// TODO: Convert to the intensity array ..
		this.removeAllGeometries();

		System.out.println("Current scale::" + currentScale);
		float newscale = currentScale * (float) vol3d.scaledZDimension
				/ (float) vol3d.maxDim;
		addGeometries(geometry.generateGeometries(vol3d.maxDim, 1.0f, 1.0f,
				newscale));
	}

	// /}}}

	// /{{{
	public void clearData() {
		removeAllGeometries();
		vol3d.clearData();
		texture.cleanup();
	}

	// /}}}

	// /{{{threshold the display at a particular point
	public void thresholdVolume(int threshold) {
		modifyAppearence(threshold, texture.currentBrightness);
	}

	// }}}
	private void setValues(SliceConfigurer.SliceView sv, float[] vals,
			float percent) {
		if (sv == SliceConfigurer.SliceView.FRONTAL) {
			vals[0] = 0.0f;
			vals[1] = percent;
		}
		if (sv == SliceConfigurer.SliceView.REVERSE) {
			vals[0] = percent;
			vals[1] = 1.0f;
		}
	}

	public void createSliceGeometry(float percent1, float percent2,
			float percent3, SliceConfigurer.SliceView[] sliceAllView) {

		this.removeAllGeometries();

		int maxDim = vol3d.getMaxDimension();

		float newscale = currentScale * (float) vol3d.scaledZDimension
				/ (float) vol3d.maxDim;
		float[] xVal = new float[2];
		float[] yVal = new float[2];
		float[] zVal = new float[2];

		setValues(sliceAllView[0], xVal, percent1);
		setValues(sliceAllView[1], yVal, percent2);
		setValues(sliceAllView[2], zVal, percent3 * newscale);

		addGeometries(geometry.generateGeometries(maxDim, xVal[1], yVal[1],
				zVal[1] * newscale, xVal[0], yVal[0], zVal[0]));
	}

	// /}}}

	// /{{{ slicegeometry
	public void createSliceGeometry(SliceConfigurer.SlicePlane slicePlane,
			float slicePercent, SliceConfigurer.SliceView sliceView) {
		this.slicePlane = slicePlane;
		this.slicePercent = slicePercent;
		this.removeAllGeometries();

		int maxDim = vol3d.getMaxDimension();

		float newscale = currentScale * (float) vol3d.scaledZDimension
				/ (float) vol3d.maxDim;
		if (slicePlane == SliceConfigurer.SlicePlane.YZPLANE) {
			if (sliceView == SliceConfigurer.SliceView.FRONTAL) {
				addGeometries(geometry.generateGeometries(maxDim, slicePercent,
						1.0f, newscale));
			}
			if (sliceView == SliceConfigurer.SliceView.REVERSE) {
				addGeometries(geometry.generateGeometries(maxDim, 1.0f, 1.0f,
						newscale, slicePercent, 0.0f, 0.0f));
			}

		}
		if (slicePlane == SliceConfigurer.SlicePlane.XZPLANE) {
			if (sliceView == SliceConfigurer.SliceView.FRONTAL) {
				addGeometries(geometry.generateGeometries(maxDim, 1.0f,
						slicePercent, newscale));
			}
			if (sliceView == SliceConfigurer.SliceView.REVERSE) {
				addGeometries(geometry.generateGeometries(maxDim, 1.0f, 1.0f,
						newscale, 0.0f, slicePercent, 0.0f));
			}

		}
		if (slicePlane == SliceConfigurer.SlicePlane.XYPLANE) {
			if (sliceView == SliceConfigurer.SliceView.FRONTAL) {
				addGeometries(geometry.generateGeometries(maxDim, 1.0f, 1.0f,
						slicePercent * newscale));
			}
			if (sliceView == SliceConfigurer.SliceView.REVERSE) {
				addGeometries(geometry.generateGeometries(maxDim, 1.0f, 1.0f,
						newscale, 0.0f, 0.0f, slicePercent * newscale));
			}

		}
	}

	// /}}}

	// /{{{ get brightness
	public int getCurrentBrightness() {
		return texture.currentBrightness;
	}

	// /}}}

	// /{{{ set brightness
	public void setBrightness(int brightness) {
		modifyAppearence(texture.currentThreshold, brightness);
	}

	// /}}}

	// /{{{ reset appearance
	public void resetAppearence() {

		modifyAppearence(texture.defaultThreshold, texture.defaultBrightness);
	}

	// /}}}

	// /{{{ reset geometry
	public void resetGeometry() {
		currentScale = 1.0f;
		setZAspect(1.0f);
	}

	// /}}}

	// /{{{ modify appearance
	public void modifyAppearence(int threshold, int brightness) {
		texture.generateTexture3D(brightness, threshold, vol3d);
		this.getAppearance().setTexture(texture.getLoadedTexture());
	}

	// /}}}

	// /{{{ get threhsold
	public int getCurrentThreshold() {
		return texture.currentThreshold;
	}

	// /}}}

	// /{{{ set Z aspect
	public void setZAspect(float scale) {
		this.currentScale = scale * vol3d.getAutoScale();
		float zScale = 1.0f / currentScale
				* ((float) vol3d.maxDim / (float) vol3d.scaledZDimension);
		Appearance currentAppearance = this.getAppearance();
		currentAppearance = null; // set this to null

		float xScale = (float) vol3d.maxDim / (float) vol3d.xDim;
		float yScale = (float) vol3d.maxDim / (float) vol3d.yDim;
		prepareAppearance(xScale, yScale, zScale);
		this.getAppearance().setTexture(texture.getLoadedTexture());
		this.removeAllGeometries();
		float newscale = currentScale * (float) vol3d.scaledZDimension
				/ (float) vol3d.maxDim;

		addGeometries(geometry.generateGeometries(vol3d.maxDim, 1.0f, 1.0f,
				newscale));
	}

	// /}}}

	// /{{{getCurrentScale
	public float getCurrentScale() {
		return currentScale;
	}// /}}}

	// /{{{getcurrentsliceplane
	public SliceConfigurer.SlicePlane getCurrentSlicePlane() {
		return slicePlane;
	}

	// /}}}

	// /{{{getCurrentslicepercent
	public float getCurrentSlicePercent() {
		return slicePercent;
	}

	// /}}}

	// /{{{addgeometries
	private void addGeometries(ArrayList<QuadArray> geomList) {
		for (int i = 0; i < geomList.size(); i++) {
			this.addGeometry(geomList.get(i));
		}
	}
	// /}}}

}
