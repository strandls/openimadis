package com.strandgenomics.imaging.iclient.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.strandgenomics.imaging.iclient.AuthenticationException;
import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * demo program to add visual annotations using compute infrastructure
 * @author anup
 *
 */
public class ComputeVAApplication {
	public static void main(String... args) throws InterruptedException, AuthenticationException, HttpException, IOException
	{
		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		String appID = "qAkzUQ82jjv8gdlMNLhAtJKhBzpP67zfLMZZVqJC";

		ispace.login(false, "anthurium", 8080, appID, getAuthCode(appID));

		Record record = ispace.findRecordForGUID(12);
						
		List<VisualObject> vObjects = new ArrayList<VisualObject>();
		Ellipse e = new Ellipse(10, 20, 30, 60);
		vObjects.add(e);

		Rectangle rect = new Rectangle(0, 0, 10, 20);
		vObjects.add(rect);

		LineSegment l = new LineSegment(50, 70, 10, 20);
		vObjects.add(l);

		TextBox tb = new TextBox(0, 0, 10, 20, "test comment");
		vObjects.add(tb);

		record.createVisualOverlays(0, "test_overlay");
		record.addVisualObjects(vObjects, "test_overlay", new VODimension(1, 0, 0));
						

		ispace.logout();
		System.out.println("[RecordListingTest]:\tDone");
	}
	
	private static String getAuthCode(String clientID) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		
		PostMethod method = new PostMethod("http://localhost:8080/iManage/auth/login");
		method.addParameter("loginUsername", "administrator");
		method.addParameter("loginPassword", "admin1234");
		
		client.executeMethod(method);
		
		PostMethod postMethod = new PostMethod("http://localhost:8080/iManage/compute/generateAuthCode");
		postMethod.addParameter("clientID", clientID);
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
