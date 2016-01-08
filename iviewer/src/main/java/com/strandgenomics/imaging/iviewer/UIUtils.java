package com.strandgenomics.imaging.iviewer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;
import com.strandgenomics.imaging.iviewer.va.VAObject;

import edu.umd.cs.piccolo.PNode;

public class UIUtils {

	public static final String ICONS_PATH = "";
	private static final String MANUAL_PATH = "imaging/acquisition/resources/manual/";

	public static JPanel newTitlePanel(String title, boolean hasGlue) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setMaximumSize(new Dimension(800, 15));
		JLabel l = new JLabel(title);
		Font f = l.getFont();
		l.setFont(new Font(f.getName(), Font.BOLD, f.getSize() + 1));
		p.add(l);
		if (hasGlue) {
			p.add(Box.createHorizontalGlue());
		}
		return p;
	}

	public static JTabbedPane getTabbedPane() {
		UIManager.put("TabbedPane.selected", ColorUIResource.gray);
		UIManager.put("TabbedPane.background", ColorUIResource.lightGray);
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setMinimumSize(new Dimension());
		tabbedPane.setUI(new UIUtils.MyTabbedPaneUI());
		return tabbedPane;
	}

	static class MyTabbedPaneUI extends
			javax.swing.plaf.basic.BasicTabbedPaneUI {
		protected Insets getContentBorderInsets(int tabPlacement) {
			return new Insets(0, 0, 0, 0);
		}

		protected void paintContentBorder(Graphics g, int tabPlacement,
				int selectedIndex) {
		}
	}

	public static JButton createButton(String text) {
		JButton b = new JButton(text);

		b.setToolTipText(text);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setBorderPainted(true);
		b.setFocusPainted(false);
		b.setIconTextGap(0);
		b.setContentAreaFilled(false);

		return b;
	}

	public static ImageIcon getIcon(String iconPath) {
		ImageIcon icon = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			//System.out.println("icon path="+(ICONS_PATH + iconPath));
			icon = new ImageIcon(ImageIO.read(loader
					.getResourceAsStream(ICONS_PATH + iconPath)));
			Image img = ((ImageIcon) icon).getImage().getScaledInstance(16, 16,
					Image.SCALE_SMOOTH);
			return new ImageIcon(img);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JideToggleButton createCommandBarToggleButton(
			String iconPath, String selectedIconPath, String tooltipText,
			String selectedTooltipText, boolean selected) {
		ToggleButton button = new ToggleButton(getIcon(iconPath),
				getIcon(selectedIconPath), tooltipText, selectedTooltipText,
				selected);

		return button;
	}

	public static List<VAObject> getClonedList(List<VAObject> list) {
		List<VAObject> result = new ArrayList<VAObject>();
		for (PNode o : list) {
			result.add((VAObject) o.clone());
		}
		return result;
	}

	public static JideButton createCommandBarButton(String iconPath,
			String tooltipText) {
		JideButton button = new JideButton(getIcon(iconPath));
		button.setSize(22, 22);
		button.setToolTipText(tooltipText);

		return button;
	}

}
