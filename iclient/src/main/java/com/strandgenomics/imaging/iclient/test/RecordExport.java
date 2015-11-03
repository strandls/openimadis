package com.strandgenomics.imaging.iclient.test;

import java.io.File;
import java.io.IOException;

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
import ome.xml.model.primitives.PositiveFloat;
import ome.xml.model.primitives.PositiveInteger;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.image.PixelArray;

public class RecordExport {

	/** The file writer. */
	private ImageWriter writer;

	/** The name of the output file. */
	private String outputFile;

	/**
	 * Construct a new FileExport that will save to the specified file.
	 * 
	 * @param outputFile
	 *            the file to which we will export
	 */
	public RecordExport(String outputFile) {
		this.outputFile = Constants.getConfigDirectory() + File.separator
				+ outputFile;
	}

	private void export(Record r) {
		// TODO Auto-generated method stub
		int width = r.getImageWidth();
		int height = r.getImageHeight();
		int pixelType = r.getPixelDepth().getByteSize();

		System.out.println("Sites: " + r.getSiteCount());

		IMetadata omexml = initializeMetadata(width, height, pixelType, r);

		System.out.println(omexml.toString());
		System.out.println(omexml.getImageCount());

		// only save a plane if the file writer was initialized successfully
		boolean initializationSuccess = initializeWriter(omexml, r);
		System.out.println(initializationSuccess);

		if (initializationSuccess) {
			for (int site = 0; site < r.getSiteCount(); site++)
				savePlane(width, height, pixelType, r, site);
		}
		cleanup();

	}

	private static void print(long[] guids) {
		if (guids != null && guids.length > 0) {
			System.out.println("Query string found in following records ");
			for (long guid : guids) {
				System.out.println(guid);
			}
		} else {
			System.out.println("Query string not found");
		}
	}

	/**
	 * Set up the file writer.
	 * 
	 * @param omexml
	 *            the IMetadata object that is to be associated with the writer
	 * @param r
	 * @return true if the file writer was successfully initialized; false if an
	 *         error occurred
	 */
	private boolean initializeWriter(IMetadata omexml, Record r) {
		// create the file writer and associate the OME-XML metadata with it
		writer = new ImageWriter();
		writer.setMetadataRetrieve(omexml);

		Exception exception = null;
		try {
			writer.setId(outputFile);
		} catch (FormatException e) {
			exception = e;
		} catch (IOException e) {
			exception = e;
		}

		if (exception != null) {
			System.err.println("Failed to initialize file writer.");
			exception.printStackTrace();
		}

		return exception == null;
	}

	/**
	 * Populate the minimum amount of metadata required to export an image.
	 * 
	 * @param width
	 *            the width (in pixels) of the image
	 * @param height
	 *            the height (in pixels) of the image
	 * @param pixelType
	 *            the pixel type of the image; @see loci.formats.FormatTools
	 * @param r
	 */
	private IMetadata initializeMetadata(int width, int height, int pixelType,
			Record r) {
		Exception exception = null;
		try {
			// create the OME-XML metadata storage object
			ServiceFactory factory = new ServiceFactory();
			OMEXMLService service = factory.getInstance(OMEXMLService.class);
			IMetadata meta = service.createOMEXMLMetadata();
			meta.createRoot();

			for (int site = 0; site < r.getSiteCount(); site++) {
				// define each stack of images - this defines a single stack of
				// images
				meta.setImageID("Image:" + site, site);
				meta.setPixelsID("Pixels:" + site, site);

				meta.setImageName(r.getSite(site).getName(), site);

				// specify that the pixel data is stored in big-endian format
				// change 'TRUE' to 'FALSE' to specify little-endian format
				meta.setPixelsBinDataBigEndian(Boolean.TRUE, site, 0);

				// specify that the images are stored in ZCT order
				meta.setPixelsDimensionOrder(DimensionOrder.XYCZT, site);

				// specify that the pixel type of the images
				meta.setPixelsType(PixelType.fromString(FormatTools
						.getPixelTypeString(pixelType)), site);

				// specify the dimensions of the images
				meta.setPixelsSizeX(new PositiveInteger(width), site);
				meta.setPixelsSizeY(new PositiveInteger(height), site);

				System.out.println(r.getPixelSizeAlongXAxis());
				System.out.println(r.getPixelSizeAlongYAxis());
				System.out.println(r.getPixelSizeAlongZAxis());

				try {
					meta.setPixelsPhysicalSizeX(
							new PositiveFloat(r.getPixelSizeAlongXAxis()), site);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					meta.setPixelsPhysicalSizeY(
							new PositiveFloat(r.getPixelSizeAlongYAxis()), site);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					meta.setPixelsPhysicalSizeZ(
							new PositiveFloat(r.getPixelSizeAlongZAxis()), site);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println(r.getSliceCount());
				System.out.println(r.getChannelCount());
				System.out.println(r.getFrameCount());

				meta.setPixelsSizeZ(new PositiveInteger(r.getSliceCount()),
						site);
				meta.setPixelsSizeC(new PositiveInteger(r.getChannelCount()),
						site);
				meta.setPixelsSizeT(new PositiveInteger(r.getFrameCount()),
						site);

				// define each channel and specify the number of samples in the
				// channel
				// the number of samples is 3 for RGB images and 1 otherwise
				for (int channel = 0; channel < r.getChannelCount(); channel++) {
					meta.setChannelID("Channel:" + channel + ":" + site, site,
							channel);
					meta.setChannelName(r.getChannel(channel).getName(), site,
							channel);
					meta.setChannelSamplesPerPixel(new PositiveInteger(1),
							site, channel);
				}

			}
			return meta;
		} catch (DependencyException e) {
			exception = e;
		} catch (ServiceException e) {
			exception = e;
		} catch (EnumerationException e) {
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
	private void savePlane(int width, int height, int pixelType, Record r,
			int site) {

		Exception exception = null;
		try {
			int count = 0;
			for (int t = 0; t < r.getFrameCount(); t++) {
				for (int z = 0; z < r.getSliceCount(); z++) {
					for (int c = 0; c < r.getChannelCount(); c++) {

						System.out.println("C: " + c + " T: " + t + " Z: " + z
								+ " site: " + site);

						IPixelData pixelData = r.getPixelData(new Dimension(t,
								z, c, site));
						PixelArray pixelArray = pixelData.getRawData();
						byte[] plane = pixelArray.getBytes();
						writer.saveBytes(count++, plane);
					}
				}
			}
		} catch (FormatException e) {
			exception = e;
		} catch (IOException e) {
			exception = e;
		}
		if (exception != null) {
			System.err.println("Failed to save plane.");
			exception.printStackTrace();
		}
	}

	/** Close the file writer. */
	private void cleanup() {
		try {
			writer.close();
		} catch (IOException e) {
			System.err.println("Failed to close file writer.");
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String... args) {
		// TODO Auto-generated method stub

		if (args == null || args.length == 0) {
			args = new String[] { "localhost", "8080", "nimisha", "nimisha123" };
		}

		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		String hostIP = args[0];
		int hostPort = Integer.parseInt(args[1]);
		String userName = args[2];
		String password = args[3];

		ispace.login(false, hostIP, hostPort, userName, password);

		// search unconditionally on all projects
		// long[] guids = ImageSpaceObject.getImageSpace().search("deltavision",
		// null, null, 1);
		// print(guids);

		// for(long guid:guids)
		// {
		long guid = 2;
		Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
		System.out.println(r);
		RecordExport exporter = new RecordExport(guid + ".ome.tiff");
		exporter.export(r);
		// }

		System.out.println("Done");
	}

}
