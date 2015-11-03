/**
 * ImageSpaceWorkers.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.worker;

public interface ImageSpaceWorkers extends java.rmi.Remote {
    public java.lang.String register(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Directive removeApplications(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.worker.Application[] in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Directive publishApplications(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.worker.Application[] in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.DoubleListConstraints testMethod1(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.LongListConstraints testMethod3(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.DoubleRangeConstraints testMethod2(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.LongRangeConstraints testMethod4(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.StringListConstraints testMethod0(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Response ping(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.worker.Request in1) throws java.rmi.RemoteException;
}
