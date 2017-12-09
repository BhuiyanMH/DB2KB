package controller;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import model.CSVReader;
import model.RDFGenerator;

public class CSVPanelController {
	
	CSVReader csvReader = new CSVReader();
	String filePath = "";
	String fileName = "";
	
	public String selectButtonHandler(JFrame frame ){
		
		//Create a file chooser
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setDialogTitle("Select CSV file to open");
		fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);
		
		
		int returnVal = fileChooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			
			File csvFile = fileChooser.getSelectedFile();
			filePath = csvFile.getPath();
			fileName = csvFile.getName();
		}
		return filePath;
	}
	
	public ArrayList<String> ConvertButtonHandler(){
		
		ArrayList<String> rows = csvReader.read(filePath);
		
//		for(int i=0; i<rows.size(); i++){
//			System.out.println(rows.get(i));
//		}
//		
		RDFGenerator rdfGenerator = new RDFGenerator();
		return rdfGenerator.generateRDF(fileName, rows);
	}


}
