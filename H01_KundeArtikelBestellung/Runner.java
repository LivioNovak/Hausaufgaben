package H01_KundeArtikelBestellung;

import java.sql.*;


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
		String url = "jdbc:mysql://localhost:3306/infi01_artkunbest";
		String user = "Livio";
		String password = "Livio";
		
		try {
			Connection c = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			c.setAutoCommit(true);
			
			System.out.println("TABELLEN-LÖSCHEN");
			Bestellung.dropTableBestellung(c);
			Artikel.dropTableArtikel(c);
			Kunde.dropTableKunde(c);
			System.out.println("Erfolgreich");
			
			System.out.println("\nTABELLEN-ERSTELLEN");
			Artikel.createTableArtikel(c);
			Kunde.createTableKunde(c);
			Bestellung.createTableBestellung(c);
			System.out.println("Erfolgreich");
			
			System.out.println("\nARTIKEL");
			Artikel.insertIntoArtikel(c, "Valo_Skins", 27.60);
			Artikel.insertIntoArtikel(c, "Vbugs", 100.00);
			Artikel.insertIntoArtikel(c, "WD40", 6.99);
			
			System.out.println("\nKUNDE");
			Kunde.insertIntoKunde(c, "Simon Vierlovic", "simon.vierlo@gmx.com");
			Kunde.insertIntoKunde(c, "Mattias Hohenzoller", "mazoller@tsn.at");
			Kunde.insertIntoKunde(c, "KarlWerner", "Werner.Karl@KalWerner.ind");
			
			System.out.println("\nBESTELLUNG");
			Bestellung.insertIntoBestellung(c, 1, 2, "2021-07-16", 3);
			Bestellung.insertIntoBestellung(c, 2, 3, "2022-01-04", 2);
			Bestellung.insertIntoBestellung(c, 3, 2, "2021-11-11", 10);
			Bestellung.insertIntoBestellung(c, 2, 1, "2022-02-21", 3);
			
			System.out.println("\nSELECT-BESTELLUNG");
			Bestellung.selectBestellung(c, 1);
			
			
			//a)
			System.out.println("\n\nERWEITERUNG a)");
			Bestellung.deleteFromBestellung(c, 1, 2, "2021-07-16");
			
			//b)
			System.out.println("\nERWEITERUNG c)");
			Bestellung.updateBestellung(c, 2, 3, 7 ,"2022-01-04");

			
			//c)
			System.out.println("\nERWEITERUNG b)");
			Artikel.AlterArtikel(c, "lagerbestand", "INTEGER");
			Artikel.updateLagerbestandArtikel(c, 2, 7);
			
			
			//d)
			System.out.println("\nERWEITERUNG d)");
			if(Artikel.LagerbestandReicht(c, 2, 3))  System.out.println("Bestellung wurde verarbeitet");
			else System.out.println("Der gewünschte Artikel ist nicht in ausreichender Menge verfügbar!");
			
			//e)
			System.out.println("\nERWEITERUNG e)");
			Bestellung.bestellenMitLagerbestand(c, 2, 2, "2022-02-07", 3);
			c.close();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
}
