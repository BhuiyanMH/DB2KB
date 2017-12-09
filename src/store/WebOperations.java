package store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import controller.OperationController;
import model.Pair;

public class WebOperations {

	public void fetchData(String webFileName, String searchValue, int hits) {
		// TODO Auto-generated method stub
		FileWriter fw = null;
		BufferedWriter bw = null;
		OperationController controller = new OperationController();
		
		try {
			URL url = new URL("http://lookup.dbpedia.org/api/search/KeywordSearch?QueryClass=&MaxHits=" +hits+ "&QueryString="+searchValue);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(7000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			
			fw = new FileWriter(webFileName);
			bw = new BufferedWriter(fw);
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				bw.write(output);
			}
			
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
			conn.disconnect();
			
			controller.showMessage("Data successfully fetched.");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			controller.errorMessage("Data Fetching Failed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			controller.errorMessage("Data Fetching Failed");
		}
	}

	public ArrayList<Pair> parseData(String webFileName) {
		// TODO Auto-generated method stub
		ArrayList<Pair> resultList = new ArrayList<>();
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(new FileReader(webFileName));
			ArrayList<Object> keyList = new ArrayList<>(object.keySet());
			
			for (int i = 0; i < keyList.size(); i++) {
				JSONArray jsonArray = (JSONArray) object.get(keyList.get(i));
				
				for (int j = 0; j < jsonArray.size(); j++) {
					Object key = "DBpedia resource " + (j+1);
					JSONObject jsonObject = (JSONObject) jsonArray.get(j);
					Object value = getAllKeyValue(jsonObject);
					
					Pair pair = new Pair();
					pair.setKey(key);
					pair.setValue(value);
					resultList.add(pair);
				}
			}
			
			// This method is for printing the whole array list. Commenting it for future reference.
			// viewList(resultList);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultList;
	}

	// These method is used for viewing the whole array list
	/*private void viewList(ArrayList<Pair> resultList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < resultList.size(); i++) {
			Pair pair = resultList.get(i);
			Object key = pair.getKey();
			Object value = pair.getValue();
			
			showIt(key);
			viewList(value);
		}
	}

	private void viewList(Object value) {
		// TODO Auto-generated method stub
		if (value instanceof ArrayList) {
			for (int i = 0; i < ((ArrayList) value).size(); i++) {
				Object newValue = ((ArrayList) value).get(i);
				viewList(newValue);
			}
		} else if (value instanceof Pair) {
			Pair pair = (Pair) value;
			Object key = pair.getKey();
			Object newValue = pair.getValue();
			
			showIt(key);
			viewList(newValue);
		} else {
			showIt(value);
		}
	}*/

	private ArrayList<Pair> getAllKeyValue(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		ArrayList<Pair> valueList = new ArrayList<>();
		ArrayList<Object> keyList = new ArrayList<>(jsonObject.keySet());
		
		for (int i = 0; i < keyList.size(); i++) {
			Object key = keyList.get(i);
			Object value = jsonObject.get(key);
			
			if ((value instanceof Long) || (value instanceof String)) {
				Pair pair = new Pair();
				pair.setKey(key);
				pair.setValue(value);
				valueList.add(pair);
			} else if (value instanceof Object) {
				value = getAllKeyValue((JSONArray) value);
				Pair pair = new Pair();
				pair.setKey(key);
				pair.setValue(value);
				valueList.add(pair);
			} else if (value == null) {
				showIt("Null value avoided for " + key);
			} else {
				showIt("Anomaly found during parsing json");
			}
		}
		return valueList;
	}

	private ArrayList<Object> getAllKeyValue(JSONArray value) {
		// TODO Auto-generated method stub
		ArrayList<Object> valueList = new ArrayList<>();
		for (int i = 0; i < value.size(); i++) {
			ArrayList<Pair> list = new ArrayList<>();
			
			JSONObject object = (JSONObject) value.get(i);
			list = getAllKeyValue(object);
			
			valueList.add(list);
		}
		
		return valueList;
	}
	
	private void showIt(Object key) {
		// TODO Auto-generated method stub
		System.out.println(key);
	}

	// This methods are used for just printing.
	/*private void showIt(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(jsonObject.toString())));
	}*/

}
