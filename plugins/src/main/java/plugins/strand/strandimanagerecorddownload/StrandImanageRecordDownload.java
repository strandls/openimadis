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

package plugins.strand.strandimanagerecorddownload;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.image.PixelDepth;

import icy.gui.dialog.MessageDialog;
import icy.image.IcyBufferedImage;
import icy.plugin.abstract_.Plugin;
import icy.plugin.interface_.PluginImageAnalysis;
import icy.sequence.Sequence;
import icy.type.DataType;
import icy.gui.frame.progress.ProgressFrame;

/**
 * ICY plugin for searching and downloading the records from Avadis iManage server.
 * 
 * @author Anup Kulkarni
 */
public class StrandImanageRecordDownload extends Plugin implements PluginImageAnalysis {

	@Override
	public void compute() {
		
		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					ImageSpace ispace = ImageSpaceObject.getConnectionManager();

					SearchDialog pd = new SearchDialog();
					int value = JOptionPane.showConfirmDialog(null, pd, "Search Record in Database", JOptionPane.PLAIN_MESSAGE);
					if (value != JOptionPane.OK_OPTION)
						return;
					
					if(pd.getSearchText().equals("")){
						MessageDialog.showDialog("Please provide text for search");
						return;
					}
					
					//start search progress bar
					ProgressFrame search=new ProgressFrame("Searching");			
					
					long[] guids = ispace.search(pd.getSearchText(), new HashSet<String>(), new HashSet<SearchCondition>(), 5);							
					
					if(guids==null){
						MessageDialog.showDialog("No record, to which you have access, was found matching "+pd.getSearchText());
						
						//close search progress bar
						search.close();
						
						return;
					}
					
					Record[] records = ispace.findRecordForGUIDs(guids);					

					// show summary table using record metadata and thumbnails
					RecordMetadataTable st = new RecordMetadataTable(Arrays.asList(records));
					JScrollPane scrollPane = new JScrollPane(st.getTable());
					
					//close search progress bar
					search.close();

					value = JOptionPane.showConfirmDialog(null, scrollPane, "Download Selected Record", JOptionPane.PLAIN_MESSAGE);
					if (value != JOptionPane.OK_OPTION)
						return;

					// download selected record from the server
					int selectedRow = st.getTable().getSelectedRow();
					
					if(selectedRow<=-1){
						MessageDialog.showDialog("You haven't selected any record for download");
						return;
					}
					
					Record record = ispace.findRecordForGUID(records[selectedRow].getGUID());
					
					//set download progress bar
					ProgressFrame progress=new ProgressFrame("Downloading");
					progress.setLength(record.getFrameCount()*record.getSliceCount()*record.getChannelCount());
					
					double averagevalue = 0.0;
					// create equivalent ICY sequence using the original record
					// data
					int site = 0;
					Sequence seq = new Sequence();
					for (int time = 0; time < record.getFrameCount(); time++)
					{
						double elapsedtime = 0;
						for (int slice = 0; slice < record.getSliceCount(); slice++)
						{
							IcyBufferedImage img = new IcyBufferedImage(record.getImageWidth(), record.getImageHeight(), record.getChannelCount(), getDataType(record.getPixelDepth()));
							for (int channel = 0; channel < record.getChannelCount(); channel++)
							{
								IPixelData pixelData = record.getPixelData(new Dimension(time, slice, channel, site));
								Object values = pixelData.getRawData().getPixelArray();
								img.setDataXY(channel, values);
								
								progress.incPosition();

								elapsedtime = pixelData.getElapsedTime();
							}

							seq.addImage(time, img);
						}
						averagevalue = elapsedtime;
					}
					
					//close download progress bar
					progress.close();

					if (record.getFrameCount() > 1)
						averagevalue = averagevalue / (record.getFrameCount() - 1);
					seq.setName(record.getSourceFilename() + "_guid_" + record.getGUID());

					// Set Meta Data (minimum: pixel size and time increment,
					// computed
					// in average: to be checked: image based metadata in ICY)

					double physicalSizeX = record.getPixelSizeAlongXAxis(); // in
																			// um
					double physicalSizeY = record.getPixelSizeAlongYAxis();
					double physicalSizeZ = record.getPixelSizeAlongZAxis();
					if (averagevalue > 0)
						seq.setTimeInterval(averagevalue); // in seconds
					if (physicalSizeX > 0)
						seq.setPixelSizeX(physicalSizeX);
					if (physicalSizeY > 0)
						seq.setPixelSizeY(physicalSizeY);
					if (physicalSizeZ > 0)
						seq.setPixelSizeZ(physicalSizeZ);

					addSequence(seq);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					MessageDialog.showDialog("Error in connecting to server. Make sure you are logged in using Login plugin.");
				}
			}
		});
		
		t.start();
	}
	
	private DataType getDataType(PixelDepth pixelDepth)
	{
		if(pixelDepth == PixelDepth.BYTE)
			return DataType.BYTE;
		if(pixelDepth == PixelDepth.SHORT)
			return DataType.SHORT;
		if(pixelDepth == PixelDepth.INT)
			return DataType.INT;
		throw new IllegalArgumentException("unknown data type");
	}
	
	/**
	 * Class showing the summary of the seached records using the record metadata and thumbnails
	 * 
	 * @author Anup Kulkarni
	 */
	private class RecordMetadataTable {
		private List<Record> records;
		
		private JTable summaryTable;
		
		public RecordMetadataTable(List<Record>records)
		{
			this.records = records;
		}
		
		public JTable getTable()
		{
			if(summaryTable!=null)
				return this.summaryTable;
			
			Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
			for(Record record: records)
			{
				Vector<Object> row = new Vector<Object>();
				row.add(record.getGUID());
				row.add(record.getFrameCount());
				row.add(record.getSliceCount());
				row.add(record.getChannelCount());
				row.add(record.getSiteCount());
				row.add(record.getImageWidth());
				row.add(record.getImageHeight());
				row.add(record.getThumbnail());
				
				rowData.add(row);
			}
			
			Vector<String> colNames = new Vector<String>();
			colNames.add("RecordId");
			colNames.add("Frames");
			colNames.add("Slices");
			colNames.add("Channels");
			colNames.add("Sites");
			colNames.add("Width");
			colNames.add("Height");
			colNames.add("Thumbnail");
			
			summaryTable = new JTable(rowData, colNames);
			summaryTable.setRowHeight(50);
			
			
			summaryTable.setDefaultRenderer(Object.class, new RecordThumbnailRenderer());
			return summaryTable;
		}
	}
	
	private class RecordThumbnailRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int col) {
			Component comp = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, col);
			
			if(col == 7)
			{
				BufferedImage image = (BufferedImage) table.getValueAt(row, col);
				JLabel label = new JLabel(new ImageIcon(image));
				label.setSize(50, 50);
				
				return label;
			}
			return comp;
		}
	}
	
	/**
	 * Dialogbox for taking input for search text
	 * 
	 * @author Anup Kulkarni
	 */
	private class SearchDialog extends JPanel {

		private JTextField searchText;

		public SearchDialog()
		{
			setLayout(new GridLayout(1, 2, 5, 5));

			setupUI();
		}

		private void setupUI()
		{
			JLabel searchLabel = new JLabel("Search Text");
			searchText = new JTextField();

			add(searchLabel);
			add(searchText);
		}

		public String getSearchText()
		{
			return this.searchText.getText();
		}
	}
}
