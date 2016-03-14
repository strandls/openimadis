package src.main.java.com.strandgenomics.imaging.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestCaseOne {
	public static void main(String args[]) throws IOException, InterruptedException{
		Properties prop = new Properties();
                prop.load(new FileInputStream(args[0]));
                System.setProperties(prop);
		LoginManager loginManager = new LoginManager();
		loginManager.login();
		Upload upload = new Upload();
		upload.uploadFiles();
		System.out.println("...RUN FINISHED......");
	}

}
