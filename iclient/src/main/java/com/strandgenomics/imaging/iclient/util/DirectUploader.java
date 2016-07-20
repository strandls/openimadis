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
