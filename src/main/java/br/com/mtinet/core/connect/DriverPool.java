package br.com.mtinet.core.connect;

import java.io.File;
import java.util.ArrayList;

public class DriverPool {
	ArrayList<DriverConfig> drivers;
	
	public DriverPool() {
		this.drivers = new ArrayList<DriverConfig>();		
	}
	
	public void addDriver(String driverName, String driverClass, String driverFile)throws Exception {
		this.addDriver(new DriverConfig(driverName,driverClass,driverFile));
	}
	
	public void addDriver(DriverConfig driver) throws Exception{
		this.drivers.add(driver);
	}
	
	public DriverConfig getDriver(String name) throws Exception {
		for (DriverConfig driver : this.drivers) {
			if (driver.getDriverName().toUpperCase().equals(name.toUpperCase()))  {
				if (driver.getDriverClass().length() == 0 )
					throw new Exception("ERRO: O atributo driverClass do driver '" + name +"' nao esta configurado corretamente no arquivo 'config/config.xml'");
				else if (driver.getDriverFile().length() == 0) 
					throw new Exception("ERRO: O atributo driverFile do driver '" + name +"' nao esta configurado corretamente no arquivo 'config/config.xml'");
				else return driver;
			}
				
		}
		throw new Exception("Driver '" + name +"' nao existe no arquivo 'config/config.xml'");
	}
	
	public  String getDriverLoadList() {
		String value = "";
		for (DriverConfig driver : this.drivers) {
			if (testDriverFileExist(driver.getDriverFile())) value += driver.getDriverName() + ", ";
		}
		if (value.length()>2) value = value.substring(0, value.length()-2);
		return value;
	}
	
	private boolean testDriverFileExist(String filePath) {
		if (filePath=="") return false;
		return new File("jdbc//"+filePath).exists();
	}

}
