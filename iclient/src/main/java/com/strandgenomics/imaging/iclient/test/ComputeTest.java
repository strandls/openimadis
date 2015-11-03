package com.strandgenomics.imaging.iclient.test;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorization;
import com.strandgenomics.imaging.iclient.impl.ws.auth.ImageSpaceAuthorizationServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.compute.ImageSpaceComputeServiceLocator;
import com.strandgenomics.imaging.iclient.impl.ws.compute.NVPair;
import com.strandgenomics.imaging.icore.Constants;

public class ComputeTest {
	public static void main(String ... args) throws Exception
    {
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"localhost", "8080", "anup", "anup123"};
    	}
    	String baseURL = "/"+Constants.getWebApplicationContext() +"/services/";
    	
    	ImageSpaceAuthorizationServiceLocator authService = new ImageSpaceAuthorizationServiceLocator();
    	ImageSpaceAuthorization iauth = authService.getiAuth(new URL("http", "localhost", 8080, baseURL+"iAuth"));
		
    	
    	
		//get the relevant access token to access the methods of the services
		String accessToken = iauth.getAccessToken("JKh4B5NNqFEOQoVdcB2wNeRFKYqpo98WOPQ72lyt", getAuthCode());

    	ImageSpaceComputeServiceLocator comupteService = new ImageSpaceComputeServiceLocator();
    	long guids[] = {15,12};
    	NVPair pairs[] = new NVPair[1];
    	NVPair p = new NVPair("Sample", "value");
    	pairs[0] = p;
    	
    	comupteService.getiCompute().executeApplication(accessToken, "demo1", "1.0", "Strand Test",pairs, guids, 1);
    }
	
	private static String getAuthCode() throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		
		PostMethod method = new PostMethod("http://localhost:8080/iManage/auth/login");
		method.addParameter("loginUsername", "administrator");
		method.addParameter("loginPassword", "admin1234");
		
		client.executeMethod(method);
		
		PostMethod postMethod = new PostMethod("http://localhost:8080/iManage/compute/generateAuthCode");
		postMethod.addParameter("clientID", "JKh4B5NNqFEOQoVdcB2wNeRFKYqpo98WOPQ72lyt");
		postMethod.addParameter("services", "[\"AUTHENTICATION\", \"ISPACE\", \"SEARCH\", \"LOADER\", \"UPDATE\", \"MANAGEMENT\", \"COMPUTE\"]");
		postMethod.addParameter("expiryTime", ""+((new Date()).getTime()+(24*3600*1000)));
		
		client.executeMethod(postMethod);
		
		String auth = postMethod.getResponseBodyAsString();

		auth = parseString(auth);
		return auth;
	}
	
	private static String parseString(String auth)
	{
		return auth.split(":")[1].split(",")[0].split("\"")[1];
	}
}
