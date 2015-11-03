/**
 * ImageSpaceCompute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.compute;

public interface ImageSpaceCompute extends java.rmi.Remote {
    public com.strandgenomics.imaging.iclient.impl.ws.compute.Application[] listApplications(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public void setTaskProgress(java.lang.String in0, long in1, int in2) throws java.rmi.RemoteException;
    public int getTaskProgress(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public java.lang.String getTaskLogUploadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException;
    public long scheduleApplication(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, com.strandgenomics.imaging.iclient.impl.ws.compute.NVPair[] in4, long[] in5, int in6, long in7) throws java.rmi.RemoteException;
    public long executeApplication(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, com.strandgenomics.imaging.iclient.impl.ws.compute.NVPair[] in4, long[] in5, int in6) throws java.rmi.RemoteException;
    public long[] getTaskOutputs(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.Parameter[] getApplicationParameters(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.Publisher[] listPublishers(java.lang.String in0) throws java.rmi.RemoteException;
    public boolean rescheduleTask(java.lang.String in0, long in1, long in2) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.NVPair[] getTaskParameters(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.LongListConstraints testMethod3(java.lang.String in0) throws java.rmi.RemoteException;
    public long[] getTaskInputs(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.Publisher[] getPublisher(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public boolean pauseTask(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public boolean removeTask(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.DoubleRangeConstraints testMethod2(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.DoubleListConstraints testMethod1(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.compute.LongRangeConstraints testMethod4(java.lang.String in0) throws java.rmi.RemoteException;
    public boolean terminateTask(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public java.lang.String getJobState(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public boolean resumeTask(java.lang.String in0, long in1) throws java.rmi.RemoteException;
}
