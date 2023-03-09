package br.com.mtinet.core;

import java.sql.Connection;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.connect.ConfigFactory;
import br.com.mtinet.core.connect.DataBaseConfig;
import br.com.mtinet.core.connect.DriverConfig;
import br.com.mtinet.core.connect.DriverPool;
import br.com.mtinet.core.dataBase.DynamicDriverManager;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.FileLoadSql;
import br.com.mtinet.util.FileResource;
import br.com.mtinet.util.MessageCommon;

public class ServiceConnect {
	private DataBaseConfig configDB = null;
	private String dataBaseType = "";
	private String parameter = null;
	private FileXmlLoader loader;
	private NodeList listOfConfig;
	private DriverPool poolDriver;

	public ServiceConnect(String[] args) throws Exception {		
			this.loadFileConfig("config//config.xml");
			if (args.length>1)   dataBaseType = args[1];
			else throw new Exception(FileResource.getText("connection.msg.notDriver") +
									 FileResource.getText("connection.msg.defatul") + "[" +poolDriver.getDriverLoadList() +"]");			
			DriverConfig driver = poolDriver.getDriver(dataBaseType);
			configDB = ConfigFactory.create(driver,args);
			int size = args.length;
			if (size==8) this.parameter = args[7];
	}
	
	
	private void loadFileConfig(String filePath)  throws Exception {
		this.poolDriver = new DriverPool();
		this.loader = new FileXmlLoader(filePath);
		this.listOfConfig = loader.getNodeList("driver");
		for (int p=0;p<listOfConfig.getLength();p++) {
			Node node = listOfConfig.item(p);
			String driverName = AttributeLoader.getAttributeValue(node,"driverName",true);
			String driverClass = AttributeLoader.getAttributeValue(node,"driverClass",false);
			String driverFile = AttributeLoader.getAttributeValue(node,"driverFile",false);
			poolDriver.addDriver(driverName,driverClass,driverFile);
		}
	}

	public void execute() throws Exception{
		Connection conn = null;
		if (configDB!=null) {			
			showMessage(configDB);	
			FileResource.printBundleInLine("connection.msg.resquest");	
			try {
			    DynamicDriverManager  factory = new DynamicDriverManager();
				factory.addDir(".//jdbc");
				conn = factory.createConnection(configDB.getDriver(), configDB.getUrl(),configDB.getUser(),configDB.getPassword());  
				FileResource.printBundle("connection.msg.resquest.sucess");
			} catch (ClassNotFoundException c) {
				throw new Exception(FileResource.getText("connection.msg.resquest.fail")+ " "+ FileResource.getText("connection.msg.loadDriver.fail") + " " + c.getMessage());
			} catch (Exception e) { 
				configDB = null; 			
				throw new Exception(FileResource.getText("connection.msg.resquest.fail") + " "  + e.getMessage());
			}
			
			try {
				if (this.parameter!=null ) executeFileSql(conn,this.parameter);
				conn.close();				
			} catch (Exception e) { 
				configDB = null; 		
				conn.close();
				throw new Exception(FileResource.getText( e.getMessage()));
			}
			printLineEnd();
		} else throw new Exception(FileResource.getText("connection.msg.configDB.invalid")); 
	}
	
	private void executeFileSql(Connection conn,String filePath) throws Exception {
		ArrayList<String> list = FileLoadSql.loadFile(filePath);		
		for (String sql : list) {
			conn.createStatement().execute(sql.trim());
		}		
	}
	
	public static void showMessage(DataBaseConfig configDB) {
		FileResource.printText(FileResource.getText("connection.msg.label.databaseType")  + configDB.getDataBaseType() );		
		FileResource.printText(FileResource.getText("connection.msg.label.host") + String.format("%-40.40s",configDB.getHost()) + " " + 
				FileResource.getText("connection.msg.label.port") + configDB.getPort());
		FileResource.printText(FileResource.getText("connection.msg.label.user") + String.format("%-40.40s",configDB.getUser()) + "     " +
			  FileResource.getText("connection.msg.label.password") + configDB.getPassword());
		FileResource.printText(FileResource.getText("connection.msg.label.driver") + configDB.getDriverFile() );
		FileResource.printText(FileResource.getText("connection.msg.label.database") + configDB.getDataBase() );
		printLine();
	}

	public static void printLine() {
		MessageCommon.printLine();
	}
	
	public static void printLineEnd() {
		MessageCommon.printLineEnd();
	}
	

}
