package H03_Autoverleih;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.time.LocalDate;

import A02_Autoverleih.Auto;
import A02_Autoverleih.Kunde;
import A02_Autoverleih.KundeLeihtAuto;

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
		String url = "jdbc:mysql://localhost:3306/infi03_Autoverleih";
		String user = "Livio";
		String password = "Livio";
		
		try {
			Connection c = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			c.setAutoCommit(true);

			System.out.println("TABELLEN-LÖSCHEN");
			KundeLeihtAuto.dropTableKundeLeihtAuto(c);
			Auto.dropTableAuto(c);
			Kunde.dropTableKunde(c);
			System.out.println("Erfolgreich");
			
			System.out.println("\nTABELLEN-ERSTELLEN");
			Auto.createTableAuto(c);
			Kunde.createTableKunde(c);
			KundeLeihtAuto.createTableKundeLeihtAuto(c);
			System.out.println("Erfolgreich");
			
			System.out.println("\nAUTO");
			Auto.insertIntoAuto(c, "IL-549IH", "Dacia Duster", 12.40, LocalDate.of(2017, 11, 29));
			Auto.insertIntoAuto(c, "IL-1RGBA", "VW Golf", 8.99, LocalDate.of(2016, 7, 2));
			Auto.insertIntoAuto(c, "IM-17CDI", "Tesla Model x", 50.00, LocalDate.of(2021, 3, 14));
			
			System.out.println("\nKUNDE");
			Kunde.insertIntoKunde(c, "Karl Werner", "KarlWerner@hindi.ind", LocalDate.of(2002, 4, 4));
			Kunde.insertIntoKunde(c, "Julie Altfrau", "Schifoarerin77@gmx.com", LocalDate.of(1977, 11, 20));
			Kunde.insertIntoKunde(c, "Stefan Adler", "StefanAdler@cni.at", LocalDate.of(1992, 12, 31));
			
			System.out.println("\nSCHUELER ZU KLASSE");
			KundeLeihtAuto.insertIntoKundeLeihtAuto(c, 3, "IL-549IH", LocalDate.of(2022, 3, 16));
			KundeLeihtAuto.insertIntoKundeLeihtAuto(c, 1, "IM-17CDI", LocalDate.of(2022, 3, 25));
			KundeLeihtAuto.insertIntoKundeLeihtAuto(c, 2, "IL-1RGBA", LocalDate.of(2022, 5, 12));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}