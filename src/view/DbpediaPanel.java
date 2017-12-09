package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import store.SearchWord;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Font;

import controller.OperationController;
import model.Pair;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.GridLayout;

public class DbpediaPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldHits;
	private JLabel lblFilePath;
	private JComboBox<Object> comboBoxResource;
	private JComboBox<Object> comboBoxProperties;
	private JComboBox<Object> comboBoxDbpedia;
	private JPanel listPanel;
	private JPanel panel;
	private JButton btnGraphMatch;

	String filePath = null;
	Object keyResource = null;
	String searchValue = null;

	ArrayList<Pair> propertylist = new ArrayList<>();
	ArrayList<Pair> resultList = new ArrayList<>();
	
	TextPanel textPanel;
	GraphPanel graphPanel;
	/**
	 * Create the panel.
	 * 
	 * @param linkingPanel
	 */
	public DbpediaPanel(LinkingPanel linkingPanel) {
		setBackground(new Color(255, 255, 255));
		setLayout(new MigLayout("", "[][][][grow][][grow]", "[][][grow]"));

		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// At first, getting the file path
				OperationController controller = new OperationController();
				filePath = controller.chooseFile();
				lblFilePath.setText(filePath);

				// Now, extract resources from file
				ArrayList<Object> resourceList = new ArrayList<>();
				resourceList = controller.extractResource(filePath);

				// setting all resources to combo box
				comboBoxResource.setModel(new DefaultComboBoxModel<Object>(resourceList.toArray()));
			}
		});
		btnOpenFile.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnOpenFile, "span 2, split 2, growx");

		lblFilePath = new JLabel();
		lblFilePath.setFont(new Font("Tahoma", Font.ITALIC, 12));
		add(lblFilePath);

		JLabel lblSelectResource = new JLabel("Select Resource");
		lblSelectResource.setForeground(Color.BLUE);
		lblSelectResource.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblSelectResource, "gapx unrelated");

		comboBoxResource = new JComboBox<Object>();
		comboBoxResource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.removeAll();
				panel.repaint();
				panel.revalidate();
				
				keyResource = comboBoxResource.getSelectedItem();

				// Now, we will fetch properties of the selected object
				OperationController controller = new OperationController();
				propertylist = controller.extractProperties(filePath, keyResource);

				// get only property name from pair
				ArrayList<Object> nameList = new ArrayList<>();
				nameList = controller.getPropertyNames(propertylist);
				
				// show properties in a list to edit
				showPropertyList();

				// setting all properties to combo box
				comboBoxProperties.setModel(new DefaultComboBoxModel<Object>(nameList.toArray()));
			}
		});
		comboBoxResource.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(comboBoxResource, "growx");

		JLabel lblSearchProperty = new JLabel("Search Property");
		lblSearchProperty.setForeground(Color.BLUE);
		lblSearchProperty.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblSearchProperty, "gapx unrelated");

		comboBoxProperties = new JComboBox<Object>();
		comboBoxProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object searchProperty = comboBoxProperties.getSelectedItem();
				
				// Now, we get the search value for the selected property
				OperationController controller = new OperationController();
				searchValue = controller.getSearchValue(searchProperty, propertylist);
			}
		});
		comboBoxProperties.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(comboBoxProperties, "growx, wrap");

		JLabel lblMaxHits = new JLabel("Max. Hits");
		lblMaxHits.setForeground(Color.BLUE);
		lblMaxHits.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblMaxHits, "span 3, split 2");

		textFieldHits = new JTextField();
		textFieldHits.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(textFieldHits, "grow");
		textFieldHits.setColumns(10);

		JButton btnFetchData = new JButton("Fetch Data From DBpedia");
		btnFetchData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String hitString = textFieldHits.getText().trim().toString();
				textFieldHits.setText("");
				
				int hits = 0;
				try {
					hits = Integer.parseInt(hitString);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				// Now, we will fetch value
				OperationController controller = new OperationController();
				resultList = controller.retrieveData(searchValue, hits);
				
				// Now, we will add in the combo box
				ArrayList<Object> nameList = new ArrayList<>();
				nameList = controller.getPropertyNames(resultList);
				comboBoxDbpedia.setModel(new DefaultComboBoxModel<Object>(nameList.toArray()));
				comboBoxDbpedia.addItem("All");
			}
		});
		btnFetchData.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnFetchData, "span 3, split 5");

		JLabel lblDbpediaResource = new JLabel("DBpedia Resource");
		lblDbpediaResource.setForeground(Color.BLUE);
		lblDbpediaResource.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblDbpediaResource);

		comboBoxDbpedia = new JComboBox<Object>();
		comboBoxDbpedia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = comboBoxDbpedia.getSelectedIndex();
				Object object = comboBoxDbpedia.getSelectedItem();
				if (!object.toString().equals("All")) {
					btnGraphMatch.setFocusPainted(false);
					btnGraphMatch.setEnabled(true);
					try {
						panel.remove(textPanel);
					} catch (Exception e) {
						// TODO: handle exception
					}
					graphPanel = new GraphPanel(resultList, index);
					panel.add(graphPanel, "span 6, grow, push");
					repaint();
					revalidate();
				} else {
					btnGraphMatch.setEnabled(false);
				}
			}
		});
		comboBoxDbpedia.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(comboBoxDbpedia, "growx");

		JButton btnMatch = new JButton("Match Textually");
		btnMatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedObject = comboBoxDbpedia.getSelectedItem();
				
				// Now, we begin the matching process
				matchInstances(propertylist, resultList, selectedObject, keyResource, searchValue, filePath);
			}
		});
		btnMatch.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnMatch);
		
		btnGraphMatch = new JButton("Match Graphically");
		btnGraphMatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.removeAll();
				graphPanel.removeAll();
				repaint();
				revalidate();
				
				int index = comboBoxDbpedia.getSelectedIndex();
				graphPanel = new GraphPanel(keyResource, propertylist, resultList, index);
				panel.add(graphPanel, "span, grow, push");
				repaint();
				revalidate();
			}
		});
		btnGraphMatch.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnGraphMatch.setEnabled(false);
		add(btnGraphMatch, "wrap");
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "span, grow");
		panel.setLayout(new MigLayout("", "[]", "[]"));
	}
	
	public void matchInstances(ArrayList<Pair> propertylist, ArrayList<Pair> resultList, Object selectedObject,
			Object keyResource, String searchValue, String filePath2) {
		// TODO Auto-generated method stub
		try {
			panel.removeAll();
			repaint();
			revalidate();
		} catch (Exception e) {
			// TODO: handle exception
		}
		textPanel = new TextPanel();
		panel.add(textPanel, "span 6, grow, push");
		repaint();
		revalidate();
		
		if (selectedObject.equals("All")) {
			Pair pair = new Pair();
			for (int i = 0; i < resultList.size(); i++) {
				pair = resultList.get(i);
				Pair newPair = new Pair();
				newPair.setKey("DBpedia Resource " +(i+1));
				newPair.setValue("");
				textPanel.addTextToExternal(newPair, Color.BLUE);
				boolean result = matchInstances(propertylist, pair, keyResource, searchValue, filePath2);
				
				if (result) {
					textPanel.addTextToToPanel(keyResource + " matches " + pair.getKey().toString(), Color.BLUE);
				}
			}
		} else {
			Pair pair = new Pair();
			for (int i = 0; i < resultList.size(); i++) {
				pair = resultList.get(i);
				Object key = pair.getKey();
				
				if (key.equals(selectedObject)) {
					break;
				}
			}
			boolean result = matchInstances(propertylist, pair, keyResource, searchValue, filePath2);
			
			if (result) {
				textPanel.addTextToToPanel(keyResource + " matches " + pair.getKey().toString(), Color.BLUE);
			}
		}
	}

	private boolean matchInstances(ArrayList<Pair> propertylist, Pair pair, Object keyResource,
			String searchValue, String filePath2) {
		// TODO Auto-generated method stub
		int count = 0;
		for (int i = 0; i < propertylist.size(); i++) {
			Pair newPair = propertylist.get(i);
			boolean result = matchInstances(newPair, pair, keyResource, searchValue, filePath2);
			if (result) {
				textPanel.addTextToInternal(newPair, Color.BLUE);
				count++;
			} else {
				textPanel.addTextToInternal(newPair);
			}
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean matchInstances(Pair newPair, Pair pair, Object keyResource, String searchValue, String filePath2) {
		// TODO Auto-generated method stub
		Object dbpediaKey = pair.getKey();
		Object dbpediaValue = pair.getValue();
		int count = 0;
		
		if (dbpediaValue instanceof ArrayList) {
			count = matchInstances(newPair, (ArrayList) dbpediaValue, keyResource, searchValue, filePath2);
			if (count > 0) {
				// textPanel.addTextToMatch(dbpediaKey + " has importance.", Color.RED);
				return true;
			}
		} else {
			boolean result = false;
			result = matchPairs(newPair, pair, textPanel);
			return result;
		}
		return false;
	}

	private boolean matchPairs(Pair newPair, Pair pair, TextPanel textPanel) {
		// TODO Auto-generated method stub
		Object dbpediaKey = pair.getKey();
		Object dbpediaValue = pair.getValue();
		
		Object fileKey = newPair.getKey();
		Object fileValue = newPair.getValue();
		
		boolean matchResult = valueMatchingAlgorithm(fileValue, dbpediaValue);
		if (matchResult) {
			// textPanel.addTextToMatch(fileValue + " matches " + dbpediaValue, Color.RED);
			boolean propertyResult = propertyMatchingAlgorithm(fileKey, dbpediaKey);
			if (propertyResult) {
				textPanel.addTextToExternal(pair, Color.BLUE);
				return true;
			} else {
				textPanel.addTextToExternal(pair);
			}
		} else {
			textPanel.addTextToExternal(pair);
		}
		return false;
	}

	private boolean propertyMatchingAlgorithm(Object fileKey, Object dbpediaKey) {
		// TODO Auto-generated method stub
		if (fileKey == null || dbpediaKey == null) {
			return false;
		} else {
			if (fileKey.toString().trim().toLowerCase().equals(dbpediaKey.toString().trim().toLowerCase())) {
				return true;
			} else {
				SearchWord searchWord = new SearchWord();
				boolean matchResult =false;
				matchResult = searchWord.matchWord(fileKey, dbpediaKey);
				return matchResult;
			}
		}
	}

	private boolean valueMatchingAlgorithm(Object fileValue, Object dbpediaValue) {
		// TODO Auto-generated method stub
		if (fileValue.toString().trim().toLowerCase().equals(dbpediaValue.toString().trim().toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	private int matchInstances(Pair newPair, ArrayList resultList, Object keyResource, String searchValue,
			String filePath2) {
		// TODO Auto-generated method stub
		int count = 0;
		for (int i = 0; i < resultList.size(); i++) {
			Object object = resultList.get(i);
			if (object instanceof ArrayList) {
				int value = 0;
				value = matchInstances(newPair, (ArrayList) object, keyResource, searchValue, filePath2);
				count = count + value;
			} else {
				boolean result = false;
				result = matchInstances(newPair, (Pair) object, keyResource, searchValue, filePath2);
				if (result) {
					count++;
				} else {
					continue;
				}
			}
		}
		return count;
	}

	private void showPropertyList() {
		// TODO Auto-generated method stub
		try {
			panel.remove(textPanel);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		listPanel = new JPanel();
		listPanel.setBackground(Color.WHITE);
		panel.add(listPanel, "span 3, wrap");
		listPanel.setLayout(new MigLayout("", "[]", "[][][]"));

		JLabel lblSelectEdit = new JLabel("Select & Edit Properties");
		lblSelectEdit.setForeground(Color.BLUE);
		lblSelectEdit.setFont(new Font("Tahoma", Font.BOLD, 12));
		listPanel.add(lblSelectEdit, "wrap");

		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (int i = 0; i < propertylist.size(); i++) {
			Pair pair = propertylist.get(i);
			listModel.addElement(pair.getKey());
		}

		JList<Object> list = new JList<Object>(listModel);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 12));
		list.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 2) {
					int index = list.locationToIndex(e.getPoint());
		            OperationController controller = new OperationController();
					propertylist = controller.updateProperties(propertylist, index);
					
					panel.removeAll();
					panel.repaint();
					panel.revalidate();
					
					showPropertyList();
				}
			}
		});
		
		JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 200));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        listPanel.add(listScroller, "wrap");

		JButton btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// we get the selected items
				int[] arrays = list.getSelectedIndices();
				
				// Now, we update the list
				OperationController controller = new OperationController();
				propertylist = controller.removeProperties(propertylist, arrays);
				
				// Now, we update the selection list
				ArrayList<Object> nameList = new ArrayList<>();
				nameList = controller.getPropertyNames(propertylist);
				comboBoxProperties.setModel(new DefaultComboBoxModel<Object>(nameList.toArray()));
				
				// Now, we should remove the panel
				panel.removeAll();
				
				graphPanel = new GraphPanel(keyResource, propertylist);
				panel.add(graphPanel, "span, split 2, grow, push");
				repaint();
				revalidate();
			}
		});
		btnFinish.setFont(new Font("Tahoma", Font.BOLD, 12));
		listPanel.add(btnFinish, "wrap");
		
		graphPanel = new GraphPanel(keyResource, propertylist);
		panel.add(graphPanel, "span, split 2, grow, push");
		repaint();
		revalidate();
	}
	
}
