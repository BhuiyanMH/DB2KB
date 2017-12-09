package model;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import org.apache.jena.graph.Node;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import com.github.jsonldjava.core.JsonLdTripleCallback;

import net.miginfocom.swing.MigLayout;

public class ETLExtractionSPARQL implements ETLOperation {
	
	CommonMethods commonMethods;
	private String rdfFilePath, sparqlQuery, fileSavingPath;
	
	public ETLExtractionSPARQL() {
		super();
		commonMethods = new CommonMethods();
	}

	@Override
	public boolean execute(JTextPane textPane) {
		
		try {

			Model tripleModel = ModelFactory.createDefaultModel() ;
			tripleModel.read(rdfFilePath);
			
			Query query = QueryFactory.create(sparqlQuery);
			QueryExecution qe = QueryExecutionFactory.create(query, tripleModel);
			ResultSet resultSet = ResultSetFactory.copyResults(qe.execSelect());

			ArrayList<String> selectedVars = (ArrayList<String>) resultSet.getResultVars();
			ArrayList<String>resultStringList = new ArrayList<>();
			ArrayList<String> resultRDFStringList = new ArrayList<>();
			
			while (resultSet.hasNext()) {

				QuerySolution querySolution = resultSet.next();
				//System.out.println(querySolution.get("s")+" "+querySolution.get("property") +" "+querySolution.get("o"));
				
				String resultString = "";
				String resultRDFString = "";
				for (String selectedVar : selectedVars) {
					
					//System.out.print(querySolution.get(selectedVar)+"\t");
					resultString += querySolution.get(selectedVar).asNode().toString()+"\t";
					
					Node tempNode = querySolution.get(selectedVar).asNode();
					
					if(tempNode.isURI()){
						
						resultRDFString+="<"+tempNode.toString()+"> ";
						
					}else{
						resultRDFString+=tempNode.toString()+" ";
					}

				}
				
				if(selectedVars.size()==3){
					resultRDFString = resultRDFString.trim();
					resultRDFString+=".";
				}
				resultRDFStringList.add(resultRDFString);
				resultStringList.add(resultString);
			
			}
			qe.close();

			String resultString = "";
			int numOfResultString  = resultStringList.size();
			
			for (int i = numOfResultString-1; i>=0; i--) {
				resultString += resultStringList.get(i) + "\n";
			}
		
			int numOfTriples = resultRDFStringList.size();
			
			String tempString ="";
			for(int i = numOfTriples-1; i>=0; i--){
				tempString+=resultRDFStringList.get(i)+"\n";
			}
			
			if(!fileSavingPath.equals("")){
				commonMethods.writeFile(fileSavingPath, tempString);	
			}
			
			textPane.setText(textPane.getText()+"\nExtraction Successful. File Saved: "+ fileSavingPath);
			return true;
		} catch (Exception e) {

			textPane.setText(textPane.getText()+"\nExtraction Failed. Please provide correct input file and query");
			e.printStackTrace();
		}		
		
		return false;
	}

	@Override
	public boolean getInput(JPanel panel, HashMap<String, LinkedHashSet<String>> inputParamsMap) {
		
		JPanel panelExtSPARQLInput = new JPanel();
		panelExtSPARQLInput.setLayout(new MigLayout("", "[][][][grow][][]", "[][grow]"));
		
		JLabel lblRdfFile = new JLabel("RDF File:");
		//lblRdfFile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRdfFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblRdfFile, "cell 1 0,alignx right");
		
		JLabel lblRDFFilePath = new JLabel("None");
		lblRDFFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblRDFFilePath, "cell 3 0");
		
		if(rdfFilePath!=null){
			lblRDFFilePath.setText(rdfFilePath);
		}
		
		JButton btnOpen = new JButton("Open");
		btnOpen.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelExtSPARQLInput.add(btnOpen, "cell 5 0");
		btnOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String filePath = commonMethods.getFilePath("Select RDF File");
				if(!filePath.equals("")){
					lblRDFFilePath.setText(filePath);
				}
				
			}
		});
		
		JLabel lblSparqlQuery = new JLabel("SPARQL Query:");
		lblSparqlQuery.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSparqlQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblSparqlQuery, "cell 1 1");
		
		JTextArea textAreaSPARQLQuery = new JTextArea();
		textAreaSPARQLQuery.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textAreaSPARQLQuery.setColumns(70);
		textAreaSPARQLQuery.setRows(10);
		JScrollPane queryScrollPane = new JScrollPane(textAreaSPARQLQuery);
		panelExtSPARQLInput.add(queryScrollPane, "cell 3 1");
		
		if(sparqlQuery!=null)
			textAreaSPARQLQuery.setText(sparqlQuery);
		
		
		JLabel lblRdfSavingFile = new JLabel("Saving File:");
		//lblRdfFile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRdfSavingFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblRdfSavingFile, "cell 1 2,alignx right");
		
		JLabel lblSavingFilePath = new JLabel("None");
		lblSavingFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblSavingFilePath, "cell 3 2");
		
		if(fileSavingPath!=null){
			lblSavingFilePath.setText(fileSavingPath);
		}
		
		JButton btnOpenSavingFile = new JButton("Open");
		btnOpenSavingFile.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelExtSPARQLInput.add(btnOpenSavingFile, "cell 5 2");
		btnOpenSavingFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String defaultName = "Ext_SPARQL";
				defaultName+="_"+commonMethods.getDateTime()+".n3";

				String filePath = commonMethods.getSaveFile("", defaultName,
						"Select Directory to extracted file");

				if (!filePath.equals("")) {
					lblSavingFilePath.setText(filePath);
				}	
				
			}
		});
		
		
		int option = JOptionPane.showConfirmDialog(null, panelExtSPARQLInput, "Please provide inputs for Extraction from SPARQL", JOptionPane.OK_CANCEL_OPTION);
		
		if(option==JOptionPane.OK_OPTION){
			
			String path = lblRDFFilePath.getText().toString();
			String query = textAreaSPARQLQuery.getText().toString();
			String savingPath = lblSavingFilePath.getText().toString();
			
			ArrayList inputList = new ArrayList<>();
			inputList.add(path);
			inputList.add(query);
			inputList.add(savingPath);
			
			if(!commonMethods.hasEmptyString(inputList)){
			
				if(path.equals("None")){
					commonMethods.showMessage("Please select the RDF file to extract");
					return false;
				}
				
				if(savingPath.equals("None")){
					commonMethods.showMessage("Please select path to save the extracted file");
					return false;
				}
				
				
				setRdfFilePath(path);
				setSparqlQuery(query);
				setFileSavingPath(savingPath);
				
				return true;
			}
			
		}else{
			commonMethods.showMessage("Please provide input for Extraction from SPARQL");
			return false;
		}
		
		return false;
	}

	
	public String getRdfFilePath() {
		return rdfFilePath;
	}

	public void setRdfFilePath(String rdfFilePath) {
		this.rdfFilePath = rdfFilePath;
	}

	public String getSparqlQuery() {
		return sparqlQuery;
	}

	public void setSparqlQuery(String sparqlQuery) {
		this.sparqlQuery = sparqlQuery;
	}

	public String getFileSavingPath() {
		return fileSavingPath;
	}

	public void setFileSavingPath(String fileSavingPath) {
		this.fileSavingPath = fileSavingPath;
	}

	
	
}
