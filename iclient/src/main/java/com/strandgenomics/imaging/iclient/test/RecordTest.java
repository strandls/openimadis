/*
 * ProjectTest.java
 *
 * AVADIS Image Management System
 * Web Service Test programs
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
package com.strandgenomics.imaging.iclient.test;

import java.util.Collection;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.IAttachment;

public class RecordTest implements TestData {

    public static void main(String ... args) throws Exception
    {
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"cid.curie.fr", "80", "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI", "lNSDvWfzIV8YkLlIJJrIlfspG4FPBP76GRgxhD4B"};
    	}

    	ImageSpace ispace = ImageSpaceObject.getConnectionManager();
    	String hostIP = args[0];
    	int hostPort = Integer.parseInt(args[1]);
    	String userName = args[2];
    	String password = args[3];
    	
    	ispace.login(false, hostIP, hostPort, userName, password);
    	
    	Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(655);

    	
		
    }
}
