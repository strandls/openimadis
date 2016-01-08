/**
 * 
 */
package com.strandgenomics.imaging.iacquisition;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.strandgenomics.imaging.iacquisition.selection.IRecordListener;
import com.strandgenomics.imaging.iacquisition.selection.IRecordSelectionListener;
import com.strandgenomics.imaging.iacquisition.selection.RecordSelectionEvent;
import com.strandgenomics.imaging.iacquisition.selection.RecordsChangedEvent;
import com.strandgenomics.imaging.iclient.ImageSpaceException;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.daemon.UploadDaemonService;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.UploadStatus;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.IValidator;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;

/**
 * @author nimisha
 * 
 */
public class Context implements IRecordSelectionListener, IValidator {

	private static Context context = new Context();

	private List<IRecord> records;
	private List<IRecord> selectedRecords;
	private List<IRecord> checkedRecords;
	Project project;

	/**
	 * monitors upload progress, used during tool shutdown
	 */
	private List<IRecordListener> recordListeners = new ArrayList<IRecordListener>();

	private List<IRecordSelectionListener> selectionListeners = new ArrayList<IRecordSelectionListener>();

	private List<DirectUploadListener> directUploadListeners = new ArrayList<DirectUploadListener>();

	/**
	 * size of import queue
	 */
	private int importQueueSize = 0;

	private Map<String, String> channelClipboard;

	private int port = 5555;

	private UploadDaemonService serviceStub = null;

	private String version = "1.20";

	private List<Project> activeProjects;

	public static Context getInstance()
	{
		if (context == null)
			context = new Context();
		return context;
	}

	private Context()
	{
		init();
	}

	public void init()
	{
		records = new ArrayList<IRecord>();
		selectedRecords = new ArrayList<IRecord>();
		channelClipboard = new HashMap<String, String>();
		project = null;
	}

	public void addRecordListener(IRecordListener listener)
	{
		recordListeners.add(listener);
	}

	public void removeRecordListener(IRecordListener listener)
	{
		recordListeners.remove(listener);
	}

	private void fireRecordsChangedEvent(int type)
	{
		fireRecordsChangedEvent(type, null);
	}

	private void fireRecordsChangedEvent(int type, String key)
	{
		RecordsChangedEvent event = new RecordsChangedEvent(type, key);
		for (IRecordListener listener : recordListeners)
		{
			listener.recordsChanged(event);
		}
	}

	private void fireAddDirectUploadExperiments(List<RawExperiment> experimentList)
	{
		for (DirectUploadListener listener : directUploadListeners)
		{
			listener.experimentsAdded(experimentList);
		}
	}

	public void addDirectUploadListener(DirectUploadListener listener)
	{
		if (directUploadListeners == null)
		{
			directUploadListeners = new ArrayList<DirectUploadListener>();
		}
		directUploadListeners.add(listener);
	}

	public List<IRecord> getRecords()
	{
		return records;
	}

	public Project getProject()
	{
		return project;
	}

	// TODO: Should be changed. This is a hack for spreadsheet and thumbnails to
	// work
	// as they hold pointer to records and not context.
	public void setProject(Project project)
	{
		this.project = project;
		records.removeAll(records);
		selectedRecords.removeAll(records);
		fireRecordsChangedEvent(RecordsChangedEvent.RECORDS_REMOVED);
		fireSelectionEvent(selectedRecords);
	}

	public int getRecordCount()
	{
		return records.size();
	}

	public void addRecords(List<IRecord> recordList)
	{
		records.addAll(recordList);
		Set<IExperiment> experiments = new HashSet<IExperiment>();
		for (IRecord record : recordList)
		{
			IExperiment experiment = record.getExperiment();
			if (!experiments.contains(experiment))
				experiments.add(experiment);
		}
		for (IExperiment experiment : experiments)
		{
			if (experiment instanceof RawExperiment)
			{
				RawExperiment rawExperiment = (RawExperiment) experiment;
				if (rawExperiment.getUploadStatus() == UploadStatus.NotUploaded || rawExperiment.getUploadStatus() == UploadStatus.Queued)
				{
					boolean uploaded = isUploaded(experiment);
					if (uploaded)
						rawExperiment.setUploadStatus(UploadStatus.Duplicate);
					else
						rawExperiment.setUploadStatus(UploadStatus.NotUploaded);
				}
			}
		}
		fireRecordsChangedEvent(RecordsChangedEvent.RECORDS_ADDED);
	}

