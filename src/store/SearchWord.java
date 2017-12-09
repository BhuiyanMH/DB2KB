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

public class SearchWord {
	String FILENAME = "word.json";
	FileWriter fw = null;
	BufferedWriter bw = null;
	int count = 0;

	public boolean matchWord(Object key, Object newKey) {
		// TODO Auto-generated method stub
		getWord(key.toString().trim().toLowerCase());
		boolean matchResult = false;
		matchResult = matchWord(newKey);
		if (matchResult) {
			return true;
		} else {
			getWord(newKey.toString().trim().toLowerCase());
			matchResult = matchWord(newKey);
		}
		return matchResult;
	}

	private boolean matchWord(Object newKey) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(FILENAME));
			ArrayList<Object> list = new ArrayList<>(jsonObject.keySet());
			for (int i = 0; i < list.size(); i++) {
				Object object = list.get(i);
				Object object2 = jsonObject.get(object);
				checkObject(object2, newKey);
			}
			if (count > 0) {
				return true;
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void checkObject(Object object, Object newKey) {
		// TODO Auto-generated method stub
		try {
			ArrayList<Object> list = new ArrayList<>(((JSONObject) object).keySet());
			for (int i = 0; i < list.size(); i++) {
				Object object1 = list.get(i);
				Object object2 = ((JSONObject) object).get(object1);
				checkObject(object2, newKey);
			}
		} catch (Exception e) {
			// TODO: handle exception
			for (int i = 0; i < ((JSONArray) object).size(); i++) {
				if (newKey.toString().trim().toLowerCase()
						.equals(((JSONArray) object).get(i).toString().trim().toLowerCase())) {
					count++;
				} else {
					continue;
				}
			}
		}
	}

	public void getWord(String key) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL("http://words.bighugelabs.com/api/2/b18b30a523bf06c6c2d295c272486796/" + key + "/json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() == 200) {

				fw = new FileWriter(FILENAME);
				bw = new BufferedWriter(fw);
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

				String output;
				// System.out.println("Output from Server .... \n");
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
			} else {
				System.out.println(conn.getResponseCode() + ". Error in fetching words from Internet.");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
