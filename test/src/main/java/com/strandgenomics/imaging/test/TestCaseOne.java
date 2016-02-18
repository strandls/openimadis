package com.strandgenomics.imaging.test;

import java.io.IOException;

public class TestCaseOne {
	public static void main(String args[]) throws IOException, InterruptedException{
		LoginManager loginManager = new LoginManager();
		loginManager.login();
		Upload upload = new Upload();
		upload.uploadFiles();
		System.out.println("...RUN FINISHED......");
	}

}
