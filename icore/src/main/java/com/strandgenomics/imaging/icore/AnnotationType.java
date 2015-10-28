/*
 * AnnotationType.java
 *
 * AVADIS Image Management System
 * Core Engine
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore;

import java.sql.Types;

/**
 *  used for annotation type to DB table mapping
 */
public enum AnnotationType {
	
	Integer 
	{
		@Override
		public String toString() 
		{
			return "int";
		}
		
		@Override
		public int getSQLType()
		{
			return Types.BIGINT;
		}
	},

	Real 
	{
		@Override
		public String toString() 
		{
			return "real";
		}
		
		@Override
		public int getSQLType()
		{
			return Types.DOUBLE;
		}
	},
	
	Text 
	{
		@Override
		public String toString() 
		{
			return "text";
		}
		
		@Override
		public int getSQLType()
		{
			return Types.VARCHAR;
		}
	},
	
	Time 
	{
		@Override
		public String toString() 
		{
			return "time";
		}
		
		@Override
		public int getSQLType()
		{
			return Types.TIMESTAMP;
		}
	};
	
	public abstract int getSQLType();
}
