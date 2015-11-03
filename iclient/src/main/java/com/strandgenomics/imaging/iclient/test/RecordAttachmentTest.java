package com.strandgenomics.imaging.iclient.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.IAttachment;

public class RecordAttachmentTest {
	public static void main(String... args) throws InterruptedException, IOException {
		if (args == null || args.length == 0) {
			args = new String[] { "banerghatta", "8080", "salamero",
					"salamero123" };
		}

		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		String hostIP = args[0];
		int hostPort = Integer.parseInt(args[1]);
		String userName = args[2];
		String password = args[3];

		ispace.login(false, hostIP, hostPort, userName, password);

		List<Project> projectList = null;
		try {
			projectList = ispace.getActiveProjects();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (projectList != null) 
		{
			for (Project p : projectList) 
			{
				System.out.println("[Project]:\tname=" + p.getName());
				
				long[] guids = p.getRecords();
				if (guids != null) 
				{
					for (long guid : guids) 
					{
						Record r = ispace.findRecordForGUID(guid);
						
						System.out.println("\t[Record]:\tchannel count="
								+ r.getChannelCount() + ", Frame Count="
								+ r.getFrameCount() + ", Slice Count="
								+ r.getSignature());
						
//						r.addAttachment(new File("/home/anup/Desktop/final_attachment.txt"),
//								"this is attachment note: anup kulkarni ");
						
						Iterator<IAttachment> it = r.getAttachments().iterator();
						while(it.hasNext()){
							IAttachment attachment = it.next();
							System.out.println(attachment.getName()+" "+attachment.getNotes());
							InputStream is = attachment.getInputStream();
							while(true){
								int value = is.read();
								if(value == -1)
									break;
								System.out.print((char)value);
							}
						}
				
						Thread.sleep(100);
						System.exit(0);
					}
				}
				
				Thread.sleep(100);
			}
		} 
		else 
		{
			System.out.println("[RecordListingTest]:\trecords not found...");
		}

		System.out.println("[RecordListingTest]:\tDone");
	}
}
