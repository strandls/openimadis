package com.strandgenomics.imaging.iacquisition;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.strandgenomics.imaging.iclient.daemon.UploadDaemonService;
import com.strandgenomics.imaging.iclient.daemon.UploadSpecification;
import com.strandgenomics.imaging.icore.IExperiment;

public class UploadDaemonTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8141453740586119365L;
	/**
	 * logged in user
	 */
	private final String user;
	/**
	 * list of upload specifications given by user
	 */
	private List<UploadSpecification> uploadSpecs;
	/**
	 * the columns shown in Upload table
	 */
	private String columnNames[] = { "Source File", "Source File Size", "No. of Records", "Target Project", "Status" };
	
	public UploadDaemonTableModel(String user) {
		this.user = user;
		uploadSpecs = new ArrayList<UploadSpecification>();
	}

	/**
	 * custom implementation of addRow(data) method of table model
	 * 
	 * @param experimentsToUpload
	 *            the experiment to be uploaded
	 */
	public void addExperiment(List<IExperiment> experimentsToUpload, List<String> projectName) 
	{
		fireTableChanged(new TableModelEvent(this));
	}

	@Override
	public int getRowCount() {
		if (uploadSpecs == null)
			return 0;
		return uploadSpecs.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return String.class;
		if (columnIndex == 1)
			return String.class;
		if (columnIndex == 2)
			return Integer.class;
		if (columnIndex == 3)
			return String.class;
		if (columnIndex == 4)
			return String.class;
		else
			return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (uploadSpecs == null)
			return 0;
		IExperiment expr = uploadSpecs.get(rowIndex).getExperiment();
		if (columnIndex == 0){
			String fullSourceFileName = expr.getReference().get(0)
					.getSourceFile();
			String separator = File.separator;
			return fullSourceFileName.substring(fullSourceFileName
					.lastIndexOf(separator) + 1);
		}
		if (columnIndex == 1)
			return AcqclientUIUtils.getExperimentSize(expr);
		if (columnIndex == 2)
			return expr.getRecordSignatures().size();
		if (columnIndex == 3)
			return uploadSpecs.get(rowIndex).getProjectName();
		if (columnIndex == 4)
			return uploadSpecs.get(rowIndex).getMessage().isEmpty() ? uploadSpecs.get(rowIndex).getStatus() : uploadSpecs.get(rowIndex).getMessage();
		else
			return null;
	}

	/**
	 * clear the experiments which have completed their upload
	 * @throws Exception 
	 * @throws RemoteException 
	 */
	public void clearUploaded() throws Exception 
	{
		String projectName = Context.getInstance().project.getName();
		
		UploadDaemonService serviceStub = getServiceStub();
		if(serviceStub == null)
		{
			JOptionPane.showMessageDialog(null, "Upload Daemon not running.", "Upload Daemon not running.", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		serviceStub.clearCompleted(user, projectName);
		
		refresh();
	}
	
	/**
	 * refresh the model
	 */
	public void refresh() 
	{
		System.out.println("******** refreshing the UploadDaemonTableModel");
		try
		{
			getStatus();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		if(uploadSpecs == null)
			return;
		
		System.out.println("************** firing table changed event");
		fireTableChanged(new TableModelEvent(this));
		
		for(UploadSpecification spec: uploadSpecs)
		{
			Context.getInstance().setBackgroundUploadStatus(spec.getExperiment(), spec.getStatus());
		}
	}
	
	private UploadDaemonService getServiceStub() throws Exception
	{
		return Context.getInstance().getServiceStub();
	}
	
	private void getStatus()
	{
		System.out.println("************** getting status");
		String projectName = Context.getInstance().project.getName();
		
		List<UploadSpecification> statusList = null;
		try
		{
			UploadDaemonService serviceStub = getServiceStub();
			if(serviceStub == null)
				return;
			statusList = serviceStub.getStatus(user, projectName);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(statusList!=null)
		{
			uploadSpecs = statusList;
		}
	}

	/**
	 * attempts to cancel the selected upload tasks
	 * @param selectedRows 
	 */
	public void cancelSelected(int[] selectedRows) 
	{
		try
		{
			UploadDaemonService serviceStub = getServiceStub();
			if(serviceStub == null)
			{
				JOptionPane.showMessageDialog(null, "Upload Daemon not running.", "Upload Daemon not running.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			List<Long> toCancel = new ArrayList<Long>();
			for(int index:selectedRows)
			{
				toCancel.add(uploadSpecs.get(index).getUploadId());
			}
			
			serviceStub.cancelSelected(user, toCancel);
			
			fireTableChanged(new TableModelEvent(this));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override 
    public void fireTableDataChanged() { 
        fireTableChanged(new TableModelEvent(this, //tableModel 
                                             0, //firstRow 
                                             getRowCount() - 1, //lastRow  
                                             TableModelEvent.ALL_COLUMNS, //column  
                                             TableModelEvent.UPDATE)); //changeType 
    } 

}