package com.strandgenomics.imaging.iviewer.va;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.ImageCanvas;
import com.strandgenomics.imaging.iviewer.ImageViewerApplet;
import com.strandgenomics.imaging.iviewer.ImageViewerState;
import com.strandgenomics.imaging.iviewer.va.VAObject.TYPE;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;
import edu.umd.cs.piccolox.event.PNotification;
import edu.umd.cs.piccolox.event.PNotificationCenter;
import edu.umd.cs.piccolox.event.PSelectionEventHandler;
import edu.umd.cs.piccolox.handles.PBoundsHandle;
import edu.umd.cs.piccolox.handles.PHandle;
import edu.umd.cs.piccolox.nodes.PComposite;
import edu.umd.cs.piccolox.util.PBoundsLocator;
import edu.umd.cs.piccolox.util.PNodeLocator;

public class VAController implements PropertyChangeListener, MouseListener {

	public static final String VA_COLOR = "VA_COLOR";

	public static final String VA_THICKNESS = "VA_THICKNESS";

	public static final String VA_TEXT_FONT = "VA_TEXT_FONT";

	public static final String VA_TEXT_BKG_COLOR = "VA_TEXT_BKG_COLOR";

	public static final String VA_CONFIG = "imaging.viz:VAParameters";

	public static BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f);

	class MyDragSequenceEventHandler extends PDragSequenceEventHandler {
		private final int HACKED_OFFSET_CONSTANT = 10;// without this the
														// freehand drawing
														// doesnot start exactly
														// on mouse pointer

		protected PPath squiggle;
		private boolean closeShape;
		private float startX, startY;
		private BasicStroke stroke;

		private PBounds viewBounds;

		MyDragSequenceEventHandler(boolean closeShape) {
			super();
			this.closeShape = closeShape;
		}

		public void drag(PInputEvent e) {
			if (!viewBounds.contains(e.getPosition())) {
				return;
			}

			super.drag(e);
			updateSquiggle(e);
		}

		public void endDrag(PInputEvent e) {
			super.endDrag(e);
			updateSquiggle(e);
			if (closeShape)
				squiggle.lineTo(startX, startY);

			PBounds bounds = squiggle.getBounds();
			squiggle = null;

			freehand = false;
			selectZoom();
		}

		public void setCloseShape(boolean closeShape) {
			this.closeShape = closeShape;
		}

		public void setStroke(BasicStroke stroke) {
			this.stroke = stroke;
		}

		public void startDrag(PInputEvent e) {
			super.startDrag(e);
			Point2D p = e.getPosition();

			viewBounds = vaPanel.getCamera().getViewBounds();

			squiggle = new PPath();
			squiggle.moveTo((float) p.getX() - HACKED_OFFSET_CONSTANT,
					(float) p.getY() - HACKED_OFFSET_CONSTANT);
			if (closeShape) {
				startX = (float) p.getX() - HACKED_OFFSET_CONSTANT;
				startY = (float) p.getY() - HACKED_OFFSET_CONSTANT;
			}

			squiggle.setStroke(stroke);
			squiggle.setStrokePaint(vaColor);
			squiggle.setPickable(true);

			((VAObject)annotationNode).addChild(squiggle, VAObject.TYPE.PATH);
			unsavedVA = true;
		}

		public void updateSquiggle(PInputEvent e) {
			Point2D p = e.getPosition();

			squiggle.lineTo((float) p.getX() - HACKED_OFFSET_CONSTANT,
					(float) p.getY() - HACKED_OFFSET_CONSTANT);
			squiggle.moveTo((float) p.getX() - HACKED_OFFSET_CONSTANT,
					(float) p.getY() - HACKED_OFFSET_CONSTANT);
		}
	};

	class MyPBoundsHandle extends PBoundsHandle {

		private PBounds viewBounds;

		public MyPBoundsHandle(PBoundsLocator locator) {
			super(locator);
			setStroke(DEFAULT_STROKE);
		}

		public void startHandleDrag(Point2D aLocalPoint, PInputEvent aEvent) {
			viewBounds = vaPanel.getCamera().getViewBounds();

			super.startHandleDrag(aLocalPoint, aEvent);
		}

		public void dragHandle(PDimension aLocalDimension, PInputEvent aEvent) {
			if (!viewBounds.contains(aEvent.getPosition())) {
				return;
			}
			super.dragHandle(aLocalDimension, aEvent);
		}

	};

	class MyPSelectionEventHandler extends PSelectionEventHandler {

		private PBounds viewBounds;
		
		public MyPSelectionEventHandler(PNode marqueeParent,
				PNode annotationNode) {
			super(marqueeParent, annotationNode);
		}

		/**
		 * Adds bound handles to the provided node.
		 * 
		 * @param node
		 *            node to be decorated
		 */
		public void decorateSelectedNode(final PNode node) {
			if (imageState.getVAState() != ImageViewerState.VAState.VA_ON
					|| selectZoom || freehand) {
				return;
			}

			if (!(node instanceof LineNode)) {
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createEastLocator(node)));
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createWestLocator(node)));
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createNorthLocator(node)));
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createSouthLocator(node)));
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createNorthEastLocator(node)));
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createNorthWestLocator(node)));
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createSouthEastLocator(node)));
				node.addChild(new MyPBoundsHandle(PBoundsLocator
						.createSouthWestLocator(node)));
			}

			addHandle(node);

			for (int i = 0; i < node.getChildrenCount(); i++) {
				node.getChild(i).setVisible(true);
			}
		}

		public void undecorateSelectedNode(PNode node) {
			if (imageState.getVAState() != ImageViewerState.VAState.VA_ON
					|| selectZoom || freehand) {
				return;
			}

			int size = node.getChildrenCount();
			if (size > 0) {
				node.removeChild(size - 1);
			}

			for (int i = 0; i < node.getChildrenCount(); i++) {
				node.getChild(i).setVisible(false);
			}

			super.undecorateSelectedNode(node);
		}

		protected void startDrag(PInputEvent e) {
			if (imageState.getVAState() != ImageViewerState.VAState.VA_ON) {
				return;
			}
			
			super.startDrag(e);

			viewBounds = vaPanel.getCamera().getViewBounds();

			initializeSelection(e);

			if (isMarqueeSelection(e)) {
				initializeMarquee(e);

				if (!isOptionSelection(e)) {
					startMarqueeSelection(e);
				} else {
					startOptionMarqueeSelection(e);
				}
			} else {
				if (!isOptionSelection(e)) {
					startStandardSelection(e);
				} else {
					startStandardOptionSelection(e);
				}
			}
		}

		protected void dragStandardSelection(PInputEvent e) {
			// There was a press node, so drag selection
			PDimension d = e.getDelta();
			e.getCamera().globalToLocal(d);
			Iterator selectionEn = getSelection().iterator();
			while (selectionEn.hasNext()) {
				PNode node = (PNode) selectionEn.next();

				node.offset(d.width, d.height);
			}
		}

		protected void drag(PInputEvent e) {
			if (imageState.getVAState() != ImageViewerState.VAState.VA_ON) {
				return;
			}

			if (!viewBounds.contains(e.getPosition())) {
				// System.out.println("mouse out of boundary");
				selectionEventHandler.unselectAll();
				return;
			}
			
			super.drag(e);
		}

		protected boolean isMarqueeSelection(PInputEvent pie) {
			PNode pickedNode = pie.getPickedNode();
			if (annotationNode != null) {
				Iterator<PNode> it = annotationNode.getChildrenIterator();
				while (it.hasNext()) {
					PNode node = it.next();
					if (node.equals(pickedNode)) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
	};

	private PNode currentPosition;

	private PNode annotationNode;

	private ImageCanvas vaPanel;

	private MyPSelectionEventHandler selectionEventHandler;

	private MyDragSequenceEventHandler squiggleEventHandler;

	private ArrayList<PNode> group;

	private boolean unsavedVA;

	private ImageViewerApplet imageState;

	private Color vaColor = Color.RED;

	private Color textBkgColor = new Color(0, 0, 0, 0);

	private BasicStroke stroke = new BasicStroke(2.5f);

	private double actualFontSize = 12.0;

	private Font newFont;

	private Boolean selectZoom;

	private boolean freehand;

	public VAController(VAObject visualOverlay, ImageViewerApplet imageState) {
		this.annotationNode = visualOverlay;
		this.vaPanel = imageState.getImagePanel();
		this.imageState = imageState;

		vaPanel.removeInputEventListener(vaPanel.getPanEventHandler());
		vaPanel.removeInputEventListener(vaPanel.getZoomEventHandler());
		vaPanel.removeInputEventListener(vaPanel.getRubberBandHandler());
//		vaPanel.removeInputEventListener(vaPanel.getToolTipEventHandler());
//		vaPanel.removeInputEventListener(vaPanel.getSelectionHandler());

		vaPanel.addMouseListener(this);

		if (this.annotationNode != null) {
			unsavedVA = false;
			init();
		}
	}

	public void addEllipse() {
		int x = ((int) vaPanel.getCamera().getViewBounds().getMinX() + (int) vaPanel
				.getCamera().getViewBounds().getMaxX()) / 2;
		int y = ((int) vaPanel.getCamera().getViewBounds().getMinY() + (int) vaPanel
				.getCamera().getViewBounds().getMaxY()) / 2;

		vaPanel.removeInputEventListener(squiggleEventHandler);

		double zoomLevel = imageState.getZoomMultiplier();
		int scaledSize = ((Double) (50.0 / zoomLevel)).intValue();

		currentPosition = getEllipse(x, y, scaledSize, scaledSize);
		currentPosition.addPropertyChangeListener(PNode.PROPERTY_TRANSFORM,
				this);
		currentPosition.addPropertyChangeListener(PNode.PROPERTY_BOUNDS, this);

		((VAObject)annotationNode).addChild(currentPosition, VAObject.TYPE.ELLIPSE);
		unsavedVA = true;
		annotationNode.repaint();
	}

	public void addLine() {
		int x = ((int) vaPanel.getCamera().getViewBounds().getMinX() + (int) vaPanel
				.getCamera().getViewBounds().getMaxX()) / 2;
		int y = ((int) vaPanel.getCamera().getViewBounds().getMinY() + (int) vaPanel
				.getCamera().getViewBounds().getMaxY()) / 2;

		vaPanel.removeInputEventListener(squiggleEventHandler);

		double zoomLevel = imageState.getZoomMultiplier();
		int scaledSize = ((Double) (50.0 / zoomLevel)).intValue();

		currentPosition = getLine(x, y, scaledSize, scaledSize);
		currentPosition.addPropertyChangeListener(PNode.PROPERTY_TRANSFORM,
				this);
		currentPosition.addPropertyChangeListener(PNode.PROPERTY_BOUNDS, this);

		((VAObject)annotationNode).addChild(currentPosition, VAObject.TYPE.LINE);

		unsavedVA = true;
		annotationNode.repaint();
	}

	public void addNewOverlay(String name) {
		imageState.addOverlay(name);
	}

	public void addPolygon() {
	}

	public void addRectangle() {
		int x = ((int) vaPanel.getCamera().getViewBounds().getMinX() + (int) vaPanel
				.getCamera().getViewBounds().getMaxX()) / 2;
		int y = ((int) vaPanel.getCamera().getViewBounds().getMinY() + (int) vaPanel
				.getCamera().getViewBounds().getMaxY()) / 2;

		vaPanel.removeInputEventListener(squiggleEventHandler);

		double zoomLevel = imageState.getZoomMultiplier();
		int scaledSize = ((Double) (50.0 / zoomLevel)).intValue();
		
		currentPosition = getRectangle(x, y, scaledSize, scaledSize);
		currentPosition.addPropertyChangeListener(PNode.PROPERTY_TRANSFORM,
				this);
		currentPosition.addPropertyChangeListener(PNode.PROPERTY_BOUNDS, this);

		((VAObject)annotationNode).addChild(currentPosition, VAObject.TYPE.RECTANGLE);

		unsavedVA = true;
		annotationNode.repaint();
		
		vaPanel.repaint();
	}

	public void addText() {
		int x = ((int) vaPanel.getCamera().getViewBounds().getMinX() + (int) vaPanel
				.getCamera().getViewBounds().getMaxX()) / 2;
		int y = ((int) vaPanel.getCamera().getViewBounds().getMinY() + (int) vaPanel
				.getCamera().getViewBounds().getMaxY()) / 2;

		vaPanel.removeInputEventListener(squiggleEventHandler);

		double zoomLevel = imageState.getZoomMultiplier();
		int scaledSize = ((Double) (50.0 / zoomLevel)).intValue();
		((Double) (actualFontSize / zoomLevel)).intValue();

		currentPosition = getTextBox(x, y, scaledSize, scaledSize);

		String comment = editTextDialog((PText) currentPosition);

		if (comment != null && !comment.isEmpty()) {
			((PText) currentPosition).setConstrainHeightToTextHeight(true);
			((PText) currentPosition).setConstrainWidthToTextWidth(true);

			((VAObject)annotationNode).addChild(currentPosition, VAObject.TYPE.TEXT);
			currentPosition.addPropertyChangeListener(PNode.PROPERTY_TRANSFORM,
					this);
			currentPosition.addPropertyChangeListener(PNode.PROPERTY_BOUNDS,
					this);
			unsavedVA = true;
			annotationNode.repaint();

			((PText) currentPosition).setConstrainHeightToTextHeight(false);
			((PText) currentPosition).setConstrainWidthToTextWidth(false);
		}
	}

	public void cleanup() {
		if (unsavedVA) {
			saveDialog();
		}
		if (selectionEventHandler != null) {
			resetSelection();
		}
		if (annotationNode != null) {
			annotationNode.setChildrenPickable(false);
			for (int i = 0; i < annotationNode.getChildrenCount(); i++) {
				PNode child = annotationNode.getChild(i);
				child.addPropertyChangeListener(PNode.PROPERTY_BOUNDS, this);
				child.addPropertyChangeListener(PNode.PROPERTY_TRANSFORM, this);
			}
		}
		vaPanel.removeInputEventListener(selectionEventHandler);
		vaPanel.addInputEventListener(vaPanel.getRubberBandHandler());
	}

	public void closedShape(boolean drawingOn) {
		if (drawingOn) {
			freehand = true;

			removeInputEventListeners();

			squiggleEventHandler.setCloseShape(true);
			squiggleEventHandler.setStroke(stroke);
			vaPanel.addInputEventListener(squiggleEventHandler);
		} else {
			squiggleEventHandler.setCloseShape(false);
			vaPanel.removeInputEventListener(squiggleEventHandler);
		}
	}

	public void colorChooser() {
		JColorChooser colorBox = new JColorChooser(vaColor);
		Color chosenColor = colorBox.showDialog(new JFrame(), "Color Chooser",
				Color.red);
		if (chosenColor != null) {
			setVaColor(chosenColor);

			for (int i = 0; i < group.size(); i++) {
				PNode node = group.get(i);
				if (node instanceof LineNode) {
					node.setPaint(vaColor);
				} else if (node instanceof PText) {
					((PText) node).setTextPaint(vaColor);
				} else {
					((PPath) node).setStrokePaint(vaColor);
				}
			}
		}
	}

	public MyDragSequenceEventHandler createSquiggleEventHandler(
			boolean closeShape) {
		MyDragSequenceEventHandler seqHandler = new MyDragSequenceEventHandler(
				closeShape);
		return seqHandler;
	}

	public void deleteOverlay(VisualAnnotationToolbar visualAnnotationToolbar) {
		List<String> overlayNames = (List<String>) imageState
				.getOverlaysNames();
		String overlayname = (String) JOptionPane.showInputDialog(
				visualAnnotationToolbar, "Choose Overlay to Delete",
				"Delete Overlay", JOptionPane.QUESTION_MESSAGE, null,
				overlayNames.toArray(), null);
		if (overlayname != null) {
			// int choice = MsgManager.getInstance().showMsg(
			// "DELETE_OVERLAY_CONF", "name", overlayname);
			int choice = JOptionPane.showConfirmDialog(null, "Confirm Delete",
					"Delete", JOptionPane.QUESTION_MESSAGE);
			if (choice == 0) {
				imageState.deleteOverlay(overlayname);
				if (annotationNode != null
						&& overlayname.equals(((VAObject) annotationNode)
								.getName())) {
					annotationNode = null;
					visualAnnotationToolbar
							.setTitle("Visual Annotation - *no_open_overlay*");
				}
			}
		}
	}

	public void removeInputEventListeners() {
		vaPanel.removeInputEventListener(vaPanel.getRubberBandHandler());
		vaPanel.removeInputEventListener(vaPanel.getRubberBandHandler());
//		vaPanel.removeInputEventListener(vaPanel.getSelectionHandler());
		vaPanel.removeInputEventListener(selectionEventHandler);
		vaPanel.removeInputEventListener(squiggleEventHandler);
	}

	public void freehandDrawing(boolean drawingOn) {
		if (drawingOn) {
			freehand = true;

			removeInputEventListeners();

			squiggleEventHandler.setStroke(stroke);
			vaPanel.addInputEventListener(squiggleEventHandler);
		} else {
			// vaPanel.removeInputEventListener(squiggleEventHandler);
		}
	}

	public PNode getAnnotationNode() {
		return annotationNode;
	}

	private PPath getEllipse(int x, int y, int width, int height) {
		Shape ellipseShape = new Ellipse2D.Double(x, y, width, height);
		final PPath path = new PPath(ellipseShape);
		path.setStrokePaint(vaColor);
		path.setPaint(new Color(0, 0, 0, 0));
		path.setStroke(stroke);
		path.setPickable(true);
		return path;
	}

	private PNode getLine(double x1, double y1, double x2, double y2) {
		PNode path = new LineNode(x1, y1, x2, y2, stroke.getLineWidth());
		path.setPaint(vaColor);
		path.setPickable(true);
		for (int i = 0; i < path.getChildrenCount(); i++) {
			path.getChild(i).setVisible(false);
		}

		return path;
	}

	private PPath getRectangle(int x, int y, int width, int height) {
		Shape rectangleShape = new Rectangle(x, y, width, height);
		PPath path = new PPath(rectangleShape);
		path.setStrokePaint(vaColor);
		path.setPaint(new Color(0, 0, 0, 0));
		path.setStroke(stroke);
		path.setPickable(true);
		
		return path;
	}

	private PText getTextBox(int x, int y, int width, int height) {
		final PText path = new PText();
		path.setBounds(x, y, width, height);
		path.setText("<sample text>");
		path.setPickable(true);

		return path;
	}

	public Color getVaColor() {
		return vaColor;
	}

	public void groupSelected() {
		PComposite composite = new PComposite();
		for (int i = group.size() - 1; i >= 0; i--) {
			PNode _child = group.get(i);
			composite.addChild(_child);
		}
		annotationNode.addChild(composite);
	}

	public void selectZoom() {

		Boolean state = (Integer) imageState.getZoomState() == 1 ? true : false;
		if (state) {// zoom state
			if (annotationNode != null)
				annotationNode.setChildrenPickable(false);

			resetSelection();

			vaPanel.removeInputEventListener(selectionEventHandler);

			vaPanel.addInputEventListener(vaPanel.getRubberBandHandler());

		} else {// select state
			if (annotationNode != null)
				annotationNode.setChildrenPickable(true);

			vaPanel.removeInputEventListener(vaPanel.getRubberBandHandler());

			vaPanel.addInputEventListener(selectionEventHandler);

		}
		selectZoom = state;
	}

	private void init() {
		annotationNode.setPickable(true);
		annotationNode.setChildrenPickable(true);
		for (int i = 0; i < annotationNode.getChildrenCount(); i++) {
			PNode child = annotationNode.getChild(i);
			child.addPropertyChangeListener(PNode.PROPERTY_BOUNDS, this);
			child.addPropertyChangeListener(PNode.PROPERTY_TRANSFORM, this);
		}

		vaPanel.removeInputEventListener(vaPanel.getPanEventHandler());
		vaPanel.removeInputEventListener(vaPanel.getZoomEventHandler());
		vaPanel.removeInputEventListener(vaPanel.getRubberBandHandler());
//		vaPanel.removeInputEventListener(vaPanel.getToolTipEventHandler());
//		vaPanel.removeInputEventListener(vaPanel.getSelectionHandler());

		removeInputEventListeners();
		
		selectionEventHandler = new MyPSelectionEventHandler(
				vaPanel.getLayer(), annotationNode);
//		vaPanel.addInputEventListener(selectionEventHandler);
		vaPanel.getRoot().getDefaultInputManager()
				.setKeyboardFocus(selectionEventHandler);
		PNotificationCenter.defaultCenter().addListener(this,
				"selectionChanged",
				MyPSelectionEventHandler.SELECTION_CHANGED_NOTIFICATION,
				selectionEventHandler);
		
		resetSelection();
		selectZoom();

		vaColor = Color.red;
		textBkgColor = new Color(0,0,0,0);
		float thickness = 2.0f;
		double zoomLevel = (Double) imageState.getZoomMultiplier();
		stroke = new BasicStroke(thickness / (float) zoomLevel);
		
		if (newFont == null) {
			newFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		}

		group = new ArrayList<PNode>();
		squiggleEventHandler = createSquiggleEventHandler(false);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2 && group != null && group.size() >= 1
				&& group.get(0) instanceof PText) {
			PText textNode = (PText) group.get(0);
			editTextDialog(textNode);
		}
	}

	public String editTextDialog(PText textNode) {
		double zoomLevel = imageState.getZoomMultiplier();
		TextEditorPane tabPane = new TextEditorPane(newFont, vaColor,
				textBkgColor, textNode.getText());

		int n = JOptionPane.showConfirmDialog(null, tabPane, "Text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
		
		String comment = tabPane.getText();

		if (n == JOptionPane.OK_OPTION && comment != null && !comment.isEmpty()) {
			newFont = tabPane.getF();

			vaColor = tabPane.getTextColor();
			textBkgColor = tabPane.getBkgColor();

			textNode.setConstrainHeightToTextHeight(true);
			textNode.setConstrainWidthToTextWidth(true);

			textNode.setText(comment);
			textNode.setFont(newFont.deriveFont(newFont.getSize()
					/ (float) zoomLevel));
			textNode.setTextPaint(vaColor);
			textNode.setPaint(textBkgColor);

			textNode.setConstrainHeightToTextHeight(false);
			textNode.setConstrainWidthToTextWidth(false);

			textNode.repaint();
		} else {
			return "";
		}

		return comment;
		
//		String value = JOptionPane.showInputDialog(null, "Enter Text: ");
//		if(value!=null && !value.isEmpty()){
//			textNode.setConstrainHeightToTextHeight(true);
//			textNode.setConstrainWidthToTextWidth(true);
//			textNode.setText(value);
//			double zoomLevel = imageState.getZoomMultiplier();
//			textNode.setFont(newFont.deriveFont(newFont.getSize()
//					/ (float) zoomLevel));
//			textNode.setTextPaint(vaColor);
//			textNode.setPaint(textBkgColor);
//
//			textNode.setConstrainHeightToTextHeight(false);
//			textNode.setConstrainWidthToTextWidth(false);
//
//			textNode.repaint();
//		}
//		return value;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (squiggleEventHandler != null) {
			squiggleEventHandler.setCloseShape(false);
			vaPanel.removeInputEventListener(squiggleEventHandler);
		}
	}

	public String createOverlayDialog() {
		String value = JOptionPane.showInputDialog(null, "Overlay Name: ");
		
		List<String> overlayNames = (List<String>) imageState
				.getOverlaysNames();
		if (!(value instanceof String) || value == null
				|| ((String) value).trim().equals("")
				|| ((String) value).isEmpty()
				|| ((String) value).equals("EnterNew")
				|| overlayNames.contains(value)) {
			// MsgManager.getInstance().showMsg("DUP_NAME");
			JOptionPane.showMessageDialog(null, "Invalid/Duplicate Name", "Error",
					JOptionPane.ERROR_MESSAGE);
			return "";
		}
		return value;
	}

	public String openOverlay() {
		if (unsavedVA)
			saveDialog();

		String overlayname = createOverlayDialog();

		if (overlayname != null && !overlayname.isEmpty()) {
			List<String> overlayNames = (List<String>) imageState
					.getOverlayNamesForAllSites();
			if (!overlayNames.contains(overlayname)) {
				addNewOverlay(overlayname);
			}

			unsavedVA = false;
			return overlayname;
		}
		return "";
	}

	public List<List<VAObject>> getOverlays() {
		List<List<VAObject>> overlays = (List<List<VAObject>>) imageState
				.getOverlays();
		return overlays;
	}

	public String setWorkingOverlay(VAObject overlay) {
		annotationNode = overlay;
		init();

		String workingOverlayName = ((VAObject) annotationNode).getName();
		imageState.setWorkingOverlayName(workingOverlayName);
		return workingOverlayName;
	}

	public void setAnnotationNode(VAObject overlay) {
		annotationNode = overlay;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		unsavedVA = true;
	}

	public void setUnsavedVA(boolean unsavedVA) {
		this.unsavedVA = unsavedVA;
	}

	public void removeDialog() {
		if (group == null || group.size() < 1) {
			// MsgManager.getInstance().showMsg("OBJ_NOT_SELECTED", "action",
			// "deletion");
			JOptionPane.showConfirmDialog(null, "Object Not Selected", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String[] options = { "Delete Current", "Delete From All Frames",
				"Delete From All Slices", "Delete From All" };
		String n = (String) JOptionPane.showInputDialog(null,
				"Delete Selected Objects", "Delete",
				JOptionPane.QUESTION_MESSAGE, (Icon) null, options, options[0]);
		
//		List<String> recordIds = imageState.getRecordIds();
		List<IRecord> records = imageState.getRecords();
		
		int maxFrame = (Integer) imageState.getMaxFrame();
		int maxSlice = (Integer) imageState.getMaxSlice();

		int currentFrame = (Integer) imageState.getFrame();
		int currentSlice = (Integer) imageState.getSlice();
		int currentSite = (Integer) imageState.getSite();
		
		IRecord record = records.get(0);
		
		List<VAObject> overlays = imageState.getOverlaysForRecord();

		if (n == null) {
			return;
		}

		if (n.equals(options[0])) {
			// delete current
			remove();
		} else if (n.equals(options[1])) {
			ArrayList<PNode> list = (ArrayList<PNode>) group.clone();

			for(VAObject overlay:overlays)
			{
				if(overlay.getFrame() == currentFrame)
					continue;
				
				for (int selection = 0; selection < list.size(); selection++) {
					PNode node = list.get(selection);
					overlay.removeChildById(((VAObject) annotationNode).getVisualObjectId(node));
				}
			}
			// for deleting current
//			remove();
		} else if (n.equals(options[2])) {
			ArrayList<PNode> list = (ArrayList<PNode>) group.clone();

			for(VAObject overlay:overlays)
			{
				if(overlay.getSlice() == currentSlice)
					continue;
				
				for (int selection = 0; selection < list.size(); selection++) {
					PNode node = list.get(selection);
					overlay.removeChildById(((VAObject) annotationNode).getVisualObjectId(node));
				}
			}
			
			// for deleting current
//			remove();
		} else if (n.equals(options[3])) {
			// delete from all
			ArrayList<PNode> list = (ArrayList<PNode>) group.clone();

			for(VAObject overlay:overlays)
			{
				if(overlay.getSlice() == currentSlice && currentFrame == overlay.getFrame())
					continue;
				
				for (int selection = 0; selection < list.size(); selection++) {
					PNode node = list.get(selection);
					overlay.removeChildById(((VAObject) annotationNode).getVisualObjectId(node));
				}
			}
			// for deleting current
			remove();
		}
		unsavedVA = true;
		resetSelection();
	}

	public void remove() {
		if (group != null && group.size() >= 1)
			annotationNode.removeChildren(group);
	}

	public void saveDialog() {
		unsavedVA = false;

		if (selectionEventHandler != null)
			resetSelection();

		// int n = MsgManager.getInstance().showMsg("OVERLAY_SAVE_CONF");
		int n = JOptionPane.showConfirmDialog(null, "Do you want to save visual annotations?",
				"Save", JOptionPane.YES_NO_OPTION);
		if (annotationNode == null)
			return;
		if (n == 0) {
			imageState.saveOverlays();
		} else if (n == 1) {
			discard();
		}
	}

	public void saveOverlay() {
		if (selectionEventHandler != null)
			resetSelection();

		unsavedVA = false;
		imageState.saveOverlays();
	}

	public void discard() {
		// List<String> recordIds = (List<String>) imageState
		// .getProperty("recordIds");
		// IRecordVO viewRecord = ((List<IRecordVO>) imageState
		// .getProperty("records")).get(0);
		//
		// IRecordVO dbRecord = IndexerEngineFactory.getIndexerEngine()
		// .getRecordVO(recordIds.get(0));
		//
		// viewRecord.setVisualObjects(dbRecord.getVisualObjects());
		// viewRecord.initVisualObjects();
		//
		// annotationNode.repaint();
		// vaPanel.repaint();
	}

	public void selectionChanged(PNotification notfication) {
		group = (ArrayList<PNode>) selectionEventHandler.getSelection();
		if (group != null && group.size() > 0) {
		}
	}

	public void addHandle(PNode n) {
		PHandle h = new PHandle(new PNodeLocator(n)) {
			private PBounds viewBounds;

			public void dragHandle(PDimension aLocalDimension,
					PInputEvent aEvent) {
				if (!viewBounds.contains(aEvent.getPosition())) {
					return;
				}
				localToParent(aLocalDimension);
				getParent().translate(aLocalDimension.getWidth(),
						aLocalDimension.getHeight());
			}

			public void startHandleDrag(Point2D aLocalPoint, PInputEvent aEvent) {
				viewBounds = vaPanel.getCamera().getViewBounds();
				super.startHandleDrag(aLocalPoint, aEvent);
			}
		};

		h.setPaint(Color.RED);
		h.setVisible(true);

		n.addChild(h);
	}

	public void setAnnotationNode(PNode annotationNode) {
		this.annotationNode = annotationNode;
	}

	public void setVaColor(Color vaColor) {
		this.vaColor = vaColor;
	}

	public void turnoffVA() {
		imageState.setVAState(ImageViewerState.VAState.VA_OFF);

		Boolean state = (Integer) imageState.getZoomState() == 1 ? true : false;
		if (state)
			vaPanel.addInputEventListener(vaPanel.getRubberBandHandler());
//		else
//			vaPanel.addInputEventListener(vaPanel.getSelectionHandler());
//
//		vaPanel.addInputEventListener(vaPanel.getToolTipEventHandler());

		annotationNode = null;
	}

	public VAObject getWorkingOverlay() {
		return (VAObject) annotationNode;
	}

	public void pasteSelection() {
		HashMap<PNode, Integer> list = (HashMap<PNode, Integer>) imageState.getClipboardObject();

		if (list == null || list.size() < 1) {
			JOptionPane.showInternalMessageDialog(null,
					"No Object Selected For Pasting", "Paste",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String[] options = { "On Current", "On All Frames", "On All Slices",
				"On All" };
		String n = (String) JOptionPane.showInputDialog(null,
				"Paste Copied Objects", "Paste", JOptionPane.QUESTION_MESSAGE,
				(Icon) null, options, options[0]);

		int maxFrame = (Integer) imageState.getMaxFrame();
		int maxSlice = (Integer) imageState.getMaxSlice();

		int currentSlice = (Integer) imageState.getSlice();
		int currentFrame = (Integer) imageState.getFrame();
		
		List<IRecord> records = (List<IRecord>) imageState.getRecords();

		if (n == null) {
			return;
		}

		if (n.equals(options[0])) {
			Iterator<PNode> nodeIterator = list.keySet().iterator();
			while (nodeIterator.hasNext()) {
				PNode node = nodeIterator.next();
				TYPE type = ((VAObject) annotationNode).getTypeMap().get(node);
				Integer id = list.get(node);
				node = (PNode) node.clone();
				((VAObject) annotationNode).addChild(node, id, type);
			}
			unsavedVA = true;
		} else if (n.equals(options[1])) {
			// paste on all frames
			resetSelection();

			List<VAObject> overlays = imageState.getOverlaysForRecord();
			for (VAObject vo : overlays) {
				if (vo != null && vo.getSlice() == currentSlice
						&& vo.getName() == ((VAObject) annotationNode)
								.getName()) {
					Iterator<PNode> nodeIterator = list.keySet().iterator();
					while (nodeIterator.hasNext()) {
						PNode node = nodeIterator.next();
						TYPE type = ((VAObject) annotationNode).getTypeMap().get(node);
						Integer id = list.get(node);
						vo.addChild((PNode) node.clone(), id, type);
					}
				}
			}
		} else if (n.equals(options[2])) {
			// paste on all slices
			resetSelection();
			
			List<VAObject> overlays = imageState.getOverlaysForRecord();
			for (VAObject vo : overlays) {
				if (vo != null && vo.getFrame() == currentFrame
						&& vo.getName() == ((VAObject) annotationNode)
								.getName()) {
					Iterator<PNode> nodeIterator = list.keySet().iterator();
					while (nodeIterator.hasNext()) {
						PNode node = nodeIterator.next();
						TYPE type = ((VAObject) annotationNode).getTypeMap().get(node);
						Integer id = list.get(node);
						vo.addChild((PNode) node.clone(), id, type);
					}
				}
			}
		} else if (n.equals(options[3])) {
			// paste on all
			resetSelection();
			
			List<VAObject> overlays = imageState.getOverlaysForRecord();
			for (VAObject vo : overlays) {
				if (vo != null && vo.getName() == ((VAObject) annotationNode)
								.getName()) {
					Iterator<PNode> nodeIterator = list.keySet().iterator();
					while (nodeIterator.hasNext()) {
						PNode node = nodeIterator.next();
						TYPE type = ((VAObject) annotationNode).getTypeMap().get(node);
						Integer id = list.get(node);
						vo.addChild((PNode) node.clone(), id, type);
					}
				}
			}

		}
		unsavedVA = true;
		resetSelection();
	}

	public void resetSelection() {
		if (selectionEventHandler != null) {
			selectionEventHandler.unselectAll();
		}
	}

	public void thicknessControl(VisualAnnotationToolbar vaToolbar) {
		LineThicknessBox ltb = new LineThicknessBox(stroke.getLineWidth());
		int n = JOptionPane.showConfirmDialog(null, ltb.createDialog(), "Thickness Control", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
		
		if (n == JOptionPane.OK_OPTION) 
		{
			float thickness = ltb.getLineWidth();
			double zoomLevel = (Double) imageState.getZoomMultiplier();
			stroke = new BasicStroke(thickness / (float) zoomLevel);

			for (int i = 0; i < group.size(); i++) {
				PNode node = group.get(i);
				if (node instanceof LineNode) {
					((LineNode) node).setStrokeWidth(stroke.getLineWidth());
				} else {
					((PPath) node).setStroke(stroke);
				}
			}
		}
	 }

	public void copySelection() {
		if (group == null || group.size() < 1) {
			JOptionPane.showInternalMessageDialog(null,
					"No Object Selected For Copying", "Copy",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		HashMap<PNode, Integer> clipboard = new HashMap<PNode, Integer>();
		for (int i = 0; i < group.size(); i++) {
			PNode node = group.get(i);
			clipboard.put(node,
					((VAObject) annotationNode).getVisualObjectId(node));
		}

		resetSelection();

		imageState.setClipboardObject(clipboard);
	}

	public boolean isUnSaved() {
		return unsavedVA;
	}

	public int getActiveOverlayIndex(String name) {
		return imageState.getActiveOverlayIndex(name);
	}
}