/*
 * HorizontalButtonPanel.java
 *
 * AVADIS Image Management System
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */

package com.strandgenomics.imaging.iclient.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Convenient class to create a JPanel representing a bunch of horizontal array of control buttons
 * @author arunabha
 *
 */
@SuppressWarnings("serial")
public class HorizontalButtonPanel extends JPanel {
	
	public static final Font BUTTON_FONT = new Font ("Arial", Font.PLAIN, 11);

    public HorizontalButtonPanel(Action[] actions, int componentOrientation) {

        setLayout(new FlowLayout(componentOrientation, 5, 5));

        for(int i = 0; i < actions.length; i++ ){
            JButton actionButton = new JButton( actions[i] );
            actionButton.setFont( BUTTON_FONT );
            actionButton.setPreferredSize(new Dimension(75,23));

            // bind the action with key Stroke when button is selected.
            InputMap map = actionButton.getInputMap();
                if (map != null){
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "pressed");
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true), "released");
                }
            add( actionButton );
        }

    }
    
    public HorizontalButtonPanel(Action[] actions, int componentOrientation,int horizontalGap, int verticalGap) {

        setLayout(new FlowLayout(componentOrientation,horizontalGap,verticalGap));

        for(int i = 0; i < actions.length; i++ ){
            JButton actionButton = new JButton( actions[i] );
            actionButton.setFont( BUTTON_FONT );
            actionButton.setPreferredSize(new Dimension(75,23));

            // bind the action with key Stroke when button is selected.
            InputMap map = actionButton.getInputMap();
                if (map != null){
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "pressed");
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true), "released");
                }
            add( actionButton );
        }

    }
    
    public HorizontalButtonPanel(Action[] actions, int componentOrientation,Dimension dimension) {

        setLayout(new FlowLayout(componentOrientation,5,5));

        for(int i = 0; i < actions.length; i++ ){
            JButton actionButton = new JButton( actions[i] );
            actionButton.setFont( BUTTON_FONT );
            if (dimension != null){
                actionButton.setPreferredSize(dimension);
            }

            // bind the action with key Stroke when button is selected.
            InputMap map = actionButton.getInputMap();
                if (map != null){
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "pressed");
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true), "released");
                }
            add( actionButton );
        }

    }
    
    public HorizontalButtonPanel(Action[] actions, int componentOrientation,
            Dimension dimension, int horizontalGap, int verticalGap) {

        setLayout(new FlowLayout(componentOrientation,horizontalGap,verticalGap));

        for(int i = 0; i < actions.length; i++ ){
            JButton actionButton = new JButton( actions[i] );
            actionButton.setFont( BUTTON_FONT );
            if (dimension != null){
                actionButton.setPreferredSize(dimension);
            }

            // bind the action with key Stroke when button is selected.
            InputMap map = actionButton.getInputMap();
                if (map != null){
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "pressed");
                        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true), "released");
                }
            add( actionButton );
        }

    }
    
    public HorizontalButtonPanel(JButton[] buttons, int componentOrientation,Dimension dimension) {

        setLayout(new FlowLayout(componentOrientation,5,5));

        for(int i = 0; i < buttons.length; i++ ){
            buttons[i].setFont( BUTTON_FONT );
            if (dimension != null)
            {
                buttons[i].setPreferredSize(dimension);
            }
            else
            {
                buttons[i].setPreferredSize(new Dimension(75,23));
            }

            add( buttons[i] );
        }
    }
    
    public HorizontalButtonPanel(JButton[] buttons, int componentOrientation, int horizontalGap, int verticalGap, Dimension dimension) {

        setLayout(new FlowLayout(componentOrientation,horizontalGap,verticalGap));

        for(int i = 0; i < buttons.length; i++ )
        {
            buttons[i].setFont( BUTTON_FONT );
            if (dimension != null){
                buttons[i].setPreferredSize(dimension);
            }

            add( buttons[i] );
        }
    }
}