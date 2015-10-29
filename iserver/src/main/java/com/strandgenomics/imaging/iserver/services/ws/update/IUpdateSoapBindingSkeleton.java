/**
 * IUpdateSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.update;

public class IUpdateSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdate, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdate impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAttachmentUploadURL", _params, new javax.xml.namespace.QName("", "getAttachmentUploadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iupdate", "getAttachmentUploadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAttachmentUploadURL") == null) {
            _myOperations.put("getAttachmentUploadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAttachmentUploadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteAttachment", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iupdate", "deleteAttachment"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteAttachment") == null) {
            _myOperations.put("deleteAttachment", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteAttachment")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("updateAttachmentNotes", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iupdate", "updateAttachmentNotes"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("updateAttachmentNotes") == null) {
            _myOperations.put("updateAttachmentNotes", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("updateAttachmentNotes")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"), java.lang.Object.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("updateRecordUserAnnotation", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iupdate", "updateRecordUserAnnotation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("updateRecordUserAnnotation") == null) {
            _myOperations.put("updateRecordUserAnnotation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("updateRecordUserAnnotation")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteVisualOverlays", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iupdate", "deleteVisualOverlays"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteVisualOverlays") == null) {
            _myOperations.put("deleteVisualOverlays", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteVisualOverlays")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iupdate", "ArrayOf_xsd_int"), int[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iupdate", "ArrayOfVOIndex"), com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteVisualObjects", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iupdate", "deleteVisualObjects"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteVisualObjects") == null) {
            _myOperations.put("deleteVisualObjects", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteVisualObjects")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iupdate", "ArrayOf_xsd_int"), int[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iupdate", "ArrayOfVOIndex"), com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteTextObjects", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iupdate", "deleteTextObjects"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteTextObjects") == null) {
            _myOperations.put("deleteTextObjects", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteTextObjects")).add(_oper);
    }

    public IUpdateSoapBindingSkeleton() 
    {
       // this.impl = new com.strandgenomics.imaging.iserver.services.ws.update.IUpdateSoapBindingImpl();
        this.impl = new com.strandgenomics.imaging.iserver.services.impl.ImageSpaceUpdateImpl();
    }

    public IUpdateSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdate impl) {
        this.impl = impl;
    }
    public java.lang.String getAttachmentUploadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getAttachmentUploadURL(in0, in1, in2);
        return ret;
    }

    public void deleteAttachment(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        impl.deleteAttachment(in0, in1, in2);
    }

    public void updateAttachmentNotes(java.lang.String in0, long in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        impl.updateAttachmentNotes(in0, in1, in2, in3);
    }

    public void updateRecordUserAnnotation(java.lang.String in0, long in1, java.lang.String in2, java.lang.Object in3) throws java.rmi.RemoteException
    {
        impl.updateRecordUserAnnotation(in0, in1, in2, in3);
    }

    public void deleteVisualOverlays(java.lang.String in0, long in1, int in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        impl.deleteVisualOverlays(in0, in1, in2, in3);
    }

    public void deleteVisualObjects(java.lang.String in0, long in1, int[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[] in4) throws java.rmi.RemoteException
    {
        impl.deleteVisualObjects(in0, in1, in2, in3, in4);
    }

    public void deleteTextObjects(java.lang.String in0, long in1, int[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[] in4) throws java.rmi.RemoteException
    {
        impl.deleteTextObjects(in0, in1, in2, in3, in4);
    }

}
