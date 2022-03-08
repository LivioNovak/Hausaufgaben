package H02_Testaufgabe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.time.LocalDate;

public class Runner {
	public static Connection getConnection(String url, String user, String password)  {
		//Class.forName("com.mysql.jdbc.Driver");
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
	
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/infi02_testbeispiel";
		String user = "Livio";
		String password = "Livio";
		
		try {
			Connection c = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			c.setAutoCommit(true);
			
			System.out.println("TABELLEN-LÖSCHEN");
			SchuelerZuKlasse.dropTableSchuelerZuKlasse(c);
			Schueler.dropTableSchueler(c);
			Klasse.dropTableKlasse(c);
			System.out.println("Erfolgreich");
			
			System.out.println("\nTABELLEN-ERSTELLEN");
			Schueler.createTableSchueler(c);
			Klasse.createTableKlasse(c);
			SchuelerZuKlasse.createTableSchuelerZuKlasse(c);
			System.out.println("Erfolgreich");
			
			System.out.println("\nSCHUELER");
			Schueler.insertIntoSchueler(c, "Johann Sebastian", LocalDate.of(2004, 11, 30));
			Schueler.insertIntoSchueler(c, "Julian Altmann", LocalDate.of(2005, 7, 2));
			Schueler.insertIntoSchueler(c, "Jasmin Muster", LocalDate.of(2003, 3, 14));
			
			System.out.println("\nKLASSEN");
			Klasse.insertIntoKlasse(c, "1BHET", "Hans-Joerg Jason");
			Klasse.insertIntoKlasse(c, "3AHWI", "Sasha Jambor");
			
			System.out.println("\nSCHUELER ZU KLASSE");
			SchuelerZuKlasse.insertIntoSchulerZuKlasse(c, "Johann Sebastian", "1BHET", LocalDate.of(2018, 9, 14));
			SchuelerZuKlasse.insertIntoSchulerZuKlasse(c, "Julian Altmann", "1BHET", LocalDate.of(2018, 9, 14));
			SchuelerZuKlasse.insertIntoSchulerZuKlasse(c, "Jasmin Muster", "3AHWI", LocalDate.of(2016, 9, 8));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
