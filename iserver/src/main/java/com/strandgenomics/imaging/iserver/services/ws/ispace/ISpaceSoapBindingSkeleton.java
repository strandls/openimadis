/**
 * ISpaceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class ISpaceSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.ispace.ImageSpaceService, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.ispace.ImageSpaceService impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in7"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTileImageDownloadURL", _params, new javax.xml.namespace.QName("", "getTileImageDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getTileImageDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTileImageDownloadURL") == null) {
            _myOperations.put("getTileImageDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTileImageDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getArchivedProjects", _params, new javax.xml.namespace.QName("", "getArchivedProjectsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getArchivedProjects"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getArchivedProjects") == null) {
            _myOperations.put("getArchivedProjects", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getArchivedProjects")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getActiveProjects", _params, new javax.xml.namespace.QName("", "getActiveProjectsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getActiveProjects"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getActiveProjects") == null) {
            _myOperations.put("getActiveProjects", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getActiveProjects")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listArchives", _params, new javax.xml.namespace.QName("", "listArchivesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "listArchives"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listArchives") == null) {
            _myOperations.put("listArchives", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listArchives")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getIntensityDistibution", _params, new javax.xml.namespace.QName("", "getIntensityDistibutionReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "Statistics"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getIntensityDistibution"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getIntensityDistibution") == null) {
            _myOperations.put("getIntensityDistibution", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getIntensityDistibution")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getIntensityDistibutionForTile", _params, new javax.xml.namespace.QName("", "getIntensityDistibutionForTileReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "Statistics"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getIntensityDistibutionForTile"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getIntensityDistibutionForTile") == null) {
            _myOperations.put("getIntensityDistibutionForTile", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getIntensityDistibutionForTile")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addUserComment", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "addUserComment"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addUserComment") == null) {
            _myOperations.put("addUserComment", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addUserComment")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getPixelDataForRecord", _params, new javax.xml.namespace.QName("", "getPixelDataForRecordReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "Image"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getPixelDataForRecord"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getPixelDataForRecord") == null) {
            _myOperations.put("getPixelDataForRecord", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getPixelDataForRecord")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOf_xsd_int"), int[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in7"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in8"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in9"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in10"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in11"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in12"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in13"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOf_xsd_int"), int[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getOverlayImageDownloadURL", _params, new javax.xml.namespace.QName("", "getOverlayImageDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getOverlayImageDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getOverlayImageDownloadURL") == null) {
            _myOperations.put("getOverlayImageDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getOverlayImageDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOf_xsd_int"), int[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in7"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in8"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in9"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in10"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in11"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in12"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in13"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in14"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getOverlayImageDownloadURL", _params, new javax.xml.namespace.QName("", "getOverlayImageDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getOverlayImageDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getOverlayImageDownloadURL") == null) {
            _myOperations.put("getOverlayImageDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getOverlayImageDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOf_xsd_int"), int[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in7"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in8"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in9"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in10"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in11"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in12"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getOverlayImageDownloadURL", _params, new javax.xml.namespace.QName("", "getOverlayImageDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getOverlayImageDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getOverlayImageDownloadURL") == null) {
            _myOperations.put("getOverlayImageDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getOverlayImageDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getChannelOverlaidSliceImagesURL", _params, new javax.xml.namespace.QName("", "getChannelOverlaidSliceImagesURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getChannelOverlaidSliceImagesURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getChannelOverlaidSliceImagesURL") == null) {
            _myOperations.put("getChannelOverlaidSliceImagesURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getChannelOverlaidSliceImagesURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createRecordAttachment", _params, new javax.xml.namespace.QName("", "createRecordAttachmentReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "createRecordAttachment"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createRecordAttachment") == null) {
            _myOperations.put("createRecordAttachment", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createRecordAttachment")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAttachmentDownloadURL", _params, new javax.xml.namespace.QName("", "getAttachmentDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getAttachmentDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAttachmentDownloadURL") == null) {
            _myOperations.put("getAttachmentDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAttachmentDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRecordAttachments", _params, new javax.xml.namespace.QName("", "getRecordAttachmentsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfRecordAttachment"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getRecordAttachments"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRecordAttachments") == null) {
            _myOperations.put("getRecordAttachments", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRecordAttachments")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("fetchUserComment", _params, new javax.xml.namespace.QName("", "fetchUserCommentReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfComments"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "fetchUserComment"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("fetchUserComment") == null) {
            _myOperations.put("fetchUserComment", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("fetchUserComment")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getDynamicMetaData", _params, new javax.xml.namespace.QName("", "getDynamicMetaDataReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfProperty"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getDynamicMetaData"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getDynamicMetaData") == null) {
            _myOperations.put("getDynamicMetaData", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getDynamicMetaData")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRecordUserAnnotations", _params, new javax.xml.namespace.QName("", "getRecordUserAnnotationsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfProperty"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getRecordUserAnnotations"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRecordUserAnnotations") == null) {
            _myOperations.put("getRecordUserAnnotations", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRecordUserAnnotations")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOfProperty"), com.strandgenomics.imaging.iserver.services.ws.ispace.Property[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addRecordUserAnnotation", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "addRecordUserAnnotation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addRecordUserAnnotation") == null) {
            _myOperations.put("addRecordUserAnnotation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addRecordUserAnnotation")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getImageMetaData", _params, new javax.xml.namespace.QName("", "getImageMetaDataReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfProperty"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getImageMetaData"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getImageMetaData") == null) {
            _myOperations.put("getImageMetaData", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getImageMetaData")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRecordHistory", _params, new javax.xml.namespace.QName("", "getRecordHistoryReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfHistoryItem"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getRecordHistory"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRecordHistory") == null) {
            _myOperations.put("getRecordHistory", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRecordHistory")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addRecordHistory", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "addRecordHistory"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addRecordHistory") == null) {
            _myOperations.put("addRecordHistory", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addRecordHistory")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getVisualOverlays", _params, new javax.xml.namespace.QName("", "getVisualOverlaysReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfOverlay"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getVisualOverlays"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getVisualOverlays") == null) {
            _myOperations.put("getVisualOverlays", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getVisualOverlays")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getVisualOverlay", _params, new javax.xml.namespace.QName("", "getVisualOverlayReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "Overlay"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getVisualOverlay"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getVisualOverlay") == null) {
            _myOperations.put("getVisualOverlay", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getVisualOverlay")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAvailableVisualOverlays", _params, new javax.xml.namespace.QName("", "getAvailableVisualOverlaysReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getAvailableVisualOverlays"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAvailableVisualOverlays") == null) {
            _myOperations.put("getAvailableVisualOverlays", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAvailableVisualOverlays")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createVisualOverlays", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "createVisualOverlays"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createVisualOverlays") == null) {
            _myOperations.put("createVisualOverlays", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createVisualOverlays")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOfShape"), com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOfVOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addVisualObjects", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "addVisualObjects"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addVisualObjects") == null) {
            _myOperations.put("addVisualObjects", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addVisualObjects")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getVisualObjects", _params, new javax.xml.namespace.QName("", "getVisualObjectsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfShape"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getVisualObjects"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getVisualObjects") == null) {
            _myOperations.put("getVisualObjects", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getVisualObjects")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getEllipticalShapes", _params, new javax.xml.namespace.QName("", "getEllipticalShapesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfEllipticalShape"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getEllipticalShapes"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getEllipticalShapes") == null) {
            _myOperations.put("getEllipticalShapes", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getEllipticalShapes")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getLineSegments", _params, new javax.xml.namespace.QName("", "getLineSegmentsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfStraightLine"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getLineSegments"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getLineSegments") == null) {
            _myOperations.put("getLineSegments", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getLineSegments")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRectangularShapes", _params, new javax.xml.namespace.QName("", "getRectangularShapesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfRectangularShape"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getRectangularShapes"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRectangularShapes") == null) {
            _myOperations.put("getRectangularShapes", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRectangularShapes")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTextBoxes", _params, new javax.xml.namespace.QName("", "getTextBoxesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfTextArea"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getTextBoxes"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTextBoxes") == null) {
            _myOperations.put("getTextBoxes", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTextBoxes")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getFreeHandShapes", _params, new javax.xml.namespace.QName("", "getFreeHandShapesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfFreehandShape"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getFreeHandShapes"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getFreeHandShapes") == null) {
            _myOperations.put("getFreeHandShapes", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getFreeHandShapes")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findOverlayLocation", _params, new javax.xml.namespace.QName("", "findOverlayLocationReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfVOIndex"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findOverlayLocation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findOverlayLocation") == null) {
            _myOperations.put("findOverlayLocation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findOverlayLocation")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "Shape"), com.strandgenomics.imaging.iserver.services.ws.ispace.Shape.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findVisualObjectLocation", _params, new javax.xml.namespace.QName("", "findVisualObjectLocationReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfVOIndex"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findVisualObjectLocation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findVisualObjectLocation") == null) {
            _myOperations.put("findVisualObjectLocation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findVisualObjectLocation")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "VOIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "Area"), com.strandgenomics.imaging.iserver.services.ws.ispace.Area.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findVisualObjects", _params, new javax.xml.namespace.QName("", "findVisualObjectsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfShape"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findVisualObjects"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findVisualObjects") == null) {
            _myOperations.put("findVisualObjects", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findVisualObjects")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listAvailableProfiles", _params, new javax.xml.namespace.QName("", "listAvailableProfilesReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfAcquisitionProfile"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "listAvailableProfiles"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listAvailableProfiles") == null) {
            _myOperations.put("listAvailableProfiles", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listAvailableProfiles")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getMicroscopeName", _params, new javax.xml.namespace.QName("", "getMicroscopeNameReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getMicroscopeName"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getMicroscopeName") == null) {
            _myOperations.put("getMicroscopeName", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getMicroscopeName")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "AcquisitionProfile"), com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setAcquisitionProfile", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "setAcquisitionProfile"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("setAcquisitionProfile") == null) {
            _myOperations.put("setAcquisitionProfile", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setAcquisitionProfile")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("requestAcquisitionLicense", _params, new javax.xml.namespace.QName("", "requestAcquisitionLicenseReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "requestAcquisitionLicense"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("requestAcquisitionLicense") == null) {
            _myOperations.put("requestAcquisitionLicense", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("requestAcquisitionLicense")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("surrenderAcquisitionLicense", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "surrenderAcquisitionLicense"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("surrenderAcquisitionLicense") == null) {
            _myOperations.put("surrenderAcquisitionLicense", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("surrenderAcquisitionLicense")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findProject", _params, new javax.xml.namespace.QName("", "findProjectReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "Project"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findProject"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findProject") == null) {
            _myOperations.put("findProject", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findProject")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listAvailableFormats", _params, new javax.xml.namespace.QName("", "listAvailableFormatsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "listAvailableFormats"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listAvailableFormats") == null) {
            _myOperations.put("listAvailableFormats", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listAvailableFormats")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listGUIDsForArchive", _params, new javax.xml.namespace.QName("", "listGUIDsForArchiveReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_xsd_long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "listGUIDsForArchive"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listGUIDsForArchive") == null) {
            _myOperations.put("listGUIDsForArchive", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listGUIDsForArchive")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findProjectForRecord", _params, new javax.xml.namespace.QName("", "findProjectForRecordReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findProjectForRecord"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findProjectForRecord") == null) {
            _myOperations.put("findProjectForRecord", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findProjectForRecord")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findProjectForArchive", _params, new javax.xml.namespace.QName("", "findProjectForArchiveReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findProjectForArchive"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findProjectForArchive") == null) {
            _myOperations.put("findProjectForArchive", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findProjectForArchive")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "FingerPrint"), com.strandgenomics.imaging.iserver.services.ws.ispace.FingerPrint.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findGUID", _params, new javax.xml.namespace.QName("", "findGUIDReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findGUID"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findGUID") == null) {
            _myOperations.put("findGUID", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findGUID")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ArrayOf_xsd_long"), long[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findRecordForGUIDs", _params, new javax.xml.namespace.QName("", "findRecordForGUIDsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfRecord"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "findRecordForGUIDs"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findRecordForGUIDs") == null) {
            _myOperations.put("findRecordForGUIDs", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findRecordForGUIDs")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getBookmarkRoot", _params, new javax.xml.namespace.QName("", "getBookmarkRootReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getBookmarkRoot"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getBookmarkRoot") == null) {
            _myOperations.put("getBookmarkRoot", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getBookmarkRoot")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getBookmarkSubFolders", _params, new javax.xml.namespace.QName("", "getBookmarkSubFoldersReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getBookmarkSubFolders"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getBookmarkSubFolders") == null) {
            _myOperations.put("getBookmarkSubFolders", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getBookmarkSubFolders")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getBookmarkGuids", _params, new javax.xml.namespace.QName("", "getBookmarkGuidsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOf_soapenc_long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getBookmarkGuids"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getBookmarkGuids") == null) {
            _myOperations.put("getBookmarkGuids", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getBookmarkGuids")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createBookmarkFolder", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "createBookmarkFolder"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createBookmarkFolder") == null) {
            _myOperations.put("createBookmarkFolder", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createBookmarkFolder")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addBookmark", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "addBookmark"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addBookmark") == null) {
            _myOperations.put("addBookmark", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addBookmark")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "MosaicRequest"), com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getMosaicResource", _params, new javax.xml.namespace.QName("", "getMosaicResourceReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "MosaicResource"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getMosaicResource"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getMosaicResource") == null) {
            _myOperations.put("getMosaicResource", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getMosaicResource")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "MosaicResource"), com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "MosaicParameters"), com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicParameters.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getMosaicElementDownloadUrl", _params, new javax.xml.namespace.QName("", "getMosaicElementDownloadUrlReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getMosaicElementDownloadUrl"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getMosaicElementDownloadUrl") == null) {
            _myOperations.put("getMosaicElementDownloadUrl", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getMosaicElementDownloadUrl")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "Contrast"), com.strandgenomics.imaging.iserver.services.ws.ispace.Contrast.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setChannelColorAndContrast", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "setChannelColorAndContrast"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("setChannelColorAndContrast") == null) {
            _myOperations.put("setChannelColorAndContrast", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setChannelColorAndContrast")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRecordChannels", _params, new javax.xml.namespace.QName("", "getRecordChannelsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfChannel"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getRecordChannels"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRecordChannels") == null) {
            _myOperations.put("getRecordChannels", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRecordChannels")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRecordSite", _params, new javax.xml.namespace.QName("", "getRecordSiteReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:ispace", "ArrayOfRecordSite"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getRecordSite"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRecordSite") == null) {
            _myOperations.put("getRecordSite", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRecordSite")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getThumbnailUploadURL", _params, new javax.xml.namespace.QName("", "getThumbnailUploadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getThumbnailUploadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getThumbnailUploadURL") == null) {
            _myOperations.put("getThumbnailUploadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getThumbnailUploadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getThumbnailDownloadURL", _params, new javax.xml.namespace.QName("", "getThumbnailDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getThumbnailDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getThumbnailDownloadURL") == null) {
            _myOperations.put("getThumbnailDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getThumbnailDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRawIntensitiesDownloadURL", _params, new javax.xml.namespace.QName("", "getRawIntensitiesDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getRawIntensitiesDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRawIntensitiesDownloadURL") == null) {
            _myOperations.put("getRawIntensitiesDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRawIntensitiesDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getImageDownloadURL", _params, new javax.xml.namespace.QName("", "getImageDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getImageDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getImageDownloadURL") == null) {
            _myOperations.put("getImageDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getImageDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:ispace", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTileIntensitiesDownloadURL", _params, new javax.xml.namespace.QName("", "getTileIntensitiesDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:ispace", "getTileIntensitiesDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTileIntensitiesDownloadURL") == null) {
            _myOperations.put("getTileIntensitiesDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTileIntensitiesDownloadURL")).add(_oper);
    }

    public ISpaceSoapBindingSkeleton() {
    	//this.impl = new com.strandgenomics.imaging.iserver.services.ws.ispace.ISpaceSoapBindingImpl();
        //this is the only change that needs to be made
    	this.impl = new com.strandgenomics.imaging.iserver.services.impl.ImageSpaceImpl();
    }

    public ISpaceSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.ispace.ImageSpaceService impl) {
        this.impl = impl;
    }
    public java.lang.String getTileImageDownloadURL(java.lang.String in0, long in1, boolean in2, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in3, int in4, int in5, int in6, int in7) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getTileImageDownloadURL(in0, in1, in2, in3, in4, in5, in6, in7);
        return ret;
    }

    public java.lang.String[] getArchivedProjects(java.lang.String in0) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.getArchivedProjects(in0);
        return ret;
    }

    public java.lang.String[] getActiveProjects(java.lang.String in0) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.getActiveProjects(in0);
        return ret;
    }

    public java.lang.String[] listArchives(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.listArchives(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics getIntensityDistibution(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics ret = impl.getIntensityDistibution(in0, in1, in2);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics getIntensityDistibutionForTile(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2, int in3, int in4, int in5, int in6) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics ret = impl.getIntensityDistibutionForTile(in0, in1, in2, in3, in4, in5, in6);
        return ret;
    }

    public void addUserComment(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        impl.addUserComment(in0, in1, in2);
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Image getPixelDataForRecord(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Image ret = impl.getPixelDataForRecord(in0, in1, in2);
        return ret;
    }

    public java.lang.String getOverlayImageDownloadURL(java.lang.String in0, long in1, int in2, int in3, int in4, int[] in5, boolean in6, boolean in7, boolean in8, int in9, int in10, int in11, int in12, int[] in13) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getOverlayImageDownloadURL(in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13);
        return ret;
    }

    public java.lang.String getOverlayImageDownloadURL(java.lang.String in0, long in1, int in2, int in3, int in4, int[] in5, boolean in6, boolean in7, boolean in8, int in9, int in10, int in11, int in12, int in13, int in14) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getOverlayImageDownloadURL(in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12, in13, in14);
        return ret;
    }

    public java.lang.String getOverlayImageDownloadURL(java.lang.String in0, long in1, int in2, int in3, int in4, int[] in5, boolean in6, boolean in7, boolean in8, int in9, int in10, int in11, int in12) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getOverlayImageDownloadURL(in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11, in12);
        return ret;
    }

    public java.lang.String getChannelOverlaidSliceImagesURL(java.lang.String in0, long in1, int in2, int in3, int in4, boolean in5) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getChannelOverlaidSliceImagesURL(in0, in1, in2, in3, in4, in5);
        return ret;
    }

    public java.lang.String createRecordAttachment(java.lang.String in0, long in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.createRecordAttachment(in0, in1, in2, in3);
        return ret;
    }

    public java.lang.String getAttachmentDownloadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getAttachmentDownloadURL(in0, in1, in2);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.RecordAttachment[] getRecordAttachments(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.RecordAttachment[] ret = impl.getRecordAttachments(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Comments[] fetchUserComment(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Comments[] ret = impl.fetchUserComment(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] getDynamicMetaData(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] ret = impl.getDynamicMetaData(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] getRecordUserAnnotations(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] ret = impl.getRecordUserAnnotations(in0, in1);
        return ret;
    }

    public void addRecordUserAnnotation(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] in2) throws java.rmi.RemoteException
    {
        impl.addRecordUserAnnotation(in0, in1, in2);
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] getImageMetaData(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] ret = impl.getImageMetaData(in0, in1, in2);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.HistoryItem[] getRecordHistory(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.HistoryItem[] ret = impl.getRecordHistory(in0, in1);
        return ret;
    }

    public void addRecordHistory(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        impl.addRecordHistory(in0, in1, in2);
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay[] getVisualOverlays(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay[] ret = impl.getVisualOverlays(in0, in1, in2);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay getVisualOverlay(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay ret = impl.getVisualOverlay(in0, in1, in2, in3);
        return ret;
    }

    public java.lang.String[] getAvailableVisualOverlays(java.lang.String in0, long in1, int in2) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.getAvailableVisualOverlays(in0, in1, in2);
        return ret;
    }

    public void createVisualOverlays(java.lang.String in0, long in1, int in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        impl.createVisualOverlays(in0, in1, in2, in3);
    }

    public void addVisualObjects(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] in4) throws java.rmi.RemoteException
    {
        impl.addVisualObjects(in0, in1, in2, in3, in4);
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] getVisualObjects(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] ret = impl.getVisualObjects(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.EllipticalShape[] getEllipticalShapes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.EllipticalShape[] ret = impl.getEllipticalShapes(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.StraightLine[] getLineSegments(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.StraightLine[] ret = impl.getLineSegments(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.RectangularShape[] getRectangularShapes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.RectangularShape[] ret = impl.getRectangularShapes(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.TextArea[] getTextBoxes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.TextArea[] ret = impl.getTextBoxes(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.FreehandShape[] getFreeHandShapes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.FreehandShape[] ret = impl.getFreeHandShapes(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] findOverlayLocation(java.lang.String in0, long in1, int in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] ret = impl.findOverlayLocation(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] findVisualObjectLocation(java.lang.String in0, long in1, int in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.ispace.Shape in4) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] ret = impl.findVisualObjectLocation(in0, in1, in2, in3, in4);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] findVisualObjects(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.ispace.Area in4) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] ret = impl.findVisualObjects(in0, in1, in2, in3, in4);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile[] listAvailableProfiles(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile[] ret = impl.listAvailableProfiles(in0);
        return ret;
    }

    public java.lang.String getMicroscopeName(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getMicroscopeName(in0, in1, in2);
        return ret;
    }

    public void setAcquisitionProfile(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile in2) throws java.rmi.RemoteException
    {
        impl.setAcquisitionProfile(in0, in1, in2);
    }

    public boolean requestAcquisitionLicense(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        boolean ret = impl.requestAcquisitionLicense(in0, in1, in2);
        return ret;
    }

    public void surrenderAcquisitionLicense(java.lang.String in0) throws java.rmi.RemoteException
    {
        impl.surrenderAcquisitionLicense(in0);
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Project findProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Project ret = impl.findProject(in0, in1);
        return ret;
    }

    public java.lang.String[] listAvailableFormats(java.lang.String in0) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.listAvailableFormats(in0);
        return ret;
    }

    public long[] listGUIDsForArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        long[] ret = impl.listGUIDsForArchive(in0, in1);
        return ret;
    }

    public java.lang.String findProjectForRecord(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.findProjectForRecord(in0, in1);
        return ret;
    }

    public java.lang.String[] findProjectForArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.findProjectForArchive(in0, in1);
        return ret;
    }

    public long findGUID(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.ispace.FingerPrint in1) throws java.rmi.RemoteException
    {
        long ret = impl.findGUID(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Record[] findRecordForGUIDs(java.lang.String in0, long[] in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Record[] ret = impl.findRecordForGUIDs(in0, in1);
        return ret;
    }

    public java.lang.String getBookmarkRoot(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getBookmarkRoot(in0, in1);
        return ret;
    }

    public java.lang.String[] getBookmarkSubFolders(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.getBookmarkSubFolders(in0, in1, in2);
        return ret;
    }

    public java.lang.Long[] getBookmarkGuids(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        java.lang.Long[] ret = impl.getBookmarkGuids(in0, in1, in2);
        return ret;
    }

    public void createBookmarkFolder(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        impl.createBookmarkFolder(in0, in1, in2, in3);
    }

    public void addBookmark(java.lang.String in0, java.lang.String in1, java.lang.String in2, long in3) throws java.rmi.RemoteException
    {
        impl.addBookmark(in0, in1, in2, in3);
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource getMosaicResource(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicRequest in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource ret = impl.getMosaicResource(in0, in1);
        return ret;
    }

    public java.lang.String getMosaicElementDownloadUrl(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource in1, com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicParameters in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getMosaicElementDownloadUrl(in0, in1, in2);
        return ret;
    }

    public void setChannelColorAndContrast(java.lang.String in0, long in1, int in2, com.strandgenomics.imaging.iserver.services.ws.ispace.Contrast in3, java.lang.String in4) throws java.rmi.RemoteException
    {
        impl.setChannelColorAndContrast(in0, in1, in2, in3, in4);
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Channel[] getRecordChannels(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.Channel[] ret = impl.getRecordChannels(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.RecordSite[] getRecordSite(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.ispace.RecordSite[] ret = impl.getRecordSite(in0, in1);
        return ret;
    }

    public java.lang.String getThumbnailUploadURL(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getThumbnailUploadURL(in0, in1);
        return ret;
    }

    public java.lang.String getThumbnailDownloadURL(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getThumbnailDownloadURL(in0, in1);
        return ret;
    }

    public java.lang.String getRawIntensitiesDownloadURL(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getRawIntensitiesDownloadURL(in0, in1, in2);
        return ret;
    }

    public java.lang.String getImageDownloadURL(java.lang.String in0, long in1, boolean in2, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in3) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getImageDownloadURL(in0, in1, in2, in3);
        return ret;
    }

    public java.lang.String getTileIntensitiesDownloadURL(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2, int in3, int in4, int in5, int in6) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getTileIntensitiesDownloadURL(in0, in1, in2, in3, in4, in5, in6);
        return ret;
    }

}
