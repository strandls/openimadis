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

package com.strandgenomics.imaging.icore.bioformats;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FileInfo;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ReaderWrapper;
import loci.formats.gui.BufferedImageReader;
import loci.formats.in.MetadataLevel;
import loci.formats.in.MetadataOptions;
import loci.formats.meta.MetadataStore;

/**
 * maintains a cache of BufferedImageReader instances
 * @author arunabha
 *
 */
public class WrappedBufferedImageReader extends BufferedImageReader {
	
	static volatile long counter = 1;
	
	public final long ID;
	
	/**
	 * the actual BufferedImageReader powering the APIs
	 */
	protected BufferedImageReader actualReader = null;
	/**
	 * the origin pool
	 */
	protected final BufferedImageReaderPool pool;
	/**
	 * last usage time
	 */
	private long lastUsageTime;
	
	/**
	 * Creates a wrapped BufferedImageReader that is linked with the specified pool
	 * @param pool the parent pool
	 * @param imageReader the actual BufferedImageReader instance
	 */
	WrappedBufferedImageReader(BufferedImageReaderPool pool, BufferedImageReader imageReader) 
	{
		this.pool = pool;
		this.actualReader = imageReader;
		ID = counter++;
	}
	
	/**
	 * sets last usage time of the reader
	 * @param lastUsageTime
	 */
	public void setLastUsageTime(long lastUsageTime)
	{
		this.lastUsageTime = lastUsageTime;
	}
	
	/**
	 * returns last usage time of the reader
	 * @return last usage time of the reader
	 */
	public long getLastUsageTime()
	{
		return this.lastUsageTime;
	}

	/**
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#close()
	 */
	public void close() throws IOException 
	{
		try
		{
			pool.returnReader(this);
		}
		catch(Exception ex)
		{
			try 
			{
				actualReader.close();
			} 
			catch (IOException e)
			{}
		}
	}

	/**
	 * @param fileOnly
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#close(boolean)
	 */
	public void close(boolean fileOnly) throws IOException 
	{
		try
		{
			pool.returnReader(this);
		}
		catch(Exception ex)
		{
			try 
			{
				actualReader.close(fileOnly);
			} 
			catch (IOException e)
			{}
		}
	}

	/**
	 * @param imageReaderClass
	 * @return
	 * @throws FormatException
	 * @see loci.formats.ReaderWrapper#duplicate(java.lang.Class)
	 */
	public ReaderWrapper duplicate(
			Class<? extends IFormatReader> imageReaderClass)
			throws FormatException {
		return actualReader.duplicate(imageReaderClass);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return actualReader.equals(obj);
	}

	/**
	 * @param id
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#fileGroupOption(java.lang.String)
	 */
	public int fileGroupOption(String id) throws FormatException, IOException {
		return actualReader.fileGroupOption(id);
	}

	/**
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#get16BitLookupTable()
	 */
	public short[][] get16BitLookupTable() throws FormatException, IOException {
		return actualReader.get16BitLookupTable();
	}

	/**
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#get8BitLookupTable()
	 */
	public byte[][] get8BitLookupTable() throws FormatException, IOException {
		return actualReader.get8BitLookupTable();
	}

	/**
	 * @param noPixels
	 * @return
	 * @see loci.formats.ReaderWrapper#getAdvancedSeriesUsedFiles(boolean)
	 */
	public FileInfo[] getAdvancedSeriesUsedFiles(boolean noPixels) {
		return actualReader.getAdvancedSeriesUsedFiles(noPixels);
	}

