/**
 * ImageSpaceUpdateServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.update;

public class ImageSpaceUpdateServiceLocator extends org.apache.axis.client.Service implements com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdateService {

    public ImageSpaceUpdateServiceLocator() {
    }


    public ImageSpaceUpdateServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ImageSpaceUpdateServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for iUpdate
    private java.lang.String iUpdate_address = "http://localhost:8080/iManage/services/";

    public java.lang.String getiUpdateAddress() {
        return iUpdate_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String iUpdateWSDDServiceName = "iUpdate";

    public java.lang.String getiUpdateWSDDServiceName() {
        return iUpdateWSDDServiceName;
    }

    public void setiUpdateWSDDServiceName(java.lang.String name) {
        iUpdateWSDDServiceName = name;
    }

    public com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdate getiUpdate() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(iUpdate_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getiUpdate(endpoint);
    }

    public com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdate getiUpdate(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.strandgenomics.imaging.iserver.services.ws.update.IUpdateSoapBindingStub _stub = new com.strandgenomics.imaging.iserver.services.ws.update.IUpdateSoapBindingStub(portAddress, this);
            _stub.setPortName(getiUpdateWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setiUpdateEndpointAddress(java.lang.String address) {
        iUpdate_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.strandgenomics.imaging.iserver.services.ws.update.ImageSpaceUpdate.class.isAssignableFrom(serviceEndpointInterface)) {
                com.strandgenomics.imaging.iserver.services.ws.update.IUpdateSoapBindingStub _stub = new com.strandgenomics.imaging.iserver.services.ws.update.IUpdateSoapBindingStub(new java.net.URL(iUpdate_address), this);
                _stub.setPortName(getiUpdateWSDDServiceName());
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
        if ("iUpdate".equals(inputPortName)) {
            return getiUpdate();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:iupdate", "ImageSpaceUpdateService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:iupdate", "iUpdate"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("iUpdate".equals(portName)) {
            setiUpdateEndpointAddress(address);
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
