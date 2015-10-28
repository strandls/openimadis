package com.strandgenomics.imaging.iengine.models;

public enum UserAction {
	DELETE {
		@Override
		public String toString() {
			return "Deletion";
		}
	},  MANAGE_USERS {
		@Override
		public String toString() {
			return "Manage";
		}
	},UPLOAD{
		@Override
		public String toString() {
			return "Upload";
		}
	}, WRITE{
		@Override
		public String toString() {
			return "Write";
		}
	}, READ{
		@Override
		public String toString() {
			return "Read";
		}
	}, EXPORT{
		@Override
		public String toString() {
			return "Export";
		}
	}, EDIT_USER_DETAILS{
		@Override
		public String toString() {
			return "Edit user details";
		}
	}
}
