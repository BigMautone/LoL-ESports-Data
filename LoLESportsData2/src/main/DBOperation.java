package main;

import static main.DBOperation.getColumnsName;
import static main.DBOperation.getValues;
import static main.ServerUtility.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import javax.swing.JOptionPane;

import view.InsertGUI;

import static view.InsertGUI.*;
import static view.OperationsGUI.*;
import static view.UpdateGUI.*;

public class DBOperation {

	private static String op1;
	private static String op2 = "SELECT Id,Nome,Cognome,Squadra, (year(CURDATE()) - year(Data_di_nascita)) as età, rateo(Uccisioni,Morti,Assist) as rateo\r\n"
			+ "FROM giocatore,contratto as c,statistiche as s\r\n"
			+ "WHERE (Id=s.Giocatore) AND (Id=c.Giocatore) AND (rateo(Uccisioni,Morti,Assist) > 2.00)\r\n"
			+ "AND (year(CURDATE()) - year(Data_di_nascita) < 20)\r\n"
			+ "AND (year(CURDATE()) - year(Data_inizio) <= 1);";
	private static String op3 = "SELECT DISTINCT Squadra_vincitrice as Squadra\r\n" + "FROM edizioni_torneo\r\n"
			+ "WHERE Torneo = \"Season World Championship\";";
	private static String op4 = "Select Id,Giocatore.nazionalità as Nazionalità\r\n"
			+ "from giocatore,contratto,squadra\r\n" + "where (giocatore.nazionalità = squadra.Nazionalità)\r\n"
			+ "and (Id = contratto.giocatore and scaduto = 0);";
	private static String op5 = "SELECT *\r\n" + "FROM staff as s1\r\n"
			+ "WHERE (Stipendio > any(SELECT Stipendio FROM staff as s2 WHERE Proprietario = 1 and s1.Squadra <> s2.Squadra)\r\n"
			+ "OR (Stipendio > any(SELECT Stipendio FROM contratto as c WHERE c.Squadra = s1.Squadra AND c.Scaduto=0)))\r\n"
			+ "AND Proprietario = 0;";
	private static String op6 = "SELECT *\r\n" + "FROM info_trofei_Giocatori\r\n" + "where Num_Trofei >= 2;";
	private static String op7 = "SELECT Id,Vittorie,t.tipo\r\n" + "FROM allenatore as a,trofei_squadra as t\r\n"
			+ "WHERE a.Squadra = t.Squadra \r\n" + "AND ((Vittorie >= 1 and t.Tipo = 'Mondiale')\r\n"
			+ "OR (Vittorie >= 2 and t.Tipo = 'Nazionale'))";
	private static String op8 = "SELECT Nome\r\n" + "FROM Squadra\r\n" + "WHERE EXISTS (select * \r\n"
			+ "			from partita \r\n" + "			where turno=5 \r\n"
			+ "			and ((Risultato=\"3-2\" and Nome = Squadra1) or (Risultato=\"2-3\" and Nome = Squadra2))\r\n"
			+ "			and Data_partita in (select Data_partita\r\n"
			+ "								from partita\r\n" + "								where turno=2\r\n"
			+ "								and (Risultato = \"0-2\" or Risultato = \"2-0\")));";
	private static String op9 = "SELECT Giocatore,Squadra, count(*) as Partite_vinte " 
			+ "FROM contratto as c, partita as p "  
			+ "WHERE Scaduto = 0 AND ((c.Squadra = p.Squadra1) OR (c.Squadra = p.Squadra2)) "
			+ "AND (Risultato LIKE \"3%\" OR Risultato LIKE \"%3\")	AND giocatore = ? group by Giocatore;";
	private static String op10 = "SELECT IF(Squadra1 < Squadra2, concat(Squadra1,\" - \", Squadra2),concat(Squadra2,\" - \", Squadra1)) as Squadre,count(*) as num_partite\r\n"
			+ "from partita as p1\r\n" + "GROUP BY Squadre \r\n" + "ORDER BY num_partite DESC\r\n" + "LIMIT 1;";
	private static String op11 = "Select *\r\n" + "FROM accordo\r\n"
			+ "WHERE (Squadra = (Select Squadra_vincitrice	\r\n" + "			FROM edizioni_torneo\r\n"
			+ "			Group by Squadra_vincitrice\r\n" + "			order by count(*) desc\r\n"
			+ "			limit 1));\r\n";
	private static String op12 = "SELECT Squadra_vincitrice,sum(montepremi) as Vincite_totali, count(*) as Trofei\r\n"
			+ "FROM torneo,edizioni_torneo\r\n" + "WHERE Nome = Torneo \r\n" + "GROUP BY squadra_vincitrice\r\n"
			+ "HAVING Vincite_totali = (SELECT max(vincite)\r\n"
			+ "			FROM (SELECT Squadra_vincitrice as Squadra,sum(montepremi) as vincite FROM torneo,edizioni_torneo WHERE Nome = Torneo Group by Squadra) as s1,\r\n"
			+ "            (SELECT Squadra_vincitrice as Squadra,count(*) as trofei FROM edizioni_torneo GROUP BY Squadra \r\n"
			+ "            HAVING trofei <= ALL (SELECT count(*) FROM edizioni_torneo GROUP BY Squadra_vincitrice)) as s2\r\n"
			+ "            WHERE s1.Squadra = s2.Squadra);";
	private static String op13 = "SELECT Giocatore,count(*) as Mondiali_vinti\r\n" + "FROM vinto as v1,Torneo\r\n"
			+ "WHERE Torneo = Nome AND tipo = 'Mondiale'\r\n" + "GROUP BY Giocatore\r\n"
			+ "HAVING Mondiali_vinti >= all (SELECT count(*)\r\n" + "						FROM vinto as v1,Torneo\r\n"
			+ "						WHERE Torneo = Nome AND tipo = 'Mondiale'\r\n"
			+ "                        Group by Giocatore);\r\n";
	private static String op14 = "SELECT Giocatore,Squadra,Stipendio\r\n" + "FROM contratto as c1\r\n"
			+ "WHERE Stipendio > all (SELECT Stipendio FROM contratto as c2\r\n"
			+ "                       WHERE (c1.Giocatore <> c2.Giocatore) and (c1.Squadra = c2.Squadra) and (Scaduto = 0));";

