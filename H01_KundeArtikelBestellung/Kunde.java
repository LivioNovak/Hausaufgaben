package H01_KundeArtikelBestellung;

import java.sql.*;


public class Kunde {
	
	public static void dropTableKunde(Connection c ) {
		Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Kunde;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableKunde(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Kunde(" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(30)," +
                    "email VARCHAR(25));";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	

    public static void insertIntoKunde(Connection c, String name, String email) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = String.format("INSERT INTO Kunde (name, email) VALUES(\"%s\", \"%s\");", name, email);
            stmt.executeUpdate(sql);
            
            System.out.println("Kunde eingefügt");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
