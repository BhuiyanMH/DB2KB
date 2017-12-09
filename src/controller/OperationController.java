package controller;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


import model.Pair;
import store.FileOperations;
import store.WebOperations;

public class OperationController {
	String filePath = "";
	String fileName = "";
	String webFileName = "dbpediaData.json";
	
	public String chooseFile() {
		// Create a file chooser
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);

		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			filePath = file.getPath();
			fileName = file.getName();
		}
		return filePath;
	}

	public ArrayList<Object> extractResource(String filePath2) {
		// TODO Auto-generated method stub
		ArrayList<Object> resourceList = new ArrayList<>();
		
		if (filePath2 != null && !filePath2.equals("")) {
			// System.out.println("Good");
			FileOperations operations = new FileOperations();
			resourceList = operations.getAllResources(filePath2);
		} else {
			errorMessage("File Path is Empty");
		}
		
		return resourceList;
	}

	public void errorMessage(String string) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, string, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public ArrayList<Pair> extractProperties(String filePath2, Object keyResource) {
		// TODO Auto-generated method stub
		ArrayList<Pair> propertylist = new ArrayList<>();
		
		if (keyResource != null) {
			FileOperations operations = new FileOperations();
			propertylist = operations.getAllProperties(filePath2, keyResource);
		} else {
			errorMessage("Selected value is null");
		}
		
		return propertylist;
	}

	public ArrayList<Object> getPropertyNames(ArrayList<Pair> propertylist) {
		// TODO Auto-generated method stub
		ArrayList<Object> nameList = new ArrayList<>();
		for (int i = 0; i < propertylist.size(); i++) {
			Pair pair = propertylist.get(i);
			Object object = pair.getKey();
			nameList.add(object);
		}
		
		return nameList;
	}

	public ArrayList<Pair> updateProperties(ArrayList<Pair> propertylist, int index) {
		// TODO Auto-generated method stub
		if (propertylist.isEmpty()) {
			errorMessage("There are no properties");
		} else {
			Pair pair = propertylist.get(index);
			Object newKey = JOptionPane.showInputDialog("Original KEY: " + pair.getKey());
			Object value = pair.getValue();
			
			if (newKey != null && !newKey.equals("")) {
				propertylist.remove(index);
				Pair pair2 = new Pair();
				pair2.setKey(newKey);
				pair2.setValue(value);
				propertylist.add(pair2);
			}
		}
		return propertylist;
	}

	public ArrayList<Pair> removeProperties(ArrayList<Pair> propertylist, int[] arrays) {
		// TODO Auto-generated method stub
		ArrayList<Pair> newList = new ArrayList<>();
		for (int i = 0; i < arrays.length; i++) {
			int index = arrays[i];
			Pair pair = propertylist.get(index);
			newList.add(pair);
		}
		return newList;
	}

	public String getSearchValue(Object searchProperty, ArrayList<Pair> propertylist) {
		// TODO Auto-generated method stub
		String value = null;
		
		for (int i = 0; i < propertylist.size(); i++) {
			Pair pair = propertylist.get(i);
			Object object = pair.getKey();
			
			if (searchProperty.toString().equals(object.toString())) {
				value = pair.getValue().toString();
				break;
			}
		}
		return value;
	}

	public ArrayList<Pair> retrieveData(String searchValue, int hits) {
		// TODO Auto-generated method stub
		ArrayList<Pair> resultList = new ArrayList<>();
		
		if (searchValue != null && hits > 0) {
			WebOperations operations = new WebOperations();
			operations.fetchData(webFileName, searchValue, hits);
			
			File file = new File(webFileName);
			if (file.exists()) {
				resultList = operations.parseData(webFileName);
			}
		} else {
			errorMessage("Select a search property and enter a valid Hit Number");
		}
		
		return resultList;
	}

	public void showMessage(String message) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, message);
	}

	public ArrayList<Object> retrieveResource(String filePath2, int hits) {
		// TODO Auto-generated method stub
		ArrayList<Object> resultList = new ArrayList<>();
		if (hits > 0) {
			FileOperations operations = new FileOperations();
			resultList = operations.getTargetResources(filePath2, hits);
		}
		return resultList;
	}

}
