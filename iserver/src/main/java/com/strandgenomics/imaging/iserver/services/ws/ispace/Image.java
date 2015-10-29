/**
 * Image.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class Image  implements java.io.Serializable {
    private double elapsedTime;

    private double exposureTime;

    private com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex index;

    private long timeStamp;

    private double x;

    private double y;

    private double z;

    public Image() {
    }

    public Image(
           double elapsedTime,
           double exposureTime,
           com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex index,
           long timeStamp,
           double x,
           double y,
           double z) {
           this.elapsedTime = elapsedTime;
           this.exposureTime = exposureTime;
           this.index = index;
           this.timeStamp = timeStamp;
           this.x = x;
           this.y = y;
           this.z = z;
    }


    /**
     * Gets the elapsedTime value for this Image.
     * 
     * @return elapsedTime
     */
    public double getElapsedTime() {
        return elapsedTime;
    }


    /**
     * Sets the elapsedTime value for this Image.
     * 
     * @param elapsedTime
     */
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }


    /**
     * Gets the exposureTime value for this Image.
     * 
     * @return exposureTime
     */
    public double getExposureTime() {
        return exposureTime;
    }


    /**
     * Sets the exposureTime value for this Image.
     * 
     * @param exposureTime
     */
    public void setExposureTime(double exposureTime) {
        this.exposureTime = exposureTime;
    }


    /**
     * Gets the index value for this Image.
     * 
     * @return index
     */
    public com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex getIndex() {
        return index;
    }


    /**
     * Sets the index value for this Image.
     * 
     * @param index
     */
    public void setIndex(com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex index) {
        this.index = index;
    }


    /**
     * Gets the timeStamp value for this Image.
     * 
     * @return timeStamp
     */
    public long getTimeStamp() {
        return timeStamp;
    }


    /**
     * Sets the timeStamp value for this Image.
     * 
     * @param timeStamp
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    /**
     * Gets the x value for this Image.
     * 
     * @return x
     */
    public double getX() {
        return x;
    }


    /**
     * Sets the x value for this Image.
     * 
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }


    /**
     * Gets the y value for this Image.
     * 
     * @return y
     */
    public double getY() {
        return y;
    }


    /**
     * Sets the y value for this Image.
     * 
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }


    /**
     * Gets the z value for this Image.
     * 
     * @return z
     */
    public double getZ() {
        return z;
    }


    /**
     * Sets the z value for this Image.
     * 
     * @param z
     */
    public void setZ(double z) {
        this.z = z;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Image)) return false;
        Image other = (Image) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.elapsedTime == other.getElapsedTime() &&
            this.exposureTime == other.getExposureTime() &&
            ((this.index==null && other.getIndex()==null) || 
             (this.index!=null &&
              this.index.equals(other.getIndex()))) &&
            this.timeStamp == other.getTimeStamp() &&
            this.x == other.getX() &&
            this.y == other.getY() &&
            this.z == other.getZ();
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
        _hashCode += new Double(getElapsedTime()).hashCode();
        _hashCode += new Double(getExposureTime()).hashCode();
        if (getIndex() != null) {
            _hashCode += getIndex().hashCode();
        }
        _hashCode += new Long(getTimeStamp()).hashCode();
        _hashCode += new Double(getX()).hashCode();
        _hashCode += new Double(getY()).hashCode();
        _hashCode += new Double(getZ()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Image.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "Image"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("elapsedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "elapsedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exposureTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "exposureTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("index");
        elemField.setXmlName(new javax.xml.namespace.QName("", "index"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:ispace", "ImageIndex"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "timeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x");
        elemField.setXmlName(new javax.xml.namespace.QName("", "x"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("y");
        elemField.setXmlName(new javax.xml.namespace.QName("", "y"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("z");
        elemField.setXmlName(new javax.xml.namespace.QName("", "z"));
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
