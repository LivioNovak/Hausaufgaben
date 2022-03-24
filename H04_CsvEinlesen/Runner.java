package H04_CsvEinlesen;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Runner {				
	static int newTable = 0;
	static String tableName = "";
	
	public static Connection getConnection(String url, String user, String password)  {
		//Class.forName("com.mysql.jdbc.Driver");
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//wird vorerst nicht verwendet
	public static void createTable(Connection c, String[] inhalt) {
		try {
			for (int i = 0; i < inhalt.length; i++) {
				if(inhalt[i].contains("id")) inhalt[i] += " INT";
				else if(inhalt[i].contains("preis")) inhalt[i] += " DOUBLE";
				else if(inhalt[i].contains("datum")) inhalt[i] += " DATE";
				else inhalt[i] += String.format(" VARCHAR(%s)", (inhalt[i].length() + 15));			//nicht so optimal
//				if (inhalt[i].contains("#"))												Primary Key dynamisch (funktinoiert so nicht, daher weggelassem)
			}
			
        	Statement stmt = c.createStatement();
            String sql = String.format("CREATE TABLE IF NOT EXISTS %s(" +
                    "%s PRIMARY KEY, %s, %s, %s);", tableName, inhalt[0], inhalt[1], inhalt[2], inhalt[3]);
           
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
		

	
	
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/infi04_AutoverleihTest";
		String user = "Livio";
		String password = "Livio";
		
		try {
			Connection conn = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			conn.setAutoCommit(true);
			
			System.out.println("\nDROP TABLES");
			KundeLeihtAuto.dropTableKundeLeihtAuto(conn);
			Auto.dropTableAuto(conn);
			Kunde.dropTableKunde(conn);
			
			System.out.println("\nCREATE TABLES");
			Auto.createTableAuto(conn);
			Kunde.createTableKunde(conn);
			KundeLeihtAuto.createTableKundeLeihtAuto(conn);
			System.out.println();
			
			Scanner scanner = new Scanner (new File("C:\\Users\\linov\\Desktop\\Autoverleih.csv"));
			while(scanner.hasNextLine()) {
				String row = scanner.nextLine();
				
				int anzInhalte = 0;
				boolean nichtLeer = false;
				for (int i = 0; i < row.length(); i++) {
					if(row.charAt(i) != ';') nichtLeer = true;
					else if (row.charAt(i) == ';' && nichtLeer == true) {
						anzInhalte++;
						nichtLeer = false;
					}
 				} 
				System.out.println(anzInhalte);
				if(anzInhalte == 0) newTable = 0;
				else {
					String[] inhalt = new String[anzInhalte];
					
					
					int index = 0;
					for (int i = 0; i < row.length(); i++) {
						if(inhalt[index] == null) inhalt[index] = "";				//mit += bleibt null vorne stehen
						char c = row.charAt(i);
						if(c != ';') inhalt[index] += c;
						else index++;
					}

					if (newTable == 0) {
						tableName = inhalt[0];
						newTable++;
					}
					else if(newTable == 1) {										//ursprünglich sollte man auch die spalten erstellen können, indem man deren namen angibt, allerdings gibt es dann probleme mit den keys, daher bleibt es für's erste so
//						createTableAuto(conn, inhalte);
						newTable++;
					}
					else {
						if(tableName.equals("Auto")) Auto.insertIntoAuto(conn, inhalt);
						else if(tableName.equals("Kunde")) Kunde.insertIntoKunde(conn, inhalt);
						else if(tableName.equals("KundeLeihtAuto")) KundeLeihtAuto.insertIntoKundeLeihtAuto(conn, inhalt);
				
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
