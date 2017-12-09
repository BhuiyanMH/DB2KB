package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import controller.OperationsController;
import model.DatabaseConnection;
import model.ETLMethods;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.Font;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections15.Factory;
import org.apache.http.auth.KerberosCredentials;

import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainFrame {

	static CardLayout appLayout;
	JPanel panelMainContainer;
	
	public static JFrame frmMain;
	JTextArea textAreaRDF;
	JLabel lblCSVFileName;
	OperationsController  operationsController;
	
	//Keys for the panels
	final static String DB_PANEL_KEY = "DBPanel";
	final static String CSV_PANEL_KEY = "CSVPanel";
	final static String DB_PROPERTIES_PANEL_KEY = "DBPropertiesPanel";
	final static String DIRECT_MAPPING_PANEL_KEY = "DirectRDFPANEL";
	final static String SPARQL_QUERY_PANEL_KEY = "SPARQLPanel";
	final static String LINKING_PANEL_KEY = "LinkingPanel";
	final static String RML_PROCESSING_PANEL_KEY = "RMLProcessingPanel";
	final static String DB_PARAMS_PANEL_KEY = "DBParamsPanel";
	final static String TBOX_EXTRACTION_PANEL_KEY = "TBoxExtractionPanel";
	final static String TBOX_ENRICHMENT_PANEL_KEY = "TBoxEnrichmentPanel";
	final static String ETL_PANEL_KEY = "ETLPanel";
	final static String EXT_DB_PANEL_KEY = "ExtractionDBPanel";
	
	public static String dbURL;
	public static String dbUserName;
	public static String dbPassword;
	private JPanel panelTopButtons;
	//All views
	private PanelDatabase dbParamsPanel;
	private PanelCSV csvPanel;
	private PanelDirectRDF directRDFPanel;
	private PanelSparqlQuery sparqlQueryPanel;
	private LinkingPanel linkingPanel;
	private PanelRMLProcessing rmlProcessingPanel;
	//private PanelKowledgeEnrichment knowledgeEnrichmentPanel;
	private PanelTBoxExtraction tBoxExtractionPanel;
	private PanelTBoxEnrichment tBoxEnrichmentPanel;
	//private PanelETL etlPanel;
	private PanelETL etlPanel;
	
	
	private JButton btnDirectMapping;
	private JButton btnSparql;
	private JButton btnLinking;
	private JButton btnRMLTransform;
	private JPanel panelMapping;
	private JLabel lblMapping;
	private JPanel panelTransform;
	private JLabel lblTransform;
	private JPanel panelRetreive;
	private JLabel lblQuery;
	private JPanel panelLinking;
	private JLabel lblLinking;
	
	static boolean dbConnected = false;
	
	private JLabel lblExtract;
	private JButton btnPostgresDb;
	private JButton btnTboxExtraction;
	private JButton btnTboxEnrichment;
	private JButton btnEtl;
	private JPanel panelETL;
	private JLabel lblEtl;
	private JPanel panelExtraction;
	private JButton btnExtDB;
	private PanelExtractDB panelExtractionDB;
//	private JButton btnRun;
//	private JButton btnClear;
	//PanelDBProperties dbPropertiesPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					MainFrame window = new MainFrame();
					window.frmMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		try {
			
			for(LookAndFeelInfo lF : UIManager.getInstalledLookAndFeels())
			{
				if(lF.getName().equals("Nimbus"))
				{
					UIManager.setLookAndFeel(lF.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Look and feel not changed.");
		}
		
		
		frmMain = new JFrame();
		frmMain.setTitle("DB2KB");
		frmMain.setBounds(100, 100, 1200, 800);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmMain.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panelTopButtons = new JPanel();
		JScrollPane scrollPaneTopButtons = new JScrollPane(panelTopButtons);
		frmMain.getContentPane().add(scrollPaneTopButtons, BorderLayout.NORTH);
		
		
		
		FlowLayout flowLayout = (FlowLayout) panelTopButtons.getLayout();
		flowLayout.setVgap(15);
		flowLayout.setHgap(10);
		panelTopButtons.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		panelExtraction = new JPanel();
		panelExtraction.setBorder(new TitledBorder(null, "Extraction", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTopButtons.add(panelExtraction);
		
		btnExtDB = new JButton("ExtDB");
		btnExtDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(EXT_DB_PANEL_KEY);
			}
		});
		btnExtDB.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelExtraction.add(btnExtDB);
		
		btnSparql = new JButton("ExtSPARQL");
		panelExtraction.add(btnSparql);
		btnSparql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(SPARQL_QUERY_PANEL_KEY);
			}
		});
		btnSparql.setFont(new Font("Tahoma", Font.BOLD, 16));
		//frmMain.getContentPane().add(panelTopButtons, BorderLayout.NORTH);
		
		panelMapping = new JPanel();
		panelMapping.setBorder(new TitledBorder(null, "Mapping", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//panelMapping.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelMapping);
		
		lblMapping = new JLabel("MAPPING: ");
		lblMapping.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMapping.setVisible(false);
		panelMapping.add(lblMapping);
		
		btnDirectMapping = new JButton("Direct Mapping");
		panelMapping.add(btnDirectMapping);
		btnDirectMapping.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JButton btnDatabaseR2RML = new JButton("R2RML");
		panelMapping.add(btnDatabaseR2RML);
		btnDatabaseR2RML.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		btnTboxExtraction = new JButton("TBox Extraction");
		
		btnTboxExtraction.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnTboxExtraction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(TBOX_EXTRACTION_PANEL_KEY);	
			}
		});
		panelMapping.add(btnTboxExtraction);
		
		btnTboxEnrichment = new JButton("TBox Enrichment");
		btnTboxEnrichment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPanel(TBOX_ENRICHMENT_PANEL_KEY);
			}
		});
		btnTboxEnrichment.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelMapping.add(btnTboxEnrichment);
		btnDatabaseR2RML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(DB_PANEL_KEY);
				
			}
		});
		btnDirectMapping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(DIRECT_MAPPING_PANEL_KEY);
			}
		});
		
		panelTransform = new JPanel();
		panelTransform.setBorder(new TitledBorder(null, "Transformation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//panelTransform.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelTransform);
		
		lblTransform = new JLabel("TRANSFORM: ");
		lblTransform.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTransform.setVisible(false);
		panelTransform.add(lblTransform);
		
		JButton btnCSV = new JButton("CSV");
		panelTransform.add(btnCSV);
		btnCSV.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			showPanel(CSV_PANEL_KEY);
				
			}
		});
		
		btnRMLTransform = new JButton("RML");
		btnRMLTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(RML_PROCESSING_PANEL_KEY);
			}
		});
		panelTransform.add(btnRMLTransform);
		btnRMLTransform.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		panelRetreive = new JPanel();
		panelRetreive.setVisible(false);
		panelRetreive.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelRetreive);
		
		lblQuery = new JLabel("RETREIVE: ");
		panelRetreive.add(lblQuery);
		lblQuery.setVisible(false);
		lblQuery.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		panelLinking = new JPanel();
		panelLinking.setBorder(new TitledBorder(null, "Linking", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//panelLinking.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelLinking);
		
		lblLinking = new JLabel("LINKING: ");
		lblLinking.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblLinking.setVisible(false);
		panelLinking.add(lblLinking);
		
		btnLinking = new JButton("DBPedia Linking");
		panelLinking.add(btnLinking);
		btnLinking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPanel(LINKING_PANEL_KEY);
				
			}
		});
		btnLinking.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		panelETL = new JPanel();
		panelETL.setBorder(new TitledBorder(null, "ETL", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//panelETL.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopButtons.add(panelETL);
		
		lblEtl = new JLabel("ETL: ");
		lblEtl.setVisible(false);
		lblEtl.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelETL.add(lblEtl);
		
		btnEtl = new JButton("ETL");
		panelETL.add(btnEtl);
		btnEtl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//etlButtonHandler();
				showPanel(ETL_PANEL_KEY);
			}
		});
		btnEtl.setFont(new Font("Tahoma", Font.BOLD, 16));
		
