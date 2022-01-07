package view;

import static main.DBOperation.getColumnsName;
import static main.DBOperation.getOperation;
import static main.DBOperation.getResultSet;
import static main.DBOperation.getValues;

import java.awt.BorderLayout;
import java.awt.Container;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.DBOperation;

import static view.SuperGUI.*;

public class TableGUI {

	/**
	 * Tables frame
	 */
	private static JFrame tabFrame = new JFrame();

	public static JFrame getFrame() {
		return tabFrame;
	}

	public static void createTablesOP9(String p) {
		ResultSet rs = DBOperation.op9(p);
		getMainFrame().setVisible(false);

		JScrollPane sp = null;
		JTable tb = null;

		tabFrame = new JFrame();
		tabFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Container container = tabFrame.getContentPane();

		String[] columns = getColumnsName(rs);
		String[][] data = getValues(rs, columns.length);
		/*
		 * for (int i = 0; i < data.length; i++) { for (int j = 0; j < data[i].length;
		 * j++) { System.out.println("dato: --> " + data[i][j]); } }
		 */

		DefaultTableModel dtm = new DefaultTableModel(data, columns);

		tb = new JTable(dtm);

		sp = new JScrollPane(tb);

		JPanel tPanel = new JPanel(new BorderLayout());

		tPanel.add(sp);

		tabFrame.add(tPanel, BorderLayout.CENTER);
		tabFrame.setSize(900, 600);
		// frame.pack();
		tabFrame.setVisible(true);

	}

	/**
	 * Crea la tabella per una determinata operazione
	 * 
	 * @param n numero operazione
	 */
	public static void createTables(int n) {
		getMainFrame().setVisible(false);

		JScrollPane sp = null;
		JTable tb = null;

		tabFrame = new JFrame();
		tabFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Container container = tabFrame.getContentPane();

		String op = getOperation(n);
		// set restituito dalla query
		ResultSet rs = getResultSet(op);

		String[] columns = getColumnsName(rs);
		String[][] data = getValues(rs, columns.length);
		/*
		 * for (int i = 0; i < data.length; i++) { for (int j = 0; j < data[i].length;
		 * j++) { System.out.println("dato: --> " + data[i][j]); } }
		 */

		DefaultTableModel dtm = new DefaultTableModel(data, columns);

		tb = new JTable(dtm);

		sp = new JScrollPane(tb);

		JPanel tPanel = new JPanel(new BorderLayout());

		tPanel.add(sp);

		tabFrame.add(tPanel, BorderLayout.CENTER);
		tabFrame.setSize(900, 600);
		// frame.pack();
		tabFrame.setVisible(true);

	}

}
