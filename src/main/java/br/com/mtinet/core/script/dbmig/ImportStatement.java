package br.com.mtinet.core.script.dbmig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.util.FileResource;


class ImportStatement  implements MigrateStatement {
    private MigrateStatement insertMig = null;
    private MigrateStatement updateMig = null;
    private Vector indexes;
    private Connection conn;
    protected SourceTableHandler sourcetable ;
    protected TableMetaData targetMeta = null;
    protected String tableName;
    
    public String showParamets() {
    	if (insertMig!=null) return insertMig.showParamets();
    	else if (updateMig!=null) return updateMig.showParamets();
    	else return "";
    }
    
    public void setOnError(String value) {
    	
    }

    public ImportStatement(Connection conn,SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta, Vector indexes) throws SQLException {
    	this.conn = conn;
  	    refresh(sourcetable,tablename,targetMeta,indexes);
    }
    
    public void refresh(SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta, Vector index) throws SQLException {
    	this.sourcetable = sourcetable ;
  	  	this.targetMeta = targetMeta;
  	  	this.tableName = tablename;
  	  	this.indexes = index;
  	  	if (indexes.size() > 0) {
  	  	} else throw new SQLException (FileResource.getText("operation.msg.error.indexNotFound"));
    }

    public int executeRecord() throws SQLException, Exception {
    	return 0;
    }
    public int execute() throws SQLException, Exception {
    	int n = 0; String sql;
//		if (insertMig==null)
//			insertMig = new InsertStatement(this.conn,this.sourcetable,this.tableName,this.targetMeta);
		if (updateMig==null)
			updateMig = new UpdateStatement(conn,sourcetable,this.tableName,targetMeta,indexes);
    	while(this.sourcetable.next(tableName)) {
//			try {
//				insertMig.executeRecord();
//			} catch (SQLException s) {
//				if (s.getMessage().toLowerCase().contains("primary") || s.getMessage().toLowerCase().contains("pk") ) {
//		    		try {
//		    			if (updateMig==null) 
//		    				updateMig = new UpdateStatement(conn,sourcetable,this.tableName,targetMeta,indexes);
//		    			updateMig.executeRecord();
//		    		} catch (Exception u) {
//		    			throw u;
//		    		}
//				}
//			} catch (Exception e) { throw e; }
    
    		
    		
    		try {
    			if (updateMig.executeRecord() < 1) {
    				if (insertMig==null)
    					insertMig = new InsertStatement(this.conn,this.sourcetable,this.tableName,this.targetMeta);
    				try {
    					insertMig.executeRecord();
    				} catch (SQLException s) {
    					
    				}
    			}
    		} catch (Exception u) {
    			throw u;
    		}
    	}
    	return n;
    }
    
   
    public void close() {
    	if (insertMig != null) insertMig.close();
        if (updateMig != null) updateMig.close();
    }
    
    public String getSqlStatement() {
    	if (insertMig != null) return insertMig.getSqlStatement();
    	else if (updateMig != null) return updateMig.getSqlStatement();
    	else return "SQL nao preparado.";
    }
    
    public String getMessageOperation() {
    	return "IMPORTACAO DADOS.";
    }
    
  }
