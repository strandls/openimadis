/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
