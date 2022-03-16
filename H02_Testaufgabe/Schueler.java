package H02_Testaufgabe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;

public class Schueler {
	public static void dropTableSchueler(Connection c ) {
		try {
        	Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Schueler;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
//AUFGABE a)	
	public static void createTableSchueler(Connection c) {
		try {
        	Statement stmt = c.createStatement();
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
	
	public static void insertIntoSchueler(Connection c, String name, LocalDate geburtsdatum) {
		try {
			String sql = "INSERT INTO Schueler (name, geburtsdatum) VALUES (?, ?)";
			PreparedStatement preStmt = c.prepareStatement(sql);
			java.sql.Date sqlGeburtsdatum = java.sql.Date.valueOf(geburtsdatum);
			
			preStmt.setString(1, name);
	        preStmt.setDate(2, sqlGeburtsdatum);
	        preStmt.executeUpdate();
	        
	        System.out.println("Schueler eingefuegt");
	        preStmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
