package com.strandgenomics.imaging.iclient.local;

/**
 * status of experiment upload
 * 
 * @author Anup Kulkarni
 */
public enum UploadStatus {
	NotUploaded {
		@Override
		public String toString() {
			return "";
		}
	}, 
	
	Uploaded {
		@Override
		public String toString() {
			return "Successful";
		}
	}, 
	
	Queued{
		@Override
		public String toString() {
			return "Queued";
		}
	},
	
	QueuedBackground{
		@Override
		public String toString() {
			return "QueuedInBackground";
		}
	},
	
	Duplicate{
		@Override
		public String toString() {
			return "Duplicate";
		}
	}
}
