import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.strandgenomics.imaging.iclient.AuthenticationException;
import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.PixelMetaData;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.iclient.RecordBuilder;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.image.PixelArray;

public class ImageSharpeningTest 
{
	public ImageSharpeningTest() 
	{ }

	private void applySharpening(Record r, String recordLabel)
	{
		Project project = r.getParentProject();
		
		RecordBuilder rb = project.createRecordBuilder(recordLabel, r);
		
		for (int t = 0; t < r.getFrameCount(); t++)
		{
			for (int z = 0; z < r.getSliceCount(); z++)
			{
				for (int c = 0; c < r.getChannelCount(); c++)
				{
					for (int site = 0; site < r.getSiteCount(); site++)
					{
						System.out.println("C: " + c + " T: " + t + " Z: " + z + " site: " + site);

						Dimension dim = new Dimension(t,z, c, site);
						IPixelData pixelData = r.getPixelData(dim);

						try
						{
							// get appropriate ImageJ processor
							ImageProcessor myip = getImageProcessor(pixelData);
							if(myip == null) 
							{
								System.out.println("Unknown pixel data");
								return;
							}
						
							// arbitrary processing
							myip.findEdges(); 
					
							// get appropriate pixeldata back from the ImageJ processor
							PixelArray rawData = getProcessedRawData(myip); 
							if(rawData == null) 
							{
								System.out.println("Unknown raw data");
								return;
							}

							PixelMetaData mymetadata = new PixelMetaData(
								pixelData.getDimension(), pixelData.getX(),
								pixelData.getY(), pixelData.getZ(),
								pixelData.getElapsedTime(),
								pixelData.getExposureTime(),
								pixelData.getTimeStamp());

							boolean result = rb.addImageData(new Dimension(t, z, c, site), rawData, mymetadata); 
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		// commit the record builder
		rb.commit();
	}
	
	/**
	 * returns the appropriate raw data for given image processor
	 * @param myip
	 * @return appropriate raw data for given image processor
	 */
	private static PixelArray getProcessedRawData(ImageProcessor myip)
	{
		if(myip instanceof ByteProcessor)
		{
			byte[] processedData= (byte[])myip.getPixels();
			return new PixelArray.Byte(processedData, myip.getWidth(), myip.getHeight());
		}
		else if(myip instanceof ShortProcessor)
		{
			short[] processedData= (short[])myip.getPixels();
			return new PixelArray.Short(processedData, myip.getWidth(), myip.getHeight());
		}
		else if(myip instanceof ColorProcessor)
		{
			int[] processedData= (int[])myip.getPixels();
			return new PixelArray.Integer(processedData, myip.getWidth(), myip.getHeight());
		}
		else if(myip instanceof FloatProcessor)
		{
			float[] processedData= (float[])myip.getPixels();
			return new PixelArray.Float(processedData, myip.getWidth(), myip.getHeight());
		}
		return null;
	}

	/**
	 * returns appropriate image processor for input pixel data
	 * @param pixelData
	 * @return
	 * @throws IOException
	 */
	private static ImageProcessor getImageProcessor(IPixelData pixelData) throws IOException
	{
		PixelArray rawData = pixelData.getRawData();
		if(rawData instanceof PixelArray.Byte)
		{
			// create byteprocessor if the data is of type BYTE
			byte[] img = (byte[]) rawData.getPixelArray();
			return new ByteProcessor(pixelData.getImageWidth(), pixelData.getImageHeight(), img, null);
		}
		else if(rawData instanceof PixelArray.Short)
		{
			// create shortprocessor if the data is of type SHORT
			short[] img = (short[]) rawData.getPixelArray();
			return new ShortProcessor(pixelData.getImageWidth(), pixelData.getImageHeight(), img, null);
		}
		else if(rawData instanceof PixelArray.Integer)
		{
			// create colorprocessor if the data is of type INT
			int[] img = (int[]) rawData.getPixelArray();
			return new ColorProcessor(pixelData.getImageWidth(), pixelData.getImageHeight(), img);
		}
		else if(rawData instanceof PixelArray.Float)
		{
			// create colorprocessor if the data is of type INT
			float[] img = (float[]) rawData.getPixelArray();
			return new FloatProcessor(pixelData.getImageWidth(), pixelData.getImageHeight(), img, null);
		}
		
		return null;
	}
	
	private static HashMap<String, List<Object>> getWorkArguments(String filepath) throws IOException
	{
		HashMap<String, List<Object>> arguments = new HashMap<String, List<Object>>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
		while(br.ready())
		{
			String line = br.readLine();
			String lineData[] = line.split("=");
			String name = lineData[0];
			
			Object values[] = lineData[1].split(",");
			arguments.put(name, Arrays.asList(values));
		}
		
		br.close();
		
		return arguments;
	}
	
	public static void main(String... args) throws AuthenticationException, HttpException, IOException
	{
		String appId = "0TYQP3zuib577zO9XOsPxbPPHT150PKKsIkdt25Z";
		
		String inputFile = System.getProperty("InputFile");
		HashMap<String, List<Object>> arguments = getWorkArguments(inputFile);
		
		String authCode = (String)arguments.get("AuthCode").get(0);
		long taskHandle = Long.parseLong((String)arguments.get("TaskHandle").get(0));
		
		String recordLabel = (String) arguments.get("RecordLabel").get(0);
		
		List<Object> inputs = arguments.get("RecordIds");

		// login to image space system
		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		ispace.login(false, "localhost", 8080, appId, authCode);
		
		ImageSharpeningTest s = new ImageSharpeningTest();

		for(int i=0; i < inputs.size(); i++)
		{
			long guid= Long.parseLong((String)inputs.get(i));

			Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
			s.applySharpening(r, recordLabel);
			
			int progress=(i*100/inputs.size());
			ImageSpaceObject.getImageSpace().setTaskProgress(taskHandle, progress);
		}
		
		// logout
		ispace.logout();
	}
}
