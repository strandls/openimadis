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

package com.strandgenomics.imaging.iserver.services.def.loader;


/**
 * Defines the context of the client when he upload a bunch of source files (as a tar-ball)
 * to upload a multi-series record (experiment)
 * @author arunabha
 *
 */
public class CreationRequest {
	
	/**
	 * List of client side source files that is actually transferred (as the tar ball)
	 */
	private Archive archive = null;
	/**
	 * MAC address of the client machine
	 */
	private String clientMacAddress = null;
	/**
	 * list of signatures (or record finger-prints) to extract, can be null
	 */
	private RecordSpecification[] specifications = null;
	
	public CreationRequest()
	{}
	
	public Archive getArchive() 
	{
		return archive;
	}

	public void setArchive(Archive archive) 
	{
		this.archive = archive;
	}


	public String getClientMacAddress()
	{
		return clientMacAddress;
	}

	public void setClientMacAddress(String clientMacAddress) 
	{
		this.clientMacAddress = clientMacAddress;
	}
	
	public RecordSpecification[] getValidSignatures()
	{
		return specifications;
	}
	
	public void setValidSignatures(RecordSpecification[] value)
	{
		specifications = value;
	}
}
