package com.strandgenomics.imaging.iengine.models;

/**
 * profile can be either a value, factor or correction. value will be taken as
 * is, correction will be added to existing value and factor will be multiplied
 * with exisiting value
 * 
 * null applied on factor or correction will yeild null.
 * 
 * @author Anup Kulkarni
 */
public enum AcquisitionProfileType {
	VALUE,
	FACTOR
}
