/*

   Copyright 2000-2001,2003  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

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
