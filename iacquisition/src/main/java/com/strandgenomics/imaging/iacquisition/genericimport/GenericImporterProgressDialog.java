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

package com.strandgenomics.imaging.iacquisition.genericimport;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class GenericImporterProgressDialog  extends JDialog {

	JLabel title;
	private JProgressBar progressBar;
	private JDialog parent;
	
	public GenericImporterProgressDialog(JDialog parent, String name) {
		super(parent, name, true);
		this.parent = parent;
		
		init();
	}
	
	private void init() 
	{
		JPanel progress = createProgressPanel();
        setContentPane(progress);
        setUndecorated(true);
        this.setPreferredSize(new Dimension(250, 50));
        setModal(true);
        setLocationRelativeTo(parent);
        pack();
	}
	
	public void setMessage(String message)
	{
    	if (message != null)
    	{
    		title.setText(message);
    	}
    }

	public void setProgress(int value)
	{
		progressBar.setValue(value);
		progressBar.revalidate();
	}
	
	public int getProgress()
	{
		return progressBar.getValue();
	}
	
	private JPanel createProgressPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.setBorder(BorderFactory.createTitledBorder(""));
        title = new JLabel("Progress .. ");

        progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(false);

        panel.add(title, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        return panel;
    }

	public void setLocation(Component genericImporterFileValidator)
	{
		setLocationRelativeTo(genericImporterFileValidator);
	}
}
