/*
 * HttpUtil.java
 *
 * AVADIS Image Management System
 * Utility Stuffs
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * Helper class to upload and download stuff using http
 * 
 * @author arunabha
 * 
 */
public class HttpUtil {
	
	public static final String DEFAULT_TYPE = "application/octet-stream";
	
	protected String contentType = DEFAULT_TYPE;
	protected boolean isSSL = false;
	protected URL url = null;
	
	public HttpUtil(URL url) {
		this(url, DEFAULT_TYPE);
	}
	
	public HttpUtil(URL url, String contentType) {
		this.url = url;
		this.isSSL = url.getProtocol().toLowerCase().startsWith("https");
		this.contentType = contentType;
	}
	
	/**
	 * @param postData
	 * @return InputStream of the requested URL to download data
	 * @throws IOException
	 */
	public InputStream getInputStream(byte[] postData) throws IOException {
		if (postData == null || postData.length == 0) {
			GetMethod get = new GetMethod(url.toExternalForm());
			get.setFollowRedirects(true);
			getHttpClient().executeMethod(get);
			return get.getResponseBodyAsStream();
		} else {
			PostMethod post = new PostMethod(url.toExternalForm());
			post.addRequestHeader("Content-Type", contentType);
			post.setRequestEntity(new ByteArrayRequestEntity(postData));
			getHttpClient().executeMethod(post);
			return post.getResponseBodyAsStream();
		}
	}
	
	/**
	 * utility method for uploading using http client
	 * 
	 * @param uploadFile
	 *            the file to upload
	 * @param observer
	 *            who observes progress of upload task
	 * @return
	 * @throws IOException
	 */
	public boolean upload(File uploadFile, UploadObserver observer) throws IOException {
		if (uploadFile == null || !uploadFile.canRead()) {
			return false;
		}
		
		PostMethod post = new PostMethod(url.toExternalForm());
		post.setRequestEntity(new FileRequestEntity(uploadFile, DEFAULT_TYPE, observer));
		getHttpClient().executeMethod(post);
		return true;
	}
	
	
	/**
	 * utility method for uploading using http client
	 * 
	 * @param uploadData
	 * @return
	 * @throws IOException
	 */
	public boolean upload(byte[] uploadData) throws IOException {
		if (uploadData == null) {
			return false;
		}
		
		PostMethod post = new PostMethod(url.toExternalForm());
		post.setRequestEntity(new ByteArrayRequestEntity(uploadData));
		return getHttpClient().executeMethod(post) == 200;
	}
	
	/**
	 * utility method for uploading using http client
	 * 
	 * @param uploadFile
	 *            the file to upload
	 * @return
	 * @throws IOException
	 */
	public boolean upload(File uploadFile) throws IOException {
		return upload(uploadFile, new UploadObserver() {
		public void reportProgress(File source, long totalBytes, long bytesUploaded) {
			}
			
			public boolean isCancelled() {
				return false;
			}
			
			public void reportProgress(int progress, String message) {
			}
		});
	}
	
	private HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		if (isHttpProxySet()) {
			HostConfiguration httpConfig = httpClient.getHostConfiguration();
			httpConfig.setProxy(getProxyHost(), getProxyPort());
			httpClient.setHostConfiguration(httpConfig);
		}
		if (isHttpProxyAuthSet()) {
			// HttpClient natively supports basic, digest, and NTLM
			// authentication.
			// NTLM authentication works almost exactly the same as any other
			// form of authentication in terms of the HttpClient API.
			// The only difference is that you need to supply 'NTCredentials'
			// instead of 'UsernamePasswordCredentials'.
			// Domain users are specified in this format "Domain\User" then use
			// NTCredentials instead.
			String proxyUser = getProxyUser();
			String proxyPass = getProxyPassword();
			
			String[] tokens = proxyUser.split("\\\\");
			Credentials proxyCred = null;
			
			if (tokens.length > 1) {
				String domain = tokens[0]; // The domain to authenticate within.
				String user = tokens[1];
				
				// The host the authentication request is originating from.
				// Essentially, the computer name for this machine.
				String localHost = Util.getMachineIP(); // to use proxy host or local host
				
				proxyCred = new NTCredentials(user, proxyPass, localHost, domain);
			} else {
				proxyCred = new UsernamePasswordCredentials(proxyUser,proxyPass);
			}
			
			HttpClientParams params = httpClient.getParams();
			HttpState state = httpClient.getState();
			// the realm for NTLM authentication is the domain name of the
			// computer being connected to. this can be troublesome as servers
			// often have multiple domain names that refer to them
			state.setProxyCredentials(AuthScope.ANY, proxyCred);
			//params.setAuthenticationPreemptive(true);
		}
		return httpClient;
	}
	
	private boolean isHttpProxySet() {
		return (getProxyHost() != null && getProxyPort() > 0);
	}
	
	private boolean isHttpProxyAuthSet() {
		return (getProxyUser() != null && getProxyPassword() != null);
	}
	
	private String getProxyHost() {
		if (isSSL) {
			return System.getProperty("https.proxyHost");
		} else {
			return System.getProperty("http.proxyHost");
		}
	}
	
	private int getProxyPort() {
		if (isSSL) {
			return Integer.parseInt(System.getProperty("https.proxyPort", "0"));
		} else {
			return Integer.parseInt(System.getProperty("http.proxyPort", "0"));
		}
	}
	
	private String getProxyUser() {
		if (isSSL) {
			return System.getProperty("https.proxyUser");
		} else {
			return System.getProperty("http.proxyUser");
		}
	}
	
	private String getProxyPassword() {
		if (isSSL) {
			return System.getProperty("https.proxyPassword");
		} else {
			return System.getProperty("http.proxyPassword");
		}
	}
	
	private class FileRequestEntity implements RequestEntity {
		final File file;
		final String contentType;
		UploadObserver observer = null;
		
		public FileRequestEntity(final File file, final String contentType, UploadObserver observer) {
			super();
			if (file == null) {
				throw new IllegalArgumentException("File may not be null");
			}
			this.file = file;
			this.contentType = contentType;
			this.observer = observer;
		}
		
		public long getContentLength() {
			return this.file.length();
		}
		
		public String getContentType() {
			return this.contentType;
		}
		
		public boolean isRepeatable() {
			return true;
		}
		
		public void writeRequest(final OutputStream out) throws IOException {
			byte[] tmp = new byte[4096];
			int bytesRead = 0;
			BufferedInputStream instream = new BufferedInputStream(new FileInputStream(this.file));
			long noOfBytesUploaded = 0;
			
			long totalBytesToTranfer = this.file.length();
			
			if (observer != null)
				observer.reportProgress(file, totalBytesToTranfer, noOfBytesUploaded);
			
			try {
				while ((bytesRead = instream.read(tmp)) >= 0) {
					if (observer != null && observer.isCancelled())
						break;
					
					noOfBytesUploaded += bytesRead;
					out.write(tmp, 0, bytesRead);
					
					if (observer != null)
						observer.reportProgress(file, totalBytesToTranfer, noOfBytesUploaded);
				}
			} finally {
				instream.close();
				if (observer != null)
					observer.reportProgress(file, totalBytesToTranfer, totalBytesToTranfer);
			}
		}
	}

}