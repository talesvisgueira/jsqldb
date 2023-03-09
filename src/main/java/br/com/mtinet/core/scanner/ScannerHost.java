package br.com.mtinet.core.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ScannerHost {
	public static final int SCANNER_TYPE_PROCESS = 1;
	public static final int SCANNER_TYPE_COMMAND = 2;
	public static final int SCANNER_TYPE_SOCKET = 3;
	public static final int SCANNER_TYPE_INETADRESS= 4;
	private String msg = "";
	private String host = "localhost";
	private int port = 80;
	private int timeout = 1000;
	private int type = SCANNER_TYPE_SOCKET;;
	
	public ScannerHost(int type) {
		this.type = type;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String scanner(){

		switch(type) {
			case 1: return pingWithProcess();
			case 2: return pingWithPing();
			case 3: return pingWitSocket();
			case 4: return pingWithInet();
			default: return "";
		}
				
	}
	
	public String getScannerTypeDefinide() {
		switch(type) {
			case 1: return "PROCESS";
			case 2: return "COMMAND"; 
			case 3: return "SOCKET"; 
			case 4: return "INETADRRESS";
			default: return "SCANNER TYPE NOT DEFINID";
		}
	}

	public String  pingWitSocket() {
		int port = 80;	
		try {
			InetAddress addr = InetAddress.getByName(host); 
			SocketAddress sockaddr = new InetSocketAddress(addr, port); 
			Socket socket = new Socket();
			socket.connect(sockaddr, timeout); 
			msg ="Ping OK: ";
			socket.close();
		}catch(Exception e) {msg ="Ping FALHOU: ";}
		return msg;
	}
	
	public String  pingWithInet() {
		
		  try {	
			  InetAddress net = InetAddress.getLocalHost();
			  // Testa se existe rede
			  if (InetAddress.getByName(host).isReachable(timeout))
				  msg = "Ping OK: "  ;
			  else msg = "Ping FALHOU: ";
		  } catch (Exception e) {
			  msg = "Ping FALHOU: " + " - " + e.getMessage();
		  }
		  return msg;
	}

	
	public String  pingWithPing() {
		String texto = "";
		String resposta=""; int fim=0;  boolean kbo=false; 
		String comando = "cmd /c ping -n 1 -w " + this.timeout + " ";
		try {
			Scanner s = new Scanner( Runtime.getRuntime().exec(comando + host).getInputStream());
			  while(s.hasNextLine()) {    
				  texto += resposta;
                 resposta=s.nextLine()+"\n";    
                 fim=resposta.length()-8;  
                 for (int j=0;j<=fim;j++){  				                	  
                     if (resposta.contains("Resposta")){  
                         kbo=true; msg = "Ping OK";  break;
                     }                  
                 }                   			                   
             }  
			  
			  if ( texto.contains("RESPOSTA") || texto.contains("RECEIVED") ){  
                  kbo=true; msg = "Ping OK";  
              } else  msg = "Ping FALHOU: \n" + texto.replace("\n", "");
			  
		} catch(Exception e) { 
			msg = "Ping FALHOU:" + e.getMessage(); }
		return msg;
	}
	


	public String pingWithProcess() {	
		String inputLine;
		String pingCmd = "ping -n 1 -w " + this.timeout + " " + host;	
		String texto = "";
		try {
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(pingCmd);	
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));			
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine.trim());
			    texto += inputLine.trim();
			}
			
			
			in.close();
			 if ( texto.toUpperCase().contains("RESPOSTA") || texto.toUpperCase().contains("RECEIVED") ){  
                 msg = "Ping OK";  
             } else  msg = "Ping FALHOU: \n" + texto.replace("\n", "");
			 
			 
		} catch (IOException e) { msg = "Ping FALHOU: Falha na conex�o" ;}
		return msg;
		
	}


	// If the first argument is a string of digits then we take that
	// to be the port number to use
	private boolean isPortValid() {
		if (Pattern.matches("[0-9]+",new Integer(this.port).toString())) return true;
		else return false;
	}
	
	
	public void scannerPort() {
		try {
			InetAddress addr = InetAddress.getByName(host); 
			SocketAddress sockaddr = new InetSocketAddress(addr, port); 
			Socket socket = new Socket();
			socket.connect(sockaddr, timeout); 
			msg ="Scanner OK: ";
			socket.close();
		}catch(Exception e) {
			msg ="Scanner FALHOU: "+e.getMessage()
		;}
		System.out.println(msg);
	}

}
