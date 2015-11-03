package com.strandgenomics.imaging.iclient.local;

import java.io.File;

import com.strandgenomics.imaging.icore.IValidator;

/**
 * Import request for creating experiment without md5 signature computation, on client side
 * 
 * @author Anup Kulkarni
 */
public class SignaturelessImportRequest extends DefaultImportRequest{

	public SignaturelessImportRequest(File file, boolean recursive, IValidator validator)
	{
		super(file, recursive, validator);
		
		actualIndexer = new SignaturelessIndexer();
	}
}
