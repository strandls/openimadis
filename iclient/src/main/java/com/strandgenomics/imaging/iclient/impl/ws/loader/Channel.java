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

package com.strandgenomics.imaging.iclient.impl.ws.loader;

public class Channel  implements java.io.Serializable {
    private com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast ZStackedContrast;

    private com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast contrast;

    private java.lang.String lutName;

    private java.lang.String name;

    private long revision;

    private int wavelength;

    public Channel() {
    }

    public Channel(
           com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast ZStackedContrast,
           com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast contrast,
           java.lang.String lutName,
           java.lang.String name,
           long revision,
           int wavelength) {
           this.ZStackedContrast = ZStackedContrast;
           this.contrast = contrast;
           this.lutName = lutName;
           this.name = name;
           this.revision = revision;
           this.wavelength = wavelength;
    }


    /**
     * Gets the ZStackedContrast value for this Channel.
     * 
     * @return ZStackedContrast
     */
    public com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast getZStackedContrast() {
        return ZStackedContrast;
    }


    /**
     * Sets the ZStackedContrast value for this Channel.
     * 
     * @param ZStackedContrast
     */
    public void setZStackedContrast(com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast ZStackedContrast) {
        this.ZStackedContrast = ZStackedContrast;
    }


    /**
     * Gets the contrast value for this Channel.
     * 
     * @return contrast
     */
    public com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast getContrast() {
        return contrast;
    }


    /**
     * Sets the contrast value for this Channel.
     * 
     * @param contrast
     */
    public void setContrast(com.strandgenomics.imaging.iclient.impl.ws.loader.Contrast contrast) {
        this.contrast = contrast;
    }


    /**
     * Gets the lutName value for this Channel.
     * 
     * @return lutName
     */
    public java.lang.String getLutName() {
        return lutName;
    }


    /**
     * Sets the lutName value for this Channel.
     * 
     * @param lutName
     */
    public void setLutName(java.lang.String lutName) {
        this.lutName = lutName;
    }


    /**
     * Gets the name value for this Channel.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Channel.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the revision value for this Channel.
     * 
     * @return revision
     */
    public long getRevision() {
        return revision;
    }


    /**
     * Sets the revision value for this Channel.
     * 
     * @param revision
     */
    public void setRevision(long revision) {
        this.revision = revision;
    }


    /**
     * Gets the wavelength value for this Channel.
     * 
     * @return wavelength
     */
    public int getWavelength() {
        return wavelength;
    }


    /**
     * Sets the wavelength value for this Channel.
     * 
     * @param wavelength
     */
    public void setWavelength(int wavelength) {
        this.wavelength = wavelength;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Channel)) return false;
        Channel other = (Channel) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ZStackedContrast==null && other.getZStackedContrast()==null) || 
             (this.ZStackedContrast!=null &&
              this.ZStackedContrast.equals(other.getZStackedContrast()))) &&
            ((this.contrast==null && other.getContrast()==null) || 
             (this.contrast!=null &&
              this.contrast.equals(other.getContrast()))) &&
            ((this.lutName==null && other.getLutName()==null) || 
             (this.lutName!=null &&
              this.lutName.equals(other.getLutName()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            this.revision == other.getRevision() &&
            this.wavelength == other.getWavelength();
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
        if (getZStackedContrast() != null) {
            _hashCode += getZStackedContrast().hashCode();
        }
        if (getContrast() != null) {
            _hashCode += getContrast().hashCode();
        }
        if (getLutName() != null) {
            _hashCode += getLutName().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        _hashCode += new Long(getRevision()).hashCode();
        _hashCode += getWavelength();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Channel.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Channel"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZStackedContrast");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ZStackedContrast"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Contrast"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contrast");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contrast"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Contrast"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lutName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lutName"));
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
        elemField.setFieldName("revision");
        elemField.setXmlName(new javax.xml.namespace.QName("", "revision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wavelength");
        elemField.setXmlName(new javax.xml.namespace.QName("", "wavelength"));
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
