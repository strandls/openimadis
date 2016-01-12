/*
 * LoginDialog.java
 *
 * AVADIS Image Management System
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iclient.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.strandgenomics.imaging.iclient.util.ConfigurationException;

/**
 * Dialog Box for editing the connection properties to connect to the server
 * please check <a href="http://java.sun.com/j2se/1.6.0/docs/guide/net/properties.html">Network properties</a>
 *
 * @see org.apache.axis.components.net.DefaultHTTPTransportClientProperties
 * @see org.apache.axis.components.net.DefaultHTTPSTransportClientProperties
 * @see org.apache.commons.discovery.tools.ManagedProperties
 * @see org.apache.axis.AxisProperties
 * 
 * @author arunabha
 */
public class LoginDialog extends JDialog  {
    
	private static final long serialVersionUID = -5927483904894608588L;
	//TODO from load from configuration
	public static final Font LABEL_FONT        = new Font ("Arial", Font.PLAIN, 11);
	public static final Dimension LABEL_SIZE   = new Dimension(120,15);
	
	/**
	 * underlying login information
	 */
	protected LoginDocomentModel docModel = null;
	
	protected boolean cancelled;
	private boolean maskLoginField = false;
	private JComboBox<String> protocolCheckBox;

    /**
     * Dialog to Display/Edit Connection Preferences 
     */
    public LoginDialog(JFrame applicationFrame, String name, boolean maskLoginField) 
    {
        super(applicationFrame, name, true);
        this.maskLoginField  = maskLoginField;
        // initializes the document model
		docModel = new LoginDocomentModel();
		
		//create the UI
		setContentPane( createUI() );
		//get it ready to be visible
		readyDialog();
    }
    
    public void readyDialog()
    {
        int preferredWidth  = 320;
        int preferredHeight = 280;
        
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        
        Point location = getMiddlePosition(preferredWidth, preferredHeight);
        setLocation(location.x, location.y);

        addWindowListener(new WindowAdapter() 
        {
        	public void windowClosing(WindowEvent e) 
        	{
        		setVisible(false);
        	}
        });

        setResizable(true);
        pack();
    }
    
    /**
     * validate and set connection parameters 
     * @return true if done successfully, false otherwise
     */
	private boolean validateAndSetParameters()
	{
		try
		{
			docModel.validateAndSetParameters();
			return true;
		}
		catch(ConfigurationException cax)
		{
			showErrorMessage(cax.getCategory(), cax.getMessage() +(cax.getValue() == null ? "" : cax.getValue()));
			return false;
		}
    }
	
	/**
	 * Creates and return the JPanel representing the main GUI of this dialog
	 * @return The JPanel of the UI
	 */
	private JPanel createUI()
	{
        JPanel contentPane = new JPanel();
        contentPane.setLayout( new BorderLayout() );
        
        contentPane.add( createTabPanel(), BorderLayout.CENTER );
        contentPane.add( createButtonPanel(), BorderLayout.SOUTH );
        
        return contentPane;
	}
	
	/**
	 * Creates the Button Panel and return the JPanel representing the UI
	 * @return the JPanel representing the Button Panel
	 */
	private JPanel createButtonPanel()
	{
        @SuppressWarnings("serial")
		AbstractAction okAction = new AbstractAction("OK", null)
        {
            public void actionPerformed(ActionEvent e){
            	String p = null;
				if(protocolCheckBox!=null)
					p =  (String) protocolCheckBox.getSelectedItem();

				if(p.equals("HTTP")){
					docModel.setProtocol(0);
					docModel.useSSL = false;
				}
				else if(p.equals("HTTPS")){
					docModel.setProtocol(1);
					docModel.useSSL = true;
				}
					
                boolean validParameters = validateAndSetParameters();
                
                
                if(!validParameters)
                {
                    return;
                }

                cancelled = false;
                setVisible(false);
                dispose();
            }
        };
        
        @SuppressWarnings("serial")
		AbstractAction cancelAction = new AbstractAction("Cancel", null)
        {
            public void actionPerformed(ActionEvent e)
            {
            	cancelled = true;
                setVisible(false);
                dispose();
            }
        };
        
        //associate keyboard shortcuts for OK and Cancel
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKeyStroke, "ENTER");
        getRootPane().getActionMap().put("ENTER", okAction);

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", cancelAction);
        
