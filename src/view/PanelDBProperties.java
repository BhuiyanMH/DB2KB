package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import controller.DBPropertiesController;
import model.DBTable;
import model.ForeignKey;

import java.awt.GridLayout;
import java.awt.TextField;
import java.util.ArrayList;

import javax.print.attribute.standard.NumberOfDocuments;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelDBProperties extends JPanel {

	private JTextField textField;

	JPanel panelPrimaryKeys, panelForeignKeys, panelValues;
	JPanel mainContainer;
	CardLayout appLayout;

	ArrayList<String> primaryKeyLabels = new ArrayList<>();
	ArrayList<JTextField> primaryKeyURIs = new ArrayList<>();

	DBPropertiesController dbPropertiesController;
	ArrayList<DBTable> dbTableStructures;

	/**
	 * Create the panel.
	 */
	public PanelDBProperties(JPanel mainContainer) {

		this.mainContainer = mainContainer;
		this.appLayout = (CardLayout) mainContainer.getLayout();
		setLayout(new BorderLayout(0, 0));

		JPanel panelDataPropertyContent = new JPanel();
		add(panelDataPropertyContent, BorderLayout.CENTER);
		panelDataPropertyContent.setLayout(new GridLayout(1, 3, 0, 0));

		JScrollPane scrollPanePrimaryKeys = new JScrollPane();
		panelDataPropertyContent.add(scrollPanePrimaryKeys);

		panelPrimaryKeys = new JPanel();
		scrollPanePrimaryKeys.setViewportView(panelPrimaryKeys);
		panelPrimaryKeys.setLayout(new BoxLayout(panelPrimaryKeys, BoxLayout.Y_AXIS));

		JLabel lblPrimaryKeys = new JLabel("Primary Keys");
		lblPrimaryKeys.setHorizontalTextPosition(SwingConstants.CENTER);
		panelPrimaryKeys.add(lblPrimaryKeys);

		JScrollPane scrollPaneForeignKeys = new JScrollPane();

		panelDataPropertyContent.add(scrollPaneForeignKeys);

		panelForeignKeys = new JPanel();
		scrollPaneForeignKeys.setViewportView(panelForeignKeys);
		panelForeignKeys.setLayout(new BoxLayout(panelForeignKeys, BoxLayout.Y_AXIS));

		JLabel lblForeignKeys = new JLabel("Foreign Keys");
		panelForeignKeys.add(lblForeignKeys);

		JScrollPane scrollPaneValues = new JScrollPane();
		panelDataPropertyContent.add(scrollPaneValues);

		panelValues = new JPanel();
		panelValues.setLayout(new BoxLayout(panelValues, BoxLayout.Y_AXIS));
		scrollPaneValues.setViewportView(panelValues);

		JLabel lblDataValues = new JLabel("Data Values");
		panelValues.add(lblDataValues);

		JLabel lblDataAndObject = new JLabel("Data and Object Properties");
		lblDataAndObject.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDataAndObject, BorderLayout.NORTH);

		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, primaryKeyURIs.get(0).getText().toString());
			}
		});
		add(btnDone, BorderLayout.SOUTH);

		// initliaize the controller
		dbPropertiesController = new DBPropertiesController();
		dbTableStructures = dbPropertiesController.getAllTableStructure();

		addViews();

	}

	private void addViews() {

		int numOfTables = dbTableStructures.size();

		for (int i = 0; i < numOfTables; i++) {
			DBTable dbTable = dbTableStructures.get(i);

			JLabel temp = new JLabel(dbTable.getTableName() + " - " + dbTable.getPrimaryKeys().get(0));
			panelPrimaryKeys.add(temp);

			JRadioButton rdbtnUri = new JRadioButton(dbTable.getPrimaryKeys().get(0) + " is an URI");

			panelPrimaryKeys.add(rdbtnUri);

			JRadioButton rdbtnLiteral = new JRadioButton(dbTable.getPrimaryKeys().get(0) + " is a Literal");
			panelPrimaryKeys.add(rdbtnLiteral);

			rdbtnUri.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JOptionPane.showMessageDialog(null, arg0.getActionCommand().toString());
					// System.out.println(arg0.getSource().toString());

				}
			});

			ButtonGroup typeButtonGroup = new ButtonGroup();
			typeButtonGroup.add(rdbtnLiteral);
			typeButtonGroup.add(rdbtnUri);

			textField = new JTextField();
			panelPrimaryKeys.add(textField);
			textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
			primaryKeyURIs.add(textField);

			ArrayList<ForeignKey> fks = dbTable.getForeignKeys();
			for (ForeignKey foreignKey : fks) {

				for (String kName : foreignKey.getColumnNames()) {
					JLabel t = new JLabel(dbTable.getTableName() + " - " + kName);
					panelForeignKeys.add(t);

					JTextField tempTextField = new JTextField();
					panelForeignKeys.add(tempTextField);
					tempTextField
							.setMaximumSize(new Dimension(Integer.MAX_VALUE, tempTextField.getPreferredSize().height));

				}

			}

			for (String columns : dbTable.getDataColumns()) {

				JLabel temp2 = new JLabel(dbTable.getTableName() + " - " + columns);
				panelValues.add(temp2);

				JRadioButton rdbtnUri2 = new JRadioButton(columns + " is an URI");

				panelValues.add(rdbtnUri2);

				JRadioButton rdbtnLiteral2 = new JRadioButton(dbTable.getPrimaryKeys().get(0) + " is a Literal");
				panelValues.add(rdbtnLiteral2);

				rdbtnUri.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						JOptionPane.showMessageDialog(null, arg0.getActionCommand().toString());
						// System.out.println(arg0.getSource().toString());

					}
				});

				ButtonGroup typeButtonGroup2 = new ButtonGroup();
				typeButtonGroup.add(rdbtnLiteral);
				typeButtonGroup.add(rdbtnUri);

				textField = new JTextField();
				panelValues.add(textField);
				textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));

			}

		}

		for (DBTable t : dbTableStructures) {
			t.printTableConfigurations();
		}
	}

}
