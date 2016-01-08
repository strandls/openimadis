package com.strandgenomics.imaging.iacquisition;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.util.Uploader;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.iviewer.UIUtils;

/**
 * Shows dialog box for selecting experiments to upload and project to which
 * experiments are uploading
 * 
 * @author Anup Kulkarni
 */
@SuppressWarnings("serial")
public class UploadDialog extends JDialog {
	
	boolean retValue = false;
	/**
	 * experiments chosen initially
	 */
	private IExperiment[] experiments;
	/**
	 * enabled state of experiments 
	 */
	private boolean[] enabledExperiments;
	/**
	 * selected project to upload experiments
	 */
	protected Project selectedProject;
	/**
	 * list of active projects valid for this user
	 */
//	private List<Project> activeProjects;

	public UploadDialog(final JFrame parent, List<IExperiment> experimentsToUpload, Project selectedProject) {
		super(parent);
		setTitle("Upload - Project : " + selectedProject.getName());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		this.experiments = experimentsToUpload.toArray(new IExperiment[0]);
		this.selectedProject = selectedProject;

		JPanel chooserTablePanel = createExperimentPanel();
		JPanel buttonPanel = createButtonPanel();
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(Box.createVerticalStrut(5));
		bottomPanel.add(Box.createVerticalGlue());
		bottomPanel.add(buttonPanel);
		bottomPanel.add(Box.createVerticalGlue());
		
		
		
		add(Box.createRigidArea(new Dimension(5,5)));
		//add(projectName);
		add(Box.createRigidArea(new Dimension(5,5)));
		add(chooserTablePanel);
		add(Box.createRigidArea(new Dimension(5,5)));
		add(Box.createVerticalGlue());
		add(bottomPanel);

		pack();
		setModal(true);
		setResizable(true);
		setLocationRelativeTo(parent);
	}
	
