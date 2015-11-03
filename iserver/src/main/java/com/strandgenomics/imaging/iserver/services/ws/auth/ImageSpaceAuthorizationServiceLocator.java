/**
 * ImageSpaceAuthorizationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.auth;

public class ImageSpaceAuthorizationServiceLocator extends org.apache.axis.client.Service implements com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorizationService {

    public ImageSpaceAuthorizationServiceLocator() {
    }


    public ImageSpaceAuthorizationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ImageSpaceAuthorizationServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for iAuth
    private java.lang.String iAuth_address = "http://localhost:8080/imanage/services/";

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

    public com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization getiAuth() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(iAuth_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getiAuth(endpoint);
    }

    public com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization getiAuth(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.strandgenomics.imaging.iserver.services.ws.auth.IAuthSoapBindingStub _stub = new com.strandgenomics.imaging.iserver.services.ws.auth.IAuthSoapBindingStub(portAddress, this);
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
            if (com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization.class.isAssignableFrom(serviceEndpointInterface)) {
                com.strandgenomics.imaging.iserver.services.ws.auth.IAuthSoapBindingStub _stub = new com.strandgenomics.imaging.iserver.services.ws.auth.IAuthSoapBindingStub(new java.net.URL(iAuth_address), this);
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
