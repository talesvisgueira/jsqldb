package br.com.mtinet.core.script.dbddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.mtinet.core.dataBase.DataBaseTest;
import br.com.mtinet.core.dataBase.MetaDataBase;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.util.MessageCommon;

public class DDLFactory extends AbstractHandler {
	 private Connection conn;
	 private String dataSource;
	 private MetaDataBase metaDB;
	 private boolean saveInFile = true;
	 private boolean printLog = true;
	 private boolean print = false;

	 public DDLFactory(String datasource,Connection conn,String filePath,String schema, boolean print) throws Exception{
		this.dataSource = datasource;
		this.conn = conn;
		this.filePath = filePath;
		this.print = print;
		this.printLog = !print;
		this.schema = schema ;
		this.metaDB = new MetaDataBase(conn);
		this.createNewFile();
	 }
	 public void setPrintLog(boolean value) {
		 this.printLog = value;
	 }
	 
	 public void setSaveInFile(boolean value) {
		 this.saveInFile = value;
	 }
	 

	 
	 public void execute() throws Exception {
		 ArrayList<DDLObject> ddl;
			 System.out.println("SCHEMA: " + schema.toUpperCase() );
			 if (conn!=null) ddl = getDDLObjectByConncection( );
			 else throw new Exception("Conexao n�o estabelecida...");			 			 
			 System.out.println("\nArquivo '"+this.filePath+"' foi criado com a estrutura do BD.");
			 MessageCommon.printLine();

	 }
	 
	 public void execute(String tableName){
		 DDLObject ddl;
		 try {			 			 
			 if (conn!=null) {
				 ddl = getDDLObjectTable(tableName );
				 if (saveInFile) save(ddl.getScript());
			 }
			 else throw new Exception("Conexao n�o estabelecida...");			 			 						
		 } catch (Exception e) { System.out.println( e.getMessage() );}
	 }
	 
	 
	 
	 public ArrayList<DDLObject> getDDLObjectByConncection() {
		 ArrayList<DDLObject> ddl = new ArrayList<DDLObject>(); 
		 int tab = 0; int fkkey = 0;
		 ddl.addAll(getDDLObjectTables(  ));
		 tab = ddl.size();
		 ddl.addAll(getAllDDLObjectForeingKey());
		 fkkey = ddl.size() - tab;
		 if (!print)  System.out.println("\n\n   *  OBJETOS CRIADOS:  TABELAS: " + tab+ "      FOREIGN KEY: " + fkkey);
		 return ddl;
	 }
	 
	 public DDLObject getDDLObjectTable(String tableName) {
		 DDLObject obj = null;

		 try {
			 Statement st = conn.createStatement();
			 String sql = "";
			 String query = "";
			 if (DataBaseTest.hasSchemaInDataBase(this.conn)){			 
				 sql = "\nCREATE TABLE " + schema +"."+ tableName + " (" ;
				 query = "select * from "+ schema +"."+ tableName + " where 1 = 2";
			 } else  {
				 sql = "\nCREATE TABLE " + tableName + " (" ;
				 query = "select * from "+ tableName + " where 1 = 2";
			 }
	       	  
	       	  ResultSet rs = st.executeQuery(query);
	       	  ResultSetMetaData attributes = rs.getMetaData();
	       	  for (int c = 0 ; c < attributes.getColumnCount();c++){
	       		  sql += "\n"+attributes.getColumnLabel(c+1) +" ";
		        	  String f2 = attributes.getColumnTypeName(c+1);
		        	  
		        	  if ( f2.toUpperCase().equals("VARCHAR") || f2.toUpperCase().equals("VARCHAR2"))
		        		   sql += f2 + "(" + new Long(attributes.getColumnDisplaySize(c+1)).toString() + ")";		        	  
		        	  else sql += f2;
		        	  
		        	  if (attributes.isNullable(c+1)==0) sql += " NOT NULL,";
		        	  else sql +=  " NULL,";
		        	  
	       	  }
	       	  
	       	  DatabaseMetaData meta = conn.getMetaData();
	       	  ResultSet keysPk = meta.getPrimaryKeys(null, schema,tableName);
	       	  
	       	  boolean valuePk = false;
	       	  String campos = "";
	       	  while (keysPk.next())  {
	       		  valuePk = true;
		          campos += keysPk.getString(4) +",";
	       	  }
	       	  if (campos.length()>0) {
	       		  keysPk = meta.getPrimaryKeys(null, schema,tableName);
	       		  keysPk.next();
		       	  if (valuePk) sql += "\nconstraint " + keysPk.getString(6) + " primary key ";
		       	  if (valuePk) sql += "("+campos.substring(0, campos.length()-1)+") \n);\n";
	       	  }
	       	  if (!valuePk) sql = sql.substring(0,sql.length()-1)+" \n);\n";

	       	  obj = new DDLObject("TABLE",tableName,sql) ;
		 } catch (Exception e) {}
		 return obj ;
	 }
	 
	 public ArrayList<DDLObject> getDDLObjectTables(  ) {
		 ArrayList<DDLObject> objectList = new ArrayList<DDLObject>();
		 try {
			  ResultSet tables = null;
			  if (DataBaseTest.hasSchemaInDataBase(this.conn))
				  tables = this.metaDB.getResultTables(schema);
			  else tables = this.metaDB.getResultTables();
			  
	          if (printLog) {
        		  if (objectList.size() % 10 ==0) System.out.print("- Tabelas identificads: " + String.format("%-9.9s",objectList.size()));
        	  }
	          while (tables.next())  {
	        	  DDLObject obj = this.getDDLObjectTable(tables.getString(3));
	        	  objectList.add(obj);
	        	  if (printLog) {
	        		  if (objectList.size() % 10 ==0) System.out.print("\b\b\b\b\b\b\b\b\b" + String.format("%-9.9s",objectList.size()));
	        	  }
	        	  if (saveInFile) save(obj.getScript());
	        	  if (this.print) System.out.println(obj.getScript()+"\n");
	          }
        	  if (printLog)   System.out.print("\b\b\b\b\b\b\b\b\b" + String.format("%-9.9s",objectList.size()));
        
	        } catch(Exception e) {System.out.println("\nErro: " +e.getMessage());}
		 return objectList;
	 }
	 
