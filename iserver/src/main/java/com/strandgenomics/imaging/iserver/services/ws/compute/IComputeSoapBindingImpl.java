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
 * IComputeSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 25, 2006 (02:39:47 GMT+05:30) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iserver.services.ws.compute;

public class IComputeSoapBindingImpl implements com.strandgenomics.imaging.iserver.services.ws.compute.ImageSpaceCompute{
    public com.strandgenomics.imaging.iserver.services.ws.compute.Publisher[] listPublishers(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.Application[] listApplications(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.Publisher[] getPublisher(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.Parameter[] getApplicationParameters(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public long executeApplication(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[] in4, long[] in5, int in6) throws java.rmi.RemoteException {
        return -3;
    }

    public long scheduleApplication(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[] in4, long[] in5, int in6, long in7) throws java.rmi.RemoteException {
        return -3;
    }

    public java.lang.String getJobState(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public boolean rescheduleTask(java.lang.String in0, long in1, long in2) throws java.rmi.RemoteException {
        return false;
    }

    public boolean pauseTask(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return false;
    }

    public boolean resumeTask(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return false;
    }

    public boolean removeTask(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return false;
    }

    public boolean terminateTask(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return false;
    }

    public int getTaskProgress(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return -3;
    }

    public void setTaskProgress(java.lang.String in0, long in1, int in2) throws java.rmi.RemoteException {
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.NVPair[] getTaskParameters(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public long[] getTaskInputs(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public long[] getTaskOutputs(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getTaskLogUploadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.DoubleListConstraints testMethod1(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.DoubleRangeConstraints testMethod2(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.LongListConstraints testMethod3(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.compute.LongRangeConstraints testMethod4(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

}
