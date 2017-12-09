package view;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.xml.crypto.Data;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import model.DBRDF;
import model.DBTable;
import model.DatabaseConnection;
import model.DatabaseOperations;
import model.DirectMapping;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.FlavorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class PanelDirectRDF extends JPanel {

	private JTextField textFieldDBURL, textFieldDBUserName, passwordFieldDBPassword, textFieldBaseURL;
	private JPanel mainContainer;
	private CardLayout appLayout;

	private JTextArea textAreaDirectRDF;

	private DatabaseOperations databaseOperations;
	private DatabaseConnection databaseConnection;

	private JLabel lblBaseUrl;
	private JButton btnConvert, btnWhatIsBaseURL;

	private String baseURL = "";
	private ArrayList<DBTable> allDBTableStructures;

	private DirectMapping directMapping;
	private ArrayList<DBRDF> directRDFMappings;

	public PanelDirectRDF(JPanel mainContainerPane) {
		setLayout(new BorderLayout(0, 10));

		JPanel panelButtons = new JPanel();
		add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String dbURL = textFieldDBURL.getText();
				String uName = textFieldDBUserName.getText();
				String uPass = passwordFieldDBPassword.getText().toString();
				
				MainFrame.dbURL = dbURL;
				MainFrame.dbUserName = uName;
				MainFrame.dbPassword = uPass;
				
				
				boolean isConnected = connectButtonHandler(dbURL, uName, uPass);

				if (isConnected) {

					textFieldBaseURL.setVisible(true);
					btnConvert.setVisible(true);
					lblBaseUrl.setVisible(true);
					btnWhatIsBaseURL.setVisible(true);

					databaseOperations = new DatabaseOperations(dbURL, uName, uPass);

				} else {

					textFieldBaseURL.setVisible(false);
					btnConvert.setVisible(false);
					lblBaseUrl.setVisible(false);
					btnWhatIsBaseURL.setVisible(false);

					JOptionPane.showMessageDialog(null, "Plase provide Correct Database Connection Properties");
				}
			}

		});
		panelButtons.add(btnConnect);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PrintWriter out;
				try {
					// Create a file chooser
					final JFileChooser fileChooser = new JFileChooser();
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);

					String rmlFilePath = "";

					int returnVal = fileChooser.showOpenDialog(mainContainer);
					if (returnVal == JFileChooser.APPROVE_OPTION) {

						File rmlFile = fileChooser.getSelectedFile();
						fileChooser.setApproveButtonText("Save");
						fileChooser.setDialogTitle("Select directory to save");
						rmlFilePath = rmlFile.getPath();
						// fileName = csvFile.getName();
					}

					out = new PrintWriter(rmlFilePath + "/DB_DIRECT_RDF.rdf");
					out.println(textAreaDirectRDF.getText().toString());
					out.close();

				} catch (FileNotFoundException e) {

					JOptionPane.showMessageDialog(null, "File Saving Failed");
				}

			}
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnSave);

		JPanel panelDBFields = new JPanel();
		add(panelDBFields, BorderLayout.NORTH);
		GridBagLayout gbl_panelDBFields = new GridBagLayout();
		gbl_panelDBFields.columnWidths = new int[] { 0, 0 };
		gbl_panelDBFields.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelDBFields.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		gbl_panelDBFields.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MAX_VALUE };
		panelDBFields.setLayout(gbl_panelDBFields);

		JLabel lblDbUrl = new JLabel("DB Name:");
		lblDbUrl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDbUrl.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblDbUrl = new GridBagConstraints();
		gbc_lblDbUrl.anchor = GridBagConstraints.EAST;
		gbc_lblDbUrl.insets = new Insets(5, 0, 5, 5);
		gbc_lblDbUrl.gridx = 0;
		gbc_lblDbUrl.gridy = 0;
		panelDBFields.add(lblDbUrl, gbc_lblDbUrl);

		textFieldDBURL = new JTextField();
		textFieldDBURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBURL.setColumns(20);
		textFieldDBURL.setToolTipText("Database Name");
		GridBagConstraints gbc_textFieldDBURL = new GridBagConstraints();
		gbc_textFieldDBURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBURL.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldDBURL.gridx = 1;
		gbc_textFieldDBURL.gridy = 0;
		panelDBFields.add(textFieldDBURL, gbc_textFieldDBURL);

		JLabel lblDbName = new JLabel("DB User Name:");
		lblDbName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblDbName = new GridBagConstraints();
		gbc_lblDbName.anchor = GridBagConstraints.EAST;
		gbc_lblDbName.insets = new Insets(5, 0, 5, 5);
		gbc_lblDbName.gridx = 2;
		gbc_lblDbName.gridy = 0;
		panelDBFields.add(lblDbName, gbc_lblDbName);

		textFieldDBUserName = new JTextField();
		textFieldDBUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBUserName.setColumns(20);
		textFieldDBUserName.setToolTipText("Database User Name");
		GridBagConstraints gbc_textFieldDBUserName = new GridBagConstraints();
		gbc_textFieldDBUserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBUserName.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldDBUserName.gridx = 3;
		gbc_textFieldDBUserName.gridy = 0;
		panelDBFields.add(textFieldDBUserName, gbc_textFieldDBUserName);

		JLabel lblDbPassword = new JLabel("DB User Password:");
		lblDbPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblDbPassword = new GridBagConstraints();
		gbc_lblDbPassword.anchor = GridBagConstraints.EAST;
		gbc_lblDbPassword.insets = new Insets(5, 0, 5, 5);
		gbc_lblDbPassword.gridx = 4;
		gbc_lblDbPassword.gridy = 0;
		panelDBFields.add(lblDbPassword, gbc_lblDbPassword);

		passwordFieldDBPassword = new JPasswordField();
		passwordFieldDBPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordFieldDBPassword.setColumns(20);
		passwordFieldDBPassword.setToolTipText("Database User Password");
		GridBagConstraints gbc_passwordFieldDBPassword = new GridBagConstraints();
		gbc_passwordFieldDBPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldDBPassword.insets = new Insets(5, 0, 5, 10);
		gbc_passwordFieldDBPassword.gridx = 5;
		gbc_passwordFieldDBPassword.gridy = 0;
		panelDBFields.add(passwordFieldDBPassword, gbc_passwordFieldDBPassword);

		lblBaseUrl = new JLabel("Base IRI: ");
		lblBaseUrl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblBaseUrl = new GridBagConstraints();
		gbc_lblBaseUrl.anchor = GridBagConstraints.EAST;
		gbc_lblBaseUrl.insets = new Insets(0, 0, 5, 5);
		gbc_lblBaseUrl.gridx = 0;
		gbc_lblBaseUrl.gridy = 2;
		panelDBFields.add(lblBaseUrl, gbc_lblBaseUrl);
		lblBaseUrl.setVisible(false);

		textFieldBaseURL = new JTextField();
		textFieldBaseURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_textFieldBaseURL = new GridBagConstraints();
		gbc_textFieldBaseURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldBaseURL.gridwidth = 3;
		gbc_textFieldBaseURL.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldBaseURL.gridx = 1;
		gbc_textFieldBaseURL.gridy = 2;
		panelDBFields.add(textFieldBaseURL, gbc_textFieldBaseURL);
		textFieldBaseURL.setColumns(10);
		textFieldBaseURL.setVisible(false);

		btnWhatIsBaseURL = new JButton("?");
		btnWhatIsBaseURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnWhatIsBaseURL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane.showMessageDialog(null, "Base URL is the URL for primary keys.");
			}
		});
		GridBagConstraints gbc_buttonWhatIsBaseURL = new GridBagConstraints();
		gbc_buttonWhatIsBaseURL.anchor = GridBagConstraints.WEST;
		gbc_buttonWhatIsBaseURL.insets = new Insets(0, 0, 5, 5);
		gbc_buttonWhatIsBaseURL.gridx = 4;
		gbc_buttonWhatIsBaseURL.gridy = 2;
		panelDBFields.add(btnWhatIsBaseURL, gbc_buttonWhatIsBaseURL);
		btnWhatIsBaseURL.setVisible(false);

		btnConvert = new JButton("Convert");
		btnConvert.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				baseURL = textFieldBaseURL.getText().toString();
				if (baseURL.equals("")) {
					JOptionPane.showMessageDialog(null, "Please provide a base url");
				} else {

					allDBTableStructures = databaseOperations.getAllDBTableStructure();

					// for(DBTable dbTable: allDBTableStructures){
					// dbTable.printTableConfigurations();
					// }

					directMapping = new DirectMapping(databaseOperations);
					directRDFMappings = directMapping.getDirectMapping(baseURL, allDBTableStructures);

				
					String rdfTripleMap = "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n";
					rdfTripleMap+="@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n";
					rdfTripleMap+="@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.\n";
					rdfTripleMap+="@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.\n";
					rdfTripleMap += "@prefix base: <"+textFieldBaseURL.getText().toString()+">.\n";
					
					
					 
//					System.out.println("Calling: " + System.currentTimeMillis());
//					long call = System.currentTimeMillis();
//					long numOfTriple = 0;
					for (DBRDF dbRDF : directRDFMappings) {
						ArrayList<String> rdfTriples = dbRDF.getRdfTriples();
//						numOfTriple+=rdfTriples.size();
						
						for (String triple : rdfTriples) {
							rdfTripleMap += triple + "\n";
						}
					}
//					System.out.println("Returned: " + System.currentTimeMillis());
//					long ret = System.currentTimeMillis();
//					System.out.println("Time Required: " + (ret-call));
//					System.out.println("Triple generated: " + numOfTriple);
					textAreaDirectRDF.setText(rdfTripleMap);

				}

			}
		});
		GridBagConstraints gbc_btnConvert = new GridBagConstraints();
		gbc_btnConvert.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConvert.insets = new Insets(0, 0, 5, 10);
		gbc_btnConvert.gridx = 5;
		gbc_btnConvert.gridy = 2;
		panelDBFields.add(btnConvert, gbc_btnConvert);
		btnConvert.setVisible(false);

		JLabel lblDirectMappingTo = new JLabel("Direct Mapping to RDF of The Database");
		lblDirectMappingTo.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblDirectMappingTo = new GridBagConstraints();
		gbc_lblDirectMappingTo.gridwidth = 6;
		gbc_lblDirectMappingTo.insets = new Insets(10, 0, 5, 5);
		gbc_lblDirectMappingTo.gridx = 0;
		gbc_lblDirectMappingTo.gridy = 5;
		panelDBFields.add(lblDirectMappingTo, gbc_lblDirectMappingTo);

		textAreaDirectRDF = new JTextArea();
		textAreaDirectRDF.setFont(new Font("Monospaced", Font.PLAIN, 18));
		JScrollPane scrollPane = new JScrollPane(textAreaDirectRDF);
		add(scrollPane, BorderLayout.CENTER);
		lblDirectMappingTo.setVisible(false);

		// add(textAreaDirectRDF, BorderLayout.CENTER);

		databaseConnection = new DatabaseConnection();

	}

	private Boolean connectButtonHandler(String dbURL, String userName, String passWord) {

		Connection connection = databaseConnection.getConnection(dbURL, userName, passWord);

		if (connection != null)
			return true;
		else
			return false;
	}
}
