package com.strandgenomics.imaging.iacquisition;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * For testing AcquisitionUI
 */
public class AcqLauncher {
	public static void main(String... args) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		
		PostMethod method = new PostMethod("http://localhost:8080/iManage/auth/login");
		method.addParameter("loginUsername", "administrator");
		method.addParameter("loginPassword", "Admin#1234");
		
		client.executeMethod(method);
		
		PostMethod postMethod = new PostMethod("http://localhost:8080/iManage/compute/generateAuthCode");
		postMethod.addParameter("clientID", "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI");
		postMethod.addParameter("services", "[\"AUTHENTICATION\", \"ISPACE\", \"SEARCH\", \"LOADER\", \"UPDATE\", \"MANAGEMENT\", \"COMPUTE\"]");
		postMethod.addParameter("expiryTime", ""+((new Date()).getTime()+(24*3600*1000)));
		
		client.executeMethod(postMethod);
		
		String auth = postMethod.getResponseBodyAsString();

		auth = parseString(auth);
		String a[] = {auth, "localhost", "8080", "1.23", "http"};
		AcquisitionUI.main(a);
	}
	
	private static String parseString(String auth)
	{
		return auth.split(":")[1].split(",")[0].split("\"")[1];
	}
}
