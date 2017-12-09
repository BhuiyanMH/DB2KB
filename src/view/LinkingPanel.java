package view;

import java.awt.Color;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class LinkingPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DbpediaPanel dbpediaPanel = new DbpediaPanel(this);
	LocalKBPanel localKBPanel = new LocalKBPanel(this);
	SparqlPanel sparqlPanel = new SparqlPanel(this);
	/**
	 * Create the panel.
	 */
	public LinkingPanel() {
		setLayout(new MigLayout("", "[grow]", "[]"));
		setBackground(Color.CYAN);
		
		ChoicePanel choicePanel = new ChoicePanel(this, dbpediaPanel, localKBPanel, sparqlPanel);
		add(choicePanel, "growx, pushx, wrap");
		
		add(dbpediaPanel, "grow, push");
	}
}
