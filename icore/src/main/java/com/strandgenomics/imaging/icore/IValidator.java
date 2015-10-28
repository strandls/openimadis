package com.strandgenomics.imaging.icore;


public interface IValidator {

	boolean isDuplicate(IExperiment experiment);
	
	/**
	 * sets the upload status of experiment to right status
	 * 
	 * @param experiment specified experiment
	 * @param duplicate true if already present on server
	 */
	void setUploaded(IExperiment experiment, boolean duplicate);

	/**
	 * checks if the experiment is already uploaded on server
	 * @param experiment specified experiment
	 * @return true if already present on server
	 */
	boolean isUploaded(IExperiment experiment);
}
