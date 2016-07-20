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

import java.util.Iterator;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.core.PyTuple;

/**
 * Some utilities to work with python.
 */
public class PythonUtil {

    static {
	PySystemState.initialize();
    }

    /**
     * Adds the specified directory to python path.
     */
    public static void addPath(String dir) {
	Py.getSystemState().path.append(new PyString(dir));
    }

    public static void setArgv(Object[] array) {
	Py.getSystemState().argv = createPythonList(array);
    }
    
    /**
     * Creates a Python List using the specified array.
     */
    public static PyList createPythonList(Object[] array) {
	PyObject[] pyarray = new PyObject[array.length];

	for (int i = 0; i < array.length; i++)
	    pyarray[i] = Py.java2py(array[i]);
	
	return new PyList(pyarray);
    }

    /**
     * Creates a Python tuple using the specified array.
     */
    public static PyTuple createPythonTuple(Object[] array) {
	PyObject[] pyarray = new PyObject[array.length];

	for (int i = 0; i < array.length; i++)
	    pyarray[i] = Py.java2py(array[i]);
	
	return new PyTuple(pyarray);
    }
    
    /**
     * Creates a Python map using the specified map.
     */
    public static PyStringMap createPythonMap(Map map) {
	
	Iterator iter = map.entrySet().iterator();
	PyStringMap pymap = new PyStringMap(map.size());

	while (iter.hasNext()) {
	    Map.Entry entry = (Map.Entry) iter.next();

	    String name  = (String) entry.getKey();
	    Object value = entry.getValue();

	    pymap.__setitem__(name, Py.java2py(value));
	}

	return pymap;
    }
}
