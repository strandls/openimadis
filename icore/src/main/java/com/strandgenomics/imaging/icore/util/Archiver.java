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

/*
 * Util.java
 *
 * AVADIS Image Management System
 * Utility Stuffs
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class Archiver {
	
	/**
	 * opens the tar.gz file into the specified folder 
	 * @param destFolder the folder to write the entries of the tar-ball
	 * @param gzTarball the tar ball under consideration
	 * @throws IOException 
	 */
	public static void unTar(File destFolder, File gzTarball) throws IOException
	{
		if(!destFolder.isDirectory()) //check if the destination folder exists
			throw new IOException("named directory not found "+destFolder);
		
		try
		{
			unTar(destFolder, gzTarball, false); //first try un-tarring without gzip
		}
		catch(Exception ex)
		{
			unTar(destFolder, gzTarball, true); //otherwise try un-tarring with gzip
		}
	}
	
	/**
	 * opens the tar.gz file into the specified folder 
	 * @param destFolder the folder to write the entries of the tar-ball
	 * @param gzTarball the tar ball under consideration
	 * @throws IOException 
	 */
	public static void unTar(File destFolder, File gzTarball, boolean compress) throws IOException
	{
		if(!destFolder.isDirectory()) //check if the destination folder exists
			throw new IOException("named directory not found "+destFolder);
		
		destFolder = destFolder.getAbsoluteFile();
		
		FileInputStream fIn = null;
		BufferedInputStream bIn = null;
		GzipCompressorInputStream gzIn = null;
		TarArchiveInputStream tIn = null;
		
		try {
			fIn = new FileInputStream(gzTarball);
			bIn = new BufferedInputStream(fIn);
			
			if(compress)
			{
				gzIn = new GzipCompressorInputStream(bIn);
				tIn = new TarArchiveInputStream(gzIn);
			}
			else
			{
				tIn = new TarArchiveInputStream(bIn);
			}
			
			TarArchiveEntry tarEntry = tIn.getNextTarEntry();
			while (tarEntry != null)
			{
				//create a file with the same name as the tarEntry
				File destPath = new File(destFolder, tarEntry.getName());
				if(tarEntry.isDirectory())
				{
					destPath.mkdirs();
				}
				else
				{
					FileOutputStream fOut = new FileOutputStream(destPath);
					BufferedOutputStream bOut = new BufferedOutputStream(fOut);
					IOUtils.copy(tIn, bOut);

					bOut.close();
					fOut.close();
				}

				tarEntry = tIn.getNextTarEntry();
			}
		} 
		finally 
		{
			if(tIn != null)  tIn.close();
			if(gzIn != null) gzIn.close();
			if(bIn != null)  bIn.close();
			if(fIn != null)  fIn.close();
		}
	}
	
	public static File createTarBall(File rootFolder, String targetName, boolean compress, File ... src) 
			throws IOException
	{
		if(!rootFolder.isDirectory()) //check if the destination folder exists
			throw new IOException("named directory not found "+rootFolder);
		
		File archiveFile = new File(rootFolder.getAbsoluteFile(), targetName);
		createTarBall(archiveFile, compress, src);
		
		return archiveFile;
	}
		
	public static void createTarBall(File archiveFile, boolean compress, File ... src) throws IOException
	{

		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		GzipCompressorOutputStream gzOut = null;
		TarArchiveOutputStream tOut = null;

		try
		{
			fOut = new FileOutputStream(archiveFile);
			bOut = new BufferedOutputStream(fOut);
			
			if(compress)
			{
				gzOut = new GzipCompressorOutputStream(bOut);
				tOut = new TarArchiveOutputStream(gzOut);
			}
			else
			{
				tOut = new TarArchiveOutputStream(bOut);
			}

			for(File f : src)
			{
				if( !f.isFile() ) continue;
		
				String entryName = f.getName();
				TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);

				tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
				tOut.putArchiveEntry(tarEntry);
				
				FileInputStream fIn = new FileInputStream(f);
				BufferedInputStream bin = new BufferedInputStream(fIn);
				
				IOUtils.copy(bin, tOut);
				tOut.closeArchiveEntry();
				
				bin.close();
				fIn.close();
			}
		} 
		finally 
		{
			tOut.finish();
			if(tOut != null) tOut.close();
			if(gzOut != null) gzOut.close();
			if(bOut != null) bOut.close();
			if(fOut != null) fOut.close();
		}
	}
	
	public static void createTarRecursively(File archiveFile, boolean compress, File dir) throws IOException
	{
		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		GzipCompressorOutputStream gzOut = null;
		TarArchiveOutputStream tOut = null;

		try
		{
			fOut = new FileOutputStream(archiveFile);
			bOut = new BufferedOutputStream(fOut);
			
			if(compress)
			{
				gzOut = new GzipCompressorOutputStream(bOut);
				tOut = new TarArchiveOutputStream(gzOut);
			}
			else
			{
				tOut = new TarArchiveOutputStream(bOut);
			}
			
			// create tar from directory
			addFileToArchive(tOut, dir,"");
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			tOut.finish();
			if(tOut != null) tOut.close();
			if(gzOut != null) gzOut.close();
			if(bOut != null) bOut.close();
			if(fOut != null) fOut.close();
		}
	}
	
    private static void addFileToArchive(TarArchiveOutputStream tOut, File dir, String base) throws IOException
	{
		File f = dir;
		String entryName = base + f.getName();
		TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);

		tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
		tOut.putArchiveEntry(tarEntry);

		if (f.isFile())
		{
			FileInputStream fin = new FileInputStream(f);
			IOUtils.copy(fin, tOut);
			fin.close();

			tOut.closeArchiveEntry();
		}
		else
		{
			tOut.closeArchiveEntry();

			File[] children = f.listFiles();

			if (children != null)
			{
				for (File child : children)
				{
					addFileToArchive(tOut, child, entryName + "/");
				}
			}
		}
	}

	public static void main(String ... args) throws Exception
    {
//    	File rootFolder = new File(args[0]).getAbsoluteFile();
//    	String targetName = args[1];
//    	
//    	List<File> targetFiles = new ArrayList<File>();
//    	
//    	for(int i = 2; i < args.length; i++)
//    	{
//    		targetFiles.add( new File(args[i]).getAbsoluteFile() );
//    	}
//    	
//    	long startTime = System.currentTimeMillis();
//    	File archiveFile = createTarBall(rootFolder, targetName, true, targetFiles.toArray(new File[0]));
//    	long endTime = System.currentTimeMillis();
//    	System.out.println("successfully created tarball "+archiveFile +" in "+(endTime-startTime) +" milli seconds");
//    	unTar(rootFolder, archiveFile);
//    	long endTime2 = System.currentTimeMillis();
//    	System.out.println("successfully untarred tarball "+archiveFile +" in "+(endTime2-endTime) +" milli seconds");
		
		File rootDirectory = new File("C:\\curie_data\\mpRGFP_mprGFP_mChLifeAct_4_nd\\original_files\\");
		createTarRecursively(new File("C:\\test\\big_tar.tar.gz"), true, rootDirectory);
    }
}
