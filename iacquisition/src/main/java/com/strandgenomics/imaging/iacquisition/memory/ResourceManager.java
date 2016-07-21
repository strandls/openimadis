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

//package org.apache.batik.util.gui;
package com.strandgenomics.imaging.iacquisition.memory;

import java.util.HashMap;
import java.util.Map;

// XXX Anand: hack to make code working
public class ResourceManager {
    Map map = new HashMap();
    
    public ResourceManager(Object dummy) {
	map.put("Frame.title", 		"Memory Monitor");
	map.put("Frame.border_title", 	"Memory Usage & History");

	map.put("CollectButton.text", 	"Collect");
	map.put("CollectButton.mnemonic", 	"l");
	map.put("CollectButton.tooltip", 	"Collect the unused memory");
	map.put("CollectButton.action", 	"CollectButtonAction");

	map.put("CloseButton.text", 	"Close");
	map.put("CloseButton.mnemonic", 	"C");
	map.put("CloseButton.tooltip", 	"Close the memory monitor frame");
	map.put("CloseButton.action", 	"CloseButtonAction");

	map.put("Usage.units", 		"K");
	map.put("Usage.total", 		"total");
	map.put("Usage.used", 		"used");

	map.put("Usage.postfix", 		"true");
    }

    public String getString(String key) {
	return (String) map.get(key);
    }

    public boolean getBoolean(String key) {
	return Boolean.getBoolean(getString(key));
    }
}
