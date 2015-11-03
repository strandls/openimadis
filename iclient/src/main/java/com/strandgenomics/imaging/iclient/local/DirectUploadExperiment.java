package com.strandgenomics.imaging.iclient.local;

import java.io.File;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class DirectUploadExperiment extends SignaturelessExperiment{

	/**
	 * 
	 */
	private static final long serialVersionUID = -473256384473178179L;
	
	protected String rootDirectory; 

	public DirectUploadExperiment(File archiveFile, File selectedFile)
	{
		super(archiveFile);
		
		if(selectedFile.isDirectory())
		{
			rootDirectory = selectedFile.getPath();
			for (File rootFile : selectedFile.listFiles())
				sourceReferences.add(new RawSourceReference(rootFile));
		}
		else
		{
			sourceReferences.add(new RawSourceReference(selectedFile));
			rootDirectory = selectedFile.getParent().toString();
		}
	}
	
	@Override
	public String getRootDirectory()
	{
		return rootDirectory;
	}

}
