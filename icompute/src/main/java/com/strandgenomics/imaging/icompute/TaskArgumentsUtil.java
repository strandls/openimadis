package com.strandgenomics.imaging.icompute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.strandgenomics.imaging.iclient.impl.ws.worker.NVPair;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Work;

/**
 * writes the task arguments to a input file.
 * the input file will be of nature name=value1,value2.. 
 * 
 * @author Anup Kulkarni
 */
public class TaskArgumentsUtil 
{
	/**
	 * returns the arguments required for a task enlisted in input file as name=value map
	 * @param filepath specified input file
	 * @return name=value map
	 * @throws IOException
	 */
	public static HashMap<String, List<Object>> getWorkArguments(String filepath) throws IOException
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
	
	/**
	 * returns map of argument names and their respective values for specified work
	 * @param work specified work
	 * @return map of argument names and their respective values for specified work
	 */
	public static HashMap<String, List<Object>> getWorkArguments(Work work)
	{
		HashMap<String, List<Object>> arguments = new HashMap<String, List<Object>>();
		
		// the arguments injected by server and used by client program
		List<Object> authCode = new ArrayList<Object>();
		authCode.add(work.getAppAuthCode());
		arguments.put("AuthCode",authCode);
		
		List<Object> taskHandle = new ArrayList<Object>();
		taskHandle.add(work.getTaskID());
		arguments.put("TaskHandle",taskHandle);
		
		// the arguments required by the client program to run
		for (NVPair nvPair : work.getParameters()) 
		{
			List<Object> param = new ArrayList<Object>();
			param.add(String.valueOf(nvPair.getValue()));
			
			arguments.put(nvPair.getName(),param);
		}
		
		// the input record ids
		long guids[] = work.getInputRecords();
		if(guids != null)
		{
			List<Object> inputs = new ArrayList<Object>();
			for(long guid:guids)
			{
				inputs.add(guid);
				arguments.put("RecordIds", inputs);
			}
		}
		
		return arguments;
	}
	
	/**
	 * writes the specified arguments to specified file in name=value1,value2.. format
	 * @param filepath specified filename
	 * @param arguments specified arguments
	 * @throws FileNotFoundException
	 */
	public static void writeArguments(String filepath, HashMap<String, List<Object>>arguments) throws FileNotFoundException
	{
		if(arguments==null || arguments.isEmpty())
			return;
		
		File argFile = new File(filepath);
		PrintWriter pw = new PrintWriter(argFile);
		
		for(Entry<String, List<Object>> arg:arguments.entrySet())
		{
			String name = arg.getKey();
			List<Object>values = arg.getValue();
			if(name!=null && !name.isEmpty() && values!=null && !values.isEmpty())
			{
				StringBuffer sb = new StringBuffer(name+"=");
				for(Object value:values)
				{
					sb.append(value);
					sb.append(",");
				}
				// remove the last ','
				sb.deleteCharAt(sb.length()-1);
				
				pw.println(sb.toString());
			}
		}
		
		pw.close();
	}
}
