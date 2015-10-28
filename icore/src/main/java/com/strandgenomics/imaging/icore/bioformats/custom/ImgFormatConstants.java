package com.strandgenomics.imaging.icore.bioformats.custom;

/**
 * placeholder for defining constants required by ImgFormatParser/Reader/Writer
 * 
 * @author Anup Kulkarni
 */
public class ImgFormatConstants {
	/**
	 * filename-extension separator
	 */
	public static final String EXTENSION_SEPARATOR = ".";
	/**
	 * separator by which filename and dimensions are separated
	 */
	public static final String FILENAME_SEPARATOR = "=";
	/**
	 * separator by which componenets of dimension(eg. slice,frame,channel,site) are separated
	 */
	public static final String DIMENSION_SEPARATOR = ",";
	/**
	 * file extension used by img format meta-source file
	 */
	public static final String IMG_FORMAT_EXTENSION = ".img";
	/**
	 * default record label hence the default name of the meta-source file 
	 */
	public static final String DEFAULT_RECORD_LABEL = "record";
}
