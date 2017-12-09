package store;

import java.util.ArrayList;
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
import org.apache.jena.rdf.model.RDFNode;

import model.Pair;

public class FileOperations {

	public ArrayList<Object> getAllResources(String filePath2) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(filePath2);

		String queryString = "SELECT DISTINCT ?s WHERE {" + " ?s ?property ?o ." + "}";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());

		ArrayList<Object> resourceList = new ArrayList<>();
		while (results.hasNext()) {
			resourceList.add(results.next().get("s"));
		}

		qe.close();
		return resourceList;
	}

	public ArrayList<Pair> getAllProperties(String filePath2, Object keyResource) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(filePath2);

		/*
		 * String queryString =
		 * "PREFIX  :     <http://extbi.lab.aau.dk/ontology/business/>\r\n" +
		 * "PREFIX  rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
		 * "PREFIX  afn:  <http://jena.apache.org/ARQ/function#>\r\n" +
		 * "SELECT ?v ?extrLabel ?o\r\n" + "WHERE\r\n" +
		 * "  {	?v	a	:Municipality ;\r\n" + "	?p	?o\r\n" +
		 * "	BIND(afn:localname(?p) AS ?extrLabel)\r\n" + "}";
		 */

		String queryString = "SELECT ?p ?o WHERE {?s ?p ?o. " + "FILTER regex(str(?s), '" + keyResource.toString()
				+ "')}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
		
		ArrayList<Pair> propertylist = new ArrayList<>();
		
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
				
				Pair pair = new Pair();
				pair.setKey(propertyString);
				pair.setValue(value);
				propertylist.add(pair);
			}
		}
		
		qe.close();
		
		return propertylist;
	}

	public ArrayList<Object> getTargetResources(String filePath2, int hits) {
		// TODO Auto-generated method stub
		Model model = ModelFactory.createDefaultModel();
		model.read(filePath2);

		String queryString = " SELECT DISTINCT ?s"                         +
                " WHERE { ?s ?p ?o . "               +
                " } "         +
                " LIMIT " + hits;

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = ResultSetFactory.copyResults(qe.execSelect());

		ArrayList<Object> resourceList = new ArrayList<>();
		while (results.hasNext()) {
			resourceList.add(results.next().get("s"));
		}

		qe.close();
		return resourceList;
	}
}