	public List<Project> getActiveProjects()
	{
		
		System.out.println("Getting all the projects...\n");
		try
		{
			activeProjects = ImageSpaceObject.getConnectionManager().getActiveProjects();
		}
		catch (final ImageSpaceException e)
		{
		}
		return activeProjects;
	}

	public void addDirectImportExperiments(List<RawExperiment> experimentList)
	{
		fireAddDirectUploadExperiments(experimentList);
	}

	public void updateUploadStatus(List<IRecord> recordList)
	{
		Set<IExperiment> experiments = new HashSet<IExperiment>();
		for (IRecord record : recordList)
		{
			IExperiment experiment = record.getExperiment();
			if (!experiments.contains(experiment))
				experiments.add(experiment);
		}
		for (IExperiment experiment : experiments)
		{
			if (experiment instanceof RawExperiment)
			{
				RawExperiment rawExperiment = (RawExperiment) experiment;
				boolean uploaded = isUploaded(experiment);
				if (uploaded)
					rawExperiment.setUploadStatus(UploadStatus.Duplicate);
				else
					rawExperiment.setUploadStatus(UploadStatus.NotUploaded);
			}
		}
		fireRecordsChangedEvent(RecordsChangedEvent.ANNOTATIONS_CHANGED);
	}

	public void addRecord(IRecord record)
	{
		records.add(record);
		fireRecordsChangedEvent(RecordsChangedEvent.RECORDS_ADDED);
	}

	public void deleteRecords(List<IRecord> recordList)
	{
		records.removeAll(recordList);
		fireRecordsChangedEvent(RecordsChangedEvent.RECORDS_REMOVED);
	}

	public List<IRecord> getSelectedRecords()
	{
		return selectedRecords;
	}
	
	public List<IRecord> getCheckedRecords()
	{
//		return checkedRecords;
		return selectedRecords;
	}
	
	public int getCheckedRecordsCount()
	{
//		return checkedRecords == null ? 0 : checkedRecords.size();
		if (selectedRecords == null)
			return 0;
		else
			return selectedRecords.size();
	}

	public int getSelectedRecordCount()
	{
		if (selectedRecords == null)
			return 0;
		else
			return selectedRecords.size();
	}

	public void updateRecordUploadTime()
	{
		fireRecordsChangedEvent(RecordsChangedEvent.ANNOTATIONS_CHANGED);
	}

	public int updateAnnotations(List<IRecord> recordList, String key, Object value, AnnotationType type)
	{
		int count = 0;
		for (IRecord record : recordList)
		{
			if (canUpdateRecord(record))
			{
				if (type.equals(AnnotationType.Text))
					record.addUserAnnotation(key, value.toString());
				else if (type.equals(AnnotationType.Integer))
					record.addUserAnnotation(key, (Long) value);
				else if (type.equals(AnnotationType.Real))
					record.addUserAnnotation(key, (Double) value);
				else if (type.equals(AnnotationType.Time))
					record.addUserAnnotation(key, (Date) value);
				count++;
			}
		}
		if (count > 0)
			fireRecordsChangedEvent(RecordsChangedEvent.ANNOTATION_ADDED, key);
		return count;
	}

	public boolean canUpdateRecord(IRecord record)
	{
		UploadStatus status = getUploadStatus(record);
		if (status == null)
			return true;
		else if (status == UploadStatus.NotUploaded)
			return true;
		else
			return false;
	}

	public boolean canDeleteRecord(IRecord record)
	{
		UploadStatus status = getUploadStatus(record);
		if (status == null)
			return true;
		if (status == UploadStatus.Queued || status == UploadStatus.QueuedBackground)
			return false;
		else
			return true;
	}

