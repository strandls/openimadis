package com.strandgenomics.imaging.iclient.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.local.DirectUploadExperiment;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.UploadObserver;

/**
 * Uploader for direct source files uploading to the server
 * 
 * @author Anup Kulkarni
 */
public class DirectUploader extends Uploader{

	public DirectUploader(Project project, RawExperiment experiment)
	{
		super(project, experiment);
	}
	
	/**
	 * all the source files are tarred together for uploading
	 * 
	 * @return tar files
	 */
	public File packSourceFiles() 
	{
		try 
		{
			File archiveFile = null;
			File rootFolder = new File(experiment.getRootDirectory());
			try
			{
				archiveFile = File.createTempFile(rootFolder.getName(), "tar.gz");
				
				List<ISourceReference> sources = experiment.getReference();
				File[] sourceFiles = new File[sources.size()];
				int i = 0;
				for(ISourceReference source: sources)
				{
					sourceFiles[i] = new File(source.getSourceFile());
					i++;
				}
				
				if (rootFolder.isDirectory())
					Archiver.createTarBall(archiveFile, false, sourceFiles);
				else
					Archiver.createTarBall(archiveFile, false, sourceFiles);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				if(archiveFile!=null)
					archiveFile.delete();
			}
			return archiveFile;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * there are no record fields in case of direct source files upload 
	 */
	public boolean uploadRecordFields(UploadObserver uploaderTask)
	{
		return true;
	}
}
