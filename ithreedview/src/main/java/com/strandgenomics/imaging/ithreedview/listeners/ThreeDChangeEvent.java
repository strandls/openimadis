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

package com.strandgenomics.imaging.ithreedview.listeners;

import javax.swing.JDialog;

public class ThreeDChangeEvent {
    
    public final static int OPENED = 0;
    public final static int ACTIVATED = 1;
    public final static int CLOSING = 2;
    
    private int eventType;
    private JDialog source;
    
    public ThreeDChangeEvent(int eventType, JDialog source){
        this.eventType = eventType;
        this.source = source;
    }

    public int getEventType() {
        return eventType;
    }

    public JDialog getSource() {
        return source;
    }
}
