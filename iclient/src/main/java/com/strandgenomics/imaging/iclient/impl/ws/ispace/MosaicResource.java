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
 * MosaicResource.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.ispace;

public class MosaicResource  implements java.io.Serializable {
    private int anchor_left;

    private int anchor_top;

    private int mosaicImageWidth;

    private int mosiacImageHeight;

    private long[] recordids;

    public MosaicResource() {
    }

    public MosaicResource(
           int anchor_left,
           int anchor_top,
           int mosaicImageWidth,
           int mosiacImageHeight,
           long[] recordids) {
           this.anchor_left = anchor_left;
           this.anchor_top = anchor_top;
           this.mosaicImageWidth = mosaicImageWidth;
           this.mosiacImageHeight = mosiacImageHeight;
           this.recordids = recordids;
    }


    /**
     * Gets the anchor_left value for this MosaicResource.
     * 
     * @return anchor_left
     */
    public int getAnchor_left() {
        return anchor_left;
    }


    /**
     * Sets the anchor_left value for this MosaicResource.
     * 
     * @param anchor_left
     */
    public void setAnchor_left(int anchor_left) {
        this.anchor_left = anchor_left;
    }


    /**
     * Gets the anchor_top value for this MosaicResource.
     * 
     * @return anchor_top
     */
    public int getAnchor_top() {
        return anchor_top;
    }


    /**
     * Sets the anchor_top value for this MosaicResource.
     * 
     * @param anchor_top
     */
    public void setAnchor_top(int anchor_top) {
        this.anchor_top = anchor_top;
    }


    /**
     * Gets the mosaicImageWidth value for this MosaicResource.
     * 
     * @return mosaicImageWidth
     */
    public int getMosaicImageWidth() {
        return mosaicImageWidth;
    }


    /**
     * Sets the mosaicImageWidth value for this MosaicResource.
     * 
     * @param mosaicImageWidth
     */
    public void setMosaicImageWidth(int mosaicImageWidth) {
        this.mosaicImageWidth = mosaicImageWidth;
    }


    /**
     * Gets the mosiacImageHeight value for this MosaicResource.
     * 
     * @return mosiacImageHeight
     */
    public int getMosiacImageHeight() {
        return mosiacImageHeight;
    }


    /**
     * Sets the mosiacImageHeight value for this MosaicResource.
     * 
     * @param mosiacImageHeight
     */
    public void setMosiacImageHeight(int mosiacImageHeight) {
        this.mosiacImageHeight = mosiacImageHeight;
    }


    /**
     * Gets the recordids value for this MosaicResource.
     * 
     * @return recordids
     */
    public long[] getRecordids() {
        return recordids;
    }


    /**
     * Sets the recordids value for this MosaicResource.
     * 
     * @param recordids
     */
    public void setRecordids(long[] recordids) {
        this.recordids = recordids;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MosaicResource)) return false;
        MosaicResource other = (MosaicResource) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.anchor_left == other.getAnchor_left() &&
            this.anchor_top == other.getAnchor_top() &&
            this.mosaicImageWidth == other.getMosaicImageWidth() &&
            this.mosiacImageHeight == other.getMosiacImageHeight() &&
            ((this.recordids==null && other.getRecordids()==null) || 
             (this.recordids!=null &&
              java.util.Arrays.equals(this.recordids, other.getRecordids())));
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
        _hashCode += getAnchor_left();
        _hashCode += getAnchor_top();
        _hashCode += getMosaicImageWidth();
        _hashCode += getMosiacImageHeight();
        if (getRecordids() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRecordids());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRecordids(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MosaicResource.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "MosaicResource"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anchor_left");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anchor_left"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anchor_top");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anchor_top"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mosaicImageWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mosaicImageWidth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mosiacImageHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mosiacImageHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordids");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recordids"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
