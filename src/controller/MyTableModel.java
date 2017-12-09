package controller;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
	List<Color> rowColours = Arrays.asList(Color.RED, Color.GREEN, Color.CYAN);

	public void setRowColour(int row, Color c) {
		rowColours.set(row, c);
		fireTableRowsUpdated(row, row);
	}

	public Color getRowColour(int row) {
		return rowColours.get(row);
	}

	@Override
	public int getRowCount() {
		return 3;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return String.format("%d %d", row, column);
	}

	public MyTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}
	
}
