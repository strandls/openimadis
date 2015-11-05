package com.strandgenomics.imaging.icompute;

import java.util.Timer;
import java.util.logging.Logger;

import com.strandgenomics.imaging.iclient.impl.ws.worker.ImageSpaceWorkers;


public class ComputePingManager{

	private static final String ICOMPUTE_PING_INTERVAL="icompute.ping.interval";
	
	private Timer timer;
	protected Logger logger;
	private ImageSpaceWorkers iworker;
	private String accessToken;
	
	public ComputePingManager(ImageSpaceWorkers iworker, String accessToken) {
		timer=new Timer();
		this.iworker=iworker;
		this.accessToken=accessToken;
	}
	
	public void startPingTimer(){
		long pingInterval=Long.parseLong(System.getProperty(ICOMPUTE_PING_INTERVAL)); 
		//timer.schedule(this, 0, pingInterval);
	}

	
}
