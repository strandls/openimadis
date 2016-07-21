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

package com.strandgenomics.imaging.iserver.services.ws.auth;

public class IAuthSoapBindingSkeleton implements com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization, org.apache.axis.wsdl.Skeleton {
    private com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization impl;
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
        _oper = new org.apache.axis.description.OperationDesc("surrenderAccessToken", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iauth", "surrenderAccessToken"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("surrenderAccessToken") == null) {
            _myOperations.put("surrenderAccessToken", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("surrenderAccessToken")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getAccessToken", _params, new javax.xml.namespace.QName("", "getAccessTokenReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iauth", "getAccessToken"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getAccessToken") == null) {
            _myOperations.put("getAccessToken", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getAccessToken")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getUser", _params, new javax.xml.namespace.QName("", "getUserReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iauth", "getUser"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getUser") == null) {
            _myOperations.put("getUser", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getUser")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getExpiryTime", _params, new javax.xml.namespace.QName("", "getExpiryTimeReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:iauth", "getExpiryTime"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getExpiryTime") == null) {
            _myOperations.put("getExpiryTime", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getExpiryTime")).add(_oper);
    }

    public IAuthSoapBindingSkeleton() {
        this.impl = new com.strandgenomics.imaging.iserver.services.ws.auth.IAuthSoapBindingImpl();
    }

    public IAuthSoapBindingSkeleton(com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization impl) {
        this.impl = impl;
    }
    public void surrenderAccessToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        impl.surrenderAccessToken(in0, in1);
    }

    public java.lang.String getAccessToken(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getAccessToken(in0, in1);
        return ret;
    }

    public java.lang.String getUser(java.lang.String in0) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getUser(in0);
        return ret;
    }

    public long getExpiryTime(java.lang.String in0) throws java.rmi.RemoteException
    {
        long ret = impl.getExpiryTime(in0);
        return ret;
    }

}