	/**
	 * @param noPixels
	 * @return
	 * @see loci.formats.ReaderWrapper#getAdvancedUsedFiles(boolean)
	 */
	public FileInfo[] getAdvancedUsedFiles(boolean noPixels) {
		return actualReader.getAdvancedUsedFiles(noPixels);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getBitsPerPixel()
	 */
	public int getBitsPerPixel() {
		return actualReader.getBitsPerPixel();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getChannelDimLengths()
	 
	public int[] getChannelDimLengths() {
		return actualReader.getChannelDimLengths();
	}*/

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getChannelDimTypes()
	 
	public String[] getChannelDimTypes() {
		return actualReader.getChannelDimTypes();
	}*/

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getCoreMetadata()
	 
	public CoreMetadata[] getCoreMetadata() {
		return actualReader.getCoreMetadata();
	}*/

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getCurrentFile()
	 */
	public String getCurrentFile() {
		return actualReader.getCurrentFile();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getDimensionOrder()
	 */
	public String getDimensionOrder() {
		return actualReader.getDimensionOrder();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getDomains()
	 */
	public String[] getDomains() {
		return actualReader.getDomains();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getEffectiveSizeC()
	 */
	public int getEffectiveSizeC() {
		return actualReader.getEffectiveSizeC();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getFormat()
	 */
	public String getFormat() {
		return actualReader.getFormat();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getGlobalMetadata()
	 */
	public Hashtable<String, Object> getGlobalMetadata() {
		return actualReader.getGlobalMetadata();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getImageCount()
	 */
	public int getImageCount() {
		return actualReader.getImageCount();
	}

	/**
	 * @param z
	 * @param c
	 * @param t
	 * @return
	 * @see loci.formats.ReaderWrapper#getIndex(int, int, int)
	 */
	public int getIndex(int z, int c, int t) {
		return actualReader.getIndex(z, c, t);
	}

	/**
	 * @return
	 * @deprecated
	 * @see loci.formats.ReaderWrapper#getMetadata()
	 
	public Hashtable<String, Object> getMetadata() {
		return actualReader.getMetadata();
	}*/

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getMetadataOptions()
	 */
	public MetadataOptions getMetadataOptions() {
		return actualReader.getMetadataOptions();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getMetadataStore()
	 */
	public MetadataStore getMetadataStore() {
		return actualReader.getMetadataStore();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getMetadataStoreRoot()
	 */
	public Object getMetadataStoreRoot() {
		return actualReader.getMetadataStoreRoot();
	}

	/**
	 * @param field
	 * @return
	 * @see loci.formats.ReaderWrapper#getMetadataValue(java.lang.String)
	 */
	public Object getMetadataValue(String field) {
		return actualReader.getMetadataValue(field);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getNativeDataType()
	 */
	public Class<?> getNativeDataType() {
		return actualReader.getNativeDataType();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getOptimalTileHeight()
	 */
	public int getOptimalTileHeight() {
		return actualReader.getOptimalTileHeight();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getOptimalTileWidth()
	 */
	public int getOptimalTileWidth() {
		return actualReader.getOptimalTileWidth();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getPixelType()
	 */
	public int getPixelType() {
		return actualReader.getPixelType();
	}

	/**
	 * @param id
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#getPossibleDomains(java.lang.String)
	 */
	public String[] getPossibleDomains(String id) throws FormatException,
			IOException {
		return actualReader.getPossibleDomains(id);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getRGBChannelCount()
	 */
	public int getRGBChannelCount() {
		return actualReader.getRGBChannelCount();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getReader()
	 */
	public IFormatReader getReader() {
		return actualReader.getReader();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSeries()
	 */
	public int getSeries() {
		return actualReader.getSeries();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSeriesCount()
	 */
	public int getSeriesCount() {
		return actualReader.getSeriesCount();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSeriesMetadata()
	 */
	public Hashtable<String, Object> getSeriesMetadata() {
		return actualReader.getSeriesMetadata();
	}

	/**
	 * @param field
	 * @return
	 * @see loci.formats.ReaderWrapper#getSeriesMetadataValue(java.lang.String)
	 */
	public Object getSeriesMetadataValue(String field) {
		return actualReader.getSeriesMetadataValue(field);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSeriesUsedFiles()
	 */
	public String[] getSeriesUsedFiles() {
		return actualReader.getSeriesUsedFiles();
	}

	/**
	 * @param noPixels
	 * @return
	 * @see loci.formats.ReaderWrapper#getSeriesUsedFiles(boolean)
	 */
	public String[] getSeriesUsedFiles(boolean noPixels) {
		return actualReader.getSeriesUsedFiles(noPixels);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSizeC()
	 */
	public int getSizeC() {
		return actualReader.getSizeC();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSizeT()
	 */
	public int getSizeT() {
		return actualReader.getSizeT();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSizeX()
	 */
	public int getSizeX() {
		return actualReader.getSizeX();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSizeY()
	 */
	public int getSizeY() {
		return actualReader.getSizeY();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSizeZ()
	 */
	public int getSizeZ() {
		return actualReader.getSizeZ();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSuffixes()
	 */
	public String[] getSuffixes() {
		return actualReader.getSuffixes();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getSupportedMetadataLevels()
	 */
	public Set<MetadataLevel> getSupportedMetadataLevels() {
		return actualReader.getSupportedMetadataLevels();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getThumbSizeX()
	 */
	public int getThumbSizeX() {
		return actualReader.getThumbSizeX();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getThumbSizeY()
	 */
	public int getThumbSizeY() {
		return actualReader.getThumbSizeY();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getUnderlyingReaders()
	 */
	public IFormatReader[] getUnderlyingReaders() {
		return actualReader.getUnderlyingReaders();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#getUsedFiles()
	 */
	public String[] getUsedFiles() {
		return actualReader.getUsedFiles();
	}

	/**
	 * @param noPixels
	 * @return
	 * @see loci.formats.ReaderWrapper#getUsedFiles(boolean)
	 */
	public String[] getUsedFiles(boolean noPixels) {
		return actualReader.getUsedFiles(noPixels);
	}

	/**
	 * @param index
	 * @return
	 * @see loci.formats.ReaderWrapper#getZCTCoords(int)
	 */
	public int[] getZCTCoords(int index) {
		return actualReader.getZCTCoords(index);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#hasCompanionFiles()
	 */
	public boolean hasCompanionFiles() {
		return actualReader.hasCompanionFiles();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return actualReader.hashCode();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isFalseColor()
	 */
	public boolean isFalseColor() {
		return actualReader.isFalseColor();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isGroupFiles()
	 */
	public boolean isGroupFiles() {
		return actualReader.isGroupFiles();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isIndexed()
	 */
	public boolean isIndexed() {
		return actualReader.isIndexed();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isInterleaved()
	 */
	public boolean isInterleaved() {
		return actualReader.isInterleaved();
	}

	/**
	 * @param subC
	 * @return
	 * @see loci.formats.ReaderWrapper#isInterleaved(int)
	 */
	public boolean isInterleaved(int subC) {
		return actualReader.isInterleaved(subC);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isLittleEndian()
	 */
	public boolean isLittleEndian() {
		return actualReader.isLittleEndian();
	}

	/**
	 * @return
	 * @deprecated
	 * @see loci.formats.ReaderWrapper#isMetadataCollected()
	 
	public boolean isMetadataCollected() {
		return actualReader.isMetadataCollected();
	}*/

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isMetadataComplete()
	 */
	public boolean isMetadataComplete() {
		return actualReader.isMetadataComplete();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isMetadataFiltered()
	 */
	public boolean isMetadataFiltered() {
		return actualReader.isMetadataFiltered();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isNormalized()
	 */
	public boolean isNormalized() {
		return actualReader.isNormalized();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isOrderCertain()
	 */
	public boolean isOrderCertain() {
		return actualReader.isOrderCertain();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isOriginalMetadataPopulated()
	 */
	public boolean isOriginalMetadataPopulated() {
		return actualReader.isOriginalMetadataPopulated();
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isRGB()
	 */
	public boolean isRGB() {
		return actualReader.isRGB();
	}

	/**
	 * @param id
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#isSingleFile(java.lang.String)
	 */
	public boolean isSingleFile(String id) throws FormatException, IOException {
		return actualReader.isSingleFile(id);
	}

	/**
	 * @param block
	 * @return
	 * @see loci.formats.ReaderWrapper#isThisType(byte[])
	 */
	public boolean isThisType(byte[] block) {
		return actualReader.isThisType(block);
	}

	/**
	 * @param stream
	 * @return
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#isThisType(loci.common.RandomAccessInputStream)
	 */
	public boolean isThisType(RandomAccessInputStream stream)
			throws IOException {
		return actualReader.isThisType(stream);
	}

	/**
	 * @param name
	 * @param open
	 * @return
	 * @see loci.formats.ReaderWrapper#isThisType(java.lang.String, boolean)
	 */
	public boolean isThisType(String name, boolean open) {
		return actualReader.isThisType(name, open);
	}

	/**
	 * @param name
	 * @return
	 * @see loci.formats.ReaderWrapper#isThisType(java.lang.String)
	 */
	public boolean isThisType(String name) {
		return actualReader.isThisType(name);
	}

	/**
	 * @return
	 * @see loci.formats.ReaderWrapper#isThumbnailSeries()
	 */
	public boolean isThumbnailSeries() {
		return actualReader.isThumbnailSeries();
	}

	/**
	 * @param no
	 * @param buf
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#openBytes(int, byte[], int, int, int, int)
	 */
	public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
			throws FormatException, IOException {
		return actualReader.openBytes(no, buf, x, y, w, h);
	}

	/**
	 * @param no
	 * @param buf
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#openBytes(int, byte[])
	 */
	public byte[] openBytes(int no, byte[] buf) throws FormatException,
			IOException {
		return actualReader.openBytes(no, buf);
	}

	/**
	 * @param no
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#openBytes(int, int, int, int, int)
	 */
	public byte[] openBytes(int no, int x, int y, int w, int h)
			throws FormatException, IOException {
		return actualReader.openBytes(no, x, y, w, h);
	}

	/**
	 * @param no
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#openBytes(int)
	 */
	public byte[] openBytes(int no) throws FormatException, IOException {
		return actualReader.openBytes(no);
	}

	/**
	 * @param no
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.gui.BufferedImageReader#openImage(int, int, int, int, int)
	 */
	public BufferedImage openImage(int no, int x, int y, int w, int h)
			throws FormatException, IOException {
		return actualReader.openImage(no, x, y, w, h);
	}

	/**
	 * @param no
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.gui.BufferedImageReader#openImage(int)
	 */
	public BufferedImage openImage(int no) throws FormatException, IOException {
		return actualReader.openImage(no);
	}

	/**
	 * @param no
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#openPlane(int, int, int, int, int)
	 */
	public Object openPlane(int no, int x, int y, int w, int h)
			throws FormatException, IOException {
		return actualReader.openPlane(no, x, y, w, h);
	}

	/**
	 * @param no
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#openThumbBytes(int)
	 */
	public byte[] openThumbBytes(int no) throws FormatException, IOException {
		return actualReader.openThumbBytes(no);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.gui.BufferedImageReader#openThumbImage(int)
	 */
	public BufferedImage openThumbImage(int arg0) throws FormatException,
			IOException {
		return actualReader.openThumbImage(arg0);
	}

	/**
	 * @param group
	 * @see loci.formats.ReaderWrapper#setGroupFiles(boolean)
	 */
	public void setGroupFiles(boolean group) {
		actualReader.setGroupFiles(group);
	}

	/**
	 * @param id
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#setId(java.lang.String)
	 */
	public void setId(String id) throws FormatException, IOException {
		actualReader.setId(id);
	}

	/**
	 * @param collect
	 * @deprecated
	 * @see loci.formats.ReaderWrapper#setMetadataCollected(boolean)
	 
	public void setMetadataCollected(boolean collect) {
		actualReader.setMetadataCollected(collect);
	}*/

	/**
	 * @param filter
	 * @see loci.formats.ReaderWrapper#setMetadataFiltered(boolean)
	 */
	public void setMetadataFiltered(boolean filter) {
		actualReader.setMetadataFiltered(filter);
	}

	/**
	 * @param options
	 * @see loci.formats.ReaderWrapper#setMetadataOptions(loci.formats.in.MetadataOptions)
	 */
	public void setMetadataOptions(MetadataOptions options) {
		actualReader.setMetadataOptions(options);
	}

	/**
	 * @param store
	 * @see loci.formats.ReaderWrapper#setMetadataStore(loci.formats.meta.MetadataStore)
	 */
	public void setMetadataStore(MetadataStore store) {
		actualReader.setMetadataStore(store);
	}

	/**
	 * @param normalize
	 * @see loci.formats.ReaderWrapper#setNormalized(boolean)
	 */
	public void setNormalized(boolean normalize) {
		actualReader.setNormalized(normalize);
	}

	/**
	 * @param populate
	 * @see loci.formats.ReaderWrapper#setOriginalMetadataPopulated(boolean)
	 */
	public void setOriginalMetadataPopulated(boolean populate) {
		actualReader.setOriginalMetadataPopulated(populate);
	}

	/**
	 * @param no
	 * @see loci.formats.ReaderWrapper#setSeries(int)
	 */
	public void setSeries(int no) {
		actualReader.setSeries(no);
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return actualReader.toString();
	}

	/**
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#unwrap()
	 */
	public IFormatReader unwrap() throws FormatException, IOException {
		return actualReader.unwrap();
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#unwrap(java.lang.Class, java.lang.String)
	 */
	public IFormatReader unwrap(Class<? extends IFormatReader> arg0, String arg1)
			throws FormatException, IOException {
		return actualReader.unwrap(arg0, arg1);
	}

	/**
	 * @param id
	 * @return
	 * @throws FormatException
	 * @throws IOException
	 * @see loci.formats.ReaderWrapper#unwrap(java.lang.String)
	 */
	public IFormatReader unwrap(String id) throws FormatException, IOException {
		return actualReader.unwrap(id);
	}
}
