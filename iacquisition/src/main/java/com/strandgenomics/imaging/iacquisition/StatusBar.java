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

package com.strandgenomics.imaging.iacquisition;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JLabel;

import com.strandgenomics.imaging.iacquisition.selection.IRecordListener;
import com.strandgenomics.imaging.iacquisition.selection.IRecordSelectionListener;
import com.strandgenomics.imaging.iacquisition.selection.RecordSelectionEvent;
import com.strandgenomics.imaging.iacquisition.selection.RecordsChangedEvent;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.local.IndexerListener;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;

public class StatusBar extends JLabel implements IndexerListener, IRecordSelectionListener, IRecordListener{

	private static final long serialVersionUID = 7603061667492119562L;

	private Context context;
    private String status;

	/** Creates a new instance of StatusBar */
    public StatusBar(Context context) {
        super();
        super.setPreferredSize(new Dimension(100, 16));
        this.context = context;
        status = "";
    }
    
    public void updateStatus() {
    	String user = ImageSpaceObject
		.getConnectionManager().getUser();
    	int recordCount = context.getRecordCount();
    	int selectedRecordCount = context.getCheckedRecordsCount();
    	String text =  "User: " + user + " Project: " + context.getProject() + " Total Records: " + recordCount + " Selected Records: " + selectedRecordCount + " Status: " + status;
        setText(" "+text);        
    }      

    
	@Override
    public void indexingStarted()
    {
		status = "Import in Progress";
		updateStatus();
    }
    
	@Override
    public void indexingComplete()
    {
		//if (context.getImportQueueSize()==0)
		status = "Import Complete";
		
		updateStatus();
    }

	@Override
	public void indexed(IRecord record) {
		updateStatus();
	}

	@Override
	public void selectionChanged(RecordSelectionEvent selectionEvent) {
		updateStatus();
	}

	@Override
	public void recordsChanged(RecordsChangedEvent e) {
		updateStatus();
	}

	@Override
	public void indexingFailed(boolean serverError) {
		//if (context.getImportQueueSize()==0)
		status = "Import Failed";
		
		updateStatus();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignoredDuplicate(IExperiment experiment)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void experimentFound(List<RawExperiment> experimentList)
	{
		status = "Archive Created";
		
		updateStatus();
	}
}
