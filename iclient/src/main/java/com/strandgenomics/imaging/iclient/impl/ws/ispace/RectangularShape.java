/**
 * RectangularShape.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.ispace;

public class RectangularShape  extends com.strandgenomics.imaging.iclient.impl.ws.ispace.Shape  implements java.io.Serializable {
    private double height;

    private double rotation;

    private double width;

    private double x;

    private double y;

    public RectangularShape() {
    }

    public RectangularShape(
           int ID,
           int penColor,
           float penWidth,
           java.lang.String type,
           int zoomLevel,
           double height,
           double rotation,
           double width,
           double x,
           double y) {
        super(
            ID,
            penColor,
            penWidth,
            type,
            zoomLevel);
        this.height = height;
        this.rotation = rotation;
        this.width = width;
        this.x = x;
        this.y = y;
    }


    /**
     * Gets the height value for this RectangularShape.
     * 
     * @return height
     */
    public double getHeight() {
        return height;
    }


    /**
     * Sets the height value for this RectangularShape.
     * 
     * @param height
     */
    public void setHeight(double height) {
        this.height = height;
    }


    /**
     * Gets the rotation value for this RectangularShape.
     * 
     * @return rotation
     */
    public double getRotation() {
        return rotation;
    }


    /**
     * Sets the rotation value for this RectangularShape.
     * 
     * @param rotation
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }


    /**
     * Gets the width value for this RectangularShape.
     * 
     * @return width
     */
    public double getWidth() {
        return width;
    }


    /**
     * Sets the width value for this RectangularShape.
     * 
     * @param width
     */
    public void setWidth(double width) {
        this.width = width;
    }


    /**
     * Gets the x value for this RectangularShape.
     * 
     * @return x
     */
    public double getX() {
        return x;
    }


    /**
     * Sets the x value for this RectangularShape.
     * 
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }


    /**
     * Gets the y value for this RectangularShape.
     * 
     * @return y
     */
    public double getY() {
        return y;
    }


    /**
     * Sets the y value for this RectangularShape.
     * 
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RectangularShape)) return false;
        RectangularShape other = (RectangularShape) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.height == other.getHeight() &&
            this.rotation == other.getRotation() &&
            this.width == other.getWidth() &&
            this.x == other.getX() &&
            this.y == other.getY();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        _hashCode += new Double(getHeight()).hashCode();
        _hashCode += new Double(getRotation()).hashCode();
        _hashCode += new Double(getWidth()).hashCode();
        _hashCode += new Double(getX()).hashCode();
        _hashCode += new Double(getY()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RectangularShape.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "RectangularShape"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("height");
        elemField.setXmlName(new javax.xml.namespace.QName("", "height"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rotation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rotation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("width");
        elemField.setXmlName(new javax.xml.namespace.QName("", "width"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
