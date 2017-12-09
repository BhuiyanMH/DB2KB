package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.jena.reasoner.rulesys.builtins.Print;
import org.apache.jena.riot.SysRIOT;
import org.apache.jena.sparql.expr.E_StrLength;
import org.apache.jena.sparql.function.library.namespace;
import org.apache.jena.sparql.function.library.substring;
import org.apache.jena.tdb.store.Hash;
import org.apache.jena.tdb.transaction.SysTxnState;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import view.MainFrame;

public class TBoxMethods {

	CommonMethods commonMethods;
	HashMap<String, String> dataTypeMapping = new HashMap<>();

	public TBoxMethods() {

		initializeDatatypeHashmap();
		commonMethods = new CommonMethods();

	}

	public TBoxModel getTBoxModel(String tBoxPath) {

		TBoxModel tBoxModel = new TBoxModel();

		ArrayList<String> triples = getTBoxTriples(tBoxPath);

		ArrayList<String> conceptDefTriples = new ArrayList<>();
		ArrayList<String> objectPropertyDefTriples = new ArrayList<>();
		ArrayList<String> datatypePropertyDefTriples = new ArrayList<>();
		ArrayList<String> prefixTriples = new ArrayList<>();
		
		
		for (String triple : triples) {

			if (triple.contains("owl#Class"))
				conceptDefTriples.add(triple);
			else if (triple.contains("owl#DatatypeProperty"))
				datatypePropertyDefTriples.add(triple);
			else if(triple.contains("owl#ObjectProperty"))
				objectPropertyDefTriples.add(triple);
			else if(triple.contains("@prefix") || triple.contains("@PREFIX") || triple.contains("@Prefix")){
				prefixTriples.add(triple);
			}

		}
		
//		System.out.println("PREFIXES");
//		for(String s: prefixTriples){
//			System.out.println(s);
//		}
		
		//Adding the prefixes
		tBoxModel.setPrefixMap(getPrefixMap(prefixTriples));
		
		for(String triple: conceptDefTriples){
			
			String subjectNode = getNode(triple, 1);
			//String conceptName = getResource(subjectNode);
			
			ArrayList<String> allConceptTriples = getTriples(subjectNode, triples);
			
//			System.out.println("CONCEPT TRIPLES:");
//			for(String s: allConceptTriples){
//				System.out.println(s);
//				
//			}
			
			tBoxModel.getConceptMap().put(subjectNode, allConceptTriples);
			ArrayList<String> subConceptNodes = getSubConcepts(subjectNode, triples);
			
			tBoxModel.getSubClassMap().put(subjectNode, subConceptNodes);
		}
		
		
		//System.out.println("OBJECT PROPERTIES:");
		for(String triple: objectPropertyDefTriples){
			
			String subjectNode = getNode(triple, 1);
			//String propertyName = getResource(subjectNode);
			//String tableName = getTableName(subjectNode);
			
			//System.out.println(subjectNode+"->"+tableName);
			ArrayList<String> allObjectPropertyTriples = getTriples(subjectNode, triples);

			//allPropertyTriples = (ArrayList<String>) allPropertyTriples.stream().distinct().collect(Collectors.toList());
//			for(String s: allObjectPropertyTriples){
//				System.out.println(s); 
//			}
			
			tBoxModel.getObjectPropertyMap().put( subjectNode, allObjectPropertyTriples);
				
		}
		
		//System.out.println("DATATYPE PROPERTIES:");
		for(String triple: datatypePropertyDefTriples){
			
			String subjectNode = getNode(triple, 1);
//			String propertyName = getResource(subjectNode);
//			String tableName = getTableName(subjectNode);
			
			//System.out.println(subjectNode+"->"+tableName);
			
			
			ArrayList<String> allDatatypePropertyTriples = getTriples(subjectNode, triples);

			//allPropertyTriples = (ArrayList<String>) allPropertyTriples.stream().distinct().collect(Collectors.toList());
//			for(String s: allDatatypePropertyTriples){
//				System.out.println(s); 
//			}
			
			tBoxModel.getDatatypePropertyMap().put(subjectNode, allDatatypePropertyTriples);
				
		}
		
		return tBoxModel;
	}
	
