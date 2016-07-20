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
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JRootPane;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.ithreedview.dataobjects.Volume3DData;
import com.strandgenomics.imaging.ithreedview.listeners.IImageCreatedListener;
import com.strandgenomics.imaging.ithreedview.slicing.SliceConfigurer;
import com.strandgenomics.imaging.ithreedview.slicing.SlicingPlane;
import com.sun.j3d.utils.geometry.Box;


public class ContentController {

	float xTrans=0.5f;
	float yTrans =0.5f;
	float zTrans = 0.5f;
	public static final Bounds INFINITE_BOUNDS = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
	private boolean defaultCoordinate =  false;
	private boolean Coordinate_ON =  defaultCoordinate;

	ShapedTexture3D st3d = null;
	Volume3DData vol3d = null;
	SlicingPlane slicer = null;

	private TransformGroup contentTransform=null; // holds transform that is used to view content and coordinate system

	private BranchGroup contentRoot=null; // holds the 3d texture mapped data
	private BranchGroup coordinateRoot=null; //holds the coordinate system
	private BranchGroup sliceCoordinateRoot=null; //holds the coordinate system

	///{{{ Constructor
	public ContentController()
	{
		st3d = new ShapedTexture3D();
		vol3d = new Volume3DData();
		
	}
	///}}}

	///{{{ initialize
	public void initialize()
	{
		contentRoot = new BranchGroup();
		contentRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		contentRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		contentRoot.setCapability(BranchGroup.ALLOW_DETACH);

		Background background = new Background();
		background.setApplicationBounds(INFINITE_BOUNDS);
		background.setColor(0,0,0);
		contentRoot.addChild(background);
		Transform3D tr = new Transform3D();
		xTrans = -0.5f* (float)vol3d.xDim/(float)vol3d.maxDim;
		yTrans = -0.5f* (float)vol3d.yDim/(float)vol3d.maxDim;
		zTrans = -0.5f* ((float)st3d.getCurrentScale()*(float)vol3d.scaledZDimension)/(float)vol3d.maxDim;
		tr.setTranslation(new Vector3f(xTrans, yTrans, zTrans));
		contentTransform = new TransformGroup(tr);
		slicer = null;
		slicer = new SlicingPlane(xTrans,yTrans,zTrans,tr);

		contentTransform.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		contentTransform.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		contentTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		contentTransform.addChild(st3d);
		//contentTransform.addChild(new ColorCube(0.4));
		contentRoot.addChild(contentTransform);
		contentRoot.addChild(slicer.getBranchRoot());

		if(defaultCoordinate)
		{
			addCoordinates();
		}
	}
	///}}}

	///{{{getBranchGroup
	public BranchGroup getBranchGroup()
	{
		return contentRoot;
	}
	///}}}

	public void clearCoordinateData()
	{
	coordinateRoot.removeAllChildren();
	coordinateRoot = null;

	}

	///{{{addCoordinates
	public void addCoordinates()
	{
		if(!Coordinate_ON)
		{
			Appearance app = new Appearance();
			PolygonAttributes polyAttr = new PolygonAttributes();
			polyAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
			app.setPolygonAttributes(polyAttr);

			Transform3D tr1 = new Transform3D();
			tr1.setTranslation(new Vector3f(-xTrans, -yTrans, -zTrans));
			TransformGroup tg1 = new TransformGroup(tr1);
			tg1.addChild(new Box(-xTrans,-yTrans, -zTrans,app));

			Transform3D t3d = new Transform3D();
			contentTransform.getTransform(t3d);
			TransformGroup newtg = new TransformGroup(t3d);
			newtg.addChild(tg1);

			coordinateRoot = new BranchGroup();
			coordinateRoot.setCapability(BranchGroup.ALLOW_DETACH);
			coordinateRoot.addChild(newtg);
			Coordinate_ON = true;
			contentRoot.addChild(coordinateRoot);
		}
	}
	///}}}

	///{{{removeCoordinates
	public void removeCoordinates()
	{
		if(Coordinate_ON)
		{
			contentRoot.removeChild(coordinateRoot);
			clearCoordinateData();
		}
		Coordinate_ON = false;
	}
	///}}}

