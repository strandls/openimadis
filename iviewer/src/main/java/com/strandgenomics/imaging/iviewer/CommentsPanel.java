package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.dataobjects.AbstractAnnotationModel;

public class CommentsPanel extends JPanel{
    private JEditorPane editorPane;
    private JButton saveButton;
    private JLabel title;
    private boolean updating;
    private List<AbstractAnnotationModel> list;
	private ImageViewerApplet iviewer;
	public static final String COMMENTS_KEY= "COMMENTS_KEY";
	
	public boolean hasUnsavedNotes = false;

    public CommentsPanel(ImageViewerApplet iviewer) {
    	this.iviewer = iviewer;
    	
        list = new ArrayList<AbstractAnnotationModel>();

        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 300));
        setPreferredSize(new Dimension(200, 300));
        setVisible(true);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        title = new JLabel("Comments");
        Font f = title.getFont();
        title.setFont(new Font(f.getName(), Font.BOLD, f.getSize() + 1));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        add(titlePanel, BorderLayout.BEFORE_FIRST_LINE);
        addCommentsEditor();
    }

    public void handlerEditorUpdate() {
        if (!updating) {
            saveButton.setEnabled(true);
            title.setText("*Comments");
            hasUnsavedNotes = true;
        }
    }

    public void addCommentsEditor() {
        editorPane = new JEditorPane();
        // editorPane.setPreferredSize(new Dimension(cFrame.getWidth(),
        // cFrame.getHeight()-50));
        editorPane.getDocument().addDocumentListener(new DocumentListener() {
            public void removeUpdate(DocumentEvent e) {
                handlerEditorUpdate();
            }
            public void insertUpdate(DocumentEvent e) {
                handlerEditorUpdate();
            }
            public void changedUpdate(DocumentEvent e) {
                handlerEditorUpdate();
            }
        });

        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                saveNotes();
            }
        });
        buttonPanel.add(saveButton);

        add(editorScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void saveNotes(){
    	String text = editorPane.getText();
        
        iviewer.setUserNotes(text);

        title.setText("Comments");
        repaint();

        hasUnsavedNotes = false;
        saveButton.setEnabled(false);
    }

    public synchronized void updatePanel() {
        /*
         * this method is made synchronized because it was accessed from two
         * threads:1. AWT 2. Movie better solution is to run all the
         * modelChanged events on AWT thread only
         */
        updating = true;
        
        List<IRecord> records = iviewer.getRecords();
        if(records==null || records.size()<=0 || records.get(0)==null){
        	editorPane.setEnabled(false);
        	saveButton.setEnabled(false);
        }
        else
        {
        	editorPane.setEnabled(true);
        	saveButton.setEnabled(true);
        }


		String text = "";

		if(iviewer.getUserNotes()!=null)
			text = iviewer.getUserNotes();

		title.setText("Comments");
		editorPane.setText(text);
		repaint();
        updating = false;
    }
    
    public boolean isUnsaved(){
    	return hasUnsavedNotes;
    }

	public void setNotesStatus(boolean b) {
		hasUnsavedNotes = b;
	}
}
