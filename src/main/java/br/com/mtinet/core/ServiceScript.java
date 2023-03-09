package br.com.mtinet.core;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import br.com.mtinet.core.connect.ConnectionConfig;
import br.com.mtinet.core.dataBase.DynamicDriverManager;
import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.HandlerFactory;
import br.com.mtinet.core.terminal.TerminalProgress;
import br.com.mtinet.core.terminal.TerminalProgressColor;
import br.com.mtinet.core.terminal.TerminalProgressRun;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.FileResource;
import br.com.mtinet.util.MessageCommon;

public class ServiceScript {
	private String taskDefault;
//	private PropertyHandler porpertyHandler ;
//	private NodeList listOfTarget;
//	private NodeList listOfDataSource;
	private PoolConnection poolConn ;
	private String task ;
	private FileXmlLoader loader;
	private DynamicDriverManager manager;
	
	
	public ServiceScript(String[] args) throws Exception  {
		manager = new DynamicDriverManager();	
		manager.addDir("jdbc");
		poolConn = new PoolConnection(manager);	
		int listenerType = 1;
		if (args[0].toUpperCase().equals("EXEC")) listenerType = 2;
		if (args[0].toUpperCase().equals("RUN")) listenerType = 3;
		if (args.length==3)  {		
			loader = new FileXmlLoader(args[1]); 	
			task = args[2];
			if ((task.toLowerCase().equals("-p")) || (task.toLowerCase().equals("-h"))) 
				this.listTarger(args[1]);
			else loadFile(listenerType,args[1]);
		} else if (args.length==2) loadFile(listenerType,args[1]);
		 else FileResource.printBundle("script.msg.default");
	}

	private void loadFile(int listenerType,String filePath) throws Exception {
		if (loader==null) loader = new FileXmlLoader(filePath);
        if (task==null) this.taskDefault = loader.getAttributeValue("default");
        else this.taskDefault= task;
        Node targetNode = getTargetNodeDefault( taskDefault );
        if (targetNode!=null) {
        	 String targetDepends = AttributeLoader.getAttributeValue(targetNode,"depends",false) ;
             String targetName = AttributeLoader.getAttributeValue(targetNode, "name",true);
             String targetDescription = AttributeLoader.getAttributeValue(targetNode, "description",false);
             FileResource.printText(FileResource.getText("script.msg.execute.target") + targetName.toUpperCase() + ": " + targetDescription);
             AttributeLoader.setPropertyHandler(loader.getPropertyHandler());
             this.loadPoolConnection();
     		try{
     			Node targetDepend = getTargetNodeDefault( targetDepends );
     			if (targetDepend!=null) this.executeTarget(listenerType,targetDepend);     			
     	        if (targetNode!=null) this.executeTarget(listenerType,targetNode);
     	        else System.out.println(FileResource.getText("msg.error") + taskDefault + "' " + FileResource.getText("script.msg.error.targertNotExist"));
     	    }catch (SAXParseException err) {
     	    	System.out.println (FileResource.getText("msg.error.parsing") + ", line " 
     	         + err.getLineNumber () + ", uri " + err.getSystemId ());
     	    	System.out.println(" " + err.getMessage ());
     	    }catch (SAXException e) {
     	    	Exception x = e.getException ();
     	    	((x == null) ? e : x).printStackTrace ();
     	    }catch (Throwable t) {
     	    	System.out.println("\n"+t.getMessage());
     	    } 

        } else {
        	 MessageCommon.printLine();
        	 String msg = FileResource.getText("msg.alert") + FileResource.getText("script.msg.error.tag.notExist",taskDefault);
        	 System.out.println(msg);
        	 System.out.println(FileResource.getText("script.msg.tag.valido"));
        	 MessageCommon.printLine();
        	 this.listTarger(filePath);
        }

	}
	
	public void listTarger(String filePath) {
		String targetName; String targetDescription;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse (new File(filePath));
	        doc.getDocumentElement ().normalize ();
			System.out.println(FileResource.getText("script.msg.help.title"));
			MessageCommon.printLine();
			for (int p=0;p<loader.getNodeList("target").getLength();p++) {
				Node node = loader.getNodeList("target").item(p);
				targetName = AttributeLoader.getAttributeValue(node,"name",true);
				targetDescription = AttributeLoader.getAttributeValue(node,"description",false);
				System.out.println("  * " + String.format("%-15.15s",targetName) + " - " + targetDescription);
			}
			MessageCommon.printLine();
		} catch(Exception e) {System.out.println("ERROR: " + e.getMessage());}
		
	}
	
	public void executeTarget(int type, Node targetNode) throws Exception {	
		ProgressListener terminal ;
		switch (type) {
			case 1: terminal = new TerminalProgress(); break;
			case 2: terminal = new TerminalProgressColor(); break;
			case 3: terminal = new TerminalProgressRun(); break;
			default : terminal = new TerminalProgressColor(); break;
		}
		HandlerFactory target = new HandlerFactory(poolConn,targetNode,terminal);
		target.execute();
		Runtime.getRuntime().gc();
	}
	
	private Node getTargetNodeDefault(String target) throws Exception {
		NodeList nodeList = loader.getNodeDefautlList();
		String value;
		Node attributeName;
		Node node;
		for(int s=0; s<nodeList.getLength() ; s++){
            node = nodeList.item(s);
            if(node.getNodeType() == Node.ELEMENT_NODE){
            	attributeName = node.getAttributes().getNamedItem("name");
            	value = attributeName.getNodeValue() ;
            	if (value.toLowerCase().equals(target.toLowerCase())) {
            		return node;
            	}
            }
		}
		return null; 
	}
	
	private void loadPoolConnection() throws Exception {
		FileResource.printBundle("script.msg.testConnection");
		Node node; String path;
		if (this.loader.getNodeList("datasource").getLength()==0) 
			throw new Exception("AVISO: Falta configurar um ou mais 'DataSource'.");
		for(int s=0; s<loader.getNodeList("datasource").getLength() ; s++){
			node = loader.getNodeList("datasource").item(s);
			path = AttributeLoader.getAttributeValue(node,"classpathref",false);
			if (path.length()>0) manager.addDir(path);
			ConnectionConfig config = new ConnectionConfig( 
					AttributeLoader.getAttributeValue(node,"name",true),
					AttributeLoader.getAttributeValue(node,"driver",true),
					AttributeLoader.getAttributeValue(node,"url",true),
					AttributeLoader.getAttributeValue(node,"username",true),
					AttributeLoader.getAttributeValue(node,"password",true) );
			poolConn.add(config);
		}

	}
	

	
}
