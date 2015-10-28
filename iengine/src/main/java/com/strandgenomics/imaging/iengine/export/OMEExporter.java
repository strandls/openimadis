package com.strandgenomics.imaging.iengine.export;

import java.io.File;
import java.io.IOException;
import java.util.List;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.ImageWriter;
import loci.formats.meta.IMetadata;
import loci.formats.services.OMEXMLService;
import ome.xml.model.enums.DimensionOrder;
import ome.xml.model.enums.EnumerationException;
import ome.xml.model.enums.PixelType;
import ome.xml.model.primitives.NonNegativeInteger;
import ome.xml.model.primitives.PositiveFloat;
import ome.xml.model.primitives.PositiveInteger;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.system.AttachmentManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Exporting records as tiff files the exported files contain all the original
 * raw data and the metadata associated with the original source files
 * 
 * @author Anup Kulkarni
 */
public class OMEExporter {

	/** The file writer. */
	private ImageWriter writer;

	public OMEExporter() 
	{ }
	
	public String export(String actorLogin, List<Long> guids, long requestId, String exportName, String rootDirectory) throws IOException
	{
		File requestRoot = new File(rootDirectory, String.valueOf(requestId));
		requestRoot.mkdir();
		
		for(long guid:guids)
		{
			File recordRoot = new File(requestRoot, "Record-"+guid);
			recordRoot.mkdir();
			
			String filepath = recordRoot.getAbsolutePath() + File.separatorChar + guid +".ome.tiff";
			// export source
			export(actorLogin, guid, filepath);
			// export system attachments
			exportSystemAttachments(actorLogin, guid, recordRoot.getAbsolutePath());
		}
		
		File tarBall = new File(rootDirectory, exportName+"_"+requestId+".tar.gz");
		Archiver.createTarRecursively(tarBall, true, requestRoot);
		
		Util.deleteTree(requestRoot);
		
		return tarBall.getAbsolutePath();
	}
	
	private void exportSystemAttachments(String actorLogin, long guid, String requestRoot) throws IOException
	{
		AttachmentManager attachmentManager = SysManagerFactory.getAttachmentManager();
		List<Attachment> recordAttachments = attachmentManager.getRecordAttachments(actorLogin, guid);
		if(recordAttachments!=null)
		{
			for(Attachment attachment:recordAttachments)
			{
				if(attachmentManager.isSystemAttachment(attachment.getName()))
				{
					File actualFile = attachmentManager.findAttachment(actorLogin, guid, attachment.getName());
					
					File dest = new File(requestRoot, attachment.getName());
					
					if(actualFile.exists())
						Util.copy(actualFile, dest);
				}
			}
		}
	}

	/**
	 * exports given record to specified file 
	 * @param recordId specified record
	 * @param filepath path of the target output file
	 * @throws IOException 
	 */
	public void export(String actorLogin, long recordId, String filepath) throws IOException
	{
		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, recordId);
		
		int width = record.imageWidth;
		int height = record.imageHeight;
		int pixelType = record.imageDepth.getByteSize();

		IMetadata omexml = initializeMetadata(actorLogin, record, width, height, pixelType);

		// only save a plane if the file writer was initialized successfully
		boolean initializationSuccess = initializeWriter(record, omexml, filepath);

		if (initializationSuccess)
		{
			for (int site = 0; site < record.getSiteCount(); site++)
				savePlane(actorLogin, width, height, pixelType, record, site);
		}
		
