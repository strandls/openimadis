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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * useful methods
 * @author arunabha
 */
public class Util {
	
	private static final Pattern LOWER_CASE = Pattern.compile("\\p{Lower}");
    private static final Pattern UPPER_CASE = Pattern.compile("\\p{Upper}");
    private static final Pattern DECIMAL_DIGIT = Pattern.compile("\\p{Digit}");
	private static final Pattern SPECIAL_SYMB = Pattern.compile("\\p{Punct}");
	private static final Pattern SPACE = Pattern.compile("\\p{Space}");
	
    /**
     * <p>Random object used by random method. This has to be not local
     * to the random method so as to not return the same value in the 
     * same millisecond.</p>
     */
    private static final Random random = new Random();
    
    /**
     * returns true if input string contains atleast one of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
     * false otherwise
     * @param input
     * @return
     */
    public static boolean containsSpecialCharacter(String input)
	{
		return SPECIAL_SYMB.matcher(input).find();
	}
	
    /**
     * returns true if input string contains space
     * @param input
     * @return
     */
    public static boolean containsSpace(String input)
	{
		return SPACE.matcher(input).find();
	}

    /**
     * returns true if input string contains atleast one digit, false otherwise
     * @param input
     * @return
     */
    public static boolean containsDigit(String input)
	{
		return DECIMAL_DIGIT.matcher(input).find();
	}

    /**
     * returns true if input string contains atleast one uppercase, false otherwise
     * @param input
     * @return
     */
    public static boolean containsUpperCase(String input)
	{
		return UPPER_CASE.matcher(input).find();
	}

    /**
     * returns true if input string contains atleast one lowecase, false otherwise
     * @param input
     * @return
     */
    public static boolean containsLowerCase(String input)
	{
		return LOWER_CASE.matcher(input).find();
	}

	/**
	 * Returns a file (with the specified prefix) that do not exist within the specified folder
	 * @param root the root
	 * @param name the filename (prefix)
	 * @return the file that can be created
	 */
	public static final File findUnique(File root, String name)
	{
		if(!root.isDirectory()) 
			throw new IllegalArgumentException("folder "+root +" do not exist");
		
		root = root.getAbsoluteFile();
		int counter = 0;
		String newName = name;
		//try to find a unique name for this by suffixing numbers
		do
		{
			File temp = new File(root, newName);
			if(!temp.exists()) 
				return temp;
			
			newName = name+"_"+counter;
		}
		while(++counter < 100);
		
		//try to find a unique name by adding the system time to the file name
		do
		{
			File temp = new File(root, newName);
			if(!temp.exists()) 
				return temp;
			
			newName = name+"_"+System.nanoTime();
		}
		while(++counter < 100);

		throw new IllegalArgumentException("unique file/folder within "+root +" cannot be created"); 
	}
	
	/**
	 * Sanitizing the text :-)
	 * honors char within a-z, A-Z and 0-9 characters only, replaces with the replacement char
	 * Additionally successive chars is substituted with only one replacement char
	 * @param text the given text
	 * @return the sanitized text
	 */
	public static final String asciiText(String text, char replacement)
	{
		return text == null ? null : asciiText(text.toCharArray(), replacement);
	}
	
	private static final String asciiText(char[] buffer, char replacement)
	{
		char lastChar = replacement;
        int counter = 0;

        for (int i = 0; i < buffer.length; i++)
        {
        	if(buffer[i] >= 'a' && buffer[i] <= 'z' || buffer[i] >= 'A' && buffer[i] <= 'Z' || buffer[i] >= '0' && buffer[i] <= '9')
        		;
        	else
                buffer[i] = replacement; // replace non printable ASCII with replacement

            if (lastChar == replacement && buffer[i] == replacement) // reject consecutive replacement char
                continue;

            buffer[counter++] = buffer[i];
            lastChar = buffer[i];
        }

        if (counter == 0)
            return new String(); // empty char string

        if (buffer[counter - 1] == replacement)
            counter--;

        if (counter == 0)
            return new String(); // empty char string

        return new String(buffer, 0, counter);
	}
	
