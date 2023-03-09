package br.com.mtinet.core.config;

import br.com.mtinet.core.connect.DataBaseConfig;

public class LdapConfig  extends SocketDBConfig{
	
	public LdapConfig(String[] args)  throws Exception {
		super(args);
		this.url = "JDBC:LDAP://"+host+":"+port+";baseDN=" + dataBase;
	}
	
	public DataBaseConfig getConfig() {	
		return new DataBaseConfig(dataBaseType,driver,host,port,dataBase,url,user,password,driverFile);
	}
}
