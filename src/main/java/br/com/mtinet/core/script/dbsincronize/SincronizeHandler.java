package br.com.mtinet.core.script.dbsincronize;

import org.w3c.dom.Node;

import br.com.mtinet.core.dataBase.PoolConnection;
import br.com.mtinet.core.listener.ProgressListener;
import br.com.mtinet.core.script.AbstractHandler;
import br.com.mtinet.core.script.Handler;

public class SincronizeHandler extends AbstractHandler  implements Handler{
	
	public SincronizeHandler(Node tagNode,PoolConnection poolConn,ProgressListener listener) throws Exception {
		this.setParameters(tagNode,poolConn,listener);
		throw new Exception(" - AVISO: A funcao da tag '" + tagNode.getNodeName().toUpperCase() + "' ainda nao foi implementada...");
	}
	
	public void stop() {
		
	}
	
	
	public void execute() throws Exception  {
		
	}
}
