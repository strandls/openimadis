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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.log4j.Level;
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
		logger.setLevel(Level.INFO);


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


