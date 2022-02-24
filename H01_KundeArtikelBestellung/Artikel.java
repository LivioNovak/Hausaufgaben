package H01_KundeArtikelBestellung;

import java.sql.*;

public class Artikel {
	
	public static void dropTableArtikel(Connection c ) {
		Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Artikel;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableArtikel(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Artikel(" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "bezeichnung VARCHAR(30)," +
                    "preis DOUBLE);";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoArtikel(Connection c, String bezeichnung, double preis) {
        Statement stmt;
        try {
            stmt = c.createStatement();
//          String sql = String.format("INSERT INTO Artikel (bezeichnung, preis) VALUES(\"%s\", %f);", bezeichnung, preis);
            String sql = String.format("INSERT INTO Artikel (bezeichnung, preis) VALUES(\"%s\", " + preis + ");", bezeichnung);
            stmt.executeUpdate(sql);
            
            System.out.println("Artikel eingefügt");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
//ERWEITERUNGS-AUFGABE c)
    public static void AlterArtikel(Connection c, String bezeichnung, String datentyp) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = String.format("ALTER TABLE Artikel ADD COLUMN %s %s;", bezeichnung, datentyp);            
            stmt.executeUpdate(sql);
            
            System.out.println("Artikel-Tabelle wurde um Lagerbestand erweitert");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateLagerbestandArtikel (Connection c, int artikelid, int lagerbestand) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = String.format("UPDATE Artikel SET lagerbestand = %d WHERE id = %d;", lagerbestand, artikelid);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
 
//ERWEITERUNGS-AUFGABE d)
  	public static boolean LagerbestandReicht(Connection c, int artikelid, int bestellAnzahl) {
  		int lagerbestand = 0;
  		try {
  			Statement stmt = c.createStatement();
  			String sql = String.format("SELECT lagerbestand FROM Artikel WHERE id = %d;", artikelid);
  			ResultSet rs = stmt.executeQuery(sql);
  			
  			while ( rs.next() ) {
  				lagerbestand = rs.getInt("lagerbestand");
  		    }
  			rs.close();
  		    stmt.close();
  			
  		} catch (SQLException e) {
  			e.printStackTrace();
  		}
  		if(lagerbestand >= bestellAnzahl) return true;
		return false;
  	}

  	
//ERWEITERUNGS-AUFGABE e)
  	public static int selectArtikelLagerbestand(Connection c, int artikelid) {
        int lagerbestand = 0;
  		Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = String.format("SELECT lagerbestand from Artikel WHERE id = %d;", artikelid);
            //stmt.executeUpdate(sql);
            ResultSet rs = stmt.executeQuery(sql);
            
			while ( rs.next() ) {
		         lagerbestand = rs.getInt("lagerbestand");		         
		    }			
		    rs.close();
		    stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lagerbestand;
    }

}
