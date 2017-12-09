package model;

import java.util.ArrayList;
import java.util.HashMap;

public class TBoxModel {
	
	HashMap<String, String> prefixMap;
	
	HashMap<String, ArrayList<String>> conceptMap;
	HashMap<String, ArrayList<String>> subClassMap;
	HashMap<String, ArrayList<String>> objectPropertyMap;
	HashMap<String, ArrayList<String>> datatypePropertyMap;

	public TBoxModel(HashMap<String, ArrayList<String>> conceptMap, HashMap<String, ArrayList<String>> subClassMap,
			HashMap<String, ArrayList<String>> objectPropertyMap,
			HashMap<String, ArrayList<String>> datatypePropertyMap, HashMap<String, String> prefixMap) {

		this.conceptMap = conceptMap;
		this.subClassMap = subClassMap;
		this.objectPropertyMap = objectPropertyMap;
		this.datatypePropertyMap = datatypePropertyMap;
		this.prefixMap = prefixMap;
	}
	
	public TBoxModel(){
		
		this.conceptMap = new HashMap<>();
		this.subClassMap = new HashMap<>();
		this.objectPropertyMap = new HashMap<>();
		this.datatypePropertyMap = new HashMap<>();
		this.prefixMap = new HashMap<>();
	}

	
	public HashMap<String, String> getPrefixMap() {
		return prefixMap;
	}

	public void setPrefixMap(HashMap<String, String> prefixMap) {
		this.prefixMap = prefixMap;
	}

	public HashMap<String, ArrayList<String>> getConceptMap() {
		return conceptMap;
	}

	public void setConceptMap(HashMap<String, ArrayList<String>> conceptMap) {
		this.conceptMap = conceptMap;
	}

	public HashMap<String, ArrayList<String>> getSubClassMap() {
		return subClassMap;
	}

	public void setSubClassMap(HashMap<String, ArrayList<String>> subClassMap) {
		this.subClassMap = subClassMap;
	}

	public HashMap<String, ArrayList<String>> getObjectPropertyMap() {
		return objectPropertyMap;
	}

	public void setObjectPropertyMap(HashMap<String, ArrayList<String>> objectPropertyMap) {
		this.objectPropertyMap = objectPropertyMap;
	}

	public HashMap<String, ArrayList<String>> getDatatypePropertyMap() {
		return datatypePropertyMap;
	}

	public void setDatatypePropertyMap(HashMap<String, ArrayList<String>> datatypePropertyMap) {
		this.datatypePropertyMap = datatypePropertyMap;
	}
	
	
}
