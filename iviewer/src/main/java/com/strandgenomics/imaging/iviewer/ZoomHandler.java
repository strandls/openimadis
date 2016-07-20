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

package com.strandgenomics.imaging.iviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PBounds;

public class ZoomHandler extends PDragSequenceEventHandler{

	protected ImageCanvas parentCanvas;
	
	protected PPath marquee = null;

	private PNode marqueeParent = null;
	protected Point2D presspt = null;
	static final int DASH_WIDTH = 5;
	private float strokeNum = 0;
	static final int NUM_STROKES = 10;
	private Stroke[] strokes = null;

	private PBounds viewBounds;

	public ZoomHandler(PNode marqueeParent, ImageCanvas parentCanvas) {
		super();
		this.marqueeParent = marqueeParent;
		this.parentCanvas = parentCanvas;
		
		final float[] dash = new float[2];
		dash[0] = DASH_WIDTH;
		dash[1] = DASH_WIDTH;
		
        strokes = new Stroke[NUM_STROKES];
        for (int i = 0; i < NUM_STROKES; i++) {
            strokes[i] = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, dash, i);
        }
	}

	/**
	 * Updates the marquee to the new bounds caused by the drag.
	 * 
	 * @param event
	 *            drag event
	 */
	protected void drag(final PInputEvent event) {
		super.drag(event);
		updateMarquee(event);
	}

	/**
	 * Ends the selection marquee when the drag is ended.
	 * 
	 * @param event
	 *            the event responsible for ending the drag
	 */
	protected void endDrag(final PInputEvent event) {
		super.endDrag(event);
		endMarqueeSelection(event);
	}

	protected void endMarqueeSelection(final PInputEvent e) {
		bandUpdated(marquee);
		marquee.removeFromParent();
		marquee = null;
	}

	/**
	 * Creates an empty marquee child for use in displaying the marquee
	 * around the selection.
	 * 
	 * @param event
	 *            event responsible for the initialization
	 */
	protected void initializeMarquee(final PInputEvent event) {
		marquee = PPath.createRectangle((float) presspt.getX(),
				(float) presspt.getY(), 0, 0);
		marquee.setPaint(null);
		marquee.setTransparency(1.0f);
		marquee.setStrokePaint(Color.gray);
		marquee.setStroke(strokes[0]);
		marqueeParent.addChild(marquee);

	}
	
	protected void initializeSelection(final PInputEvent pie) {
		presspt = pie.getPosition();
	}

	protected void startDrag(final PInputEvent e) {
		super.startDrag(e);
		
		viewBounds = parentCanvas.getCamera().getViewBounds();
		initializeSelection(e);
		initializeMarquee(e);
	}

	/**
	 * Updates the marquee rectangle as the result of a drag.
	 * 
	 * @param pie
	 *            event responsible for the change in the marquee
	 */
	protected void updateMarquee(final PInputEvent pie) {
		if(!viewBounds.contains(pie.getPosition())){
			return;
		}
		
		
		final PBounds b = new PBounds();

		b.add(presspt);
		b.add(pie.getPosition());
		
		marquee.globalToLocal(b);
		marquee.setPathToRectangle((float) b.x, (float) b.y,
				(float) b.width, (float) b.height);
		b.reset();
		b.add(presspt);
		b.add(pie.getPosition());

	}
	
	private void bandUpdated(PPath marquee) {
		double width = marquee.getWidth();
		if (width > 10) {
			double currentwidth = parentCanvas.getCamera().getViewBounds().getWidth();
			double scale = currentwidth / width;
			double finalScale = scale * parentCanvas.getCamera().getViewScale();
			finalScale = finalScale > 9.99 ? 9.99 : finalScale;
			parentCanvas.setZoom(finalScale);
		}
	}

	/**
	 * @return the marquee
	 */
	public PPath getMarquee() {
		return this.marquee;
	}

	protected void dragActivityStep(final PInputEvent aEvent) {
		if (marquee != null) {
			final float origStrokeNum = strokeNum;
			strokeNum = (strokeNum + 0.5f) % NUM_STROKES; // Increment by
			// partial steps to
			// slow down animation
			if ((int) strokeNum != (int) origStrokeNum) {
				marquee.setStroke(strokes[(int) strokeNum]);
			}
		}
	}
}
