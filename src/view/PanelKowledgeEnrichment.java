package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JList;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.AbstractListModel;

public class PanelKowledgeEnrichment extends JPanel {

	/**
	 * Create the panel.
	 */
	public PanelKowledgeEnrichment() {
		setLayout(new BorderLayout(10, 10));
		
		JPanel panelButtons = new JPanel();
		add(panelButtons, BorderLayout.SOUTH);
		
		JButton btnOpenDb = new JButton("Open DB");
		btnOpenDb.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnOpenDb);
		
		JButton btnOpenRml = new JButton("Open RML");
		btnOpenRml.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnOpenRml);
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnSave);
		
		JPanel panelNorth = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelNorth.getLayout();
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panelNorth, BorderLayout.NORTH);
		
		JLabel lblPrefixes = new JLabel("Prefixes: ");
		lblPrefixes.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelNorth.add(lblPrefixes);
		
		JComboBox comboBoxPrefixes = new JComboBox();
		comboBoxPrefixes.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxPrefixes.setModel(new DefaultComboBoxModel(new String[] {"rr: www.r2rml.com", "owl: www.owl.com"}));
		panelNorth.add(comboBoxPrefixes);
		
		JButton btnPrefix = new JButton("+ Prefix");
		btnPrefix.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelNorth.add(btnPrefix);
		
		JPanel panelContent = new JPanel();
		add(panelContent, BorderLayout.CENTER);
		panelContent.setLayout(new GridLayout(1, 2, 10, 10));
		
		JPanel panelLeft = new JPanel();
		panelLeft.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelContent.add(panelLeft);
		panelLeft.setLayout(new BorderLayout(0, 0));
		
		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelLeft.add(btnSaveChanges, BorderLayout.SOUTH);
		
		JPanel panelLeftContent = new JPanel();
		panelLeft.add(panelLeftContent, BorderLayout.CENTER);
		GridBagLayout gbl_panelLeftContent = new GridBagLayout();
		gbl_panelLeftContent.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelLeftContent.rowHeights = new int[]{0, 0, 0};
		gbl_panelLeftContent.columnWeights = new double[]{1.0, 1.0, 2.0, Double.MIN_VALUE};
		gbl_panelLeftContent.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panelLeftContent.setLayout(gbl_panelLeftContent);
		
		JPanel panelTables = new JPanel();
		GridBagConstraints gbc_panelTables = new GridBagConstraints();
		gbc_panelTables.anchor = GridBagConstraints.WEST;
		gbc_panelTables.gridheight = 2;
		gbc_panelTables.insets = new Insets(0, 5, 5, 5);
		gbc_panelTables.fill = GridBagConstraints.BOTH;
		gbc_panelTables.gridx = 0;
		gbc_panelTables.gridy = 0;
		panelLeftContent.add(panelTables, gbc_panelTables);
		panelTables.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDbTables = new JLabel("DB Tables");
		lblDbTables.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDbTables.setHorizontalAlignment(SwingConstants.CENTER);
		panelTables.add(lblDbTables, BorderLayout.NORTH);
		
		JList listTables = new JList();
		listTables.setFont(new Font("Tahoma", Font.PLAIN, 16));
		listTables.setModel(new AbstractListModel() {
			String[] values = new String[] {"Actors", "Customers", "Flims"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		panelTables.add(listTables, BorderLayout.CENTER);
		
		JPanel panelColumns = new JPanel();
		GridBagConstraints gbc_panelColumns = new GridBagConstraints();
		gbc_panelColumns.anchor = GridBagConstraints.WEST;
		gbc_panelColumns.gridheight = 2;
		gbc_panelColumns.insets = new Insets(0, 0, 5, 5);
		gbc_panelColumns.fill = GridBagConstraints.BOTH;
		gbc_panelColumns.gridx = 1;
		gbc_panelColumns.gridy = 0;
		panelLeftContent.add(panelColumns, gbc_panelColumns);
		panelColumns.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDbColumns = new JLabel("DB Columns");
		lblDbColumns.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblDbColumns.setHorizontalAlignment(SwingConstants.CENTER);
		panelColumns.add(lblDbColumns, BorderLayout.NORTH);
		
		JList listColumns = new JList();
		listColumns.setFont(new Font("Tahoma", Font.PLAIN, 16));
		listColumns.setModel(new AbstractListModel() {
			String[] values = new String[] {"actor_id", "actor_name", "last_modified"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		panelColumns.add(listColumns, BorderLayout.CENTER);
		
		JPanel panelAnnotations = new JPanel();
		GridBagConstraints gbc_panelAnnotations = new GridBagConstraints();
		gbc_panelAnnotations.insets = new Insets(0, 0, 5, 5);
		gbc_panelAnnotations.anchor = GridBagConstraints.WEST;
		gbc_panelAnnotations.gridheight = 2;
		gbc_panelAnnotations.fill = GridBagConstraints.BOTH;
		gbc_panelAnnotations.gridx = 2;
		gbc_panelAnnotations.gridy = 0;
		panelLeftContent.add(panelAnnotations, gbc_panelAnnotations);
		panelAnnotations.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAnnotations = new JLabel("Annotations");
		lblAnnotations.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnnotations.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAnnotations.add(lblAnnotations, BorderLayout.NORTH);
		
		JList listAnnotations = new JList();
		listAnnotations.setModel(new AbstractListModel() {
			String[] values = new String[] {"actor rdf:type owl:Class", "actor owl:sameAs dbpedia:actor"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listAnnotations.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAnnotations.add(listAnnotations, BorderLayout.CENTER);
		
		JPanel panelRight = new JPanel();
		panelRight.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelContent.add(panelRight);
		panelRight.setLayout(new BorderLayout(0, 10));
		
		JScrollPane scrollPane = new JScrollPane();
		panelRight.add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textAreaTBox = new JTextArea();
		textAreaTBox.setText("RDF triples of TBox is shown there ");
		textAreaTBox.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(textAreaTBox);
		
		JLabel lblTboxOfThe = new JLabel("TBox of The DB");
		lblTboxOfThe.setHorizontalAlignment(SwingConstants.CENTER);
		lblTboxOfThe.setFont(new Font("Tahoma", Font.BOLD, 16));
		scrollPane.setColumnHeaderView(lblTboxOfThe);

	}

}