	public int removeAnnotations(List<IRecord> recordList, String key)
	{

		int count = 0;
		for (IRecord record : recordList)
		{
			if (canUpdateRecord(record))
			{
				if (record.removeUserAnnotation(key) != null)
					count++;
			}
		}
		if (count > 0)
			fireRecordsChangedEvent(RecordsChangedEvent.ANNOTATION_REMOVED, key);
		return count;
	}

	@Override
	public void selectionChanged(RecordSelectionEvent selectionEvent)
	{
		if (selectionEvent.getSource().equals(this))
		{
			System.out.println("Selection has changed .. Context Generated ");
			return;
		}
		selectedRecords = selectionEvent.getRecords();
		fireSelectionEvent(selectionEvent);
	}

	public void addSelectionListener(IRecordSelectionListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionListener(IRecordSelectionListener listener)
	{
		selectionListeners.remove(listener);
	}

	private void fireSelectionEvent(List<IRecord> selectedRecords)
	{
		RecordSelectionEvent event = new RecordSelectionEvent(selectedRecords, this);
		fireSelectionEvent(event);
	}

	private void fireSelectionEvent(RecordSelectionEvent event)
	{
		for (IRecordSelectionListener listener : selectionListeners)
		{
			listener.selectionChanged(event);
		}
	}

	public void setSelectedRecords(List<IRecord> selectedRecords)
	{
		this.selectedRecords = selectedRecords;
		fireSelectionEvent(selectedRecords);
	}

	public void setSelectedRecord(IRecord selectedRecord)
	{
		List<IRecord> selectedRecords = new ArrayList<IRecord>();
		selectedRecords.add(selectedRecord);
		this.selectedRecords = selectedRecords;
		fireSelectionEvent(selectedRecords);
	}

	public void setUploadStatus(RawExperiment experiment, UploadStatus uploadStatus)
	{
		experiment.setUploadStatus(uploadStatus);
		fireRecordsChangedEvent(RecordsChangedEvent.ANNOTATIONS_CHANGED);
	}

	public void setBackgroundUploadStatus(RawExperiment experiment, UploadStatus uploadStatus)
	{
		for (IRecord record : records)
		{
			RawExperiment exp = (RawExperiment) record.getExperiment();
			if (exp.getSeedFile().getAbsolutePath().equals(experiment.getSeedFile().getAbsolutePath()))
				exp.setUploadStatus(uploadStatus);
		}
		fireRecordsChangedEvent(RecordsChangedEvent.ANNOTATIONS_CHANGED);
	}

	public UploadStatus getUploadStatus(IRecord record)
	{

		if (record.getExperiment() instanceof RawExperiment)
		{
			if (((RawExperiment) record.getExperiment()).isExistOnServer())
			{
				((RawExperiment) record.getExperiment()).setUploadStatus(UploadStatus.Duplicate);
			}
			else
			{
				((RawExperiment) record.getExperiment()).setUploadStatus(UploadStatus.NotUploaded);
			}
			return ((RawExperiment) record.getExperiment()).getUploadStatus();
		}
		else
			return null;
	}

	/*
	 * 
	 * Merges the list of records. If a single record is selected, merges
	 * siblings ..
	 */
	public void mergeRecords(List<IRecord> selectedRecords) throws IllegalArgumentException
	{
		IRecord record = selectedRecords.get(0);
		if (!canUpdateRecord(record))
			throw new IllegalArgumentException("Records queued for upload or successfully uploaded cannot be merged.");

		IExperiment experiment = record.getExperiment();
		if (!(experiment instanceof BioExperiment))
			throw new IllegalArgumentException("Unknown Experiment");

		BioExperiment bioExperiment = (BioExperiment) experiment;
		if (selectedRecords.size() == 1)
		{
			selectedRecords = bioExperiment.getAllSiblings(record);
			if (selectedRecords.size() == 1)
				throw new IllegalArgumentException("No available records with matching dimensions.");
			record = selectedRecords.get(0);
		}

		List<Signature> signatures = new ArrayList<Signature>();
		signatures.add(record.getSignature());

		for (int i = 1; i < selectedRecords.size(); i++)
		{
			IRecord otherRecord = selectedRecords.get(i);

			IExperiment otherExperiment = otherRecord.getExperiment();
			if (experiment != otherExperiment)
				throw new IllegalArgumentException("Records belong to different source files.");
			signatures.add(otherRecord.getSignature());
		}

		try
		{
			// throws exception if the records are not consistent
			// throws exceptions if the records are single site records
			Signature[] signatureArray = (Signature[]) signatures.toArray(new Signature[0]);
			bioExperiment.validateDimensions(signatureArray);
			// If validated does not throw exception, records will be merged ..
			deleteRecords(selectedRecords);
			Signature signature = bioExperiment.mergeRecordsAsSites(signatureArray);
			IRecord newRecord = bioExperiment.getRecord(signature);
			addRecord(newRecord);
			setSelectedRecord(newRecord);
		}
		catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
			throw new IllegalArgumentException("Records dimension do not match.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		}

	}

	@Override
	public boolean isDuplicate(IExperiment experiment)
	{
		for (IRecord record : records)
		{
			if (record.getExperiment().getMD5Signature().equals(experiment.getMD5Signature()))
				return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setUploaded(IExperiment experiment, boolean duplicate)
	{
		if (experiment instanceof RawExperiment)
		{
			UploadStatus status = duplicate ? UploadStatus.Duplicate : UploadStatus.NotUploaded;

			((RawExperiment) experiment).setUploadStatus(status);
		}
	}

	@Override
	public boolean isUploaded(IExperiment experiment)
	{
		if (experiment instanceof RawExperiment)
		{
			boolean duplicate = ((RawExperiment) experiment).isExistOnServer();
			return duplicate;
		}

		return false;
	}

	public List<SearchField> getAllSearchFields()
	{
		ArrayList<SearchField> searchFields = new ArrayList<SearchField>();
		ArrayList<String> annotationName = new ArrayList<String>();
		for (IRecord record : records)
		{
			Map<String, Object> annotations = record.getUserAnnotations();
			for (String key : annotations.keySet())
				if (!annotationName.contains(key))
				{
					Object value = annotations.get(key);
					AnnotationType type = null;
					if (value instanceof Date)
						type = AnnotationType.Time;
					else if (value instanceof Double)
						type = AnnotationType.Real;
					else if (value instanceof Long)
						type = AnnotationType.Integer;
					else if (value instanceof String)
						type = AnnotationType.Text;
					SearchField field = new SearchField(key, type);
					searchFields.add(field);
					annotationName.add(key);
				}
		}
		return searchFields;
	}

	public void setImportQueueSize(int importQueueSize)
	{
		this.importQueueSize = importQueueSize;
	}

	public int getImportQueueSize()
	{
		return this.importQueueSize;
	}

	public void setChannelClipboard(Map<String, String> channelClipboard)
	{
		this.channelClipboard = channelClipboard;
	}

	public Map<String, String> getChannelClipboard()
	{
		return this.channelClipboard;
	}

	public void setUploaderPort(int parseInt)
	{
		this.port = parseInt;
	}

	public int getUploaderPort()
	{
		return this.port;
	}

	public UploadDaemonService getServiceStub()
	{
		if (serviceStub != null)
			return serviceStub;

		Runnable r = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Registry registry = LocateRegistry.getRegistry("localhost", Context.getInstance().getUploaderPort());
					serviceStub = (UploadDaemonService) registry.lookup(UploadDaemonService.class.getCanonicalName());
				}
				catch(java.rmi.ConnectException je){
					System.out.println("Background Upload Service not running.");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};

		Thread t = new Thread(r);
		t.start();

		return serviceStub;
	}

	/**
	 * sets the acq client version
	 * 
	 * @param version
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * returns acq client version
	 * 
	 * @return acq client version
	 */
	public String getVersion()
	{
		return this.version;
	}

	/**
	 * updates the details of the current project; does nothing if name of the
	 * project does not match with p THIS METHOD NOT TO BE CONFUSED WITH
	 * setProject(Project p) in the same class
	 * 
	 * @param p
	 *            new project details
	 */
	public void updateProjectDetails(Project p)
	{
		if (this.project == null)
			return;
		if (this.project.getName().equals(p.getName()))
		{
			this.project = p;
		}
	}
}

