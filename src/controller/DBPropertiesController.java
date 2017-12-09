package controller;

import java.util.ArrayList;

import model.DBTable;
import model.DatabaseOperations;

public class DBPropertiesController {
	
	DatabaseOperations databaseOperations = new DatabaseOperations();
	
	public ArrayList<DBTable> getAllTableStructure(){
		return databaseOperations.getAllDBTableStructure();
		
	}

}
