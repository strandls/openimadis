package com.strandgenomics.imaging.iacquisition;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.strandgenomics.imaging.iclient.AcquisitionProfile;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;

public class ProfileDialog extends JPanel {
	
	private JTextField microscopeField;
	
	private JTextField xPixelField;
	private JComboBox xFieldType;
	
	private JTextField yPixelField;
	private JComboBox yFieldType;
	
	private JTextField zPixelField;
	private JComboBox zFieldType;
	
	private JComboBox timeUnitField;
	private JComboBox lengthUnitField;

	private JComboBox nameField;
	
	private List<AcquisitionProfile> profiles;
	
	public ProfileDialog()
	{
		setLayout(new GridLayout(10, 2, 5, 5));
		
		profiles = new ArrayList<AcquisitionProfile>(getProfilesForCurrentMicroscope());
		
		setupUI();
		nameField.setSelectedIndex(0);
	}
	
	private List<AcquisitionProfile> getProfilesForCurrentMicroscope()
	{
		String microscopeName = ImageSpaceObject.getImageSpace().getMicroscope();
		
		List<AcquisitionProfile> selectedProfiles = new ArrayList<AcquisitionProfile>();
		
		List<AcquisitionProfile> allProfiles = ImageSpaceObject.getImageSpace().listAcquisitionProfiles();
		if(allProfiles!=null)
		{
			for(AcquisitionProfile profile:allProfiles)
			{
				if(profile.getName().equals(microscopeName))
				{
					selectedProfiles.add(profile);
				}
			}
		}
		
		if(microscopeName == null)
			microscopeName = "Default";
		selectedProfiles.add(0, new AcquisitionProfile("New-Profile", microscopeName, 1.0, "VALUE", 1.0, "VALUE", 1.0, "VALUE", "", "MICROSECONDS", "MICROMETER"));
		
		return selectedProfiles;
	}
	
	private void setupUI()
	{
		JLabel nameLabel = new JLabel("Profile Name");
		nameField = new JComboBox(profiles.toArray(new AcquisitionProfile[0]));
		nameField.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				AcquisitionProfile selectedProfile = (AcquisitionProfile) nameField.getSelectedItem();
				microscopeField.setText(selectedProfile.getName());
				
				xPixelField.setText(String.valueOf(selectedProfile.getxPixelSize()));
				yPixelField.setText(String.valueOf(selectedProfile.getyPixelSize()));
				zPixelField.setText(String.valueOf(selectedProfile.getzPixelSize()));
				
				xFieldType.setSelectedItem(selectedProfile.getxType());
				yFieldType.setSelectedItem(selectedProfile.getyType());
				zFieldType.setSelectedItem(selectedProfile.getzType());
//				sourceFormatField.setText(selectedProfile.getSourceFormat());
				
				timeUnitField.setSelectedItem(selectedProfile.getTimeUnit());
				lengthUnitField.setSelectedItem(selectedProfile.getLengthUnit());
				
			}
		});
		
		JLabel microscopeNameLabel = new JLabel("Microscope Name");
		microscopeField = new JTextField();
		
		JLabel xPixelSizeLabel = new JLabel("X Pixel Size");
		xPixelField = new JTextField();
		
		JLabel yPixelSizeLabel = new JLabel("Y Pixel Size");
		yPixelField = new JTextField();
		
		JLabel zPixelSizeLabel = new JLabel("Z Pixel Size");
		zPixelField = new JTextField();
		
//		JLabel sourceFormatLabel = new JLabel("Source Format");
//		sourceFormatField = new JTextField();
		
		String[] typeValues = {"VALUE","FACTOR"};
		JLabel xFieldTypeLabel = new JLabel("X Type");
		xFieldType = new JComboBox(typeValues);
		xFieldType.setSelectedIndex(0);
		
		JLabel yFieldTypeLabel = new JLabel("Y Type");
		yFieldType = new JComboBox(typeValues);
		yFieldType.setSelectedIndex(0);
		
		JLabel zFieldTypeLabel = new JLabel("Z Type");
		zFieldType = new JComboBox(typeValues);
		zFieldType.setSelectedIndex(0);
		
		JLabel timeUnitLabel = new JLabel("Time Unit");
		String[] timeValues = {"SECONDS","MILISECONDS", "MICROSECONDS", "NANOSECONDS"}; 
		timeUnitField = new JComboBox(timeValues);
		timeUnitField.setSelectedIndex(1);
		
		JLabel lengthUnitLabel = new JLabel("Length Unit");
		String[] lengthValues = {"MILIMETER", "MICROMETER", "NANOMETER"};
		lengthUnitField = new JComboBox(lengthValues);
		lengthUnitField.setSelectedIndex(1);
		
		add(nameLabel);
		add(nameField);
		
		add(microscopeNameLabel);
		add(microscopeField);
		
		add(xFieldTypeLabel);
		add(xFieldType);
		add(xPixelSizeLabel);
		add(xPixelField);
		
		add(yFieldTypeLabel);
		add(yFieldType);
		add(yPixelSizeLabel);
		add(yPixelField);
		
		add(zFieldTypeLabel);
		add(zFieldType);
		add(zPixelSizeLabel);
		add(zPixelField);
		
//		add(sourceFormatLabel);
//		add(sourceFormatField);
		
		add(timeUnitLabel);
		add(timeUnitField);
		
		add(lengthUnitLabel);
		add(lengthUnitField);
	}
	
	public AcquisitionProfile getAcquisitionProfile()
	{
		Double xPixelSize = null;
		try
		{
			xPixelSize = Double.parseDouble(xPixelField.getText());
		}
		catch (Exception e)
		{}
		
		Double yPixelSize = null;
		
		try
		{
			yPixelSize = Double.parseDouble(yPixelField.getText());
		}
		catch (Exception e)
		{}
		
		Double zPixelSize = null;
		try
		{
			zPixelSize = Double.parseDouble(zPixelField.getText());	
		}
		catch(Exception e)
		{}
		
		AcquisitionProfile profile = new AcquisitionProfile(((AcquisitionProfile)nameField.getSelectedItem()).getProfileName(), microscopeField.getText(), xPixelSize, (String)xFieldType.getSelectedItem(), yPixelSize, (String)yFieldType.getSelectedItem(), zPixelSize, (String)zFieldType.getSelectedItem(), null, (String)timeUnitField.getSelectedItem(), (String)lengthUnitField.getSelectedItem());
		
		return profile;
	}
	
	public static void main(String[] args)
	{
		ProfileDialog pd = new ProfileDialog();
		int value = JOptionPane.showConfirmDialog(null, pd, "Add Profile", JOptionPane.PLAIN_MESSAGE);
		System.out.println(value);
		if(value == JOptionPane.OK_OPTION)
			System.out.println(pd.getAcquisitionProfile());
	}
}
