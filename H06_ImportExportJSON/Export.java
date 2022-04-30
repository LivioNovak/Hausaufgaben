package H06_ImportExportJSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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
	
	
	public static String[][] selectHeader(Connection conn, String database_name, String table_name) {
		try {
			Statement stmt = conn.createStatement();
			String sql = String.format("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA. COLUMNS WHERE TABLE_SCHEMA='%s' AND TABLE_NAME='%s';", database_name, table_name);
			ResultSet rs = stmt.executeQuery(sql);
			String temp = "";
			while(rs.next()) {
				temp += String.format("%s;", rs.getString("COLUMN_NAME"));
			}
			String[] names = temp.split(";");
			
			sql = String.format("SELECT DATA_TYPE FROM INFORMATION_SCHEMA. COLUMNS WHERE TABLE_SCHEMA='%s' AND TABLE_NAME='%s';", database_name, table_name);
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
	
	
	public static String select(Connection conn, String[][] columns, String table_name) {
		try {
			String data = "[\r\n";
			
			String sql = String.format("SELECT * FROM %s;", table_name);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			boolean hasNext = rs.next();
			while(hasNext) {
				data += "{";
				for (int i = 0; i < columns.length; i++) {
					data += String.format("\"%s\":", columns[i][0]);
					
					
					if(columns[i][1].equals("int")) data += String.format("\"%d\"", rs.getInt(columns[i][0]));
					else if(columns[i][1].equals("double")) data += String.format("\"%f\"", rs.getDouble(columns[i][0]));
					else if(columns[i][1].equals("date")) data += "\"" + rs.getDate(columns[i][0]) + "\"";
					else if(columns[i][1].contains("varchar") || columns[i][1].contains("char")) data += String.format("\"%s\"", rs.getString(columns[i][0]));
					else if(columns[i][1].equals("boolean")) data += String.format("\"%s\"", rs.getBoolean(columns[i][0]));
					
					if(i < columns.length -1) data += ",";
				}
				if(hasNext = rs.next()) data += "},\r\n";
				else data += "}\r\n";
			}
			data += "]";
			
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
			String configFilePath = "Hausaufgaben/H06_ImportExportJSON/configExp.properties";
			FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
	        prop.load(propsInput); 
	        
			String database_name = prop.getProperty("database_name");
			String table_name = prop.getProperty("table_name");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String filePath = prop.getProperty("filePath");
			
			Connection conn = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			conn.setAutoCommit(true);
			
			
			String[][] columns = selectHeader(conn, database_name, table_name); 
			
			String jsonStr = select(conn, columns, table_name);
			System.out.println(jsonStr);
		
			FileWriter fw = new FileWriter(new File(String.format("%s%s.json", filePath, table_name)));
			for (int i = 0; i < jsonStr.length(); i++) {
				char c = jsonStr.charAt(i);
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