package com.strandgenomics.imaging.iserver.services.test.load;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iserver.services.impl.web.RequestConstants;

public class UserAccessPattern extends Thread{
	String username;
	String password;
	TestConfiguration configuration;
	Long currentRecordId;
	Map<String,Object> recordMetaData;
	int sliceCount,frameCount,siteCount;
	List<Integer> channels;
	HttpClient client;
	Map<String,String> etagsStore;
	ObjectMapper mapper = new ObjectMapper();
	
	public UserAccessPattern(String username, String password,
			TestConfiguration configuration) {
		super();
		this.username = username;
		this.password = password;
		this.configuration = configuration;
		client = new HttpClient();
		etagsStore=new HashMap<String, String>();
	}

	@Override
	public void run() {
		try {
			login();
			selectRecord();
			for(int i=0;i<configuration.imageAccessesPerUser ;i++){
				accessImage();
				recordChangeDecision();
			}
			logout();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	

	private void login() throws HttpException, IOException {
		getData("auth/login", Util.createMap("loginUsername", username, "loginPassword", password));
	}

	private void logout() throws HttpException, IOException {
		getData("auth/logout", Util.createMap("loginUsername", username));
	}
	
	private int randomInt(int range){
		return (int)Math.round(Math.floor(Math.random()*range));
	}

	private void selectRecord() throws HttpException, IOException {
		int randomIndex = randomInt(configuration.recordids.length);
		this.currentRecordId =configuration.recordids[randomIndex];
		
		String url="record/getRecordData";
		
		String response=getData(url, Util.createMap(RequestConstants.RECORD_ID_KEY, currentRecordId));
		System.out.println(response);
		JsonNode jsonNode = mapper.readTree(response);
		
		
//		Map<String,Object> metadata =
//			new Gson().fromJson(response, new TypeToken<Map<String,Object>>(){}.getType());
		siteCount= jsonNode.get("Site Count").getIntValue();
		sliceCount= jsonNode.get("Slice Count").getIntValue();
		frameCount= jsonNode.get("Frame Count").getIntValue();
		
		channels=new ArrayList<Integer>();
		for(int i=0;i<jsonNode.get("Channel Count").getIntValue(); i++){
			if(Math.random()<0.5){
				channels.add(i);
			}
		}
		if(channels.size()== 0){
			channels.add(0);
		}
	}

	private void accessImage() throws HttpException, IOException, InterruptedException {
		
		String checkAvailable="project/checkImageReaderAvailability";
		String response=getData(checkAvailable, Util.createMap("recordid", currentRecordId, "requestImageReader", true));
		JsonNode jsonNode = mapper.readTree(response);
		boolean isAvailable= jsonNode.get("available").getBooleanValue();
		long sleepInterval=200;
		int callCount=1;
		
		while(isAvailable!=true){
			response=getData(checkAvailable, Util.createMap("recordid", currentRecordId, "requestImageReader", false));
			jsonNode = mapper.readTree(response);
			isAvailable= jsonNode.get("available").getBooleanValue();
			
			Thread.sleep(sleepInterval);
			sleepInterval = Math.max(5000, sleepInterval*2);
			if(++callCount >=50){
				System.out.println("====Image Generation failed");
				return;
			}
		}
		
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("channelNumbers",channels.toString());
		params.put("frameNumber",randomInt(frameCount));
		params.put("height",512);
		params.put("isGreyScale",false);
		params.put("isMosaic",false);
		params.put("isZStacked",false);
		params.put("recordid",currentRecordId);
		params.put("siteNumber",randomInt(siteCount));
		params.put("sliceNumber",randomInt(sliceCount));
		
		String url="project/getImage";
		InputStream isStream= getImageData(url, params);
		if(isStream == null){
			return;
		}
		String fileName = 
				 params.get("recordid").toString() +"_"
				+params.get("frameNumber").toString()+"_"
				+params.get("sliceNumber")+"_"
				+params.get("siteNumber")+"_"
				+params.get("channelNumbers");
		
		Util.transferData(isStream, new FileOutputStream(new File(configuration.baseDir+File.separator+fileName)));
	}
	

	private void recordChangeDecision() throws HttpException, IOException {
		double rand=Math.random();
		if(rand<this.configuration.probability_of_record_change){
			selectRecord();
		}
	}
	
	private String getData(String url, Map<String,Object> params) throws HttpException, IOException{
		url = this.configuration.baseUrl+url;
		PostMethod method=new PostMethod(url);
		for(String paramName:params.keySet()){
			method.addParameter(paramName, params.get(paramName).toString());
		}
		client.executeMethod(method);
		return method.getResponseBodyAsString();
	}
	

	private InputStream getImageData(String url, Map<String,Object> params) throws HttpException, IOException{
		url = this.configuration.baseUrl+url;
		PostMethod method=new PostMethod(url);
		for(String paramName:params.keySet()){
			method.addParameter(paramName, params.get(paramName).toString());
		}
		String oldEtag=etagsStore.get(url);
		if(oldEtag !=null){
			method.addParameter("If-None-Match", oldEtag);
		}
		client.executeMethod(method);
		String etag =null;
		if(method.getResponseHeader("Etag") !=null){
			etag =method.getResponseHeader("Etag").getValue();
			if(etag.equals(oldEtag)){
				System.out.println("Etag-match");
				return null;
			}
		}
		etagsStore.put(url, etag);
		
		return method.getResponseBodyAsStream();
	}

}
