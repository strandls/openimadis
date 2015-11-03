package com.strandgenomics.imaging.iclient.daemon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * utility methods required by UploadDaemon eg. managing session etc
 * 
 * @author Anup Kulkarni
 */
public class UploadDaemonUtil 
{
	/**
	 * stores current state of upload queue
	 * @param queue
	 */
	public static void storeSession(List<UploadSpecification> queue, String filename)
	{
		ObjectOutputStream os = null;
		try
		{
			os = new ObjectOutputStream(new FileOutputStream(new File(filename)));
			os.writeObject(queue);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.flush();
					os.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * loads current state of upload queue
	 * @param sessionfilePath path of file in which session is stored
	 * @return queue
	 */
	public static List<UploadSpecification> loadSession(String sessionfilePath)
	{
		List<UploadSpecification> queue = new ArrayList<UploadSpecification>();
		File sessionFile = new File(sessionfilePath);

		if (!sessionFile.exists())
			return queue;

		ObjectInputStream is = null;
		try
		{
			is = new ObjectInputStream(new FileInputStream(sessionFile));
			queue = (List<UploadSpecification>) is.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}

		return queue;
	}
	
	/**
	 * writes the upload specification in the file
	 * @param spec
	 */
	public static void storeObject(UploadSpecification spec)
	{
		ObjectOutputStream os = null;
		try
		{
			os = new ObjectOutputStream(new FileOutputStream(new File(spec.getSessionFilepath())));
			os.writeObject(spec);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (os != null)
				{
					os.flush();
					os.close();
				}
			}
			catch (IOException ex)
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * loads current state of upload queue
	 * @param sessionfilePath path of file in which session is stored
	 * @return queue
	 */
	public static UploadSpecification loadObject(String sessionfilePath)
	{
		File sessionFile = new File(sessionfilePath);

		if (!sessionFile.exists())
			return null;

		ObjectInputStream is = null;
		try
		{
			is = new ObjectInputStream(new FileInputStream(sessionFile));
			UploadSpecification object = (UploadSpecification) is.readObject();
			return object;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
