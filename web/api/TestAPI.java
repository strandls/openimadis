import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/*
 * 
 * This is a class that demonstrates how records can be accessed and modified from the server.
 */

public class TestAPI {
	
	
	// A method that adds a new overlay to a record and draws a circle at the center.
	
	public static void addOverlay(long guid, int siteNo, String overlayName){
		
		// Get the record with specific GUID
		Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
		System.out.println("successfully read the record "+guid);	
		
		// Get height and width of the record images
		
		int height = r.getImageHeight();
		int width = r.getImageWidth();
		System.out.println("creating overlay "+overlayName);			
		
		// Add a new visual overlay to a given site if it does not exist ..
		if(!r.getAvailableVisualOverlays(siteNo).contains(overlayName))
			r.createVisualOverlays(siteNo, overlayName);
		
		// Create visual objects to the overlay
		List<VisualObject> vObjects = new ArrayList<VisualObject>();
		Ellipse e = new Ellipse(width/2-25, height/2-25, 50, 50);
		e.setPenColor(Color.RED);
		e.setPenWidth(2.0f);
		vObjects.add(e);
		System.out.println("adding objects");
		
		// Add objects to the newly created overlay ..
		r.addVisualObjects(vObjects, overlayName, new VODimension(0, 0, 0));
		System.out.println("success");	
		
	}
	
	// Add comment to a given record
	public static void addComment(long guid, int siteNo, String comment){
		// Get the record with specific GUID
				Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
				System.out.println("successfully read the record "+guid);	
					
				System.out.println("Adding comment to the record "+comment);
				// Add a comment to the reocrd
				r.addUserComments(comment);
	}
	
	/*
	 * Search for records and add comments and visual overlays to the records ..
	 * 
	 */
	
	public static void main(String... args) throws InterruptedException, IOException
	{

	//rabObVQImgaoUBkb61elLst5AjPRYLVormphRSQj
	//bibPSWjZGUi9UEmXetaZaE6Hc6Yo33mWbygvVI9W
	//A0q3qFEStnoVByWUw6wYx376gKODtFElIGm4NQcm
	//XzPTcayQz9TII4GTvfefxcTyDyFXPYjsqcE6FKKM	
		
		// Register and view client ID as an administrator 
		String clientId = "YZ8jecgu3t2RhntPQlIq4ARPEHkMNjYMbZPE0Bva";
	
		// Generate auth-codes for the client using Settings Menu on webclient
		// Each invokation of the program will need a new auth-code
		String authCode = "pumHDDsQdPQzIbkpfdAPvJyBjta559F8FpHL2rCv";
		
		
		// login to iManage with the client id and auth code
		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		boolean loginStatus = ispace.login(false, "demo.avadis-img.com", 80, clientId, authCode);
		
		if(loginStatus){
			System.out.println("Logged in");

			// Search for records meeting a search term
			long [] records = ispace.search("nimisha", null, null, 2);
			if(records != null){
				for(long guid:records){
					int siteNo = 0;
					String overlayName = "Test Overlay";
					addOverlay(guid, siteNo, overlayName);
					addComment(guid, siteNo, "added comment using api");
				}
			}else{
				System.out.println("No record matches the search criterion .. ");
			}
		}
		
		// logout
		ispace.logout();
		System.out.println("successfully logged out");	
	}
	
	
}
