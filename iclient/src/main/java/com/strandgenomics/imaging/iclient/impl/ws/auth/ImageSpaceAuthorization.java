/**
 * ImageSpaceAuthorization.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.auth;

public interface ImageSpaceAuthorization extends java.rmi.Remote {
    public void surrenderAccessToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String getAccessToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String getUser(java.lang.String in0) throws java.rmi.RemoteException;
    public long getExpiryTime(java.lang.String in0) throws java.rmi.RemoteException;
}
