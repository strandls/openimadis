package com.strandgenomics.imaging.iengine.models;

/**
 * time unit used in elapsed time, exposure time etc
 * 
 * @author Anup Kulkarni
 */
public enum TimeUnit {
	SECONDS{
		@Override
		public String toString()
		{
			return "Seconds";
		}
	}, MILISECONDS{
		@Override
		public String toString()
		{
			return "Milliseconds";
		}
	}, MICROSECONDS{
		@Override
		public String toString()
		{
			return "Microseconds";
		}
	}, NANOSECONDS{
		@Override
		public String toString()
		{
			return "Nanoseconds";
		}
	}
}
