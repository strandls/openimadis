/**
 * MosaicRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class MosaicRequest  implements java.io.Serializable {
    private java.lang.Long[] recordids;

    public MosaicRequest() {
    }

    public MosaicRequest(
           java.lang.Long[] recordids) {
           this.recordids = recordids;
    }


    /**
     * Gets the recordids value for this MosaicRequest.
     * 
     * @return recordids
     */
    public java.lang.Long[] getRecordids() {
        return recordids;
    }


    /**
     * Sets the recordids value for this MosaicRequest.
     * 
     * @param recordids
     */
    public void setRecordids(java.lang.Long[] recordids) {
        this.recordids = recordids;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MosaicRequest)) return false;
        MosaicRequest other = (MosaicRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.recordids==null && other.getRecordids()==null) || 
             (this.recordids!=null &&
              java.util.Arrays.equals(this.recordids, other.getRecordids())));
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
        if (getRecordids() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRecordids());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRecordids(), i);
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
        new org.apache.axis.description.TypeDesc(MosaicRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "MosaicRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordids");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recordids"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
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
