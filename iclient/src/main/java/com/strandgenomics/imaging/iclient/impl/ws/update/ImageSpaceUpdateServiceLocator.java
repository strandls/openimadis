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

package com.strandgenomics.imaging.iclient.impl.ws.update;

public class ImageSpaceUpdateServiceLocator extends org.apache.axis.client.Service implements com.strandgenomics.imaging.iclient.impl.ws.update.ImageSpaceUpdateService {

    public ImageSpaceUpdateServiceLocator() {
    }


    public ImageSpaceUpdateServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ImageSpaceUpdateServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for iUpdate
    private java.lang.String iUpdate_address = "http://localhost:8080/imanage/services/iUpdate";

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

    public com.strandgenomics.imaging.iclient.impl.ws.update.ImageSpaceUpdate getiUpdate() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(iUpdate_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getiUpdate(endpoint);
    }

    public com.strandgenomics.imaging.iclient.impl.ws.update.ImageSpaceUpdate getiUpdate(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.strandgenomics.imaging.iclient.impl.ws.update.IUpdateSoapBindingStub _stub = new com.strandgenomics.imaging.iclient.impl.ws.update.IUpdateSoapBindingStub(portAddress, this);
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
            if (com.strandgenomics.imaging.iclient.impl.ws.update.ImageSpaceUpdate.class.isAssignableFrom(serviceEndpointInterface)) {
                com.strandgenomics.imaging.iclient.impl.ws.update.IUpdateSoapBindingStub _stub = new com.strandgenomics.imaging.iclient.impl.ws.update.IUpdateSoapBindingStub(new java.net.URL(iUpdate_address), this);
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
