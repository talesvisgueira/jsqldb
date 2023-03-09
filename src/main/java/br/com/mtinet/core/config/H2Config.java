package br.com.mtinet.core.config;

import br.com.mtinet.core.connect.DataBaseConfig;

public class H2Config  extends SocketDBConfig {
	
	public H2Config(String[] args)  throws Exception  {
		super(args);
		this.url = "jdbc:h2://"+host+":"+port+"//" + dataBase;
	}
	public DataBaseConfig getConfig() {	
		return new DataBaseConfig(dataBaseType,driver,host,port,dataBase,url,user,password,driverFile);
	}
}
