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
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PDimension;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.handles.PHandle;
import edu.umd.cs.piccolox.util.PLocator;

public class LineNode extends PNode{
	private static final long serialVersionUID = -4628032610533963494L;
	protected Point2D.Double pointOne;
	protected Point2D.Double pointTwo;
	protected float strokeWidth;
	protected Color strokePaint;
	
	
	public void setStrokeWidth(float strokeWidth){
		this.strokeWidth = strokeWidth;
	}
	
	public LineNode(double x1, double y1, double x2, double y2, float strokeWidth) {
		pointOne = new Point2D.Double(x1, y1);
		pointTwo = new Point2D.Double(x2, y2);
		this.strokeWidth = strokeWidth;
		setPickable(true);
		updateBounds();
		addHandles();
	}

	public void addHandles() {
		PLocator l = new PLocator() {
			private static final long serialVersionUID = -8334043653020727353L;
			public double locateX() { return pointOne.getX(); }
			public double locateY() { return pointOne.getY(); }
		};
		PHandle h = new PHandle(l) {
			private static final long serialVersionUID = -6793200987413256260L;

			public void dragHandle(PDimension aLocalDimension, PInputEvent aEvent) {
				localToParent(aLocalDimension);
				pointOne.setLocation(pointOne.getX() + aLocalDimension.getWidth(),
								     pointOne.getY() + aLocalDimension.getHeight());
				updateBounds();
				relocateHandle();
			}
		};
//		h.setVisible(false);
		addChild(h);		

		l = new PLocator() {
			private static final long serialVersionUID = 8605911834357962011L;
			public double locateX() { return pointTwo.getX(); }
			public double locateY() { return pointTwo.getY(); }
		};
		h = new PHandle(l) {
			private static final long serialVersionUID = -3207828820344379576L;

			public void dragHandle(PDimension aLocalDimension, PInputEvent aEvent) {
				localToParent(aLocalDimension);
				pointTwo.setLocation(pointTwo.getX() + aLocalDimension.getWidth(),
									 pointTwo.getY() + aLocalDimension.getHeight());
				updateBounds();
				relocateHandle();
			}
		};
//		h.setVisible(false);
		addChild(h);
	}

	public GeneralPath getAnglePath() {
		GeneralPath p = new GeneralPath();
		p.moveTo((float)pointOne.getX(), (float)pointOne.getY());
		p.lineTo((float)pointTwo.getX(), (float)pointTwo.getY());
		return p;
	}

	protected void paint(PPaintContext paintContext) {
		Graphics2D g2 = paintContext.getGraphics();
		g2.setStroke(new BasicStroke(strokeWidth));
		g2.setPaint(getPaint());
		g2.draw(getAnglePath());
	}

	public boolean setBounds(double x, double y, double width, double height) {
		return false; // bounds can be set externally
	}

	protected void updateBounds() {
		GeneralPath p = getAnglePath();
//		Rectangle2D b = stroke.createStrokedShape(p).getBounds2D();
		Rectangle2D b = p.getBounds2D();
		super.setBounds(b.getX(), b.getY(), b.getWidth(), b.getHeight());
	}

	public BasicStroke getStroke() {
		return new BasicStroke(strokeWidth);
	}

	public Color getStrokePaint() {
		return strokePaint;
	}
	
	public void setStrokePaint(Color strokePaint) {
		this.strokePaint = strokePaint;
	}
}
