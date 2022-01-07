package view;

import static main.DBOperation.getPlayers;
import static view.SuperGUI.getMainFrame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Op9 {

	private static JFrame op9Ins = new JFrame();

	private static JComboBox players = new JComboBox(getPlayers());

	public static void op9Select() {
		getMainFrame().setVisible(false);

		op9Ins = new JFrame();

		// pannello contenente tutti gli elementi
		JPanel mainCont = new JPanel();

		JLabel l = new JLabel("Seleziona il giocatore");

		JButton submit = new JButton("Cerca...");
		submit.setActionCommand("oper9");
		submit.addActionListener(new ButtonClickListener());

		Border raisedBorder = BorderFactory.createRaisedBevelBorder();

		submit.setBorder(new EmptyBorder(5, 15, 5, 15));
		submit.setBackground(new Color(0x0ccdcd));
		submit.setBorder(raisedBorder);

		mainCont.add(l);
		mainCont.add(players);
		mainCont.add(submit);

		op9Ins.add(mainCont, BorderLayout.CENTER);
		op9Ins.setSize(268, 128);
		op9Ins.setVisible(true);
	}

	public static String getSelectedPlayer() {
		return (String) players.getSelectedItem();
	}
}