	/**
	 * Restituisce la query ricercata
	 * 
	 * @param n
	 * @return
	 */
	public static String getOperation(int n) {
		switch (n) {
		case 1:
			return op1;
		case 2:
			return op2;
		case 3:
			return op3;
		case 4:
			return op4;
		case 5:
			return op5;
		case 6:
			return op6;
		case 7:
			return op7;
		case 8:
			return op8;
		case 9:
			return op9;
		case 10:
			return op10;
		case 11:
			return op11;
		case 12:
			return op12;
		case 13:
			return op13;
		case 14:
			return op14;
		default:
			return "operazione non esistente";
		}
	}

	public static ResultSet op9(String p) {
		
		String q = op9;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(q);

			// Inserisco i parametri di input
			ps.setString(1, p);
			rs = ps.executeQuery();
			
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return rs;
	}
	
	/**
	 * Restituisce il resultset utile ad effettuare le operazioni lato front-end
	 * 
	 * @param op
	 * @return
	 */
	public static ResultSet getResultSet(String op) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(op);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return rs;
	}

	/**
	 * Restituisce tutte le colonne della query inserita
	 * 
	 * @param op
	 * @return
	 */
	public static String[] getColumnsName(ResultSet rs) {

		ArrayList<String> columns = new ArrayList<String>();

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for (int j = 1; j <= columnsNumber; j++) {
				columns.add(rsmd.getColumnName(j));
			}
			for (int j = 0; j < columns.size(); j++) {
			}

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		String[] newCol = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			newCol[i] = columns.get(i);
			// System.out.println("colonna --> " + newCol[i]);

		}

		return newCol;
	}

	/**
	 * Restituisce un "dizionario" contenente tutti i valori per ogni attributo
	 * richiesto dalla query
	 * 
	 * @param rs
	 * @param nCol numero colonne necessario a recuperare tutti i dati
	 */
	public static String[][] getValues(ResultSet rs, int nCol) {

		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < nCol; i++) {
			data.add(new ArrayList<String>());
		}

		try {
			while (rs.next()) {
				for (int i = 1; i <= nCol; i++) {
					String columnValue = rs.getString(i);
					data.get(i - 1).add(columnValue);
					// System.out.print(columnValue + ", ");
				}
				// System.out.println("");
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		String[][] newData = new String[data.get(0).size()][data.size()];
		for (int i = 0; i < data.get(0).size(); i++) {
			// System.out.print("[");
			for (int j = 0; j < data.size(); j++) {
				newData[i][j] = data.get(j).get(i);
				// System.out.print(newData[i][j]+", ");
			}
			// System.out.println("]");
		}

		return newData;
	}

	/**
	 * Estrae il nickname di tutti i giocatori dal server
	 * 
	 * @return
	 */
	public static String[] getPlayers() {
		String query = "SELECT Id from giocatore";

		ResultSet rs = getResultSet(query);

		String[][] ids = getValues(rs, 1);

		ArrayList<String> newIds = new ArrayList<String>();

		for (int j = 0; j < ids.length; j++) {
			newIds.add(ids[j][0]);
		}

		String[] a = new String[newIds.size()];
		newIds.toArray(a);

		/*
		 * for (int i = 0; i < ids[0].length; i++) { System.out.print("["); for (int j =
		 * 0; j < ids.length; j++) { System.out.print(ids[j][i]+", "); }
		 * System.out.println("]"); }
		 */

		return a;
	}

	public static String[] getSquads() {
		String query = "SELECT Nome from squadra";

		ResultSet rs = getResultSet(query);

		String[][] ids = getValues(rs, 1);

		ArrayList<String> newIds = new ArrayList<String>();

		for (int j = 0; j < ids.length; j++) {
			newIds.add(ids[j][0]);
		}

		String[] a = new String[newIds.size()];
		newIds.toArray(a);

		/*
		 * for (int i = 0; i < ids[0].length; i++) { System.out.print("["); for (int j =
		 * 0; j < ids.length; j++) { System.out.print(ids[j][i]+", "); }
		 * System.out.println("]"); }
		 */

		return a;
	}

	public static void doStatisticsUpdate(String p, int u, int m, int a) {

		String updateQuery = "UPDATE statistiche "
				+ "SET Uccisioni =(Uccisioni + ?), Morti = (Morti + ?), Assist = (Assist + ?)" + "WHERE Giocatore = ?";
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(updateQuery);

			// Inserisco i parametri di input
			ps.setInt(1, u);
			ps.setInt(2, m);
			ps.setInt(3, a);
			ps.setString(4, p);
			ps.executeUpdate();
			ps.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Mostra l'ultimo contratto inserito
	 * 
	 * @param p
	 * @param squad
	 * @return
	 */
	public static String[] getNewContractValues() {
		String s = "Select * from contratto where Giocatore = ? and squadra = ? order by Codice Desc LIMIT 1";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement(s);

			// Inserisco i parametri di input
			ps.setString(1, InsertGUI.getSelectedPlayer());
			ps.setString(2, InsertGUI.getSelectedSquad());
			rs = ps.executeQuery();
			// ps.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		String[][] ss = getValues(rs, getColumnsName(rs).length);

		ArrayList<String> contractVals = new ArrayList<String>();

		for (int j = 0; j < ss[0].length; j++) {
			contractVals.add(ss[0][j]);
		}

		String[] a = new String[contractVals.size()];
		contractVals.toArray(a);
		return a;
	}

	/**
	 * 
	 * @param p      giocatore
	 * @param squad  squadra
	 * @param stip   stipendio
	 * @param inizio data inizio
	 * @param fine   data fine
	 */
	public static int doContractInsert(String p, String squad, double stip, String inizio, String fine) {
		String insertQuery = "INSERT INTO contratto(Data_inizio,Data_fine,Stipendio,Scaduto,Squadra,Giocatore)"
				+ "VALUES (?,?,?,0,?,?)";
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(insertQuery);

			// Inserisco i parametri di input
			ps.setString(1, inizio);
			ps.setString(2, fine);
			ps.setDouble(3, stip);
			ps.setString(4, squad);
			ps.setString(5, p);
			ps.executeUpdate();
			ps.close();
			showNewContract();
			return 0;

		} catch (SQLException ex) {
			String errorCode = ex.getSQLState();
			/*
	 		System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			
			
			if (errorCode == "45000") {
				// TODO
			}*/
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return Integer.parseInt(ex.getSQLState());
		}
	}

	public static void main(String[] args) {
		/*
		 * Scanner scan = new Scanner(System.in);
		 * System.out.print("Inserisci il numero dell'operazione da effettuare: "); int
		 * n = scan.nextInt(); getConnection(); System.out.println("\n");
		 * doSelectQuery(n);
		 */

	}

}
