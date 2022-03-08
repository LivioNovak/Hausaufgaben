package H02_Testaufgabe;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Klasse {

	public static void dropTableKlasse(Connection c ) {
		Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Klasse;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
//AUFGABE a)
	public static void createTableKlasse(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Klasse(" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name CHAR(5)," +
                    "Klassenvorstand VARCHAR(30));";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void insertIntoKlasse(Connection c, String name, String klassenvorstand) {
        Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = String.format("INSERT INTO Klasse (name, klassenvorstand) VALUES(\"%s\", \"%s\");", name, klassenvorstand);
            stmt.executeUpdate(sql);
            
            System.out.println("Klasse eingefügt");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
