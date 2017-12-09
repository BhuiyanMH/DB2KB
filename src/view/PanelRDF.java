package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.SwingConstants;

import model.DBTable;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class PanelRDF extends JPanel {

	private JPanel mainContainer;
	private CardLayout appLayout;
	ArrayList<DBTable> allDBTableStructure;
	String baseURL;
	
	public PanelRDF(JPanel mainContainerPanel, ArrayList<DBTable> tableConfigurations, String baseURL) {
		
		this.mainContainer = mainContainerPanel;
		this.appLayout = (CardLayout) mainContainerPanel.getLayout();
		this.baseURL = baseURL;
		this.allDBTableStructure = tableConfigurations;
				
		
		setLayout(new BorderLayout(10, 10));
		
		JLabel lblRDFPanel = new JLabel("RDF of The Database");
		lblRDFPanel.setHorizontalAlignment(SwingConstants.CENTER);
		lblRDFPanel.setFont(new Font("Tahoma", Font.BOLD, 20));
		add(lblRDFPanel, BorderLayout.NORTH);
		
		JTextArea textAreaDBRDf = new JTextArea();
		add(textAreaDBRDf, BorderLayout.CENTER);
		
		JPanel panelButtons = new JPanel();
		add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton buttonBackRDF = new JButton("< Back");
		buttonBackRDF.setFont(new Font("Tahoma", Font.BOLD, 16));
		buttonBackRDF.setBackground(new Color(240, 240, 240));
		buttonBackRDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				appLayout.show(mainContainer, PanelDatabase.DB_SUMMERY_PANEL_KEY);
			}
		});
		panelButtons.add(buttonBackRDF);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelButtons.add(btnSave);

	}

}
