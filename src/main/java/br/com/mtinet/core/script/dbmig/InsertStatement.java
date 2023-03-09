package br.com.mtinet.core.script.dbmig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import br.com.mtinet.core.dataBase.ColumnMetaData;
import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.util.FileResource;

public class InsertStatement extends AbstractStatement implements MigrateStatement {

  public InsertStatement(Connection conn,SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta) throws SQLException {
	  this.conn = conn;
	  this.refresh(sourcetable,tablename,targetMeta,indexes);
	  iniciaclie();
  }
  
  public void refresh(SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta, Vector index) throws SQLException {
  		this.sourcetable = sourcetable ;
	  	this.targetMeta = targetMeta;
	  	this.tableName = tablename;
	  	this.indexes = null;
  }
  
  private void iniciaclie() throws SQLException  {
	    TableMetaData sourceMeta = sourcetable.getMetaData();
	    Enumeration sourceColumns = sourceMeta.getColumns();
	    ColumnMetaData sourceColumn = null;
	    sourceParams = new Vector();
	    sql = "insert into " + this.tableName + " (";
	    while (sourceColumns.hasMoreElements()) {
	      sourceColumn = (ColumnMetaData) sourceColumns.nextElement();
	      if (targetMeta.contains(sourceColumn.name)) {
	        if (sourceParams.size() > 0) sql += ", ";
	        sql += sourceColumn.name;
	        sourceParams.add(sourceColumn);
	      }
	    }
	    sql += ") values (";
	    int n = sourceParams.size();
	    if (n == 0) throw new SQLException(FileResource.getText("operation.msg.error.insertWithoutParameter"));
	    for (int i = 0; i < n; i++) {
	      if (i > 0) sql += ", ";
	      sql += "?";
	    }
	    sql += ")";
	    pstmt = conn.prepareStatement(sql);
  }
  
  public String getSqlStatement() {
		return this.sql;
  }

} 
