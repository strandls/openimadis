/**
 * UploadTicket.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.loader;

public class UploadTicket  implements java.io.Serializable {
    private long ID;

    private java.lang.String archiveSignature;

    private java.lang.String downloadURL;

    private java.lang.String uploadURL;

    public UploadTicket() {
    }

    public UploadTicket(
           long ID,
           java.lang.String archiveSignature,
           java.lang.String downloadURL,
           java.lang.String uploadURL) {
           this.ID = ID;
           this.archiveSignature = archiveSignature;
           this.downloadURL = downloadURL;
           this.uploadURL = uploadURL;
    }


    /**
     * Gets the ID value for this UploadTicket.
     * 
     * @return ID
     */
    public long getID() {
        return ID;
    }


    /**
     * Sets the ID value for this UploadTicket.
     * 
     * @param ID
     */
    public void setID(long ID) {
        this.ID = ID;
    }


    /**
     * Gets the archiveSignature value for this UploadTicket.
     * 
     * @return archiveSignature
     */
    public java.lang.String getArchiveSignature() {
        return archiveSignature;
    }


    /**
     * Sets the archiveSignature value for this UploadTicket.
     * 
     * @param archiveSignature
     */
    public void setArchiveSignature(java.lang.String archiveSignature) {
        this.archiveSignature = archiveSignature;
    }


    /**
     * Gets the downloadURL value for this UploadTicket.
     * 
     * @return downloadURL
     */
    public java.lang.String getDownloadURL() {
        return downloadURL;
    }


    /**
     * Sets the downloadURL value for this UploadTicket.
     * 
     * @param downloadURL
     */
    public void setDownloadURL(java.lang.String downloadURL) {
        this.downloadURL = downloadURL;
    }


    /**
     * Gets the uploadURL value for this UploadTicket.
     * 
     * @return uploadURL
     */
    public java.lang.String getUploadURL() {
        return uploadURL;
    }


    /**
     * Sets the uploadURL value for this UploadTicket.
     * 
     * @param uploadURL
     */
    public void setUploadURL(java.lang.String uploadURL) {
        this.uploadURL = uploadURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UploadTicket)) return false;
        UploadTicket other = (UploadTicket) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ID == other.getID() &&
            ((this.archiveSignature==null && other.getArchiveSignature()==null) || 
             (this.archiveSignature!=null &&
              this.archiveSignature.equals(other.getArchiveSignature()))) &&
            ((this.downloadURL==null && other.getDownloadURL()==null) || 
             (this.downloadURL!=null &&
              this.downloadURL.equals(other.getDownloadURL()))) &&
            ((this.uploadURL==null && other.getUploadURL()==null) || 
             (this.uploadURL!=null &&
              this.uploadURL.equals(other.getUploadURL())));
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
        _hashCode += new Long(getID()).hashCode();
        if (getArchiveSignature() != null) {
            _hashCode += getArchiveSignature().hashCode();
        }
        if (getDownloadURL() != null) {
            _hashCode += getDownloadURL().hashCode();
        }
        if (getUploadURL() != null) {
            _hashCode += getUploadURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UploadTicket.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "UploadTicket"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("archiveSignature");
        elemField.setXmlName(new javax.xml.namespace.QName("", "archiveSignature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("downloadURL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "downloadURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadURL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uploadURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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
