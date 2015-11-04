package com.strandgenomics.imaging.iviewer;

import javax.swing.SwingWorker;

public class MovieWorker extends SwingWorker {

	private ImageViewerApplet iviewer;
	
	private boolean movieOnFrame = true;

	public MovieWorker(ImageViewerApplet iviewer, boolean movieOnFrame) {
		this.iviewer = iviewer;
		this.movieOnFrame = movieOnFrame;
	}

	@Override
	protected Object doInBackground() throws Exception {
		if(movieOnFrame)
		{
			int i = iviewer.getFrame();
			while(i<iviewer.getMaxFrame()){
				iviewer.setFrame(i);
				Thread.sleep(100);

				if(i == iviewer.getMaxFrame()-1)
					i = -1;
				i++;
			}
		}
		else
		{
			int i = iviewer.getSlice();
			while(i<iviewer.getMaxSlice()){
				iviewer.setSlice(i);
				Thread.sleep(100);

				if(i == iviewer.getMaxSlice()-1)
					i = -1;
				i++;
			}
		}
		
		return null;
	}

}
