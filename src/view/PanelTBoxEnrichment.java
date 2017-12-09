package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import model.CommonMethods;
import model.Pair;
import model.PropertyKey;
import model.TBoxMethods;
import model.TBoxModel;
import net.miginfocom.swing.MigLayout;
import store.Visual;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.jena.sparql.function.library.substring;
import org.apache.jena.tdb.base.objectfile.StringFile;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.omg.CosNaming._BindingIteratorImplBase;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.DefaultComboBoxModel;

public class PanelTBoxEnrichment extends JPanel {

	/**
	 * Create the panel.
	 */

	private TBoxMethods tBoxMethods;
	private String tBoxPath, tboxName;
	private JLabel labelTBoxName;

	private TBoxModel tBoxModel;
	private JTree treeTBox;

	private ArrayList<String> owlPropertyList;
	private ArrayList<String> conceptList;
	private ArrayList<String> propertyList;
	// private HashMap<String, String> prefixMap;

	// temp panel variables
	private JTextField textFieldHead;
	private JTextField txtFieldIri;
	
	private JPanel panelGraph;

	private JComboBox comboBoxConceptSubject, comboBoxPrefixList, comboBoxConceptOWL, comboBoxConceptObject,
			comboBoxPropertySubject, comboBoxPropertyOWL, comboBoxPropertyObject;

	private DefaultComboBoxModel conceptModel, propertyModel, conceptObjectModel, propertyObjectModel;
	private JButton btnPrefix, btnConceptAdd, btnPropertyAdd;
	private JButton btnNewTbox;

