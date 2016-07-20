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

package com.strandgenomics.imaging.iacquisition;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class AboutDialogBox extends JDialog {
    private ImageIcon company;
    private ImageIcon product;
    private String productName;
    private String version;
    private String build;

    private String copyright;
    private static final String title = "About Acquisition-Client";

    public AboutDialogBox(JFrame parent) {
        super(parent, title, true);
        setResizable(false);
    }

    public AboutDialogBox(JFrame parent, String productTitle) {
        super(parent, productTitle, true);
        setResizable(false);
    }

    public void setCompanyIcon(String companyIcon) {
//        company = IconManager.getIcon(companyIcon);
    }

    public void setProductIcon(ImageIcon productIcon) {
//        product = IconManager.getIcon(productIcon);
    	this.product = productIcon;
    }

    @Override
	public void setName(String name) {
        productName = name;
    }

    public void setCopyright(String cpRight) {
        copyright = cpRight;
    }

    public void setVersion(String v) {
        version = v;
    }

    public void setBuild(String b) {
        build = b;
    }

    public JPanel label(ImageIcon icon) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel(icon));

        return panel;
    }

    public JPanel label(String text) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);

        panel.add(ta);
        return panel;
    }

    @Override
	public void show() {
        buildGUI();
        super.show();
    }

    private JPanel createControls() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panel.add(closeButton);
        getRootPane().setDefaultButton(closeButton);

        return panel;
    }

    private void buildGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        if (copyright == null)
            copyright = "Copyright (c) 2003 - 2006 Strand Life Sciences Pvt Ltd.";

        String ver = "Version " + version + " - Build " + build;

        panel.add(label(product));
        panel.add(label(productName));
        panel.add(label(ver));
        panel.add(label(copyright));
        panel.add(label(company));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(createControls(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

}
