package br.com.mtinet.core.dataBase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class MetaDataBase {
	
	private Connection conn;

	 public MetaDataBase(Connection conn ) {
			this.conn = conn;
			
	 }
	 
	 public ResultSet getResultTables() {
		 try {
	          DatabaseMetaData meta = conn.getMetaData();
	          String[] args = {"TABLE"};
	          ResultSet tables = meta.getTables(null, null, null, args);
	          return tables;
	          
	        } catch(Exception e) {System.out.println("Erro: " +e.getMessage()); return null;}
	 }

	 public ResultSet getResultTables(String schema) {
		 try {
	          DatabaseMetaData meta = conn.getMetaData();
	          String[] args = {"TABLE"};
	          ResultSet tables = meta.getTables(null, schema, null, args);
	          return tables;
	          
	        } catch(Exception e) {System.out.println("Erro: " +e.getMessage()); return null;}
	 }
	 
	 public int getResultTablesCount(String schema) {
		 try {
	          DatabaseMetaData meta = conn.getMetaData();
	          String[] args = {"TABLE"};
	          ResultSet tables = meta.getTables(null, schema, null, args);
	          int count = 0;
	          while (tables.next()) count++;
	          return count;	          
	        } catch(Exception e) {System.out.println("Erro: " +e.getMessage()); return 0;}
	 }
	 
	 public ResultSet getResultForeignKey( String tableName) {
		 try {
	          DatabaseMetaData meta = conn.getMetaData();
	          ResultSet keysFk = meta.getExportedKeys(null, null,tableName);	        	
	          return keysFk;	   
	        } catch(Exception e) {System.out.println("Erro: " +e.getMessage()); return null;}
	 }
	 
	 public ResultSet getResultForeignKey(String schema, String tableName) {
		 try {
	          DatabaseMetaData meta = conn.getMetaData();
	          ResultSet keysFk = meta.getExportedKeys(null, schema,tableName);	        	
	          return keysFk;	   
	        } catch(Exception e) {System.out.println("Erro: " +e.getMessage()); return null;}
	 }
	 

	 
	
		
		


}
