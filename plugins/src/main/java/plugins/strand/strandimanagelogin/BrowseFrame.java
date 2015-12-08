package plugins.strand.strandimanagelogin;

import icy.gui.dialog.MessageDialog;
import icy.gui.frame.progress.ProgressFrame;
import icy.image.IcyBufferedImage;
import icy.plugin.abstract_.Plugin;
import icy.sequence.Sequence;
import icy.type.DataType;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.PixelMetaData;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.iclient.RecordBuilder;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelDepth;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class BrowseFrame extends Plugin {

	private JFrame guiFrame;
	private JPanel tablePanel;
	private SearchPanel searchPanel;
	private JTable resultTable;
	private int selectedRow;
	private Record[] records;
	private JPanel downloadPanel;

	public BrowseFrame()
	{
		setUI();
	}

	private void setUI()
	{
		guiFrame = new JFrame();
		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		guiFrame.setTitle("Strand Image Database - Avadis iManage");
		guiFrame.setSize(600, 500);
		guiFrame.setAlwaysOnTop(true);

		// create menu bar
		ICYPluginMenu menuBar = new ICYPluginMenu();
		// add search panel to frame
		guiFrame.setJMenuBar(menuBar);

		JPanel parentPanel = new JPanel(new BorderLayout());
		guiFrame.add(parentPanel);

		// create search Panel
		searchPanel = new SearchPanel();
		// add search panel to frame
		parentPanel.add(searchPanel, BorderLayout.NORTH);

		// add table
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());

		// initialize download button
		JButton downloadButton = new JButton("Download");
		downloadPanel = new JPanel();
		downloadPanel.setSize(600, 100);
		downloadPanel.add(downloadButton);

		// search action listener
		downloadButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectedRow = resultTable.getSelectedRow();
				downloadRecord();
			}
		});

		// add search panel to frame
		parentPanel.add(tablePanel, BorderLayout.CENTER);
		parentPanel.add(downloadPanel, BorderLayout.SOUTH);

		// make sure the JFrame is visible
		guiFrame.setVisible(true);
	}

	public void addTable(JTable table)
	{
		tablePanel.add(table);
	}

	public void doSearch()
	{
		final String searchtext = searchPanel.getSearchText();
		final int searchLimit = searchPanel.getSearchLimit();

		if (searchtext == null || searchtext.equals(""))
		{
			MessageDialog.showDialog("Please provide text for search");
			return;
		}

		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// start search progress bar
					ProgressFrame search = new ProgressFrame("Searching");

					ImageSpace ispace = ImageSpaceObject.getConnectionManager();
					
					long[] guids = ispace.search(searchtext, null, null, searchLimit);

					if (guids == null)
					{
						MessageDialog.showDialog("No record, to which you have access, was found matching " + searchtext);

						// close search progress bar
						search.close();

						return;
					}

					records = ispace.findRecordForGUIDs(guids);

					// show summary table using record metadata and thumbnails
					RecordMetadataTable st = new RecordMetadataTable(Arrays.asList(records));
					resultTable = st.getTable();

					JScrollPane scrollPane = new JScrollPane(resultTable);
					scrollPane.setSize(600, 300);
					tablePanel.removeAll();
					tablePanel.add(scrollPane, BorderLayout.CENTER);
					guiFrame.repaint();

					// close search progress bar
					search.close();
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

	public void downloadRecord()
	{

		if (selectedRow == -1)
		{
			MessageDialog.showDialog("You haven't selected any record for download");
			return;
		}

		Thread t = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					ImageSpace ispace = ImageSpaceObject.getConnectionManager();

					Record record = ispace.findRecordForGUID(records[selectedRow].getGUID());

					// set download progress bar
					ProgressFrame progress = new ProgressFrame("Downloading");
					progress.setLength(record.getFrameCount() * record.getSliceCount() * record.getChannelCount());

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

					// close download progress bar
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

	public void itemAction(JMenuItem item)
	{

		if (item.getName().equals("Upload"))
		{
			doUpload();
		}

		if (item.getName().equals("Logout"))
		{
			logout();
		}

	}

	public void doUpload()
	{

		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Sequence sequence = getFocusedSequence();
				if (sequence == null)
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
				if (value != JOptionPane.OK_OPTION)
					return;

				// set upload progress bar
				ProgressFrame upload = new ProgressFrame("Uploading");
				upload.setLength(sequence.getSizeT() * sequence.getSizeZ() * sequence.getSizeC());

				List<Channel> channels = new ArrayList<Channel>();
				for (int i = 0; i < sequence.getSizeC(); i++)
				{
					String name = sequence.getChannelName(i);
					Channel channel = new Channel(name);

					channels.add(channel);
				}

				List<Site> sites = new ArrayList<Site>();
				sites.add(new Site(0, "Site 0"));

				if (pd.getRecordLabel().equals(""))
				{
					MessageDialog.showDialog("Please provide name for record to be uploaded");

					// close upload progress bar
					upload.close();

					return;
				}

				Project prj = ispace.findProject(pd.getProjectName());
				RecordBuilder rb = prj.createRecordBuilder(pd.getRecordLabel(), sequence.getSizeT(), sequence.getSizeZ(), channels, sites, sequence.getWidth(), sequence.getHeight(),
						getPixelDepth(sequence.getDataType_()), 1.0, 1.0, 1.0, ImageType.GRAYSCALE, new SourceFormat("IMG"), "", "/tmp", System.currentTimeMillis(), System.currentTimeMillis(),
						System.currentTimeMillis());

				for (int time = 0; time < sequence.getSizeT(); time++)
				{
					for (int slice = 0; slice < sequence.getSizeZ(); slice++)
					{
						for (int channel = 0; channel < sequence.getSizeC(); channel++)
						{

							PixelArray rawData = null;
							if (getPixelDepth(sequence.getDataType_()) == PixelDepth.BYTE)
								rawData = new PixelArray.Byte(((byte[]) sequence.getDataCopyXY(time, slice, channel)), sequence.getWidth(), sequence.getHeight());
							else if (getPixelDepth(sequence.getDataType_()) == PixelDepth.SHORT)
								rawData = new PixelArray.Short(((short[]) sequence.getDataCopyXY(time, slice, channel)), sequence.getWidth(), sequence.getHeight());
							else if (getPixelDepth(sequence.getDataType_()) == PixelDepth.INT)
								rawData = new PixelArray.Integer(((int[]) sequence.getDataCopyXY(time, slice, channel)), sequence.getWidth(), sequence.getHeight());
							else
							{
								MessageDialog.showDialog("unknown type");
								return;
							}

							PixelMetaData pixelData = new PixelMetaData(new Dimension(time, slice, channel, 0), 1.0, 1.0, 1.0, 1.0, 1.0, new Date());

							rb.addImageData(new Dimension(time, slice, channel, 0), rawData, pixelData);

							upload.incPosition();
						}
					}
				}

				Record r = rb.commit();

				// close upload progress bar
				upload.close();

				MessageDialog.showDialog("Record " + r.getGUID() + " has been uploaded successfully");
			}
		});
		t.start();
	}

	public void logout()
	{

		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		ispace.logout();

		this.guiFrame.dispose();
	}

	private DataType getDataType(PixelDepth pixelDepth)
	{
		if (pixelDepth == PixelDepth.BYTE)
			return DataType.BYTE;
		if (pixelDepth == PixelDepth.SHORT)
			return DataType.SHORT;
		if (pixelDepth == PixelDepth.INT)
			return DataType.INT;
		throw new IllegalArgumentException("unknown data type");
	}

	private PixelDepth getPixelDepth(DataType dataType_)
	{
		if (dataType_ == DataType.BYTE || dataType_ == DataType.UBYTE)
			return PixelDepth.BYTE;
		if (dataType_ == DataType.SHORT || dataType_ == DataType.USHORT)
			return PixelDepth.SHORT;
		if (dataType_ == DataType.INT || dataType_ == DataType.UINT)
			return PixelDepth.INT;
		throw new IllegalArgumentException("unknown data type");
	}

	/**
	 * Class showing the summary of the seached records using the record
	 * metadata and thumbnails
	 * 
	 * @author Anup Kulkarni
	 */
	private class RecordMetadataTable {
		private List<Record> records;

		private JTable summaryTable;

		public RecordMetadataTable(List<Record> records)
		{
			this.records = records;
		}

		public JTable getTable()
		{
			if (summaryTable != null)
				return this.summaryTable;

			Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
			for (Record record : records)
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

			TableModel model = new DefaultTableModel(rowData, colNames)
			{
				public boolean isCellEditable(int row, int column)
				{
						return false;//This causes all cells to be not editable
				}
			};
			
			JTable table = new JTable(model);
			  
			summaryTable = new JTable(model);
			summaryTable.setRowHeight(50);

			summaryTable.setDefaultRenderer(Object.class, new RecordThumbnailRenderer());
			return summaryTable;
		}
	}

	private class RecordThumbnailRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8507632520534171628L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
		{
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

			if (col == 7)
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
	 * Search panel with search field and button
	 * 
	 * @author navneet
	 * 
	 */
	private class SearchPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4678999163594432940L;
		private String searchText;
		private int searchLimit = 5;

		public SearchPanel()
		{

			new BoxLayout(this, BoxLayout.X_AXIS);

			// text field for search
			final JTextField searchTextField = new JTextField();
			searchTextField.setColumns(15);

			// search button
			JButton seacrhButton = new JButton("Search");

			final JSpinner searchLimitSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
			searchLimitSpinner.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					searchLimit = (Integer) searchLimitSpinner.getValue();
				}
			});

			// search action listener
			seacrhButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					searchText = searchTextField.getText();
					doSearch();
				}
			});

			// add textfield and button to pnael
			this.add(searchTextField);
			this.add(Box.createHorizontalGlue());
			this.add(seacrhButton);
			this.add(Box.createHorizontalGlue());
			this.add(Box.createHorizontalStrut(20));
			this.add(new JLabel("Max Search Results:"));
			this.add(searchLimitSpinner);
		}

		public String getSearchText()
		{
			return searchText;
		}

		public int getSearchLimit()
		{
			return searchLimit;
		}
	}

	/**
	 * Menu bar with menu items
	 * 
	 * @author navneet
	 * 
	 */
	private class ICYPluginMenu extends JMenuBar {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7470332426624450136L;
		String menuNames[] = { "Logout", "Upload" };
		String menuItems[][] = { { "Login", "Logout" }, { "Upload" } };

		public ICYPluginMenu()
		{

			for (int i = 0; i < menuNames.length; i++)
			{
				JMenu menu = new JMenu(menuNames[i]);

				// add menu items to menu
				if (menuItems[i].length != 0)
				{
					for (int j = 0; j < menuItems[i].length; j++)
					{
						JMenuItem item = new JMenuItem(menuItems[i][j]);
						item.setName(menuItems[i][j]);

						item.addActionListener(new ActionListener()
						{

							@Override
							public void actionPerformed(ActionEvent e)
							{
								itemAction((JMenuItem) e.getSource());
							}
						});

						menu.add(item);
					}
				}

				this.add(menu);
			}
		}
	}

	/**
	 * upload dialog for records
	 * 
	 * @author navneet
	 * 
	 */
	private class UploadDialog extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7272893994092979483L;
		private JTextField record;
		private JComboBox<String> project;

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