		cleanup();
	}
	
	/**
	 * Set up the file writer.
	 * 
	 * @param record specified record
	 * @param omexml the IMetadata object that is to be associated with the writer
	 * @param outputfile target output file
	 * @return true if the file writer was successfully initialized; false if an
	 *         error occurred
	 */
	private boolean initializeWriter(Record record, IMetadata omexml, String outputFile)
	{
		// create the file writer and associate the OME-XML metadata with it
		writer = new ImageWriter();
		writer.setMetadataRetrieve(omexml);
		
		Exception exception = null;
		try
		{
			writer.setId(outputFile);
		}
		catch (FormatException e)
		{
			exception = e;
		}
		catch (IOException e)
		{
			exception = e;
		}

		if (exception != null)
		{
			System.err.println("Failed to initialize file writer.");
			exception.printStackTrace();
		}

		return exception == null;
	}

	/**
	 * Populate the minimum amount of metadata required to export an image.
	 *
	 * @param record specified record
	 * @param width the width (in pixels) of the image
	 * @param height the height (in pixels) of the image
	 * @param pixelType the pixel type of the image; @see loci.formats.FormatTools
	 * @throws IOException 
	 */
	private IMetadata initializeMetadata(String actorLogin, Record record, int width, int height, int pixelType) throws IOException 
	{
		Exception exception = null;
		try 
		{
			// create the OME-XML metadata storage object
			ServiceFactory factory = new ServiceFactory();
			OMEXMLService service = factory.getInstance(OMEXMLService.class);
			IMetadata meta = service.createOMEXMLMetadata();

			meta.createRoot();

			for (int site = 0; site < record.getSiteCount(); site++) 
			{
				// define each stack of images - this defines a single stack of
				// images
				meta.setImageID("Image:" + site, site);
				meta.setPixelsID("Pixels:" + site, site);

				meta.setImageName(record.getSite(site).getName(), site);

				// specify that the pixel data is stored in big-endian format
				// change 'TRUE' to 'FALSE' to specify little-endian format
				meta.setPixelsBinDataBigEndian(Boolean.TRUE, site, 0);

				// specify that the images are stored in ZCT order
				meta.setPixelsDimensionOrder(DimensionOrder.XYZCT,site);

				// specify that the pixel type of the images
				meta.setPixelsType(PixelType.fromString(FormatTools.getPixelTypeString(pixelType)), site);

				// specify the dimensions of the images
				meta.setPixelsSizeX(new PositiveInteger(width), site);
				meta.setPixelsSizeY(new PositiveInteger(height), site);

				try
				{
					meta.setPixelsPhysicalSizeX(new PositiveFloat(record.getXPixelSize()), site);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					meta.setPixelsPhysicalSizeY(new PositiveFloat(record.getYPixelSize()), site);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					meta.setPixelsPhysicalSizeZ(new PositiveFloat(record.getZPixelSize()), site);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				meta.setPixelsSizeZ(new PositiveInteger(record.numberOfSlices), site);
				meta.setPixelsSizeC(new PositiveInteger(record.numberOfChannels), site);
				meta.setPixelsSizeT(new PositiveInteger(record.numberOfFrames), site);
				
				// define each channel and specify the number of samples in the
				// channel
				// the number of samples is 3 for RGB images and 1 otherwise
				for (int channel = 0; channel < record.getChannelCount(); channel++) 
				{
					meta.setChannelID("Channel" + channel + ":" + site, site, channel);
					meta.setChannelName(record.getChannel(channel).getName(), site, channel);
					
					int type = record.imageType == ImageType.RGB ? 3 : 1;
					meta.setChannelSamplesPerPixel(new PositiveInteger(type), site, channel);
					
					int wavelength = record.getChannel(channel).getWavelength();
					try
					{
						if(wavelength>0)
							meta.setChannelEmissionWavelength(new PositiveInteger(wavelength), 0, channel);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				
				setImageMetadata(actorLogin, record, site, meta);
			}
			return meta;
		} 
		catch (DependencyException e) 
		{
			exception = e;
		} 
		catch (ServiceException e) 
		{
			exception = e;
		} 
		catch (EnumerationException e) 
		{
			exception = e;
		}

		System.err.println("Failed to populate OME-XML metadata object.");
		exception.printStackTrace();
		return null;
	}
	
	private void setImageMetadata(String actorLogin, Record record, int site, IMetadata meta) throws IOException
	{
		int count = 0;
		for (int t = 0; t < record.numberOfFrames; t++) 
		{
			for (int c = 0; c < record.getChannelCount(); c++) 
			{
				for (int z = 0; z < record.numberOfSlices; z++) 
				{
					ImagePixelData imageMetadata = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, record.guid, new Dimension(t, z, c, site));
					
					meta.setPlaneDeltaT(imageMetadata.getElapsed_time(), 0, count);
					meta.setPlaneExposureTime(imageMetadata.getExposureTime(), 0, count);
					meta.setPlaneTheC(new NonNegativeInteger(c), 0, count);
					meta.setPlaneTheZ(new NonNegativeInteger(z), 0, count);
					meta.setPlaneTheT(new NonNegativeInteger(t), 0, count);
					
					count++;
				}
			}
		}
	}

	/**
	 * Generate a random plane of pixel data and save it to the output file.
	 * 
	 * @param width
	 *            the width of the image in pixels
	 * @param height
	 *            the height of the image in pixels
	 * @param pixelType
	 *            the pixel type of the image; @see loci.formats.FormatTools
	 * @param record
	 */
	private void savePlane(String actorLogin, int width, int height, int pixelType, Record record, int site) 
	{

		Exception exception = null;
		try 
		{
			int count = 0;
			for (int t = 0; t < record.numberOfFrames; t++) 
			{
				for (int c = 0; c < record.getChannelCount(); c++) 
				{
					for (int z = 0; z < record.numberOfSlices; z++) 
					{
						System.out.println("C: " + c + " T: " + t + " Z: " + z + " site: " + site);

						PixelArray pixelArray = SysManagerFactory.getImageManager().getRawData(actorLogin, record.guid, new Dimension(t, z, c, site), null);
						byte[] plane = pixelArray.getBytes();
						
						writer.saveBytes(count++, plane);
					}
				}
			}
		} 
		catch (FormatException e) 
		{
			exception = e;
		} 
		catch (IOException e) 
		{
			exception = e;
		}
		if (exception != null) 
		{
			System.err.println("Failed to save plane.");
			exception.printStackTrace();
		}
	}

	/** Close the file writer. */
	private void cleanup()
	{
		try
		{
			writer.close();
		}
		catch (IOException e)
		{
			System.err.println("Failed to close file writer.");
			e.printStackTrace();
		}
	}

	public static void main(String... args) throws IOException {
	}
}
