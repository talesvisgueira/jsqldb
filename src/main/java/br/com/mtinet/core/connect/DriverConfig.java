package br.com.mtinet.core.connect;

public class DriverConfig {
	private String driverName;
	private String driverClass;
	private String driverFile;
	
	public DriverConfig() {
		
	}
	public DriverConfig(String driverName, String driverClass, String driverFile) {
		this.driverName = driverName;
		this.driverClass = driverClass;
		this.driverFile = driverFile;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getDriverFile() {
		return driverFile;
	}
	public void setDriverFile(String driverFile) {
		this.driverFile = driverFile;
	}
	
	

}
