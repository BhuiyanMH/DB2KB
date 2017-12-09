package functions;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import functions.controller.AllFileOperations;
import model.Pair;

public class Independence_2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		dbpediaResourceRetriever("Albertslund", 5, "dbpediaData.json", "dbpediaResources.txt");
		sparqlEndResourceRetriever("Copenhagen", 5, "sparqlEndResources.txt");
		localKBResourceRetriever("municipality.ttl", 5, "localKBResources.txt");

		// localKBPropertyWeightGenerator("http://extbi.lab.aau.dk/resource/business/Municipality/165", "municipality.ttl", "localKBPropertyWeight.txt");
		localKBPropertyWeightGenerator("name, area, region", "municipality.ttl", "local.txt");
		
		localKBsemanticBagGenerator("http://extbi.lab.aau.dk/resource/business/Municipality/165", "municipality.ttl", "localKBsemanticBagGenerator.txt");
		dbpediaSemanticBagGenerator("dbpediaData.json", "http://dbpedia.org/resource/Albertslund", "dbpediaSemanticBagGenerator.txt");
		dbpediaSemanticBagGenerator("dbpediaData.json", "dbpediaSemanticBagGenerator.txt");
		matchSemanticBag(0.3, "localKBsemanticBagGenerator.txt", "dbpediaSemanticBagGenerator.txt", "localKBPropertyWeight.txt");
	}

	private static void matchSemanticBag(double threshold, String localSemanticFile, String externalSemanticFile, String localWeightFile) {
		// TODO Auto-generated method stub
		AllFileOperations allFileOperations = new AllFileOperations();
		ArrayList<Object> internalSemanticBag = allFileOperations.
				readFromBinaryFile(localSemanticFile);
		ArrayList<Object> externalSemanticBag = allFileOperations.
				readFromBinaryFile(externalSemanticFile);
		ArrayList<Object> weightList = allFileOperations.
				readFromBinaryFile(localWeightFile);
		
		Object object = externalSemanticBag.get(0);
		if (object instanceof Single) {
			matchSingle(externalSemanticBag, internalSemanticBag, weightList, threshold);
		} else if (object instanceof ArrayList) {
			matchAll(externalSemanticBag, internalSemanticBag, weightList, threshold);
		}
	}

	private static void matchAll(ArrayList<Object> externalSemanticBag, ArrayList<Object> internalSemanticBag,
			ArrayList<Object> weightList, double threshold) {
		// TODO Auto-generated method stub
		for (int i = 0; i < externalSemanticBag.size(); i++) {
			float totalWeight = 0, matchWeight = 0;
			ArrayList<String> matchList = new ArrayList<>();
			
			Object object = externalSemanticBag.get(i);
			if (object instanceof ArrayList) {
				for (int j = 0; j < ((ArrayList) object).size(); j++) {
					String externalValue = (String) ((ArrayList) object).get(j);
					externalValue = externalValue.trim().toLowerCase();
					for (int k = 0; k < internalSemanticBag.size(); k++) {
						Object newObject = internalSemanticBag.get(k);
						if (newObject instanceof Single) {
							Single single2 = (Single) newObject;
							String internalValue = single2.getString();
							internalValue = internalValue.trim().toLowerCase();
							
							Pattern pattern = Pattern.compile("\\b" + internalValue + "\\b");
							Matcher matcher = pattern.matcher(externalValue);
							while (matcher.find()) {
								if (!matchList.contains(internalValue)) {
									matchList.add(internalValue);
								}
							}
						}
					}
				}
			}
			
			for (int i1 = 0; i1 < weightList.size(); i1++) {
				Weight weight = (Weight) weightList.get(i1);
				totalWeight = totalWeight + weight.getWeight();
				
				String object1 = weight.getObject();
				
				String valueString = object1.toString();
				valueString = new StringBuilder(valueString).reverse().toString();

				String regEx = "(.*?)/(.*?)$";
				Pattern pattern = Pattern.compile(regEx);
				Matcher matcher = pattern.matcher(valueString);

				while (matcher.find()) {
					valueString = matcher.group(1);
				}
				valueString = new StringBuilder(valueString).reverse().toString().trim().toLowerCase();
				
				if (matchList.contains(valueString)) {
					matchWeight = matchWeight + weight.getWeight();
				}
			}
			
			float matchResult = (matchWeight * 100) / totalWeight;
			if (matchResult >= threshold) {
				System.out.println("Matched");
			} else {
				System.out.println("Not matched");
			}
		}
	}

	private static void matchSingle(ArrayList<Object> externalSemanticBag,
			ArrayList<Object> internalSemanticBag, ArrayList<Object> weightList, double threshold) {
		// TODO Auto-generated method stub
		float totalWeight = 0, matchWeight = 0;
		ArrayList<String> matchList = new ArrayList<>();
		for (int i = 0; i < externalSemanticBag.size(); i++) {
			Object object = externalSemanticBag.get(i);
			if (object instanceof Single) {
				Single single = (Single) object;
				String externalValue = single.getString();
				externalValue = externalValue.trim().toLowerCase();
				for (int j = 0; j < internalSemanticBag.size(); j++) {
					Object newObject = internalSemanticBag.get(j);
					if (newObject instanceof Single) {
						Single single2 = (Single) newObject;
						String internalValue = single2.getString();
						internalValue = internalValue.trim().toLowerCase();
						
						Pattern pattern = Pattern.compile("\\b" + internalValue + "\\b");
						Matcher matcher = pattern.matcher(externalValue);
						while (matcher.find()) {
							if (!matchList.contains(internalValue)) {
								matchList.add(internalValue);
							}
						}
					}
				}
			}
		}
		
		for (int i = 0; i < weightList.size(); i++) {
			Weight weight = (Weight) weightList.get(i);
			totalWeight = totalWeight + weight.getWeight();
			
			String object = weight.getObject();
			
			String valueString = object.toString();
			valueString = new StringBuilder(valueString).reverse().toString();

			String regEx = "(.*?)/(.*?)$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(valueString);

			while (matcher.find()) {
				valueString = matcher.group(1);
			}
			valueString = new StringBuilder(valueString).reverse().toString().trim().toLowerCase();
			
			if (matchList.contains(valueString)) {
				matchWeight = matchWeight + weight.getWeight();
			}
		}
		
		float matchResult = (matchWeight * 100) / totalWeight;
		if (matchResult >= threshold) {
			System.out.println("Matched");
		} else {
			System.out.println("Not matched");
		}
	}

	private static void dbpediaSemanticBagGenerator(String filename, String dumpfile) {
		// TODO Auto-generated method stub

		AllFileOperations operations = new AllFileOperations();
		operations.deleteFile(dumpfile);
		
		ArrayList<Object> list = new ArrayList<>(); 
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject object = (JSONObject) parser.parse(new FileReader(filename));
			ArrayList<Object> keyList = new ArrayList<>(object.keySet());

			for (int i = 0; i < keyList.size(); i++) {
				JSONArray jsonArray = (JSONArray) object.get(keyList.get(i));

				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(j);
					ArrayList<String> arrayList = new ArrayList<>();
					arrayList = getObjects(jsonObject, arrayList);
					list.add(arrayList);
				}
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < list.size(); i++) {
			AllFileOperations allFileOperations = new AllFileOperations();
			allFileOperations.writeToBinary(dumpfile, list.get(i), true);
		}
		
		System.out.println("Works have been saved to " + dumpfile);
	}

	private static void dbpediaSemanticBagGenerator(String filename, String resource, String dumpfile) {
		// TODO Auto-generated method stub

		AllFileOperations operations = new AllFileOperations();
		operations.deleteFile(dumpfile);
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(new FileReader(filename));
			ArrayList<Object> keyList = new ArrayList<>(object.keySet());

			for (int i = 0; i < keyList.size(); i++) {
				JSONArray jsonArray = (JSONArray) object.get(keyList.get(i));

				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(j);
					Object value = jsonObject.get("uri");
					
					if (value.equals(resource)) {
						ArrayList<String> arrayList = new ArrayList<>();
						arrayList = getObjects(jsonObject, arrayList);
						
						for (int k = 0; k < arrayList.size(); k++) {
							String valueString = arrayList.get(k);
							Single single = new Single();
							single.setString(valueString);
							AllFileOperations allFileOperations = new AllFileOperations();
							allFileOperations.writeToBinary(dumpfile, single, true);
						}
					}
				}
			}
			
			System.out.println("Works have been saved to " + dumpfile);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error occured");
		}
	}

	private static ArrayList<String> getObjects(JSONObject jsonObject, ArrayList<String> arrayList) {
		// TODO Auto-generated method stub
		ArrayList<Object> keyList = new ArrayList<>(jsonObject.keySet());
		for (int i = 0; i < keyList.size(); i++) {
			Object value = jsonObject.get(keyList.get(i));
			
			if ((value instanceof Long) || (value instanceof String)) {
				if (!arrayList.contains(value.toString())) {
					arrayList.add(value.toString());
				}
			} else if (value instanceof Object) {
				getObjects((JSONArray) value, arrayList);
			}
		}
		return arrayList;
	}

	private static ArrayList<String> getObjects(JSONArray value, ArrayList<String> arrayList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < value.size(); i++) {
			JSONObject object = (JSONObject) value.get(i);
			arrayList = getObjects(object, arrayList);
		}
		return arrayList;
	}

	private static void localKBsemanticBagGenerator(String resource, String filename, String dumpfile) {
		// TODO Auto-generated method stub

		AllFileOperations operations = new AllFileOperations();
		operations.deleteFile(dumpfile);

		Model model = ModelFactory.createDefaultModel();
		model.read(filename);

		String queryString = "SELECT ?o WHERE {?s ?p ?o. " + "FILTER regex(str(?s), '" + resource + "')}";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		qe.close();
		
		ArrayList<String> arrayList = new ArrayList<>();

		while (results.hasNext()) {
			QuerySolution querySolution = (QuerySolution) results.next();

			RDFNode value = querySolution.get("o");

			String valueString = value.toString();
			valueString = new StringBuilder(valueString).reverse().toString();

			String regEx = "(.*?)/(.*?)$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(valueString);

			while (matcher.find()) {
				valueString = matcher.group(1);
			}
			valueString = new StringBuilder(valueString).reverse().toString();
			
			if (!arrayList.contains(valueString)) {
				arrayList.add(valueString);
			}
			
			for (int i = 0; i < arrayList.size(); i++) {
				Single single = new Single();
				single.setString(valueString);
				AllFileOperations allFileOperations = new AllFileOperations();
				allFileOperations.writeToBinary(dumpfile, single, true);
			}
		}
		
		System.out.println("Works have been saved to " + dumpfile);
	}

	private static void localKBPropertyWeightGenerator(String resource, String filename, String dumpfile) {
		// TODO Auto-generated method stub

		AllFileOperations operations = new AllFileOperations();
		operations.deleteFile(dumpfile);

		Model model = ModelFactory.createDefaultModel();
		model.read(filename);

		String queryString = "SELECT ?p ?o WHERE {?s ?p ?o. " + "FILTER regex(str(?s), '" + resource + "')}";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		qe.close();

		while (results.hasNext()) {
			QuerySolution querySolution = (QuerySolution) results.next();

			RDFNode property = querySolution.get("p");
			RDFNode value = querySolution.get("o");

			int total = getTotal(property.toString(), filename);
			int unique = getUnique(property.toString(), filename);

			float uniqueness = (float) unique / (float) total;
			float density = (float) total / (float) model.size();
			float keyness = (2 * uniqueness * density) / (uniqueness + density);

			System.out.println(property);

			System.out.println("Total uniqueness: (Total unique occurance / Occurance of the property)" + " " + unique
					+ " / " + total);
			System.out.println(
					"Total density: (Occurance of the property / Total Triples)" + " " + total + " / " + model.size());
			System.out.printf("Keyness: (2*uniqueness*density / uniqueness + density) %f\n\n", keyness);

			Weight weight = new Weight(value.toString(), keyness);

			AllFileOperations allFileOperations = new AllFileOperations();
			allFileOperations.writeToBinary(dumpfile, weight, true);
		}
		
		System.out.println("Works have been saved to " + dumpfile);
	}

	private static int getUnique(String property, String filename) {
		// TODO Auto-generated method stub
		int unique = 0;
		Model model = ModelFactory.createDefaultModel();
		model.read(filename);

		String string = "SELECT DISTINCT ?o WHERE {?s ?p ?o. " + "FILTER regex(str(?p), '" + property + "')}";
		Query query = QueryFactory.create(string);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = ResultSetFactory.copyResults(qe.execSelect());
		qe.close();

		while (resultSet.hasNext()) {
			unique++;
			resultSet.next();
		}

		return unique;
	}

	private static int getTotal(String property, String filename) {
		// TODO Auto-generated method stub
		int total = 0;

		Model model = ModelFactory.createDefaultModel();
		model.read(filename);

		String string = "SELECT ?p ?o WHERE {?s ?p ?o. " + "FILTER regex(str(?p), '" + property + "')}";
		Query query = QueryFactory.create(string);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = ResultSetFactory.copyResults(qe.execSelect());

		while (resultSet.hasNext()) {
			total++;
			resultSet.next();
		}

		return total;
	}

	private static void localKBResourceRetriever(String filename, int hits, String dumpfile) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(filename);

		String queryString = "SELECT DISTINCT ?s WHERE {" + " ?s ?property ?o ." + "} LIMIT " + hits;

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		qe.close();

		AllFileOperations operations = new AllFileOperations();
		operations.deleteFile(dumpfile);

		while (results.hasNext()) {
			Object object = results.next().get("s");
			Single single = new Single();
			single.setString(object.toString());

			AllFileOperations allFileOperations = new AllFileOperations();
			allFileOperations.writeToBinary(dumpfile, single, true);
			System.out.println(object);
		}
		
		System.out.println("Works have been saved to " + dumpfile);
	}

	private static void sparqlEndResourceRetriever(String key, int hits, String dumpfile) {
		// TODO Auto-generated method stub
		ParameterizedSparqlString qs = new ParameterizedSparqlString(
				"" + "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n" + "\n" + "select ?resource where {\n"
						+ "  ?resource rdfs:label ?label\n" + "} LIMIT " + hits);

		Literal london = ResourceFactory.createLangLiteral(key, "en");
		qs.setParam("label", london);

		QueryExecution exec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery());
		ResultSet results = ResultSetFactory.copyResults(exec.execSelect());

		AllFileOperations operations = new AllFileOperations();
		operations.deleteFile(dumpfile);

		while (results.hasNext()) {
			Object object = results.next().get("resource");
			Single single = new Single();
			single.setString(object.toString());

			AllFileOperations allFileOperations = new AllFileOperations();
			allFileOperations.writeToBinary(dumpfile, single, true);
			System.out.println(object);
		}
		
		System.out.println("Works have been saved to " + dumpfile);
	}

	private static void dbpediaResourceRetriever(String key, int hits, String webFileName, String dumpfile) {
		// TODO Auto-generated method stub
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			URL url = new URL("http://lookup.dbpedia.org/api/search/KeywordSearch?QueryClass=&MaxHits=" + hits
					+ "&QueryString=" + key);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(7000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			fw = new FileWriter(webFileName);
			bw = new BufferedWriter(fw);
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AllFileOperations operations = new AllFileOperations();
		operations.deleteFile(dumpfile);

		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(new FileReader(webFileName));
			ArrayList<Object> keyList = new ArrayList<>(object.keySet());

			for (int i = 0; i < keyList.size(); i++) {
				JSONArray jsonArray = (JSONArray) object.get(keyList.get(i));

				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(j);
					Object value = jsonObject.get("uri");
					Single single = new Single();
					single.setString(value.toString());

					AllFileOperations allFileOperations = new AllFileOperations();
					allFileOperations.writeToBinary(dumpfile, single, true);
					System.out.println(value);
				}
			}
			System.out.println("Works have been saved to " + dumpfile);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error occured");
		}
	}
}
