/**
 * FreehandShape.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class FreehandShape  extends com.strandgenomics.imaging.iserver.services.ws.ispace.Shape  implements java.io.Serializable {
    private java.lang.Float[] coordinates;

    public FreehandShape() {
    }

    public FreehandShape(
           int ID,
           int penColor,
           float penWidth,
           java.lang.String type,
           int zoomLevel,
           java.lang.Float[] coordinates) {
        super(
            ID,
            penColor,
            penWidth,
            type,
            zoomLevel);
        this.coordinates = coordinates;
    }


    /**
     * Gets the coordinates value for this FreehandShape.
     * 
     * @return coordinates
     */
    public java.lang.Float[] getCoordinates() {
        return coordinates;
    }


    /**
     * Sets the coordinates value for this FreehandShape.
     * 
     * @param coordinates
     */
    public void setCoordinates(java.lang.Float[] coordinates) {
        this.coordinates = coordinates;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FreehandShape)) return false;
        FreehandShape other = (FreehandShape) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.coordinates==null && other.getCoordinates()==null) || 
             (this.coordinates!=null &&
              java.util.Arrays.equals(this.coordinates, other.getCoordinates())));
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
        if (getCoordinates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCoordinates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCoordinates(), i);
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
        new org.apache.axis.description.TypeDesc(FreehandShape.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ispace", "FreehandShape"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordinates");
        elemField.setXmlName(new javax.xml.namespace.QName("", "coordinates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "float"));
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
