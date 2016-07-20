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

package com.strandgenomics.imaging.ithreedview.dataobjects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JOptionPane;

import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.ithreedview.listeners.EventMulticaster;
import com.strandgenomics.imaging.ithreedview.listeners.IImageCreatedListener;
import com.strandgenomics.imaging.ithreedview.listeners.ImageCreatedEvent;
import com.strandgenomics.imaging.ithreedview.utils.ImageUtils;

public class Volume3DData {
	private double[][][][] double3d;
	public short xDim;
	public short yDim;
	public short zDim;
	public int actualZDim;
	public short scaledZDimension;
	public short maxDim;
	private float autoZScale = 1.0f;
	// private float currscale = 1.0f;

	private double[] cal_min = new double[3];
	private double[] cal_max = new double[3];

	public int MAX_SUPPORTED_DIMENSION = 128;
	public int MAX_DEPTH = 128;

	private EventMulticaster eventMulticaster = new EventMulticaster(
			ImageCreatedEventDispatcher.INSTANCE);

	public Volume3DData() {
		// do nothing
	}

	public void setMaxDepth(int depth) {
		MAX_DEPTH = depth;
	}

	public int getMaxDepth() {
		return MAX_DEPTH;
	}

	public int getMaxSupportedDimension() {
		return MAX_SUPPORTED_DIMENSION;
	}

