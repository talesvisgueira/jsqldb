package br.com.mtinet.core.script;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.dbaudit.AuditHandler;
import br.com.mtinet.core.script.dbddl.DDLHandler;
import br.com.mtinet.core.script.dbdelete.DeleteTableHandler;
import br.com.mtinet.core.script.dbdelete.DropTableHandler;
import br.com.mtinet.core.script.dbexport.ExportHandler;
import br.com.mtinet.core.script.dbimport.ImportHandler;
import br.com.mtinet.core.script.dbmig.MigrationHandler;
import br.com.mtinet.core.script.dbrandom.RandomHandler;
import br.com.mtinet.core.script.dbreplicate.ReplicateHandler;
import br.com.mtinet.core.script.dbreport.ReportHandler;
import br.com.mtinet.core.script.dbsincronize.SincronizeHandler;
import br.com.mtinet.core.script.execFile.ExecJavaHandler;
import br.com.mtinet.util.MessageCommon;

public class HandlerFactory {
	private NodeList tagsNodeList ;
	private PoolConnection poolConn;
	private ProgressListener progress;
	private Handler handler;
	
	public HandlerFactory (PoolConnection poolConn,Node targetNode,ProgressListener progress) {
		this.tagsNodeList = targetNode.getChildNodes();
		this.poolConn = poolConn;
		this.progress = progress;
	}
	
	public void stop() {
		handler.stop();
	}
	
	public void execute() throws Exception {
		MessageCommon.printLine();	
		for (int p=0;p<tagsNodeList.getLength();p++) {			
			Node tagNode = tagsNodeList.item(p);
			if (tagNode.getNodeType()== Node.ELEMENT_NODE) {
				 handler = createHandler(tagNode,this.poolConn,this.progress);
				handler.execute();
			}
		}
	}
	
	public Handler createHandler(Node tagNode,PoolConnection poolConn,ProgressListener progress) throws Exception {
		String tagName = tagNode.getNodeName().toUpperCase();
//		ProgressListener progress = new TerminalProgressColor();
		
		switch(transformParamInIntValue(tagName)) {
			case 1: return new MigrationHandler(tagNode,this.poolConn,progress);  
			case 2: return new DDLHandler(tagNode,this.poolConn,progress); 
			case 3: return new ReplicateHandler(tagNode,this.poolConn,progress); 
			case 4: return new SincronizeHandler(tagNode,this.poolConn,progress); 
			case 5: return new ImportHandler(tagNode,this.poolConn,progress); 
			case 6: return new ExportHandler(tagNode,this.poolConn,progress); 
			case 7: return new RandomHandler(tagNode,this.poolConn,progress); 
			case 8: return new AuditHandler (tagNode,this.poolConn,progress); 
			case 9: return new ReportHandler (tagNode,this.poolConn,progress); 
			case 10: return new DeleteTableHandler (tagNode,this.poolConn,progress); 
			case 11: return new DropTableHandler (tagNode,this.poolConn,progress);
			case 12: return new ExecJavaHandler(tagNode,this.poolConn,progress);
			default: throw new Exception("ERRO: Tag '" + tagName + "' nao foi implementada nesta versao...");
		}

	}
	
	private int transformParamInIntValue(String value) {
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBMIG)) return 1;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBDDL)) return 2;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBREPLICATE)) return 3;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBSINCRONIZE)) return 4;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBIMPORT)) return 5;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBEXPORT)) return 6;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBRANDOM)) return 7;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBAUDIT)) return 8;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBREPORT)) return 9;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBDELETE)) return 10;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_DBDROP)) return 11;
		if (value.toUpperCase().equals(Handler.TAG_TYPE_EXECUTEFILE)) return 12;
		else return 0;
	}

}
