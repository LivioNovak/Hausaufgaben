package H07_ImportExportXML;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Import {				

	public static Connection getConnection(String url, String user, String password)  {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
//==================================================================================for DROPS
	
//------------------------------------------------	
	public static void dropAllTables(Connection conn, String databaseName) {
		try {
			Statement stmt = conn.createStatement();
			String sql = "SET FOREIGN_KEY_CHECKS = 0;";
			stmt.executeUpdate(sql);
			
			sql = String.format("SELECT table_name FROM information_schema.tables WHERE table_schema = '%s';", databaseName);
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
//------------------------------------------------	
	public static void dropOneTable(Connection conn, String tableName) {
		try {
			Statement stmt = conn.createStatement();
			String sql = "SET FOREIGN_KEY_CHECKS = 0;";
			stmt.executeUpdate(sql);
			
			sql = String.format("DROP TABLE IF EXISTS %s;", tableName);
			stmt.executeUpdate(sql);
			
			sql = "SET FOREIGN_KEY_CHECKS = 1;";
			stmt.executeUpdate(sql);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

//================================================================================== CREATE
	
//------------------------------------------------	

	/*Problem: ich habe es nicht geschafft, irgendwie die Namen der Attribute (Vorname, Nachname, Geburtsdatum) zu bekommen
	* 
	* mit "getNodeName()" kommen neben dem namen noch zusätzliche Werte "#text". 
	* Außerdem wird die länge eines Elementes (= 1 Schueler) als 7 erkannt, obwohl es nur 3 sein sollte (wegen "#text")
	* 
	* Daher habe ich sowohl die länge von "column", als auch die namen direkt angegeben
	*/
	public static String[][] createTable(Connection conn, String tableName, Node n) {
		try {
			String[][] columns;
			
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				//Element elem = (Element) n;
				
				//columns sollte eigentlich dynamisch zusammengebaut werden  (daher auch elem)
				columns = new String[3][2];
				
				columns[0][0] = "Vorname";
				columns[0][1] = "VARCHAR(255)";
				columns[1][0] = "Nachname";
				columns[1][1] = "VARCHAR(255)";
				columns[2][0] = "Geburtsdatum";
				columns[2][1] = "DATE";
				
				
				Statement stmt = conn.createStatement();

				StringWriter sqlWriter = new StringWriter();
				
				sqlWriter.write("CREATE TABLE IF NOT EXISTS ");
				sqlWriter.write(tableName);
				sqlWriter.write("(id INT AUTO_INCREMENT PRIMARY KEY,");
				
				for (int i = 0; i < columns.length; i++) {
					sqlWriter.write(columns[i][0]);
					sqlWriter.write(" ");
					sqlWriter.write(columns[i][1]);
					if(i < columns.length - 1) sqlWriter.write(", ");
				}
				sqlWriter.write(");");
				
				String sql = sqlWriter.toString();
				
				stmt.executeUpdate(sql);
				stmt.close();
				return columns;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	

//================================================================================== INSERT
	
//------------------------------------------------	
	public static String createInsertSql(String tableName, String[][] columns) {
		try {
			StringWriter sqlWriter = new StringWriter();
			sqlWriter.write("INSERT INTO ");
			sqlWriter.write(tableName);
			sqlWriter.write(" (");
			
			for (int i = 0; i < columns.length; i++) {
				sqlWriter.write(columns[i][0]);
				if(i < columns.length - 1) sqlWriter.write(", ");
			}
			
			sqlWriter.write(") VALUES (");
			
			for (int i = 0; i < columns.length; i++) {
				sqlWriter.write("?");
				if(i < columns.length - 1) sqlWriter.write(", ");
			}
			sqlWriter.write(");");
			
			String sql = sqlWriter.toString();
			sqlWriter.close();
			
			return sql;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
//------------------------------------------------	
	public static void insertInto(Connection conn, String tableName, String[][] columns, Node nd) {
		try {
			if(nd.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) nd;
				
				String sql = createInsertSql(tableName, columns);
				
				PreparedStatement preStmt = conn.prepareStatement(sql);
				for (int i = 0; i < columns.length; i++) {
					String value = elem.getElementsByTagName(columns[i][0]).item(0).getTextContent();
					
					//columns[i][1] -> Datentyp
					if(columns[i][1].equals("INT")) preStmt.setInt((i + 1), Integer.parseInt(value));
					else if(columns[i][1].equals("DOUBLE")) preStmt.setDouble((i + 1), Double.parseDouble(value));
					else if(columns[i][1].equals("DATE")) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
						LocalDate datum = LocalDate.parse(value, formatter);
						preStmt.setDate((i + 1), java.sql.Date.valueOf(datum));
					}
					else preStmt.setString((i + 1), value); //VARCHAR
				}
				preStmt.executeUpdate();
				preStmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	
//================================================================================== SELECT
	
	public static void select(Connection conn, String tableName, String[][] columns) {
		try {
			for (int i = 0; i < columns.length; i++) {
				System.out.print(columns[i][0]);
				if(i < columns.length - 1) System.out.print("|");
			}
			System.out.println();

			String sql = String.format("SELECT * FROM %s;", tableName);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String row = "";
				for (int i = 0; i < columns.length; i++) {
					switch (columns[i][1]) {
					case "INT": row += rs.getInt(columns[i][0]); break;
					case "DOUBLE": row += rs.getDouble(columns[i][0]); break;
					case "DATE": row += rs.getDate(columns[i][0]); break;
					default: row += rs.getString(columns[i][0]); break;
					}
					if(i < columns.length - 1) row += "|";
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


	
//==================================================================================MAIN
	
	public static void main(String[] args) {
		try {
			String configFilePath = "Hausaufgaben/H07_ImportExportXML/configImp.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput); 
            
//          String databaseName = prop.getProperty("database_name");
            String tableName = prop.getProperty("table_name");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String filePath = prop.getProperty("filePath");
			
			
			Connection conn = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			conn.setAutoCommit(true);
			
			
			dropOneTable(conn, tableName);
			
			
			
			File xmlDoc = new File(filePath);
			
			DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuild = dbFact.newDocumentBuilder();
			Document doc = dBuild.parse(xmlDoc);
			
			NodeList nList = doc.getElementsByTagName("schueler");  //Liste der Schueler
			
			
			String[][] columns = createTable(conn, tableName, nList.item(0)); //der 1. Schüler reicht, um den Aufbau der tabelle zu bestimmen
	
			for (int i = 0; i < nList.getLength(); i++) {
	        	Node nd = nList.item(i);
	        	insertInto(conn, tableName, columns, nd);
			}
			
			select(conn, tableName, columns);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
