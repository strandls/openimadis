/**
 * Shape.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.ispace;

public abstract class Shape  implements java.io.Serializable {
    private int ID;

    private int penColor;

    private float penWidth;

    private java.lang.String type;

    private int zoomLevel;

    public Shape() {
    }

    public Shape(
           int ID,
           int penColor,
           float penWidth,
           java.lang.String type,
           int zoomLevel) {
           this.ID = ID;
           this.penColor = penColor;
           this.penWidth = penWidth;
           this.type = type;
           this.zoomLevel = zoomLevel;
    }


    /**
     * Gets the ID value for this Shape.
     * 
     * @return ID
     */
    public int getID() {
        return ID;
    }


    /**
     * Sets the ID value for this Shape.
     * 
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }


    /**
     * Gets the penColor value for this Shape.
     * 
     * @return penColor
     */
    public int getPenColor() {
        return penColor;
    }


    /**
     * Sets the penColor value for this Shape.
     * 
     * @param penColor
     */
    public void setPenColor(int penColor) {
        this.penColor = penColor;
    }


    /**
     * Gets the penWidth value for this Shape.
     * 
     * @return penWidth
     */
    public float getPenWidth() {
        return penWidth;
    }


    /**
     * Sets the penWidth value for this Shape.
     * 
     * @param penWidth
     */
    public void setPenWidth(float penWidth) {
        this.penWidth = penWidth;
    }


    /**
     * Gets the type value for this Shape.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this Shape.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the zoomLevel value for this Shape.
     * 
     * @return zoomLevel
     */
    public int getZoomLevel() {
        return zoomLevel;
    }


    /**
     * Sets the zoomLevel value for this Shape.
     * 
     * @param zoomLevel
     */
    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Shape)) return false;
        Shape other = (Shape) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ID == other.getID() &&
            this.penColor == other.getPenColor() &&
            this.penWidth == other.getPenWidth() &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            this.zoomLevel == other.getZoomLevel();
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
        _hashCode += getID();
        _hashCode += getPenColor();
        _hashCode += new Float(getPenWidth()).hashCode();
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        _hashCode += getZoomLevel();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Shape.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "Shape"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("penColor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "penColor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("penWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "penWidth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zoomLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "zoomLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
