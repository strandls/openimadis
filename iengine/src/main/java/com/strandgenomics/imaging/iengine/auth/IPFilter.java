/*
 * IPFilter.java
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

import java.net.InetAddress;

/**
 * Filter for ip-addresses. Decides whether a particular ip address is allowed
 * for access or not. The implementations can be based on list of ip-addresses,
 * ip-address with mask, domain name pattern, etc.
 * 
 * @author santhosh
 */
public interface IPFilter {

    /**
     * Check whether an ip address is allowed access or not
     * 
     * @param address
     * @return <code>true</code> if access is allowed. <code>false</code>
     *         otherwise.
     */
    public boolean isAllowed(InetAddress address);

}
