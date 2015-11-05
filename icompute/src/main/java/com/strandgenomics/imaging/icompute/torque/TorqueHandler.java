package com.strandgenomics.imaging.icompute.torque;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * handler for torque related commands
 * 
 * @author Anup Kulkarni
 */
public class TorqueHandler {
	
	/**
	 * terminates running job from torque, this is just request. Already completed job cannot be terminated
	 * @param jobid specified job id
	 * @throws IOException
	 */
	public static void terminateJob(String jobid) throws IOException
	{
		Runtime.getRuntime().exec("qdel "+jobid);
	}
	
	/**
	 * submits the torque job to PBS system
	 * @param jobName name of the job
	 * @param executableFile name of the executable file
	 * @param params parameters(if any) supplied as environment variables
	 * @return torque job id
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String submitJob(String jobName, String executableFile, Map<String, String>params) throws IOException, InterruptedException
	{
		String paramString = "";
		if(params!=null && !params.isEmpty())
		{
			StringBuffer sb = new StringBuffer(paramString);
			sb.append(" -v ");
			for(Entry<String, String> param:params.entrySet())
			{
				String name = param.getKey();
				String value = param.getValue();
				sb.append(name+"="+value+",");
			}
			
			sb.deleteCharAt(sb.length()-1);
			
			paramString = sb.toString();
		}
		
		String jobNameString = "";
		if(jobName!=null && !jobName.isEmpty())
		{
			jobNameString = " -N "+jobName;
		}
		
		String pbsCommand = "qsub "+jobNameString+" "+paramString+" "+executableFile;
		
		Process p = Runtime.getRuntime().exec(pbsCommand);
		p.waitFor();
		p.getOutputStream().close();
        p.getErrorStream().close();
		
        BufferedInputStream ef = new BufferedInputStream(p.getInputStream());
        byte[] data = new byte[ef.available()];
        ef.read(data, 0, ef.available());
        ef.close();

        String result = new String(data);
        return result.trim();
	}
	
	/**
	 * returns state of job
	 * @param torqueId specified job
	 * @return job state
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getJobStatus(String torqueId) throws IOException, InterruptedException
	{
		Process p = Runtime.getRuntime().exec("qstat -f "+torqueId);
		p.waitFor();
		
		p.getOutputStream().close();
        p.getErrorStream().close();
		
        BufferedInputStream ef = new BufferedInputStream(p.getInputStream());
        byte[] data = new byte[ef.available()];
        ef.read(data, 0, ef.available());
        ef.close();

        String result = new String(data);
        
        String jobState = "C";
        String fields[] = result.split("\\n");
        for(String field:fields)
        {
        	if(field.indexOf("job_state")!=-1)
        	{
        		jobState = field.split("=")[1].trim();
        	}
        }
        
        return jobState;
	}
	
	/**
	 * returns the exit status of any "Completed" torque job
	 * @param torqueId specified job
	 * @return exit status
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int getExitStatus(String torqueId) throws IOException, InterruptedException
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
        	}
        }
        
        return exitStatus;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("args", "31416");
		
		String id = submitJob("test", args[0], params);
		for(int i=0;i<20;i++)
		{
			if(i==5)
			{
				terminateJob(id);
			}
			System.out.println(getExitStatus(id));
			System.out.println(getJobStatus(id));
			Thread.sleep(1000);
		}
	}
}
