package view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import model.Pair;
import net.miginfocom.swing.MigLayout;

public class PanelGraph extends JPanel {

	/**
	 * Create the panel.
	 */
	int width = 0, height = 0;
	
	public PanelGraph(Object object, LinkedHashMap<Object, Object> hashMap) {
		initialize();
		mxGraph graph = new mxGraph();
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		add(graphComponent, "push, grow, wrap");
		graph.getModel().beginUpdate();
		
		Object parent = graph.getDefaultParent();
		
		int x = 100, y = 100;
		
		try {
			calculateSize(object);
			Object root = graph.insertVertex(parent, null, object, x, y, width, height, "shape=ellipse");
			for (Entry<Object, Object> m : hashMap.entrySet()) {
				y = y + 150;
				calculateSize("");
				Object v1 = graph.insertVertex(parent, null, "", x, y, width, height, "shape=ellipse");
				graph.insertEdge(parent, null, "", root, v1);
				calculateSize(m.getValue());
				Object v2 = graph.insertVertex(parent, null, m.getValue(), x, (y + 100), width, height);
				graph.insertEdge(parent, null, m.getKey(), v1, v2);
				x = x + 200;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		graph.getModel().endUpdate();
	}
	
	public PanelGraph(int index, LinkedHashMap<Object, Object> webHashMap, boolean type) {
		// TODO Auto-generated constructor stub
		initialize();
		Object key = (webHashMap.keySet().toArray())[ index ];
		Object value = webHashMap.get( (webHashMap.keySet().toArray())[ index ] );
		mxGraph graph = new mxGraph();
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		
		add(graphComponent, "push, grow, wrap");
		graph.getModel().beginUpdate();
		
		Object parent = graph.getDefaultParent();
		
		int x = 100, y = 100;
		
		try {
			calculateSize(key);
			Object root = graph.insertVertex(parent, null, key, x, y, width, height, "shape=ellipse");
			setGraph(value, root, graph, parent, x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		graph.getModel().endUpdate();
	}
	
	public PanelGraph() {
		// TODO Auto-generated constructor stub
		initialize();
	}

	private void setGraph(Object object, Object root, mxGraph graph, Object parent, int x, int y) {
		// TODO Auto-generated method stub
		if (object instanceof ArrayList) {
			for (int i = 0; i < ((ArrayList) object).size(); i++) {
				Pair pair = (Pair) ((ArrayList) object).get(i);
				Object key = pair.getKey();
				Object value = pair.getValue();
				
				x =  x + 300;
				y = y + 100;
				
				if (value instanceof ArrayList) {
					calculateSize("");
					Object v1 = graph.insertVertex(parent, null, "", x, y, width, height, "shape=ellipse");
					graph.insertEdge(parent, null, "", root, v1);
					setGraph(value, v1, graph, parent, x, y);
				} else if (value instanceof LinkedHashMap) {
					calculateSize("");
					Object v1 = graph.insertVertex(parent, null, "", x, y, width, height, "shape=ellipse");
					graph.insertEdge(parent, null, "", root, v1);
					setGraph(value, v1, graph, parent, x, y);
				} else {
					calculateSize(value);
					Object v1 = graph.insertVertex(parent, null, value, x, y, width, height, "shape=ellipse");
					graph.insertEdge(parent, null, key, root, v1);
				}
				
			}
		} else if (object instanceof LinkedHashMap) {
			for (Entry<Object, Object> m : ((LinkedHashMap<Object, Object>) object).entrySet()) {
				Object key = m.getKey();
				Object value = m.getValue();
				
				x =  x + 100;
				y = y + 100;
				
				if (value instanceof ArrayList) {
					calculateSize("");
					Object v1 = graph.insertVertex(parent, null, "", x, y, width, height, "shape=ellipse");
					graph.insertEdge(parent, null, "", root, v1);
					setGraph(value, v1, graph, parent, x, y);
				} else if (value instanceof LinkedHashMap) {
					calculateSize("");
					Object v1 = graph.insertVertex(parent, null, "", x, y, width, height, "shape=ellipse");
					graph.insertEdge(parent, null, "", root, v1);
					setGraph(value, v1, graph, parent, x, y);
				} else {
					calculateSize(value);
					Object v1 = graph.insertVertex(parent, null, value, x, y, width, height, "shape=ellipse");
					graph.insertEdge(parent, null, key, root, v1);
				}
			}
		}
	}

	private void calculateSize(Object object) {
		// TODO Auto-generated method stub
		int length = 10;
		try {
			length = object.toString().length();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if (length < 5) {
			width = 50;
			height = 20;
		} else if (length > 15) {
			width = length * 6;
			height = length * 2;
		} else if (length > 25) {
			width = length * 5;
			height = length * 1;
		} else {
			width = length * 10;
			height = length * 3;
		}
	}

	private void initialize() {
		// TODO Auto-generated method stub
		setLayout(new MigLayout("", "[grow]", ""));
		setBackground(Color.WHITE);
		
		removeAll();
		revalidate();
		repaint();
	}
}
