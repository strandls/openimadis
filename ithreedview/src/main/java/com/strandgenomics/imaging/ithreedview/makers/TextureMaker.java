package com.strandgenomics.imaging.ithreedview.makers;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture3D;

import com.strandgenomics.imaging.ithreedview.dataobjects.Volume3DData;

public class TextureMaker {

	public int currentThreshold;
	public int currentBrightness;

	public int defaultBrightness = 0;
	public int defaultThreshold =0;

	private Texture3D  texture3D;

	private double[] clampedMin = new double[3];
	private double[] clampedMax = new double[3];


	public TextureMaker()
	{

	}


	public Texture3D getLoadedTexture()
	{
		return texture3D;	
	}

	public void generateTexture3D(int brightness, int threshold, Volume3DData vol3d)
	{
		currentBrightness =  brightness;
		currentThreshold = threshold;

		texture3D = null;

		texture3D = new Texture3D(Texture.BASE_LEVEL, Texture.RGBA, vol3d.xDim, vol3d.yDim, vol3d.scaledZDimension);
		ImageComponent3D pArray= loadDataMap(vol3d);

		// Create a 3D texture
		texture3D.setImage(0, pArray);
		texture3D.setEnable(true);

		// Minification filter .. when pixel consists of multiple texels
		texture3D.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		// Magnification filter .. when pixel is smaller than texel ..
		texture3D.setMagFilter(Texture.BASE_LEVEL_LINEAR);

		// Boundary Modes
		texture3D.setBoundaryModeS(Texture.CLAMP);
		texture3D.setBoundaryModeT(Texture.CLAMP);
		texture3D.setBoundaryModeR(Texture.CLAMP);

	}

	public ImageComponent3D loadDataMap(Volume3DData vol3d)
	{
		int maxDim = vol3d.getMaxDimension();

		clampedMin = vol3d.getClampedMin();
		clampedMax = vol3d.getClampedMax();
		ImageComponent3D pArray = new ImageComponent3D(ImageComponent.FORMAT_RGBA, vol3d.xDim, vol3d.yDim, vol3d.scaledZDimension);
		ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ComponentColorModel colorModel =new ComponentColorModel(colorSpace, true, false,Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		WritableRaster raster =colorModel.createCompatibleWritableRaster(vol3d.xDim, vol3d.yDim);

		BufferedImage bImage =new BufferedImage(colorModel, raster, false, null);

		byte[] byteData = ((DataBufferByte)raster.getDataBuffer()).getData();

		for (int z = 0; z < vol3d.scaledZDimension; z++) {
			int index = 0;
			for (int  y = 0; y < vol3d.yDim ; y++) {
				for (int x = 0; x < vol3d.xDim; x++) {

					double [] data = vol3d.getVolumeData(x,y,vol3d.scaledZDimension-z-1);// double3d[x][y][z];
					double [] scale = new double[3];	
					boolean intense = false;

					double sum = 0.0;
					double number = 0.0;
					for (int i = 0; i < 3; i++){
						if (data[i] < clampedMin[i]) data[i] = clampedMin[i];
						if (data[i] > clampedMax[i]) data[i] = clampedMax[i];
						scale[i] = (data[i] - clampedMin[i])/(clampedMax[i] - clampedMin[i]);
						if(scale[i]>0)
						{
							scale[i]=Math.min(1.0f,scale[i]+(float)currentBrightness*0.1f); // saturating this pixel
						}
						if(255*scale[i]>currentThreshold)
						{
							intense = true;
						}


						byteData[index++] = (byte)(255*scale[i]);
						if( clampedMax[i]>1)
						{
							// only consider these for transparency 
							sum+=255*scale[i];
							number +=1;
						}

					}

					if(intense&& z>0 && z<vol3d.actualZDim+1)
					{

						if(number < 1) {
							byteData[index++]=0;	
						}else
						{
							byteData[index++] = (byte)(( sum/number));
						}
					}
					else
					{

						byteData[index++] = (byte)0;
					}						}
			}
			pArray.set(z, bImage);
		}

		return pArray;
	}


	public void cleanup()
	{
		texture3D = null;
	}

}
