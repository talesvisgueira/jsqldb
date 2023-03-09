package br.com.mtinet.core.script.dbaudit;

import java.sql.Connection;

public class AuditFactory {
	public static Audit createAudit(String audityType,Connection conn,String filePath,String schema, boolean print) {
		if (audityType.toUpperCase().equals("RECORDCOUNT")) return new AuditRecordCount(conn,filePath,schema,print);
		else if (audityType.toUpperCase().equals("FIELDCOUNT"))return new AuditFieldCount(conn,filePath,schema,print);
		return null;
	}

}
