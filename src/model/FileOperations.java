package model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class FileOperations {
	public ArrayList<RDFNode> getAllObjects(String filePath) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(filePath);
		
		String queryString = 
				"SELECT DISTINCT ?s WHERE {" +
				" ?s ?property ?o ."
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		
		ArrayList<RDFNode> nodesList = new ArrayList<>();
		while (results.hasNext()) {
			nodesList.add(results.next().get("s"));
		}
		
		qe.close();
		
		return nodesList;
	}
	
	public LinkedHashMap<Object, Object> getAllProperties(String filePath, Object keyObject) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(filePath);
		
		/*String queryString = 
				"PREFIX  :     <http://extbi.lab.aau.dk/ontology/business/>\r\n" + 
						"PREFIX  rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"PREFIX  afn:  <http://jena.apache.org/ARQ/function#>\r\n" + 
						"SELECT ?v ?extrLabel ?o\r\n" + 
						"WHERE\r\n" + 
						"  { ?v  a                     :Municipality ;\r\n" + 
						"        ?p                    ?o\r\n" + 
						"    BIND(afn:localname(?p) AS ?extrLabel)\r\n" + 
						"  }";*/
		// System.out.println(keyObject);
		
		String queryString = 
				"SELECT ?p ?o WHERE {?s ?p ?o. "
				+ "FILTER regex(str(?s), '"+ keyObject.toString() +"')}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		
		// ResultSetFormatter.out( results );
		
		LinkedHashMap<Object, Object> hashMap = new LinkedHashMap<>();
		
		/*while (results.hasNext()) {
			QuerySolution querySolution = (QuerySolution) results.next();
			RDFNode statement = querySolution.get("v");
			
			if (statement.toString().equals(keyObject.toString())) {
				RDFNode property = querySolution.get("extrLabel");
				RDFNode value = querySolution.get("o");
				
				hashMap.put(property, value);
			}
		}*/
		
		/*for(Entry<Object, Object> m:hashMap.entrySet()){  
			System.out.println(m.getKey()+" "+m.getValue());
		}*/
		
		while (results.hasNext()) {
			QuerySolution querySolution = (QuerySolution) results.next();
			
			RDFNode property = querySolution.get("p");
			RDFNode value = querySolution.get("o");
			
			String propertyString = property.toString();
			propertyString = new StringBuilder(propertyString).reverse().toString();
			
			String regEx = "(.*?)/(.*?)$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(propertyString);
			
			while (matcher.find()) {
				propertyString = matcher.group(1);
				propertyString = new StringBuilder(propertyString).reverse().toString();
				
				hashMap.put(propertyString, value);
			}
			
			// System.out.println(property + " " + value);
		}
		
		/*for(Entry<Object, Object> m:hashMap.entrySet()){  
			System.out.println(m.getKey()+" "+m.getValue());
		}*/
		
		qe.close();
		
		return hashMap;
	}

	public void addProperty(Object propertyValue, Object resource, String filePath) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(filePath);
		
		String queryString = 
				"SELECT ?p ?o WHERE {?s ?p ?o. "
				+ "FILTER regex(str(?s), '"+ resource.toString() +"')}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		
		String propertyString = null;
		
		while (results.hasNext()) {
			QuerySolution querySolution = (QuerySolution) results.next();
			
			RDFNode property = querySolution.get("p");
			
			propertyString = property.toString();
			propertyString = new StringBuilder(propertyString).reverse().toString();
			
			String regEx = "(.*?)/(.*?)$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(propertyString);
			
			while (matcher.find()) {
				propertyString = matcher.group(2);
				propertyString = new StringBuilder(propertyString).reverse().toString();
			}
			
			// System.out.println(property + " " + value);
		}
		
		// System.out.println(propertyString);
		
		Resource r = model.getResource(resource.toString());
		
		if (model.contains(r, null, (RDFNode) null )) {
			Property property = model.createProperty( propertyString + "/owl:sameAs");
			r.addProperty(property, "dbpedia:"+propertyValue);
			/*// model.write(System.out);
			System.out.println(r);
			System.out.println(property);
			
			queryString = 
					"SELECT ?p ?o WHERE {?s ?p ?o. "
					+ "FILTER regex(str(?s), '"+ r.toString() +"')}";
			
			query = QueryFactory.create(queryString);
			qe = QueryExecutionFactory.create(query, model);
			results = ResultSetFactory.copyResults(qe.execSelect());
			
			ResultSetFormatter.out( results );*/
			
			try {
				FileOutputStream stream = new FileOutputStream("matched.rdf");
				model.write(stream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Something wrong");
		}
		
		qe.close();
	}
}
