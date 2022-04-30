package H04_CsvEinlesen;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;


public class Import {				

	public static Connection getConnection(String url, String user, String password)  {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	public static void dropTables(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			String sql = "SET FOREIGN_KEY_CHECKS = 0;";
			stmt.executeUpdate(sql);
			
			sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'infi04_Schuelerverwaltung';";
			ResultSet rs = stmt.executeQuery(sql);
			
			String temp = "";
			while(rs.next()) {
				temp += String.format("DROP TABLE IF EXISTS %s;", rs.getString("table_name"));
			}
			String[] dropSqls = temp.split(";");
			for (int i = 0; i < dropSqls.length; i++) {
				if(!dropSqls[i].equals("")) stmt.executeUpdate(dropSqls[i]);
			}
			
			sql = "SET FOREIGN_KEY_CHECKS = 1;";
			stmt.executeUpdate(sql);
			
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	public static String primKeys(String[] contents) {
		String primKeys = "PRIMARY KEY(";
		int numOfPrimKey = 0;
		for (int i = 0; i < contents.length; i++) {
			if(contents[i].contains("#")) {
				numOfPrimKey++;
				if(numOfPrimKey > 1) primKeys += ", ";
				primKeys += contents[i].split("#")[0];
			}
		}
		return primKeys + ")";
	}


	
	public static String[][] createTable(Connection conn, String tableName, String[] contents) {
		try {
			String[][] columns = new String[contents.length][3];
			
			for (int i = 0; i < contents.length; i++) {
				columns[i][0] = contents[i].split("#")[0];
				
				if (contents[i].contains("id")) columns[i][1] = "INT";
				else if (contents[i].contains("preis")) columns[i][1] = "DOUBLE";
				else if (contents[i].contains("datum")) columns[i][1] = "DATE";
				else {
					columns[i][1] = String.format("VARCHAR(%d)", contents[0].length());
					columns[i][2] = String.format("%d", contents[0].length());
				}
			}
			String keys = primKeys(contents);

			Statement stmt = conn.createStatement();

			String sql = String.format("CREATE TABLE IF NOT EXISTS %s(", tableName);
			for (int i = 0; i < columns.length; i++) {
				sql += String.format("%s %s, ", columns[i][0], columns[i][1]);
			}
			sql += String.format("%s);", keys);
			stmt.executeUpdate(sql);
			stmt.close();
			return columns;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	
	
	public static void insertInto(Connection conn, String tableName, String[][] columns, String[] contents) {
		try {
			String sql = String.format("INSERT INTO %s VALUES(", tableName);
			for (int i = 0; i < columns.length; i++) {
				sql += "?";
				if(i < columns.length - 1) sql += ", ";
			}
			sql += ");";

			PreparedStatement preStmt = conn.prepareStatement(sql);
			for (int i = 0; i < columns.length; i++) {
				//columns[i][1] -> Datentyp
				if(columns[i][1].equals("INT")) preStmt.setInt((i + 1), Integer.parseInt(contents[i]));
				else if(columns[i][1].equals("DOUBLE")) preStmt.setDouble((i + 1), Double.parseDouble(contents[i]));
				else if(columns[i][1].equals("DATE")) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
					LocalDate datum = LocalDate.parse(contents[i], formatter);
					preStmt.setDate((i + 1), java.sql.Date.valueOf(datum));
				}
				else { //VARCHAR
					if(contents[i].length() > Integer.parseInt(columns[i][2])) {	//alter Table wenn contents > laenge des VARCHAR()
						columns[i][2] = String.format("%d", contents[i].length());

						Statement stmt = conn.createStatement();
						String alterSql = String.format("ALTER TABLE %s MODIFY %s VARCHAR(%d);", tableName, columns[i][0], contents[i].length());

						stmt.executeUpdate(alterSql);
						stmt.close();
					}
					preStmt.setString((i + 1), contents[i]);
				}
			}
			preStmt.executeUpdate();
			preStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//für etwas schönere ausgabe --> zuerst alles im String speichern, dann println
	public static void select(Connection conn, String tableName, String[][] columns) {
		try {
			for (int i = 0; i < columns.length; i++) {
				System.out.print(columns[i][0]);
				if(i < columns.length - 1) System.out.print(";");
			}
			System.out.println();

			String sql = String.format("SELECT * FROM %s;", tableName);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String row = "";
				for (int i = 0; i < columns.length; i++) {
					switch (columns[i][1]) {
					case "INT": row += String.format("%d",rs.getInt(columns[i][0])); break;
					case "DOUBLE": row += String.format("%f", rs.getDouble(columns[i][0])); break;
					case "DATE": row += " " + rs.getDate(columns[i][0]); break;
					default: row += String.format("%s", rs.getString(columns[i][0])); break;
					}
					if(i < columns.length - 1) row += ";";
				}
				System.out.println(row);
			}
			System.out.println();
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	
	
	public static void main(String[] args) {
		try {
			String configFilePath = "Hausaufgaben/H04_CsvEinlesen/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput); 
            
            String tableName = prop.getProperty("table_name");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");

			
			Connection conn = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			conn.setAutoCommit(true);

			dropTables(conn);

			Scanner scanner = new Scanner (new File(prop.getProperty("csvFilePath")));
			
			String[][] columns; 
			if(scanner.hasNextLine()) {
				String[] contents = scanner.nextLine().split(";");
				for (int i = 0; i < contents.length; i++) {
					contents[i].trim();
				}
				columns = createTable(conn, tableName, contents);
			}
			else columns = new String[0][0];
			
			
			while(scanner.hasNextLine()) {
				String[] contents = scanner.nextLine().split(";");
				for (int i = 0; i < contents.length; i++) {
					contents[i].trim();
				}
				insertInto(conn, tableName, columns, contents);		//sonst contents inserten in Tabelle
			}
			select(conn, tableName, columns);
			scanner.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}
