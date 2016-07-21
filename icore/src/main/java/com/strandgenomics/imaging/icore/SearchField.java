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

package com.strandgenomics.imaging.icore;


/**
 * represents a field that is searchable
 */
public class SearchField implements Storable {
	
	private static final long serialVersionUID = 3703997754758782349L;
	/**
	 * name of the navigable field
	 */
	public final String fieldName;
	/**
	 * type of the field
	 */
	public final AnnotationType fieldType;
	
	/**
	 * Create a record field with a name and type 
	 * @param fieldName name of the field
	 * @param fieldType type of the field
	 */
	public SearchField(String fieldName, AnnotationType fieldType)
	{
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}
	
	/**
	 * name of the search field
	 * @return name of the search field
	 */
	public String getField()
	{
		return this.fieldName;
	}
	
	/**
	 * type of the search field
	 * @return type of the search field
	 */
	public AnnotationType getType()
	{
		return this.fieldType;
	}
	
	@Override
	public String toString()
	{
		return this.fieldName;
	}
	
	@Override
	public int hashCode()
	{
		return this.fieldName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof SearchField)
		{
			SearchField that = (SearchField)obj;
			if(this == that) return true;
			return (this.fieldName.equals(that.fieldName) && this.fieldType.equals(that.fieldType));
		}
		return false;
	}

	@Override
	public void dispose() 
	{
	}
	
	/**
	 * Returns true iff the field represents a text value
	 * @return true iff the field represents a text value
	 */
	public boolean isText()
	{
		return fieldType.equals(AnnotationType.Text);
	}
	
	/**
	 * Returns true iff the field represents a real value
	 * @return true iff the field represents a real value
	 */
	public boolean isReal()
	{
		return fieldType.equals(AnnotationType.Real);
	}
	
	/**
	 * Returns true iff the field represents a integral value
	 * @return true iff the field represents a integral value
	 */
	public boolean isIntegral()
	{
		return fieldType.equals(AnnotationType.Integer);
	}
	
	/**
	 * Returns true iff the field represents a date value
	 * @return true iff the field represents a date value
	 */
	public boolean isDateTime()
	{
		return fieldType.equals(AnnotationType.Time);
	}
}
