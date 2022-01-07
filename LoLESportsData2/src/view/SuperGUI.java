package view;

import static main.DBOperation.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static main.ServerUtility.*;

/**
 * Classe atta alla gestione lato front-end del prosesso
 * 
 * @author franc
 *
 */
public class SuperGUI {

	/**
	 * Main frame
	 */
	private static JFrame mainFrame=new JFrame();

	/**
	 * Frame of the operations
	 */
	JFrame opFrame = new JFrame();

	/**
	 * Tables frame
	 */
	JFrame tabFrame = new JFrame();

	public static JFrame getMainFrame() {return mainFrame;}

	public SuperGUI() {

	}

	
	/**
	 * Interfaccia di partenza
	 */
	public void mainGUI() {
		JPanel titlePanel = null;
		JPanel buttonPanel = null;
		//JPanel containerPanel = null;

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = mainFrame.getContentPane();

		// Panel del titolo
		titlePanel = new JPanel(new FlowLayout());
		titlePanel.setBounds(300, 0, 150, 150);
		titlePanel.setBackground(new Color(255, 0, 0));

		// Title settings
		String s = "Benvenuti in LoL eSports Data";
		JLabel title = new JLabel(s);
		titlePanel.add(title);

		// Button panels
		buttonPanel = new JPanel(new FlowLayout());
		//containerPanel = new JPanel(new FlowLayout());

		// Buttons
		JButton operations = new JButton("Operazioni prestabilite");
		JButton updateStats = new JButton("Aggiorna le statistiche di un giocatore");
		JButton newPlayer = new JButton("Inserisci un nuovo contratto");

		// Button listeners
		operations.setActionCommand("operazioni");
		operations.addActionListener(new ButtonClickListener());
		updateStats.setActionCommand("aggiornamento");
		updateStats.addActionListener(new ButtonClickListener());
		newPlayer.setActionCommand("inserimento");
		newPlayer.addActionListener(new ButtonClickListener());

		// buttons settings
		buttonPanel.setLayout(new GridLayout(2, 2, 50, 50));
		buttonPanel.add(operations);
		buttonPanel.add(newPlayer);
		buttonPanel.add(updateStats);
		//containerPanel.add(buttonPanel);

		container.add(titlePanel, BorderLayout.NORTH);
		container.add(buttonPanel, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.setSize(900, 600);
		mainFrame.setVisible(true);
	}



	public static void main(String[] args) {
		new SuperGUI().mainGUI();
		getConnection();
	}
}