	public PanelTBoxEnrichment() {
		setLayout(new BorderLayout(10, 5));

		JPanel panelHerrarchi = new JPanel();
		add(panelHerrarchi, BorderLayout.WEST);
		panelHerrarchi.setLayout(new BorderLayout(0, 0));

		JPanel panelTree = new JPanel();
		panelHerrarchi.add(panelTree, BorderLayout.CENTER);
		GridBagLayout gbl_panelTree = new GridBagLayout();
		gbl_panelTree.columnWidths = new int[] { 340, 0 };
		gbl_panelTree.rowHeights = new int[] { 48, 0 };
		gbl_panelTree.columnWeights = new double[] { 1.0 };
		gbl_panelTree.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelTree.setLayout(gbl_panelTree);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.ipady = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panelTree.add(scrollPane, gbc_scrollPane);

		treeTBox = new JTree();
		scrollPane.setViewportView(treeTBox);

		treeTBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) treeTBox.getCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);
		treeTBox.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				// JOptionPane.showMessageDialog(null, "Item seletcted: " +
				// arg0.getPath());

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeTBox.getLastSelectedPathComponent();
				//JOptionPane.showMessageDialog(null, selectedNode.toString());
				
				String selectedResource = "";
				
				if(selectedNode != null)
					selectedResource= selectedNode.toString();
				
				
				Set<String> prefixes = tBoxModel.getPrefixMap().keySet();
				Iterator iterator = prefixes.iterator();
				
				while(iterator.hasNext()){
					
					String prefix  = (String)iterator.next();
					if(selectedResource.contains(prefix)){
						
						String subjectNode = getNode(selectedResource);
						
						//System.out.println(subjectNode);
						showGraph(subjectNode);
					}
					
				}
				
			}
		});

		treeTBox.setVisible(false);

		JLabel lblTboxHierarchy = new JLabel(" TBox Hierarchy ");
		lblTboxHierarchy.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelHerrarchi.add(lblTboxHierarchy, BorderLayout.NORTH);

		JPanel panelEnrichment = new JPanel();
		panelEnrichment.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(panelEnrichment, BorderLayout.NORTH);
		panelEnrichment.setLayout(new MigLayout("", "[][20][20][grow][]", "[5][][10][][10][][][][10]"));

		JButton buttonOpenTbox = new JButton("Open TBox");
		buttonOpenTbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				boolean success = openButtonHandler();
				if (success) {
					loadOWLCombobox();
					loadGUIComponents();
				}
			}
		});
		buttonOpenTbox.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(buttonOpenTbox, "cell 0 1 2 1,growx");
		
		btnNewTbox = new JButton("New TBox");
		btnNewTbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				boolean flag  = newTBoxButtonHandler();
				
				if(flag){
					loadOWLCombobox();
					loadGUIComponents();
					labelTBoxName.setText(tboxName);
				}
				
			}
		});
		btnNewTbox.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(btnNewTbox, "cell 2 1");

		JLabel label = new JLabel("TBox: ");
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(label, "flowx,cell 3 1");

		JLabel lblTboxEnrichment = new JLabel("Add Annotation");
		lblTboxEnrichment.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelEnrichment.add(lblTboxEnrichment, "cell 0 3");

		JLabel lblPrefixes = new JLabel("IRIs");
		lblPrefixes.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(lblPrefixes, "flowx,cell 1 5");

		comboBoxPrefixList = new JComboBox();
		comboBoxPrefixList.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelEnrichment.add(comboBoxPrefixList, "flowx,cell 3 5");

		JLabel lblConceptEnrichment = new JLabel("Concept Enrichment");
		lblConceptEnrichment.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(lblConceptEnrichment, "flowx,cell 1 6,alignx leading");

		comboBoxConceptSubject = new JComboBox();
		comboBoxConceptSubject.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxConceptSubject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// JOptionPane.showMessageDialog(null,comboBoxConceptObject.getSelectedItem());

				if (isAddNewSelected(comboBoxConceptSubject.getSelectedItem().toString())) {
					addConcept();
				}
			}
		});
		panelEnrichment.add(comboBoxConceptSubject, "flowx,cell 3 6,alignx left");

		JLabel lblPropertyEnrichment = new JLabel("Property Enrichment");
		lblPropertyEnrichment.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(lblPropertyEnrichment, "flowx,cell 1 7");

		comboBoxConceptOWL = new JComboBox();
		comboBoxConceptOWL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxConceptOWL.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// JOptionPane.showMessageDialog(null,comboBoxConceptObject.getSelectedItem());

				if (isAddNewSelected(comboBoxConceptOWL.getSelectedItem().toString())) {
					addOWLProperty();
				}
			}
		});
		panelEnrichment.add(comboBoxConceptOWL, "cell 3 6, gapleft 20");

		comboBoxConceptObject = new JComboBox();
		comboBoxConceptObject.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxConceptObject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// JOptionPane.showMessageDialog(null,comboBoxConceptObject.getSelectedItem());

				if (isAddNewSelected(comboBoxConceptObject.getSelectedItem().toString())) {
					addConcept();
				}
			}
		});

		panelEnrichment.add(comboBoxConceptObject, "cell 3 6, gapleft 20");

		comboBoxPropertySubject = new JComboBox();
		comboBoxPropertySubject.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxPropertySubject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// JOptionPane.showMessageDialog(null,comboBoxConceptObject.getSelectedItem());

				if (isAddNewSelected(comboBoxPropertySubject.getSelectedItem().toString())) {
					addProperty();
				}
			}
		});
		panelEnrichment.add(comboBoxPropertySubject, "flowx,cell 3 7");

		comboBoxPropertyOWL = new JComboBox();
		comboBoxPropertyOWL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxPropertyOWL.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// JOptionPane.showMessageDialog(null,comboBoxConceptObject.getSelectedItem());

				if (isAddNewSelected(comboBoxPropertyOWL.getSelectedItem().toString())) {
					addOWLProperty();
				}
			}
		});

		panelEnrichment.add(comboBoxPropertyOWL, "cell 3 7, gapleft 20");

		comboBoxPropertyObject = new JComboBox();
		comboBoxPropertyObject.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxPropertyObject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// JOptionPane.showMessageDialog(null,comboBoxConceptObject.getSelectedItem());

				if (isAddNewSelected(comboBoxPropertyObject.getSelectedItem().toString())) {
					addProperty();
				}
			}
		});
		panelEnrichment.add(comboBoxPropertyObject, "cell 3 7, gapleft 20");

		btnPrefix = new JButton("+ IRI");
		btnPrefix.setEnabled(false);
		btnPrefix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				addPrefixButtonHandler();
			}
		});
		btnPrefix.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(btnPrefix, "cell 3 5");

		labelTBoxName = new JLabel("None");
		labelTBoxName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelEnrichment.add(labelTBoxName, "cell 3 1");

		btnConceptAdd = new JButton("Add");
		btnConceptAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				addConceptAnnotationButtonHandler();
				
			}
		});
		btnConceptAdd.setEnabled(false);
		btnConceptAdd.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(btnConceptAdd, "cell 3 6, gapleft 30");

		btnPropertyAdd = new JButton("Add");
		btnPropertyAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				addPropertyButtonHandler();
			}
		});
		btnPropertyAdd.setEnabled(false);
		btnPropertyAdd.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelEnrichment.add(btnPropertyAdd, "cell 3 7, gapleft 30");

		JScrollPane scrollPaneGraph = new JScrollPane();
		add(scrollPaneGraph, BorderLayout.CENTER);

		panelGraph = new JPanel();
		scrollPaneGraph.setViewportView(panelGraph);
		panelGraph.setLayout(new BorderLayout(0, 0));

		tBoxMethods = new TBoxMethods();
		tBoxModel = new TBoxModel();

		owlPropertyList = new ArrayList<>();
		conceptList = new ArrayList<>();
		propertyList = new ArrayList<>();
		

	}

	protected boolean newTBoxButtonHandler() {
	
		
		CommonMethods commonMethods = new CommonMethods();
		
		JPanel panelTBoxInputPanel = new JPanel();
		panelTBoxInputPanel.setLayout(new MigLayout("", "[][][grow]", "[][]"));
		
		JLabel lblTboxName = new JLabel("TBox Name: ");
		lblTboxName.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelTBoxInputPanel.add(lblTboxName, "cell 0 0");
		
		JTextField txtTboxname = new JTextField();
		txtTboxname.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelTBoxInputPanel.add(txtTboxname, "cell 2 0,growx");
		txtTboxname.setColumns(35);
		
		JLabel lblDirname = new JLabel("None");
		lblDirname.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelTBoxInputPanel.add(lblDirname, "cell 2 1");
		
		JButton btnDirectory = new JButton("Directory");
		btnDirectory.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelTBoxInputPanel.add(btnDirectory, "cell 0 1");
		btnDirectory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final String path  = commonMethods.getDirectoryPath("Directory for Saving TBox");
				
				if(path!=null && !path.equals("")){
					lblDirname.setText(path);
				}
			}
		});
		
		
		int confirmation = JOptionPane.showConfirmDialog(null, panelTBoxInputPanel, "Input for New TBox",
				JOptionPane.OK_CANCEL_OPTION);
		
		if(confirmation == 0){
			
			String tbName = txtTboxname.getText().toString();
			String tbPath = lblDirname.getText().toString();
			
			if(tbName.equals("")|| tbPath.equals("")){
				JOptionPane.showMessageDialog(null, "Please provide the input for saving TBox");
				return false;
			}else{
				
				
				if(!tbName.endsWith(".owl")){
					tbName+=".owl";
				}
				
				tboxName = tbName;
				tBoxPath = tbPath+"/"+tbName;
				String data ="@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n";
				data+="@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.\n";
				data+="@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.\n";
				data+= "@prefix owl: <http://www.w3.org/2002/07/owl#>.\n";
				boolean written = commonMethods.writeFile(tBoxPath, data);
				
				if(!written){
					JOptionPane.showMessageDialog(null, "TBox not saved!");
					return false;
				}else {
					return true;
				}
			}
			
		}
		return false;
	}

	private void showGraph(String subjectNode) {
		
		panelGraph.removeAll();
		panelGraph.repaint();
		panelGraph.revalidate();

		Graph graph = new SingleGraph("Graph");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.setStrict(false);
		graph.setAutoCreate(true);
		graph.addAttribute("ui.stylesheet", "url('style.css')");

		Object resource = subjectNode;

		ArrayList<String> allTriples = new ArrayList<>();
		if(tBoxModel.getConceptMap().keySet().contains(subjectNode)){
			
			allTriples = tBoxModel.getConceptMap().get(resource.toString());
			
		}else if(tBoxModel.getDatatypePropertyMap().keySet().contains(subjectNode)){
			allTriples = tBoxModel.getDatatypePropertyMap().get(resource.toString());
			
		}else if(tBoxModel.getObjectPropertyMap().keySet().contains(subjectNode)){
			allTriples = tBoxModel.getObjectPropertyMap().get(resource.toString());
		}
		
		//ArrayList<String> allTriples = tBoxModel.getConceptMap().get(resource);
		//System.out.println("Size: " + allTriples.size());
		ArrayList<Pair> triplePair = new ArrayList<>();
		
		for (String triple : allTriples) {
			String predicate = tBoxMethods.getNode(triple, 2);
			String object = tBoxMethods.getNode(triple, 3);
			
			predicate = replaceIRI(predicate);
			//object = replaceIRI(object);
			//System.out.println("Predicate : " + predicate + " Object: " + object);
			Pair pair = new Pair();
			pair.setKey(predicate);
			pair.setValue(object);
			triplePair.add(pair);

		}

		Visual visual = new Visual();
		visual.initiateGraph(resource, triplePair, graph);
		visual.initiateGraph(resource, triplePair, graph);
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		viewer.enableAutoLayout();

		ViewPanel viewPanel = viewer.addDefaultView(false);
		panelGraph.add(viewPanel);

	}
	
	private	boolean addConceptAnnotationButtonHandler() {

		String subject = comboBoxConceptSubject.getSelectedItem().toString();
		String predicate = comboBoxConceptOWL.getSelectedItem().toString();
		String object = comboBoxConceptObject.getSelectedItem().toString();
		
		
		String subjectNode = getNode(subject);
		String predicateNode = getNode(predicate);
		String objectNode = getNode(object);
		
		//System.out.println(subjectNode + " " + predicateNode + " "+ objectNode+".");
		
		ArrayList<String> conceptTriples = tBoxModel.getConceptMap().get(subjectNode);
		
		String triple = subjectNode +" "+predicateNode +" " +objectNode+".";
		if(conceptTriples.contains(triple)){
			JOptionPane.showMessageDialog(null, "Annotation already contained");
			return false;
		}else{
			
			ArrayList<String> triples = new ArrayList<>();
			triples.add(triple);
			appendAtEnd(tBoxPath, triples);
			loadGUIComponents();
		}
		
		
		return true;
		
	}

	private boolean addPropertyButtonHandler(){
		
		String subject = comboBoxPropertySubject.getSelectedItem().toString();
		String predicate = comboBoxPropertyOWL.getSelectedItem().toString();
		String object = comboBoxPropertyObject.getSelectedItem().toString();
		
		String subjectNode = getNode(subject);
		String predicateNode = getNode(predicate);
		String objectNode = getNode(object);
		
		String triple = subjectNode + " " + predicateNode + " "+ objectNode+".";
		//System.out.println(triple);
		
		ArrayList<String> dataTypeTriples = tBoxModel.getDatatypePropertyMap().get(subjectNode);
		ArrayList<String> objectTypeTriples = tBoxModel.getObjectPropertyMap().get(subjectNode);
		
		if(dataTypeTriples != null){
			if(dataTypeTriples.contains(triple)){
				JOptionPane.showMessageDialog(null, "Annotation already contained");
				return false;
			}
		}
		if(objectTypeTriples != null){
			if(objectTypeTriples.contains(triple)){
				JOptionPane.showMessageDialog(null, "Annotation already contained");
				return false;
			}
		}
			
		ArrayList<String> temp = new ArrayList<>();
		temp.add(triple);
		appendAtEnd(tBoxPath, temp);
		loadGUIComponents();
		return true;
	}

	private void loadGUIComponents() {

		tBoxModel = tBoxMethods.getTBoxModel(tBoxPath);
		checkPrefixMap();
		conceptList = getConceptList();
		propertyList = getPropertyList();

		treeTBox.setVisible(true);
		btnConceptAdd.setEnabled(true);
		btnPrefix.setEnabled(true);
		btnPropertyAdd.setEnabled(true);

		loadOWLTree();
		loadComboBoxes();

	}
	
	
	private boolean isAddNewSelected(String item) {

		if (item.equals("ADD NEW"))
			return true;
		else {
			return false;
		}
	}

	private void checkPrefixMap() {

		HashMap<String, String> temp = tBoxModel.getPrefixMap();

		Set<String> prefixKeys = temp.keySet();
		ArrayList<String> prefixList = new ArrayList<>(prefixKeys);

		if (!prefixList.contains("rdf:"))
			tBoxModel.getPrefixMap().put("rdf:", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		if (!prefixList.contains("rdfs:"))
			temp.put("rdfs:", "http://www.w3.org/2000/01/rdf-schema#");
		if (!prefixList.contains("owl:"))
			temp.put("owl:", "http://www.w3.org/2002/07/owl#");
		if (!prefixList.contains("xsd:"))
			temp.put("xsd:", "http://www.w3.org/2001/XMLSchema#");

	}

	private ArrayList<String> getPropertyList() {

		ArrayList<String> propertyList = new ArrayList<>();

		Set<String> objectPropertyKeySet = tBoxModel.getObjectPropertyMap().keySet();
		Iterator iterator = objectPropertyKeySet.iterator();

		while (iterator.hasNext()) {

			String propertyNode = (String) iterator.next();
			propertyList.add(replaceIRI(propertyNode));

		}

		Set<String> datatypePropertyKeySet = tBoxModel.getDatatypePropertyMap().keySet();
		iterator = datatypePropertyKeySet.iterator();

		while (iterator.hasNext()) {

			String propertyNode = (String) iterator.next();
			propertyList.add(replaceIRI(propertyNode));

		}
		return propertyList;
	}

	private String replaceIRI(String IRI) {

		String iriString = IRI.replace("<", "");
		iriString = iriString.replace(">", "");

		HashMap<String, String> prefixMap = tBoxModel.getPrefixMap();

		Set<String> keySet = prefixMap.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {

			String key = iterator.next().toString();
			String iri = prefixMap.get(key);

			if (iriString.contains(iri)) {
				iriString = iriString.replace(iri, key);
				break;
			}
		}

		return iriString;

	}

	private ArrayList<String> getConceptList() {

		Set<String> conceptNodesSet = tBoxModel.getConceptMap().keySet();
		ArrayList<String> conceptNames = new ArrayList<>();

		Iterator iterator = conceptNodesSet.iterator();
		while (iterator.hasNext()) {
			String conceptNode = (String) iterator.next();
			conceptNames.add(replaceIRI(conceptNode));
		}

		return conceptNames;
	}

	private ArrayList<String> getXSDProperties(){
		ArrayList<String> xsdProperties = new ArrayList<>();
		
		xsdProperties.add("xsd:string");
		xsdProperties.add("xsd:integer");
		xsdProperties.add("xsd:double");
		xsdProperties.add("xsd:float");
		xsdProperties.add("xsd:decimal");
		xsdProperties.add("xsd:boolean");
		xsdProperties.add("xsd:date");
		xsdProperties.add("xsd:time");
		xsdProperties.add("xsd:dateTime");
		xsdProperties.add("xsd:hexBinary");
		
		return xsdProperties;
	}
	
	
	private void loadComboBoxes() {

		ArrayList<String> prefixList = new ArrayList<>();

		HashMap<String, String> prefixMap = tBoxModel.getPrefixMap();
		Set<String> keySet = prefixMap.keySet();
		ArrayList<String> keyList = new ArrayList<>(keySet);
		for (String prefix : keyList) {

			prefixList.add(prefix + " <" + prefixMap.get(prefix) + ">");
		}
		comboBoxPrefixList.setModel(new DefaultComboBoxModel(prefixList.toArray()));

		conceptModel = new DefaultComboBoxModel(conceptList.toArray());
		conceptModel.addElement("ADD NEW");
		comboBoxConceptSubject.setModel(conceptModel);

		propertyModel = new DefaultComboBoxModel(propertyList.toArray());
		propertyModel.addElement("ADD NEW");
		comboBoxPropertySubject.setModel(propertyModel);


		ArrayList<String> objectListConcept = new ArrayList<>(conceptList);
		objectListConcept.addAll(propertyList);
		objectListConcept.addAll(owlPropertyList);
		conceptObjectModel = new DefaultComboBoxModel(objectListConcept.toArray());
		conceptObjectModel.addElement("ADD NEW");
		comboBoxConceptObject.setModel(conceptObjectModel);

		ArrayList<String> objectListProperty = new ArrayList<>(propertyList);
		objectListProperty.addAll(conceptList);
		objectListProperty.addAll(owlPropertyList);
		objectListProperty.addAll(getXSDProperties());
		propertyObjectModel = new DefaultComboBoxModel(objectListProperty.toArray());
		propertyObjectModel.addElement("ADD NEW");
		comboBoxPropertyObject.setModel(propertyObjectModel);

	}

	private void loadOWLCombobox(){
		
		owlPropertyList = getOWLProperties();
		DefaultComboBoxModel conceptOwlModel = new DefaultComboBoxModel(owlPropertyList.toArray());
		conceptOwlModel.addElement("ADD NEW");
		comboBoxConceptOWL.setModel(conceptOwlModel);

		DefaultComboBoxModel propertyOwlModel = new DefaultComboBoxModel(owlPropertyList.toArray());
		propertyOwlModel.addElement("ADD NEW");
		comboBoxPropertyOWL.setModel(propertyOwlModel);
		
	}
	
	private void addOWLProperty() {

		JPanel panelAddOwl = new JPanel();
		panelAddOwl.setLayout(new MigLayout("", "[grow]", "[]"));
		
		JComboBox comboBoxOWLProperties = new JComboBox();
		comboBoxOWLProperties.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddOwl.add(comboBoxOWLProperties, "cell 0 0,growx, growy");
		ArrayList<String> allOwlProperty = getAllOWLProperty();
		
		DefaultComboBoxModel comboboxOWLModel = new DefaultComboBoxModel(allOwlProperty.toArray());
		comboBoxOWLProperties.setModel(comboboxOWLModel);
		
		ArrayList<String> list = getOWLProperties();
		
		int confirmation = JOptionPane.showConfirmDialog(null, panelAddOwl, "Select OWL property to add.",
				JOptionPane.OK_CANCEL_OPTION);
		
		if (confirmation == JOptionPane.OK_OPTION) {

			String selected = comboBoxOWLProperties.getSelectedItem().toString();
			
			//System.out.println("selected: " + selected);
			if (selected.equals("")) {
				JOptionPane.showMessageDialog(null, "Failed!\nPlease select a OWL property");
				loadOWLCombobox();
			}
			else{
				if(!list.contains(selected)){
					owlPropertyList.add(selected);
					loadOWLCombobox();
				}
				else{
					JOptionPane.showMessageDialog(null, "OWL property already contained.");
					loadOWLCombobox();
				}
			}
				
		}else {
			loadOWLCombobox();
		}
		
	}

	private void addProperty() {

		JPanel panelAddProperty = new JPanel();
		panelAddProperty.setLayout(new MigLayout("", "[grow][10][grow][10][][10][grow]", "[][]"));

		JLabel lblBase = new JLabel("Base");
		lblBase.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddProperty.add(lblBase, "cell 0 0");

		JLabel lblConcept = new JLabel("Concept");
		lblConcept.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddProperty.add(lblConcept, "flowx,cell 2 0,growx");

		JLabel lblPropertyType = new JLabel("Property Type");
		lblPropertyType.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddProperty.add(lblPropertyType, "cell 6 0");

		JComboBox comboBoxBase = new JComboBox();
		comboBoxBase.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddProperty.add(comboBoxBase, "cell 0 1,growx");

		JTextField txtFieldConcept = new JTextField();
		txtFieldConcept.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddProperty.add(txtFieldConcept, "flowx,cell 2 1,growx");
		txtFieldConcept.setColumns(10);

		JLabel lblProperty = new JLabel("Property");
		lblProperty.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddProperty.add(lblProperty, "cell 4 0");

		JTextField textFieldProperty = new JTextField();
		textFieldProperty.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddProperty.add(textFieldProperty, "cell 4 1");
		textFieldProperty.setColumns(10);

		JComboBox comboBoxPropertyType = new JComboBox();
		comboBoxPropertyType.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxPropertyType
				.setModel(new DefaultComboBoxModel(new String[] {"Datatype Property", "Object Property" }));
		panelAddProperty.add(comboBoxPropertyType, "cell 6 1");

		HashMap<String, String> prefixMap = tBoxModel.getPrefixMap();

		Set<String> prefixes = prefixMap.keySet();
		prefixes.remove("rdf:");
		prefixes.remove("rdfs:");
		prefixes.remove("owl:");
		prefixes.remove("xsd:");
		ArrayList<String> userPrefixList = new ArrayList<>(prefixes);

		DefaultComboBoxModel comboboxBaseModel = new DefaultComboBoxModel(userPrefixList.toArray());
		comboBoxBase.setModel(comboboxBaseModel);

		int confirmation = JOptionPane.showConfirmDialog(null, panelAddProperty, "Select values for property",
				JOptionPane.OK_CANCEL_OPTION);

		if (confirmation == JOptionPane.OK_OPTION) {

			String base = comboBoxBase.getSelectedItem().toString();
			String conceptName = txtFieldConcept.getText().toString();
			String propertyName = textFieldProperty.getText().toString();

			String shortName = base + conceptName + "/" + propertyName;
			System.out.println("Shortname: " + shortName);
			if (propertyList.contains(shortName)) {
				JOptionPane.showMessageDialog(null, "Property already contained");
			} else if (conceptName.equals("") || propertyName.equals("")) {
				JOptionPane.showMessageDialog(null, "Failed!\nConcept Name or Property Name can't be empty!");
			} else {

				propertyList.add(0, shortName);

				// generating triples
				ArrayList<String> triples = new ArrayList<>();

				String rdfIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
				String rdfsIRI = "http://www.w3.org/2000/01/rdf-schema#";
				String owlIRI = "http://www.w3.org/2002/07/owl#";
				String xsdIRI = "http://www.w3.org/2001/XMLSchema#";
				String iri = prefixMap.get(base);
				String annotation = "";

				if (!conceptName.contains(base + conceptName)) {
					conceptList.add(0, base + conceptName);

					annotation = "<" + iri + conceptName + "> " + "<" + rdfsIRI + "type>" + " <" + owlIRI + "Class"
							+ ">.";

					triples.add(annotation);

					annotation = "<" + iri + conceptName + "> " + "<" + rdfsIRI + "label> " + "\"" + conceptName
							+ "\".";
					triples.add(annotation);
				}

				annotation = "<" + iri + conceptName + "/" + propertyName + "> " + "<" + rdfsIRI + "label> " + "\""
						+ propertyName + "\".";
				triples.add(annotation);

				if (comboBoxPropertyType.getSelectedItem().toString().equals("Object Property")) {
					annotation = "<" + iri + conceptName + "/" + propertyName + "> " + "<" + rdfIRI + "type> " + "<"
							+ owlIRI + "ObjectProperty>.";
					triples.add(annotation);
				} else {
					annotation = "<" + iri + conceptName + "/" + propertyName + "> " + "<" + rdfIRI + "type> " + "<"
							+ owlIRI + "DatatypeProperty>.";
					triples.add(annotation);

				}

				appendAtEnd(tBoxPath, triples);
				loadGUIComponents();
				// loadComboBoxes();
			}
		}else{
			loadComboBoxes();
		}
	

	}

	private ArrayList<String> getAllOWLProperty() {

		ArrayList<String> owlProperties = new ArrayList<>();
		owlProperties.add("owl:AllDifferent");
		owlProperties.add("owl:AllDisjointClasses");
		owlProperties.add("owl:AllDisjointProperties");
		owlProperties.add("owl:allValuesFrom");
		owlProperties.add("owl:annotatedProperty");
		owlProperties.add("owl:annotatedSource");
		owlProperties.add("owl:annotatedTarget");
		owlProperties.add("owl:Annotation");
		owlProperties.add("owl:AnnotationProperty");
		owlProperties.add("owl:assertionProperty");
		owlProperties.add("owl:AsymmetricProperty");
		owlProperties.add("owl:Axiom");
		owlProperties.add("owl:backwardCompatibleWith");
		owlProperties.add("owl:bottomDataProperty");
		owlProperties.add("owl:cardinality");
		owlProperties.add("owl:Class");
		owlProperties.add("owl:complementOf");
		owlProperties.add("owl:DataRange");
		owlProperties.add("owl:datatypeComplementOf");
		owlProperties.add("owl:DatatypeProperty");
		owlProperties.add("owl:deprecated");
		owlProperties.add("owl:DeprecatedClass");
		owlProperties.add("owl:DeprecatedProperty");
		owlProperties.add("owl:differentFrom");
		owlProperties.add("owl:disjointUnionOf");
		owlProperties.add("owl:disjointWith");
		owlProperties.add("owl:distinctMembers");
		owlProperties.add("owl:equivalentClass");
		owlProperties.add("owl:equivalentProperty");
		owlProperties.add("owl:FunctionalProperty");
		owlProperties.add("owl:hasSelf");
		owlProperties.add("owl:hasValue");
		owlProperties.add("owl:imports");
		owlProperties.add("owl:incompatibleWith");
		owlProperties.add("owl:intersectionOf");
		owlProperties.add("owl:InverseFunctionalProperty");
		owlProperties.add("owl:inverseOf");
		owlProperties.add("owl:IrreflexiveProperty");
		owlProperties.add("owl:maxCardinality");
		owlProperties.add("owl:maxQualifiedCardinality");
		owlProperties.add("owl:members");
		owlProperties.add("owl:minCardinality");
		owlProperties.add("owl:minQualifiedCardinality");
		owlProperties.add("owl:NamedIndividual");
		owlProperties.add("owl:NegativePropertyAssertion");
		owlProperties.add("owl:Nothing");
		owlProperties.add("owl:ObjectProperty");
		owlProperties.add("owl:onClass");
		owlProperties.add("owl:onDataRange");
		owlProperties.add("owl:onDatatype");
		owlProperties.add("owl:oneOf");
		owlProperties.add("owl:onProperty");
		owlProperties.add("owl:onProperties");
		owlProperties.add("owl:Ontology");
		owlProperties.add("owl:OntologyProperty");
		owlProperties.add("owl:priorVersion");
		owlProperties.add("owl:propertyChainAxiom");
		owlProperties.add("owl:propertyDisjointWith");
		owlProperties.add("owl:qualifiedCardinality");
		owlProperties.add("owl:ReflexiveProperty");
		owlProperties.add("owl:Restriction");
		owlProperties.add("owl:sameAs");
		owlProperties.add("owl:someValuesFrom");
		owlProperties.add("owl:sourceIndividual");
		owlProperties.add("owl:SymmetricProperty");
		owlProperties.add("owl:targetIndividual");
		owlProperties.add("owl:targetValue");
		owlProperties.add("owl:Thing");
		owlProperties.add("owl:topDataProperty");
		owlProperties.add("owl:topObjectProperty");
		owlProperties.add("owl:TransitiveProperty");
		owlProperties.add("owl:unionOf");
		owlProperties.add("owl:versionInfo");
		owlProperties.add("owl:versionIRI");
		owlProperties.add("owl:withRestrictions");

		return owlProperties;
	}

	private void addConcept() {

		JPanel panelAddConcept = new JPanel();
		panelAddConcept.setLayout(new MigLayout("", "[grow][][grow]", "[][]"));

		JLabel lblBase = new JLabel("Base");
		lblBase.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddConcept.add(lblBase, "cell 0 0");

		JLabel lblProperty = new JLabel("Concept");
		lblProperty.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddConcept.add(lblProperty, "cell 2 0,growx");

		JComboBox comboBoxBase = new JComboBox();
		comboBoxBase.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddConcept.add(comboBoxBase, "cell 0 1,growx");

		JTextField txtFieldConcept = new JTextField();
		txtFieldConcept.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddConcept.add(txtFieldConcept, "cell 2 1,growx");
		txtFieldConcept.setColumns(10);

		HashMap<String, String> prefixMap = tBoxModel.getPrefixMap();

		Set<String> prefixes = prefixMap.keySet();
		prefixes.remove("rdf:");
		prefixes.remove("rdfs:");
		prefixes.remove("owl:");
		prefixes.remove("xsd:");
		ArrayList<String> userPrefixList = new ArrayList<>(prefixes);

		DefaultComboBoxModel comboboxBaseModel = new DefaultComboBoxModel(userPrefixList.toArray());
		comboBoxBase.setModel(comboboxBaseModel);

		int confirmation = JOptionPane.showConfirmDialog(null, panelAddConcept, "Select values for concept",
				JOptionPane.OK_CANCEL_OPTION);

		if (confirmation == JOptionPane.OK_OPTION) {

			String base = comboBoxBase.getSelectedItem().toString();
			String conceptName = txtFieldConcept.getText().toString();
			String shortName = base + conceptName;

			// System.out.println("Shortname: " + shortName);
			if (conceptList.contains(shortName)) {
				JOptionPane.showMessageDialog(null, "Concept already contained");
			} else if (conceptName.equals("")) {
				JOptionPane.showMessageDialog(null, "Failed!\nConcept Name can't be empty!");
			} else {

				// generating triples
				ArrayList<String> triples = new ArrayList<>();

				String rdfIRI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
				String rdfsIRI = "http://www.w3.org/2000/01/rdf-schema#";
				String owlIRI = "http://www.w3.org/2002/07/owl#";
				String xsdIRI = "http://www.w3.org/2001/XMLSchema#";
				String iri = prefixMap.get(base);
				String annotation = "";

				conceptList.add(0, base + conceptName);

				annotation = "<" + iri + conceptName + "> " + "<" + rdfsIRI + "type>" + " <" + owlIRI + "Class" + ">.";

				triples.add(annotation);

				annotation = "<" + iri + conceptName + "> " + "<" + rdfsIRI + "label> " + "\"" + conceptName + "\".";
				triples.add(annotation);

				appendAtEnd(tBoxPath, triples);
				loadGUIComponents();
			}

		}else{
			loadComboBoxes();
		}
	}

	private void addPrefixButtonHandler() {

		JPanel panelAddPrefix = new JPanel();
		panelAddPrefix.setLayout(new MigLayout("", "[grow][][grow]", "[][]"));

		JLabel lblHead = new JLabel("Head");
		lblHead.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddPrefix.add(lblHead, "cell 0 0");

		JLabel lblIri = new JLabel("IRI");
		lblIri.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelAddPrefix.add(lblIri, "cell 2 0,growx");

		textFieldHead = new JTextField();
		textFieldHead.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddPrefix.add(textFieldHead, "cell 0 1,growx");
		textFieldHead.setColumns(10);

		txtFieldIri = new JTextField();
		txtFieldIri.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelAddPrefix.add(txtFieldIri, "cell 2 1,growx");
		txtFieldIri.setColumns(20);

		int confirmation = JOptionPane.showConfirmDialog(null, panelAddPrefix, "Please Enter Head and IRI for Prefix.",
				JOptionPane.OK_CANCEL_OPTION);

		if (confirmation == JOptionPane.OK_OPTION) {

			String head = textFieldHead.getText();
			String iri = txtFieldIri.getText();

			if (head.equals("") || iri.equals("")) {
				JOptionPane.showMessageDialog(null, "Please provide valid value of Head and IRI.");
			}else if (iri.endsWith("/") && head.endsWith(":") && !(tBoxModel.getPrefixMap().keySet().contains(head))) {
				tBoxModel.getPrefixMap().put(head, iri);
				appendAtStart(tBoxPath, "@prefix " + head.trim() + " <" + iri.trim() + ">.");

			}else{
				JOptionPane.showMessageDialog(null, "Please provide valid value of Head and IRI.\nNote: Head must end with ':' and IRI must end with '/'.\nDuplicate IRI not allowed.");
			}
		}
		loadComboBoxes();
	}

	private ArrayList<String> getOWLProperties() {

		ArrayList<String> temp = new ArrayList<>();

		temp.add("owl:sameAs");
		temp.add("owl:SymmetricProperty");
		temp.add("owl:TransitiveProperty");
		temp.add("owl:ReflexiveProperty");
		temp.add("owl:ObjectProperty");
		temp.add("owl:DatatypeProperty");
		temp.add("owl:equivalentClass");
		temp.add("owl:equivalentProperty");
		temp.add("owl:disjointWith");
		temp.add("owl:inverseOf");
		temp.add("owl:Class");
		temp.add("rdfs:domain");
		temp.add("rdfs:range");
		temp.add("rdfs:subClassOf");
		temp.add("rdfs:subPropertyOf");
		temp.add("owl:FunctionalProperty");
		temp.add("owl:InverseFunctionalProperty");
		temp.add("owl:AnnotationProperty");
		temp.add("owl:AsymmetricProperty");
		temp.add("owl:IrreflexiveProperty");

		return temp;

	}

	private void loadOWLTree() {

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(tboxName);
		DefaultMutableTreeNode conceptNode = new DefaultMutableTreeNode("Concepts");
		DefaultMutableTreeNode propertyNode = new DefaultMutableTreeNode("Properties");
		DefaultMutableTreeNode datatypePropertyNode = new DefaultMutableTreeNode("DatatypeProperties");
		DefaultMutableTreeNode objectPropertyNode = new DefaultMutableTreeNode("ObjectProperties");

		rootNode.add(conceptNode);
		rootNode.add(propertyNode);
		propertyNode.add(objectPropertyNode);
		propertyNode.add(datatypePropertyNode);

		Set<String> conceptNodesSet = tBoxModel.getConceptMap().keySet();
		// ArrayList<String> conceptList = new ArrayList<>();

		Iterator itr = conceptNodesSet.iterator();

		// System.out.println("Concept Nodes");
		while (itr.hasNext()) {

			String conceptIRINode = (String) itr.next();
			String conceptNodeName = replaceIRI(conceptIRINode);

			DefaultMutableTreeNode conceptNameNode = new DefaultMutableTreeNode(conceptNodeName);
			ArrayList<String> subConceptList = tBoxModel.getSubClassMap().get(conceptIRINode);

			if (subConceptList.size() > 0) {
				DefaultMutableTreeNode subclasses = new DefaultMutableTreeNode("SubConcepts");
				conceptNameNode.add(subclasses);
				for (String subConceptName : subConceptList) {
					subclasses.add(new DefaultMutableTreeNode(replaceIRI(subConceptName)));
				}
			}
			conceptNode.add(conceptNameNode);
		}

		// loading data properties
		Set<String> objectPropertyKeySet = tBoxModel.getObjectPropertyMap().keySet();
		Iterator iterator = objectPropertyKeySet.iterator();
		while (iterator.hasNext()) {

			String objectPropertyNodeString = (String) iterator.next();
			String objectPropertyName = replaceIRI(objectPropertyNodeString);
			DefaultMutableTreeNode objectPropertyNameNode = new DefaultMutableTreeNode(objectPropertyName);
			objectPropertyNode.add(objectPropertyNameNode);

		}

		// loading data properties
		Set<String> datatypePropertyKeySet = tBoxModel.getDatatypePropertyMap().keySet();
		iterator = datatypePropertyKeySet.iterator();
		while (iterator.hasNext()) {

			String datatypePropertyNodeString = (String) iterator.next();
			String datatypePropertyName = replaceIRI(datatypePropertyNodeString);
			DefaultMutableTreeNode datatypePropertyNameNode = new DefaultMutableTreeNode(datatypePropertyName);
			datatypePropertyNode.add(datatypePropertyNameNode);

		}
		treeTBox.setModel(new DefaultTreeModel(rootNode));

	}

	private String getNode(String shortName) {

		String node = "<" + shortName + ">";

		HashMap<String, String> prefixMap = tBoxModel.getPrefixMap();
		Set<String> keySet = prefixMap.keySet();
		ArrayList<String> prefixes = new ArrayList<>(keySet);

		for (String pre : prefixes) {

			if (node.contains(pre)) {
				node = node.replace(pre, prefixMap.get(pre));
				break;
			}

		}

		return node;
	}

	private boolean openButtonHandler() {

		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		FileFilter tBoxFileFilter = new FileNameExtensionFilter("Web Ontology Language File", "owl");
		fileChooser.setFileFilter(tBoxFileFilter);
		fileChooser.setDialogTitle("Select TBox OWL file to open");
		fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);

		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File tBoxFile = fileChooser.getSelectedFile();
			tBoxPath = tBoxFile.getPath();
			tboxName = tBoxFile.getName();

			labelTBoxName.setText(tboxName);

			return true;

		} else {
			return false;
		}

	}
	
	private boolean appendAtStart(String filePath, String text) {

		try {
			BufferedReader fileBufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath)));

			StringBuilder stringBuilder = new StringBuilder();
			String line;

			while ((line = fileBufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}

			BufferedWriter fileBufferedWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(filePath)));
			fileBufferedWriter.write(text + "\n");
			fileBufferedWriter.write(stringBuilder.toString());

			fileBufferedReader.close();
			fileBufferedWriter.close();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Annotation failed!");
			e.printStackTrace();
			return false;
		}

	}

	private boolean appendAtEnd(String filePath, ArrayList<String> triples) {

		try {
			FileWriter filewWritter = new FileWriter(filePath, true);
			BufferedWriter bufferedWriter = new BufferedWriter(filewWritter);
			PrintWriter tboxFilePrintWritter = new PrintWriter(bufferedWriter);

			for (String triple : triples) {
				tboxFilePrintWritter.println(triple);
			}
			tboxFilePrintWritter.close();
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Annotation failed");
			e.printStackTrace();
			return false;
		}

	}

}
