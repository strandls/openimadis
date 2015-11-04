package com.strandgenomics.imaging.iacquisition;

import java.util.List;

import com.strandgenomics.imaging.iclient.local.RawExperiment;

/**
 * Event handler for upload directly to the server
 * 
 * @author Anup Kulkarni
 */
public interface DirectUploadListener {

	public void experimentsAdded(List<RawExperiment> experimentList);
}
