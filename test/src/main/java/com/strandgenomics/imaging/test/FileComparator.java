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

package com.strandgenomics.imaging.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;



public class FileComparator {
	static int flag=0;
	
	public static void sortFile(String file) throws IOException{
		ArrayList<String> rows = new ArrayList<String>();
	    BufferedReader reader = new BufferedReader(new FileReader(file));

	    String s;
	    while((s = reader.readLine())!=null)
	    	rows.add(s);

	    Collections.sort(rows);

	    FileWriter writer = new FileWriter(file+"_sorted");
	    for(String row: rows)
	    	writer.write(row+"\n");

	    reader.close();
	    writer.close();
	}
	
	public static int compareFiles(String testFile, String goldFile) throws IOException{
		String line1=null, line2=null;
		FileReader fr1 = new FileReader(testFile);
		BufferedReader br1 = new BufferedReader(fr1);
		FileReader fr2 = new FileReader(goldFile);
		BufferedReader br2 = new BufferedReader(fr2);
		while((line1= br1.readLine())!=null && (line2= br2.readLine())!=null) {
			if (line2.equals(line1)){ flag=1;}
			else{ flag = 0;}
		}
		return flag;
	}
}


