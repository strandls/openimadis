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

