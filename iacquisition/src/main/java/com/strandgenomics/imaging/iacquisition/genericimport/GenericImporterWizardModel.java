package com.strandgenomics.imaging.iacquisition.genericimport;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;

import com.jidesoft.wizard.WizardDialog;
import com.strandgenomics.imaging.iclient.local.genericimport.ImportFilter;
import com.strandgenomics.imaging.icore.bioformats.custom.FieldType;
import com.strandgenomics.imaging.icore.bioformats.custom.ImgFormatConstants;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;

/**
 * model driving generic importer wizard
 * 
 * @author Anup Kulkarni
 */
public class GenericImporterWizardModel {
	
	/**
	 * separator separating the filename fields
	 */
	private String separator = "_";
	/**
	 * root directory
	 */
	private String rootDirectory = "";
	/**
	 * map specifying position of each identified field on filename
	 */
	private Map<Integer, FieldType> positioToFieldMap;
	/**
	 * file explicitly selected by user
	 */
	private String selectedFile = "";
	
	private List<RecordMetaData> selectedRecords;
	
	private WizardDialog parent;
	
	private ImportFilter importFilter;
	
	private Collection<RecordMetaData> records;
	/**
	 * validate all image files for consistent metadata
	 */
	private boolean doValidation;
	
	private FieldType multiImageDimension = FieldType.IGNORE;
	
	public GenericImporterWizardModel()
	{ 
		positioToFieldMap = new HashMap<Integer, FieldType>();
	}

	public void setRootDirectory(String rootDirectory)
	{
		this.rootDirectory = rootDirectory;
	}

	public String getRootDirectory()
	{
		return rootDirectory;
	}

	public void setPositioToFieldMap(Map<Integer, FieldType> positioToFieldMap)
	{
		this.positioToFieldMap = positioToFieldMap;
	}

	public Map<Integer, FieldType> getPositioToFieldMap()
	{
		return positioToFieldMap;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSelectedFile(String selectedFile)
	{
		this.selectedFile = selectedFile;
	}

	public String getSelectedFile()
	{
		return selectedFile;
	}
	
	public List<String> tokenizeFieldNames()
	{
		String fileName = selectedFile;
		if(fileName.contains(ImgFormatConstants.EXTENSION_SEPARATOR))
			fileName = fileName.substring(0, fileName.lastIndexOf(ImgFormatConstants.EXTENSION_SEPARATOR));
		
		String[] fields = fileName.split(separator);
		
		return Arrays.asList(fields);
	}
	
	public List<RecordMetaData> getSelectedRecords()
	{
		return selectedRecords;
	}
	
	public void setSelectedRecords(List<RecordMetaData> selectedRecords)
	{
		this.selectedRecords = selectedRecords;
	}

	public void setParent(WizardDialog importerWizard)
	{
		this.parent = importerWizard;
	}
	
	public JDialog getParent()
	{
		return this.parent;
	}
	
	public void setImportFilter(ImportFilter importFilter)
	{
		this.importFilter = importFilter;
	}

	public ImportFilter getImportFilter()
	{
		return this.importFilter;
	}

	public void setRecords(Collection<RecordMetaData> records)
	{
		this.records = records;
	}
	
	public Collection<RecordMetaData> getRecords()
	{
		return this.records;
	}

	public boolean doValidation()
	{
		return this.doValidation;
	}
	
	public void setValidation(boolean doValidation)
	{
		this.doValidation = doValidation;
	}
	
	public void setMultiImageDimenstion(FieldType dim)
	{
		this.multiImageDimension = dim;
	}

	public FieldType getMultiImageDimension()
	{
		return this.multiImageDimension;
	}
}
