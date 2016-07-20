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
