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
 * Represents a channel of a record
 * @author arunabha
 */
public interface IChannel {
	
	/**
	 * Returns the name of this channel
	 * @return the name of this channel
	 */
	public String getName();
	
	/**
	 * Returns the wavelength of the channel light source
	 * @return wavelength in micrometre if set, else -1
	 */
	public int getWavelength();
	
	/**
	 * Returns the lut name associated with this channel
	 * @return the lut name associated with this channel
	 */
	public String getLut();
	
    /**
     * Returns the custom contrast, if any, for all images of this channel of the owning record
     * @param zStacked true if zStacked contrast, false otherwise
     * @return the custom contrast, if any, for all images of this channel of the owning record, otherwise null
     */
    public VisualContrast getContrast(boolean zStacked);
}
