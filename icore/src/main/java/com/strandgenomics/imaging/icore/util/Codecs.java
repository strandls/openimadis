/*
 * Codecs.java
 *
 * AVADIS Image Management System
 * Core Engine
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

import java.util.BitSet;

/**
 * This class collects base64 encoder and decoder.
 * @author arunabha
 *
 */
public class Codecs {
	
    private static BitSet  BoundChar;
    private static byte[]  Base64EncMap, Base64DecMap;

    static  {// static Class Initializer

        // rfc-1521: bcharsnospace - used for multipart codings
        BoundChar = new BitSet(256);
        for (int ch='0'; ch <= '9'; ch++)  BoundChar.set(ch); // 48 to 57
        for (int ch='A'; ch <= 'Z'; ch++)  BoundChar.set(ch); // 65 to 90
        for (int ch='a'; ch <= 'z'; ch++)  BoundChar.set(ch); // 97 to 122
        BoundChar.set('\''); // 92
        BoundChar.set('(');  // 40
        BoundChar.set(')');  // 41
        BoundChar.set('+');  // 43
        BoundChar.set(',');  // 44
        BoundChar.set('-');  // 45
        BoundChar.set('.');  // 46
        BoundChar.set('/');  // 47
        BoundChar.set(':');  // 58
        BoundChar.set('=');  // 61
        BoundChar.set('?');  // 63
        BoundChar.set('_');  // 95

        // rfc-521: Base64 Alphabet , #of characters is 64
        byte[] map = {
            (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F',
            (byte)'G', (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L',
            (byte)'M', (byte)'N', (byte)'O', (byte)'P', (byte)'Q', (byte)'R',
            (byte)'S', (byte)'T', (byte)'U', (byte)'V', (byte)'W', (byte)'X',
            (byte)'Y', (byte)'Z',
            (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f',
            (byte)'g', (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l',
            (byte)'m', (byte)'n', (byte)'o', (byte)'p', (byte)'q', (byte)'r',
            (byte)'s', (byte)'t', (byte)'u', (byte)'v', (byte)'w', (byte)'x',
            (byte)'y', (byte)'z',
            (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
            (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/' };

        Base64EncMap = map;
        Base64DecMap = new byte[128];
        for (int idx=0; idx<Base64EncMap.length; idx++)
            Base64DecMap[Base64EncMap[idx]] = (byte) idx;
    }

    /**
     * This class isn't meant to be instantiated.
     */
    private Codecs()
    {}

    /**
     * This method encodes the given string using the base64-encoding
     * specified in RFC-1521 (Section 5.2). It's used for example in the
     * "Basic" authorization scheme.
     *
     * @param  str the string
     * @return the base64-encoded <var>str</var>
     */
    public final static String base64Encode(String str){
    
        if (str == null)  return  null;

        byte data[] = str.getBytes();
        return new String(base64Encode(data));
    }


    /**
     * This method encodes the given byte[] using the base64-encoding
     * specified in RFC-1521 (Section 5.2).
     *
     * @param  data the data
     * @return the base64-encoded <var>data</var>
     */
    public final static byte[] base64Encode(byte[] data){
    
        if(data == null) return null;

        int sidx, didx;
        byte dest[] = new byte[((data.length+2)/3)*4];

        // 3-byte to 4-byte conversion + 0-63 to ascii printable conversion
        for (sidx=0, didx=0; sidx < data.length-2; sidx += 3){
        
            dest[didx++] = Base64EncMap[(data[sidx] >>> 2) & 077];
            dest[didx++] = Base64EncMap[(data[sidx+1] >>> 4) & 017 |
                        (data[sidx] << 4) & 077];
            dest[didx++] = Base64EncMap[(data[sidx+2] >>> 6) & 003 |
                        (data[sidx+1] << 2) & 077];
            dest[didx++] = Base64EncMap[data[sidx+2] & 077];
        }

        if (sidx < data.length){

            dest[didx++] = Base64EncMap[(data[sidx] >>> 2) & 077];
            if (sidx < data.length-1){
            
                dest[didx++] = Base64EncMap[(data[sidx+1] >>> 4) & 017 |
                                (data[sidx] << 4) & 077];
                dest[didx++] = Base64EncMap[(data[sidx+1] << 2) & 077];
            }
            else
                dest[didx++] = Base64EncMap[(data[sidx] << 4) & 077];
        }

        // add padding
        for ( ; didx < dest.length; didx++)
            dest[didx] = (byte) '=';

        return dest;
    }


    /**
     * This method decodes the given string using the base64-encoding
     * specified in RFC-1521 (Section 5.2).
     *
     * @param  str the base64-encoded string.
     * @return the decoded <var>str</var>.
     */
    public final static String base64Decode(String str){
   
        if (str == null) return null;

        byte data[] = str.getBytes();
        return new String(base64Decode(data));
    }


    /**
     * This method decodes the given byte[] using the base64-encoding
     * specified in RFC-1521 (Section 5.2).
     *
     * @param  data the base64-encoded data.
     * @return the decoded <var>data</var>.
     */
    public final static byte[] base64Decode(byte[] data){
    
        if(data == null) return null;

        int tail = data.length;
        while (data[tail-1] == '=') tail--;  // remove padding

        byte dest[] = new byte[tail - data.length/4]; // 75% 

        // ascii printable to 0-63 conversion
        for (int idx = 0; idx <data.length; idx++)
            data[idx] = Base64DecMap[data[idx]];

        // 4-byte to 3-byte conversion
        int sidx, didx;
        for (sidx = 0, didx=0; didx < dest.length-2; sidx += 4, didx += 3){
        
            dest[didx]   = (byte) ( ((data[sidx] << 2) & 255) |
                    ((data[sidx+1] >>> 4) & 003) );
            dest[didx+1] = (byte) ( ((data[sidx+1] << 4) & 255) |
                    ((data[sidx+2] >>> 2) & 017) );
            dest[didx+2] = (byte) ( ((data[sidx+2] << 6) & 255) |
                    (data[sidx+3] & 077) );
        }

        if (didx < dest.length)
            dest[didx]   = (byte) ( ((data[sidx] << 2) & 255) |
                    ((data[sidx+1] >>> 4) & 003) );
        if (++didx < dest.length)
            dest[didx]   = (byte) ( ((data[sidx+1] << 4) & 255) |
                    ((data[sidx+2] >>> 2) & 017) );

        return dest;
    }
}
