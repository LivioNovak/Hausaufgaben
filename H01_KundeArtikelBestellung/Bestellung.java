package H01_KundeArtikelBestellung;

import java.sql.*;


public class Bestellung {
	
	public static void dropTableBestellung(Connection c) {
		Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Bestellung;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
       
	public static void createTableBestellung(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Bestellung" +
                    "(artikelid INTEGER NOT NULL," +
                    "kundenid INTEGER NOT NULL," +
                    "bestelldatum DATE NOT NULL," +
                    "anzahl INTEGER," +
                    "PRIMARY KEY(kundenid, artikelid, bestelldatum)," +
                    "FOREIGN KEY(kundenid) REFERENCES Kunde(id)," +
                    "FOREIGN KEY(artikelid) REFERENCES Artikel(id));";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void insertIntoBestellung(Connection c, int artikelid, int kundenid, String bestelldatum, int anzahl) {
        Statement stmt;
        try { 
            stmt = c.createStatement();
            String sql = String.format("INSERT INTO Bestellung (artikelid, kundenid, bestelldatum, anzahl) VALUES(%d, %d, \"%s\", %d);", artikelid, kundenid, bestelldatum, anzahl);
            System.out.println("Bestellungen eingefügt");
            
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
	public static void selectBestellung(Connection c, int kundenid) {
		try {
			Statement stmt = c.createStatement();
			String sql = String.format("SELECT k.name, a.bezeichnung, b.anzahl, " +
					"(SELECT a.preis * b.anzahl FROM Artikel a INNER JOIN Bestellung b ON a.id = b.artikelID WHERE b.kundenid = %d)AS gesamtPreis " + 
					"FROM Kunde k INNER JOIN Bestellung b ON k.id = b.kundenid " +
					"INNER JOIN Artikel a ON a.id = b.artikelid " +
					"WHERE b.kundenid = %d;", kundenid, kundenid);
			ResultSet rs = stmt.executeQuery(sql);
			
			while ( rs.next() ) {
		         String  name = rs.getString("name");
		         String  bezeichnung = rs.getString("bezeichnung");
		         int anzahl = rs.getInt("anzahl");
		         double gesamtPreis = rs.getDouble("gesamtPreis");
		         
		         System.out.printf("KUNDE = %s\n", name );
		         System.out.printf("ARTIKEL = %s\n", bezeichnung);
		         System.out.printf("ANZAHL = %d\n", anzahl );
		         System.out.printf("GESAMTPREIS = %.2f€\n", gesamtPreis);
		      }
			System.out.println("Bestellung ausgegeben");
			
		    rs.close();
		    stmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//ERWEITERUNGS-AUFGABE a)	
	public static void deleteFromBestellung(Connection c, int artikelid, int kundenid, String bestelldatum) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = String.format("DELETE FROM Bestellung WHERE artikelid = %d AND kundenid = %d AND bestelldatum = \"%s\";", artikelid, kundenid, bestelldatum);
            stmt.executeUpdate(sql);
            
            System.out.println(String.format("Kunde %d hat die Bestellung von Artikel %d gelöscht", kundenid, artikelid));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
//ERWEITERUNGS-AUFGABE b)	
	public static void updateBestellung(Connection c, int artikelid, int kundenid, int anzahl, String bestelldatum) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = String.format("UPDATE Bestellung SET anzahl = %d WHERE artikelid = %d AND kundenid = %d AND bestelldatum = \"%s\";", anzahl, artikelid, kundenid, bestelldatum);
            stmt.executeUpdate(sql);
            
            System.out.println(String.format("Kunde %d hat die Bestellmenge des Artikels %d auf %d Stück geändert", kundenid, artikelid, anzahl));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
//ERWEITERUNGS-AUFGABE e)
	public static void bestellenMitLagerbestand(Connection c, int artikelid, int kundenid, String bestelldatum, int anzahl) {
		if(Artikel.LagerbestandReicht(c, artikelid, anzahl)) {
			int neuerLagerbestand = Artikel.selectArtikelLagerbestand(c, artikelid) - anzahl;
			
			insertIntoBestellung(c, artikelid, kundenid, bestelldatum, anzahl);
			Artikel.updateLagerbestandArtikel(c, artikelid, neuerLagerbestand);
			System.out.println("Der Artikel wurde bestellt!");
		}
		else System.out.println("Der gewünschte Artikel ist nicht in ausreichender Menge verfügbar!");
	}
}
