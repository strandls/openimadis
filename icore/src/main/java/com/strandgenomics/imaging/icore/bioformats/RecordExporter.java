package com.strandgenomics.imaging.icore.bioformats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.ImageWriter;
import loci.formats.in.OMEXMLReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.OMEXMLService;
import ome.units.UNITS;
import ome.units.quantity.Length;
import ome.xml.model.enums.DimensionOrder;
import ome.xml.model.enums.EnumerationException;
import ome.xml.model.enums.PixelType;
import ome.xml.model.primitives.PositiveFloat;
import ome.xml.model.primitives.PositiveInteger;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Exporting records as tiff files the exported files contain all the original
 * raw data and the metadata associated with the original source files
 * 
 * @author Anup Kulkarni
 */
public class RecordExporter {

	/** The file writer. */
	private ImageWriter writer;

	public RecordExporter() 
	{ }

	/**
	 * exports given record to specified file 
	 * @param record specified record
	 * @param filepath path of the target output file
	 * @throws IOException 
	 */
	public void export(IRecord record, String filepath) throws IOException
	{
		int width = record.getImageWidth();
		int height = record.getImageHeight();
		int pixelType = record.getPixelDepth().getByteSize();

		IMetadata omexml = initializeMetadata(record, width, height, pixelType);

		// only save a plane if the file writer was initialized successfully
		boolean initializationSuccess = initializeWriter(record, omexml, filepath);

		if (initializationSuccess)
		{
			for (int site = 0; site < record.getSiteCount(); site++)
				savePlane(width, height, pixelType, record, site);
		}
		cleanup();

	}

	/**
	 * Set up the file writer.
	 * 
	 * @param r specified record
	 * @param omexml the IMetadata object that is to be associated with the writer
	 * @param outputfile target output file
	 * @return true if the file writer was successfully initialized; false if an
	 *         error occurred
	 */
	private boolean initializeWriter(IRecord r, IMetadata omexml, String outputFile)
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

	private OMEXMLReader readOMEXMLMetaData(IRecord r) throws IOException
	{
		if(r == null) return null;
		
		Collection<IAttachment> attachments = r.getAttachments();
		IAttachment omeXmlAttachment = null;
		for(IAttachment attachment:attachments)
		{
			if(attachment.getName().equals(BioExperiment.OMEXMLMetaData))
			{
				omeXmlAttachment = attachment;
				break;
			}
		}
		
		if(omeXmlAttachment == null) return null;
		
		InputStream is = omeXmlAttachment.getInputStream();
		File tempFile = File.createTempFile("OmeXML", ".xml");
		OutputStream os = new FileOutputStream(tempFile);
		Util.transferData(is, os, true);
		
		OMEXMLReader omeXmlReader = new OMEXMLReader();
		try
		{
			omeXmlReader.setId(tempFile.getAbsolutePath());
		}
		catch (FormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return omeXmlReader;
	}
	
	/**
	 * Populate the minimum amount of metadata required to export an image.
	 *
	 * @param r specified record
	 * @param width the width (in pixels) of the image
	 * @param height the height (in pixels) of the image
	 * @param pixelType the pixel type of the image; @see loci.formats.FormatTools
	 * @throws IOException 
	 */
	private IMetadata initializeMetadata(IRecord r, int width, int height, int pixelType) throws IOException 
	{
		Exception exception = null;
		try 
		{
			// create the OME-XML metadata storage object
			ServiceFactory factory = new ServiceFactory();
			OMEXMLService service = factory.getInstance(OMEXMLService.class);
			IMetadata meta = service.createOMEXMLMetadata();

			OMEXMLReader omeXmlReader = readOMEXMLMetaData(r);
			
			meta.createRoot();

			for (int site = 0; site < r.getSiteCount(); site++) 
			{
				// define each stack of images - this defines a single stack of
				// images
				meta.setImageID("Image:" + site, site);
				meta.setPixelsID("Pixels:" + site, site);

				meta.setImageName(r.getSite(site).getName(), site);

				// specify that the pixel data is stored in big-endian format
				// change 'TRUE' to 'FALSE' to specify little-endian format
				meta.setPixelsBinDataBigEndian(omeXmlReader.isLittleEndian(), site, 0);

				// specify that the images are stored in ZCT order
				String dimOrder = omeXmlReader.getDimensionOrder();
				meta.setPixelsDimensionOrder(DimensionOrder.valueOf(dimOrder),site);

				// specify that the pixel type of the images
				meta.setPixelsType(PixelType.fromString(FormatTools.getPixelTypeString(pixelType)), site);

				// specify the dimensions of the images
				meta.setPixelsSizeX(new PositiveInteger(width), site);
				meta.setPixelsSizeY(new PositiveInteger(height), site);

				try
				{
					meta.setPixelsPhysicalSizeX(new Length(r.getPixelSizeAlongXAxis(),UNITS.MICROM), site);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					meta.setPixelsPhysicalSizeY(new Length(r.getPixelSizeAlongYAxis(),UNITS.MICROM), site);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					meta.setPixelsPhysicalSizeZ(new Length(r.getPixelSizeAlongZAxis(),UNITS.MICROM), site);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}


				meta.setPixelsSizeZ(new PositiveInteger(r.getSliceCount()), site);
				meta.setPixelsSizeC(new PositiveInteger(r.getChannelCount()), site);
				meta.setPixelsSizeT(new PositiveInteger(r.getFrameCount()), site);

				// define each channel and specify the number of samples in the
				// channel
				// the number of samples is 3 for RGB images and 1 otherwise
				for (int channel = 0; channel < r.getChannelCount(); channel++) 
				{
					meta.setChannelID("Channel:" + channel + ":" + site, site, channel);
					meta.setChannelName(r.getChannel(channel).getName(), site, channel);
					meta.setChannelSamplesPerPixel(new PositiveInteger(1), site, channel);
				}
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

	/**
	 * Generate a random plane of pixel data and save it to the output file.
	 * 
	 * @param width
	 *            the width of the image in pixels
	 * @param height
	 *            the height of the image in pixels
	 * @param pixelType
	 *            the pixel type of the image; @see loci.formats.FormatTools
	 * @param r
	 */
	private void savePlane(int width, int height, int pixelType, IRecord r, int site) 
	{

		Exception exception = null;
		try 
		{
			int count = 0;
			for (int t = 0; t < r.getFrameCount(); t++) 
			{
				for (int z = 0; z < r.getSliceCount(); z++) 
				{
					for (int c = 0; c < r.getChannelCount(); c++) 
					{
						System.out.println("C: " + c + " T: " + t + " Z: " + z + " site: " + site);

						IPixelData pixelData = r.getPixelData(new Dimension(t, z, c, site));
						PixelArray pixelArray = pixelData.getRawData();
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
