/*
 * ExtractionService.java
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
package com.strandgenomics.imaging.iengine.system;

import java.io.IOException;
import java.rmi.Remote;

/**
 * provider or the service to extract records out of a tar-ball of record source files
 * @author arunabha
 *
 */
public interface ExtractionService extends Remote  {
	
	/**
	 * Submit the specified task to the underlying extraction service
	 * @param ticketNo the ticket number
	 * @param request the request
	 */
	 public boolean submitTask(long ticketNo, RecordCreationRequest request) 
			 throws IOException;
}
