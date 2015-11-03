/**
 * Application.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.worker;

public class Application  implements java.io.Serializable {
    private java.lang.String categoryName;

    private java.lang.String clientID;

    private java.lang.String description;

    private java.lang.String name;

    private com.strandgenomics.imaging.iclient.impl.ws.worker.Parameter[] parameters;

    private java.lang.String version;

    public Application() {
    }

    public Application(
           java.lang.String categoryName,
           java.lang.String clientID,
           java.lang.String description,
           java.lang.String name,
           com.strandgenomics.imaging.iclient.impl.ws.worker.Parameter[] parameters,
           java.lang.String version) {
           this.categoryName = categoryName;
           this.clientID = clientID;
           this.description = description;
           this.name = name;
           this.parameters = parameters;
           this.version = version;
    }


    /**
     * Gets the categoryName value for this Application.
     * 
     * @return categoryName
     */
    public java.lang.String getCategoryName() {
        return categoryName;
    }


    /**
     * Sets the categoryName value for this Application.
     * 
     * @param categoryName
     */
    public void setCategoryName(java.lang.String categoryName) {
        this.categoryName = categoryName;
    }


    /**
     * Gets the clientID value for this Application.
     * 
     * @return clientID
     */
    public java.lang.String getClientID() {
        return clientID;
    }


    /**
     * Sets the clientID value for this Application.
     * 
     * @param clientID
     */
    public void setClientID(java.lang.String clientID) {
        this.clientID = clientID;
    }


    /**
     * Gets the description value for this Application.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Application.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the name value for this Application.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Application.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the parameters value for this Application.
     * 
     * @return parameters
     */
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Parameter[] getParameters() {
        return parameters;
    }


    /**
     * Sets the parameters value for this Application.
     * 
     * @param parameters
     */
    public void setParameters(com.strandgenomics.imaging.iclient.impl.ws.worker.Parameter[] parameters) {
        this.parameters = parameters;
    }


    /**
     * Gets the version value for this Application.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this Application.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Application)) return false;
        Application other = (Application) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.categoryName==null && other.getCategoryName()==null) || 
             (this.categoryName!=null &&
              this.categoryName.equals(other.getCategoryName()))) &&
            ((this.clientID==null && other.getClientID()==null) || 
             (this.clientID!=null &&
              this.clientID.equals(other.getClientID()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.parameters==null && other.getParameters()==null) || 
             (this.parameters!=null &&
              java.util.Arrays.equals(this.parameters, other.getParameters()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion())));
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
        if (getCategoryName() != null) {
            _hashCode += getCategoryName().hashCode();
        }
        if (getClientID() != null) {
            _hashCode += getClientID().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getParameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getParameters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getParameters(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Application.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "Application"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categoryName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clientID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
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
        elemField.setFieldName("parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "Parameter"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
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
