/**
 * Work.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.worker;

public class Work  implements java.io.Serializable {
    private java.lang.String appAuthCode;

    private java.lang.String appName;

    private long[] inputRecords;

    private com.strandgenomics.imaging.iclient.impl.ws.worker.NVPair[] parameters;

    private long taskID;

    private java.lang.String version;

    public Work() {
    }

    public Work(
           java.lang.String appAuthCode,
           java.lang.String appName,
           long[] inputRecords,
           com.strandgenomics.imaging.iclient.impl.ws.worker.NVPair[] parameters,
           long taskID,
           java.lang.String version) {
           this.appAuthCode = appAuthCode;
           this.appName = appName;
           this.inputRecords = inputRecords;
           this.parameters = parameters;
           this.taskID = taskID;
           this.version = version;
    }


    /**
     * Gets the appAuthCode value for this Work.
     * 
     * @return appAuthCode
     */
    public java.lang.String getAppAuthCode() {
        return appAuthCode;
    }


    /**
     * Sets the appAuthCode value for this Work.
     * 
     * @param appAuthCode
     */
    public void setAppAuthCode(java.lang.String appAuthCode) {
        this.appAuthCode = appAuthCode;
    }


    /**
     * Gets the appName value for this Work.
     * 
     * @return appName
     */
    public java.lang.String getAppName() {
        return appName;
    }


    /**
     * Sets the appName value for this Work.
     * 
     * @param appName
     */
    public void setAppName(java.lang.String appName) {
        this.appName = appName;
    }


    /**
     * Gets the inputRecords value for this Work.
     * 
     * @return inputRecords
     */
    public long[] getInputRecords() {
        return inputRecords;
    }


    /**
     * Sets the inputRecords value for this Work.
     * 
     * @param inputRecords
     */
    public void setInputRecords(long[] inputRecords) {
        this.inputRecords = inputRecords;
    }


    /**
     * Gets the parameters value for this Work.
     * 
     * @return parameters
     */
    public com.strandgenomics.imaging.iclient.impl.ws.worker.NVPair[] getParameters() {
        return parameters;
    }


    /**
     * Sets the parameters value for this Work.
     * 
     * @param parameters
     */
    public void setParameters(com.strandgenomics.imaging.iclient.impl.ws.worker.NVPair[] parameters) {
        this.parameters = parameters;
    }


    /**
     * Gets the taskID value for this Work.
     * 
     * @return taskID
     */
    public long getTaskID() {
        return taskID;
    }


    /**
     * Sets the taskID value for this Work.
     * 
     * @param taskID
     */
    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }


    /**
     * Gets the version value for this Work.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this Work.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Work)) return false;
        Work other = (Work) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.appAuthCode==null && other.getAppAuthCode()==null) || 
             (this.appAuthCode!=null &&
              this.appAuthCode.equals(other.getAppAuthCode()))) &&
            ((this.appName==null && other.getAppName()==null) || 
             (this.appName!=null &&
              this.appName.equals(other.getAppName()))) &&
            ((this.inputRecords==null && other.getInputRecords()==null) || 
             (this.inputRecords!=null &&
              java.util.Arrays.equals(this.inputRecords, other.getInputRecords()))) &&
            ((this.parameters==null && other.getParameters()==null) || 
             (this.parameters!=null &&
              java.util.Arrays.equals(this.parameters, other.getParameters()))) &&
            this.taskID == other.getTaskID() &&
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
        if (getAppAuthCode() != null) {
            _hashCode += getAppAuthCode().hashCode();
        }
        if (getAppName() != null) {
            _hashCode += getAppName().hashCode();
        }
        if (getInputRecords() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInputRecords());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInputRecords(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        _hashCode += new Long(getTaskID()).hashCode();
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Work.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "Work"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appAuthCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "appAuthCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "appName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputRecords");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputRecords"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "NVPair"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taskID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
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
