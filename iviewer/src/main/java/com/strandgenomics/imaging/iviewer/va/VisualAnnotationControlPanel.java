package com.strandgenomics.imaging.iviewer.va;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JOptionPane;

import com.strandgenomics.imaging.iviewer.ImageViewerApplet;
import com.strandgenomics.imaging.iviewer.ImageViewerState;

public class VisualAnnotationControlPanel implements WindowListener {

	// private ToggleButton vaButton;
	private ImageViewerApplet imageViewer;
	private VisualAnnotationToolbar vaToolbar = null;
	protected VAController vaCanvas;

	public VisualAnnotationControlPanel(ImageViewerApplet imageViewer) {
		this.imageViewer = imageViewer;
		// vaButton = new ToggleButton(vaIcon, vaIcon,
		// "Disable Overlay Mode", "Enable Overlay Mode", false);

		// addListeners();
		// add(vaButton);
	}

	public VAController getVaCanvas() {
		return vaCanvas;
	}

	// private void addListeners() {
	// vaButton.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// if (imageViewer.getProperty("vaState") == null ||
	// (Integer)imageViewer.getProperty("vaState")==ImageModel.VAState.VA_OFF) {
	// // openVAToolBar();
	// imageViewer.actionOpenVAToolBar();
	// }
	// else if
	// ((Integer)imageViewer.getProperty("vaState")==ImageModel.VAState.VA_ON) {
	// // closeVAToolBar();
	// imageViewer.actionCloseVAToolBar();
	// }
	// }
	// });
	// }

	public boolean openVAToolBar() 
	{
		List<List<VAObject>> overlays = (List<List<VAObject>>) imageViewer
				.getOverlays();
		
		if ((Integer) imageViewer.getSliceState() == ImageViewerState.SliceState.Z_STACK) {
			int n = JOptionPane.showConfirmDialog(null,
					"Do you want to turn off z-stack view ?",
					"Conflicting Modes", JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE);
			if (n == 0) {
				imageViewer
						.setSliceState(ImageViewerState.SliceState.SINGLE_SLICE);
			} else {
				return false;
			}
		}
		if ((Integer) imageViewer.getChannelState() == ImageViewerState.ChannelState.MULTI_CHANNEL) {
			int n = JOptionPane.showConfirmDialog(null,
					"Do you want to turn off multi-channel view ?",
					"Conflicting Modes", JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE);
			if (n == 0) {
				imageViewer
						.setChannelState(ImageViewerState.ChannelState.OVERLAY);
			} else {
				return false;
			}
		}
		// if((ImageViewer.MovieState)imageViewer.getProperty("movieState")==ImageViewerState.MovieState.PLAYING){
		// int n = MsgManager.getInstance().showMsg("OVERLAY_SWITCH_MODE_CONF",
		// "name", "movie");
		// if (n == 0) {
		// imageViewer.setProperty("movieState",
		// ImageViewer.MovieState.STOPPED);
		// } else {
		// return false;
		// }
		// }

		// vaButton.setSelected(true);
		vaToolbar.createUI();
		vaToolbar.setTitle("Visual Annotation - *no_open_overlay*");
		vaToolbar.toFront();
		// vaToolbar.setLocationRelativeTo(Tool.getToolFrame());
		vaToolbar.setVisible(false);

		imageViewer.setVAState(ImageViewerState.VAState.VA_ON);
		// imageViewer.addPropertyChangeListener(vaToolbar);
		return true;
	}

	public void createVAToolBar() {
		if (vaToolbar != null)
			return;

		vaCanvas = new VAController(null, imageViewer);
		vaToolbar = new VisualAnnotationToolbar(vaCanvas);
		addWindowListener();
	}

	public void closeVAToolBar() {
		// vaButton.setSelected(false);
		vaToolbar.cleanup();
		vaToolbar.setVisible(false);
		vaCanvas.cleanup();
		vaCanvas.turnoffVA();
	}

	private void addWindowListener() {
		vaToolbar.addWindowListener(this);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		imageViewer.actionCloseVAToolBar();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	// public void propertyChanged(PropertyChangeEvent e) {
	// if (e.getPropertyName().equals("vaState")) {
	// vaButton.setSelected((Integer)imageViewer.getProperty("vaState")==ImageModel.VAState.VA_ON);
	// }
	// }

	public VisualAnnotationToolbar getToolbar() {
		return vaToolbar;
	}

	public void update() {
		if (imageViewer.getVAState() == ImageViewerState.VAState.VA_ON)
			vaToolbar.update();
	}
}
