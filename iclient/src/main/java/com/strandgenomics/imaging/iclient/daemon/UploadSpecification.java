package com.strandgenomics.imaging.iclient.daemon;

import java.io.Serializable;

import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.UploadStatus;

/**
 * The specification of the record, original data and user data to be uploaded
 * 
 * @author Anup Kulkarni
 */
public class UploadSpecification implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2944488786548908567L;
	/**
	 * details of user session
	 */
	private UserSessionSpecification session;
	/**
	 * experiment
	 */
	private RawExperiment experiment;
	/**
	 * upload status
	 */
	private UploadStatus status;
	/**
	 * upload identifier given/used by the upload daemon
	 */
	private long uploadId;
	/**
	 * path where the object is stored to maintain session of the upload daemon
	 */
	private String filepath;
	/**
	 * true if the upload is canceled
	 */
	private boolean canceled;
	/**
	 * message set by the upload daemon
	 */
	private String message = "";

	public UploadSpecification(UserSessionSpecification session, RawExperiment experiment) 
	{
		this.session = session;
		this.experiment = experiment;
		this.status = UploadStatus.QueuedBackground;
	}
	/**
	 * sets the upload id
	 * @param uploadId
	 */
	void setUploadId(long uploadId)
	{
		this.uploadId = uploadId;
	}
	/**
	 * 
	 * @return upload identifier for the upload spec
	 */
	public long getUploadId()
	{
		return this.uploadId;
	}
	
	/**
	 * sets the status of this upload specification
	 * @param status
	 */
	public void setStatus(UploadStatus status)
	{
		this.status = status;
	}
	/**
	 * upload status
	 * @return
	 */
	public UploadStatus getStatus()
	{
		return this.status;
	}
	/**
	 * returns the message
	 * @return
	 */
	public String getMessage()
	{
		return this.message;
	}
	
	void setMessage(String string)
	{
		this.message = string;
	}
	
	void setCanceled()
	{
		this.canceled = true;
	}
	
	boolean isCanceled()
	{
		return this.canceled;
	}
	
	/**
	 * returns location where is spec is stored
	 * @return
	 */
	String getSessionFilepath()
	{
		return this.filepath;
	}
	
	void setSessionFilepath(String path)
	{
		this.filepath = path;
	}

	public RawExperiment getExperiment()
	{
		return experiment;
	}
	
	public String getProjectName()
	{
		return session.projectName;
	}

	public String getUserName()
	{
		return session.userLogin;
	}

	public String getAccessKey()
	{
		return session.accessKey;
	}

	public String getHost()
	{
		return session.host;
	}

	public int getPort()
	{
		return session.port;
	}
	
	public boolean isSSL()
	{
		return session.useSSL;
	}
}
