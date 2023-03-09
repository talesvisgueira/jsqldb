package br.com.mtinet.core.script;

import java.io.File;

import org.w3c.dom.Node;

import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.dbmig.MigrationHandler;
import br.com.mtinet.core.script.dbreport.ReportHandler;
import br.com.mtinet.core.script.execFile.ExecJavaHandler;
import br.com.mtinet.util.AttributeLoader;
import br.com.mtinet.util.DateUtil;
import br.com.mtinet.util.FileLog;

public abstract class AbstractHandler extends FileLog {
	protected Node tagNode;
	protected PoolConnection poolConn;
//	protected PropertyHandler propertyHandler ;
	protected String schema;
	private String dirPath;
	protected ProgressListener listener;
	
	

	public void setParameters(Node tagNode,PoolConnection poolConn,ProgressListener listener) throws Exception {
		this.tagNode = tagNode;
		this.poolConn = poolConn;
		this.listener = listener;
//		this.propertyHandler = propertyHandler;
		boolean value = false;
		if (this instanceof MigrationHandler) value = false;
		else if (this instanceof ReportHandler) value = false;
		else if (this instanceof ExecJavaHandler) value = false;
		else value = true;
   	    this.schema = AttributeLoader.getAttributeValue(tagNode,"schema",value) ;
	}
	
	public void createDayDir() {
		try {
			DateUtil util = new DateUtil();
			this.dirPath = "audit//"+util.getDataInvertida();
			File f = new File(this.dirPath);
			f.mkdir();
			
			this.filePath = this.dirPath + "//" + new File(this.filePath).getName();
		} catch(Exception e) {}
		
	}
	

}
