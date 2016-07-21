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

public class Archive  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String rootDirectory;

    private java.lang.String signature;

    private com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile[] sourceFiles;

    public Archive() {
    }

    public Archive(
           java.lang.String name,
           java.lang.String rootDirectory,
           java.lang.String signature,
           com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile[] sourceFiles) {
           this.name = name;
           this.rootDirectory = rootDirectory;
           this.signature = signature;
           this.sourceFiles = sourceFiles;
    }


    /**
     * Gets the name value for this Archive.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Archive.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the rootDirectory value for this Archive.
     * 
     * @return rootDirectory
     */
    public java.lang.String getRootDirectory() {
        return rootDirectory;
    }


    /**
     * Sets the rootDirectory value for this Archive.
     * 
     * @param rootDirectory
     */
    public void setRootDirectory(java.lang.String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }


    /**
     * Gets the signature value for this Archive.
     * 
     * @return signature
     */
    public java.lang.String getSignature() {
        return signature;
    }


    /**
     * Sets the signature value for this Archive.
     * 
     * @param signature
     */
    public void setSignature(java.lang.String signature) {
        this.signature = signature;
    }


    /**
     * Gets the sourceFiles value for this Archive.
     * 
     * @return sourceFiles
     */
    public com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile[] getSourceFiles() {
        return sourceFiles;
    }


    /**
     * Sets the sourceFiles value for this Archive.
     * 
     * @param sourceFiles
     */
    public void setSourceFiles(com.strandgenomics.imaging.iclient.impl.ws.loader.SourceFile[] sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Archive)) return false;
        Archive other = (Archive) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.rootDirectory==null && other.getRootDirectory()==null) || 
             (this.rootDirectory!=null &&
              this.rootDirectory.equals(other.getRootDirectory()))) &&
            ((this.signature==null && other.getSignature()==null) || 
             (this.signature!=null &&
              this.signature.equals(other.getSignature()))) &&
            ((this.sourceFiles==null && other.getSourceFiles()==null) || 
             (this.sourceFiles!=null &&
              java.util.Arrays.equals(this.sourceFiles, other.getSourceFiles())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getRootDirectory() != null) {
            _hashCode += getRootDirectory().hashCode();
        }
        if (getSignature() != null) {
            _hashCode += getSignature().hashCode();
        }
        if (getSourceFiles() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSourceFiles());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSourceFiles(), i);
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
        new org.apache.axis.description.TypeDesc(Archive.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iloader", "Archive"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rootDirectory");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rootDirectory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signature");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceFiles");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceFiles"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iloader", "SourceFile"));
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
