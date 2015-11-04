package com.strandgenomics.imaging.ithreedview.slicing;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;


public class SlicingPlane
{
	private static enum Status {ACTIVE,INACTIVE};
	private Box yzBox;
	private	Box xzBox;
	private	Box xyBox;

	private SliceConfigurer.SlicePlane defaultPlane = SliceConfigurer.SlicePlane.YZPLANE;
	private float defaultPercent = 0.5f;
	public float slicePercent;
	public SliceConfigurer.SlicePlane slicePlane;

	private float xDim ;
	private float yDim ;
	private float zDim ;

	private BranchGroup yzBg;
	private BranchGroup xzBg;
	private BranchGroup xyBg;

	private TransformGroup yzTg;
	private TransformGroup xzTg;
	private TransformGroup xyTg;

	private Transform3D overAllTransform;

	private TransformGroup yzContentTransform;
	private TransformGroup xzContentTransform;
	private TransformGroup xyContentTransform;

	private Transform3D yzTr;
	private Transform3D xzTr;
	private Transform3D xyTr;

	private BranchGroup overallBG = null;
	private BranchGroup tempyzBG;
	private BranchGroup tempxzBG;
	private BranchGroup tempxyBG;


	public SlicingPlane(float xDim,float yDim,float zDim, Transform3D transform)
	{
		// do nothing??
		this.slicePlane = defaultPlane;
		this.slicePercent = defaultPercent;
		this.xDim = xDim;
		this.yDim = yDim;
		this.zDim = zDim;

		yzContentTransform = new TransformGroup();
		xzContentTransform = new TransformGroup();
		xyContentTransform = new TransformGroup();

		yzContentTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		xzContentTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		xyContentTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setContentTransform(transform);
		createBoxes();
		createBranchGroups();
		overallBG = new BranchGroup();
		overallBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		overallBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		overallBG.setCapability(BranchGroup.ALLOW_DETACH);
	}


	public void setContentTransform(Transform3D transform)
	{
		overAllTransform = transform;
		yzContentTransform.setTransform(overAllTransform);
		xzContentTransform.setTransform(overAllTransform);
		xyContentTransform.setTransform(overAllTransform);
	}

	public void setSlicePlane(SliceConfigurer.SlicePlane slicePlane)
	{
		this.slicePlane= slicePlane;
	}

	public void setSlicePercent(float slicePercent)
	{
		this.slicePercent= slicePercent;
	}


	public void createBoxes()
	{
		createBox(SliceConfigurer.SlicePlane.YZPLANE,0.00001f,-yDim, -zDim);
		createBox(SliceConfigurer.SlicePlane.XZPLANE,-xDim,-0.00001f, -zDim);
		createBox(SliceConfigurer.SlicePlane.XYPLANE,-xDim,-yDim, -0.00001f);
	}
	public void createBox(SliceConfigurer.SlicePlane slicePlane, float xVal, float yVal, float zVal)
	{
		//return the current box at the right location and stuff

		Appearance app = new Appearance();
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttr);
		ColoringAttributes colAttr = new ColoringAttributes();
		colAttr.setColor(1.0f,1.0f,0.0f); //red color
		app.setColoringAttributes(colAttr);

