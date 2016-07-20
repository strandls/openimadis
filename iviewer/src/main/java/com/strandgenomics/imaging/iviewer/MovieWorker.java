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
