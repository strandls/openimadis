/**
 * Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.worker;

public class Response  extends com.strandgenomics.imaging.iclient.impl.ws.worker.Directive  implements java.io.Serializable {
    private com.strandgenomics.imaging.iclient.impl.ws.worker.Work work;

    public Response() {
    }

    public Response(
           java.lang.String directive,
           com.strandgenomics.imaging.iclient.impl.ws.worker.Work work) {
        super(
            directive);
        this.work = work;
    }


    /**
     * Gets the work value for this Response.
     * 
     * @return work
     */
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Work getWork() {
        return work;
    }


    /**
     * Sets the work value for this Response.
     * 
     * @param work
     */
    public void setWork(com.strandgenomics.imaging.iclient.impl.ws.worker.Work work) {
        this.work = work;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Response)) return false;
        Response other = (Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.work==null && other.getWork()==null) || 
             (this.work!=null &&
              this.work.equals(other.getWork())));
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
        if (getWork() != null) {
            _hashCode += getWork().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("work");
        elemField.setXmlName(new javax.xml.namespace.QName("", "work"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "Work"));
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
