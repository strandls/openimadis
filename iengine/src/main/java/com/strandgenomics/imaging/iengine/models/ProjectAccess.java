package com.strandgenomics.imaging.iengine.models;

import java.util.Date;


public class ProjectAccess {

	private ProjectAccessKey key;
	private Date accessTime;
	private UserAction action ;
	
	public ProjectAccess() {
		key = new ProjectAccessKey();
	}

	public ProjectAccessKey getKey() {
		return key;
	}

	public void setKey(ProjectAccessKey key) {
		this.key = key;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public UserAction getUserAction() {
		return action;
	}

	public void setUserAction(UserAction action) {
		this.action = action;
	}

}
