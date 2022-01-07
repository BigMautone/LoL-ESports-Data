package view;

import javax.swing.*;
import javax.swing.border.Border;

import static main.DBOperation.*;
import static main.ServerUtility.getConnection;
import static view.SuperGUI.*;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class UpdateGUI {

	private static JFrame upFrame = new JFrame();

	private static JComboBox players = new JComboBox(getPlayers());

	private static JTextField uccisioni = new JTextField("0");

	private static JTextField morti = new JTextField("0");

	private static JTextField assist = new JTextField("0");

	public static JFrame getFrame() {
		return upFrame;
	}

	public static void updateFrame() {
		getMainFrame().setVisible(false);
		
		upFrame = new JFrame();
		
		JPanel userPanel = new JPanel();

		JLabel pLabel = new JLabel("Giocatore");
		
		JLabel uLabel = new JLabel("Uccisioni");
		
		JLabel mLabel = new JLabel("Morti");
		
		JLabel aLabel = new JLabel("Assist");
		

		userPanel.add(pLabel);
		userPanel.add(players);
		userPanel.add(pLabel);
		userPanel.add(uccisioni);
		userPanel.add(uLabel);
		userPanel.add(morti);
		userPanel.add(aLabel);
		userPanel.add(assist);

		userPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JPanel buttonPanel = new JPanel();

		JButton submit = new JButton("Esegui...");
		submit.setActionCommand("esegui aggiornamento");
		submit.addActionListener(new ButtonClickListener());

		JButton goBackB = new JButton("Torna al menu");
		goBackB.setActionCommand("Back to menu");
		goBackB.addActionListener(new ButtonClickListener());

		buttonPanel.add(goBackB);
		buttonPanel.add(submit);
		upFrame.add(userPanel, BorderLayout.CENTER);
		upFrame.add(buttonPanel, BorderLayout.SOUTH);
		upFrame.setSize(900, 600);
		upFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		upFrame.setVisible(true);
	}

	public static String getSelectedPlayer() {
		return (String) players.getSelectedItem();
	}

	public static int getKills() {
		//System.out.println(uccisioni.getText() + "--" +Integer.parseInt(uccisioni.getText()));
		return Integer.parseInt(uccisioni.getText());
	}

	public static int getDeaths() {
		//System.out.println(Integer.parseInt(morti.getText()));
		return Integer.parseInt(morti.getText());
	}

	public static int getAssists() {
		//System.out.println(Integer.parseInt(assist.getText()));
		return Integer.parseInt(assist.getText());
	}

	public static void showNewStats() {
		String s = "Select * from statistiche where Giocatore = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(s);

			// Inserisco i parametri di input
			ps.setString(1, getSelectedPlayer());
			rs = ps.executeQuery();
			// ps.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		String[][] ss = getValues(rs, getColumnsName(rs).length);

		ArrayList<String> statSet = new ArrayList<String>();

		for (int j = 0; j < ss[0].length; j++) {
			statSet.add(ss[0][j]);
		}

		String[] a = new String[statSet.size()];
		statSet.toArray(a);


		JOptionPane.showMessageDialog(null, "Le statistiche aggiornate di \"" + getSelectedPlayer() + "\" sono:"
				+ "\n Uccisioni: " + a[1] + " - Morti: " + a[2] + " - Assist: " + a[3]);
	}

}
