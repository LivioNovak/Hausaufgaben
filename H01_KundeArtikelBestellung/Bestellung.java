package H01_KundeArtikelBestellung;

import java.sql.*;


public class Bestellung {
	public static void createTableBestellung(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Bestellung;";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Bestellung" +
                    "(artikelid INTEGER NOT NULL," +
                    "kundenid INTEGER NOT NULL," +
                    "bestelldatum DATE NOT NULL," +
                    "anzahl INTEGER," +
                    "PRIMARY KEY(kundenid, artikelid, bestelldatum)" +
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
            String sql = "insert into Bestellung (artikelid, kundenid, bestelldatum, anzahl) values" +
                    "(" + artikelid + ", "  + kundenid + ", \"" + bestelldatum + "\", " + anzahl + ");";
            System.out.println("Bestellungen eingefügt");
            
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
	public static void selectBestellung(Connection c, int kunde) {
		try {
			Statement stmt = c.createStatement();
			String sql = "SELECT k.name, a.bezeichnung, b.anzahl, " +
					"(SELECT a.preis * b.anzahl FROM Artikel a INNER JOIN Bestellung b ON a.id = b.artikelID WHERE b.kundenid == " + kunde + ")AS gesamtPreis " + 
					"FROM Kunde k INNER JOIN Bestellung b ON k.id == b.kundenid " +
					"INNER JOIN Artikel a ON a.id == b.artikelid " +
					"WHERE b.kundenid == " + kunde + ";";
			ResultSet rs = stmt.executeQuery(sql);
			
			while ( rs.next() ) {
		         String  name = rs.getString("name");
		         String  bezeichnung = rs.getString("bezeichnung");
		         int anzahl = rs.getInt("anzahl");
		         double gesamtPreis = rs.getDouble("gesamtPreis");
		         
		         System.out.println("NAME = " + name );
		         System.out.println("BEZEICHNUNG = " + bezeichnung );
		         System.out.println("ANZAHL = " + anzahl );
		         System.out.printf("GESAMTPREIS = %.2f€", gesamtPreis);
		         System.out.println();
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
            String sql = "DELETE FROM Bestellung WHERE artikelid =" + artikelid + " AND kundenid =" + kundenid + " AND bestelldatum = " + bestelldatum + ";";
            stmt.executeUpdate(sql);
            System.out.println("Kunde " + kundenid + " hat die Bestellung von Artikel "+ artikelid + " gelöscht");
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
            String sql = "UPDATE Bestellung SET anzahl = " + anzahl +  " WHERE artikelid =" + artikelid + " AND kundenid =" + kundenid + " AND bestelldatum = " + bestelldatum + ";";
            stmt.executeUpdate(sql);
            
            System.out.println("Kunde " + kundenid + " hat die Bestellmenge des Artikels " + artikelid + " auf " + anzahl + " Stück geändert");
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
