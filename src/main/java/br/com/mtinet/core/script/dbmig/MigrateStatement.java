package br.com.mtinet.core.script.dbmig;

import java.sql.SQLException;
import java.util.Vector;

import br.com.mtinet.core.dataBase.SourceTableHandler;
import br.com.mtinet.core.dataBase.TableMetaData;

public interface MigrateStatement {
	public void refresh(SourceTableHandler sourcetable,String tablename,TableMetaData targetMeta, Vector index) throws SQLException ;
	public int execute() throws SQLException, Exception;
	public int executeRecord() throws SQLException, Exception;
    public void close();
    public String getSqlStatement();
    public String showParamets();
    public void setOnError(String value);
    
}
