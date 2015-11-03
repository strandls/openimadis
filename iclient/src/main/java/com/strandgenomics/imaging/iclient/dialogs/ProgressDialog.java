package com.strandgenomics.imaging.iclient.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ProgressDialog extends JDialog  {
	
    private JLabel title;
    private JProgressBar progressBar;
    private Window owner;
	
	public ProgressDialog(JFrame applicationFrame, String name) {
		super(applicationFrame, name, true);
		init(owner);
	}
	
	
	private void init(Window owner) {
		this.owner = owner;
		JPanel progress = createProgressPanel();
        setContentPane(progress);
        setUndecorated(true);
        this.setPreferredSize(new Dimension(250, 50));
        pack();
		
	}
	
    private JPanel createProgressPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.setBorder(BorderFactory.createTitledBorder(""));
        title = new JLabel("Progress .. ");

        progressBar = new JProgressBar(0, 100);
      //  progressBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        progressBar.setIndeterminate(true);

        panel.add(title, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        return panel;
    }

    public final void pack() {
        super.pack();
        setLocationRelativeTo(owner);
    }
    
    public void setMessage(String message){
    	if (message != null)
    		title.setText(message);
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("Acquisition Client");
        JFrame.setDefaultLookAndFeelDecorated(true);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        }
        catch(Exception ex)
        {}
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);

        ProgressDialog dialog = new ProgressDialog(frame, "");
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

}
