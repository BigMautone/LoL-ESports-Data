package view;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import main.DBOperation;

import org.jdatepicker.JDatePanel;

import java.time.LocalDate;

import static main.DBOperation.*;
import static main.ServerUtility.getConnection;
import static view.SuperGUI.*;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InsertGUI {

	private static JFrame insFrame = new JFrame();

	private static JComboBox players = new JComboBox(getPlayers());

	private static JComboBox squads = new JComboBox(getSquads());
	
	private static JDatePicker inizio = null;
	private static JDatePicker fine = null;

	//private static JTextField inizio = new JTextField("0");
	//private static JTextField fine = new JTextField("0");
	private static JTextField stipendio = new JTextField("0");

	public static JFrame getFrame() {
		return insFrame;
	}
	public static void closeFrame() {insFrame.dispose();}

	public static void insertFrame() {
		getMainFrame().setVisible(false);
		
		insFrame = new JFrame();
		
		//pannello contenente tutti gli elementi
		JPanel mainCont = new JPanel();
		mainCont.setLayout(new BoxLayout(mainCont, BoxLayout.PAGE_AXIS));
		
		//Panel contenente selezione giocatore e squadra
		JPanel row1 = new JPanel();
		
		//Panel contenente selezione data inizio e fine contratto
		JPanel row2 = new JPanel();
		
		//Panel inserimento stipendio
		JPanel row3 = new JPanel();
		
		//Panel pulsanti
		JPanel buttonPanel = new JPanel();
		
		JLabel pLabel = new JLabel("Giocatore");
		JLabel sLabel = new JLabel("Squadra");
		JLabel dILabel = new JLabel("Inizio contratto");
		JLabel dFLabel = new JLabel("Fine contratto");
		JLabel stipLabel = new JLabel("Stipendio");
		
		JButton submit = new JButton("Esegui...");
		submit.setActionCommand("esegui inserimento");
		submit.addActionListener(new ButtonClickListener());

		JButton goBackB = new JButton("Torna al menu");
		goBackB.setActionCommand("Back to menu");
		goBackB.addActionListener(new ButtonClickListener());

		inizio = createDatePicker();
		fine = createDatePicker();
		
		Date dataInizio = (Date) inizio.getModel().getValue(); 
		Date dataFine = (Date) inizio.getModel().getValue(); 

		//Gestione panel prima riga
		row1.add(pLabel);
		row1.add(players);
		row1.add(sLabel);
		row1.add(squads);
		
		//Gestione panel seconda riga
		row2.add(dILabel);
		row2.add(inizio);
		row2.add(dFLabel);
		row2.add(fine);
		
		//Gestione panel terza riga
		row3.add(stipLabel);
		row3.add(stipendio);
		
		//Gestione panel pulsanti
		buttonPanel.add(goBackB);
		buttonPanel.add(submit);
		
		mainCont.add(row1);
		mainCont.add(row2);
		mainCont.add(row3);
		mainCont.add(buttonPanel);
		
		insFrame.add(mainCont);
		insFrame.setSize(900, 600);
		insFrame.setVisible(true);
	}

	private static String getDate(JDatePicker j) {
		Date data = (Date) j.getModel().getValue(); 
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		String strDate = dateFormat.format(data);
		System.out.println(strDate);
		return strDate.toString();
	}
	
	public static String getDateC(int i) {
		if (i==0) return getDate(inizio);
		else return getDate(fine);
	}
	
	public static double getStipendio() {
		return Double.parseDouble(stipendio.getText());
	}
	
	public static String getSelectedPlayer() {
		return (String) players.getSelectedItem();
	}
	
	public static String getSelectedSquad() {
		return (String) squads.getSelectedItem();
	}
	
	/**
	 * Create a new DatePicker
	 * 
	 * @return
	 */
	private static JDatePicker createDatePicker() {
		UtilDateModel model = new UtilDateModel();
		
		JDatePicker datePicker = new JDatePicker(model);
		return datePicker;
	}

		 
	public static void showNewContract() {
		String[] a = DBOperation.getNewContractValues();
		JOptionPane.showMessageDialog(null, "Il nuovo contratto di \"" + a[6] +"\" con la squadra \"" + a[5] + "\" "
				+ "dispone delle seguenti informazioni: Data di inizio: " + a[1] + "; Data di fine: " + a[2] + "; Stipendio: " + a[3]);
	}

}
