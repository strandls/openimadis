/**
 * 
 */
package com.strandgenomics.imaging.iserver.services.test.load;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * @author devendra
 *
 */
public class ReverseProxyLoadTest {
	TestConfiguration testConfiguration;
	
	public ReverseProxyLoadTest(String configFilePath) throws JsonIOException, JsonSyntaxException{
		try {
			testConfiguration = new Gson().fromJson(
				new FileReader(configFilePath), 
				new TypeToken<TestConfiguration>(){}.getType()
			);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @throws java.lang.Exception
	 */
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	public void tearDown() throws Exception {
	}
	
	public void simulateUserBehaviour(){
		List<UserAccessPattern> userThreads= new ArrayList<UserAccessPattern>(); 
		
		for(int i=0;i<testConfiguration.userList.length ; i++){
			UserAccessPattern thread=
				new UserAccessPattern(testConfiguration.userList[i], testConfiguration.passwords[i], testConfiguration);
			userThreads.add(thread);
			thread.start();
		}
		
		for(Thread thread: userThreads){
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		System.out.println("Test print");
		long startTime = System.currentTimeMillis();
		ReverseProxyLoadTest reverseProxyLoadTest=new ReverseProxyLoadTest(args[0]); 
		reverseProxyLoadTest.simulateUserBehaviour();
		
		long endTime = System.currentTimeMillis();
		System.out.println();
		System.out.print("No of users:" +reverseProxyLoadTest.testConfiguration.userList.length);
		System.out.print(" Image accesses per user : "+ reverseProxyLoadTest.testConfiguration.imageAccessesPerUser);
		System.out.print(" No of records exposed : "+ reverseProxyLoadTest.testConfiguration.recordids.length);
		System.out.print(" Time required : "+(endTime-startTime) +" ms");
		System.out.print(" Time per image : "+(endTime-startTime)
				/(reverseProxyLoadTest.testConfiguration.userList.length*reverseProxyLoadTest.testConfiguration.imageAccessesPerUser) +" ms");
	}

}
