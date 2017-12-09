package controller;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import model.CSVReader;
import model.RDFGenerator;
import view.MainFrame;

public class OperationsController {
	
	CSVReader csvReader = new CSVReader();
	String filePath = "";
	String fileName = "";
	
	public String csvButtonHandler(JFrame frame ){
		
		//Create a file chooser
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);
		
		
		int returnVal = fileChooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			
			File csvFile = fileChooser.getSelectedFile();
			filePath = csvFile.getPath();
			fileName = csvFile.getName();
		}
		return filePath;
	}
	
	public ArrayList<String> runButtonHandler(){
		
		ArrayList<String> rows = csvReader.read(filePath);
		
		for(int i=0; i<rows.size(); i++){
			System.out.println(rows.get(i));
		}
		
		RDFGenerator rdfGenerator = new RDFGenerator();
		return rdfGenerator.generateRDF(fileName, rows);
	}

}
