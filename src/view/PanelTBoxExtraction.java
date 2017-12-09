package view;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.fasterxml.jackson.core.format.DataFormatMatcher;

import model.TBoxMethods;
import model.CommonMethods;
import model.DBTable;
import model.DatabaseConnection;
import model.DatabaseOperations;
import model.TableAnnotations;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.awt.event.ActionEvent;

public class PanelTBoxExtraction extends JPanel {
	private JTextField textFieldDBName;
	private JTextField textFieldDBUserName;
	private JPasswordField passwordFieldUserPassword;
	private JTextField textFieldBaseIRI;
	private JLabel lblStatusText,lblBaseIri;
	private JButton btnExtreact;
	
	private DatabaseConnection databaseConnection;
	private DatabaseOperations databaseOperations;

	private TBoxMethods tBoxMethods;
	private ArrayList<TableAnnotations> TBox;	
	private ArrayList<DBTable> tableConfigs;
	
	/**
	 * Create the panel.
	 */
	public PanelTBoxExtraction() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelContent = new JPanel();
		add(panelContent, BorderLayout.NORTH);
		panelContent.setLayout(new MigLayout("", "[grow][][grow][]", "[][][grow][grow]"));
		
		JPanel panelInput = new JPanel();
		panelContent.add(panelInput, "flowx,cell 0 0");
		panelInput.setLayout(new MigLayout("", "[95px][90px]", "[26px]"));
		
		JLabel lblExtractFrom = new JLabel("Extract From ");
		panelInput.add(lblExtractFrom, "cell 0 0");
		lblExtractFrom.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JComboBox comboBoxInput = new JComboBox();
		panelInput.add(comboBoxInput, "cell 1 0");
		comboBoxInput.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxInput.setModel(new DefaultComboBoxModel(new String[] {"DBMS"}));
		comboBoxInput.setSelectedIndex(0);
		
		JPanel panelDBParams = new JPanel();
		panelContent.add(panelDBParams, "cell 0 1,growx");
		panelDBParams.setLayout(new MigLayout("", "[]", "[]"));
		
		JLabel lblDbName = new JLabel("DB Name");
		panelDBParams.add(lblDbName, "flowx,cell 0 0");
		lblDbName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		textFieldDBName = new JTextField();
		panelDBParams.add(textFieldDBName, "cell 0 0");
		textFieldDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBName.setColumns(20);
		
		JLabel lblDbUserName = new JLabel("DB User Name");
		panelDBParams.add(lblDbUserName, "cell 0 0");
		lblDbUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		textFieldDBUserName = new JTextField();
		panelDBParams.add(textFieldDBUserName, "cell 0 0");
		textFieldDBUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBUserName.setColumns(20);
		
		JLabel lblDbUserPassword = new JLabel("DB User Password");
		panelDBParams.add(lblDbUserPassword, "cell 0 0");
		lblDbUserPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		passwordFieldUserPassword = new JPasswordField();
		panelDBParams.add(passwordFieldUserPassword, "cell 0 0");
		passwordFieldUserPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordFieldUserPassword.setColumns(20);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String dbName = textFieldDBName.getText().toString();
				String dbUserName = textFieldDBUserName.getText().toString();
				String dbPassword = passwordFieldUserPassword.getText().toString();
				
				Connection connection = databaseConnection.getConnection(dbName, dbUserName, dbPassword);
				
				if(connection == null){
					return;
				}else{
					
					lblBaseIri.setVisible(true);
					btnExtreact.setVisible(true);
					textFieldBaseIRI.setVisible(true);
					
					lblStatusText.setText(dbName+" database connected.");
					
					textFieldDBName.setText("");
					textFieldDBUserName.setText("");
					passwordFieldUserPassword.setText("");
					
				}
				MainFrame.dbURL  = dbName;
				MainFrame.dbUserName = dbUserName;
				MainFrame.dbPassword = dbPassword;
				
				
				databaseOperations = new DatabaseOperations(dbName, dbUserName, dbPassword);
				
				tableConfigs = databaseOperations.getAllDBTableStructure();
				
//				for(DBTable tableConfig: tableConfigs){
//					
//					tableConfig.printTableConfigurations();
//				}
				
				
				
			}
		});
		panelDBParams.add(btnConnect, "cell 0 0");
		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panelDBParams.add(rigidArea, "cell 0 0");
		
		lblBaseIri = new JLabel("Base IRI");
		lblBaseIri.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelDBParams.add(lblBaseIri, "cell 0 0");
		
		textFieldBaseIRI = new JTextField();
		textFieldBaseIRI.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelDBParams.add(textFieldBaseIRI, "cell 0 0");
		textFieldBaseIRI.setColumns(20);
		
		btnExtreact = new JButton("Extract");
		btnExtreact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String baseIRI = textFieldBaseIRI.getText().toString();
				
				if(baseIRI.equals("") || !(baseIRI.endsWith("/"))){
					JOptionPane.showMessageDialog(null, "Please provide a valid Base IRI.\nNote that: Base IRI must end with a \'/\'.");
				}else{
					lblStatusText.setText("TBox extraction in progress.");
					
					TBox = tBoxMethods.getDefaultTBox(tableConfigs, baseIRI);
					lblStatusText.setText("TBox extraction completed.");
					//tBoxMethods.printTableAnnotations(TBox);
					boolean success = tBoxMethods.writeTBox(TBox, baseIRI);
					
					if(success){
						lblStatusText.setText("TBox saved.");	
					}else{
						lblStatusText.setText("TBox saving failed.");
					}
				}
//				CommonMethods commonMethods = new CommonMethods();				
//				System.out.println(MainFrame.dbURL+"_TBox_" +commonMethods.getDateTime()+".owl");
			}

		});
		panelDBParams.add(btnExtreact, "cell 0 0");
		btnExtreact.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JPanel panelStatus = new JPanel();
		panelContent.add(panelStatus, "cell 0 2,grow");
		panelStatus.setLayout(new MigLayout("", "[54px][]", "[20px]"));
		
		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelStatus.add(lblStatus, "cell 0 0");
		
		lblStatusText = new JLabel("Database Not Connected");
		lblStatusText.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelStatus.add(lblStatusText, "cell 1 0");
		
		
		lblBaseIri.setVisible(false);
		textFieldBaseIRI.setVisible(false);
		btnExtreact.setVisible(false);
		
		
		databaseConnection = new DatabaseConnection();
		tBoxMethods = new TBoxMethods();
	}

}