	///{{{ are coordinates present?
	public boolean coordinatePresent()
	{
		return Coordinate_ON;
	}
	///}}}

	///{{{resetContent
	public void resetContent(JRootPane dialog)
	{
		//		if (defaultCoordinate && !Coordinate_ON)
		//		{
		//			addCoordinates();
		//		}
		//		if(!defaultCoordinate && Coordinate_ON)
		//		{
		//			removeCoordinates();
		//		}
		//		if(st3d!=null)
		//		{
		//			//thresholdVolume(0);
		//			st3d.resetGeometry();
		//			st3d.resetAppearence();
		//		}

//		ResetWorker r = new ResetWorker(dialog);
//		r.run();
//		dialog.setVisible(true);
		
		if (defaultCoordinate && !Coordinate_ON)
		{
			addCoordinates();
		}
		if(!defaultCoordinate && Coordinate_ON)
		{
			removeCoordinates();
		}
		if(st3d!=null)
		{
			//thresholdVolume(0);
			st3d.resetGeometry();
			st3d.resetAppearence();
			updateView();
		}
	}

	public void setData(IRecord record, int site, int frame,
			List<IChannel> channel, float scale, int threshold, int brightness) {

		vol3d.setData(record, site, frame, channel, scale);
		st3d.setData(vol3d, threshold, brightness, scale);
	}

	public void clearData()
	{
		if(st3d!=null)
		{
			st3d.clearData(); 
		}
		st3d = null;
	}
	///}}}

	///{{{getXDimension
	public int getXDimension()
	{
		return vol3d.xDim;
	}
	///}}}

	///{{{getYDimension
	public int getYDimension()
	{
		return vol3d.yDim;
	}
	///}}}

	///{{{thresholdVolume
	public void thresholdVolume(int threshold, JRootPane dialog)
	{
		//		st3d.thresholdVolume(threshold);

//		ThresholdWorker t = new ThresholdWorker(dialog, threshold);
//		t.run();
//		dialog.setVisible(true);
		st3d.thresholdVolume(threshold);
	}
	///}}}

	public void addImageCreatedListener(IImageCreatedListener listener) {
		vol3d.addImageCreatedListener(listener);
	}

	public void removeImageCreatedListener(IImageCreatedListener listener) {
		vol3d.removeImageCreatedListener(listener);
	}

	public void sliceVolume(SliceConfigurer.SlicePlane slicePlane, float slicePercentage, SliceConfigurer.SliceView sliceView)
	{
		st3d.createSliceGeometry(slicePlane,  slicePercentage,sliceView);	
	}

	public void sliceAll(float percent1, float percent2, float percent3, SliceConfigurer.SliceView[]sliceAllView)
	{
	
		st3d.createSliceGeometry(percent1,percent2,percent3,sliceAllView);

		//removeCoordinates();
		slicer.removeCoordinates();
	}

	public void sliceVolume(SliceConfigurer.SliceView sliceView)
	{

		if(slicer.slicePlane==SliceConfigurer.SlicePlane.XZPLANE )
		{
			st3d.createSliceGeometry(slicer.slicePlane, 1.0f-slicer.slicePercent, sliceView);	
		}
		else
		{
		
			st3d.createSliceGeometry(slicer.slicePlane, slicer.slicePercent, sliceView);	
		}
		//removeCoordinates();

		slicer.removeCoordinates();

	}

	public void resetVolume()
	{
		st3d.createSliceGeometry(slicer.slicePlane, 1.0f, SliceConfigurer.SliceView.FRONTAL);
		slicer.removeCoordinates();
	}

	public void displaySlicePlane(SliceConfigurer.SlicePlane slicePlane, float slicePercentage, float slicePercentage2, float slicePercentage3)
	{
//		addCoordinates();
		slicer.setSlicePlane(slicePlane);
		float percent = 0.5f;
		if(slicePlane== SliceConfigurer.SlicePlane.YZPLANE) {percent = slicePercentage;}
		if(slicePlane== SliceConfigurer.SlicePlane.XZPLANE) {percent = slicePercentage2;}
		if(slicePlane== SliceConfigurer.SlicePlane.XYPLANE) {percent = slicePercentage3;}
		slicer.setSlicePercent(percent);
		slicer.updateBranchGroup(slicePercentage, slicePercentage2, slicePercentage3);
	}

