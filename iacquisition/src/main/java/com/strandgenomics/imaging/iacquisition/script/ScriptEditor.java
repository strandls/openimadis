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

package com.strandgenomics.imaging.iacquisition.script;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import jsyntaxpane.actions.DocumentSearchData;
import jsyntaxpane.actions.gui.GotoLineDialog;

import com.strandgenomics.imaging.iacquisition.script.python.Python;

/**
 * Script editor
 * 
 * @author Anup Kulkarni
 */
public class ScriptEditor implements ComponentListener, ActionListener {

	private JPanel panel;
	private JSplitPane splitPane;
	private JEditorPane scriptArea;
	private OutputTextArea outputArea;
//	private ColorMap scriptColorMap;
//	private ColorMap outputColorMap;
	private UndoManager manager = new UndoManager();

	private String method;
	private boolean lineWrap;
	private boolean wrapStyleWord;
	private int undoLimit;
	private String fileFilter;
	
	private String associatedScriptpath;

	public ScriptEditor() {
		init();
	}

	///
	private void init() {
		// Default text line wrapping policy
		lineWrap = true;
		wrapStyleWord = true;
		
		initializeView(new HashMap());

		// Default colorMap
//		scriptColorMap = createDefaultColorMap();
//		outputColorMap = createDefaultColorMap();
	}

//	private ColorMap createDefaultColorMap() {
//		HashMap map = new HashMap();
//		map.put("Background", Color.WHITE);
//		map.put("Text", Color.BLACK);
//		return new ColorMap(map);
//	}

