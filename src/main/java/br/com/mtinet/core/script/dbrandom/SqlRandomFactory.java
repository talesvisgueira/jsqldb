package br.com.mtinet.core.script.dbrandom;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;

import br.com.mtinet.core.dataBase.ColumnMetaData;
import br.com.mtinet.core.dataBase.DataBaseTest;
import br.com.mtinet.core.dataBase.TableMetaData;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.dbddl.DDLObject;

public class SqlRandomFactory {
	private Connection conn;
	private TableMetaData metaData;
	private DDLObject objTable;
	private int sequence = 0;
	ArrayList<DDLObject> foreigns;
	private String schema;
	private ProgressListener listener;
	

	
	public SqlRandomFactory( Connection conn,DDLObject objTable, ArrayList<DDLObject> foreigns,ProgressListener listener) throws Exception {
		this.conn = conn;
		this.objTable = objTable ;
		this.foreigns = foreigns;
		this.listener = listener;
		String tableName =  objTable.getName();
		metaData = new TableMetaData(conn,tableName);
	}
	
	public SqlRandomFactory(String schema,Connection conn,DDLObject objTable, ArrayList<DDLObject> foreigns,ProgressListener listener) throws Exception {
		this.conn = conn;
		this.objTable = objTable ;
		this.foreigns = foreigns;
		this.listener = listener;
		this.schema = schema;
		String tableName = schema+"."+objTable.getName();
		metaData = new TableMetaData(conn,tableName);
	}
	
	
	public void execute(int tot) throws Exception {
		int size = 30;
		this.listener.setMax(tot);
		this.listener.setSize(size);
		this.listener.showMessage("- " + String.format("%-50.50s",this.objTable.getName().toUpperCase() +":"));
		for (int count = 0; count<tot; count++ ) {
			String sql = "";
			try {
				sql = createSql(metaData.getColumns());
				this.listener.showProgress(count);
				conn.createStatement().execute(sql);				
			} catch (MaxPrimaryKeyException m) {
				break;
			} catch (Exception e) {
				throw new Exception(e.getMessage()+ "\n" + sql);
			}
		}	
		if (!conn.getAutoCommit()) conn.commit();
		this.listener.clearText(size);
		this.listener.showMessage(String.format("%-"+45+"." +45+"s","                                 "+tot + " / " + tot));
		this.listener.showNewMessage("");
	}
		
	private int getSequence() {		
		return ++this.sequence;
	}
	
	private String createSql(Enumeration fields) throws MaxPrimaryKeyException {
		String sql = "";
		if (DataBaseTest.hasSchemaInDataBase(this.conn))
		    sql = "INSERT INTO "+this.schema+"."+this.objTable.getName() + " (";
		else sql = "INSERT INTO "+ this.objTable.getName() + " (";
		String values = " VALUES (";
		int maxForeignKey = 0 ;
		while (fields.hasMoreElements()) {
			ColumnMetaData obj = (ColumnMetaData) fields.nextElement();
			int tam = obj.precision;
			sql += obj.name.toLowerCase() + ", ";
			
			if (TableAdapter.getFiledsPrimaryKey(this.objTable.getScript()).toUpperCase().contains(obj.name.toUpperCase())) {
				if (hasFieldForeignKey(obj.name)) {
					
					if (maxForeignKey==0) {
						String script = getScriptForeignKeyByField(obj.name);
						maxForeignKey = getForeignKeyMaxValue(script);
					}
//					values += this.getSequence()+ ", ";
					int value = this.getSequence();
					if (value>maxForeignKey) throw new MaxPrimaryKeyException();
					else values += value+ ", ";
				} else 	values += this.getSequence()+ ", ";
			} else {
				if (hasFieldForeignKey(obj.name)) {
					String script = getScriptForeignKeyByField(obj.name);
					int value = getForeignKeyValue(script);
					values += value+ ", ";
				} else {
					if(obj.typename.toUpperCase().contains("VARCHAR")) values += "'" + RandomField.getPhraseRandom(tam) + "', ";
					else if(obj.typename.toUpperCase().contains("DATE")) values += "'" +RandomField.getDateAsString() + "', ";
					else if(obj.typename.toUpperCase().contains("INT")) values += RandomField.getInt(99999999) + ", ";
					else if(obj.typename.toUpperCase().contains("CHAR")) values += "'" +RandomField.getTextRandom(1) + "', ";
					else if(obj.typename.toUpperCase().contains("BOOL")) if (RandomField.getInt(1)==0) values +=  "false, " ;else values += "true, ";
					else if(obj.typename.toUpperCase().contains("BYTE")) values +="'" + RandomField.getByteAsString(tam)  + "', " ;
					else if(obj.typename.toUpperCase().contains("NUMERIC")) values += RandomField.getNumeric(ajustePrecision(tam)) + ", ";
				}
			
			}
			
		}	
		sql = sql.substring(0, sql.length()-2) + ")";
		values = values.substring(0, values.length()-2) + ")";
		return sql+values+";" ;
	}
	
	private boolean hasFieldForeignKey(String field) {
		boolean value = false;
		for (DDLObject obj : this.foreigns) {
			if (TableForeignAdapter.getFiledsForeignKey(obj.getScript()).toUpperCase().contains(field)) 
				return true ;
		}
		return value;
	}
	
	private String getScriptForeignKeyByField(String field) {
		for (DDLObject obj : this.foreigns) {
			if (TableForeignAdapter.getFiledsForeignKey(obj.getScript()).contains(field)) 
				return obj.getScript() ;
		}
		return null;
	}
	

	
	private int getForeignKeyValue(String script) {
		String sql = TableForeignAdapter.getSqlByTableReferences(script);
		try {
			ResultSet rs = conn.createStatement().executeQuery(sql) ;
			rs.next();
			int value = rs.getInt(1);
			int random = RandomField.getInt(1,value);
			return random;
		} catch(Exception e) {}
		
		return 0;
	}
	
	private int getForeignKeyMaxValue(String script) {
		int value = 0;
		String sql = TableForeignAdapter.getSqlByTableReferences(script);
		try {
			ResultSet rs = conn.createStatement().executeQuery(sql) ;
			rs.next();
			value = rs.getInt(1);
		} catch(Exception e) {}
		
		return value;
	}
	
	private int ajustePrecision(int value) {
		String num = "9";
		for (int p=1 ; p< value; p++) {
			num += "9";
		}
		return new Long(num).intValue();
	}
	
	
}
