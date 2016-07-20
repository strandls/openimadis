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
 * IManageSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.manage;

public class IManageSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagement, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagement impl;
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
        _oper = new org.apache.axis.description.OperationDesc("transfer", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "transfer"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("transfer") == null) {
            _myOperations.put("transfer", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("transfer")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createInternalUser", _params, new javax.xml.namespace.QName("", "createInternalUserReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "createInternalUser"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createInternalUser") == null) {
            _myOperations.put("createInternalUser", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createInternalUser")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("allowExternalUser", _params, new javax.xml.namespace.QName("", "allowExternalUserReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "allowExternalUser"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("allowExternalUser") == null) {
            _myOperations.put("allowExternalUser", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("allowExternalUser")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("restoreProject", _params, new javax.xml.namespace.QName("", "restoreProjectReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:imanage", "Task"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "restoreProject"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("restoreProject") == null) {
            _myOperations.put("restoreProject", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("restoreProject")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("createNewProject", _params, new javax.xml.namespace.QName("", "createNewProjectReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "createNewProject"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("createNewProject") == null) {
            _myOperations.put("createNewProject", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("createNewProject")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getProjectMembers", _params, new javax.xml.namespace.QName("", "getProjectMembersReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:imanage", "ArrayOfUser"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "getProjectMembers"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getProjectMembers") == null) {
            _myOperations.put("getProjectMembers", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getProjectMembers")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getProjectManager", _params, new javax.xml.namespace.QName("", "getProjectManagerReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:imanage", "ArrayOfUser"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "getProjectManager"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getProjectManager") == null) {
            _myOperations.put("getProjectManager", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getProjectManager")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("archiveProject", _params, new javax.xml.namespace.QName("", "archiveProjectReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:imanage", "Task"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "archiveProject"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("archiveProject") == null) {
            _myOperations.put("archiveProject", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("archiveProject")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:imanage", "ArrayOf_soapenc_string"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("addProjectMembers", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "addProjectMembers"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("addProjectMembers") == null) {
            _myOperations.put("addProjectMembers", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("addProjectMembers")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteArchive", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "deleteArchive"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteArchive") == null) {
            _myOperations.put("deleteArchive", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteArchive")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteRecord", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "deleteRecord"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteRecord") == null) {
            _myOperations.put("deleteRecord", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteRecord")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("deleteProject", _params, new javax.xml.namespace.QName("", "deleteProjectReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:imanage", "Task"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "deleteProject"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("deleteProject") == null) {
            _myOperations.put("deleteProject", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("deleteProject")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("renameProject", _params, new javax.xml.namespace.QName("", "renameProjectReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "renameProject"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("renameProject") == null) {
            _myOperations.put("renameProject", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("renameProject")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:imanage", "Task"), com.strandgenomics.imaging.iserver.services.ws.manage.Task.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getJobStatus", _params, new javax.xml.namespace.QName("", "getJobStatusReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:imanage", "getJobStatus"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getJobStatus") == null) {
            _myOperations.put("getJobStatus", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getJobStatus")).add(_oper);
    }

    public IManageSoapBindingSkeleton() {
        this.impl = new com.strandgenomics.imaging.iserver.services.ws.manage.IManageSoapBindingImpl();
    }

    public IManageSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagement impl) {
        this.impl = impl;
    }
    public void transfer(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        impl.transfer(in0, in1, in2);
    }

    public boolean createInternalUser(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5) throws java.rmi.RemoteException
    {
        boolean ret = impl.createInternalUser(in0, in1, in2, in3, in4, in5);
        return ret;
    }

    public boolean allowExternalUser(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        boolean ret = impl.allowExternalUser(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.manage.Task restoreProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.manage.Task ret = impl.restoreProject(in0, in1);
        return ret;
    }

    public boolean createNewProject(java.lang.String in0, java.lang.String in1, java.lang.String in2, double in3) throws java.rmi.RemoteException
    {
        boolean ret = impl.createNewProject(in0, in1, in2, in3);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.manage.User[] getProjectMembers(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.manage.User[] ret = impl.getProjectMembers(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.manage.User[] getProjectManager(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.manage.User[] ret = impl.getProjectManager(in0, in1);
        return ret;
    }

    public com.strandgenomics.imaging.iserver.services.ws.manage.Task archiveProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.manage.Task ret = impl.archiveProject(in0, in1);
        return ret;
    }

    public void addProjectMembers(java.lang.String in0, java.lang.String in1, java.lang.String[] in2, java.lang.String in3) throws java.rmi.RemoteException
    {
        impl.addProjectMembers(in0, in1, in2, in3);
    }

    public void deleteArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        impl.deleteArchive(in0, in1);
    }

    public void deleteRecord(java.lang.String in0, long in1) throws java.rmi.RemoteException
    {
        impl.deleteRecord(in0, in1);
    }

    public com.strandgenomics.imaging.iserver.services.ws.manage.Task deleteProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        com.strandgenomics.imaging.iserver.services.ws.manage.Task ret = impl.deleteProject(in0, in1);
        return ret;
    }

    public boolean renameProject(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        boolean ret = impl.renameProject(in0, in1, in2);
        return ret;
    }

    public java.lang.String getJobStatus(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.manage.Task in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getJobStatus(in0, in1);
        return ret;
    }

}
