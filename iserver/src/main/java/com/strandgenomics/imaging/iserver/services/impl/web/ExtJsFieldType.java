package com.strandgenomics.imaging.iserver.services.impl.web;

/**
 * types supported by extjs
 * 
 * @author Anup Kulkarni
 */
public enum ExtJsFieldType {
	AUTO{
		@Override
		public String toString()
		{
			return "auto";
		}
	},
	INT{
		@Override
		public String toString()
		{
			return "int";
		}
	}, STRING{
		@Override
		public String toString()
		{
			return "string";
		}
	}, FLOAT{
		@Override
		public String toString()
		{
			return "float";
		}
	}, DATE{
		@Override
		public String toString()
		{
			return "date";
		}
	}, BOOLEAN{
		@Override
		public String toString()
		{
			return "boolean";
		}
	}
}