        Action[] buttonActions = {okAction, cancelAction};
        return new HorizontalButtonPanel(buttonActions, FlowLayout.RIGHT);
	}
    

	/**
	 * Creates and return the JPanel representing the tabbed-panel in the UI
	 * @return return the JPanel representing the tabbed-panel in the UI
	 */
    private JTabbedPane createTabPanel()
    {
    	//the components are final since they are used within the anonymous class
        final JTextField proxyHostBox = new JTextField(docModel.getProxyServerAddressField(), null, 20);
        proxyHostBox.setEnabled(docModel.useProxy());  

        final JTextField proxyPortBox = new JTextField(docModel.getProxyPortField(), null, 20);
        proxyPortBox.setEnabled(docModel.useProxy());
       
        final JTextField proxyUserBox = new JTextField(docModel.getProxyUserField(), null, 20);
        proxyUserBox.setEnabled(docModel.useProxy());
     
        final JPasswordField proxyPasswordBox = new JPasswordField(docModel.getProxyPasswordField(), null, 20);
        proxyPasswordBox.setEnabled(docModel.useProxy());
        
        final JCheckBox proxyEnableBox = new JCheckBox();
        proxyEnableBox.setFont(LABEL_FONT);
        proxyEnableBox.setSelected(docModel.useProxy());

        @SuppressWarnings("serial")
		AbstractAction checkProxyAction = new AbstractAction("", null)
        {
            public void actionPerformed(ActionEvent e)
            {
            	boolean useProxy = proxyEnableBox.isSelected();;
                proxyHostBox.setEnabled(useProxy);
                proxyPortBox.setEnabled(useProxy);
                proxyUserBox.setEnabled(useProxy);
                proxyPasswordBox.setEnabled(useProxy);
                
                docModel.setProxyEnabled(useProxy);
            }
        };
        proxyEnableBox.setAction(checkProxyAction);
        
        JPasswordField passwordBox = new JPasswordField(docModel.getPasswordField(), null, 20);
        if(maskLoginField)
        	passwordBox.setText("");
        JTextField hostIPBox   = new JTextField(docModel.getServerAddressField(), null, 20);
        hostIPBox.setEnabled(!maskLoginField);

        JTextField hostPortBox = new JTextField(docModel.getServerPortField(), null, 20);
        hostPortBox.setEnabled(!maskLoginField);
        final JTextField portListener = hostPortBox;
        portListener.getDocument().addDocumentListener(new DocumentListener() {
        	  public void changedUpdate(DocumentEvent e) {
        	    setProtocol();
        	  }
        	  public void removeUpdate(DocumentEvent e) {
        		setProtocol();
        	  }
        	  public void insertUpdate(DocumentEvent e) {
        		setProtocol();
        	  }

        	  public void setProtocol() {
        	     if (portListener.getText().length()>0){
        	    	 String port = portListener.getText();
                     //System.out.println(port);
                     if(port.equals("443")){
                   	  protocolCheckBox.setSelectedIndex(1);
                     }
                     else if(port.equals("8080")|| port.equals("80")){
                   	  protocolCheckBox.setSelectedIndex(0);
                     }
        	     }
        	  }
        	});
        JTextField loginBox    = new JTextField(docModel.getLoginField(), null, 20);
        loginBox.setEnabled(!maskLoginField);
        
        String[] choices = { "HTTP","HTTPS"};
        protocolCheckBox = new JComboBox<String>(choices);
        protocolCheckBox.setFont(LABEL_FONT);
        if(hostPortBox.getText()!=null){
        	String port = hostPortBox.getText();
        	System.out.println("port" + port);
        	if(port.equals("443")){
        		protocolCheckBox.setSelectedIndex(1);
        	}
        	else if(port.equals("8080")|| port.equals("80")){
          	  protocolCheckBox.setSelectedIndex(0);
            }
        }
        
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Primary", constructPrimaryPanel(hostIPBox,hostPortBox,loginBox,passwordBox,protocolCheckBox));
        tabPane.addTab("Proxy",   constructProxyPanel(proxyHostBox, proxyPortBox, proxyUserBox, proxyPasswordBox, proxyEnableBox));

        tabPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return tabPane;
    }

    /**
     * Creates and returns JPanel representing the primary connection panel
     * @param hostIPBox the text field for the server address
     * @param hostPortBox the text field for the server port
     * @param loginBox the text field for the login
     * @param passwordBox the password text field for user's password
     * @return the JPanel representing the primary connection panel
     */
    private JPanel constructPrimaryPanel(JTextField hostIPBox, JTextField hostPortBox, 
    		JTextField loginBox, JPasswordField passwordBox,JComboBox<String> protocolCheckBox)
    {
        JPanel preferencePanel = new JPanel(new GridLayout(5,2,10,10));
        preferencePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JLabel ipLabel = new JLabel("Server IP:");
        ipLabel.setFont(LABEL_FONT);
        preferencePanel.add(ipLabel);
        preferencePanel.add(hostIPBox);

        JLabel portLabel = new JLabel("Server Port:");
        portLabel.setFont(LABEL_FONT);
        preferencePanel.add(portLabel);
        preferencePanel.add(hostPortBox);

        JLabel loginLabel = new JLabel("Login Name:");
        loginLabel.setFont(LABEL_FONT);
        preferencePanel.add(loginLabel);
        preferencePanel.add(loginBox);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(LABEL_FONT);
        preferencePanel.add(passLabel);
        preferencePanel.add(passwordBox);
        
        JLabel protocolLabel = new JLabel("Protocol:");
        protocolLabel.setFont(LABEL_FONT);
        preferencePanel.add(protocolLabel);
        preferencePanel.add(protocolCheckBox);

        return preferencePanel;
    }

    /**
     * Creates and returns JPanel representing the proxy connection panel
     * @param proxyHostBox the text field for the proxy server address
     * @param proxyPortBox the text field for the proxy server port
     * @param proxyUserBox  the text field for the proxy user
     * @param proxyPasswordBox  the password text field for proxy user's password
     * @param proxyEnableBox to check whether to use the proxy details
     * @return the JPanel representing the proxy connection panel
     */
    private JPanel constructProxyPanel(JTextField proxyHostBox, JTextField proxyPortBox,
    		JTextField proxyUserBox, JPasswordField proxyPasswordBox, JCheckBox proxyEnableBox)
    {

        JPanel proxyPanel = new JPanel(new GridLayout(5,2,10,10));
        proxyPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JLabel serverLabel = new JLabel("Proxy Host:");
        serverLabel.setPreferredSize(LABEL_SIZE);
        serverLabel.setFont(LABEL_FONT);

        proxyPanel.add(serverLabel);
        proxyPanel.add(proxyHostBox);

        JLabel portLabel = new JLabel("Proxy Port:");
        portLabel.setPreferredSize(LABEL_SIZE);
        portLabel.setFont(LABEL_FONT);

        proxyPanel.add(portLabel);
        proxyPanel.add(proxyPortBox);

        JLabel userLabel = new JLabel("User:");
        userLabel.setPreferredSize(LABEL_SIZE);
        userLabel.setFont(LABEL_FONT);

        proxyPanel.add(userLabel);
        proxyPanel.add(proxyUserBox);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setPreferredSize(LABEL_SIZE);
        passLabel.setFont(LABEL_FONT);

        proxyPanel.add(passLabel);
        proxyPanel.add(proxyPasswordBox);

        JLabel proxyLabel = new JLabel("Use Proxy:");
        proxyLabel.setFont(LABEL_FONT);

        proxyPanel.add(proxyLabel);
        proxyPanel.add(proxyEnableBox);
        
        return proxyPanel;
    }


   ///////////////////////////////////////////////////////////////////////////////////

    public void showMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showWarningMessage(String title, String message)
    {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public static Point getMiddlePosition(int width, int height)
    {
        Toolkit toolBox = Toolkit.getDefaultToolkit();
        //get the actual physical size of the physical screen
        Dimension screenBounds = toolBox.getScreenSize();
        
        if(width >= screenBounds.width || height > screenBounds.height)
            return new Point(0,0);

        int x = (screenBounds.width - width)/2;
        int y = (screenBounds.height - height)/2;

        return new Point(x,y);
    }
    
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("ArrayAssist Enterprise Editon");
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

        LoginDialog dialog = new LoginDialog(frame, "Server Connection Preferences", false);
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

    
    public boolean isCancelled(){
    	return cancelled;
    }

}
