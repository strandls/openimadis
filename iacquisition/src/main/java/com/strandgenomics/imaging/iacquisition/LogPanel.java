package com.strandgenomics.imaging.iacquisition;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class LogPanel extends JPanel{
	
	private JScrollPane scrollPane;
	private JTextArea textArea;
	
	private Logger logger = Logger.getRootLogger();
	private LogPanelTextAreaAppender appender;
	
	public LogPanel() {
		setLayout(new BorderLayout());
		setName("console");

		scrollPane = createScrollPane();
		add(scrollPane, BorderLayout.CENTER);
	}

	public void cleanup() {
		textArea = null;
		scrollPane = null;
	}

	private JScrollPane createScrollPane() {
		textArea = createTextArea();
		scrollPane = new JScrollPane(textArea);
		org.apache.log4j.lf5.viewer.LF5SwingUtils
				.makeVerticalScrollBarTrack(scrollPane);
		
		appender = new LogPanelTextAreaAppender();
		appender.setTextArea(textArea);
		logger.addAppender(appender);
		


		int w = 400;
		int h = 100;

		scrollPane.setPreferredSize(new Dimension(w, h));
		return scrollPane;
	}

	private JTextArea createTextArea() {
		JTextArea t = new JTextArea();
		t.setEditable(false);
		t.setLineWrap(true);

		return t;
	}

	public void clear() {
		textArea.setText("");
	}
	
}

