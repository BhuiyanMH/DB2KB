package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import controller.OperationController;

public class ParseController {
	OperationController controller = new OperationController();

	public LinkedHashMap<Object, Object> parseData(String webFileName) {
		// TODO Auto-generated method stub
		LinkedHashMap<Object, Object> hashMap = new LinkedHashMap<>();
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(webFileName));
			// showIt(jsonObject);
			
			JSONArray jsonArray = (JSONArray) jsonObject.get("results");
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject object = (JSONObject) jsonArray.get(i);
				
				ArrayList<Pair> list = getAllKeyValue(object);
				
				// viewValues(list);
				
				String key = "DBpedia Object " + (i+1);
				hashMap.put(key, list);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			controller.errorMessage("Problem in parsing JSON file.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			controller.errorMessage("Problem in parsing JSON file.");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			controller.errorMessage("Problem in parsing JSON file.");
		}
		return hashMap;
	}
	
	private ArrayList<Pair> getAllKeyValue(JSONObject object) {
		// TODO Auto-generated method stub
		ArrayList<Pair> list = new ArrayList<>();
		// showIt(object);
		
		ArrayList<Object> keyList = new ArrayList<>(object.keySet());
		for (int i = 0; i < keyList.size(); i++) {
			Object key = keyList.get(i);
			Object value = object.get(key);
			
			// showIt(key + " *** " + value);
			
			if ((value instanceof Long) || value instanceof String) {
				Pair pair = new Pair();
				pair.setKey(key);
				pair.setValue(value);
				list.add(pair);
				
				// showIt("\n\nAdded\n\n");
			} else if (value instanceof Object) {
				// showIt(value);
				Object valueObject = getAllKeyValue((JSONArray) value);
				// showIt(valueObject);
				Pair pair = new Pair();
				pair.setKey(key);
				pair.setValue(valueObject);
				list.add(pair);
			}
		}
		
		return list;
	}

	private LinkedHashMap<Object, Object> getAllKeyValue(JSONArray value) {
		// TODO Auto-generated method stub
		LinkedHashMap<Object, Object> hashMap = new LinkedHashMap<>();
		for (int i = 0; i < value.size(); i++) {
			ArrayList<Pair> list = new ArrayList<>();
			JSONObject object = (JSONObject) value.get(i);
			list = getAllKeyValue(object);
			hashMap.put(i, list);
		}
		
		return hashMap;
	}

	private void viewValues(ArrayList<Pair> list) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size(); i++) {
			Pair pair = list.get(i);
		    Object key = pair.getKey();
		    Object value = pair.getValue();
		    
		    if (value instanceof ArrayList) {
		    	viewValues((ArrayList<Pair>) value);
			} else if (value instanceof LinkedHashMap) {
				viewValues((LinkedHashMap<Object, Object>) value);
			} else {
				showIt(key + " = " + value);
			}
		}
	}

	private void viewValues(LinkedHashMap<Object, Object> value) {
		// TODO Auto-generated method stub
		for (Entry<Object, Object> entry : value.entrySet()) {
		    Object key = entry.getKey();
		    Object valueObject = entry.getValue();
		    
		    if (valueObject instanceof ArrayList) {
				viewValues((ArrayList<Pair>) valueObject);
			} else {
				controller.errorMessage("Unknown Instances");
				showIt(valueObject);
			}
		}
	}

	private void showIt(Object object) {
		// TODO Auto-generated method stub
		System.out.println(object);
	}

	private void showIt(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(jsonObject.toString())));
	}
}
