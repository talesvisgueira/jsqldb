package br.com.mtinet.core.script.dbaudit;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.connect.ConnectionConfig;
import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.MessageCommon;

public class AuditHandler extends AbstractHandler implements Handler{

	private String dsnTarget;
	protected boolean print = false;

	public AuditHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener ) throws Exception {
		this.setParameters(tagNode,poolConn, listener);
		this.dsnTarget = AttributeLoader.getAttributeValue(tagNode,"dsn",true);
		this.print = AttributeLoader.getAttributeValue(tagNode,"print",true).toLowerCase().equals("true") ;
		this.printLog = !print;
		printTitle() ;
	}
	
	private void printTitle() {
		MessageCommon.print("\n                    AUDITORIA DO DATASOURCE " + dsnTarget.toUpperCase() );
//		MessageCommon.printLine();		
	}
	
	public void stop() {
		
	}
	
	public void execute() throws Exception {
		NodeList dbaudit = tagNode.getChildNodes();
		ConnectionConfig conf = poolConn.getConn(dsnTarget);
		
		for (int a=0;a<dbaudit.getLength();a++) {
			Node audit = dbaudit.item(a);
			if (audit.getNodeType()== Node.ELEMENT_NODE) {
				String filePath = AttributeLoader.getAttributeValue(audit,"filePath",true);
				Audit  factory =  AuditFactory.createAudit(audit.getNodeName(),conf.getConn(),filePath,schema,this.print );								
				factory.execute();
			}
		}
		MessageCommon.printLine();
	}
}
