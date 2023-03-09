package br.com.mtinet.core.script.dbddl;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.connect.ConnectionConfig;
import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.MessageCommon;

public class DDLHandler extends AbstractHandler   implements Handler {

	private String dsnTarget;
	protected boolean print = false;
	
	public DDLHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener)  throws Exception {
		this.setParameters(tagNode,poolConn, listener);		
		this.print = AttributeLoader.getAttributeValue(tagNode,"print",true).toLowerCase().equals("true") ;
		this.printLog = !print;
		this.dsnTarget = AttributeLoader.getAttributeValue(tagNode,"dsn",true) ;
		this.filePath = AttributeLoader.getAttributeValue(tagNode,"filePath",true) ;
		this.schema = AttributeLoader.getAttributeValue(tagNode,"schema",true) ;
	}
	
	private void printTitle() {
		 System.out.println("\n            CRIANDO SCRIPT DO DATASOURCE " +this.dsnTarget.toUpperCase());		
		 System.out.println("SCHEMA: " + schema.toUpperCase() );
		 MessageCommon.printLine();
	 }
	
	public void stop() {
		
	}
	
	public void execute() throws Exception {
		ConnectionConfig conf = poolConn.getConn(dsnTarget);
		
		DDLFactory factory = new DDLFactory(conf.getName(),conf.getConn(),filePath,schema,this.print);
		NodeList nodeTables = tagNode.getChildNodes();
		if (nodeTables.getLength()==0) {
			printTitle() ;
			factory.execute();
		}else {
			printTitle() ;
			
			int count = 0;
			for (int a=0;a<nodeTables.getLength();a++) {
				Node tableNode = nodeTables.item(a);
				if (tableNode.getNodeType()== Node.ELEMENT_NODE) {
					String tableName = AttributeLoader.getAttributeValue(tableNode,"name",true) ;
					factory.execute(tableName); count++;
				}
			}
			if (this.printLog) System.out.println(" - Tabelas criadas: " + String.format("%-9.9s",count));
			count = 0;
			factory.setPrintLog(false);
			for (int a=0;a<nodeTables.getLength();a++) {
				Node tableNode = nodeTables.item(a);
				if (tableNode.getNodeType()== Node.ELEMENT_NODE) {
					String tableName = AttributeLoader.getAttributeValue(tableNode,"name",true) ;								
					ArrayList<DDLObject> obj = factory.getForeignKeyByTable(tableName);
					count += obj.size();
				}
			}
			if (this.printLog) System.out.println(" - Foreign key criadas: " + String.format("%-9.9s",count));
			
			System.out.println("\nArquivo '"+filePath+"' foi criado com a estrutura do BD.");
			MessageCommon.printLine();
		}
	}
}
