
package com.strandgenomics.imaging.ithreedview.listeners;

import java.util.Vector;

/**
 * The <code>EventMulticaster</code> class manages list of listeners 
 * effectively and handles multicasting of events.
 * It provides <code>addListener</code> and 
 * <code>removeListener</code> methods for adding and removing listners.
 * Special care is taken to handle removal of listeners from the listener 
 * methods (while the event is being fired).
 * <p>
 * The <code>EventMulticaster</code> works with any type of listeners 
 * and events. 
 * For creating an EventMulticaster object the caller must
 * provide an <code>IEventDispatcher<code> object. That event dispatcher
 * is used to call the listener method on individual listeners.
 */
public class EventMulticaster {

    /** list of listeners */
    private Vector listenerList;
    
    /** 
     * Flag to indicate whether any removal of listeners happend while
     * the event dispatching is in process.
     * @see #removeListener(Object)
     */
    private boolean needsCleaning;

    /**
     * Flag to indicate whether or not the event dispatching is in process.
     */
    private boolean eventDispatchingInProcess;

    /** 
     * Event dispacher to dispatch the events. 
     * This is used in fireEvent method.
     */
    private IEventDispatcher eventDispatcher;
    
    /**
     * Constructs a new EventMulticaster.
     * @param eventDispatcher event dispatcher for dispatching individual events
     */
    public EventMulticaster(IEventDispatcher eventDispatcher) {
	this.eventDispatcher = eventDispatcher;
    }

    /** 
     * Returns the number of listeners added to this event multicaster.
     */
    public int getListenerCount() {
	return (listenerList == null) ? 0 : listenerList.size();
    }

    /**
     * Adds the specified listener to the list of listners.
     */
    public void addListener(Object listener) {
	if (listenerList == null)
	    listenerList = new Vector();

	listenerList.add(listener);
    }

    /**
     * Removes the specified listener to the list of listners.
     */
    public void removeListener(Object listener) {
	if (listenerList == null)
	    return;

	int index = listenerList.indexOf(listener);
	if (index < 0) 
	    return;
	
	if (eventDispatchingInProcess) {
	    // When this method is called while firing events, listener 
	    // should not be removed from the list immediately.  Which might cause 
	    // unexpected beheavior because the listenerList is in use there.
	    // To prevent that the listener is replaced by null and cleaned later 
	    // in fireEvent method.
	    listenerList.set(index, null);
	    needsCleaning = true;

	    if (listenerList.size() == 0)
		listenerList = null;
	}
	else {
	    listenerList.remove(index);
	}
    }

    public void removeAll() {
        if (listenerList != null)
	    listenerList.clear();
    }

    /**
     * Fires the specified event.
     */
    public void fireEvent(Object event) {
	if (listenerList == null)
	    return;

	eventDispatchingInProcess = true;

	int size = listenerList.size();
	for (int i=0; i<size; i++) {
	    Object listener = listenerList.get(i);
	    try {
		if (listener != null)
		    eventDispatcher.dispatchEvent(listener, event);
	    }
	    catch (Throwable e) {
		e.printStackTrace();
	    }
	}

	eventDispatchingInProcess = false;

	if (needsCleaning) {
	    cleanListenerList();
	    needsCleaning = false;
	}
    }

    // removes null values from the listenerList
    private void cleanListenerList() {
	int size = listenerList.size();
	for (int i = size-1; i >= 0; i--) {
	    if (listenerList.get(i) == null)
		listenerList.remove(i);
	}
    }

    /**
     * The Event dispatcher interface.
     * 
     */
    public static interface IEventDispatcher {

	/**
	 * Calls the appropriate method on the specified listner with 
	 * the specified event as an argument.
	 */
	public void dispatchEvent(Object listener, Object event);
    }
}
