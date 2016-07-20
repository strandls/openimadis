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

package com.strandgenomics.imaging.iclient.test;

import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * test class to demonstrate addition of different visual objects on specified overlay of specified record
 * 
 * @author Anup Kulkarni
 */
public class VAObjectAdditionTest {
	public static void main(String... args) throws InterruptedException
	{
		if (args == null || args.length == 0) {
			args = new String[] { "banerghatta", "8080", "anup", "anup123" };
		}
		String hostIP = args[0];
		int hostPort = Integer.parseInt(args[1]);
		String userName = args[2];
		String password = args[3];

		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		ispace.login(false, hostIP, hostPort, userName, password);
		
		
		List<VisualObject> vObjects = new ArrayList<VisualObject>();
		Ellipse e = new Ellipse(10, 20, 30, 60);
		vObjects.add(e);
		
		Rectangle rect = new Rectangle(0, 0, 10, 20);
		vObjects.add(rect);
		
		LineSegment l = new LineSegment(50, 70, 10, 20);
		vObjects.add(l);
		
		TextBox tb = new TextBox(0, 0, 10, 20, "test comment");
		vObjects.add(tb);
		
    	Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(1);
    	r.addVisualObjects(vObjects, "overlay", new VODimension(0,0,0));
	}
}
