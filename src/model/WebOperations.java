package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import controller.OperationController;

public class WebOperations {
	FileWriter fw = null;
	BufferedWriter bw = null;
	OperationController controller = new OperationController();

	public void retriveData(String webFileName, int hits, String string) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL("http://lookup.dbpedia.org/api/search/KeywordSearch?QueryClass=&MaxHits=" +hits+ "&QueryString="+string);
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
}
