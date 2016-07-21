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