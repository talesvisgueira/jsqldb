package br.com.mtinet.core.script.dbaudit;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;

import br.com.mtinet.core.dataBase.MetaDataBase;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.util.NumericFormat;

public class AuditRecordCount extends AbstractHandler implements Audit{
	private Connection conn;
	private String audityType = "RECORD COUNT";
	private boolean print = false;
	
	
	public AuditRecordCount( Connection conn,String filePath,String schema, boolean print) {
		this.conn  = conn;
		this.filePath = filePath;
		this.schema = schema;
		this.printLog = !print;
		this.print = print;
		createDayDir();
		
	}

	public void execute() throws Exception  {
		this.auditTableCount(this.schema);		
	}
	
	private void auditTableCount(String schema) throws Exception {
		MetaDataBase meta = new MetaDataBase(conn );
		ResultSet tables = meta.getResultTables(schema);
		int tot= 0; int pos =0;
		if (!new File(filePath).exists()) 
			this.createNewFile();
		this.save("-----------------------------------------------------------------------------");
		this.save("                 QUANTIDADE DE REGISTROS DO SCHEMA " + schema.toUpperCase());
		this.save("         TABELA                                                 REGISTROS");
		this.save("-----------------------------------------------------------------------------");
//		if (this.printLog)  if (pos % 10 ==0) System.out.print(audityType + " Tabelas: " + String.format("%-7.7s",pos));
		String sql = "";
		while (tables.next())  {
			String tableName = tables.getString(3);
			if (this.conn.toString().toUpperCase().contains("FIREBIRD")) 
				 sql = "select count(*) from " +  tableName;
			else sql = "select count(*) from " + schema+"."+ tableName;
			
			ResultSet rs = conn.createStatement().executeQuery(sql);
			int count = 0;
			if (rs.next()) { count = rs.getInt(1); }
			String msg = "   - " + String.format("%-55.55s",tableName) + " " + NumericFormat.align(count,15,NumericFormat.ALIGN_RIGHT);
			tot += count; pos++;
			this.save(msg);
			if (this.print) System.out.println(sql);
//			if (this.printLog) if (pos % 10 ==0) System.out.print("\b\b\b\b\b\b\b" + String.format("%-7.7s",pos));
		}
//		if (this.printLog) System.out.print("\b\b\b\b\b\b\b" + String.format("%-7.7s",pos));
		if (tot==0) this.save("Not found tables in database.");
		this.save("-----------------------------------------------------------------------------");
		this.save(String.format("%-47.47s","") +" TOTAL:  "  + NumericFormat.align(tot,20,NumericFormat.ALIGN_RIGHT));
		System.out.println("   Arquivo: '"+this.filePath+"'.");
	}
	
	
	

}
