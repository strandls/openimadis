/**
 * ISearchSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.search;

public class ISearchSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.search.ImageSpaceSearch, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.search.ImageSpaceSearch impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:isearch", "ArrayOf_soapenc_string"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:isearch", "ArrayOfSearchCondition"), com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("search", _params, new javax.xml.namespace.QName("", "searchReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:isearch", "ArrayOf_xsd_long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:isearch", "search"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("search") == null) {
            _myOperations.put("search", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("search")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAvailableDynamicMetaData", _params, new javax.xml.namespace.QName("", "getAvailableDynamicMetaDataReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:isearch", "ArrayOfSearchField"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:isearch", "getAvailableDynamicMetaData"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAvailableDynamicMetaData") == null) {
            _myOperations.put("getAvailableDynamicMetaData", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAvailableDynamicMetaData")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAvailableUserAnnotations", _params, new javax.xml.namespace.QName("", "getAvailableUserAnnotationsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:isearch", "ArrayOfSearchField"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:isearch", "getAvailableUserAnnotations"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAvailableUserAnnotations") == null) {
            _myOperations.put("getAvailableUserAnnotations", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAvailableUserAnnotations")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getNavigableFields", _params, new javax.xml.namespace.QName("", "getNavigableFieldsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:isearch", "ArrayOfSearchField"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:isearch", "getNavigableFields"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getNavigableFields") == null) {
            _myOperations.put("getNavigableFields", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getNavigableFields")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:isearch", "ArrayOfSearchCondition"), com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findRecords", _params, new javax.xml.namespace.QName("", "findRecordsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:isearch", "ArrayOf_xsd_long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:isearch", "findRecords"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findRecords") == null) {
            _myOperations.put("findRecords", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findRecords")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:isearch", "ArrayOfSearchCondition"), com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:isearch", "SearchCondition"), com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getNavigationBins", _params, new javax.xml.namespace.QName("", "getNavigationBinsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:isearch", "ArrayOfSearchNode"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:isearch", "getNavigationBins"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getNavigationBins") == null) {
            _myOperations.put("getNavigationBins", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getNavigationBins")).add(_oper);
    }

    public ISearchSoapBindingSkeleton() {
        //this.impl = new com.strandgenomics.imaging.iserver.services.ws.search.ISearchSoapBindingImpl();
        this.impl = new com.strandgenomics.imaging.iserver.services.impl.ImageSpaceSearchImpl();
    }

    public ISearchSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.search.ImageSpaceSearch impl) {
        this.impl = impl;
    }
    public long[] search(java.lang.String in0, java.lang.String in1, java.lang.String[] in2, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[] in3, int in4) throws java.rmi.RemoteException
    {
        long[] ret = impl.search(in0, in1, in2, in3, in4);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] getAvailableDynamicMetaData(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] ret = impl.getAvailableDynamicMetaData(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] getAvailableUserAnnotations(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] ret = impl.getAvailableUserAnnotations(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] getNavigableFields(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.search.SearchField[] ret = impl.getNavigableFields(in0, in1);
        return ret;
    }

    public long[] findRecords(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[] in2) throws java.rmi.RemoteException
    {
        long[] ret = impl.findRecords(in0, in1, in2);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.search.SearchNode[] getNavigationBins(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition[] in2, com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.search.SearchNode[] ret = impl.getNavigationBins(in0, in1, in2, in3);
        return ret;
    }

}
