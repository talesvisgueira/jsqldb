package br.com.mtinet.core.config;

public abstract class DBConfig {
	protected String dataBaseType = "";
	protected String host = "";
	protected String port = "";
	protected String dataBase = "";
	protected String driver  = "";
	protected String url = "";
	protected String user = "";
	protected String password = "";
	protected String driverFile = "";
	protected String parameter = "";
	
	
	public String getDataBaseType() {
		return dataBaseType;
	}
	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDataBase() {
		return dataBase;
	}
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverFile() {
		return driverFile;
	}
	public void setDriverFile(String driverFile) {
		this.driverFile = driverFile;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	
	
}
