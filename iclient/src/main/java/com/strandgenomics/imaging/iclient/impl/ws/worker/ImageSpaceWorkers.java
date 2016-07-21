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

package com.strandgenomics.imaging.iclient.impl.ws.worker;

public interface ImageSpaceWorkers extends java.rmi.Remote {
    public java.lang.String register(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Directive removeApplications(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.worker.Application[] in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Directive publishApplications(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.worker.Application[] in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.DoubleListConstraints testMethod1(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.LongListConstraints testMethod3(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.DoubleRangeConstraints testMethod2(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.LongRangeConstraints testMethod4(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.StringListConstraints testMethod0(java.lang.String in0) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.worker.Response ping(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.worker.Request in1) throws java.rmi.RemoteException;
}
