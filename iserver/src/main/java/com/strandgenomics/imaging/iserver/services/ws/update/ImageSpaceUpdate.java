/**
 * ImageSpaceUpdate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.update;

public interface ImageSpaceUpdate extends java.rmi.Remote {
    public java.lang.String getAttachmentUploadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException;
    public void deleteAttachment(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException;
    public void updateAttachmentNotes(java.lang.String in0, long in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public void updateRecordUserAnnotation(java.lang.String in0, long in1, java.lang.String in2, java.lang.Object in3) throws java.rmi.RemoteException;
    public void deleteVisualOverlays(java.lang.String in0, long in1, int in2, java.lang.String in3) throws java.rmi.RemoteException;
    public void deleteVisualObjects(java.lang.String in0, long in1, int[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[] in4) throws java.rmi.RemoteException;
    public void deleteTextObjects(java.lang.String in0, long in1, int[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[] in4) throws java.rmi.RemoteException;
}
