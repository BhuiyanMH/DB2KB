package functions;

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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import functions.controller.AllFileOperations;

public class Practise {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		semanticBag("localKBResources.txt", "municipality.ttl", "localKBsemanticBagGenerator.txt");
	}

	private static void semanticBag(String resourcefile, String filename, String dumpfile) {
		// TODO Auto-generated method stub
		AllFileOperations allFileOperations = new AllFileOperations();
		ArrayList<Object> resourceList = allFileOperations.readFromBinaryFile(resourcefile);
		
		for (int i = 0; i < resourceList.size(); i++) {
			Single single = (Single) resourceList.get(i);
			String resource = single.getString();
			
			Model model = ModelFactory.createDefaultModel();
			model.read(filename);
			
			String queryString = "SELECT ?o WHERE {?s ?p ?o. " + "FILTER regex(str(?s), '" + resource + "')}";

			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = ResultSetFactory.copyResults(qe.execSelect());
			qe.close();
			
			ArrayList<Object> objectList = new ArrayList<>();
			
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
				
				if (!objectList.contains(valueString)) {
					objectList.add(valueString);
				}
			}
			
			ModelDouble modelDouble = new ModelDouble();
			modelDouble.setObjectOne(resource);
			modelDouble.setObjectTwo(objectList);
			
			AllFileOperations operations = new AllFileOperations();
			operations.writeToBinary(dumpfile, modelDouble, true);
			System.out.println(operations.readFromBinaryFile(dumpfile).size());
		}
		
		
	}
}
