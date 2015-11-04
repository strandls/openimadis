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
