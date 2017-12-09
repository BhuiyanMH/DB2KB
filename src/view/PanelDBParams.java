package view;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.fasterxml.jackson.core.sym.Name;

import model.DatabaseConnection;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class PanelDBParams extends JPanel {
	private JTextField textFieldDBURL;
	private JTextField textFieldDBName;
	private JPasswordField passwordFieldDBPassword;
	JLabel lblDBStatus;

	/**
	 * Create the panel.
	 */
	public PanelDBParams() {
		setLayout(new BorderLayout(10, 10));
		
		JPanel panelDBParams = new JPanel();
		add(panelDBParams, BorderLayout.NORTH);
		GridBagLayout gbl_panelDBParams = new GridBagLayout();
		gbl_panelDBParams.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelDBParams.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelDBParams.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelDBParams.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.7976931348623157E308, Double.MIN_VALUE};
		panelDBParams.setLayout(gbl_panelDBParams);
		
		JLabel labelDBURL = new JLabel("DB URL:");
		labelDBURL.setHorizontalAlignment(SwingConstants.LEFT);
		labelDBURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_labelDBURL = new GridBagConstraints();
		gbc_labelDBURL.anchor = GridBagConstraints.EAST;
		gbc_labelDBURL.insets = new Insets(5, 5, 5, 5);
		gbc_labelDBURL.gridx = 0;
		gbc_labelDBURL.gridy = 0;
		panelDBParams.add(labelDBURL, gbc_labelDBURL);
		
		textFieldDBURL = new JTextField();
		textFieldDBURL.setToolTipText("DB URL");
		textFieldDBURL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldDBURL.setColumns(20);
		GridBagConstraints gbc_textFieldDBURL = new GridBagConstraints();
		gbc_textFieldDBURL.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBURL.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldDBURL.gridx = 1;
		gbc_textFieldDBURL.gridy = 0;
		panelDBParams.add(textFieldDBURL, gbc_textFieldDBURL);
		
		JLabel labelDBName = new JLabel("DB Name:");
		labelDBName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_labelDBName = new GridBagConstraints();
		gbc_labelDBName.anchor = GridBagConstraints.EAST;
		gbc_labelDBName.insets = new Insets(5, 0, 5, 5);
		gbc_labelDBName.gridx = 2;
		gbc_labelDBName.gridy = 0;
		panelDBParams.add(labelDBName, gbc_labelDBName);
		
		textFieldDBName = new JTextField();
		textFieldDBName.setToolTipText("DB User Name");
		textFieldDBName.setColumns(20);
		GridBagConstraints gbc_textFieldDBName = new GridBagConstraints();
		gbc_textFieldDBName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDBName.insets = new Insets(5, 0, 5, 5);
		gbc_textFieldDBName.gridx = 3;
		gbc_textFieldDBName.gridy = 0;
		panelDBParams.add(textFieldDBName, gbc_textFieldDBName);
		
		JLabel labelDBPassword = new JLabel("DB Password:");
		labelDBPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_labelDBPassword = new GridBagConstraints();
		gbc_labelDBPassword.anchor = GridBagConstraints.EAST;
		gbc_labelDBPassword.insets = new Insets(5, 0, 5, 5);
		gbc_labelDBPassword.gridx = 4;
		gbc_labelDBPassword.gridy = 0;
		panelDBParams.add(labelDBPassword, gbc_labelDBPassword);
		
		passwordFieldDBPassword = new JPasswordField();
		passwordFieldDBPassword.setToolTipText("DB Password");
		passwordFieldDBPassword.setColumns(20);
		GridBagConstraints gbc_passwordFieldDBPassword = new GridBagConstraints();
		gbc_passwordFieldDBPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldDBPassword.insets = new Insets(5, 0, 5, 10);
		gbc_passwordFieldDBPassword.gridx = 5;
		gbc_passwordFieldDBPassword.gridy = 0;
		panelDBParams.add(passwordFieldDBPassword, gbc_passwordFieldDBPassword);
		
		JPanel panelControlButton = new JPanel();
		add(panelControlButton, BorderLayout.SOUTH);
		
		JButton button = new JButton("Connect");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String userName = textFieldDBName.getText().toString();
				String uPass = passwordFieldDBPassword.getText().toString();
				String url = textFieldDBURL.getText().toString();
				DatabaseConnection databaseConnection = new DatabaseConnection();
				Connection connection = databaseConnection.getConnection(url, userName, uPass);
				
				if(connection!=null){
					
					MainFrame.dbURL = url;
					MainFrame.dbPassword = uPass;
					MainFrame.dbUserName = userName;
					MainFrame.dbConnected = true;
					String DB_name = "";
					try {
						DB_name = connection.getCatalog();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lblDBStatus.setText("  Database status: Connected   DB Name: "+ DB_name);
				}
				
			}
		});
		panelControlButton.add(button);
		button.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDisconnect.setForeground(new Color(0, 0, 0));
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				MainFrame.dbURL = "";
				MainFrame.dbPassword = "";
				MainFrame.dbUserName = "";
				MainFrame.dbConnected = false;
				lblDBStatus.setText("  Database status: Disconnected");
			}
		});
		panelControlButton.add(btnDisconnect);
		
		lblDBStatus = new JLabel("  Database status: Disconnected");
		lblDBStatus.setVerticalAlignment(SwingConstants.TOP);
		lblDBStatus.setFont(new Font("Tahoma", Font.BOLD, 16));
		add(lblDBStatus, BorderLayout.WEST);

	}

}
