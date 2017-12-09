package test;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;

public class SPARQLTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ParameterizedSparqlString qs = new ParameterizedSparqlString( "" +
                "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?property ?object WHERE {\n" +
                	"?subject ?property ?object." +
                	"?subject rdfs:label 'Johnny Cage'@en.\n" +
                	"FILTER (lang(?object) = 'en')" +
                "}" );

        Literal london = ResourceFactory.createLangLiteral( "London", "en" );
        qs.setParam( "label", london );

        System.out.println( qs );

        QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

        // Normally you'd just do results = exec.execSelect(), but I want to 
        // use this ResultSet twice, so I'm making a copy of it.  
        ResultSet results = ResultSetFactory.copyResults( exec.execSelect() );

        // A simpler way of printing the results.
        ResultSetFormatter.out( results );
	}

}
