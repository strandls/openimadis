package com.strandgenomics.imaging.ithreedview;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.swing.JFileChooser;
import javax.swing.JRootPane;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Point3f;

import com.strandgenomics.imaging.ithreedview.utils.ImageUtils;

public class ExtendedCanvas3D extends Canvas3D  {

	private boolean imgWrite;
	private boolean continuousCapture;
	private int filenumber;
	private ImageStack imgStack = null;
	private Raster snapShot = null;
	private int DefaultMaxSize = 500;
	private int MaxFrameSize =DefaultMaxSize;

	private int DefaultxScale = 128;
	private int xScale =DefaultxScale;

	private int DefaultyScale = 128;
	private int yScale =DefaultyScale;
	private JRootPane rootPane;



	///{{{ constructor
	public ExtendedCanvas3D(GraphicsConfiguration gc) {
		super(gc);
		imgWrite = false;
		continuousCapture = false;
		filenumber = 0;
	}
	///}}}

	///{{{ postSwap
	public void postSwap() 
	{
		if(imgWrite) {
			GraphicsContext3D  g3d = getGraphicsContext3D();
			Rectangle rect = this.getBounds();
			// setup new raster
			Point3f pf = new Point3f(-1.0f,-1.0f,-1.0f);
			BufferedImage bimg = new BufferedImage(rect.width,rect.height,BufferedImage.TYPE_INT_RGB);
			ImageComponent2D image2D = new ImageComponent2D(ImageComponent.FORMAT_RGB,bimg);
			snapShot = new Raster(pf,Raster.RASTER_COLOR,0,0,rect.width,rect.height,image2D,null);
			g3d.readRaster(snapShot);

			BufferedImage origimg = snapShot.getImage().getImage();

			if(!continuousCapture && imgWrite)
			{
				JFileChooser saveDialog = new JFileChooser();
				List<String> filters = Arrays.asList(".png", ".jpeg", ".jpg");
				FileFilter imageFiles = new FileFilter() {
					
					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public boolean accept(File f) {
						String filename = f.getName();
						if(filename.endsWith("png")||filename.endsWith("jpeg")||filename.endsWith("jpg"))
							return true;
						return false;
					}
				};
				saveDialog.setFileFilter(imageFiles);
				saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
				saveDialog.setVisible(true);
				int retVal = saveDialog.showSaveDialog(rootPane);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					try{
						File f = saveDialog.getSelectedFile();
						if(!f.exists())
							f.createNewFile();
						String[] token = f.getName().split("\\.");
						ImageIO.write(origimg, token[token.length - 1], f);
						}catch (Exception e)
						{
							System.err.println("Not able to write 3D snapshot image");
						}
				} else {
					resetStacks();	
				}
			}
			if (continuousCapture && filenumber+1< MaxFrameSize)
			{
				// If totalMemory is equal to maxMemory and if freeMemory is 
				// within 10 % of totalMemory, then recording is stopped.

				BufferedImage img = ImageUtils.getScaledImage(origimg,(short)yScale,(short)xScale);
				long freeMemory = (Runtime.getRuntime().freeMemory())/(1024*1024);
				long maxMemory = (Runtime.getRuntime().maxMemory())/(1024*1024);
				long totalMemory = (Runtime.getRuntime().totalMemory())/(1024*1024);
				
				if (freeMemory <= (0.1*totalMemory) && totalMemory == maxMemory) {
					stopVideoCapture();
					firePropertyChange("memoryLimitReached", false, true);
					return;
				}

				filenumber++;
				ImagePlus framePlus = new ImagePlus("Frame",img);
				ImageProcessor frameProcessor = framePlus.getChannelProcessor();
				if(imgStack==null)
				{
					imgStack = new ImageStack(xScale,yScale,img.getColorModel());
				}
				String title = "Slice"+filenumber;
				imgStack.addSlice(title,frameProcessor);
			}
			else if (continuousCapture && filenumber+1>=MaxFrameSize)
			{
				stopVideoCapture();	
				firePropertyChange("frameLimitReached", false,true);
			}
			else if (!continuousCapture)
			{
				imgWrite = false;
			}
		}
	}
	///}}}

	///{{{ capture Simgle frame
	public void captureSingleFrame(JRootPane rootPane)
	{
		this.rootPane = rootPane;
		if(continuousCapture)
		{
			// already in capture mode - do nothing
			return;
		}

		resetStacks();
		if (imgWrite == false) {
			imgWrite = true;
		}
	}
	///}}}

	///{{{ start continuous capture
	public void startVideoCapture(int frameSize, int xDimension, int yDimension)
	{
		MaxFrameSize = frameSize;
		xScale = xDimension;
		yScale = yDimension; 
		if(continuousCapture)
		{
			//already capturing 
			System.out.println("Already Capturing");
			return;
		}

		resetStacks();
		if (imgWrite == false) {
			imgWrite = true;
			continuousCapture = true;
			filenumber = 0;
		}

	}
	///}}}

	///{{{stop continuous capture
	public int stopVideoCapture()
	{
		System.out.println("Stopping continuous capture");
		if(!continuousCapture)
		{
			//capture not started
			//System.err.println("Capture not even started");
			return 0;
		}
		imgWrite = false;
		continuousCapture = false;
		return filenumber-1;
	}
	///}}}

	///{{{getCreatedStack
	public ImageStack getCreatedStack()
	{
		return imgStack;
	}
	///}}}

	///{{{resetVideoStack
	public void resetStacks()
	{
		if(continuousCapture)
		{
			stopVideoCapture();
			firePropertyChange("recordingTerminated", false,true);
		}
		imgWrite =false;
		snapShot = null;
		imgStack = null;
		System.gc();
		System.gc();
	}
	///}}}

	///{{{ getSnapShotImage
	public BufferedImage getSnapshotImage()
	{
		return snapShot.getImage().getImage();
	}
	///}}}

	///{{{ getDefaultFrameSize
	public int  getDefaultFrameSize()
	{
		return DefaultMaxSize;
	}
	///}}}

	///{{{ getDefaultxScale
	public int getDefaultxScale()
	{
		return DefaultxScale;
	}
	///}}}

	///{{{getDefaultyscale
	public int getDefaultyScale()
	{
		return DefaultyScale;
	}
	///}}}

	///{{{returnrecordingstatus
	public boolean isRecordingOn()
	{
		return continuousCapture;
	}
	///}}}

}

