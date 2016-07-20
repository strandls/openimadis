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

package com.strandgenomics.imaging.icore.bioformats;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import loci.formats.ChannelSeparator;
import loci.formats.FormatException;
import loci.formats.ImageReader;
import loci.formats.MetadataTools;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.ome.OMEXMLMetadata;

import org.apache.log4j.Logger;

public class BioformatsTester {
	
	
	/**
	 * Initialize a Bioformats reader by setting a filepath
	 * @param file the file source of records
	 * @return the image reader instance
	 */
	private static ImageReader createFormatReader(File file)
	{
		Logger.getRootLogger().info("[Indexer]:\t Creating Format Reader with "+file);
		
		ImageReader formatHandler = new ImageReader(); 
		IMetadata omexmlMetadata = MetadataTools.createOMEXMLMetadata();
		formatHandler.setMetadataStore(omexmlMetadata);

		// initialize the file
		try
		{
			long e = System.currentTimeMillis();
			formatHandler.setId(file.getAbsolutePath());
			long newe = System.currentTimeMillis();
			//Logger.getRootLogger().info("Time to Initialize without metadata: " + file.getName() + " " + (newe - e)/1000.0);
			System.out.println("Time to Initialize with metadata: " + file.getName() + " " + (newe - e)/1000.0);
			System.out.println(" Transformer: " + System.getProperty("javax.xml.transform.TransformerFactory"));
			String xml = ((OMEXMLMetadata)(formatHandler.getMetadataStore())).dumpXML();
			System.out.println(" OME XML " + xml);
		}
		catch(Exception e)
		{
			Logger.getRootLogger().error("Cannot create Format Handler ..",e);
			// For debugging .. Handle this case ..
			//e.printStackTrace();
		}
		return formatHandler;
	}
	
	/**
	 * Initialize a Bioformats reader by setting a filepath
	 * @param file the file source of records
	 * @return the image reader instance
	 */
	private static BufferedImageReader createImageReader(File file){
		ChannelSeparator separator = new ChannelSeparator();
		BufferedImageReader imageReader = new BufferedImageReader(separator); 
		imageReader.setOriginalMetadataPopulated(false);

		// initialize the file
		try
		{
			long e = System.currentTimeMillis();
			imageReader.setId(file.getAbsolutePath());
			long newe = System.currentTimeMillis();
			Logger.getRootLogger().info("Time to Initialize without metadata: " + file.getName() + " " + (newe - e)/1000.0);
			System.out.println("Time to Initialize without metadata: " + file.getName() + " " + (newe - e)/1000.0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Logger.getRootLogger().error("Cannot create Format Handler ..",e);
		}

		return imageReader;
	}
	
	
	private static BufferedImage readImage( File file, int series, int slice, int frame, int channel){
		BufferedImageReader imageReader = createImageReader(file);
		imageReader.setSeries(series);
		int index = imageReader.getIndex(slice, channel, frame);
		System.out.println(" index: " + index);
		BufferedImage image = null;
		try 
		{
			//the buffered imaged returned here if of unknown type
			long e = System.currentTimeMillis();
			image = imageReader.openImage(index);
			long newe = System.currentTimeMillis();
			Logger.getRootLogger().info("Time to Read Image in secs: " + (newe - e)/1000.0);
			System.out.println("Time to Read Image in secs: " + (newe - e)/1000.0);
			
        	imageReader.close();
        	
		} 
		catch (FormatException e) 
		{
			e.printStackTrace();
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return image;
	}
	
	public static void main(String... args) {
		
		
		System.out.println(" Transformer: " + System.getProperty("javax.xml.transform.TransformerFactory"));
//		System.exit(0);
		JFileChooser fileChooser = null;
		String currentDirectory = System.getProperty("user.dir");
		FileSystemView fsv = FileSystemView.getFileSystemView();
		if (currentDirectory == null && fsv == null)
			fileChooser = new JFileChooser();
		else if (currentDirectory != null && fsv == null)
			fileChooser = new JFileChooser(currentDirectory);
		else if (currentDirectory == null && fsv != null)
			fileChooser = new JFileChooser(fsv);
		else
			fileChooser = new JFileChooser(currentDirectory, fsv);

		fileChooser.setDialogTitle("Select Directories For Indexing");
		int selectionMode = JFileChooser.FILES_AND_DIRECTORIES;
		fileChooser.setFileSelectionMode(selectionMode);
		fileChooser.setMultiSelectionEnabled(true);
	    
	    int ret = fileChooser.showOpenDialog(null);
	    if (ret == JFileChooser.APPROVE_OPTION) {
	        File[] selectedFiles = new File[1];
	        selectedFiles[0] = fileChooser.getSelectedFile();
	        for(File file: selectedFiles){
	        	createFormatReader(file);
	        	BufferedImageReader imageReader = createImageReader(file);
	        	int seriesCount = imageReader.getSeriesCount();
	        	int sliceCount = imageReader.getSizeZ();
	        	int channelCount = imageReader.getSizeC();
	        	int frameCount = imageReader.getSizeT();
	        	try{
	        	imageReader.close();
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
				/*for (int s = 0; s < seriesCount; s++) {

					

					System.out.println("frame : " + frameCount
							+ " slice: " + sliceCount
							+ " channel: " + channelCount);
					for (int i = 0; i < sliceCount; i++) {
						for (int j = 0; j < channelCount; j++) {
							for (int k = 0; k < frameCount; k++) {
								long e = System.currentTimeMillis();
								BufferedImage image = readImage(file, s, i, k, j);
								long newe = System.currentTimeMillis();
								System.out
										.println("Time to read image randomly: "
												+ (newe - e) / 1000.0);
							}
						}
					}
				}*/
			}
		}
	}

}
