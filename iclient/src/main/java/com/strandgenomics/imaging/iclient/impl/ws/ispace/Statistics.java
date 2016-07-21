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

package com.strandgenomics.imaging.iclient.impl.ws.ispace;

public class Statistics  implements java.io.Serializable {
    private int[] frequencies;

    private int[] intensities;

    private int maxFreq;

    private int maxValue;

    private int minValue;

    private int pixelDepth;

    public Statistics() {
    }

    public Statistics(
           int[] frequencies,
           int[] intensities,
           int maxFreq,
           int maxValue,
           int minValue,
           int pixelDepth) {
           this.frequencies = frequencies;
           this.intensities = intensities;
           this.maxFreq = maxFreq;
           this.maxValue = maxValue;
           this.minValue = minValue;
           this.pixelDepth = pixelDepth;
    }


    /**
     * Gets the frequencies value for this Statistics.
     * 
     * @return frequencies
     */
    public int[] getFrequencies() {
        return frequencies;
    }


    /**
     * Sets the frequencies value for this Statistics.
     * 
     * @param frequencies
     */
    public void setFrequencies(int[] frequencies) {
        this.frequencies = frequencies;
    }


    /**
     * Gets the intensities value for this Statistics.
     * 
     * @return intensities
     */
    public int[] getIntensities() {
        return intensities;
    }


    /**
     * Sets the intensities value for this Statistics.
     * 
     * @param intensities
     */
    public void setIntensities(int[] intensities) {
        this.intensities = intensities;
    }


    /**
     * Gets the maxFreq value for this Statistics.
     * 
     * @return maxFreq
     */
    public int getMaxFreq() {
        return maxFreq;
    }


    /**
     * Sets the maxFreq value for this Statistics.
     * 
     * @param maxFreq
     */
    public void setMaxFreq(int maxFreq) {
        this.maxFreq = maxFreq;
    }


    /**
     * Gets the maxValue value for this Statistics.
     * 
     * @return maxValue
     */
    public int getMaxValue() {
        return maxValue;
    }


    /**
     * Sets the maxValue value for this Statistics.
     * 
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


    /**
     * Gets the minValue value for this Statistics.
     * 
     * @return minValue
     */
    public int getMinValue() {
        return minValue;
    }


    /**
     * Sets the minValue value for this Statistics.
     * 
     * @param minValue
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }


    /**
     * Gets the pixelDepth value for this Statistics.
     * 
     * @return pixelDepth
     */
    public int getPixelDepth() {
        return pixelDepth;
    }


    /**
     * Sets the pixelDepth value for this Statistics.
     * 
     * @param pixelDepth
     */
    public void setPixelDepth(int pixelDepth) {
        this.pixelDepth = pixelDepth;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Statistics)) return false;
        Statistics other = (Statistics) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.frequencies==null && other.getFrequencies()==null) || 
             (this.frequencies!=null &&
              java.util.Arrays.equals(this.frequencies, other.getFrequencies()))) &&
            ((this.intensities==null && other.getIntensities()==null) || 
             (this.intensities!=null &&
              java.util.Arrays.equals(this.intensities, other.getIntensities()))) &&
            this.maxFreq == other.getMaxFreq() &&
            this.maxValue == other.getMaxValue() &&
            this.minValue == other.getMinValue() &&
            this.pixelDepth == other.getPixelDepth();
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
        if (getFrequencies() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFrequencies());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFrequencies(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIntensities() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIntensities());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIntensities(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getMaxFreq();
        _hashCode += getMaxValue();
        _hashCode += getMinValue();
        _hashCode += getPixelDepth();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Statistics.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "Statistics"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("frequencies");
        elemField.setXmlName(new javax.xml.namespace.QName("", "frequencies"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intensities");
        elemField.setXmlName(new javax.xml.namespace.QName("", "intensities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxFreq");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxFreq"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "minValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pixelDepth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pixelDepth"));
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
