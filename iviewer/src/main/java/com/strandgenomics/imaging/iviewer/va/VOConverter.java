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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Iterator;
import java.util.List;

import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iviewer.va.VAObject.TYPE;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;

public class VOConverter {
	
	public static PNode convertShapeToPiccolo(VisualObject vo, double zoom){
		Shape shape = null;
		Color color = vo.getPenColor();
		float divisor = (zoom == 0.0) ? 1.0f : (float)zoom;
		BasicStroke stroke = new BasicStroke(vo.getPenWidth()/divisor);
		Double b = vo.getBounds();
		
		if(vo instanceof Ellipse){
			shape = new Ellipse2D.Double(b.x, b.y, b.width, b.height);
			return getPPathObject(shape, color, stroke);
		} else if(vo instanceof TextBox){
			TextBox e = (TextBox)vo;

			PText path = new PText();
			path.setBounds(b.x, b.y, b.width, b.height);
			path.setText(e.getText());
			path.setPaint(e.getBkgColor());
			path.setTextPaint(color);
			
			Font f = e.getFont();
//			Font scaledFont = new Font(f.getName(), f.getStyle(), (int)(f.getSize()/divisor));
//			path.setFont(scaledFont);
			path.setFont(f);
			
			path.setPickable(true);
			
			return path;
		} else if(vo instanceof Rectangle){
			shape = new Rectangle2D.Double(b.x, b.y, b.width, b.height);
			return getPPathObject(shape, color, stroke);
		} else if(vo instanceof GeometricPath){
			GeometricPath g = (GeometricPath) vo;
			PPath squiggle = new PPath();
			
			List<Point2D> points = g.getPathPoints();
			for(int i=0;i<points.size();i++){
				double x = points.get(i).getX();
				double y = points.get(i).getY();
				if(i!=0)
					squiggle.lineTo((float)x,(float)y);
				squiggle.moveTo((float)x,(float)y);
			}
			squiggle.setStroke(stroke);
			squiggle.setStrokePaint(color);
			squiggle.setPickable(true);
			return squiggle;
		} else if(vo instanceof LineSegment){
			LineSegment e = (LineSegment)vo;
			LineNode n = new LineNode(e.startX, e.startY, e.endX, e.endY, stroke.getLineWidth());
			
			for (int i = 0; i < n.getChildrenCount(); i++) {
				n.getChild(i).setVisible(false);
			}
			n.setStrokeWidth(stroke.getLineWidth());
			n.setPaint(color);
			n.setStrokePaint(color);
			n.setPickable(true);
			return n;
		}
		return null;
	}
	
	public static VisualObject convertPiccoloToShape(PNode node, VAObject.TYPE type, double zoom){
		VisualObject e = null;
		
		PBounds b = node.getFullBounds();
		if(type == VAObject.TYPE.ELLIPSE){
			e = new Ellipse(b.x, b.y, b.width, b.height);
			Color color = (Color)((PPath)node).getStrokePaint();
			BasicStroke stroke = (BasicStroke) ((PPath)node).getStroke();
			e.setPenColor(color);
			e.setPenWidth(stroke.getLineWidth()*(float)zoom);
		} else if(type == VAObject.TYPE.RECTANGLE){
			e = new Rectangle(b.x, b.y, b.width, b.height);
			Color color = (Color)((PPath)node).getStrokePaint();
			BasicStroke stroke = (BasicStroke) ((PPath)node).getStroke();
			e.setPenColor(color);
			e.setPenWidth(stroke.getLineWidth()*(float)zoom);
		} else if(type == VAObject.TYPE.LINE){
			e = new LineSegment(b.x, b.y, b.x+b.width, b.y+b.height);
			Color color = (Color)((LineNode)node).getPaint();
			BasicStroke stroke = (BasicStroke) ((LineNode)node).getStroke();
			e.setPenColor(color);
			e.setPenWidth(stroke.getLineWidth()*(float)zoom);
		} else if(type == VAObject.TYPE.TEXT){
			e = new TextBox(b.x, b.y, b.width, b.height);
			
			Color color = (Color)((PText)node).getTextPaint();
			Color bkgColor = (Color)((PText)node).getPaint();
			Font textFont = (Font) ((PText)node).getFont();
			Font scaledFont = new Font(textFont.getName(), textFont.getStyle(), (int)(textFont.getSize()*zoom));
			String text = ((PText)node).getText();
			
			((TextBox)e).setText(text);
			e.setPenColor(color);
			e.setPenWidth(textFont.getSize()*(float)zoom);
			((TextBox)e).setFont(textFont);
			((TextBox)e).setBkgColor(bkgColor);
		} else if(type == VAObject.TYPE.PATH){
			e = new GeometricPath();
			Color color = (Color)((PPath)node).getStrokePaint();
			BasicStroke stroke = (BasicStroke) ((PPath)node).getStroke();
			e.setPenColor(color);
			e.setPenWidth(stroke.getLineWidth()*(float)zoom);
			
			PPath path = (PPath)node;
			GeneralPath gp = path.getPathReference();
			PathIterator it = gp.getPathIterator(null);
			
			while(!it.isDone()){
				double point[] = new double[6]; 
				int val = it.currentSegment(point);
				
				((GeometricPath)e).lineTo(point[0], point[1]);
				
				it.next();
			}
		}
		
		return e;
	}
	
	public static VAObject convertToVAObject(IVisualOverlay vo, double zoom)
	{
		VAObject v = new VAObject();
		VODimension voID = vo.getDimension();
		v.setFrame(voID.frameNo);
		v.setSlice(voID.sliceNo);
		v.setSite(voID.siteNo);
		v.setName(vo.getName());
		
		Iterator<VisualObject> it = vo.getVisualObjects().iterator();
		while(it.hasNext()){
			VisualObject object = it.next();
			TYPE type = getObjectType(object);
			if(object!=null)
				v.addChild(convertShapeToPiccolo(object, zoom), type);
		}
		return v;
	}
	
	private static TYPE getObjectType(VisualObject vo){
		if(vo instanceof Ellipse){
			return VAObject.TYPE.ELLIPSE;
		} else if(vo instanceof TextBox){
			return VAObject.TYPE.TEXT;
		} else if(vo instanceof Rectangle){
			return VAObject.TYPE.RECTANGLE;
		} else if(vo instanceof GeometricPath){
			return VAObject.TYPE.PATH;
		}  else if(vo instanceof LineSegment){
			return VAObject.TYPE.LINE;
		}
		return null;
	}
	
	private static PPath getPPathObject(Shape shape, Color color, BasicStroke stroke){
		PPath path = new PPath(shape);
		path.setStrokePaint(color);
		path.setPaint(new Color(0,0,0,0));
		path.setStroke(stroke);
		path.setPickable(true);
		return path;
	}
}
