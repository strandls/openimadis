package com.strandgenomics.imaging.iacquisition.script.python;

import org.python.core.Py;
import org.python.core.PyJavaClass;
import org.python.core.PyObject;

/**
 * TODO: write docs
 */
public class PythonObject extends PyObject {
    
    private PyObject self;
    private PyObject tojavaMethod;
    
    public PythonObject(PyObject self, PyObject tojavaMethod) {
	this.self = self;
	this.tojavaMethod = tojavaMethod;
    }

    public Object __tojava__(Class clazz) {
	if (clazz == PyObject.class)
	    return this;

	PyObject pyclazz = PyJavaClass.lookup(clazz);
	return tojavaMethod.__call__(pyclazz).__tojava__(clazz);
    }

    public PyObject __findattr__(String key) {
	return self.__getattr__(key);
    }

    /**
     * iteration support.
     */
    public PyObject __finditem__(int index) {
	if (0 <= index && index < self.__len__())
	    return self.__getitem__(Py.newInteger(index));

	return null;
    }
}
