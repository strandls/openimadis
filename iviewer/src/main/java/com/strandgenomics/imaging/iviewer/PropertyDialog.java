package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jidesoft.combobox.DateChooserPanel;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.SearchField;



public class PropertyDialog extends JDialog {

    private JButton btnOK;
    private JButton btnCancel;
    private boolean succeeded = false;
    
	private JComboBox cbKey;
	private JComboBox cbType;
	private JTextField tfValue;
	
	private JLabel lbKey;
	private JLabel lbType;
    private JLabel lbValue;
    
    private static String TEXT = "Text";
    private static String NUMBER = "Number";
    private static String DECIMAL = "Decimal";
    private static String DATE = "Date (yyyy/mm/dd)";

    public PropertyDialog(Frame parent, boolean add, List<SearchField> properties){
    	 super(parent,"Add Property" , true);
    	if(!add)
    		setTitle("Remove Property");
       
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        
        cs.fill = GridBagConstraints.BOTH;
    	cs.insets = new Insets(3, 3, 3, 3);
        
        lbKey = new JLabel("Property Name: ", SwingConstants.RIGHT);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(lbKey, cs);
        
        cbKey = new JComboBox();
        if(add){
        	cbKey.addItem(NEW);
        	cbKey.setEditable(true);
        	cbKey.getEditor().selectAll();
        }
        for(SearchField field: properties){
        	cbKey.addItem(field);
        	System.out.println("Field: " + field + " Type: " + field.getType());
        }
        cs.gridx = 2;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(cbKey, cs);
        cbKey.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
            	AnnotationType type = getSelectedFieldType();
            	if( cbKey.getSelectedItem() == NEW){
            		cbType.setEnabled(true);
            		cbKey.setEditable(true);
            		cbKey.getEditor().selectAll();
            	}else {
            		cbKey.setEditable(false);
            		if(AnnotationType.Time.equals(type))
            			cbType.setSelectedItem(DATE);
            		else if(AnnotationType.Real.equals(type))
            			cbType.setSelectedItem(DECIMAL);
            		else  if(AnnotationType.Integer.equals(type))
            			cbType.setSelectedItem(NUMBER);
            		else
            			cbType.setSelectedItem(TEXT);
            		cbType.setEnabled(false);
            	}
                
            }
        });
        
        lbType = new JLabel("Property Type: ", SwingConstants.RIGHT);
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(lbType, cs);
        lbType.setEnabled(add);
        
        
        cbType = new JComboBox();
        cbType.addItem(TEXT);
        cbType.addItem(NUMBER);
        cbType.addItem(DECIMAL);
        cbType.addItem(DATE);
        cs.gridx = 2;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(cbType, cs);
        cbType.setEnabled(add);
        
        lbValue = new JLabel("Property Value:  ", SwingConstants.RIGHT);
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(lbValue, cs);
        lbValue.setEnabled(add);
        
        tfValue = new JTextField(20);
        cs.gridx = 2;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(tfValue, cs);
        tfValue.setEnabled(add);
        
        tfValue.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(cbType.getSelectedItem() == DATE)
					showCalendarDialog();
			}
		});

        btnOK = new JButton("OK");

        btnOK.addActionListener(new ActionListener() {

            @Override
			public void actionPerformed(ActionEvent e) {
            	
                if (getPropertyName()!=null && getPropertyName()!="") {
                	if(!cbKey.isEditable() || (getPropertyValue() != null)){
                		succeeded=true;
                		dispose();
                	}else
                		JOptionPane.showMessageDialog(PropertyDialog.this,
                                "Please add a valid Property Value.",
                                "Invalid Property Value",
                                JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(PropertyDialog.this,
                            "Please add a valid Property Name.",
                            "Invalid Property Name",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            @Override
			public void actionPerformed(ActionEvent e) {
            	succeeded=false;
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnOK);
        bp.add(btnCancel);

        getRootPane().setDefaultButton(btnOK);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    private void showCalendarDialog()
	{
    	DateChooserPanel dcp = new DateChooserPanel(true, false);
		int value = JOptionPane.showConfirmDialog(null, dcp, "Choose Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
		if(value == JOptionPane.OK_OPTION){
			int year = dcp.getSelectedCalendar().get(Calendar.YEAR);
			int month = dcp.getSelectedCalendar().get(Calendar.MONTH)+1;
			int day = dcp.getSelectedCalendar().get(Calendar.DAY_OF_MONTH);
			
			tfValue.setText(year+"/"+month+"/"+day);
		}
	}

	public AnnotationType getSelectedFieldType(){
    	Object field = cbKey.getSelectedItem();
    	System.out.println(field);
    	if(field instanceof SearchField)
    		return ((SearchField) field).getType();
    	else
    		return null;
    }
    
    public String getPropertyName() {
    	Object name = cbKey.getSelectedItem();
    	if(name!=null)
    		return cbKey.getSelectedItem().toString();
    	else
    		return null;
    }

    public Object getPropertyValue() {
        String value = tfValue.getText().trim();
        String type = cbType.getSelectedItem().toString();
        if(type.equals(DECIMAL))
        try{
    		return Double.parseDouble(value);
        }catch(NumberFormatException e){
        	return null;
        }
    	else if(type.equals(NUMBER)){
    		try{
    			return Long.parseLong(value);
            }catch(NumberFormatException e){
            	return null;
            }
    	}
    	else if(type.equals(DATE)){
    		try{
    			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
    			return dateFormat.parse(value);
    		}catch(ParseException e){
    			return null;
    		}
    	}
    	else
    		return value.toString();
    }
    
    public AnnotationType getPropertyType() {
    	String type = cbType.getSelectedItem().toString();
    	if(type.equals(DECIMAL))
    		return AnnotationType.Real;
    	else if(type.equals(NUMBER))
    		return AnnotationType.Integer;
    	else if(type.equals(DATE))
    		return AnnotationType.Time;
    	else
    		return AnnotationType.Text;
    }

    public boolean isSucceeded() {
        return succeeded;
    }
    
    // Object for Enter New
    // NEED TO REMOVE...
    protected static final Object NEW = new Object() {
    public String toString() {
    	return "EnterNew";
    }
 };
}



