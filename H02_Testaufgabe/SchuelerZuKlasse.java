package H02_Testaufgabe;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.time.LocalDate;

public class SchuelerZuKlasse {
	public static void dropTableSchuelerZuKlasse(Connection c ) {
		Statement stmt;
        try {
            stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS SchuelerZuKlasse;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	
//AUFGABE b)
	public static void createTableSchuelerZuKlasse(Connection c) {
        Statement stmt;
        try {
            stmt = c.createStatement();
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
	
	
	public static void insertIntoSchulerZuKlasse(Connection c, String schuelername, String klassenname, LocalDate ld) {
		Statement stmt;
		int schuelerid = 0;
		int klassenid = 0;
		java.sql.Date sqlLd = java.sql.Date.valueOf(ld);
		
		try {
			stmt = c.createStatement();
			String sql = String.format("SELECT id FROM schueler where name = \"%s\"", schuelername);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				schuelerid = rs.getInt("id");
			}
			
			sql = String.format("SELECT id FROM klasse where name = \"%s\"", klassenname);
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				klassenid = rs.getInt("id");
			}
			
			sql = String.format("INSERT INTO SchuelerZuKlasse VALUES(%d, %d, \"%s\");", schuelerid, klassenid, sqlLd);
			stmt.executeUpdate(sql);
			
			System.out.println(String.format("Schueler %d der Klasse %d zugeteilt.", schuelerid, klassenid));
			rs.close();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
