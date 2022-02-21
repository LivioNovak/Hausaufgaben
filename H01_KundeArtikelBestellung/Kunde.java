package H01_KundeArtikelBestellung;

import java.sql.*;


public class Kunde {
	
	public static void createTableKunde(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Kunde;";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Kunde" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
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
            String sql = "insert into Kunde (name, email) values" +
                    "(\"" + name + "\", \"" + email +"\");";
            System.out.println("Kunde eingefügt");
            
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