	public HashMap<String, String> getPrefixMap(ArrayList<String> prefixTriples){
		
		HashMap<String , String> prefixMap = new HashMap<>();
		
		String regEx = "(@prefix|@Prefix|@PREFIX)\\s(.*?:)\\s<(.*?)>(\\s*?).";
		Pattern pattern = Pattern.compile(regEx);
		for(String triple: prefixTriples){
			Matcher matcher = pattern.matcher(triple); 
			
			while (matcher.find()) {
				String pre = matcher.group(2);
				String iri = matcher.group(3);
				
				prefixMap.put(pre, iri);
				
				//System.out.println("Pre: " + pre + " IRI: "+ iri);
				break;
			}
			
		}
		
		return prefixMap;
	}
	private ArrayList<String> getSubConcepts(String objectNode, ArrayList<String> allTriples){
		
		ArrayList<String> subConcepts = new ArrayList<>();
		
		String regEx = "(<.*?>)(\\s*?)<(.*?)owl#subClassOf>(\\s*?)"+objectNode+"(\\s*?).";
		
		Pattern pattern = Pattern.compile(regEx);
		for(String triple: allTriples){
			Matcher matcher = pattern.matcher(triple);
			
			while (matcher.find()) {
				subConcepts.add(matcher.group(1));
			}
			
		}
		
		return subConcepts;
		
	}
	
	public String getNode(String triple, int group){
		
		String s = "";
		//String regEx = "<(.*?)>\\s<(.*?)>\\s<(.*?)>(\\s*?).";
		String regEx = "(<.*?>)\\s(<.*?>)\\s(<.*?>)(\\s*?).";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(triple);

		while (matcher.find()) {
			s = matcher.group(group);
			return s;
		}
		return s;
		
	}
	
	private ArrayList<String> getTriples(String subjectNode, ArrayList<String> allTriples){
		
		ArrayList<String> triples = new ArrayList<>();
		String regEx = subjectNode+"(\\s*?)<(.*?)>(\\s*?)<(.*?)>(\\s*?).";
		
		Pattern pattern = Pattern.compile(regEx);
		for(String triple: allTriples){
			Matcher matcher = pattern.matcher(triple);
			
			while (matcher.find()) {
				triples.add(matcher.group(0));
			}
			
		}
		
//		for(String triple: allTriples){
//			if(triple.contains(regEx))
//				triples.add(triple);
//		}
//		
		
		return triples;
	}
	public String getResource(String node){
		
		String[] parts = node.split("/");
		int length = parts.length;
		if(length-1>0){
			
			String temp = parts[(length-1)];
			temp = temp.trim();
			temp = temp.replace(">", "");
			return temp;
		}
		else
			return "";
		
//		String s = "";
//		
//		String regEx = "(.*?)/(.*?)";
//		Pattern pattern = Pattern.compile(regEx);
//		
//		node = new StringBuilder(node).reverse().toString();
//		Matcher matcher = pattern.matcher(node);
//
//		while (matcher.find()) {
//			s = matcher.group(1);
//			s = new StringBuilder(s).reverse().toString();
//			return s;
//		}
//		return s;
		
	}
	
	public String getTableName(String node){
		
		
		String[] parts = node.split("/");
		int length = parts.length;
		if(length-2>0)
			return parts[(length-2)];
		else {
			return "";
		}
		
//		String url = "";
//		String tablename = "";
//		
//		String regEx = "(.*?)/(.)$";
//		Pattern pattern = Pattern.compile(regEx);
//		
//		node = new StringBuilder(node).reverse().toString();
//		System.out.println("Node = "+node);
//		
//		Matcher matcher = pattern.matcher(node);
//
//		
//		while (matcher.find()) {
//			url = matcher.group(2);
//			System.out.println(url);
//			
//		}
//		
//		matcher = pattern.matcher(url);
//		while (matcher.find()) {
//			tablename = matcher.group(1);
//			
//		}
//		tablename = new StringBuilder(tablename).reverse().toString();
//		return tablename;
		
	}

	
	
