package br.com.mtinet.core.config;

import br.com.mtinet.core.connect.DataBaseConfig;

public class MySqlConfig  extends SocketDBConfig {

	public MySqlConfig(String[] args)  throws Exception  {
		super(args);
		this.url = "jdbc:mysql://"+host+":"+port+"/" + dataBase;
	}
	
	public DataBaseConfig getConfig() {	
		return new DataBaseConfig(dataBaseType,driver,host,port,dataBase,url,user,password,driverFile);
	}
}
