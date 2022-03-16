package H02_Testaufgabe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Klasse {

	public static void dropTableKlasse(Connection c ) {
		try {
        	Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Klasse;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
//AUFGABE a)
	public static void createTableKlasse(Connection c) {
		try {
        	Statement stmt = c.createStatement();
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
		try {
			String sql = "INSERT INTO Klasse (name, Klassenvorstand) VALUES (?, ?)";
			PreparedStatement preStmt = c.prepareStatement(sql);
			
			preStmt.setString(1, name);
			preStmt.setString(2, klassenvorstand);
	        preStmt.executeUpdate();
	        
	        System.out.println("Klasse eingefuegt");
	        preStmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
    }
}
