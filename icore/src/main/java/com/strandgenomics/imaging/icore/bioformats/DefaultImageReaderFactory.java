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

import java.io.File;

import loci.formats.ChannelSeparator;
import loci.formats.MetadataTools;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.IMetadata;

import org.apache.log4j.Logger;

/**
 * The default ImageReaderFactory that works with a seed file
 * @author arunabha
 *
 */
public class DefaultImageReaderFactory extends ImageReaderFactory {
	
	/**
	 * the seed file that will generate the BufferedImageReaders
	 */
	protected File seedFile = null;
	
	public DefaultImageReaderFactory(File seedFile)
	{
		this.seedFile = seedFile.getAbsoluteFile();
	}
	
	@Override
	public String toString()
	{
		return seedFile.toString();
	}
	
	@Override
	public int hashCode()
	{
		return seedFile.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof DefaultImageReaderFactory)
		{
			DefaultImageReaderFactory that = (DefaultImageReaderFactory) obj;
			if(this == that) return true;
			
			return this.seedFile.equals(that.seedFile);
		}
		
		return false;
	}

	@Override
	public BufferedImageReader createBufferedImageReader() 
	{
		BufferedImageReader imageReader = new BufferedImageReader(); 
		IMetadata omexmlMetadata = MetadataTools.createOMEXMLMetadata();
		imageReader.setMetadataStore(omexmlMetadata);

		MetadataTools.setDefaultDateEnabled(false);
		// initialize the file
		try
		{
			imageReader.setId(seedFile.getAbsolutePath());
			//use channel separator ignoring the image types (RGB or GRAYSCALE)
			ChannelSeparator separator = new ChannelSeparator(imageReader);
			//the BufferedImageReader
			return new BufferedImageReader(separator); 
		}
		catch(Exception e)
		{
			Logger.getRootLogger().error("Cannot create Format Handler ..",e);
		}
		
		return null;
	}
}
