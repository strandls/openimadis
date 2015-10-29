/*
 * Property.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.ispace;

/**
 * A name value stuff
 * @author arunabha
 *
 */
public class Property {
	
    /**
     * a property is defined by its name
     */
    protected String name = null;
    /**
     * a property will have a non null value (one of Java String, Long, and Double)
     */
	private Object value = null;
	
	public Property()
	{}
	
    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        name = value;
    }
	
    public Object getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
