package H02_Testaufgabe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.time.LocalDate;

public class SchuelerZuKlasse {
	public static void dropTableSchuelerZuKlasse(Connection c ) {
        try {
        	Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS SchuelerZuKlasse;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	
//AUFGABE b)
	public static void createTableSchuelerZuKlasse(Connection c) {
		try {
        	Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SchuelerZuKlasse" +
                    "(schuelerid INTEGER NOT NULL," +
                    "klassenid INTEGER NOT NULL," +
                    "zuteilungsdatum DATE NOT NULL," +
                    "PRIMARY KEY(schuelerid, klassenid, zuteilungsdatum)," +
                    "FOREIGN KEY(schuelerid) REFERENCES Schueler(id) ON DELETE Restrict," +
                    "FOREIGN KEY(klassenid) REFERENCES Klasse(id) ON DELETE Restrict);";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
	public static void insertIntoSchulerZuKlasse(Connection c, String schuelername, String klassenname) {
		try {
			int schuelerid = 0;
			int klassenid = 0;
			java.sql.Date sqlZuteilungsdatum = java.sql.Date.valueOf(LocalDate.now());
			
			String sql = "SELECT id FROM schueler where name = ?";
			PreparedStatement preStmt = c.prepareStatement(sql);
			preStmt.setString(1, schuelername);
			
			ResultSet rs = preStmt.executeQuery();
			while(rs.next()) {
				schuelerid = rs.getInt("id");
			}
			
			
			sql = "SELECT id FROM klasse where name = ?";
			preStmt = c.prepareStatement(sql);
			preStmt.setString(1, klassenname);
			
			rs = preStmt.executeQuery();
			while(rs.next()) {
				klassenid = rs.getInt("id");
			}
			
	        
	        sql = "INSERT INTO SchuelerZuKlasse VALUES (?, ?, ?)";
	        preStmt = c.prepareStatement(sql);
	        preStmt.setInt(1, schuelerid);
	        preStmt.setInt(2, klassenid);
	        preStmt.setDate(3, sqlZuteilungsdatum);
	        preStmt.executeUpdate();
	        
	        
	        System.out.printf("Schueler %d in Klasse %d eingefuegt\n", schuelerid, klassenid);
	        preStmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
