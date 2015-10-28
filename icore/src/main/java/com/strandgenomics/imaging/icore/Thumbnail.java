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
