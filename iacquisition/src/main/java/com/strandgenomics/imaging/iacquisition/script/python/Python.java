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

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 * Utility class to interact with jython system. This class provides an easy
 * interface to compile and execute python code just by importing this one
 * class.
 * 
 * TODO: Better handling of python exceptions.
 */
public class Python {

    static {
        PySystemState.initialize();
    }

    // {{{ public static Code comile(...)
    /**
     * Compiles the specified python script and returns a Code object
     * representing the compiled code.
     * 
     * @param script
     *            script to compile
     * @param label
     *            label of the code, this is shown in exceptions
     * @return compiled code object
     */
    public static Code compile(String script, String label) {
        PyCode pycode = Py.compile_flags(script, label, "exec", Py.getCompilerFlags());
        return new Code(pycode, label);
    }

    // }}}

    // //

    // {{{ exec methods
    /**
     * Executes the specified python script. This method uses possibly a
     * different interpreter for each call. So it is not guaranteed that state
     * set by one call to exec will be available to the next call.
     */
    public static void exec(String script) {
        createInterpreter().exec(script);
    }

    /**
     * Executes the specified python script after initialzing the environment of
     * the interpreter with the specified variables.
     * 
     * @see exec(String)
     */
    public static void exec(String script, Map variables) {
        // XXX: optimize
        Interpreter interp = createInterpreter();

        if (variables != null)
            interp.set(variables);

        interp.exec(script);
    }

    /**
     * Executes the specified python script after initialzing the environment of
     * the interpreter with the variables specified by the name-value pairs.
     * 
     * @deprecated use exec(String,Map) instead
     * @see exec(String)
     */
    public static void exec(String script, String[] names, Object[] values) {
        Interpreter interp = createInterpreter();

        for (int i = 0; i < names.length; i++)
            interp.set(names[i], values[i]);

        interp.exec(script);
    }

    /**
     * Convinient method for calling exec(String,Map)
     * 
     * @see #exec(String,Map)
     */
    public static void exec(String script, String name0, Object value0) {

    	 Map variables = new HashMap();
         variables.put(name0, value0);
         
        exec(script, variables);
    }

    /**
     * Convinient method for calling exec(String,Map).
     * 
     * @see #exec(String,Map)
     */
    public static void exec(String script, String name0, Object value0, String name1, Object value1) {

        Map variables = new HashMap();
        variables.put(name0, value0);
        variables.put(name1, value1);

        exec(script, variables);
    }

    // //

    /**
     * Executes the specified compiled code. This method uses possibly a
     * different interpreter for each call. So it is not guaranteed that state
     * set by one call to exec will be available to the next call.
     */
    public static void exec(Code code) {
        createInterpreter().exec(code);
    }

    /**
     * Executes the specified compiled code after initialzing the environment of
     * the interpreter with the specified variables.
     * 
     * @see exec(Code)
     */
    public static void exec(Code code, Map variables) {
        // XXX: optimize
        Interpreter interp = createInterpreter();

        Iterator iter = variables.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            String name = (String) entry.getKey();
            Object value = entry.getValue();

            interp.set(name, value);
        }

