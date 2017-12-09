package view;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChoicePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 * @param linkingPanel 
	 * @param localPanel 
	 * @param dbpediaPanel 
	 * @param sparqlPanel2 
	 */
	public ChoicePanel(LinkingPanel linkingPanel, DbpediaPanel dbpediaPanel, LocalKBPanel localPanel, SparqlPanel sparqlPanel) {
		setBackground(new Color(255, 255, 204));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnWebApiApi = new JButton("Web API");
		btnWebApiApi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				linkingPanel.remove(localPanel);
				linkingPanel.add(dbpediaPanel, "grow, push");
				linkingPanel.repaint();
				linkingPanel.revalidate();
			}
		});
		btnWebApiApi.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnWebApiApi);
		
		JButton btnKb = new JButton("Local KB");
		btnKb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				linkingPanel.remove(dbpediaPanel);
				linkingPanel.add(localPanel, "grow, push");
				linkingPanel.repaint();
				linkingPanel.revalidate();
			}
		});
		
		JButton btnSparqlEnd = new JButton("SPARQL end");
		btnSparqlEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSparqlEnd.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnSparqlEnd);
		btnKb.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnKb);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(btnExit);

	}
}
