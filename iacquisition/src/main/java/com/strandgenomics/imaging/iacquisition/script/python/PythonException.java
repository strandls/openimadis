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