	private JPanel createProjectChooserPanel()
	{
		List<Project> projects = Context.getInstance().getActiveProjects();
		
		Project projectNames[] = new Project[projects.size()];
		int i=0;
		int selectedIndex = 0;
		for(Project project: projects)
		{
			projectNames[i] = project;
			if(project.getName().equals(selectedProject.getName()))
				selectedIndex = i;
			
			i++;
		}
		
		final JComboBox<Project> projectChooser = new JComboBox<Project>(projectNames);
		projectChooser.setSelectedIndex(selectedIndex);
		projectChooser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectedProject = (Project) projectChooser.getSelectedItem();
			}
		});

		JLabel projectLabel = new JLabel("Upload To:");
		
		JPanel projectChooserPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		projectChooserPanel.add(projectLabel);
		projectChooserPanel.add(projectChooser);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
		
		containerPanel.add(Box.createHorizontalStrut(5));
		containerPanel.add(projectChooserPanel);
		containerPanel.add(Box.createHorizontalStrut(5));
		
		return containerPanel;
	}

	private JPanel createExperimentPanel(){
		JPanel chooserTablePanel = new JPanel();
		chooserTablePanel.setLayout(new BoxLayout(chooserTablePanel, BoxLayout.Y_AXIS));
		updateExperimentPanel(chooserTablePanel);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
		
		containerPanel.add(Box.createHorizontalStrut(5));
		containerPanel.add(chooserTablePanel);
		containerPanel.add(Box.createHorizontalStrut(5));
		
		return containerPanel;
	}
	
	private JPanel createButtonPanel()
	{
		JButton okBtn = new JButton("Upload");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getSelectedProject()!=null && getSelectedExperiments().size() > 0){
					retValue = true;
					dispose();
				}else if(getSelectedProject()==null){
					JOptionPane.showMessageDialog(UploadDialog.this, "Please choose a project to upload record.", "No project selected.", JOptionPane.ERROR_MESSAGE);
				}else if(getSelectedExperiments().size() == 0){
					JOptionPane.showMessageDialog(UploadDialog.this, "Please choose record groups to upload.", "No record group selected.", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				retValue = false;
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(okBtn);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(cancelBtn);
		
		return buttonPanel;
	}
	
	protected void startUpload(Project project)
	{
		for(int i=0;i<experiments.length;i++)
		{
			if(!enabledExperiments[i])
				continue;
			
			Uploader uploader = project.getUploader( (RawExperiment) experiments[i]);
			
			final UploadProgressDialog uploadMonitor = new UploadProgressDialog(this, i+1 , experiments.length);
			final UploadTask uploadTask = new UploadTask(uploader);
			
			uploadTask.addPropertyChangeListener(new PropertyChangeListener() 
			{
				@Override
				public void propertyChange(PropertyChangeEvent evt) 
				{
					if ("progress" == evt.getPropertyName() )
					{
						uploadMonitor.setNote(uploadTask.getMessage());
						uploadMonitor.setProgress((Integer)evt.getNewValue());
					}
				}
			});
			
			uploadTask.execute();
			uploadMonitor.setVisible(true);
			
			if(i == experiments.length-1)
				uploadMonitor.showDoneDialog();
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
	
	private void updateExperimentPanel(final JPanel parentPanel) 
	{
		JPanel experimentChooser = newJPanel();
		
		JPanel experimentTitle = UIUtils.newTitlePanel("Select Record Groups to Upload", true);
		experimentTitle.add(Box.createHorizontalStrut(5));
		
		final JScrollPane scrollPane = new JScrollPane(experimentChooser);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(500, 100));
		
		parentPanel.add(experimentTitle);
		parentPanel.add(Box.createVerticalStrut(5));
		parentPanel.add(scrollPane);
		
		String experimentNames[] = new String[experiments.length];
		enabledExperiments = new boolean[experiments.length];

		for (int i = 0; i < experimentNames.length; i++) {
			String fullSourceFileName = getSeedFile(experiments[i]);
			String separator = File.separator;
			experimentNames[i] = fullSourceFileName
					.substring(fullSourceFileName.lastIndexOf(separator)+1);
			enabledExperiments[i] = true;
		}

		ArrayList<JCheckBox> experimentButtonGroup = new ArrayList<JCheckBox>();
		for (int i = 0; i < experiments.length; i++) {
			JPanel bp = newButtonPanel();
			final JCheckBox b = new JCheckBox();
			b.setEnabled(true);
			b.setSelected(true);

			b.setActionCommand(((Integer) i).toString());
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateSelected(new Integer(e.getActionCommand()));
				}
			});
			b.setBackground(Color.white);
			experimentButtonGroup.add(b);
			bp.add(b);
			bp.add(new JLabel(experimentNames[i]));
			bp.add(Box.createHorizontalStrut(40));
			bp.add(Box.createHorizontalGlue());
			bp.add(new JLabel(AcqclientUIUtils.getExperimentSize(experiments[i])));
			bp.add(Box.createHorizontalStrut(10));
			bp.add(new JLabel(experiments[i].getRecordSignatures().size()+ " records"));
			bp.add(Box.createHorizontalStrut(5));

			experimentChooser.add(Box.createHorizontalStrut(5));
			experimentChooser.add(bp);
			experimentChooser.add(Box.createHorizontalStrut(5));
		}
		
		experimentChooser.add(Box.createVerticalStrut(30));
	}
	
	protected void updateSelected(Integer integer) {
		enabledExperiments[integer] = !enabledExperiments[integer]; 
	}

	private JPanel newJPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Color.white);
		return p;
	}
	
	private JPanel newButtonPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBackground(Color.white);
		return p;
	}

	public boolean returnValue() {
		return retValue;
	}
	
	public boolean isSpaceAvailable(long size)
	{
		Project project = this.selectedProject;
		double sizeInGB = (double)size/(double)(1024*1024*1024);
		if(project.getDiskQuota()< project.getSpaceUsage() + sizeInGB)
		{
			JOptionPane.showMessageDialog(AcquisitionUI.getFrame(), "Selected records will exceed remaining storage quota assigned for selected project\nSelected records size = "+sizeInGB+" GB \nQuota assigned to selected project = "+project.getDiskQuota()+" GB" + "\nRemaining storage quota = "+ (project.getDiskQuota() -project.getSpaceUsage()) + " GB", "Can't upload", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public List<IExperiment> getSelectedExperiments() {
		List<IExperiment> toUpload = new ArrayList<IExperiment>();
		for(int i=0;i<experiments.length;i++){
			if(enabledExperiments[i])
				toUpload.add(experiments[i]);
		}
		
		return toUpload;
	}
}
