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

public interface ImageSpaceLoader extends java.rmi.Remote {
    public java.lang.String getArchiveDownloadURL(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.Long registerRecordBuilder(java.lang.String in0, com.strandgenomics.imaging.iclient.impl.ws.loader.RecordBuilderObject in1) throws java.rmi.RemoteException;
    public void commitRecordCreation(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.loader.UploadTicket recordCreationRequest(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iclient.impl.ws.loader.CreationRequest in2) throws java.rmi.RemoteException;
    public java.lang.String getRecordDownloadURL(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.loader.UploadTicket directUploadCreationRequest(java.lang.String in0, java.lang.String in1, com.strandgenomics.imaging.iclient.impl.ws.loader.CreationRequest in2) throws java.rmi.RemoteException;
    public void abortRecordCreation(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public java.lang.String getTicketStatus(java.lang.String in0, long in1) throws java.rmi.RemoteException;
    public com.strandgenomics.imaging.iclient.impl.ws.loader.Archive findArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String addImageData(java.lang.String in0, long in1, com.strandgenomics.imaging.iclient.impl.ws.loader.ImageIndex in2, com.strandgenomics.imaging.iclient.impl.ws.loader.Image in3) throws java.rmi.RemoteException;
}
