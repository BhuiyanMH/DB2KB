package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

public class Word {

	private static final String FILENAME = "word.json";
	FileWriter fw = null;
	BufferedWriter bw = null;
	
	public boolean wordSearch(String word1, String word2) {
		getData(word1);
		boolean match = matchWord(word2);
		if (match) {
			return true;
		} else {
			getData(word2);
			match = matchWord(word1);
			if (match) {
				return true;
			}
		}
		return false;
	}

	public boolean matchWord(String word) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject) parser.parse(new FileReader("word.json"));
			ArrayList<Object> list = new ArrayList<>(jsonObject.keySet());
			
			for (int i = 0; i < list.size(); i++) {
				Object keyObject = list.get(i);
				JSONObject object = (JSONObject) jsonObject.get(keyObject);
				
				JSONArray jsonArray = (JSONArray) object.get("syn");
				ArrayList<String> itemList = new ArrayList<>();
				
				if (jsonArray != null) {
					for (int j = 0; j < jsonArray.size(); j++) {
						Object object2 = jsonArray.get(j);
						String demo = object2.toString();
						
						if (demo.equals(word)) {
							return true;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void getData(String key) {
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
				System.out.println("Error!");
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