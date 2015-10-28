package com.strandgenomics.imaging.iengine.search;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServerException;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.system.SolrSearchManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

public class SolrSearchTest {
	
	private static long seed = 1234; 
	private static Random random = new Random(seed);
	
	private static Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.search");
	
	
	private static final int MAX_PIXEL_SIZE = 16;
	private static final int MAX_PROJECT_ID = 1000;
	private static final int MAX_SLICES = 1000;
	private static final int MAX_FRAMES = 1000;
	private static final String[] sources = {"jgp", "png", "bmp", "cr2", "raw"};
	private static final String[] uploaders = {"varun", "santhosh", "vijay", "arunabha", "nimisha", "anup", "ramesh", "sarath", "nitesh", "vamsi", "saurabh", "kamal"};

	public static void main(String[] args) throws SolrServerException, IOException {
		SolrSearchManager manager = SysManagerFactory.getSolrSearchEngine();
		
//		empty(manager);
		
//		add(manager);
		
		search(manager);
		
		
	}

	private static void search(SolrSearchManager manager) throws MalformedURLException, SolrServerException {
		List<Integer> projects = new ArrayList<Integer>();
		for (int i=0; i<100; i++) {
			projects.add(i);
		}
		List<SearchResult> results = manager.search("varun", projects, false);
		for (SearchResult result : results) {
			System.out.println(result);
		}
	}

	private static void add(SolrSearchManager manager) throws SolrServerException, IOException {
		int docs = 100000;
		int time = 3600000;
		logger.logp(Level.INFO, "SolrSearchTest", "main", "Auto Commit : docs = " + docs + ", time = " + time);
		
		// create 10 million records
		long t1 = System.currentTimeMillis();
		long t2 = System.currentTimeMillis();
		for (int i=0; i<10000000; i++) {
			if (i % 10000 == 0) {
				t2 = System.currentTimeMillis();
				logger.logp(Level.INFO, "SolrSearchTest", "main", i + " : time taken = "+(t2-t1));
				t1 = t2;
			}
			
			addRecord(manager, i);
		}
	}

	private static void empty(SolrSearchManager manager) throws SolrServerException, IOException {
		deleteAll(manager);
		manager.commit();
	}

	private static void deleteAll(SolrSearchManager manager) throws SolrServerException, IOException {
		manager.deleteAll();
	}

	private static void addRecord(SolrSearchManager manager, int i) throws SolrServerException, IOException {
		String src = getSource();
		PixelDepth imageDepth = getPixelDepth();
		ImageType imageType = getImageType();
		String sourceFolder = "some/folder";
		double xPixelSize = getXPixelSize();
		double yPixelSize = getYPixedSize();
		double zPixelSize = getZPixelSize();
		int projectID = getProjectID();
		String uploadedBy = getUploader();
		String machineIP = getMachineIP();
		String macAddress = getMacAddress();
		int noOfSlices = getNoOfSlices();
		int noOfFrames = getNoOfFrames();
		int imageWidth = getImageWidth();
		int imageHeight = getImageHeight();
		List<Channel> channels = getChannels();
		List<Site> sites = getSites();
		
		String sourceFilename = "img_" + i + "." + src;
		SourceFormat sourceType = new SourceFormat(src);
		
		
		BigInteger siteSignature = BigInteger.ZERO;
		BigInteger archiveSignature = BigInteger.ZERO;
		Long uploadTime = 0l;
		Long sourceTime = 0l;
		Long creationTime = 0l;
		Long acquiredTime = 0l;
		
		Record r = new Record(i, projectID, uploadedBy, noOfSlices, noOfFrames, imageWidth, imageHeight, siteSignature, archiveSignature, uploadTime, sourceTime, creationTime, acquiredTime, imageDepth, xPixelSize, yPixelSize, zPixelSize, sourceType, imageType, machineIP, macAddress, sourceFolder, sourceFilename, channels, sites, null);
		manager.addRecord(r);
	}

	private static int getImageWidth() {
		return random.nextInt(16) * 512;
	}

	private static int getImageHeight() {
		return random.nextInt(16) * 512;
	}

	private static int getNoOfSlices() {
		return random.nextInt(MAX_SLICES);
	}

	private static int getNoOfFrames() {
		return random.nextInt(MAX_FRAMES);
	}

	private static String getMacAddress() {
		String a = Integer.toHexString(random.nextInt(256));
		String b = Integer.toHexString(random.nextInt(256));
		String c = Integer.toHexString(random.nextInt(256));
		String d = Integer.toHexString(random.nextInt(256));
		String e = Integer.toHexString(random.nextInt(256));
		String f = Integer.toHexString(random.nextInt(256));
		a = a.length() == 0 ? "00" : (a.length() == 1 ? "0" + a : a);
		b = b.length() == 0 ? "00" : (b.length() == 1 ? "0" + b : b);
		c = c.length() == 0 ? "00" : (c.length() == 1 ? "0" + c : c);
		d = d.length() == 0 ? "00" : (d.length() == 1 ? "0" + d : d);
		e = e.length() == 0 ? "00" : (e.length() == 1 ? "0" + e : e);
		f = f.length() == 0 ? "00" : (f.length() == 1 ? "0" + f : f);
		return a + "-" + b + "-" + c + "-" + d + "-" + e + "-" + f;
	}

	private static String getMachineIP() {
		return "192.168." + random.nextInt(256) + "." + random.nextInt(256);
	}

	private static String getUploader() {
		int x = random.nextInt(uploaders.length);
		return uploaders[x];
	}

	private static int getProjectID() {
		return random.nextInt(MAX_PROJECT_ID);
	}

	private static double getZPixelSize() {
		return random.nextInt(MAX_PIXEL_SIZE) + 1;
	}

	private static double getYPixedSize() {
		return random.nextInt(MAX_PIXEL_SIZE) + 1;
	}

	private static double getXPixelSize() {
		return random.nextInt(MAX_PIXEL_SIZE) + 1;
	}

	private static ImageType getImageType() {
		int x = random.nextInt(2);
		if (x == 0)
			return ImageType.GRAYSCALE;
		else
			return ImageType.RGB;
	}

	private static PixelDepth getPixelDepth() {
		int x = random.nextInt(3);
		if (x == 0)
			return PixelDepth.BYTE;
		else if (x == 1)
			return PixelDepth.INT;
		else 
			return PixelDepth.SHORT;
	}

	private static String getSource() {
		return sources[random.nextInt(sources.length)];
	}

	private static List<Site> getSites() {
		List<Site> sites = new ArrayList<Site>();
		int count = random.nextInt(100);
		for (int i=0; i<count; i++)
			sites.add(new Site(random.nextInt(), "site" + random.nextInt()));
		return sites;
	}

	private static List<Channel> getChannels() {
		List<Channel> channels = new ArrayList<Channel>();
		int count = random.nextInt(100);
		for (int i=0; i<count; i++)
			channels.add(new Channel("channel" + random.nextInt()));
		return channels;
	}
}
