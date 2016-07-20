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

package com.strandgenomics.imaging.icore.bioformats.custom;

/**
 * placeholder for defining constants required by ImgFormatParser/Reader/Writer
 * 
 * @author Anup Kulkarni
 */
public class ImgFormatConstants {
	/**
	 * filename-extension separator
	 */
	public static final String EXTENSION_SEPARATOR = ".";
	/**
	 * separator by which filename and dimensions are separated
	 */
	public static final String FILENAME_SEPARATOR = "=";
	/**
	 * separator by which componenets of dimension(eg. slice,frame,channel,site) are separated
	 */
	public static final String DIMENSION_SEPARATOR = ",";
	/**
	 * file extension used by img format meta-source file
	 */
	public static final String IMG_FORMAT_EXTENSION = ".img";
	/**
	 * default record label hence the default name of the meta-source file 
	 */
	public static final String DEFAULT_RECORD_LABEL = "record";
}
