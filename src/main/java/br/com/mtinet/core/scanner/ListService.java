package br.com.mtinet.core.scanner;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class ListService{
   private String FILE_LOG = "log\\list.log";
   private Manager manager = null;
   
   public ListService(Manager manager) {
	   this.manager = manager;
   }
   
   public void listObject() {
	   deleteLog();
	   listServices();	   
	   listNets() ;	   
	   listServicesInHost();
	}
   
    private void deleteLog() {
    		File f = new File(FILE_LOG);
    		if (f.exists()) {
    			if (!f.isDirectory()) f.delete();
    		}
    }
   
	private void printLine() {
		String line = "--------------------------------------------------------------------------------";
		logOperation(line);
	}
	
	public void listNets() {
//		printLine();
//		logOperation("\t\t\t REDES DE EQUIPAMENTOS CONFIGURADAS");
//		ObjectSet result0 = manager.getObjectContainer().queryByExample(new Net());
//		printLine();
//		logOperation("\tNET\t\t  IP_MIN\t   IP_MAX\t HOSTS\tDESCRIPTION");
//		printLine();
//		try{
//			for(int i = 0 ; i< result0.size(); i++ ) {
//				Net obj = (Net) result0.get(i);
//				logOperation(obj.getId() + ". " + "NET: " + String.format ("%-15.15s",obj.getName()) + " "+ String.format ("%-16.16s",obj.getIp_min()) + " " + String.format ("%-16.16s",obj.getIp_max()) + " "  + obj.getHostsCount() + " - "+  obj.getDescription());
//				
//				int cont = 1;
//				for (Host host : obj.getHosts()) {
//					if (host!=null)
//						logOperation("   "+ String.format ("%-4.4s",obj.getId() + "."+cont++) + " - HOST: "  +String.format ("%-4.4s","(" +host.getId()+")") + " " +String.format ("%-18.18s",host.getAlias()) + " IP: " + host.getIp() + " "+String.format ("%-10.10s","("+ host.getSystem()+ ") ")) ;
//				}
//				logOperation("\n");
//			}
//			printLine();
//			logOperation("\n");
//		}catch (Exception e) {System.out.println("Erro: " + e.getMessage() );}
	}
	
	public void listServices() {
//		try{
//			printLine();
//			logOperation("\t\t\t SERVICOS DO REPOSITORIO");
//			printLine();
//			logOperation("   ID\t SERVICO \t TIPO\t\t DESCRICAO");
//			printLine();
//			ObjectSet result2 = manager.getObjectContainer().queryByExample(new Service());
//			for(int i = 0 ; i< result2.size(); i++ ) {
//				Service obj = (Service) result2.get(i);
//				logOperation(" - "+ String.format ("%-5.5s",obj.getId().toString())+ String.format ("%-15.15s",obj.getName())+ String.format ("%-10.10s",obj.getType())+ " -  "+String.format ("%-25.25s",obj.getDescription()));
//			}
//			printLine();
//			logOperation("\n");
//		}catch (Exception e) {System.out.println("Erro: " + e.getMessage() );}
	}
	
	public void listServicesInHost() {
//		ObjectSet result3 = dataBase.queryByExample(new ServiceHost());		
//		for(int i = 0 ; i< result3.size(); i++ ) {
//			ServiceHost obj = (ServiceHost) result3.get(i);
//			System.out.println("HOST: " + obj.getHost().getAlias() + "\t\t SERVICE: " + obj.getService().getName() + " \t PORTA: " + obj.getPort());
//		}
	}
	
	private void logOperation(String msg) {
		System.out.println(msg);
		try{ 
			 FileWriter fstream = new FileWriter(FILE_LOG,true);
			 BufferedWriter out = new BufferedWriter(fstream);
			 out.write(msg+"\r\n");
			 out.close();
		}catch (Exception e){
			 System.err.println("Error: " + e.getMessage());
		}
	}
	
	
}