	public float getAutoScale(IRecord record, int site, int frame, List<IChannel> channel)
	{
		return 	vol3d.getAutoScale(record, site, frame, channel)	;
	}

	public void clearup()
	{
		slicer.cleanup();
		slicer = null;
		contentTransform=null; 
		contentRoot=null; 
		coordinateRoot=null; 
	}

	public void setBrightness(int brightness, JRootPane dialog)
	{
		//		st3d.setBrightness(brightness);

//		BrightnessWorker w = new BrightnessWorker(dialog, st3d, brightness);
//		w.run();
//		dialog.setVisible(true);
		
		st3d.setBrightness(brightness);
	}

	public int getCurrentBrightness()
	{
		return st3d.getCurrentBrightness();
	}

//	private class BrightnessWorker extends UIWorker {	
//
//		ShapedTexture3D instance;
//		int brightness; 
//
//		public BrightnessWorker(JDialog frame, ShapedTexture3D instance, int brightness){
//
//			super(frame, false, true); // parent, isIndeterminate, disableCancel
//			this.instance = instance;
//			this.brightness = brightness;
//		}
//		public void task(){
//			progressDialog.setTitle("");
//			progressModel.setMessage("Please wait ... setting brightness");
//			progressModel.setProgressValue(0);
//			if (progressModel.canContinue()) {
//				instance.setBrightness(brightness);
//			}
//		}
//	}

//	private class ResetWorker  extends UIWorker {	
//
//		public ResetWorker(JDialog frame){
//			super(frame, false, true); // parent, isIndeterminate, disableCancel
//		}
//		public void task(){
//			progressDialog.setTitle("");
//			progressModel.setMessage("Please wait ... reset in progress");
//			progressModel.setProgressValue(0);
//			if (progressModel.canContinue()) {
//
//				if (defaultCoordinate && !Coordinate_ON)
//				{
//					addCoordinates();
//				}
//				if(!defaultCoordinate && Coordinate_ON)
//				{
//					removeCoordinates();
//				}
//				if(st3d!=null)
//				{
//					//thresholdVolume(0);
//					st3d.resetGeometry();
//					st3d.resetAppearence();
//					updateView();
//				}
//
//			}
//		}
//	}

//	private class ThresholdWorker extends UIWorker {	
//
//		int threshold;
//		public ThresholdWorker(JDialog frame, int threshold){
//			super(frame, false, true); // parent, isIndeterminate, disableCancel
//			this.threshold = threshold;
//		}
//		public void task(){
//			progressDialog.setTitle("");
//			progressModel.setMessage("Please wait .. thresholding in progress");
//			progressModel.setProgressValue(0);
//			if (progressModel.canContinue()) {
//				st3d.thresholdVolume(threshold);
//			}
//		}
//	}

	public ShapedTexture3D get3DVolume()
	{
		return st3d;
	}

	public Volume3DData getVolumeData()
	{
		return vol3d;
	}


	public void updateView()
	{
	
		Transform3D tr = new Transform3D();
		xTrans = -0.5f* (float)vol3d.xDim/(float)vol3d.maxDim;
		yTrans = -0.5f* (float)vol3d.yDim/(float)vol3d.maxDim;
		zTrans = -0.5f* ((float)st3d.getCurrentScale()*(float)vol3d.scaledZDimension)/(float)vol3d.maxDim;

		tr.setTranslation(new Vector3f(xTrans, yTrans, zTrans));

		contentTransform.setTransform(tr);
		slicer.setContentTransform(tr);
		if(Coordinate_ON)
		{
			removeCoordinates();
			addCoordinates();
		}

	}

	public void setZAspect(float scale)
	{
		st3d.setZAspect(scale);
		updateView();
		slicer.updateBranch(xTrans,yTrans,zTrans);
	}

	public int getCurrentThreshold()
	{
		return st3d.getCurrentThreshold();
	}
	public void resetGeometries()
	{
		st3d.resetGeometry();
	}
}

