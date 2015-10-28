/*
 * AuthorizationException.java
 *
 * Product:  AvadisIMG Server
 *
 * Copyright 2007-2012, Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal,
 * Bangalore 560024
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iengine.auth;

import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;

/**
 * Exception to indicate particular type of failure during authorization. For
 * example, to communicate an access token has expired.
 * 
 * @author santhosh
 */
public class AuthorizationException extends ImagingEngineException {

	private static final long serialVersionUID = -6287645676561373159L;

    /**
     * Constructor with errorcode
     * 
     * @param errorCode
     *            errorcode indicating a particular type of failure.
     */
    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
