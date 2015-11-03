/**
 * Record.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.ispace;

public class Record  implements java.io.Serializable {
    private java.lang.Long acquiredDate;

    private java.lang.Long creationTime;

    private int imageType;

    private java.lang.String ipAddress;

    private java.lang.String macAddress;

    private int pixelDepth;

    private double pixelSizeAlongXAxis;

    private double pixelSizeAlongYAxis;

    private double pixelSizeAlongZAxis;

    private com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint signature;

    private long sizeOnDisk;

    private java.lang.Long sourceFileTime;

    private java.lang.String sourceFilename;

    private java.lang.String sourceFolder;

    private java.lang.String sourceFormat;

    private java.lang.Long uploadTime;

    private java.lang.String uploadedBy;

    private boolean writable;

    public Record() {
    }

    public Record(
           java.lang.Long acquiredDate,
           java.lang.Long creationTime,
           int imageType,
           java.lang.String ipAddress,
           java.lang.String macAddress,
           int pixelDepth,
           double pixelSizeAlongXAxis,
           double pixelSizeAlongYAxis,
           double pixelSizeAlongZAxis,
           com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint signature,
           long sizeOnDisk,
           java.lang.Long sourceFileTime,
           java.lang.String sourceFilename,
           java.lang.String sourceFolder,
           java.lang.String sourceFormat,
           java.lang.Long uploadTime,
           java.lang.String uploadedBy,
           boolean writable) {
           this.acquiredDate = acquiredDate;
           this.creationTime = creationTime;
           this.imageType = imageType;
           this.ipAddress = ipAddress;
           this.macAddress = macAddress;
           this.pixelDepth = pixelDepth;
           this.pixelSizeAlongXAxis = pixelSizeAlongXAxis;
           this.pixelSizeAlongYAxis = pixelSizeAlongYAxis;
           this.pixelSizeAlongZAxis = pixelSizeAlongZAxis;
           this.signature = signature;
           this.sizeOnDisk = sizeOnDisk;
           this.sourceFileTime = sourceFileTime;
           this.sourceFilename = sourceFilename;
           this.sourceFolder = sourceFolder;
           this.sourceFormat = sourceFormat;
           this.uploadTime = uploadTime;
           this.uploadedBy = uploadedBy;
           this.writable = writable;
    }


    /**
     * Gets the acquiredDate value for this Record.
     * 
     * @return acquiredDate
     */
    public java.lang.Long getAcquiredDate() {
        return acquiredDate;
    }


    /**
     * Sets the acquiredDate value for this Record.
     * 
     * @param acquiredDate
     */
    public void setAcquiredDate(java.lang.Long acquiredDate) {
        this.acquiredDate = acquiredDate;
    }


    /**
     * Gets the creationTime value for this Record.
     * 
     * @return creationTime
     */
    public java.lang.Long getCreationTime() {
        return creationTime;
    }


    /**
     * Sets the creationTime value for this Record.
     * 
     * @param creationTime
     */
    public void setCreationTime(java.lang.Long creationTime) {
        this.creationTime = creationTime;
    }


    /**
     * Gets the imageType value for this Record.
     * 
     * @return imageType
     */
    public int getImageType() {
        return imageType;
    }


    /**
     * Sets the imageType value for this Record.
     * 
     * @param imageType
     */
    public void setImageType(int imageType) {
        this.imageType = imageType;
    }


    /**
     * Gets the ipAddress value for this Record.
     * 
     * @return ipAddress
     */
    public java.lang.String getIpAddress() {
        return ipAddress;
    }


    /**
     * Sets the ipAddress value for this Record.
     * 
     * @param ipAddress
     */
    public void setIpAddress(java.lang.String ipAddress) {
        this.ipAddress = ipAddress;
    }


    /**
     * Gets the macAddress value for this Record.
     * 
     * @return macAddress
     */
    public java.lang.String getMacAddress() {
        return macAddress;
    }


    /**
     * Sets the macAddress value for this Record.
     * 
     * @param macAddress
     */
    public void setMacAddress(java.lang.String macAddress) {
        this.macAddress = macAddress;
    }


    /**
     * Gets the pixelDepth value for this Record.
     * 
     * @return pixelDepth
     */
    public int getPixelDepth() {
        return pixelDepth;
    }


    /**
     * Sets the pixelDepth value for this Record.
     * 
     * @param pixelDepth
     */
    public void setPixelDepth(int pixelDepth) {
        this.pixelDepth = pixelDepth;
    }


    /**
     * Gets the pixelSizeAlongXAxis value for this Record.
     * 
     * @return pixelSizeAlongXAxis
     */
    public double getPixelSizeAlongXAxis() {
        return pixelSizeAlongXAxis;
    }


    /**
     * Sets the pixelSizeAlongXAxis value for this Record.
     * 
     * @param pixelSizeAlongXAxis
     */
    public void setPixelSizeAlongXAxis(double pixelSizeAlongXAxis) {
        this.pixelSizeAlongXAxis = pixelSizeAlongXAxis;
    }


    /**
     * Gets the pixelSizeAlongYAxis value for this Record.
     * 
     * @return pixelSizeAlongYAxis
     */
    public double getPixelSizeAlongYAxis() {
        return pixelSizeAlongYAxis;
    }


    /**
     * Sets the pixelSizeAlongYAxis value for this Record.
     * 
     * @param pixelSizeAlongYAxis
     */
    public void setPixelSizeAlongYAxis(double pixelSizeAlongYAxis) {
        this.pixelSizeAlongYAxis = pixelSizeAlongYAxis;
    }


    /**
     * Gets the pixelSizeAlongZAxis value for this Record.
     * 
     * @return pixelSizeAlongZAxis
     */
    public double getPixelSizeAlongZAxis() {
        return pixelSizeAlongZAxis;
    }


    /**
     * Sets the pixelSizeAlongZAxis value for this Record.
     * 
     * @param pixelSizeAlongZAxis
     */
    public void setPixelSizeAlongZAxis(double pixelSizeAlongZAxis) {
        this.pixelSizeAlongZAxis = pixelSizeAlongZAxis;
    }


    /**
     * Gets the signature value for this Record.
     * 
     * @return signature
     */
    public com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint getSignature() {
        return signature;
    }


    /**
     * Sets the signature value for this Record.
     * 
     * @param signature
     */
    public void setSignature(com.strandgenomics.imaging.iclient.impl.ws.ispace.FingerPrint signature) {
        this.signature = signature;
    }


    /**
     * Gets the sizeOnDisk value for this Record.
     * 
     * @return sizeOnDisk
     */
    public long getSizeOnDisk() {
        return sizeOnDisk;
    }


    /**
     * Sets the sizeOnDisk value for this Record.
     * 
     * @param sizeOnDisk
     */
    public void setSizeOnDisk(long sizeOnDisk) {
        this.sizeOnDisk = sizeOnDisk;
    }


    /**
     * Gets the sourceFileTime value for this Record.
     * 
     * @return sourceFileTime
     */
    public java.lang.Long getSourceFileTime() {
        return sourceFileTime;
    }


    /**
     * Sets the sourceFileTime value for this Record.
     * 
     * @param sourceFileTime
     */
    public void setSourceFileTime(java.lang.Long sourceFileTime) {
        this.sourceFileTime = sourceFileTime;
    }


    /**
     * Gets the sourceFilename value for this Record.
     * 
     * @return sourceFilename
     */
    public java.lang.String getSourceFilename() {
        return sourceFilename;
    }


    /**
     * Sets the sourceFilename value for this Record.
     * 
     * @param sourceFilename
     */
    public void setSourceFilename(java.lang.String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }


    /**
     * Gets the sourceFolder value for this Record.
     * 
     * @return sourceFolder
     */
    public java.lang.String getSourceFolder() {
        return sourceFolder;
    }


    /**
     * Sets the sourceFolder value for this Record.
     * 
     * @param sourceFolder
     */
    public void setSourceFolder(java.lang.String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }


    /**
     * Gets the sourceFormat value for this Record.
     * 
     * @return sourceFormat
     */
    public java.lang.String getSourceFormat() {
        return sourceFormat;
    }


    /**
     * Sets the sourceFormat value for this Record.
     * 
     * @param sourceFormat
     */
    public void setSourceFormat(java.lang.String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }


    /**
     * Gets the uploadTime value for this Record.
     * 
     * @return uploadTime
     */
    public java.lang.Long getUploadTime() {
        return uploadTime;
    }


    /**
     * Sets the uploadTime value for this Record.
     * 
     * @param uploadTime
     */
    public void setUploadTime(java.lang.Long uploadTime) {
        this.uploadTime = uploadTime;
    }


    /**
     * Gets the uploadedBy value for this Record.
     * 
     * @return uploadedBy
     */
    public java.lang.String getUploadedBy() {
        return uploadedBy;
    }


    /**
     * Sets the uploadedBy value for this Record.
     * 
     * @param uploadedBy
     */
    public void setUploadedBy(java.lang.String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }


    /**
     * Gets the writable value for this Record.
     * 
     * @return writable
     */
    public boolean isWritable() {
        return writable;
    }


    /**
     * Sets the writable value for this Record.
     * 
     * @param writable
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Record)) return false;
        Record other = (Record) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acquiredDate==null && other.getAcquiredDate()==null) || 
             (this.acquiredDate!=null &&
              this.acquiredDate.equals(other.getAcquiredDate()))) &&
            ((this.creationTime==null && other.getCreationTime()==null) || 
             (this.creationTime!=null &&
              this.creationTime.equals(other.getCreationTime()))) &&
            this.imageType == other.getImageType() &&
            ((this.ipAddress==null && other.getIpAddress()==null) || 
             (this.ipAddress!=null &&
              this.ipAddress.equals(other.getIpAddress()))) &&
            ((this.macAddress==null && other.getMacAddress()==null) || 
             (this.macAddress!=null &&
              this.macAddress.equals(other.getMacAddress()))) &&
            this.pixelDepth == other.getPixelDepth() &&
            this.pixelSizeAlongXAxis == other.getPixelSizeAlongXAxis() &&
            this.pixelSizeAlongYAxis == other.getPixelSizeAlongYAxis() &&
            this.pixelSizeAlongZAxis == other.getPixelSizeAlongZAxis() &&
            ((this.signature==null && other.getSignature()==null) || 
             (this.signature!=null &&
              this.signature.equals(other.getSignature()))) &&
            this.sizeOnDisk == other.getSizeOnDisk() &&
            ((this.sourceFileTime==null && other.getSourceFileTime()==null) || 
             (this.sourceFileTime!=null &&
              this.sourceFileTime.equals(other.getSourceFileTime()))) &&
            ((this.sourceFilename==null && other.getSourceFilename()==null) || 
             (this.sourceFilename!=null &&
              this.sourceFilename.equals(other.getSourceFilename()))) &&
            ((this.sourceFolder==null && other.getSourceFolder()==null) || 
             (this.sourceFolder!=null &&
              this.sourceFolder.equals(other.getSourceFolder()))) &&
            ((this.sourceFormat==null && other.getSourceFormat()==null) || 
             (this.sourceFormat!=null &&
              this.sourceFormat.equals(other.getSourceFormat()))) &&
            ((this.uploadTime==null && other.getUploadTime()==null) || 
             (this.uploadTime!=null &&
              this.uploadTime.equals(other.getUploadTime()))) &&
            ((this.uploadedBy==null && other.getUploadedBy()==null) || 
             (this.uploadedBy!=null &&
              this.uploadedBy.equals(other.getUploadedBy()))) &&
            this.writable == other.isWritable();
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
        if (getAcquiredDate() != null) {
            _hashCode += getAcquiredDate().hashCode();
        }
        if (getCreationTime() != null) {
            _hashCode += getCreationTime().hashCode();
        }
        _hashCode += getImageType();
        if (getIpAddress() != null) {
            _hashCode += getIpAddress().hashCode();
        }
        if (getMacAddress() != null) {
            _hashCode += getMacAddress().hashCode();
        }
        _hashCode += getPixelDepth();
        _hashCode += new Double(getPixelSizeAlongXAxis()).hashCode();
        _hashCode += new Double(getPixelSizeAlongYAxis()).hashCode();
        _hashCode += new Double(getPixelSizeAlongZAxis()).hashCode();
        if (getSignature() != null) {
            _hashCode += getSignature().hashCode();
        }
        _hashCode += new Long(getSizeOnDisk()).hashCode();
        if (getSourceFileTime() != null) {
            _hashCode += getSourceFileTime().hashCode();
        }
        if (getSourceFilename() != null) {
            _hashCode += getSourceFilename().hashCode();
        }
        if (getSourceFolder() != null) {
            _hashCode += getSourceFolder().hashCode();
        }
        if (getSourceFormat() != null) {
            _hashCode += getSourceFormat().hashCode();
        }
        if (getUploadTime() != null) {
            _hashCode += getUploadTime().hashCode();
        }
        if (getUploadedBy() != null) {
            _hashCode += getUploadedBy().hashCode();
        }
        _hashCode += (isWritable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Record.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "Record"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acquiredDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "acquiredDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ipAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ipAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("macAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "macAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pixelDepth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pixelDepth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pixelSizeAlongXAxis");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pixelSizeAlongXAxis"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pixelSizeAlongYAxis");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pixelSizeAlongYAxis"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pixelSizeAlongZAxis");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pixelSizeAlongZAxis"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signature");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:ispace", "FingerPrint"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sizeOnDisk");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sizeOnDisk"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceFileTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceFileTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceFilename");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceFilename"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceFolder");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceFolder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uploadTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadedBy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uploadedBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("writable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "writable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
