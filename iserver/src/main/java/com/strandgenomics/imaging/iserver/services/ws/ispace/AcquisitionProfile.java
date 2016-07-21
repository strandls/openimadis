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

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class AcquisitionProfile  implements java.io.Serializable {
    private java.lang.String lengthUnit;

    private java.lang.String name;

    private java.lang.String profileName;

    private java.lang.String sourceFormat;

    private java.lang.String timeUnit;

    private java.lang.Double xPixelSize;

    private java.lang.String xType;

    private java.lang.Double yPixelSize;

    private java.lang.String yType;

    private java.lang.Double zPixelSize;

    private java.lang.String zType;

    public AcquisitionProfile() {
    }

    public AcquisitionProfile(
           java.lang.String lengthUnit,
           java.lang.String name,
           java.lang.String profileName,
           java.lang.String sourceFormat,
           java.lang.String timeUnit,
           java.lang.Double xPixelSize,
           java.lang.String xType,
           java.lang.Double yPixelSize,
           java.lang.String yType,
           java.lang.Double zPixelSize,
           java.lang.String zType) {
           this.lengthUnit = lengthUnit;
           this.name = name;
           this.profileName = profileName;
           this.sourceFormat = sourceFormat;
           this.timeUnit = timeUnit;
           this.xPixelSize = xPixelSize;
           this.xType = xType;
           this.yPixelSize = yPixelSize;
           this.yType = yType;
           this.zPixelSize = zPixelSize;
           this.zType = zType;
    }


    /**
     * Gets the lengthUnit value for this AcquisitionProfile.
     * 
     * @return lengthUnit
     */
    public java.lang.String getLengthUnit() {
        return lengthUnit;
    }


    /**
     * Sets the lengthUnit value for this AcquisitionProfile.
     * 
     * @param lengthUnit
     */
    public void setLengthUnit(java.lang.String lengthUnit) {
        this.lengthUnit = lengthUnit;
    }


    /**
     * Gets the name value for this AcquisitionProfile.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this AcquisitionProfile.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the profileName value for this AcquisitionProfile.
     * 
     * @return profileName
     */
    public java.lang.String getProfileName() {
        return profileName;
    }


    /**
     * Sets the profileName value for this AcquisitionProfile.
     * 
     * @param profileName
     */
    public void setProfileName(java.lang.String profileName) {
        this.profileName = profileName;
    }


    /**
     * Gets the sourceFormat value for this AcquisitionProfile.
     * 
     * @return sourceFormat
     */
    public java.lang.String getSourceFormat() {
        return sourceFormat;
    }


    /**
     * Sets the sourceFormat value for this AcquisitionProfile.
     * 
     * @param sourceFormat
     */
    public void setSourceFormat(java.lang.String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }


    /**
     * Gets the timeUnit value for this AcquisitionProfile.
     * 
     * @return timeUnit
     */
    public java.lang.String getTimeUnit() {
        return timeUnit;
    }


    /**
     * Sets the timeUnit value for this AcquisitionProfile.
     * 
     * @param timeUnit
     */
    public void setTimeUnit(java.lang.String timeUnit) {
        this.timeUnit = timeUnit;
    }


    /**
     * Gets the xPixelSize value for this AcquisitionProfile.
     * 
     * @return xPixelSize
     */
    public java.lang.Double getXPixelSize() {
        return xPixelSize;
    }


    /**
     * Sets the xPixelSize value for this AcquisitionProfile.
     * 
     * @param xPixelSize
     */
    public void setXPixelSize(java.lang.Double xPixelSize) {
        this.xPixelSize = xPixelSize;
    }


    /**
     * Gets the xType value for this AcquisitionProfile.
     * 
     * @return xType
     */
    public java.lang.String getXType() {
        return xType;
    }


    /**
     * Sets the xType value for this AcquisitionProfile.
     * 
     * @param xType
     */
    public void setXType(java.lang.String xType) {
        this.xType = xType;
    }


    /**
     * Gets the yPixelSize value for this AcquisitionProfile.
     * 
     * @return yPixelSize
     */
    public java.lang.Double getYPixelSize() {
        return yPixelSize;
    }


    /**
     * Sets the yPixelSize value for this AcquisitionProfile.
     * 
     * @param yPixelSize
     */
    public void setYPixelSize(java.lang.Double yPixelSize) {
        this.yPixelSize = yPixelSize;
    }


    /**
     * Gets the yType value for this AcquisitionProfile.
     * 
     * @return yType
     */
    public java.lang.String getYType() {
        return yType;
    }


    /**
     * Sets the yType value for this AcquisitionProfile.
     * 
     * @param yType
     */
    public void setYType(java.lang.String yType) {
        this.yType = yType;
    }


    /**
     * Gets the zPixelSize value for this AcquisitionProfile.
     * 
     * @return zPixelSize
     */
    public java.lang.Double getZPixelSize() {
        return zPixelSize;
    }


    /**
     * Sets the zPixelSize value for this AcquisitionProfile.
     * 
     * @param zPixelSize
     */
    public void setZPixelSize(java.lang.Double zPixelSize) {
        this.zPixelSize = zPixelSize;
    }


    /**
     * Gets the zType value for this AcquisitionProfile.
     * 
     * @return zType
     */
    public java.lang.String getZType() {
        return zType;
    }


    /**
     * Sets the zType value for this AcquisitionProfile.
     * 
     * @param zType
     */
    public void setZType(java.lang.String zType) {
        this.zType = zType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AcquisitionProfile)) return false;
        AcquisitionProfile other = (AcquisitionProfile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lengthUnit==null && other.getLengthUnit()==null) || 
             (this.lengthUnit!=null &&
              this.lengthUnit.equals(other.getLengthUnit()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.profileName==null && other.getProfileName()==null) || 
             (this.profileName!=null &&
              this.profileName.equals(other.getProfileName()))) &&
            ((this.sourceFormat==null && other.getSourceFormat()==null) || 
             (this.sourceFormat!=null &&
              this.sourceFormat.equals(other.getSourceFormat()))) &&
            ((this.timeUnit==null && other.getTimeUnit()==null) || 
             (this.timeUnit!=null &&
              this.timeUnit.equals(other.getTimeUnit()))) &&
            ((this.xPixelSize==null && other.getXPixelSize()==null) || 
             (this.xPixelSize!=null &&
              this.xPixelSize.equals(other.getXPixelSize()))) &&
            ((this.xType==null && other.getXType()==null) || 
             (this.xType!=null &&
              this.xType.equals(other.getXType()))) &&
            ((this.yPixelSize==null && other.getYPixelSize()==null) || 
             (this.yPixelSize!=null &&
              this.yPixelSize.equals(other.getYPixelSize()))) &&
            ((this.yType==null && other.getYType()==null) || 
             (this.yType!=null &&
              this.yType.equals(other.getYType()))) &&
            ((this.zPixelSize==null && other.getZPixelSize()==null) || 
             (this.zPixelSize!=null &&
              this.zPixelSize.equals(other.getZPixelSize()))) &&
            ((this.zType==null && other.getZType()==null) || 
             (this.zType!=null &&
              this.zType.equals(other.getZType())));
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
        if (getLengthUnit() != null) {
            _hashCode += getLengthUnit().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getProfileName() != null) {
            _hashCode += getProfileName().hashCode();
        }
        if (getSourceFormat() != null) {
            _hashCode += getSourceFormat().hashCode();
        }
        if (getTimeUnit() != null) {
            _hashCode += getTimeUnit().hashCode();
        }
        if (getXPixelSize() != null) {
            _hashCode += getXPixelSize().hashCode();
        }
        if (getXType() != null) {
            _hashCode += getXType().hashCode();
        }
        if (getYPixelSize() != null) {
            _hashCode += getYPixelSize().hashCode();
        }
        if (getYType() != null) {
            _hashCode += getYType().hashCode();
        }
        if (getZPixelSize() != null) {
            _hashCode += getZPixelSize().hashCode();
        }
        if (getZType() != null) {
            _hashCode += getZType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AcquisitionProfile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "AcquisitionProfile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lengthUnit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lengthUnit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("profileName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "profileName"));
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
        elemField.setFieldName("timeUnit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "timeUnit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XPixelSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xPixelSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("YPixelSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "yPixelSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("YType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "yType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZPixelSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "zPixelSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "double"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "zType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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
