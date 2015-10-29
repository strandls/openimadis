/**
 * ImageSpaceAuthorizationService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.auth;

public interface ImageSpaceAuthorizationService extends javax.xml.rpc.Service {
    public java.lang.String getiAuthAddress();

    public com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization getiAuth() throws javax.xml.rpc.ServiceException;

    public com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization getiAuth(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
