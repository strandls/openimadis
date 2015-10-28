package com.strandgenomics.imaging.icore.bioformats.custom;

/**
 * type definitions for different fields embedded in file name
 * 
 * @author Anup Kulkarni
 */
public enum FieldType {

	/**
	 * skip this field while parsing the file name
	 */
	IGNORE
	{
		@Override
		public String toString()
		{
			return "Ignore";
		}
	}, 
	/**
	 * name of the record
	 */
	RECORD_LABEL
	{
		@Override
		public String toString()
		{
			return "Record Label";
		}
	},
	/**
	 * represents the frame number
	 */
	FRAME 
	{
		@Override
		public String toString()
		{
			return "Frame (T)";
		}
	},
	/**
	 * represents the slice number
	 */
	SLICE 
	{
		@Override
		public String toString()
		{
			return "Slice (Z)";
		}
	}, 
	/**
	 * represents the channel number
	 */
	CHANNEL
	{
		@Override
		public String toString()
		{
			return "Channel (λ)";
		}
	}, 
	/**
	 * represents the site number
	 */
	SITE
	{
		@Override
		public String toString()
		{
			return "Site";
		}
	}
}
