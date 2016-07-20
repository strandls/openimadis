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

package com.strandgenomics.imaging.iserver.services.test.load;

import com.google.gson.Gson;

public class TestConfiguration {

	long[] recordids={1}; 
	int imageAccessesPerUser =5;
	double probability_of_record_change=0.1;
	
	String baseUrl= "http://localhost:8080/iManage/";
	String baseDir = "/home/devendra/sandbox/img/loadtest/";
	
	String[] userList={"a","administrator","a","administrator","a","administrator","a","administrator"};
	String[] passwords={"a","admin1234","a","admin1234","a","admin1234","a","admin1234"};
	
	public static void main(String[] args) {
		System.out.println(new Gson().toJson(new TestConfiguration()));
	}
	
	
}