	private void createGUI() {

		panel = new JPanel(new BorderLayout());
		
		ScriptEditorToolbar toolbar = new ScriptEditorToolbar(this);
		panel.add(toolbar, BorderLayout.NORTH);
		
		jsyntaxpane.syntaxkits.PythonSyntaxKit.initKit();
		scriptArea = new JEditorPane();
		JScrollPane scriptPane = new JScrollPane(scriptArea);
		scriptArea.setContentType("text/python");
		setupTextArea(scriptArea);
		
		scriptArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				updateTicker();
			}
		});

	
		scriptArea.getDocument().addUndoableEditListener(new UndoableEditListener(){
			public void undoableEditHappened(UndoableEditEvent ev){
				manager.addEdit(ev.getEdit());
			}
		});


		scriptArea.getActionMap().put("undo",
				new AbstractAction("undo") {
			public void actionPerformed(ActionEvent evt) {
				undo();
			}
		}
		);
		scriptArea.getActionMap().put("redo",
				new AbstractAction("undo") {
			public void actionPerformed(ActionEvent evt) {
				redo();
			}
		}
		);
		scriptArea.getActionMap().put("runScript",
				new AbstractAction("runScript") {
			public void actionPerformed(ActionEvent evt) {
				runScript();
			}
		}
		);

		scriptArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "undo");
		scriptArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "redo");
		scriptArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F11,0), "runScript");

		// Add output text area
		outputArea = new OutputTextArea();
		outputArea.setEditable(false);
		setupTextArea(outputArea);
		JScrollPane outputPane = new JScrollPane(outputArea);
		outputPane.setBorder(BorderFactory.createTitledBorder("Console"));

		// Add split pane
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scriptPane, outputPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(0.7);
		splitPane.repaint();
		
		panel.add(splitPane, BorderLayout.CENTER);
		panel.addComponentListener(this);
	}
	
	private void updateTicker() {
		int pos = scriptArea.getCaretPosition();

		char[] text = scriptArea.getText().substring(0, pos).toCharArray();

		// find #lines and column number in the text
		int line = 0;
		int col = 0;

		for (int i = 0; i < pos; i++) {
			col++;
			if (text[i] == '\n') {
				line++;
				col = 0;
			}
		}

		// line number and column should start with 1 (not 0)
		line++;
		col++;
	}

	private void setupTextArea(JEditorPane t) {
		t.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
		Document doc = t.getDocument();
		if (doc instanceof PlainDocument) {
			doc.putProperty(PlainDocument.tabSizeAttribute, 8);
		}
	}

	private void setupTextArea(JTextArea t) {
		// XXX-Anand: hard coding font and tabsize.
		// may be these things should be moved to uidefaults.conf
		t.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
		t.setTabSize(8);
	}

	/* button actions  */
	public void goToLine(){
		GotoLineDialog.showForEditor(scriptArea);
	}
	public void searchAndReplace(){
		   DocumentSearchData dsd = DocumentSearchData.getFromEditor(scriptArea);
	       dsd.showReplaceDialog(scriptArea);
	}
	
	public void searchText(){
		DocumentSearchData.getFromEditor(scriptArea).showQuickFindDialog(scriptArea);
	}

	public void openScriptFile() 
	{
		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showOpenDialog(panel);

		if (ret == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileChooser.getSelectedFile();
			try 
			{
				BufferedReader br = new BufferedReader(new FileReader(file));
				StringBuffer text = new StringBuffer();
				while(br.ready())
				{
					text.append(br.readLine());
					text.append("\n");
				}
				scriptArea.setText(text.toString());
			}
			catch (IOException e) {
				System.err.println("Unable to open " + file.getAbsolutePath());
			}
			finally {
				outputArea.clear();	
			}
		}
	}

	public void saveScriptToFile() 
	{

		String script = scriptArea.getText();

		if (script == null || script.length() == 0) {
			System.err.println("Empty script cannot be saved.");
			return;
		}
		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showSaveDialog(panel);
		if (ret == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileChooser.getSelectedFile();
			try 
			{
				PrintWriter pw = new PrintWriter(file);
				pw.print(script);
				pw.close();
			}
			catch (IOException e) {
				System.err.println("Unable to save script to the file.");
			}
		}
	}

	public void runScript() 
	{
		Thread scriptThread = new Thread() {
			public void run() {
				String script = scriptArea.getText();

				Python.Interpreter interpreter = Python.createInterpreter();

				Writer w = outputArea.getWriter();

				interpreter.setOut(w);
				interpreter.setErr(w);

				try 
				{
					safeWriteln(w, "-- beginning script execution --");

					if (method != null)
					{
						interpreter.set("_script", script);
						interpreter.set("_method_name", method);

						interpreter.exec("method = eval(_method_name)");
						interpreter.exec("method(_script)");
					}
					else
						interpreter.exec(script);
				}
				catch (RuntimeException e) 
				{
					writeException(e, w);
				}
				finally 
				{
					safeWriteln(w, "-- finished script execution --");
				}
			}
		};

		try 
		{
			scriptThread.start();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void safeWriteln(Writer w, String line) 
	{
		try 
		{
			w.write(line);
			w.write("\n");
			w.flush();
		}
		catch (IOException e) 
		{
		}
	}

	private void writeException(RuntimeException e, Writer w) 
	{
		Throwable cause = e.getCause();

		if (cause == null)
			e.printStackTrace(new PrintWriter(w));

		else if (cause instanceof org.python.core.PyException)
			safeWriteln(w, cause.toString());

		else
			cause.printStackTrace(new PrintWriter(w));
	}

	public void stopRunningScript() 
	{
		System.out.println(" Stopped the running script...");
	}   

	public void undo()
	{
		try
		{
			manager.undo();
		}
		catch(CannotUndoException e){ }
	}

	public void redo()
	{
		try
		{
			manager.redo();
		}
		catch(CannotRedoException e){ }
	}

	public boolean canUndo()
	{
		return manager.canUndo();
	}

	public boolean canRedo()
	{
		return manager.canRedo();
	}

	public void clearOutput() 
	{
		outputArea.clear();
	}

	///
	public void initializeView(Map properties) 
	{
		createGUI();
//		setProperties(properties);
	}

	public void cleanupView() 
	{
//		super.cleanupView();

		outputArea.cleanup();
		outputArea = null;
		scriptArea = null;
		splitPane = null;

		if (panel != null)
			panel.removeComponentListener(this);

		panel = null;
	}

	public Component getComponent() 
	{
		return panel;
	}

	public void setViewProperty(String name, Object value) 
	{
		if (name.equals("text")) {
			scriptArea.setText((String)value);
			scriptArea.setCaretPosition(scriptArea.getDocument().getLength());
		}
		else if (name.equals("method"))
			method = (String) value;

		else if (name.equals("fileFilter"))
			fileFilter = (String) value;

		else if (name.equals("scriptFont")) {
			if (value != null)
				scriptArea.setFont((Font)value);
		}

		else if (name.equals("path"))
			associatedScriptpath = (String) value;
		
		else if (name.equals("scriptColorMap")) {
			if (value != null) {
//				scriptColorMap = (ColorMap)value;
//				scriptArea.setBackground(scriptColorMap.getColor("Background"));
//				scriptArea.setForeground(scriptColorMap.getColor("Text"));
			}
		}

		else if (name.equals("outputFont")) {
			if (value != null)
				outputArea.setFont((Font)value);
		}

		else if (name.equals("outputColorMap")) {
			if (value != null) {
//				outputColorMap = (ColorMap)value;
//				outputArea.setBackground(outputColorMap.getColor("Background"));
//				outputArea.setForeground(outputColorMap.getColor("Text"));
			}
		}

		else if (name.equals("lineWrap")) {
			lineWrap = ((Boolean)value).booleanValue();
			outputArea.setLineWrap(lineWrap);
		}

		else if (name.equals("wrapStyleWord")) {
			wrapStyleWord = ((Boolean)value).booleanValue();
			outputArea.setWrapStyleWord(wrapStyleWord);
		}
		else if (name.equals("undoLimit")) {
			undoLimit = ((Integer)value).intValue();
			manager.setLimit(undoLimit);	    
		}	
//		else
//			super.setViewProperty(name, value);        
	}

	public Object getViewProperty(String name) {

		if (name.equals("text"))
			return scriptArea.getText();

		else if (name.equals("method"))
			return method;

		else if (name.equals("scriptFont"))
			return scriptArea.getFont();

		else if (name.equals("fileFilter"))
			return fileFilter;
		
		else if (name.equals("path"))
			return associatedScriptpath;

//		else if (name.equals("scriptColorMap"))
//			return scriptColorMap;

		else if (name.equals("outputFont"))
			return outputArea.getFont();

//		else if (name.equals("outputColorMap"))
//			return outputColorMap;

		else if (name.equals("lineWrap"))
			return Boolean.valueOf(lineWrap);

		else if (name.equals("wrapStyleWord"))
			return Boolean.valueOf(wrapStyleWord);

		else if (name.equals("undoLimit"))
			return Integer.valueOf(undoLimit);
		else
			return null;
//			return super.getViewProperty(name);        
	}

	/// ComponentListener methods

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		splitPane.setDividerLocation(0.7);
	}

	public void componentShown(ComponentEvent e) {
	}

	///


	private static class OutputTextArea extends JTextArea {

		private StringWriter writer = new StringWriter() {
			public void flush() {
				String contents = toString();
				setText(contents);
				setCaretPosition(getDocument().getLength());
			}
		};

		public void clear() {
			StringBuffer buffer = writer.getBuffer();
			buffer.delete(0, buffer.length());  
			setText("");
		}

		public Writer getWriter() {
			return writer;
		}

		public void cleanup(){
			writer = null;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if(command.equals("runScript")){
			runScript();
		} 
		else if(command.equals("openScript")){
			openScriptFile();
		} 
		else if(command.equals("saveScript")){
			saveScriptToFile();
		} 
		else if(command.equals("undoScript")){
			undo();
		} 
		else if(command.equals("redoScript")){
			redo();
		} 
		else if(command.equals("clearOutput")){
			clearOutput();
		} 
		else if(command.equals("gotoLine")){
			goToLine();
		} 
		else if(command.equals("findReplace")){
			searchAndReplace();
		} 
	}
	
	public void launchScriptEditor()
	{
		JPanel panel = (JPanel) getComponent();
		
		JFrame frame = new JFrame("Script Editor");
		frame.add(panel);
		frame.setSize(400,400);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.out.println("window closing");
				cleanupView();
			}
		});
	}
	
	public static void main(String args[]){
		ScriptEditor sc = new ScriptEditor();
		sc.launchScriptEditor();
	}
}
