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

package plugins.strand.strandimanagelogin;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Dialog box for user registartion
 * 
 * @author Anup Kulkarni
 */
public class RegisterDialog extends JPanel {

	private static final long serialVersionUID = 7484821731422621819L;
	
	/**
	 * login name
	 */
	private String login;
	
	private String password;
	
	private String rePassword;
	
	private String email;
	
	private String fascilityName;
	
	private String fullName;
	
	private int dataSize;
	
	private WorkProfile profile = WorkProfile.FascilityManager;
	
	private FascilitySize size = FascilitySize.Small;
	
	public RegisterDialog()
	{
		setLayout(new GridLayout(9, 2, 5, 5));

		setupUI();
	}

	private void setupUI()
	{
		JLabel loginLabel = new JLabel("Login");
		final JTextField loginField = new JTextField();
		loginField.addFocusListener(new FocusAdapter()
		{
			
			@Override
			public void focusLost(FocusEvent e)
			{
				login = loginField.getText();
			}
		});
		
		JLabel passwordLabel = new JLabel("Password");
		final JPasswordField passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				password = passwordField.getText();
			}
		});
		
		JLabel repasswordLabel = new JLabel("Re-enter Password");
		final JPasswordField repasswordField = new JPasswordField();
		repasswordField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				rePassword = repasswordField.getText();
			}
		});
		
		JLabel fullNameLabel = new JLabel("Full Name");
		final JTextField nameField = new JTextField();
		nameField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				fullName = nameField.getText();
			}
		});
		
		JLabel profileLabel = new JLabel("Nature of Work");
		final JComboBox<WorkProfile> profileField = new JComboBox<WorkProfile>(WorkProfile.values());
		profileField.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				profile = (WorkProfile) profileField.getSelectedItem();
			}
		});
		
		JLabel emailLabel = new JLabel("Email");
		final JTextField emailField = new JTextField();
		emailField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				email = emailField.getText();
			}
		});
		
		JLabel fascilitylabel = new JLabel("Fascility Name");
		final JTextField fascilityField = new JTextField();
		fascilityField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				fascilityName = fascilityField.getText();
			}
		});
		
		JLabel fascilitySizeLabel = new JLabel("Fascility Size");
		final JComboBox<FascilitySize> sizeField = new JComboBox<FascilitySize>(FascilitySize.values());
		sizeField.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				size = (FascilitySize) sizeField.getSelectedItem();
			}
		});
		
		JLabel dataLabel = new JLabel("Data Generated per Year(in TB)");
		final JTextField dataField = new JTextField();
		dataField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				dataSize = Integer.parseInt(dataField.getText());
			}
		});
		
		add(loginLabel);
		add(loginField);
		
		add(passwordLabel);
		add(passwordField);
		
		add(repasswordLabel);
		add(repasswordField);

		add(emailLabel);
		add(emailField);
		
		add(fullNameLabel);
		add(nameField);
		
		add(profileLabel);
		add(profileField);
		
		add(fascilitylabel);
		add(fascilityField);
		
		add(fascilitySizeLabel);
		add(sizeField);
		
		add(dataLabel);
		add(dataField);
	}
	
	public UserInformation getUserInformation()
	{
		return new UserInformation(login, password, fullName, email, profile, fascilityName, size, dataSize);
	}

	public boolean isConsistent()
	{
		if(!password.equals(rePassword))
		{
			JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else if(!email.contains("@") || !email.contains("."))
		{
			JOptionPane.showMessageDialog(null, "Invalid email address", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}