		if(slicePlane==SliceConfigurer.SlicePlane.YZPLANE){yzBox = new Box(xVal, yVal,zVal,app);}
		if(slicePlane==SliceConfigurer.SlicePlane.XZPLANE){xzBox = new Box(xVal, yVal,zVal,app);}
		if(slicePlane==SliceConfigurer.SlicePlane.XYPLANE){xyBox = new Box(xVal, yVal,zVal,app);}

	}



	public Box getCurrentBox()
	{
		if (slicePlane == SliceConfigurer.SlicePlane.YZPLANE) {


			return yzBox;}
		if (slicePlane == SliceConfigurer.SlicePlane.XZPLANE){
			return xzBox;}
		if (slicePlane == SliceConfigurer.SlicePlane.XYPLANE) {
			return xyBox;}
		return null;
	}

	public void createBranchGroups()
	{
		createBranchGroup(SliceConfigurer.SlicePlane.YZPLANE);
		createBranchGroup(SliceConfigurer.SlicePlane.XZPLANE);
		createBranchGroup(SliceConfigurer.SlicePlane.XYPLANE);
	}
	public void createBranchGroup(SliceConfigurer.SlicePlane slicePlane){

		if(slicePlane==SliceConfigurer.SlicePlane.YZPLANE)
		{
			yzTr = new Transform3D();
			yzTr.setTranslation(new Vector3f(-xDim, -yDim, -zDim));
			yzTg = new TransformGroup(yzTr);
			yzTg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
			yzTg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

			tempyzBG = new BranchGroup();
			tempyzBG.setCapability(BranchGroup.ALLOW_DETACH);
			tempyzBG.addChild(yzBox);
			yzTg.addChild(tempyzBG);
			yzContentTransform.addChild(yzTg);
			yzBg = new BranchGroup();
			yzBg.setCapability(BranchGroup.ALLOW_DETACH);
			yzBg.addChild(yzContentTransform);
		}
		if(slicePlane==SliceConfigurer.SlicePlane.XZPLANE)
		{
			xzTr = new Transform3D();
			xzTr.setTranslation(new Vector3f(-xDim, -yDim, -zDim));
			xzTg = new TransformGroup(xzTr);
			xzTg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
			xzTg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

			tempxzBG = new BranchGroup();
			tempxzBG.setCapability(BranchGroup.ALLOW_DETACH);
			tempxzBG.addChild(xzBox);

			xzTg.addChild(tempxzBG);
			xzContentTransform.addChild(xzTg);
			xzBg = new BranchGroup();
			xzBg.setCapability(BranchGroup.ALLOW_DETACH);
			xzBg.addChild(xzContentTransform);
		}
		if(slicePlane==SliceConfigurer.SlicePlane.XYPLANE)
		{
			xyTr = new Transform3D();
			xyTr.setTranslation(new Vector3f(-xDim, -yDim, -zDim));
			xyTg = new TransformGroup(xyTr);
			xyTg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
			xyTg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

			tempxyBG = new BranchGroup();
			tempxyBG.setCapability(BranchGroup.ALLOW_DETACH);
			tempxyBG.addChild(xyBox);

			xyTg.addChild(tempxyBG);
			xyContentTransform.addChild(xyTg);
			xyBg = new BranchGroup();
			xyBg.setCapability(BranchGroup.ALLOW_DETACH);
			xyBg.addChild(xyContentTransform);
		}
	}

	public void updateBranch(float xTrans, float yTrans, float zTrans)
	{
		yzTg.removeAllChildren();
		xzTg.removeAllChildren();
		xyTg.removeAllChildren();
		tempxzBG.removeChild(xzBox);
		tempxyBG.removeChild(xyBox);
		tempyzBG.removeChild(yzBox);

		yzBox = null;
		xzBox = null;
		xyBox = null;

		this.xDim = xTrans;
		this.yDim = yTrans;
		this.zDim = zTrans;

		createBoxes();
		tempxzBG.addChild(xzBox);
		tempyzBG.addChild(yzBox);
		tempxyBG.addChild(xyBox);
		yzTg.addChild(tempyzBG);
		xzTg.addChild(tempxzBG);
		xyTg.addChild(tempxyBG);

	}

	public void updateBranchGroup(float slicePercent1, float slicePercent2,float slicePercent3)
	{
		overallBG.removeAllChildren();

		if(slicePlane==SliceConfigurer.SlicePlane.YZPLANE)
		{
			setColoringAttr(yzBox,Status.ACTIVE);
			setColoringAttr(xzBox,Status.INACTIVE);
			setColoringAttr(xyBox,Status.INACTIVE);
		}
		if(slicePlane==SliceConfigurer.SlicePlane.XZPLANE)
		{
			setColoringAttr(yzBox,Status.INACTIVE);
			setColoringAttr(xzBox,Status.ACTIVE);
			setColoringAttr(xyBox,Status.INACTIVE);
		}
		if(slicePlane==SliceConfigurer.SlicePlane.XYPLANE)
		{
			setColoringAttr(yzBox,Status.INACTIVE);
			setColoringAttr(xzBox,Status.INACTIVE);
			setColoringAttr(xyBox,Status.ACTIVE);
		}



		float sliceVal = slicePercent1*2.0f*xDim -xDim;
		yzTr.setTranslation(new Vector3f(-xDim-(sliceVal),-yDim,-zDim));
		yzTg.setTransform(yzTr);
		overallBG.addChild(yzBg);

		sliceVal = slicePercent2*2.0f*yDim -yDim;

		xzTr.setTranslation(new Vector3f(-xDim,-yDim+(sliceVal),-zDim));
		xzTg.setTransform(xzTr);
		overallBG.addChild(xzBg);

		sliceVal = slicePercent3*2.0f*zDim -zDim;
		xyTr.setTranslation(new Vector3f(-xDim,-yDim,-zDim-(sliceVal)));
		xyTg.setTransform(xyTr);
		overallBG.addChild(xyBg);
	}
	public BranchGroup getBranchRoot()
	{
		return overallBG;
	}

	public void removeCoordinates()
	{
		overallBG.removeAllChildren();
	}
	private void setColoringAttr(Box box, Status status)
	{
		if(status==Status.ACTIVE)
		{
			//PolygonAttributes polyAttr= box.getAppearance().getPolygonAttributes();

			//polyAttr.setPolygonMode(PolygonAttributes.POLYGON_FILL);

			ColoringAttributes colAttr = box.getAppearance().getColoringAttributes();
			colAttr.setColor(1.0f, 0.0f,0.0f);
		}
		else
		{

			PolygonAttributes polyAttr= box.getAppearance().getPolygonAttributes();
			polyAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);

			ColoringAttributes colAttr = box.getAppearance().getColoringAttributes();
			colAttr.setColor(1.0f,1.0f,0.0f);

		}
		//get current coloring attributes

	}


	public void cleanup()
	{

		overallBG.removeAllChildren();
		yzBg.removeAllChildren();
		xzBg.removeAllChildren();
		xyBg.removeAllChildren();

		xyContentTransform.removeAllChildren();
		yzContentTransform.removeAllChildren();
		xzContentTransform.removeAllChildren();

		xzTg.removeAllChildren();
		yzTg.removeAllChildren();
		xyTg.removeAllChildren();

		tempxyBG.removeAllChildren();
		tempxzBG.removeAllChildren();
		tempyzBG.removeAllChildren();

		yzBox=null;
		xzBox=null;
		xyBox=null;

		tempxyBG = null;
		tempxzBG = null;
		tempyzBG = null;

		yzBg=null;
		xzBg=null;
		xyBg=null;

		yzTg=null;
		xzTg=null;
		xyTg=null;

		overAllTransform=null;

		yzContentTransform=null;
		xzContentTransform=null;
		xyContentTransform=null;

		yzTr=null;
		xzTr=null;
		xyTr=null;

		overallBG = null;


	}
}
