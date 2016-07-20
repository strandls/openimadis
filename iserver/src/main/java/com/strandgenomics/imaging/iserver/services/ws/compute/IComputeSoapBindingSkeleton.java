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
 * IComputeSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.compute;

public class IComputeSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.compute.ImageSpaceCompute, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.compute.ImageSpaceCompute impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listApplications", _params, new javax.xml.namespace.QName("", "listApplicationsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "ArrayOfApplication"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "listApplications"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listApplications") == null) {
            _myOperations.put("listApplications", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listApplications")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setTaskProgress", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "setTaskProgress"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("setTaskProgress") == null) {
            _myOperations.put("setTaskProgress", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setTaskProgress")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTaskProgress", _params, new javax.xml.namespace.QName("", "getTaskProgressReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getTaskProgress"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTaskProgress") == null) {
            _myOperations.put("getTaskProgress", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTaskProgress")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTaskLogUploadURL", _params, new javax.xml.namespace.QName("", "getTaskLogUploadURLReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getTaskLogUploadURL"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTaskLogUploadURL") == null) {
            _myOperations.put("getTaskLogUploadURL", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTaskLogUploadURL")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:icompute", "ArrayOfNVPair"), com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:icompute", "ArrayOf_xsd_long"), long[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in7"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("scheduleApplication", _params, new javax.xml.namespace.QName("", "scheduleApplicationReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "scheduleApplication"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("scheduleApplication") == null) {
            _myOperations.put("scheduleApplication", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("scheduleApplication")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:icompute", "ArrayOfNVPair"), com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:icompute", "ArrayOf_xsd_long"), long[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in6"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("executeApplication", _params, new javax.xml.namespace.QName("", "executeApplicationReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "executeApplication"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("executeApplication") == null) {
            _myOperations.put("executeApplication", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("executeApplication")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTaskOutputs", _params, new javax.xml.namespace.QName("", "getTaskOutputsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "ArrayOf_xsd_long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getTaskOutputs"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTaskOutputs") == null) {
            _myOperations.put("getTaskOutputs", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTaskOutputs")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getApplicationParameters", _params, new javax.xml.namespace.QName("", "getApplicationParametersReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "ArrayOfParameter"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getApplicationParameters"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getApplicationParameters") == null) {
            _myOperations.put("getApplicationParameters", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getApplicationParameters")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listPublishers", _params, new javax.xml.namespace.QName("", "listPublishersReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "ArrayOfPublisher"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "listPublishers"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listPublishers") == null) {
            _myOperations.put("listPublishers", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listPublishers")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("rescheduleTask", _params, new javax.xml.namespace.QName("", "rescheduleTaskReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "rescheduleTask"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("rescheduleTask") == null) {
            _myOperations.put("rescheduleTask", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("rescheduleTask")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTaskParameters", _params, new javax.xml.namespace.QName("", "getTaskParametersReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "ArrayOfNVPair"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getTaskParameters"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTaskParameters") == null) {
            _myOperations.put("getTaskParameters", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTaskParameters")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod3", _params, new javax.xml.namespace.QName("", "testMethod3Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "LongListConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "testMethod3"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod3") == null) {
            _myOperations.put("testMethod3", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod3")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTaskInputs", _params, new javax.xml.namespace.QName("", "getTaskInputsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "ArrayOf_xsd_long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getTaskInputs"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTaskInputs") == null) {
            _myOperations.put("getTaskInputs", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTaskInputs")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getPublisher", _params, new javax.xml.namespace.QName("", "getPublisherReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "ArrayOfPublisher"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getPublisher"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getPublisher") == null) {
            _myOperations.put("getPublisher", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getPublisher")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("pauseTask", _params, new javax.xml.namespace.QName("", "pauseTaskReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "pauseTask"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("pauseTask") == null) {
            _myOperations.put("pauseTask", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("pauseTask")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("removeTask", _params, new javax.xml.namespace.QName("", "removeTaskReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "removeTask"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("removeTask") == null) {
            _myOperations.put("removeTask", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("removeTask")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod2", _params, new javax.xml.namespace.QName("", "testMethod2Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "DoubleRangeConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "testMethod2"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod2") == null) {
            _myOperations.put("testMethod2", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod2")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod1", _params, new javax.xml.namespace.QName("", "testMethod1Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "DoubleListConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "testMethod1"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod1") == null) {
            _myOperations.put("testMethod1", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod1")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod4", _params, new javax.xml.namespace.QName("", "testMethod4Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:icompute", "LongRangeConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "testMethod4"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod4") == null) {
            _myOperations.put("testMethod4", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod4")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("terminateTask", _params, new javax.xml.namespace.QName("", "terminateTaskReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "terminateTask"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("terminateTask") == null) {
            _myOperations.put("terminateTask", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("terminateTask")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getJobState", _params, new javax.xml.namespace.QName("", "getJobStateReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "getJobState"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getJobState") == null) {
            _myOperations.put("getJobState", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getJobState")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("resumeTask", _params, new javax.xml.namespace.QName("", "resumeTaskReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:icompute", "resumeTask"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("resumeTask") == null) {
            _myOperations.put("resumeTask", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("resumeTask")).add(_oper);
    }

    public IComputeSoapBindingSkeleton() {
        this.impl = new com.strandgenomics.imaging.iserver.services.ws.compute.IComputeSoapBindingImpl();
    }

    public IComputeSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.compute.ImageSpaceCompute impl) {
        this.impl = impl;
    }
    public com.strandgenomics.imaging.iserver.services.ws.compute.Application[] listApplications(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.Application[] ret = impl.listApplications(in0, in1, in2);
        return ret;
    }

    public void setTaskProgress(java.lang.String in0, long in1, int in2) throws java.rmi.RemoteException
    {
        impl.setTaskProgress(in0, in1, in2);
    }

    public int getTaskProgress(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        int ret = impl.getTaskProgress(in0, in1);
        return ret;
    }

    public java.lang.String getTaskLogUploadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getTaskLogUploadURL(in0, in1, in2);
        return ret;
    }

    public long scheduleApplication(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[] in4, long[] in5, int in6, long in7) throws java.rmi.RemoteException
    {
        long ret = impl.scheduleApplication(in0, in1, in2, in3, in4, in5, in6, in7);
        return ret;
    }

    public long executeApplication(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[] in4, long[] in5, int in6) throws java.rmi.RemoteException
    {
        long ret = impl.executeApplication(in0, in1, in2, in3, in4, in5, in6);
        return ret;
    }

    public long[] getTaskOutputs(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        long[] ret = impl.getTaskOutputs(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.Parameter[] getApplicationParameters(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.Parameter[] ret = impl.getApplicationParameters(in0, in1, in2);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.Publisher[] listPublishers(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.Publisher[] ret = impl.listPublishers(in0);
        return ret;
    }

    public boolean rescheduleTask(java.lang.String in0, long in1, long in2) throws java.rmi.RemoteException
    {
        boolean ret = impl.rescheduleTask(in0, in1, in2);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[] getTaskParameters(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[] ret = impl.getTaskParameters(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.LongListConstraints testMethod3(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.LongListConstraints ret = impl.testMethod3(in0);
        return ret;
    }

    public long[] getTaskInputs(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        long[] ret = impl.getTaskInputs(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.Publisher[] getPublisher(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.Publisher[] ret = impl.getPublisher(in0, in1, in2);
        return ret;
    }

    public boolean pauseTask(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        boolean ret = impl.pauseTask(in0, in1);
        return ret;
    }

    public boolean removeTask(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        boolean ret = impl.removeTask(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.DoubleRangeConstraints testMethod2(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.DoubleRangeConstraints ret = impl.testMethod2(in0);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.DoubleListConstraints testMethod1(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.DoubleListConstraints ret = impl.testMethod1(in0);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.LongRangeConstraints testMethod4(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.compute.LongRangeConstraints ret = impl.testMethod4(in0);
        return ret;
    }

    public boolean terminateTask(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        boolean ret = impl.terminateTask(in0, in1);
        return ret;
    }

    public java.lang.String getJobState(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getJobState(in0, in1);
        return ret;
    }

    public boolean resumeTask(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        boolean ret = impl.resumeTask(in0, in1);
        return ret;
    }

}
