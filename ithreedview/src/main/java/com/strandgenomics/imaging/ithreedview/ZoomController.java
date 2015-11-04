package com.strandgenomics.imaging.ithreedview;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;


public class ZoomController {

	private float defaultZoom = 0f;
	private float maxZoom = 0.75f;
	private float minZoom = -4f;

	private TransformGroup zoomTransform = null; // holds the zoom transform
	private BranchGroup zoomBranch = null;

	public ZoomController()
	{
		// do nothing
	}

	///{{{initialize
	public void initialize()
	{
		//set up default zoom transform	
		Transform3D zoom3D = new Transform3D();
		zoom3D.setTranslation(new Vector3f(0f,0f,defaultZoom));
		zoomTransform = new TransformGroup(zoom3D);
		zoomTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		zoomBranch = new BranchGroup();
		zoomBranch.setCapability(BranchGroup.ALLOW_DETACH);
		zoomBranch.addChild(zoomTransform);
	}
	///}}}

	///{{{getTransformGroup
	public TransformGroup getTransformGroup()
	{
		return zoomTransform;
	}
	///}}}

	public BranchGroup getBranchGroup()
	{
		return zoomBranch;
	}

	///{{{set zoom
	public void setZoomParameter(float zoomParam, Transform3D currTransform)
	{
		Vector3f origVector = new Vector3f(0f,0f,zoomParam);
		currTransform.transform(origVector);
		Transform3D zoom3D= new Transform3D();
		zoom3D.setTranslation(origVector);
		zoomTransform.setTransform(zoom3D);

	}
	///}}}

	///{{{getMaxZoom
	public float getMaxZoom()
	{
		return maxZoom;
	}
	///}}}

	///{{{getMinZoom
	public float getMinZoom()
	{
		return minZoom;
	}
	///}}}

	///{{{ getCurrentZoom
	public float getCurrentZoom(Transform3D currTransform)
	{
		Transform3D zoom3D =  new Transform3D();
		zoomTransform.getTransform(zoom3D);	
		Vector3f vect = new Vector3f();
		zoom3D.get(vect);
		Transform3D inverse  = new Transform3D();
		inverse.invert(currTransform);
		inverse.transform(vect);
		return vect.z;
	}
	///}}}

	///{{{resetZoom
	public void resetZoom()
	{
		setZoomParameter(defaultZoom, new Transform3D());
	}
	///}}}

public void clearup()
{
	zoomTransform = null;
}

}