	 private String mountSqlForeignKey(String tableConstraint, String constraintName,String keyFk, String tableReference, String keyRef){
		 String sql = ""; 
		 if (DataBaseTest.hasSchemaInDataBase(this.conn))
			 sql = "ALTER TABLE " +schema+ "."+ tableConstraint + "\n ADD CONSTRAINT " + constraintName ;
		 else sql = "ALTER TABLE " +  tableConstraint + "\n ADD CONSTRAINT " + constraintName ;
		  
    	  sql += " ("+keyFk+")\n REFERENCES " + schema+ "." +tableReference +" ("+keyRef+");\n";
		 return sql;
	 }
	 
	 private  ArrayList<DDLObject> getForeignKeyInTable(String schemaLocal, String tableName ) {
		 ArrayList<DDLObject> objectList = new ArrayList<DDLObject>();
		 try {
				DatabaseMetaData meta = conn.getMetaData();				
		   	  	ResultSet keysFk = meta.getExportedKeys(null, schemaLocal,tableName);	        	
		   	  	while (keysFk.next())  {
		   	  		String sql = mountSqlForeignKey(keysFk.getString(7),keysFk.getString(12),keysFk.getString(8),keysFk.getString(3),keysFk.getString(4)); 		   	
		   	  		objectList.add(new DDLObject("CONSTRAINT",keysFk.getString(12),sql)) ;
			 	}
		 } catch(Exception e) {}	
		 return objectList;
	 }
	 
	 
	 public ArrayList<DDLObject> getAllDDLObjectForeingKey() {
		 ArrayList<DDLObject> objectList = new ArrayList<DDLObject>();
		 try {
	          DatabaseMetaData meta = conn.getMetaData();
	          String[] args = {"TABLE"};
	          ResultSet tables = null;
	          if (DataBaseTest.hasSchemaInDataBase(this.conn))
	        	  tables = meta.getTables(null, schema, null, args);
	          else tables = meta.getTables(null, null, null, args);
	          
	          if (printLog) {
        		  if (objectList.size() % 10 ==0) System.out.print("\n- Foreign key identificadas: " + String.format("%-9.9s",objectList.size()));
        	  }
	          while (tables.next())  {
//	        	  objectList.addAll(getForeignKeyInTable(schemaLocal,tables.getString(3)));
	        	  ResultSet keysFk = meta.getExportedKeys(null, schema,tables.getString(3));	        	
	        	  while (keysFk.next())  {
	        		  String sql = mountSqlForeignKey(keysFk.getString(7),keysFk.getString(12),keysFk.getString(8),keysFk.getString(3),keysFk.getString(4)); 
	        	
		        	  objectList.add(new DDLObject("CONSTRAINT",keysFk.getString(12),sql)) ;
		        	  if (print) System.out.println( sql);
		        	  if (saveInFile) save(sql);
		        	  if (printLog) {
		        		  if (objectList.size() % 10 ==0) System.out.print("\b\b\b\b\b\b\b\b\b" + String.format("%-9.9s",objectList.size()));
		        	  }
	        	  }
	        	  
	          }

        	  if (printLog) System.out.println("\b\b\b\b\b\b\b\b\b" + String.format("%-9.9s",objectList.size()));
	          
	        } catch(Exception e) {System.out.println("Erro: " +e.getMessage());}
		 return objectList;
	 }
	 
	 public ArrayList<DDLObject> getForeignKeyByReference(String tableName) {
		 ArrayList<DDLObject> objectList = new ArrayList<DDLObject>();
		 try {
	          DatabaseMetaData meta = conn.getMetaData();
	          String schemaLocal = schema.replace(".", "");
	          ResultSet keysFk = null;
	          if (DataBaseTest.hasSchemaInDataBase(this.conn))
	        	  keysFk = meta.getExportedKeys(null, schemaLocal,tableName);	
	          else keysFk = meta.getExportedKeys(null, null,tableName);	
        	          	
        	  while (keysFk.next())  {
        		  String sql = mountSqlForeignKey(keysFk.getString(7),keysFk.getString(12),keysFk.getString(8),keysFk.getString(3),keysFk.getString(4)); 
	        	  objectList.add(new DDLObject("CONSTRAINT",keysFk.getString(12),sql)) ;
	        	  if (print) System.out.println( sql);
	        	  if (saveInFile) save(sql);		        
        	  }

	        } catch(Exception e) {System.out.println("Erro: " +e.getMessage());}
		 return objectList;
	 }
	 
	 public ArrayList<DDLObject> getForeignKeyByTable(String tableName) {
		 ArrayList<DDLObject> objectList = new ArrayList<DDLObject>();
		 try {
			 ArrayList<DDLObject> list  = getAllDDLObjectForeingKey();
			 for (DDLObject obj : list) {
				 String script = obj.getScript().toUpperCase();
				 script = script.substring(0,script.indexOf("ADD")).trim();
				 if (script.contains("."))
					 script = script.substring(script.indexOf(".")+1,script.length()).trim();
				 if ( script.toUpperCase().equals(tableName.toUpperCase()) ) {
					 objectList.add(obj);
					 this.save(obj.getScript());
				 }
			 }
	     } catch(Exception e) {System.out.println("Erro: " +e.getMessage());}
		 return objectList;
	 }
	 
}
