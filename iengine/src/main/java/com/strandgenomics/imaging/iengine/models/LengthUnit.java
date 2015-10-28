package com.strandgenomics.imaging.iengine.models;
/**
 * unit of length used for pixel to physical length mapping
 * 
 * @author Anup Kulkarni
 */
public enum LengthUnit {
	MILIMETER {
		@Override
		public String toString()
		{
			return "Millimeter";
		}
	}, MICROMETER {
		@Override
		public String toString()
		{
			return "Micrometer";
		}
	}, NANOMETER {
		@Override
		public String toString()
		{
			return "Nanometer";
		}
	}
}