//		btnRun = new JButton("Run");
//		btnRun.setVisible(true);
//		
//		btnClear = new JButton("Clear");
//		btnClear.setFont(new Font("Tahoma", Font.BOLD, 16));
//		panelETL.add(btnClear);
//		btnRun.setFont(new Font("Tahoma", Font.BOLD, 16));
//		panelETL.add(btnRun);
		
		
		panelMainContainer = new JPanel();
		frmMain.getContentPane().add(panelMainContainer, BorderLayout.CENTER);
		panelMainContainer.setLayout(new CardLayout(0, 0));
		
		
		//Initilaize the panels
		
		dbParamsPanel = new PanelDatabase(panelMainContainer);
		csvPanel = new PanelCSV(panelMainContainer);
		//dbPropertiesPanel = new PanelDBProperties(panelMainContainer);
		directRDFPanel = new PanelDirectRDF(panelMainContainer);
		sparqlQueryPanel = new PanelSparqlQuery(panelMainContainer);
		linkingPanel = new LinkingPanel();
		rmlProcessingPanel = new PanelRMLProcessing(panelMainContainer);
		tBoxExtractionPanel = new PanelTBoxExtraction();
		tBoxEnrichmentPanel = new PanelTBoxEnrichment();
		etlPanel = new PanelETL();
		panelExtractionDB = new PanelExtractDB();
	
		//Adding the panels to the frame container
		panelMainContainer.add(etlPanel, ETL_PANEL_KEY);
		panelMainContainer.add(panelExtractionDB, EXT_DB_PANEL_KEY);
		panelMainContainer.add(sparqlQueryPanel, SPARQL_QUERY_PANEL_KEY);
		panelMainContainer.add(tBoxEnrichmentPanel, TBOX_ENRICHMENT_PANEL_KEY);
		panelMainContainer.add(tBoxExtractionPanel, TBOX_EXTRACTION_PANEL_KEY);
		
		panelMainContainer.add(dbParamsPanel, DB_PANEL_KEY);
		//panelMainContainer.add(dbPropertiesPanel, DB_PROPERTIES_PANEL_KEY);
		panelMainContainer.add(csvPanel, CSV_PANEL_KEY);	
		panelMainContainer.add(directRDFPanel, DIRECT_MAPPING_PANEL_KEY);
		panelMainContainer.add(linkingPanel, LINKING_PANEL_KEY);
		panelMainContainer.add(rmlProcessingPanel, RML_PROCESSING_PANEL_KEY);
		
		

		
		//Initializing the controller for the buttons
		
		operationsController = new OperationsController();
		
		appLayout = (CardLayout)panelMainContainer.getLayout();
			
		//System.out.println("System Initialized");
		
		//showPanel(DB_PROPERTIES_PANEL_KEY);

	}
	
	void showPanel(String key)
	{
		appLayout.show(panelMainContainer, key);
	}
	

}
