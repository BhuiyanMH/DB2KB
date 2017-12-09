package view;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.stream.thread.ThreadProxyPipe;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import model.DBParams;
import model.DatabaseConnection;
import model.ETLMethods;
import model.Pair;
import model.RMLProcessor;
import net.miginfocom.swing.MigLayout;
import store.Visual;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class PanelETLDump extends JPanel{

	/**
	 * Create the panel.
	 */

	JPanel panelGraph;

	private String etlFilePath = "";
	private JTextPane textPaneStatus;

	private PanelETL etlGraph;
	
	public PanelETLDump() {
		setLayout(new BorderLayout(0, 0));
//		panelGraph = new JPanel();
//		add(panelGraph, BorderLayout.CENTER);
//		panelGraph.setLayout(new BorderLayout(0, 0));
//
//		JButton btnGraph = new JButton("Run");
//		add(btnGraph, BorderLayout.EAST);
//		btnGraph.setFont(new Font("Tahoma", Font.BOLD, 16));
//		btnGraph.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//
//				// drawETLGraph();
//				etlButtonHandler();
//
//			}
//		});
		
		JPanel panelETLStatus = new JPanel();
		add(panelETLStatus, BorderLayout.NORTH);
		panelETLStatus.setLayout(new BorderLayout(0, 0));

//		textPaneStatus = new JTextPane();
//		textPaneStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		textPaneStatus.setText("ETL STATUS:");
//		panelETLStatus.add(textPaneStatus);
		
		
		etlGraph = new PanelETL();
		add(etlGraph, BorderLayout.CENTER);

	}

	protected boolean etlButtonHandler() {

		JPanel panelETL = new JPanel();
		panelETL.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

		JLabel lblDbName = new JLabel("DB Name: ");
		lblDbName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelETL.add(lblDbName, "cell 0 0,alignx trailing");

		JTextField textFieldDBName = new JTextField();
		textFieldDBName.setText("employee");
		textFieldDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelETL.add(textFieldDBName, "cell 1 0");
		textFieldDBName.setColumns(35);

		JLabel lblDbUserName = new JLabel("DB User Name: ");
		lblDbUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelETL.add(lblDbUserName, "cell 0 1,alignx trailing");

		JTextField textFieldUserName = new JTextField();
		textFieldUserName.setText("postgres");
		textFieldUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelETL.add(textFieldUserName, "cell 1 1");
		textFieldUserName.setColumns(35);

		JLabel lblDbPassword = new JLabel("DB Password: ");
		lblDbPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelETL.add(lblDbPassword, "cell 0 2,alignx trailing");

		JPasswordField passwordFieldPassword = new JPasswordField();
		passwordFieldPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordFieldPassword.setColumns(35);
		passwordFieldPassword.setText("13701005");
		panelETL.add(passwordFieldPassword, "cell 1 2");

		JLabel lblBaseIri = new JLabel("Base IRI: ");
		lblBaseIri.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBaseIri.setHorizontalAlignment(SwingConstants.TRAILING);
		panelETL.add(lblBaseIri, "cell 0 3,alignx trailing");

		JTextField textFieldBaseIRI = new JTextField();
		textFieldBaseIRI.setText("www.example.com/");
		textFieldBaseIRI.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelETL.add(textFieldBaseIRI, "cell 1 3");
		textFieldBaseIRI.setColumns(35);

		JLabel lblFilePath = new JLabel("File Path: None");
		lblFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelETL.add(lblFilePath, "cell 1 4,growx");

		JButton btnFileLocation = new JButton("File Location");
		btnFileLocation.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnFileLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				etlFilePath = getETLFileSavingPath();
				if (!etlFilePath.equals("")) {
					lblFilePath.setText("File Path: " + etlFilePath);
				}
			}
		});
		panelETL.add(btnFileLocation, "cell 0 4");

		int confirmation = JOptionPane.showConfirmDialog(null, panelETL, "Provide values for ETL process",
				JOptionPane.OK_CANCEL_OPTION);

		if (confirmation == JOptionPane.OK_OPTION) {

			String dbName = textFieldDBName.getText().toString();
			String dbUserName = textFieldUserName.getText().toString();
			String dbUserPassword = passwordFieldPassword.getText().toString();
			String baseIRI = textFieldBaseIRI.getText().toString();

			if (etlFilePath.equals("")) {
				JOptionPane.showMessageDialog(null, "Please select ETL file path");
				return false;
			}

			DatabaseConnection databaseConnection = new DatabaseConnection();
			Connection dbConnection = databaseConnection.getConnection(dbName, dbUserName, dbUserPassword);
			if (dbConnection == null) {
				JOptionPane.showMessageDialog(null, "ETL Failed!\nDatabase connection not extablished");
				return false;
			}

			// Initialize the connection variables
			MainFrame.dbURL = dbName;
			MainFrame.dbUserName = dbUserName;
			MainFrame.dbPassword = dbUserPassword;

			// generate R2RML mapping graph
			textPaneStatus.setText("ETL STATUS:");
			textPaneStatus.setText(textPaneStatus.getText().toString() + "\nConnected Database: " + dbName);
			ETLMethods etlMethods = new ETLMethods(new DBParams(dbName, dbUserName, dbUserPassword));
			etlMethods.performETL(etlFilePath, baseIRI, textPaneStatus);
			
			return true;
		}

		return false;
	}

	
	public String getETLFileSavingPath() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setApproveButtonText("OK");
		fileChooser.setDialogTitle("Select directory to save file");
		fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);

		String etlFilePath = "";

		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File etlFile = fileChooser.getSelectedFile();
			etlFilePath = etlFile.getPath();
			// fileName = csvFile.getName();
		}

		return etlFilePath;
	}

	
	

}