	public void setMaxDimension(int dimension, int numSlices) {

		// IRecordVO record = IndexAccessDelegate.getRecordVO(recordId);
		long allMemory = (Runtime.getRuntime().maxMemory()) / (1024 * 1024);
		double factor = 3.0 * Double.SIZE / (8 * 1024 * 1024);
		long dim = (long) dimension;
		// long zdim = record.getNumSlices();
		long zdim = numSlices;
		long neededMemory = (long) (factor * (dim * dim * zdim));
		System.out.println("Avialable Memory::" + allMemory
				+ "\tneededMemory::" + neededMemory);
		float frac = neededMemory / allMemory;
		if (frac < 0.7) {
			this.MAX_SUPPORTED_DIMENSION = dimension;
			System.out.println("MAX_SUPPORTED_DIMENSION::"
					+ this.MAX_SUPPORTED_DIMENSION);
		} else {
			String message = "Available Memory Insufficient, Reverting to minimum 3D image size";
			JOptionPane.showMessageDialog(null, message,
					"Memory Insufficient Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void setData(IRecord record, int site, int frame, List<IChannel> channel, float scale)
	{
		double3d = null;
		try 
		{
			init(record, site, frame, channel, scale);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void init(IRecord record, int site, int frame,
			List<IChannel> channel, float scale)
			throws Exception {

		short[] dimensions = getDimensions(record.getImageHeight(),
				record.getImageWidth(), record.getSliceCount() + 2);
		yDim = (short) dimensions[0];
		xDim = (short) dimensions[1];
		zDim = (short) dimensions[2];
		dimensions = null;
		actualZDim = Math.min(MAX_DEPTH - 2, record.getSliceCount());
		scaledZDimension = (short) (actualZDim + 2);// two empty frames on
													// either side for texture
													// clamping to work
		maxDim = (short) Math.max(xDim, yDim);
		// maxDim = (short)Math.max(maxDim,zDim);

		double3d = new double[xDim][yDim][scaledZDimension][3];
		
		System.out.println("started reading images "+System.currentTimeMillis());
		List<BufferedImage> allSliceImages = record.getChannelOverlayImagesForSlices(frame, site, xDim, true);
		System.out.println("started reading images "+System.currentTimeMillis());

		float zScaleFactor = 1.0f;
		if (record.getSliceCount() > actualZDim) {
			zScaleFactor = (float) record.getSliceCount() / (float) (actualZDim); // will
																					// be
																					// a
																					// number
																					// greater
																					// than
																					// 1
		}
		for (int k = 0; k < maxDim; k++) {
			BufferedImage bis = null;
			if (k > 0 && k < actualZDim + 1) {
				int imageindex = Math.round((float) (k - 1) * zScaleFactor);
				
//				Set<Dimension> imageDimensions = new HashSet<Dimension>();
//				for(int i=0;i<channel.size();i++){
//					imageDimensions.add(new Dimension(frame,imageindex,i, site));
//				}
//				BufferedImage bi = record.getOverlayedPixelData(imageDimensions).getImage(true);
				
				BufferedImage bi = allSliceImages.get(imageindex);
				
				fireEvent(k + 1, scaledZDimension); // firing ImageCreatedEvent
				bis = ImageUtils.getScaledImage(bi, (short) yDim, (short) xDim);
			}
			for (int j = 0; j < yDim; j++) {
				for (int i = 0; i < xDim; i++) {
					if (k == 0) {
						double3d[i][j][k][0] = 0;
						double3d[i][j][k][1] = 0;
						double3d[i][j][k][2] = 0;

					}
					if (k > 0 && k < actualZDim + 1) {
						Color color = new Color(bis.getRGB(i, j));
						double3d[i][j][k][0] = ((float) color.getRed());
						double3d[i][j][k][1] = ((float) color.getGreen());
						double3d[i][j][k][2] = ((float) color.getBlue());
					} else if (k == actualZDim + 1) {

						double3d[i][j][k][0] = 0;
						double3d[i][j][k][1] = 0;
						double3d[i][j][k][2] = 0;

					} else {
						// do nothing
					}
				}
			}
			bis = null;
		}
		findMinAndMaxValues();
	}

	// /}}}

	// /{{{ Getting the dimensions
	private short[] getDimensions(String[] fileNames) {
		RenderedOp bi = JAI.create("fileload", fileNames[0]);
		// actual height,width
		int height = bi.getHeight();
		int width = bi.getWidth();

		// Nearest 2 multiple
		short HEIGHT = (short) Math.min(MAX_SUPPORTED_DIMENSION, Math.ceil(Math
				.pow(2, Math.ceil(Math.log(height) / Math.log(2)))));
		short WIDTH = (short) Math.min(MAX_SUPPORTED_DIMENSION, Math.ceil(Math
				.pow(2, Math.ceil(Math.log(width) / Math.log(2)))));

		int stackSize = fileNames.length;
		short NUM_IMAGES = (short) Math.ceil(Math.pow(2,
				Math.ceil(Math.log(stackSize) / Math.log(2))));

		short[] dim = new short[3];
		dim[0] = (short) HEIGHT;
		dim[1] = (short) WIDTH;
		dim[2] = (short) NUM_IMAGES;

		return dim;
	}

	// /}}}

	// /{{{ Getting the dimensions
	private short[] getDimensions(int height, int width, int slices) {

		// Nearest 2 multiple
		short HEIGHT = (short) Math.min(MAX_SUPPORTED_DIMENSION, Math.ceil(Math
				.pow(2, Math.ceil(Math.log(height) / Math.log(2)))));
		short WIDTH = (short) Math.min(MAX_SUPPORTED_DIMENSION, Math.ceil(Math
				.pow(2, Math.ceil(Math.log(width) / Math.log(2)))));
		short NUM_IMAGES = (short) Math.max(2, Math.ceil(Math.pow(2,
				Math.ceil(Math.log(slices) / Math.log(2)))));

		short[] dim = new short[3];
		dim[0] = (short) HEIGHT;
		dim[1] = (short) WIDTH;
		dim[2] = (short) NUM_IMAGES;

		return dim;
	}

	// /}}}

	// /{{{ Getting autoscale
	private float getAutoScale(int height, int width, int slices) {
		if (height > MAX_SUPPORTED_DIMENSION || width > MAX_SUPPORTED_DIMENSION) {
			double scale1 = (double) MAX_SUPPORTED_DIMENSION / (double) height;
			double scale2 = (double) MAX_SUPPORTED_DIMENSION / (double) width;

			double scale3 = (double) Math.min(MAX_DEPTH, slices)
					/ (double) slices;
			float prescale = (float) Math.min(scale1, scale2);
			float autoScale = (float) (prescale / scale3);

			autoZScale = autoScale;
			return autoScale;

		}
		return 1.0f;
	}

	// /}}}

	// /{{{getXDimension
	public short getXDimension() {
		return xDim;
	}

	// /}}}

	// /{{{ getYDimention
	public short getYDimension() {
		return yDim;
	}

	// /}}}

	// /{{{getZDimension
	public short getZDimension() {
		return zDim;
	}

	// /}}}

	// /{{{getmaxDimension
	public short getMaxDimension() {
		return maxDim;
	}

	// /}}}

	public int getStackSize() {
		return actualZDim;
	}

	public float getAutoScale() {
		return autoZScale;
	}

	public float getAutoScale(IRecord record, int site, int frame,
			List<IChannel> channel) {
		// IRecordVO record = IndexAccessDelegate.getRecordVO(recordId);
		// return getAutoScale(record.getHeight(), record.getWidth(),
		// record.getNumSlices());

		return getAutoScale(record.getImageHeight(), record.getImageWidth(), record.getSliceCount());
	}

	// /{{{ min and max values for the colors ..
	private void findMinAndMaxValues() {
		cal_min[0] = Double.MAX_VALUE;
		cal_min[1] = Double.MAX_VALUE;
		cal_min[2] = Double.MAX_VALUE;
		cal_max[0] = Double.MIN_VALUE;
		cal_max[1] = Double.MIN_VALUE;
		cal_max[2] = Double.MIN_VALUE;
		for (int k = 0; k < scaledZDimension; k++) {
			for (int j = 0; j < yDim; j++) {
				for (int i = 0; i < xDim; i++) {
					for (int color = 0; color < 3; color++) {
						if (double3d[i][j][k][color] > cal_max[color])
							cal_max[color] = double3d[i][j][k][color];
						if (double3d[i][j][k][color] < cal_min[color])
							cal_min[color] = double3d[i][j][k][color];
					}
				}
			}
		}

	}

	// /}}}

	public double[] getClampedMin() {
		return cal_min;
	}

	public double[] getClampedMax() {
		return cal_max;
	}

	public double[] getVolumeData(int x, int y, int z) {
		return double3d[x][y][z];
	}

	public void clearData() {
		double3d = null;
	}

	public short getAllocatedZDimension() {
		return scaledZDimension;
	}

	public void addImageCreatedListener(IImageCreatedListener l) {
		eventMulticaster.addListener(l);
	}

	public void removeImageCreatedListener(IImageCreatedListener l) {
		eventMulticaster.removeListener(l);
	}

	private void fireEvent(int currImageIndex, int totalImageCount) {
		ImageCreatedEvent event = new ImageCreatedEvent(currImageIndex,
				totalImageCount);
		eventMulticaster.fireEvent(event);
	}

	private static class ImageCreatedEventDispatcher implements
			EventMulticaster.IEventDispatcher {
		public static final ImageCreatedEventDispatcher INSTANCE = new ImageCreatedEventDispatcher();

		public void dispatchEvent(Object listenerObject, Object eventObject) {
			IImageCreatedListener listener = (IImageCreatedListener) listenerObject;
			ImageCreatedEvent event = (ImageCreatedEvent) eventObject;
			listener.imageCreated(event);
		}
	}

}
