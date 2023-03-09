package br.com.mtinet.core;

import java.io.File;
import java.util.List;

import br.com.mtinet.core.scanner.Host;
import br.com.mtinet.core.scanner.IPHander;
import br.com.mtinet.core.scanner.ListService;
import br.com.mtinet.core.scanner.Manager;
import br.com.mtinet.core.scanner.Net;
import br.com.mtinet.core.scanner.ScannerHost;
import br.com.mtinet.core.scanner.Service;
import br.com.mtinet.exception.InvalidCommandRunException;
import br.com.mtinet.util.FileResource;
import br.com.mtinet.util.MessageCommon;

public class ServiceScanner {
	
	private Manager man;

	public ServiceScanner(String[] args) {
		execute(args);
	}
	
	private Manager getManager() {
		if (man==null) man = new Manager();
		return man;
	}
	
	public  void execute(String[] args) {
		int index = 1;
		if (args.length > 1) {
			if (args[index].toUpperCase().equals("LIST")) listNets();
			else if (args[index].toUpperCase().equals("RUN")) run(args);
			else if (args[index].toUpperCase().equals("ADD")) addNets(args);
			else if (args[index].toUpperCase().equals("DEL")) deleteNets(args);
			else if (args[index].toUpperCase().equals("CONN")) connect(args);
			else if (args[index].toUpperCase().equals("DROP")) dropDatabase();
			else if (args[index].toUpperCase().equals("SERVICE")) configureService(args);
			else if (args[index].toUpperCase().equals("HOST")) connectHost(args);
//			else if (args[index].toUpperCase().equals("TEST"))testSystem();
			else showMessage();
		} else showMessage();
		this.getManager().close();
	}
	
	public void configureService(String[] args) {
		System.out.println(FileResource.getText("scanner.msg.service.config.service"));
		if (args.length > 2) {
			if (args[2].toUpperCase().equals("ADD")) {
				if (args.length == 6) {
					this.getManager().insertObject(new Service(this.getManager().getMaxId(Service.class),args[3],args[4],args[5],false)) ;
					System.out.println(FileResource.getText("scanner.msg.service.config.add",args[4]));
				} else System.out.println(FileResource.getText("scanner.msg.service.config.add.error") );
			} else if (args[2].toUpperCase().equals("DEL")) {
				if (args.length == 4) {
					removeService(args);
					System.out.println(FileResource.getText("scanner.msg.service.config.del"));
				} else System.out.println(FileResource.getText("scanner.msg.service.config.del.error"));
			} else if (args[2].toUpperCase().equals("LIST")) {
				
			} else System.out.println(FileResource.getText("scanner.msg.service.config.error") );
		} else System.out.println(FileResource.getText("scanner.msg.service.config.help"));
		
	}
	
	private  void removeService(String[] args) {
		Service obj = null;
	    try {
	    	long id = new Long(args[3]).longValue();	    	
	    	obj = this.getManager().findServiceById(id);
	    }catch(Exception e) {
	    	obj = this.getManager().findServiceByName( args[3]);
	    }
//	    this.getManager().getConnection().delete(obj);
//	    this.getManager().getConnection().commit();
		System.out.println(FileResource.getText("scanner.msg.service.deleted",obj.getName()));	
	}
	
	public void connectHost(String[] args) {
		System.out.println(FileResource.getText("scanner.msg.connect.host.config") );
		if (args.length == 7) {
			
		} else System.out.println(FileResource.getText("scanner.msg.connect.host.error") );
	}
	
