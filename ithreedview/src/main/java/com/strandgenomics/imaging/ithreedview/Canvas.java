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

import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.filter.AVI_Writer;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IRecord;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Canvas {

	private ExtendedCanvas3D canvas;
	public static final Bounds INFINITE_BOUNDS = new BoundingSphere(
			new Point3d(), Double.MAX_VALUE);
	private IRecord record;

	public static enum AxisSet {
		XAXIS, YAXIS, ZAXIS
	};

	private Vector3d xVector = new Vector3d(Math.toRadians(90), 0, 0);
	private Vector3d yVector = new Vector3d(0, Math.toRadians(90), 0);
	private Vector3d zVector = new Vector3d(0, 0, Math.toRadians(90));

	// Default behavior
	private Vector3d defaultAxis = new Vector3d(yVector);

	private ZoomController zoom = null;
	private ContentController content = null;
	private RotationController rotator = null;
	SimpleUniverse simpleU = null;
	OrbitBehavior orbitBehavior = null;
	private BranchGroup contentBG = null;
	private BranchGroup graphBG = null;
	private TransformGroup sliceTG = null;
	private Transform3D sliceTransform = null;
	private BranchGroup sliceCoordinates = null;
	private BranchGroup sliceGroup = null;

	// /{{{ Constructor
	public Canvas(GraphicsConfiguration graphicsConfiguration) {
		try {
			canvas = new ExtendedCanvas3D(graphicsConfiguration);
			content = new ContentController();
			zoom = new ZoomController();
			rotator = new RotationController();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// /}}}

	// /{{{Initialize
	public void initialize() throws MalformedURLException {
		try {

			simpleU = new SimpleUniverse(canvas);
			// This moves the ViewPlatform back a bit so the objects in the
			// scene can be viewed.
			simpleU.getViewingPlatform().setNominalViewingTransform();

			orbitBehavior = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
			orbitBehavior.setSchedulingBounds(INFINITE_BOUNDS);
			orbitBehavior.setProportionalZoom(true);
			orbitBehavior.setRotationCenter(new Point3d(0, 0, 0));
			orbitBehavior.setTranslateEnable(false);
			orbitBehavior.setZoomEnable(false);

			simpleU.getViewingPlatform().setViewPlatformBehavior(orbitBehavior);

			canvas.getView().setFrontClipPolicy(View.VIRTUAL_EYE);
			canvas.getView().setTransparencySortingPolicy(
					View.TRANSPARENCY_SORT_GEOMETRY);

			graphBG = new BranchGroup();

			graphBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			graphBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			graphBG.setCapability(BranchGroup.ALLOW_DETACH);
			zoom.initialize();
			graphBG.addChild(zoom.getBranchGroup());
			rotator.initialize(defaultAxis);
			(zoom.getTransformGroup()).addChild(rotator.getBranchGroup());

			content.initialize();

			contentBG = new BranchGroup();
			contentBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			contentBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			contentBG.setCapability(BranchGroup.ALLOW_DETACH);
			contentBG.addChild(content.getBranchGroup());
			(rotator.getTransformGroup()).addChild(contentBG);
			simpleU.addBranchGraph(graphBG);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /}}}

	// /{{{changeActiveAxis
	public void changeActiveAxis(AxisSet changedAxis) {
		float zoomParam = getCurrentZoom();
		simpleU.getViewingPlatform().setNominalViewingTransform();
		setZoomParameter(zoomParam);
		if (changedAxis == AxisSet.XAXIS) {
			rotator.changeActiveAxis(xVector);
		}
		if (changedAxis == AxisSet.YAXIS) {
			rotator.changeActiveAxis(yVector);
		}
		if (changedAxis == AxisSet.ZAXIS) {
			rotator.changeActiveAxis(zVector);
		}
	}

	// /}}}

	// /{{{setRecordID
	public void setRecordID(IRecord record, int site, int frame,
			List<IChannel> channel, float scale, int threshold, int brightness) {
		this.record = record;
		content.setData(record, site, frame, channel, scale, threshold,
				brightness);
	}

	// /}}}

	// /{{{getRecordID
	public IRecord getRecordID() {
		return record;
	}

	// /}}}

	// /{{{getCanvas
	public ExtendedCanvas3D getCanvas() {
		return canvas;
	}

	// /}}}

	// /{{{cleanup
	public void cleanup() {

		clearData();
		content.clearup();
		content = null;
		zoom.clearup();
		zoom = null;
		rotator.clearup();
		rotator = null;
		canvas.getGraphicsContext3D().clear();
		canvas = null;
		simpleU.cleanup();
		simpleU = null;
	}

	// /}}}

	// /{{{resetView
	public void resetView(JRootPane dialog) {
		simpleU.getViewingPlatform().setNominalViewingTransform();
		content.resetContent(dialog);
		zoom.resetZoom();
		rotator.resetRotator();
	}

	// /}}}

	public void resetZoom() {
		simpleU.getViewingPlatform().setNominalViewingTransform();
		zoom.resetZoom();
	}

	// /{{{set zoom
	public void setZoomParameter(float zoomParam) {
		zoom.setZoomParameter(zoomParam, getViewTransform());

	}

	// /}}}

	// /{{{getView trasnform
	private Transform3D getViewTransform() {
		TransformGroup viewTG = simpleU.getViewingPlatform()
				.getViewPlatformTransform();
		Transform3D viewTransform = new Transform3D();
		viewTG.getTransform(viewTransform);
		return viewTransform;
	}

	// /}}}

	// /{{{ getCurrentZoom
	public float getCurrentZoom() {
		return zoom.getCurrentZoom(getViewTransform());
	}

	// /}}}

	// /{{{ clearData
	public void clearData() {
		content.clearData();
		canvas.resetStacks();
	}

	// /}}}

	// /{{{captureFrame
	public void captureFrame(JRootPane rootPane) {
		canvas.captureSingleFrame(rootPane);
		canvas.startRenderer();
	}

	// /}}}

	// /{{{ saveFrame
	public void saveFrame(File writeFile, String formatName) {
		BufferedImage capturedImage = canvas.getSnapshotImage();
		// JAI.create("filestore",capturedImage,fileName,"JPEG",null);
		try {
			ImageIO.write(capturedImage, formatName, writeFile);
		} catch (IOException e) {
			System.err.println("Unable to write 3D file");
		}
		canvas.resetStacks();
	}

	// /}}}

	// /{{{video capture functions
	public void startCapture(int maxFrameSize, int xDim, int yDim) {
		canvas.startVideoCapture(maxFrameSize, xDim, yDim);
		canvas.repaint();
	}

	// /}}}

	// /{{{stopcapture
	public ImageStack stopCapture() throws Exception {
		ImageStack videoStack = null;
		int size = canvas.stopVideoCapture();
		if (size >= 0) {
			videoStack = canvas.getCreatedStack();
		}
		return videoStack;

	}

	// /}}}

	// /{{{ saveVideo
	public void saveVideo(String aviFileURL, ImageStack videoStack) {
		ImagePlus videoPlus = new ImagePlus("3D Video Stream", videoStack);
		canvas.resetStacks();
		AVI_Writer writer = new AVI_Writer();
		try {
			writer.writeImage(videoPlus, aviFileURL,
					AVI_Writer.JPEG_COMPRESSION, 0);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// /}}}

	// /{{{cancelVideoSave
	public void cancelSave() {
		canvas.resetStacks();
	}

	// /}}}

	// /{{{
	public int getDefaultCaptureLimit() {
		return canvas.getDefaultFrameSize();
	}

	public int getDefaultxScale() {
		return canvas.getDefaultxScale();
	}

	public int getDefaultyScale() {
		return canvas.getDefaultyScale();
	}

	// /}}}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		canvas.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		canvas.removePropertyChangeListener(listener);
	}

	protected RotationController getRotationController() {
		return rotator;
	}

	protected ContentController getContentController() {
		return content;
	}

	protected ZoomController getZoomController() {
		return zoom;
	}

	public int getCurrentBrightness() {
		return content.getCurrentBrightness();
	}

	public void setBrightness(int brightness, JRootPane dialog) {
		content.setBrightness(brightness, dialog);
	}

	public boolean setDataDepth(int numSlices, int maxDim) {
		Map graphicsMap = canvas.queryProperties();

		int textureDepthMax = (Integer) graphicsMap.get("texture3DDepthMax");

		int depth = numSlices;

		String systemOS = System.getProperty("os.name");
		if (systemOS.contains("Windows")) {
			// textureDepthMax = textureDepthMax/2;// windows hack
			textureDepthMax = Math.min(maxDim, textureDepthMax);
		}

		if (textureDepthMax < depth && textureDepthMax > 0) {

			String message = "Graphics Capabilities Insufficient: DownSampling";
			JOptionPane.showMessageDialog(new JFrame(), message,
					"Graphics Capabilities Insufficient",
					JOptionPane.INFORMATION_MESSAGE);
			content.getVolumeData().setMaxDepth(textureDepthMax);
			return false;
		}
		return true;

	}

	public void setMaxDimension(int dimension, int numSlices) {
		Map graphicsMap = canvas.queryProperties();
		int textureWidthMax = (Integer) graphicsMap.get("textureWidthMax");
		int textureHeightMax = (Integer) graphicsMap.get("textureHeightMax");

		System.out.println("Graphics card capabilities:: " + textureHeightMax
				+ "\t" + textureWidthMax);
		if (textureHeightMax < dimension || textureWidthMax < dimension) {

			String message = "Graphics card does not support 3D image size. Reverting to minimum 3D image size";
			JOptionPane.showMessageDialog(new JFrame(), message,
					"Graphics Capabilities Insufficient",
					JOptionPane.WARNING_MESSAGE);

			content.getVolumeData().setMaxDimension(128, numSlices);
		} else {
			content.getVolumeData().setMaxDimension(dimension, numSlices);
		}
	}

	public void removeOrbitBehavior() {

		simpleU.getViewingPlatform().setViewPlatformBehavior(null);
	}

	public void setOrbitBehavior() {

		simpleU.getViewingPlatform().setViewPlatformBehavior(orbitBehavior);
	}

}