/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
