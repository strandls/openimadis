/**
 * RecordBuilderObject.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.loader;

public class RecordBuilderObject  implements java.io.Serializable {
    private java.lang.Long acquiredTime;

    private com.strandgenomics.imaging.iserver.services.ws.loader.Channel[] channels;

    private java.lang.Long creationTime;

    private int imageDepth;

    private int imageHeight;

    private int imageType;

    private int imageWidth;

    private java.lang.String macAddress;

    private java.lang.String machineIP;

    private int noOfFrames;

    private int noOfSlices;

    private java.lang.Long parentRecord;

    private java.lang.String projectName;

    private java.lang.String recordLabel;

    private com.strandgenomics.imaging.iserver.services.ws.loader.RecordSite[] sites;

    private java.lang.String sourceFilename;

    private java.lang.String sourceFolder;

    private java.lang.Long sourceTime;

    private java.lang.String sourceType;

    private java.lang.String uploadedBy;

    private double xPixelSize;

    private double yPixelSize;

    private double zPixelSize;

    public RecordBuilderObject() {
    }

    public RecordBuilderObject(
           java.lang.Long acquiredTime,
           com.strandgenomics.imaging.iserver.services.ws.loader.Channel[] channels,
           java.lang.Long creationTime,
           int imageDepth,
           int imageHeight,
           int imageType,
           int imageWidth,
           java.lang.String macAddress,
           java.lang.String machineIP,
           int noOfFrames,
           int noOfSlices,
           java.lang.Long parentRecord,
           java.lang.String projectName,
           java.lang.String recordLabel,
           com.strandgenomics.imaging.iserver.services.ws.loader.RecordSite[] sites,
           java.lang.String sourceFilename,
           java.lang.String sourceFolder,
           java.lang.Long sourceTime,
           java.lang.String sourceType,
           java.lang.String uploadedBy,
           double xPixelSize,
           double yPixelSize,
           double zPixelSize) {
           this.acquiredTime = acquiredTime;
           this.channels = channels;
           this.creationTime = creationTime;
           this.imageDepth = imageDepth;
           this.imageHeight = imageHeight;
           this.imageType = imageType;
           this.imageWidth = imageWidth;
           this.macAddress = macAddress;
           this.machineIP = machineIP;
           this.noOfFrames = noOfFrames;
           this.noOfSlices = noOfSlices;
           this.parentRecord = parentRecord;
           this.projectName = projectName;
           this.recordLabel = recordLabel;
           this.sites = sites;
           this.sourceFilename = sourceFilename;
           this.sourceFolder = sourceFolder;
           this.sourceTime = sourceTime;
           this.sourceType = sourceType;
           this.uploadedBy = uploadedBy;
           this.xPixelSize = xPixelSize;
           this.yPixelSize = yPixelSize;
           this.zPixelSize = zPixelSize;
    }


    /**
     * Gets the acquiredTime value for this RecordBuilderObject.
     * 
     * @return acquiredTime
     */
    public java.lang.Long getAcquiredTime() {
        return acquiredTime;
    }


    /**
     * Sets the acquiredTime value for this RecordBuilderObject.
     * 
     * @param acquiredTime
     */
    public void setAcquiredTime(java.lang.Long acquiredTime) {
        this.acquiredTime = acquiredTime;
    }


    /**
     * Gets the channels value for this RecordBuilderObject.
     * 
     * @return channels
     */
    public com.strandgenomics.imaging.iserver.services.ws.loader.Channel[] getChannels() {
        return channels;
    }


    /**
     * Sets the channels value for this RecordBuilderObject.
     * 
     * @param channels
     */
    public void setChannels(com.strandgenomics.imaging.iserver.services.ws.loader.Channel[] channels) {
        this.channels = channels;
    }


    /**
     * Gets the creationTime value for this RecordBuilderObject.
     * 
     * @return creationTime
     */
    public java.lang.Long getCreationTime() {
        return creationTime;
    }


    /**
     * Sets the creationTime value for this RecordBuilderObject.
     * 
     * @param creationTime
     */
    public void setCreationTime(java.lang.Long creationTime) {
        this.creationTime = creationTime;
    }


    /**
     * Gets the imageDepth value for this RecordBuilderObject.
     * 
     * @return imageDepth
     */
    public int getImageDepth() {
        return imageDepth;
    }


    /**
     * Sets the imageDepth value for this RecordBuilderObject.
     * 
     * @param imageDepth
     */
    public void setImageDepth(int imageDepth) {
        this.imageDepth = imageDepth;
    }


    /**
     * Gets the imageHeight value for this RecordBuilderObject.
     * 
     * @return imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }


    /**
     * Sets the imageHeight value for this RecordBuilderObject.
     * 
     * @param imageHeight
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }


    /**
     * Gets the imageType value for this RecordBuilderObject.
     * 
     * @return imageType
     */
    public int getImageType() {
        return imageType;
    }


    /**
     * Sets the imageType value for this RecordBuilderObject.
     * 
     * @param imageType
     */
    public void setImageType(int imageType) {
        this.imageType = imageType;
    }


    /**
     * Gets the imageWidth value for this RecordBuilderObject.
     * 
     * @return imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }


    /**
     * Sets the imageWidth value for this RecordBuilderObject.
     * 
     * @param imageWidth
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }


    /**
     * Gets the macAddress value for this RecordBuilderObject.
     * 
     * @return macAddress
     */
    public java.lang.String getMacAddress() {
        return macAddress;
    }


    /**
     * Sets the macAddress value for this RecordBuilderObject.
     * 
     * @param macAddress
     */
    public void setMacAddress(java.lang.String macAddress) {
        this.macAddress = macAddress;
    }


    /**
     * Gets the machineIP value for this RecordBuilderObject.
     * 
     * @return machineIP
     */
    public java.lang.String getMachineIP() {
        return machineIP;
    }


    /**
     * Sets the machineIP value for this RecordBuilderObject.
     * 
     * @param machineIP
     */
    public void setMachineIP(java.lang.String machineIP) {
        this.machineIP = machineIP;
    }


    /**
     * Gets the noOfFrames value for this RecordBuilderObject.
     * 
     * @return noOfFrames
     */
    public int getNoOfFrames() {
        return noOfFrames;
    }


    /**
     * Sets the noOfFrames value for this RecordBuilderObject.
     * 
     * @param noOfFrames
     */
    public void setNoOfFrames(int noOfFrames) {
        this.noOfFrames = noOfFrames;
    }


    /**
     * Gets the noOfSlices value for this RecordBuilderObject.
     * 
     * @return noOfSlices
     */
    public int getNoOfSlices() {
        return noOfSlices;
    }


    /**
     * Sets the noOfSlices value for this RecordBuilderObject.
     * 
     * @param noOfSlices
     */
    public void setNoOfSlices(int noOfSlices) {
        this.noOfSlices = noOfSlices;
    }


    /**
     * Gets the parentRecord value for this RecordBuilderObject.
     * 
     * @return parentRecord
     */
    public java.lang.Long getParentRecord() {
        return parentRecord;
    }


    /**
     * Sets the parentRecord value for this RecordBuilderObject.
     * 
     * @param parentRecord
     */
    public void setParentRecord(java.lang.Long parentRecord) {
        this.parentRecord = parentRecord;
    }


    /**
     * Gets the projectName value for this RecordBuilderObject.
     * 
     * @return projectName
     */
    public java.lang.String getProjectName() {
        return projectName;
    }


    /**
     * Sets the projectName value for this RecordBuilderObject.
     * 
     * @param projectName
     */
    public void setProjectName(java.lang.String projectName) {
        this.projectName = projectName;
    }


    /**
     * Gets the recordLabel value for this RecordBuilderObject.
     * 
     * @return recordLabel
     */
    public java.lang.String getRecordLabel() {
        return recordLabel;
    }


    /**
     * Sets the recordLabel value for this RecordBuilderObject.
     * 
     * @param recordLabel
     */
    public void setRecordLabel(java.lang.String recordLabel) {
        this.recordLabel = recordLabel;
    }


    /**
     * Gets the sites value for this RecordBuilderObject.
     * 
     * @return sites
     */
    public com.strandgenomics.imaging.iserver.services.ws.loader.RecordSite[] getSites() {
        return sites;
    }


    /**
     * Sets the sites value for this RecordBuilderObject.
     * 
     * @param sites
     */
    public void setSites(com.strandgenomics.imaging.iserver.services.ws.loader.RecordSite[] sites) {
        this.sites = sites;
    }


    /**
     * Gets the sourceFilename value for this RecordBuilderObject.
     * 
     * @return sourceFilename
     */
    public java.lang.String getSourceFilename() {
        return sourceFilename;
    }


    /**
     * Sets the sourceFilename value for this RecordBuilderObject.
     * 
     * @param sourceFilename
     */
    public void setSourceFilename(java.lang.String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }


    /**
     * Gets the sourceFolder value for this RecordBuilderObject.
     * 
     * @return sourceFolder
     */
    public java.lang.String getSourceFolder() {
        return sourceFolder;
    }


    /**
     * Sets the sourceFolder value for this RecordBuilderObject.
     * 
     * @param sourceFolder
     */
    public void setSourceFolder(java.lang.String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }


    /**
     * Gets the sourceTime value for this RecordBuilderObject.
     * 
     * @return sourceTime
     */
    public java.lang.Long getSourceTime() {
        return sourceTime;
    }


    /**
     * Sets the sourceTime value for this RecordBuilderObject.
     * 
     * @param sourceTime
     */
    public void setSourceTime(java.lang.Long sourceTime) {
        this.sourceTime = sourceTime;
    }


    /**
     * Gets the sourceType value for this RecordBuilderObject.
     * 
     * @return sourceType
     */
    public java.lang.String getSourceType() {
        return sourceType;
    }


    /**
     * Sets the sourceType value for this RecordBuilderObject.
     * 
     * @param sourceType
     */
    public void setSourceType(java.lang.String sourceType) {
        this.sourceType = sourceType;
    }


    /**
     * Gets the uploadedBy value for this RecordBuilderObject.
     * 
     * @return uploadedBy
     */
    public java.lang.String getUploadedBy() {
        return uploadedBy;
    }


    /**
     * Sets the uploadedBy value for this RecordBuilderObject.
     * 
     * @param uploadedBy
     */
    public void setUploadedBy(java.lang.String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }


    /**
     * Gets the xPixelSize value for this RecordBuilderObject.
     * 
     * @return xPixelSize
     */
    public double getXPixelSize() {
        return xPixelSize;
    }


    /**
     * Sets the xPixelSize value for this RecordBuilderObject.
     * 
     * @param xPixelSize
     */
    public void setXPixelSize(double xPixelSize) {
        this.xPixelSize = xPixelSize;
    }


    /**
     * Gets the yPixelSize value for this RecordBuilderObject.
     * 
     * @return yPixelSize
     */
    public double getYPixelSize() {
        return yPixelSize;
    }


    /**
     * Sets the yPixelSize value for this RecordBuilderObject.
     * 
     * @param yPixelSize
     */
    public void setYPixelSize(double yPixelSize) {
        this.yPixelSize = yPixelSize;
    }


    /**
     * Gets the zPixelSize value for this RecordBuilderObject.
     * 
     * @return zPixelSize
     */
    public double getZPixelSize() {
        return zPixelSize;
    }


    /**
     * Sets the zPixelSize value for this RecordBuilderObject.
     * 
     * @param zPixelSize
     */
    public void setZPixelSize(double zPixelSize) {
        this.zPixelSize = zPixelSize;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RecordBuilderObject)) return false;
        RecordBuilderObject other = (RecordBuilderObject) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acquiredTime==null && other.getAcquiredTime()==null) || 
             (this.acquiredTime!=null &&
              this.acquiredTime.equals(other.getAcquiredTime()))) &&
            ((this.channels==null && other.getChannels()==null) || 
             (this.channels!=null &&
              java.util.Arrays.equals(this.channels, other.getChannels()))) &&
            ((this.creationTime==null && other.getCreationTime()==null) || 
             (this.creationTime!=null &&
              this.creationTime.equals(other.getCreationTime()))) &&
            this.imageDepth == other.getImageDepth() &&
            this.imageHeight == other.getImageHeight() &&
            this.imageType == other.getImageType() &&
            this.imageWidth == other.getImageWidth() &&
            ((this.macAddress==null && other.getMacAddress()==null) || 
             (this.macAddress!=null &&
              this.macAddress.equals(other.getMacAddress()))) &&
            ((this.machineIP==null && other.getMachineIP()==null) || 
             (this.machineIP!=null &&
              this.machineIP.equals(other.getMachineIP()))) &&
            this.noOfFrames == other.getNoOfFrames() &&
            this.noOfSlices == other.getNoOfSlices() &&
            ((this.parentRecord==null && other.getParentRecord()==null) || 
             (this.parentRecord!=null &&
              this.parentRecord.equals(other.getParentRecord()))) &&
            ((this.projectName==null && other.getProjectName()==null) || 
             (this.projectName!=null &&
              this.projectName.equals(other.getProjectName()))) &&
            ((this.recordLabel==null && other.getRecordLabel()==null) || 
             (this.recordLabel!=null &&
              this.recordLabel.equals(other.getRecordLabel()))) &&
            ((this.sites==null && other.getSites()==null) || 
             (this.sites!=null &&
              java.util.Arrays.equals(this.sites, other.getSites()))) &&
            ((this.sourceFilename==null && other.getSourceFilename()==null) || 
             (this.sourceFilename!=null &&
              this.sourceFilename.equals(other.getSourceFilename()))) &&
            ((this.sourceFolder==null && other.getSourceFolder()==null) || 
             (this.sourceFolder!=null &&
              this.sourceFolder.equals(other.getSourceFolder()))) &&
            ((this.sourceTime==null && other.getSourceTime()==null) || 
             (this.sourceTime!=null &&
              this.sourceTime.equals(other.getSourceTime()))) &&
            ((this.sourceType==null && other.getSourceType()==null) || 
             (this.sourceType!=null &&
              this.sourceType.equals(other.getSourceType()))) &&
            ((this.uploadedBy==null && other.getUploadedBy()==null) || 
             (this.uploadedBy!=null &&
              this.uploadedBy.equals(other.getUploadedBy()))) &&
            this.xPixelSize == other.getXPixelSize() &&
            this.yPixelSize == other.getYPixelSize() &&
            this.zPixelSize == other.getZPixelSize();
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
        if (getAcquiredTime() != null) {
            _hashCode += getAcquiredTime().hashCode();
        }
        if (getChannels() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getChannels());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getChannels(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCreationTime() != null) {
            _hashCode += getCreationTime().hashCode();
        }
        _hashCode += getImageDepth();
        _hashCode += getImageHeight();
        _hashCode += getImageType();
        _hashCode += getImageWidth();
        if (getMacAddress() != null) {
            _hashCode += getMacAddress().hashCode();
        }
        if (getMachineIP() != null) {
            _hashCode += getMachineIP().hashCode();
        }
        _hashCode += getNoOfFrames();
        _hashCode += getNoOfSlices();
        if (getParentRecord() != null) {
            _hashCode += getParentRecord().hashCode();
        }
        if (getProjectName() != null) {
            _hashCode += getProjectName().hashCode();
        }
        if (getRecordLabel() != null) {
            _hashCode += getRecordLabel().hashCode();
        }
        if (getSites() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSites());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSites(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSourceFilename() != null) {
            _hashCode += getSourceFilename().hashCode();
        }
        if (getSourceFolder() != null) {
            _hashCode += getSourceFolder().hashCode();
        }
        if (getSourceTime() != null) {
            _hashCode += getSourceTime().hashCode();
        }
        if (getSourceType() != null) {
            _hashCode += getSourceType().hashCode();
        }
        if (getUploadedBy() != null) {
            _hashCode += getUploadedBy().hashCode();
        }
        _hashCode += new Double(getXPixelSize()).hashCode();
        _hashCode += new Double(getYPixelSize()).hashCode();
        _hashCode += new Double(getZPixelSize()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RecordBuilderObject.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "RecordBuilderObject"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acquiredTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "acquiredTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("channels");
        elemField.setXmlName(new javax.xml.namespace.QName("", "channels"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Channel"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageDepth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageDepth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageType"));
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
        elemField.setFieldName("macAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "macAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("machineIP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "machineIP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfFrames");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noOfFrames"));
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
        elemField.setFieldName("parentRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parentRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "projectName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordLabel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recordLabel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sites");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sites"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "RecordSite"));
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
        elemField.setFieldName("sourceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadedBy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uploadedBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XPixelSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xPixelSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("YPixelSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "yPixelSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZPixelSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "zPixelSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
