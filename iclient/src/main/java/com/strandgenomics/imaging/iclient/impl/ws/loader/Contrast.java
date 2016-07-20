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
 * Contrast.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.loader;

public class Contrast  implements java.io.Serializable {
    private double gamma;

    private int maxIntensity;

    private int minIntensity;

    public Contrast() {
    }

    public Contrast(
           double gamma,
           int maxIntensity,
           int minIntensity) {
           this.gamma = gamma;
           this.maxIntensity = maxIntensity;
           this.minIntensity = minIntensity;
    }


    /**
     * Gets the gamma value for this Contrast.
     * 
     * @return gamma
     */
    public double getGamma() {
        return gamma;
    }


    /**
     * Sets the gamma value for this Contrast.
     * 
     * @param gamma
     */
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }


    /**
     * Gets the maxIntensity value for this Contrast.
     * 
     * @return maxIntensity
     */
    public int getMaxIntensity() {
        return maxIntensity;
    }


    /**
     * Sets the maxIntensity value for this Contrast.
     * 
     * @param maxIntensity
     */
    public void setMaxIntensity(int maxIntensity) {
        this.maxIntensity = maxIntensity;
    }


    /**
     * Gets the minIntensity value for this Contrast.
     * 
     * @return minIntensity
     */
    public int getMinIntensity() {
        return minIntensity;
    }


    /**
     * Sets the minIntensity value for this Contrast.
     * 
     * @param minIntensity
     */
    public void setMinIntensity(int minIntensity) {
        this.minIntensity = minIntensity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Contrast)) return false;
        Contrast other = (Contrast) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.gamma == other.getGamma() &&
            this.maxIntensity == other.getMaxIntensity() &&
            this.minIntensity == other.getMinIntensity();
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
        _hashCode += new Double(getGamma()).hashCode();
        _hashCode += getMaxIntensity();
        _hashCode += getMinIntensity();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Contrast.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Contrast"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gamma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gamma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxIntensity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxIntensity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minIntensity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "minIntensity"));
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
