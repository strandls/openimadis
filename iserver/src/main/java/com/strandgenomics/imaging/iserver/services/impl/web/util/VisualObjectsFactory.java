/*
 * VisualObjectsFactory.java
 *
 * AVADIS Image Management System
 * Core Engine Components
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

package com.strandgenomics.imaging.iserver.services.impl.web.util;

import com.strandgenomics.imaging.iengine.system.ISSecurityManager;
import com.strandgenomics.imaging.iengine.system.UserManager;


/**
 * Provider of all visual object transformers.
 * @author navneet
 *
 */
public class VisualObjectsFactory {
	/**
	 * used for synchronizing the creation transformer objects
	 */
	private static Object padLock = new Object();
	/**
	 * singleton instance of the kinetic Transformer
	 */
	private static VisualObjectTransformer visualObjectTransformer = null;
	

	/**
	 * Returns the singleton instance of visualObjectTransformer 
	 * @return the singleton instance of visualObjectTransformer 
	 */
	@SuppressWarnings("rawtypes")
	public static VisualObjectTransformer getVisualObjectTransformer(String transformerType)
	{
		if(visualObjectTransformer == null)
		{
			synchronized(padLock)
			{
				if(visualObjectTransformer == null)
				{
					VisualObjectTransformer transformer = null;
					
					if(transformerType.equals("kinetic"))
						transformer= new KineticTransformer();
					if(transformerType.equals("raphael"))
						transformer= new RaphaelTransformer();					

					visualObjectTransformer = transformer;
				}
			}
		}
		return visualObjectTransformer;
	}
}
