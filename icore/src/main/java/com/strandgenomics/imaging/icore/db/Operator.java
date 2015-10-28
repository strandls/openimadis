/*
 * Operator.java
 *
 * AVADIS Image Management System
 * Core Engine Database Module
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

package com.strandgenomics.imaging.icore.db;

/**
 * supported operators within SQL queries
 * @author arunabha
 *
 */
public enum Operator 
{
    NOT_EQUALS_TO("!="),
    EQUALS_TO("="),
    LESS_THAN_EQUALS_TO("<="),
    GREATER_THAN_EQUALS_TO(">="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    NONE("");
    
    private String op = null;
    
    private Operator(String op)
    {
    	this.op = op;
    }
    
    public String toString()
    {
    	return op;
    }
}
