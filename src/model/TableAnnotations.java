package model;

import java.util.ArrayList;
import java.util.HashMap;

public class TableAnnotations {
	
	private HashMap<String, ArrayList<String>> conceptAnnotations;
	private HashMap<String, ArrayList<String>> propertyAnnotations;
	
	public TableAnnotations(HashMap<String, ArrayList<String>> conceptAnnotations,
			HashMap<String, ArrayList<String>> propertyAnnotations) {
		this.conceptAnnotations = conceptAnnotations;
		this.propertyAnnotations = propertyAnnotations;
	}
	
	public HashMap<String, ArrayList<String>> getConceptAnnotations() {
		return conceptAnnotations;
	}
	public void setConceptAnnotations(HashMap<String, ArrayList<String>> conceptAnnotations) {
		this.conceptAnnotations = conceptAnnotations;
	}
	public HashMap<String, ArrayList<String>> getPropertyAnnotations() {
		return propertyAnnotations;
	}
	public void setPropertyAnnotations(HashMap<String, ArrayList<String>> propertyAnnotations) {
		this.propertyAnnotations = propertyAnnotations;
	}
	
}
