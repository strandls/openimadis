/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * StraightLine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class StraightLine  extends com.strandgenomics.imaging.iserver.services.ws.ispace.Shape  implements java.io.Serializable {
    private double endX;

    private double endY;

    private double startX;

    private double startY;

    public StraightLine() {
    }

    public StraightLine(
           int ID,
           int penColor,
           float penWidth,
           java.lang.String type,
           int zoomLevel,
           double endX,
           double endY,
           double startX,
           double startY) {
        super(
            ID,
            penColor,
            penWidth,
            type,
            zoomLevel);
        this.endX = endX;
        this.endY = endY;
        this.startX = startX;
        this.startY = startY;
    }


    /**
     * Gets the endX value for this StraightLine.
     * 
     * @return endX
     */
    public double getEndX() {
        return endX;
    }


    /**
     * Sets the endX value for this StraightLine.
     * 
     * @param endX
     */
    public void setEndX(double endX) {
        this.endX = endX;
    }


    /**
     * Gets the endY value for this StraightLine.
     * 
     * @return endY
     */
    public double getEndY() {
        return endY;
    }


    /**
     * Sets the endY value for this StraightLine.
     * 
     * @param endY
     */
    public void setEndY(double endY) {
        this.endY = endY;
    }


    /**
     * Gets the startX value for this StraightLine.
     * 
     * @return startX
     */
    public double getStartX() {
        return startX;
    }


    /**
     * Sets the startX value for this StraightLine.
     * 
     * @param startX
     */
    public void setStartX(double startX) {
        this.startX = startX;
    }


    /**
     * Gets the startY value for this StraightLine.
     * 
     * @return startY
     */
    public double getStartY() {
        return startY;
    }


    /**
     * Sets the startY value for this StraightLine.
     * 
     * @param startY
     */
    public void setStartY(double startY) {
        this.startY = startY;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StraightLine)) return false;
        StraightLine other = (StraightLine) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.endX == other.getEndX() &&
            this.endY == other.getEndY() &&
            this.startX == other.getStartX() &&
            this.startY == other.getStartY();
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
        _hashCode += new Double(getEndX()).hashCode();
        _hashCode += new Double(getEndY()).hashCode();
        _hashCode += new Double(getStartX()).hashCode();
        _hashCode += new Double(getStartY()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StraightLine.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "StraightLine"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endX");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endX"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endY");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endY"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startX");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startX"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startY");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startY"));
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
