/**
 * ImageSpaceAuthorization.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.auth;

public interface ImageSpaceAuthorization extends java.rmi.Remote {
    public java.lang.String getAccessToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String getUser(java.lang.String in0) throws java.rmi.RemoteException;
    public long getExpiryTime(java.lang.String in0) throws java.rmi.RemoteException;
    public void surrenderAccessToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
}
