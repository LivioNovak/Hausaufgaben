package H06_ImportExportJSON;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;



public class Import {				

	public static Connection getConnection(String url, String user, String password)  {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	public static boolean isInt(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isDate(String s) {
	    try { 
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate.parse(s, formatter);
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    } catch(java.time.format.DateTimeParseException e) {
	    	return false;
	    }
	    return true;
	}
	
	
	
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
	
	

	public static String primKeys(String[][] columns) {
		String primKeys = "PRIMARY KEY(";
		int numOfPrimKey = 0;
		for (int i = 0; i < columns.length; i++) {
			if(columns[i][0].contains("id")) {
				numOfPrimKey++;
				if(numOfPrimKey > 1) primKeys += ", ";
				primKeys += columns[i][0];
			}
		}
		return primKeys + ")";
	}


	
	public static String[][] createTable(Connection conn, String tableName, JSONArray jsArr) {
		try {
			JSONObject jsObj = (JSONObject) jsArr.get(0);
			
			String[][] columns = new String[jsObj.size()][3]; //columns[][0]-> name; columns[][1]-> dataType; (only if datatype is varchar) columns[][2]-> length of varchar
			
			int index = 0;
	 	    for (Object key: jsObj.keySet()){	// keySet() vertauscht aus irgendeinem Grund die plätze der Keys (zb. vorher id an 1. stelle, jetzt an 3. Stelle)
	 	      	columns[index][0] = (String) key;
	 	       	index++;
			}
 	        
			for (int i = 0; i < columns.length; i++) {
				String value = jsObj.get(columns[i][0]).toString();
				
				if(isInt(value)) columns[i][1] = "INT";
				else if(isDouble(value)) columns[i][1] = "DOUBLE";
				else if(isDate(value)) columns[i][1] = "DATE";
				else {
					columns[i][1] = String.format("VARCHAR(%d)", value.length());
					columns[i][2] = String.format("%d", value.length());
				}
			}
			

			String primKey = primKeys(columns);

			Statement stmt = conn.createStatement();

			String sql = String.format("CREATE TABLE IF NOT EXISTS %s(", tableName);
			for (int i = 0; i < columns.length; i++) {
				sql += String.format("%s %s, ", columns[i][0], columns[i][1]);
			}
			sql += String.format("%s);", primKey);
			
			stmt.executeUpdate(sql);
			stmt.close();
			return columns;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	public static void insertInto(Connection conn, String tableName, String[][] columns, JSONObject jsObj) {
		try {
			String sql = String.format("INSERT INTO %s VALUES(", tableName);
			for (int i = 0; i < columns.length; i++) {
				sql += "?";
				if(i < columns.length - 1) sql += ", ";
			}
			sql += ");";

			PreparedStatement preStmt = conn.prepareStatement(sql);
			for (int i = 0; i < columns.length; i++) {
				String value = jsObj.get(columns[i][0]).toString();
				
				//columns[i][1] -> Datentyp
				if(columns[i][1].equals("INT")) preStmt.setInt((i + 1), Integer.parseInt(value));
				else if(columns[i][1].equals("DOUBLE")) preStmt.setDouble((i + 1), Double.parseDouble(value));
				else if(columns[i][1].equals("DATE")) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
					LocalDate datum = LocalDate.parse(value, formatter);
					preStmt.setDate((i + 1), java.sql.Date.valueOf(datum));
				}
				else { //VARCHAR
					if(value.length() > Integer.parseInt(columns[i][2])) { //alter table wenn aktueller Wert > laenge des VARCHAR()
						columns[i][2] = String.format("%d", value.length());
						Statement stmt = conn.createStatement();
						String alterSql = String.format("ALTER TABLE %s MODIFY %s VARCHAR(%d);", tableName, columns[i][0], Integer.parseInt(columns[i][2]));
						System.out.println(alterSql);
						stmt.executeUpdate(alterSql);
						stmt.close();
					}
					preStmt.setString((i + 1), value);
				}
			}
			preStmt.executeUpdate();
			preStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


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
					case "INT": row += String.format("%d",rs.getInt(columns[i][0])); break;
					case "DOUBLE": row += String.format("%f", rs.getDouble(columns[i][0])); break;
					case "DATE": row += rs.getDate(columns[i][0]); break;
					default: row += String.format("%s", rs.getString(columns[i][0])); break;
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


	
	
	public static void main(String[] args) {
		try {
			String configFilePath = "Hausaufgaben/H06_ImportExportJSON/configImp.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput); 
            
//            String databaseName = prop.getProperty("database_name");
            String tableName = prop.getProperty("table_name");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String filePath = prop.getProperty("filePath");
			
			
			Connection conn = getConnection(url, user, password);
			System.out.println("Connection erfolgreich\n");
			conn.setAutoCommit(true);
			
			
			Object ob = new JSONParser().parse(new FileReader(filePath));
	        JSONArray jsArr = (JSONArray) ob;


			dropOneTable(conn, tableName);	
			
			String[][] columns = createTable(conn, tableName, jsArr);

			for (int i = 0; i < jsArr.size(); i++) {
	        	JSONObject jsObj = (JSONObject) jsArr.get(i);
	        	insertInto(conn, tableName, columns, jsObj);
			}
			
			select(conn, tableName, columns);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
