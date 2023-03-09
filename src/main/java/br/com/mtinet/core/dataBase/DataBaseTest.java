package br.com.mtinet.core.dataBase;

import java.sql.Connection;

public class DataBaseTest {
	public static boolean hasSchemaInDataBase(Connection conn) {
		 boolean value = true;		 
		 if(conn.toString().toUpperCase().contains("FIREBIRD")) value = false;
		 else if(conn.toString().toUpperCase().contains("MYSQL")) value = false;
		 return value;
	 }
}
