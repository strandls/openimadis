package com.strandgenomics.imaging.icompute;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Application;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Constraints;
import com.strandgenomics.imaging.iclient.impl.ws.worker.LongListConstraints;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Parameter;
import com.strandgenomics.imaging.iclient.impl.ws.worker.StringListConstraints;

public class ApplicationGsonGenerator {
	
	public static void main(String[] args) {
		createApplicationConfig();
	}

	private static void createApplicationConfig() {
		Application application=new Application();
		application.setCategoryName("Demo");
		application.setDescription("Draws Circle at the center of image.");
		application.setName("center");
		long[] validValues = {0L,1L,2L,4L,9L};
		Parameter site = new Parameter(new LongListConstraints(validValues), 0L,"site of the record", "SiteNo", "INTEGER");
		String[] values = {"default", "anup", "kulkarni"};
		Parameter overlay = new Parameter(new StringListConstraints(values), "default","name of the overlay", "OverlayName", "STRING");
		Parameter[] ps = {site, overlay};
		application.setParameters(ps);
		application.setVersion("1.0");
		application.setClientID("hYRkPg664WzZsIFzHqfd00WKe0tbp3VCWGCFDANF");
		
		Gson gson = new GsonBuilder().registerTypeAdapter(Constraints.class, new InterfaceAdapter<Constraints>())
                .create();
		String json=gson.toJson(application);
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\repository\\rep\\imaging\\compute-deploy\\apps\\center\\center.gson"));
			out.write(json);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
