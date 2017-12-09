package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.swing.JPanel;
import javax.swing.JTextPane;


//Common interface for all operation in ETL

public interface ETLOperation {
	final static String DB_NAME = "DB Name:";
	final static String DB_USER_NAME = "DB User Name:";
	final static String DB_USER_PASSWORD = "DB User Passoword:";
	final static String BASE_IRI = "Base IRI:";
	final static String FILE_PATH = "File Location:";
	final static String FILE_NAME = "File Name:";
	final static String MAPPING_GRAPH_FILE = "Mapping Graph File:";
	final static String RDF_FILE = "RDF File:";
	final static String TBox_FILE = "TBox File:";
	
	//this method will be implemented by concrete classes. This will perform the operation
	public boolean execute(JTextPane textPane);
	
	//This method is responsible to take input for each operations
	public boolean getInput(JPanel panel, HashMap<String, LinkedHashSet<String>> inputParamsMap);
}
