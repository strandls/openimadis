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

package com.strandgenomics.imaging.iengine.compute;

/**
 * ComputeWorkers is a Publisher and represents an instance of a worker-system managed by the server
 * @author arunabha
 *
 */
public class ComputeWorker extends Publisher
{
    /** 
     * current state of this worker 
     */
    protected boolean isWorking = false;
    /**
     *  the time when the last ping call was received 
     */
    protected long lastPingTime = 0;
    /** 
     * flag to indicate if this worker should be terminated 
     */
    protected boolean terminateWorker = false;
    
    /**
     * Creates an empty worker
     */
    public ComputeWorker(Publisher p)
    {
    	super(p.name, p.description, p.publisherCode, p.ipFilter);
    	this.key = p.key;
    }
    
    public void setTerminateWorker(boolean terminate) 
    {
        terminateWorker = terminate;
    }
    
    public boolean isTerminateWorker()
    {
        return terminateWorker;
    }
    
    public void setWorking(boolean status)
    {
        isWorking = status;
    }
    
    public boolean isWorking()
    {
        return isWorking;
    }
}
