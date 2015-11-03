/**
 * ImageSpaceLoader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.loader;

public interface ImageSpaceLoader extends java.rmi.Remote {
    public java.lang.String getArchiveDownloadURL(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.Long registerRecordBuilder(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.loader.RecordBuilderObject in1) throws java.rmi.RemoteException;
    public void commitRecordCreation(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.loader.UploadTicket recordCreationRequest(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iclient.impl.ws.loader.CreationRequest in2) throws java.rmi.RemoteException;
    public java.lang.String getRecordDownloadURL(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.loader.UploadTicket directUploadCreationRequest(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iclient.impl.ws.loader.CreationRequest in2) throws java.rmi.RemoteException;
    public void abortRecordCreation(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public java.lang.String getTicketStatus(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.loader.Archive findArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String addImageData(java.lang.String in0, long in1, com.strandgenomics.imaging.iclient.impl.ws.loader.ImageIndex in2, com.strandgenomics.imaging.iclient.impl.ws.loader.Image in3) throws java.rmi.RemoteException;
}
