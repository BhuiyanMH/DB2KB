package model;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthInternalFrameUI;

public class RDFGenerator {

	final String rdfNamespace = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	final String rdfsSchema = "<http://www.w3.org/2000/01/rdf-schema#>";
	final String xmlSchema = "<http://www.w3.org/2001/XMLSchema#>";
	String splitBy = ",";

	public ArrayList<String> generateRDF(String fileName, ArrayList<String> rows) {

		ArrayList<String> rdfTuples = new ArrayList<String>();

		String filePrefix = "<http://myexample.org/data/" + fileName + "#";

		rdfTuples.add("@prefix rdf: " + rdfNamespace + ".");
		rdfTuples.add("@prefix rdfs: " + rdfsSchema + ".");
		rdfTuples.add("@prefix xsd: " + xmlSchema + ".");
		rdfTuples.add("@prefix : " + filePrefix + ">.");
		

		String[] headers = rows.get(0).split(splitBy);

		for (int i = 1; i < rows.size(); i++) {

			String values[] = rows.get(i).split(splitBy);
			String temp = filePrefix + "row=" + i + "> ";

			int numOfCols = headers.length;
			for (int j = 0; j < headers.length; j++) {

				if (j != 0)
					temp += "\t\t\t";

				temp += ":" + headers[j] + " ";

				if (!isNumber(values[j]))
					temp += "\"" + values[j] + "\"";
				else
					temp += values[j];
				if (j == numOfCols - 1)
					temp += ".\n";
				else
					temp += ";\n";

			}

			rdfTuples.add(temp);
		}

		return rdfTuples;
	}

	public boolean isNumber(String s) {
		try {
			double d = Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
