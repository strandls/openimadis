/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * ILoaderSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.loader;

public class ILoaderSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.loader.ImageSpaceLoader, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.loader.ImageSpaceLoader impl;
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
        };
        _oper = new org.apache.axis.description.OperationDesc("getArchiveDownloadURL", _params, new javax.xml.namespace.QName("", "getArchiveDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "getArchiveDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getArchiveDownloadURL") == null) {
            _myOperations.put("getArchiveDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getArchiveDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iloader", "RecordBuilderObject"), com.strandgenomics.imaging.iserver.services.ws.loader.RecordBuilderObject.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("registerRecordBuilder", _params, new javax.xml.namespace.QName("", "registerRecordBuilderReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "registerRecordBuilder"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("registerRecordBuilder") == null) {
            _myOperations.put("registerRecordBuilder", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("registerRecordBuilder")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("commitRecordCreation", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "commitRecordCreation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("commitRecordCreation") == null) {
            _myOperations.put("commitRecordCreation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("commitRecordCreation")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iloader", "CreationRequest"), com.strandgenomics.imaging.iserver.services.ws.loader.CreationRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("recordCreationRequest", _params, new javax.xml.namespace.QName("", "recordCreationRequestReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iloader", "UploadTicket"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "recordCreationRequest"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("recordCreationRequest") == null) {
            _myOperations.put("recordCreationRequest", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("recordCreationRequest")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getRecordDownloadURL", _params, new javax.xml.namespace.QName("", "getRecordDownloadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "getRecordDownloadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getRecordDownloadURL") == null) {
            _myOperations.put("getRecordDownloadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getRecordDownloadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iloader", "CreationRequest"), com.strandgenomics.imaging.iserver.services.ws.loader.CreationRequest.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("directUploadCreationRequest", _params, new javax.xml.namespace.QName("", "directUploadCreationRequestReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iloader", "UploadTicket"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "directUploadCreationRequest"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("directUploadCreationRequest") == null) {
            _myOperations.put("directUploadCreationRequest", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("directUploadCreationRequest")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("abortRecordCreation", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "abortRecordCreation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("abortRecordCreation") == null) {
            _myOperations.put("abortRecordCreation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("abortRecordCreation")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTicketStatus", _params, new javax.xml.namespace.QName("", "getTicketStatusReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "getTicketStatus"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTicketStatus") == null) {
            _myOperations.put("getTicketStatus", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTicketStatus")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("findArchive", _params, new javax.xml.namespace.QName("", "findArchiveReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iloader", "Archive"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "findArchive"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("findArchive") == null) {
            _myOperations.put("findArchive", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("findArchive")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iloader", "ImageIndex"), com.strandgenomics.imaging.iserver.services.ws.loader.ImageIndex.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iloader", "Image"), com.strandgenomics.imaging.iserver.services.ws.loader.Image.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addImageData", _params, new javax.xml.namespace.QName("", "addImageDataReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iloader", "addImageData"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addImageData") == null) {
            _myOperations.put("addImageData", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addImageData")).add(_oper);
    }

    public ILoaderSoapBindingSkeleton() {
        this.impl = new com.strandgenomics.imaging.iserver.services.ws.loader.ILoaderSoapBindingImpl();
    }

    public ILoaderSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.loader.ImageSpaceLoader impl) {
        this.impl = impl;
    }
    public java.lang.String getArchiveDownloadURL(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getArchiveDownloadURL(in0, in1);
        return ret;
    }

    public java.lang.Long registerRecordBuilder(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.loader.RecordBuilderObject in1) throws java.rmi.RemoteException
    {
        java.lang.Long ret = impl.registerRecordBuilder(in0, in1);
        return ret;
    }

    public void commitRecordCreation(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        impl.commitRecordCreation(in0, in1);
    }

    public com.strandgenomics.imaging.iserver.services.ws.loader.UploadTicket recordCreationRequest(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iserver.services.ws.loader.CreationRequest in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.loader.UploadTicket ret = impl.recordCreationRequest(in0, in1, in2);
        return ret;
    }

    public java.lang.String getRecordDownloadURL(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getRecordDownloadURL(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.loader.UploadTicket directUploadCreationRequest(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iserver.services.ws.loader.CreationRequest in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.loader.UploadTicket ret = impl.directUploadCreationRequest(in0, in1, in2);
        return ret;
    }

    public void abortRecordCreation(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        impl.abortRecordCreation(in0, in1);
    }

    public java.lang.String getTicketStatus(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getTicketStatus(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.loader.Archive findArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.loader.Archive ret = impl.findArchive(in0, in1);
        return ret;
    }

    public java.lang.String addImageData(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.loader.ImageIndex in2, com.strandgenomics.imaging.iserver.services.ws.loader.Image in3) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.addImageData(in0, in1, in2, in3);
        return ret;
    }

}
