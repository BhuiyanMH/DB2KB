package view;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class PanelView extends JPanel {

	/**
	 * Create the panel.
	 */
	public PanelView() {
		setLayout(new MigLayout("", "[grow]", ""));
		
	}
}
