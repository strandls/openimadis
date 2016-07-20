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

package plugins.strand.strandimanagelogin;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class UserInformation{
	
	/**
	 * login of the user
	 */
	private String login;
	/**
	 * password of the user
	 */
	private String password;
	/**
	 * full name of the user
	 */
	private String fullName;
	/**
	 * email of the user
	 */
	private String email;
	/**
	 * work profile of the user
	 */
	private WorkProfile workProfile;
	/**
	 * name of the fascility
	 */
	private String fascilityName;
	/**
	 * number of microscopes in the fascility
	 */
	private FascilitySize sizeOfFascility;
	/**
	 * size of the data in TB
	 */
	private int dataSizePerYear;
	
	public UserInformation(String login, String password, String fullName, String email, WorkProfile workProfile, String fascilityName, FascilitySize sizeOfFascility, int dataSize)
	{
		this.login = login;
		this.fullName = fullName;
		this.email = email;
		this.workProfile = workProfile;
		this.fascilityName = fascilityName;
		this.sizeOfFascility = sizeOfFascility;
		this.dataSizePerYear = dataSize;
		this.password = password;
	}
	
	public String getPassword()
	{
		return password;
	}

	public String getLogin()
	{
		return login;
	}

	public String getFullName()
	{
		return fullName;
	}

	public String getEmail()
	{
		return email;
	}

	public WorkProfile getWorkProfile()
	{
		return workProfile;
	}

	public String getFascilityName()
	{
		return fascilityName;
	}

	public FascilitySize getSizeOfFascility()
	{
		return sizeOfFascility;
	}

	public int getDataSizePerYear()
	{
		return dataSizePerYear;
	}
}
