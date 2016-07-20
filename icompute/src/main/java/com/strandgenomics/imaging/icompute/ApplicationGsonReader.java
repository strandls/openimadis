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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Application;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Constraints;
import com.strandgenomics.imaging.iclient.impl.ws.worker.LongListConstraints;
import com.strandgenomics.imaging.iclient.impl.ws.worker.Parameter;

/**
 * test class to read/debug application gson file
 * 
 * @author Anup Kulkarni
 */
public class ApplicationGsonReader {
	public static void main(String[] args)
	{
		try
		{
			Gson gson = new GsonBuilder().registerTypeAdapter(Constraints.class, new InterfaceAdapter<Constraints>())
	                .create();
			Reader reader = new FileReader("C:\\repository\\rep\\imaging\\compute-deploy\\apps\\center\\center.gson");
			Application application = gson.fromJson(reader, Application.class);
			System.out.println(application.getName());
			Parameter[] params = application.getParameters();
			for(Parameter param:params)
			{
				if(param.getConstraints() instanceof LongListConstraints)
				{
					long[] values = ((LongListConstraints)param.getConstraints()).getValidValues();
					System.out.println("default value = "+param.getDefaultValue()+" "+param.getDefaultValue().getClass());
					for(long value:values)
						System.out.println(value);
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (JsonSyntaxException e)
		{
			e.printStackTrace();
		}
		catch (JsonIOException e)
		{
			e.printStackTrace();
		}
	}
}
