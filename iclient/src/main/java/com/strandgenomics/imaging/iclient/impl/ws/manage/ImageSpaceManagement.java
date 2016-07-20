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
 * ImageSpaceManagement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.strandgenomics.imaging.iclient.impl.ws.manage;

public interface ImageSpaceManagement extends java.rmi.Remote {
    public void transfer(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public boolean createInternalUser(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4, java.lang.String in5) throws java.rmi.RemoteException;
    public boolean allowExternalUser(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.manage.Task restoreProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public boolean createNewProject(java.lang.String in0, java.lang.String in1, java.lang.String in2, double in3) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.manage.User[] getProjectMembers(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.manage.User[] getProjectManager(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.manage.Task archiveProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public void addProjectMembers(java.lang.String in0, java.lang.String in1, java.lang.String[] in2, java.lang.String in3) throws java.rmi.RemoteException;
    public void deleteArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public void deleteRecord(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.manage.Task deleteProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public boolean renameProject(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String getJobStatus(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.manage.Task in1) throws java.rmi.RemoteException;
}
