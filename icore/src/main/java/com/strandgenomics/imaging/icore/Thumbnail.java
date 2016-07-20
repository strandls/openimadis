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

package com.strandgenomics.imaging.icore;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * Thumbnail powered by byte array
 * 
 * @author Anup Kulkarni
 */
public class Thumbnail implements Storable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1500092420533949236L;
	/**
	 * thumbnail data
	 */
	private byte[] data;
	long revision;
	
	public Thumbnail(BufferedImage thumbnail, long revision)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			
			ImageIO.write(thumbnail, "jpg", os);
			this.data = os.toByteArray();
			this.revision=revision;
		}
		catch (IOException e) {
			try{
				os.close();
			}
			catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	public Thumbnail(byte[] thumbnail)
	{
		this.data = thumbnail;
	}
	
	public Thumbnail(InputStream jpegStream, long revision) throws IOException
	{
		ByteArrayOutputStream sink = new ByteArrayOutputStream();
		Util.transferData(jpegStream, sink);
		
		this.data = sink.toByteArray();
		this.revision=revision;
	}
	
	/**
	 * 
	 * @return input stream for thumbnail
	 */
	public InputStream getInputStream()
	{
		InputStream is = new ByteArrayInputStream(data);
		return is;
	}
	
	/**
	 * 
	 * @return bufferedimage from thumbnail object
	 * @throws IOException
	 */
	public BufferedImage getBufferedImage() throws IOException
	{
		BufferedImage thumbnail = ImageIO.read(getInputStream());
		return thumbnail;
	}
	
	/**
	 * 
	 * @return revision for thumbnail
	 */
	public long getRevision() {
		return revision;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
