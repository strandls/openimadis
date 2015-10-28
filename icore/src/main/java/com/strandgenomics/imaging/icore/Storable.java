/*
 * Storable.java
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
 
package com.strandgenomics.imaging.icore;

import java.io.Serializable;



/**
 * Any class interested in accessing the data access layer would need to extends this marker interface
 * @author arunabha
 *
 */
public interface Storable extends Disposable, Serializable {

}