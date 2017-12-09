package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.CommonMethods;
import model.DatabaseConnection;
import model.DatabaseOperations;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class PanelExtractDB extends JPanel {
	private JTable tableDBContent;
	private JTextField textFieldDBName;
	private JTextField textFieldDBUserName;
	private JPasswordField passwordFieldDBPass;
	private JTextArea textAreaSQLQuery;
	CommonMethods commonMethods;

	/**
	 * Create the panel.
	 */
	public PanelExtractDB() {
		setLayout(new BorderLayout(10, 10));
		
		JPanel panelButtons = new JPanel();
		add(panelButtons, BorderLayout.SOUTH);
		
		JButton btnExtract = new JButton("Extract");
		btnExtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean status = extractDBButtonHandler();
			}
		});
		btnExtract.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnExtract);
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnSave);
		
		JPanel panelTopUI = new JPanel();
		add(panelTopUI, BorderLayout.NORTH);
		panelTopUI.setLayout(new MigLayout("", "[][grow][][grow][][grow]", "[][grow]"));
		
		JLabel lblDbName = new JLabel("DB Name:");
		lblDbName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelTopUI.add(lblDbName, "cell 0 0,alignx right");
		
		textFieldDBName = new JTextField();
		textFieldDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelTopUI.add(textFieldDBName, "cell 1 0,growx");
		textFieldDBName.setColumns(10);
		
		JLabel lblDbUserName = new JLabel("DB User Name:");
		lblDbUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelTopUI.add(lblDbUserName, "cell 2 0,alignx right");
		
		textFieldDBUserName = new JTextField();
		textFieldDBUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelTopUI.add(textFieldDBUserName, "cell 3 0,growx");
		textFieldDBUserName.setColumns(10);
		
		JLabel lblDbPassword = new JLabel("DB Password:");
		lblDbPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelTopUI.add(lblDbPassword, "cell 4 0,alignx right");
		
		passwordFieldDBPass = new JPasswordField();
		passwordFieldDBPass.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordFieldDBPass.setColumns(10);
		panelTopUI.add(passwordFieldDBPass, "cell 5 0,growx");
		
		JLabel lblSqlQuery = new JLabel("SQL Query");
		lblSqlQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelTopUI.add(lblSqlQuery, "cell 0 1,alignx right,aligny top");
		
		textAreaSQLQuery = new JTextArea();
		textAreaSQLQuery.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textAreaSQLQuery.setRows(8);
		panelTopUI.add(textAreaSQLQuery, "cell 1 1,grow, span 5");
		
		JScrollPane scrollPaneContent = new JScrollPane();
		add(scrollPaneContent, BorderLayout.CENTER);
		
		tableDBContent = new JTable();
		tableDBContent.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scrollPaneContent.setViewportView(tableDBContent);
		
		commonMethods  = new CommonMethods();

	}

	private boolean extractDBButtonHandler() {
		
		String dbName = textFieldDBName.getText().toString();
		String uName = textFieldDBUserName.getText().toString();
		String uPass = passwordFieldDBPass.getText().toString();
		String query  = textAreaSQLQuery.getText().toString();
		
		ArrayList<String> temp = new ArrayList<>();
		temp.add(dbName);
		temp.add(uName);
		temp.add(query);
		
		boolean hasNull = commonMethods.hasEmptyString(temp);
		
		if(hasNull){
			commonMethods.showMessage("Please provide all input values");
			return false;
		}
		
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection connection = databaseConnection.getConnection(dbName, uName, uPass);
		
		if(connection == null){
			commonMethods.showMessage("Database connection not established.\nExtraction failed!");
		}else{
			MainFrame.dbURL = dbName;
			MainFrame.dbUserName = uName;
			MainFrame.dbPassword = uPass;
			
		}
		
		DatabaseOperations dbOperations = new DatabaseOperations(dbName, uName, uPass);
		DefaultTableModel tableModel = dbOperations.getQueryTableModel(query);

		if(tableModel==null){
			commonMethods.showMessage("No data found");
		}
		
		tableDBContent.setModel(tableModel);
		tableDBContent.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
		return false;
	}

}
