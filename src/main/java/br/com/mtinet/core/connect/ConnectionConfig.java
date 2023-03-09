package br.com.mtinet.core.connect;

import java.sql.Connection;

public class ConnectionConfig {
	private String name;
	private String driver;
	private String url;
	private String user;
	private String password;
	private Connection conn;

	
	public ConnectionConfig() {

	}
	
	public ConnectionConfig(String name,String driver,String url,String user,String password) {
		super();
		this.name = name;
		this.driver = driver;
		this.user = user;
		this.password = password;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	
	
	
}
