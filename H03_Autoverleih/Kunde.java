package H03_Autoverleih;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Kunde {
	
	public static void dropTableKunde(Connection c ) {
        try {
            Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Kunde;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableKunde(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Kunde(" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(30)," +
                    "email VARCHAR(25)," +
                    "Geburtsdatum DATE);";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	

    public static void insertIntoKunde(Connection c, String name, String email, LocalDate geburtsdatum) {
    	try {
    		String sql = "INSERT INTO Kunde (name, email, geburtsdatum) VALUES (?, ?, ?)";
			PreparedStatement preStmt = c.prepareStatement(sql);
			java.sql.Date sqlGeburtsdatum = java.sql.Date.valueOf(geburtsdatum);
			
			preStmt.setString(1, name);
			preStmt.setString(2, email);
	        preStmt.setDate(3, sqlGeburtsdatum);
	        preStmt.executeUpdate();
	        
	        System.out.println("Kunde eingefuegt");
	        preStmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
    	
    	
//        try {
//            Statement stmt = c.createStatement();
//            java.sql.Date sqlGeburtsdatum = java.sql.Date.valueOf(geburtsdatum);
//            
//            String sql = String.format("INSERT INTO Kunde (name, email, geburtsdatum) VALUES(\"%s\", \"%s\", \"" + sqlGeburtsdatum + "\");", name, email);
//            stmt.executeUpdate(sql);
//            
//            System.out.println("Kunde eingefügt");
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
