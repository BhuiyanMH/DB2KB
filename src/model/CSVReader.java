package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class CSVReader {
	
	private BufferedReader csvBufferReader;
	private String row = "";
	private ArrayList<String> rows = new ArrayList<String>();
	
	public ArrayList<String> read(String filePath){
		
		try {
			csvBufferReader = new BufferedReader(new FileReader(filePath));
			
			try {
				while ((row = csvBufferReader.readLine())!= null) {
					rows.add(row);	
				}
				
			} catch (IOException e) {
				
				JOptionPane.showMessageDialog(null, "File Reading Failed");
			}
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, filePath + " not found!");
		}
		return rows;
	}

}
