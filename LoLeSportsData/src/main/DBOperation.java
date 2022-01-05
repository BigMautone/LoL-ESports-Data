package main;

import static main.ServerUtility.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class DBOperation {
	
	private static String op1;
	private static String op2 = "SELECT Id,Nome,Cognome,Squadra, (year(CURDATE()) - year(Data_di_nascita)) as età, rateo(Uccisioni,Morti,Assist) as rateo\r\n"
						+ "FROM giocatore,contratto as c,statistiche as s\r\n"
						+ "WHERE (Id=s.Giocatore) AND (Id=c.Giocatore) AND (rateo(Uccisioni,Morti,Assist) > 2.00)\r\n"
						+ "AND (year(CURDATE()) - year(Data_di_nascita) < 20)\r\n"
						+ "AND (year(CURDATE()) - year(Data_inizio) <= 1);";
	private static String op3 = "SELECT DISTINCT Squadra_vincitrice as Squadra\r\n"
						 +"FROM edizioni_torneo\r\n"
						 +"WHERE Torneo = \"Season World Championship\";";
	private static String op4 = "Select Id,Giocatore.nazionalità as Nazionalità\r\n"
						+ "from giocatore,contratto,squadra\r\n"
						+ "where (giocatore.nazionalità = squadra.Nazionalità)\r\n"
						+ "and (Id = contratto.giocatore and scaduto = 0);";
	private static String op5 = "SELECT *\r\n"
						+ "FROM staff as s1\r\n"
						+ "WHERE (Stipendio > any(SELECT Stipendio FROM staff as s2 WHERE Proprietario = 1 and s1.Squadra <> s2.Squadra)\r\n"
						+ "OR (Stipendio > any(SELECT Stipendio FROM contratto as c WHERE c.Squadra = s1.Squadra AND c.Scaduto=0)))\r\n"
						+ "AND Proprietario = 0;";
	private static String op6 = "SELECT *\r\n"
						+ "FROM info_trofei_Giocatori\r\n"
						+ "where Num_Trofei >= 2;";
	private static String op7 = "SELECT Id,Vittorie,t.tipo\r\n"
						+ "FROM allenatore as a,trofei_squadra as t\r\n"
						+ "WHERE a.Squadra = t.Squadra \r\n"
						+ "AND ((Vittorie >= 1 and t.Tipo = 'Mondiale')\r\n"
						+ "OR (Vittorie >= 2 and t.Tipo = 'Nazionale'))";
	private static String op8 = "SELECT Nome\r\n"
						+ "FROM Squadra\r\n"
						+ "WHERE EXISTS (select * \r\n"
						+ "			from partita \r\n"
						+ "			where turno=5 \r\n"
						+ "			and ((Risultato=\"3-2\" and Nome = Squadra1) or (Risultato=\"2-3\" and Nome = Squadra2))\r\n"
						+ "			and Data_partita in (select Data_partita\r\n"
						+ "								from partita\r\n"
						+ "								where turno=2\r\n"
						+ "								and (Risultato = \"0-2\" or Risultato = \"2-0\")));";
	private static String op9 = "";
	private static String op10 = "SELECT IF(Squadra1 < Squadra2, concat(Squadra1,\" - \", Squadra2),concat(Squadra2,\" - \", Squadra1)) as Squadre,count(*) as num_partite\r\n"
							+ "from partita as p1\r\n"
							+ "GROUP BY Squadre \r\n"
							+ "ORDER BY num_partite DESC\r\n"
							+ "LIMIT 1;";
	private static String op11 = "Select *\r\n"
							+ "FROM accordo\r\n"
							+ "WHERE (Squadra = (Select Squadra_vincitrice	\r\n"
							+ "			FROM edizioni_torneo\r\n"
							+ "			Group by Squadra_vincitrice\r\n"
							+ "			order by count(*) desc\r\n"
							+ "			limit 1));\r\n";
	private static String op12 = "SELECT Squadra_vincitrice,sum(montepremi) as Vincite_totali, count(*) as Trofei\r\n"
							+ "FROM torneo,edizioni_torneo\r\n"
							+ "WHERE Nome = Torneo \r\n"
							+ "GROUP BY squadra_vincitrice\r\n"
							+ "HAVING Vincite_totali = (SELECT max(vincite)\r\n"
							+ "			FROM (SELECT Squadra_vincitrice as Squadra,sum(montepremi) as vincite FROM torneo,edizioni_torneo WHERE Nome = Torneo Group by Squadra) as s1,\r\n"
							+ "            (SELECT Squadra_vincitrice as Squadra,count(*) as trofei FROM edizioni_torneo GROUP BY Squadra \r\n"
							+ "            HAVING trofei <= ALL (SELECT count(*) FROM edizioni_torneo GROUP BY Squadra_vincitrice)) as s2\r\n"
							+ "            WHERE s1.Squadra = s2.Squadra);";
	private static String op13 = "SELECT Giocatore,count(*) as Mondiali_vinti\r\n"
							+ "FROM vinto as v1,Torneo\r\n"
							+ "WHERE Torneo = Nome AND tipo = 'Mondiale'\r\n"
							+ "GROUP BY Giocatore\r\n"
							+ "HAVING Mondiali_vinti >= all (SELECT count(*)\r\n"
							+ "						FROM vinto as v1,Torneo\r\n"
							+ "						WHERE Torneo = Nome AND tipo = 'Mondiale'\r\n"
							+ "                        Group by Giocatore);\r\n";

	private static String op14 = "SELECT Giocatore,Squadra,Stipendio\r\n"
							+ "FROM contratto as c1\r\n"
							+ "WHERE Stipendio > all (SELECT Stipendio \r\n"
							+ "					   FROM contratto as c2\r\n"
							+ "                       WHERE (c1.Giocatore <> c2.Giocatore) and (c1.Squadra = c2.Squadra) and (Scaduto = 0));";
	
	
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
			case default:
				return "operazione non esistente";
		}
	}
	
	/**
	 * Permette di aggiornare le statistiche di un giocatore
	 * @param p giocatore
	 * @param u nuove uccisioni
	 * @param m nuove morti
	 * @param a nuovi assist
	 */

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Inserisci il numero dell'operazione da effettuare: ");
		int n =scan.nextInt();
		getConnection();
		System.out.println("\n");
		doSelectQuery(n);
	}

}
