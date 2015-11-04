package com.strandgenomics.imaging.iacquisition.script.python;

import org.python.core.PyException;

/**
 * Wrapper over PyException.
 */
public class PythonException 
extends RuntimeException {
    PyException e;
    PythonException(PyException e) {
	super("Error", e);
	this.e = e;
    }

    public String getMessage() {
	Object value = e.value.__tojava__(Object.class);
	Object type = e.type.__tojava__(Object.class);

	if (value instanceof Exception) {
	    Exception cause = (Exception) value;
	    return cause.getMessage();
	}
	else if (type instanceof String) {
	    return (String) type;
	}
	
	return "Unknown error";
    }
}
