package H03_Autoverleih;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Auto {

	public static void dropTableAuto(Connection c ) {
		try {
        	Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS Auto;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
//AUFGABE a)
	public static void createTableAuto(Connection c) {
		try {
        	Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Auto(" +
                    "kennzeichen VARCHAR(8) PRIMARY KEY," +
                    "modell VARCHAR(30)," +
                    "PreisProTag double," +
                    "Zulassungsdatum DATE);";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void insertIntoAuto(Connection c, String kennzeichen, String modell, double preisProTag, LocalDate zulassungsdatum) {
		try {
			String sql = "INSERT INTO Auto VALUES (?, ?, ?, ?)";
			PreparedStatement preStmt = c.prepareStatement(sql);
			java.sql.Date sqlZulassungsdatum = java.sql.Date.valueOf(zulassungsdatum);
			
			preStmt.setString(1, kennzeichen);
			preStmt.setString(2, modell);
	        preStmt.setDouble(3, preisProTag);
	        preStmt.setDate(4, sqlZulassungsdatum);
	        preStmt.executeUpdate();
	        
	        System.out.println("Auto eingefuegt");
	        preStmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
//		try {
//        	Statement stmt = c.createStatement();
//        	java.sql.Date sqlZulassungsdatum = java.sql.Date.valueOf(zulassungsdatum);
//        	
//            String sql = String.format("INSERT INTO Auto VALUES(\"%s\", \"%s\", " + preisProTag + ", \"" + sqlZulassungsdatum + "\");", kennzeichen, modell);
//            stmt.executeUpdate(sql);
//            
//            System.out.println("Auto eingefügt");
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}