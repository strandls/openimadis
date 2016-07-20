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
import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;


public class RotationController {
	private int MaxIncrementTime = 7000;
	private int MinIncrementTime = 1000;
	private int defaultIncrementTime = MaxIncrementTime;

	private BranchGroup rotatorRoot = null; // holds the rotatorObject
	private TransformGroup rotationTransform = null; // holds the content and rotator objects
	private Alpha rotationAlpha = null;
	private RotationInterpolator rotator = null;
	private BranchGroup baseGroup = null;
	private Vector3d defaultAxis;
	private boolean AlphaSpeedChanged = false;
	private int ChangedDuration = defaultIncrementTime;

	public RotationController()
	{
		//do nothing
	}

	public void initialize(Vector3d defltAxis)
	{
		rotationTransform = new TransformGroup();
		rotationTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotationTransform.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		rotationTransform.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		baseGroup = new BranchGroup();
		baseGroup.addChild(rotationTransform);
		this.defaultAxis = new Vector3d(defltAxis);

		initialiseRotators(defltAxis);

	}
	public BranchGroup getBranchGroup()
	{
		return baseGroup;
	}

	public TransformGroup getTransformGroup()
	{
		return rotationTransform;
	}

	public void addBranchGroup(BranchGroup bg)
	{
		rotationTransform.addChild(bg);
	}

	///{{{initialiseRotators
	public void initialiseRotators(Vector3d currentAxis)
	{
		rotationAlpha = new Alpha(-1,defaultIncrementTime);
		changeActiveAxis(currentAxis);
	}
	///}}}

	///{{{ is RotationOn
	public boolean isRotationOn()
	{
		return !(rotationAlpha.isPaused());
	}
	///}}}

	///{{{ Start rotation
	public boolean startRotation()
	{
		if(rotationAlpha.isPaused())
		{
			if(AlphaSpeedChanged)
			{
				rotationAlpha.setIncreasingAlphaDuration(ChangedDuration);
				AlphaSpeedChanged = false;
			}
			rotationAlpha.resume();
		}
		return true;
	}
	///}}}

	///{{{ Stop rotation
	public boolean pauseRotation()
	{
		if(!rotationAlpha.isPaused())
		{
			rotationAlpha.pause();
		}
		return true;
	}
	///}}}

	///{{{changeActiveAxis
	public void changeActiveAxis(Vector3d rotationVector)
	{
		boolean rotationPaused = false;
		if(rotatorRoot!=null)
		{
			rotationPaused = rotationAlpha.isPaused();
			pauseRotation();
			rotationTransform.removeChild(rotatorRoot);
		}
		Transform3D axisTransform = new Transform3D();
		axisTransform.setEuler(rotationVector);

		//work around to reset alpha to zero
		long alphaTime = rotationAlpha.getIncreasingAlphaDuration();
		rotationAlpha.setIncreasingAlphaDuration(alphaTime);
		rotator = new RotationInterpolator(rotationAlpha, rotationTransform, axisTransform, 0.0f, (float) (2.0f *Math.PI));
		rotatorRoot = new BranchGroup();
		rotatorRoot.setCapability(BranchGroup.ALLOW_DETACH);
		BoundingSphere bounds = new BoundingSphere();
		rotator.setSchedulingBounds(bounds);
		rotatorRoot.addChild(rotator);

		rotationTransform.addChild(rotatorRoot);

		if(!rotationPaused)
		{
			startRotation();
		}
	}
	///}}}

	///{{{ set Alpha speed
	public void setRotationSpeed(int newSpeed)
	{
		boolean paused = rotationAlpha.isPaused();
		pauseRotation();
		if(!paused)
		{
			rotationAlpha.setIncreasingAlphaDuration(newSpeed);
			startRotation();
		}
		else
		{
			ChangedDuration =  newSpeed;
			AlphaSpeedChanged = true;
		}
	}
	///}}}

	///{{{ getRotationspeed
	public int getRotationSpeed()
	{
		return  (int)rotationAlpha.getIncreasingAlphaDuration();
	}
	///}}}

	///{{{ resetRotator
	public void resetRotator()
	{
		setRotationSpeed(defaultIncrementTime);
		changeActiveAxis(defaultAxis);
	}
	///}}}

	///{{{getMaxIncrementTime
	public int getMaximumIncrementTime()
	{
		return MaxIncrementTime;
	}
	///}}}

	///{{{getMinIncrementTime
	public int getMinimumIncrementTime()
	{
		return MinIncrementTime;
	}
	///}}}

public void clearup()
{
	rotatorRoot = null; 
	rotationTransform = null; 
	rotationAlpha = null;
	rotator = null;
	baseGroup = null;
}

}

