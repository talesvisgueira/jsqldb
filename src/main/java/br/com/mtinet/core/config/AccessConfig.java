package br.com.mtinet.core.config;

import br.com.mtinet.core.connect.DataBaseConfig;

public class AccessConfig extends FileDBConfig {
	
	public AccessConfig(String[] args) throws Exception {
		super(args);
	}	

	public DataBaseConfig getConfig() {	
		this.url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="  + dataBase;
		return new DataBaseConfig(dataBaseType,driver,host,port,dataBase,url,user,password,driverFile);
	}

}
