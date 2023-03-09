package br.com.mtinet.core.config;

import br.com.mtinet.core.connect.DataBaseConfig;

public class SqlLiteConfig  extends SocketDBConfig{
	
	public SqlLiteConfig(String[] args)  throws Exception {
		super(args);
		this.url = "jdbc:postgresql://"+host+":"+port+"/" + dataBase;
	}
	
	public DataBaseConfig getConfig() {	
		return new DataBaseConfig(dataBaseType,driver,host,port,dataBase,url,user,password,driverFile);
	}
}
