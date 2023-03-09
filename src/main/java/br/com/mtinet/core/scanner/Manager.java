package br.com.mtinet.core.scanner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Manager {
	
	private String FILE_LOG = "log\\scanner.log";
//	private ObjectContainer dataBase;
	private int scannerType = ScannerHost.SCANNER_TYPE_COMMAND;
	private int timeout;
	public static final int SCANNER_ALL = 1;
	public static final int SCANNER_PING = 2;
	public static final int SCANNER_SERVICE = 3;
	public static final String DB4OFILENAME = "data/dataBase.dbo";

	public Manager() {		
		this.open() ;
	}
	
//	public ObjectContainer getObjectContainer() {
//		return dataBase;
//	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getScannerType() {
		return scannerType;
	}
	public void setScannerType(int scannerType) {
		this.scannerType = scannerType;
	}
	
	public void open() {
//		if (this.dataBase==null) this.getConnection();
	}
	
//	public ObjectContainer getConnection() {		
//		if (dataBase==null) {
//			EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
//			conf.common().objectClass(Net.class).cascadeOnUpdate(true);
//			conf.common().objectClass(Host.class).objectField("ip").indexed(true);
//			conf.common().add(new UniqueFieldValueConstraint(Host.class, "ip"));
//			dataBase = Db4oEmbedded.openFile(conf, DB4OFILENAME);			
//		}
//			
//		return dataBase;
//	}
	
	public void insertHost(Net net, Host host) {
		host.setAlias(host.getAlias().toUpperCase());
		net.getHosts().add(host);
	}
	
	public void insertObject(Object obj) {
//		this.getConnection().store(obj);
	}
	
	public Long getMaxId(Class cl) {
//		int maxId = dataBase.queryByExample(cl).size();
//		return new Long(maxId + 1) ;
		return null;
	}
	
	public Object findObject(Class cl, Long id) {
//		ObjectSet listObj = null;
//		if (cl == Net.class) listObj = dataBase.get(new Net(id));
//		if (cl == Host.class) listObj = dataBase.get(new Host(id));
//		if (cl == Service.class) listObj = dataBase.get(new Service(id));
//		if (cl == ServiceHost.class) listObj = dataBase.get(new ServiceHost(id));
//		return listObj.get(0);
		return null;
	}
	
	public Service findServiceById(Long id) {
//		ObjectSet listObj = null;
//		Service obj = null;
//		Service critery = new Service();
//		critery.setId(id);
//		listObj = dataBase.get(critery);
//		for (int p = 0;p<listObj.size();p++) {
//			obj = (Service) listObj.get(p);
//			if (obj.getId().equals(id));				
//		}
//		return obj;
		return null;
	}
	
	public Service findServiceByName(String name) {
//		ObjectSet listObj = null;
//		Service obj = null;
//		Service critery = new Service();
//		critery.setName(name);
//		listObj = dataBase.get(critery);
//		for (int p = 0;p<listObj.size();p++) {
//			obj = (Service) listObj.get(p);
//			if (obj.getName().toUpperCase().equals(name.toUpperCase()));				
//		}
//		return obj;
		return null;
	}
	
	public Host findHostInNetById(Long idNet,Long idHost) {
//		ObjectSet listObj = null;
//		Host obj = null;
//		Host critery = new Host();
//		critery.setId(idHost);
//		listObj = dataBase.get(critery);
//		for (int p = 0;p<listObj.size();p++) {
//			obj = (Host) listObj.get(p);
//			if (obj.getId().equals(idHost));				
//		}
//		return obj;
		return null;
	}
	
	public Host findHostInNetByName(Long idNet, String hostName) {
//		ObjectSet listObj = null;
//		Host obj = null;
//		Host critery = new Host();
//		critery.setAlias(hostName.toUpperCase());
//		listObj = dataBase.get(critery);
//		for (int p = 0;p<listObj.size();p++) {
//			obj = (Host) listObj.get(p);
//			if (obj.getAlias().toUpperCase().equals(hostName.toUpperCase()));				
//		}
//		return obj;
		return null;
	}
	
	public boolean deleteNet(Long id) {
//		boolean value = false;
//		Net net = (Net) dataBase.get(new Net(id)).get(0);
//		dataBase.delete(net);
//		value = true;
//		return value;
		return false;
	}
	
	
	
	public void scanner(int type) {

		ScannerHost scanner = new ScannerHost(this.scannerType);
//		System.out.println("JCoruja version 0.2  -  STARTUP IN: ");	
//
//			printLine();
//			System.out.println("\t\t ---  SCANNER WITH PING IN HOSTS ---");	
//			printLine();
//			ObjectSet result = dataBase.queryByExample(new Net());
//			for(int i = 0 ; i< result.size(); i++ ) {
//				Net obj = (Net) result.get(i);
//				System.out.println(" - Scanner in Net: " + obj.getName());
////				printLine();
//				for (Host host : obj.getHosts()) {
//					if (host!=null) {
//						String msg =  " " + String.format("%-16.16s",host.getIp()) + " " +String.format("%-18.18s",host.getAlias()) ;
//						scanner.setHost(host.getAlias()) ;
//						scanner.setTimeout(timeout);
//						msg +=  scanner.scanner();
//						System.out.println(msg);
//						logScanner(msg);
//						JTextField field = new JTextField("",3);
//						
//						
//					}
//					
//				}	
//				printLine();
//			}
//
//		
//		if (type == SCANNER_ALL || type ==SCANNER_SERVICE) {
//			System.out.println("\nScanner ports: ");	
//			printLine();
//			ObjectSet result3 = dataBase.queryByExample(new ServiceHost());
//			for(int i = 0 ; i< result3.size(); i++ ) {
//				ServiceHost obj = (ServiceHost) result3.get(i);
//				System.out.print("HOST: " + obj.getHost().getAlias() + 
//						"\t\t SERVICE: " + obj.getService().getName() +
//						" \t\t PORTA: " + obj.getPort() + " \t ");
//				scanner.setHost(obj.getHost().getAlias()) ;
//				scanner.setPort(obj.getPort()) ;
//				scanner.setTimeout(timeout);
//				scanner.scannerPort();
//			}
//			printLine();
//		}
		
	}
	
	private void logScanner(String msg) {
		try{
			 Date d = new Date();
			 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");   
			 FileWriter fstream = new FileWriter(FILE_LOG,true);
			 BufferedWriter out = new BufferedWriter(fstream);
			 out.write(formato.format(d)+msg+"\n");
			 out.close();
		}catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void printLine() {
		System.out.println("--------------------------------------------------------------------------------");
	}
	
	public void close() {
//		this.dataBase.close();
	}
	

}
