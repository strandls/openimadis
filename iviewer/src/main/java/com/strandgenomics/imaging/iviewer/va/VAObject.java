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

package com.strandgenomics.imaging.iviewer.va;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;

public class VAObject extends PLayer implements Serializable{
	
	public static enum TYPE{
		ELLIPSE, RECTANGLE, TEXT, LINE, PATH;
	};

	private static final long serialVersionUID = 6891257737780898445L;

	public static final String FORMAT_ID = "imaging.migration.VisualOverlay";
	
	private static volatile int ID_GENERATOR = (int)System.currentTimeMillis();

    private int frame;
	private String name;
	private int slice;
	
	private int site;
	
	private HashMap<PNode, Integer> map;
	
	private HashMap<PNode, HashMap<String, Object>> properties;
	private long id = 0;

	private HashMap<PNode, TYPE> typeMap;
	
	public VAObject(){
		map = new HashMap<PNode, Integer>();
		typeMap = new HashMap<PNode, VAObject.TYPE>();
	}
	
	/** Called by hexff **/
	protected void initialize() {

	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VAObject other = (VAObject) obj;
		if (this.frame != other.frame)
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.slice != other.slice)
			return false;
		if(this.site != other.site)
			return false;
		return true;
	}

	/**
	 * @return the frame
	 */
	public int getFrame() {
		return this.frame;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the slice
	 */
	public int getSlice() {
		return this.slice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.frame;
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + this.slice;
		return result;
	}

	/**
	 * @param frame
	 *            the frame to set
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param slice
	 *            the slice to set
	 */
	public void setSlice(int slice) {
		this.slice = slice;
	}
	
	/**
	 * @param site
	 *            the site to set
	 */
	public void setSite(int site) {
		this.site = site;
	}
	
	public void addChild(PNode node, Integer objId){		
		if(map.containsValue(objId)){
			Iterator<PNode> it = map.keySet().iterator();
			PNode dummyNode = null;
			while(it.hasNext()){
				dummyNode = it.next();
				if(map.get(dummyNode).equals(objId))
					break;
			}
			super.removeChild(dummyNode);
			map.remove(dummyNode);
		}
		
		super.addChild(node);
		map.put(node, objId);
	}
	
	public void addChild(PNode node, int objId, TYPE type){		
		addChild(node, objId);
		typeMap.put(node, type);
	}
	
	public void addChild(PNode node, TYPE type){
		addChild(node, generateObjId());
		typeMap.put(node, type);
	}
	
	public void addChild(PNode node){
		addChild(node, generateObjId());
	}
	
	public PNode removeChild(PNode node){
		PNode returnedNode = super.removeChild(node);
		map.remove(node);
		return returnedNode;
	}
	
	public PNode removeChildById(Integer objId){
		boolean flg = false;
		Iterator<PNode> it = map.keySet().iterator();
		PNode node = null;
		while(it.hasNext()){
			node = it.next();
			if(objId.equals(map.get(node))){
				flg = true;
				break;
			}
		}
		if(flg)
			return removeChild(node);
		else 
			return null;
	}
	
	private int generateObjId(){
		return ID_GENERATOR++;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VisualObject [frame=" + this.frame + ", name=" + this.name
				+ ", slice=" + this.slice + ", site=" + this.site + "]";
	}
	
	public HashMap<PNode, TYPE> getTypeMap(){
		return typeMap;
	}
	
	public Integer getVisualObjectId(PNode pNode) {
		return map.get(pNode);
	}
	
	public PNode getVisualObject(int objId){
		Iterator<PNode> it = map.keySet().iterator();
		while(it.hasNext()){
			PNode obj = it.next();
			Integer name = map.get(obj);
			if(name == objId)
				return obj;
		}
		return null;
	}
	
	public void addProperty(PNode node, String property, Object value){
		HashMap<String, Object> p = properties.get(node);
		p.put(property, value);
		properties.put(node, p);
	}
	
	public void removeProperty(PNode node, String property){
		HashMap<String, Object> p = properties.get(node);
		p.remove(property);
		properties.put(node, p);
	}
	
	public Object getProperty(PNode node, String property){
		HashMap<String, Object> p = properties.get(node);
		if(p.containsKey(property))
			return p.get(property);
		else
			return null;
	}

	public int getSite() {
		// TODO Auto-generated method stub
		return site;
	}
}