	/**
     * trip a string of white space from ends as well as excess (more than one)
     * white spaces in the middle
     */
    public static final String trim(String text)
    {
        return text == null ? null : trim(text.toCharArray());
    }

    /**
     * trim a string of white space from ends as well as excess (more than one)
     * white spaces in the middle
     */
    public static final String trim(char[] buffer) 
    {
        char lastChar = ' ';
        int counter = 0;

        for (int i = 0; i < buffer.length; i++) {

            if (buffer[i] < 32) // replace non printable ASCII with SPACE
                buffer[i] = ' ';

            if (lastChar == ' ' && buffer[i] == ' ') // reject consecutive SPACE
                continue;

            buffer[counter++] = buffer[i];
            lastChar = buffer[i];
        }

        if (counter == 0)
            return new String(); // empty char string

        if (buffer[counter - 1] == ' ')
            counter--;

        if (counter == 0)
            return new String(); // empty char string

        return new String(buffer, 0, counter);
    }
	
    public static final Boolean getBoolean(Object obj) 
    {
    	Boolean value = null;
        if (obj != null) 
        {
            if (obj instanceof Number) 
            {
                Number bInt = (Number) obj;
                int number = bInt.intValue();
                value = number == 0 ? false : true;
            } 
            else if (obj instanceof Boolean) 
            {
                value = ((Boolean) obj).booleanValue();
            }
        }
        return value;
    }

    public static final Integer getInteger(Object obj) 
    {
        Integer value = null;
        if (obj != null) 
        {
            if (obj instanceof Number) 
            {
                Number bInt = (Number) obj;
                value = new Integer(bInt.intValue());
            }
        }
        return value;
    }
    
    public static final String getString(Object obj) 
    {
        String value = null;
        if (obj != null) 
        {
            if (obj instanceof String) 
            {
            	value = (String) obj;
            }
        }
        return value;
    }

    public static final Long getLong(Object obj) 
    {
        Long value = null;
        if (obj != null) 
        {
            if (obj instanceof Number) 
            {
                Number bInt = (Number) obj;
                value = new Long(bInt.longValue());
            }
        }
        return value;
    }

    public static final Float getFloat(Object obj) 
    {
    	Float value = null;
        if (obj != null) 
        {
            if (obj instanceof Number) 
            {
                Number bInt = (Number) obj;
                value = new Float(bInt.floatValue());
            }
        }
        return value;
    }
    
    public static final Double getDouble(Object obj) 
    {
        Double value = null;
        if (obj != null) 
        {
            if (obj instanceof Number) 
            {
                Number bInt = (Number) obj;
                value = new Double(bInt.doubleValue());
            }
        }
        return value;
    }

    public static final Timestamp getTimestamp(Object obj) 
    {
        Timestamp value = null;
        if (obj != null) 
        {
            if (obj instanceof Timestamp) 
            {
                value = (Timestamp) obj;
            } 
            else if (obj instanceof java.sql.Date) 
            {
                value = new Timestamp(((java.sql.Date) obj).getTime());
            } 
            else if (obj instanceof java.util.Date) 
            {
                value = new Timestamp(((java.util.Date) obj).getTime());
            } 
            else if (obj instanceof Number) 
            {
                value = new Timestamp(((Number) obj).longValue());
            }
        }
        return value;
    }
	
	/**
	 * Transfers data from the specified source InputStream to the specified sink (an OutputStream)
	 * @param src the source of data
	 * @param sink the sink of the data
	 * @param closeAll whether to close the streams after the transfer
	 * @return number of bytes that is transferred
	 * @throws IOException 
	 */
    public static long transferData(InputStream src, OutputStream sink, boolean closeAll) throws IOException 
    {
        byte[] buffer = new byte[1024 * 16];
        int bytesRead = 0;
        long noOfBytes = 0L;
        
        try
        {
	        while (true) 
	        {
	            bytesRead = src.read(buffer, 0, buffer.length);
	            if (bytesRead == -1) // EOF
	            {
	                break;
	            }
	            
	            sink.write(buffer, 0, bytesRead);
	            noOfBytes += bytesRead;
	        }
	        sink.flush();
        }
        finally
        {
            if(closeAll)
            {
            	closeStream(sink);
            	closeStream(src);
            }
        }

        return noOfBytes;
    }
    
