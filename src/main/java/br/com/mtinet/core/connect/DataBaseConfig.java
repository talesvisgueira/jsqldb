package br.com.mtinet.core.connect;

public class DataBaseConfig {
		
	private String dataBaseType;
	private String driver;
	private String url;
	private String host;
	private String port;
	private String dataBase;
	private String user;
	private String password;
	private String driverFile;
	private String parameter;
	
	public static final String DRIVER_ORACLE = "ORACLE";
	public static final String DRIVER_POSTGRESQL = "POSTGRESQL";
	public static final String DRIVER_MYSQL = "MYSQL";
	public static final String DRIVER_SQLSERVER= "SQLSERVER";
	public static final String DRIVER_MSACCESS = "MSACCESS";
	public static final String DRIVER_PROGRESS = "PROGRESS";
	public static final String DRIVER_FIREBIRD = "FIREBIRD";
	public static final String DRIVER_DERBY = "DERBY";
	public static final String DRIVER_DB2 = "DB2";
	public static final String DRIVER_H2 = "H2";
	public static final String DRIVER_HSQLDB = "HSQLDB";
	public static final String DRIVER_SQLLITE = "SQLLITE";
	public static final String DRIVER_LDAP = "LDAP";
	
	
	public DataBaseConfig(String dataBaseType,String driver,String host,String port, String dataBase,String url,String user,String password,String driverFile) {
		this.dataBaseType = dataBaseType;
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.dataBase = dataBase;
		this.user = user;
		this.password = password;
		this.url = url;
		this.driverFile = driverFile;
	}
	
	public String getDataBaseType() {
		return dataBaseType;
	}
	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
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
