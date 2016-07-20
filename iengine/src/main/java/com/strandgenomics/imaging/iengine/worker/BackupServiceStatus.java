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
