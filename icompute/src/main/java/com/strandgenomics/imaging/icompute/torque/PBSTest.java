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

package com.strandgenomics.imaging.icompute.torque;

import java.io.BufferedInputStream;
import java.io.IOException;

import pbsTorque.Job;
import pbsTorque.Server;

/**
 * test class for checking pbs4java jar functionalities
 * 
 * @author Anup Kulkarni
 */
public class PBSTest {
	public PBSTest()
	{ }
	
	public String submitJob(String jobName, String shellFilepath) throws IOException, InterruptedException, Exception
	{
		Job pbsJob = new Job(jobName, shellFilepath);
		String id = pbsJob.queue();

		return id;
	}
	
	public void deleteJob(String jobId) throws IOException, InterruptedException, Exception
	{
		System.out.println("deleting job "+jobId);
		Server.deleteJob(jobId);
	}
	
	public void getStatus() throws IOException, InterruptedException
	{
		Job[] jobs = Server.Jobs();
		
		if(jobs!=null)
		{
			for(Job j: jobs)
			{
				System.out.println(j.getId()+" "+j.getStatus());
			}
		}
		System.out.println("________________________________");
	}
	
	public static Job[] jobs() throws IOException, InterruptedException
	{
		String[] res;
		int NameIndex, UserIndex, TimeIndex, StatusIndex, QueueIndex;
		Process p = Runtime.getRuntime().exec("qstat");
		p.waitFor();

		BufferedInputStream ef = new BufferedInputStream(p.getInputStream());
		byte[] data = new byte[ef.available()];
		ef.read(data, 0, ef.available());
		ef.close();
		p.getOutputStream().close();
		p.getErrorStream().close();
		String Result = new String(data);
		String[] Jobs = Result.split("\n");
		String JobName;
		// res=new String[Jobs.length -2];
		NameIndex = Jobs[0].indexOf("Name");
		UserIndex = Jobs[0].indexOf("User");
		TimeIndex = Jobs[0].indexOf("Time");
		StatusIndex = Jobs[0].indexOf("S");
		QueueIndex = Jobs[0].indexOf("Queue");
		Job[] js = new Job[Jobs.length - 2];

		for (int i = 2; i < Jobs.length; i++)
		{
			js[i - 2] = new Job();
			js[i - 2].setId(Jobs[i].substring(0, Jobs[i].indexOf(" ", 0)));
			js[i - 2].setName(Jobs[i].substring(NameIndex,
					Jobs[i].indexOf(" ", NameIndex)));
			js[i - 2].setOwner(Jobs[i].substring(UserIndex,
					Jobs[i].indexOf(" ", UserIndex)));
			js[i - 2].setWallTime(Jobs[i].substring(TimeIndex,
					Jobs[i].indexOf(" ", TimeIndex)));
			js[i - 2].setStatus(Jobs[i].substring(StatusIndex,
					Jobs[i].indexOf(" ", StatusIndex)));
			js[i - 2].setQueue(Jobs[i].substring(QueueIndex,
					Jobs[i].indexOf(" ", QueueIndex)));
		}

		return js;
	}

	private static int getExitStatus(String torqueId) throws IOException, InterruptedException
	{
		int exitStatus = 0;
		
		Process p = Runtime.getRuntime().exec("qstat -f "+torqueId);
		p.waitFor();
		p.getOutputStream().close();
        p.getErrorStream().close();
		
        BufferedInputStream ef = new BufferedInputStream(p.getInputStream());
        byte[] data = new byte[ef.available()];
        ef.read(data, 0, ef.available());
        ef.close();

        String result = new String(data);
        
        String fields[] = result.split("\\n");
        for(String field:fields)
        {
        	if(field.indexOf("exit_status")!=-1)
        	{
        		String exitStatusString = field.split("=")[1].trim();
        		exitStatus = Integer.parseInt(exitStatusString);
        		
        		System.out.println("exitStatus "+exitStatus);
        	}
        }
        
        return exitStatus;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, Exception
	{
		PBSTest t = new PBSTest();
		String jobId = t.submitJob("test", "-v args=9876 "+args[0]);
		
		for(int i=0;i<20;i++)
		{
			Job[] jobs = jobs();
			for(Job job: jobs)
			{
				if(i==5)
				{
					Runtime.getRuntime().exec("qdel "+job.getId()+".curie.fr");
				}
				getExitStatus(job.getId()+".curie.fr");
			}
			Thread.sleep(1000);
		}
	}
}
