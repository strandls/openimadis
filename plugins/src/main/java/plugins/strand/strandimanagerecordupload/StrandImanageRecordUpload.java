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

package plugins.strand.strandimanagerecordupload;

import icy.gui.dialog.MessageDialog;
import icy.plugin.abstract_.Plugin;
import icy.plugin.interface_.PluginImageAnalysis;
import icy.sequence.Sequence;
import icy.type.DataType;
import icy.gui.frame.progress.ProgressFrame;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.PixelMetaData;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.iclient.RecordBuilder;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelDepth;

/**
 * Plugin to connect to Avadis iManage server and upload the currently focused sequence to the server
 * 
 * @author Anup Kulkarni
 */
public class StrandImanageRecordUpload extends Plugin implements PluginImageAnalysis {


	@Override
	public void compute() {
		
		Thread t = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				Sequence sequence = getFocusedSequence();
				if(sequence == null)
				{
					MessageDialog.showDialog("No data to upload");
					return;
				}

				ImageSpace ispace = ImageSpaceObject.getConnectionManager();
				List<Project> projectList = ispace.getActiveProjects();
				
				String[] listofproject = null;
				if (projectList != null)
				{
					listofproject = new String[projectList.size()];
					int i = 0;
					for (Project p : projectList)
					{
						listofproject[i] = p.getName();
						i = i + 1;
					}
				}
				else
				{
					MessageDialog.showDialog("No active project");
					return;
				}
		        
				UploadDialog pd = new UploadDialog(listofproject);
				int value = JOptionPane.showConfirmDialog(null, pd, "Upload Record to Database", JOptionPane.PLAIN_MESSAGE);
				if(value != JOptionPane.OK_OPTION)
					return;
				
				//set upload progress bar
				ProgressFrame upload=new ProgressFrame("Uploading");
				upload.setLength(sequence.getSizeT()*sequence.getSizeZ()*sequence.getSizeC());
				
				List<Channel> channels = new ArrayList<Channel>();
				for(int i=0;i<sequence.getSizeC();i++)
				{
					String name = sequence.getChannelName(i);
					Channel channel = new Channel(name);
					
					channels.add(channel);
				}
				
				List<Site> sites = new ArrayList<Site>();
				sites.add(new Site(0, "Site 0"));
				
				if(pd.getRecordLabel().equals("")){
					MessageDialog.showDialog("Please provide name for record to be uploaded");
					
					//close upload progress bar
					upload.close();
					
					return;
				}
				
				Project prj = ispace.findProject(pd.getProjectName());
				RecordBuilder rb = prj.createRecordBuilder(pd.getRecordLabel(), sequence.getSizeT(), sequence.getSizeZ(), channels, sites , sequence.getWidth(), sequence.getHeight(), getPixelDepth(sequence.getDataType_()), 1.0, 1.0, 1.0, ImageType.GRAYSCALE, new SourceFormat("IMG"), "", "/tmp", System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis());
				
				for(int time = 0; time<sequence.getSizeT();time++)
				{
					for(int slice = 0;slice<sequence.getSizeZ();slice++)
					{
						for(int channel = 0;channel<sequence.getSizeC();channel++)
						{
							
							PixelArray rawData = null;
							if(getPixelDepth(sequence.getDataType_()) == PixelDepth.BYTE)
								rawData = new PixelArray.Byte(((byte[])sequence.getDataCopyXY(time, slice, channel)), sequence.getWidth(), sequence.getHeight());
							else if(getPixelDepth(sequence.getDataType_()) == PixelDepth.SHORT)
								rawData = new PixelArray.Short(((short[])sequence.getDataCopyXY(time, slice, channel)), sequence.getWidth(), sequence.getHeight());
							else if(getPixelDepth(sequence.getDataType_()) == PixelDepth.INT)
								rawData = new PixelArray.Integer(((int[])sequence.getDataCopyXY(time, slice, channel)), sequence.getWidth(), sequence.getHeight());
							else
							{
								MessageDialog.showDialog("unknown type");
								return;
							}
							
							PixelMetaData pixelData = new PixelMetaData(new Dimension(time, slice, channel, 0), 1.0, 1.0, 1.0, 1.0, 1.0, new Date());
							
							rb.addImageData(new Dimension(time, slice, channel, 0), rawData, pixelData );
							
							upload.incPosition();
						}
					}
				}
				
				Record r = rb.commit();
				
				//close upload progress bar
				upload.close();
				
				MessageDialog.showDialog("Record "+r.getGUID()+" has been uploaded successfully");
			}
		});
		t.start();
		
		
	}

	private PixelDepth getPixelDepth(DataType dataType_)
	{
		if(dataType_ == DataType.BYTE || dataType_ == DataType.UBYTE)
			return PixelDepth.BYTE;
		if(dataType_ == DataType.SHORT || dataType_ == DataType.USHORT)
			return PixelDepth.SHORT;
		if(dataType_ == DataType.INT || dataType_ == dataType_.UINT)
			return PixelDepth.INT;
		throw new IllegalArgumentException("unknown data type");
	}
	
	
	private class UploadDialog extends JPanel {

		private JTextField record;
		private JComboBox project;
		
		String[] projects;

		public UploadDialog(String[] projects)
		{
			this.projects = projects;
			
			setLayout(new GridLayout(2, 2, 5, 5));

			setupUI();
		}

		private void setupUI()
		{
			JLabel projectNameLabel = new JLabel("Project Name");
			project = new JComboBox(projects);
			
			JLabel recordLabel = new JLabel("Record Name");
			record = new JTextField();

			add(projectNameLabel);
			add(project);
			
			add(recordLabel);
			add(record);
		}

		public String getProjectName()
		{
			return (String) this.project.getSelectedItem();
		}

		public String getRecordLabel()
		{
			return this.record.getText();
		}
	}

}
