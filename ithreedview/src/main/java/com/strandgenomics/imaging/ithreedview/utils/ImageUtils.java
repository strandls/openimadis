package com.strandgenomics.imaging.ithreedview.utils;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import com.strandgenomics.imaging.icore.image.PixelArray;

public class ImageUtils {

	public ImageUtils() {

	}

	public static BufferedImage getDummyImage(int dim1, int dim2) {

		ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ComponentColorModel colorModel = new ComponentColorModel(colorSpace,
				true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		WritableRaster raster = colorModel.createCompatibleWritableRaster(dim1,
				dim2);
		BufferedImage bImage = new BufferedImage(colorModel, raster, false,
				null);
		byte[] byteData = ((DataBufferByte) raster.getDataBuffer()).getData();
		int index = 0;
		for (int y = 0; y < dim1; y++) {
			for (int x = 0; x < dim2; x++) {

				byteData[index++] = (byte) 0;
			}
		}
		return bImage;
	}

	// /{{{JAI based image scaling
	public static BufferedImage getScaledImage(BufferedImage image, int HEIGHT,
			int WIDTH) {

		PlanarImage bi = PlanarImage.wrapRenderedImage(image);

		int height = bi.getHeight();
		int width = bi.getWidth();

		float scalex = (float) HEIGHT / (float) height;
		float scaley = (float) WIDTH / (float) width;

		float minscale = Math.min(scalex, scaley);
		float transx = 0;
		float transy = 0;

		if (scalex > minscale) {
			transx = (float) Math.floor(scalex * HEIGHT - minscale * HEIGHT);
		}
		if (scaley > minscale) {
			transy = (float) Math.floor(scaley * WIDTH - minscale * WIDTH);
		}

		// scaling the image
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(bi);
		pb.add(minscale);
		pb.add(minscale);
		pb.add(0.0F);
		pb.add(0.0F);
		pb.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
		PlanarImage imgScale = JAI.create("scale", pb);

		// Adding a border to the image
		pb = new ParameterBlock();
		pb.addSource(imgScale);
		int toppad = (int) transx / 2;
		int bottompad = HEIGHT - imgScale.getHeight() - (int) transx / 2;
		int leftpad = (int) transy / 2;
		int rightpad = WIDTH - imgScale.getWidth() - (int) transy / 2;
		pb.add(leftpad);
		pb.add(rightpad);
		pb.add(toppad);
		pb.add(bottompad);

		PlanarImage paddedImage = JAI.create("border", pb);
		BufferedImage bis = paddedImage.getAsBufferedImage();
		return bis;
	}

	public static BufferedImage getBufferedImage(int xDim, int yDim, int[][][] image)
	{
		int alpha = 255; // opaque images
		int noOfPixels = xDim * yDim;
		int[] pixels = new int[noOfPixels];
		
		for (int x = 0; x < xDim; x++) 
		{
			for (int y = 0; y < yDim; y++) 
			{
				int red   = image[x][y][0];
				int green = image[x][y][1];
				int blue  = image[x][y][2];
				int argb =  ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
				//pixel index in a linear array
				int index = (y * xDim) + x;
				pixels[index] = argb;
			}
		}
		
		return PixelArray.getRGBImage(xDim, yDim, pixels);
	}

	private void thresholdImage(int[][][] imageData, int dim1, int dim2, int thresholdVal) 
	{
		for (int x = 0; x < dim1; x++) {
			for (int y = 0; y < dim2; y++) {
				if (imageData[x][y][0] < thresholdVal)
					imageData[x][y][0] = 0;
				if (imageData[x][y][1] < thresholdVal)
					imageData[x][y][1] = 0;
				if (imageData[x][y][2] < thresholdVal)
					imageData[x][y][2] = 0;
			}
		}
	}

	private BufferedImage getBrightenedImage(double[] constants, BufferedImage origImage)
	{
		ParameterBlock pb;
		double k0, k1, k2;

		pb = new ParameterBlock();
		pb.addSource(origImage);
		pb.add(constants);

		// Construct the AddConst operation.
		RenderedImage addConstImage = JAI.create("addconst", pb, null);
		return (BufferedImage) addConstImage;
	}
}
