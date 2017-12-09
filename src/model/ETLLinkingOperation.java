package model;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.jena.tdb.base.objectfile.StringFile;

import net.miginfocom.swing.MigLayout;

public class ETLLinkingOperation implements ETLOperation{

	
	String inputFilePath;
	CommonMethods commonMethods;
	
	ArrayList<String> linkingRDFFileList;
	public ETLLinkingOperation() {
		super();
		commonMethods = new CommonMethods();
		linkingRDFFileList = new ArrayList<>();
	}
	

	@Override
	public boolean execute(JTextPane textPane) {
		
		return false;
	}

	@Override
	public boolean getInput(JPanel panel, HashMap<String, LinkedHashSet<String>> inputParamsMap) {
		
		JPanel panelLinkingInput = new JPanel();
		panelLinkingInput.setLayout(new MigLayout("", "[grow][][grow]", "[][][10][][][][]"));
			
		JLabel lblSourceFile = new JLabel("Source File:");
		lblSourceFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelLinkingInput.add(lblSourceFile, "cell 0 1,alignx right");
		
		JComboBox comboBoxInputFilePath = new JComboBox();
		comboBoxInputFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelLinkingInput.add(comboBoxInputFilePath, "cell 2 1,growx");
		
		if(this.inputFilePath != null){
			this.linkingRDFFileList.add(this.inputFilePath);
		}
		comboBoxInputFilePath.setModel(new DefaultComboBoxModel<>(this.linkingRDFFileList.toArray()));
		if(this.inputFilePath != null){
			comboBoxInputFilePath.setSelectedItem(this.inputFilePath);
		}
		
		JTextField textField = new JTextField();
		textField.setColumns(70);
		textField.setVisible(false);
		panelLinkingInput.add(textField, "cell 2 2");
		
		JButton btnNewInputFile = new JButton("Open");
		btnNewInputFile.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelLinkingInput.add(btnNewInputFile, "cell 2 1");
		btnNewInputFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String filePath = commonMethods.getFilePath("Select RDF File for Linking");
				if(!filePath.equals("")){
					
					DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) comboBoxInputFilePath.getModel();
					comboBoxModel.addElement(filePath);
					comboBoxInputFilePath.setModel(comboBoxModel);
					comboBoxInputFilePath.setSelectedItem(filePath);
				}
				
			}
		});

		
		int confirmation = JOptionPane.showConfirmDialog(null, panelLinkingInput, "Please Input Values Lingking.",
				JOptionPane.OK_CANCEL_OPTION);

		if (confirmation == JOptionPane.OK_OPTION) {
			
			String selectedItem = comboBoxInputFilePath.getSelectedItem().toString();
			if(selectedItem!=null && !selectedItem.equals("")){
				
				this.inputFilePath = selectedItem;
				return true;
			
			}else{
				
				return false;
			}
			
		}

		return false;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	
	public ArrayList<String> getLinkingRDFFileList() {
		return linkingRDFFileList;
	}

	public void setLinkingRDFFileList(ArrayList<String> linkingRDFFileList) {
		this.linkingRDFFileList = linkingRDFFileList;
	}
}
