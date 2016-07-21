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

package com.strandgenomics.imaging.iserver.services.ws.ispace;

public class ISpaceSoapBindingImpl implements com.strandgenomics.imaging.iserver.services.ws.ispace.ImageSpaceService{
    public java.lang.String[] listArchives(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String[] getActiveProjects(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String[] getArchivedProjects(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String createRecordAttachment(java.lang.String in0, long in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getAttachmentDownloadURL(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.RecordAttachment[] getRecordAttachments(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Comments[] fetchUserComment(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] getDynamicMetaData(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] getRecordUserAnnotations(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public void addRecordUserAnnotation(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] in2) throws java.rmi.RemoteException {
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Property[] getImageMetaData(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.HistoryItem[] getRecordHistory(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public void addRecordHistory(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException {
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay[] getVisualOverlays(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay getVisualOverlay(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String[] getAvailableVisualOverlays(java.lang.String in0, long in1, int in2) throws java.rmi.RemoteException {
        return null;
    }

    public void createVisualOverlays(java.lang.String in0, long in1, int in2, java.lang.String in3) throws java.rmi.RemoteException {
    }

    public void addVisualObjects(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] in4) throws java.rmi.RemoteException {
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] getVisualObjects(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.EllipticalShape[] getEllipticalShapes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.StraightLine[] getLineSegments(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.RectangularShape[] getRectangularShapes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.TextArea[] getTextBoxes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.FreehandShape[] getFreeHandShapes(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] findOverlayLocation(java.lang.String in0, long in1, int in2, java.lang.String in3) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex[] findVisualObjectLocation(java.lang.String in0, long in1, int in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.ispace.Shape in4) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Shape[] findVisualObjects(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex in2, java.lang.String in3, com.strandgenomics.imaging.iserver.services.ws.ispace.Area in4) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile[] listAvailableProfiles(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getMicroscopeName(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public void setAcquisitionProfile(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile in2) throws java.rmi.RemoteException {
    }

    public boolean requestAcquisitionLicense(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return false;
    }

    public void surrenderAcquisitionLicense(java.lang.String in0) throws java.rmi.RemoteException {
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Project findProject(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String[] listAvailableFormats(java.lang.String in0) throws java.rmi.RemoteException {
        return null;
    }

    public long[] listGUIDsForArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String findProjectForRecord(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String[] findProjectForArchive(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return null;
    }

    public long findGUID(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.ispace.FingerPrint in1) throws java.rmi.RemoteException {
        return -3;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Record[] findRecordForGUIDs(java.lang.String in0, long[] in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getBookmarkRoot(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String[] getBookmarkSubFolders(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.Long[] getBookmarkGuids(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException {
        return null;
    }

    public void createBookmarkFolder(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException {
    }

    public void addBookmark(java.lang.String in0, java.lang.String in1, java.lang.String in2, long in3) throws java.rmi.RemoteException {
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Image getPixelDataForRecord(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics getIntensityDistibutionForTile(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2, int in3, int in4, int in5, int in6) throws java.rmi.RemoteException {
        return null;
    }

    public void setChannelColorAndContrast(java.lang.String in0, long in1, int in2, com.strandgenomics.imaging.iserver.services.ws.ispace.Contrast in3, java.lang.String in4) throws java.rmi.RemoteException {
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Channel[] getRecordChannels(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.RecordSite[] getRecordSite(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public void addUserComment(java.lang.String in0, long in1, java.lang.String in2) throws java.rmi.RemoteException {
    }

    public java.lang.String getThumbnailUploadURL(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getThumbnailDownloadURL(java.lang.String in0, long in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getRawIntensitiesDownloadURL(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getImageDownloadURL(java.lang.String in0, long in1, boolean in2, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in3) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getTileIntensitiesDownloadURL(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2, int in3, int in4, int in5, int in6) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getTileImageDownloadURL(java.lang.String in0, long in1, boolean in2, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in3, int in4, int in5, int in6, int in7) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getOverlayImageDownloadURL(java.lang.String in0, long in1, int in2, int in3, int in4, int[] in5, boolean in6, boolean in7, boolean in8, int in9, int in10, int in11, int in12, int in13, int in14) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getOverlayImageDownloadURL(java.lang.String in0, long in1, int in2, int in3, int in4, int[] in5, boolean in6, boolean in7, boolean in8, int in9, int in10, int in11, int in12, int[] in13) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getOverlayImageDownloadURL(java.lang.String in0, long in1, int in2, int in3, int in4, int[] in5, boolean in6, boolean in7, boolean in8, int in9, int in10, int in11, int in12) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getChannelOverlaidSliceImagesURL(java.lang.String in0, long in1, int in2, int in3, int in4, boolean in5) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics getIntensityDistibution(java.lang.String in0, long in1, com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex in2) throws java.rmi.RemoteException {
        return null;
    }

    public com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource getMosaicResource(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicRequest in1) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String getMosaicElementDownloadUrl(java.lang.String in0, com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource in1, com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicParameters in2) throws java.rmi.RemoteException {
        return null;
    }

}
