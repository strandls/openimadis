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
