package br.com.mtinet.core.script.execFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.MessageCommon;

public class ExecJavaHandler extends AbstractHandler implements Handler{

	private String dsnTarget;
	protected boolean print = false;
	protected String fileParam;
	
	public ExecJavaHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener ) throws Exception {
		this.setParameters(tagNode,poolConn, listener);
		this.print = AttributeLoader.getAttributeValue(tagNode,"print",true).toLowerCase().equals("true") ;
		this.printLog = !print;
		printTitle() ;
	}
	
	private void printTitle() {
		MessageCommon.print("\n     ---  EXECUTE FILE OPERATION ---" );
	
	}
	
	public void stop() {
		
	}
	
	public void execute() throws Exception {
		NodeList tags = tagNode.getChildNodes();
		for (int a=0;a<tags.getLength();a++) {
			Node tag = tags.item(a);
			if (tag.getNodeType()== Node.ELEMENT_NODE) {
				this.filePath = AttributeLoader.getAttributeValue(tag,"filePath",true) ;
				this.fileParam = AttributeLoader.getAttributeValue(tag,"param",true) ;
				if (tag.getNodeName().toUpperCase().equals("EXECJAVA")) {					
					executeJavaFile();
				} else if (tag.getNodeName().toUpperCase().equals("EXECSHELL")) {
					executeShellFile();
				} else {
					
				}
			}
		}

	}
	
	private void executeJavaFile( ) {
		 String[] cmds = {"java","-jar", "'" + this.filePath + " " + fileParam + "'" };  
		  try{  
//			  Process process  =  new ProcessBuilder( this.filePath, "-teste").start();
			   Process process = new ProcessBuilder(cmds).start();
		       InputStream is = process.getInputStream();
		       InputStreamReader isr = new InputStreamReader(is);
		       BufferedReader br = new BufferedReader(isr);
		       String line;
		       System.out.printf("- JAR_FILE: %s \n", this.filePath);
		       while ((line = br.readLine()) != null) {
		         System.out.println(line);
		       } 
		   }catch(Exception e){  
		   e.printStackTrace();  
		   }  
	}
	
	private void executeShellFile() {
		 String[] cmds = {"/bin/sh","-c", this.filePath + " " + this.fileParam} ; 
		  try{  
			   Process process = new ProcessBuilder(cmds).start();
		       InputStream is = process.getInputStream();
		       InputStreamReader isr = new InputStreamReader(is);
		       BufferedReader br = new BufferedReader(isr);
		       String line;
		       System.out.printf("- SHELL_FILE: %s \n", this.filePath,this.fileParam);
		       while ((line = br.readLine()) != null) {
		         System.out.println(line);
		       } 
		   }catch(Exception e){  
		        e.printStackTrace();  
		   }  
	}

}
