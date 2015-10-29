/**
 * ImageSpaceSearchService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.search;

public interface ImageSpaceSearchService extends javax.xml.rpc.Service {
    public java.lang.String getiSearchAddress();

    public com.strandgenomics.imaging.iserver.services.ws.search.ImageSpaceSearch getiSearch() throws javax.xml.rpc.ServiceException;

    public com.strandgenomics.imaging.iserver.services.ws.search.ImageSpaceSearch getiSearch(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
