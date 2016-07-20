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

package com.strandgenomics.imaging.iclient.local;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * Client experiment for which signature computation will be done on server side:
 * this means on acquisition client same record can be imported multiple times
 * 
 * @author Anup Kulkarni
 */
public class SignaturelessExperiment extends RawExperiment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8880916882220772324L;

	public SignaturelessExperiment(File sourceFile) {
		super(sourceFile);
	}
	
	/**
	 *Returns a random bigInteger as hash value
	 */
	@Override
	public synchronized BigInteger getMD5Signature() 
	{
		if (md5Signature == null) {
			Logger.getRootLogger().info(" [Indexing] : Computing signature");
			
		   	Random rand = new Random();
		   	md5Signature = new BigInteger(32, rand);
			    
			Logger.getRootLogger().info(" [Indexing] : Done computing signature");
		}
		return md5Signature;
	}

}
