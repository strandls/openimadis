/**
 * IWorkersSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.worker;

public class IWorkersSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.worker.ImageSpaceWorkers, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.worker.ImageSpaceWorkers impl;
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
        _oper = new org.apache.axis.description.OperationDesc("register", _params, new javax.xml.namespace.QName("", "registerReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "register"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("register") == null) {
            _myOperations.put("register", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("register")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iworkers", "ArrayOfApplication"), com.strandgenomics.imaging.iserver.services.ws.worker.Application[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("removeApplications", _params, new javax.xml.namespace.QName("", "removeApplicationsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "Directive"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "removeApplications"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("removeApplications") == null) {
            _myOperations.put("removeApplications", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("removeApplications")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iworkers", "ArrayOfApplication"), com.strandgenomics.imaging.iserver.services.ws.worker.Application[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("publishApplications", _params, new javax.xml.namespace.QName("", "publishApplicationsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "Directive"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "publishApplications"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("publishApplications") == null) {
            _myOperations.put("publishApplications", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("publishApplications")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod1", _params, new javax.xml.namespace.QName("", "testMethod1Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "DoubleListConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "testMethod1"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod1") == null) {
            _myOperations.put("testMethod1", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod1")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod3", _params, new javax.xml.namespace.QName("", "testMethod3Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "LongListConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "testMethod3"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod3") == null) {
            _myOperations.put("testMethod3", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod3")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod2", _params, new javax.xml.namespace.QName("", "testMethod2Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "DoubleRangeConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "testMethod2"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod2") == null) {
            _myOperations.put("testMethod2", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod2")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod4", _params, new javax.xml.namespace.QName("", "testMethod4Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "LongRangeConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "testMethod4"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod4") == null) {
            _myOperations.put("testMethod4", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod4")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("testMethod0", _params, new javax.xml.namespace.QName("", "testMethod0Return"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "StringListConstraints"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "testMethod0"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("testMethod0") == null) {
            _myOperations.put("testMethod0", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("testMethod0")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:iworkers", "Request"), com.strandgenomics.imaging.iserver.services.ws.worker.Request.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("ping", _params, new javax.xml.namespace.QName("", "pingReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:iworkers", "Response"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iworkers", "ping"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("ping") == null) {
            _myOperations.put("ping", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("ping")).add(_oper);
    }

    public IWorkersSoapBindingSkeleton() {
        this.impl = new com.strandgenomics.imaging.iserver.services.ws.worker.IWorkersSoapBindingImpl();
    }

    public IWorkersSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.worker.ImageSpaceWorkers impl) {
        this.impl = impl;
    }
    public java.lang.String register(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.register(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.Directive removeApplications(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.worker.Application[] in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.Directive ret = impl.removeApplications(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.Directive publishApplications(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.worker.Application[] in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.Directive ret = impl.publishApplications(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.DoubleListConstraints testMethod1(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.DoubleListConstraints ret = impl.testMethod1(in0);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.LongListConstraints testMethod3(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.LongListConstraints ret = impl.testMethod3(in0);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.DoubleRangeConstraints testMethod2(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.DoubleRangeConstraints ret = impl.testMethod2(in0);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.LongRangeConstraints testMethod4(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.LongRangeConstraints ret = impl.testMethod4(in0);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.StringListConstraints testMethod0(java.lang.String in0) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.StringListConstraints ret = impl.testMethod0(in0);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.worker.Response ping(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.worker.Request in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.worker.Response ret = impl.ping(in0, in1);
        return ret;
    }

}
