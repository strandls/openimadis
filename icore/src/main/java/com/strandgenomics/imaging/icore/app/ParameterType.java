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

package com.strandgenomics.imaging.icore.app;

public enum ParameterType {
	
	BOOLEAN(Boolean.class, "boolean"),
	INTEGER(Integer.class, "int"),
	DECIMAL(Double.class, "double"),
	STRING(String.class, "String");
	
	private final String description;
	private final Class<?> clazz;
	
	private ParameterType(Class<?> clazz, String description)
	{
		this.description = description;
		this.clazz = clazz;
	}
	
	@Override
	public String toString()
	{
		return description;
	}
	
	public boolean isValidValue(Object value)
	{
		return value == null || clazz.isInstance(value);
	}
}
