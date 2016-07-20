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

import java.io.IOException;
import java.io.Writer;

/**
 * The ThreadLocalWriter class is an output stream wrapper, 
 * which writes the data to different streams based on the calling thread.
 */
public class ThreadLocalWriter 
extends Writer {
    
    /** ThreadLocal to store Writers for different threads. */
    ThreadLocal threadLocal = new ThreadLocal();

    private Writer defaultWriter;

    public ThreadLocalWriter(Writer defaultWriter) {
	this.defaultWriter = defaultWriter;
    }

    /** 
     * Sets value of output stream for the calling thread.
     */
    public void set(Writer out) {
	threadLocal.set(out);
    }

    // {{{ implementation Writer methods
    public void close() throws IOException {
	getWriter().close();
    }

    public void flush() throws IOException {
	getWriter().flush();
    }

    public void write(char[] cbuf, int offset, int len) 
    throws IOException {
	getWriter().write(cbuf, offset, len);
    }

    /**
     * Returns output stream for the calling thread.
     * Default output stream is returned if no output stream 
     * is set for the calling thread.
     * @see set(Writer)
     * @see setDefaultWriter(Writer)
     */
    private Writer getWriter() {
	Writer out = (Writer) threadLocal.get();
	return (out == null) ? defaultWriter : out;
    }
    // }}}
}

