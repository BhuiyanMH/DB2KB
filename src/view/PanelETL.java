package view;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.JobAttributes;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;

import org.apache.jena.iri.ViolationCodes.Initialize;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.function.library.namespace;
import org.apache.jena.sparql.function.library.leviathan.factorial;
import org.apache.jena.tdb.base.objectfile.StringFile;

import edu.uci.ics.jung.visualization.jai.PerspectiveShapeTransformer;
import model.ETLABoxGenOperation;
import model.ETLExtractionSPARQL;
import model.ETLLinkingOperation;
import model.ETLLoadingOperation;
import model.ETLMappingGenOperation;
import model.ETLMatcher;
import model.ETLOperation;
import model.ETLPWeightGenerator;
import model.ETLResourceRetreiver;
import model.ETLSBagGenerator;
import model.ETLTBoxGenOperation;
import scala.annotation.implicitNotFound;
import test.PanelCreationApp;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.annotation.Resource;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;

public class PanelETL extends JPanel {

	JTextPane textPaneETLStatus;
	ArrayList<Operation> allOperations;
	ArrayList<Arrow> allArrows;
	Graph graphPanel;
	ETLSparqlQuery queryPanel;
	JPanel panelETLButtons;
	ButtonGroup etlButtonGroup;
	JButton selectedButton = null;
	JPanel panelComponentPalette;

	Color defaultButtonBGColor;

	// Strings for oepration names
	static final String START = "Start";
	static final String MAPPING_GEN = "Mapping Generator";
	static final String ABOX_GEN = "ABox Generator";
	static final String TBOX_GEN = "TBox Generator";
	static final String LOADER = "Loader";
	static final String EXT_SPARQL = "ExtSPARQL";

	static final String RESOURCE_RETRIEVER = "Resource Retriever";
	static final String PWEIGHT_GENERATOR = "PWeight Generator";
	static final String SBAG_GENERATOR = "SBag Generator";
	static final String MATCHER = "Matcher";

