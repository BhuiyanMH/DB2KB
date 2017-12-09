package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import controller.OperationController;
import model.Pair;
import net.miginfocom.swing.MigLayout;
import store.SearchWord;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class LocalKBPanel extends JPanel {
	private JTextField textField;
	private JLabel lblSourceFile;
	private JLabel lblTargetFile;
	private JComboBox comboBoxSource;
	private JComboBox comboBoxTarget;
	private JPanel sourceListPanel;
	private JPanel targetListPanel;
	TextPanel textPanel;
	
	String sourceFile = null, targetFile = null;
	Object sourceResource = null, targetResource = null;
	
	ArrayList<Pair> sourceList = new ArrayList<>();
	ArrayList<Pair> targetList = new ArrayList<>();
	ArrayList<Object> sourceResourceList = new ArrayList<>();
	ArrayList<Object> targetResourceList = new ArrayList<>();
	/**
	 * Create the panel.
	 * @param linkingPanel 
	 */
	public LocalKBPanel(LinkingPanel linkingPanel) {
		setBackground(new Color(255, 255, 255));
		setLayout(new MigLayout("", "[][][][grow][][grow]", "[][]"));
		
		JButton btnOpenSource = new JButton("Source KB");
		btnOpenSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// At first, getting the file path
				OperationController controller = new OperationController();
				sourceFile = controller.chooseFile();
				lblSourceFile.setText(sourceFile);

				// Now, extract resources from file
				sourceResourceList = controller.extractResource(sourceFile);

				// setting all resources to combo box
				comboBoxSource.setModel(new DefaultComboBoxModel<Object>(sourceResourceList.toArray()));
				try {
					comboBoxSource.setSelectedIndex(0);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnOpenSource.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnOpenSource);
		
		lblSourceFile = new JLabel();
		lblSourceFile.setForeground(Color.BLACK);
		lblSourceFile.setFont(new Font("Tahoma", Font.ITALIC, 11));
		add(lblSourceFile);
		
		JLabel lblSelectResource = new JLabel("Select Resource");
		lblSelectResource.setForeground(Color.BLUE);
		lblSelectResource.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblSelectResource, "span 4, split 3");
		
		comboBoxSource = new JComboBox();
		comboBoxSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sourceResource = comboBoxSource.getSelectedItem();

				// Now, we will fetch properties of the selected object
				OperationController controller = new OperationController();
				sourceList = controller.extractProperties(sourceFile, sourceResource);
				
				// show properties in a list to edit
				showPropertyList();
			}
		});
		comboBoxSource.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(comboBoxSource, "pushx, growx, wrap");
		
		JButton btnOpenTarget = new JButton("Target KB");
		btnOpenTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperationController controller = new OperationController();
				targetFile = controller.chooseFile();
				lblTargetFile.setText(targetFile);

				// Now, extract resources from file
				targetResourceList = controller.extractResource(targetFile);

				// setting all resources to combo box
				comboBoxTarget.setModel(new DefaultComboBoxModel<Object>(targetResourceList.toArray()));
				comboBoxTarget.addItem("All");
				try {
					comboBoxTarget.setSelectedIndex(0);
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		btnOpenTarget.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnOpenTarget, "span 2, split 2,growx");
		
		lblTargetFile = new JLabel();
		lblTargetFile.setForeground(Color.BLACK);
		lblTargetFile.setFont(new Font("Tahoma", Font.ITALIC, 11));
		add(lblTargetFile);
		
		JLabel lblMaxQuery = new JLabel("Max. Query");
		lblMaxQuery.setForeground(Color.BLUE);
		lblMaxQuery.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblMaxQuery, "span 4, split 6");
		
		textField = new JTextField(10);
		textField.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(textField, "growy");
		textField.setColumns(10);
		
		JButton btnFetch = new JButton("Fetch");
		btnFetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String hitString = textField.getText().trim().toString();
				textField.setText("");
				
				int hits = 0;
				try {
					hits = Integer.parseInt(hitString);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				// Now, we will fetch value
				OperationController controller = new OperationController();
				targetResourceList = controller.retrieveResource(targetFile, hits);
				

				try {
					removePanel(targetListPanel);
				} catch (Exception e2) {
					// TODO: handle exception
				}
				// setting all resources to combo box
				comboBoxTarget.setModel(new DefaultComboBoxModel<Object>(targetResourceList.toArray()));
				comboBoxTarget.addItem("All");
				try {
					comboBoxTarget.setSelectedIndex(0);
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		btnFetch.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnFetch);
		
		JLabel lblSelectResource_1 = new JLabel("Select Resource");
		lblSelectResource_1.setForeground(Color.BLUE);
		lblSelectResource_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblSelectResource_1);
		
		comboBoxTarget = new JComboBox();
		comboBoxTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetResource = comboBoxTarget.getSelectedItem();

				// Now, we will fetch properties of the selected object
				OperationController controller = new OperationController();
				targetList = controller.extractProperties(targetFile, targetResource);
				
				// show properties in a list to edit
				if (!targetResource.equals("All")) {
					showTargetList();
				} else {
					try {
						removePanel(targetListPanel);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
		comboBoxTarget.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(comboBoxTarget, "growx");
		
		JButton btnMatch = new JButton("Match");
		btnMatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				matchInstance(sourceResource, targetResource, sourceList, targetList, targetResourceList);
			}
		});
		btnMatch.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnMatch, "span 1 1, wrap");
		
	}
	
	protected void matchInstance(Object sourceResource2, Object targetResource2, ArrayList<Pair> sourceList2, 
			ArrayList<Pair> targetList2, ArrayList<Object> targetResourceList2) {
		// TODO Auto-generated method stub
		try {
			remove(textPanel);
			remove(sourceListPanel);
			remove(targetListPanel);
		} catch (Exception e) {
			// TODO: handle exception
		}
		textPanel = new TextPanel();
		add(textPanel, "span 6,grow, push");
		repaint();
		revalidate();
		
		if (sourceResource2 != null && targetResource2 != null && !sourceList2.isEmpty() && !targetResourceList2.isEmpty()) {
			if (targetResource2.equals("All")) {
				matchInstance(sourceResource2, sourceList2, targetResourceList2);
			} else {
				boolean resultList = matchInstance(sourceList2, targetList2);
				if (resultList) {
					textPanel.addTextToToPanel(sourceResource2 + " matches  " + targetResource2, Color.BLUE);
				}
			}
		} else {
			OperationController controller = new OperationController();
			controller.errorMessage("Select all value first!");
		}
	}

	private void matchInstance(Object sourceResource2, ArrayList<Pair> sourceList2,
			ArrayList<Object> targetResourceList2) {
		// TODO Auto-generated method stub
		for (int i = 0; i < targetResourceList2.size(); i++) {
			Object targetResource = targetResourceList2.get(i);
			
			OperationController controller = new OperationController();
			ArrayList<Pair> targetList = new ArrayList<>();
			targetList = controller.extractProperties(targetFile, targetResource);
			
			boolean resultList = matchInstance(sourceList2, targetList);
			if (resultList) {
				textPanel.addTextToToPanel(sourceResource2 + " matches  " + targetResource, Color.BLUE);
			}
		}
	}

	private boolean matchInstance(ArrayList<Pair> sourceList2, ArrayList<Pair> targetList2) {
		// TODO Auto-generated method stub
		int count = 0;
		for (int i = 0; i < sourceList2.size(); i++) {
			Pair pair = new Pair();
			pair = sourceList2.get(i);
			textPanel.addTextToInternal(pair);
			for (int j = 0; j < targetList2.size(); j++) {
				Pair newPair = new Pair();
				newPair = targetList2.get(j);
				boolean result = matchPairs(pair, newPair);
				if (result) {
					count++;
					textPanel.addTextToInternal(pair, Color.BLUE);
				}
			}
		}
		
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean matchPairs(Pair pair, Pair newPair) {
		// TODO Auto-generated method stub
		Object internalKey = pair.getKey();
		Object internalValue = pair.getValue();
		
		Object externalKey = newPair.getKey();
		Object externalValue = newPair.getValue();
		
		boolean matchResult = valueMatchingAlgorithm(internalValue, externalValue);
		if (matchResult) {
			boolean propertyResult = propertyMatchingAlgorithm(internalKey, externalKey);
			if (propertyResult) {
				textPanel.addTextToExternal(newPair, Color.BLUE);
				return true;
			} else {
				textPanel.addTextToExternal(newPair);
			}
		} else {
			textPanel.addTextToExternal(newPair);
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

	protected void showTargetList() {
		// TODO Auto-generated method stub
		try {
			removePanel(targetListPanel);
			remove(textPanel);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		targetListPanel = new JPanel();
		targetListPanel.setBackground(Color.WHITE);
		add(targetListPanel, "span 3, wrap");
		repaint();
		revalidate();
		targetListPanel.setLayout(new MigLayout("", "[]", "[][][]"));
		
		JLabel lblTarget = new JLabel("Target");
		lblTarget.setForeground(Color.BLUE);
		lblTarget.setFont(new Font("Tahoma", Font.BOLD, 12));
		targetListPanel.add(lblTarget, "span, wrap");

		JLabel lblSelectEdit = new JLabel("Select & Edit Properties");
		lblSelectEdit.setForeground(Color.BLUE);
		lblSelectEdit.setFont(new Font("Tahoma", Font.BOLD, 12));
		targetListPanel.add(lblSelectEdit, "wrap");

		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (int i = 0; i < targetList.size(); i++) {
			Pair pair = targetList.get(i);
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
		            targetList = controller.updateProperties(targetList, index);
					showPropertyList();
				}
			}
		});
		
		JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 200));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        targetListPanel.add(listScroller, "wrap");

		JButton btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// we get the selected items
				int[] arrays = list.getSelectedIndices();
				
				// Now, we update the list
				OperationController controller = new OperationController();
				targetList = controller.removeProperties(targetList, arrays);
				
				// Now, we should remove the panel
				removePanel(targetListPanel);
			}
		});
		btnFinish.setFont(new Font("Tahoma", Font.BOLD, 12));
		targetListPanel.add(btnFinish, "growx, wrap");
	}
	
	
	private void showPropertyList() {
		// TODO Auto-generated method stub
		try {
			removePanel(sourceListPanel);
			remove(textPanel);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		sourceListPanel = new JPanel();
		sourceListPanel.setBackground(Color.WHITE);
		add(sourceListPanel, "span 3, wrap");
		repaint();
		revalidate();
		sourceListPanel.setLayout(new MigLayout("", "[]", "[][][]"));
		
		JLabel lblSource = new JLabel("Source");
		lblSource.setForeground(Color.BLUE);
		lblSource.setFont(new Font("Tahoma", Font.BOLD, 12));
		sourceListPanel.add(lblSource, "span, wrap");

		JLabel lblSelectEdit = new JLabel("Select & Edit Properties");
		lblSelectEdit.setForeground(Color.BLUE);
		lblSelectEdit.setFont(new Font("Tahoma", Font.BOLD, 12));
		sourceListPanel.add(lblSelectEdit, "wrap");

		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (int i = 0; i < sourceList.size(); i++) {
			Pair pair = sourceList.get(i);
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
		            sourceList = controller.updateProperties(sourceList, index);
					showPropertyList();
				}
			}
		});
		
		JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 200));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        sourceListPanel.add(listScroller, "wrap");

		JButton btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// we get the selected items
				int[] arrays = list.getSelectedIndices();
				
				// Now, we update the list
				OperationController controller = new OperationController();
				sourceList = controller.removeProperties(sourceList, arrays);
				
				// Now, we should remove the panel
				removePanel(sourceListPanel);
			}
		});
		btnFinish.setFont(new Font("Tahoma", Font.BOLD, 12));
		sourceListPanel.add(btnFinish, "growx, wrap");
	}

	private void removePanel(JPanel listPanel2) {
		// TODO Auto-generated method stub
		try {
			remove(listPanel2);
			repaint();
			revalidate();
		} catch (Exception e1) {
			// TODO: handle exception
		}
	}

}
