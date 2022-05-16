package H07_ImportExportXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Export {
	public static Connection getConnection(String url, String user, String password)  {
		//Class.forName("com.mysql.jdbc.Driver");
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String[][] selectHeader(Connection conn, String databaseName, String tableName) {
		try {
			Statement stmt = conn.createStatement();
			String sql = String.format("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA. COLUMNS WHERE TABLE_SCHEMA='%s' AND TABLE_NAME='%s';", databaseName, tableName);
			ResultSet rs = stmt.executeQuery(sql);
			String temp = "";
			while(rs.next()) {
				temp += String.format("%s;", rs.getString("COLUMN_NAME"));
			}
			String[] names = temp.split(";");
			
			sql = String.format("SELECT DATA_TYPE FROM INFORMATION_SCHEMA. COLUMNS WHERE TABLE_SCHEMA='%s' AND TABLE_NAME='%s';", databaseName, tableName);
			rs = stmt.executeQuery(sql);
			temp = "";
			while(rs.next()) {	//index für columnName, falls text als Dateityp
				temp += String.format("%s;", rs.getString("DATA_TYPE"));
			}
			String[] datatypes = temp.split(";");
						
			String[][] columns = new String[names.length][2];
			for (int i = 0; i < names.length; i++) {
				columns[i][0] = names[i];
				columns[i][1] = datatypes[i];
			}
			rs.close();
			stmt.close();
			
			return columns; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static String select(Connection conn, String[][] columns, String tableName) {
		try {
			String data = String.format("<all_%s>\n", tableName);
			
			String sql = String.format("SELECT * FROM %s;", tableName);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				data += String.format("\t<%s>\n", tableName);
				for (int i = 0; i < columns.length; i++) {
					data += String.format("\t\t<%s>", columns[i][0]);

					if(columns[i][1].equals("int")) data += rs.getInt(columns[i][0]);
					else if(columns[i][1].equals("double")) data += rs.getDouble(columns[i][0]);
					else if(columns[i][1].equals("date")) data += rs.getDate(columns[i][0]);
					else if(columns[i][1].contains("varchar") || columns[i][1].contains("char")) data += rs.getString(columns[i][0]);
					else if(columns[i][1].equals("boolean")) data += rs.getBoolean(columns[i][0]);
					
					data += String.format("</%s>\n", columns[i][0]);
				}
				data += String.format("\t</%s>\n", tableName);
			}
			data += String.format("</all_%s>", tableName);
			
			rs.close();
			stmt.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	public static void main(String[] args) {
		try {
			String configFilePath = "Hausaufgaben/H07_ImportExportXML/configExp.properties";
			FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
	        prop.load(propsInput); 
	        
			String databaseName = prop.getProperty("database_name");
			String tableName = prop.getProperty("table_name");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String filePath = prop.getProperty("filePath");
			
			Connection conn = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			conn.setAutoCommit(true);
			
			
			String[][] columns = selectHeader(conn, databaseName, tableName); 
			
			String xmlStr = select(conn, columns, tableName);
			System.out.println(xmlStr);
		
			FileWriter fw = new FileWriter(new File(String.format("%s%s.xml", filePath, tableName)));
			for (int i = 0; i < xmlStr.length(); i++) {
				char c = xmlStr.charAt(i);
				fw.append(c);
			}		
			fw.flush();
			fw.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}