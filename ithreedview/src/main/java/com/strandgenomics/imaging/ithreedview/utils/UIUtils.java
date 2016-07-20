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

package com.strandgenomics.imaging.ithreedview.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

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

	public static JideButton createCommandBarButton(String iconPath,
			String tooltipText) {
		JideButton button = new JideButton(getIcon(iconPath));
		button.setSize(22, 22);
		button.setToolTipText(tooltipText);

		return button;
	}

}
