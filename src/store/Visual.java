package store;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import com.fasterxml.jackson.annotation.JsonFormat.Value;

import model.Pair;

public class Visual {

	public void initiateGraph(ArrayList<Pair> arrayList, int index, Graph graph) {
		// TODO Auto-generated method stub
		System.setProperty("org.graphstream.ui.renderer","org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.addAttribute("ui.stylesheet","url('style.css')");
        
        Pair pair = arrayList.get(index);
        Object key = pair.getKey();
        Object value = pair.getValue();
        
        if (value instanceof ArrayList) {
        	Node root = graph.addNode(key.toString());
    		root.addAttribute("ui.label", key.toString());
    		
    		drawGraph(root, (ArrayList) value, graph);
		}
        setNodesSizes(graph);
	}

	private void drawGraph(Object root, ArrayList list, Graph graph) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			
			if (object instanceof Pair) {
				Pair pair = (Pair) object;
				Object key = pair.getKey();
		        Object value = pair.getValue();
		        
		        if (value instanceof ArrayList) {
		        	if (((ArrayList) value).size() != 0) {
		        		Node newRoot = graph.addNode(key.toString());
			        	newRoot.addAttribute("ui.label", "        ");
			        	Edge edge = graph.addEdge(newRoot.toString(), root.toString(), newRoot.toString(), true);
						edge.addAttribute("ui.label", key.toString());
			        	
			    		drawGraph(key, (ArrayList) value, graph);
					}
				} else {
					if (value != null || value != "") {
			        	try {
			        		boolean isUri = checkNode(value.toString());
				        	Node newRoot = graph.addNode(value.toString());
				        	newRoot.addAttribute("ui.label", value.toString());
				        	if (!isUri) {
				        		newRoot.addAttribute("ui.class", "value");
							}
				        	
				        	String keyString = checkEdge(key.toString());
				        	
			        		Edge edge = graph.addEdge(newRoot.toString(), root.toString(), newRoot.toString(), true);
				        	edge.addAttribute("ui.label", keyString);
						} catch (Exception e) {
							// TODO: handle exception
							for (Node node : graph) {
								if (node.toString().equals(value.toString())) {
									String keyString = checkEdge(key.toString());
									Edge edge = graph.addEdge(keyString, root.toString(), node.toString(), true);
						        	edge.addAttribute("ui.label", keyString);
									for (Edge e2 : node) {
										e2.addAttribute("ui.class", "matched");
									}
								}
							}
						}
					}
				}
			} else if (object instanceof ArrayList) {
				Node node = graph.getNode(root.toString());
				for (int j = 0; j < ((ArrayList) object).size(); j++) {
					drawGraph(node, (ArrayList) object, graph);
				}
			}
		}
	}

	private String checkEdge(Object key) {
		// TODO Auto-generated method stub
		String keyString = key.toString();
		keyString = new StringBuilder(keyString).reverse().toString();
		
		String regEx = "(epyt.*?)#(.*?)$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(keyString);
		
		while (matcher.find()) {
			keyString = "rdf:type";
			return keyString;
		}
		keyString = new StringBuilder(keyString).reverse().toString();
		
		return keyString;
	}

	private boolean checkNode(String string) {
		// TODO Auto-generated method stub
		String regEx = "((http.*?)/(.*?))|(<www\\..*?>)$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(string);
		
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	private void setNodesSizes(Graph graph) {
		// TODO Auto-generated method stub
		for(Node n:graph.getEachNode()){
			if(null != n.getAttribute("ui.style")){
				n.setAttribute("ui.style", n.getAttribute("ui.style") + ""
						+ " size:"+ Math.round((6*n.toString().length())) +"px, "+ Math.round((n.toString().length())) +"px;");
			}
			else{
				n.addAttribute("ui.style", ""
						+ " size:"+ Math.round((6*n.toString().length())) +"px, "+ Math.round((n.toString().length())) +"px;");
			}
		}
	}

	private void showIt(Object key) {
		// TODO Auto-generated method stub
		System.out.println(key);
	}

	public void initiateGraph(Object keyResource, ArrayList<Pair> propertylist, Graph graph) {
		// TODO Auto-generated method stub
		Node root = graph.addNode(keyResource.toString());
		root.addAttribute("ui.label", keyResource.toString());		
		drawGraph(root, propertylist, graph);
	}
}
