package com.strandgenomics.imaging.icompute.torque;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class TorqueTest {
	public static void main(String[] args) throws IOException, InterruptedException
	{
		File f = new File("/data/cmms/torque_test/a.txt");

		PrintWriter bf = new PrintWriter(new FileOutputStream(f));
		bf.println("my torque test");
		bf.close();
		
		Thread.sleep(10000);
		
		System.out.println("my torque test");
	}
}