    public static final void closeStreams(InputStream in, OutputStream out) 
    {
    	closeStream(in);
    	closeStream(out);
    }

    public static final void closeStream(InputStream in) 
    {
        try 
        {
        	if (in != null)  in.close();
        } 
        catch (Exception ex) 
        {}
    }

    public static final void closeStream(OutputStream out)
    {
        try 
        {
        	if (out != null) out.close();
        } 
        catch (Exception ex) 
        {}
    }
	
    /**
     * Serializes the specified object into xml string using java bean
     * XMLEncoder Uses the specified charEncoding to convert the bytes into
     * string
     * 
     * @param obj
     *            the object to be serialized into xml string
     * @param charEncoding
     *            the encoding used to create the returned string e.g., UTF-8
     * @return A string which is the xml serialized version of the specified
     *         object
     * @exception UnsupportedEncodingException if the specified char encoding
     *             is not supported
     * @see java.beans.XMLEncoder
     * @see java.nio.charset.Charset
     */
    public static final String marshallIntoXML(Object obj, String charEncoding) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024 * 8);// 8k,
        // typical
        // size
        // of
        // a
        // request

        XMLEncoder encoder = new XMLEncoder(outStream);
        encoder.writeObject(obj);
        encoder.close();

        String data = outStream.toString(charEncoding);
        outStream.close();
        return data;
    }

    /**
     * Serializes the specified object into xml string using java bean
     * XMLEncoder Uses the specified charEncoding to convert the bytes into
     * string and writes into the specified outputstream.
     * 
     * @param obj
     *            the object to be serialized into xml string
     * @param charEncoding
     *            the encoding used to create the returned string e.g., UTF-8
     * @return A string which is the xml serialized version of the specified
     *         object
     * @exception UnsupportedEncodingException
     *                if the specified char encoding is not supported
     * @see java.beans.XMLEncoder
     * @see java.nio.charset.Charset
     */
    public static final void marshallIntoXML(Object obj, OutputStream outStream, String charEncoding) throws IOException {
        XMLEncoder encoder = new XMLEncoder(outStream);
        encoder.writeObject(obj);
        encoder.close();
    }

    public static final void marshallIntoXML(Object obj, File fileSrc, String charEncoding) throws IOException {
        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(fileSrc));
        marshallIntoXML(obj, outStream, charEncoding);
        outStream.close();
    }

    public static final Object unmarshallFromXML(File fileSrc, String charEncoding) throws IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileSrc));
        Object obj = unmarshallFromXML(input, charEncoding);
        input.close();

        return obj;
    }

    /**
     * de-Serializes the specified xml stream using java bean XMLDecoder into
     * Object Uses the specified charEncoding to convert the bytes into string
     * 
     * @param xmlString
     *            the serialized version of the object (serialized using
     *            java.beans.XMLEncoder)
     * @param charEncoding
     *            the encoding used to serialize the object in the first place
     *            e.g., UTF-8
     * @return the unmarshalled object
     * @exception UnsupportedEncodingException
     *                if the specified char encoding is not supported
     */
    public static final Object unmarshallFromXML(InputStream inStream, String charEncoding) throws IOException {
        XMLDecoder decoder = new XMLDecoder(inStream);
        Object obj = decoder.readObject();
        decoder.close();
        return obj;
    }

    /**
     * de-Serializes the specified xml string using java bean XMLDecoder into
     * Object Uses the specified charEncoding to convert the bytes into string
     * 
     * @param xmlString
     *            the serialized version of the object (serialized using
     *            java.beans.XMLEncoder)
     * @param charEncoding
     *            the encoding used to serialize the object in the first place
     *            e.g., UTF-8
     * @return the unmarshalled object
     * @exception UnsupportedEncodingException
     *                if the specified char encoding is not supported
     */
    public static final Object unmarshallFromXML(String xmlString, String charEncoding) throws IOException {

        ByteArrayInputStream inStream = new ByteArrayInputStream(xmlString.getBytes(charEncoding));

        XMLDecoder decoder = new XMLDecoder(inStream);
        Object obj = decoder.readObject();
        decoder.close();

        inStream.close();
        return obj;
    }
    
    public static final BigInteger toBigInteger(String hexString)
    {
    	return hexString == null ? null : new BigInteger( hexToByteArray(hexString) );
    }
    
    public static String toHexString(BigInteger bigint)
    {
    	return toHexString( bigint.toByteArray() );
    }

    public static String toHexString(byte[] value) {
    	
    	if(value == null) return null;

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < value.length; i++) {
            int ithValue = ((int) value[i]) & 0xFF;

            int lowerByte = ithValue & 0xF;
            int higherByte = ithValue >> 4 & 0xF;

        	buffer.append(Integer.toHexString(higherByte));
        	buffer.append(Integer.toHexString(lowerByte)); // lower case only
        }
        
        return buffer.toString();
    }
    
    public static byte[] hexToByteArray(String hexString) 
    {
        char[] charBuffer = hexString.toCharArray();
        if (charBuffer.length % 2 != 0) 
        {
            throw new IllegalArgumentException("not a valid hex dump, even length expected, found " + charBuffer.length);
        }

        byte[] bytes = new byte[charBuffer.length / 2];
        for (int i = 0, j = 0; j < bytes.length; j++) 
        {
            String jthByte = new String(charBuffer, i, 2);
            bytes[j] = (byte) Integer.parseInt(jthByte, 16);
            i += 2;
        }

        return bytes;
    }
	
    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height)
    {
        //XXX: Image type needs to be verified
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
     
        return resizedImage;
    }

	/**
	 * resizes image maintaining aspect ratio. the max of the dimension of the
	 * original image is matched with the input dimension and the other
	 * dimension is caculated maintaining the aspect ratio
	 * 
	 * @param originalImage
	 * @param dim
	 * @return scaled instance of buffered image
	 */
    public static BufferedImage resizeImage(BufferedImage originalImage, int dim)
    {
    	int width = originalImage.getWidth();
    	int height = originalImage.getHeight();
    	
    	if(height >= width)
    	{
    		double ratio = (dim/(height * 1.0));
    		
    		height = dim;
    		width = (int) (width * ratio);
    	}
    	else
    	{
    		double ratio = (dim/(width * 1.0));
    		
    		width = dim;
    		height = (int) (height * ratio);
    	}
    	
    	BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
     
        return resizedImage;
    }
    
	static class FileHash implements Comparable<FileHash> {
		
		private BigInteger hash = null;
		private File file = null;
		
		FileHash(File f, BigInteger hash)
		{
			this.file = f;
			this.hash = hash;
		}
		
		public File getFile()
		{
			return file;
		}
		
		public BigInteger getHash()
		{
			return hash;
		}
		
		//defines the natural ordering
		public int compareTo(FileHash that)
		{
			return this.hash.compareTo(that.hash);
		}
	}
    
	/**
	 * Returns the md5 signature of the specified list of files.
	 * for single file stuff, return the md5 hash of tht file.
	 * for multi-files, calculate the md5 hash of each file,
	 * then sort the files w.r.t their md5 hash (to get a unique order)
	 * and then compute the combined md5 hash f this files in that sorted order
	 */
    public static BigInteger computeMD5Hash(File ... files) throws IOException
    {
    	if(files.length == 1)
    	{
    		return computeMD5Hash(files[0]);
    	}

    	FileHash[] fileList = new FileHash[files.length];
    	for(int i = 0;i < files.length; i++)
    	{
    		fileList[i] = new FileHash(files[i], computeMD5Hash(files[i]));
    	}

    	return computeMD5Hash(fileList);
    }
    
	private static BigInteger computeMD5Hash(FileHash[] fileList) throws IOException 
	{
		MessageDigest md = null;
		try 
		{
			md = MessageDigest.getInstance("MD5");
		} 
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
		
		// sort them with natural ordering
		Arrays.sort(fileList);

		FileInputStream fileIn = null;
		BufferedInputStream inStream = null;

		int bytesRead = -1;
		byte[] buffer = new byte[8192];// 8k
		
		for(int i = 0; i < fileList.length; i++)
		{
			try 
			{
	    		fileIn = new FileInputStream(fileList[i].getFile());
	    		inStream = new BufferedInputStream(fileIn);
				
				while ((bytesRead = inStream.read(buffer, 0, buffer.length)) != -1) 
				{
					md.update(buffer, 0, bytesRead);
				}
			} 
			finally
			{
	    		closeStream(inStream);
	    		closeStream(fileIn);
			}
		}
		
		return new BigInteger(md.digest());
	}
    
    public static BigInteger computeMD5Hash(File file) throws IOException
    {
    	FileInputStream fileIn = null;
    	BufferedInputStream inStream = null;
    	
    	try
    	{
    		fileIn = new FileInputStream(file);
    		inStream = new BufferedInputStream(fileIn,16*1024);
    		
    		return computeMD5Hash(inStream);
    	}
    	finally
    	{
    		closeStream(inStream);
    		closeStream(fileIn);
    	}
    }
    
    public static BigInteger computeMD5Hash(String data) throws IOException {
    	ByteArrayInputStream bStream= new ByteArrayInputStream(data.getBytes());
		return Util.computeMD5Hash(bStream);
	}
	
	public static BigInteger computeMD5Hash(InputStream inStream) throws IOException
	{
		 byte[] md5value = null;
	     try 
	     {
	    	 MessageDigest md = MessageDigest.getInstance("MD5");

             int bytesRead = -1;
             byte[] buffer = new byte[256*1024];//64k
             
             while((bytesRead = inStream.read(buffer, 0, buffer.length)) != -1)
             {
            	 md.update(buffer, 0, bytesRead);
             }
             
	         md5value = md.digest();//128 bit number
	         buffer = null;
	         md     = null;
	     } 
	     catch (NoSuchAlgorithmException e) 
	     {
	    	 throw new RuntimeException(e);
	     }
	     return new BigInteger(md5value);
	}
	
	public static String getMachineAddress() 
	{
		String address = null;
		try
		{
			InetAddress localhost = InetAddress.getLocalHost();
			if (!localhost.isLoopbackAddress())
			{
				// if InetAddress returns correct ip
				NetworkInterface nw = NetworkInterface.getByInetAddress(localhost);
				byte[] mac = nw.getHardwareAddress();
				address = Util.toHexString(mac);
			}
			else
			{
				// loop over all the interfaces
				Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces();
				while(allInterfaces.hasMoreElements())
				{
					NetworkInterface ni = allInterfaces.nextElement();
					if(ni.isVirtual() || ni.isLoopback() || ni.getHardwareAddress() == null || !ni.isUp())
						continue;
					
					if(ni.isUp())
					{
						String temp = toHexString(ni.getHardwareAddress());
						if(address == null) 
						{
							address = temp;
						}
						else 
						{
							address = address.compareTo(temp) < 0 ? address : temp;
						}
					}
				}
			}
		}
		catch (UnknownHostException e1)
		{
			e1.printStackTrace();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		
		return address;
	}
	
	public static String getMachineIP() 
	{
		String address = null;
		try
		{
			InetAddress localhost = InetAddress.getLocalHost();
			if (!localhost.isLoopbackAddress())
			{
				// if correct ip address is fetched
				address = localhost.getHostAddress();
			}
			else
			{
				// else loop over all the interfaces and find out the active one
				Enumeration<NetworkInterface> intefaces = NetworkInterface.getNetworkInterfaces();
				while(intefaces.hasMoreElements())
				{
					NetworkInterface ni = intefaces.nextElement();
					
					if(ni.isVirtual() || ni.isLoopback() || ni.getHardwareAddress() == null || !ni.isUp())
						continue;
					
					Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
			        while(inetAddresses.hasMoreElements()) 
			        {
			            InetAddress ia = inetAddresses.nextElement();
			            if(!ia.isLoopbackAddress()) 
			            {
			                address = ia.getHostAddress();
			            }
			        }
				}
			}
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		return address;
	}
	
	public static long writeToFile(InputStream isteam, File file) throws IOException
	{
		FileOutputStream out = null;
		long noOfBytes = 0L;
		
		try 
		{
			out = new FileOutputStream(file);
			noOfBytes = transferData(isteam, out);
		} 
		finally 
		{
			out.close();
			isteam.close();
		}
		return noOfBytes;
	}
	
    public static long transferData(InputStream src, OutputStream sink) throws IOException {

        BufferedInputStream inStream = null;
        BufferedOutputStream outStream = null;

        if (src instanceof BufferedInputStream) 
        {
            inStream = (BufferedInputStream) src;
        }
        else 
        {
            inStream = new BufferedInputStream(src);
        }

        if (sink instanceof BufferedOutputStream) 
        {
            outStream = (BufferedOutputStream) sink;
        } 
        else 
        {
            outStream = new BufferedOutputStream(sink);
        }

        byte[] buffer = new byte[1024 * 16];
        int bytesRead = 0;
        long noOfBytes = 0L;

        while (true) 
        {
            bytesRead = inStream.read(buffer, 0, buffer.length);
            if (bytesRead == -1) 
            {
                break;
            }
            
            outStream.write(buffer, 0, bytesRead);
            noOfBytes += bytesRead;
        }

        outStream.flush();
        return noOfBytes;
    }
    
    public static File dumpToFile(Map<String, Object> dict, File file, String separator) throws IOException
    {
    	separator = separator == null ? "\t" : separator;
    	file = file == null ? File.createTempFile("dump", ".tsv") : file;
    	
    	PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    	for(Map.Entry<String, Object> entry : dict.entrySet())
    	{
    		writer.println(entry.getKey() + separator +entry.getValue());
    	}
    	writer.flush();
    	writer.close();
    	
    	return file;
    }
    
    public static File dumpToFile(String data, File file) throws IOException
    {
    	file = file == null ? File.createTempFile("dump", ".txt") : file;
    	
    	PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    	writer.println(data);
    	writer.close();
    	
    	return file;
    }
    
	public static final long calculateSize(File archiveRoot) 
	{
		if(archiveRoot.isFile()) return archiveRoot.length();
		
		long size = 0;
        File[] contentsList = archiveRoot.listFiles();

        if(contentsList != null)
        {
	        for (File member : contentsList) {
	
	            if (member.isDirectory()) 
	            {
	            	size += calculateSize(member);
	            } 
	            else 
	            {
	                size += member.length();
	            }
	        }
        }
        return size;
	}
    
    /**
     * moves the content of the source folders to the destination folder
     * @param srcFolder
     * @param destinationFolder
     * @return
     */
	public static final void move(File srcFolder, File destinationFolder) throws IOException 
	{
		srcFolder = srcFolder.getAbsoluteFile();
		destinationFolder = destinationFolder.getAbsoluteFile();
		
		if(!srcFolder.isDirectory() || !destinationFolder.isDirectory())
			throw new RuntimeException("source "+srcFolder +" and destination "+destinationFolder +" must be existing folders");
		

		File[] contentsList = srcFolder.listFiles();

        if(contentsList != null)
        {
	        for (File member : contentsList) {
	
	            if (member.isDirectory()) 
	            {
	            	File target = new File(destinationFolder, member.getName());
	            	target.mkdir();
	            	
	            	move(member, target);
	            } 
	            else 
	            {
	            	File target = new File(destinationFolder, member.getName());
	            	//true if and only if the renaming succeeded; false otherwise 
	                boolean renameSuccessful = member.renameTo(target);
	                if(!renameSuccessful)
	                {
	                	copy(member, target); //do a physical copy
	                	member.delete();
	                }
	            }
	        }
        }
	}
	
	/**
     * moves the content of the source folders to the destination folder
     * @param srcFolder
     * @param destinationFolder
     * @return
     */
	public static final void copyTree(File srcFolder, File destinationFolder) throws IOException 
	{
		srcFolder = srcFolder.getAbsoluteFile();
		destinationFolder = destinationFolder.getAbsoluteFile();
		
		if(!srcFolder.isDirectory() || !destinationFolder.isDirectory())
			throw new RuntimeException("source "+srcFolder +" and destination "+destinationFolder +" must be existing folders");
		

		File[] contentsList = srcFolder.listFiles();

        if(contentsList != null)
        {
	        for (File member : contentsList) {
	
	            if (member.isDirectory()) 
	            {
	            	File target = new File(destinationFolder, member.getName());
	            	target.mkdir();
	            	
	            	copyTree(member, target);
	            } 
	            else 
	            {
	            	File target = new File(destinationFolder, member.getName());
	                copy(member, target); //do a physical copy
	            }
	        }
        }
	}
	
    /**
     * copies the content of source file to the destination file by physically
     */
    public static final void copy(File src, File des) throws IOException {

        if (!src.isFile()) return;

        int bufferSize = 1024 * 16; // 16k

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(src), bufferSize);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(des), bufferSize);

        byte[] buffer = new byte[bufferSize];
        int lengthRead = 0;

        while ((lengthRead = in.read(buffer, 0, bufferSize)) != -1) 
        {
            out.write(buffer, 0, lengthRead);
        }

        closeStreams(in, out);
    }

	  /**
     * This method deletes an entire folder tree structure resursively. All the
     * files and folder within the requested folder are deleted. Finally the
     * requested folder is deleted.
     * 
     * @param File
     *            object of the folder to be deleted.
     * @return true - if folder was deleted false - if folder could not be
     *         deleted
     */
    public static boolean deleteTree(File folder) {

        File[] contentsList = folder.listFiles();

        if(contentsList != null)
        {
	        for (File member : contentsList) {
	
	            if (member.isDirectory()) 
	            {
	                deleteTree(member);
	                member.delete();
	            } 
	            else 
	            {
	                member.delete();
	            }
	        }
        }
        return folder.delete();
    }
    
    /*
     * delete only file not a folder
     */
    public static boolean delete(File file) 
    {
    	boolean isDeleted = false;
        try
        {
        	if(file != null)
        	{
        		isDeleted = file.delete();
        	}
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
		}
        return isDeleted;
    }
    
    /**
     *  Compares two objects safely allowing either or both of them to be null 
     */
    public static boolean safeEquals(Object str1, Object str2){
        if (str1 == null) {
            return str2 == null;
        } else if (str2 == null) {
            return false;
        } else {
            // both are not null
            return str1.equals(str2);
        }
    }

	/**
	* Returns a double containg the value represented by the 
	* specified <code>String</code>.
	*
	* @param      s   the string to be parsed.
	* @param      defaultValue   the value returned if <code>s</code>
	*	does not contain a parsable double
	* @return     The double value represented by the string argument or
	*	<code>defaultValue</code> if the string does not contain a parsable double
	*/
	public static double parseDouble(String s, double defaultValue) 
	{
		if (s==null) return defaultValue;
		try 
		{
			defaultValue = Double.parseDouble(s);
		} 
		catch (NumberFormatException e)
		{}
		return defaultValue;			
	}
	
	/**
	 * List of resources from a package
	 * @param packageName
	 * @return
	 * @throws IOException
	 */
	public static List<String> getResourceFromPackage(String packageName) throws IOException
	{
		packageName = packageName.replace(".", "/");//class loader works with / rather than .
		if(!packageName.endsWith("/")) packageName = packageName + "/";
		
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    List<String> names = new ArrayList<String>();;

	    //example: jar:file:/D:/svn/rep/imaging/iengine/trunk/lib/ienginedao.jar!/imagingdao/mysql
	    URL packageURL = classLoader.getResource(packageName);

	    if(packageURL.getProtocol().equals("jar"))
	    {
	        // build jar file name, then loop through zipped entries
	        // file:/D:/svn/rep/imaging/iengine/trunk/lib/ienginedao.jar!/imagingdao/mysql
	        String jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
	        // D:/svn/rep/imaging/iengine/trunk/lib/ienginedao.jar
	        jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
	        
	        JarFile jf = new JarFile(jarFileName);
	        Enumeration<JarEntry> jarEntries = jf.entries();
	        
	        while(jarEntries.hasMoreElements())
	        {
	        	String entryName = jarEntries.nextElement().getName();
	            if(entryName.startsWith(packageName) && entryName.length() > packageName.length())
	            {
	                entryName = entryName.substring(packageName.length());
	                names.add(entryName);
	            }
	        }
	        
	        jf.close();
	    }
	    else //for folder within the classpath
	    {
	        File folder = new File(packageURL.getFile());
	        File[] contenuti = folder.listFiles();
	        
	        for(File actual: contenuti)
	        {
	            names.add(actual.getName());
	        }
	    }
	    return names;
	}

	/**
	 * Utility to create a map with the given key value.
	 * 
	 * @param key key to use in the map
	 * @param value value to use in the map
	 * @return map with an entry for the given key-value
	 */
	public static Map<String, Object> createMap(String key, Object value) 
	{
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put(key, value);
	    return map;
	}
	  
	/**
     * Utility to create a map with the given two key values.
     * 
     * @param key1 key to use in the map
     * @param value1 value to use in the map
     * @param key2 key to use in the map
     * @param value2 value to use in the map
     * @return map with an entry for the given key-value
     */
    public static Map<String, Object> createMap(String key1, Object value1, String key2, Object value2) 
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    /**
     * Generate a random alphanumeric string. Copied and modified from 
     * apache:commons-lang:RandomStringUtils.java
     *  
     * @param count length of string required
     * @return random string of requested length
     */
    public static String generateRandomString (int count) 
    {
        int start = 0, end = 0;
        boolean letters = true, numbers = true;
        char[] chars = null;
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if (start == 0 && end == 0) {
            end = 'z' + 1;
            start = ' ';
            if (!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }

        char[] buffer = new char[count];
        int gap = end - start;

        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }
            if (letters && Character.isLetter(ch)
                    || numbers && Character.isDigit(ch)
                    || !letters && !numbers) {
                if(ch >= 56320 && ch <= 57343) {
                    if(count == 0) {
                        count++;
                    } else {
                        // low surrogate, insert high surrogate after putting it in
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char) (55296 + random.nextInt(128));
                    }
                } else if(ch >= 55296 && ch <= 56191) {
                    if(count == 0) {
                        count++;
                    } else {
                        // high surrogate, insert low surrogate before putting it in
                        buffer[count] = (char) (56320 + random.nextInt(128));
                        count--;
                        buffer[count] = ch;
                    }
                } else if(ch >= 56192 && ch <= 56319) {
                    // private high surrogate, no effing clue, so skip it
                    count++;
                } else {
                    buffer[count] = ch;
                }
            } else {
                count++;
            }
        }
        return new String(buffer);
    }
    
    private static boolean isValidPassword(String password)
	{
		return Util.containsDigit(password) && Util.containsLowerCase(password) && Util.containsUpperCase(password) && Util.containsSpecialCharacter(password) && !Util.containsSpace(password);
	}
    
    public static String encodeHtml(String text)
    {
    	return StringEscapeUtils.escapeHtml4(text).toString();
    }
    
	public static void main(String ... args) throws Exception
    {
    }
	
	public static long[] toLongArray(List<Long> list)
	{
		if(list == null || list.isEmpty())
			return null;
		
		long[] array = new long[list.size()];
		for(int i = 0;i < array.length; i++)
		{
			array[i] = list.get(i);
		}
		
		return array;
	}

	public static int[] toIntArray(List<Integer> list)
	{
		if(list == null || list.isEmpty())
			return null;
		
		int[] array = new int[list.size()];
		for(int i = 0;i < array.length; i++)
		{
			array[i] = list.get(i);
		}
		
		return array;
	}
	
}
