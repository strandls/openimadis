package com.strandgenomics.imaging.iviewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.commons.io.FilenameUtils;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideToggleButton;
import com.strandgenomics.imaging.icore.IRecord;

@SuppressWarnings("serial")
public class ImagePropertiesControlPanel extends JMenuBar 
{
	private ImageViewerPanel imageViewerPanel;
	// private JSlider zoom;

	private JideButton fitWidth;
	private JideButton zoomTrueSize;
	
	private JideToggleButton channelScale;
	private JideToggleButton channelState;
	private JideToggleButton sliceState;
	private JideToggleButton movieState;
	
	private JideSplitButton movieButton;
	private JideSplitButton downloadButton; 
	private ImageViewerApplet imageViewerApplet;
	private JideButton view3d;

	public ImagePropertiesControlPanel(ImageViewerPanel ivp,
			ImageViewerApplet imageViewerApplet) {
		this.imageViewerPanel = ivp;
		this.imageViewerApplet = imageViewerApplet;

		setOpaque(true);
		setVisible(true);
		createGUI();
	}

	private void addListeners() {
		// zoom.addChangeListener(new ChangeListener() {
		// public void stateChanged(ChangeEvent e) {
		// int value = zoom.getValue();
		// imageViewerPanel.setZoom(value / 100.0);
		// }
		// });

		fitWidth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageViewerPanel.toggleFitWidth();
			}
		});
		

		zoomTrueSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageViewerPanel.setZoom(1.0);
			}
		});

		channelScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int channelScaleValue = imageViewerApplet.getChannelScale() == ImageViewerState.ChannelScale.COLOR ? ImageViewerState.ChannelScale.GRAY
						: ImageViewerState.ChannelScale.COLOR;
				String iconName = imageViewerApplet.getChannelScale() == ImageViewerState.ChannelScale.GRAY ? "color_pallet.png" :
						"B-W_pallet.png";
				channelScale.setIcon(UIUtils.getIcon(iconName));
				imageViewerApplet.setChannelScale(channelScaleValue);
			}
		});

		channelState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int channelStateValue = imageViewerApplet.getChannelState() == ImageViewerState.ChannelState.MULTI_CHANNEL ? ImageViewerState.ChannelState.OVERLAY
						: ImageViewerState.ChannelState.MULTI_CHANNEL;
				String iconName = imageViewerApplet.getChannelState() == ImageViewerState.ChannelState.MULTI_CHANNEL ? "Multi-channel_OFF.png"
						: "Multi-channel_ON.png";
				channelState.setIcon(UIUtils.getIcon(iconName));
				
				imageViewerApplet.setChannelState(channelStateValue);
			}
		});

		view3d.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Runnable r = new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							imageViewerApplet.launch3D();
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				};
				
				Thread t = new Thread(r);
				t.start();
			}
		});

		movieState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String iconName = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? "PlayNormal.png"
						: "StopNormal.png";
				movieState.setIcon(UIUtils.getIcon(iconName));
				
				int movieState = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? ImageViewerState.MovieState.STOPPED
						: ImageViewerState.MovieState.PLAYING;
				imageViewerApplet.setMovieState(movieState);
			}
		});

		sliceState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int sliceStateValue = imageViewerApplet.getSliceState() == ImageViewerState.SliceState.SINGLE_SLICE ? ImageViewerState.SliceState.Z_STACK
						: ImageViewerState.SliceState.SINGLE_SLICE;
				String iconName = imageViewerApplet.getSliceState() == ImageViewerState.SliceState.SINGLE_SLICE ? "Z_projection-ON.png"
						: "Z_projection-OFF.png";
				sliceState.setIcon(UIUtils.getIcon(iconName));
				
				imageViewerApplet.setSliceState(sliceStateValue);
			}
		});
	}

	private void createGUI() {
		// zoom = new JSlider(10, 999, 100);

		fitWidth = UIUtils.createCommandBarButton("fit_width.png", "Fit to window");

		zoomTrueSize = UIUtils.createCommandBarButton("zoom_rest.png",
				"Zoom 100%");
		
		channelState = UIUtils
				.createCommandBarToggleButton("Multi-channel_ON.png",
						"Multi-channel_OFF.png", "Click to reset tile channels",
						"Click to tile channels", false);

		sliceState = UIUtils.createCommandBarToggleButton(
				"Z_projection-ON.png", "Z_projection-OFF.png",
				"Click to reset Z-Project", "Click to Z-Project", false);
				
		channelScale = UIUtils.createCommandBarToggleButton("color_pallet.png",
				"B-W_pallet.png", "Color mode active",
				"Gray scale mode active", true);
		
		movieState = UIUtils.createCommandBarToggleButton("PlayNormal.png",
				"PlayNormal.png", "Stop Playing Movie", "Play Movie", false);
		
		movieButton = new JideSplitButton(UIUtils.getIcon("PlayNormal.png"));
		movieButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int movieState = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? ImageViewerState.MovieState.STOPPED
						: ImageViewerState.MovieState.PLAYING;
				String iconName = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? "PlayNormal.png"
						: "StopNormal.png";
				if(movieState == ImageViewerState.MovieState.STOPPED)
					imageViewerApplet.setMovieState(movieState);
				else
				{
					if(imageViewerApplet.getMaxFrame()>1)
						imageViewerApplet.playMovieOnFrame(movieState);
					else
						return;
				}
				movieButton.setIcon(UIUtils.getIcon(iconName));
			}
		});
		
		movieButton.setSize(22, 22);
		JMenuItem sliceMenu = new JMenuItem("Slice (Z)");
		sliceMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(imageViewerApplet.getMaxSlice()<=1)
					return;
				
				String iconName = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? "PlayNormal.png"
						: "StopNormal.png";
				movieButton.setIcon(UIUtils.getIcon(iconName));
				
				int movieState = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? ImageViewerState.MovieState.STOPPED
						: ImageViewerState.MovieState.PLAYING;
				
				imageViewerApplet.playMovieOnSlice(movieState);
			}
		});
		JMenuItem frameMenu = new JMenuItem("Frame (T)");
		frameMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(imageViewerApplet.getMaxFrame()<=1)
					return;
				
				String iconName = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? "PlayNormal.png"
						: "StopNormal.png";
				movieButton.setIcon(UIUtils.getIcon(iconName));
				
				int movieState = imageViewerApplet.getMovieState() == ImageViewerState.MovieState.PLAYING ? ImageViewerState.MovieState.STOPPED
						: ImageViewerState.MovieState.PLAYING;
				
				imageViewerApplet.playMovieOnFrame(movieState);
			}
		});
		movieButton.add(sliceMenu);
		movieButton.add(frameMenu);
		
		
		downloadButton = new JideSplitButton(UIUtils.getIcon("movie.png"));
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {}
		});
		
		downloadButton.setSize(22, 22);
		JMenuItem sliceDMenu = new JMenuItem("Slice(Z)");
		sliceDMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("hello slice");
				JFrame frame = new JFrame();
		        MovieDialog mypanel = new MovieDialog(frame);
		        JDialog jd = new JDialog(frame,"",true);
		        jd.setTitle("Create Video");
		        jd.add(mypanel);
		        jd.pack();
		        jd.setLocationRelativeTo(frame);
		        jd.setVisible(true);
		        if(!mypanel.getValidation())
		        	return;
		        String fileName = mypanel.getFileName();
		        String fileExtension = FilenameUtils.getExtension(fileName);
		        if(fileExtension.length()==0){
		        	fileName += ".mp4";
		        }
		        String filePath = mypanel.getDirectory();
		        int option = mypanel.getOption();
		        double fps = 0;
				if(option == 1)
		        	fps = mypanel.getFPS();
				String notes = mypanel.getNotes();
		        imageViewerApplet.downloadMovie(fileName,filePath,option,fps,notes,false);
		        //imageViewerApplet.addUserAttachment(selectedFile, notes);
			}
		});
		JMenuItem frameDMenu = new JMenuItem("Frame(T)");
		frameDMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();
		        MovieDialog mypanel = new MovieDialog(frame);
		        JDialog jd = new JDialog(frame,"",true);
		        jd.setTitle("Create Video");
		        jd.add(mypanel);
		        jd.pack();
		        jd.setLocationRelativeTo(frame);
		        jd.setVisible(true);
		        if(!mypanel.getValidation())
		        	return;
		        String fileName = mypanel.getFileName();
		        String fileExtension = FilenameUtils.getExtension(fileName);
		        if(fileExtension.length()==0){
		        	fileName += ".mp4";
		        }
		        String filePath = mypanel.getDirectory();
		        int option = mypanel.getOption();
		        double fps = 0;
				if(option == 1)
		        	fps = mypanel.getFPS();
				String notes = mypanel.getNotes();
		        imageViewerApplet.downloadMovie(fileName,filePath,option,fps,notes,true);
			}
		});
		downloadButton.add(sliceDMenu);
		downloadButton.add(frameDMenu);
		view3d = UIUtils.createCommandBarButton("3D-ON.png", "Render 3D");

		// add(zoom);
		add(fitWidth);
		
		add(zoomTrueSize);
		add(sliceState);
		add(channelScale);
		add(channelState);
//		add(movieState);
		add(movieButton);
//		add(view3d);
		add(downloadButton);
		addListeners();
	}

	public void updatePanel() {
		List<IRecord> records = imageViewerApplet.getRecords();
		if(records==null || records.size()<=0 || records.get(0)==null)
		{
			fitWidth.setEnabled(false);
			downloadButton.setEnabled(false);
			zoomTrueSize.setEnabled(false);
			sliceState.setEnabled(false);
			channelScale.setEnabled(false);
			channelState.setEnabled(false);
			movieButton.setEnabled(false);
//			add(movieButton);
			view3d.setEnabled(false);
			return;
		}
		else
		{
			fitWidth.setEnabled(true);
			downloadButton.setEnabled(true);
			zoomTrueSize.setEnabled(true);
			sliceState.setEnabled(true);
			channelScale.setEnabled(true);
			channelState.setEnabled(true);
			movieButton.setEnabled(true);
//			add(movieButton);
			view3d.setEnabled(true);
		}
		
		
		movieState.setIcon(UIUtils.getIcon("PlayNormal.png"));
		movieState.setSelected(false);
		movieState.setEnabled(imageViewerApplet.getMaxFrame()>1);
	}
}