	public ArrayList<String> getTBoxTriples(String tBoxPath) {

		BufferedReader tBoxReader;
		String row = "";
		ArrayList<String> rows = new ArrayList<String>();

		try {
			tBoxReader = new BufferedReader(new FileReader(tBoxPath));
			try {
				while ((row = tBoxReader.readLine()) != null) {
					rows.add(row);
				}

			} catch (IOException e) {

				JOptionPane.showMessageDialog(null, "File Reading Failed");
			}

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, tBoxPath + " not found!");
		}
		return rows;

	}

	private void initializeDatatypeHashmap() {
		dataTypeMapping.put("character varying", "string");
		dataTypeMapping.put("text", "string");
		dataTypeMapping.put("tsvector", "string");
		dataTypeMapping.put("character", "string");
		dataTypeMapping.put("char", "string");

		dataTypeMapping.put("boolean", "boolean");
		dataTypeMapping.put("bool", "boolean");

		dataTypeMapping.put("integer", "integer");
		dataTypeMapping.put("int", "integer");
		dataTypeMapping.put("int2", "integer");
		dataTypeMapping.put("int4", "integer");
		dataTypeMapping.put("smallint", "integer");
		dataTypeMapping.put("bigint", "integer");
		dataTypeMapping.put("int8", "integer");
		dataTypeMapping.put("smallserial", "integer");
		dataTypeMapping.put("serial", "integer");
		dataTypeMapping.put("bigserial", "integer");
		dataTypeMapping.put("serial8", "integer");
		dataTypeMapping.put("serial2", "integer");
		dataTypeMapping.put("serial4", "integer");

		dataTypeMapping.put("numeric", "decimal");
		dataTypeMapping.put("decimal", "decimal");

		dataTypeMapping.put("real", "double");
		dataTypeMapping.put("double precision", "double");
		dataTypeMapping.put("float8", "double");

		dataTypeMapping.put("float", "float");
		dataTypeMapping.put("float4", "float");

		dataTypeMapping.put("timestamp", "dateTime");
		dataTypeMapping.put("timestamptz", "dateTime");
		dataTypeMapping.put("timestamp without time zone", "dateTime");
		dataTypeMapping.put("timestamp with time zone", "dateTime");

		dataTypeMapping.put("date", "date");

		dataTypeMapping.put("time", "time");
		dataTypeMapping.put("timetz", "time");
		dataTypeMapping.put("time without time zone", "time");
		dataTypeMapping.put("time with time zone", "time");

		dataTypeMapping.put("binary", "hexBinary");
		dataTypeMapping.put("binary varying", "hexBinary");
		dataTypeMapping.put("binary large object", "hexBinary");
		dataTypeMapping.put("bytea", "hexBinary"); // picture

	}

	public String getXSDDatatype(String sqlDatatype) {

		String type = "string";
		if (dataTypeMapping.get(sqlDatatype) != null) {
			type = dataTypeMapping.get(sqlDatatype);
		}
		;
		return type;
	}

	public ArrayList<TableAnnotations> getDefaultTBox(ArrayList<DBTable> tableConfigs, String baseIRI) {

		ArrayList<TableAnnotations> tBox = new ArrayList<>();

		String rdfIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String rdfsIRI = "http://www.w3.org/2000/01/rdf-schema#";
		String owlIRI = "http://www.w3.org/2002/07/owl#";
		String xsdIRI = "http://www.w3.org/2001/XMLSchema#";

		for (DBTable tableConfig : tableConfigs) {

			HashMap<String, ArrayList<String>> conceptMap = new HashMap<>();
			HashMap<String, ArrayList<String>> propertyMap = new HashMap<>();
			String conceptName = tableConfig.getTableName();
			ArrayList<String> conceptAnnotations = new ArrayList<>();
			String annotation = "<" + baseIRI + conceptName + "> " + "<" + rdfsIRI + "type>" + " <" + owlIRI + "Class"
					+ ">.";
			conceptAnnotations.add(annotation);

			annotation = "<" + baseIRI + conceptName + "> " + "<" + rdfsIRI + "label> " + "\"" + conceptName + "\".";
			conceptAnnotations.add(annotation);

			conceptMap.put(conceptName, conceptAnnotations);

			ArrayList<String> primaryKeys = tableConfig.getPrimaryKeys();
			ArrayList<ForeignKey> foreignKeys = tableConfig.getForeignKeys();
			ArrayList<String> dataColumns = tableConfig.getDataColumns();
			HashMap<String, String> columnDatatypes = tableConfig.getColumnDatatypes();

			for (String pk : primaryKeys) {

				ArrayList<String> propertyAnnotations = new ArrayList<>();
				annotation = "<" + baseIRI + conceptName + "> <" + owlIRI + "hasKey> " + "<" + baseIRI + pk + ">.";
				propertyAnnotations.add(annotation);

				annotation = "<" + baseIRI+conceptName+"/" + pk + "> " + "<" + rdfsIRI + "label> " + "\"" + pk + "\".";
				propertyAnnotations.add(annotation);

				annotation = "<" + baseIRI+conceptName+"/" + pk + "> " + "<" + rdfsIRI + "domain> " + "<" + baseIRI + conceptName
						+ ">.";
				propertyAnnotations.add(annotation);

				String datatype = getXSDDatatype(columnDatatypes.get(pk));
				annotation = "<" + baseIRI+conceptName+"/" + pk + "> " + "<" + rdfsIRI + "range> " + "<" + xsdIRI + datatype + ">.";
				propertyAnnotations.add(annotation);

				annotation = "<" + baseIRI +conceptName+"/"+ pk + "> " + "<" + rdfsIRI + "type> " + "<" + owlIRI
						+ "FunctionalProperty>.";
				propertyAnnotations.add(annotation);

				if (isForeignKey(pk, foreignKeys)) {

					annotation = "<" + baseIRI +conceptName+"/"+ pk + "> " + "<" + rdfsIRI + "type> " + "<" + owlIRI
							+ "InverseFunctionalProperty>.";
					propertyAnnotations.add(annotation);
				}

				propertyMap.put(pk, propertyAnnotations);
			}

			for (ForeignKey fk : foreignKeys) {

				ArrayList<String> fkColNames = fk.getColumnNames();
				String parentTable = fk.getTableName();
				ArrayList<String> parentColNames = fk.getTargetTableColumnNames();

				for (int i = 0; i < fkColNames.size(); i++) {

					String fkColName = fkColNames.get(i);

					ArrayList<String> propertyAnnotations = new ArrayList<>();

					if (propertyMap.containsKey(fkColName)) { // this FK is also
																// a PK

						propertyAnnotations = propertyMap.get(fkColName);

						annotation = "<" + baseIRI+conceptName+"/" + fkColName + "> " + "<" + rdfIRI + "type> " + "<" + owlIRI
								+ "ObjectProperty>.";
						propertyAnnotations.add(annotation);

						annotation = "<" + baseIRI +conceptName+"/"+ fkColName + "> " + "<" + rdfsIRI + "domain> " + "<" + baseIRI
								+ conceptName + ">.";
						propertyAnnotations.add(annotation);

						annotation = "<" + baseIRI+conceptName+"/" + fkColName + "> " + "<" + rdfsIRI + "range> " + "<" + baseIRI
								+ parentTable + ">.";
						propertyAnnotations.add(annotation);

						// baseTable owl:subClassOf refTable

						if (isRefTablePK(parentTable, parentColNames.get(i), tableConfigs)) {

							annotation = "<" + baseIRI + conceptName + "> " + "<" + owlIRI + "subClassOf> " + "<"
									+ baseIRI + parentTable + ">.";
							propertyAnnotations.add(annotation);

						}

					} else {

						// rdfs:type owl:ObjectProperty
						// rdfs:domain baseTable
						// rdfs:range refTable

						annotation = "<" + baseIRI +conceptName+"/"+ fkColName + "> " + "<" + rdfsIRI + "label> " + "\"" + fkColName
								+ "\".";
						propertyAnnotations.add(annotation);

						annotation = "<" + baseIRI+conceptName+"/" + fkColName + "> " + "<" + rdfIRI + "type> " + "<" + owlIRI
								+ "ObjectProperty>.";
						propertyAnnotations.add(annotation);

						annotation = "<" + baseIRI +conceptName+"/"+ fkColName + "> " + "<" + rdfsIRI + "domain> " + "<" + baseIRI
								+ conceptName + ">.";
						propertyAnnotations.add(annotation);

						annotation = "<" + baseIRI +conceptName+"/"+ fkColName + "> " + "<" + rdfsIRI + "range> " + "<" + baseIRI
								+ parentTable + ">.";
						propertyAnnotations.add(annotation);

						if (isRefTablePK(parentTable, parentColNames.get(i), tableConfigs)) {

							annotation = "<" + baseIRI + conceptName + "> " + "<" + owlIRI + "subClassOf> " + "<"
									+ baseIRI + parentTable + ">.";
							propertyAnnotations.add(annotation);

						}

						propertyMap.put(fkColName, propertyAnnotations);
					}
				}
			}

			ArrayList<String> dataColumnNames = tableConfig.getDataColumns();
			// System.out.println("Table: "+conceptName+" Num of data Cols: " +
			// dataColumnNames.size());

			for (String dataColName : dataColumnNames) {

				// owl:DatatypeProperty
				// rdfs:domain table
				// rdfs:range datatype
				ArrayList<String> propertyAnnotations = new ArrayList<>();

				annotation = "<" + baseIRI +conceptName+"/"+ dataColName + "> " + "<" + rdfIRI + "type> " + "<" + owlIRI
						+ "DatatypeProperty>.";
				propertyAnnotations.add(annotation);

				annotation = "<" + baseIRI +conceptName+"/"+ dataColName + "> " + "<" + rdfsIRI + "label> " + "\"" + dataColName
						+ "\".";
				propertyAnnotations.add(annotation);

				annotation = "<" + baseIRI +conceptName+"/"+ dataColName + "> " + "<" + rdfsIRI + "domain> " + "<" + baseIRI
						+ conceptName + ">.";
				propertyAnnotations.add(annotation);

				String datatype = getXSDDatatype(columnDatatypes.get(dataColName));
				annotation = "<" + baseIRI +conceptName+"/"+ dataColName + "> " + "<" + rdfsIRI + "range> " + "<" + xsdIRI + datatype
						+ ">.";
				propertyAnnotations.add(annotation);

				propertyMap.put(dataColName, propertyAnnotations);

			}

			tBox.add(new TableAnnotations(conceptMap, propertyMap));

		}

		return tBox;
	}

	private boolean isRefTablePK(String tableName, String refColName, ArrayList<DBTable> tableConfigs) {

		for (DBTable tableConfig : tableConfigs) {

			if (tableConfig.getTableName().equals(tableName)) {

				ArrayList<String> Pks = tableConfig.getPrimaryKeys();

				if (Pks.contains(refColName))
					return true;
				else
					return false;

			}
		}

		return false;
	}

	private boolean isForeignKey(String primaryKey, ArrayList<ForeignKey> foreignKeys) {

		for (ForeignKey fk : foreignKeys) {

			ArrayList<String> fkColNames = fk.getColumnNames();
			if (fkColNames.contains(primaryKey))
				return true;
		}

		return false;
	}

	public boolean writeTBox(ArrayList<TableAnnotations> TBox, String baseIRI) {

		try {
			PrintWriter out;
			// Create a file chooser
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setApproveButtonText("Save");
			fileChooser.setDialogTitle("Select directory to save");
			fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);

			String tBoxFilePath = "";

			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File tBoxFile = fileChooser.getSelectedFile();
				tBoxFilePath = tBoxFile.getPath();
				// fileName = csvFile.getName();

				String dateTime = commonMethods.getDateTime();
				String tBoxString = "@prefix base: <"+baseIRI+">.\n";
				tBoxString += "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n";
				tBoxString += "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.\n";
				tBoxString+= "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.\n";
				tBoxString+= "@prefix owl: <http://www.w3.org/2002/07/owl#>.\n";
			
				for (TableAnnotations tableAnnotations : TBox) {

					HashMap<String, ArrayList<String>> conceptMap, propertyMap;

					conceptMap = tableAnnotations.getConceptAnnotations();
					propertyMap = tableAnnotations.getPropertyAnnotations();
					// System.out.println("Number of concept annotations: "+
					// conceptMap.size());
					for (Map.Entry<String, ArrayList<String>> mapEntry : conceptMap.entrySet()) {

						ArrayList<String> annotations = mapEntry.getValue();
						for (String ann : annotations) {
							tBoxString += ann + "\n";
						}
					}

					for (Map.Entry<String, ArrayList<String>> mapEntry : propertyMap.entrySet()) {
						ArrayList<String> annotations = mapEntry.getValue();

						for (String ann : annotations) {
							tBoxString += ann + "\n";
						}
					}
				}

				out = new PrintWriter(tBoxFilePath + "/" + MainFrame.dbURL + "_TBox_" + dateTime + ".n3");
				out.println(tBoxString);
				out.close();

				return true;
			}

			return false;

		} catch (FileNotFoundException error) {

			JOptionPane.showMessageDialog(null, "File Saving Failed");

			return false;
		}

	}

	public void printTableAnnotations(ArrayList<TableAnnotations> TBox) {

		for (TableAnnotations tableAnnotations : TBox) {

			HashMap<String, ArrayList<String>> conceptMap, propertyMap;

			conceptMap = tableAnnotations.getConceptAnnotations();
			propertyMap = tableAnnotations.getPropertyAnnotations();

			for (Map.Entry<String, ArrayList<String>> mapEntry : conceptMap.entrySet()) {

				System.out.println("Concept: " + mapEntry.getKey() + "\nAnnotations:");
				ArrayList<String> annotations = mapEntry.getValue();

				for (String ann : annotations) {
					System.out.println(ann);
				}
			}

			for (Map.Entry<String, ArrayList<String>> mapEntry : propertyMap.entrySet()) {

				System.out.println("Property: " + mapEntry.getKey() + "\nAnnotations:");
				ArrayList<String> annotations = mapEntry.getValue();

				for (String ann : annotations) {
					System.out.println(ann);
				}
			}

		}

	}

}
