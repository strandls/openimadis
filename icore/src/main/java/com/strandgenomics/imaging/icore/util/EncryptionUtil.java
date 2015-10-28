/*
 * EncryptionUtil.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * useful methods for encryptions
 * @author arunabha
 *
 */
public class EncryptionUtil {
    /**
     * Compute the MD5 on the content of the specified file
     */
    public static final byte[] computeMessageDigest(File file){

        byte[] md5value = null;
        BufferedInputStream reader = null;

        try {
            reader = new BufferedInputStream(new FileInputStream(file));
            MessageDigest md = MessageDigest.getInstance("MD5");

            int bytesRead = -1;
            byte[] buffer = new byte[8192];//8k
            while((bytesRead = reader.read(buffer, 0, buffer.length)) != -1){
                md.update(buffer, 0, bytesRead);
            }
            md5value = md.digest();//128 bit number

            buffer = null;
            md     = null;
        }
        catch(Exception ioex){
            System.out.println(ioex);
        }
        finally {
            try{
                if(reader != null) reader.close();
                reader = null;
            }
            catch(Exception ex)
            {}
        }
        return md5value;
    }

    /**
     * Returns a 128 bit or 16 byte long MD5 digest
     */
    public static byte[] computeMessageDigest(String str) {
        try {
            if(str == null) return null;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            return md.digest();
        }
        catch(Exception ex){
            return null;
        }
    }

    /**
     * Returns a reconstructed DES (Digital Encryption Standard) secret key
     */
    public static Key getSymmetricKey(byte[] keyBytes) 
    {
        try {
            DESKeySpec pass = new DESKeySpec( keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            return keyFactory.generateSecret(pass);
        }
        catch( Exception e ){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recreate RSA private key
     */
    public static PrivateKey getPrivateKey(BigInteger modulus, BigInteger privateExponent)
    {
        try {
            RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, privateExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recreate RSA public key
     */
    public static PublicKey getPublicKey(BigInteger modulus, BigInteger publicExponent)
    {
        try {
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generate DES (Digital Encryption Standard) secret key
     */
    public static Key generateSymmetricKey()
    {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("DES");
            return keygen.generateKey();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns an array of public and private RSA keys
     * the first element of the array is the public key, and the second element is the private key
     */
    public static Key[] generateAsymmetricKeys() 
    {
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize( 2048 );
            KeyPair pair = keyGen.generateKeyPair();

            Key[] asymmetricKeys = new Key[2];

            asymmetricKeys[0] = pair.getPublic();
            asymmetricKeys[1] = pair.getPrivate();

            return asymmetricKeys;
        }
        catch(Exception e ){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * encrypt the specified data with the given SecretKey
     */
    public static byte[] encryptSymmetric(byte[] cleartext, Key key) 
    {
        try {
            // Create the cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE, key);
            // Encrypt the cleartext
            return desCipher.doFinal(cleartext);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * decrypt the specified encrypted data with the given SecretKey
     */
    public static byte[] decryptSymmetric(byte[] encryptedData, Key key) 
    {
        try {
            // Create the cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // Initialize the same cipher for decryption
            desCipher.init(Cipher.DECRYPT_MODE, key);
            // Decrypt the encrypted Data
            return desCipher.doFinal(encryptedData);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptKey(Key symmetricKey, Key publicKey) 
    {
        byte[] keyBytes = symmetricKey.getEncoded();
        return encryptAsymmetric(keyBytes, publicKey);
    }

    /**
     * do an asymmetric encryption to the specified data with the given RSA key
     */
    public static byte[] encryptAsymmetric(byte[] data, Key publicKey) 
    {
        try {
            //Create cipher for asymmetric encryption (RSA)
            Cipher rsaCipher = Cipher.getInstance("RSA");
            //Initialize cipher for encryption with public key
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //Encrypt the data
            return rsaCipher.doFinal(data);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * do an asymmetric encryption to the specified data with the given RSA key
     */
    public static byte[] decryptAsymmetric(byte[] data, Key privateKey) 
    {
        try {
            //Create cipher for asymmetric encryption (RSA)
            Cipher rsaCipher = Cipher.getInstance("RSA");
            //Initialize cipher for decryption with private key
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            //Decrypt the data
            return rsaCipher.doFinal(data);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] encryptedSymmetricKey, Key privateKey)
    {
        try {
            //Decrypt symmetric key with the private key
            byte[] symmetricKeyBytes = decryptAsymmetric(encryptedSymmetricKey, privateKey);
            //generate the symmetric key
            Key symmetricKey = getSymmetricKey(symmetricKeyBytes);
            // decrypt data with this symmetric key
            return decryptSymmetric(encryptedData, symmetricKey);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception 
    {
        
        Key secretKey = generateSymmetricKey();
        byte[] keyBytes = secretKey.getEncoded();
        System.out.println("keyBytes length "+keyBytes.length +" biginteger = "+new BigInteger(keyBytes) );
        
        Key secretKey2 = getSymmetricKey(keyBytes);
        System.out.println(" biginteger = "+new BigInteger(secretKey2.getEncoded()) );

        byte[] cleartext = args[0].getBytes("UTF-8");
        
        byte[] encryptedData = encryptSymmetric(cleartext, secretKey);
        byte[] data = decryptSymmetric(encryptedData, secretKey2);
        
        System.out.println( new String(data, "UTF-8") );
        
        /*
        byte[] source = args[0].getBytes("UTF-8");
        //get a symmetric secret key
        Key secretKey = generateSymmetricKey();
        //entrypt data with symmetric key
        byte[] encodedData = encryptSymmetric(source, secretKey);
        //generate a pair of public and private asymmetric keys
        Key[] publicPrivateKeys = generateAsymmetricKeys(); //key[0] is the public key

        Key publicKey  = publicPrivateKeys[0];
        Key privateKey = publicPrivateKeys[1];
        
        
        BigInteger publicModulus  = ((RSAPublicKey)publicKey).getModulus();
        BigInteger publicExponent = ((RSAPublicKey)publicKey).getPublicExponent();
        
        BigInteger publicModulus  = ((RSAPublicKey)privateKey).getModulus();
        BigInteger publicExponent = ((RSAPublicKey)privateKey).getPublicExponent();

        //encode the symmetric key with the public asymmetric key
        byte[] encodedSKey = encryptKey(secretKey, publicKey);
        //decode the data
        byte[] value = decrypt(encodedData, encodedSKey, privateKey);

        System.out.println("decoded stuff="+new String(value, "UTF-8"));

        String publicKeyStr  = Util.toHexString( publicKey.getEncoded() );
        String privateKeyStr = Util.toHexString( privateKey.getEncoded() );

        System.out.println("publicKey="+publicKeyStr);
        System.out.println("privateKey="+privateKeyStr);

        BigInteger modulus  = ((RSAPublicKey)publicKey).getModulus();
        BigInteger exponent = ((RSAPublicKey)publicKey).getPublicExponent();

        System.out.println("modulus="+modulus);
        System.out.println("exponent="+exponent);

        String hexModulus  = Util.toHexString(modulus.toByteArray());
        String hexExponent = Util.toHexString(exponent.toByteArray());

        System.out.println("modulus hex="+hexModulus);
        System.out.println("exponent hex="+hexExponent);

        BigInteger t1 = new BigInteger(Util.toByteArray(hexModulus));
        BigInteger t2 = new BigInteger(Util.toByteArray(hexExponent));

        System.out.println("modulus="+t1);
        System.out.println("exponent="+t2);
        */
    }

}
