package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import static view.SuperGUI.*;
import static view.OperationsGUI.*;
import static view.TableGUI.*;
import static view.UpdateGUI.*;
import static main.DBOperation.*;
import static view.InsertGUI.*;

public class ButtonClickListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		switch (command) {
		case "aggiornamento":
			// JOptionPane.showMessageDialog(null, "aggiornamento");
			updateFrame();
			break;
		case "esegui aggiornamento":
			doStatisticsUpdate(UpdateGUI.getSelectedPlayer(), getKills(), getDeaths(), getAssists());
			showNewStats();
			UpdateGUI.getFrame().dispose();
			getMainFrame().setVisible(true);
			break;
		case "inserimento":
			// JOptionPane.showMessageDialog(null, "inserimento");
			insertFrame();
			break;
		case "esegui inserimento":
			doContractInsert(InsertGUI.getSelectedPlayer(), getSelectedSquad(), getStipendio(), getDateC(0),
					getDateC(1));
			InsertGUI.getFrame().dispose();
			getMainFrame().setVisible(true);
			break;
		case "operazioni":
			// JOptionPane.showMessageDialog(null, "operazioni");
			operationFrame();
			break;
		case "Back to menu":
			OperationsGUI.closeFrame();
			UpdateGUI.closeFrame();
			InsertGUI.closeFrame();
			getMainFrame().setVisible(true);
			break;
		case "1":
			createTables(1);
			break;
		case "2":
			createTables(2);
			break;
		case "3":
			createTables(3);
			break;
		case "4":
			createTables(4);
			break;
		case "5":
			createTables(5);
			break;
		case "6":
			createTables(6);
			break;
		case "7":
			createTables(7);
			break;
		case "8":
			createTables(8);
			break;
		case "9":
			createTables(9);
			break;
		case "10":
			createTables(10);
			break;
		case "11":
			createTables(11);
			break;
		case "12":
			createTables(12);
			break;
		case "13":
			createTables(13);
			break;
		case "14":
			createTables(14);
			break;
		}
	}
}
