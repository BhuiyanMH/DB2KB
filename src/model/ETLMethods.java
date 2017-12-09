package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BooleanDeserializer;

public class ETLMethods {

	private DBParams dbParams;
	DatabaseOperations databaseOperations;

	public ETLMethods(DBParams dbParams) {
		this.dbParams = dbParams;

		databaseOperations = new DatabaseOperations(dbParams.getDbName(), dbParams.getDbUserName(),
				dbParams.getDbUserPassword());
	}

	public DBParams getDbParams() {
		return dbParams;
	}

	public void setDbParams(DBParams dbParams) {
		this.dbParams = dbParams;
	}

	public boolean performETL(String folderPath, String baseIRI, JTextPane status) {

		CommonMethods commonMethods = new CommonMethods();
		
		//getAll Table Structure
		ArrayList<String> rmlMappingFileList = new ArrayList<>();
		ArrayList<DBTable> dbTableStructures = databaseOperations.getAllDBTableStructure();		
		
		
		//getRMLFile
		RMLFile rmlFile = new RMLFile();
		ArrayList<TripleMap> tripleMaps = rmlFile.getTripleMapsTC(dbTableStructures, baseIRI);
		String rmlFileString = rmlFile.getRMLFile(tripleMaps, baseIRI);
		
		String filePathName = folderPath+"\\"+dbParams.getDbName()+"_R2RMLGraph"+"_"+commonMethods.getDateTime()+".ttl";		
		boolean flag = writeFile(filePathName, rmlFileString);
		if(flag)
			status.setText(status.getText().toString()+"\nR2RML Mapping Graph Saved: "+ filePathName);
			
		//generate RDF from RMl File
		RMLProcessor rmlProcessor = new RMLProcessor();
		String[] lines = rmlFileString.split("\n");
		ArrayList<String> rmlFileLines = new ArrayList<>();
		int size = lines.length;
		for(int i=0; i<size; i++){
			rmlFileLines.add(lines[i]);
		}
		//JOptionPane.showMessageDialog(null, "Triple Generation in Progress.\nPlease wait...");
		status.setText(status.getText().toString()+"\nTriple Generation in Progress. Please wait...");
		ArrayList<String> rdfTriples  = rmlProcessor.getRDFTriples(rmlFileLines);
		String rdfString = "";
		for(String triple: rdfTriples){
			rdfString+=triple+"\n";			
		}
		
		filePathName = folderPath+"\\"+dbParams.getDbName()+"_R2RML_RDF"+"_"+commonMethods.getDateTime()+".n3";		
		flag = writeFile(filePathName, rdfString);
		if(flag){
			status.setText(status.getText().toString()+"\nRDF File Saved: "+ filePathName);
			status.setText(status.getText().toString()+"\nETL compleated.");
		}else{
			status.setText(status.getText().toString()+"\nETL Failed.");
		}
		return true;
	}
	
	private boolean writeFile(String filePathName, String data){
		
		PrintWriter out;
		try {
			out = new PrintWriter(filePathName);
			out.println(data);
			out.close();
			return true;
		} catch (FileNotFoundException error) {
			JOptionPane.showMessageDialog(null, "File Saving Failed");
			return false;
		}
	}
	
	
	

}
