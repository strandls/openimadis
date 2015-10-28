package com.strandgenomics.imaging.iengine.worker;

/**
 * status for backup service
 * @author navneet
 *
 */
@ServiceAnnotation(type = ServiceType.BACKUP_SERVICE)
public class BackupServiceStatus implements ServiceStatus {

	public int getNumberOfBackupRequests() {
		return numberOfBackupRequests;
	}

	public void setNumberOfBackupRequests(int numberOfBackupRequests) {
		this.numberOfBackupRequests = numberOfBackupRequests;
	}

	public int getNumberOfRestorationRequests() {
		return numberOfRestorationRequests;
	}

	public void setNumberOfRestorationRequests(int numberOfRestorationRequests) {
		this.numberOfRestorationRequests = numberOfRestorationRequests;
	}

	public boolean isCleaningUp() {
		return isCleaningUp;
	}
	
	public String getisCleaningUp() {
		return isCleaningUp == true ? "Yes" : "No";
	}

	public void setCleaningUp(boolean isCleaningUp) {
		this.isCleaningUp = isCleaningUp;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6142640387616743080L;
	
	@ServiceParameter(name = "Number of BackUp Requests")
	private int numberOfBackupRequests;
	
	@ServiceParameter(name = "Number of Restoration Requests")
	private int numberOfRestorationRequests;
	
	@ServiceParameter(name = "Cleaning")
	private boolean isCleaningUp;

}
