package br.com.mtinet.core.connect;

import br.com.mtinet.core.config.AccessConfig;
import br.com.mtinet.core.config.DB2Config;
import br.com.mtinet.core.config.DerbyConfig;
import br.com.mtinet.core.config.FireBirdConfig;
import br.com.mtinet.core.config.H2Config;
import br.com.mtinet.core.config.HSqlDbConfig;
import br.com.mtinet.core.config.LdapConfig;
import br.com.mtinet.core.config.MySqlConfig;
import br.com.mtinet.core.config.OracleConfig;
import br.com.mtinet.core.config.PostgresqlConfig;
import br.com.mtinet.core.config.ProgressConfig;
import br.com.mtinet.core.config.SQLServerConfig;
import br.com.mtinet.core.config.SqlLiteConfig;

public class ConfigFactory {
	
	
	public static DataBaseConfig create(DriverConfig driver ,String[] args) throws Exception{
		
		DataBaseConfig config = null;
		if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_ORACLE)) 
			config = new OracleConfig(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_POSTGRESQL)) 
    		config = new PostgresqlConfig(args).getConfig();
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_MYSQL)) 
    		config = new MySqlConfig(args).getConfig();
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_SQLSERVER)) 
    		config = new SQLServerConfig(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_MSACCESS)) 
    		config = new AccessConfig(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_PROGRESS)) 
    		config = new ProgressConfig(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_FIREBIRD)) 
    		config = new FireBirdConfig(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_DERBY)) 
    		config = new DerbyConfig(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_DB2)) 
    		config = new DB2Config(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_H2)) 
    		config = new H2Config(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_HSQLDB)) 
    		config = new HSqlDbConfig(args).getConfig(); 
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_SQLLITE)) 
    		config = new SqlLiteConfig(args).getConfig(); 	
    	else if (driver.getDriverName().toUpperCase().contains(DataBaseConfig.DRIVER_LDAP)) 
    		config = new LdapConfig(args).getConfig(); 	

		if (config!=null) {
			config.setDataBaseType(driver.getDriverName());
			config.setDriver(driver.getDriverClass());
			config.setDriverFile(driver.getDriverFile());
		}
		return config;
	}
	
}
