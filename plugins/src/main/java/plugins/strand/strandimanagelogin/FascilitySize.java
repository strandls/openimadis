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
 * Number of microscopes in the fascility
 * 
 * @author Anup Kulkarni
 */
public enum FascilitySize {
	Small
	{
		@Override
		public String toString()
		{
			return "< 5 Microscopes";
		}
	},
	Moderate
	{
		@Override
		public String toString()
		{
			return "5 to 15 Microscopes";
		}
	},
	Large
	{
		@Override
		public String toString()
		{
			return "> 15 Microscopes";
		}
	}
}
