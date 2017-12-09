package view;

import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import model.Pair;
import net.miginfocom.swing.MigLayout;
import store.Visual;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GraphPanel extends JPanel {
	Graph graph;
	JPanel panel;
	ViewPanel viewPanel;
	double zoom = 0.0;

	/**
	 * Create the panel.
	 * 
	 * @param propertylist
	 * @param keyResource
	 */
	public GraphPanel(Object keyResource, ArrayList<Pair> propertylist) {
		initialize();

		Visual visual = new Visual();
		visual.initiateGraph(keyResource, propertylist, graph);
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		viewer.enableAutoLayout();

		viewPanel = viewer.addDefaultView(false);
		panel.add(viewPanel);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public GraphPanel(ArrayList<Pair> resultList, int index) {
		// TODO Auto-generated constructor stub
		initialize();

		Visual visual = new Visual();
		visual.initiateGraph(resultList, index, graph);
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		viewer.enableAutoLayout();

		viewPanel = viewer.addDefaultView(false);
		panel.add(viewPanel);
	}

	public GraphPanel(Object keyResource, ArrayList<Pair> propertylist, ArrayList<Pair> resultList, int index) {
		// TODO Auto-generated constructor stub
		initialize();

		Visual visual = new Visual();
		visual.initiateGraph(keyResource, propertylist, graph);
		visual.initiateGraph(resultList, index, graph);
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		viewer.enableAutoLayout();

		viewPanel = viewer.addDefaultView(false);
		panel.add(viewPanel);
	}

	private void initialize() {
		// TODO Auto-generated method stub
		setBackground(Color.WHITE);
		setLayout(new MigLayout("", "[grow]", "[][grow]"));

		JButton btnZoomOut = new JButton("-");
		btnZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zoom = zoom + 0.5;
				viewPanel.getCamera().setViewPercent(zoom);
			}
		});
		btnZoomOut.setBackground(new Color(255, 255, 102));
		add(btnZoomOut, "flowx,cell 0 0");

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1,grow");

		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JButton btnZoomIn = new JButton("+");
		btnZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoom = zoom - 0.5;
				viewPanel.getCamera().setViewPercent(zoom);
			}
		});
		btnZoomIn.setBackground(new Color(255, 255, 102));
		add(btnZoomIn, "cell 0 0");

		graph = new SingleGraph("Graph");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.setStrict(false);
		graph.setAutoCreate(true);
		graph.addAttribute("ui.stylesheet", "url('style.css')");
	}
}
