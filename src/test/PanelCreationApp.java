package test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JTextPane;

public class PanelCreationApp {

	private JFrame frame;
	private JTextField txtFieldConcept;
	private JTextField textFieldProperty;
	private JTextField textFieldDBName;
	private JTextField textFieldUserName;
	private JPasswordField passwordFieldPassword;
	private JTextField textFieldBaseIRI;
	private JTextField txtTboxname;
	private JTextField textFieldThreshold;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PanelCreationApp window = new PanelCreationApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PanelCreationApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {

			for (LookAndFeelInfo lF : UIManager.getInstalledLookAndFeels()) {
				if (lF.getName().equals("Nimbus")) {
					UIManager.setLookAndFeel(lF.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Look and feel not changed.");
		}

		frame = new JFrame();
		frame.setBounds(100, 100, 966, 765);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelExtSPARQLInput = new JPanel();
		frame.getContentPane().add(panelExtSPARQLInput, BorderLayout.CENTER);
		panelExtSPARQLInput.setLayout(new MigLayout("", "[][][][grow][][]", "[][grow]"));
		
		JLabel lblRdfFile = new JLabel("RDF File:");
		lblRdfFile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRdfFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblRdfFile, "cell 1 0,alignx right");
		
		JLabel lblRDFFilePath = new JLabel("None");
		lblRDFFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblRDFFilePath, "cell 3 0");
		
		JButton btnOpen = new JButton("Open");
		btnOpen.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelExtSPARQLInput.add(btnOpen, "cell 5 0");
		
		JLabel lblSparqlQuery = new JLabel("SPARQL Query:");
		lblSparqlQuery.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSparqlQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelExtSPARQLInput.add(lblSparqlQuery, "cell 1 1");
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textArea.setColumns(70);
		textArea.setRows(10);
		panelExtSPARQLInput.add(textArea, "cell 3 1");

		// JPanel panelAddProperty = new JPanel();
		// frame.getContentPane().add(panelAddProperty, BorderLayout.SOUTH);
		// panelAddProperty.setLayout(new MigLayout("",
		// "[grow][10][grow][10][][10][grow]", "[][]"));
		//
		// JLabel lblBase = new JLabel("Base IRI");
		// lblBase.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelAddProperty.add(lblBase, "cell 0 0");
		//
		// JLabel lblConcept = new JLabel("Concept");
		// lblConcept.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelAddProperty.add(lblConcept, "flowx,cell 2 0,growx");
		//
		// JLabel lblPropertyType = new JLabel("Property Type");
		// lblPropertyType.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelAddProperty.add(lblPropertyType, "cell 6 0");
		//
		// JComboBox comboBoxBase = new JComboBox();
		// comboBoxBase.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelAddProperty.add(comboBoxBase, "cell 0 1,growx");
		//
		// txtFieldConcept = new JTextField();
		// txtFieldConcept.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelAddProperty.add(txtFieldConcept, "flowx,cell 2 1,growx");
		// txtFieldConcept.setColumns(15);
		//
		// JLabel lblProperty = new JLabel("Property");
		// lblProperty.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelAddProperty.add(lblProperty, "cell 4 0");
		//
		// textFieldProperty = new JTextField();
		// textFieldProperty.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelAddProperty.add(textFieldProperty, "cell 4 1");
		// textFieldProperty.setColumns(15);
		//
		// JComboBox comboBoxPropertyType = new JComboBox();
		// comboBoxPropertyType.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// comboBoxPropertyType
		// .setModel(new DefaultComboBoxModel(new String[] { "Datatype
		// Property", "Object Property" }));
		// panelAddProperty.add(comboBoxPropertyType, "cell 6 1");

		// JScrollPane scrollPaneCenterPanel = new JScrollPane();
		// frame.getContentPane().add(scrollPaneCenterPanel,
		// BorderLayout.CENTER);

		// JPanel panelTBoxInputPanel = new JPanel();
		// scrollPaneCenterPanel.setViewportView(panelTBoxInputPanel);
		// panelTBoxInputPanel.setLayout(new MigLayout("", "[][][grow]",
		// "[][]"));
		//
		// JLabel lblTboxName = new JLabel("TBox Name: ");
		// lblTboxName.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelTBoxInputPanel.add(lblTboxName, "cell 0 0");
		//
		// txtTboxname = new JTextField();
		// txtTboxname.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelTBoxInputPanel.add(txtTboxname, "cell 2 0,growx");
		// txtTboxname.setColumns(10);
		//
		// JButton btnDirectory = new JButton("Directory");
		// btnDirectory.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelTBoxInputPanel.add(btnDirectory, "cell 0 1");
		//
		// JLabel lblDirname = new JLabel("None");
		// lblDirname.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelTBoxInputPanel.add(lblDirname, "cell 2 1");
		//
		// JPanel panelComponentPalette = new JPanel();
		// frame.getContentPane().add(panelComponentPalette, BorderLayout.WEST);
		// panelComponentPalette.setLayout(new MigLayout("", "[]", "[][][][]"));
		//
		// ButtonGroup etlButtonGroup = new ButtonGroup();
		//
		// JPanel panelTransformation = new JPanel();
		// panelTransformation.setBorder(
		// new TitledBorder(null, "Transformation", TitledBorder.LEADING,
		// TitledBorder.TOP, null, null));
		// panelComponentPalette.add(panelTransformation, "cell 0 0,grow");
		// panelTransformation.setLayout(new MigLayout("", "[grow][grow]",
		// "[][][][]"));
		//
		//
		// JButton btnMappingGeneration = new JButton("Mapping Generator");
		// panelTransformation.add(btnMappingGeneration, "cell 0 1,growx,
		// span");
		// btnMappingGeneration.setFont(new Font("Tahoma", Font.BOLD, 12));
		// // btnDirectMapping.setBackground(Color.decode("#4CAF50"));
		// btnMappingGeneration.setBackground(Color.decode("#64DD17"));
		// btnMappingGeneration.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnMappingGeneration);
		//
		// JButton btnABoxGeneration = new JButton("ABox Generator");
		// panelTransformation.add(btnABoxGeneration, "cell 0 2,growx");
		// btnABoxGeneration.setFont(new Font("Tahoma", Font.BOLD, 12));
		// //
		// btnRrmlMapping.setBorder(BorderFactory.createLoweredBevelBorder());
		// btnABoxGeneration.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnABoxGeneration);
		//
		// JButton btnTboxGeneration = new JButton("TBox Generator");
		// panelTransformation.add(btnTboxGeneration, "cell 1 2,growx");
		// btnTboxGeneration.setFont(new Font("Tahoma", Font.BOLD, 12));
		// btnTboxGeneration.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnTboxGeneration);
		//
		// JPanel panelLinking = new JPanel();
		// panelLinking.setBorder(new TitledBorder(null, "Linking",
		// TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// panelComponentPalette.add(panelLinking, "cell 0 1,grow");
		// panelLinking.setLayout(new MigLayout("", "[][]", "[][][][][]"));
		//
		//
		// JButton btnResourceExtractor = new JButton("Resource Extractor");
		// panelLinking.add(btnResourceExtractor, "cell 0 1,growx");
		// btnResourceExtractor.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// }
		// });
		// btnResourceExtractor.setFont(new Font("Tahoma", Font.BOLD, 14));
		// btnResourceExtractor.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnResourceExtractor);
		//
		// JButton btnPropertyWeightGenerator = new JButton("PWeight
		// Generator");
		// panelLinking.add(btnPropertyWeightGenerator, "cell 1 1,growx");
		// btnPropertyWeightGenerator.setFont(new Font("Tahoma", Font.BOLD,
		// 14));
		// btnPropertyWeightGenerator.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnPropertyWeightGenerator);
		//
		// JButton btnSemanticBagGenerator = new JButton("SBag Generator");
		// panelLinking.add(btnSemanticBagGenerator, "cell 0 2,growx");
		// btnSemanticBagGenerator.setFont(new Font("Tahoma", Font.BOLD, 14));
		// btnSemanticBagGenerator.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnSemanticBagGenerator);
		//
		// JButton btnMatcher = new JButton("Matcher");
		// panelLinking.add(btnMatcher, "cell 1 2,growx");
		// btnMatcher.setFont(new Font("Tahoma", Font.BOLD, 14));
		// btnMatcher.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnMatcher);
		//
		// JPanel panelLoading = new JPanel();
		// panelLoading.setBorder(new TitledBorder(null, "Load",
		// TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// panelComponentPalette.add(panelLoading, "cell 0 2,grow");
		// panelLoading.setLayout(new MigLayout("", "[grow]", "[][][]"));
		//
		//
		//
		// JButton btnLoading = new JButton("Loading");
		// panelLoading.add(btnLoading, "cell 0 1,growx, span");
		// btnLoading.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// }
		// });
		// btnLoading.setFont(new Font("Tahoma", Font.BOLD, 14));
		// btnLoading.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnLoading);
		//
		// JPanel panelAssociation = new JPanel();
		// panelAssociation
		// .setBorder(new TitledBorder(null, "Association",
		// TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// panelComponentPalette.add(panelAssociation, "cell 0 3,grow");
		// panelAssociation.setLayout(new MigLayout("", "[grow]", "[][]"));
		//
		//
		//
		// JButton btnAssociation = new JButton("");
		// panelAssociation.add(btnAssociation, "cell 0 1,growx");
		// btnAssociation.setIcon(new
		// ImageIcon(PanelCreationApp.class.getResource("/view/images/arrow.png")));
		// btnAssociation.setFont(new Font("Tahoma", Font.BOLD, 14));
		// btnAssociation.setMargin(new Insets(10, 5, 10, 5));
		// etlButtonGroup.add(btnAssociation);
		//
		// JPanel panelCenterContent = new JPanel();
		// scrollPaneCenterPanel.setViewportView(panelCenterContent);
		// panelCenterContent.setLayout(new MigLayout("", "[20][][][]",
		// "[30][][][][]"));

		// JLabel lblColumnName = new JLabel("Base IRI: ");
		// lblColumnName.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelCenterContent.add(lblColumnName, "flowx,cell 1 0");
		//
		// JLabel lblNewLabel = new JLabel("Employee Table");
		// lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		// lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		// panelCenterContent.add(lblNewLabel, "flowx,cell 1 1 3 1,alignx
		// left,gapy 20 10");
		//
		// JLabel lblDbColumn = new JLabel("DB Column");
		// lblDbColumn.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelCenterContent.add(lblDbColumn, "cell 1 2,alignx left");
		//
		// JLabel lblProperty_1 = new JLabel("Property");
		// lblProperty_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelCenterContent.add(lblProperty_1, "cell 3 2,alignx left");
		//
		// JLabel lblBaseiri = new JLabel("baseIRI");
		// lblBaseiri.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelCenterContent.add(lblBaseiri, "cell 1 0");
		//
		// JLabel lblEmpName = new JLabel("Emp Name");
		// lblEmpName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelCenterContent.add(lblEmpName, "cell 1 3");
		//
		// JLabel lblLiteral = new JLabel("Literal");
		// lblLiteral.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelCenterContent.add(lblLiteral, "cell 3 3");
		//
		// JLabel lblWeb = new JLabel("Web");
		// lblWeb.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelCenterContent.add(lblWeb, "cell 1 4");
		//
		// JLabel lblWwwexamplecomeds = new JLabel("www.example.com/eds/");
		// lblWwwexamplecomeds.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelCenterContent.add(lblWwwexamplecomeds, "cell 3 4");

//		JPanel panelMatcher = new JPanel();
//		frame.getContentPane().add(panelMatcher, BorderLayout.NORTH);
//		panelMatcher.setLayout(new MigLayout("", "[grow][][grow]", "[][][][][][][10][][][][][]"));
//		
//		JLabel lblThreshold = new JLabel("Threshold:");
//		lblThreshold.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(lblThreshold, "cell 0 0,alignx right");
//		
//		textFieldThreshold = new JTextField();
//		textFieldThreshold.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(textFieldThreshold, "cell 2 0,growx");
//		textFieldThreshold.setColumns(60);
//
//		JLabel lblPWeight = new JLabel("Pweight File:");
//		lblPWeight.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(lblPWeight, "cell 0 1,alignx right");
//
//		JComboBox comboBoxPWeightFile = new JComboBox();
//		comboBoxPWeightFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(comboBoxPWeightFile, "flowx,cell 2 1,growx");
//
//		JLabel lblDBpediaSBag = new JLabel("DBpedia KB SBag File:");
//		lblDBpediaSBag.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(lblDBpediaSBag, "cell 0 2,alignx right");
//
//		JComboBox comboBoxDBpediaSBag = new JComboBox();
//		comboBoxDBpediaSBag.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(comboBoxDBpediaSBag, "flowx,cell 2 2,growx");
//
//		JLabel lblLocalKBSbag = new JLabel("Local KB SBag File:");
//		lblLocalKBSbag.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(lblLocalKBSbag, "cell 0 3,alignx right");
//
//		JButton btnOpenDBPediaSBag = new JButton("Open");
//		btnOpenDBPediaSBag.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
//		btnOpenDBPediaSBag.setFont(new Font("Tahoma", Font.BOLD, 16));
//		panelMatcher.add(btnOpenDBPediaSBag, "cell 2 2");
//
//		JButton btnOpenLKSBag = new JButton("Open");
//		btnOpenLKSBag.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
//		
//		JComboBox comboBoxLocalKBSbagFilePath = new JComboBox();
//		comboBoxLocalKBSbagFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelMatcher.add(comboBoxLocalKBSbagFilePath, "flowx,cell 2 3,growx");
//		btnOpenLKSBag.setFont(new Font("Tahoma", Font.BOLD, 16));
//		panelMatcher.add(btnOpenLKSBag, "cell 2 3");
//		
//		JButton btnOpenPWeightFile = new JButton("Open");
//		btnOpenPWeightFile.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
//		btnOpenPWeightFile.setFont(new Font("Tahoma", Font.BOLD, 16));
//		panelMatcher.add(btnOpenPWeightFile, "cell 2 1");
		

//		 JPanel panelSBagGeneration = new JPanel();
//		 frame.getContentPane().add(panelSBagGeneration,BorderLayout.CENTER);
//		 panelSBagGeneration.setLayout(new MigLayout("", "[][][700,grow]", "[][][][][][][10][][][][][]"));
//		
//		 JLabel lblOperation = new JLabel("Operation:");
//		 lblOperation.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		 panelSBagGeneration.add(lblOperation, "cell 0 0,alignx right");
//		
//		 JComboBox comboBoxOperation = new JComboBox();
//		 comboBoxOperation.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		 comboBoxOperation.setModel(new DefaultComboBoxModel(new String[] {"Local KB Semantic Bag Generation", "DBpedia Resource Semantic Bag Generation", "DBpedia Data Semantic Bag Generation"}));
//		 panelSBagGeneration.add(comboBoxOperation, "cell 2 0,growx");
//		 
//		 JButton btnOpenResourceFile = new JButton("Open Resource File");
//		 btnOpenResourceFile.setFont(new Font("Tahoma", Font.BOLD, 16));
//		 btnOpenResourceFile.addActionListener(new ActionListener() {
//		 	public void actionPerformed(ActionEvent arg0) {
//		 	}
//		 });
//		 panelSBagGeneration.add(btnOpenResourceFile, "cell 2 1,growx");
//		
//		 JLabel lblResource = new JLabel("Resource:");
//		 lblResource.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		 panelSBagGeneration.add(lblResource, "cell 0 2,alignx right");
//		 
//		 JComboBox comboBoxResources = new JComboBox();
//		 comboBoxResources.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		 panelSBagGeneration.add(comboBoxResources, "cell 2 2,growx");
//		 
//		  JLabel lblRdfFile = new JLabel("RDF File:");
//		  lblRdfFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		  panelSBagGeneration.add(lblRdfFile, "cell 0 3,alignx right");
//		 
//		  JComboBox comboBoxRDFFilePath = new JComboBox();
//		  comboBoxRDFFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		  panelSBagGeneration.add(comboBoxRDFFilePath, "flowx,cell 2 3,growx");
//		 
//		  JLabel lblDBPediaDataFile = new JLabel("DBpedia Data File:");
//		  lblDBPediaDataFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		  panelSBagGeneration.add(lblDBPediaDataFile, "cell 0 4,alignx right");
//		 
//		  JComboBox comboBoxDBpediDataFilePath = new JComboBox();
//		  comboBoxDBpediDataFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		  panelSBagGeneration.add(comboBoxDBpediDataFilePath, "flowx,cell 2 4,growx");
//		 
//		  JLabel lblSBagFile = new JLabel("Semantic Bag File:");
//		  lblSBagFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		  panelSBagGeneration.add(lblSBagFile, "cell 0 5,alignx right");
//		 
//		  JButton btnOpenRDFFile = new JButton("Open");
//		  btnOpenRDFFile.addActionListener(new ActionListener() {
//		  	public void actionPerformed(ActionEvent arg0) {
//		  	}
//		  });
//		  btnOpenRDFFile.setFont(new Font("Tahoma", Font.BOLD, 16));
//		  panelSBagGeneration.add(btnOpenRDFFile, "cell 2 3");
//		  
//		   JButton btnOpenDBprdiaDataFile = new JButton("Open");
//		   btnOpenDBprdiaDataFile.addActionListener(new ActionListener() {
//		   	public void actionPerformed(ActionEvent arg0) {
//		   	}
//		   });
//		   btnOpenDBprdiaDataFile.setFont(new Font("Tahoma", Font.BOLD, 16));
//		   panelSBagGeneration.add(btnOpenDBprdiaDataFile, "cell 2 4");
//		    
//		    JLabel lblSBagFilePath = new JLabel("None");
//		    lblSBagFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		    panelSBagGeneration.add(lblSBagFilePath, "flowx,cell 2 5,growx");
//		   
//		    JButton btnOpenSBagFile = new JButton("Open");
//		    btnOpenSBagFile.addActionListener(new ActionListener() {
//		    	public void actionPerformed(ActionEvent arg0) {
//		    	}
//		    });
//		    btnOpenSBagFile.setFont(new Font("Tahoma", Font.BOLD, 16));
//		    panelSBagGeneration.add(btnOpenSBagFile, "cell 2 5");

		
		
		//
		// JPanel panelResourceExtractor = new JPanel();
		// frame.getContentPane().add(panelResourceExtractor,
		// BorderLayout.CENTER);
		// panelResourceExtractor.setLayout(new MigLayout("", "[grow][][grow]",
		// "[][][][10][][][][][]"));
		//
		// JLabel lblOperation = new JLabel("Operation:");
		// lblOperation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(lblOperation, "cell 0 0,alignx right");
		//
		// JComboBox comboBox = new JComboBox();
		// comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// comboBox.setModel(new DefaultComboBoxModel(new String[] {"DBpedia
		// Resource Retriever", "SPARQL End Resource Retriever", "Local KB
		// Resource Retriever"}));
		// panelResourceExtractor.add(comboBox, "cell 2 0,growx");
		//
		// JLabel lblKeyword = new JLabel("Key Word:");
		// lblKeyword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(lblKeyword, "cell 0 1,alignx right");
		//
		// txtKeyword = new JTextField();
		// txtKeyword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(txtKeyword, "cell 2 1,growx");
		// txtKeyword.setColumns(60);
		//
		// JLabel lblnumOfHit = new JLabel("Number of Hit:");
		// lblnumOfHit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(lblnumOfHit, "cell 0 2,alignx right");
		//
		// JComboBox comboBoxDBName = new JComboBox();
		// comboBoxDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// comboBoxDBName.setEditable(true);
		// panelResourceExtractor.add(comboBoxDBName, "cell 2 2,growx");
		//
		// JLabel lblDBPediaDataFile = new JLabel("DBpedia Data File:");
		// lblDBPediaDataFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(lblDBPediaDataFile, "cell 0 3,alignx
		// right");
		//
		// JComboBox comboBoxDBUserName = new JComboBox();
		// comboBoxDBUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(comboBoxDBUserName, "flowx,cell 2
		// 3,growx");
		//
		// JLabel lblRdfFile = new JLabel("RDF File:");
		// lblRdfFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(lblRdfFile, "cell 0 4,alignx right");
		//
		// JComboBox comboBoxRDFFile = new JComboBox();
		// comboBoxRDFFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(comboBoxRDFFile, "flowx,cell 2 4,growx");
		//
		// JLabel lblResourceFile = new JLabel("Resource File:");
		// lblResourceFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(lblResourceFile, "cell 0 5,alignx right");
		//
		// JButton btnNewDBPEdiaDataFile = new JButton("New");
		// btnNewDBPEdiaDataFile.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelResourceExtractor.add(btnNewDBPEdiaDataFile, "cell 2 3");
		//
		// JComboBox comboBoxResourceFile = new JComboBox();
		// comboBoxResourceFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelResourceExtractor.add(comboBoxResourceFile, "flowx,cell 2
		// 5,growx");
		//
		// JButton btnNewResFile = new JButton("New");
		// btnNewResFile.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelResourceExtractor.add(btnNewResFile, "cell 2 5");
		//
		// JButton btnOpenRDF = new JButton("Open");
		// btnOpenRDF.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelResourceExtractor.add(btnOpenRDF, "cell 2 4");

//		JPanel panelETL = new JPanel();
//		// frame.getContentPane().add(panelETL, BorderLayout.CENTER);
//		panelETL.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][][][grow]"));
//
//		JLabel lblDbName = new JLabel("DB Name: ");
//		lblDbName.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelETL.add(lblDbName, "cell 0 0,alignx trailing");
//
//		textFieldDBName = new JTextField();
//		textFieldDBName.setText("dvdrental");
//		textFieldDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelETL.add(textFieldDBName, "cell 1 0");
//		textFieldDBName.setColumns(20);
//
//		JLabel lblDbUserName = new JLabel("DB User Name:");
//		lblDbUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelETL.add(lblDbUserName, "cell 0 1,alignx trailing");
//
//		textFieldUserName = new JTextField();
//		textFieldUserName.setText("postgres");
//		textFieldUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelETL.add(textFieldUserName, "cell 1 1");
//		textFieldUserName.setColumns(35);
//
//		JLabel lblDbPassword = new JLabel("DB Password");
//		lblDbPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelETL.add(lblDbPassword, "cell 0 2,alignx trailing");
//
//		passwordFieldPassword = new JPasswordField();
//		passwordFieldPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		passwordFieldPassword.setColumns(35);
//		passwordFieldPassword.setText("13701005");
//		panelETL.add(passwordFieldPassword, "cell 1 2");
//
//		JButton btnFileLocation = new JButton("File Location");
//		btnFileLocation.setFont(new Font("Tahoma", Font.BOLD, 16));
//		btnFileLocation.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//			}
//		});
//
//		JLabel lblBaseIri = new JLabel("Base IRI: ");
//		lblBaseIri.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		lblBaseIri.setHorizontalAlignment(SwingConstants.TRAILING);
//		panelETL.add(lblBaseIri, "cell 0 3,alignx trailing");
//
//		textFieldBaseIRI = new JTextField();
//		textFieldBaseIRI.setFont(new Font("Tahoma", Font.PLAIN, 16));
//		panelETL.add(textFieldBaseIRI, "cell 1 3");
//		textFieldBaseIRI.setColumns(35);
//		panelETL.add(btnFileLocation, "cell 0 4");
//
//		JLabel lblFilePath = new JLabel("Selected File: None");
//		panelETL.add(lblFilePath, "cell 1 4,growx");

		// JPanel panelMGInput = new JPanel();
		// frame.getContentPane().add(panelMGInput, BorderLayout.CENTER);
		// panelMGInput.setLayout(new MigLayout("", "[grow][][grow]",
		// "[][][10][][][][]"));
		//
		// JLabel lblBaseIri_1 = new JLabel("Base IRI:");
		// lblBaseIri_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelMGInput.add(lblBaseIri_1, "cell 0 0,alignx right");
		//
		// JComboBox comboBoxBaseIRI = new JComboBox();
		// comboBoxBaseIRI.setEditable(true);
		// panelMGInput.add(comboBoxBaseIRI, "cell 2 0,growx");
		//
		// JLabel lblDbName_1 = new JLabel("DB Name: ");
		// lblDbName_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelMGInput.add(lblDbName_1, "cell 0 1,alignx right");
		//
		// JComboBox comboBoxDBName = new JComboBox();
		// comboBoxDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// comboBoxDBName.setEditable(true);
		// panelMGInput.add(comboBoxDBName, "cell 2 1,growx");
		//
		// JLabel lblDbUserName_1 = new JLabel("DB User Name:");
		// lblDbUserName_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelMGInput.add(lblDbUserName_1, "cell 0 2,alignx right");
		//
		// JComboBox comboBoxDBUserName = new JComboBox();
		// comboBoxDBUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// comboBoxDBUserName.setEditable(true);
		// panelMGInput.add(comboBoxDBUserName, "cell 2 2,growx");
		//
		// JLabel lblDbPassword_1 = new JLabel("DB Password:");
		// lblDbPassword_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelMGInput.add(lblDbPassword_1, "cell 0 3,alignx right");
		//
		// JComboBox comboBoxDBPassword = new JComboBox();
		// comboBoxDBPassword.setEditable(true);
		// panelMGInput.add(comboBoxDBPassword, "cell 2 3,growx");
		//
		// JLabel lblOutputFilePath = new JLabel("Output File Path:");
		// lblOutputFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelMGInput.add(lblOutputFilePath, "cell 0 5,alignx right");
		//
		// JComboBox comboBoxOutputFilePath = new JComboBox();
		// panelMGInput.add(comboBoxOutputFilePath, "flowx,cell 2 5,growx");
		//
		// JLabel lblOutputFileName = new JLabel("Output File Name:");
		// lblOutputFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelMGInput.add(lblOutputFileName, "cell 0 6,alignx right");
		//
		// textFieldOutputFileName = new JTextField();
		// textFieldOutputFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelMGInput.add(textFieldOutputFileName, "cell 2 6,growx");
		// textFieldOutputFileName.setColumns(35);
		//
		// JButton btnNew = new JButton("New");
		// btnNew.setFont(new Font("Tahoma", Font.BOLD, 16));
		// panelMGInput.add(btnNew, "cell 2 5");
		//
		// JPanel panelETL = new JPanel();
		// //frame.getContentPane().add(panelETL, BorderLayout.CENTER);
		// panelETL.setLayout(new MigLayout("", "[][grow]",
		// "[][][][][][][][][][][][grow]"));
		//
		// JLabel lblDbName = new JLabel("DB Name: ");
		// lblDbName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelETL.add(lblDbName, "cell 0 0,alignx trailing");
		//
		// textFieldDBName = new JTextField();
		// textFieldDBName.setText("dvdrental");
		// textFieldDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelETL.add(textFieldDBName, "cell 1 0");
		// textFieldDBName.setColumns(20);
		//
		// JLabel lblDbUserName = new JLabel("DB User Name:");
		// lblDbUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelETL.add(lblDbUserName, "cell 0 1,alignx trailing");
		//
		// textFieldUserName = new JTextField();
		// textFieldUserName.setText("postgres");
		// textFieldUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelETL.add(textFieldUserName, "cell 1 1");
		// textFieldUserName.setColumns(35);
		//
		// JLabel lblDbPassword = new JLabel("DB Password");
		// lblDbPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelETL.add(lblDbPassword, "cell 0 2,alignx trailing");
		//
		// passwordFieldPassword = new JPasswordField();
		// passwordFieldPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// passwordFieldPassword.setColumns(35);
		// passwordFieldPassword.setText("13701005");
		// panelETL.add(passwordFieldPassword, "cell 1 2");
		//
		// JButton btnFileLocation = new JButton("File Location");
		// btnFileLocation.setFont(new Font("Tahoma", Font.BOLD, 16));
		// btnFileLocation.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// }
		// });
		//
		// JLabel lblBaseIri = new JLabel("Base IRI: ");
		// lblBaseIri.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// lblBaseIri.setHorizontalAlignment(SwingConstants.TRAILING);
		// panelETL.add(lblBaseIri, "cell 0 3,alignx trailing");
		//
		// textFieldBaseIRI = new JTextField();
		// textFieldBaseIRI.setFont(new Font("Tahoma", Font.PLAIN, 16));
		// panelETL.add(textFieldBaseIRI, "cell 1 3");
		// textFieldBaseIRI.setColumns(35);
		// panelETL.add(btnFileLocation, "cell 0 4");
		//
		// JLabel lblFilePath = new JLabel("Selected File: None");
		// panelETL.add(lblFilePath, "cell 1 4,growx");

	}

}
