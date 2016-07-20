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
 * LongRangeConstraints.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.compute;

public class LongRangeConstraints  extends com.strandgenomics.imaging.iclient.impl.ws.compute.Constraints  implements java.io.Serializable {
    private long lowerLimit;

    private long upperLimit;

    public LongRangeConstraints() {
    }

    public LongRangeConstraints(
           long lowerLimit,
           long upperLimit) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }


    /**
     * Gets the lowerLimit value for this LongRangeConstraints.
     * 
     * @return lowerLimit
     */
    public long getLowerLimit() {
        return lowerLimit;
    }


    /**
     * Sets the lowerLimit value for this LongRangeConstraints.
     * 
     * @param lowerLimit
     */
    public void setLowerLimit(long lowerLimit) {
        this.lowerLimit = lowerLimit;
    }


    /**
     * Gets the upperLimit value for this LongRangeConstraints.
     * 
     * @return upperLimit
     */
    public long getUpperLimit() {
        return upperLimit;
    }


    /**
     * Sets the upperLimit value for this LongRangeConstraints.
     * 
     * @param upperLimit
     */
    public void setUpperLimit(long upperLimit) {
        this.upperLimit = upperLimit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LongRangeConstraints)) return false;
        LongRangeConstraints other = (LongRangeConstraints) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.lowerLimit == other.getLowerLimit() &&
            this.upperLimit == other.getUpperLimit();
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
        _hashCode += new Long(getLowerLimit()).hashCode();
        _hashCode += new Long(getUpperLimit()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LongRangeConstraints.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:icompute", "LongRangeConstraints"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lowerLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lowerLimit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("upperLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "upperLimit"));
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
