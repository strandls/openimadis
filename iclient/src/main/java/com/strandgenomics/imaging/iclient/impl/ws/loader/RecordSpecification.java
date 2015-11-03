/**
 * RecordSpecification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.loader;

public class RecordSpecification  implements java.io.Serializable {
    private com.strandgenomics.imaging.iclient.impl.ws.loader.Channel[] customChannels;

    private com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[] siteToMerge;

    public RecordSpecification() {
    }

    public RecordSpecification(
           com.strandgenomics.imaging.iclient.impl.ws.loader.Channel[] customChannels,
           com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[] siteToMerge) {
           this.customChannels = customChannels;
           this.siteToMerge = siteToMerge;
    }


    /**
     * Gets the customChannels value for this RecordSpecification.
     * 
     * @return customChannels
     */
    public com.strandgenomics.imaging.iclient.impl.ws.loader.Channel[] getCustomChannels() {
        return customChannels;
    }


    /**
     * Sets the customChannels value for this RecordSpecification.
     * 
     * @param customChannels
     */
    public void setCustomChannels(com.strandgenomics.imaging.iclient.impl.ws.loader.Channel[] customChannels) {
        this.customChannels = customChannels;
    }


    /**
     * Gets the siteToMerge value for this RecordSpecification.
     * 
     * @return siteToMerge
     */
    public com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[] getSiteToMerge() {
        return siteToMerge;
    }


    /**
     * Sets the siteToMerge value for this RecordSpecification.
     * 
     * @param siteToMerge
     */
    public void setSiteToMerge(com.strandgenomics.imaging.iclient.impl.ws.loader.RecordSite[] siteToMerge) {
        this.siteToMerge = siteToMerge;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RecordSpecification)) return false;
        RecordSpecification other = (RecordSpecification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.customChannels==null && other.getCustomChannels()==null) || 
             (this.customChannels!=null &&
              java.util.Arrays.equals(this.customChannels, other.getCustomChannels()))) &&
            ((this.siteToMerge==null && other.getSiteToMerge()==null) || 
             (this.siteToMerge!=null &&
              java.util.Arrays.equals(this.siteToMerge, other.getSiteToMerge())));
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
        if (getCustomChannels() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCustomChannels());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCustomChannels(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSiteToMerge() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSiteToMerge());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSiteToMerge(), i);
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
        new org.apache.axis.description.TypeDesc(RecordSpecification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "RecordSpecification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customChannels");
        elemField.setXmlName(new javax.xml.namespace.QName("", "customChannels"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Channel"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siteToMerge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "siteToMerge"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "RecordSite"));
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
