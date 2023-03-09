package br.com.mtinet.core.script.dbexport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import br.com.mtinet.core.dataBase.ColumnMetaData;
import br.com.mtinet.core.dataBase.DataBaseTest;
import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.core.script.dbmig.AbstractStatement;
import br.com.mtinet.core.script.dbmig.MigrateStatement;
import br.com.mtinet.util.Project;

public class ExportStatement extends AbstractStatement implements MigrateStatement {
	public static final String EXPORT_TYPE_SQL = "sql";
	public static final String EXPORT_TYPE_CSV = "csv";
	public static final String EXPORT_TYPE_TXT = "txt";
	
	private String filePath;
	private String type;
	

	public ExportStatement(SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta,String type, String filePath) throws SQLException,Exception{
		  this.conn = conn;
		  this.refresh(sourcetable,tablename,targetMeta,indexes);
		  this.filePath = filePath;
		  this.type = type;
		  prepareStatement();
    }
	
	public void refresh(SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta, Vector index) throws SQLException {
	  	
  		this.sourcetable = sourcetable ;
	  	this.targetMeta = targetMeta;
	  	this.tableName = tablename;
	  	this.indexes = index;
	  
  }
  
   public ExportStatement(SourceTableHandler sourcetable,String schema,String tablename,TableMetaData targetMeta,String type, String filePath) throws SQLException,Exception{
	  this.sourcetable=sourcetable ;
	  this.targetMeta = targetMeta;
	  this.schema = schema;
	  this.tableName = tablename;
	  this.conn = sourcetable.getConn();
	  this.filePath = filePath;
	  this.type = type;
	  prepareStatement();
  }
  
  private void prepareStatement() throws SQLException,Exception{
	  String sql = "";
	  TableMetaData sourceMeta = sourcetable.getMetaData();
	  Enumeration sourceColumns = sourceMeta.getColumns();
	  ColumnMetaData sourceColumn = null;
	    sourceParams = new Vector();
	    if (DataBaseTest.hasSchemaInDataBase(this.conn)) sql = "insert into " + schema + "." + this.tableName + " (";
	    else sql = "insert into " +  this.tableName + " (";
	    while (sourceColumns.hasMoreElements()) {
	      sourceColumn = (ColumnMetaData) sourceColumns.nextElement();
	      if (targetMeta.contains(sourceColumn.name)) {
	        if (sourceParams.size() > 0)
	          sql += ", ";
	        sql += sourceColumn.name;
	        sourceParams.add(sourceColumn);
	      }
	    }
	    sql += ") values (";
	    int n = sourceParams.size();
	    if (n == 0)
	      throw new SQLException("insert SQL without parameters.");
	    for (int i = 0; i < n; i++) {
	      if (i > 0)
	        sql += ", ";
	      sql += "?";
	    }
	    sql += ")";
	    pstmt = conn.prepareStatement(sql);
  }
  
  public int executeStatement() throws SQLException, Exception {	  
	  String obj = tableName.substring(tableName.indexOf(".")+1,tableName.length());
	  if (this.sourcetable.getRecords().isClosed()) this.sourcetable.open();
	  while(this.sourcetable.next(obj)) {
		  try {
  			  bindParameters();  	
  			  if (type.toUpperCase().equals(EXPORT_TYPE_SQL.toUpperCase())) {
  				  save(getSQLRecordValues()+";");
  			  }else if (type.toUpperCase().equals(EXPORT_TYPE_CSV.toUpperCase())) {
  				  save(this.getCVSRecordValues());
  			  }else if (type.toUpperCase().equals(EXPORT_TYPE_TXT.toUpperCase())) {
    			  save(this.getTXTRecordValues());
  			  }else throw new Exception("Tipo de exportacao invalida.");
		  } catch(Exception e) {  }
	  }
	  return 0;
  }
  
  public void save(String msg) {
		try{ 
			 FileWriter fstream = new FileWriter(this.filePath,true);
			 BufferedWriter out = new BufferedWriter(fstream);
			 out.write(msg+"\r\n");
			 out.close();
		}catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
		}
  }
  
  public String getSqlStatement() {
		return this.sql;
  }

  public String getMessageOperation() {
	  String db="";
	  try{ db = this.conn.getCatalog();}
	  catch(Exception e) {}
	  return "Operation: export in table " + this.tableName + " in database " + db;
  }
  
  protected String getTXTRecordValues() throws Exception {
	  String txt = "";
      Object val = null;
      ColumnMetaData sourceColumn = null;   
      int n = sourceParams.size();
      for (int i = 0; i < n; i++)
      try {
    	  sourceColumn = (ColumnMetaData) sourceParams.elementAt(i);
          val = sourcetable.getObject(sourceColumn.name);
          if(sourceColumn.precision>0)
        	  txt += String.format("%-"+sourceColumn.precision+"."+sourceColumn.precision+"s",val);
          else txt += val;
      } catch (Exception e) {
          log("Error on binding param(" + (i+1) + "): " + e, Project.MSG_ERR);  throw e;
      }
	return txt ;
  }
  
  protected String getCVSRecordValues() throws SQLException {
	  String txt = "";	  
      Object val = null;
      ColumnMetaData sourceColumn = null;      
      int n = sourceParams.size();
      for (int i = 0; i < n; i++)
      try {
    	  sourceColumn = (ColumnMetaData) sourceParams.elementAt(i);
          val = sourcetable.getObject(sourceColumn.name);
          txt += val + ",";
      } catch (SQLException e) {
          log("Error on binding param(" + (i+1) + "): " + e, Project.MSG_ERR);  throw e;
      }
	return txt.substring(0, txt.length()-1);
  }
  
  protected String getSQLRecordValues() throws Exception {
		String sql = "INSERT INTO " + schema + "." + this.tableName + " ("; String values = "";
		
		Object val = null;
		ColumnMetaData sourceColumn = null;
		
		int n = sourceParams.size();
		for (int i = 0; i < n; i++) {
			sourceColumn = (ColumnMetaData) sourceParams.elementAt(i);
			val = sourcetable.getObject(sourceColumn.name);
			sql += sourceColumn.name + ", ";
	        if (val != null) {
	        	if (sourceColumn.typename.toUpperCase().contains("VARCHAR")) values += "'"+val.toString() + "', ";
	        	else if (sourceColumn.typename.toUpperCase().contains("CHAR")) values += "'"+val.toString() + "', ";
	        	else values += val.toString() + ", ";
	        		
	        } else values +=  "null, ";
	    }
		sql = sql.substring(0, sql.length()-2) + ") VALUES (";
		values = values.substring(0, values.length()-2) + ")";
		return sql + values;
	}
}  
