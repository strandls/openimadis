import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.VisualObject;

public class FindCenterTest {
	public FindCenterTest() {}
	
	public void addOverlay(long guid, int siteNo, String overlayName){
		Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
		System.out.println("successfully read the record "+guid);	
		int height = r.getImageHeight();
		int width = r.getImageWidth();

		System.out.println("creating overlay "+overlayName);			
		r.createVisualOverlays(siteNo, overlayName);
		List<VisualObject> vObjects = new ArrayList<VisualObject>();
		Ellipse e = new Ellipse(width/2-25, height/2-25, 50, 50);
		e.setPenColor(Color.RED);
		e.setPenWidth(2.0f);
		vObjects.add(e);

		System.out.println("adding objects");	
		r.addVisualObjects(vObjects, overlayName, new VODimension(0, 0, 0));
		System.out.println("success");	
		
	}
	
	private static HashMap<String, List<Object>> getWorkArguments(String filepath) throws IOException
	{
		HashMap<String, List<Object>> arguments = new HashMap<String, List<Object>>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
		while(br.ready())
		{
			String line = br.readLine();
			String lineData[] = line.split("=");
			String name = lineData[0];
			
			Object values[] = lineData[1].split(",");
			arguments.put(name, Arrays.asList(values));
		}
		
		br.close();
		
		return arguments;
	}
	
	public static void main(String... args) throws InterruptedException, IOException
	{
		
		String appId = "hYRkPg664WzZsIFzHqfd00WKe0tbp3VCWGCFDANF";
		String authCode = System.getProperty("InputFile");
		
		HashMap<String, List<Object>> arguments = getWorkArguments(authCode);
		authCode = (String)arguments.get("AuthCode").get(0);
		
		long taskHandle = Long.parseLong((String)arguments.get("TaskHandle").get(0));
		
		int siteNo = Integer.parseInt((String)arguments.get("SiteNo").get(0));
		String overlayName =(String) arguments.get("OverlayName").get(0);
		List<Object> inputs = arguments.get("RecordIds");
		
		System.out.println("authCode "+authCode);		
		System.out.println("site "+siteNo);		
		System.out.println("overlayName "+overlayName);
		System.out.println("TaskHandle "+taskHandle);

		// login to image space system
		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		ispace.login(false, "localhost", 8080, appId, authCode);
		FindCenterTest findCenterTest = new FindCenterTest();

		for(int i=0; i < inputs.size(); i++){
			long guid= Long.parseLong((String)inputs.get(i));
			findCenterTest.addOverlay(guid, siteNo, overlayName);
			int progress=(i*100/inputs.size());
			
			Thread.sleep(10*1000);
		}
		
		// logout
		ispace.logout();
		System.out.println("successfully logged in");	
	}
	
	
}
