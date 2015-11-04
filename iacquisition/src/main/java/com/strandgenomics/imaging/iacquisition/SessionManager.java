package com.strandgenomics.imaging.iacquisition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.iclient.local.RawRecord;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.IRecord;

/**
 * This class manages the session for a particular user. Session Manager is attached per user. It is initiated when the user logs in
 * and is functional till the user logs out. 
 * 
 * @author Anup Kulkarni
 * 
 */
public class SessionManager {

	private String userName;
	
	public SessionManager(String userName){
		this.userName = userName;
	}

	/**
	 * stores the session given list of records
	 * @param list of records to be saved
	 */
	public void storeSession(List<IRecord> records){
		
		String filename = Constants.getConfigDirectory() + File.separator + "imanage-acqclient-" + userName + ".session";
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(new File(filename)));
			os.writeObject(records);
			os.writeObject(System.getProperty("imanage.user.dir"));
			
			for(IRecord record:records)
			{
				if(record instanceof RawRecord)
				{
					if(((RawRecord) record).hasCustomThumbnail())
					{
						String thumbnailFilename = Constants.getConfigDirectory() + File.separator + "imanage-acqclient-" + record.getSourceFilename()  + "_thumbnail.png";
						File thumbnailFile = new File(thumbnailFilename);
						ImageIO.write(record.getThumbnail(), "PNG", thumbnailFile);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if (os != null) {
                	os.flush();
                	os.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
	
	/**
	 * loads the session for this user
	 * @return list of records saved in previous session
	 */
	public List<IRecord> loadSession()
	{
		List<IRecord> records = new ArrayList<IRecord>();
		File sessionFile = new File(Constants.getConfigDirectory(), "imanage-acqclient-" + userName + ".session");
		
		if(!sessionFile.exists())
			return records;
		
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream(sessionFile));
			
			
			long stime = System.currentTimeMillis();
			records = (List<IRecord>) is.readObject();
			long etime = System.currentTimeMillis();
			
			String dir = (String)is.readObject();
			System.setProperty("imanage.user.dir", dir);
			
			stime = System.currentTimeMillis();
			for(IRecord record:records)
			{
				String thumbnailFilename = Constants.getConfigDirectory() + File.separator + "imanage-acqclient-" + record.getSourceFilename()  + "_thumbnail.png";
				File thumbnailFile = new File(thumbnailFilename);
				if(thumbnailFile.exists())
				{
					record.setThumbnail(thumbnailFile);
				}
			}
			etime = System.currentTimeMillis();
			
			System.out.println("**************** thumbnail loading time="+(etime-stime));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}finally {
            try {
                if (is != null) {
                	is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		
		return records;
	}
	
	public static void main(String[] args)
	{
		SessionManager sm = new SessionManager("administrator_Strand");
		List<IRecord> records = sm.loadSession();
		System.out.println(records.size());
	}
}
