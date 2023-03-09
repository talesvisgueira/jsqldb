package br.com.mtinet.core.script;

import java.sql.Connection;

public abstract class AbstractFactory {
	protected String filePath;
	protected Connection conn;
	protected String schema;
	protected boolean print = false;
	
	
	
}
