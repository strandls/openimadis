package com.strandgenomics.imaging.iacquisition;

import javax.swing.JTextArea;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class LogPanelTextAreaAppender extends WriterAppender {

	static private JTextArea jTextArea = null;
	static private PatternLayout layout = null;
	static String pattern = "%d{dd MMM yyyy HH:mm:ss} %-5p: %m%n";
	private static int MAX_NO_OF_MESSAGES = 2000;
	private static int noOfMessages;

	/** Set the target JTextArea for the logging information to appear. */
	public void setTextArea(JTextArea jTextArea) {
		LogPanelTextAreaAppender.jTextArea = jTextArea;
		layout = new PatternLayout(pattern);
		noOfMessages = 0;
	}

	/**
	 * Format and then append the loggingEvent to the stored JTextArea.
	 */
	@Override
	public void append(LoggingEvent loggingEvent) {
		final String message = layout.format(loggingEvent);
		if (noOfMessages > MAX_NO_OF_MESSAGES) {
			jTextArea.setText("");
			noOfMessages = 0;
		}
		jTextArea.append(message);
		noOfMessages++;
	}
}
