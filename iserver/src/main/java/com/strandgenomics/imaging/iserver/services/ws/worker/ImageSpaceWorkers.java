/**
 * ImageSpaceWorkers.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.worker;

public interface ImageSpaceWorkers extends java.rmi.Remote {
    public java.lang.String register(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.Directive removeApplications(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.worker.Application[] in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.Directive publishApplications(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.worker.Application[] in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.DoubleListConstraints testMethod1(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.LongListConstraints testMethod3(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.DoubleRangeConstraints testMethod2(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.LongRangeConstraints testMethod4(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.StringListConstraints testMethod0(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.worker.Response ping(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.worker.Request in1) throws java.rmi.RemoteException;
}