	public PanelETL() {
		setLayout(new BorderLayout(10, 10));

		// panel showing the graph
		graphPanel = new Graph();
		add(graphPanel, BorderLayout.CENTER);

		panelETLButtons = new JPanel();
		panelETLButtons.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panelETLButtons, BorderLayout.SOUTH);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				graphPanel.clearPanel();
				textPaneETLStatus.setText("ETL STATUS:");
				desectAllPaletteButton();
			}
		});

		defaultButtonBGColor = btnClear.getBackground();

		btnClear.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelETLButtons.add(btnClear);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				graphPanel.refreshPanel();
				textPaneETLStatus.setText("ETL STATUS:");

			}
		});
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelETLButtons.add(btnRefresh);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				textPaneETLStatus.setText("ETL STATUS:");

				boolean checkStatus = checkStatus();
				boolean checkAssociation = checkAssociation();
				if (checkStatus) {

					if (checkAssociation) {
						executeETL();
					} else {
						JOptionPane.showMessageDialog(null, "Please connect all Operation to flow.");
					}

				} else {
					JOptionPane.showMessageDialog(null, "Please Configure all Operations.");
				}
			}
		});
		btnRun.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelETLButtons.add(btnRun);

		textPaneETLStatus = new JTextPane();
		textPaneETLStatus.setEditable(false);
		textPaneETLStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textPaneETLStatus.setText("ETL STATUS:");

		add(textPaneETLStatus, BorderLayout.NORTH);

		panelComponentPalette = new JPanel();
		panelComponentPalette.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panelComponentPalette, BorderLayout.WEST);
		panelComponentPalette.setLayout(new MigLayout("", "[grow]", "[grow][][][][]"));

		etlButtonGroup = new ButtonGroup();
		
		JPanel panelExtraction = new JPanel();
		panelExtraction.setBorder(new TitledBorder(null, "Extraction", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelComponentPalette.add(panelExtraction, "cell 0 0,grow");
		panelExtraction.setLayout(new MigLayout("", "[]", "[][]"));
		
		JButton btnExtsparql = new JButton(EXT_SPARQL);
		btnExtsparql.setMargin(new Insets(10, 5, 10, 5));
		etlButtonGroup.add(btnExtsparql);
		btnExtsparql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
				
			}
		});
		btnExtsparql.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelExtraction.add(btnExtsparql, "cell 0 0");

		JPanel panelTransformation = new JPanel();
		panelTransformation.setBorder(
				new TitledBorder(null, "Transformation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelComponentPalette.add(panelTransformation, "cell 0 1,grow");
		panelTransformation.setLayout(new MigLayout("", "[grow][grow]", "[][][][]"));

		JButton btnMappingGeneration = new JButton(MAPPING_GEN);
		panelTransformation.add(btnMappingGeneration, "cell 0 1,growx, span");
		btnMappingGeneration.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnMappingGeneration.setMargin(new Insets(10, 5, 10, 5));
		btnMappingGeneration.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
			}
		});
		etlButtonGroup.add(btnMappingGeneration);

		JButton btnABoxGeneration = new JButton(ABOX_GEN);
		panelTransformation.add(btnABoxGeneration, "cell 0 2,growx");
		btnABoxGeneration.setFont(new Font("Tahoma", Font.BOLD, 12));
		// btnRrmlMapping.setBorder(BorderFactory.createLoweredBevelBorder());
		btnABoxGeneration.setMargin(new Insets(10, 5, 10, 5));
		btnABoxGeneration.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
			}
		});
		etlButtonGroup.add(btnABoxGeneration);

		JButton btnTboxGeneration = new JButton(TBOX_GEN);
		panelTransformation.add(btnTboxGeneration, "cell 1 2,growx");
		btnTboxGeneration.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnTboxGeneration.setMargin(new Insets(10, 5, 10, 5));
		btnTboxGeneration.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);

			}
		});
		etlButtonGroup.add(btnTboxGeneration);

		JPanel panelLinking = new JPanel();
		panelLinking.setBorder(new TitledBorder(null, "Linking", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelComponentPalette.add(panelLinking, "cell 0 2,grow");
		panelLinking.setLayout(new MigLayout("", "[][]", "[][][][][]"));

		JButton btnResourceRetriever = new JButton(RESOURCE_RETRIEVER);
		panelLinking.add(btnResourceRetriever, "cell 0 1,growx");
		btnResourceRetriever.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
			}
		});
		btnResourceRetriever.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnResourceRetriever.setMargin(new Insets(10, 5, 10, 5));
		etlButtonGroup.add(btnResourceRetriever);

		JButton btnPropertyWeightGenerator = new JButton(PWEIGHT_GENERATOR);
		panelLinking.add(btnPropertyWeightGenerator, "cell 1 1,growx");
		btnPropertyWeightGenerator.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnPropertyWeightGenerator.setMargin(new Insets(10, 5, 10, 5));
		btnPropertyWeightGenerator.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
			}
		});
		etlButtonGroup.add(btnPropertyWeightGenerator);

		JButton btnSemanticBagGenerator = new JButton(SBAG_GENERATOR);
		panelLinking.add(btnSemanticBagGenerator, "cell 0 2,growx");
		btnSemanticBagGenerator.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSemanticBagGenerator.setMargin(new Insets(10, 5, 10, 5));
		btnSemanticBagGenerator.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
			}
		});
		etlButtonGroup.add(btnSemanticBagGenerator);

		JButton btnMatcher = new JButton(MATCHER);
		panelLinking.add(btnMatcher, "cell 1 2,growx");
		btnMatcher.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnMatcher.setMargin(new Insets(10, 5, 10, 5));
		btnMatcher.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);

			}
		});
		etlButtonGroup.add(btnMatcher);

		JPanel panelLoading = new JPanel();
		panelLoading.setBorder(new TitledBorder(null, "Load", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelComponentPalette.add(panelLoading, "cell 0 3,grow");
		panelLoading.setLayout(new MigLayout("", "[grow]", "[][][]"));

		JButton btnLoading = new JButton(LOADER);
		panelLoading.add(btnLoading, "cell 0 1,growx, span");
		btnLoading.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
			}
		});
		btnLoading.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLoading.setMargin(new Insets(10, 5, 10, 5));
		etlButtonGroup.add(btnLoading);

		JPanel panelAssociation = new JPanel();
		panelAssociation
				.setBorder(new TitledBorder(null, "Control Flow", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelComponentPalette.add(panelAssociation, "cell 0 4,grow");
		panelAssociation.setLayout(new MigLayout("", "[grow]", "[][]"));

		JButton btnAssociation = new JButton("");
		panelAssociation.add(btnAssociation, "cell 0 1,growx");
		btnAssociation.setIcon(new ImageIcon(PanelCreationApp.class.getResource("/view/images/arrow.png")));
		btnAssociation.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAssociation.setMargin(new Insets(10, 5, 10, 5));
		btnAssociation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				paletteButtonHandler(arg0);
			}
		});
		etlButtonGroup.add(btnAssociation);
		allOperations = new ArrayList<>();
		allArrows = new ArrayList<>();

	}

	// handler for click on paltte buttons
	public void paletteButtonHandler(ActionEvent arg0) {

		selectedButton = (JButton) arg0.getSource();
		updateColor((JButton) arg0.getSource());
	}

	// update the color of buttons
	private void updateColor(JButton selectedButton) {

		Enumeration<AbstractButton> paletteButton = etlButtonGroup.getElements();

		while (paletteButton.hasMoreElements()) {
			JButton button = (JButton) paletteButton.nextElement();

			if (button == selectedButton) {
				button.setBackground(Color.decode("#64DD17"));
			} else {
				button.setBackground(defaultButtonBGColor);

			}
		}

	}

	private void desectAllPaletteButton() {

		selectedButton = null;
		Enumeration<AbstractButton> paletteButton = etlButtonGroup.getElements();

		while (paletteButton.hasMoreElements()) {
			JButton button = (JButton) paletteButton.nextElement();
			button.setBackground(defaultButtonBGColor);
		}

	}

	// check whether the associations between the operations of ETL are done
	// correctly
	protected boolean checkAssociation() {

		ArrayList<Operation> associatedOperation = new ArrayList<>();

		for (Arrow arrow : allArrows) {
			associatedOperation.add(arrow.getTargetOperation());
		}

		for (Operation operation : allOperations) {

			if (!associatedOperation.contains(operation) && !operation.getOperationName().equals("Start")) {
				return false;
			}
		}

		return true;
	}

	// Execute the ETL operation
	protected boolean executeETL() {

		// Execute the operations as in order..
		// First all mapping gen, then TBox... etc

		
		ArrayList<Operation> extractSPAQRQLOpertations = getNamedOperations(EXT_SPARQL);
		ArrayList<Operation> mappingGenOperations = getNamedOperations(MAPPING_GEN);
		ArrayList<Operation> tBoxGenOperations = getNamedOperations(TBOX_GEN);
		ArrayList<Operation> aBoxGenOperations = getNamedOperations(ABOX_GEN);

		ArrayList<Operation> resourceRetrieverOperations = getNamedOperations(RESOURCE_RETRIEVER);
		ArrayList<Operation> pWeightGenOperations = getNamedOperations(PWEIGHT_GENERATOR);
		ArrayList<Operation> sBagGenOperations = getNamedOperations(SBAG_GENERATOR);
		ArrayList<Operation> matcherOperations = getNamedOperations(MATCHER);
		ArrayList<Operation> loadingOperations = getNamedOperations(LOADER);
		
		for(Operation operation: extractSPAQRQLOpertations){
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		for (Operation operation : mappingGenOperations) {
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		for (Operation operation : tBoxGenOperations) {
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		for (Operation operation : aBoxGenOperations) {
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		for (Operation operation : resourceRetrieverOperations) {
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		for (Operation operation : pWeightGenOperations) {
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		for (Operation operation : sBagGenOperations) {
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		for (Operation operation : matcherOperations) {
			operation.getEtlOperation().execute(textPaneETLStatus);
		}

		// Loading: get RDFs from loading operations and load them in a common
		// Jena model

		ArrayList<String> rdfFilePaths = new ArrayList<>();

		boolean isLoadingFound = false;
		for (Operation operation : loadingOperations) {

			ETLLoadingOperation etlLoadingOperation = (ETLLoadingOperation) operation.getEtlOperation();
			rdfFilePaths.add(etlLoadingOperation.getInputFilePath());
			isLoadingFound = true;
		}

		// if Loading operation found, show the Query panel
		if (isLoadingFound) {
			queryPanel = new ETLSparqlQuery(rdfFilePaths);
			this.add(queryPanel, BorderLayout.CENTER);
			graphPanel.setVisible(false);
			panelETLButtons.setVisible(false);
			panelComponentPalette.setVisible(false);
		}

		return false;
	}

	// return operation object by name
	private ArrayList<Operation> getNamedOperations(String opName) {

		ArrayList<Operation> temp = new ArrayList<>();

		for (Operation operation : allOperations) {

			if (operation.getOperationName().equals(opName))
				temp.add(operation);
		}
		return temp;
	}

	// check whether all inputs to all operations are done or not
	protected boolean checkStatus() {

		for (Operation operation : allOperations) {
			if (!operation.inputStatus)
				return false;
		}
		return true;
	}

	// Panel to draw the graph
	class Graph extends JPanel implements MouseListener, MouseMotionListener {

		Graphics2D g2d;

		// Association map contains association, i.e., which operation can
		// follow which one
		HashMap<String, ArrayList<String>> associationMap;

		// Inputs given to a operation is are saved in some category so that
		// they can be suggested in other operations

		HashMap<String, LinkedHashSet<String>> inputMap;

		final static String DB_NAME = "DB Name:";
		final static String DB_USER_NAME = "DB User Name:";
		final static String DB_USER_PASSWORD = "DB User Passoword:";
		final static String BASE_IRI = "Base IRI:";
		final static String FILE_PATH = "File Location:";
		final static String FILE_NAME = "File Name:";

		final static String KEY_WORD = "Key Word:";
		final static String DBPEDIA_DATA_FILE = "DBpedia Data File:";
		final static String RESOURCE_FILE = "Resource File:";
		final static String PROPERTY_WEIGHT_FILE = "Property Weight File:";
		final static String SEMANTIC_BAG_FILE = "Semantic Bag File:";
		final static String RDF_FILE = "RDF File:";
		final static String MAPPING_GRAPH_FILE = "Mapping Graph File:";
		final static String TBox_FILE = "TBox File:";

		boolean isDragged = false;

		Operation selectedOperation;
		String dbName, dbUserName, dbUserPassword, r2rmlPath, r2rmlMappingFileName, tBoxPath, tBoxName;

		public Graph() {

			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			setLayout(new BorderLayout(0, 0));
			allOperations = new ArrayList<>();
			allArrows = new ArrayList<>();
			associationMap = getAssociationMap();
			inputMap = getInitializedInputMap();

		}

		public void refreshPanel() {

			desectAllPaletteButton();
			repaint();

		}

		public void clearPanel() {
			int option = JOptionPane.showConfirmDialog(null, "Are you sure?\nThis will clear all the ETL Flow.",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			if (option == 0) {

				allOperations = new ArrayList<>();
				allArrows = new ArrayList<>();
				repaint();

			}
		}

		// initilize the input map
		private HashMap<String, LinkedHashSet<String>> getInitializedInputMap() {

			HashMap<String, LinkedHashSet<String>> inputMap = new HashMap<>();

			LinkedHashSet<String> stringList = new LinkedHashSet<>();
			inputMap.put(DB_NAME, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(DB_USER_NAME, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(DB_USER_PASSWORD, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(BASE_IRI, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(FILE_PATH, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(FILE_NAME, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(MAPPING_GRAPH_FILE, stringList);
			
			stringList = new LinkedHashSet<>();
			inputMap.put(RDF_FILE, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(TBox_FILE, stringList);

			
			// Params for for linking

			stringList = new LinkedHashSet<>();
			inputMap.put(KEY_WORD, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(DBPEDIA_DATA_FILE, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(RESOURCE_FILE, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(PROPERTY_WEIGHT_FILE, stringList);

			stringList = new LinkedHashSet<>();
			inputMap.put(SEMANTIC_BAG_FILE, stringList);

			
			return inputMap;
		}

		private HashMap<String, ArrayList<String>> getAssociationMap() {

			// This method returns the map containing the association between
			// the operations

			HashMap<String, ArrayList<String>> associations = new HashMap<>();

			// This array list contains the operation that can be done after the
			// particular operation

			// Association for Start
			ArrayList<String> association = new ArrayList<>();
			association.add(EXT_SPARQL);
			association.add(MAPPING_GEN);
			association.add(ABOX_GEN);
			association.add(TBOX_GEN);
			association.add(RESOURCE_RETRIEVER);
			association.add(PWEIGHT_GENERATOR);
			association.add(SBAG_GENERATOR);
			association.add(MATCHER);
			association.add(LOADER);
			
			associations.put(START, association);
			
			
			//Association for ExtSPARQL
			association = new ArrayList<>();
			association.add(MAPPING_GEN);
			associations.put(EXT_SPARQL, association);

			// Association for Mapping generation
			association = new ArrayList<>();
			association.add(ABOX_GEN);
			association.add(TBOX_GEN);
			associations.put(MAPPING_GEN, association);

			// Association for Abox gen
			association = new ArrayList<>();
			association.add(RESOURCE_RETRIEVER);
			association.add(PWEIGHT_GENERATOR);
			association.add(SBAG_GENERATOR);
			association.add(MATCHER);
			association.add(LOADER);
			association.add(TBOX_GEN);
			associations.put(ABOX_GEN, association);

			// Association for TBox Gen
			association = new ArrayList<>();
			association.add(RESOURCE_RETRIEVER);
			association.add(PWEIGHT_GENERATOR);
			association.add(SBAG_GENERATOR);
			association.add(MATCHER);
			association.add(LOADER);
			association.add(ABOX_GEN);
			associations.put(TBOX_GEN, association);

			// Association for Resource Retriever
			association = new ArrayList<>();
			association.add(PWEIGHT_GENERATOR);
			association.add(SBAG_GENERATOR);
			association.add(MATCHER);
			association.add(LOADER);
			associations.put(RESOURCE_RETRIEVER, association);

			// Association for PWeight Generator

			association = new ArrayList<>();
			association.add(SBAG_GENERATOR);
			association.add(MATCHER);
			association.add(LOADER);
			associations.put(PWEIGHT_GENERATOR, association);

			// Association for SBag Generator

			association = new ArrayList<>();
			association.add(MATCHER);
			association.add(LOADER);
			associations.put(SBAG_GENERATOR, association);

			// Association for Matcher
			association = new ArrayList<>();
			association.add(LOADER);
			associations.put(MATCHER, association);

			// Association for Loading
			association = new ArrayList<>();
			associations.put(LOADER, association);

			return associations;
		}

		public void paintComponent(Graphics g) {

			// System.out.println("Paint Called");
			g2d = (Graphics2D) g;
			this.removeAll();
			super.paintComponent(g);
			// this.updateUI();

			// For first time initialization of panel
			if (allOperations.size() <= 0) {

				Operation startOperation = new Operation("Start", 50, 300);
				startOperation.setInputStatus(true);
				startOperation.setOperationShape(
						drawStart(g2d, startOperation.getUpperLeftX(), startOperation.getUpperLeftY()));

				allOperations.add(startOperation);
			} else {

				for (Operation operation : allOperations) {

					String operationName = operation.getOperationName();

					if (operationName.equals("Start")) {
						Shape newShape = drawStart(g2d, operation.getUpperLeftX(), operation.getUpperLeftY());
						operation.setOperationShape(newShape);

					} else {

						Shape oldShape = operation.getOperationShape();

						Color rectColor;

						if (operation.isInputStatus()) {
							rectColor = Color.decode("#32CD32");
						} else {
							rectColor = Color.RED;
						}

						Shape newShape = drawRoundRectangle(g2d, operation.getUpperLeftX(), operation.getUpperLeftY(),
								operationName, rectColor);
						operation.setOperationShape(newShape);
					}
				}
			}

			for (Arrow arrow : allArrows) {

				// draw the arrows

				ArrayList<Point> drawPoints = getDrawPoints(arrow.getSourceOperation().getOperationShape(),
						arrow.getTargetOperation().getOperationShape());

				Point startPoint = drawPoints.get(0);
				Point endPoint = drawPoints.get(1);

				ArrayList<Shape> arrowSeg = drawArrow(g2d, startPoint.x, startPoint.y, endPoint.x, endPoint.y,
						Color.BLACK);

				arrow.setArrowSegments(arrowSeg);
			}

		}

		private Shape drawStart(Graphics2D graphics2d, double d, double e) {

			Ellipse2D startShape = new Ellipse2D.Double(d, e, 80, 80);

			graphics2d.setStroke(new BasicStroke(4));
			graphics2d.setColor(Color.BLACK);
			graphics2d.draw(startShape);
			Ellipse2D startShapeFill = new Ellipse2D.Double(d + 2, e + 2, 76, 76);
			graphics2d.setColor(Color.GREEN);
			graphics2d.fill(startShapeFill);
			graphics2d.setColor(Color.BLACK);

			Font font = new Font("Tahoma", Font.BOLD, 18);
			graphics2d.setFont(font);

			FontMetrics fontMetrics = graphics2d.getFontMetrics();

			// Determine the X coordinate for the text
			int xCor = (int) (startShape.getX() + (startShape.getWidth() - fontMetrics.stringWidth("Start")) / 2);
			// Determine the Y coordinate for the text (note we add the ascent,
			// as
			// in java 2d 0 is top of the screen)
			int yCor = (int) (startShape.getY() + ((startShape.getHeight() - fontMetrics.getHeight()) / 2)
					+ fontMetrics.getAscent());
			// Draw the String
			graphics2d.drawString("Start", xCor, yCor);

			return startShape;
		}

		private ArrayList<Shape> drawArrow(Graphics2D graphics2d, int startX, int startY, int endX, int endY,
				Color color) {

			// contains the segments of an arrow
			ArrayList<Shape> arrowSegments = new ArrayList<>();

			graphics2d.setStroke(new BasicStroke(3));
			graphics2d.setColor(color);
			// temp variable to store line segments
			Line2D tempLine2d;

			if (startX == endX && startY < endY) {
				// Vertical Down

				if (endY - startY > 10) {

					tempLine2d = new Line2D.Double(startX, startY, endX, endY - 10);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX - 5, endY - 10);
					arrowHead.addPoint(startX + 5, endY - 10);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {

					// lowDistance, just draw the arrow head

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX - 5, startY);
					arrowHead.addPoint(startX + 5, startY);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			} else if (startX == endX && startY > endY) {
				// vertical Up

				if (startY - endY > 10) {

					tempLine2d = new Line2D.Double(startX, startY, endX, endY + 10);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX - 5, endY + 10);
					arrowHead.addPoint(startX + 5, endY + 10);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {

					// lowDistance, just draw the arrow head

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX - 5, startY);
					arrowHead.addPoint(startX + 5, startY);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			} else if (startY == endY && startX < endX) {
				// Horizontal right

				if (endX - startX > 10) {

					tempLine2d = new Line2D.Double(startX, startY, endX - 10, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(endX - 10, endY - 5);
					arrowHead.addPoint(endX - 10, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {

					// lowDistance, just draw the arrow head

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX, startY - 5);
					arrowHead.addPoint(startX, startY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			} else if (startY == endY && startX > endX) {
				// Horizontal left

				if (startX - endX > 10) {

					tempLine2d = new Line2D.Double(startX, startY, endX + 10, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX - 5, startY);
					arrowHead.addPoint(startX + 5, startY);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {

					// lowDistance, just draw the arrow head

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX, startY - 5);
					arrowHead.addPoint(startX, startY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			} else if (startY < endY && startX > endX) {
				// Lower Left

				int distance = startX - endX;
				if (distance >= 12) {

					int movePos = (distance - 10) / 2;

					tempLine2d = new Line2D.Double(startX, startY, startX - movePos, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - movePos, startY, startX - movePos, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - movePos, endY, endX + 10, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(endX + 10, endY - 5);
					arrowHead.addPoint(endX + 10, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {
					// not enough distance

					tempLine2d = new Line2D.Double(startX, startY, startX - 1, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - 1, startY, startX - 1, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - 1, endY, startX - 2, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX - 2, endY - 5);
					arrowHead.addPoint(startX - 2, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			} else if (startY < endY && startX < endX) {
				// Lower right

				int distance = endX - startX;
				if (distance >= 12) {

					int movePos = (distance - 10) / 2;

					tempLine2d = new Line2D.Double(startX, startY, startX + movePos, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + movePos, startY, startX + movePos, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + movePos, endY, endX - 10, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(endX - 10, endY - 5);
					arrowHead.addPoint(endX - 10, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {
					// not enough distance

					tempLine2d = new Line2D.Double(startX, startY, startX + 1, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + 1, startY, startX + 1, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + 1, endY, startX + 2, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX + 2, endY - 5);
					arrowHead.addPoint(startX + 2, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			} else if (startY > endY && startX > endX) {
				// Upper Left

				int distance = startX - endX;
				if (distance >= 12) {

					int movePos = (distance - 10) / 2;

					tempLine2d = new Line2D.Double(startX, startY, startX - movePos, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - movePos, startY, startX - movePos, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - movePos, endY, endX + 10, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(endX + 10, endY - 5);
					arrowHead.addPoint(endX + 10, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {
					// not enough distance

					tempLine2d = new Line2D.Double(startX, startY, startX - 1, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - 1, startY, startX - 1, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX - 1, endY, startX - 2, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX - 2, endY - 5);
					arrowHead.addPoint(startX - 2, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			} else if (startY > endY && startX < endX) {
				// Upper right

				int distance = endX - startX;
				if (distance >= 12) {

					int movePos = (distance - 10) / 2;

					tempLine2d = new Line2D.Double(startX, startY, startX + movePos, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + movePos, startY, startX + movePos, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + movePos, endY, endX - 10, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(endX - 10, endY - 5);
					arrowHead.addPoint(endX - 10, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				} else {
					// not enough distance

					tempLine2d = new Line2D.Double(startX, startY, startX + 1, startY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + 1, startY, startX + 1, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					tempLine2d = new Line2D.Double(startX + 1, endY, startX + 2, endY);
					graphics2d.draw(tempLine2d);
					arrowSegments.add(tempLine2d);

					Polygon arrowHead = new Polygon();
					arrowHead.addPoint(startX + 2, endY - 5);
					arrowHead.addPoint(startX + 2, endY + 5);
					arrowHead.addPoint(endX, endY);
					graphics2d.fill(arrowHead);
					arrowSegments.add(arrowHead);

				}

			}
			return arrowSegments;
		}

		private Operation getSelectedOperation(int x, int y) {

			Shape selectedShape = null;
			for (Operation operation : allOperations) {
				selectedShape = operation.getOperationShape();
				if (selectedShape.contains(x, y))
					break;
			}

			for (Operation operation : allOperations) {

				Shape shape = operation.getOperationShape();

				if (shape == selectedShape) {
					return operation;
				}

			}

			return null;
		}

		private RoundRectangle2D drawRoundRectangle(Graphics2D graphics2d, double x, double y, String text,
				Color color) {

			graphics2d.setColor(color);
			graphics2d.setStroke(new BasicStroke(3));

			// graphics2d.setColor(Color.RED);

			// Default Height = 100, default width = 200
			RoundRectangle2D rectangle = new RoundRectangle2D.Double(x, y, 200, 100, 10, 10);
			graphics2d.draw(rectangle);

			Font font = new Font("Tahoma", Font.BOLD, 16);
			graphics2d.setFont(font);

			FontMetrics fontMetrics = graphics2d.getFontMetrics();

			// Determine the X coordinate for the text
			int xCor = (int) (rectangle.getX() + (rectangle.getWidth() - fontMetrics.stringWidth(text)) / 2);
			// Determine the Y coordinate for the text (note we add the ascent,
			// as
			// in java 2d 0 is top of the screen)
			int yCor = (int) (rectangle.getY() + ((rectangle.getHeight() - fontMetrics.getHeight()) / 2)
					+ fontMetrics.getAscent());
			// Draw the String
			graphics2d.setColor(Color.BLACK);
			graphics2d.drawString(text, xCor, yCor);

			return rectangle;

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {

			if (arg0.getClickCount() == 2) {

				if (SwingUtilities.isLeftMouseButton(arg0)) {

					if (selectedButton != null) {

						String selectedOpName = "";

						if (selectedButton.getIcon() != null) {
							selectedOpName = "Association";
						} else {
							selectedOpName = selectedButton.getText().toString();
						}

						if (!selectedOpName.equals("Association")) {
							Operation newOperation = new Operation(selectedOpName, arg0.getX(), arg0.getY());
							setOperation(newOperation);
							allOperations.add(newOperation);
							desectAllPaletteButton();
							repaint();
						}
					} else {

						Operation operation = getSelectedOperation(arg0.getX(), arg0.getY());

						if (!operation.getOperationName().equals("Start")) {
							// initialize the input file list
							//setInputFileList(operation.getEtlOperation());
							// take input

							boolean isInputTaken = operation.getEtlOperation().getInput(this, inputMap);

							if (isInputTaken) {
								if (!operation.isInputStatus()) {
									operation.setInputStatus(isInputTaken);
									repaint();
								}
							}

						}

					}

				} else {

					if (SwingUtilities.isRightMouseButton(arg0)) {

						boolean found = false;
						int option = -1;
						ArrayList<Integer> arrowIndexes = new ArrayList<>();
						int operationIndex = -1;

						for (Operation operation : allOperations) {

							Shape shape = operation.getOperationShape();

							if (shape.contains(arg0.getX(), arg0.getY())) {

								found = true;
								option = JOptionPane.showConfirmDialog(null,
										"Are you sure to delete this " + operation.getOperationName()
												+ " operation?\nAll association to this operation also will be deleted.",
										"Confirmation", JOptionPane.YES_NO_OPTION);
								if (option == 0) {
									for (Arrow arrow : allArrows) {
										if (arrow.targetOperation == operation || arrow.sourceOperation == operation) {
											arrowIndexes.add(allArrows.indexOf(arrow));
										}
									}
									operationIndex = allOperations.indexOf(operation);
									break;
								}
							}
						}

						if (found) {

							if (option == 0) {

								for (int index : arrowIndexes) {

									allArrows.remove(index);
								}
								allOperations.remove(operationIndex);
								repaint();
								return;

							}

						} else {

							int arrowIndex = -1;
							for (Arrow arrow : allArrows) {

								ArrayList<Shape> arrowSegments = arrow.getArrowSegments();

								for (Shape shape : arrowSegments) {
									if (shape.contains(arg0.getX(), arg0.getY())) {

										found = true;
										break;
									}
								}

								arrowIndex = allArrows.indexOf(arrow);
								if (found)
									break;

							}

							if (found) {

								int op = JOptionPane.showConfirmDialog(null,
										"Are you sure to delete association between \n"
												+ allArrows.get(arrowIndex).getSourceOperation().getOperationName()
												+ " and "
												+ allArrows.get(arrowIndex).getTargetOperation().getOperationName(),
										"Confirmation", JOptionPane.YES_NO_OPTION);

								if (op == 0) {

									allArrows.remove(arrowIndex);
									repaint();
									return;

								}

							}
						}

					}

				}

			} else {

				if (SwingUtilities.isLeftMouseButton(arg0)) {

					for (Operation operation : allOperations) {

						Shape shape = operation.getOperationShape();

						if (shape.contains(arg0.getX(), arg0.getY())) {
							break;
						}
					}

					for (Arrow arrow : allArrows) {

						boolean found = false;
						ArrayList<Shape> arrowSegments = arrow.getArrowSegments();

						for (Shape shape : arrowSegments) {
							if (shape.contains(arg0.getX(), arg0.getY())) {

								found = true;
								break;
							}
						}

						if (found)
							break;

					}
				}

			}

			if (selectedButton != null) {
				if (SwingUtilities.isLeftMouseButton(arg0)) {
					String selectedOpName = "";

					if (selectedButton.getIcon() != null) {
						selectedOpName = "Association";
					} else {
						selectedOpName = selectedButton.getText().toString();
					}
				}
			}

		}

//		private boolean setInputFileList(ETLOperation etlOperation) {
//
//			if (etlOperation instanceof ETLABoxGenOperation) {
//
//				((ETLABoxGenOperation) etlOperation).setMappingGraphFileList(getMappingGraphFiles());
//
//			} else if (etlOperation instanceof ETLLoadingOperation) {
//
//				((ETLLoadingOperation) etlOperation).setLoadingRDFFileList(getRDFFiles());
//
//			} else if (etlOperation instanceof ETLLinkingOperation) {
//
//				((ETLLinkingOperation) etlOperation).setLinkingRDFFileList(getRDFFiles());
//
//			}
//
//			return true;
//		}

		// return the path of all mapping graph output files
		private ArrayList<String> getMappingGraphFiles() {

			ArrayList<String> mappingGraphFilesPath = new ArrayList<>();
			for (Operation operation : allOperations) {

				if (operation.getOperationName().equals("Mapping Generation")) {

					ETLMappingGenOperation mapGenOperation = (ETLMappingGenOperation) operation.getEtlOperation();
					mappingGraphFilesPath.add(mapGenOperation.getFileSavingPath());
				}
			}

			return mappingGraphFilesPath;
		}

		// return the path of all RDF output Files
		private ArrayList<String> getRDFFiles() {

			ArrayList<String> rdfFilesPath = new ArrayList<>();
			for (Operation operation : allOperations) {

				if (operation.getOperationName().equals("ABox Generation")) {

					ETLABoxGenOperation aBoxGenOperation = (ETLABoxGenOperation) operation.getEtlOperation();
					rdfFilesPath.add(aBoxGenOperation.getFileSavingPath());
				}

			}

			return rdfFilesPath;
		}

		private boolean setOperation(Operation operation) {

			String operationName = operation.getOperationName();
			boolean isMatched = false;
			switch (operationName) {
			
			case EXT_SPARQL:
				operation.setEtlOperation(new ETLExtractionSPARQL());
				isMatched=true;
				break;
				
			case MAPPING_GEN:
				operation.setEtlOperation(new ETLMappingGenOperation());
				isMatched = true;
				break;
			case TBOX_GEN:
				operation.setEtlOperation(new ETLTBoxGenOperation());
				isMatched = true;
				break;
			case ABOX_GEN:
				operation.setEtlOperation(new ETLABoxGenOperation());
				isMatched = true;
				break;
			case LOADER:
				operation.setEtlOperation(new ETLLoadingOperation());
				isMatched = true;
				break;

			case RESOURCE_RETRIEVER:
				operation.setEtlOperation(new ETLResourceRetreiver());
				isMatched = true;
				break;

			case PWEIGHT_GENERATOR:
				operation.setEtlOperation(new ETLPWeightGenerator());
				isMatched = true;
				break;

			case SBAG_GENERATOR:
				operation.setEtlOperation(new ETLSBagGenerator());
				isMatched = true;
				break;

			case MATCHER:
				operation.setEtlOperation(new ETLMatcher());
				isMatched = true;
				break;

			}

			if (isMatched)
				return true;
			else
				return false;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			// System.out.println("Entered");
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			// System.out.println("Exited");

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// System.out.println("Pressed");

			if (SwingUtilities.isLeftMouseButton(e)) {
				selectedOperation = getSelectedOperation(e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// System.out.println("Released");

			if (SwingUtilities.isLeftMouseButton(e)) {
				String selectedOpName = "";

				if (selectedButton != null) {

					if (selectedButton.getIcon() != null) {
						selectedOpName = "Association";
					} else {
						selectedOpName = selectedButton.getText().toString();
					}

				}

				if (isDragged && selectedOpName.equals("Association")) {

					int x = e.getX();
					int y = e.getY();

					for (Operation operation : allOperations) {

						Shape shape = operation.getOperationShape();

						if (shape.contains(x, y) && selectedOperation != getSelectedOperation(e.getX(), e.getY())) {

							ArrayList<String> associationList = associationMap
									.get(selectedOperation.getOperationName().toString());

							if (associationList != null && associationList.contains(operation.getOperationName())) {

								selectedOperation.getNextOperations().add(operation);
								Arrow arrow = new Arrow(selectedOperation, operation);
								allArrows.add(arrow);
								repaint();
								isDragged = false;

								desectAllPaletteButton();

								break;

							} else {

								System.out.println("Association List = " + associationList);
								System.out.println(operation.getOperationName());
								JOptionPane.showMessageDialog(null,
										"Association from " + selectedOperation.getOperationName() + " to "
												+ operation.getOperationName() + " is invalid!");
								break;
							}
						}

					}

				}

				selectedOperation = null;
				isDragged = false;

			}

		}

		@Override
		public void mouseDragged(MouseEvent arg0) {

			if (SwingUtilities.isLeftMouseButton(arg0)) {

				isDragged = true;

				if (selectedOperation != null && selectedButton == null) {
					selectedOperation.setUpperLeftX(arg0.getX());
					selectedOperation.setUpperLeftY(arg0.getY());

					repaint();
				}
			}

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {

		}

		private Operation getOperation(Shape shape) {

			for (Operation operation : allOperations) {

				if (operation.getOperationShape() == shape) {
					return operation;
				}
			}
			return null;
		}

		private ArrayList<Point> getDrawPoints(Shape sourceShape, Shape targetShape) {

			ArrayList<Point> points = new ArrayList<>();

			Point pointSource = new Point(0, 0);
			Point pointDestination = new Point(0, 0);

			if (sourceShape instanceof Ellipse2D) {

				Ellipse2D sourceEllipse = (Ellipse2D) sourceShape;

				double sourceMinX = sourceEllipse.getMinX();
				double sourceMinY = sourceEllipse.getMinY();
				double sourceMaxX = sourceEllipse.getMaxX();
				double sourceMaxY = sourceEllipse.getMaxY();

				if (targetShape instanceof RoundRectangle2D) {

					RoundRectangle2D targetRectange = (RoundRectangle2D) targetShape;

					double targetMinX = targetRectange.getMinX();
					double targetMinY = targetRectange.getMinY();
					double targetMaxX = targetRectange.getMaxX();
					double targetMaxY = targetRectange.getMaxY();

					if (targetMinX > sourceMaxX) {
						// perfect Right to the source

						// target points
						pointDestination.x = (int) targetRectange.getMinX();
						pointDestination.y = (int) (targetMinY + (targetRectange.getHeight() / 2));

						// System.out.println("Right of the source");
						pointSource.x = (int) sourceEllipse.getMaxX();
						pointSource.y = (int) (sourceEllipse.getMinY() + (sourceEllipse.getHeight() / 2));

					} else if (targetMaxX < sourceMinX) {
						// perfect left to the source

						// target points
						pointDestination.x = (int) targetRectange.getMaxX();
						pointDestination.y = (int) (targetMinY + (targetRectange.getHeight() / 2));

						// System.out.println("Left of the Source");
						pointSource.x = (int) sourceEllipse.getMinX();
						pointSource.y = (int) (sourceEllipse.getMinY() + (sourceEllipse.getHeight() / 2));

					} else {

						pointSource.x = (int) sourceMinX;
						pointSource.y = (int) (sourceMinY + (sourceEllipse.getHeight() / 2));

						pointDestination.x = (int) targetMinX;
						pointDestination.y = (int) (targetMinY + (targetRectange.getHeight() / 2));
					}
				}

			} else {

				// Source is a rectangle

				RoundRectangle2D sourceRectengle = (RoundRectangle2D) sourceShape;
				RoundRectangle2D targetRectangle = (RoundRectangle2D) targetShape;

				double sourceMinX = sourceRectengle.getMinX();
				double sourceMinY = sourceRectengle.getMinY();
				double sourceMaxX = sourceRectengle.getMaxX();
				double sourceMaxY = sourceRectengle.getMaxY();

				double targetMinX = targetRectangle.getMinX();
				double targetMinY = targetRectangle.getMinY();
				double targetMaxX = targetRectangle.getMaxX();
				double targetMaxY = targetRectangle.getMaxY();

				if (targetMinX >= sourceMaxX) {
					// right

					pointSource.x = (int) sourceMaxX;
					pointSource.y = (int) (sourceMinY + (sourceRectengle.getHeight() / 2));

					pointDestination.x = (int) targetMinX;
					pointDestination.y = (int) (targetMinY + (targetRectangle.getHeight() / 2));

				} else if (targetMaxX < sourceMinX) {
					// left

					pointSource.x = (int) sourceMinX;
					pointSource.y = (int) (sourceMinY + (sourceRectengle.getHeight() / 2));

					pointDestination.x = (int) targetMaxX;
					pointDestination.y = (int) (targetMinY + (targetRectangle.getHeight() / 2));

				} else {

					if (targetMaxY <= sourceMinY || targetMaxY <= sourceMaxY) {
						// top

						pointSource.y = (int) sourceMinY;
						pointDestination.y = (int) targetMaxY;

						int commonX = getCommonX(sourceMinX, sourceMaxX, targetMinX, targetMaxX);

						pointSource.x = commonX;
						pointDestination.x = commonX;

					} else {

						// bottom
						pointSource.y = (int) sourceMaxY;
						pointDestination.y = (int) targetMinY;

						int commonX = getCommonX(sourceMinX, sourceMaxX, targetMinX, targetMaxX);

						pointSource.x = commonX;
						pointDestination.x = commonX;

					}
				}
			}
			points.add(pointSource);
			points.add(pointDestination);
			return points;
		}

		private int getCommonX(double line1MinX, double line1MaxX, double line2MinX, double line2MaxX) {

			if (line1MinX == line2MinX && line1MaxX == line2MaxX) {
				return (int) (line1MinX + ((line1MaxX - line1MinX) / 2));
			} else {

				double line1Length = line1MaxX - line1MinX;
				double line2Length = line2MaxX - line2MinX;

				if (line1MinX < line2MinX) {
					// line 1 start left

					double commonX1 = line2MinX;
					double commonX2;
					if (line1Length <= line2Length) {

						commonX2 = commonX1 + (line1Length - (line2MinX - line1MinX));

					} else {

						commonX2 = commonX1 + (line2Length - (line2MinX - line1MinX));
					}

					return (int) (line2MinX + Math.ceil((commonX2 - commonX1) / 2));

				} else {
					// line 2 start left

					double commonX1 = line1MinX;
					double commonX2;
					if (line1Length <= line2Length) {

						commonX2 = commonX1 + (line1Length - (line1MinX - line2MinX));

					} else {

						commonX2 = commonX1 + (line2Length - (line1MinX - line2MinX));
					}

					return (int) (line1MinX + Math.ceil((commonX2 - commonX1) / 2));
				}

			}

		}

		// return where to draw arrow
		private Point getDrawPoint(Shape shape) {

			Point tempPoint = new Point(-1, -1);

			if (shape instanceof RoundRectangle2D) {

				// System.out.println("Shape is a rectengle");
				RoundRectangle2D tempRect = (RoundRectangle2D) shape;
				tempPoint.x = (int) tempRect.getMinX();

				tempPoint.y = (int) (tempRect.getMinY() + (tempRect.getHeight() / 2));

			} else if (shape instanceof Ellipse2D) {

				Ellipse2D tempEllipse = (Ellipse2D) shape;
				tempPoint.x = (int) tempEllipse.getMaxX();
				tempPoint.y = (int) (tempEllipse.getMinY() + (tempEllipse.getHeight() / 2));
			}

			return tempPoint;
		}

	}

	class Operation {

		// This class represents an activity in the ETL flow

		String operationName;
		Shape operationShape;
		int upperLeftX, upperLeftY;
		ETLOperation etlOperation;
		ArrayList<Operation> nextOperations;

		// flag to check whether all input to the operation is given
		boolean inputStatus, isExecuted;

		public Operation(String operationName, int upperLeftX, int upperLeftY) {

			this.operationName = operationName;
			this.upperLeftX = upperLeftX;
			this.upperLeftY = upperLeftY;

			nextOperations = new ArrayList<>();
			inputStatus = false;
			isExecuted = false;
		}

		public boolean isInputStatus() {
			return inputStatus;
		}

		public void setInputStatus(boolean inputStatus) {
			this.inputStatus = inputStatus;
		}

		public boolean isExecuted() {
			return isExecuted;
		}

		public void setExecuted(boolean isExecuted) {
			this.isExecuted = isExecuted;
		}

		public int getUpperLeftX() {
			return upperLeftX;
		}

		public void setUpperLeftX(int upperLeftX) {
			this.upperLeftX = upperLeftX;
		}

		public int getUpperLeftY() {
			return upperLeftY;
		}

		public void setUpperLeftY(int upperLeftY) {
			this.upperLeftY = upperLeftY;
		}

		public String getOperationName() {
			return operationName;
		}

		public void setOperationName(String operationName) {
			this.operationName = operationName;
		}

		public Shape getOperationShape() {
			return operationShape;
		}

		public void setOperationShape(Shape operationShape) {
			this.operationShape = operationShape;
		}

		public ArrayList<Operation> getNextOperations() {
			return nextOperations;
		}

		public void setNextOperations(ArrayList<Operation> nextOperations) {
			this.nextOperations = nextOperations;
		}

		public ETLOperation getEtlOperation() {
			return etlOperation;
		}

		public void setEtlOperation(ETLOperation etlOperation) {
			this.etlOperation = etlOperation;
		}

	}

	// contains data and method of arrow
	class Arrow {

		Operation sourceOperation, targetOperation;
		ArrayList<Shape> arrowSegments;

		public Arrow(Operation sourceOperation, Operation targetOperation) {
			super();
			this.sourceOperation = sourceOperation;
			this.targetOperation = targetOperation;
			this.arrowSegments = new ArrayList<>();
		}

		private boolean containsPoint(Double x, Double y) {

			for (Shape segment : arrowSegments) {
				if (segment.contains(x, y)) {
					return true;
				}
			}
			return false;
		}

		public Operation getSourceOperation() {
			return sourceOperation;
		}

		public void setSourceOperation(Operation sourceOperation) {
			this.sourceOperation = sourceOperation;
		}

		public Operation getTargetOperation() {
			return targetOperation;
		}

		public void setTargetOperation(Operation targetOperation) {
			this.targetOperation = targetOperation;
		}

		public ArrayList<Shape> getArrowSegments() {
			return arrowSegments;
		}

		public void setArrowSegments(ArrayList<Shape> arrowSegments) {
			this.arrowSegments = arrowSegments;
		}

	}

	class ETLSparqlQuery extends JPanel {

		JTextArea textAreaQueryResult, textAreaSparqlQuery;
		Model tripleModel, tempModel;

		JLabel lblQueryResult;

		public ETLSparqlQuery(ArrayList<String> triplePaths) {
			setLayout(new BorderLayout(5, 5));

			JPanel panelButton = new JPanel();
			add(panelButton, BorderLayout.SOUTH);
			JButton buttonETL = new JButton("< ETL");
			buttonETL.setFont(new Font("Tahoma", Font.BOLD, 16));
			panelButton.add(buttonETL);
			buttonETL.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					setVisible(false);
					graphPanel.setVisible(true);
					panelETLButtons.setVisible(true);
					textPaneETLStatus.setText("ETL STATUS:");
					panelComponentPalette.setVisible(true);

				}
			});

			JPanel panelTop = new JPanel();
			add(panelTop, BorderLayout.NORTH);
			GridBagLayout gbl_panelTop = new GridBagLayout();

			gbl_panelTop.columnWeights = new double[] { 0.0, 1.0, 0.0 };
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

			JButton btnRunQuery = new JButton("Run");
			btnRunQuery.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnRunQuery.addActionListener(new ActionListener() {
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

						ArrayList<String> selectedVars = (ArrayList<String>) resultSet.getResultVars();
						ArrayList<String> resultStringList = new ArrayList<>();

						while (resultSet.hasNext()) {

							QuerySolution querySolution = resultSet.next();

							String resultString = "";
							for (String selectedVar : selectedVars) {

								resultString += querySolution.get(selectedVar).asNode().toString() + "\t";

							}
							resultStringList.add(resultString);
						}
						qe.close();

						String resultString = "";
						int numOfResultString = resultStringList.size();
						for (int i = numOfResultString - 1; i >= 0; i--) {
							resultString += resultStringList.get(i) + "\n";
						}

						textAreaQueryResult.setText(resultString);

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error in query!");
						e.printStackTrace();
					}
				}

			});
			GridBagConstraints gbc_btnRunQuery = new GridBagConstraints();
			gbc_btnRunQuery.insets = new Insets(0, 5, 5, 20);
			gbc_btnRunQuery.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnRunQuery.gridx = 2;
			gbc_btnRunQuery.gridy = 0;
			panelTop.add(btnRunQuery, gbc_btnRunQuery);

			JPanel panelContent = new JPanel();
			add(panelContent, BorderLayout.CENTER);
			panelContent.setLayout(new BorderLayout(0, 0));

			lblQueryResult = new JLabel(" Query Result");
			lblQueryResult.setFont(new Font("Tahoma", Font.PLAIN, 16));
			panelContent.add(lblQueryResult, BorderLayout.NORTH);

			JScrollPane scrollPaneQueryResult = new JScrollPane();
			panelContent.add(scrollPaneQueryResult, BorderLayout.CENTER);

			textAreaQueryResult = new JTextArea();
			textAreaQueryResult.setEditable(false);
			textAreaQueryResult.setFont(new Font("Monospaced", Font.PLAIN, 16));
			scrollPaneQueryResult.setViewportView(textAreaQueryResult);

			tripleModel = ModelFactory.createDefaultModel();
			tempModel = ModelFactory.createDefaultModel();

			for (String triplePath : triplePaths) {

				tempModel.read(triplePath);
				tripleModel = ModelFactory.createUnion(tripleModel, tempModel);

				textPaneETLStatus.setText(textPaneETLStatus.getText().toString() + "\nLoaded: " + triplePath);
			}

		}

	}
}
