package br.com.mtinet.core.script.dbmig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import br.com.mtinet.core.dataBase.ColumnMetaData;
import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.util.FileResource;


public class UpdateStatement extends AbstractStatement implements MigrateStatement {
  
	public UpdateStatement(Connection conn,SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta, Vector indexes) throws SQLException {
		  this.conn = conn;
		  this.refresh(sourcetable,tablename,targetMeta,indexes);
		  iniciaclie() ;
  }
	
  public void refresh(SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta, Vector index) throws SQLException {
  		this.sourcetable = sourcetable ;
	  	this.targetMeta = targetMeta;
	  	this.tableName = tablename;
	  	this.indexes = index;
  }
	
  private void iniciaclie() throws SQLException  {
	    TableMetaData sourceMeta = sourcetable.getMetaData();
	    Enumeration sourceColumns = sourceMeta.getColumns();
	    ColumnMetaData sourceColumn = null;
	    sourceParams = new Vector();
	    sql = "update " + this.tableName + " set ";
	    while (sourceColumns.hasMoreElements()) {
	      sourceColumn = (ColumnMetaData) sourceColumns.nextElement();
	      if (this.indexes.contains(sourceColumn.name)) continue;
	      if (targetMeta.contains(sourceColumn.name)) {
	        if (sourceParams.size() > 0) sql += ", ";
	        sql += sourceColumn.name + " = ? ";
	        sourceParams.add(sourceColumn);
	      }
	    }
	    int n = sourceParams.size();
	    if (n == 0) throw new SQLException(FileResource.getText("operation.msg.error.updateWithoutParameter"));
	    n = this.indexes.size();
	    if (n == 0) throw new SQLException(FileResource.getText("operation.msg.error.updateWithoutIndexes"));
	    sql += " where ";
	    for (int i = 0; i < n; i++) {
	      String index = (String) this.indexes.elementAt(i);
	      sourceColumn = (ColumnMetaData) sourceMeta.getColumn(index);
	      if (sourceColumn == null) throw new SQLException(FileResource.getText("operation.msg.indexNotFound") + index);
	      if (i > 0) sql += " and ";
	      sql += index + " = ? ";
	      sourceParams.add(sourceColumn);
	    }
	    pstmt = conn.prepareStatement(sql);
  }
  
  public String getSqlStatement() {
		return this.sql;
  }

} 
