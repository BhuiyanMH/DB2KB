package view;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.CSVPanelController;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class PanelCSV extends JPanel {

	/**
	 * Create the panel.
	 */
	
	JLabel lblCSVFileDetails;
	
	CSVPanelController csvPanelController;
	JPanel mainContainer;
	CardLayout appLayoput;
	
	public PanelCSV(JPanel mainContainer) {
		
		this.mainContainer = mainContainer;
		appLayoput = (CardLayout)mainContainer.getLayout();
		
		
		setLayout(new BorderLayout(0, 0));
		
		lblCSVFileDetails = new JLabel("Selected File: None. Open CSV file to Convert.");
		lblCSVFileDetails.setFont(new Font("Tahoma", Font.BOLD, 16));
		add(lblCSVFileDetails, BorderLayout.NORTH);
		
		JTextArea textAreaRDF = new JTextArea();
		textAreaRDF.setFont(new Font("Monospaced", Font.PLAIN, 18));
		add(textAreaRDF, BorderLayout.CENTER);
		
		JPanel panelCSVButtons = new JPanel();
		add(panelCSVButtons, BorderLayout.SOUTH);
		
		JButton btnSelectCSV = new JButton("Open CSV File");
		btnSelectCSV.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSelectCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String path = csvPanelController.selectButtonHandler(MainFrame.frmMain);
				if(!path.equals(""))
					lblCSVFileDetails.setText("Selected File : "+path);
			}
		});
		panelCSVButtons.add(btnSelectCSV);
		
		JButton btnCSVConvert = new JButton("Convert");
		btnCSVConvert.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnCSVConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				textAreaRDF.setText("Conversion in Progress.\nPlease Wait...");
				
				ArrayList<String> RDFTuples = csvPanelController.ConvertButtonHandler();
				String text = "";
				for(String s:RDFTuples){
					text +=s+"\n";
				}
				textAreaRDF.setText(text);
			}
		});
		panelCSVButtons.add(btnCSVConvert);
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PrintWriter out;
				try {
					// Create a file chooser
					final JFileChooser fileChooser = new JFileChooser();
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setApproveButtonText("Save");
					fileChooser.setDialogTitle("Select directory to save");
					fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);

					String rmlFilePath = "";

					int returnVal = fileChooser.showOpenDialog(mainContainer);
					if (returnVal == JFileChooser.APPROVE_OPTION) {

						File rmlFile = fileChooser.getSelectedFile();
						rmlFilePath = rmlFile.getPath();
						// fileName = csvFile.getName();
					}

					out = new PrintWriter(rmlFilePath + "/CSV_RDF.n3");
					out.println(textAreaRDF.getText().toString());
					out.close();

				} catch (FileNotFoundException error) {

					JOptionPane.showMessageDialog(null, "File Saving Failed");
				}

			}
		});
		panelCSVButtons.add(btnSave);
		
		
		csvPanelController = new CSVPanelController();

	}

}
