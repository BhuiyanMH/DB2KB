package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
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
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.omg.CORBA.INTERNAL;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import model.CommonMethods;
import model.RMLProcessor;

public class PanelSparqlQuery extends JPanel {

	/**
	 * Create the panel.
	 */
	private JPanel mainContainer;
	private JTextArea textAreaQueryResult, textAreaSparqlQuery;
	private Model tripleModel;
	private String rmlFilePath = "None";
	private JLabel lblQueryResult;
	private CommonMethods commonMethods;
	
	private ArrayList<String> resultRDFStringList;
	private ArrayList<String> selectedVars, resultStringList;
	
	public PanelSparqlQuery(JPanel panelMainContainer) {
		
		this.mainContainer = panelMainContainer;
		setLayout(new BorderLayout(5, 5));
		
		JPanel panelButton = new JPanel();
		add(panelButton, BorderLayout.SOUTH);
		
		JButton btnOpenRF = new JButton("Open RDF File");
		btnOpenRF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);
				
				rmlFilePath = "";
				//String rmlFilePath = "C:\\Users\\ASUS\\Documents\\DB_RML.rml";
				int returnVal = fileChooser.showOpenDialog(mainContainer);
				if(returnVal == JFileChooser.APPROVE_OPTION){
					
					File rmlFile = fileChooser.getSelectedFile();
					rmlFilePath = rmlFile.getPath();
					//String fileName = rmlFile.getName();
					//System.out.println(rmlFilePath);
					
					
					try {
						
						tripleModel = ModelFactory.createDefaultModel() ;
						tripleModel.read(rmlFilePath) ;
						lblQueryResult.setText(" Query Result for file: "+rmlFilePath);
						
					} catch (Exception e) {
							
						JOptionPane.showMessageDialog(null, "Loading failed!\nError in rdf syntax.");
						e.printStackTrace();
					}
					
				}
			}
		});
		btnOpenRF.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButton.add(btnOpenRF);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				String defaultName ="Extr_RDF_"+commonMethods.getDateTime();
				try {
					if(selectedVars.size()==3){
						defaultName+=".n3";
					}else{
						defaultName+=".txt";
					}
				} catch (Exception e) {
					defaultName+=".txt";
				}
				
				String path = commonMethods.getSaveFile("",defaultName , "Select Directory to save file");
				
				int numOfTriples = resultRDFStringList.size();
				
				String tempString ="";
				for(int i = numOfTriples-1; i>=0; i--){
					tempString+=resultRDFStringList.get(i)+"\n";
				}
				
				if(!path.equals("")){
					commonMethods.writeFile(path, tempString);	
				}
				
			}
		});
		
		JButton btnExtract = new JButton("Extract");
		panelButton.add(btnExtract);
		btnExtract.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnExtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (tripleModel == null) {
					JOptionPane.showMessageDialog(null, "Please load an RDF file!");
					return;
				}

				try {

					String queryString = textAreaSparqlQuery.getText().toString().trim();
					Query query = QueryFactory.create(queryString);
					QueryExecution qe = QueryExecutionFactory.create(query, tripleModel);
					ResultSet resultSet = ResultSetFactory.copyResults(qe.execSelect());

					selectedVars = (ArrayList<String>) resultSet.getResultVars();
					resultStringList = new ArrayList<>();
					resultRDFStringList = new ArrayList<>();
					
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
					textAreaQueryResult.setText(resultString);

				} catch (Exception e) {

					JOptionPane.showMessageDialog(null, "Error in query!");

					e.printStackTrace();
				}
			}	
				
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButton.add(btnSave);
		
		JPanel panelTop = new JPanel();
		add(panelTop, BorderLayout.NORTH);
		GridBagLayout gbl_panelTop = new GridBagLayout();
		//gbl_panelTop.columnWidths = new int[]{0, 0};
		//gbl_panelTop.rowHeights = new int[]{0, 0, 0};
		gbl_panelTop.columnWeights = new double[]{0.0, 1.0};
		//gbl_panelTop.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panelTop.setLayout(gbl_panelTop);
		
		JLabel lblSparqlQuery = new JLabel("SPARQL Query: ");
		lblSparqlQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblSparqlQuery = new GridBagConstraints();
		gbc_lblSparqlQuery.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblSparqlQuery.insets = new Insets(0, 5, 5, 5);
		gbc_lblSparqlQuery.gridx = 0;
		gbc_lblSparqlQuery.gridy = 0;
		panelTop.add(lblSparqlQuery, gbc_lblSparqlQuery);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		panelTop.add(scrollPane, gbc_scrollPane);
		
		textAreaSparqlQuery = new JTextArea();
		textAreaSparqlQuery.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textAreaSparqlQuery.setColumns(40);
		textAreaSparqlQuery.setRows(8);
		scrollPane.setViewportView(textAreaSparqlQuery);
		
		JPanel panelContent = new JPanel();
		add(panelContent, BorderLayout.CENTER);
		panelContent.setLayout(new BorderLayout(0, 0));
		
		lblQueryResult = new JLabel(" Query Result for file: "+rmlFilePath);
		lblQueryResult.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelContent.add(lblQueryResult, BorderLayout.NORTH);
		
		JScrollPane scrollPaneQueryResult = new JScrollPane();
		panelContent.add(scrollPaneQueryResult, BorderLayout.CENTER);
		
		textAreaQueryResult = new JTextArea();
		textAreaQueryResult.setEditable(false);
		textAreaQueryResult.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPaneQueryResult.setViewportView(textAreaQueryResult);
		
		commonMethods = new CommonMethods();
		
	}

}
