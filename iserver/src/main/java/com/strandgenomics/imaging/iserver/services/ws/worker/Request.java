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

package com.strandgenomics.imaging.iserver.services.ws.worker;

public class Request  implements java.io.Serializable {
    private com.strandgenomics.imaging.iserver.services.ws.worker.WorkStatus[] activeJobs;

    private java.lang.String workerState;

    public Request() {
    }

    public Request(
           com.strandgenomics.imaging.iserver.services.ws.worker.WorkStatus[] activeJobs,
           java.lang.String workerState) {
           this.activeJobs = activeJobs;
           this.workerState = workerState;
    }


    /**
     * Gets the activeJobs value for this Request.
     * 
     * @return activeJobs
     */
    public com.strandgenomics.imaging.iserver.services.ws.worker.WorkStatus[] getActiveJobs() {
        return activeJobs;
    }


    /**
     * Sets the activeJobs value for this Request.
     * 
     * @param activeJobs
     */
    public void setActiveJobs(com.strandgenomics.imaging.iserver.services.ws.worker.WorkStatus[] activeJobs) {
        this.activeJobs = activeJobs;
    }


    /**
     * Gets the workerState value for this Request.
     * 
     * @return workerState
     */
    public java.lang.String getWorkerState() {
        return workerState;
    }


    /**
     * Sets the workerState value for this Request.
     * 
     * @param workerState
     */
    public void setWorkerState(java.lang.String workerState) {
        this.workerState = workerState;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Request)) return false;
        Request other = (Request) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.activeJobs==null && other.getActiveJobs()==null) || 
             (this.activeJobs!=null &&
              java.util.Arrays.equals(this.activeJobs, other.getActiveJobs()))) &&
            ((this.workerState==null && other.getWorkerState()==null) || 
             (this.workerState!=null &&
              this.workerState.equals(other.getWorkerState())));
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
        if (getActiveJobs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActiveJobs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActiveJobs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWorkerState() != null) {
            _hashCode += getWorkerState().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Request.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "Request"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeJobs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "activeJobs"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:iworkers", "WorkStatus"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workerState");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workerState"));
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
