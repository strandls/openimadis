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

package com.strandgenomics.imaging.iserver.services.ws.update;

public interface ImageSpaceUpdate extends java.rmi.Remote {
    public void updateAttachmentNotes(java.lang.String in0, long in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public void deleteAttachment(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException;
    public void deleteTextObjects(java.lang.String in0, long in1, int[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[] in4) throws java.rmi.RemoteException;
    public java.lang.String getAttachmentUploadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException;
    public void deleteVisualOverlays(java.lang.String in0, long in1, int in2, java.lang.String in3) throws java.rmi.RemoteException;
    public void deleteVisualObjects(java.lang.String in0, long in1, int[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.update.VOIndex[] in4) throws java.rmi.RemoteException;
    public void updateRecordUserAnnotation(java.lang.String in0, long in1, java.lang.String in2, java.lang.Object in3) throws java.rmi.RemoteException;
}
