package com.strandgenomics.imaging.iclient.local;

/**
 * class indexing without calculating MD5 checksum of the source files
 * 
 * @author Anup Kulkarni
 */
public class SignaturelessIndexer extends Indexer{

	public SignaturelessIndexer()
	{
		this.computeSignature = false;
	}
}
