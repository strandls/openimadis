package com.strandgenomics.imaging.icompute;

/**
 * type of compute worker
 * 
 * @author Anup Kulkarni
 */
public enum ComputeWorkerType {

	/**
	 * for internal compute workers
	 */
	INTERNAL,
	/**
	 * for external compute workers eg. Torque/PBS etc
	 */
	EXTERNAL
}
