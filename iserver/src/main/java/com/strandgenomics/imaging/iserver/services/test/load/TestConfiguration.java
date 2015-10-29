package com.strandgenomics.imaging.iserver.services.test.load;

import com.google.gson.Gson;

public class TestConfiguration {

	long[] recordids={1}; 
	int imageAccessesPerUser =5;
	double probability_of_record_change=0.1;
	
	String baseUrl= "http://localhost:8080/iManage/";
	String baseDir = "/home/devendra/sandbox/img/loadtest/";
	
	String[] userList={"a","administrator","a","administrator","a","administrator","a","administrator"};
	String[] passwords={"a","admin1234","a","admin1234","a","admin1234","a","admin1234"};
	
	public static void main(String[] args) {
		System.out.println(new Gson().toJson(new TestConfiguration()));
	}
	
	
}
