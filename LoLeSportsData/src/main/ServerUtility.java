package main;

import static main.DBOperation.getOperation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

/**
 * Classe contenente tutto cio che serve lato server (e.g. Connessione)
 * 
 * @author franc
 *
 */

public class ServerUtility {

	private static final String USER = "root";
	private static final String PASS = "root";

	private static Connection conn = null;

	/**
	 * Location del Driver jdbc(JConnector di MySQL)
	 */
	private final static String DRIVER = "com.mysql.cj.jdbc.Driver";

	/**
	 * URL del db
	 */
	private static final String URL = "jdbc:mysql://localhost:3306/esports";

	/**
	 * istanzio la connessione
	 * 
	 * @return
	 */
	public static Connection getConnection() {

		if (conn == null) {

			try {

				importDriver();

				// Istanzio la connessione con il sesso
				conn = DriverManager.getConnection(URL, USER, PASS);
				System.out.println("connessione effettuata");

			} catch (SQLException ex) {
				// handle the error
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		return conn;
	}

	/**
	 * Carica la classe contenente i driver del Java connector di MySQL
	 * 
	 * @return Driver JConnector
	 * @throws ClassNotFoundException Classe JDBC non trovata
	 */
	public static Class<?> importDriver() throws ClassNotFoundException {
		return Class.forName(DRIVER);
	}

	/**
	 * Esegue la query selezionata e restituisce il risultato
	 * @param n operazione selezionata
	 * @return
	 */
	public static void doSelectQuery(int n) {
		String op = getOperation(n);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(op);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for(int j = 1; j <= columnsNumber; j++) {				
				System.out.print(rsmd.getColumnName(j) + ", ");
			}
			System.out.println("");
			while (rs.next()) {
			    for (int i = 1; i <= columnsNumber; i++) {
			        String columnValue = rs.getString(i);
			        System.out.print(columnValue + ", ");
			    }
			    System.out.println("");
			}
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	
	public static void doStatisticsUpdate(String p,int u, int m, int a) {
		String updateQuery = "UPDATE statistiche "
							+ "SET Uccisioni =(Uccisioni+" + p + "), Morti = (Morti + "+ m + "), Assist = (Assist + " + a + ")" 
							+ "WHERE Giocatore = " +p;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(updateQuery);
			
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	

	public static void main(String[] args) {
		getConnection();
		doStatisticsUpdate("Faker", 10, 10, 10);
	}

}
