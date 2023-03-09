package br.com.mtinet.core.script.dbaudit;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;

import br.com.mtinet.core.dataBase.MetaDataBase;
import br.com.mtinet.core.script.AbstractHandler;

public class AuditFieldCount extends AbstractHandler  implements Audit{
    private Connection conn;
	private String audityType = "FIELD COUNT";
	private boolean print = false;
	
	public AuditFieldCount( Connection conn,String filePath,String schema, boolean print) {
		this.conn  = conn;
		this.filePath = filePath;
		this.schema = schema+".";
		this.printLog = !print;
		this.print = print;
		createDayDir();
	}
	
	public void execute() throws Exception  {
		this.audit(this.schema);
		
	}
	
	private void audit(String schema) throws Exception {
		MetaDataBase meta = new MetaDataBase(conn );
		ResultSet tables = meta.getResultTables(schema);
		int tot= 0; int pos =0;
		if (!new File(filePath).exists()) 
			this.createNewFile();
		this.save("-----------------------------------------------------------------------------");
		this.save("                      QUANTIDADE DE CAMPOS DAS TABELAS");
		this.save("         TABELA                                                 CAMPOS");
		this.save("-----------------------------------------------------------------------------");
		if (!print)  if (pos % 10 ==0) System.out.print(audityType + "  Tabelas: " + String.format("%-7.7s",pos));
    	
		while (tables.next())  {
//			String tableName = tables.getString(3);
//			ResultSet rs = conn.createStatement().executeQuery("select count(*) from " + schema+ tableName);
//			int count = 0;
//			if (rs.next()) { count = rs.getInt(1); }
//			String msg = "   - " + String.format("%-60.60s",tableName) + " " + count;
//			tot += count; pos++;
//			this.save(msg);
//			if (!print) if (pos % 10 ==0) System.out.print("\b\b\b\b\b\b\b" + String.format("%-7.7s",pos));
		}
		if (!print) System.out.print("\b\b\b\b\b\b\b" + String.format("%-7.7s",pos));
		if (tot==0) this.save("Not found tables in database.");
		this.save("-----------------------------------------------------------------------------");
		this.save(String.format("%-55.55s","") +" TOTAL:  "  + tot);
		System.out.println("   Arquivo: '"+this.filePath+"'.");
	}
	

}
