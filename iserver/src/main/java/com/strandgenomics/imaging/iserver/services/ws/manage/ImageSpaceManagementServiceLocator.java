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
 * ImageSpaceManagementServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.manage;

public class ImageSpaceManagementServiceLocator extends org.apache.axis.client.Service implements com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagementService {

    public ImageSpaceManagementServiceLocator() {
    }


    public ImageSpaceManagementServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ImageSpaceManagementServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for iManage
    private java.lang.String iManage_address = "http://localhost:8080/imanage/services/";

    public java.lang.String getiManageAddress() {
        return iManage_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String iManageWSDDServiceName = "iManage";

    public java.lang.String getiManageWSDDServiceName() {
        return iManageWSDDServiceName;
    }

    public void setiManageWSDDServiceName(java.lang.String name) {
        iManageWSDDServiceName = name;
    }

    public com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagement getiManage() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(iManage_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getiManage(endpoint);
    }

    public com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagement getiManage(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.strandgenomics.imaging.iserver.services.ws.manage.IManageSoapBindingStub _stub = new com.strandgenomics.imaging.iserver.services.ws.manage.IManageSoapBindingStub(portAddress, this);
            _stub.setPortName(getiManageWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setiManageEndpointAddress(java.lang.String address) {
        iManage_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagement.class.isAssignableFrom(serviceEndpointInterface)) {
                com.strandgenomics.imaging.iserver.services.ws.manage.IManageSoapBindingStub _stub = new com.strandgenomics.imaging.iserver.services.ws.manage.IManageSoapBindingStub(new java.net.URL(iManage_address), this);
                _stub.setPortName(getiManageWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("iManage".equals(inputPortName)) {
            return getiManage();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:imanage", "ImageSpaceManagementService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:imanage", "iManage"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("iManage".equals(portName)) {
            setiManageEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
