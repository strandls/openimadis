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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jidesoft.swing.JideButton;

public class FrameSliceControlPanel extends JPanel {

	final private JSlider frameSlider;
	final private JSlider sliceSlider;
	final private JLabel sliceTextLabel;
	final private JLabel frameTextLabel;
	
	private int currentFrame = 0;
	private int currentSlice = 0;

	boolean updating = false;
	private ImageViewerApplet imageViewerApplet;
	private JideButton sliceNextButton;
	private JideButton slicePrevButton;
	private JideButton framePrevButton;
	private JideButton frameNextButton;

	public FrameSliceControlPanel(ImageViewerApplet imageViewerApplet) {
		
		this.imageViewerApplet = imageViewerApplet;

		sliceTextLabel = new JLabel(" Z :");
		sliceTextLabel.setMinimumSize(new Dimension(100, 20));
		frameTextLabel = new JLabel(" T :");

		frameSlider = new JSlider();
		frameSlider.setMinorTickSpacing(1);
		frameSlider.setBorder(BorderFactory.createEmptyBorder());
		frameSlider.setPaintLabels(false);

		sliceSlider = new JSlider();
		sliceSlider.setMinorTickSpacing(1);
		sliceSlider.setBorder(BorderFactory.createEmptyBorder());
		
		sliceNextButton = UIUtils.createCommandBarButton("Forward16.gif", "next slice");
		slicePrevButton = UIUtils.createCommandBarButton("Back16.gif", "previous slice");
		framePrevButton = UIUtils.createCommandBarButton("Back16.gif", "previous frame");
		frameNextButton = UIUtils.createCommandBarButton("Forward16.gif", "next frame");

		this.setBorder(BorderFactory.createEmptyBorder());

		final GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(false);
		layout.setAutoCreateContainerGaps(false);

		final GroupLayout.SequentialGroup hGroup = layout
				.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup().addComponent(frameTextLabel).addComponent(sliceTextLabel));
		hGroup.addGroup(layout.createParallelGroup().addComponent(framePrevButton).addComponent(slicePrevButton));
		hGroup.addGroup(layout.createParallelGroup().addComponent(sliceSlider).addComponent(frameSlider));
		hGroup.addGroup(layout.createParallelGroup().addComponent(frameNextButton).addComponent(sliceNextButton));
		layout.setHorizontalGroup(hGroup);
		final GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(framePrevButton).addComponent(frameTextLabel).addComponent(frameSlider).addComponent(frameNextButton));
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(slicePrevButton).addComponent(sliceTextLabel).addComponent(sliceSlider).addComponent(sliceNextButton));
		layout.setVerticalGroup(vGroup);
		
		addChangeListeners();
		
		updateSliders();
	}
	
	/**
	 * called by ImageViewerApplet when a new record is selected
	 */
	public void updateSliders()
	{
		updating = true;
		int maxFrame = imageViewerApplet.getMaxFrame();
		int maxSlice = imageViewerApplet.getMaxSlice();

		final int sliceState = imageViewerApplet.getSliceState();

		currentSlice = imageViewerApplet.getSlice();
		currentFrame = imageViewerApplet.getFrame();
		
		String frameTT = " T : " + (currentFrame+1) + "/" + maxFrame;
		String sliceTT = " Z : " + (currentSlice+1) + "/" + maxSlice;
		
		if(maxFrame<=0)
		{
			frameTT = " T : " + (currentFrame) + "/" + maxFrame;
			frameSlider.setEnabled(false);
			frameNextButton.setEnabled(false);
			framePrevButton.setEnabled(false);
		} 
		else
		{
			frameSlider.setEnabled(true);
			frameNextButton.setEnabled(true);
			framePrevButton.setEnabled(true);
		}
		
		if(maxSlice<=0 || sliceState==ImageViewerState.SliceState.Z_STACK)
		{
			sliceTT = " Z : " + (currentSlice) + "/" + maxSlice;
			sliceSlider.setEnabled(false);
			sliceNextButton.setEnabled(false);
			slicePrevButton.setEnabled(false);
		} 
		else 
		{
			sliceSlider.setEnabled(true);
			sliceNextButton.setEnabled(true);
			slicePrevButton.setEnabled(true);
		}
		
		frameSlider.setMaximum(maxFrame);
		frameSlider.setValue(currentFrame+1);
		frameSlider.setMinimum(1);
		sliceSlider.setMaximum(maxSlice);
		sliceSlider.setValue(currentSlice+1);
		sliceSlider.setMinimum(1);
//		frameSlider.setToolTipText(currentFrameVal + " s");
//		sliceSlider.setToolTipText(currentSliceVal + " um");

		frameTextLabel.setText(frameTT);
//		Font frameFont = frameTextLabel.getFont(); 
//		frameTextLabel.setFont(new Font(frameFont.getName(), Font.PLAIN, frameFont.getSize()));
		sliceTextLabel.setText(sliceTT);
//		Font sliceFont = sliceTextLabel.getFont(); 
//		sliceTextLabel.setFont(new Font(sliceFont.getName(), Font.PLAIN, sliceFont.getSize()));

		revalidate();
		
		updating = false;
	}
	
	public void addChangeListeners() 
	{
		frameSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) 
			{
				JSlider slider = (JSlider) e.getSource();
				if (!slider.getValueIsAdjusting() && !updating)
				{
					int newFrame = frameSlider.getValue() - 1;
					if (currentFrame != newFrame) 
					{
						currentFrame = newFrame;
						imageViewerApplet.setFrame(newFrame);
					}
				}
				else
				{
					System.out.println("Frame is adjusting...");
				}
			}
		});

		sliceSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) 
			{
				JSlider slider = (JSlider) e.getSource();
				if (!slider.getValueIsAdjusting() && !updating)
				{
					int newSlice = sliceSlider.getValue() - 1;
					if (currentSlice != newSlice) {
						currentSlice = newSlice;
						imageViewerApplet.setSlice(newSlice);
					}
				}
				else
				{
					System.out.println("Slice is adjusting...");
				}
			}
		});
		
		frameNextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currValue = frameSlider.getValue();
				int newValue = currValue + 1;
				if(newValue > frameSlider.getMaximum())
					return;
				frameSlider.setValue(newValue);
			}
		});
		
		sliceNextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currValue = sliceSlider.getValue();
				int newValue = currValue + 1;
				if(newValue > sliceSlider.getMaximum())
					return;
				sliceSlider.setValue(newValue);
			}
		});
		
		framePrevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currValue = frameSlider.getValue();
				int newValue = currValue - 1;
				if(newValue < frameSlider.getMinimum())
					return;
				frameSlider.setValue(newValue);
			}
		});
		
		slicePrevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currValue = sliceSlider.getValue();
				int newValue = currValue - 1;
				if(newValue < sliceSlider.getMinimum())
					return;
				sliceSlider.setValue(newValue);
			}
		});

	}

	public void setSliceSliderEnabled(boolean value){
		sliceSlider.setEnabled(value);
		slicePrevButton.setEnabled(value);
		sliceNextButton.setEnabled(value);
	}
}
