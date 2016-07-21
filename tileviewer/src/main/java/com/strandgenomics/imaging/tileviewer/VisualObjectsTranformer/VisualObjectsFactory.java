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

package com.strandgenomics.imaging.tileviewer.VisualObjectsTranformer;

/**
 * Provider of all visual object transformers.
 * @author navneet
 *
 */
public class VisualObjectsFactory {
	/**
	 * used for synchronizing the creation transformer objects
	 */
	private static Object padLock = new Object();
	/**
	 * singleton instance of the kinetic Transformer
	 */
	private static VisualObjectTransformer visualObjectTransformer = null;
	

	/**
	 * Returns the singleton instance of visualObjectTransformer 
	 * @return the singleton instance of visualObjectTransformer 
	 */
	@SuppressWarnings("rawtypes")
	public static VisualObjectTransformer getVisualObjectTransformer(String transformerType)
	{
		if(visualObjectTransformer == null)
		{
			synchronized(padLock)
			{
				if(visualObjectTransformer == null)
				{
					VisualObjectTransformer transformer = null;
					
					if(transformerType.equals("kinetic"))
						transformer= new KineticTransformer();				

					visualObjectTransformer = transformer;
				}
			}
		}
		return visualObjectTransformer;
	}
}
