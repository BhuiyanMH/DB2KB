package view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.Pair;

public class TextPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tableFile;
	private JTable tableWeb;
	private JPanel panel;
	/**
	 * Create the panel.
	 * @param linkingPanel 
	 */
	public TextPanel() {
		setBackground(Color.BLACK);
		setLayout(new GridLayout(0, 3, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		DefaultTableModel model = new DefaultTableModel (
				new Object[][] {
				},
				new String[] {
					"File Property", "File Value", "Status"
				}
			);
		
		tableFile = new JTable(model) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// TODO Auto-generated method stub
				Component comp = super.prepareRenderer(renderer, row, column);
				String status = (String) getModel().getValueAt(row, 2);
				comp.setForeground("Matched".equals(status) ? Color.BLUE : Color.BLACK);
				return comp;
			}
		};
		
		
		tableFile.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableFile.getColumnModel().getColumn(1).setPreferredWidth(130);
		tableFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(tableFile);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1);
		
		DefaultTableModel model2 = new DefaultTableModel (
				new Object[][] {
				},
				new String[] {
						"Dbpedia Property", "Dbpedia Value", "Status"
				}
			);
		
		tableWeb = new JTable(model2) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// TODO Auto-generated method stub
				Component comp = super.prepareRenderer(renderer, row, column);
				String status = (String) getModel().getValueAt(row, 2);
				comp.setForeground("Matched".equals(status) ? Color.BLUE : Color.BLACK);
				return comp;
			}
		};
		tableWeb.getColumnModel().getColumn(0).setPreferredWidth(130);
		tableWeb.getColumnModel().getColumn(1).setPreferredWidth(200);
		tableWeb.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_1.setViewportView(tableWeb);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		add(scrollPane_2);
		
		panel = new JPanel();
		scrollPane_2.setViewportView(panel);
		panel.setLayout(new MigLayout("", "[]", "[]"));

	}

	public void addTextToInternal(Pair pair, Color blue) {
		// TODO Auto-generated method stub
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	boolean result = checkValue(pair, tableFile);
		    	
		    	if (!result) {
		    		DefaultTableModel model = (DefaultTableModel) tableFile.getModel();
					model.addRow(new Object[] {pair.getKey(), pair.getValue(), "Matched"});
				}
		    }
		});
		timer.setRepeats(false);
        timer.setCoalesce(true);
		timer.start();
	}

	public void addTextToInternal(Pair pair) {
		// TODO Auto-generated method stub
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	boolean result = checkValue(pair, tableFile);
		    	
		    	if (!result) {
		    		DefaultTableModel model = (DefaultTableModel) tableFile.getModel();
					model.addRow(new Object[] {pair.getKey(), pair.getValue(), "Not matched"});
				}
		    }
		});
		timer.setRepeats(false);
        timer.setCoalesce(true);
		timer.start();
	}

	protected boolean checkValue(Pair pair, JTable tableFile2) {
		// TODO Auto-generated method stub
		for (int i = 0; i < tableFile2.getRowCount(); i++) {
			String property = tableFile2.getModel().getValueAt(i, 0).toString().trim().toLowerCase();;
			String value = tableFile2.getModel().getValueAt(i, 1).toString().trim().toLowerCase();;
			String type = tableFile2.getModel().getValueAt(i, 2).toString().trim().toLowerCase();;
			
			String newValue = pair.getValue().toString().trim().toLowerCase();
			String newProperty = pair.getKey().toString().trim().toLowerCase();
			
			if (value.equals(newValue)) {
				if (property.equals(newProperty)) {
					if (!type.equals("matched")) {
						DefaultTableModel model = (DefaultTableModel) tableFile2.getModel();
						model.removeRow(i);
					} else {
						return true;
					}
				}
			} else {
				continue;
			}
		}
		
		return false;
	}

	public void addTextToExternal(Pair pair, Color blue) {
		// TODO Auto-generated method stub
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	boolean result = checkValue(pair, tableWeb);
		    	
		    	if (!result) {
		    		if (!pair.getValue().equals("")) {
		    			DefaultTableModel model = (DefaultTableModel) tableWeb.getModel();
						model.addRow(new Object[] {pair.getKey(), pair.getValue(), "Matched"});
					} else {
						DefaultTableModel model = (DefaultTableModel) tableWeb.getModel();
						model.addRow(new Object[] {pair.getKey(), pair.getValue(), ""});
					}
				}
		    }
		});
		timer.setRepeats(false);
        timer.setCoalesce(true);
		timer.start();
	}

	public void addTextToExternal(Pair pair) {
		// TODO Auto-generated method stub
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	boolean result = checkValue(pair, tableWeb);
		    	
		    	if (!result) {
		    		DefaultTableModel model = (DefaultTableModel) tableWeb.getModel();
					model.addRow(new Object[] {pair.getKey(), pair.getValue(), "Not matched"});
				}
		    }
		});
		timer.setRepeats(false);
        timer.setCoalesce(true);
		timer.start();
	}

	public void addTextToToPanel(String string, Color blue) {
		// TODO Auto-generated method stub
		Timer timer = new Timer(1000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	JLabel label = new JLabel(string);
		    	
		    	panel.add(label, "wrap");
		    	repaint();
		    	revalidate();
		    }
		});
		timer.setRepeats(false);
        timer.setCoalesce(true);
		timer.start();
	}
	
}
