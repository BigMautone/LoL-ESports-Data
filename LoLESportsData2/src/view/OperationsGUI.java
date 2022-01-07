package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import view.ButtonClickListener;

import static view.SuperGUI.*;

public class OperationsGUI {
	/**
	 * Frame of the operations
	 */
	private static JFrame opFrame = new JFrame();

	public static JFrame getFrame() {
		return opFrame;
	}

	public static void closeFrame() {
		opFrame.dispose();
	}

	/**
	 * Apre la finestra di scelta operazioni
	 */
	public static void operationFrame() {
		getMainFrame().setVisible(false);

		JPanel titlePanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		opFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Panel del titolo
		titlePanel = new JPanel(new FlowLayout());


		JLabel title = new JLabel("Tutte le operazioni");
		title.setFont(new Font("Serif", Font.BOLD, 18));
		titlePanel.add(title);
		titlePanel.add(title);

		JButton op2 = new JButton("<html>Dato il rateo di ogni giocatore, elenca Nickname,nome,cognome,<br>"
				+ "età e squadra di un giocatore che ha meno di 20 anni con<br>"
				+ "rateo più alto che gioca in una squadra da meno di 1 anno</html");
		JButton op3 = new JButton("<html>Elenca tutte le squadre che hanno vinto<br>"
				+ "le diverse edizioni dei mondiali(Season World Championship)</html>");
		JButton op4 = new JButton("<html>Trova tutti i giocatori della stessa nazionalità della squadra<br>"
				+ "in cui giocano attualmente</html>");
		JButton op5 = new JButton(
				"<html>Trova tutte le informazioni dei membri dello staff di una squadra che guadagnano più di uno<br>"
						+ "o più proprietari di un’altra squadra<br>"
						+ "oppure più di uno o più giocatori della stessa squadra</html>");
		// JButton op1 = new JButton("Operazioni prestabilite");
		JButton op6 = new JButton(
				"<html>Dati i giocatori che hanno vinto almeno 2 tornei giocando nella stessa squadra,<br>"
						+ "elenca Nickname del giocatore, squadra e numero trofei vinti</html>");
		JButton op7 = new JButton(
				"<html>Trova gli allenatori che allenano una squadra che ha vinto almeno un mondiale<br>"
						+ "oppure almeno 2 tornei nazionali</html>");
		JButton op8 = new JButton("<html>Elenca le squadre che hanno giocato partite da 5 turni<br>"
				+ "ribaltando la serie da 0-2 a 3-2 o viceversa</html>");
		JButton op9 = new JButton("<html>Dato un giocatore, <br>mostra il numero di partite vinte</html>");
		JButton op10 = new JButton("<html>Trova le 2 squadre che si sono <br>affrontate più volte</html>");
		JButton op11 = new JButton("<html>Elenca gli sponsor e le informazioni sugli accordi che hanno con la squadra"
				+ "<br>che ha vinto più tornei</html>");
		JButton op12 = new JButton(
				"<html>Trova la squadra(o le squadre)che ha vinto<br>meno tornei ma ha guadagnato di più</html>");
		JButton op13 = new JButton(
				"<html>Trova il giocatore o i giocatori che hanno vinto<br>più edizioni di un mondiale</html>");
		JButton op14 = new JButton(
				"<html>Trova il giocatore, la squadra e lo stipendio,il cui è più alto di tutti gli altri"
						+ "<br>giocatori della stessa squadra attuale</html>");

		Border raisedBorder = BorderFactory.createRaisedBevelBorder();

		op10.setBorder(new EmptyBorder(5, 15, 5, 15));
		op10.setBackground(new Color(0x0ccdcd));
		op10.setBorder(raisedBorder);

		op2.setBorder(new EmptyBorder(5, 15, 5, 15));
		op2.setBackground(new Color(0x0ccdcd));
		op2.setBorder(raisedBorder);

		op3.setBorder(new EmptyBorder(5, 15, 5, 15));
		op3.setBackground(new Color(0x0ccdcd));
		op3.setBorder(raisedBorder);

		op4.setBorder(new EmptyBorder(5, 15, 5, 15));
		op4.setBackground(new Color(0x0ccdcd));
		op4.setBorder(raisedBorder);

		op5.setBorder(new EmptyBorder(5, 15, 5, 15));
		op5.setBackground(new Color(0x0ccdcd));
		op5.setBorder(raisedBorder);

		op6.setBorder(new EmptyBorder(5, 15, 5, 15));
		op6.setBackground(new Color(0x0ccdcd));
		op6.setBorder(raisedBorder);

		op7.setBorder(new EmptyBorder(5, 15, 5, 15));
		op7.setBackground(new Color(0x0ccdcd));
		op7.setBorder(raisedBorder);

		op8.setBorder(new EmptyBorder(5, 15, 5, 15));
		op8.setBackground(new Color(0x0ccdcd));
		op8.setBorder(raisedBorder);
		
		op9.setBorder(new EmptyBorder(5, 15, 5, 15));
		op9.setBackground(new Color(0x0ccdcd));
		op9.setBorder(raisedBorder);

		op11.setBorder(new EmptyBorder(5, 15, 5, 15));
		op11.setBackground(new Color(0x0ccdcd));
		op11.setBorder(raisedBorder);
		
		op12.setBorder(new EmptyBorder(5, 15, 5, 15));
		op12.setBackground(new Color(0x0ccdcd));
		op12.setBorder(raisedBorder);
		
		op13.setBorder(new EmptyBorder(5, 15, 5, 15));
		op13.setBackground(new Color(0x0ccdcd));
		op13.setBorder(raisedBorder);

		op14.setBorder(new EmptyBorder(5, 15, 5, 15));
		op14.setBackground(new Color(0x0ccdcd));
		op14.setBorder(raisedBorder);
		
		// op1.setActionCommand("1");
		op2.setActionCommand("2");
		op3.setActionCommand("3");
		op4.setActionCommand("4");
		op5.setActionCommand("5");
		op6.setActionCommand("6");
		op7.setActionCommand("7");
		op8.setActionCommand("8");
		op9.setActionCommand("9");
		op10.setActionCommand("10");
		op11.setActionCommand("11");
		op12.setActionCommand("12");
		op13.setActionCommand("13");
		op14.setActionCommand("14");

		// op1.addActionListener(new ButtonClickListener());
		op2.addActionListener(new ButtonClickListener());
		op3.addActionListener(new ButtonClickListener());
		op4.addActionListener(new ButtonClickListener());
		op5.addActionListener(new ButtonClickListener());
		op6.addActionListener(new ButtonClickListener());
		op7.addActionListener(new ButtonClickListener());
		op8.addActionListener(new ButtonClickListener());
		op9.addActionListener(new ButtonClickListener());
		op10.addActionListener(new ButtonClickListener());
		op11.addActionListener(new ButtonClickListener());
		op12.addActionListener(new ButtonClickListener());
		op13.addActionListener(new ButtonClickListener());
		op14.addActionListener(new ButtonClickListener());

		buttonPanel.setLayout(new GridLayout(4, 3, 0, 0));
		// buttonPanel.add(op1);
		buttonPanel.add(op2);
		buttonPanel.add(op3);
		buttonPanel.add(op4);
		buttonPanel.add(op5);
		buttonPanel.add(op6);
		buttonPanel.add(op7);
		buttonPanel.add(op8);
		buttonPanel.add(op9);
		buttonPanel.add(op10);
		buttonPanel.add(op11);
		buttonPanel.add(op12);
		buttonPanel.add(op13);
		buttonPanel.add(op14);

		JButton goBackB = new JButton("Torna al menu");
		goBackB.setActionCommand("Back to menu");
		goBackB.addActionListener(new ButtonClickListener());

		goBackB.setBorder(new EmptyBorder(5, 15, 5, 15));
		goBackB.setBackground(new Color(0x0ccdcd));
		goBackB.setBorder(raisedBorder);

		titlePanel.add(goBackB, BorderLayout.EAST);

		opFrame.add(titlePanel, BorderLayout.NORTH);
		opFrame.add(buttonPanel, BorderLayout.CENTER);

		opFrame.setSize(900, 600);
		opFrame.setVisible(true);
	}
	
	
	
}
