/**
 * SourceFile.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.loader;

public class SourceFile  implements java.io.Serializable {
    private java.lang.String filePath;

    private long lastModificationTime;

    private long length;

    public SourceFile() {
    }

    public SourceFile(
           java.lang.String filePath,
           long lastModificationTime,
           long length) {
           this.filePath = filePath;
           this.lastModificationTime = lastModificationTime;
           this.length = length;
    }


    /**
     * Gets the filePath value for this SourceFile.
     * 
     * @return filePath
     */
    public java.lang.String getFilePath() {
        return filePath;
    }


    /**
     * Sets the filePath value for this SourceFile.
     * 
     * @param filePath
     */
    public void setFilePath(java.lang.String filePath) {
        this.filePath = filePath;
    }


    /**
     * Gets the lastModificationTime value for this SourceFile.
     * 
     * @return lastModificationTime
     */
    public long getLastModificationTime() {
        return lastModificationTime;
    }


    /**
     * Sets the lastModificationTime value for this SourceFile.
     * 
     * @param lastModificationTime
     */
    public void setLastModificationTime(long lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }


    /**
     * Gets the length value for this SourceFile.
     * 
     * @return length
     */
    public long getLength() {
        return length;
    }


    /**
     * Sets the length value for this SourceFile.
     * 
     * @param length
     */
    public void setLength(long length) {
        this.length = length;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SourceFile)) return false;
        SourceFile other = (SourceFile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.filePath==null && other.getFilePath()==null) || 
             (this.filePath!=null &&
              this.filePath.equals(other.getFilePath()))) &&
            this.lastModificationTime == other.getLastModificationTime() &&
            this.length == other.getLength();
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
        if (getFilePath() != null) {
            _hashCode += getFilePath().hashCode();
        }
        _hashCode += new Long(getLastModificationTime()).hashCode();
        _hashCode += new Long(getLength()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SourceFile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "SourceFile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filePath");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filePath"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModificationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lastModificationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("length");
        elemField.setXmlName(new javax.xml.namespace.QName("", "length"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
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
