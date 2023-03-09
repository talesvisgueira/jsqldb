package br.com.mtinet.core.config;

import br.com.mtinet.core.connect.DataBaseConfig;

public class ProgressConfig extends SocketDBConfig {

	public ProgressConfig(String[] args) throws Exception {
		super(args);
		this.url = "jdbc:oracle:thin:@"+host+":"+port+":" + dataBase;
	}
	
	public DataBaseConfig getConfig() {	
		return new DataBaseConfig(dataBaseType,driver,host,port,dataBase,url,user,password,driverFile);
	}
}
