package br.com.mtinet.core.config;

import br.com.mtinet.core.connect.DataBaseConfig;

public class SQLServerConfig  extends SocketDBConfig {
	
	public SQLServerConfig(String[] args)  throws Exception  {
		super(args);
		this.url = "jdbc:jtds:sqlserver://"+host+":"+port+";databaseName=" + dataBase;
	}
	
	public DataBaseConfig getConfig() {	
		return new DataBaseConfig(dataBaseType,driver,host,port,dataBase,url,user,password,driverFile);
	}
}
