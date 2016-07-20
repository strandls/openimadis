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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.iviewer.UIUtils;

public class DeleteDialog extends JDialog {
	/**
	 * experiments initially selected by user
	 */
	private IExperiment[] experiments;
	
	/**
	 * final experiments list to be deleted
	 */
	private List<IExperiment> toDelete;
	
	private ArrayList<JCheckBox> experimentButtonGroup;
	
	public DeleteDialog(final JFrame parent, Set<IExperiment> experimentsToUpload) {
		super(parent);
		setTitle("Delete");
		
		toDelete = new ArrayList<IExperiment>();
		
		this.experiments = experimentsToUpload.toArray(new IExperiment[0]);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel chooserExpPanel = new JPanel();
		chooserExpPanel.setLayout(new BoxLayout(chooserExpPanel,
				BoxLayout.Y_AXIS));
		updateExperimentPanel(chooserExpPanel);
		chooserExpPanel.setBorder(new LineBorder(Color.GRAY));

		JButton okBtn = new JButton("Delete");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opt = JOptionPane.showConfirmDialog(parent, "Do you want to delete all the checked records? ");
				if(opt == JOptionPane.YES_OPTION){
					startDelete();
					dispose();
				}
			}
		});

		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
		buttonPanel.add(okBtn);
		buttonPanel.add(cancelBtn);

		add(chooserExpPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);

		pack();
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	/**
	 * populates final list of experiments to be deleted
	 */
	protected void startDelete() {
		for(int i=0;i<experimentButtonGroup.size();i++){
			if(experimentButtonGroup.get(i).isSelected()){
				toDelete.add(experiments[i]);
			}
		}
	}
	
	private String getSeedFile(IExperiment experiment)
	{
		String seedFile = "";
		Collection<Signature> signs = experiment.getRecordSignatures();
		for(Signature sign : signs)
		{
			seedFile = experiment.getRecord(sign).getSourceFilename();
			break;
		}
		return seedFile;
	}

	private void updateExperimentPanel(final JPanel parentPanel) {
		JPanel experimentChooser = newJPanel();
		String experimentNames[] = new String[experiments.length];
		boolean enabledExperiments[] = new boolean[experiments.length];

		for (int i = 0; i < experimentNames.length; i++)
		{
			String fullSourceFileName = getSeedFile(experiments[i]);
			String separator = File.separator;
			experimentNames[i] = fullSourceFileName.substring(fullSourceFileName.lastIndexOf(separator) + 1);
			enabledExperiments[i] = true;
		}

		JPanel experimentTitle = UIUtils.newTitlePanel(" Select Record Groups to Delete   ", true);
		experimentTitle.add(Box.createHorizontalStrut(5));
		
		parentPanel.add(experimentTitle);
		parentPanel.add(Box.createVerticalStrut(10));
		JScrollPane scrollPane = new JScrollPane(experimentChooser);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(500, 100));
		parentPanel.add(scrollPane);
		
		experimentButtonGroup = new ArrayList<JCheckBox>();
		for (int i = 0; i < experiments.length; i++) {
			JPanel bp = newButtonPanel();
			JCheckBox b = new JCheckBox();
			b.setEnabled(true);
			b.setSelected(true);

			b.setActionCommand(((Integer) i).toString());
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO:
				}
			});
			b.setBackground(Color.white);
			experimentButtonGroup.add(b);
			bp.add(b);
			bp.add(new JLabel(experimentNames[i]));
			bp.add(Box.createHorizontalStrut(40));
			bp.add(Box.createHorizontalGlue());
			bp.add(new JLabel(experiments[i].getRecordSignatures().size()+ " records"));
			bp.add(Box.createHorizontalStrut(5));

			experimentChooser.add(bp);

		}
		experimentChooser.add(Box.createVerticalStrut(10));
	}
	
	private JPanel newJPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Color.white);
		p.setMaximumSize(new Dimension(50, 100));
		return p;
	}
	
	private JPanel newButtonPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBackground(Color.white);
		p.setMaximumSize(new Dimension(800, 30));
		return p;
	}

	/**
	 * 
	 * @return list of experiments to be deleted
	 */
	public List<IExperiment> getSelectedExperiments() {
		return toDelete;
	}
}

