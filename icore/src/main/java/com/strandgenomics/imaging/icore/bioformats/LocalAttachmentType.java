package com.strandgenomics.imaging.icore.bioformats;

/**
 * Defines the category of local attachments as SystemAdded and UserAdded. 
 * This is used during the upload time to decide which attachments need to be
 * uploaded. System Added attachments need not be uploaded to server as they are
 * created again.
 * 
 * @author Anup Kulkarni
 */
public enum LocalAttachmentType {
	SystemAdded, UserAdded 
}
