package H02_Testaufgabe;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;

public class Schueler {
	public static void dropTableSchueler(Connection c ) {
		Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Schueler;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
//AUFGABE a)	
	public static void createTableSchueler(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Schueler(" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(30)," +
                    "geburtsdatum DATE);";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void insertIntoSchueler(Connection c, String name, LocalDate ld) {
        Statement stmt;
        java.sql.Date sqlLd = java.sql.Date.valueOf(ld);
        
        try {
            stmt = c.createStatement();
            String sql = String.format("INSERT INTO Schueler (name, geburtsdatum) VALUES(\"%s\", \"%s\");", name, sqlLd);
            stmt.executeUpdate(sql);
            
            System.out.println("Schueler eingefügt");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
