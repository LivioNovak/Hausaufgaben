package H03_Autoverleih;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class KundeLeihtAuto {
	public static void dropTableKundeLeihtAuto(Connection c ) {
        try {
            Statement stmt = c.createStatement();
            String sql = "DROP TABLE IF EXISTS KundeLeihtAuto;";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void createTableKundeLeihtAuto(Connection c) {
        try {
            Statement stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS KundeleihtAuto(" +
                    "kundenid INT," +
                    "kennzeichen VARCHAR(8)," +
                    "beginn DATE," +
                    "ende DATE," +
                    "PRIMARY KEY(kundenid, kennzeichen, beginn)," +
                    "FOREIGN KEY(kundenid) REFERENCES Kunde(id) ON DELETE Restrict," +
                    "FOREIGN KEY(kennzeichen) REFERENCES Auto(kennzeichen) ON DELETE Restrict);";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void insertIntoKundeLeihtAuto (Connection c, int kundenid, String kennzeichen, LocalDate ende) {
		try {
			String sql = "INSERT INTO KundeLeihtAuto VALUES (?, ?, ?, ?);";
			PreparedStatement preStmt = c.prepareStatement(sql);
			java.sql.Date sqlBeginn = java.sql.Date.valueOf(LocalDate.now());
			java.sql.Date sqlEnde = java.sql.Date.valueOf(ende);
			
			preStmt.setInt(1, kundenid);
			preStmt.setString(2, kennzeichen);
	        preStmt.setDate(3, sqlBeginn);
	        preStmt.setDate(4, sqlEnde);
	        preStmt.executeUpdate();
			
			System.out.printf("Kunde %d hat Auto %s ausgeliehen\n", kundenid, kennzeichen);
            preStmt.close();
		} catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
