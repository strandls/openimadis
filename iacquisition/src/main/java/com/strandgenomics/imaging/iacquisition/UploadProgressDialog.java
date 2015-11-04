package com.strandgenomics.imaging.iacquisition;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Shows progress of the uploader task
 * @author Anup Kulkarni
 */
public class UploadProgressDialog extends JDialog {
	/**
	 * message displaying state of uploader task
	 */
	private JLabel messageLabel;

	/**
	 * progress of uploader task
	 */
	private JProgressBar progressBar;

	public UploadProgressDialog(JDialog parent, int curr, int total) {
		super(parent);
		setTitle("Uploading "+curr+" of "+total);

		// Not allowed to kill the operation
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));

		messageLabel = new JLabel("Starting                 ");

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(progressBar.getValue() == 100){
//					showDoneDialog();
					closeDialog();
				}
			}
		});

		progressPanel.add(messageLabel);
		progressPanel.add(progressBar);
		
		add(progressPanel, BorderLayout.PAGE_START);

		pack();
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	public void showDoneDialog(){
		JOptionPane.showMessageDialog(this, messageLabel.getText(), "Upload",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void closeDialog(){
		this.dispose();
	}

	public void setProgress(Integer newValue) {
		progressBar.setValue(newValue);
	}

	public void setNote(String message) {
		messageLabel.setText(message);
	}
}