	public  void showMessage() {
		System.out.println(FileResource.getText("scanner.msg.help.title"));
		System.out.println(FileResource.getText("scanner.msg.help.title.list"));
		System.out.println(FileResource.getText("scanner.msg.help.title.run"));
		System.out.println(FileResource.getText("scanner.msg.help.title.add"));
		System.out.println(FileResource.getText("scanner.msg.help.title.del"));
		System.out.println(FileResource.getText("scanner.msg.help.title.conn"));
		System.out.println(FileResource.getText("scanner.msg.help.title.drop"));
		System.out.println(FileResource.getText("scanner.msg.help.title.service"));
		System.out.println(FileResource.getText("scanner.msg.help.title.host"));
		System.out.println(FileResource.getText("scanner.msg.help.title.help"));
		MessageCommon.printLine();
	}
	
	
	public  void listNets() {
		new ListService(this.getManager()).listObject();	
	}	
	
	
	public  void run(String[] args) {
		int type = ScannerHost.SCANNER_TYPE_INETADRESS;
		int scanner = Manager.SCANNER_ALL;
		try {
			this.getManager().setScannerType(type) ;
			this.getManager().setTimeout(2000) ;
			this.getManager().scanner(scanner);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	public  void addNets(String[] args) {
		try {
			if (args.length == 6) {
				this.getManager().insertObject(new Net(this.getManager().getMaxId(Net.class),args[2],args[3],args[4],args[5]));
				System.out.println("The net "+args[2] +" inserted with sucess...");
			} else {
				System.out.println("java -jar JSqlDB.jar SCANNER ADD <<net>> <<description>> <<IP_MIN>> <<IP_MAX>>");
			}
		} catch(Exception e) {System.out.println(e.getMessage());}
		
	}
	
	private void testIpHostinNet(Net net, Host host) throws Exception{
		IPHander ipMax = new IPHander(net.getIp_max()) ;
		IPHander ipMin = new IPHander(net.getIp_min()) ;
		IPHander ipHost = new IPHander(host.getIp()) ;
		if(ipMax.getIpNumber()<ipHost.getIpNumber()) throw new Exception("Error:  IP at host not in net block.");
		if(ipMin.getIpNumber()>ipHost.getIpNumber()) throw new Exception("Error:  IP at host not in net block.");
	}
	
	public  void connect(String[] args) {
		Net net ;
		try {
			if (args.length > 2) {
				net = (Net) this.getManager().findObject(Net.class,new Long(args[2]));
				if (args.length == 7) {
					if (args[3].toUpperCase().equals("ADD")) {
						 if (args[4].toUpperCase().equals("HOST")) {
							 Host host = new Host(this.getManager().getMaxId(Host.class),args[5],args[6]," "," "," "," ");
//							 try{
//								 testIpHostinNet(net,host);
//								 this.getManager().getConnection().store(host);
//								 List<Host> hosts = net.getHosts();
//								 hosts.add(host);
//								 net.setHosts(hosts);					 
//								 this.getManager().getConnection().store(net);
//								 this.getManager().getConnection().commit();
//								 System.out.println("Host "+host.getAlias()+" inserted in " + net.getName());
//								 new ListService(this.getManager()).listObject();
//								 this.getManager().close();
//							 }catch(UniqueFieldValueConstraintViolationException exc) {
//								 System.out.println("Erro: O ip " + host.getIp() + " ja existe cadastrado.");
//								 this.getManager().getConnection().rollback();
//							 } catch(Exception x) { System.out.println(x.getMessage());};
							 
						 } else if (args[4].toUpperCase().equals("SERVICE")) {
							 
						 } else {
							 throw new InvalidCommandRunException();
						 }
						
					} else if (args[3].toUpperCase().equals("DEL")) {
						removeHostInNet(args);
					} else System.out.println("Falta o parametro <<[ADD,DEL]>> <<[HOST,SERVICE]>> <<ALIAS>> <<IP>>");
				} else if (args.length == 5) {
					if (args[3].toUpperCase().equals("DEL")) {
						removeHostInNet(args);
					}	
				} else System.out.println("Falta o parametro <<[ADD,DEL]>> <<[HOST,SERVICE]>> <<ALIAS>> <<IP>>");		
			}else System.out.println("Falta parametros <<id_Net>> <<[ADD,DELL]>>");
		} catch(InvalidCommandRunException r) {
			System.out.println("Falta o parametro <<[ADD,DEL]>> [HOST,SERVICE] <<ALIAS>> <<IP>>");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public  void deleteNets(String[] args) {
		if (this.getManager().deleteNet(new Long(args[2]))) System.out.println("Rede removida da base de dados..");
		else System.out.println("Erro na exclusao");
	}
	
	private  void removeHostInNet(String[] args) {
		Host host = null;
	    Long idNet = new Long(args[2]);
	    try {
	    	long id = new Long(args[4]).longValue();	    	
	    	host = this.getManager().findHostInNetById(idNet, id);
	    }catch(Exception e) {
	    	host = this.getManager().findHostInNetByName(idNet, args[4]);
	    }
	    
//	    this.getManager().getConnection().delete(host);
//	    this.getManager().getConnection().commit();
		System.out.println("Host " + host.getAlias() + " " + host.getIp() + " deleted...");	
	}
	
	public void dropDatabase() {
		this.getManager().close();
		File file = new File(Manager.DB4OFILENAME);
		file.delete();
		System.out.println("DataBase droped with sucess...");
	}
	
	public void reconfigure() {
		this.getManager().insertObject(new Service(1L,"APACHE","HTTP","Servidor Web Apache",false)) ;
		this.getManager().insertObject(new Service(2L,"JBOSS","HTTP","Servidor web",false)) ;
		this.getManager().insertObject(new Service(3L,"TOMCAT","HTTP","Servidor web",false)) ;
		this.getManager().insertObject(new Service(4L,"JETTY","HTTP","Servidor web",false)) ;
		this.getManager().insertObject(new Service(5L,"GERONIMO","HTTP","Servidor web",false)) ;
		this.getManager().insertObject(new Service(6L,"ORACLE","SGDB","Servidor de banco de dados",false)) ;
		this.getManager().insertObject(new Service(7L,"POSTGRESQL","SGDB","Servidor de banco de dados",false)) ;
		this.getManager().insertObject(new Service(8L,"MYSQL","SGDB","Servidor de banco de dados",false)) ;
		this.getManager().insertObject(new Service(9L,"PROGRESS","SGDB","Servidor de banco de dados",false)) ;
		this.getManager().insertObject(new Service(10L,"SQLSERVER","SGDB","Servidor de banco de dados",false)) ;
		
		
//		this.getManager().insertObject(new ServiceHost(1L,(Host) this.getManager().findObject(Host.class, 1L),(Service) this.getManager().findObject(Service.class, 7L),5432)) ;
//		this.getManager().insertObject(new ServiceHost(2L,(Host) this.getManager().findObject(Host.class, 2L),(Service) this.getManager().findObject(Service.class, 6L),1521)) ;
//		this.getManager().insertObject(new ServiceHost(3L,(Host) this.getManager().findObject(Host.class, 1L),(Service) this.getManager().findObject(Service.class, 6L),1521)) ;
		
	}

}
