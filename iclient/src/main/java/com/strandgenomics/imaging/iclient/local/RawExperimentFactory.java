package com.strandgenomics.imaging.iclient.local;

import java.io.File;

/**
 * factory for creating experiment on client side
 * 
 * @author Anup Kulkarni
 */
public class RawExperimentFactory {

	/**
	 * returns appropriate client experiment
	 * @param sourceFile specified source file
	 * @param computeSignature true if md5 signature of the experiment is to be computed, false otherwise
	 * @return appropriate client experiment
	 */
	public static RawExperiment createExperiment(File sourceFile, boolean computeSignature)
	{
		if(!computeSignature)
        	return new SignaturelessExperiment(sourceFile);
        else
        	return new RawExperiment(sourceFile);
	}

}
