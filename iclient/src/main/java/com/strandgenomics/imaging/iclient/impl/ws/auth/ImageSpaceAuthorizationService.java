/**
 * ImageSpaceAuthorizationService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.auth;

public interface ImageSpaceAuthorizationService extends javax.xml.rpc.Service {
    public java.lang.String getiAuthAddress();

    public com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorization getiAuth() throws javax.xml.rpc.ServiceException;

    public com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorization getiAuth(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
