/**
 * ImageSpaceSearch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.search;

public interface ImageSpaceSearch extends java.rmi.Remote {
    public long[] search(java.lang.String in0, java.lang.String in1, java.lang.String[] in2, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[] in3, int in4) throws java.rmi.RemoteException;
    public long[] findRecords(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[] in2) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] getNavigableFields(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] getAvailableUserAnnotations(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.search.SearchNode[] getNavigationBins(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[] in2, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition in3) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] getAvailableDynamicMetaData(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
}
