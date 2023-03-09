package br.com.mtinet.core.dataBase;

import java.util.ArrayList;
import java.util.Scanner;

import br.com.mtinet.core.connect.ConnectionConfig;

public class PoolConnection {
	ArrayList<ConnectionConfig> poolConnection;
	private DynamicDriverManager manager;
	
	public PoolConnection(DynamicDriverManager manager) {
		poolConnection = new ArrayList<ConnectionConfig>();
		this.manager = manager;
	}
	
	public void add(ConnectionConfig config) throws Exception {
		try {
			testPassword(config);
			System.out.print(" - Datasource: " + config.getName() + ".........");
			poolConnection.add(manager.createConnection(config));
			System.out.println("OK");
		} catch(Exception e) {System.out.println("FALHA");throw e;}
		
	}
	
	public ConnectionConfig getConn(String name) {
		for (ConnectionConfig conf: poolConnection) {
			if (conf.getName().equals(name)) return conf;
		}
		return null;
	}
	
	public ConnectionConfig getConn(int index) {
		 return poolConnection.get(index);
	}
	
	public void testDataSource() {
		
	}
	
	private void testPassword(ConnectionConfig config) {
		if (!isPasswordValid(config)) {
			String pwd = changePassword(config);
			config.setPassword(pwd);
		}
	}
	
	private boolean isPasswordValid(ConnectionConfig config) {
		if (config.getPassword().equals("*")) return false;
//		if (config.getPassword().equals("")) return false;
		return true;
	}
	
	private String changePassword(ConnectionConfig config) {
		String pwd = "123";
		Scanner reader = new Scanner(System.in);
	 	System.out.print("Digite a senha para o usu�rio '"+config.getUser() +"' do BD "+config.getName().toUpperCase() +":");  	 	
	 	pwd = reader.next();
		reader = null;
		return pwd;
	}

}