        interp.exec(code);
    }

    /**
     * Executes the specified compiled code after initialzing the environment of
     * the interpreter with the variables specified by the name-value pairs.
     * 
     * @deprecated use <code>exec(Code,Map)</code> instead.
     * @see exec(Code,Map)
     */
    public static void exec(Code code, String[] names, Object[] values) {
        Interpreter interp = createInterpreter();

        for (int i = 0; i < names.length; i++)
            interp.set(names[i], values[i]);

        interp.exec(code);
    }

    /**
     * Convinient method for calling exec(Code,Map).
     * 
     * @see #exec(Code,Map)
     */
    public static void exec(Code code, String name0, Object value0) {

    	 Map variables = new HashMap();
         variables.put(name0, value0);
        exec(code, variables);

    }

    /**
     * Convinient method for calling exec(Code,Map).
     * 
     * @see #exec(Code,Map)
     */
    public static void exec(Code code, String name0, Object value0, String name1, Object value1) {

    	 Map variables = new HashMap();
         variables.put(name0, value0);
         variables.put(name1, value1);

        exec(code, variables);
    }

    // }}}

    // {{{ eval, execfile and createInterpreter methods
    public static Object eval(String script) {
        return createInterpreter().eval(script);
    }

    public static void execfile(String filepath) {
        createInterpreter().execfile(filepath);
    }

    /**
     * Creates a new instance of Interpreter.
     */
    public static Interpreter createInterpreter() {
        return new Interpreter();
    }

    // }}}

    // {{{ public static RuntimeException extractException(PyException e)
    public static RuntimeException extractException(PyException e) {
        Object value = e.value.__tojava__(Object.class);
        Object type = e.type.__tojava__(Object.class);

        if (value instanceof Exception) {
            Exception cause = (Exception) value;

            if (cause instanceof PyException)
                return extractException((PyException) cause);

            if (cause instanceof RuntimeException)
                return (RuntimeException) cause;

            return new RuntimeException(cause.getMessage(), cause);
        } else if (type instanceof String) {
            String msg = (String) type;
            return new RuntimeException(msg, e);
        } else {
            return new RuntimeException("Unknown error", e);
        }
    } // }}}

    // {{{ public static final class Interpreter
    /**
     * Abstraction of python interpreter.
     */
    public static final class Interpreter {
        private static ThreadLocalWriter stdout;
        private static ThreadLocalWriter stderr;

        PythonInterpreter interp;

        Interpreter() {
            setupIO();

            interp = new PythonInterpreter();
            interp.exec(INIT_CODE.code);
        }

        /**
         * 
         */
        private synchronized void setupIO() {
            if (stderr==null || stdout==null) {
                stdout = new ThreadLocalWriter(new OutputStreamWriter(System.out));
                stderr = new ThreadLocalWriter(new OutputStreamWriter(System.err));
            }
        }

        /**
         * Returns the instance of PythonInterpreter this object is using.
         * <p>
         * Note: In general, there should not be any need to use this method
         * directly. Use with caution!
         */
        public PythonInterpreter getPythonInterpreter() {
            return interp;
        }

        /** Returns value of a varaible in the local namespace. */
        public Object get(String name) {
            PyObject o = interp.get(name);
            return (o == null) ? o : o.__tojava__(Object.class);
        }

        /** Sets value of a varaible in the local namespace. */
        public void set(String name, Object value) {
            interp.set(name, value);
        }

        /** Executes the specified python script. */
        public void exec(String script) {
            try {
                interp.setOut(stdout);
                interp.setErr(stderr);
                interp.exec(script);
            } catch (PyException e) {
                throw extractException(e);
            }
        }

        /** Executes the specified compiled python code. */
        public void exec(Code code) {
            try {
                interp.setOut(stdout);
                interp.setErr(stderr);
                interp.exec(code.code);
            } catch (PyException e) {
                throw extractException(e);
            }
        }

        /** Evaluates the specified python script and returns the result. */
        public Object eval(String script) {
            interp.setOut(stdout);
            interp.setErr(stderr);
            try {
                PyObject o = interp.eval(script);

                return (o == null) ? null : o.__tojava__(Object.class);
            } catch (PyException e) {
                throw extractException(e);
            }
        }

        public void execfile(String filename) {
            try {
                interp.execfile(filename);
            } catch (PyException e) {
                throw extractException(e);
            }
        }

        /** Sets the all the specified variables in the local namespace. */
        public void set(Map variables) {
            Iterator iter = variables.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();

                String name = (String) entry.getKey();
                Object value = entry.getValue();

                interp.set(name, value);
            }
        }

        public void setOut(Writer out) {
            stdout.set(out);
        }

        public void setErr(Writer err) {
            stderr.set(err);
        }
    }

    // }}}

    // {{{ public static final class Code
    /**
     * Abstraction of compiled python code.
     */
    public static final class Code {
        final PyCode code;
        final String label;

        Code(PyCode code, String label) {
            this.code = code;
            this.label = label;
        }

        /**
         * Returns label of this code. Label will be displayed in the error
         * messages.
         */
        public String getLabel() {
            return label;
        }

        public String toString() {
            return "compiled python code - " + label;
        }
    }

    // }}}

    public static void main(String[] args) throws Exception {
        execfile(args[0]);
    }

    private static Code INIT_CODE = Python.compile("import java, javax, com", "Python.INIT_CODE");
}
