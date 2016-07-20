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

package com.strandgenomics.imaging.iengine.system;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.models.MosaicParameters;
import com.strandgenomics.imaging.iengine.models.MosaicRequest;
import com.strandgenomics.imaging.iengine.models.MosaicResource;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.icore.image.PixelArray.Integer;

/**
 * manages mosaic images
 * Mosaic Manager is given a feed, which it interprets to 
 * generate a mosaic resource. 
 * @author navneet
 *
 */
public class MosaicManager extends SystemManager {
	
	/**
	 * gives a resource which represents the mosaic image
	 * @return
	 * @throws IOException 
	 */
	public MosaicResource getMosaicResource(String actorLogin, MosaicRequest feed) throws IOException{
		
		MosaicResource resource = new MosaicResource();
		
		long recordids[] = feed.getRecordids();
		
		List<Record> records = new ArrayList<Record>();
		
		for(long recordid : recordids){
			records.add(SysManagerFactory.getRecordManager().findRecord(actorLogin, recordid));
		}
		
		// logic to generate every parameter in mosaic resource
		
		// min_x and min_y among all the records constituting this mosaic image will
		// be the left and top anchor of the resultant mosaic image. All records will be arranged
		// in resultant image according to this top and left anchor
		
		// max_x-min_x will be the width of resultant mosaic image
		// max_y-min_y will be the height of resultant mosaic image
		
		Dimension imageCoordinate = new Dimension(0, 0, 0, 0);
		
		double min_x = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, records.get(0).guid, imageCoordinate).getX();
		double min_y = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, records.get(0).guid, imageCoordinate).getY();
		
		double max_x = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, records.get(0).guid, imageCoordinate).getX()+ SysManagerFactory.getRecordManager().findRecord(actorLogin, records.get(0).guid).imageWidth;
		double max_y = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, records.get(0).guid, imageCoordinate).getY()+ SysManagerFactory.getRecordManager().findRecord(actorLogin, records.get(0).guid).imageHeight;
		
		for(Record record : records){
			double x_coordinate = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, record.guid, imageCoordinate).getX();
			double y_coordinate = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, record.guid, imageCoordinate).getY();
			
			min_x = Math.min(min_x, x_coordinate);
			min_y = Math.min(min_y, y_coordinate);
			
			max_x = Math.max(max_x, x_coordinate+ SysManagerFactory.getRecordManager().findRecord(actorLogin, record.guid).imageWidth);
			max_y = Math.max(max_y, y_coordinate+ SysManagerFactory.getRecordManager().findRecord(actorLogin, record.guid).imageHeight);
		}
		
		resource.setRecordids(recordids);
		resource.setAnchor_top((int)Math.floor(min_y));
		resource.setAnchor_left((int)Math.floor(min_x));
		resource.setMosaicImageWidth((int)Math.floor(max_x-min_x));
		resource.setMosiacImageHeight((int)Math.floor(max_y-min_y));
		
		return resource;
	}
	
	/**
	 * gives the requested element from specified resource
	 * The element is a sub image of resultant mosaic image which is identified by mosaic resource
	 * @param resource
	 * @return
	 * @throws IOException 
	 */
	public BufferedImage getMosaicElement(String actorLogin, MosaicResource resource, MosaicParameters element) throws IOException{
		
		logger.logp(Level.INFO,"","getMosaicElement","element="+element.getX()+" "+element.getY());
		
		List<Record> records = new ArrayList<Record>();
		
		for(long recordid : resource.getRecordids()){
			logger.logp(Level.INFO,"","getMosaicElement","record="+recordid);
			records.add(SysManagerFactory.getRecordManager().findRecord(actorLogin, recordid));
		}
		
		//given a mosaic resource and asked for a specific mosaic element will return a image 
		//formed using the contributing record.
		//Let say the resultant mosaic image is W X H. Asking for an element starting at coordinate(present in image metadata)
		//X & Y and of dimension w&h would look for records which are contributing to this portion on resultant mosaic image.
		//Then sub images from each of these contributing records will be stitched to form the required element of mosaic image.
		
		//This is whole mosaic image of W X H formed using several records.
		//The rectangle starting at X,Y and of dimension wXh has portion of several individual images.
//		____________W_______
//		|    X,Y|		|  |
//		|		|		|  |
//		|		|w      |  |H
//		|		|__h____|  |
//		|                  |
//		|__________________|
		
		
		
		int tileWidth = element.getTileWidth();
		int tileHeight = element.getTileHeight();
		
		Dimension imageCoordinate = new Dimension(0, 0, 0, 0);
		
		int X,Y, requiredWidth, requiredHeight;
		
		X = element.getX();
		Y = element.getY();
		
		requiredWidth = element.getWidth();
		requiredHeight = element.getHeight();
		
		BufferedImage tileImage = new BufferedImage(requiredWidth, requiredHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gfx = tileImage.createGraphics();
		
		List<Record> contributingRecords = new ArrayList<Record>();
		
		for(Record record : records){
			
			double x_coordinate = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, record.guid, imageCoordinate).getX();
			double y_coordinate = SysManagerFactory.getImageManager().getImageMetaData(actorLogin, record.guid, imageCoordinate).getY();
			
			if((x_coordinate<=X+requiredWidth && y_coordinate<=Y+requiredHeight)&&(x_coordinate+record.imageWidth>X && y_coordinate+record.imageHeight> Y)){
				
				contributingRecords.add(record);
				System.out.println(record.guid);
			}
		}
		
		for(Record contributingRecord : contributingRecords){
			BufferedImage tileSubimage = getSubimage(actorLogin, resource, contributingRecord, X, Y, requiredWidth, requiredHeight);
			
			System.out.println(((int)SysManagerFactory.getImageManager().getImageMetaData(actorLogin, contributingRecord.guid, imageCoordinate).getX()-resource.getAnchor_left())+" "+ ((int)SysManagerFactory.getImageManager().getImageMetaData(actorLogin, contributingRecord.guid, imageCoordinate).getY()-resource.getAnchor_top())+" "+tileSubimage.getWidth()+" "+tileSubimage.getHeight());
			gfx.drawImage(tileSubimage, (int)SysManagerFactory.getImageManager().getImageMetaData(actorLogin, contributingRecord.guid, imageCoordinate).getX()-X, (int)SysManagerFactory.getImageManager().getImageMetaData(actorLogin, contributingRecord.guid, imageCoordinate).getY()-Y, null);
		}
		
		gfx.dispose();
		
		ImageIO.write(tileImage,"png",new File("/home/shaik/imanage_storage/mosaictiles/"+requiredHeight));
		
		return Util.resizeImage(tileImage, element.getTileWidth(), element.getTileHeight());
	}
	
	/**
	 * gets a sub image for a given record which is part of mosaic resource
	 * This sub image is obtained using the x and y coordinates of target record
	 * @param actorLogin
	 * @param resource
	 * @param record
	 * @param X
	 * @param Y
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 * @throws IOException
	 */
	private BufferedImage getSubimage(String actorLogin, MosaicResource resource, Record record, int X, int Y, int maxWidth, int maxHeight) throws IOException{
		
		//given X&Y coordinate, this will give the subimage from contributing record
		//these X&Y are coordinates in image metadata.
		
		Dimension imageCoordinate = new Dimension(0, 0, 0, 0);
		
		int recordX =  (int) SysManagerFactory.getImageManager().getImageMetaData(actorLogin, record.guid, imageCoordinate).getX();
		int recordY = (int) SysManagerFactory.getImageManager().getImageMetaData(actorLogin, record.guid, imageCoordinate).getY();
		
		int recordWidth = record.imageWidth;
		int recordHeight = record.imageHeight;
		
		int requiredWidth = recordX+recordWidth < X+maxWidth ? recordWidth : X+maxWidth-recordX;
		int requiredHeight = recordY+recordHeight < Y+maxHeight ? recordHeight : Y+maxHeight-recordY;
		
		int requiredX = recordX<X ? X : recordX;
		int requiredY = recordY<Y ? Y : recordY;
		
		Rectangle requiredRectangle = new Rectangle(requiredX-recordX, requiredY-recordY, requiredWidth, requiredHeight);
		
		int channels[] = new int[record.getChannelCount()];
		for(int i=0;i<record.getChannelCount();i++){
			channels[i] = i;
		}
		
		PixelArray requiredPixelArray = SysManagerFactory.getImageDataDownloadManager().createOverlayPixelArray(actorLogin, record.guid, 0, 0, 0,channels ,true, false, false, requiredRectangle);
		
		Integer pixelArrayIntFormat = requiredPixelArray.getRGBPixels(requiredPixelArray);
		System.out.println("bytes array length:"+pixelArrayIntFormat.getBytes().length);
		System.out.println("expected array length:"+requiredPixelArray.getWidth()*requiredPixelArray.getHeight());
		return PixelArray.getRGBAImage(requiredPixelArray.getWidth(), requiredPixelArray.getHeight(), (int[]) pixelArrayIntFormat.getPixelArray());
		//return PixelArray.getRGBAImage(requiredPixelArray.getWidth(), requiredPixelArray.getHeight(), (int[]) requiredPixelArray.getPixelArray());
	}
	
	/**
	 * return mosaic element download url
	 * @param actorLogin
	 * @param clientIPAddress
	 * @param resource
	 * @param params
	 * @return
	 */
	public String getMosaicElementDownloadUrl(String actorLogin, String clientIPAddress, MosaicResource resource, MosaicParameters params){
		
		List<Object> parameters = new ArrayList<Object>();
		
		parameters.add(actorLogin);
		
		parameters.add(resource.getRecordids().length);
		for(long id : resource.getRecordids())
			parameters.add(id);
		
		parameters.add(resource.getAnchor_left());
		parameters.add(resource.getAnchor_top());
		
		parameters.add(params.getX());
		parameters.add(params.getY());
		parameters.add(params.getWidth());
		parameters.add(params.getHeight());
		parameters.add(params.getTileWidth());
		parameters.add(params.getTileHeight());
		
		return DataExchange.MOSAIC.generateURL(actorLogin, clientIPAddress, System.currentTimeMillis(), parameters.toArray());
	}
	
}
