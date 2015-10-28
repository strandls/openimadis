package com.strandgenomics.imaging.iengine.models;

public class ProjectAccessKey {
	private String projectName;
	private String userLogin;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (o == null || getClass() != o.getClass())
			return false;

		ProjectAccessKey that = (ProjectAccessKey) o;

		if (userLogin != null && projectName != null)
		{
			if(this.projectName.equals(that.projectName) && this.userLogin.equals(that.userLogin))
				return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Project:"+projectName+" UserLogin:"+userLogin;
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
}
