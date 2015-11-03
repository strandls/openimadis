/**
 * FingerPrint.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class FingerPrint  implements java.io.Serializable {
    private java.lang.String archiveHash;

    private int imageHeight;

    private int imageWidth;

    private int noOfChannels;

    private int noOfFrames;

    private int noOfSites;

    private int noOfSlices;

    private java.lang.String siteHash;

    public FingerPrint() {
    }

    public FingerPrint(
           java.lang.String archiveHash,
           int imageHeight,
           int imageWidth,
           int noOfChannels,
           int noOfFrames,
           int noOfSites,
           int noOfSlices,
           java.lang.String siteHash) {
           this.archiveHash = archiveHash;
           this.imageHeight = imageHeight;
           this.imageWidth = imageWidth;
           this.noOfChannels = noOfChannels;
           this.noOfFrames = noOfFrames;
           this.noOfSites = noOfSites;
           this.noOfSlices = noOfSlices;
           this.siteHash = siteHash;
    }


    /**
     * Gets the archiveHash value for this FingerPrint.
     * 
     * @return archiveHash
     */
    public java.lang.String getArchiveHash() {
        return archiveHash;
    }


    /**
     * Sets the archiveHash value for this FingerPrint.
     * 
     * @param archiveHash
     */
    public void setArchiveHash(java.lang.String archiveHash) {
        this.archiveHash = archiveHash;
    }


    /**
     * Gets the imageHeight value for this FingerPrint.
     * 
     * @return imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }


    /**
     * Sets the imageHeight value for this FingerPrint.
     * 
     * @param imageHeight
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }


    /**
     * Gets the imageWidth value for this FingerPrint.
     * 
     * @return imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }


    /**
     * Sets the imageWidth value for this FingerPrint.
     * 
     * @param imageWidth
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }


    /**
     * Gets the noOfChannels value for this FingerPrint.
     * 
     * @return noOfChannels
     */
    public int getNoOfChannels() {
        return noOfChannels;
    }


    /**
     * Sets the noOfChannels value for this FingerPrint.
     * 
     * @param noOfChannels
     */
    public void setNoOfChannels(int noOfChannels) {
        this.noOfChannels = noOfChannels;
    }


    /**
     * Gets the noOfFrames value for this FingerPrint.
     * 
     * @return noOfFrames
     */
    public int getNoOfFrames() {
        return noOfFrames;
    }


    /**
     * Sets the noOfFrames value for this FingerPrint.
     * 
     * @param noOfFrames
     */
    public void setNoOfFrames(int noOfFrames) {
        this.noOfFrames = noOfFrames;
    }


    /**
     * Gets the noOfSites value for this FingerPrint.
     * 
     * @return noOfSites
     */
    public int getNoOfSites() {
        return noOfSites;
    }


    /**
     * Sets the noOfSites value for this FingerPrint.
     * 
     * @param noOfSites
     */
    public void setNoOfSites(int noOfSites) {
        this.noOfSites = noOfSites;
    }


    /**
     * Gets the noOfSlices value for this FingerPrint.
     * 
     * @return noOfSlices
     */
    public int getNoOfSlices() {
        return noOfSlices;
    }


    /**
     * Sets the noOfSlices value for this FingerPrint.
     * 
     * @param noOfSlices
     */
    public void setNoOfSlices(int noOfSlices) {
        this.noOfSlices = noOfSlices;
    }


    /**
     * Gets the siteHash value for this FingerPrint.
     * 
     * @return siteHash
     */
    public java.lang.String getSiteHash() {
        return siteHash;
    }


    /**
     * Sets the siteHash value for this FingerPrint.
     * 
     * @param siteHash
     */
    public void setSiteHash(java.lang.String siteHash) {
        this.siteHash = siteHash;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FingerPrint)) return false;
        FingerPrint other = (FingerPrint) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.archiveHash==null && other.getArchiveHash()==null) || 
             (this.archiveHash!=null &&
              this.archiveHash.equals(other.getArchiveHash()))) &&
            this.imageHeight == other.getImageHeight() &&
            this.imageWidth == other.getImageWidth() &&
            this.noOfChannels == other.getNoOfChannels() &&
            this.noOfFrames == other.getNoOfFrames() &&
            this.noOfSites == other.getNoOfSites() &&
            this.noOfSlices == other.getNoOfSlices() &&
            ((this.siteHash==null && other.getSiteHash()==null) || 
             (this.siteHash!=null &&
              this.siteHash.equals(other.getSiteHash())));
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
        if (getArchiveHash() != null) {
            _hashCode += getArchiveHash().hashCode();
        }
        _hashCode += getImageHeight();
        _hashCode += getImageWidth();
        _hashCode += getNoOfChannels();
        _hashCode += getNoOfFrames();
        _hashCode += getNoOfSites();
        _hashCode += getNoOfSlices();
        if (getSiteHash() != null) {
            _hashCode += getSiteHash().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FingerPrint.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "FingerPrint"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("archiveHash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "archiveHash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageWidth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfChannels");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noOfChannels"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfFrames");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noOfFrames"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfSites");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noOfSites"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfSlices");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noOfSlices"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siteHash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "siteHash"));
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
