package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import controller.DBPanelController;

import model.DBTable;

import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class PanelDatabase extends JPanel {

	private JTextField textFieldDBURL;
	private JTextField textFieldDBUserName;
	private JPasswordField passwordFieldDBPassword;
	private JTable tableDBTables;

	private JList<String> listDBTables;

	private ArrayList<String> tableNames;
	private ArrayList<String> tableStatus;

	private CardLayout appLayout;
	private JPanel mainContainer;
	private PanelDBSummary panelDBSummary;

	final static String DB_SUMMERY_PANEL_KEY = "DBPanelSummer";

	private DBPanelController dbPanelController;
	private JTextField textFieldBaseURL;

	private JPanel panelCenterContent;
	private JButton btnOk;
	private ArrayList<DBTable> allDBTableStructures;

	private ArrayList<JTextField> attributeValues;

	private int selectedTableIndex = -1;
	private ArrayList<Boolean> propertyType;
	private String baseURL = "";

	/**
	 * Create the panel.
	 */
	public PanelDatabase(JPanel panelMainContainer) {

		this.mainContainer = panelMainContainer;
		appLayout = (CardLayout) panelMainContainer.getLayout();
		//
		
		allDBTableStructures = new ArrayList<>();

		setLayout(new BorderLayout(0, 5));

		JPanel panelDBButtons = new JPanel();
		add(panelDBButtons, BorderLayout.SOUTH);

		JButton btnDBConnect = new JButton("Connect");
		btnDBConnect.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDBConnect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				String url = textFieldDBURL.getText().toString();
				String userName = textFieldDBUserName.getText().toString();
				String password = passwordFieldDBPassword.getText().toString();

				MainFrame.dbURL = url;
				MainFrame.dbUserName = userName;
				MainFrame.dbPassword = password;

				tableNames = dbPanelController.connectButtonHandler();

				
				if (tableNames != null) {
					loadTableList();
					panelCenterContent.removeAll();
					panelCenterContent.updateUI();
					allDBTableStructures = dbPanelController.getAllDBTableStructures();
				}else{
					JOptionPane.showMessageDialog(null, "Table Names not Found!");
				}

			}
		});
		panelDBButtons.add(btnDBConnect);

		JButton btnConvertDB = new JButton("Proceed >");
		btnConvertDB.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnConvertDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				
				if(allDBTableStructures == null){
					
					JOptionPane.showMessageDialog(null, "Please Connect a database");
					
				}else{
					baseURL = textFieldBaseURL.getText().toString();
					panelDBSummary = new PanelDBSummary(mainContainer, allDBTableStructures, baseURL);
					mainContainer.add(panelDBSummary, DB_SUMMERY_PANEL_KEY);
					appLayout.show(mainContainer, DB_SUMMERY_PANEL_KEY);
					
//					for (DBTable table : allDBTableStructures) {
//
//						table.printTableConfigurations();
//					}
				}

			}
		});
		panelDBButtons.add(btnConvertDB);

		JPanel panelDBCenter = new JPanel();
		panelDBCenter.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panelDBCenter, BorderLayout.CENTER);
		panelDBCenter.setLayout(new BorderLayout(0, 0));

		btnOk = new JButton("Save Changes");
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnOk.setEnabled(false);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				okButtonHandler();
			}

		});
		panelDBCenter.add(btnOk, BorderLayout.SOUTH);

		panelCenterContent = new JPanel();
		// panelDBCenter.add(panelCenterContent, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(panelCenterContent);
		GridBagLayout gbl_panelCenterContent = new GridBagLayout();
		gbl_panelCenterContent.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelCenterContent.rowHeights = new int[] { 0, 0 };
		gbl_panelCenterContent.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelCenterContent.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelCenterContent.setLayout(gbl_panelCenterContent);
		
		panelDBCenter.add(scrollPane, BorderLayout.CENTER);

		JPanel panelDBWest = new JPanel();
		panelDBWest.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panelDBWest, BorderLayout.WEST);
		panelDBWest.setLayout(new BorderLayout(0, 0));

		JLabel lblTablesInThe = new JLabel("Tables in the Database");
		lblTablesInThe.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelDBWest.add(lblTablesInThe, BorderLayout.NORTH);

		listDBTables = new JList<String>();
		listDBTables.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelDBWest.add(listDBTables, BorderLayout.CENTER);
		listDBTables.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// JOptionPane.showMessageDialog(null, "selected "+
				// listDBTables.getSelectedValue());
				// tableDBTables.setModel(dbPanelController.getDBTableTableModel(listDBTables.getSelectedValue().toString()));
				if (textFieldBaseURL.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please provide base url");

				} else {
					baseURL = textFieldBaseURL.getText().toString();
					btnOk.setEnabled(true);
					//JOptionPane.showMessageDialog(null, "Selected Value: " + listDBTables.getSelectedValue());
					//String tName = listDBTables.getSelectedValue().toString();
					if (listDBTables.getSelectedValue() != null)
						loadDataColumns(listDBTables.getSelectedValue().toString());
				}

			}
		});

		JPanel panelDBParamsNorth = new JPanel();
		add(panelDBParamsNorth, BorderLayout.NORTH);
		GridBagLayout gbl_panelDBParamsNorth = new GridBagLayout();
		gbl_panelDBParamsNorth.columnWidths = new int[] { 0, 0 };
		gbl_panelDBParamsNorth.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelDBParamsNorth.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		gbl_panelDBParamsNorth.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MAX_VALUE };
		panelDBParamsNorth.setLayout(gbl_panelDBParamsNorth);

		JLabel lblDbUrl = new JLabel("DB Name:");
		lblDbUrl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDbUrl.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblDbUrl = new GridBagConstraints();
		gbc_lblDbUrl.anchor = GridBagConstraints.EAST;
		gbc_lblDbUrl.insets = new Insets(0, 0, 5, 5);
		gbc_lblDbUrl.gridx = 0;
		gbc_lblDbUrl.gridy = 0;
		panelDBParamsNorth.add(lblDbUrl, gbc_lblDbUrl);

		textFieldDBURL = new JTextField();
		textFieldDBURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBURL.setColumns(20);
		textFieldDBURL.setToolTipText("DB Name");
		GridBagConstraints gbc_textFieldDBURL = new GridBagConstraints();
		gbc_textFieldDBURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBURL.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldDBURL.gridx = 1;
		gbc_textFieldDBURL.gridy = 0;
		panelDBParamsNorth.add(textFieldDBURL, gbc_textFieldDBURL);

		JLabel lblDbName = new JLabel("DB User Name:");
		lblDbName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblDbName = new GridBagConstraints();
		gbc_lblDbName.anchor = GridBagConstraints.EAST;
		gbc_lblDbName.insets = new Insets(0, 0, 5, 5);
		gbc_lblDbName.gridx = 2;
		gbc_lblDbName.gridy = 0;
		panelDBParamsNorth.add(lblDbName, gbc_lblDbName);

		textFieldDBUserName = new JTextField();
		textFieldDBUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBUserName.setColumns(20);
		textFieldDBUserName.setToolTipText("DB User Name");
		GridBagConstraints gbc_textFieldDBUserName = new GridBagConstraints();
		gbc_textFieldDBUserName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBUserName.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldDBUserName.gridx = 3;
		gbc_textFieldDBUserName.gridy = 0;
		panelDBParamsNorth.add(textFieldDBUserName, gbc_textFieldDBUserName);

		JLabel lblDbPassword = new JLabel("DB User Password:");
		lblDbPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblDbPassword = new GridBagConstraints();
		gbc_lblDbPassword.anchor = GridBagConstraints.EAST;
		gbc_lblDbPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblDbPassword.gridx = 4;
		gbc_lblDbPassword.gridy = 0;
		panelDBParamsNorth.add(lblDbPassword, gbc_lblDbPassword);

		passwordFieldDBPassword = new JPasswordField();
		passwordFieldDBPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordFieldDBPassword.setColumns(20);
		passwordFieldDBPassword.setToolTipText("DB User Password");
		GridBagConstraints gbc_passwordFieldDBPassword = new GridBagConstraints();
		gbc_passwordFieldDBPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldDBPassword.insets = new Insets(0, 0, 5, 10);
		gbc_passwordFieldDBPassword.gridx = 5;
		gbc_passwordFieldDBPassword.gridy = 0;
		panelDBParamsNorth.add(passwordFieldDBPassword, gbc_passwordFieldDBPassword);

		JLabel lblBaseUrl = new JLabel("Base IRI: ");
		lblBaseUrl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblBaseUrl = new GridBagConstraints();
		gbc_lblBaseUrl.anchor = GridBagConstraints.EAST;
		gbc_lblBaseUrl.insets = new Insets(0, 0, 5, 5);
		gbc_lblBaseUrl.gridx = 0;
		gbc_lblBaseUrl.gridy = 2;
		panelDBParamsNorth.add(lblBaseUrl, gbc_lblBaseUrl);

		textFieldBaseURL = new JTextField();
		textFieldBaseURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_textFieldBaseURL = new GridBagConstraints();
		gbc_textFieldBaseURL.gridwidth = 3;
		gbc_textFieldBaseURL.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldBaseURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldBaseURL.gridx = 1;
		gbc_textFieldBaseURL.gridy = 2;
		panelDBParamsNorth.add(textFieldBaseURL, gbc_textFieldBaseURL);
		textFieldBaseURL.setColumns(10);

		JButton buttonWhatIsBaseURL = new JButton("?");
		buttonWhatIsBaseURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonWhatIsBaseURL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JOptionPane.showMessageDialog(null, "Base IRI is used to dereference Relative IRIs.");
			}
		});
		GridBagConstraints gbc_buttonWhatIsBaseURL = new GridBagConstraints();
		gbc_buttonWhatIsBaseURL.anchor = GridBagConstraints.WEST;
		gbc_buttonWhatIsBaseURL.insets = new Insets(0, 0, 5, 5);
		gbc_buttonWhatIsBaseURL.gridx = 4;
		gbc_buttonWhatIsBaseURL.gridy = 2;
		panelDBParamsNorth.add(buttonWhatIsBaseURL, gbc_buttonWhatIsBaseURL);

		dbPanelController = new DBPanelController();
		attributeValues = new ArrayList<>();

	}

	private void loadTableList() {

		DefaultListModel<String> tableListModel = new DefaultListModel<>();
		
		for (String tableName : tableNames) {
			tableListModel.addElement(tableName);
		}
		listDBTables.setModel(tableListModel);

	}
	
	private void okButtonHandler() {

		if (textFieldBaseURL.getText().equals("")) {

			JOptionPane.showMessageDialog(null, "Please priovide a base URL");
			return;
		} else {
			baseURL = textFieldBaseURL.getText().toString();
		}

		String tableName = listDBTables.getSelectedValue().toString();
		DBTable temp = getDBTableObject(tableName);
		temp.getDataColumnsIsURL().clear();

		for (Boolean b : propertyType) {
			temp.getDataColumnsIsURL().add(b);
		}

		String s = "";
		for (Boolean dummy : temp.getDataColumnsIsURL()) {
			s += dummy + "\n";
		}

		// saving the column values
		int numOfDataColumns = temp.getDataColumnsIsURL().size();
		for (int i = 0; i < numOfDataColumns; i++) {

			if ((propertyType.get(i))) {

				String url = attributeValues.get(i).getText();

				if (url.equals("")) {
					JOptionPane.showMessageDialog(null, "Please provide value of all URIs");
					return;
				}
			}

		}

		ArrayList<String> dataCols = temp.getDataColumnValues();
		int size = temp.getDataColumns().size();

		for (int loopIndex = 0; loopIndex < size; loopIndex++) {

			if (propertyType.get(loopIndex)) {
				
				dataCols.set(loopIndex, attributeValues.get(loopIndex).getText().toString());

			} else {
				dataCols.set(loopIndex, "L");
			}
		}

		int numOfCols = propertyType.size();
		for (int j = 0; j < numOfCols; j++) {
			s += dataCols.get(j) + "\n";

		}

		//JOptionPane.showMessageDialog(null, listDBTables.getSelectedValue().toString() + "\n" + s);

		int i = listDBTables.getSelectedIndex();
		// tableStatus.remove(i);
		// tableStatus.add(i, "OK");
		//tableStatus.set(i, "OK");
		//loadTableStatusList();

		panelCenterContent.removeAll();
		panelCenterContent.updateUI();

		btnOk.setEnabled(false);

	}

	private DBTable getDBTableObject(String tableName) {

		for (DBTable temp : allDBTableStructures) {
			if (temp.getTableName().equals(tableName)) {

				return temp;
			}
		}
		return null;
	}

	private void loadDataColumns(String tableName) {

		panelCenterContent.removeAll();
		panelCenterContent.updateUI();

		attributeValues = new ArrayList<>();
		propertyType = new ArrayList<>();

		JLabel lblColumnName = new JLabel("DB Column");
		lblColumnName.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblColumnName = new GridBagConstraints();
		gbc_lblColumnName.anchor = GridBagConstraints.NORTH;
		gbc_lblColumnName.insets = new Insets(0, 0, 10, 5);
		gbc_lblColumnName.gridx = 0;
		gbc_lblColumnName.gridy = 0;
		panelCenterContent.add(lblColumnName, gbc_lblColumnName);

		JLabel lblNewLabel = new JLabel("Property");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.insets = new Insets(0, 0, 10, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 0;

		panelCenterContent.add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblPropertyValue = new JLabel("IRI");
		lblPropertyValue.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_lblPropertyValue = new GridBagConstraints();
		gbc_lblPropertyValue.anchor = GridBagConstraints.NORTH;
		gbc_lblPropertyValue.insets = new Insets(0, 0, 10, 5);
		gbc_lblPropertyValue.gridx = 6;
		gbc_lblPropertyValue.gridy = 0;
		panelCenterContent.add(lblPropertyValue, gbc_lblPropertyValue);

		DBTable dbTable = getDBTableObject(tableName);

		// initilizing the property values
		for (Boolean type : dbTable.getDataColumnsIsURL()) {
			propertyType.add(type);
		}

		selectedTableIndex = allDBTableStructures.indexOf(dbTable);

		int i = 0;
		for (String columnName : dbTable.getDataColumns()) {

			final int index = i;

			JLabel lblColumnHeader = new JLabel(columnName);
			lblColumnHeader.setFont(new Font("Tahoma", Font.PLAIN, 16));
			GridBagConstraints gbc_lblColumnHeader = new GridBagConstraints();
			gbc_lblColumnHeader.weightx = 1.0;
			gbc_lblColumnHeader.ipady = 10;
			gbc_lblColumnHeader.ipadx = 2;
			gbc_lblColumnHeader.insets = new Insets(0, 0, 15, 20);
			gbc_lblColumnHeader.gridx = 0;
			gbc_lblColumnHeader.gridy = 1 + i;
			gbc_lblColumnHeader.weighty = 1.0;
			panelCenterContent.add(lblColumnHeader, gbc_lblColumnHeader);

			JRadioButton rdbtnLiteral = new JRadioButton("Literal");
			rdbtnLiteral.setFont(new Font("Tahoma", Font.PLAIN, 16));
			GridBagConstraints gbc_rdbtnLiteral = new GridBagConstraints();
			gbc_rdbtnLiteral.ipady = 10;
			gbc_rdbtnLiteral.ipadx = 2;
			gbc_rdbtnLiteral.insets = new Insets(0, 0, 15, 5);
			gbc_rdbtnLiteral.gridx = 2;
			gbc_rdbtnLiteral.gridy = 1 + i;
			gbc_rdbtnLiteral.weighty = 1.0;
			panelCenterContent.add(rdbtnLiteral, gbc_rdbtnLiteral);

			JRadioButton rdbtnInternalURL = new JRadioButton("Internal IRI");
			rdbtnInternalURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
			GridBagConstraints gbc_rdbtnInternalURL = new GridBagConstraints();
			gbc_rdbtnInternalURL.ipady = 10;
			gbc_rdbtnInternalURL.insets = new Insets(0, 0, 15, 5);
			gbc_rdbtnInternalURL.gridx = 3;
			gbc_rdbtnInternalURL.gridy = 1 + i;
			gbc_rdbtnInternalURL.weighty = 1.0;
			panelCenterContent.add(rdbtnInternalURL, gbc_rdbtnInternalURL);

			JRadioButton rdbtnExternalURL = new JRadioButton("External IRI");
			rdbtnExternalURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
			GridBagConstraints gbc_rdbtnExternalURL = new GridBagConstraints();
			gbc_rdbtnExternalURL.ipady = 10;
			gbc_rdbtnExternalURL.insets = new Insets(0, 0, 15, 20);
			gbc_rdbtnExternalURL.gridx = 4;
			gbc_rdbtnExternalURL.gridy = 1 + i;
			gbc_rdbtnExternalURL.weighty = 1.0;
			panelCenterContent.add(rdbtnExternalURL, gbc_rdbtnExternalURL);

			ButtonGroup radioButtonGroup = new ButtonGroup();
			radioButtonGroup.add(rdbtnLiteral);
			radioButtonGroup.add(rdbtnInternalURL);
			radioButtonGroup.add(rdbtnExternalURL);

			JTextField textField = new JTextField();
			textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.ipady = 10;
			gbc_textField.weightx = 2.0;
			gbc_textField.weighty = 1.0;
			gbc_textField.insets = new Insets(0, 0, 15, 20);
			gbc_textField.gridx = 6;
			gbc_textField.gridy = 1 + i;
			panelCenterContent.add(textField, gbc_textField);
			textField.setColumns(20);

			attributeValues.add(textField);

			rdbtnInternalURL.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					textField.setEnabled(true);
					textField.setText(textFieldBaseURL.getText().toString());
					propertyType.set(index, true);

				}
			});

			rdbtnExternalURL.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					textField.setEnabled(true);
					textField.setText("");
					propertyType.set(index, true);
				}
			});

			rdbtnLiteral.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					textField.setEnabled(false);
					textField.setText("Literal");
					// propertyType.add(index, false);
					// propertyType.remove(index + 1);

					propertyType.set(index, false);

				}
			});

			ArrayList<String> columProperties = dbTable.getDataColumnValues();

			if (propertyType.get(i)) {

				if (dbTable.getDataColumnValues().get(i).equals(baseURL)) {
					rdbtnInternalURL.setSelected(true);
					textField.setText(dbTable.getDataColumnValues().get(i));

					// System.out.println("IsURL: True" );
				} else {
					rdbtnExternalURL.setSelected(true);
					textField.setText(dbTable.getDataColumnValues().get(i));

					// System.out.println("IsURL: True" );
				}
			} else {
				// System.out.println("exec");
				rdbtnLiteral.setSelected(true);
				textField.setText("Literal");
				textField.setEnabled(false);
			}
			// }

			i++;

		}

	}
}
