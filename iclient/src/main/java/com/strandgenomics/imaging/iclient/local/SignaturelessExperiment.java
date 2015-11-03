package com.strandgenomics.imaging.iclient.local;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * Client experiment for which signature computation will be done on server side:
 * this means on acquisition client same record can be imported multiple times
 * 
 * @author Anup Kulkarni
 */
public class SignaturelessExperiment extends RawExperiment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8880916882220772324L;

	public SignaturelessExperiment(File sourceFile) {
		super(sourceFile);
	}
	
	/**
	 *Returns a random bigInteger as hash value
	 */
	@Override
	public synchronized BigInteger getMD5Signature() 
	{
		if (md5Signature == null) {
			Logger.getRootLogger().info(" [Indexing] : Computing signature");
			
		   	Random rand = new Random();
		   	md5Signature = new BigInteger(32, rand);
			    
			Logger.getRootLogger().info(" [Indexing] : Done computing signature");
		}
		return md5Signature;
	}

}
