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
 * CreationRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.loader;

public class CreationRequest  implements java.io.Serializable {
    private com.strandgenomics.imaging.iserver.services.ws.loader.Archive archive;

    private java.lang.String clientMacAddress;

    private com.strandgenomics.imaging.iserver.services.ws.loader.RecordSpecification[] validSignatures;

    public CreationRequest() {
    }

    public CreationRequest(
           com.strandgenomics.imaging.iserver.services.ws.loader.Archive archive,
           java.lang.String clientMacAddress,
           com.strandgenomics.imaging.iserver.services.ws.loader.RecordSpecification[] validSignatures) {
           this.archive = archive;
           this.clientMacAddress = clientMacAddress;
           this.validSignatures = validSignatures;
    }


    /**
     * Gets the archive value for this CreationRequest.
     * 
     * @return archive
     */
    public com.strandgenomics.imaging.iserver.services.ws.loader.Archive getArchive() {
        return archive;
    }


    /**
     * Sets the archive value for this CreationRequest.
     * 
     * @param archive
     */
    public void setArchive(com.strandgenomics.imaging.iserver.services.ws.loader.Archive archive) {
        this.archive = archive;
    }


    /**
     * Gets the clientMacAddress value for this CreationRequest.
     * 
     * @return clientMacAddress
     */
    public java.lang.String getClientMacAddress() {
        return clientMacAddress;
    }


    /**
     * Sets the clientMacAddress value for this CreationRequest.
     * 
     * @param clientMacAddress
     */
    public void setClientMacAddress(java.lang.String clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
    }


    /**
     * Gets the validSignatures value for this CreationRequest.
     * 
     * @return validSignatures
     */
    public com.strandgenomics.imaging.iserver.services.ws.loader.RecordSpecification[] getValidSignatures() {
        return validSignatures;
    }


    /**
     * Sets the validSignatures value for this CreationRequest.
     * 
     * @param validSignatures
     */
    public void setValidSignatures(com.strandgenomics.imaging.iserver.services.ws.loader.RecordSpecification[] validSignatures) {
        this.validSignatures = validSignatures;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreationRequest)) return false;
        CreationRequest other = (CreationRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.archive==null && other.getArchive()==null) || 
             (this.archive!=null &&
              this.archive.equals(other.getArchive()))) &&
            ((this.clientMacAddress==null && other.getClientMacAddress()==null) || 
             (this.clientMacAddress!=null &&
              this.clientMacAddress.equals(other.getClientMacAddress()))) &&
            ((this.validSignatures==null && other.getValidSignatures()==null) || 
             (this.validSignatures!=null &&
              java.util.Arrays.equals(this.validSignatures, other.getValidSignatures())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getArchive() != null) {
            _hashCode += getArchive().hashCode();
        }
        if (getClientMacAddress() != null) {
            _hashCode += getClientMacAddress().hashCode();
        }
        if (getValidSignatures() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getValidSignatures());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValidSignatures(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreationRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "CreationRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("archive");
        elemField.setXmlName(new javax.xml.namespace.QName("", "archive"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Archive"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientMacAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clientMacAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validSignatures");
        elemField.setXmlName(new javax.xml.namespace.QName("", "validSignatures"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "RecordSpecification"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
