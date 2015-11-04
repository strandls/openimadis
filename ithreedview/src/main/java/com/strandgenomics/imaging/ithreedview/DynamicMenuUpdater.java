package com.strandgenomics.imaging.ithreedview;

import javax.swing.JDialog;

import com.strandgenomics.imaging.ithreedview.listeners.EventMulticaster;
import com.strandgenomics.imaging.ithreedview.listeners.IThreeDListener;
import com.strandgenomics.imaging.ithreedview.listeners.ThreeDChangeEvent;

/**
 * @author sandeept
 * this class is used to fire event on 3d view status(open/close) change.
 * Listener to this class is in project module. As viz does not depend up on 
 * project we need to use event mechanism instead of simple function call for msg passing. 
 */
public class DynamicMenuUpdater {

    private static DynamicMenuUpdater instance;
    private EventMulticaster eventMulticaster;

    private DynamicMenuUpdater() {
        eventMulticaster = new EventMulticaster(
                LauncherEventDispatcher.INSTANCE);
    }

    public static DynamicMenuUpdater getInstance() {
        if (instance == null) {
            instance = new DynamicMenuUpdater();
        }
        return instance;
    }

    public void threeDOpened() {
        eventMulticaster.fireEvent(new ThreeDChangeEvent(
                ThreeDChangeEvent.OPENED, get3DLauncher()));
    }

    public void threeDActivated() {
        eventMulticaster.fireEvent(new ThreeDChangeEvent(
                ThreeDChangeEvent.ACTIVATED, get3DLauncher()));
    }

    public void threeDClosed() {
        eventMulticaster.fireEvent(new ThreeDChangeEvent(
                ThreeDChangeEvent.CLOSING, get3DLauncher()));
    }

    private JDialog get3DLauncher() {
        return Launcher.getInstance();
    }

    // /////////////////////////////////////////////////
    // ////////////// EVENTS FRAMEWORK /////////////////
    // /////////////////////////////////////////////////
    /**
     * add the ThreeDListener
     */
    public void add3DChangeListener(IThreeDListener l) {
        eventMulticaster.addListener(l);
    }

    /**
     * Remove the ThreeDListener.
     */
    public void remove3DChangeChangeListener(IThreeDListener l) {
        eventMulticaster.removeListener(l);
    }

    /**
     * The dispatcher class for the Multicaster. dispatches the event to the
     * listeners.
     */
    static class LauncherEventDispatcher implements
            EventMulticaster.IEventDispatcher {
        public static final LauncherEventDispatcher INSTANCE = new LauncherEventDispatcher();

        public void dispatchEvent(Object listenerObject, Object eventObject) {

            IThreeDListener listener = (IThreeDListener) listenerObject;
            ThreeDChangeEvent event = (ThreeDChangeEvent) eventObject;
            listener.stateChanged(event);
        }
    }

}
