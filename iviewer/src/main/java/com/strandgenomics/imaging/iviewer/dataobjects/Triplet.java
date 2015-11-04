/**
 * Triplet.java
 * 
 * Project imaging
 * Module com.strandgenomics.imaging.framework.dataobjects
 *
 * Copyright 2009-2010 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.


 */
package com.strandgenomics.imaging.iviewer.dataobjects;

/**
 * @author nishu
 * 
 */
public class Triplet {

	public static String TYPE_INT = "integer";
	public static String TYPE_FLOAT = "float";
	public static String TYPE_LONG = "long";
	public static String TYPE_STRING = "string";
	public static String TYPE_OBJECT = "object";
	public static String TYPE_USER_ANNOTATION = "userAnnotation";
	public static String TYPE_UNINTERPRETED_FIELD = "uninterpretedField";

	private String key;
	private String type;
	private Object value;

	/**
	 * @param key
	 * @param value
	 * @param type
	 */
	public Triplet(String key, Object value, String type) {
		super();
		this.key = key;
		this.value = value;
		this.type = type;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the value
	 */
	public int intValue() {
		if (type.equals(Triplet.TYPE_INT)) {
			return (Integer) this.value;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the value
	 */
	public float floatValue() {
		if (type.equals(Triplet.TYPE_FLOAT)) {
			return (Float) this.value;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the value
	 */
	public long longValue() {
		if (type.equals(Triplet.TYPE_LONG)) {
			return (Long) this.value;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the value
	 */
	public String stringValue() {
		if (type.equals(Triplet.TYPE_STRING)
				|| type.equals(Triplet.TYPE_USER_ANNOTATION)
				|| type.equals(Triplet.TYPE_UNINTERPRETED_FIELD)) {
			return (String) this.value;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Triplet [key=" + this.key + ", type=" + this.type + ", value="
				+ this.value + "]";
	}

}
