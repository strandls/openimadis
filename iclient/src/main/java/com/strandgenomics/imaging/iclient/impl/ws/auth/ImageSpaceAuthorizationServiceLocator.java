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

package com.strandgenomics.imaging.iclient.impl.ws.auth;

public class ImageSpaceAuthorizationServiceLocator extends org.apache.axis.client.Service implements com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorizationService {

    public ImageSpaceAuthorizationServiceLocator() {
    }


    public ImageSpaceAuthorizationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ImageSpaceAuthorizationServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for iAuth
    private java.lang.String iAuth_address = "http://localhost:8080/imanage/services/iAuth";

    public java.lang.String getiAuthAddress() {
        return iAuth_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String iAuthWSDDServiceName = "iAuth";

    public java.lang.String getiAuthWSDDServiceName() {
        return iAuthWSDDServiceName;
    }

    public void setiAuthWSDDServiceName(java.lang.String name) {
        iAuthWSDDServiceName = name;
    }

    public com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorization getiAuth() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(iAuth_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getiAuth(endpoint);
    }

    public com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorization getiAuth(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.strandgenomics.imaging.iclient.impl.ws.auth.IAuthSoapBindingStub _stub = new com.strandgenomics.imaging.iclient.impl.ws.auth.IAuthSoapBindingStub(portAddress, this);
            _stub.setPortName(getiAuthWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setiAuthEndpointAddress(java.lang.String address) {
        iAuth_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorization.class.isAssignableFrom(serviceEndpointInterface)) {
                com.strandgenomics.imaging.iclient.impl.ws.auth.IAuthSoapBindingStub _stub = new com.strandgenomics.imaging.iclient.impl.ws.auth.IAuthSoapBindingStub(new java.net.URL(iAuth_address), this);
                _stub.setPortName(getiAuthWSDDServiceName());
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
        if ("iAuth".equals(inputPortName)) {
            return getiAuth();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:iauth", "ImageSpaceAuthorizationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:iauth", "iAuth"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("iAuth".equals(portName)) {
            setiAuthEndpointAddress(address);
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
